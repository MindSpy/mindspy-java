package org.mindspy.protobufs;

import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Message;
import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Part of project mindspy-java.
 * User: pborky
 * Date: 2014-09-09
 */
public class DelimitedStreamUtils {

    public static <T extends Message> Iterable<T> createIterable(final DelimitedStream<T> ds)
    {
        return new Iterable<T>()
        {
            boolean fired = false;
            @Override
            public Iterator<T> iterator()
            {
                if (fired)
                    throw new IllegalStateException("Iterator already fired.");

                fired = true;

                return createIterator(ds);
            }
        };
    }

    public static <T extends Message> Iterator<T> createIterator(final DelimitedStream<T> ds)
    {
        return new Iterator<T>()
        {
            T elem;

            @Override
            public boolean hasNext()
            {
                if (elem == null)
                    try
                    {
                        elem = ds.read();
                    }
                    catch (IOException e)
                    {
                        elem = null;
                    }
                return elem != null;
            }

            @Override
            public T next()
            {
                if (!hasNext())
                    throw new NoSuchElementException();

                try
                {
                    return elem;
                }
                finally
                {
                    elem = null;
                }

            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static DelimitedStream<Regs.Response> createDelimitedResponseStream(final InputStream is)
    {
        return new DelimitedStream<Regs.Response>()
        {
            @Override
            public Regs.Response read() throws IOException
            {
                return Regs.Response.parseDelimitedFrom(is);
            }

            @Override
            public void close() throws IOException
            {
                is.close();
            }
        };
    }

    public static Iterable<Regs.Sample> extractSamples(final Iterable<Regs.Response> responses)
    {
        return new Iterable<Regs.Sample>()
        {
            @Override
            public Iterator<Regs.Sample> iterator()
            {
                return new Iterator<Regs.Sample>()
                {
                    Iterator<Regs.Response> i = responses.iterator();
                    Iterator<Regs.Sample> current;

                    @Override
                    public boolean hasNext()
                    {
                        while (current == null || !current.hasNext())
                        {
                            if (i.hasNext()) {
                                current = i.next().getSampleList().iterator();
                            }
                            else
                            {
                                current = null;
                                break;
                            }

                        }
                        return current != null;
                    }

                    @Override
                    public Regs.Sample next()
                    {
                        if (!hasNext())
                            throw new NoSuchElementException();

                        return current.next();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static SimpleMatrix createMatrix(Iterable<Regs.Sample> samples)
    {
        SimpleMatrix result = new SimpleMatrix(1,1);
        for (Regs.Sample sample : samples)
        {
            int sequence = (int) sample.getSequence();
            double [] data = new double[sample.getPayloadCount()];

            for (int i = 0; i < data.length; i++)
                data[i] = sample.getPayload(i);

            SimpleMatrix B = new SimpleMatrix(data.length, 1, true, data);

            int maxRow = B.numRows();
            int maxCol = sequence + B.numCols();

            int numRows = result.numRows();
            int numCols = result.numCols();

            if( maxRow >  numRows || maxCol > numCols ) {
                int M = Math.max(maxRow,numRows);
                int N = Math.max(maxCol,numCols);

                SimpleMatrix old = result;
                result = new SimpleMatrix(M,N);
                result.insertIntoThis(0,0,old);
            }
            result.insertIntoThis(0,sequence,B);
        }
        return result;
    }
}

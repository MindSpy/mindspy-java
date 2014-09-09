package org.mindspy.protobufs;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;


/**
 * Unit test for simple App.
 */
public class ProtobufsTest
    extends TestCase
{
    final static Logger logger = Logger.getLogger("AppTest") ;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ProtobufsTest(String testName)
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ProtobufsTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws IOException {
        InputStream is;

        Regs.Request req;


        is = ProtobufsTest.class.getResourceAsStream("/req_samples.bin");
        try
        {
            req = Regs.Request.parseFrom(is);
        }
        finally
        {
            is.close();
        }
        logger.info(String.valueOf(req));

        is = ProtobufsTest.class.getResourceAsStream("/req_echo.bin");
        try
        {
            req = Regs.Request.parseFrom(is);
        }
        finally
        {
            is.close();
        }
        logger.info(String.valueOf(req));


        is = ProtobufsTest.class.getResourceAsStream("/res_samples.bin");
        try
        {
            DelimitedStream<Regs.Response> ds = DelimitedStreamUtils.createDelimitedResponseStream(is);
            for (Regs.Response res : DelimitedStreamUtils.createIterable(ds))
            {
                logger.info(String.valueOf(res));
            }
        }
        finally
        {
            is.close();
        }


        is = ProtobufsTest.class.getResourceAsStream("/res_samples.bin");
        try
        {
            DelimitedStream<Regs.Response> ds = DelimitedStreamUtils.createDelimitedResponseStream(is);
            Iterable<Regs.Sample> samples = DelimitedStreamUtils.extractSamples(DelimitedStreamUtils.createIterable(ds));
            SimpleMatrix mat = DelimitedStreamUtils.createMatrix(samples);
            logger.info(String.valueOf(mat));
        }
        finally
        {
            is.close();
        }

        assertTrue(true);
    }
}

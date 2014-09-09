package org.mindspy.protobufs;

import com.google.protobuf.Message;

import java.io.Closeable;
import java.io.IOException;

/**
 * Part of project mindspy-java.
 * User: pborky
 * Date: 2014-09-09
 */

interface DelimitedStream<T extends Message> extends Closeable {
    T read() throws IOException;
}

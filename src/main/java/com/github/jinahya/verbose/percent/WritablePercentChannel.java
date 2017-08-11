/*
 * Copyright 2016 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jinahya.verbose.percent;

import java.io.IOException;
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.WritableByteChannel;
import static java.util.Objects.requireNonNull;
import java.util.function.Supplier;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

public class WritablePercentChannel<T extends WritableByteChannel, U extends PercentEncoder>
        implements WritableByteChannel {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    public WritablePercentChannel(final Supplier<T> channelSupplier,
                                  final Supplier<U> encoderSupplier) {
        super();
        this.channelSupplier = requireNonNull(
                channelSupplier, "channelSupplier is null");
        this.encoderSupplier = requireNonNull(
                encoderSupplier, "encoderSupplier is null");
    }

    @Override
    public boolean isOpen() {
        return !closed;
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        if (channel != null) {
            channel.close();
            channel = null;
        }
        closed = true;
    }

    protected T channel() throws ClosedChannelException {
        if (closed) {
            throw new ClosedChannelException();
        }
        if (channel == null && (channel = channelSupplier.get()) == null) {
            throw new RuntimeException("null channel supplied");
        }
        return channel;
    }

    protected U encoder() {
        if (encoder == null && (encoder = encoderSupplier.get()) == null) {
            throw new RuntimeException("null supplied from encoderSupplier");
        }
        return encoder;
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        if (src == null) {
            throw new NullPointerException("src is null");
        }
        if (src.capacity() == 0 || !src.hasRemaining()) {
            return 0;
        }
        if (aux == null) {
            aux = allocate(src.capacity() * 3);
        }
        final int encoded = encoder().encode(src, aux);
        for (aux.flip(); aux.hasRemaining();) {
            channel().write(aux);
        }
        aux.clear();
        return encoded;
    }

    private final Supplier<T> channelSupplier;

    private final Supplier<U> encoderSupplier;

    private boolean closed;

    private T channel;

    private U encoder;

    private ByteBuffer aux;
}

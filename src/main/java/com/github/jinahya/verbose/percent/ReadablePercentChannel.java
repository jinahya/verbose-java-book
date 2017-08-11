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

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;
import static java.util.Objects.requireNonNull;
import java.util.function.Supplier;

public class ReadablePercentChannel<T extends ReadableByteChannel, U extends PercentDecoder>
        implements ReadableByteChannel {

    public ReadablePercentChannel(final Supplier<T> channelSupplier,
                                  final Supplier<U> decoderSupplier) {
        super();
        this.channelSupplier = requireNonNull(
                channelSupplier, "channelSupplier is null");
        this.decoderSupplier = requireNonNull(
                decoderSupplier, "decoderSupplier is null");
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

    protected U decoder() {
        if (decoder == null && (decoder = decoderSupplier.get()) == null) {
            throw new RuntimeException("null decoder supplied");
        }
        return decoder;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        if (dst == null) {
            throw new NullPointerException("dst is null");
        }
        if (dst.capacity() == 0 || !dst.hasRemaining()) {
            return 0;
        }
        if (aux == null) {
            aux = allocate(dst.capacity() * 3);
        }
        assert aux.position() == 0;
        aux.limit(dst.remaining());
        if (channel().read(aux) == -1) {
            aux.clear();
            return -1;
        }
        aux.flip();
        int decoded = decoder().decode(aux, dst);
        if (aux.hasRemaining()) {
            assert aux.remaining() < 3;
            assert dst.hasRemaining();
            aux.compact();
            aux.limit(3);
            while (aux.hasRemaining()) {
                if (channel().read(aux) == -1) {
                    throw new EOFException("unexpected eof");
                }
            }
            aux.flip();
            assert decoder.decode(aux, dst) == 1;
            decoded += 1;
        }
        aux.clear();
        return decoded;
    }

    private final Supplier<T> channelSupplier;

    private final Supplier<U> decoderSupplier;

    private boolean closed;

    private T channel;

    private U decoder;

    private ByteBuffer aux;
}

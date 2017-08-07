/*
 * Copyright 2017 Jin Kwon &lt;onacit at gmail.com&gt;.
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
package com.github.jinahya.verbose.hello;

import static java.lang.String.format;
import static java.lang.invoke.MethodHandles.lookup;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * An extended {@link HelloWorldImpl}.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class ExtendedHelloWorldImpl extends HelloWorldImpl {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    @Override
    public void set(final byte[] array, final int offset) {
        logger.log(Level.FINE, () -> format("array: %s", array));
        logger.log(Level.FINE, () -> format("offset: %d", offset));
        super.set(array, offset);
    }
}

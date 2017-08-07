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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import static java.util.Objects.requireNonNull;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * An invocation handler for {@link HelloWorld}.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class HelloWorldInvocationHandler implements InvocationHandler {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    public HelloWorldInvocationHandler(final HelloWorld helloWorld) { // <1>
        super();
        this.helloWorld = requireNonNull(helloWorld);
    }

    @Override
    public Object invoke(final Object proxy, final Method method,
                         final Object[] args)
            throws Throwable {
        // proxy: 호출된 프록시 객체
        // method: 호출된 메서드
        // args: 호출된 메서드에 제공된 인자들
        if (method.getDeclaringClass().equals(HelloWorld.class)) { // <1>
            logger.log(Level.INFO, () -> format(
                    "invoking: %s with %s", method, Arrays.toString(args)));
        }
        return method.invoke(helloWorld, args); // <2>
    }

    private final HelloWorld helloWorld;
}

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

import static java.lang.invoke.MethodHandles.lookup;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class HelloWorldProxyTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    @Test
    public void test1() {
        final HelloWorld helloWorld = (a, o) -> { // <1>
            // does nothing
        };
        final HelloWorld helloProxy = HelloWorldProxy.newProxy(helloWorld); // <2>
        helloProxy.set(null, 0); // <3>
    }

    @Test
    public void test2() {
        final HelloWorld helloWorld = (a, o) -> {
            // does nothing
        };
        final HelloWorld helloProxy = (HelloWorld) Proxy.newProxyInstance(
                lookup().lookupClass().getClassLoader(), // ClasLoader
                new Class<?>[]{HelloWorld.class}, // Class<?>[]
                (p, m, a) -> { // InvocationHandler
                    if (m.getDeclaringClass().equals(HelloWorld.class)) {
                        logger.info(
                                "invoking {} with {}", m, Arrays.toString(a));
                    }
                    return m.invoke(helloWorld, a);
                }
        );
        helloProxy.set(null, 0);
    }
}

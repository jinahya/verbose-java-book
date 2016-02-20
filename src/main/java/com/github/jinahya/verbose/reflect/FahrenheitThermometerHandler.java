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
package com.github.jinahya.verbose.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class FahrenheitThermometerHandler implements InvocationHandler {

    private static final Method TARGET;

    static {
        try {
            TARGET = FahrenheitThermometer.class.getMethod( // <1>
                    "getTemperatureInFahrenheit");
        } catch (final NoSuchMethodException nsme) {
            throw new InstantiationError(nsme.getMessage());
        }
    }

    public FahrenheitThermometerHandler(final CelsiusThermometer adaptee) {
        super();
        this.adaptee = adaptee;
    }

    @Override
    public Object invoke(final Object proxy, final Method method,
                         final Object[] args)
            throws Throwable {
        // proxy: method 가 호출된 객체
        // method: 호출된 메서드
        // args: 호출된 메서드에 전달된 인자.
        if (method.equals(TARGET)) { // <2>
            return adaptee.getTemperatureInCelsius() * 1.8f + 32;
        }
        return method.invoke(adaptee, args); // <3>
    }

    private final CelsiusThermometer adaptee;
}

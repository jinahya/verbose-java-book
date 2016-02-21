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

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
@Stateless
public class TemperatureSchedules {

    private static Method GET_TEMPERATURE_IN_FAHRENHEIT;

    static {
        try {
            GET_TEMPERATURE_IN_FAHRENHEIT
                    = FahrenheitThermometer.class.getMethod(
                            "getTemperatureInFahrenheit");
        } catch (final NoSuchMethodException nsme) {
            throw new InstantiationError(nsme.getMessage());
        }
    }

    public static FahrenheitThermometer newFahrenheitProxyInstance(
            final CelsiusThermometer celsius) {
        final FahrenheitThermometer proxy
                = (FahrenheitThermometer) Proxy.newProxyInstance(
                        FahrenheitThermometer.class.getClassLoader(),
                        new Class<?>[]{FahrenheitThermometer.class},
                        new FahrenheitThermometerHandler(celsius));
        return proxy;
    }

    @Schedule
    public void schedule1() {
        service.persistTemperature(fahrenheit);
    }

    @Schedule
    public void schedule2() {
        service.persistTemperature(new FahrenheitThermometerAdapter(celsius));
    }

    @Schedule
    public void schedule3() {
        service.persistTemperature(
                () -> celsius.getTemperatureInCelsius() * 1.8f + 32);
    }

    @Schedule
    public void schedule4() {
        service.persistTemperature(newFahrenheitProxyInstance(celsius));
    }

    @Schedule
    public void schedule5() {
        service.persistTemperature(
                (FahrenheitThermometer) Proxy.newProxyInstance(
                        FahrenheitThermometer.class.getClassLoader(),
                        new Class<?>[]{FahrenheitThermometer.class},
                        (p, m, a) -> {
                            if (m.equals(GET_TEMPERATURE_IN_FAHRENHEIT)) {
                                return celsius.getTemperatureInCelsius()
                                       * 1.8f + 32;
                            }
                            return m.invoke(celsius, a);
                        }));
    }

    @Inject
    private FahrenheitThermometer fahrenheit;

    @Inject
    private CelsiusThermometer celsius;

    @EJB
    private TemperatureService service;
}

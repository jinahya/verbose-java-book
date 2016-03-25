/*
 * Copyright 2016 Jin Kwon &lt;onacit_at_gmail.com&gt;.
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
package com.github.jinahya.verbose.ieee754;

import static java.lang.Double.doubleToRawLongBits;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Long.toBinaryString;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

/**
 * Prints IEEE 754 Single Precision.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class IEEE754D {

    static String ieee754d(final long value) {
        final String s = toBinaryString(value >>> 63); // 1
        final String e = format(
                "%11s", toBinaryString((value << 1) >>> 53)); // 11
        final String m = format(
                "%52s", toBinaryString((value << 12) >>> 12)); // 52
        return format("%1$s %2$s %3$s",
                      s, e.replace(' ', '0'), m.replace(' ', '0'));
    }

    private static void ieee754d(final double d, final String n) {
        final long l = doubleToRawLongBits(d);
        System.out.printf("%1s %2$-+13e %3$s%n", ieee754d(l),
                          longBitsToDouble(l), ofNullable(n).orElse(""));
    }

    public static void main(final String... args) {
        if (args.length == 0) {
            ieee754d(.0d, ".0d");
            ieee754d(Double.MAX_VALUE, "Double.MAX_VALUE");
            ieee754d(Double.MIN_VALUE, "Double.MIN_VALUE");
            ieee754d(Double.MIN_NORMAL, "Double.MIN_NORMAL");
            ieee754d(Double.NEGATIVE_INFINITY, "Double.NEGATIVE_INFINITY");
            ieee754d(Double.POSITIVE_INFINITY, "Double.POSITIVE_INFINITY");
            ieee754d(Double.NaN, "Double.NaN");
            return;
        }
        ieee754d(parseFloat(args[0]), null);
    }
}

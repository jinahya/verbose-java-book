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

import static java.lang.Float.floatToRawIntBits;
import static java.lang.Float.intBitsToFloat;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.toBinaryString;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

/**
 * Prints IEEE 754 Single Precision.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class IEEE754S {

    private static String ieee754s(final int value) {
        final String s = toBinaryString(value >>> 31); // 1
        final String e = format(
                "%8s", toBinaryString((value << 1) >>> 24)); // 8
        final String m = format(
                "%23s", toBinaryString((value << 9) >>> 9)); // 23
        return format("%1$s %2$s %3$s",
                      s, e.replace(' ', '0'), m.replace(' ', '0'));
    }

    private static void ieee754s(final float f, final String n) {
        final int i = floatToRawIntBits(f);
        System.out.printf("%1s %2$-13e %3$s%n", ieee754s(i), intBitsToFloat(i),
                          ofNullable(n).orElse(""));
    }

    public static void main(final String... args) {
        if (args.length == 0) {
            ieee754s(-.0f, "-.0f");
            ieee754s(+.0f, "+.0f");
            ieee754s(Float.MAX_VALUE, "Float.MAX_VALUE");
            ieee754s(Float.MIN_VALUE, "Float.MIN_VALUE");
            ieee754s(Float.MIN_NORMAL, "Float.MIN_NORMAL");
            ieee754s(Float.NaN, "Float.NaN");
            ieee754s(Float.NEGATIVE_INFINITY, "Float.NEGATIVE_INFINITY");
            ieee754s(Float.POSITIVE_INFINITY, "Float.POSITIVE_INFINITY");
            return;
        }
        ieee754s(parseFloat(args[0]), null);
    }
}

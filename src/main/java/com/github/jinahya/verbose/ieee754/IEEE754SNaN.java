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

import static java.lang.Float.floatToIntBits;
import static java.lang.Float.floatToRawIntBits;
import static java.lang.Float.intBitsToFloat;

/**
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class IEEE754SNaN {

    public static void main(final String... args) {
        float f = intBitsToFloat(0b11111111_10011011_11100011_01111100); // NaN
        int i1 = floatToIntBits(f);
        int i2 = floatToRawIntBits(f);
        System.out.printf("   floatToIntBits: 0x%08x\n", i1);
        System.out.printf("floatToRawIntBits: 0x%08x\n", i2);
    }
}

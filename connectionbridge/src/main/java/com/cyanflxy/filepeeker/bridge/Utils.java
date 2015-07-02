/*
 * Copyright (C) 2015 CyanFlxy <cyanflxy@163.com>
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

package com.cyanflxy.filepeeker.bridge;

import java.util.Arrays;

/**
 * Created on 2015/7/2.
 */
public class Utils {

    public static void checkArgumentIs(String[] args, int... count) {
        int current = 0;
        if (args != null) {
            current = args.length;
        }

        for (int i : count) {
            if (i == current) {
                return;
            }
        }

        throw new IllegalArgumentException("Argument count error, need:"
                + Arrays.toString(count) + ",current is :" + current);

    }

    public static void checkArgumentNot(String[] args, int... count) {
        int current = 0;
        if (args != null) {
            current = args.length;
        }

        for (int i : count) {
            if (i == current) {
                throw new IllegalArgumentException("Argument count error, cannot resolve count:"
                        + Arrays.toString(count) + ", now is:" + current);
            }
        }
    }
}

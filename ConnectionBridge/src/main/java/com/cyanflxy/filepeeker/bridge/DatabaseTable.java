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

import java.io.Serializable;

/**
 * Created on 2015/7/3.
 */
public class DatabaseTable implements Serializable {
    public static final long serialVersionUID = 1L;

    public String tableName;
    public String[] tableTitle;
    public String[][] tableContent;

    public void print() {
        System.out.println(tableName);

        for (String s : tableTitle) {
            System.out.print(s);
            System.out.print("\t");
        }
        System.out.println();

        for (String[] line : tableContent) {
            for (String s : line) {
                System.out.print(s);
                System.out.print("\t");
            }
            System.out.println();
        }

        System.out.println();
    }
}

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
 * 远程文件处理命令
 * <p/>
 * Created by CyanFlxy on 2015/6/24.
 */
public class Command implements Serializable {
    public static final long serialVersionUID = 1L;

    public final String command;
    public final String currentDir;

    public Command(String cmd, String dir) {
        command = cmd;
        currentDir = dir;
    }

    @Override
    public String toString() {
        return "Command:" + command + " dir:" + currentDir;
    }
}

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

/**
 * 支持的几种命令
 * Created by CyanFlxy on 2015/6/29.
 */
public enum CommandType {
    ls("                    - show all files in current folder."),
    cd(" [dir]              - change to dir or root dir."),
    mkdir(" <dir>           - make a new dir in current folder."),
    create(" <file>         - make a new file in current folder."),
    rm(" <file>             - remove a file."),
    rmdir(" <dir>           - remove a directory"),
    put(" <local> [remote]  - put local file to remote path"),
    get(" <remote> [local]  - get remote file to local path"),
    mv(" <old> <new>        - rename a file or directory"),
    cat(" <filename>        - show file content"),
    cat_sp(" <filename>     - show SharedPreference file content"),
    cat_db(" <filename>     - show Database file content"),
    help("                  - show this help message."),
    exit("                  - exit this program.");

    public final String usage;

    CommandType(String usage) {
        this.usage = name() + usage;
    }

}

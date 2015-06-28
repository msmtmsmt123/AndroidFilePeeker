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

package com.cyanflxy.filepeeker.command;

import com.cyanflxy.filepeeker.bridge.Command;
import com.cyanflxy.filepeeker.bridge.Response;

/**
 * 帮助命令
 * <p/>
 * Created by CyanFlxy on 2015/6/28.
 */
public class HelpCommand implements CommandExecutor {

    public static final String HELP_STRING = "Peek Android Private File:\n"
            + "list                       - show all files in current folder\n"
            + "cd [dir]                   - change to dir or root dir\n"
            + "mkdir <dir>                - make a new dir in current folder\n"
            + "create <file>              - make a new file in current folder\n"
            + "help                       - show this help message\n"
            + "exit                       - exit this program\n";

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public Response execute(Command command) {
        Response response = new Response();
        response.code = Response.CODE_SUCCESS;
        response.data =HELP_STRING;
        return response;
    }
}

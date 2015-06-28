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
 * 无效命令
 * <p/>
 * Created by CyanFlxy on 2015/6/28.
 */
public class UnknownCommand {
    public static Response execute(Command command) {

        Response response = new Response();
        response.code = Response.CODE_UNKNOWN_COMMAND;
        response.message = "Unsupported Command";
        response.data = HelpCommand.HELP_STRING;

        return response;
    }
}

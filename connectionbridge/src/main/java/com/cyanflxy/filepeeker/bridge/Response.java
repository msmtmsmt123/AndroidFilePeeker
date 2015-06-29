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

public class Response implements Serializable {
    public static final long serialVersionUID = 1L;

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_UNKNOWN_COMMAND = 101;
    public static final int CODE_ARG_ERROR = 102;
    public static final int CODE_EXECUTE_ERROR = 103;

    public int code;
    public Object data;
    public CommandType cmdType;

    public static String getResponseCodeMessage(int code) {
        switch (code) {
            case CODE_SUCCESS:
                return "success";
            case CODE_UNKNOWN_COMMAND:
                return "unsupported command";
            case CODE_ARG_ERROR:
                return "args error";
            case CODE_EXECUTE_ERROR:
                return "command execute error";
            default:
                break;
        }
        return "";
    }
}

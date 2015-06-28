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

package com.cyanflxy.filepeeker.socket.client;

import com.cyanflxy.filepeeker.bridge.Response;

/**
 * 远程执行状态与结果
 * <p/>
 * Created by CyanFlxy on 2015/6/28.
 */
public class RemoteEnvironment {
    private String currentDir;

    public RemoteEnvironment() {
        currentDir = "/";
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public void cdCommand(String cmd) {

    }

    public void parseResponse(Response response) {

        System.out.println(response.message);
        System.out.println(response.data);
    }
}

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

import java.net.Socket;

/**
 * PC端通信入口
 * <p/>
 * Created by CyanFlxy on 2015/6/24.
 */
public class PeekFile {
    public static void main(String[] args) {
        AdbConnect connect = new AdbConnect("com.cyanflxy.peekerdemo");
        Socket socket = connect.getConnectionSocket();
        SocketCommunicate communicate = new SocketCommunicate(socket);
        communicate.start();
    }
}
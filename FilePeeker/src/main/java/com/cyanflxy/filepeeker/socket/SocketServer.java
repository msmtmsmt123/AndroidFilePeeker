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

package com.cyanflxy.filepeeker.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class SocketServer implements Runnable {

    private OnSocketAccept onSocketAcceptListener;
    protected ServerSocket mServerSocket;

    public void setOnSocketAccept(OnSocketAccept accept) {
        onSocketAcceptListener = accept;
    }

    protected void onSocketAccept(Socket socket) {
        if (onSocketAcceptListener != null) {
            onSocketAcceptListener.socketAccept(this, socket);
        }
    }

    public void close() {
        if (mServerSocket != null) {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mServerSocket = null;
        }
    }
}

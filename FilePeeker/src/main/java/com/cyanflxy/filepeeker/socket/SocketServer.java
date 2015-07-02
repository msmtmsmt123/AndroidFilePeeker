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

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.cyanflxy.filepeeker.bridge.ConnectionUtils.TAG;

public class SocketServer implements Runnable {

    private OnSocketAccept onSocketAcceptListener;
    private ServerSocket serverSocket;
    private int port;

    public void setPort(int p) {
        port = p;
    }

    public void setOnSocketAccept(OnSocketAccept accept) {
        onSocketAcceptListener = accept;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            Log.w(TAG, "Server Create Socket Exception.", e);
            return;
        }

        Log.i(TAG, "Server Listen Socket @port:" + port);

        while (true) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                Log.i(TAG, "Server accept Socket @port:" + port);

                if (onSocketAcceptListener != null) {
                    onSocketAcceptListener.socketAccept(this, clientSocket);
                }
            } catch (IOException e) {
                Log.w(TAG, "Server Listen Socket Exception", e);
                break;
            }

        }
    }

    public void close() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverSocket = null;
        }
    }

}

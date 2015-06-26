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

import com.cyanflxy.filepeeker.FilePeeker;
import com.cyanflxy.filepeeker.bridge.ConnectionUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.cyanflxy.filepeeker.bridge.ConnectionUtils.TAG;

/**
 * 监听adb连接
 * <p/>
 * Created by CyanFlxy on 2015/6/24.
 */
public class AdbServer extends SocketServer {

    @Override
    public void run() {
        int port = ConnectionUtils.getAdbPort(FilePeeker.packageName);

        try {
            mServerSocket = new ServerSocket(port);
        } catch (IOException e) {
            Log.w(TAG, "AdbServer Create Socket Exception.", e);
            return;
        }

        Log.i(TAG, "AdbServer Listen Socket @port:" + port);

        while (true) {
            Socket clientSocket;
            try {
                clientSocket = mServerSocket.accept();
                Log.i(TAG, "AdbServer accept Socket @port:" + port);
                onSocketAccept(clientSocket);
            } catch (IOException e) {
                Log.w(TAG, "AdbServer Listen Socket Exception", e);
                break;
            }

        }
    }

}

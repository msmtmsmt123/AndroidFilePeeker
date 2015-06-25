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

package com.cyanflxy.filepeeker.socket.adb;

import android.util.Log;

import com.cyanflxy.filepeeker.FilePeeker;
import com.cyanflxy.filepeeker.bridge.ConnectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 监听adb连接
 * <p/>
 * Created by CyanFlxy on 2015/6/24.
 */
public class AdbServer {
    private static AdbServer adbServer;
    private ExecutorService executorService;

    private AdbServer() {
        executorService = Executors.newScheduledThreadPool(3);
    }

    public static void start() {
        if (adbServer == null) {
            adbServer = new AdbServer();
        }
        adbServer.listen();
    }

    public static void stop() {
        if (adbServer != null) {
            adbServer.exit();
            adbServer = null;
        }
    }

    public void listen() {
        AcceptThread thread = new AcceptThread();
        executorService.execute(thread);
    }

    public void exit() {
        executorService.shutdown();
    }

    private class AcceptThread implements Runnable {
        @Override
        public void run() {
            int port = ConnectionUtils.getAdbPort(FilePeeker.packageName);
            ServerSocket mServerSocket;
            try {
                mServerSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            while (true) {
                Socket clientSocket;
                try {
                    Log.i("xyq", "Listen Socket @port:" + port);
                    clientSocket = mServerSocket.accept();
                } catch (IOException e) {
                    Log.e("xyq", "Listen Socket Exception", e);
                    return;
                }

                Receiver receiver = new Receiver(clientSocket);
                executorService.execute(receiver);
            }
        }
    }

    private class Receiver implements Runnable {
        private InputStream inputStream;
        private OutputStream outputStream;

        public Receiver(Socket socket) {
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            while (true) {
                byte[] buff = new byte[128];
                try {
                    int len = inputStream.read(buff);
                    String str = new String(buff, 0, len);
                    Log.i("xyq", "Receive:" + str);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("xyq", "Receive exception", e);
                    break;
                }


                try {
                    Log.i("xyq", "Send Message");
                    outputStream.write("Unsupported Command".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("xyq", "Send exception", e);
                    break;
                }

            }
        }
    }
}

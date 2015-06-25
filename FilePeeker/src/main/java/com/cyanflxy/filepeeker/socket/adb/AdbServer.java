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

import com.cyanflxy.filepeeker.FilePeeker;
import com.cyanflxy.filepeeker.bridge.Command;
import com.cyanflxy.filepeeker.bridge.ConnectionUtils;
import com.cyanflxy.filepeeker.bridge.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
            int port = ConnectionUtils.getAdbConnectPort(FilePeeker.packageName);
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
                    clientSocket = mServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                ReceiveThread mReceiveThread = new ReceiveThread(clientSocket);
                executorService.execute(mReceiveThread);
            }
        }
    }

    private class ReceiveThread extends Thread {
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        ReceiveThread(Socket s) {
            try {
                // 获得输入流
                inputStream = new ObjectInputStream(s.getInputStream());
                outputStream = new ObjectOutputStream(s.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                Command command = null;
                try {
                    command = (Command) inputStream.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

                Response response = new Response();
                response.code = Response.CODE_UNKNOWN_COMMAND;
                response.message = "Unknown Command";

                try {
                    outputStream.writeObject(response);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }

        }
    }
}

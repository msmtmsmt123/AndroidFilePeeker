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

import com.cyanflxy.filepeeker.bridge.Command;
import com.cyanflxy.filepeeker.bridge.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 利用socket进行通信
 * <p/>
 * Created by CyanFlxy on 2015/6/24.
 */
public class SocketCommunicate {
    private Socket mSocket;
    private ObjectInputStream mInputStream;
    private ObjectOutputStream mOutputStream;

    private RemoteEnvironment env;

    public SocketCommunicate(Socket socket) {
        env = new RemoteEnvironment();
        mSocket = socket;
        try {
            mOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
            mInputStream = new ObjectInputStream(mSocket.getInputStream());
        } catch (IOException e) {
            System.err.println("Open Socket Stream Error.");
            e.printStackTrace();

            System.exit(1);
        }
    }

    public void start() {
        while (true) {
            System.out.print(env.getCurrentDir() + ">");
            String cmd = readCommandLineString();
            if (cmd == null || cmd.equals("")) {
                continue;
            }

            if ("exit".equals(cmd)) {
                break;
            } else if (cmd.startsWith("cd")) {
                env.cdCommand(cmd);
                continue;
            }

            executeCommand(cmd);
        }

        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readCommandLineString() {
        try {
            byte[] buff = new byte[128];
            int len = System.in.read(buff);
            return new String(buff, 0, len - 1);//过滤掉最后的回车号
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void executeCommand(String cmd) {
        Command command = new Command(cmd, env.getCurrentDir());
        try {
            mOutputStream.writeObject(command);
        } catch (IOException e) {
            System.err.println("write to socket error!");
            e.printStackTrace();
            return;
        }

        try {
            Response response = (Response) mInputStream.readObject();
            env.parseResponse(response);
        } catch (Exception e) {
            System.err.println("read from socket error!");
            e.printStackTrace();
        }
    }

}

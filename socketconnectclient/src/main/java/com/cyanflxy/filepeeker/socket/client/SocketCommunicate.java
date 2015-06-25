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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 利用socket进行通信
 * <p/>
 * Created by CyanFlxy on 2015/6/24.
 */
public class SocketCommunicate {
    private Socket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;

    private String mCurrentDir;

    public SocketCommunicate(Socket socket) {
        mCurrentDir = "/";
        mSocket = socket;
        try {
            mInputStream = (mSocket.getInputStream());
//            mOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
            mOutputStream = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();

            System.err.println("Open Socket Stream Error.");
            System.exit(1);
        }
    }

    public void start() {
        while (true) {
            System.out.print(">");
            String cmd = readCommandLineString();

            if ("exit".equals(cmd)) {
                break;
            } else if ("help".equals(cmd)) {
                printUsage();
            } else {
                executeCommand(cmd);
            }
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
        Command command = new Command(cmd, mCurrentDir);
        try {
            mOutputStream.write(command.command.getBytes());
        } catch (IOException e) {
            e.printStackTrace();

            System.err.println("write to socket error!");
            return;
        }

        try {
            byte[] bytes = new byte[128];
            int len = mInputStream.read(bytes);
            System.out.println(new String(bytes, 0, len));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("read from socket error!");
        }
    }

    public void printUsage() {
        System.out.print(
                "Peek Android Private File:\n"
                        + "list                       - show all files in current folder\n"
                        + "cd [dir]                   - change to dir or root dir\n"
                        + "mkdir <dir>                - make a new dir in current folder\n"
                        + "create <file>              - make a new file in current folder\n"
                        + "exit                       - exit this program\n"
                        + "help                       - show this help message\n");
    }
}

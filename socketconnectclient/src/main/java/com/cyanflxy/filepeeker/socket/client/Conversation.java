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

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 利用socket进行通信
 * <p/>
 * Created by CyanFlxy on 2015/6/24.
 */
public class Conversation {
    private Socket mSocket;
    private ObjectInputStream mInputStream;
    private ObjectOutputStream mOutputStream;

    private ResponseParser responseParser;
    private RemoteEnvironment remoteEnvironment;
    private CommandCreator commandCreator;

    public Conversation(Socket socket) {
        responseParser = new ResponseParser();
        remoteEnvironment = new RemoteEnvironment();
        commandCreator = new CommandCreator();

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
            System.out.print(remoteEnvironment.getCurrentDirectory() + ">");
            String cmd = readCommandLineString();
            if (cmd == null || cmd.equals("")) {
                continue;
            }

            if ("exit".equals(cmd)) {
                break;
            }

            try {
                Command command = commandCreator.createCommand(cmd, remoteEnvironment);
                executeCommand(command);
            } catch (Exception e) {
                System.out.println(e.getMessage());
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
            byte[] buff = new byte[32];
            StringBuilder sb = new StringBuilder();

            do {
                int len = System.in.read(buff);
                if (len < 0) {
                    System.out.println("read console Error!");
                    System.exit(0);//读取异常结束了
                }

                sb.append(new String(buff, 0, len));

            } while (System.in.available() > 0);

            return sb.substring(0, sb.length() - 1).trim();//去掉最后的回车键
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void executeCommand(Command command) {
        try {
            mOutputStream.writeObject(command);
        } catch (IOException e) {
            System.err.println("write to socket error!");
            e.printStackTrace();
            return;
        }

        try {
            Response response = (Response) mInputStream.readObject();
            responseParser.parseResponse(response, remoteEnvironment);
        } catch (EOFException e) {
            System.err.println("Remote Socket is Closed!");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("read from socket error!");
            e.printStackTrace();
        }
    }

}

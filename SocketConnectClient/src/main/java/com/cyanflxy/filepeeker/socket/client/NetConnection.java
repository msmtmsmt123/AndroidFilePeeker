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

import com.cyanflxy.filepeeker.bridge.ConnectionUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 网络连接socket方式
 * Created by CyanFlxy on 2015/6/30.
 */
public class NetConnection {
    private final String ip;
    private final String pkgName;

    public NetConnection(String ip, String pkgName) {
        this.ip = ip;
        this.pkgName = pkgName;
    }

    public Socket connect() {

        InetAddress inetAddress = null;

        try {
            inetAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();

            System.err.println("cannot resolved ip:" + ip);
            System.exit(1);
        }

        int port = ConnectionUtils.getAdbPort(pkgName);
        try {
            System.out.println("Connect to " + pkgName + " @IP:" + ip + " @RemotePort:" + port);
            return new Socket(inetAddress, port);
        } catch (IOException e) {
            e.printStackTrace();

            System.err.println("Create Socket Error!");
            System.exit(1);
            return null;
        }
    }
}

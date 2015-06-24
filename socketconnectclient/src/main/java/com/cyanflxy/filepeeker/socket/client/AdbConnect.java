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
 * adb连接pc端命令行工具
 * <p/>
 * Created by CyanFlxy on 2015/6/24.
 */
public class AdbConnect {

    private final String mPkgName;
    private final int mPort;

    public AdbConnect(String pkgName) {
        mPkgName = pkgName;
        mPort = ConnectionUtils.getAdbConnectPort(mPkgName);
    }

    public Socket getConnectionSocket() {
        try {
            System.out.println("Connect to " + mPkgName + " @port:" + mPort);
            Runtime.getRuntime().exec(String.format("adb forward tcp:%d tcp:%d", mPort, mPort));
        } catch (IOException e) {
            e.printStackTrace();

            System.err.println("ADB Command Execute Error! Please Check ADB Server and Device.");
            System.exit(1);
        }

        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();

            System.err.println("localhost IP <127.0.0.1> cannot be resolved!");
            System.exit(1);
        }

        try {
            return new Socket(inetAddress, mPort);
        } catch (IOException e) {
            e.printStackTrace();

            System.err.println("Create Socket Error!");
            System.exit(1);
            return null;
        }

    }

}

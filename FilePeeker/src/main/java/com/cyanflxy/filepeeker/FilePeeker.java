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

package com.cyanflxy.filepeeker;

import android.content.Context;

import com.cyanflxy.filepeeker.bridge.ConnectionUtils;
import com.cyanflxy.filepeeker.socket.OnSocketAccept;
import com.cyanflxy.filepeeker.socket.SocketCommunicate;
import com.cyanflxy.filepeeker.socket.SocketServer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 入口
 * Created by CyanFlxy on 2015/6/1.
 */
public class FilePeeker {
    public static String localFileDir;
    public static String packageName;

    private ExecutorService executorService;
    private List<SocketServer> serverList;

    public void start(Context c) {
        if (executorService != null) {
            return;
        }

        FileUtils.init(c);

        localFileDir = c.getFilesDir().getParent();
        packageName = c.getPackageName();

        SocketServer adbServer = new SocketServer();
        adbServer.setPort(ConnectionUtils.getAdbPort(packageName));
        adbServer.setOnSocketAccept(onSocketAccept);

        SocketServer netServer = new SocketServer();
        netServer.setPort(ConnectionUtils.getNetPort(packageName));
        netServer.setOnSocketAccept(onSocketAccept);

        executorService = Executors.newCachedThreadPool();
        executorService.execute(adbServer);
        executorService.execute(netServer);

        serverList = new ArrayList<>(3);
        serverList.add(adbServer);
    }

    public void destroy() {
        if (executorService == null) {
            return;
        }

        for (SocketServer s : serverList) {
            s.close();
        }
        serverList.clear();

        executorService.shutdown();
        executorService = null;
    }

    private OnSocketAccept onSocketAccept = new OnSocketAccept() {
        @Override
        public void socketAccept(SocketServer server, Socket socket) {
            Runnable receiver = new SocketCommunicate(socket);
            executorService.execute(receiver);
        }
    };

}

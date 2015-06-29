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

import com.cyanflxy.filepeeker.socket.AdbServer;
import com.cyanflxy.filepeeker.socket.OnSocketAccept;
import com.cyanflxy.filepeeker.socket.SocketCommunicate;
import com.cyanflxy.filepeeker.socket.SocketServer;

import java.io.File;
import java.io.IOException;
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

        executorService = Executors.newScheduledThreadPool(3);
        SocketServer adbServer = new AdbServer();
        adbServer.setOnSocketAccept(onSocketAccept);
        executorService.execute(adbServer);

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

    // 以下的内容属于过期代码

    /**
     * 列出dir目录下的所有文件
     *
     * @param dir 相对(应用私有目录的)目录
     * @return dir下的所有文件(目录)
     */
    @Deprecated
    public static File[] listFiles(String dir) {
        File file = new File(localFileDir, dir);
        return file.listFiles();
    }

    @Deprecated
    public static boolean createFile(String dir, String fileName) {
        File file = new File(localFileDir, dir);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return false;
            }
        }

        file = new File(file, fileName);
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Deprecated
    public static boolean createFolder(String dir, String folderName) {
        File file = new File(localFileDir, dir);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return false;
            }
        }

        file = new File(file, folderName);
        return file.mkdir();
    }
}

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

import java.io.File;
import java.io.IOException;

/**
 * 入口
 * Created by CyanFlxy on 2015/6/1.
 */
public class FilePeeker {
    public static String localFileDir;
    public static String packageName;

    public static void init(Context c) {
        localFileDir = c.getFilesDir().getParent();
        packageName = c.getPackageName();
        // c.getDir(name,mode);//这种方法获取的文件夹路径会导致名称前添加app_前缀

        AdbServer.start();
    }

    /**
     * 列出dir目录下的所有文件
     *
     * @param dir 相对(应用私有目录的)目录
     * @return dir下的所有文件(目录)
     */
    public static File[] listFiles(String dir) {
        File file = new File(localFileDir, dir);
        return file.listFiles();
    }

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

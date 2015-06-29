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

import com.cyanflxy.filepeeker.bridge.RemoteFile;

import java.io.File;

/**
 * 文件处理工具
 * <p/>
 * Created by CyanFlxy on 2015/6/29.
 */
public class FileUtils {
    public static String localFileDir;


    public static void init(Context c) {
        localFileDir = c.getFilesDir().getParent();
    }

    public static String changeDirectory(String current, String path) {
        if (path == null || path.trim().equals("")) {
            return "/";
        }

        path = path.trim();

        if (path.startsWith("/")) {
            return path;
        }

        String[] dirs = path.split("/");
        for (String s : dirs) {
            current = changeSingleDirectory(current, s.trim());
        }

        return current;
    }

    private static String changeSingleDirectory(String current, String path) {
        if (path == null || "".equals(path) || ".".equals(path)) {
            return current;
        }

        if ("..".equals(path)) {
            return parentDirectory(current);
        }

        if (!current.endsWith("/")) {
            current += "/";
        }
        current += path;

        return current;
    }

    public static String parentDirectory(String current) {
        if ("/".equals(current)) {
            return "/";
        }

        int index = current.lastIndexOf("/");
        if (index == 0) {
            return "/";
        }

        return current.substring(0, index - 1);
    }

    public static boolean validDir(String dir) {
        File file = new File(localFileDir, dir);
        return file.isDirectory();
    }

    public static RemoteFile[] listFile(String dir) {
        File file = new File(localFileDir, dir);
        File[] files = file.listFiles();
        RemoteFile[] remoteFiles = new RemoteFile[files.length];

        for (int i = 0; i < files.length; i++) {
            remoteFiles[i] = new RemoteFile(files[i]);
        }

        return remoteFiles;
    }

}

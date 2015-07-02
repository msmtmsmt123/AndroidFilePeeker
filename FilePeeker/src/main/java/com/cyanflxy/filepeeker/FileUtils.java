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

import java.io.File;
import java.io.IOException;

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

        return current.substring(0, index);
    }

    public static boolean validDir(String dir) {
        File file = new File(localFileDir, dir);
        return file.isDirectory();
    }

    public static File[] listFile(String dir) {
        File file = new File(localFileDir, dir);
        return file.listFiles();
    }

    public static File getFile(String currentDir, String name) throws IOException {
        File dirFile = new File(localFileDir, currentDir);
        if (!dirFile.exists()) {
            throw new IOException("Current directory is not exist, Name:" + name);
        }

        return new File(dirFile, name);
    }

    public static void create(String currentDir, String name) throws IOException {
        File file = getFile(currentDir, name);
        if (file.exists()) {
            throw new IOException("Target file name is exist, Name:" + name);
        }

        if (!file.createNewFile()) {
            throw new IOException("File create failed for already exist, Name:" + name);
        }
    }

    public static void mkdir(String currentDir, String name) throws IOException {
        File file = getFile(currentDir, name);
        if (file.exists()) {
            throw new IOException("Target file Name is Exist, Name:" + name);
        }

        if (!file.mkdirs()) {
            throw new IOException("Directory create failed for already exist, Name:" + name);
        }
    }

    public static void rm(String currentDir, String name) throws IOException {

        File file = getFile(currentDir, name);
        if (!file.exists()) {
            throw new IOException("Target file is not Exist, Name:" + name);
        }

        if (file.isDirectory()) {
            throw new IOException("Target is directory please use 'rmdir', Name:" + name);
        }

        if (!file.delete()) {
            throw new IOException("File delete failed, Name: " + name);
        }
    }

    public static void rmdir(String currentDir, String name) throws IOException {

        File file = getFile(currentDir, name);
        if (!file.exists()) {
            throw new IOException("Target file is not Exist, Name:" + name);
        }

        if (file.isFile()) {
            throw new IOException("Target is file please use 'rm', Name:" + name);
        }

        remove(file);
    }

    private static void remove(File file) throws IOException {
        if (file.isFile()) {
            if (!file.delete()) {
                throw new IOException("delete File failed, Name: " + relativeName(file));
            }
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                remove(f);
            }

            if (!file.delete()) {
                throw new IOException("delete directory failed, Name: " + relativeName(file));
            }
        }
    }

    public static String relativeName(File file) {
        return file.getAbsolutePath().substring(localFileDir.length());
    }

    public static void put(String currentDir, String name, byte[] data) throws IOException {
        File file = getFile(currentDir, name);
    }
}

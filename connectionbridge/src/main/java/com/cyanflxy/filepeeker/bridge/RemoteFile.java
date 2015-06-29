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

package com.cyanflxy.filepeeker.bridge;

import java.io.File;
import java.io.Serializable;

/**
 * 远程文件属性数据
 * Created by CyanFlxy on 2015/6/29.
 */
public class RemoteFile implements Serializable {
    public static final long serialVersionUID = 1L;

    private final String fileName;
    private final boolean isDirectory;

    public RemoteFile(File file) {
        fileName = file.getName();
        isDirectory = file.isDirectory();
    }

    private RemoteFile() {
        fileName = "test";
        isDirectory = false;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isFile() {
        return !isDirectory;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    @Override
    public String toString() {
        String type = isDirectory ? "<DIR>" : "<FILE>";
        return String.format("%-10s %5s",
                fileName, type);
    }

    public static void main(String[] args) {
        RemoteFile file = new RemoteFile();
        System.out.println(file);
    }
}

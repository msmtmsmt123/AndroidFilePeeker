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

    private final String relativePath;
    private final String fileName;
    private final boolean isDirectory;
    private final long lastModified;
    private int subFileNumber;

    public RemoteFile(File file, String path) {
        fileName = file.getName();
        isDirectory = file.isDirectory();
        relativePath = path;

        if (isDirectory) {
            String[] subFiles = file.list();
            if (subFiles != null) {
                subFileNumber = subFiles.length;
            }
        }

        lastModified = file.lastModified();
    }

    public String getPath() {
        return relativePath;
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

    public int getSubFileNumber() {
        return subFileNumber;
    }

    public long getLastModified() {
        return lastModified;
    }

    @Override
    public String toString() {
        String type = isDirectory ? "<DIR>" : "<FILE>";
        return String.format("%-10s %5s",
                fileName, type);
    }

}

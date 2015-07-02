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
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 远程设备上的文件数据
 * Created by CyanFlxy on 2015/6/30.
 */
public class RemoteFileData {

    public int length;
    public String name;
    public byte[] data;

    public RemoteFileData(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.length() > Integer.MAX_VALUE) {
            throw new IOException("File too large(larger than 2G).");
        }

        length = (int) file.length();
        name = file.getName();

        FileInputStream is = null;

        try {
            data = new byte[length];

            is = new FileInputStream(file);
            int readLen = is.read(data, 0, length);

            if (readLen != length) {
                throw new IOException("File read error cannot read all len=" + length + ", read:" + readLen);
            }

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}

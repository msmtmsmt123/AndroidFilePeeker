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

/**
 * Created by CyanFlxy on 2015/6/24.
 */
public class ConnectionUtils {
    /**
     * 获取包名对应的socket端口号
     *
     * @param pkgName 正在准备进行连接的包名
     * @return 连接端口号
     */
    public static int getAdbPort(String pkgName) {
        // 找了个靠近65536的质数，并添加公用端口号偏移
        return pkgName.hashCode() % 64451 + 1048;
    }

//    public static int getNetConnectPort(String pkgName) {
//        return getAdbPortForPhone(pkgName) + 7;
//    }


}

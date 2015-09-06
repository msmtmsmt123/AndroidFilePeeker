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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * 文件浏览服务
 * <p/>
 * Created by CyanFlxy on 2015/9/6.
 */
public class FilePeekerService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return stub.asBinder();
    }

    IFilePeeker.Stub stub = new IFilePeeker.Stub() {
        @Override
        public String getString(int arg) throws RemoteException {
            return "Hello World " + arg;
        }

    };
}

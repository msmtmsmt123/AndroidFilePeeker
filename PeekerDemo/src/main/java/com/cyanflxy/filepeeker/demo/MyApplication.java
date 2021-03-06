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

package com.cyanflxy.filepeeker.demo;

import android.app.Application;

import com.cyanflxy.filepeeker.FilePeeker;
import com.facebook.stetho.Stetho;

/**
 * 初始化FilePeeker
 * <p/>
 * Created by CyanFlxy on 2015/6/3.
 */
public class MyApplication extends Application {

    private FilePeeker filePeeker = new FilePeeker();

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
        filePeeker.start(this);
    }

    @Override
    public void onTerminate() {
        filePeeker.destroy();
        super.onTerminate();
    }
}

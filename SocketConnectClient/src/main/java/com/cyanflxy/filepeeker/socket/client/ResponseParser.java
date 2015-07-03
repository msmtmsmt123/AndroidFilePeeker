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

package com.cyanflxy.filepeeker.socket.client;

import com.cyanflxy.filepeeker.bridge.RemoteFile;
import com.cyanflxy.filepeeker.bridge.RemoteFileData;
import com.cyanflxy.filepeeker.bridge.Response;
import com.cyanflxy.filepeeker.bridge.SharedPrefData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 解析并处理结果
 * <p/>
 * Created by CyanFlxy on 2015/7/2.
 */
public class ResponseParser {

    public void parseResponse(Response response, RemoteEnvironment environment) {

        if (response.code != Response.CODE_SUCCESS) {
            String msg = Response.getResponseCodeMessage(response.code);
            System.out.println(msg);

            if (response.data != null) {
                System.out.println(response.data);
            }
        } else {

            try {
                Method executor = ResponseParser.class.getDeclaredMethod(
                        response.commandType.name(), Response.class, RemoteEnvironment.class);
                executor.invoke(this, response, environment);
            } catch (NoSuchMethodException e) {
                System.out.println(Response.getResponseCodeMessage(response.code));
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("response parse error.");
            }
        }
    }

    @SuppressWarnings("unused")//ReflectInvoke
    private void ls(Response response, RemoteEnvironment environment) {
        RemoteFile[] files = (RemoteFile[]) response.data;
        for (RemoteFile f : files) {
            System.out.println(f);
        }
    }

    @SuppressWarnings("unused")//ReflectInvoke
    private void cd(Response response, RemoteEnvironment environment) {
        environment.setCurrentDirectory((String) response.data);
    }

    @SuppressWarnings("unused")//ReflectInvoke
    private void get(Response response, RemoteEnvironment environment) {
        RemoteFileData data = (RemoteFileData) response.data;

        File file = new File(data.name);
        if (file.exists()) {
            System.err.println("Target file is exist:" + data.name);
            return;
        }
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            os.write(data.data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    @SuppressWarnings("unused")//ReflectInvoke
    private void cat(Response response, RemoteEnvironment environment) {
        String content = (String) response.data;
        System.out.println(content);
    }

    @SuppressWarnings("unused")//ReflectInvoke
    private void cat_sp(Response response, RemoteEnvironment environment) {
        System.out.println("TYPE\tKEY\tVALUE");
        SharedPrefData[] data = (SharedPrefData[]) response.data;
        for (SharedPrefData d : data) {
            System.out.println(String.format("%8s\t%s\t%s", d.className, d.key, d.value));
        }
    }

    @SuppressWarnings("unused")//ReflectInvoke
    private void cat_db(Response response, RemoteEnvironment environment) {
        String[][] data = (String[][]) response.data;
        for (String[] line : data) {
            for (String s : line) {
                System.out.print(s);
                System.out.print("\t");
            }
            System.out.println();
        }
    }

    @SuppressWarnings("unused")//ReflectInvoke
    private void help(Response response, RemoteEnvironment environment) {
        System.out.println(response.data);
    }

    @SuppressWarnings("unused")//ReflectInvoke
    private void exit(Response response, RemoteEnvironment environment) {
        System.exit(0);
    }

}

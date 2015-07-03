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

import com.cyanflxy.filepeeker.bridge.Command;
import com.cyanflxy.filepeeker.bridge.CommandType;
import com.cyanflxy.filepeeker.bridge.RemoteFileData;
import com.cyanflxy.filepeeker.bridge.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 命令组装器
 * <p/>
 * Created by CyanFlxy on 2015/7/2.
 */
public class CommandCreator {

    public Command createCommand(String cmd, RemoteEnvironment environment) throws IOException {
        String[] args = cmd.split(" ");

        CommandType type;
        try {
            type = CommandType.valueOf(args[0]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown Command! use 'help' check usage.");
        }

        Command command = new Command();
        command.commandType = type;
        command.currentDir = environment.getCurrentDirectory();

        if (args.length > 1) {
            int len = args.length - 1;
            command.args = new String[len];
            System.arraycopy(args, 1, command.args, 0, len);
        }

        command.data = getCommandData(type, command.args);

        return command;

    }

    private Object getCommandData(CommandType type, String[] args) throws IOException {

        try {
            Method executor = CommandCreator.class.getDeclaredMethod(
                    type.name(), String[].class);
            return executor.invoke(this, new Object[]{args});
        } catch (NoSuchMethodException e) {
            // ignore
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("unused")//ReflectInvoke
    private Object put(String[] args) throws IOException {
        Utils.checkArgumentIs(args, 1, 2);

        return new RemoteFileData(args[0]);
    }


}

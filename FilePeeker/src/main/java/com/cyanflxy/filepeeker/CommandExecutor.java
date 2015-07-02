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

import com.cyanflxy.filepeeker.bridge.Command;
import com.cyanflxy.filepeeker.bridge.CommandType;
import com.cyanflxy.filepeeker.bridge.RemoteFile;
import com.cyanflxy.filepeeker.bridge.RemoteFileData;
import com.cyanflxy.filepeeker.bridge.Response;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 第二层，执行命令，返回结果
 * <p/>
 * Created by CyanFlxy on 2015/6/28.
 */
@SuppressWarnings("unused")
public class CommandExecutor {

    public Response executeCommand(Command command) {
        Response response;

        try {
            Method executor = CommandExecutor.class.getDeclaredMethod(
                    command.commandType.name(), Command.class);
            response = (Response) executor.invoke(this, command);
            response.code = Response.CODE_SUCCESS;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();

            response = unknownCommand();
        } catch (Exception e) {
            e.printStackTrace();

            Throwable t = e.getCause();
            if (t == null) {
                t = e;
            }

            if (t instanceof IllegalArgumentException) {
                response = new Response();
                response.code = Response.CODE_ARG_ERROR;
                response.data = t.getMessage() + "\n" + command.commandType.usage;
            } else {
                response = new Response();
                response.code = Response.CODE_EXECUTE_ERROR;
                response.data = t.getMessage();
            }

        }

        response.commandType = command.commandType;
        return response;
    }

    private Response cd(Command command) {
        checkArgumentIs(command.args, 0, 1);

        Response response = new Response();

        if (command.args == null || command.args.length == 0) {
            response.data = "/";
        } else {
            String dir = FileUtils.changeDirectory(command.currentDir, command.args[0]);
            if (FileUtils.validDir(dir)) {
                response.data = dir;
            } else {
                throw new IllegalArgumentException("directory not exits.");
            }
        }

        return response;
    }

    private Response ls(Command command) {
        checkArgumentIs(command.args, 0);

        File[] files = FileUtils.listFile(command.currentDir);
        RemoteFile[] remoteFiles = new RemoteFile[files.length];

        for (int i = 0; i < files.length; i++) {
            remoteFiles[i] = new RemoteFile(files[i], FileUtils.relativeName(files[i]));
        }

        Response response = new Response();
        response.data = remoteFiles;
        return response;
    }

    private Response create(Command command) throws IOException {
        checkArgumentIs(command.args, 1);

        FileUtils.create(command.currentDir, command.args[0]);
        return new Response();
    }

    private Response mkdir(Command command) throws IOException {
        checkArgumentIs(command.args, 1);

        FileUtils.mkdir(command.currentDir, command.args[0]);
        return new Response();
    }

    private Response rm(Command command) throws IOException {
        checkArgumentNot(command.args, 0);

        FileUtils.rm(command.currentDir, command.args[0]);
        return new Response();
    }

    private Response rmdir(Command command) throws IOException {
        checkArgumentNot(command.args, 0);

        FileUtils.rmdir(command.currentDir, command.args[0]);
        return new Response();
    }

    private Response put(Command command) throws IOException {
        checkArgumentIs(command.args, 1, 2);

        RemoteFileData remoteFileData = (RemoteFileData) command.data;

        if (remoteFileData.length != remoteFileData.data.length) {
            throw new IOException("File data length error, expect:" +
                    remoteFileData.length + ", current:" + remoteFileData.data.length);
        }

        String fileName;

        if (command.args.length == 2) {
            fileName = command.args[1];
        } else {
            fileName = remoteFileData.name;
        }

        FileUtils.put(command.currentDir, fileName, remoteFileData.data);

        return new Response();
    }

    private Response help(Command command) {
        checkArgumentIs(command.args, 0);

        Response response = new Response();
        response.data = getHelpString();
        return response;
    }

    private Response unknownCommand() {
        Response response = new Response();
        response.code = Response.CODE_UNKNOWN_COMMAND;
        response.data = getHelpString();
        return response;
    }

    private String getHelpString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Peek Android Private File:\n");

        for (CommandType cmd : CommandType.values()) {
            sb.append(cmd.usage).append("\n");
        }

        return sb.toString();
    }

    private Response exit(Command command) {
        checkArgumentIs(command.args, 0);

        return new Response();
    }

    private void checkArgumentIs(String[] args, int... count) {
        int current = 0;
        if (args != null) {
            current = args.length;
        }

        for (int i : count) {
            if (i == current) {
                return;
            }
        }

        throw new IllegalArgumentException("Argument count error, need:"
                + Arrays.toString(count) + ",current is :" + current);

    }

    private void checkArgumentNot(String[] args, int... count) {
        int current = 0;
        if (args != null) {
            current = args.length;
        }

        for (int i : count) {
            if (i == current) {
                throw new IllegalArgumentException("Argument count error, cannot resolve count:"
                        + Arrays.toString(count) + ", now is:" + current);
            }
        }
    }


}

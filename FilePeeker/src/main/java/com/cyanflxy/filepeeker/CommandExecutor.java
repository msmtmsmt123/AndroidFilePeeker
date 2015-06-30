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
import com.cyanflxy.filepeeker.bridge.Response;

import java.io.IOException;

/**
 * 第二层，执行命令，返回结果
 * <p/>
 * Created by CyanFlxy on 2015/6/28.
 */
public class CommandExecutor {

    public CommandExecutor() {

    }

    public Response executeCommand(Command command) {
        String[] args = command.command.trim().split(" ");
        try {
            CommandType type = CommandType.valueOf(args[0]);
            Response response = executeCommand(type, args, command.currentDir);
            response.cmdType = type;
            return response;
        } catch (IllegalArgumentException e) {
            return unknownCommand();
        }
    }

    private Response executeCommand(CommandType type, String[] args, String currentDir) {
        switch (type) {
            case cd:
                return cd(args, currentDir);
            case ls:
                return ls(currentDir);
            case create:
                return create(args, currentDir);
            case mkdir:
                return mkdir(args, currentDir);
            case rm:
                return rm(args, currentDir);
            case rmdir:
                return rmdir(args, currentDir);
            case help:
                return help();
            case exit:
                return exit();
        }

        return unknownCommand();
    }

    private Response cd(String[] args, String currentDir) {
        Response response = new Response();
        if (args.length == 1) {
            response.code = Response.CODE_SUCCESS;
            response.data = "/";
        } else if (args.length > 2) {
            response.code = Response.CODE_ARG_ERROR;
            response.data = CommandType.cd.usage;
        } else {
            String dir = FileUtils.changeDirectory(currentDir, args[1]);
            if (FileUtils.validDir(dir)) {
                response.code = Response.CODE_SUCCESS;
                response.data = dir;
            } else {
                response.code = Response.CODE_EXECUTE_ERROR;
                response.data = "directory not exits.";
            }
        }

        return response;
    }

    private Response ls(String currentDir) {
        Response response = new Response();
        response.code = Response.CODE_SUCCESS;
        response.data = FileUtils.listFile(currentDir);
        return response;
    }

    private Response create(String[] args, String currentDir) {
        Response response = new Response();
        if (args.length != 2) {
            response.code = Response.CODE_ARG_ERROR;
            response.data = CommandType.create.usage;
        } else {
            try {
                FileUtils.create(currentDir, args[1]);
                response.code = Response.CODE_SUCCESS;
            } catch (IOException e) {
                response.code = Response.CODE_EXECUTE_ERROR;
                response.data = e.getMessage();
            }
        }

        return response;
    }

    private Response mkdir(String[] args, String currentDir) {
        Response response = new Response();
        if (args.length != 2) {
            response.code = Response.CODE_ARG_ERROR;
            response.data = CommandType.mkdir.usage;
        } else {
            try {
                FileUtils.mkdir(currentDir, args[1]);
                response.code = Response.CODE_SUCCESS;
            } catch (IOException e) {
                response.code = Response.CODE_EXECUTE_ERROR;
                response.data = e.getMessage();
            }
        }
        return response;
    }

    private Response rm(String[] args, String currentDir) {
        Response response = new Response();
        if (args.length != 2) {
            response.code = Response.CODE_ARG_ERROR;
            response.data = CommandType.mkdir.usage;
        } else {
            try {
                FileUtils.rm(currentDir, args[1]);
                response.code = Response.CODE_SUCCESS;
            } catch (IOException e) {
                response.code = Response.CODE_EXECUTE_ERROR;
                response.data = e.getMessage();
            }
        }
        return response;
    }

    private Response rmdir(String[] args, String currentDir) {
        Response response = new Response();
        if (args.length != 2) {
            response.code = Response.CODE_ARG_ERROR;
            response.data = CommandType.mkdir.usage;
        } else {
            try {
                FileUtils.rmdir(currentDir, args[1]);
                response.code = Response.CODE_SUCCESS;
            } catch (IOException e) {
                response.code = Response.CODE_EXECUTE_ERROR;
                response.data = e.getMessage();
            }
        }
        return response;
    }

    private Response help() {
        Response response = new Response();
        response.code = Response.CODE_SUCCESS;
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

    private Response exit() {
        Response response = new Response();
        response.code = Response.CODE_SUCCESS;
        return response;
    }
}

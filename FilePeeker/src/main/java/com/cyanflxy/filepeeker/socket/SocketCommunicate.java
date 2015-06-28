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

package com.cyanflxy.filepeeker.socket;

import android.util.Log;

import com.cyanflxy.filepeeker.bridge.Command;
import com.cyanflxy.filepeeker.bridge.ConnectionUtils;
import com.cyanflxy.filepeeker.bridge.Response;
import com.cyanflxy.filepeeker.command.CommandManager;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static com.cyanflxy.filepeeker.bridge.ConnectionUtils.TAG;

public class SocketCommunicate implements Runnable {

    private Socket mSocket;

    public SocketCommunicate(Socket socket) {

        mSocket = socket;
    }

    @Override
    public void run() {
        ObjectInputStream inputStream;
        ObjectOutputStream outputStream;

        try {
            outputStream = new ObjectOutputStream(mSocket.getOutputStream());
            inputStream = new ObjectInputStream(mSocket.getInputStream());
        } catch (IOException e) {
            Log.w(ConnectionUtils.TAG, "Socket Communicate Open Stream Exception.", e);
            return;
        }

        while (true) {
            Command command;
            try {
                command = (Command) inputStream.readObject();
                Log.i(TAG, "Receive:" + command.command);
            } catch (EOFException e) {
                // Socket Closed
                break;
            } catch (Exception e) {
                Log.w(TAG, "Receive Command exception", e);
                break;
            }

            Response response = CommandManager.executeCommand(command);

            try {
                if (response != null) {
                    Log.i(TAG, "Send Message" + response.message);
                } else {
                    Log.i(TAG, "Response is null!");
                }
                outputStream.writeObject(response);
            } catch (Exception e) {
                Log.w(TAG, "Send Response exception", e);
                break;
            }
        }

        try {
            mSocket.close();
        } catch (IOException e) {
            // ignore
        }
    }

}

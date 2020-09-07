/*
 * Copyright 2017 John Grosh (john.a.grosh@gmail.com).
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

package de.blutmondgilde.modpackaddons.discord.utils.discord.rpc.entities.pipe;

import de.blutmondgilde.modpackaddons.discord.utils.discord.rpc.IPCClient;
import de.blutmondgilde.modpackaddons.discord.utils.discord.rpc.entities.Callback;
import de.blutmondgilde.modpackaddons.discord.utils.discord.rpc.entities.Packet;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class UnixPipe extends Pipe {
    private final AFUNIXSocket socket;

    UnixPipe(IPCClient ipcClient, HashMap<String, Callback> callbacks, String location) throws IOException {
        super(ipcClient, callbacks);

        socket = AFUNIXSocket.newInstance();
        socket.connect(new AFUNIXSocketAddress(new File(location)));
    }

    @Override
    public Packet read() throws IOException, JsonParseException {
        InputStream is = socket.getInputStream();

        while ((status == PipeStatus.CONNECTED || status == PipeStatus.CLOSING) && is.available() == 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
        }

        if (status == PipeStatus.DISCONNECTED)
            throw new IOException("Disconnected!");

        if (status == PipeStatus.CLOSED)
            return new Packet(Packet.OpCode.CLOSE, null, ipcClient.getEncoding());

        // Read the op and length. Both are signed ints
        byte[] d = new byte[8];
        int readResult = is.read(d);
        ByteBuffer bb = ByteBuffer.wrap(d);


        Packet.OpCode op = Packet.OpCode.values()[Integer.reverseBytes(bb.getInt())];
        d = new byte[Integer.reverseBytes(bb.getInt())];

        int reversedResult = is.read(d);


        JsonObject packetData = new JsonObject();
        packetData.addProperty("", new String(d));
        Packet p = new Packet(op, packetData, ipcClient.getEncoding());

        if (listener != null)
            listener.onPacketReceived(ipcClient, p);
        return p;
    }

    @Override
    public void write(byte[] b) throws IOException {
        socket.getOutputStream().write(b);
    }

    @Override
    public void close() throws IOException {
        status = PipeStatus.CLOSING;
        send(Packet.OpCode.CLOSE, new JsonObject(), null);
        status = PipeStatus.CLOSED;
        socket.close();
    }
}

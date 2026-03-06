package org.jts.mock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TcpClient {

    private final String host;

    private final int port;

    public TcpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        try (Socket socket = new Socket(host, port);
             OutputStream outputStream = socket.getOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
             DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {

            String MSG_TEMPLE = "\"Hello, server!2131236446464646646466aweq2131313123123123awdadawdawda123123131233464我们949847974129436261463241643614634\"";
            StringBuilder message = new StringBuilder(MSG_TEMPLE);
            for (int i = 0; i < 60; i++){
                message.append(i)
                        .append(MSG_TEMPLE);
            }
            byte[] bytes = message.toString().getBytes();
            int length = bytes.length;
            System.out.printf("TCP send length: [%d]KB%n", length/1024);
            // 写入消息长度
            dataOutputStream.writeInt(length);
            // 写入消息内容
            dataOutputStream.write(bytes);
            dataOutputStream.flush();

            // 读取服务器返回的消息长度
            int resLength = dataInputStream.readInt();

            byte[] responseBytes = new byte[resLength];
            // 读取消息内容
            dataInputStream.readFully(responseBytes);
            String response = new String(responseBytes);
            System.out.printf("TCP Server response: [%s]%n", response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TcpClient client = new TcpClient("127.0.0.1", 9999);
        client.run();
    }
}
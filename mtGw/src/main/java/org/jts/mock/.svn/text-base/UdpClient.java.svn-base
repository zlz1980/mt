package org.jts.mock;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jts.util.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdpClient.class);

    private final int port;

    private final String host;

    public UdpClient( String host,int port) {
        this.port = port;
        this.host = host;
    }

    public void start() {
        try (DatagramSocket socket = new DatagramSocket()) {
            // 发送一个测试消息到服务器
            String MSG_TEMPLE = "\"Hello, server!2131236446464646646466aweq2131313123123123awdadawdawda123123131233464我们949847974129436261463241643614634\"";
            StringBuilder message = new StringBuilder(MSG_TEMPLE);
            for (int i = 0; i < 50; i++){
                message.append(i)
                        .append(MSG_TEMPLE);
            }
            InetAddress address = InetAddress.getByName(host);
            byte[] contextBytes = message.toString().getBytes();
            int length = contextBytes.length;
            System.out.printf("Udp send length: [%s]KB%n", length/1024);
            DatagramPacket packet = new DatagramPacket(contextBytes, length, address, port);
            socket.send(packet);
            // 接收服务器的响应
            byte[] buffer = new byte[7000];
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(responsePacket);
            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.printf("Udp Server response: [%s]%n", response);

        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new UdpClient("127.0.0.1",9998).start();
    }
}

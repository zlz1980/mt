package org.jts.server.udp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpJdkServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdpJdkServer.class);

    @Value("${udp.server.port:9998}")
    private int port;

    public void start() throws IOException {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            LOGGER.info("UDP Jdk server started on port [{}]", port);
            byte[] buffer = new byte[3000];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String receive = new  String(packet.getData(), 0, packet.getLength());
                System.out.printf("Server receive: [%s]%n", receive);
            }
        }
    }
}

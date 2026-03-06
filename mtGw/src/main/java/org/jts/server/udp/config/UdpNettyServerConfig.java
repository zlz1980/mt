package org.jts.server.udp.config;

import org.jts.server.udp.UdpJdkServer;
import org.jts.server.udp.UdpNettyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class UdpNettyServerConfig {

    @Bean
    public UdpNettyServer udpNettyServer() {
        UdpNettyServer udpNettyServer = new UdpNettyServer();
        new Thread(()-> {
            try {
                udpNettyServer.start();
            } catch (InterruptedException e) {
                throw new RuntimeException("udpNettyServer exception",e);
            }
        }).start();
        return udpNettyServer;
    }

    // @Bean
    public UdpJdkServer udpJdkServer() {
        UdpJdkServer udpJdkServer = new UdpJdkServer();
        new Thread(()-> {
            try {
                udpJdkServer.start();
            } catch (IOException e) {
                throw new RuntimeException("udpJdkServer exception",e);
            }
        }).start();
        return udpJdkServer;
    }
}

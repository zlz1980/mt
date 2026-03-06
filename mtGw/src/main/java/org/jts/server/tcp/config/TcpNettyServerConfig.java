package org.jts.server.tcp.config;

import org.jts.server.tcp.TcpNettyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TcpNettyServerConfig {

    @Bean
    public TcpNettyServer tcpNettyServer() {
        TcpNettyServer tcpNettyServer = new TcpNettyServer();
        new Thread(()-> {
            try {
                tcpNettyServer.start();
            } catch (InterruptedException e) {
                throw new RuntimeException("tcpNettyServer exception",e);
            }
        }).start();
        return tcpNettyServer;
    }
}

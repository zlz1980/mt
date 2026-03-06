package org.jts.server.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.jts.server.udp.channel.UdpServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class UdpNettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdpNettyServer.class);

    @Value("${udp.server.port:9998}")
    private int port;

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioDatagramChannel.class)
             // 允许广播
             .option(ChannelOption.SO_BROADCAST, true)
             .handler(new ChannelInitializer<NioDatagramChannel>() {
                 @Override
                 protected void initChannel(NioDatagramChannel ch) {
                     DatagramChannelConfig config = ch.config();
                     // 设置接收缓冲区大小1 MB
                     config.setReceiveBufferSize(1024 * 1024);
                     // 设置发送缓冲区大小1 MB
                     config.setSendBufferSize(1024 * 1024);
                     config.setRecvByteBufAllocator(new FixedRecvByteBufAllocator(1024 * 1024));

                     ChannelPipeline pipeline = ch.pipeline();
                     pipeline.addLast(new UdpServerHandler());
                 }
             });

            Channel ch = b.bind(port).sync().channel();
            LOGGER.info("UDP Netty server started on port [{}]", port);

            ch.closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }
    }
}
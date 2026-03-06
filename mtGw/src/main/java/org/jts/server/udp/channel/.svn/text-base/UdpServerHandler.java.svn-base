package org.jts.server.udp.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdpServerHandler.class);

    private final ConcurrentLinkedQueue<ByteBuf> messageQueue = new ConcurrentLinkedQueue<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        // 处理接收到的数据包
        String received = msg.content().toString(CharsetUtil.UTF_8);
        LOGGER.info("Received UDP packet: [{}]", received);

        // 可以在这里添加对数据包的处理逻辑
        ByteBuf response =  Unpooled.wrappedBuffer(("Response: " + received).getBytes());
        ctx.writeAndFlush(new DatagramPacket(response, msg.sender()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Exception in channel pipeline ", cause);
        ctx.close();
    }
}
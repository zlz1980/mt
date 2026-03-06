package org.jts.server.tcp.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomEncoder extends MessageToByteEncoder<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {
        // 这里实现你的编码逻辑，例如写入长度字段，然后写入数据
        if(msg instanceof byte[]){
            byte[] msgBytes = (byte[]) msg;
            int length = msgBytes.length;
            out.writeInt(length);
            out.writeBytes(msgBytes);
        }else {
            LOGGER.warn("msg is not byte[],is[{}]",msg.getClass());
        }

    }
}

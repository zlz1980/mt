package org.jts.server.tcp.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.jts.util.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 前4个字节是数据长度+报文体
 */
public class CustomDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomDecoder.class);

    private final int pkgLength;

    public CustomDecoder() {
        this.pkgLength = 4;
    }

    public CustomDecoder(int pkgLength) {
        this.pkgLength = pkgLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 这里实现你的解码逻辑，例如读取一个长度字段，然后读取相应长度的数据
        if (in.readableBytes() < pkgLength) {
            // 数据不够，等待下一次读取
            return;
        }
        // 标记当前读指针
        in.markReaderIndex();

        byte[] pkgLengthBytes = new byte[4];
        LOGGER.info("pkgLengthBytes:[{}]", pkgLengthBytes);
        in.readBytes(pkgLengthBytes);
        int length = ByteUtils.byteArrayToInt(pkgLengthBytes);
        // int length = in.readInt();
        if (in.readableBytes() < length) {
            // 数据不够，恢复读取索引，等待下一次读取
            in.resetReaderIndex();
            return;
        }
        
        byte[] data = new byte[length];
        in.readBytes(data);
        // 解码成功，添加到输出列表
        out.add(data);
    }

}

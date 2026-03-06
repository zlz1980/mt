   package org.jts.server.tcp.channel;

   import io.netty.channel.ChannelFutureListener;
   import io.netty.channel.ChannelHandlerContext;
   import io.netty.channel.SimpleChannelInboundHandler;
   import org.jts.server.udp.channel.UdpServerHandler;
   import org.slf4j.Logger;
   import org.slf4j.LoggerFactory;
   import org.springframework.stereotype.Component;

   @Component
   public class TcpShortMessageHandler extends SimpleChannelInboundHandler<Object> {

       private static final Logger LOGGER = LoggerFactory.getLogger(TcpShortMessageHandler.class);

       @Override
       protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
           // 处理消息并返回响应
           String msgStr = "";
           if(msg instanceof byte[]){
               msgStr = new String((byte[]) msg);
           }
           LOGGER.info("Received message: [{}]", msgStr);

           ctx.writeAndFlush(("Processed:"+msgStr).getBytes())
                   .addListener(ChannelFutureListener.CLOSE);
       }

       @Override
       public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
           LOGGER.error("Exception in channel pipeline ", cause);
           ctx.close();
       }
   }
   
package com.hbliu.herostory;

import com.google.protobuf.GeneratedMessageV3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgEncoder.class);

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
    if (ctx == null || msg == null) {
      return;
    }

    try {
      if (!(msg instanceof GeneratedMessageV3)) {
        super.write(ctx, msg, promise);
        return;
      }

      // 根据要返回的msg的类型, 找到对应的msgCode值
      final int msgCode = GameMsgRecognizer.getMsgCodeByClazz(msg.getClass());

      if (msgCode == -1) {
        LOGGER.error("Undefined message type, msg class = {}", msg.getClass()
                                                                  .getName());
        super.write(ctx, msg, promise);
        return;
      }

      final byte[] msgBody = ((GeneratedMessageV3) msg).toByteArray();
      final ByteBuf byteBuf = ctx.alloc()
                                 .buffer();
      // 写header (消息长度 + 消息编码) 到ByteBuffer
      byteBuf.writeShort((short) msgBody.length);
      byteBuf.writeShort((short) msgCode);

      // 写body到ByteBuffer
      byteBuf.writeBytes(msgBody);

      final BinaryWebSocketFrame outputFrame = new BinaryWebSocketFrame(byteBuf);
      super.write(ctx, outputFrame, promise);

    } catch (final Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

}

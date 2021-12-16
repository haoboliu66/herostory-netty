package com.hbliu.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.hbliu.herostory.message.GameMsgProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameMsgDecoder extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgDecoder.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (ctx == null || msg == null) return;

        // BinaryWebsocketFrame
        if (!(msg instanceof BinaryWebSocketFrame)) {
            return;
        }

        try {
            BinaryWebSocketFrame inputFrame = (BinaryWebSocketFrame) msg;
            ByteBuf byteBuf = inputFrame.content();

            byteBuf.readShort(); // 消息长度
            short msgCode = byteBuf.readShort(); // 消息编码/编号

            byte[] msgBody = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(msgBody);

            // 根据接收到的msgCode值, decode出对应的cmd
            Message.Builder msgBuilder = GameMsgRecognizer.getBuilderByMsgCode(msgCode);
            if (msgBuilder == null) return;
            msgBuilder.clear();
            msgBuilder.mergeFrom(msgBody);
            Message cmd = msgBuilder.build();

            if (cmd != null) {
                ctx.fireChannelRead(cmd);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}

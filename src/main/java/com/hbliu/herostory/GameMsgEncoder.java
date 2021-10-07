package com.hbliu.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.hbliu.herostory.message.GameMsgProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Arrays;


public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (ctx == null || msg == null) return;

        try {
            if (!(msg instanceof GeneratedMessageV3)) {
                super.write(ctx, msg, promise);
                return;
            }


            // 根据要返回的msg的类型, 找到对应的msgCode值
            int msgCode = -1;
            if (msg instanceof GameMsgProtocol.UserEntryResult) {
                msgCode = GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE;
            } else if (msg instanceof GameMsgProtocol.WhoElseIsHereResult) {
                msgCode = GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE;
            } else {
                LOGGER.error("Undefined message type, msg class = {}", msg.getClass().getName());
                super.write(ctx, msg, promise);
                return;
            }

            byte[] msgBody = ((GeneratedMessageV3) msg).toByteArray();
            ByteBuf byteBuf = ctx.alloc().buffer();
            // 写header (消息长度 + 消息编码) 到ByteBuffer
            byteBuf.writeShort((short) msgBody.length);
            byteBuf.writeShort((short) msgCode);

            // 写body到ByteBuffer
            byteBuf.writeBytes(msgBody);

            BinaryWebSocketFrame outputFrame = new BinaryWebSocketFrame(byteBuf);
            super.write(ctx, outputFrame, promise);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    public static void main(String[] args) {
        ByteBuffer var5 = ByteBuffer.allocate(1024);
        ByteBuffer buffer1 = ByteBuffer.allocate(1024);
        byte[] b = new byte[1000];
        Arrays.fill(b, (byte) 12);
        var5.put(b);
        System.out.println(var5.remaining());
        System.out.println("var5 position before rewind " + var5.position());
//        buffer.mark();
//        buffer.reset();
        var5.rewind();
        System.out.println("var5 position after rewind " + var5.position());
        System.out.println("buffer1 position before put: " + buffer1.position());
        var5.flip();
        buffer1.put(var5);
        System.out.println("buffer1 position: " + buffer1.position());

    }
}

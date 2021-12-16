package com.hbliu.herostory.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

public interface CmdHandler<T extends GeneratedMessageV3> {

    void handle(ChannelHandlerContext ctx, T cmd);
}

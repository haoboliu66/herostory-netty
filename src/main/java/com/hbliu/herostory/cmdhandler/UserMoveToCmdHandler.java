package com.hbliu.herostory.cmdhandler;

import com.hbliu.herostory.Broadcaster;
import com.hbliu.herostory.message.generated.GameMsgProtocol.UserMoveToResult;
import com.hbliu.herostory.message.generated.GameMsgProtocol.UserMoveToCmd;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserMoveToCmdHandler implements CmdHandler<UserMoveToCmd> {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserMoveToCmdHandler.class);

  @Override
  public void handle(ChannelHandlerContext ctx, UserMoveToCmd cmd) {
    Object userId = ctx.channel()
                       .attr(AttributeKey.valueOf("userId"))
                       .get();
    if (userId == null || !(userId instanceof Integer)) {
      return;
    }

//        GameMsgProtocol.UserMoveToCmd cmd = (GameMsgProtocol.UserMoveToCmd) msg;
    UserMoveToResult.Builder resultBuilder = UserMoveToResult.newBuilder();
    resultBuilder.setMoveUserId((Integer) userId)
                 .setMoveToPosX(cmd.getMoveToPosX())
                 .setMoveToPosY(cmd.getMoveToPosY());

    UserMoveToResult result = resultBuilder.build();
    Broadcaster.broadcast(result);
  }
}

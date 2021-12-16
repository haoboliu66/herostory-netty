package com.hbliu.herostory.cmdhandler;

import com.hbliu.herostory.Broadcaster;
import com.hbliu.herostory.User;
import com.hbliu.herostory.UserManager;
import com.hbliu.herostory.message.GameMsgProtocol.UserEntryResult;
import com.hbliu.herostory.message.GameMsgProtocol.UserEntryCmd;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserEntryCmdHandler implements CmdHandler<UserEntryCmd> {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserEntryCmdHandler.class);

  @Override
  public void handle(ChannelHandlerContext ctx, UserEntryCmd cmd) {
    LOGGER.info("UserEntryCmd received");
//        GameMsgProtocol.UserEntryCmd cmd = (GameMsgProtocol.UserEntryCmd) msg;
    int userId = cmd.getUserId();
    String heroAvatar = cmd.getHeroAvatar();

    User newUser = new User(userId, heroAvatar);
    UserManager.addUser(newUser);

    // 保存用户id到session
    ctx.channel()
       .attr(AttributeKey.valueOf("userId"))
       .set(userId);

    UserEntryResult.Builder resultBuilder = UserEntryResult.newBuilder();
    resultBuilder.setUserId(userId)
                 .setHeroAvatar(heroAvatar);

    UserEntryResult result = resultBuilder.build();
    Broadcaster.broadcast(result);
  }

}

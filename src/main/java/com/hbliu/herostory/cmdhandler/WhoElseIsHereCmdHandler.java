package com.hbliu.herostory.cmdhandler;

import com.hbliu.herostory.entity.User;
import com.hbliu.herostory.UserManager;
import com.hbliu.herostory.message.GameMsgProtocol.WhoElseIsHereResult;
import com.hbliu.herostory.message.GameMsgProtocol.WhoElseIsHereCmd;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhoElseIsHereCmdHandler implements CmdHandler<WhoElseIsHereCmd> {

  private static final Logger LOGGER = LoggerFactory.getLogger(WhoElseIsHereCmdHandler.class);

  @Override
  public void handle(ChannelHandlerContext ctx, WhoElseIsHereCmd cmd) {
    WhoElseIsHereResult.Builder resultBuilder = WhoElseIsHereResult.newBuilder();

    for (User user : UserManager.listUsers()) {
      if (user == null) {
        continue;
      }

      WhoElseIsHereResult.UserInfo.Builder builder = WhoElseIsHereResult.UserInfo.newBuilder();
      builder.setUserId(user.getUserId())
             .setHeroAvatar(user.getHeroAvatar());

      resultBuilder.addUserInfo(builder);
    }
    WhoElseIsHereResult result = resultBuilder.build();
    ctx.writeAndFlush(result);
  }
}

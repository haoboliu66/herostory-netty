package com.hbliu.herostory.cmdhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hbliu.herostory.Broadcaster;
import com.hbliu.herostory.entity.User;
import com.hbliu.herostory.UserManager;
import com.hbliu.herostory.message.GameMsgProtocol.UserAttkCmd;
import com.hbliu.herostory.message.GameMsgProtocol.UserAttkResult;
import com.hbliu.herostory.message.GameMsgProtocol.UserAttkResult.Builder;
import com.hbliu.herostory.message.GameMsgProtocol.UserDieResult;
import com.hbliu.herostory.message.GameMsgProtocol.UserSubtractHpResult;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class UserAttkCmdHandler implements CmdHandler<UserAttkCmd> {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserAttkCmdHandler.class);

  private static final int DAMAGE_POINT = 10;

  @Override
  public void handle(ChannelHandlerContext ctx, UserAttkCmd cmd) {
    if (ctx == null || cmd == null) {
      return;
    }

    Integer attkUserId = (Integer) ctx.channel()
                                      .attr(AttributeKey.valueOf("userId"))
                                      .get();
    if (attkUserId == null) {
      return;
    }

    int targetUserId = cmd.getTargetUserId();
    User targetUser = UserManager.getUserById(targetUserId);
    if (targetUser == null) {
      return;
    }

    int curHp = targetUser.getCurrentHp();
    targetUser.setCurrentHp(curHp - DAMAGE_POINT);

    broadcastAttack(attkUserId, targetUserId);
    broadcastSubtractHp(targetUserId, DAMAGE_POINT);

    if (curHp <= DAMAGE_POINT) {
      broadcastDeath(targetUserId);
      return;
    }
  }

  private static void broadcastAttack(int attkUserId, int targetUserId) {
    if (attkUserId <= 0) {
      return;
    }
    Builder resultBuilder = UserAttkResult.newBuilder();
    resultBuilder.setAttkUserId(attkUserId)
                 .setTargetUserId(targetUserId);
    UserAttkResult attkResult = resultBuilder.build();
    Broadcaster.broadcast(attkResult);
  }

  private static void broadcastSubtractHp(int targetUserId, int subtractHp) {
    if (targetUserId <= 0 || subtractHp <= 0) {
      return;
    }
    UserSubtractHpResult.Builder builder = UserSubtractHpResult.newBuilder();
    builder.setTargetUserId(targetUserId)
           .setSubtractHp(subtractHp);
    UserSubtractHpResult subtractHpResult = builder.build();
    Broadcaster.broadcast(subtractHpResult);
  }

  private static void broadcastDeath(int targetUserId) {
    if (targetUserId <= 0) {
      return;
    }
    UserDieResult.Builder resultBuilder = UserDieResult.newBuilder();
    resultBuilder.setTargetUserId(targetUserId);
    UserDieResult userDieResult = resultBuilder.build();
    Broadcaster.broadcast(userDieResult);
  }


}

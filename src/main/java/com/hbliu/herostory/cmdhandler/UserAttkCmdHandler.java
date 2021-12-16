package com.hbliu.herostory.cmdhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hbliu.herostory.message.GameMsgProtocol.UserAttkCmd;

import io.netty.channel.ChannelHandlerContext;

public class UserAttkCmdHandler implements CmdHandler<UserAttkCmd>{

  private static final Logger LOGGER = LoggerFactory.getLogger(UserAttkCmdHandler.class);

  @Override
  public void handle(ChannelHandlerContext ctx, UserAttkCmd cmd) {
    System.out.println("attack......");
  }
}

package com.hbliu.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.hbliu.herostory.factory.CmdHandlerFactory;
import com.hbliu.herostory.cmdhandler.CmdHandler;
import com.hbliu.herostory.message.generated.GameMsgProtocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgHandler.class);

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    if (ctx == null) {
      return;
    }
    try {
      super.channelActive(ctx);
      Broadcaster.addChannel(ctx.channel());
    } catch (final Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) {
    if (ctx == null) {
      return;
    }

    try {
      super.handlerRemoved(ctx);

      final Object userId = ctx.channel()
                               .attr(AttributeKey.valueOf("userId"))
                               .get();
      if (userId == null || !(userId instanceof Integer)) {
        return;
      }

      final GameMsgProtocol.UserQuitResult.Builder resultBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
      resultBuilder.setQuitUserId((Integer) userId);
      GameMsgProtocol.UserQuitResult result = resultBuilder.build();
      Broadcaster.broadcast(result);

      UserManager.removeUser((Integer) userId);
      Broadcaster.removeChannel(ctx.channel());
    } catch (final Exception e) {
      LOGGER.error(e.getMessage(), e);
    }


  }

  @Override
  protected void channelRead0(final ChannelHandlerContext ctx, final Object msg) {
    if (ctx == null || msg == null) {
      return;
    }
    LOGGER.info("client message received, msg = {}", msg);
    try {
      final CmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.createHandler(msg.getClass());
      if (cmdHandler != null) {
        cmdHandler.handle(ctx, cast(msg));
      }
    } catch (final Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  private static <TCmd extends GeneratedMessageV3> TCmd cast(Object m) {
    if (m == null) {
      return null;
    }
    return (TCmd) m;
  }
}

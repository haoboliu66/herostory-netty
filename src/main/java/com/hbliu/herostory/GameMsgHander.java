package com.hbliu.herostory;

import com.hbliu.herostory.message.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GameMsgHander extends SimpleChannelInboundHandler<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgHander.class);

    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final Map<Integer, User> userMap = new HashMap<>();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (ctx == null || msg == null) return;

        LOGGER.info("client message received, msg = {}", msg);

        try {
            if (msg instanceof GameMsgProtocol.UserEntryCmd) {
                LOGGER.info("UserEntryCmd received");
                GameMsgProtocol.UserEntryCmd cmd = (GameMsgProtocol.UserEntryCmd) msg;
                int userId = cmd.getUserId();
                String heroAvatar = cmd.getHeroAvatar();

                User newUser = new User(userId, heroAvatar);
                userMap.putIfAbsent(userId, newUser);

                GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
                resultBuilder.setUserId(userId).setHeroAvatar(heroAvatar);

                GameMsgProtocol.UserEntryResult result = resultBuilder.build();
                channelGroup.writeAndFlush(result);
            } else if (msg instanceof GameMsgProtocol.WhoElseIsHereCmd) {
                GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

                for (User user : userMap.values()) {
                    if (user == null) continue;

                    GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder builder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
                    builder.setUserId(user.userId).setHeroAvatar(user.heroAvatar);

                    resultBuilder.addUserInfo(builder);
                }
                GameMsgProtocol.WhoElseIsHereResult result = resultBuilder.build();
                ctx.writeAndFlush(result);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }


}

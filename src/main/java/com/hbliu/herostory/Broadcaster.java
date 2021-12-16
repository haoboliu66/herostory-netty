package com.hbliu.herostory;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public final class Broadcaster {

    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Broadcaster() {
    }


    public static void addChannel(Channel ch) {
        if (ch != null) {
            channelGroup.add(ch);
        }
    }


    public static void removeChannel(Channel ch) {
        if (ch != null) {
            channelGroup.remove(ch);
        }
    }

    public static void broadcast(Object obj) {
        if (obj != null) {
            channelGroup.writeAndFlush(obj);
        }
    }

}

package com.hbliu.herostory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
http://cdn0001.afrxvk.cn/hero_story/demo/step010/index.html
 */

public class ServerMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);
    private static int SERVER_PORT = 12345;

    public static void main(String[] args) {
        PropertyConfigurator.configure(ServerMain.class.getClassLoader().getResourceAsStream("log4j.properties"));

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new HttpServerCodec(),
                                    new HttpObjectAggregator(65535),
                                    new WebSocketServerProtocolHandler("/websocket"),
                                    new GameMsgDecoder(),
                                    new GameMsgEncoder(),
                                    new GameMsgHander()   // 自定义的消息处理器
                            );
                        }
                    });
//            .option(ChannelOption.SO_BACKLOG, 128)
//            .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(SERVER_PORT).sync();
            if (future.isSuccess()) {
                LOGGER.info("server started");
            }

            future.channel().close().sync();

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }


    }
}

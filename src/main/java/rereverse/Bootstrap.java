/*******************************************************************************
 *        File: Bootstrap.java
 *      Author: Morteza Ansarinia <ansarinia@me.com>
 *  Created on: Nov 10, 2013
 *     Project: ReReverse
 *   Copyright: See the file "LICENSE" for the full license governing this code.
 *******************************************************************************/
package rereverse;

import rereverse.model.DeliveryMapping;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Bootstrap {

	//TODO remove, create a model for configurations
    private final int localPort;

    public Bootstrap(int localPort) {
        this.localPort = localPort;
    }

    public void run() throws Exception {
        System.out.println("Starting ReReverse on local port " + localPort + "...");

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel c) throws Exception {
					//FIXME remove this shit. This is for testing only
					DeliveryMapping app = new DeliveryMapping("9030","www.bitbucket.com");
					//TODO iterate over all ApplicationDelivery models
					c.pipeline().addLast("rereverse", new ReReverseHandler(app));
				}
			})
             .childOption(ChannelOption.AUTO_READ, false)
             .bind(localPort).sync().channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.err.println("Usage: rereverse <local port>");
            System.exit(1);
        }
        
        int localPort = Integer.parseInt(args[0]);

        new Bootstrap(localPort).run();
    }
}

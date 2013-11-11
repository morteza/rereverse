/*******************************************************************************
 *        File: ReReverseHandler.java
 *      Author: Morteza Ansarinia <ansarinia@me.com>
 *  Created on: Nov 10, 2013
 *     Project: ReReverse
 *   Copyright: See the file "LICENSE" for the full license governing this code.
 *******************************************************************************/
package rereverse;

import rereverse.model.DeliveryMapping;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;

public class ReReverseHandler extends ChannelInboundHandlerAdapter {

    private DeliveryMapping application;

    private volatile Channel outbound;

    
    public ReReverseHandler(DeliveryMapping application) {
    	this.application = application;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final Channel inboundChannel = ctx.channel();

        Bootstrap b = new Bootstrap();
        b.group(inboundChannel.eventLoop())
         .channel(ctx.channel().getClass())
         .handler(new ChannelInboundHandlerAdapter(){
        	    @Override
        	    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        	        ctx.read();
        	        ctx.write(Unpooled.EMPTY_BUFFER);
        	    }

        	    @Override
        	    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        	        inboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
        	            @Override
        	            public void operationComplete(ChannelFuture future) throws Exception {
        	                if (future.isSuccess()) {
        	                    ctx.channel().read();
        	                } else {
        	                    future.channel().close();
        	                }
        	            }
        	        });
        	    }

        	    @Override
        	    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        	        closeChannel(inboundChannel);
        	    }

        	    @Override
        	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        	        cause.printStackTrace();
        	        closeChannel(ctx.channel());
        	    }
         })
         .option(ChannelOption.AUTO_READ, false);
        
        //TODO map using url without port (?)
        ChannelFuture f = b.connect(application.remoteHost, application.remotePort);
        outbound = f.channel();
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    inboundChannel.read();
                } else {
                	// failed
                    inboundChannel.close();
                }
            }
        });
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        if (outbound.isActive()) {
            outbound.writeAndFlush(msg).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        ctx.channel().read();
                    } else {
                        future.channel().close();
                    }
                }
            });
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        closeChannel(outbound);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        closeChannel(ctx.channel());
    }

    static void closeChannel(Channel ch) {
        if (ch!=null && ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}

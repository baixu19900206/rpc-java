package com.wenweihu86.rpc.server.handler;

import com.wenweihu86.rpc.codec.ProtoV3Header;
import com.wenweihu86.rpc.codec.ProtoV3Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wenweihu86 on 2017/4/25.
 */
public class RPCServerHandler extends SimpleChannelInboundHandler<ProtoV3Message<ProtoV3Header.RequestHeader>> {

    private static final Logger LOG = LoggerFactory.getLogger(RPCServerHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             ProtoV3Message<ProtoV3Header.RequestHeader> request) throws Exception {
        WorkHandler.WorkTask task = new WorkHandler.WorkTask(ctx, request);
        WorkHandler.getExecutor().submit(task);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ProtoV3Message<ProtoV3Header.ResponseHeader> response = new ProtoV3Message<>();
        ProtoV3Header.ResponseHeader header = ProtoV3Header.ResponseHeader.newBuilder()
                .setResCode(ProtoV3Header.ResCode.RES_FAIL).setResMsg(cause.getMessage()).build();
        response.setHeader(header);
        response.setBody(new byte[]{});
        ctx.fireChannelRead(response);
    }
}

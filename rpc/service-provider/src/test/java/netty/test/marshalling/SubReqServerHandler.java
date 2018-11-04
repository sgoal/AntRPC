package netty.test.marshalling;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;;

@Sharable
public class SubReqServerHandler extends ChannelInboundHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		RpcRequest request = (RpcRequest)msg;
		System.out.println("read: "+ request.getMehtodName());
		RpcResponse response = new RpcResponse();
		response.setMehtodName(request.getMehtodName()+"lalalala");
		ctx.write(response);
//		super.channelRead(ctx, msg);
	}
	
}

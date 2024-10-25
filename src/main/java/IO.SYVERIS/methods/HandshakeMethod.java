package IO.SYVERIS.methods;

import IO.SYVERIS.Main;
import IO.SYVERIS.utils.Handshake;
import IO.SYVERIS.utils.NettyBootstrap;
import IO.SYVERIS.utils.ProxyLoader;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class HandshakeMethod implements Method {
   private Handshake handshake;
   private byte[] bytes;

   public HandshakeMethod() {
      this.handshake = new Handshake(Main.protcolID, Main.srvRecord, Main.port, 1);
      this.bytes = this.handshake.getWrappedPacket();
   }

   public void accept(Channel channel, ProxyLoader.Proxy proxy) {
      channel.writeAndFlush(Unpooled.buffer().writeBytes(this.bytes));
      ++NettyBootstrap.integer;
      channel.close();
   }
}

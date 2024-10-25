package IO.SYVERIS.methods;

import IO.SYVERIS.Main;
import IO.SYVERIS.utils.Handshake;
import IO.SYVERIS.utils.NettyBootstrap;
import IO.SYVERIS.utils.PingPacket;
import IO.SYVERIS.utils.ProxyLoader;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class Ping implements Method {
   private byte[] handshakebytes;

   public Ping() {
      this.handshakebytes = (new Handshake(Main.protcolID, Main.srvRecord, Main.port, 1)).getWrappedPacket();
   }

   public void accept(Channel channel, ProxyLoader.Proxy proxy) {
      channel.writeAndFlush(Unpooled.buffer().writeBytes(this.handshakebytes));
      channel.writeAndFlush(Unpooled.buffer().writeBytes(new byte[]{1, 0}));
      channel.writeAndFlush(Unpooled.buffer().writeBytes((new PingPacket(System.currentTimeMillis())).getWrappedPacket()));
      ++NettyBootstrap.integer;
      ++NettyBootstrap.totalConnections;
      channel.close();
   }
}

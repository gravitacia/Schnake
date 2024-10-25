package IO.SYVERIS.methods;

import IO.SYVERIS.Main;
import IO.SYVERIS.utils.Handshake;
import IO.SYVERIS.utils.LoginRequest;
import IO.SYVERIS.utils.NettyBootstrap;
import IO.SYVERIS.utils.ProxyLoader;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import java.security.SecureRandom;
import java.util.Random;

public class Join implements Method {
   private Handshake handshake;
   private byte[] bytes;

   private String randomString(int len) {
      int leftLimit = 97;
      int rightLimit = 122;
      int targetStringLength = len;
      Random random = new Random();
      StringBuilder buffer = new StringBuilder(len);

      for(int i = 0; i < targetStringLength; ++i) {
         int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (float)(rightLimit - leftLimit + 1));
         buffer.append((char)randomLimitedInt);
      }

      return buffer.toString();
   }

   public Join() {
      this.handshake = new Handshake(Main.protcolID, Main.srvRecord, Main.port, 2);
      this.bytes = this.handshake.getWrappedPacket();
   }

   public void accept(Channel channel, ProxyLoader.Proxy proxy) {
      channel.writeAndFlush(Unpooled.buffer().writeBytes(this.bytes));
      channel.writeAndFlush(Unpooled.buffer().writeBytes((new LoginRequest("syveris_" + (new SecureRandom()).nextInt(99999))).getWrappedPacket()));
      ++NettyBootstrap.integer;
      ++NettyBootstrap.totalConnections;
      channel.close();
   }
}

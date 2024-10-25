package IO.SYVERIS.utils;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public final class RandomUtils {
   public static String randomUTF16String1(int length) {
      Random rnd = new Random();
      StringBuilder s = new StringBuilder();

      for(int i = 0; i < length; ++i) {
         s.append((char)(-128 + rnd.nextInt(255)));
      }

      return new String(s.toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_16);
   }

   public static int nextInt(int startInclusive, int endExclusive) {
      return endExclusive - startInclusive <= 0 ? startInclusive : startInclusive + (new Random()).nextInt(endExclusive - startInclusive);
   }

   public static double nextDouble(double startInclusive, double endInclusive) {
      return startInclusive != endInclusive && !(endInclusive - startInclusive <= 0.0D) ? startInclusive + (endInclusive - startInclusive) * Math.random() : startInclusive;
   }

   public static float nextFloat(float startInclusive, float endInclusive) {
      return startInclusive != endInclusive && !(endInclusive - startInclusive <= 0.0F) ? (float)((double)startInclusive + (double)(endInclusive - startInclusive) * Math.random()) : startInclusive;
   }

   public static String randomNumber(int length) {
      return random(length, "123456789");
   }

   public static String randomString(int length) {
      return random(length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
   }

   public static String random(int length, String chars) {
      return random(length, chars.toCharArray());
   }

   public static String random(int length, char[] chars) {
      StringBuilder stringBuilder = new StringBuilder();

      for(int i = 0; i < length; ++i) {
         stringBuilder.append(chars[(new Random()).nextInt(chars.length)]);
      }

      return stringBuilder.toString();
   }
}

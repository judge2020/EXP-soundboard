/*    */ package exp.soundboard;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ public class SoundUtils
/*    */ {
/*    */   public static short[] byteToShortArray(byte[] byteArray)
/*    */   {
/*  9 */     short[] shortArray = new short[byteArray.length / 2];
/* 10 */     for (int i = 0; i < shortArray.length; i++) {
/* 11 */       int ub1 = byteArray[(i * 2 + 0)] & 0xFF;
/* 12 */       int ub2 = byteArray[(i * 2 + 1)] & 0xFF;
/* 13 */       shortArray[i] = ((short)((ub2 << 8) + ub1));
/*    */     }
/* 15 */     return shortArray;
/*    */   }
/*    */   
/*    */   public static byte[] shortArrayToByteArray(short[] shortArray) {
/* 19 */     byte[] byteArray = new byte[shortArray.length * 2];
/* 20 */     ByteBuffer.wrap(byteArray).order(java.nio.ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(shortArray);
/* 21 */     return byteArray;
/*    */   }
/*    */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\soundboard\SoundUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
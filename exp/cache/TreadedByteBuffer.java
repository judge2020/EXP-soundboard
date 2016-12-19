/*    */ package exp.cache;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class TreadedByteBuffer
/*    */ {
/*    */   private ArrayList<byte[]> fBytes;
/*    */   private HashMap<String, Integer> fIndexTracker;
/*    */   private int fBufferSize;
/*    */   
/*    */   public TreadedByteBuffer(int aBufferSize)
/*    */   {
/* 14 */     this.fBufferSize = aBufferSize;
/* 15 */     this.fIndexTracker = new HashMap();
/* 16 */     this.fBytes = new ArrayList();
/*    */   }
/*    */   
/*    */   public void concat(byte[] aByteArray, int aBytesRead) {
/* 20 */     if (aByteArray.length == this.fBufferSize) {
/* 21 */       this.fBytes.add(aByteArray);
/*    */     } else {
/* 23 */       byte[] bytes = java.util.Arrays.copyOfRange(aByteArray, 0, aBytesRead - 1);
/* 24 */       this.fBytes.add(bytes);
/*    */     }
/*    */   }
/*    */   
/*    */   public byte[] getNext(String uuid) {
/* 29 */     if (!this.fIndexTracker.containsKey(uuid)) {
/* 30 */       this.fIndexTracker.put(uuid, Integer.valueOf(0));
/*    */     }
/*    */     
/* 33 */     int index = ((Integer)this.fIndexTracker.get(uuid)).intValue();
/* 34 */     byte[] returnArray = (byte[])this.fBytes.get(index);
/* 35 */     if (returnArray.length != this.fBufferSize)
/* 36 */       this.fIndexTracker.remove(uuid);
/* 37 */     return returnArray;
/*    */   }
/*    */   
/*    */   public long getCurrentBufferedSizeRounded() {
/* 41 */     int arraySize = this.fBytes.size();
/* 42 */     long bytesBuffered = arraySize * this.fBufferSize;
/* 43 */     return bytesBuffered;
/*    */   }
/*    */   
/*    */   public int getBufferSize() {
/* 47 */     return this.fBufferSize;
/*    */   }
/*    */   
/*    */   public void clear() {
/* 51 */     this.fBytes.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\cache\TreadedByteBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
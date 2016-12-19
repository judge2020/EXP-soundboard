/*    */ package exp.cache;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.lang.ref.SoftReference;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ 
/*    */ public class MemoryCache
/*    */ {
/*    */   private long maxCacheBytes;
/*    */   private ConcurrentHashMap<String, SoftReference<BufferedFile>> fMap;
/*    */   
/*    */   public MemoryCache(int maxMegabytes)
/*    */   {
/* 17 */     this.fMap = new ConcurrentHashMap();
/* 18 */     this.maxCacheBytes = (maxMegabytes * 1000000);
/*    */   }
/*    */   
/*    */   public BufferedFile getBufferedFile(File file, int aBufferSize) {
/* 22 */     if (!this.fMap.containsKey(file.toString())) {
/* 23 */       BufferedFile bf = new BufferedFile(file, aBufferSize);
/* 24 */       SoftReference<BufferedFile> srbf = new SoftReference(bf);
/* 25 */       this.fMap.put(file.toString(), srbf);
/* 26 */       cacheMaintainance();
/*    */     }
/* 28 */     BufferedFile bf = (BufferedFile)((SoftReference)this.fMap.get(file.toString())).get();
/* 29 */     if (bf != null) {
/* 30 */       return bf;
/*    */     }
/* 32 */     this.fMap.remove(file.toString());
/* 33 */     return getBufferedFile(file, aBufferSize);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private void cacheMaintainance()
/*    */   {
/* 41 */     long currentSize = 0L;
/* 42 */     HashMap<String, Long> timeMap = new HashMap();
/* 43 */     for (SoftReference<BufferedFile> sf : this.fMap.values()) {
/* 44 */       BufferedFile bf = null;
/* 45 */       if ((bf = (BufferedFile)sf.get()) != null) {
/* 46 */         currentSize += bf.getCurrentBufferedSizeRounded();
/* 47 */         timeMap.put(bf.getFile().toString(), Long.valueOf(bf.getLastCallTime()));
/*    */       }
/*    */     }
/* 50 */     while (currentSize > this.maxCacheBytes) {
/* 51 */       removeOldestEntry(timeMap);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private synchronized void removeOldestEntry(HashMap<String, Long> aTimeMap)
/*    */   {
/* 59 */     int oldest = 0;
/* 60 */     long oldestTime = 0L;
/* 61 */     ArrayList<Map.Entry<String, Long>> times = new ArrayList(aTimeMap.entrySet());
/* 62 */     for (int i = 0; i < times.size(); i++) {
/* 63 */       if (i == 0) {
/* 64 */         oldestTime = ((Long)((Map.Entry)times.get(0)).getValue()).longValue();
/*    */       }
/* 66 */       else if (((Long)((Map.Entry)times.get(i)).getValue()).longValue() < oldestTime) {
/* 67 */         oldestTime = ((Long)((Map.Entry)times.get(i)).getValue()).longValue();
/* 68 */         oldest = i;
/*    */       }
/*    */     }
/*    */     
/* 72 */     this.fMap.remove(((Map.Entry)times.get(oldest)).getKey());
/*    */   }
/*    */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\cache\MemoryCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
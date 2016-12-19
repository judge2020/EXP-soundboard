/*    */ package exp.cache;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class BufferedFile extends TreadedByteBuffer
/*    */ {
/*    */   private final long fFileSize;
/*    */   private HashMap<String, Long> fReadingTracker;
/*    */   private File fFile;
/* 15 */   private BufferedInputStream fBufferedInput = null;
/*    */   private byte[] fByteBuffer;
/*    */   private long fLastCallMs;
/*    */   
/*    */   public BufferedFile(File file, int aBufferSize) {
/* 20 */     super(aBufferSize);
/* 21 */     this.fFileSize = file.length();
/* 22 */     this.fReadingTracker = new HashMap();
/*    */     try {
/* 24 */       this.fBufferedInput = new BufferedInputStream(new FileInputStream(this.fFile));
/*    */     } catch (FileNotFoundException e) {
/* 26 */       e.printStackTrace();
/*    */     }
/* 28 */     this.fByteBuffer = new byte[aBufferSize];
/* 29 */     this.fLastCallMs = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public byte[] readNextBytes(String uuid) {
/* 33 */     this.fLastCallMs = System.currentTimeMillis();
/* 34 */     if (!this.fReadingTracker.containsKey(uuid)) {
/* 35 */       this.fReadingTracker.put(uuid, Long.valueOf(0L));
/*    */     }
/* 37 */     long totalRead = ((Long)this.fReadingTracker.get(uuid)).longValue();
/* 38 */     if (totalRead >= this.fFileSize) {
/*    */       try {
/* 40 */         this.fBufferedInput.close();
/*    */       } catch (IOException e) {
/* 42 */         e.printStackTrace();
/*    */       }
/*    */     }
/*    */     
/* 46 */     if (getCurrentBufferedSizeRounded() < this.fFileSize) {
/* 47 */       int bytesRead = 0;
/*    */       try {
/* 49 */         bytesRead = this.fBufferedInput.read(this.fByteBuffer);
/*    */       } catch (IOException e) {
/* 51 */         e.printStackTrace();
/*    */       }
/* 53 */       if (bytesRead > 0)
/*    */       {
/*    */ 
/* 56 */         concat(this.fByteBuffer, bytesRead);
/*    */       }
/* 58 */       long prevRead = ((Long)this.fReadingTracker.get(uuid)).longValue();
/* 59 */       this.fReadingTracker.put(uuid, Long.valueOf(prevRead + bytesRead));
/*    */     }
/*    */     
/* 62 */     byte[] returnBytes = getNext(uuid);
/* 63 */     return returnBytes;
/*    */   }
/*    */   
/*    */   public long getFileSize() {
/* 67 */     return this.fFileSize;
/*    */   }
/*    */   
/*    */   public long getLastCallTime() {
/* 71 */     return this.fLastCallMs;
/*    */   }
/*    */   
/*    */   public File getFile() {
/* 75 */     return this.fFile;
/*    */   }
/*    */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\cache\BufferedFile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package exp.soundboard;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.ShortBuffer;
/*     */ import java.util.ArrayList;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ import javax.sound.sampled.DataLine.Info;
/*     */ import javax.sound.sampled.FloatControl;
/*     */ import javax.sound.sampled.FloatControl.Type;
/*     */ import javax.sound.sampled.Line.Info;
/*     */ import javax.sound.sampled.LineUnavailableException;
/*     */ import javax.sound.sampled.Mixer;
/*     */ import javax.sound.sampled.Mixer.Info;
/*     */ import javax.sound.sampled.SourceDataLine;
/*     */ import javax.sound.sampled.TargetDataLine;
/*     */ import javax.swing.JOptionPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MicInjector
/*     */   extends Thread
/*     */ {
/*     */   private static final int INTERNAL_BUFFER_SIZE = 8192;
/*     */   private static final int bufferSize = 512;
/*  34 */   private static float fFrameRate = 44100.0F;
/*     */   Mixer inputMixer;
/*     */   Mixer outputMixer;
/*     */   private String inputLineName;
/*     */   private String outputLineName;
/*     */   private SourceDataLine sourceDataLine;
/*     */   private TargetDataLine targetDataLine;
/*     */   private final byte[] inputBuffer;
/*     */   private int bytesRead;
/*  43 */   private static final AudioFormat signedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, fFrameRate, 16, 2, 4, fFrameRate, false);
/*  44 */   public static final DataLine.Info targetDataLineInfo = new DataLine.Info(TargetDataLine.class, signedFormat, 8192);
/*  45 */   public static final DataLine.Info sourceDataLineInfo = new DataLine.Info(SourceDataLine.class, signedFormat, 8192);
/*     */   private static float gainLevel;
/*     */   private boolean bypass;
/*     */   FloatControl gainControl;
/*     */   private boolean fadeOut;
/*     */   int userVolume;
/*  51 */   private boolean muted = false;
/*  52 */   private boolean run = false;
/*     */   
/*     */   private long nextDrift;
/*  55 */   private final long driftinterval = 1800000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   MicInjector()
/*     */   {
/*  63 */     this.inputBuffer = new byte['Ȁ'];
/*  64 */     this.inputLineName = "none selected";
/*  65 */     this.outputLineName = "none selected";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setInputMixer(String mixerName)
/*     */   {
/*  74 */     String[] mixers = getMixerNames(targetDataLineInfo);
/*  75 */     String[] arrayOfString1; int j = (arrayOfString1 = mixers).length; for (int i = 0; i < j; i++) { String x = arrayOfString1[i];
/*  76 */       Mixer.Info[] arrayOfInfo; int m = (arrayOfInfo = AudioSystem.getMixerInfo()).length; for (int k = 0; k < m; k++) { Mixer.Info mixerInfo = arrayOfInfo[k];
/*  77 */         if (mixerName.equals(mixerInfo.getName())) {
/*  78 */           this.inputMixer = AudioSystem.getMixer(mixerInfo);
/*  79 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setOutputMixer(String mixerName)
/*     */   {
/*  92 */     String[] mixers = getMixerNames(sourceDataLineInfo);
/*  93 */     String[] arrayOfString1; int j = (arrayOfString1 = mixers).length; for (int i = 0; i < j; i++) { String x = arrayOfString1[i];
/*  94 */       Mixer.Info[] arrayOfInfo; int m = (arrayOfInfo = AudioSystem.getMixerInfo()).length; for (int k = 0; k < m; k++) { Mixer.Info mixerInfo = arrayOfInfo[k];
/*  95 */         if (mixerName.equals(mixerInfo.getName())) {
/*  96 */           this.outputMixer = AudioSystem.getMixer(mixerInfo);
/*  97 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setupGate()
/*     */   {
/* 109 */     if (this.targetDataLine != null) {
/* 110 */       clearLines();
/*     */     }
/*     */     try
/*     */     {
/* 114 */       this.targetDataLine = ((TargetDataLine)this.inputMixer.getLine(targetDataLineInfo));
/* 115 */       this.inputLineName = this.inputMixer.getMixerInfo().getName();
/* 116 */       this.targetDataLine.open(signedFormat, 8192);
/* 117 */       this.targetDataLine.start();
/*     */     } catch (LineUnavailableException ex) {
/* 119 */       JOptionPane.showMessageDialog(null, "Selected Input Line " + this.inputLineName + " is currently unavailable.", "Line Unavailable Exception", 0);
/*     */     }
/*     */     try
/*     */     {
/* 123 */       this.sourceDataLine = ((SourceDataLine)this.outputMixer.getLine(sourceDataLineInfo));
/* 124 */       this.outputLineName = this.outputMixer.getMixerInfo().getName();
/* 125 */       this.sourceDataLine.open(signedFormat, 8192);
/* 126 */       this.sourceDataLine.start();
/*     */     } catch (LineUnavailableException ex) {
/* 128 */       JOptionPane.showMessageDialog(null, "Selected Output Line " + this.outputLineName + " is currently unavailable.", "Line Unavailable Exception", 0);
/*     */     }
/* 130 */     this.gainControl = ((FloatControl)this.sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN));
/* 131 */     this.gainControl.setValue(gainLevel);
/* 132 */     System.out.println(this.targetDataLine.getLineInfo().toString());
/* 133 */     System.out.println("Buffer size is " + this.targetDataLine.getBufferSize());
/* 134 */     this.fadeOut = true;
/*     */   }
/*     */   
/*     */   public synchronized void setGain(float level) {
/* 138 */     gainLevel = level;
/* 139 */     if (this.gainControl != null) {
/* 140 */       this.gainControl.setValue(level);
/*     */     }
/*     */   }
/*     */   
/*     */   public static synchronized float getGain() {
/* 145 */     return gainLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private synchronized void clearLines()
/*     */   {
/* 152 */     this.targetDataLine.close();
/* 153 */     this.sourceDataLine.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void read()
/*     */   {
/* 162 */     this.bytesRead = this.targetDataLine.read(this.inputBuffer, 0, 512);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void write()
/*     */   {
/* 170 */     this.sourceDataLine.write(this.inputBuffer, 0, this.bytesRead);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void writeFadeIn()
/*     */   {
/* 181 */     this.sourceDataLine.write(this.inputBuffer, 0, this.bytesRead);
/* 182 */     if (this.gainControl.getValue() < this.userVolume) {
/* 183 */       if (this.gainControl.getValue() < -20.0F) {
/* 184 */         this.gainControl.setValue(-20.0F);
/*     */       }
/* 186 */       this.gainControl.shift(this.gainControl.getValue(), this.gainControl.getValue() + 1.0F, 10000000);
/*     */     }
/* 188 */     if (this.gainControl.getValue() >= this.userVolume) {
/* 189 */       resetGain();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void writeFadeOut()
/*     */   {
/* 200 */     this.sourceDataLine.write(this.inputBuffer, 0, this.bytesRead);
/* 201 */     if (this.fadeOut) {
/* 202 */       if (this.gainControl.getValue() > -70.0F) {
/* 203 */         this.gainControl.setValue(this.gainControl.getValue() - 0.1F);
/*     */       }
/* 205 */       if (this.gainControl.getValue() <= -69.9F) {
/* 206 */         this.gainControl.setValue(-80.0F);
/* 207 */         this.fadeOut = false;
/* 208 */         System.out.println("Fade OUT off!");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/* 221 */     setupGate();
/* 222 */     this.run = true;
/* 223 */     this.nextDrift = (System.currentTimeMillis() + 1800000L);
/* 224 */     while (this.run) {
/* 225 */       read();
/* 226 */       write();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 234 */       if (System.currentTimeMillis() > this.nextDrift) {
/* 235 */         driftReset();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isRunning() {
/* 241 */     return this.run;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   synchronized void setBypass(boolean bypass)
/*     */   {
/* 250 */     this.bypass = bypass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   synchronized void setFadeOut(boolean fadeOut)
/*     */   {
/* 259 */     this.fadeOut = fadeOut;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] getMixerNames(DataLine.Info lineInfo)
/*     */   {
/* 271 */     ArrayList<String> mixerNames = new ArrayList();
/* 272 */     Mixer.Info[] info = AudioSystem.getMixerInfo();
/* 273 */     Mixer.Info[] arrayOfInfo1; int j = (arrayOfInfo1 = info).length; for (int i = 0; i < j; i++) { Mixer.Info elem = arrayOfInfo1[i];
/* 274 */       Mixer mixer = AudioSystem.getMixer(elem);
/*     */       try {
/* 276 */         if (mixer.isLineSupported(lineInfo)) {
/* 277 */           mixerNames.add(elem.getName());
/*     */         }
/*     */       } catch (NullPointerException e) {
/* 280 */         System.err.println(e);
/*     */       }
/*     */     }
/* 283 */     String[] returnarray = new String[mixerNames.size()];
/* 284 */     return (String[])mixerNames.toArray(returnarray);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static float findLevel(byte[] buffer)
/*     */   {
/* 299 */     double dB = 0.0D;
/* 300 */     for (int i = 0; i < buffer.length; i++) {
/* 301 */       dB = 20.0D * Math.log10(Math.abs(buffer[i] / 32767.0D));
/* 302 */       if ((dB == Double.NEGATIVE_INFINITY) || (dB == NaN.0D)) {
/* 303 */         dB = -90.0D;
/*     */       }
/*     */     }
/* 306 */     float level = (float)dB + 91.0F;
/* 307 */     return level;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static float getdB(byte[] buffer)
/*     */   {
/* 319 */     double dB = 0.0D;
/* 320 */     short[] shortArray = new short[buffer.length / 2];
/* 321 */     ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortArray);
/* 322 */     for (int i = 0; i < shortArray.length; i++) {
/* 323 */       dB = 20.0D * Math.log10(Math.abs(shortArray[i] / 32767.0D));
/* 324 */       if ((dB == Double.NEGATIVE_INFINITY) || (dB == NaN.0D)) {
/* 325 */         dB = -90.0D;
/*     */       }
/*     */     }
/* 328 */     float level = (float)dB + 91.0F;
/* 329 */     return level;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static short[] byteToShortArray(byte[] byteArray)
/*     */   {
/* 339 */     short[] shortArray = new short[byteArray.length / 2];
/* 340 */     for (int i = 0; i < shortArray.length; i++) {
/* 341 */       int ub1 = byteArray[(i * 2 + 0)] & 0xFF;
/* 342 */       int ub2 = byteArray[(i * 2 + 1)] & 0xFF;
/* 343 */       shortArray[i] = ((short)((ub2 << 8) + ub1));
/*     */     }
/* 345 */     return shortArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static byte[] shortArrayToByteArray(short[] shortArray)
/*     */   {
/* 355 */     byte[] byteArray = new byte[shortArray.length * 2];
/* 356 */     ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(shortArray);
/* 357 */     return byteArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   synchronized void setMute(boolean mute)
/*     */   {
/* 366 */     this.muted = mute;
/* 367 */     if (this.muted) {
/* 368 */       this.bypass = false;
/* 369 */       this.fadeOut = true;
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isMuted() {
/* 374 */     return this.muted;
/*     */   }
/*     */   
/*     */   public void resetGain() {
/* 378 */     this.gainControl.setValue(this.userVolume);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBypassing()
/*     */   {
/* 386 */     return this.bypass;
/*     */   }
/*     */   
/*     */   public String getSelectedInputLineName() {
/* 390 */     return this.inputLineName;
/*     */   }
/*     */   
/*     */   public String getSelectedOutputLineName() {
/* 394 */     return this.outputLineName;
/*     */   }
/*     */   
/*     */   public void stopRunning() {
/* 398 */     this.run = false;
/*     */   }
/*     */   
/*     */   private synchronized void driftReset() {
/* 402 */     if (System.currentTimeMillis() > this.nextDrift) {
/* 403 */       this.nextDrift = (System.currentTimeMillis() + 1800000L);
/*     */       try {
/* 405 */         this.targetDataLine.open(signedFormat, 8192);
/* 406 */         this.targetDataLine.start();
/*     */       } catch (LineUnavailableException ex) {
/* 408 */         JOptionPane.showMessageDialog(null, "Selected Input Line is currently unavailable", "Line Unavailable Exception", 0);
/*     */       }
/*     */       try
/*     */       {
/* 412 */         this.sourceDataLine.open(signedFormat, 8192);
/* 413 */         this.sourceDataLine.start();
/*     */       } catch (LineUnavailableException ex) {
/* 415 */         JOptionPane.showMessageDialog(null, "Selected Output Line is currently unavailable.", "Line Unavailable Exception", 0);
/*     */       }
/* 417 */       System.out.println("DriftReset");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\soundboard\MicInjector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
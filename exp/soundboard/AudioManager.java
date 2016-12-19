/*     */ package exp.soundboard;
/*     */ 
/*     */ import java.io.File;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ import javax.sound.sampled.DataLine.Info;
/*     */ import javax.sound.sampled.FloatControl;
/*     */ import javax.sound.sampled.FloatControl.Type;
/*     */ import javax.sound.sampled.LineUnavailableException;
/*     */ import javax.sound.sampled.Mixer;
/*     */ import javax.sound.sampled.Mixer.Info;
/*     */ import javax.sound.sampled.SourceDataLine;
/*     */ import javax.swing.JOptionPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AudioManager
/*     */ {
/*     */   private static final int INTERNAL_BUFFER_SIZE = 8192;
/*     */   public final DataLine.Info standardDataLineInfo;
/*     */   Mixer primaryOutput;
/*     */   Mixer secondaryOutput;
/*  27 */   private boolean useSecondary = false;
/*     */   
/*     */   private static float firstOutputGain;
/*     */   private static float secondOutputGain;
/*     */   
/*     */   public AudioManager()
/*     */   {
/*  34 */     this.standardDataLineInfo = new DataLine.Info(SourceDataLine.class, Utils.format, 2048);
/*     */   }
/*     */   
/*     */ 
/*     */   void playSoundClip(File file, boolean halfspeed)
/*     */   {
/*     */     AudioFormat format;
/*     */     
/*     */     AudioFormat format;
/*     */     
/*  44 */     if (halfspeed) {
/*  45 */       format = Utils.modifiedPlaybackFormat;
/*     */     } else {
/*  47 */       format = Utils.format;
/*     */     }
/*  49 */     if ((file.exists()) && (file.canRead())) {
/*  50 */       SourceDataLine primarySpeaker = null;
/*  51 */       SourceDataLine secondarySpeaker = null;
/*     */       try {
/*  53 */         primarySpeaker = (SourceDataLine)this.primaryOutput.getLine(this.standardDataLineInfo);
/*  54 */         primarySpeaker.open(format, 8192);
/*  55 */         FloatControl gain = (FloatControl)primarySpeaker.getControl(FloatControl.Type.MASTER_GAIN);
/*  56 */         gain.setValue(firstOutputGain);
/*  57 */         primarySpeaker.start();
/*     */       } catch (LineUnavailableException ex) {
/*  59 */         JOptionPane.showMessageDialog(null, "Selected Output Line: Primary Speaker is currently unavailable.", "Line Unavailable Exception", 0);
/*     */       }
/*  61 */       if ((this.secondaryOutput != null) && (this.useSecondary)) {
/*     */         try {
/*  63 */           secondarySpeaker = (SourceDataLine)this.secondaryOutput.getLine(this.standardDataLineInfo);
/*  64 */           secondarySpeaker.open(format, 8192);
/*  65 */           FloatControl gain = (FloatControl)secondarySpeaker.getControl(FloatControl.Type.MASTER_GAIN);
/*  66 */           gain.setValue(secondOutputGain);
/*  67 */           secondarySpeaker.start();
/*     */         } catch (LineUnavailableException ex) {
/*  69 */           JOptionPane.showMessageDialog(null, "Selected Output Line: Secondary Speaker is currently unavailable.", "Line Unavailable Exception", 0);
/*     */         }
/*     */       }
/*  72 */       Utils.playNewSoundClipThreaded(file, primarySpeaker, secondarySpeaker);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setPrimaryOutputMixer(String mixerName)
/*     */   {
/*  82 */     String[] mixers = Utils.getMixerNames(this.standardDataLineInfo);
/*  83 */     String[] arrayOfString1; int j = (arrayOfString1 = mixers).length; for (int i = 0; i < j; i++) { String x = arrayOfString1[i];
/*  84 */       Mixer.Info[] arrayOfInfo; int m = (arrayOfInfo = AudioSystem.getMixerInfo()).length; for (int k = 0; k < m; k++) { Mixer.Info mixerInfo = arrayOfInfo[k];
/*  85 */         if (mixerName.equals(mixerInfo.getName())) {
/*  86 */           this.primaryOutput = AudioSystem.getMixer(mixerInfo);
/*  87 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseSecondary(boolean use)
/*     */   {
/*  99 */     this.useSecondary = use;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean useSecondary()
/*     */   {
/* 107 */     return this.useSecondary;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setSecondaryOutputMixer(String mixerName)
/*     */   {
/* 116 */     String[] mixers = Utils.getMixerNames(this.standardDataLineInfo);
/* 117 */     String[] arrayOfString1; int j = (arrayOfString1 = mixers).length; for (int i = 0; i < j; i++) { String x = arrayOfString1[i];
/* 118 */       Mixer.Info[] arrayOfInfo; int m = (arrayOfInfo = AudioSystem.getMixerInfo()).length; for (int k = 0; k < m; k++) { Mixer.Info mixerInfo = arrayOfInfo[k];
/* 119 */         if (mixerName.equals(mixerInfo.getName())) {
/* 120 */           this.secondaryOutput = AudioSystem.getMixer(mixerInfo);
/* 121 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static float getFirstOutputGain() {
/* 128 */     return firstOutputGain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setFirstOutputGain(float firstOutputGain)
/*     */   {
/* 136 */     firstOutputGain = firstOutputGain;
/*     */   }
/*     */   
/*     */   public static float getSecondOutputGain() {
/* 140 */     return secondOutputGain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setSecondOutputGain(float secondOutputGain)
/*     */   {
/* 148 */     secondOutputGain = secondOutputGain;
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\soundboard\AudioManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
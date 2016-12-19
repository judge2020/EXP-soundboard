/*     */ package exp.soundboard;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import org.jnativehook.keyboard.NativeKeyEvent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SoundboardEntry
/*     */ {
/*     */   private String file;
/*     */   public int[] activationKeysNumbers;
/*     */   
/*     */   public SoundboardEntry(File file, int[] keys)
/*     */   {
/*  36 */     Path p = Paths.get(new String(file.getAbsolutePath()), new String[0]);
/*  37 */     this.file = p.toAbsolutePath().toString();
/*  38 */     this.activationKeysNumbers = keys;
/*  39 */     if (this.activationKeysNumbers == null) {
/*  40 */       this.activationKeysNumbers = new int[0];
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean matchesPressed(ArrayList<Integer> pressedKeys)
/*     */   {
/*  52 */     int keysRemaining = this.activationKeysNumbers.length;
/*  53 */     if (keysRemaining == 0)
/*  54 */       return false;
/*     */     int[] arrayOfInt;
/*  56 */     int j = (arrayOfInt = this.activationKeysNumbers).length; for (int i = 0; i < j; i++) { int actkey = arrayOfInt[i];
/*  57 */       for (Iterator localIterator = pressedKeys.iterator(); localIterator.hasNext();) { int presskey = ((Integer)localIterator.next()).intValue();
/*  58 */         if (actkey == presskey) {
/*  59 */           keysRemaining--;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  64 */     if (keysRemaining <= 0) {
/*  65 */       return true;
/*     */     }
/*  67 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int matchesHowManyPressed(ArrayList<Integer> pressedKeys)
/*     */   {
/*  76 */     int matches = 0;
/*  77 */     int j; int i; for (Iterator localIterator = pressedKeys.iterator(); localIterator.hasNext(); 
/*  78 */         i < j)
/*     */     {
/*  77 */       int key = ((Integer)localIterator.next()).intValue();
/*  78 */       int[] arrayOfInt; j = (arrayOfInt = this.activationKeysNumbers).length;i = 0; continue;int hotkey = arrayOfInt[i];
/*  79 */       if (key == hotkey) {
/*  80 */         matches++;
/*     */       }
/*  78 */       i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  84 */     return matches;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void play(AudioManager audio, boolean moddedspeed)
/*     */   {
/*  94 */     File file = toFile();
/*  95 */     audio.playSoundClip(file, moddedspeed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public File toFile()
/*     */   {
/* 103 */     File f = new File(this.file);
/* 104 */     if (!f.exists()) {
/* 105 */       Path p = Paths.get(this.file, new String[0]);
/* 106 */       return p.toFile();
/*     */     }
/* 108 */     return f;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFile(File file)
/*     */   {
/*     */     try
/*     */     {
/* 118 */       this.file = new String(file.getAbsolutePath().getBytes(Utils.fileEncoding));
/*     */     } catch (UnsupportedEncodingException e) {
/* 120 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFileString()
/*     */   {
/* 129 */     return this.file;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFileName()
/*     */   {
/* 137 */     char seperator = File.separatorChar;
/* 138 */     return this.file.substring(this.file.lastIndexOf(seperator) + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] getActivationKeys()
/*     */   {
/* 146 */     return this.activationKeysNumbers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getActivationKeysAsReadableString()
/*     */   {
/* 155 */     String s = "";
/* 156 */     if (this.activationKeysNumbers.length == 0)
/* 157 */       return s;
/*     */     int[] arrayOfInt;
/* 159 */     int j = (arrayOfInt = getActivationKeys()).length; for (int i = 0; i < j; i++) { int i = arrayOfInt[i];
/* 160 */       s = s.concat(NativeKeyEvent.getKeyText(i) + "+");
/*     */     }
/* 162 */     s = s.substring(0, s.length() - 1);
/* 163 */     return s;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setActivationKeys(int[] activationKeys)
/*     */   {
/* 171 */     this.activationKeysNumbers = activationKeys;
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\soundboard\SoundboardEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
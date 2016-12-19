/*     */ package exp.soundboard;
/*     */ 
/*     */ import exp.gui.SettingsFrame;
/*     */ import exp.gui.SoundboardFrame;
/*     */ import java.awt.AWTException;
/*     */ import java.awt.Robot;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.prefs.Preferences;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ import javax.sound.sampled.DataLine.Info;
/*     */ import javax.sound.sampled.Mixer;
/*     */ import javax.sound.sampled.Mixer.Info;
/*     */ import javax.sound.sampled.SourceDataLine;
/*     */ import javax.sound.sampled.UnsupportedAudioFileException;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.jnativehook.GlobalScreen;
/*     */ import org.jnativehook.NativeHookException;
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
/*     */ public class Utils
/*     */ {
/*  42 */   private static ThreadGroup clipPlayerThreadGroup = new ThreadGroup("Clip Player Group");
/*     */   private static final String prefsName = "Expenosa's Soundboard";
/*  44 */   public static final Preferences prefs = Preferences.userRoot().node("Expenosa's Soundboard");
/*  45 */   private static boolean PLAYALL = true;
/*     */   public static final int BUFFERSIZE = 2048;
/*     */   public static final float STANDARDSAMPLERATE = 44100.0F;
/*     */   private static float modifiedPlaybackSpeed;
/*     */   public static final float modifiedSpeedIncrements = 0.05F;
/*     */   public static final float modifiedSpeedMin = 0.1F;
/*     */   public static final float modifiedSpeedMax = 2.0F;
/*  52 */   public static final AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, false);
/*     */   public static AudioFormat modifiedPlaybackFormat;
/*  54 */   public static int stopKey = 19;
/*  55 */   public static int slowKey = 35;
/*  56 */   public static int modspeedupKey = 39;
/*  57 */   public static int modspeeddownKey = 37;
/*  58 */   private static int overlapSwitchKey = 36;
/*  59 */   public static MicInjector micInjector = new MicInjector();
/*     */   private static Robot robot;
/*  61 */   public static boolean autoPTThold = true;
/*  62 */   private static ArrayList<Integer> pttkeys = new ArrayList();
/*  63 */   private static int currentlyPlayingClipCount = 0;
/*     */   
/*  65 */   private static ConcurrentHashMap<String, Long> lastNativeKeyPressMap = new ConcurrentHashMap();
/*  66 */   private static ConcurrentHashMap<String, Long> lastRobotKeyPressMap = new ConcurrentHashMap();
/*     */   
/*  68 */   public static String fileEncoding = System.getProperty("file.encoding");
/*     */   
/*  70 */   public static boolean overlapSameClipWhilePlaying = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void playNewSoundClipThreaded(File file, final SourceDataLine primarySpeaker, final SourceDataLine secondarySpeaker)
/*     */   {
/*  80 */     SwingUtilities.invokeLater(new Runnable()
/*     */     {
/*     */       public void run() {
/*  83 */         Utils.ClipPlayer clip = new Utils.ClipPlayer(Utils.this, primarySpeaker, secondarySpeaker);
/*  84 */         if (!Utils.overlapSameClipWhilePlaying) {
/*  85 */           Utils.stopFilePlaying(Utils.this);
/*     */         }
/*  87 */         clip.start();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] getMixerNames(DataLine.Info lineInfo)
/*     */   {
/*  98 */     ArrayList<String> mixerNames = new ArrayList();
/*  99 */     Mixer.Info[] info = AudioSystem.getMixerInfo();
/* 100 */     Mixer.Info[] arrayOfInfo1; int j = (arrayOfInfo1 = info).length; for (int i = 0; i < j; i++) { Mixer.Info elem = arrayOfInfo1[i];
/* 101 */       Mixer mixer = AudioSystem.getMixer(elem);
/*     */       try {
/* 103 */         if (mixer.isLineSupported(lineInfo)) {
/* 104 */           mixerNames.add(elem.getName());
/*     */         }
/*     */       } catch (NullPointerException e) {
/* 107 */         System.err.println(e);
/*     */       }
/*     */     }
/* 110 */     String[] returnarray = new String[mixerNames.size()];
/* 111 */     return (String[])mixerNames.toArray(returnarray);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void stopAllClips()
/*     */   {
/* 118 */     PLAYALL = false;
/* 119 */     zeroCurrentClipCount();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getStopKey()
/*     */   {
/* 127 */     return stopKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setStopKey(int stopKey)
/*     */   {
/* 135 */     stopKey = stopKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getModifiedSpeedKey()
/*     */   {
/* 143 */     return slowKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setModifiedSpeedKey(int slowKey)
/*     */   {
/* 151 */     slowKey = slowKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void startMicInjector(String inputMixerName, String outputMixerName)
/*     */   {
/* 160 */     boolean inputexists = false;
/* 161 */     boolean outputexists = false;
/* 162 */     if (isMicInjectorRunning())
/* 163 */       stopMicInjector();
/*     */     String[] arrayOfString;
/* 165 */     int j = (arrayOfString = MicInjector.getMixerNames(MicInjector.targetDataLineInfo)).length; for (int i = 0; i < j; i++) { String mixer = arrayOfString[i];
/* 166 */       if (mixer.equals(inputMixerName)) {
/* 167 */         inputexists = true;
/*     */       }
/*     */     }
/* 170 */     j = (arrayOfString = MicInjector.getMixerNames(MicInjector.sourceDataLineInfo)).length; for (i = 0; i < j; i++) { String mixer = arrayOfString[i];
/* 171 */       if (mixer.equals(outputMixerName)) {
/* 172 */         outputexists = true;
/*     */       }
/*     */     }
/* 175 */     if ((inputexists) && (outputexists)) {
/* 176 */       micInjector.setInputMixer(inputMixerName);
/* 177 */       micInjector.setOutputMixer(outputMixerName);
/* 178 */       micInjector.start();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void stopMicInjector()
/*     */   {
/* 186 */     micInjector.stopRunning();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setMicInjectorGain(float level)
/*     */   {
/* 194 */     micInjector.setGain(level);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static float getMicInjectorGain()
/*     */   {
/* 202 */     return MicInjector.getGain();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isMicInjectorRunning()
/*     */   {
/* 210 */     return micInjector.isRunning();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void startMp3Decoder()
/*     */   {
/* 218 */     InputStream loaderfile = ClipPlayer.class.getResourceAsStream("loader.mp3");
/*     */     try {
/* 220 */       AudioSystem.getAudioFileFormat(loaderfile);
/* 221 */       AudioInputStream stream = AudioSystem.getAudioInputStream(loaderfile);
/* 222 */       stream.close();
/*     */     } catch (UnsupportedAudioFileException|IOException e) {
/* 224 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean initGlobalKeyLibrary()
/*     */   {
/*     */     try
/*     */     {
/*     */       
/*     */     }
/*     */     catch (NativeHookException ex)
/*     */     {
/* 242 */       System.err.println("There was a problem registering the native hook.");
/* 243 */       System.err.println(ex.getMessage());
/* 244 */       JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error occured whilst initiating global hotkeys", 
/* 245 */         0);
/*     */     }
/* 247 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean deregisterGlobalKeyLibrary()
/*     */   {
/* 255 */     if (GlobalScreen.isNativeHookRegistered()) {
/* 256 */       GlobalScreen.unregisterNativeHook();
/* 257 */       return true;
/*     */     }
/* 259 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isFileSupported(File file)
/*     */   {
/*     */     try
/*     */     {
/* 270 */       AudioSystem.getAudioFileFormat(file);
/* 271 */       return true;
/*     */     } catch (UnsupportedAudioFileException e) {
/* 273 */       e.printStackTrace();
/*     */     } catch (IOException e) {
/* 275 */       e.printStackTrace();
/*     */     }
/* 277 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized void setModifiedPlaybackSpeed(float speed)
/*     */   {
/* 285 */     modifiedPlaybackSpeed = speed;
/* 286 */     float newSampleRate = 44100.0F * speed;
/* 287 */     modifiedPlaybackFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, newSampleRate, 16, 2, 4, newSampleRate, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized float getModifiedPlaybackSpeed()
/*     */   {
/* 295 */     return modifiedPlaybackSpeed;
/*     */   }
/*     */   
/*     */   public static int getModspeedupKey() {
/* 299 */     return modspeedupKey;
/*     */   }
/*     */   
/*     */   public static void setModspeedupKey(int modspeedupKey) {
/* 303 */     modspeedupKey = modspeedupKey;
/*     */   }
/*     */   
/*     */   public static int getModspeeddownKey() {
/* 307 */     return modspeeddownKey;
/*     */   }
/*     */   
/*     */   public static void setModspeeddownKey(int modspeeddownKey) {
/* 311 */     modspeeddownKey = modspeeddownKey;
/*     */   }
/*     */   
/*     */   public static int getOverlapSwitchKey() {
/* 315 */     return overlapSwitchKey;
/*     */   }
/*     */   
/*     */   public static void setOverlapSwitchKey(int overlapSwitchKey) {
/* 319 */     overlapSwitchKey = overlapSwitchKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void incrementModSpeedUp()
/*     */   {
/* 326 */     float speed = modifiedPlaybackSpeed + 0.05F;
/* 327 */     if (speed > 2.0F) {
/* 328 */       speed = 2.0F;
/*     */     }
/* 330 */     setModifiedPlaybackSpeed(speed);
/* 331 */     if (SettingsFrame.instance != null) {
/* 332 */       SettingsFrame.instance.updateDisplayedModSpeed();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void decrementModSpeedDown()
/*     */   {
/* 340 */     float speed = modifiedPlaybackSpeed - 0.05F;
/* 341 */     if (speed < 0.1F) {
/* 342 */       speed = 0.1F;
/*     */     }
/* 344 */     setModifiedPlaybackSpeed(speed);
/* 345 */     if (SettingsFrame.instance != null) {
/* 346 */       SettingsFrame.instance.updateDisplayedModSpeed();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JFileChooser getFileChooser()
/*     */   {
/* 355 */     return SoundboardFrame.filechooser;
/*     */   }
/*     */   
/*     */   public MicInjector getMicInjector()
/*     */   {
/* 360 */     return micInjector;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Robot getRobotInstance()
/*     */   {
/* 369 */     if (robot != null) {
/* 370 */       return robot;
/*     */     }
/*     */     try {
/* 373 */       robot = new Robot();
/*     */     } catch (AWTException e) {
/* 375 */       e.printStackTrace();
/*     */     }
/* 377 */     if (robot != null) {
/* 378 */       return robot;
/*     */     }
/*     */     
/* 381 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean checkAndUseAutoPPThold()
/*     */   {
/* 389 */     if ((!autoPTThold) || (pttkeys.size() == 0)) {
/* 390 */       return false;
/*     */     }
/* 392 */     if (SoundboardFrame.soundboard.entriesContainPTTKeys(pttkeys)) {
/* 393 */       return false;
/*     */     }
/*     */     
/* 396 */     ArrayList<Integer> pressed = SoundboardFrame.macroListener.getPressedNativeKeys();
/* 397 */     Robot robot = getRobotInstance();
/* 398 */     int noofkeys = pttkeys.size();
/* 399 */     for (int i = 0; i < noofkeys; i++) {
/* 400 */       int key = ((Integer)pttkeys.get(i)).intValue();
/* 401 */       boolean pressedAlready = false;
/* 402 */       for (Integer nativekey : pressed) {
/* 403 */         if (KeyEventIntConverter.getKeyEventText(key).toLowerCase().equals(NativeKeyEvent.getKeyText(nativekey.intValue()).toLowerCase())) {
/* 404 */           pressedAlready = true;
/* 405 */           break;
/*     */         }
/*     */       }
/* 408 */       if (!pressedAlready) {
/* 409 */         robot.keyPress(key);
/* 410 */         submitRobotKeyPressTime(KeyEventIntConverter.getKeyEventText(key));
/* 411 */         System.out.println("Robot pressed: " + KeyEvent.getKeyText(key));
/*     */       }
/*     */     }
/* 414 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean checkAndReleaseHeldPPTKeys()
/*     */   {
/* 422 */     if (!autoPTThold) {
/* 423 */       return false;
/*     */     }
/* 425 */     if (SoundboardFrame.soundboard.entriesContainPTTKeys(pttkeys)) {
/* 426 */       SwingUtilities.invokeLater(new Runnable()
/*     */       {
/*     */         public void run() {
/* 429 */           JOptionPane.showMessageDialog(null, "A soundboard entry is using a key that conflicts with a 'Push to Talk' key. \n Disable 'Auto-hold PTT keys', or edit the entry or PTT keys.", 
/* 430 */             "Alert!", 0);
/*     */         }
/* 432 */       });
/* 433 */       return false;
/*     */     }
/*     */     
/* 436 */     if (currentlyPlayingClipCount == 0) {
/* 437 */       Robot robot = getRobotInstance();
/* 438 */       for (Integer i : pttkeys) {
/* 439 */         if (wasKeyLastPressedByRobot(KeyEventIntConverter.getKeyEventText(i.intValue()))) {
/* 440 */           robot.keyRelease(i.intValue());
/* 441 */           System.out.println("Robot released: " + KeyEvent.getKeyText(i.intValue()));
/*     */         }
/*     */       }
/*     */     }
/* 445 */     return true;
/*     */   }
/*     */   
/*     */   public static ArrayList<Integer> getPTTkeys()
/*     */   {
/* 450 */     return pttkeys;
/*     */   }
/*     */   
/*     */   public static void setPTTkeys(Collection<Integer> pTTkeys)
/*     */   {
/* 455 */     pttkeys = new ArrayList(pTTkeys);
/*     */   }
/*     */   
/*     */   public static boolean isAutoPTThold()
/*     */   {
/* 460 */     return autoPTThold;
/*     */   }
/*     */   
/*     */   public static void setAutoPTThold(boolean autoPTThold)
/*     */   {
/* 465 */     autoPTThold = autoPTThold;
/*     */   }
/*     */   
/*     */   public static synchronized void incrementCurrentClipCount() {
/* 469 */     currentlyPlayingClipCount += 1;
/*     */   }
/*     */   
/*     */   public static synchronized void decrementCurrentClipCount() {
/* 473 */     if (currentlyPlayingClipCount >= 1) {
/* 474 */       currentlyPlayingClipCount -= 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public static synchronized void zeroCurrentClipCount() {
/* 479 */     currentlyPlayingClipCount = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ArrayList<Integer> stringToIntArrayList(String string)
/*     */   {
/* 488 */     String arrayString = string.replace('[', ' ').replace(']', ' ').trim();
/* 489 */     ArrayList<Integer> array = new ArrayList();
/* 490 */     String[] numberstring = arrayString.split(",");
/* 491 */     String[] arrayOfString1; int j = (arrayOfString1 = numberstring).length; for (int i = 0; i < j; i++) { String s = arrayOfString1[i];
/* 492 */       if (!s.equals("")) {
/* 493 */         int i = Integer.parseInt(s.trim());
/* 494 */         array.add(Integer.valueOf(i));
/*     */       }
/*     */     }
/* 497 */     return array;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void submitNativeKeyPressTime(String key, long time)
/*     */   {
/* 505 */     lastNativeKeyPressMap.put(key.toLowerCase(), Long.valueOf(time));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void submitRobotKeyPressTime(String key)
/*     */   {
/* 514 */     long time = System.currentTimeMillis();
/* 515 */     lastNativeKeyPressMap.put(key.toLowerCase(), Long.valueOf(time));
/* 516 */     lastRobotKeyPressMap.put(key.toLowerCase(), Long.valueOf(time));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long getLastNativeKeyPressTimeForKey(String keyname)
/*     */   {
/* 526 */     Long time = (Long)lastNativeKeyPressMap.get(keyname.toLowerCase());
/* 527 */     if (time == null) {
/* 528 */       return 0L;
/*     */     }
/* 530 */     return time.longValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long getLastRobotKeyPressTimeForKey(String keyname)
/*     */   {
/* 540 */     Long time = (Long)lastRobotKeyPressMap.get(keyname.toLowerCase());
/* 541 */     if (time == null) {
/* 542 */       return 0L;
/*     */     }
/* 544 */     return time.longValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean wasKeyLastPressedByRobot(String keyname)
/*     */   {
/* 555 */     long human = getLastNativeKeyPressTimeForKey(keyname);
/* 556 */     long robot = getLastRobotKeyPressTimeForKey(keyname);
/* 557 */     if (robot == human) {
/* 558 */       return true;
/*     */     }
/* 560 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isOverlapSameClipWhilePlaying()
/*     */   {
/* 568 */     return overlapSameClipWhilePlaying;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setOverlapSameClipWhilePlaying(boolean overlap)
/*     */   {
/* 577 */     overlapSameClipWhilePlaying = overlap;
/* 578 */     if (SettingsFrame.instance != null) {
/* 579 */       SettingsFrame.instance.updateOverlapSwitchCheckBox();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean stopFilePlaying(File file)
/*     */   {
/* 588 */     boolean stopped = false;
/* 589 */     String filepath = file.toString();
/* 590 */     Thread[] threads = new Thread[clipPlayerThreadGroup.activeCount()];
/* 591 */     clipPlayerThreadGroup.enumerate(threads);
/*     */     
/* 593 */     System.out.println("Thread count: " + threads.length);
/* 594 */     System.out.println("Thread groups: " + clipPlayerThreadGroup.activeGroupCount());
/* 595 */     System.out.println("Requesting: " + filepath + " to stop");
/*     */     Thread[] arrayOfThread1;
/* 597 */     int j = (arrayOfThread1 = threads).length; for (int i = 0; i < j; i++) { Thread thread = arrayOfThread1[i];
/* 598 */       System.out.println("thread name: " + thread.getName());
/* 599 */       if (filepath.equals(thread.getName())) {
/* 600 */         ClipPlayer cp = (ClipPlayer)thread;
/* 601 */         cp.stopPlaying();
/* 602 */         stopped = true;
/*     */       }
/*     */     }
/* 605 */     return stopped;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ClipPlayer
/*     */     extends Thread
/*     */   {
/*     */     File file;
/*     */     
/*     */ 
/* 616 */     SourceDataLine primarySpeaker = null;
/* 617 */     SourceDataLine secondarySpeaker = null;
/* 618 */     boolean playing = true;
/*     */     
/*     */     public ClipPlayer(File file, SourceDataLine primarySpeaker, SourceDataLine secondarySpeaker) {
/* 621 */       super(file.toString());
/* 622 */       this.file = file;
/* 623 */       this.primarySpeaker = primarySpeaker;
/* 624 */       this.secondarySpeaker = secondarySpeaker;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 629 */       playSoundClip(this.file, this.primarySpeaker, this.secondarySpeaker);
/*     */     }
/*     */     
/*     */     public void stopPlaying() {
/* 633 */       System.out.println("Stopping clip: " + this.file.getName());
/* 634 */       this.playing = false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void playSoundClip(File file, SourceDataLine primarySpeaker, SourceDataLine secondarySpeaker)
/*     */     {
/* 644 */       Utils.PLAYALL = true;
/* 645 */       AudioInputStream clip = null;
/* 646 */       AudioFormat clipformat = null;
/*     */       try {
/* 648 */         clip = AudioSystem.getAudioInputStream(file);
/* 649 */         clipformat = clip.getFormat();
/* 650 */         if (!clipformat.equals(Utils.format)) {
/* 651 */           clip = AudioSystem.getAudioInputStream(Utils.format, clip);
/*     */         }
/*     */       } catch (UnsupportedAudioFileException e) {
/* 654 */         e.printStackTrace();
/* 655 */         JOptionPane.showMessageDialog(null, file.getName() + " uses an unsupported format.", "Unsupported Format", 0);
/*     */       } catch (IOException e) {
/* 657 */         e.printStackTrace();
/*     */       }
/* 659 */       if (clip != null) {
/* 660 */         Utils.incrementCurrentClipCount();
/* 661 */         byte[] buffer = new byte['ࠀ'];
/* 662 */         int bytesRead = 0;
/* 663 */         while ((this.playing) && (Utils.PLAYALL)) {
/*     */           try {
/* 665 */             bytesRead = clip.read(buffer, 0, 2048);
/*     */           } catch (IOException e) {
/* 667 */             e.printStackTrace();
/*     */           }
/* 669 */           Utils.checkAndUseAutoPPThold();
/* 670 */           if (bytesRead > 0) {
/* 671 */             primarySpeaker.write(buffer, 0, bytesRead);
/* 672 */             if (secondarySpeaker != null) {
/* 673 */               secondarySpeaker.write(buffer, 0, bytesRead);
/*     */             }
/*     */           }
/* 676 */           if (bytesRead < 2048) {
/* 677 */             this.playing = false;
/*     */           }
/*     */         }
/* 680 */         Utils.decrementCurrentClipCount();
/* 681 */         Utils.checkAndReleaseHeldPPTKeys();
/*     */       }
/* 683 */       if (clip != null) {
/*     */         try {
/* 685 */           clip.close();
/*     */         } catch (IOException e) {
/* 687 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 690 */       primarySpeaker.close();
/* 691 */       if (secondarySpeaker != null) {
/* 692 */         secondarySpeaker.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\soundboard\Utils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
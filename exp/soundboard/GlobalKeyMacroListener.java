/*     */ package exp.soundboard;
/*     */ 
/*     */ import exp.gui.SoundboardFrame;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import org.jnativehook.keyboard.NativeKeyEvent;
/*     */ import org.jnativehook.keyboard.NativeKeyListener;
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
/*     */ public class GlobalKeyMacroListener
/*     */   implements NativeKeyListener
/*     */ {
/*     */   SoundboardFrame soundboardFrame;
/*     */   ArrayList<Integer> pressedKeys;
/*     */   
/*     */   public GlobalKeyMacroListener(SoundboardFrame frame)
/*     */   {
/*  26 */     this.soundboardFrame = frame;
/*  27 */     this.pressedKeys = new ArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void nativeKeyPressed(NativeKeyEvent e)
/*     */   {
/*  37 */     int pressed = e.getKeyCode();
/*  38 */     Utils.submitNativeKeyPressTime(NativeKeyEvent.getKeyText(pressed), e.getWhen());
/*  39 */     boolean alreadyPressed = false;
/*  40 */     for (Iterator localIterator = this.pressedKeys.iterator(); localIterator.hasNext();) { int i = ((Integer)localIterator.next()).intValue();
/*  41 */       if (pressed == i) {
/*  42 */         alreadyPressed = true;
/*  43 */         break;
/*     */       }
/*     */     }
/*  46 */     if (!alreadyPressed) {
/*  47 */       this.pressedKeys.add(Integer.valueOf(pressed));
/*     */     }
/*  49 */     if (pressed == Utils.stopKey) {
/*  50 */       Utils.stopAllClips();
/*  51 */     } else if (pressed == Utils.modspeedupKey) {
/*  52 */       Utils.incrementModSpeedUp();
/*  53 */     } else if (pressed == Utils.modspeeddownKey) {
/*  54 */       Utils.decrementModSpeedDown();
/*  55 */     } else if (pressed == Utils.getOverlapSwitchKey()) {
/*  56 */       boolean overlap = Utils.isOverlapSameClipWhilePlaying();
/*  57 */       Utils.setOverlapSameClipWhilePlaying(!overlap);
/*     */     }
/*  59 */     checkMacros();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void nativeKeyReleased(NativeKeyEvent e)
/*     */   {
/*  67 */     int released = e.getKeyCode();
/*  68 */     for (int i = 0; i < this.pressedKeys.size(); i++) {
/*  69 */       if (released == ((Integer)this.pressedKeys.get(i)).intValue()) {
/*  70 */         this.pressedKeys.remove(i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void nativeKeyTyped(NativeKeyEvent arg0) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSpeedModKeyHeld()
/*     */   {
/*  89 */     for (Iterator localIterator = this.pressedKeys.iterator(); localIterator.hasNext();) { int key = ((Integer)localIterator.next()).intValue();
/*  90 */       if (key == Utils.slowKey) {
/*  91 */         return true;
/*     */       }
/*     */     }
/*  94 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<Integer> getPressedNativeKeys()
/*     */   {
/* 102 */     ArrayList<Integer> array = new ArrayList();
/* 103 */     for (Integer i : this.pressedKeys) {
/* 104 */       array.add(new Integer(i.intValue()));
/*     */     }
/* 106 */     return array;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkMacros()
/*     */   {
/* 114 */     boolean modspeed = false;
/* 115 */     if (isSpeedModKeyHeld()) {
/* 116 */       modspeed = true;
/*     */     }
/* 118 */     ArrayList<SoundboardEntry> potential = new ArrayList();
/* 119 */     for (SoundboardEntry entry : SoundboardFrame.soundboard.getSoundboardEntries()) {
/* 120 */       int[] actKeys = entry.getActivationKeys();
/* 121 */       if ((actKeys.length > 0) && 
/* 122 */         (entry.matchesPressed(this.pressedKeys))) {
/* 123 */         potential.add(entry);
/*     */       }
/*     */     }
/*     */     
/* 127 */     if (potential.size() == 1) {
/* 128 */       ((SoundboardEntry)potential.get(0)).play(this.soundboardFrame.audioManager, modspeed);
/*     */     } else {
/* 130 */       int highest = 0;
/* 131 */       Object potentialCopy = new ArrayList(potential);
/* 132 */       for (SoundboardEntry p : (ArrayList)potentialCopy) {
/* 133 */         int matches = p.matchesHowManyPressed(this.pressedKeys);
/* 134 */         if (matches > highest) {
/* 135 */           highest = matches;
/* 136 */         } else if (matches < highest) {
/* 137 */           potential.remove(p);
/*     */         }
/*     */       }
/* 140 */       potentialCopy = new ArrayList(potential);
/* 141 */       for (SoundboardEntry p : (ArrayList)potentialCopy) {
/* 142 */         int matches = p.matchesHowManyPressed(this.pressedKeys);
/* 143 */         if (matches < highest) {
/* 144 */           potential.remove(p);
/*     */         }
/*     */       }
/*     */       
/* 148 */       for (SoundboardEntry p : potential) {
/* 149 */         p.play(this.soundboardFrame.audioManager, modspeed);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\soundboard\GlobalKeyMacroListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
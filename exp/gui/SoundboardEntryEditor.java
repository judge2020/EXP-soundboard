/*     */ package exp.gui;
/*     */ 
/*     */ import exp.soundboard.Soundboard;
/*     */ import exp.soundboard.SoundboardEntry;
/*     */ import exp.soundboard.Utils;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.GroupLayout;
/*     */ import javax.swing.GroupLayout.Alignment;
/*     */ import javax.swing.GroupLayout.ParallelGroup;
/*     */ import javax.swing.GroupLayout.SequentialGroup;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.LayoutStyle.ComponentPlacement;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import org.jnativehook.GlobalScreen;
/*     */ import org.jnativehook.keyboard.NativeKeyEvent;
/*     */ import org.jnativehook.keyboard.NativeKeyListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SoundboardEntryEditor
/*     */   extends JFrame
/*     */ {
/*     */   private static final long serialVersionUID = -8420285054567246768L;
/*     */   private JTextField keysTextField;
/*     */   private NativeKeyInputGetter inputGetter;
/*     */   SoundboardFrame soundboardframe;
/*     */   Soundboard soundboard;
/*  44 */   SoundboardEntry soundboardEntry = null;
/*     */   
/*     */ 
/*     */ 
/*     */   File soundfile;
/*     */   
/*     */ 
/*     */   public int[] keyNums;
/*     */   
/*     */ 
/*     */   private JLabel selectedSoundClipLabel;
/*     */   
/*     */ 
/*     */ 
/*     */   public SoundboardEntryEditor(SoundboardFrame soundboardframe)
/*     */   {
/*  60 */     this.soundboardframe = soundboardframe;
/*  61 */     this.soundboard = SoundboardFrame.soundboard;
/*  62 */     this.inputGetter = new NativeKeyInputGetter(null);
/*     */     
/*  64 */     setDefaultCloseOperation(2);
/*  65 */     setTitle("Soundboard Entry Editor");
/*  66 */     setIconImage(SoundboardFrame.icon);
/*     */     
/*  68 */     JLabel lblSoundClip = new JLabel("Sound clip:");
/*     */     
/*  70 */     this.selectedSoundClipLabel = new JLabel("None selected");
/*     */     
/*  72 */     JButton btnSelect = new JButton("Select");
/*  73 */     btnSelect.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*  75 */         JFileChooser filechooser = Utils.getFileChooser();
/*  76 */         filechooser.setMultiSelectionEnabled(true);
/*  77 */         filechooser.setFileFilter(new SoundboardEntryEditor.AudioClipFileFilter(SoundboardEntryEditor.this, null));
/*  78 */         int session = filechooser.showDialog(null, "Select");
/*  79 */         if (session == 0) {
/*  80 */           File[] selected = filechooser.getSelectedFiles();
/*  81 */           if (selected.length > 1) {
/*  82 */             SoundboardEntryEditor.this.multiAdd(selected);
/*     */           } else {
/*  84 */             SoundboardEntryEditor.this.soundfile = selected[0];
/*     */           }
/*  86 */           filechooser.setMultiSelectionEnabled(false);
/*  87 */           if (Utils.isFileSupported(SoundboardEntryEditor.this.soundfile)) {
/*  88 */             SoundboardEntryEditor.this.selectedSoundClipLabel.setText(SoundboardEntryEditor.this.soundfile.getAbsolutePath());
/*     */           } else {
/*  90 */             SoundboardEntryEditor.this.soundfile = null;
/*  91 */             JOptionPane.showMessageDialog(null, SoundboardEntryEditor.this.soundfile.getName() + " uses an unsupported codec format.", "Unsupported Format", 0);
/*     */           }
/*     */         }
/*  94 */         filechooser.setMultiSelectionEnabled(false);
/*  95 */         SoundboardEntryEditor.this.pack();
/*     */       }
/*     */       
/*  98 */     });
/*  99 */     JSeparator separator = new JSeparator();
/*     */     
/* 101 */     JLabel lblMacroKeys = new JLabel("HotKeys:");
/*     */     
/* 103 */     this.keysTextField = new JTextField();
/* 104 */     this.keysTextField.addMouseListener(new MouseAdapter()
/*     */     {
/*     */       public void mousePressed(MouseEvent e) {
/* 107 */         if (e.getButton() == 1) {
/* 108 */           SoundboardEntryEditor.this.keysTextField.setBackground(Color.CYAN);
/* 109 */           GlobalScreen.getInstance().addNativeKeyListener(SoundboardEntryEditor.this.inputGetter);
/* 110 */           SoundboardEntryEditor.this.inputGetter.clearPressedKeys();
/* 111 */         } else if (e.getButton() == 3) {
/* 112 */           SoundboardEntryEditor.this.keysTextField.setBackground(Color.WHITE);
/* 113 */           GlobalScreen.getInstance().removeNativeKeyListener(SoundboardEntryEditor.this.inputGetter);
/* 114 */           SoundboardEntryEditor.this.inputGetter.clearPressedKeys();
/* 115 */           SoundboardEntryEditor.this.keyNums = new int[0];
/* 116 */           SoundboardEntryEditor.this.keysTextField.setText("none");
/*     */         }
/*     */       }
/* 119 */     });
/* 120 */     this.keysTextField.setText("none");
/* 121 */     this.keysTextField.setEditable(false);
/* 122 */     this.keysTextField.setColumns(10);
/*     */     
/* 124 */     JButton btnDone = new JButton("Done");
/* 125 */     btnDone.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 127 */         SoundboardEntryEditor.this.submit();
/*     */       }
/*     */       
/* 130 */     });
/* 131 */     JLabel lblRightclickTo = new JLabel("* Right-click to clear hotkeys");
/* 132 */     GroupLayout groupLayout = new GroupLayout(getContentPane());
/* 133 */     groupLayout.setHorizontalGroup(
/* 134 */       groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
/* 135 */       .addGroup(groupLayout.createSequentialGroup()
/* 136 */       .addContainerGap()
/* 137 */       .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
/* 138 */       .addComponent(separator, -1, 414, 32767)
/* 139 */       .addComponent(this.selectedSoundClipLabel, -1, 414, 32767)
/* 140 */       .addGroup(groupLayout.createSequentialGroup()
/* 141 */       .addComponent(lblSoundClip)
/* 142 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
/* 143 */       .addComponent(btnSelect))
/* 144 */       .addComponent(lblMacroKeys)
/* 145 */       .addComponent(this.keysTextField, -1, 414, 32767)
/* 146 */       .addGroup(GroupLayout.Alignment.TRAILING, groupLayout.createSequentialGroup()
/* 147 */       .addComponent(lblRightclickTo)
/* 148 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 311, 32767)
/* 149 */       .addComponent(btnDone)))
/* 150 */       .addContainerGap()));
/*     */     
/* 152 */     groupLayout.setVerticalGroup(
/* 153 */       groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
/* 154 */       .addGroup(groupLayout.createSequentialGroup()
/* 155 */       .addContainerGap()
/* 156 */       .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
/* 157 */       .addComponent(lblSoundClip)
/* 158 */       .addComponent(btnSelect))
/* 159 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
/* 160 */       .addComponent(this.selectedSoundClipLabel)
/* 161 */       .addGap(13)
/* 162 */       .addComponent(separator, -2, -1, -2)
/* 163 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
/* 164 */       .addComponent(lblMacroKeys)
/* 165 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
/* 166 */       .addComponent(this.keysTextField, -2, -1, -2)
/* 167 */       .addGap(19)
/* 168 */       .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
/* 169 */       .addComponent(btnDone)
/* 170 */       .addComponent(lblRightclickTo))
/* 171 */       .addContainerGap(-1, 32767)));
/*     */     
/* 173 */     getContentPane().setLayout(groupLayout);
/* 174 */     getContentPane().addMouseListener(new MouseAdapter()
/*     */     {
/*     */       public void mouseClicked(MouseEvent arg0) {
/* 177 */         SoundboardEntryEditor.this.keysTextField.setBackground(Color.WHITE);
/* 178 */         GlobalScreen.getInstance().removeNativeKeyListener(SoundboardEntryEditor.this.inputGetter);
/*     */       }
/* 180 */     });
/* 181 */     pack();
/* 182 */     setLocationRelativeTo(soundboardframe);
/* 183 */     setVisible(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SoundboardEntryEditor(SoundboardFrame soundboardframe, SoundboardEntry entry)
/*     */   {
/* 192 */     this(soundboardframe);
/*     */     
/* 194 */     this.soundfile = new File(entry.getFileString());
/* 195 */     this.keyNums = entry.activationKeysNumbers;
/* 196 */     this.selectedSoundClipLabel.setText(entry.getFileString());
/* 197 */     this.keysTextField.setText(entry.getActivationKeysAsReadableString());
/* 198 */     pack();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void submit()
/*     */   {
/* 205 */     if (this.soundfile != null) {
/* 206 */       if (this.soundboardEntry == null) {
/* 207 */         this.soundboard.addEntry(this.soundfile, this.keyNums);
/* 208 */         this.soundboardframe.updateSoundboardTable();
/*     */       } else {
/* 210 */         this.soundboardEntry.setFile(this.soundfile);
/* 211 */         this.soundboardEntry.setActivationKeys(this.keyNums);
/* 212 */         this.soundboardframe.updateSoundboardTable();
/*     */       }
/*     */     }
/* 215 */     dispose();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void multiAdd(File[] files)
/*     */   {
/*     */     File[] arrayOfFile;
/*     */     
/* 224 */     int j = (arrayOfFile = files).length; for (int i = 0; i < j; i++) { File file = arrayOfFile[i];
/* 225 */       this.soundboard.addEntry(file, null);
/*     */     }
/* 227 */     this.soundboardframe.updateSoundboardTable();
/* 228 */     dispose();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 236 */     super.dispose();
/* 237 */     GlobalScreen.getInstance().removeNativeKeyListener(this.inputGetter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private class NativeKeyInputGetter
/*     */     implements NativeKeyListener
/*     */   {
/* 247 */     int pressedKeys = 0;
/* 248 */     ArrayList<Integer> pressedKeyNums = new ArrayList();
/* 249 */     ArrayList<String> pressedKeyNames = new ArrayList();
/*     */     
/*     */     private NativeKeyInputGetter() {}
/*     */     
/* 253 */     public void nativeKeyPressed(NativeKeyEvent e) { if (this.pressedKeys <= 0) {
/* 254 */         this.pressedKeyNames.clear();
/* 255 */         this.pressedKeyNums.clear();
/*     */       }
/* 257 */       this.pressedKeys += 1;
/* 258 */       int key = e.getKeyCode();
/* 259 */       String keyname = NativeKeyEvent.getKeyText(key);
/* 260 */       System.out.println("key pressed: " + key + " " + keyname);
/* 261 */       for (Integer i : this.pressedKeyNums) {
/* 262 */         if (i.intValue() == key) {
/* 263 */           return;
/*     */         }
/*     */       }
/* 266 */       this.pressedKeyNums.add(Integer.valueOf(key));
/* 267 */       this.pressedKeyNames.add(keyname);
/* 268 */       updateTextField();
/* 269 */       int[] macroKeys = new int[this.pressedKeyNums.size()];
/* 270 */       for (int i = 0; i < macroKeys.length; i++) {
/* 271 */         macroKeys[i] = ((Integer)this.pressedKeyNums.get(i)).intValue();
/*     */       }
/* 273 */       SoundboardEntryEditor.this.keyNums = macroKeys;
/*     */     }
/*     */     
/*     */     public void nativeKeyReleased(NativeKeyEvent e)
/*     */     {
/* 278 */       this.pressedKeys -= 1;
/* 279 */       if (this.pressedKeys < 0) {
/* 280 */         this.pressedKeys = 0;
/*     */       }
/* 282 */       int key = e.getKeyCode();
/* 283 */       this.pressedKeyNums.remove(new Integer(key));
/* 284 */       this.pressedKeyNames.remove(NativeKeyEvent.getKeyText(key));
/*     */     }
/*     */     
/*     */ 
/*     */     public void nativeKeyTyped(NativeKeyEvent arg0) {}
/*     */     
/*     */     public void clearPressedKeys()
/*     */     {
/* 292 */       this.pressedKeys = 0;
/* 293 */       this.pressedKeyNames.clear();
/* 294 */       this.pressedKeyNums.clear();
/*     */     }
/*     */     
/*     */     private synchronized void updateTextField() {
/* 298 */       String allKeys = "";
/* 299 */       for (String key : this.pressedKeyNames) {
/* 300 */         allKeys = allKeys.concat(key + "+");
/*     */       }
/* 302 */       allKeys = allKeys.substring(0, allKeys.length() - 1);
/* 303 */       SoundboardEntryEditor.this.keysTextField.setText(allKeys);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class AudioClipFileFilter
/*     */     extends FileFilter
/*     */   {
/*     */     private AudioClipFileFilter() {}
/*     */     
/*     */ 
/*     */     public boolean accept(File file)
/*     */     {
/* 317 */       if (file.isDirectory()) {
/* 318 */         return true;
/*     */       }
/* 320 */       String filename = file.getName().toLowerCase();
/* 321 */       if ((filename.endsWith(".wav")) || (filename.endsWith(".mp3"))) {
/* 322 */         return true;
/*     */       }
/* 324 */       return false;
/*     */     }
/*     */     
/*     */     public String getDescription()
/*     */     {
/* 329 */       return ".mp3 or uncompressed .wav";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\gui\SoundboardEntryEditor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
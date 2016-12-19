/*     */ package exp.gui;
/*     */ 
/*     */ import exp.soundboard.MicInjector;
/*     */ import exp.soundboard.UpdateChecker;
/*     */ import exp.soundboard.Utils;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Desktop;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowFocusListener;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFormattedTextField;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.JSpinner;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SpinnerNumberModel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.text.DefaultFormatter;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ import org.jnativehook.GlobalScreen;
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
/*     */ public class SettingsFrame
/*     */   extends JFrame
/*     */ {
/*  59 */   public static SettingsFrame instance = null;
/*     */   private static final long serialVersionUID = -4758092886690912749L;
/*     */   private JTextField stopAllTextField;
/*     */   private StopKeyNativeKeyInputGetter stopKeyInputGetter;
/*     */   private ModSpeedKeyNativeKeyInputGetter slowKeyInputGetter;
/*     */   private IncKeyNativeKeyInputGetter incKeyInputGetter;
/*     */   private DecKeyNativeKeyInputGetter decKeyInputGetter;
/*     */   private PttKeysNativeKeyInputGetter pttKeysInputGetter;
/*     */   private OverlapSwitchNativeKeyInputGetter fOverlapKeyInputGetter;
/*     */   private JComboBox<String> micComboBox;
/*     */   private JComboBox<String> vacComboBox;
/*     */   private JTextField slowKeyTextField;
/*     */   private JSpinner modSpeedSpinner;
/*     */   private JTextField incModSpeedHotKeyTextField;
/*     */   private JTextField decModSpeedHotKeyTextField;
/*     */   private JTextField pttKeysTextField;
/*     */   private JCheckBox fOverlapClipsCheckbox;
/*     */   private JTextField fOverlapHotkeyTextField;
/*     */   
/*     */   private SettingsFrame() {
/*  79 */     getContentPane().addMouseListener(new MouseAdapter()
/*     */     {
/*     */       public void mousePressed(MouseEvent arg0) {
/*  82 */         SettingsFrame.this.focusLostOnItems();
/*     */       }
/*  84 */     });
/*  85 */     addWindowFocusListener(new WindowFocusListener() {
/*     */       public void windowGainedFocus(WindowEvent arg0) {}
/*     */       
/*     */       public void windowLostFocus(WindowEvent arg0) {
/*  89 */         SettingsFrame.this.focusLostOnItems();
/*     */       }
/*  91 */     });
/*  92 */     setResizable(false);
/*     */     
/*  94 */     this.stopKeyInputGetter = new StopKeyNativeKeyInputGetter(null);
/*  95 */     this.slowKeyInputGetter = new ModSpeedKeyNativeKeyInputGetter(null);
/*  96 */     this.incKeyInputGetter = new IncKeyNativeKeyInputGetter(null);
/*  97 */     this.decKeyInputGetter = new DecKeyNativeKeyInputGetter(null);
/*  98 */     this.pttKeysInputGetter = new PttKeysNativeKeyInputGetter(null);
/*  99 */     this.fOverlapKeyInputGetter = new OverlapSwitchNativeKeyInputGetter(null);
/*     */     
/* 101 */     setDefaultCloseOperation(2);
/* 102 */     setTitle("Settings");
/*     */     
/* 104 */     JLabel lblstopAllSounds = new JLabel("'Stop All Sounds' hotkey:");
/* 105 */     lblstopAllSounds.setFont(new Font("Tahoma", 1, 11));
/*     */     
/* 107 */     this.stopAllTextField = new JTextField();
/* 108 */     this.stopAllTextField.addFocusListener(new FocusAdapter()
/*     */     {
/*     */       public void focusLost(FocusEvent arg0) {
/* 111 */         GlobalScreen.getInstance().removeNativeKeyListener(SettingsFrame.this.stopKeyInputGetter);
/* 112 */         SettingsFrame.this.stopAllTextField.setBackground(Color.WHITE);
/*     */       }
/* 114 */     });
/* 115 */     this.stopAllTextField.setEditable(false);
/* 116 */     this.stopAllTextField.addMouseListener(new MouseAdapter()
/*     */     {
/*     */       public void mousePressed(MouseEvent arg0) {
/* 119 */         SettingsFrame.this.stopAllTextField.setBackground(Color.CYAN);
/* 120 */         GlobalScreen.getInstance().addNativeKeyListener(SettingsFrame.this.stopKeyInputGetter);
/*     */       }
/* 122 */     });
/* 123 */     this.stopAllTextField.setColumns(10);
/*     */     
/* 125 */     final JCheckBox chckbxCheckForUpdate = new JCheckBox("Check for update on launch.");
/* 126 */     chckbxCheckForUpdate.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 128 */         SoundboardFrame.updateCheck = !SoundboardFrame.updateCheck;
/* 129 */         chckbxCheckForUpdate.setSelected(SoundboardFrame.updateCheck);
/*     */       }
/* 131 */     });
/* 132 */     chckbxCheckForUpdate.setSelected(SoundboardFrame.updateCheck);
/* 133 */     final JButton btnCheckForUpdate = new JButton("Check for Update");
/* 134 */     btnCheckForUpdate.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 136 */         if (UpdateChecker.isUpdateAvailable()) {
/* 137 */           SwingUtilities.invokeLater(new UpdateChecker());
/*     */         } else {
/* 139 */           btnCheckForUpdate.setText("No Updates");
/*     */         }
/*     */         
/*     */       }
/* 143 */     });
/* 144 */     JLabel lblExpenosa = new JLabel(" © Expenosa. 2014.");
/*     */     
/* 146 */     JButton btnProjectWebsite = new JButton("Project Website");
/* 147 */     btnProjectWebsite.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/* 150 */           Desktop.getDesktop().browse(new URI("https://sourceforge.net/projects/expsoundboard/"));
/*     */         } catch (IOException e1) {
/* 152 */           e1.printStackTrace();
/*     */         } catch (URISyntaxException e1) {
/* 154 */           e1.printStackTrace();
/*     */         }
/*     */         
/*     */       }
/* 158 */     });
/* 159 */     JSeparator separator = new JSeparator();
/* 160 */     separator.setForeground(Color.BLACK);
/*     */     
/* 162 */     JSeparator separator_1 = new JSeparator();
/* 163 */     separator_1.setForeground(Color.BLACK);
/*     */     
/* 165 */     JLabel lblMicInjectorSettings = new JLabel("Mic Injector settings:");
/* 166 */     lblMicInjectorSettings.setForeground(Color.RED);
/*     */     
/* 168 */     JLabel lblMicrophone = new JLabel("Microphone:");
/*     */     
/* 170 */     this.micComboBox = new JComboBox();
/*     */     
/*     */ 
/* 173 */     JLabel lblVirtualAudioCable = new JLabel("Virtual Audio Cable:");
/*     */     
/* 175 */     this.vacComboBox = new JComboBox();
/*     */     
/* 177 */     JLabel lblUseMicInjector = new JLabel("*Use Mic Injector when your using a virtual audio cable as your input on other software.");
/* 178 */     lblUseMicInjector.setFont(new Font("Tahoma", 2, 10));
/*     */     
/* 180 */     JLabel lblVersion = new JLabel("Version: 0.5");
/*     */     
/* 182 */     JLabel lblhalfSpeedPlayback = new JLabel("'Modified playback speed' combo key:");
/*     */     
/* 184 */     this.slowKeyTextField = new JTextField();
/* 185 */     this.slowKeyTextField.addFocusListener(new FocusAdapter()
/*     */     {
/*     */       public void focusLost(FocusEvent e) {
/* 188 */         GlobalScreen.getInstance().removeNativeKeyListener(SettingsFrame.this.slowKeyInputGetter);
/* 189 */         SettingsFrame.this.slowKeyTextField.setBackground(Color.WHITE);
/*     */       }
/* 191 */     });
/* 192 */     this.slowKeyTextField.setEditable(false);
/* 193 */     this.slowKeyTextField.addMouseListener(new MouseAdapter()
/*     */     {
/*     */       public void mousePressed(MouseEvent arg0) {
/* 196 */         SettingsFrame.this.slowKeyTextField.setBackground(Color.CYAN);
/* 197 */         GlobalScreen.getInstance().addNativeKeyListener(SettingsFrame.this.slowKeyInputGetter);
/*     */       }
/* 199 */     });
/* 200 */     this.slowKeyTextField.setColumns(10);
/*     */     
/* 202 */     JLabel lblModifiedPlaybackSpeed = new JLabel("Modified playback speed multiplier:");
/*     */     
/* 204 */     this.modSpeedSpinner = new JSpinner();
/* 205 */     this.modSpeedSpinner.setModel(new SpinnerNumberModel(new Float(Utils.getModifiedPlaybackSpeed()), 
/* 206 */       new Float(0.1F), new Float(2.0F), new Float(0.05F)));
/* 207 */     JComponent comp = this.modSpeedSpinner.getEditor();
/* 208 */     JFormattedTextField field = (JFormattedTextField)comp.getComponent(0);
/* 209 */     field.setEditable(false);
/* 210 */     DefaultFormatter formatter = (DefaultFormatter)field.getFormatter();
/* 211 */     formatter.setCommitsOnValidEdit(true);
/* 212 */     this.modSpeedSpinner.addChangeListener(new ChangeListener()
/*     */     {
/*     */       public void stateChanged(ChangeEvent arg0) {
/* 215 */         float speed = ((Float)SettingsFrame.this.modSpeedSpinner.getValue()).floatValue();
/* 216 */         if ((speed >= 0.1F) && (speed <= 2.0F)) {
/* 217 */           Utils.setModifiedPlaybackSpeed(speed);
/*     */         }
/*     */         
/*     */       }
/* 221 */     });
/* 222 */     this.incModSpeedHotKeyTextField = new JTextField();
/* 223 */     this.incModSpeedHotKeyTextField.addFocusListener(new FocusAdapter()
/*     */     {
/*     */       public void focusLost(FocusEvent arg0) {
/* 226 */         GlobalScreen.getInstance().removeNativeKeyListener(SettingsFrame.this.incKeyInputGetter);
/* 227 */         SettingsFrame.this.incModSpeedHotKeyTextField.setBackground(Color.WHITE);
/*     */       }
/* 229 */     });
/* 230 */     this.incModSpeedHotKeyTextField.addMouseListener(new MouseAdapter()
/*     */     {
/*     */       public void mousePressed(MouseEvent arg0) {
/* 233 */         GlobalScreen.getInstance().addNativeKeyListener(SettingsFrame.this.incKeyInputGetter);
/* 234 */         SettingsFrame.this.incModSpeedHotKeyTextField.setBackground(Color.CYAN);
/*     */       }
/* 236 */     });
/* 237 */     this.incModSpeedHotKeyTextField.setEditable(false);
/* 238 */     this.incModSpeedHotKeyTextField.setColumns(10);
/*     */     
/* 240 */     this.decModSpeedHotKeyTextField = new JTextField();
/* 241 */     this.decModSpeedHotKeyTextField.addFocusListener(new FocusAdapter()
/*     */     {
/*     */       public void focusLost(FocusEvent e) {
/* 244 */         SettingsFrame.this.decModSpeedHotKeyTextField.setBackground(Color.WHITE);
/* 245 */         GlobalScreen.getInstance().removeNativeKeyListener(SettingsFrame.this.decKeyInputGetter);
/*     */       }
/* 247 */     });
/* 248 */     this.decModSpeedHotKeyTextField.addMouseListener(new MouseAdapter()
/*     */     {
/*     */       public void mousePressed(MouseEvent e) {
/* 251 */         GlobalScreen.getInstance().addNativeKeyListener(SettingsFrame.this.decKeyInputGetter);
/* 252 */         SettingsFrame.this.decModSpeedHotKeyTextField.setBackground(Color.CYAN);
/*     */       }
/* 254 */     });
/* 255 */     this.decModSpeedHotKeyTextField.setEditable(false);
/* 256 */     this.decModSpeedHotKeyTextField.setColumns(10);
/*     */     
/* 258 */     JLabel lblModifierSpeedIncrement = new JLabel("Modifier speed Increment hotkey:");
/*     */     
/* 260 */     JLabel lblNewLabel = new JLabel("Modifier speed Decrement hotkey:");
/*     */     
/* 262 */     JLabel lblpushToTalk = new JLabel("VoIP 'Push To Talk' Key(s): ");
/* 263 */     lblpushToTalk.setFont(new Font("Tahoma", 1, 11));
/*     */     
/* 265 */     this.pttKeysTextField = new JTextField();
/* 266 */     this.pttKeysTextField.addFocusListener(new FocusAdapter() {
/*     */       public void focusLost(FocusEvent arg0) {
/* 268 */         SettingsFrame.this.pttKeysTextField.setBackground(Color.WHITE);
/* 269 */         SettingsFrame.PttKeysNativeKeyInputGetter.access$1(SettingsFrame.this.pttKeysInputGetter);
/* 270 */         SettingsFrame.this.pttKeysTextField.removeKeyListener(SettingsFrame.this.pttKeysInputGetter);
/*     */       }
/* 272 */     });
/* 273 */     this.pttKeysTextField.addMouseListener(new MouseAdapter()
/*     */     {
/*     */       public void mousePressed(MouseEvent arg0) {
/* 276 */         SettingsFrame.this.pttKeysTextField.removeKeyListener(SettingsFrame.this.pttKeysInputGetter);
/* 277 */         SettingsFrame.this.pttKeysTextField.addKeyListener(SettingsFrame.this.pttKeysInputGetter);
/* 278 */         SettingsFrame.this.pttKeysTextField.setBackground(Color.CYAN);
/*     */       }
/* 280 */     });
/* 281 */     this.pttKeysTextField.setEditable(false);
/* 282 */     this.pttKeysTextField.setColumns(10);
/* 283 */     this.pttKeysTextField.setFocusTraversalKeysEnabled(false);
/*     */     
/* 285 */     JLabel lblOverlapSameSound = new JLabel("Overlap same sound file:");
/* 286 */     lblOverlapSameSound.setFont(new Font("Tahoma", 1, 11));
/*     */     
/* 288 */     this.fOverlapClipsCheckbox = new JCheckBox("");
/* 289 */     this.fOverlapClipsCheckbox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 291 */         boolean selected = SettingsFrame.this.fOverlapClipsCheckbox.isSelected();
/* 292 */         Utils.setOverlapSameClipWhilePlaying(selected);
/*     */       }
/* 294 */     });
/* 295 */     setIconImage(SoundboardFrame.icon);
/*     */     
/* 297 */     String[] inputMixers = MicInjector.getMixerNames(MicInjector.targetDataLineInfo);
/* 298 */     String[] outputMixers = MicInjector.getMixerNames(MicInjector.sourceDataLineInfo);
/* 299 */     String[] arrayOfString1; int j = (arrayOfString1 = inputMixers).length; for (int i = 0; i < j; i++) { String input = arrayOfString1[i];
/* 300 */       this.micComboBox.addItem(input);
/*     */     }
/* 302 */     j = (arrayOfString1 = outputMixers).length; for (i = 0; i < j; i++) { String output = arrayOfString1[i];
/* 303 */       this.vacComboBox.addItem(output);
/*     */     }
/* 305 */     this.micComboBox.setSelectedItem(SoundboardFrame.micInjectorInputMixerName);
/* 306 */     this.vacComboBox.setSelectedItem(SoundboardFrame.micInjectorOutputMixerName);
/* 307 */     this.micComboBox.addItemListener(new ItemListener() {
/*     */       public void itemStateChanged(ItemEvent e) {
/* 309 */         if (e.getStateChange() == 1) {
/* 310 */           SettingsFrame.this.updateMicInjectorSettings();
/*     */         }
/*     */       }
/* 313 */     });
/* 314 */     this.vacComboBox.addItemListener(new ItemListener() {
/*     */       public void itemStateChanged(ItemEvent e) {
/* 316 */         if (e.getStateChange() == 1) {
/* 317 */           SettingsFrame.this.updateMicInjectorSettings();
/*     */         }
/*     */         
/*     */       }
/* 321 */     });
/* 322 */     this.stopAllTextField.setText(NativeKeyEvent.getKeyText(Utils.getStopKey()));
/* 323 */     this.slowKeyTextField.setText(NativeKeyEvent.getKeyText(Utils.getModifiedSpeedKey()));
/* 324 */     this.incModSpeedHotKeyTextField.setText(NativeKeyEvent.getKeyText(Utils.getModspeedupKey()));
/* 325 */     this.decModSpeedHotKeyTextField.setText(NativeKeyEvent.getKeyText(Utils.getModspeeddownKey()));
/* 326 */     this.pttKeysInputGetter.updateTextField();
/* 327 */     this.fOverlapClipsCheckbox.setSelected(Utils.isOverlapSameClipWhilePlaying());
/* 328 */     getContentPane().setLayout(new MigLayout("", "[101px][20px][45px][13px][71px][4px][34px,grow][10px][135px]", "[20px][20px][20px][20px][20px][20px][21px][][2px][14px][20px][20px][13px][2px][14px][23px]"));
/* 329 */     getContentPane().add(lblstopAllSounds, "cell 0 0 3 1,alignx left,aligny center");
/* 330 */     getContentPane().add(lblhalfSpeedPlayback, "cell 0 1 5 1,growx,aligny center");
/* 331 */     getContentPane().add(lblModifiedPlaybackSpeed, "cell 0 2 3 1,alignx left,aligny center");
/* 332 */     getContentPane().add(this.stopAllTextField, "cell 6 0 3 1,growx,aligny top");
/* 333 */     getContentPane().add(this.slowKeyTextField, "cell 6 1 3 1,growx,aligny top");
/* 334 */     getContentPane().add(this.modSpeedSpinner, "cell 6 2 3 1,growx,aligny top");
/*     */     
/* 336 */     JLabel lblOverlapSwitchHotkey = new JLabel("Overlap switch hotkey:");
/* 337 */     getContentPane().add(lblOverlapSwitchHotkey, "cell 0 7 3 1");
/*     */     
/* 339 */     this.fOverlapHotkeyTextField = new JTextField();
/* 340 */     this.fOverlapHotkeyTextField.setEditable(false);
/* 341 */     getContentPane().add(this.fOverlapHotkeyTextField, "cell 6 7 3 1,growx");
/* 342 */     this.fOverlapHotkeyTextField.setColumns(10);
/* 343 */     this.fOverlapHotkeyTextField.addMouseListener(new MouseAdapter()
/*     */     {
/*     */       public void mousePressed(MouseEvent e) {
/* 346 */         GlobalScreen.getInstance().addNativeKeyListener(SettingsFrame.this.fOverlapKeyInputGetter);
/* 347 */         SettingsFrame.this.fOverlapHotkeyTextField.setBackground(Color.CYAN);
/*     */       }
/* 349 */     });
/* 350 */     this.fOverlapHotkeyTextField.addFocusListener(new FocusAdapter()
/*     */     {
/*     */       public void focusLost(FocusEvent e) {
/* 353 */         SettingsFrame.this.fOverlapHotkeyTextField.setBackground(Color.WHITE);
/* 354 */         GlobalScreen.getInstance().removeNativeKeyListener(SettingsFrame.this.fOverlapKeyInputGetter);
/*     */       }
/* 356 */     });
/* 357 */     this.fOverlapHotkeyTextField.setText(NativeKeyEvent.getKeyText(Utils.getOverlapSwitchKey()));
/*     */     
/* 359 */     getContentPane().add(separator, "cell 0 13 9 1,growx,aligny top");
/* 360 */     getContentPane().add(chckbxCheckForUpdate, "cell 0 15 3 1,alignx left,aligny top");
/* 361 */     getContentPane().add(btnProjectWebsite, "cell 4 15 3 1,alignx right,aligny top");
/* 362 */     getContentPane().add(btnCheckForUpdate, "cell 8 15,alignx right,aligny top");
/* 363 */     getContentPane().add(lblVersion, "cell 0 14,alignx left,aligny top");
/* 364 */     getContentPane().add(lblExpenosa, "cell 8 14,alignx right,aligny top");
/* 365 */     getContentPane().add(lblMicInjectorSettings, "cell 0 9,alignx left,aligny top");
/* 366 */     getContentPane().add(lblMicrophone, "cell 0 10,alignx left,aligny center");
/* 367 */     getContentPane().add(lblVirtualAudioCable, "cell 0 11,alignx left,aligny center");
/* 368 */     getContentPane().add(this.vacComboBox, "cell 2 11 7 1,growx,aligny top");
/* 369 */     getContentPane().add(this.micComboBox, "cell 2 10 7 1,growx,aligny top");
/* 370 */     getContentPane().add(lblUseMicInjector, "cell 0 12 9 1,alignx left,aligny top");
/* 371 */     getContentPane().add(separator_1, "cell 0 8 9 1,growx,aligny top");
/* 372 */     getContentPane().add(lblNewLabel, "cell 0 4 5 1,growx,aligny center");
/* 373 */     getContentPane().add(lblModifierSpeedIncrement, "cell 0 3 5 1,growx,aligny center");
/* 374 */     getContentPane().add(lblpushToTalk, "cell 0 5 3 1,alignx left,aligny center");
/* 375 */     getContentPane().add(lblOverlapSameSound, "cell 0 6 3 1,alignx left,growy");
/* 376 */     getContentPane().add(this.fOverlapClipsCheckbox, "cell 6 6,alignx left,aligny top");
/* 377 */     getContentPane().add(this.pttKeysTextField, "cell 6 5 3 1,growx,aligny top");
/* 378 */     getContentPane().add(this.decModSpeedHotKeyTextField, "cell 6 4 3 1,growx,aligny top");
/* 379 */     getContentPane().add(this.incModSpeedHotKeyTextField, "cell 6 3 3 1,growx,aligny top");
/* 380 */     pack();
/* 381 */     setVisible(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void updateMicInjectorSettings()
/*     */   {
/* 388 */     SoundboardFrame.micInjectorInputMixerName = (String)this.micComboBox.getSelectedItem();
/* 389 */     SoundboardFrame.micInjectorOutputMixerName = (String)this.vacComboBox.getSelectedItem();
/* 390 */     if (SoundboardFrame.useMicInjector) {
/* 391 */       Utils.startMicInjector(SoundboardFrame.micInjectorInputMixerName, SoundboardFrame.micInjectorOutputMixerName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateDisplayedModSpeed()
/*     */   {
/* 399 */     this.modSpeedSpinner.setValue(Float.valueOf(Utils.getModifiedPlaybackSpeed()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateOverlapSwitchCheckBox()
/*     */   {
/* 406 */     this.fOverlapClipsCheckbox.setSelected(Utils.isOverlapSameClipWhilePlaying());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 415 */     super.dispose();
/* 416 */     GlobalScreen gs = GlobalScreen.getInstance();
/* 417 */     gs.removeNativeKeyListener(this.slowKeyInputGetter);
/* 418 */     gs.removeNativeKeyListener(this.stopKeyInputGetter);
/* 419 */     gs.removeNativeKeyListener(this.incKeyInputGetter);
/* 420 */     gs.removeNativeKeyListener(this.decKeyInputGetter);
/* 421 */     this.pttKeysTextField.removeKeyListener(this.pttKeysInputGetter);
/* 422 */     instance = null;
/*     */   }
/*     */   
/*     */   private class OverlapSwitchNativeKeyInputGetter implements NativeKeyListener { private OverlapSwitchNativeKeyInputGetter() {}
/*     */     
/* 427 */     int key = Utils.stopKey;
/*     */     
/*     */     private void updateTextField() {
/* 430 */       String keyname = NativeKeyEvent.getKeyText(this.key);
/* 431 */       SettingsFrame.this.fOverlapHotkeyTextField.setText(keyname);
/*     */     }
/*     */     
/*     */     public void nativeKeyPressed(NativeKeyEvent e)
/*     */     {
/* 436 */       this.key = e.getKeyCode();
/* 437 */       Utils.setOverlapSwitchKey(this.key);
/* 438 */       updateTextField();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void nativeKeyReleased(NativeKeyEvent arg0) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void nativeKeyTyped(NativeKeyEvent arg0) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class PttKeysNativeKeyInputGetter
/*     */     implements KeyListener
/*     */   {
/* 455 */     HashSet<Integer> pressedkeys = new HashSet();
/*     */     
/*     */     private PttKeysNativeKeyInputGetter() {}
/*     */     
/* 459 */     public void keyPressed(KeyEvent e) { int key = e.getKeyCode();
/* 460 */       this.pressedkeys.add(Integer.valueOf(key));
/* 461 */       Utils.setPTTkeys(this.pressedkeys);
/* 462 */       updateTextField();
/* 463 */       System.out.println("PPT listener key pressed: " + KeyEvent.getKeyText(key));
/*     */     }
/*     */     
/*     */     public void keyReleased(KeyEvent e)
/*     */     {
/* 468 */       Integer key = Integer.valueOf(e.getKeyCode());
/* 469 */       this.pressedkeys.remove(key);
/* 470 */       System.out.println("PPT listener key released: " + KeyEvent.getKeyText(key.intValue()));
/*     */     }
/*     */     
/*     */ 
/*     */     public void keyTyped(KeyEvent arg0) {}
/*     */     
/*     */ 
/*     */     private synchronized void updateTextField()
/*     */     {
/* 479 */       StringBuilder keyString = new StringBuilder();
/* 480 */       ArrayList<Integer> keys = Utils.getPTTkeys();
/* 481 */       for (int i = 0; i < keys.size(); i++) {
/* 482 */         if (i == 0) {
/* 483 */           keyString.append(KeyEvent.getKeyText(((Integer)keys.get(i)).intValue()));
/*     */         } else {
/* 485 */           keyString.append(" + " + KeyEvent.getKeyText(((Integer)keys.get(i)).intValue()));
/*     */         }
/*     */       }
/* 488 */       SettingsFrame.this.pttKeysTextField.setText(keyString.toString());
/* 489 */       System.out.println("PTT listener text field updated");
/*     */     }
/*     */     
/*     */     private synchronized void clearPressedKeys() {
/* 493 */       this.pressedkeys.clear();
/* 494 */       System.out.println("PTT listener keys cleared");
/*     */     }
/*     */   }
/*     */   
/*     */   private class ModSpeedKeyNativeKeyInputGetter implements NativeKeyListener
/*     */   {
/* 500 */     int key = Utils.slowKey;
/*     */     
/*     */     private ModSpeedKeyNativeKeyInputGetter() {}
/*     */     
/* 504 */     public void nativeKeyPressed(NativeKeyEvent e) { this.key = e.getKeyCode();
/* 505 */       Utils.setModifiedSpeedKey(this.key);
/* 506 */       updateTextField();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void nativeKeyReleased(NativeKeyEvent e) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void nativeKeyTyped(NativeKeyEvent arg0) {}
/*     */     
/*     */ 
/*     */     private synchronized void updateTextField()
/*     */     {
/* 520 */       String keyname = NativeKeyEvent.getKeyText(this.key);
/* 521 */       SettingsFrame.this.slowKeyTextField.setText(keyname);
/*     */     }
/*     */   }
/*     */   
/*     */   private class StopKeyNativeKeyInputGetter
/*     */     implements NativeKeyListener
/*     */   {
/* 528 */     int key = Utils.stopKey;
/*     */     
/*     */     private StopKeyNativeKeyInputGetter() {}
/*     */     
/* 532 */     public void nativeKeyPressed(NativeKeyEvent e) { this.key = e.getKeyCode();
/* 533 */       Utils.setStopKey(this.key);
/* 534 */       updateTextField();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void nativeKeyReleased(NativeKeyEvent e) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void nativeKeyTyped(NativeKeyEvent arg0) {}
/*     */     
/*     */ 
/*     */     private synchronized void updateTextField()
/*     */     {
/* 548 */       String keyname = NativeKeyEvent.getKeyText(this.key);
/* 549 */       SettingsFrame.this.stopAllTextField.setText(keyname);
/*     */     }
/*     */   }
/*     */   
/*     */   private class IncKeyNativeKeyInputGetter
/*     */     implements NativeKeyListener
/*     */   {
/* 556 */     int key = Utils.getModspeedupKey();
/*     */     
/*     */     private IncKeyNativeKeyInputGetter() {}
/*     */     
/* 560 */     public void nativeKeyPressed(NativeKeyEvent e) { this.key = e.getKeyCode();
/* 561 */       Utils.setModspeedupKey(this.key);
/* 562 */       updateTextField();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void nativeKeyReleased(NativeKeyEvent e) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void nativeKeyTyped(NativeKeyEvent arg0) {}
/*     */     
/*     */ 
/*     */     private synchronized void updateTextField()
/*     */     {
/* 576 */       String keyname = NativeKeyEvent.getKeyText(this.key);
/* 577 */       SettingsFrame.this.incModSpeedHotKeyTextField.setText(keyname);
/*     */     }
/*     */   }
/*     */   
/*     */   private class DecKeyNativeKeyInputGetter
/*     */     implements NativeKeyListener
/*     */   {
/* 584 */     int key = Utils.getModspeeddownKey();
/*     */     
/*     */     private DecKeyNativeKeyInputGetter() {}
/*     */     
/* 588 */     public void nativeKeyPressed(NativeKeyEvent e) { this.key = e.getKeyCode();
/* 589 */       Utils.setModspeeddownKey(this.key);
/* 590 */       updateTextField();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void nativeKeyReleased(NativeKeyEvent e) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void nativeKeyTyped(NativeKeyEvent arg0) {}
/*     */     
/*     */ 
/*     */     private synchronized void updateTextField()
/*     */     {
/* 604 */       String keyname = NativeKeyEvent.getKeyText(this.key);
/* 605 */       SettingsFrame.this.decModSpeedHotKeyTextField.setText(keyname);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SettingsFrame getInstanceOf()
/*     */   {
/* 616 */     if (instance == null) {
/* 617 */       instance = new SettingsFrame();
/*     */     } else {
/* 619 */       instance.setVisible(true);
/* 620 */       instance.requestFocus();
/*     */     }
/* 622 */     return instance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void focusLostOnItems()
/*     */   {
/* 629 */     this.stopAllTextField.setBackground(Color.WHITE);
/* 630 */     this.slowKeyTextField.setBackground(Color.WHITE);
/* 631 */     this.decModSpeedHotKeyTextField.setBackground(Color.WHITE);
/* 632 */     this.incModSpeedHotKeyTextField.setBackground(Color.WHITE);
/* 633 */     this.fOverlapHotkeyTextField.setBackground(Color.WHITE);
/* 634 */     this.pttKeysTextField.setBackground(Color.WHITE);
/* 635 */     this.pttKeysInputGetter.clearPressedKeys();
/* 636 */     GlobalScreen gs = GlobalScreen.getInstance();
/* 637 */     gs.removeNativeKeyListener(this.stopKeyInputGetter);
/* 638 */     gs.removeNativeKeyListener(this.slowKeyInputGetter);
/* 639 */     gs.removeNativeKeyListener(this.incKeyInputGetter);
/* 640 */     gs.removeNativeKeyListener(this.decKeyInputGetter);
/* 641 */     gs.removeNativeKeyListener(this.fOverlapKeyInputGetter);
/* 642 */     this.pttKeysTextField.removeKeyListener(this.pttKeysInputGetter);
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\gui\SettingsFrame.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
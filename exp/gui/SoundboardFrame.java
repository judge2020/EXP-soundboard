/*     */ package exp.gui;
/*     */ 
/*     */ import com.apple.eawt.Application;
/*     */ import com.google.gson.Gson;
/*     */ import exp.converter.ConverterFrame;
/*     */ import exp.soundboard.AudioManager;
/*     */ import exp.soundboard.GlobalKeyMacroListener;
/*     */ import exp.soundboard.Soundboard;
/*     */ import exp.soundboard.SoundboardEntry;
/*     */ import exp.soundboard.UpdateChecker;
/*     */ import exp.soundboard.Utils;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Desktop;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.prefs.Preferences;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.UIManager.LookAndFeelInfo;
/*     */ import javax.swing.UnsupportedLookAndFeelException;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ import org.jnativehook.GlobalScreen;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SoundboardFrame
/*     */   extends JFrame
/*     */ {
/*     */   private static final long serialVersionUID = 8934802095461138592L;
/*     */   final SoundboardFrame thisFrameInstance;
/*     */   public static final float VERSION = 0.5F;
/*  68 */   private final String title = "EXP Soundboard vers. 0.5 | ";
/*     */   private JComboBox<String> secondarySpeakerComboBox;
/*     */   private JComboBox<String> primarySpeakerComboBox;
/*     */   public AudioManager audioManager;
/*     */   public static Soundboard soundboard;
/*     */   public File testFile;
/*     */   private JTable table;
/*     */   public static GlobalKeyMacroListener macroListener;
/*     */   static boolean updateCheck;
/*  77 */   public static String micInjectorInputMixerName = "";
/*  78 */   public static String micInjectorOutputMixerName = "";
/*  79 */   public static boolean useMicInjector = false;
/*     */   
/*  81 */   public static final Image icon = new ImageIcon(SoundboardFrame.class.getResource("EXP logo.png")).getImage();
/*     */   
/*     */   public static JFileChooser filechooser;
/*  84 */   private final String useSecondaryKey = "useSecondSpeaker";
/*  85 */   private final String firstSpeakerKey = "firstSpeaker";
/*  86 */   private final String secondSpeakerKey = "secondSpeaker";
/*  87 */   private final String lastSoundboardFileKey = "lastSoundboardUsed";
/*  88 */   private final String stopallKeyKey = "stopAllKey";
/*  89 */   private final String modPlaybackSpeedKey = "modplaybackspeed";
/*  90 */   private final String modPlaybackSpeedKeyKey = "slowSoundKey";
/*  91 */   private final String modSpeedIncKeyKey = "modSpeedIncKey";
/*  92 */   private final String modSpeedDecKeyKey = "modSpeedDecKey";
/*  93 */   private final String updateCheckKey = "updateCheckOnLaunch";
/*  94 */   private final String micInjectorInputKey = "micInjectorInput";
/*  95 */   private final String micInjectorOutputKey = "micInjectorOutput";
/*  96 */   private final String micInjectorEnabledKey = "micInjectorEnabled";
/*  97 */   private final String primaryOutputGainKey = "primaryOutputGain";
/*  98 */   private final String secondaryOutputGainKey = "secondaryOutputGain";
/*  99 */   private final String micInjectorOutputGainKey = "micInjectorOutputGain";
/* 100 */   private final String autoPPTenabledKey = "autoPPTenabled";
/* 101 */   private final String autoPPTKeysKey = "autoPTTkeys";
/* 102 */   private final String overlapClipsKey = "OverlapClipsWhilePlaying";
/* 103 */   private final String OVERLAPSWITCHKEYKEY = "OverlapClipsKey";
/*     */   
/*     */   private JCheckBox useSecondaryCheckBox;
/* 106 */   private File currentSoundboardFile = null;
/*     */   
/*     */   private JCheckBox useMicInjectorCheckBox;
/*     */   
/*     */   private JMenuBar menuBar;
/*     */   
/*     */   private JCheckBox autoPptCheckBox;
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/* 116 */     Utils.initGlobalKeyLibrary();
/* 117 */     Utils.startMp3Decoder();
/* 118 */     new SoundboardFrame().setVisible(true);
/*     */   }
/*     */   
/*     */   public SoundboardFrame() {
/* 122 */     addWindowListener(new WindowAdapter()
/*     */     {
/*     */ 
/* 125 */       public void windowClosing(WindowEvent e) { SoundboardFrame.this.exit(); }
/*     */     });
/*     */     try {
/*     */       UIManager.LookAndFeelInfo[] arrayOfLookAndFeelInfo;
/* 129 */       int j = (arrayOfLookAndFeelInfo = UIManager.getInstalledLookAndFeels()).length; for (int i = 0; i < j; i++) { UIManager.LookAndFeelInfo info = arrayOfLookAndFeelInfo[i];
/* 130 */         if ("Nimbus".equals(info.getName())) {
/* 131 */           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/* 132 */           break;
/*     */         }
/*     */       }
/*     */     } catch (ClassNotFoundException ex) {
/* 136 */       Logger.getLogger(SoundboardFrame.class.getName()).log(Level.SEVERE, null, ex);
/*     */     } catch (InstantiationException ex) {
/* 138 */       Logger.getLogger(SoundboardFrame.class.getName()).log(Level.SEVERE, null, ex);
/*     */     } catch (IllegalAccessException ex) {
/* 140 */       Logger.getLogger(SoundboardFrame.class.getName()).log(Level.SEVERE, null, ex);
/*     */     } catch (UnsupportedLookAndFeelException ex) {
/* 142 */       Logger.getLogger(SoundboardFrame.class.getName()).log(Level.SEVERE, null, ex);
/*     */     }
/* 144 */     filechooser = new JFileChooser();
/* 145 */     this.audioManager = new AudioManager();
/* 146 */     soundboard = new Soundboard();
/* 147 */     setDefaultCloseOperation(3);
/* 148 */     setTitle("EXP Soundboard vers. 0.5 | ");
/* 149 */     setIconImage(icon);
/*     */     
/* 151 */     macInit();
/*     */     
/* 153 */     this.secondarySpeakerComboBox = new JComboBox();
/* 154 */     this.secondarySpeakerComboBox.addItemListener(new ItemListener() {
/*     */       public void itemStateChanged(ItemEvent e) {
/* 156 */         if (e.getStateChange() == 1) {
/* 157 */           String name = (String)SoundboardFrame.this.secondarySpeakerComboBox.getSelectedItem();
/* 158 */           SoundboardFrame.this.audioManager.setSecondaryOutputMixer(name);
/*     */         }
/*     */         
/*     */       }
/* 162 */     });
/* 163 */     this.primarySpeakerComboBox = new JComboBox();
/* 164 */     this.primarySpeakerComboBox.addItemListener(new ItemListener() {
/*     */       public void itemStateChanged(ItemEvent e) {
/* 166 */         if (e.getStateChange() == 1) {
/* 167 */           String name = (String)SoundboardFrame.this.primarySpeakerComboBox.getSelectedItem();
/* 168 */           SoundboardFrame.this.audioManager.setPrimaryOutputMixer(name);
/*     */         }
/*     */         
/*     */       }
/* 172 */     });
/* 173 */     JButton btnStop = new JButton("Stop All");
/* 174 */     btnStop.addActionListener(new ActionListener()
/*     */     {
/*     */ 
/*     */       public void actionPerformed(ActionEvent e) {}
/*     */ 
/* 179 */     });
/* 180 */     this.useSecondaryCheckBox = new JCheckBox("Use");
/* 181 */     this.useSecondaryCheckBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 183 */         SoundboardFrame.this.audioManager.setUseSecondary(SoundboardFrame.this.useSecondaryCheckBox.isSelected());
/*     */       }
/*     */       
/* 186 */     });
/* 187 */     JScrollPane scrollPane = new JScrollPane();
/*     */     
/* 189 */     JButton btnAdd = new JButton("Add");
/* 190 */     btnAdd.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 192 */         new SoundboardEntryEditor(SoundboardFrame.this.thisFrameInstance);
/*     */       }
/*     */       
/* 195 */     });
/* 196 */     this.useMicInjectorCheckBox = new JCheckBox("Use Mic Injector (see Option -> Settings)");
/* 197 */     this.useMicInjectorCheckBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 199 */         SoundboardFrame.useMicInjector = SoundboardFrame.this.useMicInjectorCheckBox.isSelected();
/* 200 */         SoundboardFrame.this.updateMicInjector();
/*     */       }
/*     */       
/* 203 */     });
/* 204 */     JLabel lblstOutputeg = new JLabel("1st Output (e.g. your speakers)");
/* 205 */     lblstOutputeg.setForeground(Color.DARK_GRAY);
/*     */     
/* 207 */     JLabel lblndOutputeg = new JLabel("2nd Output (e.g. virtual audio cable \"input\")(optional)");
/* 208 */     lblndOutputeg.setForeground(Color.DARK_GRAY);
/*     */     
/* 210 */     JSeparator separator_4 = new JSeparator();
/*     */     
/* 212 */     this.autoPptCheckBox = new JCheckBox("Auto-hold PTT key(s)");
/* 213 */     this.autoPptCheckBox.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 215 */         boolean selected = SoundboardFrame.this.autoPptCheckBox.isSelected();
/* 216 */         Utils.setAutoPTThold(selected);
/*     */       }
/*     */       
/* 219 */     });
/* 220 */     this.table = new JTable();
/* 221 */     this.table.setSelectionMode(0);
/* 222 */     this.table.setAutoCreateRowSorter(true);
/* 223 */     this.table.setModel(new DefaultTableModel(
/* 224 */       new Object[][] {
/* 225 */       new Object[2] }, 
/*     */       
/* 227 */       new String[] {
/* 228 */       "Sound Clip", "HotKeys" })
/*     */       {
/*     */         private static final long serialVersionUID = 1L;
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
/* 236 */         Class[] columnTypes = {
/* 237 */           String.class, String.class };
/*     */         
/*     */ 
/*     */ 
/* 241 */         public Class getColumnClass(int columnIndex) { return this.columnTypes[columnIndex]; }
/*     */         
/* 243 */         boolean[] columnEditables = new boolean[2];
/*     */         
/*     */         public boolean isCellEditable(int row, int column)
/*     */         {
/* 247 */           return this.columnEditables[column];
/*     */         }
/* 249 */       });
/* 250 */     scrollPane.setViewportView(this.table);
/* 251 */     getContentPane().setLayout(new MigLayout("gapx  2:4:5, gapy 2:4:5, fillx", "[][][][6px][20px][6px][2px][10px][53px][6px][][24px][2px][43px]", "[grow,fill][23px][14px][20px][14px][23px][2px][23px]"));
/*     */     
/* 253 */     JButton btnRemove = new JButton("Remove");
/* 254 */     btnRemove.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 256 */         int selected = SoundboardFrame.this.table.getSelectedRow();
/* 257 */         if (selected > -1) {
/* 258 */           int index = SoundboardFrame.this.getSelectedEntryIndex();
/* 259 */           SoundboardFrame.soundboard.removeEntry(index);
/* 260 */           SoundboardFrame.this.updateSoundboardTable();
/* 261 */           if (index >= SoundboardFrame.this.table.getRowCount()) {
/* 262 */             index--;
/*     */           }
/* 264 */           if (index >= 0) {
/* 265 */             SoundboardFrame.this.table.setRowSelectionInterval(index, index);
/*     */           }
/*     */         }
/*     */       }
/* 269 */     });
/* 270 */     getContentPane().add(btnRemove, "cell 1 1,alignx left,aligny top");
/*     */     
/* 272 */     JButton btnEdit = new JButton("Edit");
/* 273 */     btnEdit.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 275 */         int selected = SoundboardFrame.this.table.getSelectedRow();
/* 276 */         if (selected > -1) {
/* 277 */           int index = SoundboardFrame.this.getSelectedEntryIndex();
/* 278 */           System.out.println("index " + index);
/* 279 */           SoundboardEntry entry = SoundboardFrame.soundboard.getEntry(index);
/* 280 */           new SoundboardEntryEditor(SoundboardFrame.this.thisFrameInstance, entry);
/*     */         }
/*     */       }
/* 283 */     });
/* 284 */     getContentPane().add(btnEdit, "cell 2 1,alignx left,aligny top");
/*     */     
/* 286 */     JButton btnPlay = new JButton("Play");
/* 287 */     btnPlay.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 289 */         int selected = SoundboardFrame.this.table.getSelectedRow();
/* 290 */         if (selected > -1) {
/* 291 */           int index = SoundboardFrame.this.getSelectedEntryIndex();
/* 292 */           SoundboardEntry entry = SoundboardFrame.soundboard.getEntry(index);
/* 293 */           if (SoundboardFrame.macroListener.isSpeedModKeyHeld()) {
/* 294 */             entry.play(SoundboardFrame.this.audioManager, true);
/*     */           } else {
/* 296 */             entry.play(SoundboardFrame.this.audioManager, false);
/*     */           }
/*     */         }
/*     */       }
/* 300 */     });
/* 301 */     getContentPane().add(btnPlay, "cell 10 1,alignx right,aligny top");
/* 302 */     getContentPane().add(this.useMicInjectorCheckBox, "cell 0 7 7 1,alignx left,aligny top");
/* 303 */     getContentPane().add(this.autoPptCheckBox, "cell 8 7 6 1,alignx right,aligny top");
/* 304 */     getContentPane().add(separator_4, "cell 0 6 14 1,growx,aligny top");
/* 305 */     getContentPane().add(scrollPane, "cell 0 0 14 1,grow");
/* 306 */     getContentPane().add(this.primarySpeakerComboBox, "cell 0 3 14 1,growx,aligny top");
/* 307 */     getContentPane().add(this.secondarySpeakerComboBox, "cell 0 5 12 1,growx,aligny center");
/* 308 */     getContentPane().add(this.useSecondaryCheckBox, "cell 13 5,alignx left,aligny top");
/* 309 */     getContentPane().add(btnAdd, "cell 0 1,alignx left,aligny top");
/* 310 */     getContentPane().add(btnStop, "cell 11 1 3 1,alignx right,aligny top");
/* 311 */     getContentPane().add(lblndOutputeg, "cell 0 4 7 1,alignx left,aligny top");
/* 312 */     getContentPane().add(lblstOutputeg, "cell 0 2 7 1,alignx left,aligny top");
/*     */     
/* 314 */     this.menuBar = new JMenuBar();
/* 315 */     setJMenuBar(this.menuBar);
/*     */     
/* 317 */     JMenu mnFile = new JMenu("File");
/* 318 */     this.menuBar.add(mnFile);
/*     */     
/* 320 */     JMenuItem mntmNew = new JMenuItem("New");
/* 321 */     mntmNew.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 323 */         SoundboardFrame.this.fileNew();
/*     */       }
/* 325 */     });
/* 326 */     mnFile.add(mntmNew);
/*     */     
/* 328 */     JMenuItem mntmOpen = new JMenuItem("Open");
/* 329 */     mntmOpen.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 331 */         SoundboardFrame.this.fileOpen();
/*     */       }
/* 333 */     });
/* 334 */     mnFile.add(mntmOpen);
/*     */     
/* 336 */     JSeparator separator = new JSeparator();
/* 337 */     mnFile.add(separator);
/*     */     
/* 339 */     JMenuItem mntmSave = new JMenuItem("Save");
/* 340 */     mntmSave.setAccelerator(KeyStroke.getKeyStroke(83, 2));
/* 341 */     mntmSave.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 343 */         SoundboardFrame.this.fileSave();
/*     */       }
/* 345 */     });
/* 346 */     mnFile.add(mntmSave);
/*     */     
/* 348 */     JMenuItem mntmSaveAs = new JMenuItem("Save As...");
/* 349 */     mntmSaveAs.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 351 */         SoundboardFrame.this.fileSaveAs();
/*     */       }
/* 353 */     });
/* 354 */     mnFile.add(mntmSaveAs);
/*     */     
/* 356 */     JSeparator separator_3 = new JSeparator();
/* 357 */     mnFile.add(separator_3);
/*     */     
/* 359 */     JMenuItem mntmProjectPage = new JMenuItem("Sourceforge Page");
/* 360 */     mntmProjectPage.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/*     */         try {
/* 363 */           Desktop.getDesktop().browse(new URI("https://sourceforge.net/projects/expsoundboard/"));
/*     */         } catch (IOException|URISyntaxException e) {
/* 365 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 368 */     });
/* 369 */     mnFile.add(mntmProjectPage);
/*     */     
/* 371 */     JSeparator separator_1 = new JSeparator();
/* 372 */     mnFile.add(separator_1);
/*     */     
/* 374 */     JMenuItem mntmQuit = new JMenuItem("Quit");
/* 375 */     mntmQuit.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 377 */         SoundboardFrame.this.exit();
/*     */       }
/* 379 */     });
/* 380 */     mnFile.add(mntmQuit);
/*     */     
/* 382 */     JMenu mnEdit = new JMenu("Option");
/* 383 */     this.menuBar.add(mnEdit);
/*     */     
/* 385 */     JMenuItem mntmSettings = new JMenuItem("Settings");
/* 386 */     mntmSettings.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 388 */         SoundboardFrame.this.getSettingsMenu();
/*     */       }
/* 390 */     });
/* 391 */     mnEdit.add(mntmSettings);
/*     */     
/* 393 */     JMenuItem mntmAudioLevels = new JMenuItem("Audio Levels");
/* 394 */     mntmAudioLevels.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/* 396 */         AudioLevelsFrame.getInstance().setLocationRelativeTo(SoundboardFrame.this.thisFrameInstance);
/*     */       }
/* 398 */     });
/* 399 */     mnEdit.add(mntmAudioLevels);
/*     */     
/* 401 */     JSeparator separator_2 = new JSeparator();
/* 402 */     mnEdit.add(separator_2);
/*     */     
/* 404 */     JMenuItem mntmAudioConverter = new JMenuItem("Audio Converter");
/* 405 */     mntmAudioConverter.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 407 */         if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
/* 408 */           new ConverterFrame();
/*     */         } else {
/* 410 */           JOptionPane.showMessageDialog(null, "Audio Converter currently not supported on Mac OS X", "Feature not supported", 1);
/*     */         }
/*     */       }
/* 413 */     });
/* 414 */     mnEdit.add(mntmAudioConverter);
/*     */     
/* 416 */     setMinimumSize(new Dimension(400, 500));
/*     */     
/* 418 */     updateSpeakerComboBoxes();
/* 419 */     pack();
/* 420 */     this.thisFrameInstance = this;
/* 421 */     macroListener = new GlobalKeyMacroListener(this);
/* 422 */     GlobalScreen.getInstance().addNativeKeyListener(macroListener);
/* 423 */     setLocationRelativeTo(null);
/* 424 */     loadPrefs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateSoundboardTable()
/*     */   {
/* 432 */     Object[][] entryArray = soundboard.getEntriesAsObjectArrayForTable();
/*     */     
/* 434 */     this.table.setModel(new DefaultTableModel(
/* 435 */       entryArray, 
/* 436 */       new String[] {
/* 437 */       "Sound Clip", "HotKeys", "File Locations", "Index" })
/*     */       {
/*     */         private static final long serialVersionUID = 1L;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 445 */         Class[] columnTypes = {
/* 446 */           String.class, String.class, String.class, Integer.TYPE };
/*     */         
/*     */ 
/*     */ 
/* 450 */         public Class getColumnClass(int columnIndex) { return this.columnTypes[columnIndex]; }
/*     */         
/* 452 */         boolean[] columnEditables = new boolean[4];
/*     */         
/*     */         public boolean isCellEditable(int row, int column)
/*     */         {
/* 456 */           return this.columnEditables[column];
/*     */         }
/* 458 */       });
/* 459 */     TableColumnModel columnmod = this.table.getColumnModel();
/* 460 */     columnmod.getColumn(3).setMinWidth(0);
/* 461 */     columnmod.getColumn(3).setMaxWidth(0);
/* 462 */     columnmod.getColumn(3).setWidth(0);
/* 463 */     this.table.removeColumn(columnmod.getColumn(2));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void fileNew()
/*     */   {
/* 470 */     Utils.stopAllClips();
/* 471 */     saveReminder();
/* 472 */     this.currentSoundboardFile = null;
/* 473 */     soundboard = new Soundboard();
/* 474 */     updateSoundboardTable();
/* 475 */     setTitle("EXP Soundboard vers. 0.5 | ");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void fileOpen()
/*     */   {
/* 482 */     Utils.stopAllClips();
/* 483 */     saveReminder();
/* 484 */     filechooser.setFileFilter(new JsonFileFilter(null));
/* 485 */     int session = filechooser.showOpenDialog(null);
/* 486 */     if (session == 0) {
/* 487 */       File jsonfile = filechooser.getSelectedFile();
/* 488 */       open(jsonfile);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void fileSave()
/*     */   {
/* 497 */     if (this.currentSoundboardFile != null) {
/* 498 */       soundboard.saveAsJsonFile(this.currentSoundboardFile);
/*     */     } else {
/* 500 */       fileSaveAs();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void fileSaveAs()
/*     */   {
/* 508 */     JFileChooser fc = new JFileChooser();
/* 509 */     fc.setFileFilter(new JsonFileFilter(null));
/* 510 */     if (this.currentSoundboardFile != null) {
/* 511 */       fc.setSelectedFile(this.currentSoundboardFile);
/*     */     }
/* 513 */     int session = fc.showSaveDialog(null);
/* 514 */     if (session == 0) {
/* 515 */       File file = fc.getSelectedFile();
/* 516 */       this.currentSoundboardFile = soundboard.saveAsJsonFile(file);
/* 517 */       setTitle("EXP Soundboard vers. 0.5 | " + this.currentSoundboardFile.getName());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void getSettingsMenu()
/*     */   {
/* 525 */     SettingsFrame.getInstanceOf().setLocationRelativeTo(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void open(File jsonfile)
/*     */   {
/* 533 */     if (jsonfile.exists()) {
/* 534 */       Soundboard sb = Soundboard.loadFromJsonFile(jsonfile);
/* 535 */       if (sb != null) {
/* 536 */         soundboard = sb;
/* 537 */         updateSoundboardTable();
/* 538 */         this.currentSoundboardFile = jsonfile;
/* 539 */         setTitle("EXP Soundboard vers. 0.5 | " + this.currentSoundboardFile.getName());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateSpeakerComboBoxes()
/*     */   {
/* 548 */     String[] outputmixerStringArray = Utils.getMixerNames(this.audioManager.standardDataLineInfo);
/* 549 */     String[] arrayOfString1; int j = (arrayOfString1 = outputmixerStringArray).length; for (int i = 0; i < j; i++) { String speaker = arrayOfString1[i];
/* 550 */       this.primarySpeakerComboBox.addItem(speaker);
/* 551 */       this.secondarySpeakerComboBox.addItem(speaker);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getSelectedEntryIndex()
/*     */   {
/* 562 */     int selected = this.table.getSelectedRow();
/* 563 */     return ((Integer)this.table.getValueAt(selected, 2)).intValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateMicInjector()
/*     */   {
/* 570 */     this.useMicInjectorCheckBox.setSelected(useMicInjector);
/* 571 */     if (useMicInjector) {
/* 572 */       Utils.startMicInjector(micInjectorInputMixerName, micInjectorOutputMixerName);
/*     */     } else {
/* 574 */       Utils.stopMicInjector();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void savePrefs()
/*     */   {
/* 583 */     Preferences prefs = Utils.prefs;
/* 584 */     prefs.putBoolean("useSecondSpeaker", this.useSecondaryCheckBox.isSelected());
/* 585 */     prefs.put("firstSpeaker", (String)this.primarySpeakerComboBox.getSelectedItem());
/* 586 */     prefs.put("secondSpeaker", (String)this.secondarySpeakerComboBox.getSelectedItem());
/* 587 */     if (this.currentSoundboardFile != null) {
/* 588 */       prefs.put("lastSoundboardUsed", this.currentSoundboardFile.getAbsolutePath());
/*     */     }
/* 590 */     prefs.putBoolean("OverlapClipsWhilePlaying", Utils.isOverlapSameClipWhilePlaying());
/* 591 */     prefs.putInt("OverlapClipsKey", Utils.getOverlapSwitchKey());
/* 592 */     prefs.putInt("stopAllKey", Utils.getStopKey());
/* 593 */     prefs.putFloat("modplaybackspeed", Utils.getModifiedPlaybackSpeed());
/* 594 */     prefs.putInt("slowSoundKey", Utils.getModifiedSpeedKey());
/* 595 */     prefs.putInt("modSpeedIncKey", Utils.getModspeedupKey());
/* 596 */     prefs.putInt("modSpeedDecKey", Utils.getModspeeddownKey());
/* 597 */     prefs.putBoolean("updateCheckOnLaunch", updateCheck);
/* 598 */     prefs.put("micInjectorInput", micInjectorInputMixerName);
/* 599 */     prefs.put("micInjectorOutput", micInjectorOutputMixerName);
/* 600 */     prefs.putBoolean("micInjectorEnabled", useMicInjector);
/* 601 */     prefs.putBoolean("autoPPTenabled", Utils.autoPTThold);
/* 602 */     prefs.put("autoPTTkeys", Utils.getPTTkeys().toString());
/*     */     
/* 604 */     prefs.putFloat("primaryOutputGain", AudioManager.getFirstOutputGain());
/* 605 */     prefs.putFloat("secondaryOutputGain", AudioManager.getSecondOutputGain());
/* 606 */     prefs.putFloat("micInjectorOutputGain", Utils.getMicInjectorGain());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void loadPrefs()
/*     */   {
/* 615 */     Preferences prefs = Utils.prefs;
/* 616 */     boolean useSecond = prefs.getBoolean("useSecondSpeaker", false);
/* 617 */     this.useSecondaryCheckBox.setSelected(useSecond);
/* 618 */     this.audioManager.setUseSecondary(useSecond);
/*     */     
/* 620 */     String firstspeaker = prefs.get("firstSpeaker", null);
/* 621 */     String secondspeaker = prefs.get("secondSpeaker", null);
/* 622 */     if (firstspeaker != null) {
/* 623 */       this.primarySpeakerComboBox.setSelectedItem(firstspeaker);
/* 624 */       this.audioManager.setPrimaryOutputMixer(firstspeaker);
/*     */     }
/* 626 */     if (secondspeaker != null) {
/* 627 */       this.secondarySpeakerComboBox.setSelectedItem(secondspeaker);
/* 628 */       this.audioManager.setSecondaryOutputMixer(secondspeaker);
/*     */     }
/*     */     
/* 631 */     String lastfile = prefs.get("lastSoundboardUsed", null);
/* 632 */     if (lastfile != null) {
/* 633 */       open(new File(lastfile));
/*     */     }
/*     */     
/* 636 */     float modSpeed = prefs.getFloat("modplaybackspeed", 0.5F);
/* 637 */     Utils.setModifiedPlaybackSpeed(modSpeed);
/*     */     
/*     */ 
/* 640 */     int slowkey = prefs.getInt("slowSoundKey", 35);
/* 641 */     Utils.setModifiedSpeedKey(slowkey);
/* 642 */     int stopkey = prefs.getInt("stopAllKey", 19);
/* 643 */     Utils.setStopKey(stopkey);
/* 644 */     int incKey = prefs.getInt("modSpeedIncKey", 39);
/* 645 */     Utils.setModspeedupKey(incKey);
/* 646 */     int decKey = prefs.getInt("modSpeedDecKey", 37);
/* 647 */     Utils.setModspeeddownKey(decKey);
/*     */     
/* 649 */     updateCheck = prefs.getBoolean("updateCheckOnLaunch", true);
/* 650 */     if (updateCheck) {
/* 651 */       new Thread(new UpdateChecker()).start();
/*     */     }
/*     */     
/* 654 */     float firstOutputGain = prefs.getFloat("primaryOutputGain", 0.0F);
/* 655 */     float secondOutputGain = prefs.getFloat("secondaryOutputGain", 0.0F);
/* 656 */     float micinjectorOutputGain = prefs.getFloat("micInjectorOutputGain", 0.0F);
/* 657 */     AudioManager.setFirstOutputGain(firstOutputGain);
/* 658 */     AudioManager.setSecondOutputGain(secondOutputGain);
/* 659 */     Utils.setMicInjectorGain(micinjectorOutputGain);
/*     */     
/*     */ 
/* 662 */     micInjectorInputMixerName = prefs.get("micInjectorInput", "");
/* 663 */     micInjectorOutputMixerName = prefs.get("micInjectorOutput", "");
/* 664 */     useMicInjector = prefs.getBoolean("micInjectorEnabled", false);
/* 665 */     updateMicInjector();
/*     */     
/*     */ 
/* 668 */     boolean useautoptt = prefs.getBoolean("autoPPTenabled", false);
/* 669 */     this.autoPptCheckBox.setSelected(useautoptt);
/* 670 */     Utils.setAutoPTThold(useautoptt);
/* 671 */     String autopttkeys = prefs.get("autoPTTkeys", null);
/* 672 */     if (autopttkeys != null) {
/* 673 */       ArrayList<Integer> keys = Utils.stringToIntArrayList(autopttkeys);
/* 674 */       Utils.setPTTkeys(keys);
/*     */     }
/*     */     
/*     */ 
/* 678 */     Utils.setOverlapSameClipWhilePlaying(prefs.getBoolean("OverlapClipsWhilePlaying", true));
/* 679 */     int overlapKey = prefs.getInt("OverlapClipsKey", 36);
/* 680 */     Utils.setOverlapSwitchKey(overlapKey);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void exit()
/*     */   {
/* 687 */     Utils.stopAllClips();
/* 688 */     saveReminder();
/* 689 */     savePrefs();
/* 690 */     dispose();
/* 691 */     Utils.deregisterGlobalKeyLibrary();
/* 692 */     System.exit(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void saveReminder()
/*     */   {
/* 699 */     if (this.currentSoundboardFile != null) {
/* 700 */       if (this.currentSoundboardFile.exists()) {
/* 701 */         Gson gson = new Gson();
/* 702 */         Soundboard savedFile = Soundboard.loadFromJsonFile(this.currentSoundboardFile);
/* 703 */         String savedjson = gson.toJson(savedFile);
/* 704 */         String currentjson = gson.toJson(soundboard);
/* 705 */         if (!savedjson.equals(currentjson)) {
/* 706 */           int option = JOptionPane.showConfirmDialog(null, "Soundboard has changed. Do you want to save?", "Save Reminder", 0);
/* 707 */           if (option == 0) {
/* 708 */             soundboard.saveAsJsonFile(this.currentSoundboardFile);
/*     */           }
/*     */         }
/*     */       }
/* 712 */     } else if (soundboard.getSoundboardEntries().size() > 0) {
/* 713 */       int option = JOptionPane.showConfirmDialog(null, "Soundboard has not been saved. Do you want to save?", "Save Reminder", 0);
/* 714 */       if (option == 0) {
/* 715 */         fileSave();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void macInit()
/*     */   {
/* 724 */     if (System.getProperty("os.name").toLowerCase().contains("mac")) {
/* 725 */       Application application = Application.getApplication();
/* 726 */       application.setDockIconImage(icon);
/* 727 */       System.setProperty("apple.laf.useScreenMenuBar", "true");
/* 728 */       System.setProperty("com.apple.mrj.application.apple.menu.about.name", "EXP Soundboard");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class JsonFileFilter
/*     */     extends FileFilter
/*     */   {
/*     */     private JsonFileFilter() {}
/*     */     
/*     */ 
/*     */     public boolean accept(File f)
/*     */     {
/* 741 */       if (f.isDirectory()) {
/* 742 */         return true;
/*     */       }
/* 744 */       if (f.getName().toLowerCase().endsWith(".json")) {
/* 745 */         return true;
/*     */       }
/* 747 */       return false;
/*     */     }
/*     */     
/*     */     public String getDescription()
/*     */     {
/* 752 */       return ".json Soundboard save file";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\gui\SoundboardFrame.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
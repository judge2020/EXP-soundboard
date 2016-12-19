/*     */ package exp.converter;
/*     */ 
/*     */ import exp.gui.SoundboardFrame;
/*     */ import exp.soundboard.Utils;
/*     */ import it.sauronsoftware.jave.EncoderProgressListener;
/*     */ import it.sauronsoftware.jave.MultimediaInfo;
/*     */ import java.awt.Container;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import net.miginfocom.swing.MigLayout;
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
/*     */ public class ConverterFrame
/*     */   extends JFrame
/*     */ {
/*     */   private static final long serialVersionUID = -6720455160041920802L;
/*     */   private File[] inputfiles;
/*     */   private File outputfile;
/*     */   private JLabel inputFileLabel;
/*     */   private JLabel outputFileLabel;
/*     */   private JButton changeOutputButton;
/*     */   private JRadioButton mp3RadioButton;
/*     */   private JRadioButton wavRadioButton;
/*     */   private JLabel encodingProgressLabel;
/*     */   private JLabel encodingMessageLabel;
/*     */   private JButton convertButton;
/*     */   
/*     */   public ConverterFrame()
/*     */   {
/*  51 */     setResizable(false);
/*  52 */     setDefaultCloseOperation(2);
/*  53 */     setTitle("EXP soundboard : Audio Converter");
/*  54 */     setIconImage(SoundboardFrame.icon);
/*     */     
/*  56 */     JLabel lblInputFile = new JLabel("Input Files:");
/*     */     
/*  58 */     this.inputFileLabel = new JLabel("none selected");
/*     */     
/*  60 */     JButton selectInputButton = new JButton("Select");
/*  61 */     selectInputButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/*  63 */         JFileChooser fc = Utils.getFileChooser();
/*  64 */         fc.setFileFilter(null);
/*  65 */         fc.setMultiSelectionEnabled(true);
/*  66 */         int session = fc.showDialog(null, "Select");
/*  67 */         if (session == 0) {
/*  68 */           ConverterFrame.this.inputfiles = fc.getSelectedFiles();
/*  69 */           if (ConverterFrame.this.inputfiles.length > 1) {
/*  70 */             ConverterFrame.this.outputfile = ConverterFrame.this.inputfiles[0];
/*  71 */             ConverterFrame.this.inputFileLabel.setText("Multiple files");
/*  72 */             ConverterFrame.this.renameOutputForFormat();
/*  73 */             ConverterFrame.this.outputFileLabel.setText(ConverterFrame.this.outputfile.getAbsolutePath());
/*     */           } else {
/*  75 */             ConverterFrame.this.outputfile = ConverterFrame.this.inputfiles[0];
/*  76 */             ConverterFrame.this.renameOutputForFormat();
/*  77 */             ConverterFrame.this.inputFileLabel.setText(ConverterFrame.this.inputfiles[0].getAbsolutePath());
/*  78 */             ConverterFrame.this.outputFileLabel.setText(ConverterFrame.this.outputfile.getAbsolutePath());
/*     */           }
/*  80 */           ConverterFrame.this.changeOutputButton.setEnabled(true);
/*  81 */           ConverterFrame.this.convertButton.setEnabled(true);
/*     */         }
/*  83 */         fc.setMultiSelectionEnabled(false);
/*  84 */         ConverterFrame.this.pack();
/*     */       }
/*     */       
/*  87 */     });
/*  88 */     JSeparator separator = new JSeparator();
/*     */     
/*  90 */     JLabel lblOutputFile = new JLabel("Output File:");
/*     */     
/*  92 */     this.outputFileLabel = new JLabel("none selected");
/*     */     
/*  94 */     this.changeOutputButton = new JButton("Change");
/*  95 */     this.changeOutputButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/*  97 */         if ((ConverterFrame.this.inputfiles != null) && (ConverterFrame.this.outputfile != null)) {
/*  98 */           JFileChooser fc = Utils.getFileChooser();
/*  99 */           fc.setMultiSelectionEnabled(false);
/* 100 */           if (ConverterFrame.this.inputfiles.length > 1) {
/* 101 */             fc.setFileFilter(new FileFilter()
/*     */             {
/*     */               public boolean accept(File f) {
/* 104 */                 return f.isDirectory();
/*     */               }
/*     */               
/*     */               public String getDescription() {
/* 108 */                 return "Folders only";
/*     */               }
/* 110 */             });
/* 111 */             fc.setSelectedFile(ConverterFrame.this.outputfile);
/* 112 */             fc.setFileSelectionMode(1);
/*     */           } else {
/* 114 */             fc.setFileFilter(null);
/* 115 */             fc.setSelectedFile(ConverterFrame.this.outputfile);
/*     */           }
/* 117 */           int session = fc.showSaveDialog(null);
/* 118 */           if (session == 0) {
/* 119 */             ConverterFrame.this.outputfile = fc.getSelectedFile();
/* 120 */             System.out.println("change: " + ConverterFrame.this.outputfile.getAbsolutePath());
/* 121 */             if (ConverterFrame.this.inputfiles.length < 2) {
/* 122 */               ConverterFrame.this.renameOutputForFormat();
/*     */             } else {
/* 124 */               ConverterFrame.this.outputFileLabel.setText(ConverterFrame.this.outputfile.getAbsolutePath());
/*     */             }
/*     */           }
/* 127 */           fc.setFileSelectionMode(0);
/* 128 */           ConverterFrame.this.pack();
/*     */         }
/*     */       }
/* 131 */     });
/* 132 */     this.changeOutputButton.setEnabled(false);
/*     */     
/* 134 */     JLabel lblOutputFormat = new JLabel("Output Format:");
/*     */     
/* 136 */     this.mp3RadioButton = new JRadioButton("MP3");
/* 137 */     this.mp3RadioButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 139 */         ConverterFrame.this.renameOutputForFormat();
/*     */       }
/* 141 */     });
/* 142 */     this.mp3RadioButton.setSelected(true);
/*     */     
/* 144 */     this.wavRadioButton = new JRadioButton("WAV");
/* 145 */     this.wavRadioButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 147 */         ConverterFrame.this.renameOutputForFormat();
/*     */       }
/*     */       
/* 150 */     });
/* 151 */     ButtonGroup buttonGroup = new ButtonGroup();
/* 152 */     buttonGroup.add(this.mp3RadioButton);
/* 153 */     buttonGroup.add(this.wavRadioButton);
/*     */     
/*     */ 
/* 156 */     this.convertButton = new JButton("Convert");
/* 157 */     this.convertButton.setEnabled(false);
/* 158 */     this.convertButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 160 */         ConverterFrame.this.convertAction();
/*     */       }
/*     */       
/* 163 */     });
/* 164 */     JSeparator separator_1 = new JSeparator();
/*     */     
/* 166 */     JLabel lblEncodingProgress = new JLabel("Encoding Progress:");
/*     */     
/* 168 */     JLabel lblEncodingMessages = new JLabel("Encoding Messages:");
/*     */     
/* 170 */     this.encodingProgressLabel = new JLabel("0%");
/*     */     
/* 172 */     this.encodingMessageLabel = new JLabel("");
/* 173 */     getContentPane().setLayout(new MigLayout("", "[45px][2px][14px][2px][34px][1px][33px][222px][71px]", "[14px][23px][2px][14px][23px][14px][23px][2px][14px][14px]"));
/* 174 */     getContentPane().add(separator_1, "cell 0 7 9 1,growx,aligny top");
/* 175 */     getContentPane().add(separator, "cell 0 2 9 1,growx,aligny top");
/* 176 */     getContentPane().add(lblInputFile, "cell 0 0 3 1,alignx left,aligny top");
/* 177 */     getContentPane().add(this.inputFileLabel, "cell 4 0 3 1,alignx right,aligny top");
/* 178 */     getContentPane().add(selectInputButton, "cell 0 1 3 1,alignx left,aligny top");
/* 179 */     getContentPane().add(lblOutputFile, "cell 0 3 3 1,alignx left,aligny top");
/* 180 */     getContentPane().add(this.outputFileLabel, "cell 4 3 3 1,alignx left,aligny top");
/* 181 */     getContentPane().add(this.changeOutputButton, "cell 0 4 5 1,alignx left,aligny top");
/* 182 */     getContentPane().add(lblOutputFormat, "cell 0 5 5 1,alignx left,aligny top");
/* 183 */     getContentPane().add(this.mp3RadioButton, "cell 0 6,alignx left,aligny top");
/* 184 */     getContentPane().add(this.wavRadioButton, "cell 2 6 3 1,alignx left,aligny top");
/* 185 */     getContentPane().add(this.convertButton, "cell 8 6,alignx left,aligny top");
/* 186 */     getContentPane().add(lblEncodingProgress, "cell 0 8 5 1,alignx right,aligny top");
/* 187 */     getContentPane().add(this.encodingProgressLabel, "cell 6 8,alignx left,aligny top");
/* 188 */     getContentPane().add(lblEncodingMessages, "cell 0 9 5 1,alignx right,aligny top");
/* 189 */     getContentPane().add(this.encodingMessageLabel, "cell 6 9,alignx left,aligny top");
/*     */     
/* 191 */     pack();
/* 192 */     setLocationRelativeTo(null);
/* 193 */     setVisible(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void convertAction()
/*     */   {
/* 202 */     boolean cont = true;
/* 203 */     if (this.outputfile.equals(this.inputfiles[0])) {
/* 204 */       JOptionPane.showMessageDialog(null, "Input and output files cannot be the same", "File parsing error", 1);
/* 205 */       cont = false;
/*     */     }
/* 207 */     if ((cont) && (this.outputfile.exists())) {
/* 208 */       int session = JOptionPane.showConfirmDialog(null, "Output file already exists. Overwrite?", 
/* 209 */         "Overwrite confirmation", 1);
/* 210 */       if (session != 0)
/*     */       {
/*     */ 
/* 213 */         cont = false;
/*     */       }
/*     */     }
/*     */     
/* 217 */     if (cont) {
/* 218 */       if (this.mp3RadioButton.isSelected()) {
/* 219 */         if (this.inputfiles.length > 1) {
/* 220 */           AudioConverter.batchConvertToMP3(this.inputfiles, this.outputfile, new ConvertProgressListener(null));
/*     */         } else {
/* 222 */           AudioConverter.convertToMP3(this.inputfiles[0], this.outputfile, new ConvertProgressListener(null));
/*     */         }
/* 224 */       } else if (this.wavRadioButton.isSelected()) {
/* 225 */         if (this.inputfiles.length > 1) {
/* 226 */           AudioConverter.batchConvertToWAV(this.inputfiles, this.outputfile, new ConvertProgressListener(null));
/*     */         } else {
/* 228 */           AudioConverter.convertToWAV(this.inputfiles[0], this.outputfile, new ConvertProgressListener(null));
/*     */         }
/*     */       }
/* 231 */       this.convertButton.setEnabled(false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void renameOutputForFormat()
/*     */   {
/* 239 */     if (this.inputfiles.length > 1) {
/* 240 */       if (!this.outputfile.isDirectory()) {
/* 241 */         String inputabs = this.inputfiles[0].getAbsolutePath();
/* 242 */         int slash = inputabs.lastIndexOf(File.separator);
/* 243 */         String output = inputabs.substring(0, slash + 1) + "Converted";
/* 244 */         this.outputfile = new File(output);
/* 245 */         this.outputFileLabel.setText(this.outputfile.getAbsolutePath());
/*     */       }
/*     */     } else {
/* 248 */       String outputfileabs = this.outputfile.getAbsolutePath();
/* 249 */       int period = outputfileabs.lastIndexOf('.');
/* 250 */       if (period > 0) {
/* 251 */         outputfileabs = outputfileabs.substring(0, period);
/* 252 */         if (this.mp3RadioButton.isSelected()) {
/* 253 */           outputfileabs = outputfileabs + ".mp3";
/* 254 */         } else if (this.wavRadioButton.isSelected()) {
/* 255 */           outputfileabs = outputfileabs + ".wav";
/*     */         }
/*     */       }
/* 258 */       this.outputfile = new File(outputfileabs);
/* 259 */       this.outputFileLabel.setText(this.outputfile.getAbsolutePath());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private class ConvertProgressListener
/*     */     implements EncoderProgressListener
/*     */   {
/* 270 */     int current = 1;
/*     */     
/*     */     private ConvertProgressListener() {}
/*     */     
/* 274 */     public void message(String m) { if ((ConverterFrame.this.inputfiles.length > 1) && 
/* 275 */         (this.current < ConverterFrame.this.inputfiles.length)) {
/* 276 */         ConverterFrame.this.encodingMessageLabel.setText(this.current + "/" + ConverterFrame.this.inputfiles.length);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void progress(int p)
/*     */     {
/* 283 */       float progress = p / 10;
/* 284 */       ConverterFrame.this.encodingProgressLabel.setText(progress + "%");
/* 285 */       if (p >= 1000) {
/* 286 */         if (ConverterFrame.this.inputfiles.length > 1) {
/* 287 */           this.current += 1;
/* 288 */           if (this.current > ConverterFrame.this.inputfiles.length) {
/* 289 */             ConverterFrame.this.encodingMessageLabel.setText("Encoding Complete!");
/* 290 */             ConverterFrame.this.convertButton.setEnabled(true);
/*     */           }
/*     */         }
/* 293 */         else if (p == 1001) {
/* 294 */           ConverterFrame.this.encodingMessageLabel.setText("Encoding Failed!");
/* 295 */           ConverterFrame.this.convertButton.setEnabled(true);
/*     */         } else {
/* 297 */           ConverterFrame.this.encodingMessageLabel.setText("Encoding Complete!");
/* 298 */           ConverterFrame.this.convertButton.setEnabled(true);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void sourceInfo(MultimediaInfo m) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\converter\ConverterFrame.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
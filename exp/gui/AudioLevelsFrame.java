/*     */ package exp.gui;
/*     */ 
/*     */ import exp.soundboard.AudioManager;
/*     */ import exp.soundboard.Utils;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import javax.swing.GroupLayout;
/*     */ import javax.swing.GroupLayout.Alignment;
/*     */ import javax.swing.GroupLayout.ParallelGroup;
/*     */ import javax.swing.GroupLayout.SequentialGroup;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.JSlider;
/*     */ import javax.swing.LayoutStyle.ComponentPlacement;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AudioLevelsFrame
/*     */   extends JFrame
/*     */ {
/*  27 */   private static AudioLevelsFrame instance = null;
/*     */   private static final long serialVersionUID = 464347549019590824L;
/*     */   private JSlider primarySlider;
/*     */   private JSlider secondarySlider;
/*     */   private JSlider micinjectorSlider;
/*     */   
/*     */   private AudioLevelsFrame()
/*     */   {
/*  35 */     setTitle("Audio Gain Controls");
/*  36 */     setResizable(false);
/*  37 */     setDefaultCloseOperation(2);
/*  38 */     setIconImage(SoundboardFrame.icon);
/*     */     
/*  40 */     JLabel lblPrimaryOutputGain = new JLabel("Primary Output Gain:");
/*     */     
/*  42 */     int primaryGain = (int)AudioManager.getFirstOutputGain();
/*  43 */     int secondaryGain = (int)AudioManager.getSecondOutputGain();
/*  44 */     int micInjectorGain = (int)Utils.getMicInjectorGain();
/*     */     
/*  46 */     this.primarySlider = new JSlider();
/*  47 */     this.primarySlider.addChangeListener(new ChangeListener() {
/*     */       public void stateChanged(ChangeEvent e) {
/*  49 */         if (!AudioLevelsFrame.this.primarySlider.getValueIsAdjusting()) {
/*  50 */           float gain = AudioLevelsFrame.this.primarySlider.getValue();
/*  51 */           AudioManager.setFirstOutputGain(gain);
/*     */         }
/*     */       }
/*  54 */     });
/*  55 */     this.primarySlider.setMajorTickSpacing(6);
/*  56 */     this.primarySlider.setPaintLabels(true);
/*  57 */     this.primarySlider.setPaintTicks(true);
/*  58 */     this.primarySlider.setSnapToTicks(true);
/*  59 */     this.primarySlider.setMinorTickSpacing(1);
/*  60 */     this.primarySlider.setValue(0);
/*  61 */     this.primarySlider.setMinimum(-66);
/*  62 */     this.primarySlider.setMaximum(6);
/*     */     
/*  64 */     JSeparator separator = new JSeparator();
/*  65 */     separator.setForeground(Color.BLACK);
/*     */     
/*  67 */     JLabel lblSecondaryOutputGain = new JLabel("Secondary Output Gain:");
/*     */     
/*  69 */     this.secondarySlider = new JSlider();
/*  70 */     this.secondarySlider.addChangeListener(new ChangeListener() {
/*     */       public void stateChanged(ChangeEvent arg0) {
/*  72 */         if (!AudioLevelsFrame.this.secondarySlider.getValueIsAdjusting()) {
/*  73 */           float gain = AudioLevelsFrame.this.secondarySlider.getValue();
/*  74 */           AudioManager.setSecondOutputGain(gain);
/*     */         }
/*     */       }
/*  77 */     });
/*  78 */     this.secondarySlider.setValue(0);
/*  79 */     this.secondarySlider.setSnapToTicks(true);
/*  80 */     this.secondarySlider.setPaintTicks(true);
/*  81 */     this.secondarySlider.setPaintLabels(true);
/*  82 */     this.secondarySlider.setMinorTickSpacing(1);
/*  83 */     this.secondarySlider.setMinimum(-66);
/*  84 */     this.secondarySlider.setMaximum(6);
/*  85 */     this.secondarySlider.setMajorTickSpacing(6);
/*     */     
/*  87 */     JSeparator separator_1 = new JSeparator();
/*  88 */     separator_1.setForeground(Color.BLACK);
/*     */     
/*  90 */     JLabel lblMicInjectorGain = new JLabel("Mic Injector Gain:");
/*     */     
/*  92 */     this.micinjectorSlider = new JSlider();
/*  93 */     this.micinjectorSlider.addChangeListener(new ChangeListener() {
/*     */       public void stateChanged(ChangeEvent arg0) {
/*  95 */         if (!AudioLevelsFrame.this.micinjectorSlider.getValueIsAdjusting()) {
/*  96 */           float gain = AudioLevelsFrame.this.micinjectorSlider.getValue();
/*  97 */           Utils.setMicInjectorGain(gain);
/*     */         }
/*     */       }
/* 100 */     });
/* 101 */     this.micinjectorSlider.setValue(0);
/* 102 */     this.micinjectorSlider.setSnapToTicks(true);
/* 103 */     this.micinjectorSlider.setPaintTicks(true);
/* 104 */     this.micinjectorSlider.setPaintLabels(true);
/* 105 */     this.micinjectorSlider.setMinorTickSpacing(1);
/* 106 */     this.micinjectorSlider.setMinimum(-66);
/* 107 */     this.micinjectorSlider.setMaximum(6);
/* 108 */     this.micinjectorSlider.setMajorTickSpacing(6);
/* 109 */     GroupLayout groupLayout = new GroupLayout(getContentPane());
/* 110 */     groupLayout.setHorizontalGroup(
/* 111 */       groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
/* 112 */       .addGroup(groupLayout.createSequentialGroup()
/* 113 */       .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
/* 114 */       .addGroup(groupLayout.createSequentialGroup()
/* 115 */       .addContainerGap()
/* 116 */       .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
/* 117 */       .addComponent(separator, -1, 424, 32767)
/* 118 */       .addComponent(this.primarySlider, -1, 424, 32767)
/* 119 */       .addComponent(lblPrimaryOutputGain)
/* 120 */       .addComponent(lblSecondaryOutputGain)
/* 121 */       .addComponent(this.secondarySlider, -2, 424, -2)))
/* 122 */       .addGroup(groupLayout.createSequentialGroup()
/* 123 */       .addGap(11)
/* 124 */       .addComponent(separator_1, -1, 423, 32767))
/* 125 */       .addGroup(groupLayout.createSequentialGroup()
/* 126 */       .addContainerGap()
/* 127 */       .addComponent(lblMicInjectorGain))
/* 128 */       .addGroup(groupLayout.createSequentialGroup()
/* 129 */       .addContainerGap()
/* 130 */       .addComponent(this.micinjectorSlider, -2, 424, -2)))
/* 131 */       .addContainerGap()));
/*     */     
/* 133 */     groupLayout.setVerticalGroup(
/* 134 */       groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
/* 135 */       .addGroup(groupLayout.createSequentialGroup()
/* 136 */       .addContainerGap()
/* 137 */       .addComponent(lblPrimaryOutputGain)
/* 138 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
/* 139 */       .addComponent(this.primarySlider, -2, -1, -2)
/* 140 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
/* 141 */       .addComponent(separator, -2, 2, -2)
/* 142 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
/* 143 */       .addComponent(lblSecondaryOutputGain)
/* 144 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
/* 145 */       .addComponent(this.secondarySlider, -2, 45, -2)
/* 146 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
/* 147 */       .addComponent(separator_1, -2, 2, -2)
/* 148 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
/* 149 */       .addComponent(lblMicInjectorGain)
/* 150 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
/* 151 */       .addComponent(this.micinjectorSlider, -2, 45, -2)
/* 152 */       .addContainerGap(38, 32767)));
/*     */     
/* 154 */     getContentPane().setLayout(groupLayout);
/*     */     
/* 156 */     this.primarySlider.setValue(primaryGain);
/* 157 */     this.secondarySlider.setValue(secondaryGain);
/* 158 */     this.micinjectorSlider.setValue(micInjectorGain);
/*     */     
/* 160 */     pack();
/* 161 */     setVisible(true);
/*     */   }
/*     */   
/*     */   public void dispose()
/*     */   {
/* 166 */     super.dispose();
/* 167 */     instance = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static AudioLevelsFrame getInstance()
/*     */   {
/* 175 */     if (instance == null) {
/* 176 */       instance = new AudioLevelsFrame();
/*     */     } else {
/* 178 */       instance.setVisible(true);
/* 179 */       instance.requestFocus();
/*     */     }
/* 181 */     return instance;
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\gui\AudioLevelsFrame.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
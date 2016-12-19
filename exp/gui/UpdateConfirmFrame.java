/*     */ package exp.gui;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.Desktop;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import javax.swing.GroupLayout;
/*     */ import javax.swing.GroupLayout.Alignment;
/*     */ import javax.swing.GroupLayout.ParallelGroup;
/*     */ import javax.swing.GroupLayout.SequentialGroup;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextPane;
/*     */ import javax.swing.LayoutStyle.ComponentPlacement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UpdateConfirmFrame
/*     */   extends JFrame
/*     */ {
/*     */   private static final long serialVersionUID = -6700862565543741036L;
/*     */   private static final String url = "https://sourceforge.net/projects/expsoundboard/";
/*     */   private JTextPane textPane;
/*     */   
/*     */   public UpdateConfirmFrame(String updateNotes)
/*     */   {
/*  36 */     setResizable(false);
/*  37 */     setDefaultCloseOperation(2);
/*  38 */     setTitle("Update Available!");
/*     */     
/*  40 */     JLabel lblSoundboardUpdateAvailable = new JLabel("EXP Soundboard Update Available");
/*     */     
/*  42 */     JScrollPane scrollPane = new JScrollPane();
/*     */     
/*  44 */     JButton btnClose = new JButton("Close");
/*  45 */     btnClose.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent arg0) {
/*  47 */         UpdateConfirmFrame.this.dispose();
/*     */       }
/*     */       
/*  50 */     });
/*  51 */     JButton btnGetUpdate = new JButton("Get Update");
/*  52 */     btnGetUpdate.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*     */         try {
/*  55 */           Desktop.getDesktop().browse(new URI("https://sourceforge.net/projects/expsoundboard/"));
/*     */         } catch (IOException e1) {
/*  57 */           e1.printStackTrace();
/*     */         } catch (URISyntaxException e1) {
/*  59 */           e1.printStackTrace();
/*     */         }
/*  61 */         UpdateConfirmFrame.this.dispose();
/*     */       }
/*     */       
/*  64 */     });
/*  65 */     final JCheckBox chckbxCheckForUpdates = new JCheckBox("Check for Updates on launch");
/*  66 */     chckbxCheckForUpdates.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/*  68 */         SoundboardFrame.updateCheck = !SoundboardFrame.updateCheck;
/*  69 */         chckbxCheckForUpdates.setSelected(SoundboardFrame.updateCheck);
/*     */       }
/*  71 */     });
/*  72 */     chckbxCheckForUpdates.setSelected(SoundboardFrame.updateCheck);
/*  73 */     GroupLayout groupLayout = new GroupLayout(getContentPane());
/*  74 */     groupLayout.setHorizontalGroup(
/*  75 */       groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
/*  76 */       .addGroup(groupLayout.createSequentialGroup()
/*  77 */       .addContainerGap()
/*  78 */       .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
/*  79 */       .addComponent(scrollPane, -1, 480, 32767)
/*  80 */       .addComponent(lblSoundboardUpdateAvailable)
/*  81 */       .addGroup(groupLayout.createSequentialGroup()
/*  82 */       .addComponent(chckbxCheckForUpdates)
/*  83 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 161, 32767)
/*  84 */       .addComponent(btnGetUpdate)
/*  85 */       .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
/*  86 */       .addComponent(btnClose)))
/*  87 */       .addContainerGap()));
/*     */     
/*  89 */     groupLayout.setVerticalGroup(
/*  90 */       groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
/*  91 */       .addGroup(groupLayout.createSequentialGroup()
/*  92 */       .addContainerGap()
/*  93 */       .addComponent(lblSoundboardUpdateAvailable)
/*  94 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
/*  95 */       .addComponent(scrollPane, -2, 124, -2)
/*  96 */       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
/*  97 */       .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
/*  98 */       .addComponent(btnClose)
/*  99 */       .addComponent(chckbxCheckForUpdates)
/* 100 */       .addComponent(btnGetUpdate))
/* 101 */       .addContainerGap(78, 32767)));
/*     */     
/*     */ 
/* 104 */     this.textPane = new JTextPane();
/* 105 */     this.textPane.setEditable(false);
/* 106 */     this.textPane.setText(updateNotes);
/* 107 */     scrollPane.setViewportView(this.textPane);
/* 108 */     getContentPane().setLayout(groupLayout);
/* 109 */     pack();
/* 110 */     setLocationRelativeTo(null);
/* 111 */     setVisible(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\gui\UpdateConfirmFrame.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
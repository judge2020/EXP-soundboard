/*     */ package exp.soundboard;
/*     */ 
/*     */ import exp.gui.UpdateConfirmFrame;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.SwingUtilities;
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
/*     */ public class UpdateChecker
/*     */   implements Runnable
/*     */ {
/*     */   private static final String updatelink = "http://sourceforge.net/projects/expsoundboard/files/";
/*     */   
/*     */   public static String getUpdateNotes()
/*     */   {
/*  37 */     boolean internetconnection = false;
/*  38 */     BufferedReader reader = null;
/*     */     try {
/*  40 */       URL url = new URL("http://sourceforge.net/projects/expsoundboard/files/");
/*  41 */       reader = new BufferedReader(new InputStreamReader(url.openStream()));
/*  42 */       internetconnection = true;
/*     */     } catch (MalformedURLException ex) {
/*  44 */       Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
/*     */     } catch (IOException ex) {
/*  46 */       Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
/*     */     }
/*     */     
/*  49 */     if (internetconnection) {
/*  50 */       System.out.println("UpdateChecker: System has Internet Connection.");
/*  51 */       boolean versionfound = false;
/*     */       
/*  53 */       String patchlist = "";
/*  54 */       boolean changelogFound = false;
/*     */       try { String line;
/*  56 */         while ((line = reader.readLine()) != null) { String line;
/*  57 */           if (!changelogFound) {
/*  58 */             if (line.startsWith("CHANGELOG")) {
/*  59 */               changelogFound = true;
/*     */             }
/*  61 */           } else if (changelogFound) {
/*  62 */             if ((line.startsWith("vers.")) && (!versionfound)) {
/*  63 */               versionfound = true;
/*  64 */               patchlist = patchlist + '\n' + line;
/*  65 */             } else { if ((versionfound) && (line.startsWith("vers."))) {
/*  66 */                 reader.close();
/*  67 */                 return patchlist;
/*     */               }
/*  69 */               patchlist = patchlist + '\n' + line;
/*     */             }
/*     */           }
/*     */         }
/*  73 */         reader.close();
/*     */       } catch (IOException ex) {
/*  75 */         Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
/*     */       }
/*     */     }
/*  78 */     System.out.println("UpdateChecker: System does not have Internet Connection.");
/*     */     
/*  80 */     return "Update notes could not be found";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isUpdateAvailable()
/*     */   {
/*  90 */     boolean internetconnection = false;
/*  91 */     BufferedReader reader = null;
/*     */     try {
/*  93 */       URL url = new URL("http://sourceforge.net/projects/expsoundboard/files/");
/*  94 */       reader = new BufferedReader(new InputStreamReader(url.openStream()));
/*  95 */       internetconnection = true;
/*     */     } catch (MalformedURLException ex) {
/*  97 */       Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
/*     */     } catch (IOException ex) {
/*  99 */       Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
/*     */     }
/*     */     
/* 102 */     if (internetconnection) {
/* 103 */       System.out.println("UpdateChecker: System has Internet Connection.");
/*     */       
/* 105 */       boolean changelogFound = false;
/*     */       try { String line;
/* 107 */         while ((line = reader.readLine()) != null) { String line;
/* 108 */           if (!changelogFound) {
/* 109 */             if (line.startsWith("CHANGELOG")) {
/* 110 */               changelogFound = true;
/*     */             }
/* 112 */           } else if ((changelogFound) && 
/* 113 */             (line.startsWith("vers."))) {
/* 114 */             String version = line.substring(line.indexOf('.') + 1, line.lastIndexOf(':')).trim();
/* 115 */             float versionNo = Float.parseFloat(version);
/* 116 */             if (versionNo > 0.5F) {
/* 117 */               System.out.println("UpdateChecker: New version available!");
/* 118 */               reader.close();
/* 119 */               return true;
/*     */             }
/* 121 */             System.out.println("UpdateChecker: Currently up to date!");
/* 122 */             reader.close();
/* 123 */             return false;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 128 */         reader.close();
/*     */       } catch (IOException ex) {
/* 130 */         Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
/*     */       }
/*     */     }
/* 133 */     System.out.println("UpdateChecker: System does not have Internet Connection.");
/*     */     
/* 135 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/* 143 */     if (isUpdateAvailable()) {
/* 144 */       SwingUtilities.invokeLater(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 148 */           new UpdateConfirmFrame(UpdateChecker.getUpdateNotes());
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\soundboard\UpdateChecker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
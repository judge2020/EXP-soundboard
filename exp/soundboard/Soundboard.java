/*     */ package exp.soundboard;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
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
/*     */ public class Soundboard
/*     */ {
/*     */   private ArrayList<SoundboardEntry> soundboardEntries;
/*  26 */   private static ArrayList<SoundboardEntry> soundboardEntriesClone = new ArrayList();
/*  27 */   private static boolean containsPPTKey = false;
/*  28 */   private static ArrayList<Integer> pttKeysClone = new ArrayList();
/*     */   
/*     */   public Soundboard() {
/*  31 */     this.soundboardEntries = new ArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object[][] getEntriesAsObjectArrayForTable()
/*     */   {
/*  39 */     Object[][] array = new Object[this.soundboardEntries.size()][4];
/*  40 */     for (int i = 0; i < array.length; i++) {
/*  41 */       SoundboardEntry entry = (SoundboardEntry)this.soundboardEntries.get(i);
/*  42 */       array[i][0] = entry.getFileName();
/*  43 */       array[i][1] = entry.getActivationKeysAsReadableString();
/*  44 */       array[i][2] = entry.getFileString();
/*  45 */       array[i][3] = Integer.valueOf(i);
/*     */     }
/*  47 */     return array;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addEntry(File file, int[] keyNumbers)
/*     */   {
/*  56 */     this.soundboardEntries.add(new SoundboardEntry(file, keyNumbers));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SoundboardEntry getEntry(String filename)
/*     */   {
/*  65 */     for (SoundboardEntry entry : this.soundboardEntries) {
/*  66 */       if (entry.getFileName().equals(filename)) {
/*  67 */         return entry;
/*     */       }
/*     */     }
/*  70 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeEntry(int index)
/*     */   {
/*  78 */     this.soundboardEntries.remove(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeEntry(String filename)
/*     */   {
/*  87 */     for (SoundboardEntry entry : this.soundboardEntries) {
/*  88 */       if (entry.getFileName().equals(filename)) {
/*  89 */         this.soundboardEntries.remove(entry);
/*  90 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayList<SoundboardEntry> getSoundboardEntries()
/*     */   {
/* 100 */     return this.soundboardEntries;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public File saveAsJsonFile(File file)
/*     */   {
/* 110 */     String filestring = file.getAbsolutePath();
/* 111 */     System.out.println(filestring);
/* 112 */     if (filestring.contains(".")) {
/* 113 */       filestring = filestring.substring(0, filestring.lastIndexOf('.'));
/*     */     }
/* 115 */     filestring = filestring + ".json";
/* 116 */     System.out.println("amended: " + filestring);
/* 117 */     Gson gson = new Gson();
/*     */     
/* 119 */     String json = gson.toJson(this);
/* 120 */     File realfile = new File(filestring);
/* 121 */     BufferedWriter writer = null;
/*     */     try {
/* 123 */       writer = new BufferedWriter(new FileWriter(realfile));
/* 124 */       writer.write(json);
/* 125 */       writer.close();
/*     */     } catch (IOException e) {
/* 127 */       e.printStackTrace();
/*     */     }
/* 129 */     return realfile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Soundboard loadFromJsonFile(File file)
/*     */   {
/* 138 */     BufferedReader br = null;
/*     */     try {
/* 140 */       br = new BufferedReader(new FileReader(file));
/*     */     } catch (FileNotFoundException e) {
/* 142 */       e.printStackTrace();
/*     */     }
/* 144 */     Gson json = new Gson();
/*     */     
/*     */ 
/* 147 */     Soundboard sb = (Soundboard)json.fromJson(br, Soundboard.class);
/*     */     try {
/* 149 */       br.close();
/*     */     } catch (IOException e) {
/* 151 */       e.printStackTrace();
/*     */     }
/* 153 */     return sb;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SoundboardEntry getEntry(int index)
/*     */   {
/*     */     try
/*     */     {
/* 163 */       return (SoundboardEntry)this.soundboardEntries.get(index);
/*     */     } catch (ArrayIndexOutOfBoundsException e) {
/* 165 */       e.printStackTrace();
/*     */     }
/*     */     
/* 168 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean entriesContainPTTKeys(ArrayList<Integer> pttkeys)
/*     */   {
/* 178 */     if ((!pttkeys.equals(pttKeysClone)) || (hasSoundboardChanged())) {
/* 179 */       soundboardEntriesClone = (ArrayList)this.soundboardEntries.clone();
/* 180 */       pttKeysClone = (ArrayList)pttkeys.clone();
/* 181 */       String key = null;
/* 182 */       int j; int i; for (Iterator localIterator1 = this.soundboardEntries.iterator(); localIterator1.hasNext(); 
/* 183 */           i < j)
/*     */       {
/* 182 */         SoundboardEntry entry = (SoundboardEntry)localIterator1.next();
/* 183 */         int[] arrayOfInt; j = (arrayOfInt = entry.getActivationKeys()).length;i = 0; continue;int actKey = arrayOfInt[i];
/* 184 */         key = NativeKeyEvent.getKeyText(actKey).toLowerCase();
/* 185 */         for (Iterator localIterator2 = pttkeys.iterator(); localIterator2.hasNext();) { int i = ((Integer)localIterator2.next()).intValue();
/* 186 */           if (key.equals(KeyEventIntConverter.getKeyEventText(i).toLowerCase())) {
/* 187 */             containsPPTKey = true;
/* 188 */             return true;
/*     */           }
/*     */         }
/* 183 */         i++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 193 */       containsPPTKey = false;
/* 194 */       return false; }
/* 195 */     if (containsPPTKey) {
/* 196 */       return true;
/*     */     }
/* 198 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasSoundboardChanged()
/*     */   {
/* 207 */     if (!this.soundboardEntries.equals(soundboardEntriesClone)) {
/* 208 */       System.out.println("Soundboard changed");
/* 209 */       return true;
/*     */     }
/* 211 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\soundboard\Soundboard.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
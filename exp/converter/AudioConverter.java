/*     */ package exp.converter;
/*     */ 
/*     */ import it.sauronsoftware.jave.AudioAttributes;
/*     */ import it.sauronsoftware.jave.Encoder;
/*     */ import it.sauronsoftware.jave.EncoderException;
/*     */ import it.sauronsoftware.jave.EncoderProgressListener;
/*     */ import it.sauronsoftware.jave.EncodingAttributes;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import javax.swing.JOptionPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AudioConverter
/*     */ {
/*     */   private static final String mp3 = "libmp3lame";
/*     */   private static final String wav = "pcm_s16le";
/*  24 */   private static final Integer mp3bitrate = new Integer(256000);
/*  25 */   private static final Integer channels = new Integer(2);
/*  26 */   private static final Integer samplerate = new Integer(44100);
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
/*     */   public static void batchConvertToMP3(File[] inputfiles, final File outputfolder, final EncoderProgressListener listener)
/*     */   {
/*  39 */     new Thread(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         File[] arrayOfFile;
/*  33 */         int j = (arrayOfFile = AudioConverter.this).length; for (int i = 0; i < j; i++) { File input = arrayOfFile[i];
/*  34 */           File output = AudioConverter.getAbsoluteForOutputExtensionAndFolder(input, outputfolder, ".mp3");
/*  35 */           System.out.println("processing: " + output.getAbsolutePath());
/*  36 */           AudioConverter.mp3(input, output, listener);
/*     */         }
/*     */       }
/*     */     })
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  39 */       .start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void batchConvertToWAV(File[] inputfiles, final File outputfolder, final EncoderProgressListener listener)
/*     */   {
/*  52 */     new Thread(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         File[] arrayOfFile;
/*  46 */         int j = (arrayOfFile = AudioConverter.this).length; for (int i = 0; i < j; i++) { File input = arrayOfFile[i];
/*  47 */           File output = AudioConverter.getAbsoluteForOutputExtensionAndFolder(input, outputfolder, ".wav");
/*  48 */           System.out.println("processing: " + output.getAbsolutePath());
/*  49 */           AudioConverter.wav(input, output, listener);
/*     */         }
/*     */       }
/*     */     })
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */       .start();
/*     */   }
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
/*     */   public static void convertToMP3(File inputfile, final File outputfile, final EncoderProgressListener listener)
/*     */   {
/*  67 */     new Thread(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*  65 */         AudioConverter.mp3(AudioConverter.this, outputfile, listener);
/*     */       }
/*     */     })
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  67 */       .start();
/*     */   }
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
/*     */   public static void convertToWAV(File inputfile, final File outputfile, final EncoderProgressListener listener)
/*     */   {
/*  82 */     new Thread(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*  80 */         AudioConverter.wav(AudioConverter.this, outputfile, listener);
/*     */       }
/*     */     })
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  82 */       .start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void mp3(File inputfile, File outputfile, EncoderProgressListener listener)
/*     */   {
/*  92 */     AudioAttributes audio = new AudioAttributes();
/*  93 */     audio.setCodec("libmp3lame");
/*  94 */     audio.setBitRate(mp3bitrate);
/*  95 */     audio.setChannels(channels);
/*  96 */     audio.setSamplingRate(samplerate);
/*  97 */     EncodingAttributes attrs = new EncodingAttributes();
/*  98 */     attrs.setFormat("mp3");
/*  99 */     attrs.setAudioAttributes(audio);
/* 100 */     Encoder encoder = new Encoder();
/*     */     try {
/* 102 */       if (listener != null) {
/* 103 */         encoder.encode(inputfile, outputfile, attrs, listener);
/*     */       } else {
/* 105 */         encoder.encode(inputfile, outputfile, attrs);
/*     */       }
/*     */     } catch (IllegalArgumentException|EncoderException e) {
/* 108 */       JOptionPane.showMessageDialog(null, "Input file formatting/encoding is incompatible\n" + inputfile.getName(), 
/* 109 */         "Input File incompatible", 0);
/* 110 */       listener.progress(1001);
/* 111 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void wav(File inputfile, File outputfile, EncoderProgressListener listener)
/*     */   {
/* 122 */     AudioAttributes audio = new AudioAttributes();
/* 123 */     audio.setCodec("pcm_s16le");
/* 124 */     EncodingAttributes attrs = new EncodingAttributes();
/* 125 */     attrs.setFormat("wav");
/* 126 */     attrs.setAudioAttributes(audio);
/* 127 */     Encoder encoder = new Encoder();
/*     */     try {
/* 129 */       if (listener != null) {
/* 130 */         encoder.encode(inputfile, outputfile, attrs, listener);
/*     */       } else {
/* 132 */         encoder.encode(inputfile, outputfile, attrs);
/*     */       }
/*     */     } catch (IllegalArgumentException|EncoderException e) {
/* 135 */       JOptionPane.showMessageDialog(null, "Input file formatting/encoding is incompatible\n" + inputfile.getName(), 
/* 136 */         "Input File incompatible", 0);
/* 137 */       listener.progress(1001);
/* 138 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static File getAbsoluteForOutputExtensionAndFolder(File inputfile, File outputfolder, String dotext)
/*     */   {
/* 151 */     String filename = inputfile.getName();
/* 152 */     int period = filename.lastIndexOf('.');
/* 153 */     if (period > 0) {
/* 154 */       filename = filename.substring(0, period) + dotext;
/*     */     }
/* 156 */     return new File(outputfolder + File.separator + filename);
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\converter\AudioConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
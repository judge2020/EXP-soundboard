/*     */ package org.eclipse.jdt.internal.jarinjarloader;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Manifest;
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
/*     */ public class JarRsrcLoader
/*     */ {
/*     */   private static class ManifestInfo
/*     */   {
/*     */     String rsrcMainClass;
/*     */     String[] rsrcClassPath;
/*     */     
/*     */     private ManifestInfo() {}
/*     */     
/*     */     ManifestInfo(ManifestInfo paramManifestInfo)
/*     */     {
/*  37 */       this();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, IOException
/*     */   {
/*  43 */     ManifestInfo mi = getManifestInfo();
/*  44 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*  45 */     URL.setURLStreamHandlerFactory(new RsrcURLStreamHandlerFactory(cl));
/*  46 */     URL[] rsrcUrls = new URL[mi.rsrcClassPath.length];
/*  47 */     for (int i = 0; i < mi.rsrcClassPath.length; i++) {
/*  48 */       String rsrcPath = mi.rsrcClassPath[i];
/*  49 */       if (rsrcPath.endsWith("/")) {
/*  50 */         rsrcUrls[i] = new URL("rsrc:" + rsrcPath);
/*     */       } else
/*  52 */         rsrcUrls[i] = new URL("jar:rsrc:" + rsrcPath + "!/");
/*     */     }
/*  54 */     ClassLoader jceClassLoader = new URLClassLoader(rsrcUrls, null);
/*  55 */     Thread.currentThread().setContextClassLoader(jceClassLoader);
/*  56 */     Class c = Class.forName(mi.rsrcMainClass, true, jceClassLoader);
/*  57 */     Method main = c.getMethod("main", new Class[] { args.getClass() });
/*  58 */     main.invoke(null, new Object[] { args });
/*     */   }
/*     */   
/*     */   private static ManifestInfo getManifestInfo() throws IOException
/*     */   {
/*  63 */     Enumeration resEnum = Thread.currentThread().getContextClassLoader().getResources("META-INF/MANIFEST.MF");
/*  64 */     while (resEnum.hasMoreElements()) {
/*     */       try {
/*  66 */         URL url = (URL)resEnum.nextElement();
/*  67 */         InputStream is = url.openStream();
/*  68 */         if (is != null) {
/*  69 */           ManifestInfo result = new ManifestInfo(null);
/*  70 */           Manifest manifest = new Manifest(is);
/*  71 */           Attributes mainAttribs = manifest.getMainAttributes();
/*  72 */           result.rsrcMainClass = mainAttribs.getValue("Rsrc-Main-Class");
/*  73 */           String rsrcCP = mainAttribs.getValue("Rsrc-Class-Path");
/*  74 */           if (rsrcCP == null)
/*  75 */             rsrcCP = "";
/*  76 */           result.rsrcClassPath = splitSpaces(rsrcCP);
/*  77 */           if ((result.rsrcMainClass != null) && (!result.rsrcMainClass.trim().equals(""))) {
/*  78 */             return result;
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*  85 */     System.err.println("Missing attributes for JarRsrcLoader in Manifest (Rsrc-Main-Class, Rsrc-Class-Path)");
/*  86 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String[] splitSpaces(String line)
/*     */   {
/*  97 */     if (line == null)
/*  98 */       return null;
/*  99 */     List result = new ArrayList();
/* 100 */     int firstPos = 0;
/* 101 */     while (firstPos < line.length()) {
/* 102 */       int lastPos = line.indexOf(' ', firstPos);
/* 103 */       if (lastPos == -1)
/* 104 */         lastPos = line.length();
/* 105 */       if (lastPos > firstPos) {
/* 106 */         result.add(line.substring(firstPos, lastPos));
/*     */       }
/* 108 */       firstPos = lastPos + 1;
/*     */     }
/* 110 */     return (String[])result.toArray(new String[result.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\org\eclipse\jdt\internal\jarinjarloader\JarRsrcLoader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
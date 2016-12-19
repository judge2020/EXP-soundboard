/*    */ package exp.soundboard;
/*    */ 
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.event.KeyEvent;
/*    */ 
/*    */ public class KeyEventIntConverter
/*    */ {
/*    */   public static String getKeyEventText(int keyCode)
/*    */   {
/* 10 */     if ((keyCode >= 96) && (keyCode <= 105)) {
/* 11 */       String numpad = Toolkit.getProperty("AWT.numpad", "NumPad");
/* 12 */       char c = (char)(keyCode - 96 + 48);
/* 13 */       return numpad + " " + c;
/*    */     }
/* 15 */     return KeyEvent.getKeyText(keyCode);
/*    */   }
/*    */ }


/* Location:              C:\Users\hunte\Downloads\EXP Soundboard_05.jar!\exp\soundboard\KeyEventIntConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
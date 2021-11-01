/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Felipe Costa
 */
public class SoNumerosEPonto extends PlainDocument{
    @Override
    public void insertString(int offSet,String str, javax.swing.text.AttributeSet attr)
    throws BadLocationException{
        super.insertString(offSet, str.replaceAll("[^0-9|^.]",""), attr);
    
    }
    public void replace(int offSet,String str, javax.swing.text.AttributeSet attr)
    throws BadLocationException{
        super.insertString(offSet, str.replaceAll("[^0-9|^.]",""), attr);
    
    }
    
}

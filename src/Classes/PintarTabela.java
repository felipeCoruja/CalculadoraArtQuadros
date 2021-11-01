/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Felipe Costa
 */
public class PintarTabela extends DefaultTableCellRenderer{
public PintarTabela(){}

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); //To change body of generated methods, choose Tools | Templates.
    
    
        Color background; 
        Color foreground;
        
        
        
        
        if(hasFocus){
            foreground = Color.black;//cor do texto  
            background = Color.PINK;  
        }else{
            if(row %2 == 0){
            background = Color.white;
            foreground = Color.black;
            
            }else{
                background = Color.lightGray;
                foreground = Color.black;
            }
        }
        
        label.setBackground(background);
        label.setForeground(foreground);
       
                    
        
        return label;
    }
    
}

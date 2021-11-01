/*
    CLASSE RESPONSÁVEL POR DEIXAR A JTABLE ZEBRADA
 */
package Classes;
import java.awt.Color;
import static java.awt.Color.red;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;


public class MeuRender implements TableCellRenderer {  
  
  public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();  
  
  public Component getTableCellRendererComponent(JTable table, Object value,  
      boolean isSelected, boolean hasFocus, int row, int column) {  
  
    Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(  
        table, value, isSelected, hasFocus, row, column);  
  
    ((JLabel) renderer).setOpaque(true); 
    
    
  
    Color foreground, background,colorRow;  
  
//      if (hasFocus) {
//        foreground = Color.black;//cor do texto  
//        background = Color.PINK;  
//        colorRow = Color.red;
//        
//      } else if (row % 2 == 0) {  
//        foreground = Color.BLACK;  
//        background = Color.white;  
//  
//      } else {  
//        foreground = Color.BLACK;  
//        background = Color.lightGray;  
//  
//      }  

        if(isSelected){
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
  
    renderer.setForeground(foreground);  
    renderer.setBackground(background);  
    
   
    
    return renderer;  
  
  }  
}  
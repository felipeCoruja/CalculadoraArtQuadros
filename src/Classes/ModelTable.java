/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Felipe Costa
 */
public class ModelTable extends AbstractTableModel{
    private ArrayList linhas = null;
    private String[] colunas = null;
    
    public ModelTable(ArrayList lin,String[] col){
        this.setLinhas(lin);
        this.setColunas(col);
    
    }
    public ArrayList getLinhas(){
        return linhas;
    }
    public void setLinhas(ArrayList dados){
        this.linhas = dados;
    }
    public String[] getColunas(){
        return colunas;
    }
    
    public void setColunas(String[] nomes){//para preencher o nome das colunas
        this.colunas = nomes;
    }
    
    public int getColumnCount(){
        return colunas.length;
    }
    public int getRowCount(){
        return linhas.size();
    }
 
    public String getColumnName(int numCol){
        return colunas[numCol];
    }
    public Object getValueAt(int numLin,int numCol){
        Object[] linha = (Object[])getLinhas().get(numLin);
        return linha[numCol];
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Classes.ModelTable;
import Classes.PintarTabela;
import Classes.SoNumerosEPonto;
import java.awt.Color;
import java.awt.HeadlessException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Felipe Costa
 */
public class Calculadora extends javax.swing.JFrame{

    private double vidro;//valor metro quadrado do vidro
    private double eucatex;//valor metro quadrado eucatex
    private double espelho;//valor metro quadrado espelho
    private double maoDeObra;//porcentagen de mão de obra em cima do valorPedido
    private double molduraMetro;//valor por metro da moldura
    private double paspatuMetro;//valor por metro do paspatu
    private double altura;
    private double largura;
    private boolean flagVidro;
    private boolean flagEucatex;
    private boolean flagEntreVidros;
    private int quantidade;
    private double larguraPaspatu;
    private double valorPedido;
    private ArrayList dados;
    private double valorMoldura;//valor do preço da moldura a ser pago
    private double valorPasp;//valor do preço do paspatu a ser pago
    private double valorVidro;//preço final do vodro por unidade
    private double valorEucatex;//preço final do eucatex por unidade
    private double total; // soma total da (moldura + paspatu + vidro + eucatex) * quantidade
    private int row;
    
    
    public Calculadora() {
        initComponents();
        
        this.vidro = 0.0;
        this.eucatex = 0.0;
        this.espelho = 0.0;
        this.maoDeObra = 0;
        this.molduraMetro = 0.0;
        this.paspatuMetro = 0.0;
        this.altura = 0.0;
        this.largura = 0.0;
        this.flagEntreVidros = false;
        this.flagEucatex = false;
        this.flagVidro = false;
        this.quantidade = 0;
        this.larguraPaspatu = 0.0;
        this.dados = new ArrayList();
        this.valorMoldura = 0.0;
        this.valorPasp = 0.0;
        this.valorVidro = 0.0;
        this.valorEucatex = 0.0;
        this.total = 0.0;
        this.valorPedido = 0.0;
        this.row = -1;
        
        SpinnerNumberModel model = new SpinnerNumberModel(1,1,500,1);//(valor padrao,valor min,valor max,passo)
        this.spinerQuantidade.setModel(model);
        
        //configurando campos para só aceitarem numeros e ponto
        this.edtVidro.setDocument(new SoNumerosEPonto());
        this.edtEucatex.setDocument(new SoNumerosEPonto());
        this.edtEspelho.setDocument(new SoNumerosEPonto());
        this.edtMaoDeObra.setDocument(new SoNumerosEPonto());
        this.edtMolduraMetro.setDocument(new SoNumerosEPonto());
        this.edtAltura.setDocument(new SoNumerosEPonto());
        this.edtLargura.setDocument(new SoNumerosEPonto());
        this.edtPaspatuMetro.setDocument(new SoNumerosEPonto());
        this.edtLarguraPaspatu.setDocument(new SoNumerosEPonto());
        
        loadConfig();
        habilitarCamposConfig(false);
        this.btnSalvarConfig.setEnabled(false);
        
        //setando a tabela para que fique zebrada e com o foco colorido
        tabela.setDefaultRenderer(Object.class, new PintarTabela());
         
        //USANDO OUTRA CLASSE QUE FAZ A MESMA COISA QUE A (PintarTabela)
        //TableCellRenderer renderer = new MeuRender();
        //this.tabela.setDefaultRenderer(Object.class, renderer);
        
        dados.add(new Object[]{"","","","","","",""});//para poder criar a primeira linha junto com o cabeçalho
        String[] colunas = colunas();
        this.criarTabela(dados, colunas);
        dados.remove(0);//para excluir a primeira linha deixando só o cabeçalho
        
        this.btnRemoverDaTabela.setEnabled(false);
        this.btnResetar.setEnabled(false);
    }
    
    private void resetVariaveis(){
       //variaveis de calculo do pedido
        this.molduraMetro = 0.0;
        this.paspatuMetro = 0.0;
        this.altura = 0.0;
        this.largura = 0.0;
        this.flagEntreVidros = false;
        this.flagEucatex = false;
        this.flagVidro = false;
        this.quantidade = 0;
        this.larguraPaspatu = 0.0;
        this.valorMoldura = 0.0;
        this.valorPasp = 0.0;
        this.valorVidro = 0.0;
        this.valorEucatex = 0.0;
        this.valorPedido = 0.0;
        this.row = -1;
    }
    private void habilitarCamposConfig(boolean flag){ 
        this.edtVidro.setEditable(flag);
        this.edtEucatex.setEditable(flag);
        this.edtEspelho.setEditable(flag);
        this.edtMaoDeObra.setEditable(flag);
         Color corFundo = null;
         
        if(flag){
            corFundo = Color.white;
        }else{
           corFundo = Color.LIGHT_GRAY;
        }
        
        this.edtVidro.setBackground(corFundo);
        this.edtEucatex.setBackground(corFundo);
        this.edtEspelho.setBackground(corFundo);
        this.edtMaoDeObra.setBackground(corFundo);
       
    }
    
    private void habilitaCampos(boolean flag){
        this.edtMolduraMetro.setEditable(flag);
        this.edtAltura.setEditable(flag);
        this.edtLargura.setEditable(flag);
        this.edtPaspatuMetro.setEditable(flag);
        this.checkBoxEntreVidros.setEnabled(flag);
        this.checkBoxEucatex.setEnabled(flag);
        this.checkBoxVidro.setEnabled(flag);
        this.btnCalcular.setEnabled(flag);
        this.btnResetar.setEnabled(flag);
        this.edtLarguraPaspatu.setEnabled(flag);
    }
    
    private void resetCampos(){//apenas os campos editáveis de calculo(área cor de rosa)
        this.edtMolduraMetro.setText("");
        this.edtAltura.setText("");
        this.edtLargura.setText("");
        this.edtPaspatuMetro.setText("");
        this.checkBoxEntreVidros.setSelected(false);
        this.checkBoxEucatex.setSelected(false);
        this.checkBoxVidro.setSelected(false);
        this.edtLarguraPaspatu.setText("");
        this.spinerQuantidade.setValue(1);
    }
    private boolean verificaVariaveisVazias(){//confere se os campos necessarios para o calculo estão vazios
        boolean flag = false;
        
        if(this.edtVidro.getText().isEmpty()){
            this.edtVidro.setText(JOptionPane.showInputDialog
                                 ("O Campo 'Vidro' em Configurações está vazio. Insira um valor:"));
            flag = true;
        }else if(this.edtEucatex.getText().isEmpty()){
            this.edtEucatex.setText(JOptionPane.showInputDialog
                                   ("O campo 'Eucatex' em configurações está vazio. Insira um valor:"));
            flag = true;
        }else if(this.edtEspelho.getText().isEmpty()){
            this.edtEspelho.setText(JOptionPane.showInputDialog
                                    ("O campo 'Espelho' em configurações está vazio. Insira um valor:"));  
            flag = true;
        }else if(this.edtMaoDeObra.getText().isEmpty()){
            this.edtMaoDeObra.setText(JOptionPane.showInputDialog
                                     ("O campo 'Mão De Obra' em configurações está vazio. Insira um valor: ex 20"));
            flag = true;
        }else if(this.edtMolduraMetro.getText().isEmpty()){
            this.edtMolduraMetro.setText(JOptionPane.showInputDialog
                                        ("O campo 'Moldura' está vazio. Insira um valor R$/Metro:"));
            flag = true;
        }else if(this.edtAltura.getText().isEmpty()){
            this.edtAltura.setText(JOptionPane.showInputDialog("O campo 'Altura' está vazio. Insira um valor em CM:"));
            flag = true;
        }else if(this.edtLargura.getText().isEmpty()){
            this.edtLargura.setText(JOptionPane.showInputDialog("O campo 'Largura' está vazio. Insira um valor em CM:"));
            flag = true;
        }else if(this.edtPaspatuMetro.getText().isEmpty() && !this.edtLarguraPaspatu.getText().isEmpty()){
        // Caso a largura do paspatuMetro seja preenchida e o paspatuMetro não
            this.edtPaspatuMetro.setText(JOptionPane.showInputDialog("O campo 'Paspatu' está vazio. Insira um valor R$/Metro:"));
            flag = true;
        }else if(!this.edtPaspatuMetro.getText().isEmpty() && this.edtLarguraPaspatu.getText().isEmpty()){
        // Caso o paspatuMetro seja preenchida e a Largura do Paspatu não
            this.edtLarguraPaspatu.setText(JOptionPane.showInputDialog("O campo 'Largura Do Paspatu' está vazio. Insira um valor em CM:"));
        }
        
        return flag;
        
    }
    
    private void converteVariaveis(){//converte os valores dos campos de calculo (String) para os tipos corretos
        
        this.setMolduraMetro(Double.parseDouble(this.edtMolduraMetro.getText()));
        double alt = Double.parseDouble(this.edtAltura.getText());
        this.setAltura(alt/100);// convertende para metros
        double larg = Double.parseDouble(this.edtLargura.getText());
        this.setLargura(larg/100);// convertende para metros
        if(!this.edtPaspatuMetro.getText().isEmpty()){
            this.setPaspatuMetro(Double.parseDouble(this.edtPaspatuMetro.getText()));
            
            double largPasp = Double.parseDouble(this.edtLarguraPaspatu.getText());
            this.setLarguraPaspatu(largPasp/100);// convertende para metros  
        }
        this.setFlagVidro(this.checkBoxVidro.isSelected());
        this.setFlagEucatex(this.checkBoxEucatex.isSelected());
        this.setFlagEntreVidros(this.checkBoxEucatex.isSelected());
        this.setQuantidade((int)this.spinerQuantidade.getValue()); 
        
        //Configurações
        this.setVidro(Double.parseDouble(this.edtVidro.getText()));//valor do vidro por metro quadrado
        this.setEucatex(Double.parseDouble(this.edtEucatex.getText()));//valor do eucatex por metro quadrado
        this.setMaoDeObra(Double.parseDouble(this.edtMaoDeObra.getText()));
        this.setEspelho(Double.parseDouble(this.edtEspelho.getText()));
    }
    
    
    private void calculaQuadro(){
        double valorVid = 0.0;
        double valorEuc = 0.0;
        double valorEntreVid = 0.0;
        double porcentagem = this.maoDeObra / 100;
        double valorUnitario = 0.0;
        
        
        converteVariaveis();

        this.flagVidro = this.checkBoxVidro.isSelected();
        this.flagEucatex = this.checkBoxEucatex.isSelected();
        this.flagEntreVidros = this.checkBoxEntreVidros.isSelected();

        if(this.flagVidro){//calculando o valor do vidro e entre Vidros
            valorVid = this.altura * this.largura * this.vidro;
            this.valorVidro = valorVid + (valorVid * porcentagem);
        }else if(this.flagEntreVidros){
            valorEntreVid = 2*(this.altura * this.largura * this.vidro);
            this.valorVidro = valorEntreVid + (valorEntreVid * porcentagem);  
        }else{
            this.valorVidro = 0.0;
        }

        if(this.flagEucatex){
            valorEuc = this.altura * this.largura * this.eucatex;
            this.valorEucatex = valorEuc + (valorEuc * porcentagem);
        }else{
            this.valorEucatex = 0.0;
        }

        if(this.paspatuMetro == 0.0){
            //calculos com porcentagem de mão de obra
            this.valorMoldura = calculaMoldura(this.altura, this.largura,this.molduraMetro);   
        }else{
            calculaMolduraEPaspatu();// Calcula paspatuMetro e molduraMetro atribuindo respectivamente
                                     //a this.valorPaspatu e this.valorMoldura
        }


        //A baixo estou convertendo um double com varias casas decimais para uma estring de duas casas decimais
        //E em ceguida convertendo para double novamente
            String aux = converterDoubleStringDecimal(this.valorMoldura);
        this.valorMoldura = Double.parseDouble(aux);
            aux = converterDoubleStringDecimal(this.valorPasp);
        this.valorPasp = Double.parseDouble(aux);
            aux = converterDoubleStringDecimal(this.valorVidro);
        this.valorVidro = Double.parseDouble(aux);
            aux = converterDoubleStringDecimal(this.valorEucatex);
        this.valorEucatex = Double.parseDouble(aux);
        
        
        valorUnitario = this.valorMoldura + this.valorPasp + this.valorEucatex + this.valorVidro;
        this.valorPedido = valorUnitario * this.quantidade;
        
        valorUnitario = Double.parseDouble(converterDoubleStringDecimal(valorUnitario));
            aux = converterDoubleStringDecimal(this.valorPedido);
        this.valorPedido = Double.parseDouble(aux);


        String medida = converterDoubleStringDecimal(this.largura)+" X "+converterDoubleStringDecimal(this.altura);
        //{"Qtd","Moldura","Pasp","Vid","Euc","Alt X Larg","Total"} 
        this.dados.add(new Object[] {this.quantidade, this.valorMoldura,this.valorPasp,this.valorVidro,
                                    this.valorEucatex,medida,valorUnitario,this.valorPedido});


        this.total = this.total + this.valorPedido;
        this.edtTotal.setText(converterDoubleStringDecimal(this.total));
    }
    
    private String converterDoubleStringDecimal(double precoDouble) {
        /*Transformando um double em 2 casas decimais*/
        DecimalFormat fmt = new DecimalFormat("0.00");   //limita o número de casas decimais    
        String string = fmt.format(precoDouble);
        String[] part = string.split("[,]");
        String preco = part[0]+"."+part[1];
        return preco;
    }
    
    private double calculaMoldura(double alt, double larg,double mold){
        double total = (2*(alt+larg))*mold;
        double porcentagem = this.maoDeObra/100;
        total = total + (total*porcentagem);
        return total;
    }
    
    private void calculaMolduraEPaspatu(){
        double total;
        
        double alt = this.altura + 2*(this.larguraPaspatu);//this.larguraPaspatu já em metros ex: 0.03m = 3cm
        double larg = this.largura + 2*(this.larguraPaspatu);
        
        //calculando a moldura de dentro(paspatu)
        //adicionando a lardura do paspatu nas medidas para calcular a moldura de fora
        this.valorPasp = calculaMoldura(this.altura,this.largura,this.paspatuMetro);
        this.valorMoldura = calculaMoldura(alt, larg, this.molduraMetro);
    }
    
    private String[] colunas(){
        String[] coluna = {"Qtd","Moldura","Paspatu","Vidro","Eucatex","Altura X Largura","Valor Uni.","Total"};
        return coluna;
    }
    
    private void criarTabela(ArrayList dados, String[] coluna){
        ModelTable modelo = new ModelTable(dados, coluna);
        this.tabela.setModel(modelo);
        
        this.tabela.getColumnModel().getColumn(0).setPreferredWidth(35);//Quantidade
        this.tabela.getColumnModel().getColumn(0).setResizable(false);
        
        this.tabela.getColumnModel().getColumn(1).setPreferredWidth(78);//Moldura
        this.tabela.getColumnModel().getColumn(1).setResizable(false);
        
        this.tabela.getColumnModel().getColumn(2).setPreferredWidth(78);//Paspatu
        this.tabela.getColumnModel().getColumn(2).setResizable(false);
        
        this.tabela.getColumnModel().getColumn(3).setPreferredWidth(70);//Vidro
        this.tabela.getColumnModel().getColumn(3).setResizable(false);
        
        this.tabela.getColumnModel().getColumn(4).setPreferredWidth(70);//Eucatex
        this.tabela.getColumnModel().getColumn(4).setResizable(false);
    
        this.tabela.getColumnModel().getColumn(5).setPreferredWidth(110);//Alt X Larg
        this.tabela.getColumnModel().getColumn(5).setResizable(false);
        
        this.tabela.getColumnModel().getColumn(6).setPreferredWidth(69);//Valor Uni.
        this.tabela.getColumnModel().getColumn(6).setResizable(false);
        
        this.tabela.getColumnModel().getColumn(7).setPreferredWidth(65);//Valor Total.
        this.tabela.getColumnModel().getColumn(7).setResizable(false);
        
        
        this.tabela.getTableHeader().setReorderingAllowed(false);//para nao reordernar o cabeçalho
       // this.tabela.setAutoResizeMode(tabela.AUTO_RESIZE_OFF);//para a tabela não ser auto redimencionavel
        this.tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//para que só se possa selecionar apenas um elemento da tabela
  
        if(this.dados.size() > 0){
            this.btnRemoverDaTabela.setEnabled(true);
            this.btnResetar.setEnabled(true);
        }
    }
    

    private String caminhoDoArquivo(){
        String str = "config.csv";
        //C:\Users\Felipe Costa\Documents\NetBeansProjects\CalculadoraArtQuadros\src\Arquivos\config.csv
        return str;
    }
    private void salvarConfig(){
        
        try{
           
            FileOutputStream arq = new FileOutputStream(caminhoDoArquivo());
            PrintWriter escrever = new PrintWriter(arq);
            
            String str = this.edtVidro.getText()+";"+this.edtEucatex.getText()+";"+this.edtEspelho.getText()+";"
                    +this.edtMaoDeObra.getText()+";";
            escrever.print(str);
            escrever.print("\n");
            
            escrever.close();
            arq.close();
            JOptionPane.showMessageDialog(null, "Configurações Salvas com sucesso! ");
        
        }catch(HeadlessException | IOException e){
            System.out.println("NAO FOI POSSIVEL SALVAR CONFIGURAÇÕES");
            JOptionPane.showMessageDialog(null, "ERRO AO SALVAR NOVAS CONFIGURAÇÕES! "
                    + "( Verifique se o arquivo config.csv está na mesma pasta do aplicação 'Calculadora.jar'  ");
        }
    
    }
    
    private void loadConfig(){
        try {
            FileReader file = new FileReader(caminhoDoArquivo());
            Scanner scan = new Scanner(file);
            scan.useDelimiter("\n");
            
            String lido = scan.next();
            String[] vet = lido.split(";");
            
            this.edtVidro.setText(vet[0]);
            this.edtEucatex.setText(vet[1]);
            this.edtEspelho.setText(vet[2]);
            this.edtMaoDeObra.setText(vet[3]);
            
            if(!vet[0].equals(null)){
                this.vidro = Double.parseDouble(vet[0]);  
                
            }
            
            if(!vet[1].equals(null)){
                this.eucatex = Double.parseDouble(vet[1]);
            }
            
            if(!vet[2].equals(null)){
                this.espelho = Double.parseDouble(vet[2]);
            }
            
            if(!vet[3].equals(null)){
                this.maoDeObra = Double.parseDouble(vet[3]);
            }
            
            scan.close();
            file.close();
            
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Não foi possível carregar as configurações salvas");
            
        }
    
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        edtVidro = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        edtEucatex = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        edtEspelho = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        edtMaoDeObra = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnSalvarConfig = new javax.swing.JButton();
        btnEditarConfig = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        edtMolduraMetro = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        edtAltura = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        edtLargura = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        edtPaspatuMetro = new javax.swing.JTextField();
        checkBoxVidro = new javax.swing.JCheckBox();
        checkBoxEucatex = new javax.swing.JCheckBox();
        checkBoxEntreVidros = new javax.swing.JCheckBox();
        spinerQuantidade = new javax.swing.JSpinner();
        jLabel13 = new javax.swing.JLabel();
        btnCalcular = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        edtLarguraPaspatu = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        edtTotal = new javax.swing.JTextField();
        btnRemoverDaTabela = new javax.swing.JButton();
        btnResetar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(0, 151, 181));
        setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(240, 255, 183));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configurações", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel1.setText("Vidro");

        edtVidro.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel2.setText("Eucatex");

        edtEucatex.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setText("Espelho");

        edtEspelho.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel4.setText("Mão de Obra");

        edtMaoDeObra.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel5.setText("%");

        btnSalvarConfig.setText("Salvar");
        btnSalvarConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarConfigActionPerformed(evt);
            }
        });

        btnEditarConfig.setText("Editar");
        btnEditarConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarConfigActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edtVidro, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(edtEucatex, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(edtEspelho, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(edtMaoDeObra, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addComponent(jLabel4))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnEditarConfig)
                .addGap(18, 18, 18)
                .addComponent(btnSalvarConfig)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtVidro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtEucatex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtEspelho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtMaoDeObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditarConfig)
                    .addComponent(btnSalvarConfig))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(253, 196, 242));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Moldura");

        edtMolduraMetro.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Altura(cm)");

        edtAltura.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Largura(cm)");

        edtLargura.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel11.setText("X");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("Paspatu");

        edtPaspatuMetro.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        checkBoxVidro.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        checkBoxVidro.setText("Vidro");
        checkBoxVidro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBoxVidroItemStateChanged(evt);
            }
        });

        checkBoxEucatex.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        checkBoxEucatex.setText("Eucatex");
        checkBoxEucatex.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBoxEucatexItemStateChanged(evt);
            }
        });

        checkBoxEntreVidros.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        checkBoxEntreVidros.setText("Entre Vidros");
        checkBoxEntreVidros.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBoxEntreVidrosItemStateChanged(evt);
            }
        });
        checkBoxEntreVidros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxEntreVidrosActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setText("Quantidade");

        btnCalcular.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnCalcular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/business-color_calculator_icon-icons.com_53466.png"))); // NOI18N
        btnCalcular.setText("Calcular");
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setText("Largura do Paspatu");

        edtLarguraPaspatu.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        edtLarguraPaspatu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edtLarguraPaspatuActionPerformed(evt);
            }
        });

        jLabel15.setText("cm");

        jLabel6.setText("R$");

        jLabel17.setText("R$");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCalcular, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(checkBoxVidro)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                                .addComponent(checkBoxEucatex)
                                .addGap(18, 18, 18)
                                .addComponent(checkBoxEntreVidros))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(spinerQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(edtMolduraMetro, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(27, 27, 27)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(edtAltura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(12, 12, 12)
                                                .addComponent(jLabel11)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(edtLargura, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel10))))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(edtPaspatuMetro, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(36, 36, 36))
                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(27, 27, 27)))
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(edtLarguraPaspatu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel15))
                                            .addComponent(jLabel14))))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtMolduraMetro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtAltura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtLargura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtPaspatuMetro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(edtLarguraPaspatu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkBoxVidro)
                    .addComponent(checkBoxEucatex)
                    .addComponent(checkBoxEntreVidros))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(spinerQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(btnCalcular)
                .addGap(23, 23, 23))
        );

        tabela.setBackground(new java.awt.Color(255, 214, 235));
        tabela.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tabela.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabela.setCellSelectionEnabled(true);
        tabela.setOpaque(false);
        tabela.setRowHeight(30);
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabela);

        jLabel16.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel16.setText("TOTAL  R$");

        edtTotal.setEditable(false);
        edtTotal.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        edtTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnRemoverDaTabela.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/15_icon-icons.com_73786.png"))); // NOI18N
        btnRemoverDaTabela.setText("Remover Da Tabela");
        btnRemoverDaTabela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverDaTabelaActionPerformed(evt);
            }
        });

        btnResetar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/Clear_37294.png"))); // NOI18N
        btnResetar.setText("Resetar Tabela");
        btnResetar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetarActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/business-color_calculator_icon-icons.com_53466.png"))); // NOI18N
        jLabel7.setText("CALCULADORA ART QUADROS");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                                .addComponent(btnResetar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnRemoverDaTabela)))))
                .addGap(4, 4, 4))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(edtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRemoverDaTabela)
                            .addComponent(btnResetar))))
                .addGap(30, 30, 30))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void checkBoxEntreVidrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxEntreVidrosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBoxEntreVidrosActionPerformed

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularActionPerformed
        if(verificaVariaveisVazias() == false){
            calculaQuadro();
            criarTabela(this.dados, colunas());
            resetVariaveis();
            resetCampos();
        }
        
    }//GEN-LAST:event_btnCalcularActionPerformed

    private void btnSalvarConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarConfigActionPerformed
        this.btnSalvarConfig.setEnabled(false);
        this.btnEditarConfig.setEnabled(true);
        salvarConfig();
        habilitarCamposConfig(false);
    }//GEN-LAST:event_btnSalvarConfigActionPerformed

    private void btnEditarConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarConfigActionPerformed
        this.btnEditarConfig.setEnabled(false);
        this.btnSalvarConfig.setEnabled(true);
        habilitarCamposConfig(true);
    }//GEN-LAST:event_btnEditarConfigActionPerformed

    private void btnResetarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetarActionPerformed
        int value = 0;
        if(this.dados.size() > 0){
            value = JOptionPane.showConfirmDialog(this, "Atenção! Deseja apagar todos os dados salvos?","Confirme!"
                ,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        }
        if(value == JOptionPane.YES_OPTION){
            this.dados.clear();
            criarTabela(this.dados, colunas());
            this.total = 0.0;
            this.edtTotal.setText("");
            
            this.btnResetar.setEnabled(false);
            this.btnRemoverDaTabela.setEnabled(false);
        }
        
    }//GEN-LAST:event_btnResetarActionPerformed

    private void edtLarguraPaspatuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edtLarguraPaspatuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edtLarguraPaspatuActionPerformed

    private void checkBoxEntreVidrosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBoxEntreVidrosItemStateChanged
        this.checkBoxEucatex.setSelected(false);
        this.flagEucatex = false;
        this.checkBoxVidro.setSelected(false);
        this.flagVidro = false;
    }//GEN-LAST:event_checkBoxEntreVidrosItemStateChanged

    private void checkBoxVidroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBoxVidroItemStateChanged
        this.checkBoxEntreVidros.setSelected(false);
        this.flagEntreVidros = false;
    }//GEN-LAST:event_checkBoxVidroItemStateChanged

    private void checkBoxEucatexItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBoxEucatexItemStateChanged
        this.checkBoxEntreVidros.setSelected(false);
        this.flagEntreVidros = false;
    }//GEN-LAST:event_checkBoxEucatexItemStateChanged

    private void tabelaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaMouseClicked
        this.row = this.tabela.getSelectedRow();
        this.btnRemoverDaTabela.setEnabled(true);
        
        if(this.row != -1){
            this.btnRemoverDaTabela.setEnabled(true);
        }else{
            this.btnRemoverDaTabela.setEnabled(false);
            
        }
        
    }//GEN-LAST:event_tabelaMouseClicked

    private void btnRemoverDaTabelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverDaTabelaActionPerformed
        
        if(this.row != -1){
            int linha = this.row;
            linha = linha +1;
            int value = JOptionPane.showConfirmDialog(this, "Atenção! Deseja apagar os dados da linha "+linha+"?","Confirme!"
                ,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            
             if(value == JOptionPane.YES_OPTION){
                 Object[] obj = null;
                obj = (Object[]) this.dados.get(row);
                String str = obj[6].toString();
                double aux = Double.parseDouble(str);
                this.total = this.total - aux;
                String str2 = converterDoubleStringDecimal(this.total);
                this.edtTotal.setText(str2);
                this.dados.remove(row);
                JOptionPane.showMessageDialog(null, "Dado Removido");
                ArrayList list = new ArrayList();
                list.add(new Object[]{"","","","","","",""});
                criarTabela(list, colunas());//Para limpar a tabela que ainda mostra os dados que foram apagados
                criarTabela(dados, colunas());
           
            }else{
                 this.row = -1;
            }
            
            
            if(this.dados.size() == 0){
                this.btnRemoverDaTabela.setEnabled(false);
                this.btnResetar.setEnabled(false);
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "Marque um Valor da Linha Da Tabela Para Excluir-la");
        }
    }//GEN-LAST:event_btnRemoverDaTabelaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Calculadora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Calculadora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Calculadora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Calculadora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Calculadora().setVisible(true);
            }
        });
    }
    
    /**
     * @return the vidro
     */
    public double getVidro() {
        return vidro;
    }

    /**
     * @param vidro the vidro to set
     */
    public void setVidro(double vidro) {
        this.vidro = vidro;
    }

    /**
     * @return the eucatex
     */
    public double getEucatex() {
        return eucatex;
    }

    /**
     * @param eucatex the eucatex to set
     */
    public void setEucatex(double eucatex) {
        this.eucatex = eucatex;
    }

    /**
     * @return the espelho
     */
    public double getEspelho() {
        return espelho;
    }

    /**
     * @param espelho the espelho to set
     */
    public void setEspelho(double espelho) {
        this.espelho = espelho;
    }

    /**
     * @return the maoDeObra
     */
    public double getMaoDeObra() {
        return maoDeObra;
    }

    /**
     * @param maoDeObra the maoDeObra to set
     */
    public void setMaoDeObra(double maoDeObra) {
        this.maoDeObra = maoDeObra;
    }

    /**
     * @return the molduraMetro
     */
    public double getMolduraMetro() {
        return molduraMetro;
    }

    /**
     * @param molduraMetro the molduraMetro to set
     */
    public void setMolduraMetro(double molduraMetro) {
        this.molduraMetro = molduraMetro;
    }

    /**
     * @return the paspatuMetro
     */
    public double getPaspatuMetro() {
        return paspatuMetro;
    }

    /**
     * @param paspatuMetro the paspatuMetro to set
     */
    public void setPaspatuMetro(double paspatuMetro) {
        this.paspatuMetro = paspatuMetro;
    }

    /**
     * @return the altura
     */
    public double getAltura() {
        return altura;
    }

    /**
     * @param altura the altura to set
     */
    public void setAltura(double altura) {
        this.altura = altura;
    }

    /**
     * @return the largura
     */
    public double getLargura() {
        return largura;
    }

    /**
     * @param largura the largura to set
     */
    public void setLargura(double largura) {
        this.largura = largura;
    }

    /**
     * @return the flagVidro
     */
    public boolean isFlagVidro() {
        return flagVidro;
    }

    /**
     * @param flagVidro the flagVidro to set
     */
    public void setFlagVidro(boolean flagVidro) {
        this.flagVidro = flagVidro;
    }

    /**
     * @return the flagEucatex
     */
    public boolean isFlagEucatex() {
        return flagEucatex;
    }

    /**
     * @param flagEucatex the flagEucatex to set
     */
    public void setFlagEucatex(boolean flagEucatex) {
        this.flagEucatex = flagEucatex;
    }

    /**
     * @return the flagEntreVidros
     */
    public boolean isFlagEntreVidros() {
        return flagEntreVidros;
    }

    /**
     * @param flagEntreVidros the flagEntreVidros to set
     */
    public void setFlagEntreVidros(boolean flagEntreVidros) {
        this.flagEntreVidros = flagEntreVidros;
    }

    /**
     * @return the quantidade
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade the quantidade to set
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
     /**
     * @return the valorMoldura
     */
    public double getValorMoldura() {
        return valorMoldura;
    }

    /**
     * @param valorMoldura the valorMoldura to set
     */
    public void setValorMoldura(double valorMoldura) {
        this.valorMoldura = valorMoldura;
    }

    /**
     * @return the valorPasp
     */
    public double getValorPasp() {
        return valorPasp;
    }

    /**
     * @param valorPasp the valorPasp to set
     */
    public void setValorPasp(double valorPasp) {
        this.valorPasp = valorPasp;
    }

    /**
     * @return the valorVidro
     */
    public double getValorVidro() {
        return valorVidro;
    }

    /**
     * @param valorVidro the valorVidro to set
     */
    public void setValorVidro(double valorVidro) {
        this.valorVidro = valorVidro;
    }

    /**
     * @return the valorEucatex
     */
    public double getValorEucatex() {
        return valorEucatex;
    }

    /**
     * @param valorEucatex the valorEucatex to set
     */
    public void setValorEucatex(double valorEucatex) {
        this.valorEucatex = valorEucatex;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalcular;
    private javax.swing.JButton btnEditarConfig;
    private javax.swing.JButton btnRemoverDaTabela;
    private javax.swing.JButton btnResetar;
    private javax.swing.JButton btnSalvarConfig;
    private javax.swing.JCheckBox checkBoxEntreVidros;
    private javax.swing.JCheckBox checkBoxEucatex;
    private javax.swing.JCheckBox checkBoxVidro;
    private javax.swing.JTextField edtAltura;
    private javax.swing.JTextField edtEspelho;
    private javax.swing.JTextField edtEucatex;
    private javax.swing.JTextField edtLargura;
    private javax.swing.JTextField edtLarguraPaspatu;
    private javax.swing.JTextField edtMaoDeObra;
    private javax.swing.JTextField edtMolduraMetro;
    private javax.swing.JTextField edtPaspatuMetro;
    private javax.swing.JTextField edtTotal;
    private javax.swing.JTextField edtVidro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spinerQuantidade;
    private javax.swing.JTable tabela;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the edtLarguraPaspatu
     */
    public javax.swing.JTextField getEdtLarguraPaspatu() {
        return edtLarguraPaspatu;
    }

    /**
     * @param edtLarguraPaspatu the edtLarguraPaspatu to set
     */
    public void setEdtLarguraPaspatu(javax.swing.JTextField edtLarguraPaspatu) {
        this.edtLarguraPaspatu = edtLarguraPaspatu;
    }

    /**
     * @return the larguraPaspatu
     */
    public double getLarguraPaspatu() {
        return larguraPaspatu;
    }

    /**
     * @param larguraPaspatu the larguraPaspatu to set
     */
    public void setLarguraPaspatu(double larguraPaspatu) {
        this.larguraPaspatu = larguraPaspatu;
    }

    /**
     * @return the valorPedido
     */
    public double getValorPedido() {
        return valorPedido;
    }

    /**
     * @param valorPedido the valorPedido to set
     */
    public void setValorPedido(double valorPedido) {
        this.valorPedido = valorPedido;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }
       
}

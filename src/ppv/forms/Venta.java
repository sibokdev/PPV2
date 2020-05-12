/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppv.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ButtonType;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.security.auth.callback.ConfirmationCallback;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import ppv.dbConection.DBConect;
import ppv.model.Configuracion;
import ppv.model.Productos;
import ppv.utils.MyPrintable;
import ppv.utils.TestCut;
import ppv.utils.Ticket;
import ppv.utils.Utils;

/**
 *
 * @author user2
 */
public class Venta extends javax.swing.JFrame implements ListSelectionListener,KeyListener{

    /**
     * Creates new form Venta
     */
    DefaultTableModel resultadoClientes = new DefaultTableModel();
    DefaultTableModel modeloRetiro= new DefaultTableModel();
    DefaultTableModel resultadoClientesEdicion = new DefaultTableModel();
    DefaultTableModel productosEncontradosAModificar=new DefaultTableModel();
    DefaultTableModel listaDetalleVentasPeriodo=new DefaultTableModel();
    DefaultTableModel usuariosEncontrados=new DefaultTableModel();
    DefaultTableModel gridVenta = new DefaultTableModel();
    ComboBoxModel modeloListaUsuarios;
    ListSelectionModel selectionModel;
    SpinnerModel modeloSpinnerDesde;
    SpinnerModel modeloSpinnerHasta;
    ListSelectionListener selectionListener;
    private String clienteId="1";
    int contador=5;
    private Timer timer;
    Productos productoParaCarrrito;
    private Productos productoParaCarrritoDesdeOtraVentana;
    
    private int usuarioLogueado;
    private int indicadorRefrescoUtilizadoEnPaquete=0;
    private Double ivaConfigurado;   
    DecimalFormat decimales = new DecimalFormat("0.00");
    DecimalFormat decimalEntero=new DecimalFormat("0");
    private int seleccionGlobalCliente=0;
    public static HashSet<Integer> tiposDeProductoAgregados=new HashSet<>();
    String usuarioSesion="";
    String passSesion="";
    
    
    public Configuracion obtenerConfiguracion(){
        DBConect conexion=new DBConect();  
        Configuracion c=new Configuracion();
        try{

          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();
          
          String sqlString="Select * from  installData";
          ResultSet rs=statement.executeQuery(sqlString);
          
          while(rs.next()){
              c.setIva(rs.getDouble("iva"));
              c.setDescuento1(rs.getDouble("desc1"));
              c.setDescuento2(rs.getDouble("desc2"));
              c.setDescuento3(rs.getDouble("desc3"));
              c.setDescuento4(rs.getDouble("desc4"));
              c.setDescuento5(rs.getDouble("desc5"));
              c.setDireccion(rs.getString("direccion"));
              c.setRfc(rs.getString("rfc"));
              c.setSlogan(rs.getString("slogan"));
              c.setSucursal(rs.getString("sucursal"));
              
              comboDescuentos.addItem(rs.getDouble("desc1")*(100)+" %");
              comboDescuentos.addItem(rs.getDouble("desc2")*(100)+" %");
              comboDescuentos.addItem(rs.getDouble("desc3")*(100)+" %");
              comboDescuentos.addItem(rs.getDouble("desc4")*(100)+" %");
              comboDescuentos.addItem(rs.getDouble("desc5")*(100)+" %");
          }
          
          sqlString="Select descripcion from  productos where TipoProducto=1";
          ResultSet rs2=statement.executeQuery(sqlString);
          
          while(rs2.next()){             
              seleccionPizza1.addItem(rs2.getString("descripcion"));
              seleccionPizza2.addItem(rs2.getString("descripcion"));
              seleccionPizza3.addItem(rs2.getString("descripcion"));
          }
          
          sqlString="Select descripcion from  productos where TipoProducto=3";
          ResultSet rs3=statement.executeQuery(sqlString);
          
          while(rs3.next()){             
              seleccionExtras1.addItem(rs3.getString("descripcion"));
              seleccionExtras2.addItem(rs3.getString("descripcion"));
          }
          
          
        }catch(Exception e){
            e.printStackTrace();
        }
    return c;
    } 
    
    public void refrescarComboAlAgregarProducto(){
       DBConect conexion=new DBConect();  
       seleccionPizza1.removeAllItems();
        try{

          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();    
          String sqlString="Select descripcion from  productos where TipoProducto=1";
          ResultSet rs2=statement.executeQuery(sqlString);
          
          while(rs2.next()){             
              seleccionPizza1.addItem(rs2.getString("descripcion"));
              seleccionPizza2.addItem(rs2.getString("descripcion"));
              seleccionPizza3.addItem(rs2.getString("descripcion"));
          }
        
          }catch(Exception e){
            e.printStackTrace();
        }
    
    }
   
    public Venta(int c1I,int c2I,int c3I,int c4I,int c5I,String user, String pass) {
        this.usuarioSesion=user;
        this.passSesion=pass;
        
        Date d=new Date();
        d.setHours(00);
        d.setMinutes(00);
        d.setSeconds(00);
        modeloSpinnerDesde=new SpinnerDateModel(d,null,null,Calendar.HOUR_OF_DAY);
        
        d.setHours(23);
        d.setMinutes(59);
        d.setSeconds(59);
        modeloSpinnerHasta=new SpinnerDateModel(d,null,null,Calendar.HOUR_OF_DAY);
        
        
        initComponents();
        
        ocultarCamposAltaAlInicio();
        
        setLocationRelativeTo(null);
        
        agregarUsuarioSinRegistro();
        ///ir por el iva configurado
        Configuracion c=obtenerConfiguracion();
        ivaConfigurado=c.getIva();
        valorIVA.setText(""+decimalEntero.format((ivaConfigurado*100)));
        valorD1.setText(""+decimalEntero.format(c.getDescuento1()*100));
        valorD2.setText(""+decimalEntero.format(c.getDescuento2()*100));
        valorD3.setText(""+decimalEntero.format(c.getDescuento3()*100));
        valorD4.setText(""+decimalEntero.format(c.getDescuento4()*100));
        valorD5.setText(""+decimalEntero.format(c.getDescuento5()*100));
        valorSucursal.setText(c.getSucursal());
        valorDireccion.setText(c.getDireccion());
        valorRFC.setText(c.getRfc());
        valorSlogan.setText(c.getSlogan());
        
        agregarUsuariosAListaDeReportes();
        selectionModel=tablaClientesEncontrados.getSelectionModel();
        
        agregarListener();
        //panelAgregarClienteVenta.setVisible(false);
        panelModificarCliente.setVisible(false);
        panelModificarProducto.setVisible(false);
        panelModificarUsuario.setVisible(false);
        labelAgergarCliente.setVisible(false);
        preAgregarCliente.setVisible(false);
        jPanel16.setVisible(false);
        jPanel24.setVisible(false);
        //jButton1.setVisible(false);
        agregarListenerACodigo();
        
        ///ocultamos la venta de los tacos, ya que se realizara por busqueda
        //jPanel19.setVisible(false);
        
        ///Mostramos la busqueda para el codigo de barras no se utiliza para las pizzas
        jPanel21.setVisible(true);
        
        
        jLabel8.setVisible(true);
        codigoAltaText.setVisible(true);
        
        
        timer = new Timer(500, new ActionListener() {
          
       public void actionPerformed(ActionEvent e) {
                lectorCBFinalizoLectura();
            }
        });
      
        
        
      comboTipoProducto.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                String seleccion=comboTipoProducto.getSelectedItem().toString();
                
//                if(seleccion.equals("Pizza")){
//                    ocultarCamposEnAltaPizza();
//                }
                
                if(seleccion.equals("General")){
                   ocultarCamposEnAltaGeneral();
                }
                
            }
        });   
        
        
      comboDescuentos.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                aplicarDescuento();
            }
        });  
      
      
            jTabbedPane1.remove(tabConfiguracion);
            if(c1I==1){
                //jTabbedPane1.add(jPanel2);
            }else{
                //jTabbedPane1.getComponentAt(0).setVisible(true);
                jTabbedPane1.remove(jPanel2);
            }

            if(c2I==1){
                //jTabbedPane1.add(jPanel3);
            }else{
                jTabbedPane1.remove(jScrollPane11);
            }

            if(c3I==1){
                //jTabbedPane1.add(jScrollPane3);
            }else{
               
                jTabbedPane1.remove(tabReportes);
            }

            if(c4I==1){
                //jTabbedPane1.add(tabReportes);
            }else{
                jTabbedPane1.remove(jScrollPane3); 
            }

            
            if(c5I==1){
               tabConfiguracion.setName("Configuración");
               jTabbedPane1.add(tabConfiguracion);
            }else{
                jTabbedPane1.remove(TabUsuarios);
                
            }
            
            jTabbedPane1.remove(jPanel24);
           
            DateEditor de=new DateEditor(jSpinner1,"HH:mm:ss");
            DateEditor de2=new DateEditor(jSpinner2,"HH:mm:ss");
            jSpinner1.setEditor(de);
            jSpinner2.setEditor(de2);
         codigoBusqueda.setFocusable(true);
         codigoBusqueda.grabFocus(); 
         comboTipoProducto.setSelectedIndex(2);
         
        addListenersToCoinBalance();
        agregarListenersBilletes();
        newNombreVenta1.requestFocus(true);
        addKeyListener(this);
        addGlobalListeners();
    }
    
    public Venta() {   
        Date d=new Date();
        d.setHours(00);
        d.setMinutes(00);
        d.setSeconds(00);
        modeloSpinnerDesde=new SpinnerDateModel(d,null,null,Calendar.HOUR_OF_DAY);
        
        d.setHours(23);
        d.setMinutes(59);
        d.setSeconds(59);
        modeloSpinnerHasta=new SpinnerDateModel(d,null,null,Calendar.HOUR_OF_DAY);
        
        initComponents();
        
        setLocationRelativeTo(null);
        
        agregarUsuarioSinRegistro();
        ///ir por el iva configurado
        Configuracion c=obtenerConfiguracion();
        ivaConfigurado=c.getIva();
        valorIVA.setText(""+ivaConfigurado*100);
        valorD1.setText(""+c.getDescuento1()*100);
        valorD2.setText(""+c.getDescuento2()*100);
        valorD3.setText(""+c.getDescuento3()*100);
        valorD4.setText(""+c.getDescuento4()*100);
        valorD5.setText(""+c.getDescuento5()*100);
        valorSucursal.setText(c.getSucursal());
        valorDireccion.setText(c.getDireccion());
        valorRFC.setText(c.getRfc());
        valorSlogan.setText(c.getSlogan());
        
        agregarUsuariosAListaDeReportes();
        selectionModel=tablaClientesEncontrados.getSelectionModel();
        
        agregarListener();
        //panelAgregarClienteVenta.setVisible(false);
        panelModificarCliente.setVisible(false);
        panelModificarProducto.setVisible(false);
        panelModificarUsuario.setVisible(false);
        labelAgergarCliente.setVisible(false);
        preAgregarCliente.setVisible(false);
        //jButton1.setVisible(false);
        agregarListenerACodigo();
        
        timer = new Timer(500, new ActionListener() {
          
       public void actionPerformed(ActionEvent e) {
                lectorCBFinalizoLectura();
            }
        });       
     
        comboDescuentos.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                aplicarDescuento();
            }
        });
        
      
            DateEditor de=new DateEditor(jSpinner1,"HH:mm:ss");
            DateEditor de2=new DateEditor(jSpinner2,"HH:mm:ss");
            jSpinner1.setEditor(de);
            jSpinner2.setEditor(de2);
            codigoBusqueda.setFocusable(true);
            comboTipoProducto.setSelectedIndex(2);
        
        addListenersToCoinBalance();
        agregarListenersBilletes();
        newNombreVenta1.requestFocus(true);
        addKeyListener(this);
        addGlobalListeners();
    }
    public void getMonedasValue(){
        Double valorMoneda50=0.5;
        Double valorMoneda1=1.0;
        Double valorMoneda2=2.0;
        Double valorMoneda5=5.0;
        Double valorMoneda10=10.0;
        Double valorMoneda20=20.0;
        
        Double valorBillete20=20.0;
        Double valorBillete50=50.0;
        Double valorBillete100=100.0;
        Double valorBillete200=200.0;
        Double valorBillete500=500.0;
        Double valorBillete1000=1000.0;
        try{
            Integer moneda50=Integer.parseInt(coin1.getText()); 
            Double totalMoneda50=moneda50*valorMoneda50;

            Integer moneda1=Integer.parseInt(coin2.getText()); 
            Double totalMoneda1=moneda1*valorMoneda1;

            Integer moneda2=Integer.parseInt(coin3.getText()); 
            Double totalMoneda2=moneda2*valorMoneda2;

            Integer moneda5=Integer.parseInt(coin4.getText()); 
            Double totalMoneda5=moneda5*valorMoneda5;

            Integer moneda10=Integer.parseInt(coin5.getText()); 
            Double totalMoneda10=moneda10*valorMoneda10;

            Integer moneda20=Integer.parseInt(coin6.getText()); 
            Double totalMoneda20=moneda20*valorMoneda20;

            Double totalMonedas=totalMoneda50+totalMoneda1+totalMoneda2+totalMoneda5+totalMoneda10+totalMoneda20;
            
            Integer billete20=Integer.parseInt(paperCoin1.getText()); 
            Double totalBillete20=billete20*valorBillete20;

            Integer billete50=Integer.parseInt(paperCoin2.getText()); 
            Double totalBillete50=billete50*valorBillete50;

            Integer billete100=Integer.parseInt(paperCoin3.getText()); 
            Double totalBillete100=billete100*valorBillete100;

            Integer billete200=Integer.parseInt(paperCoin4.getText()); 
            Double totalBillete200=billete200*valorBillete200;

            Integer billete500=Integer.parseInt(paperCoin5.getText()); 
            Double totalBillete500=billete500*valorBillete500;

            Integer billete1000=Integer.parseInt(paperCoin6.getText()); 
            Double totalBillete1000=billete1000*valorBillete1000;

            Double totalBilletes=totalBillete20+totalBillete50+totalBillete100+totalBillete200+totalBillete500+totalBillete1000;
            Double cantidadEnCaja=0.0;
            String efectivoInicialoS=efectivoInicialLabel.getText();
            if(efectivoInicialoS.equals("---")){
                cantidadEnCaja=0.0;
            }else{
                efectivoInicialoS=efectivoInicialoS.replace("$","");
                cantidadEnCaja=Double.parseDouble(efectivoInicialoS);
            }
            
            Double totalArqueo=totalMonedas+totalBilletes;
            
            totalCoins.setText("$"+totalArqueo);
        }catch(Exception e){
            
            JOptionPane.showMessageDialog(null,"Alguno de los valores ingresados no es un número entero");
        }
    }
    
    public void addListenersToCoinBalance(){
            
        ///listeners monedas
            coin1.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    coin1.selectAll();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(coin1.getText().equals("")){
                        coin1.setText("0");
                    }
                }
            
            });
            coin1.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                  warn();
                }
                public void removeUpdate(DocumentEvent e) {
                  //warn();
                }
                public void insertUpdate(DocumentEvent e) {
                  warn();
                }

                public void warn() {
                   Integer errores=0;
                   if (Integer.parseInt(coin1.getText())<0){
                     JOptionPane.showMessageDialog(null,
                        "Error: No se pueden ingresar valores menores a cero", "Error Massage",
                        JOptionPane.ERROR_MESSAGE);
                     errores++;
                   }

                   if(errores>0){
                   }else{
                       getMonedasValue();
                   }
                }
            });
            
            coin2.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    coin2.selectAll();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(coin2.getText().equals("")){
                        coin2.setText("0");
                    }
                }
            
            });
            coin2.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                  warn();
                }
                public void removeUpdate(DocumentEvent e) {
                  //warn();
                }
                public void insertUpdate(DocumentEvent e) {
                  warn();
                }

                public void warn() {
                   Integer errores=0;
                   if (Integer.parseInt(coin2.getText())<0){
                     JOptionPane.showMessageDialog(null,
                        "Error: No se pueden ingresar valores menores a cero", "Error Massage",
                        JOptionPane.ERROR_MESSAGE);
                     errores++;
                   }
                   
                   if(errores>0){
                   }else{
                       getMonedasValue();
                   }
                }
            });
            
            coin3.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    coin3.selectAll();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(coin3.getText().equals("")){
                        coin3.setText("0");
                    }
                }
            
            });
            coin3.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                  warn();
                }
                public void removeUpdate(DocumentEvent e) {
                  //warn();
                }
                public void insertUpdate(DocumentEvent e) {
                  warn();
                }

                public void warn() {
                   Integer errores=0;
                   if (Integer.parseInt(coin3.getText())<0){
                     JOptionPane.showMessageDialog(null,
                        "Error: No se pueden ingresar valores menores a cero", "Error Massage",
                        JOptionPane.ERROR_MESSAGE);
                     errores++;
                   }
                   
                   if(errores>0){
                   }else{
                       getMonedasValue();
                   }
                }
            });
            
            coin4.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    coin4.selectAll();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(coin4.getText().equals("")){
                        coin4.setText("0");
                    }
                }
            
            });
            coin4.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                  warn();
                }
                public void removeUpdate(DocumentEvent e) {
                  //warn();
                }
                public void insertUpdate(DocumentEvent e) {
                  warn();
                }

                public void warn() {
                   Integer errores=0;
                   if (Integer.parseInt(coin4.getText())<0){
                     JOptionPane.showMessageDialog(null,
                        "Error: No se pueden ingresar valores menores a cero", "Error Massage",
                        JOptionPane.ERROR_MESSAGE);
                     errores++;
                   }

                   if(errores>0){
                   }else{
                       getMonedasValue();
                   }
                }
            });
            
            coin5.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    coin5.selectAll();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(coin5.getText().equals("")){
                        coin5.setText("0");
                    }
                }
            
            });
            coin5.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                  warn();
                }
                public void removeUpdate(DocumentEvent e) {
                  //warn();
                }
                public void insertUpdate(DocumentEvent e) {
                  warn();
                }

                public void warn() {
                   Integer errores=0;
                   if (Integer.parseInt(coin5.getText())<0){
                     JOptionPane.showMessageDialog(null,
                        "Error: No se pueden ingresar valores menores a cero", "Error Massage",
                        JOptionPane.ERROR_MESSAGE);
                     errores++;
                   }

                   if(errores>0){
                   }else{
                       getMonedasValue();
                   }
                }
            });
            
            coin6.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    coin6.selectAll();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(coin6.getText().equals("")){
                        coin6.setText("0");
                    }
                }
            
            });
            
            coin6.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                  warn();
                }
                public void removeUpdate(DocumentEvent e) {
                  //warn();
                }
                public void insertUpdate(DocumentEvent e) {
                  warn();
                }

                public void warn() {
                   Integer errores=0;
                   if (Integer.parseInt(coin6.getText())<0){
                     JOptionPane.showMessageDialog(null,
                        "Error: No se pueden ingresar valores menores a cero", "Error Massage",
                        JOptionPane.ERROR_MESSAGE);
                     errores++;
                   }

                   if(errores>0){
                   }else{
                       getMonedasValue();
                   }
                }
            });
    }
    
    public void agregarListenersBilletes(){
            ///listeners monedas
        
            paperCoin1.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    paperCoin1.selectAll();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(paperCoin1.getText().equals("")){
                        paperCoin1.setText("0");
                    }
                }
            
            });
            paperCoin1.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                  warn();
                }
                public void removeUpdate(DocumentEvent e) {
                  //warn();
                }
                public void insertUpdate(DocumentEvent e) {
                  warn();
                }

                public void warn() {
                   Integer errores=0;
                   if (Integer.parseInt(paperCoin1.getText())<0){
                     JOptionPane.showMessageDialog(null,
                        "Error: No se pueden ingresar valores menores a cero", "Error Massage",
                        JOptionPane.ERROR_MESSAGE);
                     errores++;
                   }

                   if(errores>0){
                   }else{
                       getMonedasValue();
                   }
                }
            });
            
            paperCoin2.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    paperCoin2.selectAll();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(paperCoin2.getText().equals("")){
                        paperCoin2.setText("0");
                    }
                }
            
            });
            paperCoin2.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                  warn();
                }
                public void removeUpdate(DocumentEvent e) {
                  //warn();
                }
                public void insertUpdate(DocumentEvent e) {
                  warn();
                }

                public void warn() {
                   Integer errores=0;
                   if (Integer.parseInt(paperCoin2.getText())<0){
                     JOptionPane.showMessageDialog(null,
                        "Error: No se pueden ingresar valores menores a cero", "Error Massage",
                        JOptionPane.ERROR_MESSAGE);
                     errores++;
                   }

                   if(errores>0){
                   }else{
                       getMonedasValue();
                   }
                }
            });
            
            paperCoin3.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    paperCoin3.selectAll();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(paperCoin3.getText().equals("")){
                        paperCoin3.setText("0");
                    }
                }
            
            });
            paperCoin3.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                  warn();
                }
                public void removeUpdate(DocumentEvent e) {
                  //warn();
                }
                public void insertUpdate(DocumentEvent e) {
                  warn();
                }

                public void warn() {
                   Integer errores=0;
                   if (Integer.parseInt(paperCoin3.getText())<0){
                     JOptionPane.showMessageDialog(null,
                        "Error: No se pueden ingresar valores menores a cero", "Error Massage",
                        JOptionPane.ERROR_MESSAGE);
                     errores++;
                   }
                   
                   if(errores>0){
                   }else{
                       getMonedasValue();
                   }
                }
            });
            
            paperCoin4.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    paperCoin4.selectAll();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(paperCoin4.getText().equals("")){
                        paperCoin4.setText("0");
                    }
                }
            
            });
            paperCoin4.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                  warn();
                }
                public void removeUpdate(DocumentEvent e) {
                  //warn();
                }
                public void insertUpdate(DocumentEvent e) {
                  warn();
                }

                public void warn() {
                   Integer errores=0;
                   if (Integer.parseInt(paperCoin4.getText())<0){
                     JOptionPane.showMessageDialog(null,
                        "Error: No se pueden ingresar valores menores a cero", "Error Massage",
                        JOptionPane.ERROR_MESSAGE);
                     errores++;
                   }

                   if(errores>0){
                   }else{
                       getMonedasValue();
                   }
                }
            });
            
            paperCoin5.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    paperCoin5.selectAll();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(paperCoin5.getText().equals("")){
                        paperCoin5.setText("0");
                    }
                }
            
            });
            paperCoin5.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                  warn();
                }
                public void removeUpdate(DocumentEvent e) {
                  //warn();
                }
                public void insertUpdate(DocumentEvent e) {
                  warn();
                }

                public void warn() {
                   Integer errores=0;
                   if (Integer.parseInt(paperCoin5.getText())<0){
                     JOptionPane.showMessageDialog(null,
                        "Error: No se pueden ingresar valores menores a cero", "Error Massage",
                        JOptionPane.ERROR_MESSAGE);
                     errores++;
                   }

                   if(errores>0){
                   }else{
                       getMonedasValue();
                   }
                }
            });
            
            paperCoin6.addFocusListener(new FocusListener(){

                @Override
                public void focusGained(FocusEvent e) {
                    paperCoin6.selectAll();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if(paperCoin6.getText().equals("")){
                        paperCoin6.setText("0");
                    }
                }
            
            });
            paperCoin6.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                  warn();
                }
                public void removeUpdate(DocumentEvent e) {
                  //warn();
                }
                public void insertUpdate(DocumentEvent e) {
                  warn();
                }

                public void warn() {
                   Integer errores=0;
                   if (Integer.parseInt(paperCoin6.getText())<0){
                     JOptionPane.showMessageDialog(null,
                        "Error: No se pueden ingresar valores menores a cero", "Error Massage",
                        JOptionPane.ERROR_MESSAGE);
                     errores++;
                   }

                   if(errores>0){
                   }else{
                       getMonedasValue();
                   }
                }
            });
    
    }
    public void addGlobalListeners(){
        
        this.addComponentListener(new ComponentListener(){

            @Override
            public void componentResized(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
                if(jTabbedPane1.getSelectedIndex()==7){
                    codigoBusqueda.requestFocus(true);
                }
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        
        });
        jTabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switch(jTabbedPane1.getSelectedIndex()){
                    case 0:
                        newNombreVenta1.requestFocus(true);
                    break;
                    case 1:
                        descripcionAltaText.requestFocus(true);
                    break;
                    case 2:
                        fechaInicio.requestFocus(true);
                    break;
                    case 3:
                        newUserToAdd.requestFocus(true);
                    break;
                    case 4:
                        valorIVA.requestFocus(true);
                    break;
                    case 5:
                       
                        codigoBusqueda.requestFocus(true);
                    break;
                    case 7:
                         cantidadRetiro.requestFocus(true);
                    break;      
                }
            }
        });
    
    }
    public void aplicarDescuento(){
        DefaultTableModel model = (DefaultTableModel) tablaDetalleVenta.getModel();
            double granTotal=0.0;
            for(int i=0;i<model.getRowCount();i++){

                Double val=(Double)model.getValueAt(i, 3);
                String cantidad=(String)model.getValueAt(i, 1);
                Double valNumeric=val;
                Double cantidadNumeric=Double.parseDouble(cantidad);
                Double numericTotal=valNumeric*cantidadNumeric;
                granTotal+=numericTotal;
            }
            
            ///obtenemos el descuento
            String descuentoUnidades=comboDescuentos.getSelectedItem().toString();
            
            String descuentoDecimal=descuentoUnidades.replace("%","");
            Double valorDescuento=0.0;
            
            if(descuentoDecimal.equals("---")){
                
            }else{
                valorDescuento=Double.parseDouble(descuentoDecimal);
                 valorDescuento=valorDescuento/100;
            }
            
            Double descuentoTotal=(granTotal)*valorDescuento;
            
            ivaTotal.setText("$"+decimales.format(granTotal*ivaConfigurado));
            etiquetaGranTotal.setText("$"+decimales.format((granTotal-descuentoTotal)));
            codigoBusqueda.setText("");
            descripcionManual.setText("");
            cantidadPizza.setText("1");
            codigoBusqueda.setFocusable(true);
    }
    public void agregarUsuariosAListaDeReportes(){
        DBConect conexion=new DBConect();  
        
        try{

          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();
          
          String sqlString="Select NombreCompleto from  usuarios where estatus=1";
          ResultSet rs=statement.executeQuery(sqlString);
          
          while(rs.next()){
              listaTodosLosUsuarios.addItem(rs.getString(1));
          }
          
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void agregarUsuarioSinRegistro(){
    
        Vector row = new Vector();
        row.add(1);
        row.add("Usuario sin registro");
        row.add("");
        row.add("");
        //tablaClientesEncontrados.setModel(resultadoClientes);
        resultadoClientes.addColumn("Cliente");
        resultadoClientes.addColumn("Nombre completo");
        resultadoClientes.addColumn("Dirección");
        resultadoClientes.addColumn("Teléfono");
        
        resultadoClientes.addRow(row);
        tablaClientesEncontrados.selectAll();
    
    }
    
    public void agregarProductoDesdeOtraVentana(Productos productoParaCarrritoDesdeOtraVentana){
        DefaultTableModel model = (DefaultTableModel) tablaDetalleVenta.getModel();
        Vector row = new Vector();

            int filasTotales=model.getRowCount();
            int nuevaFila=filasTotales+1;
            row.add(nuevaFila);
            row.add(productoParaCarrritoDesdeOtraVentana.getUnidadesEnCaja());
            row.add(productoParaCarrrito.getDescripcion());
            row.add(productoParaCarrrito.getPrecioVenta());
            String x=(String)row.get(1);
            Double y=(Double)row.get(3);

            Double cant=Double.parseDouble(x);
            Double precio=y;

            Double subtGrid=cant*precio;

            row.add(""+subtGrid);

            row.add(productoParaCarrrito.getId());
            
            model.addRow(row);

            tablaDetalleVenta.getColumn("Id producto").setWidth(0);
            tablaDetalleVenta.getColumn("Id producto").setMinWidth(0);
            tablaDetalleVenta.getColumn("Id producto").setMaxWidth(0);
            tablaDetalleVenta.getColumn("Tamaño").setWidth(0);
            tablaDetalleVenta.getColumn("Tamaño").setMinWidth(0);
            tablaDetalleVenta.getColumn("Tamaño").setMaxWidth(0);
            
            String descuentoUnidades=comboDescuentos.getSelectedItem().toString();
            
            String descuentoDecimal=descuentoUnidades.replace("%","");
            Double valorDescuento=0.0;
            
            if(descuentoDecimal.equals("---")){
                
            }else{
                valorDescuento=Double.parseDouble(descuentoDecimal);
                 valorDescuento=valorDescuento/100;
            }
            
            double granTotal=0.0;
            for(int i=0;i<model.getRowCount();i++){

                Double val=(Double)model.getValueAt(i, 3);
                String cantidad=(String)model.getValueAt(i, 1);
                Double valNumeric=val;
                Double cantidadNumeric=Double.parseDouble(cantidad);
                Double numericTotal=valNumeric*cantidadNumeric;
                granTotal+=numericTotal;
            }
            Double descuentoTotal=(granTotal)*valorDescuento;
            ivaTotal.setText("$"+decimales.format(granTotal*ivaConfigurado));
            etiquetaGranTotal.setText("$"+decimales.format(granTotal-descuentoTotal));
            codigoBusqueda.setText("");
            descripcionManual.setText("");
            cantidadPizza.setText("1");
            codigoBusqueda.setFocusable(true);
                
    }
    
    public void lectorCBFinalizoLectura(){
        
        ///detenemos el timer para evitar que se reinicie cuando se esta agregando el producto
        timer.stop();
    
        int tipoDeProductoAgregado=0;
        
        ///buscamos el producto enm la db
        DBConect conexion=new DBConect();  
        
        try{

          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();

          String codigoVar=codigoBusqueda.getText();
          String descripcionVar=descripcionManual.getText();
          
          String sqlString="Select * from  productos " +
                           " where estatus=0 and codigo='"+codigoVar+"'";
                           
          if(!descripcionVar.equals("")){
               sqlString+= " And descripcion like '%"+descripcionVar+"%'";
          }
          
          if(codigoVar.equals("") && !descripcionVar.equals("")){
              
              sqlString="SELECT * FROM productos where estatus=0 and descripcion like '%"+descripcionVar+"%'";
              
          }
          
          ///validamos si existe producto seleccionado de la ventana buscador de productos
          if(productoParaCarrritoDesdeOtraVentana!=null){
              sqlString="SELECT * FROM productos where estatus=0 and idProductos="+productoParaCarrritoDesdeOtraVentana.getId();
              cantidadPizza.setText(""+productoParaCarrritoDesdeOtraVentana.getUnidadesEnCaja());

          }
          
          
          ResultSet rs = statement.executeQuery(sqlString); 
          int contador=0;  
          while (rs.next()) {
              
           contador++;
           
           productoParaCarrrito=new Productos();
           productoParaCarrrito.setId(""+rs.getInt("idProductos"));
           productoParaCarrrito.setCodigo(rs.getString("codigo"));
           productoParaCarrrito.setDescripcion(rs.getString("descripcion"));
           productoParaCarrrito.setEstatus(rs.getInt("estatus"));
           productoParaCarrrito.setPrecioCompra(rs.getDouble("precioUnitarioC"));
           productoParaCarrrito.setPrecioVenta(rs.getDouble("precioUnitarioV"));
           productoParaCarrrito.setPresentacion(rs.getString("presentacion"));
           productoParaCarrrito.setUnidadMedida(rs.getString("uMedida"));
           productoParaCarrrito.setUnidadesEnCaja(rs.getDouble("unidadesEnCaja"));
           
           tipoDeProductoAgregado=rs.getInt("TipoProducto");
           
           if(productoParaCarrrito.getDescripcion().trim().equals("1 kg carne arabe")){
               tipoDeProductoAgregado=5;
           }
           if(productoParaCarrrito.getDescripcion().trim().equals("1/2 kg carne arabe")){
               tipoDeProductoAgregado=5;
           }
           
           int activo=rs.getInt("activo");
           
           if(activo==0){
               JOptionPane.showMessageDialog(null, "El producto buscado YA NO ESTA ACTIVO en el inventario.\n\nPara agregar un producto a la venta, debe estar activo");
               //cantidadPizza.setText("1");
               codigoBusqueda.setText("");
               return;
           }
           
          }
          
          if(contador==0){
              JOptionPane.showMessageDialog(null, "No existe un producto con el código proporcionado");
              //cantidadPizza.setText("1");
              codigoBusqueda.setText("");
              return;
          }
          
          DefaultTableModel modelPre = (DefaultTableModel) tablaDetalleVenta.getModel();
          int cantidadProductos=modelPre.getRowCount();
          
          Object[] content = new Object[cantidadProductos];
          Object[] cantidadProdBuscado = new Object[cantidadProductos];
            for (int i = 0; i < cantidadProductos; i++) {
                content[i] = modelPre.getValueAt(i, 5);
                cantidadProdBuscado[i]=modelPre.getValueAt(i,1);
            }
            
            
            Object value_to_find= productoParaCarrrito.getId();

                int totalParaAgregar=0;
                for(int i=0;i<content.length;i++){

                    if(value_to_find.equals(content[i])){
                        int filaDeProducto=Arrays.asList(content).indexOf(value_to_find);
                        String cantidadYaEnLista2=(String)cantidadProdBuscado[i];
                        int cantidadYaEnLista=Integer.parseInt(cantidadYaEnLista2);
                        totalParaAgregar+=cantidadYaEnLista;
                    }
                }
                Double cantidadAvenderValidacion=Double.parseDouble(cantidadPizza.getText());
                totalParaAgregar+=cantidadAvenderValidacion;

                if(totalParaAgregar>productoParaCarrrito.getUnidadesEnCaja()){
                    String mensaje="Solo existen "+productoParaCarrrito.getUnidadesEnCaja()+" unidades en almacen";
                    mensaje+=" del producto: \n "+productoParaCarrrito.getDescripcion()+" \n";
                    mensaje+=" por lo cual no se puede agregar la cantidad deseada";
                    JOptionPane.showMessageDialog(null, mensaje);
                    cantidadPizza.setText("1");
                    codigoBusqueda.setText("");
                    return;
                }

          if(contador>0){
              
             Double cantidadAvender=Double.parseDouble(cantidadPizza.getText());
             Double cantidadEnAlmacen=productoParaCarrrito.getUnidadesEnCaja();
             ///recorrer y sumar los productos con el mismo codigo o id 
             ///para obtener cuantos productos en total de cantidad se han agregado
             
             
             if(cantidadAvender>cantidadEnAlmacen){
                 
                 String mensaje="Solo existen "+cantidadEnAlmacen+" unidades en almacen";
                 mensaje+=" del producto: \n "+productoParaCarrrito.getDescripcion()+" \n";
                 mensaje+=" por lo cual no se puede vender la cantidad deseada";
                 JOptionPane.showMessageDialog(null, mensaje);
                 
                 if(cantidadEnAlmacen>0){
                    codigoBusqueda.setText("");
                    cantidadPizza.setText(""+productoParaCarrrito.getUnidadesEnCaja());
                    codigoBusqueda.setText(productoParaCarrrito.getCodigo());
                 }else{
                     codigoBusqueda.setText("");
                     cantidadPizza.setText("1");
                 }
                 
                 return;
             } 
              
            DefaultTableModel model = (DefaultTableModel) tablaDetalleVenta.getModel();
            Vector row = new Vector();

            int filasTotales=model.getRowCount();
            int nuevaFila=filasTotales+1;
            row.add(nuevaFila);
            row.add(cantidadPizza.getText());
            row.add(productoParaCarrrito.getDescripcion());
            row.add(productoParaCarrrito.getPrecioVenta());
            String x=(String)row.get(1);
            Double y=(Double)row.get(3);

            Double cant=Double.parseDouble(x);
            Double precio=y;

            Double subtGrid=cant*precio;

            row.add(""+subtGrid);

            row.add(productoParaCarrrito.getId());
            row.add(" ");
            
            model.addRow(row);

            tablaDetalleVenta.getColumn("Id producto").setWidth(0);
            tablaDetalleVenta.getColumn("Id producto").setMinWidth(0);
            tablaDetalleVenta.getColumn("Id producto").setMaxWidth(0);
            tablaDetalleVenta.getColumn("Tamaño").setWidth(0);
            tablaDetalleVenta.getColumn("Tamaño").setMinWidth(0);
            tablaDetalleVenta.getColumn("Tamaño").setMaxWidth(0);
             ///obtenemos el descuento
            String descuentoUnidades=comboDescuentos.getSelectedItem().toString();
            
            String descuentoDecimal=descuentoUnidades.replace("%","");
            Double valorDescuento=0.0;
            
            if(descuentoDecimal.equals("---")){
                
            }else{
                valorDescuento=Double.parseDouble(descuentoDecimal);
                valorDescuento=valorDescuento/100;
            }
            
            double granTotal=0.0;
            for(int i=0;i<model.getRowCount();i++){

                Double val=(Double)model.getValueAt(i, 3);
                String cantidad=(String)model.getValueAt(i, 1);
                Double valNumeric=val;
                Double cantidadNumeric=Double.parseDouble(cantidad);
                Double numericTotal=valNumeric*cantidadNumeric;
                granTotal+=numericTotal;
            }
            Double descuentoTotal=granTotal*valorDescuento;
             
            ivaTotal.setText("$"+decimales.format(granTotal*ivaConfigurado));
            etiquetaGranTotal.setText("$"+decimales.format((granTotal-descuentoTotal)));
            codigoBusqueda.setText("");
            descripcionManual.setText("");
            cantidadPizza.setText("1");
            codigoBusqueda.setFocusable(true);
            
            /////agregamos el producto a la lista de tipos para saber si imprimir ticket o no
            tiposDeProductoAgregados.add(tipoDeProductoAgregado);
            
          }else{
              JOptionPane.showMessageDialog(null,"No existe producto con el código proporcionado");
              codigoBusqueda.setText("");
              descripcionManual.setText("");
          }
          
        }catch(Exception e){
                e.printStackTrace();
        } 
        
    }
    public void agregarListener(){
         selectionModel.addListSelectionListener(this);
    }

    
    public void marcarRefrescoUtilizadoEnPaquete(){
        
        String refrescoSel=jComboBox2.getSelectedItem().toString();
        
        DBConect conexion=new DBConect();  
        
        try{

          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();
          
          String sqlString1="Select * from productos where descripcion='"+refrescoSel+"' and tipoProducto=2";
          
          ResultSet rs1 = statement.executeQuery(sqlString1);
          int idRefresco=0;
          
          while(rs1.next()){
              idRefresco=rs1.getInt(1);
          }
          
          
          String vendidos="select cantidadVendidos,unidadesEnCaja from productos where idProductos="+idRefresco;
                
            ResultSet rsP=statement.executeQuery(vendidos);
            Double cantidadVend=0.0;
            Double cantidadAlm=0.0;
            while(rsP.next()){
               cantidadVend=rsP.getDouble(1);
               cantidadAlm=rsP.getDouble(2);
            }
            
          ///indicamos que un refresco se fuen en un paquete de venta por eso fijamos el valor a 1  
          marcarProductoComoUtilizadoEnVenta(""+idRefresco,""+cantidadAlm,""+cantidadVend,"1");
          
          indicadorRefrescoUtilizadoEnPaquete=0;
        }catch(Exception e){
            e.printStackTrace();
        }
    
    
    }
    
    public void marcarProductoComoUtilizadoEnVenta(String idProd,String cantidadAlmacen,String cantidadVendida, String catidadVendidaAlMomento){
        ///se descuenta del total de unidades y se incrementa el numero de vendidos
        
        Double cantidadAl=Double.parseDouble(cantidadAlmacen);
        Double cantidadVe=Double.parseDouble(cantidadVendida);
        Double cantidadVendidaAlMomento=Double.parseDouble(catidadVendidaAlMomento);
        DBConect conexion=new DBConect();  
        
        try{

          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();
          
          Double cantidadUpdate=cantidadAl-cantidadVendidaAlMomento;
          
          if(cantidadUpdate<0.0){
              cantidadUpdate=0.0;
          }
          
          String sqlString="Update productos set unidadesEnCaja="+ cantidadUpdate +
                           " , cantidadVendidos="+(cantidadVe+cantidadVendidaAlMomento)+
                           " where idProductos='"+idProd+"'";
          
          statement.executeUpdate(sqlString);
          
          
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    int numeroCambios=0;
    public void agregarListenerACodigo(){

        
        codigoBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
              warn();
            }
            public void removeUpdate(DocumentEvent e) {
               //warn();
            }
            public void insertUpdate(DocumentEvent e) {
              warn();
            }
            
           
      });
    
    }
    
    public void warn() {
        
        if (timer.isRunning()) {
            
            timer.stop();
        }
   
       timer.start();
         
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        panelAgregarClienteVenta1 = new javax.swing.JPanel();
        labelNombreC2 = new javax.swing.JLabel();
        newNombreVenta1 = new javax.swing.JTextField();
        newApellidoPVenta1 = new javax.swing.JTextField();
        newApellidoMVenta1 = new javax.swing.JTextField();
        newDireccionVenta1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        labelApellidoPC2 = new javax.swing.JLabel();
        labelApellidoMC2 = new javax.swing.JLabel();
        agregarClienteAGrid1 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        newTel2 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        NombreBusqueda2 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        apellidoBusqueda2 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        apellidoMBusqueda2 = new javax.swing.JTextField();
        BuscadorModificacionCliente = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaClientesEncontradosEdicion = new javax.swing.JTable();
        DesactivarCliente = new javax.swing.JButton();
        MostrarPanelModificacionCliente = new javax.swing.JButton();
        panelModificarCliente = new javax.swing.JPanel();
        newNombreVenta2 = new javax.swing.JTextField();
        labelNombreC3 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        newDireccionVenta3 = new javax.swing.JTextField();
        labelApellidoPC3 = new javax.swing.JLabel();
        newApellidoPVenta2 = new javax.swing.JTextField();
        newApellidoMVenta2 = new javax.swing.JTextField();
        labelApellidoMC3 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        newTelVenta4 = new javax.swing.JTextField();
        GuardarCambiosCliente = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        descripcionAltaText = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        codigoAltaText = new javax.swing.JTextField();
        comboTipoProducto = new javax.swing.JComboBox();
        jLabel70 = new javax.swing.JLabel();
        panelAltaPizza = new javax.swing.JPanel();
        pFamiliar = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        pGrande = new javax.swing.JTextField();
        pChica = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        pMediana = new javax.swing.JTextField();
        panelAltaGeneral = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        precioVentaAltaText = new javax.swing.JTextField();
        presentacionLista = new javax.swing.JComboBox();
        uMedidaLista = new javax.swing.JComboBox();
        precioCompraAltaText = new javax.swing.JTextField();
        unidadesEnCajaText = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        descripcionProdFiltro = new javax.swing.JTextField();
        BusquedaProductosTodo = new javax.swing.JButton();
        agregarProducto = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        listaProductosAModificar = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        removerProducto = new javax.swing.JButton();
        editarProducto = new javax.swing.JButton();
        panelModificarProducto = new javax.swing.JPanel();
        descripcionAltaText1 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        precioCompraAltaText1 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        precioVentaAltaText1 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        unidadesEnCajaText1 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        uMedidaLista1 = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        presentacionLista1 = new javax.swing.JComboBox();
        ModificarProductosGuardar = new javax.swing.JButton();
        tabReportes = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        fechaFin = new com.toedter.calendar.JDateChooser();
        fechaInicio = new com.toedter.calendar.JDateChooser();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        listaTodosLosUsuarios = new javax.swing.JComboBox();
        generarReporteVentas = new javax.swing.JButton();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel37 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        labelTotalPeriodo = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        labelUsuarioQueVendio = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        listaReporte = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        jLabel42 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        labelTotalIvaPeriodo = new javax.swing.JLabel();
        TabUsuarios = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        c1 = new javax.swing.JCheckBox();
        c2 = new javax.swing.JCheckBox();
        c3 = new javax.swing.JCheckBox();
        c4 = new javax.swing.JCheckBox();
        c5 = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        newPassToAdd = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        newUserToAdd = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        newNameToAdd = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        AgregarNuevoUsuario = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        usuarioFiltro = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        nombreUsuarioFiltro = new javax.swing.JTextField();
        BuscarUusariosRegistrados = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        listadoUsuariosRegistrados = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        DesactivarUsuario = new javax.swing.JButton();
        ModificarUsuario = new javax.swing.JButton();
        panelModificarUsuario = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        c12 = new javax.swing.JCheckBox();
        c22 = new javax.swing.JCheckBox();
        c32 = new javax.swing.JCheckBox();
        c42 = new javax.swing.JCheckBox();
        c52 = new javax.swing.JCheckBox();
        jPanel18 = new javax.swing.JPanel();
        newPassToAdd2 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        newUserToAdd1 = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        newNameToAdd2 = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        tabConfiguracion = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        valorIVA = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        valorD1 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        valorD2 = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        valorD3 = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        valorD4 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        valorD5 = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        valorSucursal = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        valorRFC = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        valorDireccion = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        valorSlogan = new javax.swing.JTextField();
        actualizarConfiguracion = new javax.swing.JButton();
        cajaTab = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cantidadRetiro = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        motivoRetiro = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        listadoRetiro = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        totalGasto = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        totalVenta = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        totalBalance = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        coin1 = new javax.swing.JTextField();
        jLabel85 = new javax.swing.JLabel();
        coin2 = new javax.swing.JTextField();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        coin3 = new javax.swing.JTextField();
        coin4 = new javax.swing.JTextField();
        coin5 = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        coin6 = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        paperCoin1 = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        paperCoin2 = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        paperCoin3 = new javax.swing.JTextField();
        paperCoin4 = new javax.swing.JTextField();
        paperCoin5 = new javax.swing.JTextField();
        jLabel95 = new javax.swing.JLabel();
        paperCoin6 = new javax.swing.JTextField();
        jLabel96 = new javax.swing.JLabel();
        totalCoins = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        efectivoInicialLabel = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        labelBalanceDiaCorte = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        produccionRebanada = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        panelBusquedaClientes = new javax.swing.JPanel();
        labelCliente = new javax.swing.JLabel();
        clienteBusqueda = new javax.swing.JTextField();
        labelNombreC = new javax.swing.JLabel();
        nombreBusqueda = new javax.swing.JTextField();
        apellidoPBusqueda = new javax.swing.JTextField();
        labelApellidoPC = new javax.swing.JLabel();
        labelApellidoMC = new javax.swing.JLabel();
        apellidoMBusqueda = new javax.swing.JTextField();
        botonBusquedaCliente = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        direccionBusqueda = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaClientesEncontrados = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        labelAgergarCliente = new javax.swing.JLabel();
        panelAgregarClienteVenta = new javax.swing.JPanel();
        labelNombreC1 = new javax.swing.JLabel();
        newNombreVenta = new javax.swing.JTextField();
        newApellidoPVenta = new javax.swing.JTextField();
        newApellidoMVenta = new javax.swing.JTextField();
        newDireccionVenta = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        labelApellidoPC1 = new javax.swing.JLabel();
        labelApellidoMC1 = new javax.swing.JLabel();
        agregarClienteAGrid = new javax.swing.JButton();
        labelCliente1 = new javax.swing.JLabel();
        newClienteVentaTel = new javax.swing.JTextField();
        preAgregarCliente = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        codigoBusqueda = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        cantidadPizza = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        descripcionManual = new javax.swing.JTextField();
        busquedaManual = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        tamanioPizza = new javax.swing.JComboBox();
        jLabel44 = new javax.swing.JLabel();
        labelPizza2 = new javax.swing.JLabel();
        seleccionPizza2 = new javax.swing.JComboBox();
        jLabel71 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel74 = new javax.swing.JLabel();
        seleccionPizza1 = new javax.swing.JComboBox();
        seleccionExtras1 = new javax.swing.JComboBox();
        seleccionExtras2 = new javax.swing.JComboBox();
        jLabel73 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        seleccionPizza3 = new javax.swing.JComboBox();
        jPanel23 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDetalleVenta = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        confirmarVentaBoton = new javax.swing.JButton();
        etiquetaGranTotal = new javax.swing.JLabel();
        eliminarProductoDeVenta = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        efectivoRecibido = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        cambioTotal = new javax.swing.JLabel();
        comboDescuentos = new javax.swing.JComboBox();
        jLabel56 = new javax.swing.JLabel();
        labelIVA = new javax.swing.JLabel();
        ivaTotal = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        consecutivoReimpresionTicket = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        ReimpresionTicket = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel72 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel2.setAutoscrolls(true);

        panelAgregarClienteVenta1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registrar nuevo cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        labelNombreC2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelNombreC2.setText("Nombre:");

        newNombreVenta1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        newApellidoPVenta1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        newApellidoMVenta1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        newDireccionVenta1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Dirección:");

        labelApellidoPC2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelApellidoPC2.setText("Apellido P:");

        labelApellidoMC2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelApellidoMC2.setText("Apellido M:");

        agregarClienteAGrid1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1446634246_Add-Male-User.png"))); // NOI18N
        agregarClienteAGrid1.setText("Agregar cliente");
        agregarClienteAGrid1.setToolTipText("");
        agregarClienteAGrid1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarClienteAGrid1ActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText("Telefóno:");

        newTel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel12.setText("Listado de clientes");

        jLabel24.setText("Nombre:");

        jLabel23.setText("Apellido p.");

        jLabel25.setText("Apellido m.");

        BuscadorModificacionCliente.setText("Buscar");
        BuscadorModificacionCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscadorModificacionClienteActionPerformed(evt);
            }
        });

        tablaClientesEncontradosEdicion.setModel(resultadoClientesEdicion);
        jScrollPane4.setViewportView(tablaClientesEncontradosEdicion);

        DesactivarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1448503464_sign-error.png"))); // NOI18N
        DesactivarCliente.setText("Desactivar cliente");
        DesactivarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DesactivarClienteActionPerformed(evt);
            }
        });

        MostrarPanelModificacionCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1448503954_pencil.png"))); // NOI18N
        MostrarPanelModificacionCliente.setText("Modificar cliente");
        MostrarPanelModificacionCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MostrarPanelModificacionClienteActionPerformed(evt);
            }
        });

        panelModificarCliente.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Modificar datos de cliente"), "Modificar datos de cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        newNombreVenta2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        labelNombreC3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelNombreC3.setText("Nombre:");

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setText("Dirección:");

        newDireccionVenta3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        labelApellidoPC3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelApellidoPC3.setText("Apellido P:");

        newApellidoPVenta2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        newApellidoMVenta2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        labelApellidoMC3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelApellidoMC3.setText("Apellido M:");

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel28.setText("Telefóno:");

        newTelVenta4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        GuardarCambiosCliente.setText("Guardar cambios");
        GuardarCambiosCliente.setToolTipText("");
        GuardarCambiosCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarCambiosClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelModificarClienteLayout = new javax.swing.GroupLayout(panelModificarCliente);
        panelModificarCliente.setLayout(panelModificarClienteLayout);
        panelModificarClienteLayout.setHorizontalGroup(
            panelModificarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModificarClienteLayout.createSequentialGroup()
                .addGroup(panelModificarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(labelNombreC3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelModificarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelModificarClienteLayout.createSequentialGroup()
                        .addComponent(newNombreVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelApellidoPC3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newApellidoPVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelApellidoMC3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newApellidoMVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newTelVenta4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(newDireccionVenta3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(GuardarCambiosCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelModificarClienteLayout.setVerticalGroup(
            panelModificarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModificarClienteLayout.createSequentialGroup()
                .addGroup(panelModificarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelModificarClienteLayout.createSequentialGroup()
                        .addGroup(panelModificarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelNombreC3)
                            .addComponent(newApellidoPVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(newApellidoMVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(newNombreVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelApellidoPC3)
                            .addComponent(labelApellidoMC3)
                            .addComponent(jLabel28)
                            .addComponent(newTelVenta4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelModificarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(newDireccionVenta3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27)))
                    .addComponent(GuardarCambiosCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelAgregarClienteVenta1Layout = new javax.swing.GroupLayout(panelAgregarClienteVenta1);
        panelAgregarClienteVenta1.setLayout(panelAgregarClienteVenta1Layout);
        panelAgregarClienteVenta1Layout.setHorizontalGroup(
            panelAgregarClienteVenta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregarClienteVenta1Layout.createSequentialGroup()
                .addGroup(panelAgregarClienteVenta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAgregarClienteVenta1Layout.createSequentialGroup()
                        .addGroup(panelAgregarClienteVenta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAgregarClienteVenta1Layout.createSequentialGroup()
                                .addGroup(panelAgregarClienteVenta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(labelNombreC2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelAgregarClienteVenta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelAgregarClienteVenta1Layout.createSequentialGroup()
                                        .addComponent(newNombreVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelApellidoPC2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(newApellidoPVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(labelApellidoMC2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(newApellidoMVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(newTel2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(newDireccionVenta1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(agregarClienteAGrid1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel12)
                            .addGroup(panelAgregarClienteVenta1Layout.createSequentialGroup()
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(NombreBusqueda2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(apellidoBusqueda2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(apellidoMBusqueda2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BuscadorModificacionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelAgregarClienteVenta1Layout.createSequentialGroup()
                                .addComponent(DesactivarCliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(MostrarPanelModificacionCliente)))
                        .addGap(0, 2, Short.MAX_VALUE))
                    .addComponent(jScrollPane4)
                    .addComponent(panelModificarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelAgregarClienteVenta1Layout.setVerticalGroup(
            panelAgregarClienteVenta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregarClienteVenta1Layout.createSequentialGroup()
                .addGroup(panelAgregarClienteVenta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAgregarClienteVenta1Layout.createSequentialGroup()
                        .addGroup(panelAgregarClienteVenta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelNombreC2)
                            .addComponent(newApellidoPVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(newApellidoMVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(newNombreVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelApellidoPC2)
                            .addComponent(labelApellidoMC2)
                            .addComponent(jLabel26)
                            .addComponent(newTel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAgregarClienteVenta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(newDireccionVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addComponent(agregarClienteAGrid1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAgregarClienteVenta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAgregarClienteVenta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel23)
                        .addComponent(jLabel24)
                        .addComponent(NombreBusqueda2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(apellidoBusqueda2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel25)
                        .addComponent(apellidoMBusqueda2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(BuscadorModificacionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelAgregarClienteVenta1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DesactivarCliente)
                    .addComponent(MostrarPanelModificacionCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelModificarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane12.setViewportView(panelAgregarClienteVenta1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 672, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 29, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Clientes", jPanel2);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agregar productos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel14.setText("Descripción:");

        jLabel8.setText("Código:");

        codigoAltaText.setMinimumSize(new java.awt.Dimension(6, 80));
        codigoAltaText.setName(""); // NOI18N

        comboTipoProducto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---", "Pizza", "General" }));
        comboTipoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTipoProductoActionPerformed(evt);
            }
        });

        jLabel70.setText("Tipo de producto:");

        pFamiliar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pFamiliarActionPerformed(evt);
            }
        });

        jLabel68.setText("Precio mediana:");

        jLabel75.setText("Precio grande:");

        jLabel78.setText("Precio familiar:");

        jLabel43.setText("Precio chica:");

        javax.swing.GroupLayout panelAltaPizzaLayout = new javax.swing.GroupLayout(panelAltaPizza);
        panelAltaPizza.setLayout(panelAltaPizzaLayout);
        panelAltaPizzaLayout.setHorizontalGroup(
            panelAltaPizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAltaPizzaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pChica, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel68)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pMediana, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel75)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pGrande, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel78)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pFamiliar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(126, Short.MAX_VALUE))
        );
        panelAltaPizzaLayout.setVerticalGroup(
            panelAltaPizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAltaPizzaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAltaPizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(pChica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel68)
                    .addComponent(pMediana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pGrande, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel75)
                    .addComponent(jLabel78)
                    .addComponent(pFamiliar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel15.setText("Precio compra");

        jLabel16.setText("Precio venta");

        jLabel19.setText("Unidad medida:");

        presentacionLista.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---", "Caja", "Bolsa", "Costal" }));

        uMedidaLista.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---", "Pieza", "Kilogramo", "Gramo", "Litro" }));
        uMedidaLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uMedidaListaActionPerformed(evt);
            }
        });

        jLabel6.setText("Cantidad:");

        jLabel20.setText("Presentación:");

        javax.swing.GroupLayout panelAltaGeneralLayout = new javax.swing.GroupLayout(panelAltaGeneral);
        panelAltaGeneral.setLayout(panelAltaGeneralLayout);
        panelAltaGeneralLayout.setHorizontalGroup(
            panelAltaGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAltaGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(precioCompraAltaText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(precioVentaAltaText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unidadesEnCajaText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(uMedidaLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(presentacionLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(187, Short.MAX_VALUE))
        );
        panelAltaGeneralLayout.setVerticalGroup(
            panelAltaGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAltaGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAltaGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(precioCompraAltaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(precioVentaAltaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(unidadesEnCajaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uMedidaLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(presentacionLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addGap(90, 90, 90))
        );

        jLabel13.setText("Listado productos");

        jLabel22.setText("Descripción:");

        BusquedaProductosTodo.setText("Buscar");
        BusquedaProductosTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BusquedaProductosTodoActionPerformed(evt);
            }
        });

        agregarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1448503432_sign-add.png"))); // NOI18N
        agregarProducto.setText("Agregar producto");
        agregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarProductoActionPerformed(evt);
            }
        });

        listaProductosAModificar.setModel(productosEncontradosAModificar);
        jScrollPane5.setViewportView(listaProductosAModificar);

        removerProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1448503464_sign-error.png"))); // NOI18N
        removerProducto.setText("Desactivar producto");
        removerProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removerProductoActionPerformed(evt);
            }
        });

        editarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1448503954_pencil.png"))); // NOI18N
        editarProducto.setText("Modificar producto");
        editarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarProductoActionPerformed(evt);
            }
        });

        panelModificarProducto.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Modificar producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel29.setText("Descripción:");

        jLabel30.setText("Precio compra");

        jLabel31.setText("Precio venta");

        jLabel32.setText("Cantidad:");

        jLabel33.setText("Unidad medida:");

        uMedidaLista1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---", "Pieza", "Kilogramo", "Gramo", "Litro" }));
        uMedidaLista1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uMedidaLista1ActionPerformed(evt);
            }
        });

        jLabel34.setText("Presentación:");

        presentacionLista1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---", "Caja", "Bolsa", "Costal" }));

        ModificarProductosGuardar.setText("Guardar cambios");
        ModificarProductosGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModificarProductosGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelModificarProductoLayout = new javax.swing.GroupLayout(panelModificarProducto);
        panelModificarProducto.setLayout(panelModificarProductoLayout);
        panelModificarProductoLayout.setHorizontalGroup(
            panelModificarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModificarProductoLayout.createSequentialGroup()
                .addGroup(panelModificarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelModificarProductoLayout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uMedidaLista1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(presentacionLista1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelModificarProductoLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descripcionAltaText1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(precioCompraAltaText1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(precioVentaAltaText1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unidadesEnCajaText1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ModificarProductosGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 343, Short.MAX_VALUE))
        );
        panelModificarProductoLayout.setVerticalGroup(
            panelModificarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModificarProductoLayout.createSequentialGroup()
                .addGroup(panelModificarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(descripcionAltaText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(precioCompraAltaText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(precioVentaAltaText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(unidadesEnCajaText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ModificarProductosGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelModificarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(uMedidaLista1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34)
                    .addComponent(presentacionLista1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelAltaGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelAltaPizza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jLabel70)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(comboTipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(descripcionAltaText, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(codigoAltaText, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(removerProducto)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editarProducto))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jLabel22)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(descripcionProdFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BusquedaProductosTodo))
                                    .addComponent(agregarProducto))))
                        .addGap(0, 396, Short.MAX_VALUE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelModificarProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel70)
                    .addComponent(comboTipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(descripcionAltaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(codigoAltaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAltaPizza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAltaGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(agregarProducto)
                .addGap(14, 14, 14)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(descripcionProdFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BusquedaProductosTodo))
                .addGap(17, 17, 17)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(removerProducto, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(editarProducto, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelModificarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(652, Short.MAX_VALUE))
        );

        jScrollPane11.setViewportView(jPanel8);

        jTabbedPane1.addTab("Productos", jScrollPane11);

        tabReportes.setAutoscrolls(true);
        tabReportes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabReportesMouseClicked(evt);
            }
        });

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos para generar el reporte", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel35.setText("Desde:");

        jLabel36.setText("Hasta:");

        listaTodosLosUsuarios.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---" }));

        generarReporteVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1449296269_sales-report.png"))); // NOI18N
        generarReporteVentas.setText("Generar reporte");
        generarReporteVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarReporteVentasActionPerformed(evt);
            }
        });

        jSpinner1.setModel(modeloSpinnerDesde);

        jSpinner2.setModel(modeloSpinnerHasta);

        jLabel37.setText("Usuario:");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listaTodosLosUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generarReporteVentas)
                .addGap(27, 27, 27))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(listaTodosLosUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(generarReporteVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resultados del periodo seleccionado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel39.setText("Total de ventas:");

        labelTotalPeriodo.setText("----");

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel40.setText("Usuario que realizó la venta:");

        labelUsuarioQueVendio.setText("---");

        listaReporte.setAutoCreateRowSorter(true);
        listaReporte.setModel(listaDetalleVentasPeriodo);
        jScrollPane6.setViewportView(listaReporte);

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel42.setText("Detalle de los productos vendidos:");

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel46.setText("Total de IVA:");

        labelTotalIvaPeriodo.setText("----");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelTotalPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelTotalIvaPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelUsuarioQueVendio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel42)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 902, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(labelTotalPeriodo)
                    .addComponent(jLabel40)
                    .addComponent(labelUsuarioQueVendio)
                    .addComponent(jLabel46)
                    .addComponent(labelTotalIvaPeriodo))
                .addGap(21, 21, 21)
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(678, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                .addGap(28, 28, 28))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane10.setViewportView(jPanel9);

        javax.swing.GroupLayout tabReportesLayout = new javax.swing.GroupLayout(tabReportes);
        tabReportes.setLayout(tabReportesLayout);
        tabReportesLayout.setHorizontalGroup(
            tabReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabReportesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 933, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabReportesLayout.setVerticalGroup(
            tabReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Reporte de ventas", tabReportes);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Crear nuevo usuario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selecciona los privilegios que deseas otorgar al usuario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        c1.setText("Administración de Clientes");
        c1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c1ActionPerformed(evt);
            }
        });

        c2.setText("Administración de Usuarios");
        c2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c2ActionPerformed(evt);
            }
        });

        c3.setText("Administración de Inventario");

        c4.setSelected(true);
        c4.setText("Ejecución de ventas");

        c5.setText("Generación de reportes");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(c1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c4))
                    .addComponent(c5)))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(c1)
                    .addComponent(c2)
                    .addComponent(c3)
                    .addComponent(c4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, Short.MAX_VALUE)
                .addComponent(c5))
        );

        jLabel50.setText("Nombre completo:");

        jLabel49.setText("Contraseña:");

        newNameToAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newNameToAddActionPerformed(evt);
            }
        });

        jLabel48.setText("Usuario:");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel48)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newUserToAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newPassToAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newNameToAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(142, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(newUserToAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49)
                    .addComponent(newPassToAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50)
                    .addComponent(newNameToAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        AgregarNuevoUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1448503432_sign-add.png"))); // NOI18N
        AgregarNuevoUsuario.setText("Agregar usuario");
        AgregarNuevoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AgregarNuevoUsuarioActionPerformed(evt);
            }
        });

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Usuarios existentes"));

        jLabel51.setText("Usuario");

        jLabel52.setText("Nombre:");

        BuscarUusariosRegistrados.setText("Buscar");
        BuscarUusariosRegistrados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarUusariosRegistradosActionPerformed(evt);
            }
        });

        listadoUsuariosRegistrados.setModel(usuariosEncontrados);
        jScrollPane7.setViewportView(listadoUsuariosRegistrados);

        DesactivarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1448503464_sign-error.png"))); // NOI18N
        DesactivarUsuario.setText("Desactivar usuario");
        DesactivarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DesactivarUsuarioActionPerformed(evt);
            }
        });

        ModificarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1448503954_pencil.png"))); // NOI18N
        ModificarUsuario.setText("Modificar usuario");
        ModificarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModificarUsuarioActionPerformed(evt);
            }
        });

        panelModificarUsuario.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Modificar usuario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selecciona los privilegios que deseas otorgar al usuario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        c12.setText("Administración de Clientes");
        c12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c12ActionPerformed(evt);
            }
        });

        c22.setText("Administración de Usuarios");
        c22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c22ActionPerformed(evt);
            }
        });

        c32.setText("Administración de Inventario");

        c42.setText("Ejecución de ventas");

        c52.setText("Generación de reportes");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(c12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c42))
                    .addComponent(c52)))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(c12)
                    .addComponent(c22)
                    .addComponent(c32)
                    .addComponent(c42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(c52)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel53.setText("Nombre completo:");

        jLabel54.setText("Contraseña:");

        newNameToAdd2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newNameToAdd2ActionPerformed(evt);
            }
        });

        jLabel55.setText("Usuario:");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newUserToAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel54)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newPassToAdd2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel53)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newNameToAdd2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(142, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(newUserToAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54)
                    .addComponent(newPassToAdd2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53)
                    .addComponent(newNameToAdd2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButton7.setText("Guardar cambios");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelModificarUsuarioLayout = new javax.swing.GroupLayout(panelModificarUsuario);
        panelModificarUsuario.setLayout(panelModificarUsuarioLayout);
        panelModificarUsuarioLayout.setHorizontalGroup(
            panelModificarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModificarUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelModificarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelModificarUsuarioLayout.createSequentialGroup()
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(354, Short.MAX_VALUE))
        );
        panelModificarUsuarioLayout.setVerticalGroup(
            panelModificarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelModificarUsuarioLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelModificarUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usuarioFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombreUsuarioFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BuscarUusariosRegistrados)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(DesactivarUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ModificarUsuario)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(panelModificarUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(usuarioFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52)
                    .addComponent(nombreUsuarioFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BuscarUusariosRegistrados))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DesactivarUsuario)
                    .addComponent(ModificarUsuario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelModificarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AgregarNuevoUsuario)))
                        .addGap(0, 406, Short.MAX_VALUE))
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(AgregarNuevoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane9.setViewportView(jPanel11);

        javax.swing.GroupLayout TabUsuariosLayout = new javax.swing.GroupLayout(TabUsuarios);
        TabUsuarios.setLayout(TabUsuariosLayout);
        TabUsuariosLayout.setHorizontalGroup(
            TabUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TabUsuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 933, Short.MAX_VALUE)
                .addContainerGap())
        );
        TabUsuariosLayout.setVerticalGroup(
            TabUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TabUsuariosLayout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Usuarios", TabUsuarios);

        jLabel47.setText("IVA:");

        jLabel57.setText("Descuento 1:");

        jLabel58.setText("Descuento 2:");

        jLabel59.setText("Descuento 3:");

        jLabel60.setText("Descuento 4:");

        jLabel61.setText("Descuento 5:");

        jLabel62.setText("Por favor, introduce el porcentaje en números enteros, que deseas asignar a los siguientes valores del sistema,por  ejemplo, si quieres el IVA a 16% debes escribir 16");

        jLabel63.setText("Los siguientes datos que proporciones, aparecerán en el ticket cuando se realicé una venta, en caso de que No quieras algun dato, solo dejalo en blanco.");

        jLabel64.setText("RFC:");

        jLabel65.setText("Sucursal:");

        jLabel66.setText("Dirección:");

        jLabel67.setText("Slogan:");

        actualizarConfiguracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1449960748_floppy.png"))); // NOI18N
        actualizarConfiguracion.setText("Guardar");
        actualizarConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarConfiguracionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel65)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(valorSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel64)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(valorRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel66)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(valorDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel67)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(valorSlogan))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jLabel47)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(valorIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel57)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(valorD1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel58)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(valorD2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel59)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(valorD3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel60)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(valorD4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel61)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(valorD5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(actualizarConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(valorIVA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57)
                    .addComponent(valorD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58)
                    .addComponent(valorD2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59)
                    .addComponent(valorD3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60)
                    .addComponent(valorD4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61)
                    .addComponent(valorD5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel63)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(valorSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65)
                    .addComponent(valorRFC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel66)
                    .addComponent(valorDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67)
                    .addComponent(valorSlogan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(actualizarConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout tabConfiguracionLayout = new javax.swing.GroupLayout(tabConfiguracion);
        tabConfiguracion.setLayout(tabConfiguracionLayout);
        tabConfiguracionLayout.setHorizontalGroup(
            tabConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabConfiguracionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabConfiguracionLayout.setVerticalGroup(
            tabConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabConfiguracionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(458, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Configuración", tabConfiguracion);

        jLabel9.setText("Cantidad:");

        jLabel69.setText("Motivo:");

        jButton1.setText("Registrar Gasto");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        listadoRetiro.setModel(modeloRetiro);
        jScrollPane8.setViewportView(listadoRetiro);

        jLabel76.setText("Gastos del día:");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cantidadRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel69)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(motivoRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jLabel76)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 792, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(129, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cantidadRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel69)
                    .addComponent(motivoRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel76)
                .addGap(16, 16, 16)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel77.setText("Total gastos:");

        totalGasto.setText("---");

        jLabel79.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel79.setText("Total venta:");

        totalVenta.setText("---");

        jLabel81.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel81.setText("Balance del día:");

        totalBalance.setText("---");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Arqueo de efectivo:");

        jLabel10.setText("A continuación, captura la cantidad de monedas o billetes que entregas, segun su denominación:");

        jLabel83.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel83.setText("Monedas");

        jLabel84.setText("$.50");

        coin1.setText("0");

        jLabel85.setText("$1");

        coin2.setText("0");

        jLabel86.setText("$2");

        jLabel87.setText("$5");

        jLabel88.setText("$10");

        coin3.setText("0");

        coin4.setText("0");

        coin5.setText("0");

        jLabel97.setText("$20");

        coin6.setText("0");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel97, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel85, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel84, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel86, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jLabel87)
                        .addComponent(jLabel88)))
                .addGap(18, 18, 18)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(coin5, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(coin2)
                    .addComponent(coin3)
                    .addComponent(coin1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(coin4)
                    .addComponent(coin6))
                .addGap(25, 25, 25))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jLabel83)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84)
                    .addComponent(coin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel85)
                    .addComponent(coin2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel86)
                    .addComponent(coin3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(coin4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel88)
                    .addComponent(coin5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel97)
                    .addComponent(coin6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel89.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel89.setText("Billetes");

        jLabel90.setText("$20");

        paperCoin1.setText("0");

        jLabel91.setText("$50");

        paperCoin2.setText("0");

        jLabel92.setText("$100");

        jLabel93.setText("$200");

        jLabel94.setText("$500");

        paperCoin3.setText("0");

        paperCoin4.setText("0");

        paperCoin5.setText("0");

        jLabel95.setText("$1000");

        paperCoin6.setText("0");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel91, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel90, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel92, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel93)
                            .addComponent(jLabel94))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(paperCoin5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                            .addComponent(paperCoin4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(paperCoin3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(paperCoin2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(paperCoin1, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel95)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(paperCoin6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 26, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jLabel89)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(paperCoin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel91)
                    .addComponent(paperCoin2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel92)
                    .addComponent(paperCoin3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel93)
                    .addComponent(paperCoin4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel94)
                    .addComponent(paperCoin5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel95)
                    .addComponent(paperCoin6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel96.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel96.setText("Total efectivo al corte:");

        totalCoins.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalCoins.setText("---");

        jLabel98.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel98.setText("Efectivo a inicio de día:");

        efectivoInicialLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        efectivoInicialLabel.setText("---");

        jLabel100.setText("Balance del día + Efectivo al inicio del día ");

        jLabel99.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel99.setText("Balance del día:");

        jLabel101.setText("Cantidad que había en caja");

        labelBalanceDiaCorte.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelBalanceDiaCorte.setText("---");

        jLabel102.setText("Ventas totales del día");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel99, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelBalanceDiaCorte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel100, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel98)
                                    .addComponent(jLabel96))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(efectivoInicialLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(totalCoins, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel102))
                        .addGap(0, 54, Short.MAX_VALUE))
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(154, 154, 154))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel101)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel98)
                            .addComponent(efectivoInicialLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel102)
                        .addGap(3, 3, 3)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel99)
                            .addComponent(labelBalanceDiaCorte))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel100)
                        .addGap(3, 3, 3)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalCoins)
                            .addComponent(jLabel96))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1456631529_vault.png"))); // NOI18N
        jButton3.setLabel("Realizar corte de caja");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cajaTabLayout = new javax.swing.GroupLayout(cajaTab);
        cajaTab.setLayout(cajaTabLayout);
        cajaTabLayout.setHorizontalGroup(
            cajaTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cajaTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cajaTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cajaTabLayout.createSequentialGroup()
                        .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(cajaTabLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(cajaTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cajaTabLayout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(cajaTabLayout.createSequentialGroup()
                                .addComponent(jLabel77)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel79)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel81)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(66, Short.MAX_VALUE))))
        );
        cajaTabLayout.setVerticalGroup(
            cajaTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cajaTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(cajaTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel77)
                    .addComponent(totalGasto)
                    .addComponent(jLabel79)
                    .addComponent(totalVenta)
                    .addComponent(jLabel81)
                    .addComponent(totalBalance))
                .addGroup(cajaTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cajaTabLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cajaTabLayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jButton3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Caja", cajaTab);

        jPanel24.setEnabled(false);

        jLabel21.setText("Por favor introduce la cantidad de rebanadas extras que se han cocinado:");

        jButton4.setText("Agregar producción");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(produccionRebanada, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addGap(0, 137, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(produccionRebanada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(jButton4))
                .addGap(0, 78, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 191, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 598, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Captura de rebanadas", jPanel24);

        jScrollPane3.setAutoscrolls(true);

        panelBusquedaClientes.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Busqueda de cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        panelBusquedaClientes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        labelCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelCliente.setText("Cliente:");

        clienteBusqueda.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        clienteBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clienteBusquedaActionPerformed(evt);
            }
        });

        labelNombreC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelNombreC.setText("Nombre:");

        nombreBusqueda.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        apellidoPBusqueda.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        labelApellidoPC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelApellidoPC.setText("Apellido P:");

        labelApellidoMC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelApellidoMC.setText("Apellido M:");

        apellidoMBusqueda.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        botonBusquedaCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1446600389_system-search.png"))); // NOI18N
        botonBusquedaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBusquedaClienteActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Dirección:");

        direccionBusqueda.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout panelBusquedaClientesLayout = new javax.swing.GroupLayout(panelBusquedaClientes);
        panelBusquedaClientes.setLayout(panelBusquedaClientesLayout);
        panelBusquedaClientesLayout.setHorizontalGroup(
            panelBusquedaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBusquedaClientesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBusquedaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelCliente)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBusquedaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelBusquedaClientesLayout.createSequentialGroup()
                        .addComponent(clienteBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelNombreC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombreBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelApellidoPC)
                        .addGap(2, 2, 2)
                        .addComponent(apellidoPBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelApellidoMC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(apellidoMBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(direccionBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonBusquedaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelBusquedaClientesLayout.setVerticalGroup(
            panelBusquedaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBusquedaClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBusquedaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCliente)
                    .addComponent(clienteBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNombreC)
                    .addComponent(apellidoPBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelApellidoPC)
                    .addComponent(labelApellidoMC)
                    .addComponent(apellidoMBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nombreBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBusquedaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(direccionBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(panelBusquedaClientesLayout.createSequentialGroup()
                .addComponent(botonBusquedaCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resultado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel2.setText("Selecciona un cliente para realizar la venta");

        tablaClientesEncontrados.setModel(resultadoClientes);
        tablaClientesEncontrados.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablaClientesEncontrados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaClientesEncontradosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaClientesEncontrados);

        labelAgergarCliente.setText("Si no encuentras el cliente que buscas en la lista, por favor agrega un nuevo cliente.");

        panelAgregarClienteVenta.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registrar nuevo cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        labelNombreC1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelNombreC1.setText("Nombre:");

        newNombreVenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        newApellidoPVenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        newApellidoMVenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        newDireccionVenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Dirección:");

        labelApellidoPC1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelApellidoPC1.setText("Apellido P:");

        labelApellidoMC1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelApellidoMC1.setText("Apellido M:");

        agregarClienteAGrid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1446634246_Add-Male-User.png"))); // NOI18N
        agregarClienteAGrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarClienteAGridActionPerformed(evt);
            }
        });

        labelCliente1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelCliente1.setText("Telefono:");

        newClienteVentaTel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        newClienteVentaTel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newClienteVentaTelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAgregarClienteVentaLayout = new javax.swing.GroupLayout(panelAgregarClienteVenta);
        panelAgregarClienteVenta.setLayout(panelAgregarClienteVentaLayout);
        panelAgregarClienteVentaLayout.setHorizontalGroup(
            panelAgregarClienteVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregarClienteVentaLayout.createSequentialGroup()
                .addGroup(panelAgregarClienteVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(labelNombreC1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAgregarClienteVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelAgregarClienteVentaLayout.createSequentialGroup()
                        .addComponent(newNombreVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelApellidoPC1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newApellidoPVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelApellidoMC1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newApellidoMVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelCliente1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(newClienteVentaTel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(newDireccionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(agregarClienteAGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelAgregarClienteVentaLayout.setVerticalGroup(
            panelAgregarClienteVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregarClienteVentaLayout.createSequentialGroup()
                .addGroup(panelAgregarClienteVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAgregarClienteVentaLayout.createSequentialGroup()
                        .addGroup(panelAgregarClienteVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelNombreC1)
                            .addComponent(newApellidoPVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(newApellidoMVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(newNombreVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelApellidoPC1)
                            .addComponent(labelApellidoMC1)
                            .addComponent(labelCliente1)
                            .addComponent(newClienteVentaTel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(panelAgregarClienteVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(newDireccionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(agregarClienteAGrid, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        preAgregarCliente.setText("Agregar cliente");
        preAgregarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preAgregarClienteActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registrar venta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Busqueda de producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel5.setText("Cantidad:");

        codigoBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigoBusquedaActionPerformed(evt);
            }
        });

        jLabel17.setText("Código:");

        cantidadPizza.setText("1");

        jLabel18.setText("Descripción:");

        busquedaManual.setText("Buscar");
        busquedaManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                busquedaManualActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(codigoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cantidadPizza, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descripcionManual, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(busquedaManual)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(descripcionManual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(busquedaManual))
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17)
                        .addComponent(codigoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(cantidadPizza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pizza", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        tamanioPizza.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Chica", "Mediana", "Grande", "Familiar" }));

        jLabel44.setText("Tamaño:");

        labelPizza2.setText("Sabores pizza 2:");

        seleccionPizza2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---" }));

        jLabel71.setText("Sabor pizza 1:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Fanta", "Sprite", "Fresca", "Manzana" }));
        jComboBox2.setToolTipText("");

        jLabel74.setText("Refresco:");

        seleccionPizza1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---" }));

        seleccionExtras1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---" }));

        seleccionExtras2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---" }));

        jLabel73.setText("Extras:");

        jLabel45.setText("Extras:");

        seleccionPizza3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---" }));
        seleccionPizza3.setToolTipText("");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1446637777_shopping-add.png"))); // NOI18N
        jButton2.setText("Agregar a la orden");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tamanioPizza, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel74)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel71)
                    .addComponent(labelPizza2)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(seleccionPizza2, 0, 410, Short.MAX_VALUE)
                            .addComponent(seleccionPizza1, 0, 410, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(seleccionExtras1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(seleccionExtras2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(seleccionPizza3, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(tamanioPizza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel74))
                .addGap(23, 23, 23)
                .addComponent(jLabel71)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(seleccionPizza1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45)
                    .addComponent(seleccionExtras1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPizza2)
                .addGap(6, 6, 6)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(seleccionPizza2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(seleccionExtras2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(seleccionPizza3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel74.getAccessibleContext().setAccessibleName("");
        jLabel74.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tablaDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "cantidad", "descripcion", "precio", "subtotal", "Id producto", "Tamaño"
            }
        ));
        tablaDetalleVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDetalleVentaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaDetalleVenta);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Total:");

        confirmarVentaBoton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        confirmarVentaBoton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1446685215_Purse.png"))); // NOI18N
        confirmarVentaBoton.setText("Confirmar venta");
        confirmarVentaBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmarVentaBotonActionPerformed(evt);
            }
        });

        etiquetaGranTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        etiquetaGranTotal.setText("---");

        eliminarProductoDeVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/1448503464_sign-error.png"))); // NOI18N
        eliminarProductoDeVenta.setText("Eliminar producto");
        eliminarProductoDeVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarProductoDeVentaActionPerformed(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel38.setText("Efectivo:");

        efectivoRecibido.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        efectivoRecibido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                efectivoRecibidoActionPerformed(evt);
            }
        });
        efectivoRecibido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                efectivoRecibidoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                efectivoRecibidoKeyReleased(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel41.setText("Cambio:");

        cambioTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cambioTotal.setText("$---");

        comboDescuentos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---" }));

        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel56.setText("Descuento:");

        labelIVA.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelIVA.setText("IVA:");

        ivaTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ivaTotal.setText("---");
        ivaTotal.setToolTipText("");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(eliminarProductoDeVenta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel41)
                    .addComponent(jLabel56)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(comboDescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelIVA)
                        .addGap(4, 4, 4)
                        .addComponent(ivaTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cambioTotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addComponent(efectivoRecibido, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(92, 92, 92)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(etiquetaGranTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(confirmarVentaBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(eliminarProductoDeVenta)
                            .addComponent(jLabel38)
                            .addComponent(efectivoRecibido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(etiquetaGranTotal)
                            .addComponent(jLabel56)
                            .addComponent(comboDescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelIVA)
                            .addComponent(ivaTotal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(confirmarVentaBoton)
                            .addComponent(jLabel41)
                            .addComponent(cambioTotal))))
                .addGap(22, 22, 22))
        );

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Reimpresión de ticket", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel80.setText("Número de ticket que desea imprimir:");

        consecutivoReimpresionTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consecutivoReimpresionTicketActionPerformed(evt);
            }
        });

        jLabel82.setText("Nota: Solo se imprimen tickets generados el día de hoy.");

        ReimpresionTicket.setText("Imprimir ticket");
        ReimpresionTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReimpresionTicketActionPerformed(evt);
            }
        });

        jButton5.setText("Cancelar venta");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel80)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(consecutivoReimpresionTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ReimpresionTicket))
                    .addComponent(jLabel82)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 701, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(consecutivoReimpresionTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ReimpresionTicket))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel82)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(labelAgergarCliente)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(preAgregarCliente))
                        .addComponent(panelAgregarClienteVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2)
                        .addComponent(jScrollPane1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelAgergarCliente)
                    .addComponent(preAgregarCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAgregarClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelBusquedaClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 186, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBusquedaClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane3.setViewportView(jPanel1);

        jTabbedPane1.addTab("Ventas", jScrollPane3);
        jScrollPane3.getAccessibleContext().setAccessibleParent(jTabbedPane1);

        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/logoSmall.png"))); // NOI18N
        jLabel72.setMinimumSize(new java.awt.Dimension(200, 200));
        jLabel72.setPreferredSize(new java.awt.Dimension(200, 200));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 807, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void clienteBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clienteBusquedaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clienteBusquedaActionPerformed

    private void botonBusquedaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBusquedaClienteActionPerformed
        
      DBConect conexion=new DBConect();  
        
      try{
        
        resultadoClientes= new DefaultTableModel();
        
        Connection conexionMysql = conexion.GetConnection();
        
        Statement statement = conexionMysql.createStatement();
        
        String clienteVar=clienteBusqueda.getText();
        String nombreVar=nombreBusqueda.getText();
        String apellidoPVar=apellidoPBusqueda.getText();
        String apellidoMVar=apellidoMBusqueda.getText();
        String direccionVar=direccionBusqueda.getText();
        
        String varBusqueda=clienteVar+nombreVar+apellidoMVar+apellidoPVar+direccionVar;
        
        if(varBusqueda.equals("")){
            JOptionPane.showMessageDialog(null,"Se necesita al menos un campo para realizar la busqueda"); 
            return;
        }
        
        String sqlString="Select idcliente as 'Cliente',concat(nombre,' ',apellidop,' ',apellidom) as 'Nombre completo',dirección as 'Dirección', " +
                         " telefono as 'Teléfono',activo" +
                         " from cliente where ";
        
        int indicadorAnd=0;
        
        if(!clienteVar.equals("")){
           sqlString+=" idcliente="+clienteVar;
           indicadorAnd++;
           
        }
        
        if(!nombreVar.equals("")){
            if(indicadorAnd>0){
                sqlString+=" and ";
            }
            
            sqlString+=" nombre like '"+nombreVar+"%'";
            indicadorAnd++;
        }
        
        if(!apellidoPVar.equals("")){
            if(indicadorAnd>0){
                sqlString+=" and ";
            } 
            sqlString+=" apellidoP like '"+apellidoPVar+"%'";
             indicadorAnd++;
        }
        
        if(!apellidoMVar.equals("")){
                    
            if(indicadorAnd>0){
                sqlString+=" and ";
            }
            sqlString+=" apellidoM like '"+apellidoMVar+"%'";
            indicadorAnd++;
        }
        
        if(!direccionVar.equals("")){
            if(indicadorAnd>0){
                sqlString+=" and ";
            }        
            sqlString+= " dirección like '%"+direccionVar+"%'";
              indicadorAnd++;
        }
        
        ResultSet rs = statement.executeQuery(sqlString);
        
        ResultSetMetaData rsMd = rs.getMetaData();
        //La cantidad de columnas que tiene la consulta
        int cantidadColumnas = rsMd.getColumnCount();
        //Establecer como cabezeras el nombre de las colimnas
        for (int i = 1; i <= cantidadColumnas; i++) {
         resultadoClientes.addColumn(rsMd.getColumnLabel(i));
        }
        //Creando las filas para el JTable
        int usuariosInactivos=0;
        int totalUsuariosEncontrados=0;
        while (rs.next()) {
            totalUsuariosEncontrados++;
            int activo=rs.getInt("activo");
           
            if(activo==0){
//                  JOptionPane.showMessageDialog(null, "El cliente YA NO ESTA ACTIVO en el sistema.\n\nRegistra nuevamente al cliente o utiliza el usuario sin registro");
//                  clienteBusqueda.setText("");
//                  nombreBusqueda.setText("");
//                  apellidoPBusqueda.setText("");
//                  apellidoMBusqueda.setText("");
//                  direccionBusqueda.setText("");
//
//                  return;
                usuariosInactivos++;
            }else{
               Object[] fila = new Object[cantidadColumnas];
               for (int i = 0; i < cantidadColumnas; i++) {
                 fila[i]=rs.getObject(i+1);
               }
               resultadoClientes.addRow(fila);
           }   
           
        }
        
        if(usuariosInactivos>0){
            JOptionPane.showMessageDialog(null,"Se encontraron usuarios inactivos en el sistema.\nNo fuerón agregados a la lista.");
            resultadoClientes= new DefaultTableModel();
            tablaClientesEncontrados.setModel(resultadoClientes);
            agregarUsuarioSinRegistro();
            return;
        }
        
        if(totalUsuariosEncontrados==0){
            JOptionPane.showMessageDialog(null,"No se encontro un cliente con los datos proporcionados.");
            resultadoClientes= new DefaultTableModel();
            tablaClientesEncontrados.setModel(resultadoClientes);
            agregarUsuarioSinRegistro();
            return;
        }
        
        tablaClientesEncontrados.setModel(resultadoClientes);
        
        tablaClientesEncontrados.getColumn("activo").setWidth(0);
        tablaClientesEncontrados.getColumn("activo").setMinWidth(0);
        tablaClientesEncontrados.getColumn("activo").setMaxWidth(0);
        
      }catch(Exception e){
         e.printStackTrace();
      }       
         
    }//GEN-LAST:event_botonBusquedaClienteActionPerformed

    private void newClienteVentaTelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newClienteVentaTelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newClienteVentaTelActionPerformed

    private void agregarClienteAGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarClienteAGridActionPerformed
  
        DefaultTableModel model = (DefaultTableModel) tablaClientesEncontrados.getModel();
        
        resultadoClientes= new DefaultTableModel();
        tablaClientesEncontrados.setModel(resultadoClientes);
        
        //tablaClientesEncontrados.setModel(new DefaultTableModel());
 
        int count=model.getRowCount();
        
        for(int i=0;i<count;i++){
            model.removeRow(i);
        }
        
        int idCliente=agregarCliente(newNombreVenta.getText(),newApellidoPVenta.getText(),newApellidoMVenta.getText(),newDireccionVenta.getText(),newClienteVentaTel.getText());
        
        if(idCliente==0){
           tablaClientesEncontrados.setModel(model);
        }else{
        
            Vector row = new Vector();
            row.add(idCliente);
            row.add(newNombreVenta.getText()+" "+newApellidoPVenta.getText()+" "+newApellidoMVenta.getText());
            row.add(newDireccionVenta.getText());
            row.add(newClienteVentaTel.getText());
            model.addRow(row);
            tablaClientesEncontrados.setModel(model);
            JOptionPane.showMessageDialog(null, "Se agrego el cliente No.: "+idCliente);


            limpiaNuevoClienteDesdeVenta();
        }
    }//GEN-LAST:event_agregarClienteAGridActionPerformed

public int agregarCliente(String nombre,String apellidoP,String apellidoM,String dir,String tel){
     int idCliente=0;
     DBConect conexion=new DBConect();
        
        try{
            Connection conexionMysql = conexion.GetConnection();

            Statement statement = conexionMysql.createStatement();

            String varNombre=nombre;
            String varPaterno=apellidoP;
            String varMaterno=apellidoM;
            String varDireccion=dir;
            
            String validadorVacios=varNombre+varPaterno+varMaterno+varDireccion;
            
            if(validadorVacios.equals("")){
                
                JOptionPane.showMessageDialog(null,"No has proporcionado ningun dato");
                
                
            }
            
            String mensajeError="";
            
            
            if(varNombre.equals("")){
                
                mensajeError+="Nombre \n";
                
            }
            
            if(varPaterno.equals("")){
                
                mensajeError+="Apellido paterno \n";
                
            }
            
             if(varDireccion.equals("")){
                
                mensajeError+="Dirección \n";
                
            }

            
            if(!mensajeError.equals("")){
            
                mensajeError="Los siguientes campos son necesarios para continuar: \n\n "+mensajeError;
                
                JOptionPane.showMessageDialog(null, mensajeError);
                return 0; 
            }
              
            
            Date fecha =new Date();
              
            SimpleDateFormat formatoFecha=new SimpleDateFormat("yyyy/MM/dd");
            
            String fechaActual=formatoFecha.format(fecha);
            
            String sqlString="INSERT INTO `cliente` (`nombre`, `apellidop`, `apellidom`,`dirección`,`fechaDeRegistro`,`telefono`) "
                    + " VALUES ('"+varNombre+"', '"+varPaterno+"', '"+varMaterno+"', '"+varDireccion+"','"+fechaActual+"','"+tel+"' )";
        
            int resultado=statement.executeUpdate(sqlString);
       
            if(resultado>0){

                //JOptionPane.showMessageDialog(null, "El cliente se agrego correctamente");
                
            }
            
            
            String obtenerUltimaInsercion="Select MAX(idCliente) from cliente";
            ResultSet rs2 = statement.executeQuery(obtenerUltimaInsercion );
            
            while(rs2.next()){
                idCliente=rs2.getInt(1);
            }
            
           
         
       }catch(Exception e){
           e.printStackTrace();
       }   
        
       return idCliente; 

}    
    
    private void confirmarVentaBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmarVentaBotonActionPerformed
    
         JTable jTableHelper = new JTable() {
            private static final long serialVersionUID = 1;
            public boolean isCellEditable(int row, int column) {                
                return false;               
            };
        };
        
        jTableHelper.setModel(tablaDetalleVenta.getModel());
        
        tablaDetalleVenta=jTableHelper;
        
        DefaultTableModel model = (DefaultTableModel) tablaDetalleVenta.getModel();
        ////obtenemos el consecutivo de venta del día
        DBConect conexion=new DBConect();  
        int consecutivoVenta=1;
        try{

          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();
          
          Date fechaInicioDia=new Date();
          fechaInicioDia.setHours(01);
          fechaInicioDia.setMinutes(00);
          fechaInicioDia.setSeconds(00);
          Date fechaFinDia=new Date();
          fechaFinDia.setHours(23);
          fechaFinDia.setMinutes(59);
          fechaFinDia.setSeconds(59);
          
          SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String fi=sf.format(fechaInicioDia);
          String ff=sf.format(fechaFinDia);
                  
          
          String sqlString="select max(consecutivoVenta) from Venta where (fechaVenta BETWEEN '"+fi+"' AND '"+ff+"')";
          System.out.println(sqlString);
          ResultSet rs=statement.executeQuery(sqlString);
          
          while(rs.next()){
              //consecutivoVenta=rs.getInt(1);
              //if(consecutivoVenta>1){
                  consecutivoVenta=rs.getInt(1)+1;
              //}
              
          }
          
        }catch(Exception e){
            e.printStackTrace();
        }
        
        contador++;
        
        int noSeleccionados=//seleccionGlobalCliente;
        
        tablaClientesEncontrados.getSelectedRowCount();

//        if (tablaClientesEncontrados.getCellSelectionEnabled()) {
//            //tablaClientesEncontrados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//            int rowIndex = tablaClientesEncontrados.getSelectedRow();
//            int colIndex = tablaClientesEncontrados.getSelectedColumn();
//            System.out.print(rowIndex+","+colIndex);
//
//        }
       
        if(noSeleccionados<=0){
            JOptionPane.showMessageDialog(null,"Es necesario seleccionar un cliente para continuar");
            return;
        }
        
        
        String cliente=getClienteId();
        
        if(cliente.equals("")){
            JOptionPane.showMessageDialog(null,"Es necesario seleccionar un cliente para continuar");
            return;
        }
        ////realizar insert en venta
                
       // DBConect conexion=new DBConect();
        
        try{
            Connection conexionMysql = conexion.GetConnection();

            Statement statement = conexionMysql.createStatement();
            Statement statement2 = conexionMysql.createStatement();
            
            ///insertamos la cabecera de la venta
            int cantidadProductos=model.getRowCount();
            
            if(cantidadProductos<=0){
                
               JOptionPane.showMessageDialog(null, "No has agregado ningun producto");
               return;
                
            }else{

               String precioSinSigno=etiquetaGranTotal.getText().substring(1,etiquetaGranTotal.getText().length());
               
               String efectivo=efectivoRecibido.getText();
               Double efectivoRecibido1=0.0;
               Double precioSinSigno1=0.0;
               
               
               if(efectivo.equals("")){
                   JOptionPane.showMessageDialog(null, "Se necesita saber cuanto efectivo recibiste");
                   return;
               }else{
                   try{
                    precioSinSigno1=Double.parseDouble(precioSinSigno);
                    efectivoRecibido1=Double.parseDouble(efectivo);
                    
                   }catch(Exception e){
                       JOptionPane.showMessageDialog(null, "No introdujiste un numero");
                       return;
                   }
                    if(efectivoRecibido1<precioSinSigno1){
                        JOptionPane.showMessageDialog(null, "La cantidad recibida no puede ser menor al costo");
                        return;
                    }
               }
               
                String sqlString="INSERT INTO `venta` (`total`,`cliente_idcliente`,`usuarios_idusuario`,`consecutivoVenta`,`efectivoRecib`,`cambio`) "
                        + " VALUES ('"+precioSinSigno+"','"+getClienteId()+"', '"+this.usuarioLogueado+"',"+consecutivoVenta+","+efectivoRecibido1+","+(efectivoRecibido1-precioSinSigno1)+" )";

                int resultado=statement.executeUpdate(sqlString);
                
                Utils u=new Utils();
                
                String sqlMaxID="select max(consecutivoVenta) as max from Venta where "+u.obtenerBetweenParaConsulta("fechaVenta");
                
                ResultSet rs=statement.executeQuery(sqlMaxID);
                int ultimaVentaRealizada=1;
                while(rs.next()){
                    ultimaVentaRealizada=rs.getInt(1);
                }
                
                //if(ultimaVentaRealizada<1){
                //    ultimaVentaRealizada=1;
                //}
                
                
                ////obtenemos el max id de la ultima venta insertada
                String sqlMaxIDM="select max(idVenta) as max from Venta where "+u.obtenerBetweenParaConsulta("fechaVenta");
                
                ResultSet rsM=statement.executeQuery(sqlMaxIDM);
                int ultimaVentaRealizadaM=1;
                while(rsM.next()){
                    ultimaVentaRealizadaM=rsM.getInt(1);
                }
                
                if(ultimaVentaRealizadaM<1){
                    ultimaVentaRealizadaM=1;
                }
                
                
                ////Obtenemos el tamaño para el ticket
                //de la pizza
                String tamañoTicket="";
                if(resultado>0){
                
                    int errores=0;
                    for(int i=0;i<model.getRowCount();i++){
                    //insertamos el detalle de la venta
                        String consecutivo,cantidad,descripcion,precio,id;
                    
                            consecutivo=""+model.getValueAt(i,0);
                            cantidad=""+model.getValueAt(i,1);
                            descripcion=""+model.getValueAt(i,2);
                            precio=""+model.getValueAt(i,3);
                            id=""+model.getValueAt(i,5);
                            tamañoTicket=""+model.getValueAt(i,6);
                            
                            String sqlString2="INSERT INTO `detalleVenta` (`consecutivoVenta`,`cantidad`,`precioTotal`,`descripcionProd`,`Productos_idProductos`,`Venta_idVenta`,`tamanio`) "
                            + " VALUES ('"+consecutivo+"','"+cantidad+"', '"+precio+"','"+descripcion+"','"+id+"','"+ultimaVentaRealizadaM+"','"+tamañoTicket+"' )";
                        
                            int resultado2=statement2.executeUpdate(sqlString2);
                            
                            if(resultado2>0){
                                
                                String vendidos="select cantidadVendidos,unidadesEnCaja from productos where idProductos="+id;
                
                                ResultSet rsP=statement.executeQuery(vendidos);
                                Double cantidadVend=0.0;
                                Double cantidadAlm=0.0;
                                while(rsP.next()){
                                    cantidadVend=rsP.getDouble(1);
                                    cantidadAlm=rsP.getDouble(2);
                                }
                                
                                marcarProductoComoUtilizadoEnVenta(id,""+cantidadAlm,""+cantidadVend,cantidad);
                                if(indicadorRefrescoUtilizadoEnPaquete==1){                                
                                    marcarRefrescoUtilizadoEnPaquete();
                                }
                                
                                
                            }else{
                                errores++;
                            }
                    }
                    
                    if(errores==0){
                        
                        String itemsVenta=obtenerProductosParaTicket(tablaDetalleVenta);
                        
                        JOptionPane.showMessageDialog(null,"Se genero correctamente la venta No. "+ultimaVentaRealizada);
                        DefaultTableModel modelLimpio=(DefaultTableModel)tablaDetalleVenta.getModel();
                        modelLimpio.setNumRows(0);
                        
                        String udFueAtendidoPor="";
                        
                        ///obtenemos el nombre completo del usuario para insertarlo al ticket
                        try{

                            statement = conexionMysql.createStatement();

                            sqlString="select NombreCompleto from usuarios where idusuario="+usuarioLogueado;

                            ResultSet rs2=statement.executeQuery(sqlString);

                            while(rs2.next()){
                                udFueAtendidoPor=rs2.getString(1);
                            }

                          }catch(Exception e){
                              e.printStackTrace();
                          }

                        int fila=tablaClientesEncontrados.getSelectedRow();
                        String clienteTicket=""+tablaClientesEncontrados.getValueAt(fila, 1);
                        
                        String direccionCliente=""+tablaClientesEncontrados.getValueAt(fila, 2);
                        String telefonoCliente="";
                        String preTelefono=""+tablaClientesEncontrados.getValueAt(fila, 3);
                        
                        if(!preTelefono.equals("")){
                            telefonoCliente="\nTelefono del cliente:"+tablaClientesEncontrados.getValueAt(fila, 3);
                            
                            direccionCliente+=telefonoCliente;
                        }
                        
                        
                        if(clienteTicket.equals("Usuario sin registro")){
                            clienteTicket="Cliente en local";
                            direccionCliente="--------------------";
                            
                            String seleccion=seleccionPizza1.getSelectedItem().toString();
                            boolean tipoProdAgregado=tiposDeProductoAgregados.contains(5);
                            
                            if(!seleccion.equals("---") || tipoProdAgregado==true){
                                do{
                                    clienteTicket=JOptionPane.showInputDialog(null,"¿Cual es el nombre del cliente?");
                                }while(clienteTicket.equals(""));
                            }
                            
                        }
                        ///damos el formato deseado a la hora y fecha
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                        Date now = new Date();
                        String fecha=sdf.format(now);
                                     
                        Double ivaDouble=Double.parseDouble(ivaTotal.getText().replace("$", ""));
                        Double totalDouble=Double.parseDouble(etiquetaGranTotal.getText().replace("$", ""));
                        
                        Double subTotal=totalDouble-ivaDouble;
                        //boolean tipoProdVendido=tiposDeProductoAgregados.contains(0);
                        //if(tiposDeProductoAgregados.size()==1 && tipoProdVendido==true){
                           ///No imprimimos ticket, solo abrimos la caja 
                        //   Ticket t=new Ticket();
                        //   t.AbrirCaja();
                           //JOptionPane.showMessageDialog(null,"Abriendo caja por venta de rebanada");
                        //}else{
                            ///imprimimos el ticket normalmente
                            
                        //    boolean tipoProd4=tiposDeProductoAgregados.contains(4);
                            
                            //obtenemos el refresco seleccionado en la interfaz
//                            String refresco=jComboBox2.getSelectedItem().toString();
//                            if(tipoProd4==true){
//                                refresco="";
//                            }else{                        
//                                refresco="Refresco:"+refresco;
//                            }
                            
                            Ticket t=new Ticket(valorSucursal.getText(),valorDireccion.getText(),ultimaVentaRealizada+"",udFueAtendidoPor,fecha,itemsVenta,subTotal+"",ivaTotal.getText(),etiquetaGranTotal.getText(),efectivoRecibido.getText(),cambioTotal.getText(),clienteTicket,direccionCliente,"");
                            imprimirLogo();
                            t.Imprimir();
                            TestCut testCut= new TestCut();
                            testCut.clean();
                            testCut.cortar();
                            //JOptionPane.showConfirmDialog(null,"Se imprimira el segundo ticket","",JOptionPane.OK_OPTION);
                            //imprimirLogo();
                            //t.Imprimir();
                        //}
                        
                        resultadoClientes.setColumnCount(0);
                        resultadoClientes.setRowCount(0);
                        
                      
        
                        resultadoClientes= new DefaultTableModel();
                        tablaClientesEncontrados.setModel(resultadoClientes);
                        etiquetaGranTotal.setText("---");
                        efectivoRecibido.setText("");
                        cambioTotal.setText("$");
                        ivaTotal.setText("---");
                        seleccionGlobalCliente=0;
                        
                        agregarUsuarioSinRegistro();
                        
                        seleccionPizza1.setSelectedIndex(0);
                        seleccionPizza2.setSelectedIndex(0);
                        seleccionPizza3.setSelectedIndex(0);
                        seleccionExtras1.setSelectedIndex(0);
                        seleccionExtras2.setSelectedIndex(0);
                        
                        tiposDeProductoAgregados.clear();
                    }
                    
                }
           
            }
       }catch(Exception e){
           e.printStackTrace();
       } 
        
         codigoBusqueda.setFocusable(true);
         codigoBusqueda.grabFocus();  
         codigoBusqueda.requestFocus(true);
    }//GEN-LAST:event_confirmarVentaBotonActionPerformed

    public void imprimirLogo(){
    
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob job = service.createPrintJob();
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        SimpleDoc doc = new SimpleDoc(new MyPrintable(), flavor, null);

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(OrientationRequested.PORTRAIT);
        aset.add(MediaSizeName.INVOICE);
        try {
            job.print(doc,aset);
        } catch (PrintException ex) {
            Logger.getLogger(Venta.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public String obtenerProductosParaTicket(JTable tabla){
        
        String productos = "";
        
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        int filas = model.getRowCount();

        int espacio1 = 3;
        int espacio2 = 29;
        int espacio3 = 3;

        int resta1 = 0;
        int resta2 = 0;
        String espacios1 = "";
        String espacios2 = "";

        String can = "";
        String pro = "";
        String pre = "";
        String tamanio="";
        
        for (int i = 0; i < filas; i++) {
             
            ///aqui obtenemos la cantidad del producto
            if (tabla.getValueAt(i, 1).toString().length() < espacio1) {
                 can = tabla.getValueAt(i, 1).toString();
                 resta1 = espacio1 - tabla.getValueAt(i, 1).toString().length();
             } else {
                can = tabla.getValueAt(i, 1).toString().substring(0, espacio1 - 1);

             }
             
            ///aqui obtenemos la descripción del producto
             if (tabla.getValueAt(i, 2).toString().length() < espacio2) {
             
                 resta2 = espacio2 - tabla.getValueAt(i, 2).toString().length();
                 pro = tabla.getValueAt(i, 2).toString();
                 
                boolean contains = pro.contains(",");
                 
               if(contains==false){
                   pro=pro+", ";
               } 
                 
                String[] split = pro.split(",");
                
                String compar="";
                if(split.length==2){ 
                    compar=split[1];
                    compar=compar.trim();
                }

                 if(compar.equals("Extras:---")){
                     pro=pro.replace("Extras:---", "");
                 }else{
                     if(compar.equals("Extras:Queso extra")){
                         pro=pro.replace(compar, " Ext Q");
                     }
                     if(compar.equals("Extras:Extra general")){
                         pro=pro.replace(compar, " Ext G");
                     }
                 }
                 
             } else {
             
                 pro = tabla.getValueAt(i, 2).toString();//.substring(0, espacio2 - 1);
                 /////sistituimos la cadena de extras
                 String[] split = pro.split(",");
                
                 String compar="";
                 if(split.length==2){ 
                     compar=split[1];
                     compar=compar.trim();
                 }

                 if(compar.equals("Extras:---")){
                      pro=pro.replace("Extras:---", "");
                 }else{
                     if(compar.equals("Extras:Queso extra")){
                         pro=pro.replace(compar, " Ext Q");
                     }
                     if(compar.equals("Extras:Extra general")){
                         pro=pro.replace(compar, " Ext G");
                     }
                 }
                 
                 pro += " ";
                 
             }
             
             if (tabla.getValueAt(i, 4).toString().length() < espacio3) {
             
                 pre = tabla.getValueAt(i, 4).toString();
                 
             } else {
             
                 pre = tabla.getValueAt(i, 4).toString();//.substring(0, espacio3 - 1);
                 
             }
             
             for (int j = 0; j < resta1; j++) {
             
                 espacios1 += " ";

             }
             
             for (int j = 0; j < resta2; j++) {
             
                 espacios2 += " ";
                 
             }
             
             
             if (tabla.getValueAt(i, 6).toString().length() < espacio3) {
             
                 tamanio = tabla.getValueAt(i, 6).toString();
                 
             } 
             
             Double precioDecimal=Double.parseDouble(pre);
             
             productos += can + espacios1 + pro + espacios2 +""+tamanio+"    $"+ precioDecimal.toString() + "\n";
             espacios1 = "";
             espacios2 = "";
             
        }
        
        return productos;
    }
    
    private void agregarClienteAGrid1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarClienteAGrid1ActionPerformed
        DBConect conexion=new DBConect();
        
        try{
            Connection conexionMysql = conexion.GetConnection();

            Statement statement = conexionMysql.createStatement();

            String varNombre=newNombreVenta1.getText();
            String varPaterno=newApellidoPVenta1.getText();
            String varMaterno=newApellidoMVenta1.getText();
            String varDireccion=newDireccionVenta1.getText();
            
            String validadorVacios=varNombre+varPaterno+varMaterno+varDireccion;
            
            if(validadorVacios.equals("")){
                
                JOptionPane.showMessageDialog(null,"No has proporcionado ningun dato");
                
                
            }
            
            String mensajeError="";
            
            
            if(varNombre.equals("")){
                
                mensajeError+="Nombre \n";
                
            }
            
            if(varPaterno.equals("")){
                
                mensajeError+="Apellido paterno \n";
                
            }
            
             if(varDireccion.equals("")){
                
                mensajeError+="Dirección \n";
                
            }

            
            if(!mensajeError.equals("")){
            
                mensajeError="Los siguientes campos son necesarios para continuar: \n\n "+mensajeError;
                
                JOptionPane.showMessageDialog(null, mensajeError);
                return; 
            }
              
            
            Date fecha =new Date();
              
            SimpleDateFormat formatoFecha=new SimpleDateFormat("yyyy/MM/dd");
            
            String fechaActual=formatoFecha.format(fecha);
            
            String sqlString="INSERT INTO `cliente` (`nombre`, `apellidop`, `apellidom`,`dirección`,`fechaDeRegistro`,`telefono`) "
                    + " VALUES ('"+varNombre+"', '"+varPaterno+"', '"+varMaterno+"', '"+varDireccion+"','"+fechaActual+"','"+newTel2.getText()+"' )";
        
            int resultado=statement.executeUpdate(sqlString);
       
            if(resultado>0){
                int idCliente=0;
                String obtenerUltimaInsercion="Select MAX(idCliente) from cliente";
                ResultSet rs2 = statement.executeQuery(obtenerUltimaInsercion );
            
                while(rs2.next()){
                    idCliente=rs2.getInt(1);
                }
                JOptionPane.showMessageDialog(null, "El cliente No.: "+idCliente+", se agrego correctamente");
                limpiaNuevoCliente();
            }
            
       }catch(Exception e){
           e.printStackTrace();
       }     
    }//GEN-LAST:event_agregarClienteAGrid1ActionPerformed

    private void codigoBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigoBusquedaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoBusquedaActionPerformed

    private void busquedaManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busquedaManualActionPerformed
        
        DBConect conexion=new DBConect();
        try{

          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();

          String codigoVar="";//codigoManual.getText();
          String descripcionVar=descripcionManual.getText();
          
          String filter=codigoVar+descripcionVar;
        
          //if(filter.equals("")){
          //  JOptionPane.showMessageDialog(null, "Se necesita al menos un dato para realizar la busqueda de productos");
          //  return;
         // }
          
          String sqlString="Select idProductos as 'Id',codigo,descripcion as 'Descripción',precioUnitarioC as 'Precio compra', precioUnitarioV as 'Precio venta',unidadesEnCaja as 'Cantidad'"
                  + ",uMedida as 'Unidad medida', presentacion as 'Presentación' from  productos " +
                           " where estatus=0 and codigo='"+codigoVar+"'";
                           
          if(!descripcionVar.equals("")){
               sqlString+= " And descripcion like '%"+descripcionVar+"%'";
          }
          
          if(codigoVar.equals("") && !descripcionVar.equals("")){
              
              sqlString="SELECT idProductos as 'Id',descripcion as 'Descripción',precioUnitarioV as 'Precio venta',unidadesEnCaja as 'Cantidad' "
                      +",uMedida as 'Unidad medida', presentacion as 'Presentación'"
                      + " FROM productos where estatus=0 and descripcion like '%"+descripcionVar+"%'";
              
          }
          
            ResultSet rs = statement.executeQuery(sqlString); 
            int contador=0;  
            
            while (rs.next()) {
             contador++;
            }
            rs.beforeFirst();
            if(contador>=1){
                
               
                DefaultTableModel productosEncontrados= new DefaultTableModel();
                
                ResultSetMetaData rsMd = rs.getMetaData();
                //La cantidad de columnas que tiene la consulta
                int cantidadColumnas = rsMd.getColumnCount();
                //Establecer como cabezeras el nombre de las colimnas
                for (int i = 1; i <= cantidadColumnas; i++) {
                 productosEncontrados.addColumn(rsMd.getColumnLabel(i));
                }
                //Creando las filas para el JTable
                while (rs.next()) {
                 Object[] fila = new Object[cantidadColumnas];
                 for (int i = 0; i < cantidadColumnas; i++) {
                   fila[i]=rs.getObject(i+1);
                 }
                 productosEncontrados.addRow(fila);
                }
                
                
                BuscadorProductos buscador=new BuscadorProductos(productosEncontrados,this);
                buscador.setTitle("Buscador de productos");
                buscador.setVisible(true);
                buscador.toFront();
                
            }else{
                JOptionPane.showMessageDialog(null,"No se encontraron resultados en búsqueda manual");
                return;
            }
            
        }catch(Exception e){
            e.printStackTrace();
        } 
    }//GEN-LAST:event_busquedaManualActionPerformed

    private void uMedidaListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uMedidaListaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_uMedidaListaActionPerformed

    
    public void ocultarCamposEnAltaPizza(){
     ///aqui ocultamos los campos de el alta general.
     jLabel15.setVisible(false);
     precioCompraAltaText.setVisible(false);
     jLabel16.setVisible(false);
     precioVentaAltaText.setVisible(false);
     jLabel6.setVisible(false);
     unidadesEnCajaText.setVisible(false);
     jLabel19.setVisible(false);
     uMedidaLista.setVisible(false);
     jLabel20.setVisible(false);
     presentacionLista.setVisible(false);
     
     ///mostramos los campos que deseamos ver
     jLabel43.setVisible(true);
     pChica.setVisible(true);
     jLabel68.setVisible(true);
     pMediana.setVisible(true);
     jLabel75.setVisible(true);
     pGrande.setVisible(true);
     jLabel78.setVisible(true);
     pFamiliar.setVisible(true);
     
     panelAltaGeneral.setVisible(false);
     panelAltaPizza.setVisible(true);  
     
    }
    public void ocultarCamposEnAltaGeneral(){
        
     panelAltaGeneral.setVisible(true);
     panelAltaPizza.setVisible(false);   
     //// ocultamos los campos para dar de alta la pizza
     jLabel43.setVisible(false);
     pChica.setVisible(false);
     jLabel68.setVisible(false);
     pMediana.setVisible(false);
     jLabel75.setVisible(false);
     pGrande.setVisible(false);
     jLabel78.setVisible(false);
     pFamiliar.setVisible(false);
     
     /////mostramos los campos que deseamos ver
     jLabel15.setVisible(true);
     precioCompraAltaText.setVisible(true);
     jLabel16.setVisible(true);
     precioVentaAltaText.setVisible(true);
     jLabel6.setVisible(true);
     unidadesEnCajaText.setVisible(true);
     jLabel19.setVisible(true);
     uMedidaLista.setVisible(true);
     jLabel20.setVisible(true);
     presentacionLista.setVisible(true);
     
     
    }
    
    public void ocultarCamposAltaAlInicio(){
     
     panelAltaGeneral.setVisible(false);
     panelAltaPizza.setVisible(false);
        
     jLabel43.setVisible(false);
     pChica.setVisible(false);
     jLabel68.setVisible(false);
     pMediana.setVisible(false);
     jLabel75.setVisible(false);
     pGrande.setVisible(false);
     jLabel78.setVisible(false);
     pFamiliar.setVisible(false);
     
     jLabel15.setVisible(false);
     precioCompraAltaText.setVisible(false);
     jLabel16.setVisible(false);
     precioVentaAltaText.setVisible(false);
     jLabel6.setVisible(false);
     unidadesEnCajaText.setVisible(false);
     jLabel19.setVisible(false);
     uMedidaLista.setVisible(false);
     jLabel20.setVisible(false);
     presentacionLista.setVisible(false);
    
    }
    
    private void agregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarProductoActionPerformed
       
        DBConect conexion=new DBConect();
        String tipoProdSel=comboTipoProducto.getSelectedItem().toString();
        try{
            Connection conexionMysql = conexion.GetConnection();

            Statement statement = conexionMysql.createStatement();

            String varDescripcion=descripcionAltaText.getText();
            String varUnidades=unidadesEnCajaText.getText();
            String varPrecioC=precioCompraAltaText.getText();
            String varPrecioV=precioVentaAltaText.getText();
            String varUMedida=uMedidaLista.getSelectedItem().toString();
            String varPresentacion=presentacionLista.getSelectedItem().toString();
            String varCodigo=codigoAltaText.getText();
            
            String precioChica=pChica.getText();
            String precioMediana=pMediana.getText();
            String precioGrande=pGrande.getText();
            String precioFamiliar=pFamiliar.getText();
            
            String tipoProd="";
            //0 general
            //1 pizza
            // tacos
            ////en el combo... Pizza, Tacos, General
            
            if(tipoProdSel.equals("---")){
                tipoProd="0";
            }
            if(tipoProdSel.equals("Pizza")){
                tipoProd="1";
                varPrecioC="0";
                varPrecioV="0";
                
            }
            if(tipoProdSel.equals("Tacos")){
                tipoProd="2";
            }
            if(tipoProdSel.equals("General")){
                tipoProd="0";
                precioChica="0";
                precioMediana="0";
                precioGrande="0";
                precioFamiliar="0";
            }
            
            
            String validadorVacios=varDescripcion+varUnidades+varPrecioC+varPrecioV+varUMedida+varPresentacion+varCodigo;
            
            if(validadorVacios.equals("")){
                
                JOptionPane.showMessageDialog(null,"No has proporcionado ningun dato");
                
            }
            
            String mensajeError="";
            
            
//            if(varCodigo.equals("")){
//                
//                mensajeError+="Código \n";
//                
//            }
            
            if(varDescripcion.equals("")){
                
                mensajeError+="Descripción \n";
                
            }
            String seleccion=comboTipoProducto.getSelectedItem().toString();
            
            if(seleccion.equals("Pizza")){
                varUnidades="1";
                
            }
            
             if(varUnidades.equals("")){
                
                mensajeError+="Cantidad \n";
                
            }
             if(varPrecioC.equals("")){
                
                mensajeError+="Precio de compra \n";
                
            }
              if(varPrecioV.equals("")){
                
                mensajeError+="Precio de venta \n";
                
            }
            
            if(!mensajeError.equals("")){
            
                mensajeError="Los siguientes campos son necesarios para continuar: \n\n "+mensajeError;
                
                JOptionPane.showMessageDialog(null, mensajeError);
                return; 
            }
             
            String sqlString="INSERT INTO `productos` (`descripcion`, `unidadesEnCaja`, `precioUnitarioC`, `uMedida`, `presentacion`, `cantidadFraccion`, `codigo`,`precioUnitarioV`,`TipoProducto`,precioChica,precioMediana,precioGrande,precioFamiliar) "
                    + " VALUES ('"+varDescripcion+"', '"+varUnidades+"', '"+varPrecioC+"', '"+varUMedida+"', '"+varPresentacion+"', '0', '"+varCodigo+"', '"+varPrecioV+"',"+tipoProd+","+precioChica+","+precioMediana+","+precioGrande+","+precioFamiliar+")";
        
            int resultado=statement.executeUpdate(sqlString);
       
            if(resultado>0){

                JOptionPane.showMessageDialog(null, "El producto se agrego correctamente");
                if(tipoProd.equals("1")){
                    refrescarComboAlAgregarProducto();
                }
                
                limpiaNuevoProducto();
            }
            
       }catch(Exception e){
           e.printStackTrace();
       }
         
    }//GEN-LAST:event_agregarProductoActionPerformed

    private void BusquedaProductosTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BusquedaProductosTodoActionPerformed
        
        DBConect conexion=new DBConect();
        productosEncontradosAModificar= new DefaultTableModel();
        listaProductosAModificar.setModel(productosEncontradosAModificar);
        
//        String code=codigoProdFiltro.getText();
        String desc=descripcionProdFiltro.getText();
        
        String filter=desc;
        
//        if(filter.equals("")){
//            JOptionPane.showMessageDialog(null, "Se necesita al menos un dato para realizar la busqueda de productos");
//            return;
//        }
        
        try{

          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();

          //String codigoVar=codigoProdFiltro.getText();
          String descripcionVar=descripcionProdFiltro.getText();
          
          String sqlString="Select idProductos as 'Id',codigo,descripcion as 'Descripción',precioUnitarioC as 'Precio compra', precioUnitarioV as 'Precio venta',unidadesEnCaja as 'Cantidad'"
                  + ",uMedida as 'Unidad medida', presentacion as 'Presentación' from  productos " +
                           " where estatus=0 ";
                           
          if(!descripcionVar.equals("")){
               sqlString+= " And descripcion like '%"+descripcionVar+"%'";
          }
          
//          if(codigoVar.equals("") && !descripcionVar.equals("")){
//              
//              sqlString="SELECT idProductos as 'Id',codigo,descripcion as 'Descripción',precioUnitarioC as 'Precio compra', precioUnitarioV as 'Precio venta',unidadesEnCaja as 'Cantidad' "
//                      +",uMedida as 'Unidad medida', presentacion as 'Presentación'"
//                      + " FROM productos where estatus=0 and descripcion like '%"+descripcionVar+"%'";
//              
//          }
          
            ResultSet rs = statement.executeQuery(sqlString); 
            int contador=0;  
     
            ResultSetMetaData rsMd = rs.getMetaData();
            //La cantidad de columnas que tiene la consulta
            int cantidadColumnas = rsMd.getColumnCount();
            //Establecer como cabezeras el nombre de las colimnas
            for (int i = 1; i <= cantidadColumnas; i++) {
             productosEncontradosAModificar.addColumn(rsMd.getColumnLabel(i));
            }
            //Creando las filas para el JTable
            while (rs.next()) {
             contador++;
             Object[] fila = new Object[cantidadColumnas];
             for (int i = 0; i < cantidadColumnas; i++) {
               fila[i]=rs.getObject(i+1);
             }
             productosEncontradosAModificar.addRow(fila);
            }
          
            if(contador==0){
                JOptionPane.showMessageDialog(null, "No se encontro ningun producto con los datos proporcionados.");
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_BusquedaProductosTodoActionPerformed

    private void BuscadorModificacionClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscadorModificacionClienteActionPerformed
      
        DBConect conexion=new DBConect();  
        
        try{

          resultadoClientesEdicion= new DefaultTableModel();
          tablaClientesEncontradosEdicion.setModel(resultadoClientesEdicion);
          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();
          String nombreVar=NombreBusqueda2.getText();
          String apellidoPVar=apellidoBusqueda2.getText();
          String apellidoMVar=apellidoMBusqueda2.getText();

          String varBusqueda=nombreVar+apellidoMVar+apellidoPVar;

          if(varBusqueda.equals("")){
              JOptionPane.showMessageDialog(null,"Se necesita al menos un campo para realizar la busqueda"); 
              return;
          }

          String sqlString="Select idcliente as 'Cliente',concat(nombre,' ',apellidop,' ',apellidom) as 'Nombre completo',dirección as 'Dirección', " +
                           " telefono as 'Teléfono' " +
                           " from cliente where ";

          int indicadorAnd=0;

          if(!nombreVar.equals("")){
              sqlString+=" nombre like '"+nombreVar+"%'";
              indicadorAnd++;
          }

          if(!apellidoPVar.equals("")){
              if(indicadorAnd>0){
                  sqlString+=" and ";
              } 
              sqlString+=" apellidoP like '"+apellidoPVar+"%'";
               indicadorAnd++;
          }

          if(!apellidoMVar.equals("")){

              if(indicadorAnd>0){
                  sqlString+=" and ";
              }
              sqlString+=" apellidoM like '"+apellidoMVar+"%'";
              indicadorAnd++;
          }

          ResultSet rs = statement.executeQuery(sqlString);

          ResultSetMetaData rsMd = rs.getMetaData();
          //La cantidad de columnas que tiene la consulta
          int cantidadColumnas = rsMd.getColumnCount();
          //Establecer como cabezeras el nombre de las colimnas
          for (int i = 1; i <= cantidadColumnas; i++) {
           resultadoClientesEdicion.addColumn(rsMd.getColumnLabel(i));
          }
          //Creando las filas para el JTable
          int resultados=0;
          while (rs.next()) {
           resultados++;
              Object[] fila = new Object[cantidadColumnas];
           for (int i = 0; i < cantidadColumnas; i++) {
             fila[i]=rs.getObject(i+1);
           }
           resultadoClientesEdicion.addRow(fila);
          }
          
          if(resultados==0){
              JOptionPane.showMessageDialog(null,"No se encontro un cliente con los datos proporcionados");
          }

        }catch(Exception e){
               e.printStackTrace();
        }       
    }//GEN-LAST:event_BuscadorModificacionClienteActionPerformed

    private void GuardarCambiosClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarCambiosClienteActionPerformed
        
        int fila=tablaClientesEncontradosEdicion.getSelectedRow();
        int idClienteAModificar=(int)tablaClientesEncontradosEdicion.getValueAt(fila, 0);
        String varNombre=newNombreVenta2.getText();
        String varApellidoP=newApellidoPVenta2.getText();
        String varApellidoM=newApellidoMVenta2.getText();
        String varDireccion=newDireccionVenta3.getText();
        String varTelefono=newTelVenta4.getText();

        DBConect conexion=new DBConect();  
        
        try{

          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();
          
          String sqlString="Update cliente set nombre='"+varNombre+"'"
                           +" , apellidoP='"+varApellidoP+"'"
                           +" , apellidoM='"+varApellidoM+"'"
                           +" , `dirección`='"+varDireccion+"'"
                           +" , telefono='"+varTelefono+"'"
                           +" where idCliente='"+idClienteAModificar+"'";
          
          int resultado=statement.executeUpdate(sqlString);
          if(resultado>=1){
             JOptionPane.showMessageDialog(null,"Se modifico la información correctamente.");
          }else{
             JOptionPane.showMessageDialog(null,"Ocurrio un error al guardar la información. \n Por favor intenta guardar cambios nuevamente");
          }
          
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        //actualizamos con los datos correspondientes
        BuscadorModificacionClienteActionPerformed(evt);
        panelModificarCliente.setVisible(false);
    }//GEN-LAST:event_GuardarCambiosClienteActionPerformed

    private void MostrarPanelModificacionClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MostrarPanelModificacionClienteActionPerformed
        int fila=tablaClientesEncontradosEdicion.getSelectedRowCount();
        
        if(fila==0){
            JOptionPane.showMessageDialog(null,"No has seleccionado un cliente para modificar");
            return;
        }else{
            fila=fila-1;
        }
        
        fila=tablaClientesEncontradosEdicion.getSelectedRow();
        
        String nombreCompleto=""+tablaClientesEncontradosEdicion.getValueAt(fila, 1);
        
        String nombre[]=nombreCompleto.split(" ");
        
        String nombreG=nombre[0];
        String apellidoP=nombre[1];
        String apellidoM="";
        if(nombre.length>=3){        
               apellidoM=nombre[2];
        }
        String direccion=""+tablaClientesEncontradosEdicion.getValueAt(fila, 2);
        String telefono=""+tablaClientesEncontradosEdicion.getValueAt(fila, 3);
        
        newNombreVenta2.setText(nombreG);
        newApellidoPVenta2.setText(apellidoP);
        newApellidoMVenta2.setText(apellidoM);
        newDireccionVenta3.setText(direccion);
        newTelVenta4.setText(telefono);
        
        panelModificarCliente.setVisible(true);
       
    }//GEN-LAST:event_MostrarPanelModificacionClienteActionPerformed

    private void DesactivarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DesactivarClienteActionPerformed
        
        int fila=tablaClientesEncontradosEdicion.getSelectedRowCount();
       
        if(fila==0){
            JOptionPane.showMessageDialog(null,"No has seleccionado un cliente para desactivarlo");
            return;
        }else{
            fila=fila-1;
        }
        
         int idClienteAModificar=(int)tablaClientesEncontradosEdicion.getValueAt(fila, 0);
        
       int confirmacion= JOptionPane.showConfirmDialog(null,""
                + "Al desactivar un cliente, el cliente no será encontrado desde la venta,"
                + "el cliente no podra volver a activarse,\n será necesario crear un nuevo cliente en caso de ser necesario. \n"+
                "Las ventas realizadas a este cliente quedan guardadas en el historial.","¿Estas seguro que deseas desactivar el cliente?",JOptionPane.YES_NO_OPTION);
       if(confirmacion==JOptionPane.YES_OPTION){
           DBConect conexion=new DBConect();  
        
        try{

          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();
          
          String sqlString="Update cliente set activo=0"
                           +" where idCliente='"+idClienteAModificar+"'";
          
          int resultado=statement.executeUpdate(sqlString);
          if(resultado>=1){
             JOptionPane.showMessageDialog(null,"Se desactivo el cliente, ya no se podran realizar ventas a este cliente");
          }else{
             JOptionPane.showMessageDialog(null,"Ocurrio un error al desactivar el cliente la información. \n Por favor intentalo nuevamente");
          }
          
        }catch(Exception e){
            e.printStackTrace();
        }
       }else{
       
       }
       
    }//GEN-LAST:event_DesactivarClienteActionPerformed

    private void uMedidaLista1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uMedidaLista1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_uMedidaLista1ActionPerformed

    private void ModificarProductosGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModificarProductosGuardarActionPerformed
        int fila=listaProductosAModificar.getSelectedRowCount();
        
        if(fila==0){
            JOptionPane.showMessageDialog(null,"No has seleccionado un producto para modificar");
            return;
        }else{
            fila=fila-1;
        }
        fila=listaProductosAModificar.getSelectedRow();
        String idProductoModificar=""+listaProductosAModificar.getValueAt(fila, 0);
        String codigo=""+listaProductosAModificar.getValueAt(fila, 1);
        DBConect conexion=new DBConect();  
        
        try{

          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();
          String descripcionVar=descripcionAltaText1.getText();
          String precioCVar=precioCompraAltaText1.getText();
          String precioVVar=precioVentaAltaText1.getText();
          String cantidad=unidadesEnCajaText1.getText();
          String uM=uMedidaLista1.getSelectedItem().toString();
          String presentacion=presentacionLista1.getSelectedItem().toString();

          String sqlString="UPDATE productos set descripcion='"+descripcionVar+"',precioUnitarioC="+precioCVar+", "
                            + " precioUnitarioV="+precioVVar+",unidadesEnCaja="+cantidad+",uMedida='"+uM+"', presentacion='"+presentacion+"'"
                            +" where idProductos='"+idProductoModificar+"'";
          
          int resultado=statement.executeUpdate(sqlString);
          
          if(resultado>=1){
                JOptionPane.showMessageDialog(null, "Se modifico correctamente la informacion del producto");
                BusquedaProductosTodoActionPerformed(evt);
                descripcionAltaText1.setText("");
                precioVentaAltaText1.setText("");
                precioCompraAltaText1.setText("");
                unidadesEnCajaText1.setText("");

                uMedidaLista1.setSelectedItem("---");
                presentacionLista1.setSelectedItem("---");
          }else{
              JOptionPane.showMessageDialog(null, "Ocurrio un error al guardar los cambios por favor intente nuevamente");
          }
          
          //codigoProdFiltro.setText("");
          descripcionProdFiltro.setText("");
          panelModificarProducto.setVisible(false);
          
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_ModificarProductosGuardarActionPerformed

    private void editarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarProductoActionPerformed
        int fila=listaProductosAModificar.getSelectedRowCount();
        
        if(fila==0){
            JOptionPane.showMessageDialog(null,"No has seleccionado un producto para modificar");
            return;
        }else{
            fila=fila-1;
        }
        fila=listaProductosAModificar.getSelectedRow();
        String idProductoModificar=""+listaProductosAModificar.getValueAt(fila, 0);
        String codigo=""+listaProductosAModificar.getValueAt(fila, 2);
        String descripcion=""+listaProductosAModificar.getValueAt(fila, 2);
        String precioC=""+listaProductosAModificar.getValueAt(fila, 3);
        String precioV=""+listaProductosAModificar.getValueAt(fila, 4);
        String cantidad=""+listaProductosAModificar.getValueAt(fila, 5);
        String uMedida=""+listaProductosAModificar.getValueAt(fila, 6);
        String presentacion=""+listaProductosAModificar.getValueAt(fila, 7);
        
        descripcionAltaText1.setText(descripcion);
        precioVentaAltaText1.setText(precioV);
        precioCompraAltaText1.setText(precioC);
        unidadesEnCajaText1.setText(cantidad);
        
        uMedidaLista1.setSelectedItem(uMedida);
        presentacionLista1.setSelectedItem(presentacion);
        
        panelModificarProducto.setVisible(true);
        
    }//GEN-LAST:event_editarProductoActionPerformed

    private void preAgregarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preAgregarClienteActionPerformed
        String clienteVar=clienteBusqueda.getText();
        String nombreVar=nombreBusqueda.getText();
        String apellidoPVar=apellidoPBusqueda.getText();
        String apellidoMVar=apellidoMBusqueda.getText();
        String direccionVar=direccionBusqueda.getText();

        //newClienteVenta.setText(clienteVar);

        newNombreVenta.setText(nombreVar);

        newApellidoPVenta.setText(apellidoPVar);

        newApellidoMVenta.setText(apellidoMVar);

        newDireccionVenta.setText(direccionVar);

        panelAgregarClienteVenta.setVisible(true);
    }//GEN-LAST:event_preAgregarClienteActionPerformed

    private void removerProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removerProductoActionPerformed
        int fila=listaProductosAModificar.getSelectedRowCount();
        
        if(fila==0){
            JOptionPane.showMessageDialog(null,"No has seleccionado un producto para desactivar");
            return;
        }else{
            fila=fila-1;
        }
        
        int confirmacion= JOptionPane.showConfirmDialog(null,""
                + "Al desactivar un producto, el producto no será encontrado desde la venta,"
                + "el producto no podra volver a activarse,\n será necesario registrar un nuevo producto en caso de ser necesario. \n"+
                "Las ventas de este producto quedan guardadas en el historial.","¿Estas seguro que deseas desactivar el producto?",JOptionPane.YES_NO_OPTION);
       if(confirmacion==JOptionPane.YES_OPTION){
            String idProductoModificar=""+listaProductosAModificar.getValueAt(fila, 0);
        
            DBConect conexion=new DBConect();  

            try{

              Connection conexionMysql = conexion.GetConnection();

              Statement statement = conexionMysql.createStatement();

              String sqlString="UPDATE productos set activo=0"
                                +" where idProductos='"+idProductoModificar+"'";

              int resultado=statement.executeUpdate(sqlString);

              if(resultado>=1){
                    JOptionPane.showMessageDialog(null, "Se desactivo correctamente la informacion del producto");
              }else{
                  JOptionPane.showMessageDialog(null, "Ocurrio un error al desactivar el producto por favor intente nuevamente");
              }

              //codigoProdFiltro.setText("");
              descripcionProdFiltro.setText("");
              panelModificarProducto.setVisible(true);

            }catch(Exception e){
                e.printStackTrace();
            }
       }
        
    }//GEN-LAST:event_removerProductoActionPerformed

    private void eliminarProductoDeVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarProductoDeVentaActionPerformed
        int fila=tablaDetalleVenta.getSelectedRowCount();
        
        if(fila==0){
            JOptionPane.showMessageDialog(null,"No has seleccionado un producto a eliminar");
            return;
        }else{
            fila=fila-1;
        }
        
        fila=tablaDetalleVenta.getSelectedRow();
        
       ((DefaultTableModel)tablaDetalleVenta.getModel()).removeRow(fila);
        TableModel model = tablaDetalleVenta.getModel();
       double granTotal=0.0;
       int nuevoConsecutivo=1;
            for(int i=0;i<model.getRowCount();i++){
                
                
                ((DefaultTableModel)tablaDetalleVenta.getModel()).setValueAt(nuevoConsecutivo, i, 0);
                
                Double val=(Double)model.getValueAt(i, 3);
                String cantidad=(String)model.getValueAt(i, 1);
                Double valNumeric=val;
                Double cantidadNumeric=Double.parseDouble(cantidad);
                Double numericTotal=valNumeric*cantidadNumeric;
                granTotal+=numericTotal;
                nuevoConsecutivo++; 
            }

            etiquetaGranTotal.setText("$"+granTotal);
            codigoBusqueda.setText("");
            descripcionManual.setText("");
            cantidadPizza.setText("1");
            codigoBusqueda.setFocusable(true);
            codigoBusqueda.grabFocus(); 
       
    }//GEN-LAST:event_eliminarProductoDeVentaActionPerformed

    private void generarReporteVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarReporteVentasActionPerformed
        Date fechaInit = fechaInicio.getDate();
        Date fechaEnd = fechaFin.getDate();
        
        if(fechaInit==null || fechaFin==null){
            JOptionPane.showMessageDialog(null,"Es necesario proporcionar el periodo de tiempo del reporte");
            return;
        }
        
        if(fechaEnd.before(fechaInit)){
            JOptionPane.showMessageDialog(null,"La fecha de fin no puede ser anterior a la fecha de inicio");
            return;
        }
        
        SimpleDateFormat formatoFecha=new SimpleDateFormat("yyyy/MM/dd");   
        String fechaInicial=formatoFecha.format(fechaInit);
        String fechaFinal=formatoFecha.format(fechaEnd);
        DBConect conexion=new DBConect();
        
        try{
            Connection conexionMysql = conexion.GetConnection();

            Statement statement = conexionMysql.createStatement();
            DateEditor de=(DateEditor)jSpinner1.getEditor();
            DateEditor de2=(DateEditor)jSpinner2.getEditor();
            String tiempoInicio=fechaInicial+" "+de.getFormat().format(jSpinner1.getValue());
            String tiempoFin=fechaFinal+" "+de2.getFormat().format(jSpinner2.getValue());
            
            
            String SQLString="SELECT a.consecutivoVenta as 'No. de venta',a.total,a.fechaVenta,b.descripcionProd,d.dirección as 'Dirección de entrega',b.precioTotal,b.cantidad as 'Cantidad vendida', "+
                             " b.precioTotal*b.cantidad as 'Subtotal',c.NombreCompleto as 'Vendido por', "+
                             " c.NombreUsuario FROM venta as a inner join detalleventa b on a.idVenta=b.Venta_idVenta "+
                             " inner join usuarios as c on c.idUsuario=a.usuarios_idUsuario "+
                             " inner join cliente as d on d.idCliente=a.cliente_idcliente"+
                             " where a.fechaVenta BETWEEN '"+tiempoInicio+"' AND '"+tiempoFin+"' ";
            
            String usuarioSeleccionado=listaTodosLosUsuarios.getSelectedItem().toString();
            if(usuarioSeleccionado.equals("---")){        
                 SQLString+=" order by a.idventa,b.consecutivoVenta";
            }else{
                
                 SQLString+=" and c.NombreCompleto='"+usuarioSeleccionado+"' ";
                
                 SQLString+=" order by a.idventa,b.consecutivoVenta";
                 
            }        
           
            System.out.println(SQLString);
            
            ResultSet rs= statement.executeQuery(SQLString);
            
            Double sumaTotalPeriodo=0.0;
            String nombreUsuario="---";
            Double sumaIvaPeriodo=0.0;
            while(rs.next()){
                
                Double cantidad=rs.getDouble("Cantidad vendida");
                if(cantidad>1){
                    sumaTotalPeriodo+=(rs.getDouble("precioTotal")*cantidad);
                    sumaIvaPeriodo+=(rs.getDouble("precioTotal")*cantidad)*ivaConfigurado;
                }else{
                    sumaTotalPeriodo+=rs.getDouble("precioTotal");
                    sumaIvaPeriodo+=rs.getDouble("precioTotal")*ivaConfigurado;
                }
                
                nombreUsuario=rs.getString("Vendido por");
               
            }
            
            if(sumaTotalPeriodo==0.0){
                JOptionPane.showMessageDialog(null, "No se encontraron registros de venta en el periodo seleccionado");
            }
            
            if(usuarioSeleccionado.equals("---")){
            
            }else{
                labelUsuarioQueVendio.setText(nombreUsuario);
            }
            
            labelTotalPeriodo.setText("$"+decimales.format(sumaTotalPeriodo));
            labelTotalIvaPeriodo.setText("$"+decimales.format(sumaIvaPeriodo));
            
            rs.beforeFirst();
            
            DefaultTableModel listaDetalleVentasPeriodo= new DefaultTableModel();
                
                ResultSetMetaData rsMd = rs.getMetaData();
                //La cantidad de columnas que tiene la consulta
                int cantidadColumnas = rsMd.getColumnCount();
                //Establecer como cabezeras el nombre de las colimnas
                for (int i = 1; i <= cantidadColumnas; i++) {
                 listaDetalleVentasPeriodo.addColumn(rsMd.getColumnLabel(i));
                }
                //Creando las filas para el JTable
                while (rs.next()) {
                 Object[] fila = new Object[cantidadColumnas];
                 for (int i = 0; i < cantidadColumnas; i++) {
                   fila[i]=rs.getObject(i+1);
                 }
                 listaDetalleVentasPeriodo.addRow(fila);
                }
            
                listaReporte.setModel(listaDetalleVentasPeriodo);

        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        
    }//GEN-LAST:event_generarReporteVentasActionPerformed

    private void newNameToAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newNameToAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newNameToAddActionPerformed

    private void c1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c1ActionPerformed

    private void c2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c2ActionPerformed

    private void c12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c12ActionPerformed

    private void c22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c22ActionPerformed

    private void newNameToAdd2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newNameToAdd2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newNameToAdd2ActionPerformed

    private void AgregarNuevoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarNuevoUsuarioActionPerformed
      DBConect conexion=new DBConect();
        
        try{
            Connection conexionMysql = conexion.GetConnection();

            Statement statement = conexionMysql.createStatement();

            String varUsuario=newUserToAdd.getText();
            String varPass=newPassToAdd.getText();
            String varNombre=newNameToAdd.getText();
                        
            String validadorVacios=varUsuario+varPass+varNombre;
            
            if(validadorVacios.equals("")){
                
                JOptionPane.showMessageDialog(null,"No has proporcionado ningun dato");
                
            }
            
            String mensajeError="";
            
            
            if(varUsuario.equals("")){
                
                mensajeError+="Usuario \n";
                
            }
            
            if(varPass.equals("")){
                
                mensajeError+="Contraseña \n";
                
            }
            
             if(varNombre.equals("")){
                
                mensajeError+="Nombre \n";
                
            }
            
            if(!mensajeError.equals("")){
            
                mensajeError="Los siguientes campos son necesarios para continuar: \n\n "+mensajeError;
                
                JOptionPane.showMessageDialog(null, mensajeError);
                return; 
            }
             
            ///procesamos los checkbox
            int varPermisoClientes=0;
            int varPermisoProductos=0;
            int varPermisoVentas=0;
            int varPermisoUsuarios=0;
            int varPermisoReportes=0;
            
            if(c1.isSelected()){
                varPermisoClientes=1;
            }
            if(c2.isSelected()){
                varPermisoProductos=1;
            }
            if(c3.isSelected()){
                varPermisoVentas=1;
            }
            if(c4.isSelected()){
                varPermisoUsuarios=1;
            }
            if(c5.isSelected()){
                varPermisoReportes=1;
            }
              
            String sqlString="INSERT INTO `usuarios` (nombreUsuario,password,NombreCompleto,c1,c2,c3,c4,c5) "
                    + " VALUES ('"+varUsuario+"','"+varPass+"','"+varNombre+"',"+varPermisoClientes+","+varPermisoProductos+","+varPermisoVentas+","+varPermisoUsuarios+","+varPermisoReportes+")";
        
            int resultado=statement.executeUpdate(sqlString);
       
            if(resultado>0){

                JOptionPane.showMessageDialog(null, "El usuario se agrego correctamente");
                newUserToAdd.setText("");
                newPassToAdd.setText("");
                newNameToAdd.setText("");
            }
            
       }catch(Exception e){
           e.printStackTrace();
       }
    }//GEN-LAST:event_AgregarNuevoUsuarioActionPerformed

    private void BuscarUusariosRegistradosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarUusariosRegistradosActionPerformed
        DBConect conexion=new DBConect();  
        
        try{

          usuariosEncontrados= new DefaultTableModel();
          listadoUsuariosRegistrados.setModel(usuariosEncontrados);
          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();
          String varUsuario=usuarioFiltro.getText();
          String varNombre=nombreUsuarioFiltro.getText();
         

          String varBusqueda=varUsuario+varNombre;

          if(varBusqueda.equals("")){
              JOptionPane.showMessageDialog(null,"Se necesita al menos un campo para realizar la busqueda"); 
              return;
          }

          String sqlString="Select nombreUsuario as 'Usuario',password as 'Contraseña', NombreCompleto as 'Nombre completo', estatus as 'Activo'" +
                           ",c1,c2,c3,c4,c5,idUsuario from usuarios where ";

          int indicadorAnd=0;

          if(!varUsuario.equals("")){
              sqlString+=" nombreUsuario like '"+varUsuario+"%'";
              indicadorAnd++;
          }

          if(!varNombre.equals("")){
              if(indicadorAnd>0){
                  sqlString+=" and ";
              } 
              sqlString+=" NombreCompleto like '"+varNombre+"%'";
               indicadorAnd++;
          }

          ResultSet rs = statement.executeQuery(sqlString);

          ResultSetMetaData rsMd = rs.getMetaData();
          //La cantidad de columnas que tiene la consulta
          int cantidadColumnas = rsMd.getColumnCount();
          //Establecer como cabezeras el nombre de las colimnas
          for (int i = 1; i <= cantidadColumnas; i++) {
           usuariosEncontrados.addColumn(rsMd.getColumnLabel(i));
          }
          //Creando las filas para el JTable
          int resultados=0;
          while (rs.next()) {
           resultados++;
              Object[] fila = new Object[cantidadColumnas];
           for (int i = 0; i < cantidadColumnas; i++) {
             fila[i]=rs.getObject(i+1);
           }
           usuariosEncontrados.addRow(fila);
          }
          
          if(resultados==0){
              JOptionPane.showMessageDialog(null,"No se encontro un usuario con los datos proporcionados");
          }
          
          listadoUsuariosRegistrados.getColumn("c1").setWidth(0);
          listadoUsuariosRegistrados.getColumn("c1").setMinWidth(0);
          listadoUsuariosRegistrados.getColumn("c1").setMaxWidth(0);
          
          listadoUsuariosRegistrados.getColumn("c2").setWidth(0);
          listadoUsuariosRegistrados.getColumn("c2").setMinWidth(0);
          listadoUsuariosRegistrados.getColumn("c2").setMaxWidth(0);
          
          listadoUsuariosRegistrados.getColumn("c3").setWidth(0);
          listadoUsuariosRegistrados.getColumn("c3").setMinWidth(0);
          listadoUsuariosRegistrados.getColumn("c3").setMaxWidth(0);
          
          listadoUsuariosRegistrados.getColumn("c4").setWidth(0);
          listadoUsuariosRegistrados.getColumn("c4").setMinWidth(0);
          listadoUsuariosRegistrados.getColumn("c4").setMaxWidth(0);
          
          listadoUsuariosRegistrados.getColumn("c5").setWidth(0);
          listadoUsuariosRegistrados.getColumn("c5").setMinWidth(0);
          listadoUsuariosRegistrados.getColumn("c5").setMaxWidth(0);
          
          listadoUsuariosRegistrados.getColumn("idUsuario").setWidth(0);
          listadoUsuariosRegistrados.getColumn("idUsuario").setMinWidth(0);
          listadoUsuariosRegistrados.getColumn("idUsuario").setMaxWidth(0);
          
        }catch(Exception e){
               e.printStackTrace();
        }
    }//GEN-LAST:event_BuscarUusariosRegistradosActionPerformed

    private void ModificarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModificarUsuarioActionPerformed
        
        int fila=listadoUsuariosRegistrados.getSelectedRowCount();
        
        if(fila==0){
            JOptionPane.showMessageDialog(null,"No has seleccionado un usuario a modificar");
            return;
        }else{
            fila=listadoUsuariosRegistrados.getSelectedRow();
        }
        
        String usuario=""+listadoUsuariosRegistrados.getValueAt(fila, 0);
        String pass=""+listadoUsuariosRegistrados.getValueAt(fila, 1);
        String NombreCompleto=""+listadoUsuariosRegistrados.getValueAt(fila, 2);
        String estatus=""+listadoUsuariosRegistrados.getValueAt(fila, 3);
        String c1=""+listadoUsuariosRegistrados.getValueAt(fila, 4);
        String c2=""+listadoUsuariosRegistrados.getValueAt(fila, 5);
        String c3=""+listadoUsuariosRegistrados.getValueAt(fila, 6);
        String c4=""+listadoUsuariosRegistrados.getValueAt(fila, 7);
        String c5=""+listadoUsuariosRegistrados.getValueAt(fila, 8);
        
        newUserToAdd1.setText(usuario);
        newPassToAdd2.setText(pass);
        newNameToAdd2.setText(NombreCompleto);
        
        if(c1.equals("1")){
         c12.setSelected(true);
        }else{
         c12.setSelected(false);
        }
        if(c2.equals("1")){
         c22.setSelected(true);
        }else{
         c22.setSelected(false);
        }
        if(c3.equals("1")){
         c32.setSelected(true);
        }else{
         c32.setSelected(false);
        }
        if(c4.equals("1")){
         c42.setSelected(true);
        }else{
         c42.setSelected(false);
        }
        if(c5.equals("1")){
         c52.setSelected(true);
        }else{
         c52.setSelected(false);
        }
        
        panelModificarUsuario.setVisible(true);
       
    }//GEN-LAST:event_ModificarUsuarioActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        
        String idUsuario=""+listadoUsuariosRegistrados.getValueAt(listadoUsuariosRegistrados.getSelectedRow(), 9);
        
        String usuario=newUserToAdd1.getText();
        String pass=newPassToAdd2.getText();
        String nombre=newNameToAdd2.getText();
        String permiso1,permiso2,permiso3,permiso4,permiso5;
        
        if(c12.isSelected()){
            permiso1="1";
        }else{
             permiso1="0";
        }
        
        if(c22.isSelected()){
            permiso2="1";
        }else{
             permiso2="0";
        }
        
        if(c32.isSelected()){
            permiso3="1";
        }else{
             permiso3="0";
        }
        
        if(c42.isSelected()){
            permiso4="1";
        }else{
             permiso4="0";
        }
        
        if(c52.isSelected()){
            permiso5="1";
        }else{
             permiso5="0";
        }
        
        DBConect conexion=new DBConect();  
        
        try{

          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();
          
          String sqlString="Update usuarios set nombreUsuario='"+ usuario+
                           "', password='"+pass+
                           "', NombreCompleto='"+nombre+"',c1="+permiso1+",c2="+permiso2+",c3="+permiso3+",c4="+permiso4+",c5="+permiso5
                           + " where idUsuario="+idUsuario;
          
          int resultado=statement.executeUpdate(sqlString);
          if(resultado>=1){
              JOptionPane.showMessageDialog(null, "Se modificaron los datos de usuario correctamente.");
              BuscarUusariosRegistradosActionPerformed(evt);
          }else{
              JOptionPane.showMessageDialog(null, "Ocurrio un error al guardar los cambios, por favor intente nuevamente.");
          }
          
        }catch(Exception e){
            e.printStackTrace();
        }
        
        newUserToAdd1.setText("");
        newPassToAdd2.setText("");
        newNameToAdd2.setText("");
        panelModificarUsuario.setVisible(false);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void DesactivarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DesactivarUsuarioActionPerformed
       int fila=listadoUsuariosRegistrados.getSelectedRowCount();
       if(fila==0){
           JOptionPane.showMessageDialog(null, "No se ha seleccionado un usuario para desactivar");
           return;
       }else{
           fila=listadoUsuariosRegistrados.getSelectedRow();
       }
       
       int confirmacion= JOptionPane.showConfirmDialog(null,""
                + "Al desactivar un usuario, el usuario no podra iniciar sesión el el sistema,"
                , "¿Estas seguro que deseas desactivar el producto?",JOptionPane.YES_NO_OPTION);
       if(confirmacion==JOptionPane.YES_OPTION){
           String idUsuario=""+listadoUsuariosRegistrados.getValueAt(fila, 9);
       
            DBConect conexion=new DBConect();  

            try{

              Connection conexionMysql = conexion.GetConnection();

              Statement statement = conexionMysql.createStatement();

              String sqlString="Update usuarios set estatus=0 "
                               + " where idUsuario="+idUsuario;

              int resultado=statement.executeUpdate(sqlString);
              if(resultado>=1){
                  JOptionPane.showMessageDialog(null, "Se desactivo el usuario correctamente.");
                  BuscarUusariosRegistradosActionPerformed(evt);
              }else{
                  JOptionPane.showMessageDialog(null, "Ocurrio un error al desactivar el usuario, por favor intente nuevamente.");
              }

            }catch(Exception e){
                e.printStackTrace();
            }
       }
       
        newUserToAdd1.setText("");
        newPassToAdd2.setText("");
        newNameToAdd2.setText("");
        panelModificarUsuario.setVisible(false);
    }//GEN-LAST:event_DesactivarUsuarioActionPerformed

    private void tabReportesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabReportesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabReportesMouseClicked

    private void efectivoRecibidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efectivoRecibidoKeyPressed

    }//GEN-LAST:event_efectivoRecibidoKeyPressed

    private void efectivoRecibidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efectivoRecibidoKeyReleased
        String precioSinSigno=etiquetaGranTotal.getText().substring(1,etiquetaGranTotal.getText().length());
        Double precio=0.0;
        String efectivo=efectivoRecibido.getText();
        Double efectivoRecibido=0.0;
        
        int numProductos=tablaDetalleVenta.getRowCount();
        if(numProductos>=1){
            
        }else{
            JOptionPane.showMessageDialog(null,"No se han agregado productos a la orden");
            
            return;
        }
        
        if(efectivo.equals("")){
             cambioTotal.setText("$0.00");
        }else{
            if(precioSinSigno==""){
                precioSinSigno="0.0";
            }
            precio=Double.parseDouble(precioSinSigno);
            efectivoRecibido=Double.parseDouble(efectivo);
            Double cambio=efectivoRecibido-precio;
            cambioTotal.setText("$"+decimales.format(cambio));
            
        }
    }//GEN-LAST:event_efectivoRecibidoKeyReleased

    private void actualizarConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarConfiguracionActionPerformed
        
        DBConect conexion=new DBConect();
        
        try{
            Connection conexionMysql = conexion.GetConnection();

            Statement statement = conexionMysql.createStatement();
            
            String iva=""+Double.parseDouble(valorIVA.getText())/100;
            String d1=""+Double.parseDouble(valorD1.getText())/100;
            String d2=""+Double.parseDouble(valorD2.getText())/100;
            String d3=""+Double.parseDouble(valorD3.getText())/100;
            String d4=""+Double.parseDouble(valorD4.getText())/100;
            String d5=""+Double.parseDouble(valorD5.getText())/100;
            String sucursal=valorSucursal.getText();
            String rfc=valorRFC.getText();
            String slogan=valorSlogan.getText();
            String direccion=valorDireccion.getText();
            
            
            String sqlString="update installData set iva="+iva+", desc1="+d1+", desc2="+d2+", desc3="+d3+", desc4="+d4+", desc5="+d5+
                    ",sucursal='"+sucursal+"',rfc='"+rfc+"',slogan='"+slogan+"',direccion='"+direccion+"' "
                    + "where idInstallData=1";
            
            int result=statement.executeUpdate(sqlString);
            
            if(result>=1){
                JOptionPane.showMessageDialog(null,"Se guardo la configuración correctamente.");
                
                Configuracion c=obtenerConfiguracion();
                ivaConfigurado=c.getIva();
                valorIVA.setText(""+decimalEntero.format((ivaConfigurado*100)));
                valorD1.setText(""+decimalEntero.format(c.getDescuento1()*100));
                valorD2.setText(""+decimalEntero.format(c.getDescuento2()*100));
                valorD3.setText(""+decimalEntero.format(c.getDescuento3()*100));
                valorD4.setText(""+decimalEntero.format(c.getDescuento4()*100));
                valorD5.setText(""+decimalEntero.format(c.getDescuento5()*100));
                valorSucursal.setText(c.getSucursal());
                valorDireccion.setText(c.getDireccion());
                valorRFC.setText(c.getRfc());
                valorSlogan.setText(c.getSlogan());
            }else{
                JOptionPane.showMessageDialog(null,"Ocurrio un error al guardar la configuración, por favor intente nuevamente.");
            }
            
        }catch(Exception e){
            
            e.printStackTrace();
        }    
        
        
        
    }//GEN-LAST:event_actualizarConfiguracionActionPerformed

    public void agregarPizzas2Paquete(){
     /// aqui agregamos la pizzas que pueden ir combinadas en el paquete
         DefaultTableModel model = (DefaultTableModel) tablaDetalleVenta.getModel();
            Vector row = new Vector();

            int filasTotales=model.getRowCount();
            int nuevaFila=filasTotales+1;
            row.add(nuevaFila);
            row.add("1");
            
            String sabor1=seleccionPizza2.getSelectedItem().toString();
            String sabor2=seleccionPizza3.getSelectedItem().toString();
            String textoExplicacionSabores="";
            
            String descripProd2=seleccionPizza2.getSelectedItem().toString();
            String[] s2 = descripProd2.split(" | ");
            String descripProd3=seleccionPizza3.getSelectedItem().toString();
            String[] s3 = descripProd3.split(" | ");
            
            if(sabor2.equals("---")){
                textoExplicacionSabores=s2[0]+", Extras:"+seleccionExtras2.getSelectedItem().toString();
            }else{
                
                textoExplicacionSabores="1/2 "+s2[0]+", 1/2 "+s3[0]+", Extras:"+seleccionExtras2.getSelectedItem().toString();
                
            }
            
            row.add(textoExplicacionSabores);
         Double precioDeLaPizza=0.0;
         int idProducto=0;
         try{
          ///obtenemos el precio de venta por la descripcion de la pizza
          DBConect conexion=new DBConect(); 
          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();
          
          String sqlString2="Select * from  productos where descripcion='"+seleccionPizza1.getSelectedItem().toString()+"' and TipoProducto=1";
           ResultSet rs=statement.executeQuery(sqlString2);
            while(rs.next()){
               idProducto=rs.getInt("idProductos");
            }
         }catch(Exception e){
             e.printStackTrace();
         }
         ///fijamos el valor a cero ya que la pizza va incluida en  un paquete
            String seleccionExt1=seleccionExtras2.getSelectedItem().toString();
            Double precioExtra=0.0;
            String tamB=tamanioPizza.getSelectedItem().toString();
            
            if(tamB.equals("Chica")){
                if(!seleccionExt1.equals("---")){
                            precioExtra=10.0;
                            //precioDeLaPizza=precioDeLaPizza+precioExtra;
                }
            }
            if(tamB.equals("Mediana")){
                if(!seleccionExt1.equals("---")){
                            precioExtra=12.0;
                            //precioDeLaPizza=precioDeLaPizza+precioExtra;
                }
            }
         
            if(tamB.equals("Grande")){
                if(!seleccionExt1.equals("---")){
                            precioExtra=14.0;
                            //precioDeLaPizza=precioDeLaPizza+precioExtra;
                }
            }
            
            if(tamB.equals("Familiar")){
                if(!seleccionExt1.equals("---")){
                            precioExtra=24.0;
                            //precioDeLaPizza=precioDeLaPizza+precioExtra;
                }
            }
            
            row.add(precioExtra);
            String x=(String)row.get(1);
            Double y=(Double)row.get(3);
            
            Double cant=Double.parseDouble(x);
            Double precio=y;

            Double subtGrid=cant*precio;

            row.add(""+subtGrid);

            row.add(idProducto);
            
            String tam=tamanioPizza.getSelectedItem().toString();
            //Chica, Mediana, Grande, Familiar
            
            String letraT="---";
            
            if(tam.equals("Chica")){
                letraT="C";
            }
            if(tam.equals("Mediana")){
                letraT="M";
            }
            if(tam.equals("Grande")){
                letraT="G";
            }
            if(tam.equals("Familiar")){
                letraT="F";
            }
            row.add(letraT);
           
            model.addRow(row);
            //tablaDetalleVenta.setModel(model);

            tablaDetalleVenta.getColumn("Id producto").setWidth(0);
            tablaDetalleVenta.getColumn("Id producto").setMinWidth(0);
            tablaDetalleVenta.getColumn("Id producto").setMaxWidth(0);
            tablaDetalleVenta.getColumn("Tamaño").setWidth(0);
            tablaDetalleVenta.getColumn("Tamaño").setMinWidth(0);
            tablaDetalleVenta.getColumn("Tamaño").setMaxWidth(0);
             ///obtenemos el descuento
            String descuentoUnidades=comboDescuentos.getSelectedItem().toString();
            
            String descuentoDecimal=descuentoUnidades.replace("%","");
            Double valorDescuento=0.0;
            
            if(descuentoDecimal.equals("---")){
                
            }else{
                valorDescuento=Double.parseDouble(descuentoDecimal);
                valorDescuento=valorDescuento/100;
            }
            
            double granTotal=0.0;
            for(int i=0;i<model.getRowCount();i++){

                Double val=(Double)model.getValueAt(i, 3);
                String cantidad=(String)model.getValueAt(i, 1);
                Double valNumeric=val;
                Double cantidadNumeric=Double.parseDouble(cantidad);
                Double numericTotal=valNumeric*cantidadNumeric;
                granTotal+=numericTotal;
            }
            Double descuentoTotal=granTotal*valorDescuento;
             
            ivaTotal.setText("$"+decimales.format(granTotal*ivaConfigurado));
            etiquetaGranTotal.setText("$"+decimales.format((granTotal-descuentoTotal)));
            //codigoBusqueda.setText("");
            descripcionManual.setText("");
            //cantidadPizza.setText("1");
            //codigoBusqueda.setFocusable(true);
    }
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        indicadorRefrescoUtilizadoEnPaquete=1;
        DefaultTableModel model = (DefaultTableModel) tablaDetalleVenta.getModel();
            Vector row = new Vector();

            int filasTotales=model.getRowCount();
            int nuevaFila=filasTotales+1;
            row.add(nuevaFila);
            row.add("1");
            
         String descripProd=seleccionPizza1.getSelectedItem().toString();
            
         String[] s = descripProd.split(" | ");
         String descSinIngred=s[0];   
         row.add(descSinIngred+", Extras:"+seleccionExtras1.getSelectedItem().toString());
         Double precioDeLaPizza=0.0;
         String p1=seleccionPizza2.getSelectedItem().toString();
         String p2=seleccionPizza2.getSelectedItem().toString();
         int idProducto=0;
         try{
          ///obtenemos el precio de venta por la descripcion de la pizza
          DBConect conexion=new DBConect(); 
          Connection conexionMysql = conexion.GetConnection();

          Statement statement = conexionMysql.createStatement();
          
          String sqlString2="Select * from  productos where descripcion='"+seleccionPizza1.getSelectedItem().toString()+"' and TipoProducto=1";
          ResultSet rs=statement.executeQuery(sqlString2);
          Double precioExtra=0.0;
          
          String seleccionExt1=seleccionExtras1.getSelectedItem().toString();
  
          
          while(rs.next()){
                if(tamanioPizza.getSelectedItem().toString().equals("Chica")){
                    precioDeLaPizza=rs.getDouble("precioChica");
                    if(!seleccionExt1.equals("---")){
                        precioExtra=10.0;
                        if(p1.equals("---") && p2.equals("---")){
                             precioDeLaPizza=45.0;
                        }
                        precioDeLaPizza=precioDeLaPizza+precioExtra;

                    }
                    if(p1.equals("---") && p2.equals("---")){
                             precioDeLaPizza=45.0;
                    }
                }
                if(tamanioPizza.getSelectedItem().toString().equals("Mediana")){
                    precioDeLaPizza=rs.getDouble("precioMediana");
                    if(!seleccionExt1.equals("---")){
                        precioExtra=12.0;
                        if(p1.equals("---") && p2.equals("---")){
                            precioDeLaPizza=80.0;
                        }
                        precioDeLaPizza=precioDeLaPizza+precioExtra;
                    }
                    if(p1.equals("---") && p2.equals("---")){
                            precioDeLaPizza=80.0;
                    }
                }
                if(tamanioPizza.getSelectedItem().toString().equals("Grande")){
                    precioDeLaPizza=rs.getDouble("precioGrande");
                    if(!seleccionExt1.equals("---")){
                        precioExtra=14.0;
                        if(p1.equals("---") && p2.equals("---")){
                            precioDeLaPizza=100.0;
                        }                        
                        precioDeLaPizza=precioDeLaPizza+precioExtra;
                    }
                    if(p1.equals("---") && p2.equals("---")){
                            precioDeLaPizza=100.0;
                    } 
                }
                if(tamanioPizza.getSelectedItem().toString().equals("Familiar")){
                    precioDeLaPizza=rs.getDouble("precioFamiliar");
                     if(!seleccionExt1.equals("---")){
                         precioExtra=24.0;
                         if(p1.equals("---") && p2.equals("---")){
                            precioDeLaPizza=120.0;
                         }
                         precioDeLaPizza=precioDeLaPizza+precioExtra;
                     }
                     if(p1.equals("---") && p2.equals("---")){
                            precioDeLaPizza=120.0;
                     }
                }
                
               idProducto=rs.getInt("idProductos");
            }
         }catch(Exception e){
             e.printStackTrace();
         }
            row.add(precioDeLaPizza);
            String x=(String)row.get(1);
            Double y=(Double)row.get(3);

            Double cant=Double.parseDouble(x);
            Double precio=y;

            Double subtGrid=cant*precio;

            row.add(""+subtGrid);

            row.add(idProducto);
            
            String t=tamanioPizza.getSelectedItem().toString();
            
            row.add(t.charAt(0));
           
            model.addRow(row);
            //tablaDetalleVenta.setModel(model);

            tablaDetalleVenta.getColumn("Id producto").setWidth(0);
            tablaDetalleVenta.getColumn("Id producto").setMinWidth(0);
            tablaDetalleVenta.getColumn("Id producto").setMaxWidth(0);
            tablaDetalleVenta.getColumn("Tamaño").setWidth(0);
            tablaDetalleVenta.getColumn("Tamaño").setMinWidth(0);
            tablaDetalleVenta.getColumn("Tamaño").setMaxWidth(0);
             ///obtenemos el descuento
            String descuentoUnidades=comboDescuentos.getSelectedItem().toString();
            
            String descuentoDecimal=descuentoUnidades.replace("%","");
            Double valorDescuento=0.0;
            
            if(descuentoDecimal.equals("---")){
                
            }else{
                valorDescuento=Double.parseDouble(descuentoDecimal);
                valorDescuento=valorDescuento/100;
            }
            
            double granTotal=0.0;
            for(int i=0;i<model.getRowCount();i++){

                Double val=(Double)model.getValueAt(i, 3);
                String cantidad=(String)model.getValueAt(i, 1);
                Double valNumeric=val;
                Double cantidadNumeric=Double.parseDouble(cantidad);
                Double numericTotal=valNumeric*cantidadNumeric;
                granTotal+=numericTotal;
            }
            Double descuentoTotal=granTotal*valorDescuento;
             
            ivaTotal.setText("$"+decimales.format(granTotal*ivaConfigurado));
            etiquetaGranTotal.setText("$"+decimales.format((granTotal-descuentoTotal)));
            //codigoBusqueda.setText("");
            descripcionManual.setText("");
            //cantidadPizza.setText("1");
            //codigoBusqueda.setFocusable(true);
            //p1=seleccionPizza2.getSelectedItem().toString();
            //p2=seleccionPizza2.getSelectedItem().toString();
            if(p1.equals("---") && p2.equals("---")){
                
            }else{
                agregarPizzas2Paquete();
            }
            /////agregamos el producto a la lista de tipos para saber si imprimir ticket o no
            tiposDeProductoAgregados.add(1);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        Date fechaInicioDia=new Date();
          fechaInicioDia.setHours(01);
          fechaInicioDia.setMinutes(00);
          fechaInicioDia.setSeconds(00);
          Date fechaFinDia=new Date();
          fechaFinDia.setHours(23);
          fechaFinDia.setMinutes(59);
          fechaFinDia.setSeconds(59);
          
          SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String fi=sf.format(fechaInicioDia);
          String ff=sf.format(fechaFinDia);
        
        
        DBConect conexion=new DBConect(); 
        Connection conexionMysql = conexion.GetConnection();
        
        String cantidad=cantidadRetiro.getText();
        String retiro=motivoRetiro.getText();
        if(cantidad.equals("") && retiro.equals("")){
            
            JOptionPane.showMessageDialog(null,"Los datos necesarios para realizar la operación estan vacios,\n por lo cual no se abrira la caja.");
            return;
        }
        
        try{
            Statement statement = conexionMysql.createStatement();
            
            
            String getMax="select MAX(consecutivo) as max from " +
                           " retiro where (fecha BETWEEN '"+fi+"' AND '"+ff+"')";
             ResultSet rs=statement.executeQuery(getMax);
            int max=0;
             while(rs.next()){
            
                max=rs.getInt(1);
                
            }
            max=max+1;
             
            String sqlString2="insert into retiro(cantidad,descripcion,consecutivo,usuario) VALUES("+cantidad+",'"+retiro+"',"+max+","+usuarioLogueado+")";
            
            statement.executeUpdate(sqlString2);
            Ticket t=new Ticket();
            t.AbrirCaja();
            obtenerGastosDelDia(fi,ff);
            motivoRetiro.setText("");
            cantidadRetiro.setText("");
        }catch(Exception e){
            e.printStackTrace();
        }
        
        Double efectivoInicial=obtenerEfectivoAlinicioDelDia();
        efectivoInicialLabel.setText("$ "+efectivoInicial);
        String stotalBalance=labelBalanceDiaCorte.getText();
        Double totalVentasDia=0.0;
        if(stotalBalance.equals("$ 0.0")){
            labelBalanceDiaCorte.setText("$ 0.0");
        }else{
            labelBalanceDiaCorte.setText(totalBalance.getText());
            String resultado=labelBalanceDiaCorte.getText();
            resultado=resultado.replace("$", "");
            resultado=resultado.trim();
            totalVentasDia=Double.parseDouble(resultado);
        }
        
        //totalCoins.setText("$ "+efectivoInicial+totalVentasDia);
        totalCoins.setText("$ 0.0");
    }//GEN-LAST:event_jButton1ActionPerformed

    public void obtenerGastosDelDia(String fi,String ff){
               
          String sqlString="select consecutivo,cantidad,descripcion as motivo,NombreCompleto as 'retiró' from " +
                           " retiro a inner join usuarios b on a.usuario=b.idUsuario   where (a.fecha BETWEEN '"+fi+"' AND '"+ff+"')";
          System.out.println(sqlString);
          DBConect conexion=new DBConect();  

          Connection conexionMysql=conexion.GetConnection();
      
      try{
          
        Statement statement = conexionMysql.createStatement();
        modeloRetiro= new DefaultTableModel();
        ResultSet rs = statement.executeQuery(sqlString);
        
        Double totalGastoD=0.0;
        while(rs.next()){
            totalGastoD+=rs.getDouble("cantidad");
        }
        rs.beforeFirst();
        totalGasto.setText("$ "+totalGastoD);
        Double pretotal=calcularTotalVentasCaja(fi,ff);
        Double operacionBalance=pretotal-totalGastoD;
        totalBalance.setText("$ "+operacionBalance);
        
        ResultSetMetaData rsMd = rs.getMetaData();
        //La cantidad de columnas que tiene la consulta
        int cantidadColumnas = rsMd.getColumnCount();
        //Establecer como cabezeras el nombre de las colimnas
        for (int i = 1; i <= cantidadColumnas; i++) {
         modeloRetiro.addColumn(rsMd.getColumnLabel(i));
        }
        //Creando las filas para el JTable
        while (rs.next()) {
               Object[] fila = new Object[cantidadColumnas];
               for (int i = 0; i < cantidadColumnas; i++) {
                 fila[i]=rs.getObject(i+1);
               }
               modeloRetiro.addRow(fila);
        }
        listadoRetiro.setModel(modeloRetiro);
        
        
      }catch(Exception e){
          e.printStackTrace();
      }
    }
    
    
    public Double calcularTotalVentasCaja(String fi,String ff){
        String fechaInicial=fi;
        String fechaFinal=ff;
        DBConect conexion=new DBConect();
                    Double sumaTotalPeriodo=0.0;
        try{
            Connection conexionMysql = conexion.GetConnection();

            Statement statement = conexionMysql.createStatement();
            DateEditor de=(DateEditor)jSpinner1.getEditor();
            DateEditor de2=(DateEditor)jSpinner2.getEditor();
            String tiempoInicio=fechaInicial+" "+de.getFormat().format(jSpinner1.getValue());
            String tiempoFin=fechaFinal+" "+de2.getFormat().format(jSpinner2.getValue());
            
            
            String SQLString="SELECT a.idVenta as 'No. de venta',a.total,a.fechaVenta,b.descripcionProd,b.precioTotal,b.cantidad as 'Cantidad vendida', "+
                             " b.precioTotal*b.cantidad as 'Subtotal',c.NombreCompleto as 'Vendido por', "+
                             " c.NombreUsuario FROM venta as a inner join detalleventa b on a.idVenta=b.Venta_idVenta "+
                             " inner join usuarios as c on c.idUsuario=a.usuarios_idUsuario "+
                             " where a.fechaVenta BETWEEN '"+tiempoInicio+"' AND '"+tiempoFin+"' ";
            
            String usuarioSeleccionado=listaTodosLosUsuarios.getSelectedItem().toString();
            if(usuarioSeleccionado.equals("---")){        
                 SQLString+=" order by a.idventa,b.consecutivoVenta";
            }else{
                
                 SQLString+=" and c.NombreCompleto='"+usuarioSeleccionado+"' ";
                
                 SQLString+=" order by a.idventa,b.consecutivoVenta";
                 
            }        
           
            
            ResultSet rs= statement.executeQuery(SQLString);
            

            String nombreUsuario="---";
            Double sumaIvaPeriodo=0.0;
            while(rs.next()){
               int cantidad=rs.getInt("Cantidad vendida");
                if(cantidad>1){
                    sumaTotalPeriodo+=(rs.getDouble("precioTotal")*cantidad);
                    sumaIvaPeriodo+=(rs.getDouble("precioTotal")*cantidad)*ivaConfigurado;
                }else{
                    sumaTotalPeriodo+=rs.getDouble("precioTotal");
                    sumaIvaPeriodo+=rs.getDouble("precioTotal")*ivaConfigurado;
                } 
                
//                int cantidad=rs.getInt("cantidad");
//                if(cantidad>1){
//                     sumaTotalPeriodo+=(rs.getDouble("precioTotal")*cantidad);
//                    
//                }else{
//                    sumaTotalPeriodo+=rs.getDouble("precioTotal");
//                }
//                
//                nombreUsuario=rs.getString("Vendido por");
//                sumaIvaPeriodo+=rs.getDouble("precioTotal")*ivaConfigurado;
            }
            
            totalVenta.setText("$ "+sumaTotalPeriodo);
            
            
        }catch(Exception e){
        
        }
        return sumaTotalPeriodo;
    }
    
    private Double obtenerEfectivoAlinicioDelDia(){
        
        DBConect conexion=new DBConect(); 
        Connection conexionMysql = conexion.GetConnection();
         Double inicioDia=0.0;
        /////obtenemos el maximo ingresado en la caja
         String sqlString="Select max(idCaja),inicioDelDia,finalDelDia,fecha from caja ";
        try{
         
         Statement statement=conexionMysql.createStatement();
         ResultSet rs2 = statement.executeQuery(sqlString);
         SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        
            while(rs2.next()){
                inicioDia=rs2.getDouble(2);
            }
        
        }catch(Exception e){
            e.printStackTrace();
        } 
        
        return inicioDia;
    }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        
        int gastos=listadoRetiro.getRowCount();
        
        if(gastos==0){
            JOptionPane.showMessageDialog(null,"Si no hubo gastos por favor agregue un gasto con cantidad CERO\nparapoder realizar el corte de caja.");
            return;
        }
        
        Double totalVentaDelDia=0.0;
        
        String valorBalance=totalBalance.getText().replace("$", "");
        valorBalance=valorBalance.trim();
        if(!valorBalance.equals("---") ){
            totalVentaDelDia=Double.parseDouble(valorBalance);
        }
        
        Double totalVentaEnCajaInicio=0.0;
        String valorInicio=efectivoInicialLabel.getText().replace("$", "");
        valorInicio=valorInicio.trim();
        if(!valorInicio.equals("---") ){
            totalVentaEnCajaInicio=Double.parseDouble(valorInicio);
        }
        
        Double totalBalanceMonedas=0.0;
        String valorBalanceMonedas=totalCoins.getText().replace("$", "");
        valorBalanceMonedas=valorBalanceMonedas.trim();
        if(!valorBalanceMonedas.equals("---")){
            totalBalanceMonedas=Double.parseDouble(valorBalanceMonedas);
        }
        
        Double totalArqueo=totalVentaEnCajaInicio+totalVentaDelDia;
        
        if(totalBalanceMonedas<totalArqueo){
            JOptionPane.showMessageDialog(null,"El balance de efectivo es menor al balance del día\nverifique la distribución de billetes y monedas.");
            return;
        } 
        
        ConfirmarCorte v=new ConfirmarCorte(totalCoins.getText(),this.usuarioSesion,this.passSesion,efectivoInicialLabel.getText(),totalBalance.getText());
        v.setTitle("Confirmación corte de caja");
        v.setVisible(true);
        v.toFront();

//        ///realizando corte de caja
//        int gastos=listadoRetiro.getRowCount();
//        
//        if(gastos==0){
//            JOptionPane.showMessageDialog(null,"Si no hubo gastos por favor agregue un gasto con cantidad CERO\nparapoder realizar el corte de caja.");
//            return;
//        }
//        
//        DBConect conexion=new DBConect(); 
//        Connection conexionMysql = conexion.GetConnection();
//        
//        /////obtenemos el maximo ingresado en la caja
//         String sqlString="Select max(idCaja),inicioDelDia,finalDelDia,fecha from caja ";
//        try{
//         
//         Statement statement=conexionMysql.createStatement();
//         ResultSet rs2 = statement.executeQuery(sqlString);
//         SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//         int idCaja=0;
//            while(rs2.next()){
//                idCaja=rs2.getInt(1);
//            }
//            
//            if(idCaja!=0){        
//                
//                String balance=totalCoins.getText();
//                balance=balance.replace("$", "");
//                balance=balance.trim();
//                
//                String sqlString2="UPDATE CAJA set finalDelDia="+balance+ " WHERE idCaja="+idCaja;
//                statement.executeUpdate(sqlString2);
//                JOptionPane.showMessageDialog(null,"Cantidad en caja al final del día: "+balance);
//            }
//        
//        }catch(Exception e){
//            e.printStackTrace();
//        }        
        
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void pFamiliarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pFamiliarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pFamiliarActionPerformed

    private void comboTipoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTipoProductoActionPerformed
        
    }//GEN-LAST:event_comboTipoProductoActionPerformed

    private void efectivoRecibidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_efectivoRecibidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_efectivoRecibidoActionPerformed

    private void tablaDetalleVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDetalleVentaMouseClicked
            
            
    }//GEN-LAST:event_tablaDetalleVentaMouseClicked

    private void tablaClientesEncontradosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClientesEncontradosMouseClicked
        seleccionGlobalCliente=tablaClientesEncontrados.getSelectedRow()+1;
    }//GEN-LAST:event_tablaClientesEncontradosMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String cantidad=produccionRebanada.getText();
        int cantidadNumerica=0;        
        try{
          
            cantidadNumerica=Integer.parseInt(cantidad);
            
             DBConect conexion=new DBConect(); 
             Connection conexionMysql = conexion.GetConnection();
             Statement statement = conexionMysql.createStatement();
          
          String sqlConsulta="select unidadesEnCaja from productos where descripcion='rebanadas'";
          ResultSet executeQuery = statement.executeQuery(sqlConsulta);
          int cantidadEnInventario=0;
          while(executeQuery.next()){
             cantidadEnInventario=executeQuery.getInt("unidadesEnCaja");
          }
          
          cantidadNumerica=cantidadNumerica+cantidadEnInventario;
             
          String sqlString="Update productos set unidadesEnCaja="+cantidadNumerica
                           +" where descripcion='rebanadas'";
          
          int resultado=statement.executeUpdate(sqlString);
          if(resultado>=1){
                JOptionPane.showMessageDialog(null,"Se agregaron "+(cantidadNumerica-cantidadEnInventario)+" rebanadas más al inventario.");
                produccionRebanada.setText("");
          }else{
                JOptionPane.showMessageDialog(null,"Ocurrio un error al agregar rebanadas a inventario \n Por favor intentalo nuevamente");
          }

        }catch(Exception e){
              JOptionPane.showMessageDialog(null,"El valor proporcionado no es un número");
              e.printStackTrace();
        }
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void consecutivoReimpresionTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_consecutivoReimpresionTicketActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_consecutivoReimpresionTicketActionPerformed

    private void ReimpresionTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReimpresionTicketActionPerformed
        int confirmacion= JOptionPane.showConfirmDialog(null,""
                + "Deseas reimprimir el ticket No. "+consecutivoReimpresionTicket.getText()+" de este día.","Reimpresion de ticket",JOptionPane.YES_NO_OPTION);
       if(confirmacion==JOptionPane.YES_OPTION){
       
        Utils u=new Utils();        
        DBConect conexion=new DBConect();  
        Connection conexionMysql = conexion.GetConnection();
        int venta=0;
        int cliente=0;
        int usuario=0;
        String nombreCliente="";
        String nombreUsuario="";
        
        double efectivoRecib=0.0;
        double cambio=0.0;
        ///obtenemos la Venta del consecutivo proporcionado
        String consec=consecutivoReimpresionTicket.getText();
        
        try{
            
            Statement statement = conexionMysql.createStatement();

            String sqlString="select * from venta where consecutivoVenta="+consec+" and "+u.obtenerBetweenParaConsulta("fechaVenta");

            ResultSet rs2=statement.executeQuery(sqlString);

            while(rs2.next()){
                venta=rs2.getInt("idVenta");
                cliente=rs2.getInt("cliente_idCliente");
                usuario=rs2.getInt("usuarios_idUsuario");
                efectivoRecib=rs2.getDouble("efectivoRecib");
                cambio=rs2.getDouble("cambio");
            }

            if(venta!=0){
                
                nombreCliente=u.obtenerNombreCompletoCliente(cliente);
                nombreUsuario=u.obtenerNombreCompletoUsuarioDelSistema(usuario);
                JTable obtenerDetalleCompra = u.obtenerDetalleCompra(venta);
                String itemsVenta=obtenerProductosParaTicket(obtenerDetalleCompra);
                                
                String direccionCliente="";
                String telefonoCliente="";
                
                Statement statement2 = conexionMysql.createStatement();

                String sqlString2="select * from cliente where idCliente="+cliente;

                ResultSet rs3=statement.executeQuery(sqlString2);
                while(rs3.next()){
                    direccionCliente=rs3.getString("dirección");
                    telefonoCliente=rs3.getString("telefono");
                }
                
                if(nombreCliente.equals("Cliente en local")){
                    
                    direccionCliente="";
                    telefonoCliente="";
                    nombreCliente="";
                    do{
                        nombreCliente=JOptionPane.showInputDialog(null,"¿Cual es el nombre del cliente?");
//                        if(nombreCliente==null){
//                            JOptionPane.showMessageDialog(null,"Has introducido un valor vacio, por favor proporciona el nombre");
//                            nombreCliente="";
//                        }
                    }while(nombreCliente.equals(""));
                }
                
                String preTelefono=""+telefonoCliente;
                        
                        if(!preTelefono.equals("")){
                            telefonoCliente="\nTelefono del cliente:"+telefonoCliente;
                            
                            direccionCliente+=telefonoCliente;
                        }

                        ///damos el formato deseado a la hora y fecha
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                        Date now = new Date();
                        String fecha=sdf.format(now);
                         
                        DefaultTableModel model = (DefaultTableModel) obtenerDetalleCompra.getModel();
                        double granTotal=0.0;
                        
                        for(int i=0;i<model.getRowCount();i++){

                            Double val=(Double)model.getValueAt(i, 3);
                            String cantidad=""+model.getValueAt(i, 1);
                            Double valNumeric=val;
                            Double cantidadNumeric=Double.parseDouble(cantidad);
                            Double numericTotal=valNumeric*cantidadNumeric;
                            granTotal+=numericTotal;
                        }
                       
                        ivaTotal.setText("$"+decimales.format(granTotal*ivaConfigurado));
                        etiquetaGranTotal.setText("$"+decimales.format((granTotal)));
                        codigoBusqueda.setText("");
                        descripcionManual.setText("");
                        cantidadPizza.setText("1");
                        codigoBusqueda.setFocusable(true);
                        //codigoBusqueda.grabFocus(); 
            
                        
                        Double ivaDouble=Double.parseDouble(ivaTotal.getText().replace("$", ""));
                        Double totalDouble=Double.parseDouble(etiquetaGranTotal.getText().replace("$", ""));
                        
                        Double subTotal=totalDouble-ivaDouble;
                        
                        boolean tipoProdVendido=tiposDeProductoAgregados.contains(0);
                        if(tiposDeProductoAgregados.size()==1 && tipoProdVendido==true){
                           ///No imprimimos ticket, solo abrimos la caja 
                           //Ticket t=new Ticket();
                           //t.AbrirCaja();
                           //JOptionPane.showMessageDialog(null,"Abriendo caja por venta de rebanada");
                        }else{
                            ///imprimimos el ticket normalmente
                            
                            boolean tipoProd4=tiposDeProductoAgregados.contains(4);
                            
                            //obtenemos el refresco seleccionado en la interfaz
                            String refresco=jComboBox2.getSelectedItem().toString();
                            if(tipoProd4==true){
                                refresco="";
                            }else{                        
                                refresco="Refresco:"+refresco;
                            }
                            
                            Ticket t=new Ticket(valorSucursal.getText(),valorDireccion.getText(),consec+"",nombreUsuario,fecha,itemsVenta,subTotal+"",ivaTotal.getText(),etiquetaGranTotal.getText(),efectivoRecib+"",cambio+"",nombreCliente,direccionCliente,refresco);
                            imprimirLogo();
                            t.Imprimir();
                            TestCut testCut= new TestCut();
                            testCut.clean();
                            testCut.cortar();
                        }
                
            }else{
                JOptionPane.showMessageDialog(null,"No se encontro una venta con ese consecutivo");
            }    
            
            
        }catch(Exception e){
            e.printStackTrace();                  
        }
         
        etiquetaGranTotal.setText("---");
        efectivoRecibido.setText("");
        cambioTotal.setText("$");
        ivaTotal.setText("---");
                        
        seleccionPizza1.setSelectedIndex(0);
        seleccionPizza2.setSelectedIndex(0);
        seleccionPizza3.setSelectedIndex(0);
        seleccionExtras1.setSelectedIndex(0);
        seleccionExtras2.setSelectedIndex(0);
        agregarUsuarioSinRegistro();
        tiposDeProductoAgregados.clear();      
        consecutivoReimpresionTicket.setText("");
       }
    }//GEN-LAST:event_ReimpresionTicketActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        int dialogResult =JOptionPane.showConfirmDialog(null,"Estas seguro que deseas cancelar una venta?","",JOptionPane.YES_NO_OPTION );
        if(dialogResult==JOptionPane.YES_OPTION){
            Date fechaInit = new Date();
            Date fechaEnd = new Date();
            
            SimpleDateFormat formatoFecha=new SimpleDateFormat("yyyy/MM/dd");   
            String fechaInicial=formatoFecha.format(fechaInit);
            String fechaFinal=formatoFecha.format(fechaEnd);
            DBConect conexion=new DBConect();

            try{
                Connection conexionMysql = conexion.GetConnection();

                Statement statement = conexionMysql.createStatement();
                String tiempoInicio=fechaInicial+" 00:00:00";
                String tiempoFin=fechaFinal+" 23:59:59";


                String SQLString=//"SELECT a.consecutivoVenta as 'No. de venta',a.total,a.fechaVenta,b.descripcionProd,b.cantidad as 'Cantidad vendida', "+
                                "SELECT a.consecutivoVenta as 'No. de venta',a.total,a.fechaVenta,b.descripcionProd,b.precioTotal,b.cantidad as 'Cantidad vendida', "+
                                " b.precioTotal*b.cantidad as 'Subtotal',c.NombreCompleto as 'Vendido por', "+
                                 " c.NombreCompleto as 'Vendido por', "+
                                 " c.NombreUsuario FROM venta as a inner join detalleventa b on a.idVenta=b.Venta_idVenta "+
                                 " inner join usuarios as c on c.idUsuario=a.usuarios_idUsuario "+
                                 " inner join cliente as d on d.idCliente=a.cliente_idcliente"+
                                 " where a.fechaVenta BETWEEN '"+tiempoInicio+"' AND '"+tiempoFin+"' ";

                SQLString+=" order by a.idventa,b.consecutivoVenta";       

                System.out.println(SQLString);

                ResultSet rs= statement.executeQuery(SQLString);

                Double sumaTotalPeriodo=0.0;
                String nombreUsuario="---";
                Double sumaIvaPeriodo=0.0;
                while(rs.next()){

                    Double cantidad=rs.getDouble("Cantidad vendida");
                    if(cantidad>1){
                        sumaTotalPeriodo+=(rs.getDouble("precioTotal")*cantidad);
                        sumaIvaPeriodo+=(rs.getDouble("precioTotal")*cantidad)*ivaConfigurado;
                    }else{
                        sumaTotalPeriodo+=rs.getDouble("precioTotal");
                        sumaIvaPeriodo+=rs.getDouble("precioTotal")*ivaConfigurado;
                    }

                    nombreUsuario=rs.getString("Vendido por");

                }

                if(sumaTotalPeriodo==0.0){
                    JOptionPane.showMessageDialog(null, "No se encontraron registros de venta");
                }

                rs.beforeFirst();

                DefaultTableModel listaDetalleVentasPeriodo= new DefaultTableModel();

                    ResultSetMetaData rsMd = rs.getMetaData();
                    //La cantidad de columnas que tiene la consulta
                    int cantidadColumnas = rsMd.getColumnCount();
                    //Establecer como cabezeras el nombre de las colimnas
                    for (int i = 1; i <= cantidadColumnas; i++) {
                     listaDetalleVentasPeriodo.addColumn(rsMd.getColumnLabel(i));
                    }
                    //Creando las filas para el JTable
                    while (rs.next()) {
                     Object[] fila = new Object[cantidadColumnas];
                     for (int i = 0; i < cantidadColumnas; i++) {
                       fila[i]=rs.getObject(i+1);
                     }
                     listaDetalleVentasPeriodo.addRow(fila);
                    }

                    BuscadorVentas buscador=new BuscadorVentas(listaDetalleVentasPeriodo,this);
                    buscador.setTitle("Ventas del día");
                    buscador.setVisible(true);
                    buscador.toFront();

            }catch(Exception e){
                e.printStackTrace();
            }
        
        }
        
    }//GEN-LAST:event_jButton5ActionPerformed

    public void limpiaNuevoProducto(){
    
         descripcionAltaText.setText("");
         unidadesEnCajaText.setText("");
         precioCompraAltaText.setText("");
         precioVentaAltaText.setText("");
         codigoAltaText.setText("");
         pChica.setText("");
         pGrande.setText("");
         pMediana.setText("");
         pFamiliar.setText("");
    
    }
    
    public void limpiaFiltros(){
    
        descripcionProdFiltro.setText("");
//        codigoProdFiltro.setText("");
    
    }
    
    public void limpiaVenta(){
    
    
    }
    
    public void limpiaNuevoClienteDesdeVenta(){
        
      newNombreVenta.setText("");
      newApellidoPVenta.setText("");
      newApellidoMVenta.setText("");
      newDireccionVenta.setText("");
      newClienteVentaTel.setText("");
    
    }
    public void limpiaNuevoCliente(){
        
        newNombreVenta1.setText("");
        newApellidoPVenta1.setText("");
        newApellidoMVenta1.setText("");
        newDireccionVenta1.setText("");
        newTel2.setText("");
    
    }
    
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
            java.util.logging.Logger.getLogger(Venta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Venta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Venta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Venta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Venta().setVisible(true);
                
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AgregarNuevoUsuario;
    private javax.swing.JButton BuscadorModificacionCliente;
    private javax.swing.JButton BuscarUusariosRegistrados;
    private javax.swing.JButton BusquedaProductosTodo;
    private javax.swing.JButton DesactivarCliente;
    private javax.swing.JButton DesactivarUsuario;
    private javax.swing.JButton GuardarCambiosCliente;
    private javax.swing.JButton ModificarProductosGuardar;
    private javax.swing.JButton ModificarUsuario;
    private javax.swing.JButton MostrarPanelModificacionCliente;
    private javax.swing.JTextField NombreBusqueda2;
    private javax.swing.JButton ReimpresionTicket;
    private javax.swing.JPanel TabUsuarios;
    private javax.swing.JButton actualizarConfiguracion;
    private javax.swing.JButton agregarClienteAGrid;
    private javax.swing.JButton agregarClienteAGrid1;
    private javax.swing.JButton agregarProducto;
    private javax.swing.JTextField apellidoBusqueda2;
    private javax.swing.JTextField apellidoMBusqueda;
    private javax.swing.JTextField apellidoMBusqueda2;
    private javax.swing.JTextField apellidoPBusqueda;
    private javax.swing.JButton botonBusquedaCliente;
    private javax.swing.JButton busquedaManual;
    private javax.swing.JCheckBox c1;
    private javax.swing.JCheckBox c12;
    private javax.swing.JCheckBox c2;
    private javax.swing.JCheckBox c22;
    private javax.swing.JCheckBox c3;
    private javax.swing.JCheckBox c32;
    private javax.swing.JCheckBox c4;
    private javax.swing.JCheckBox c42;
    private javax.swing.JCheckBox c5;
    private javax.swing.JCheckBox c52;
    private javax.swing.JPanel cajaTab;
    private javax.swing.JLabel cambioTotal;
    private javax.swing.JTextField cantidadPizza;
    private javax.swing.JTextField cantidadRetiro;
    private javax.swing.JTextField clienteBusqueda;
    private javax.swing.JTextField codigoAltaText;
    private javax.swing.JTextField codigoBusqueda;
    private javax.swing.JTextField coin1;
    private javax.swing.JTextField coin2;
    private javax.swing.JTextField coin3;
    private javax.swing.JTextField coin4;
    private javax.swing.JTextField coin5;
    private javax.swing.JTextField coin6;
    private javax.swing.JComboBox comboDescuentos;
    private javax.swing.JComboBox comboTipoProducto;
    private javax.swing.JButton confirmarVentaBoton;
    private javax.swing.JTextField consecutivoReimpresionTicket;
    private javax.swing.JTextField descripcionAltaText;
    private javax.swing.JTextField descripcionAltaText1;
    private javax.swing.JTextField descripcionManual;
    private javax.swing.JTextField descripcionProdFiltro;
    private javax.swing.JTextField direccionBusqueda;
    private javax.swing.JButton editarProducto;
    private javax.swing.JLabel efectivoInicialLabel;
    private javax.swing.JTextField efectivoRecibido;
    private javax.swing.JButton eliminarProductoDeVenta;
    private javax.swing.JLabel etiquetaGranTotal;
    public com.toedter.calendar.JDateChooser fechaFin;
    public com.toedter.calendar.JDateChooser fechaInicio;
    private javax.swing.JButton generarReporteVentas;
    private javax.swing.JLabel ivaTotal;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelAgergarCliente;
    private javax.swing.JLabel labelApellidoMC;
    private javax.swing.JLabel labelApellidoMC1;
    private javax.swing.JLabel labelApellidoMC2;
    private javax.swing.JLabel labelApellidoMC3;
    private javax.swing.JLabel labelApellidoPC;
    private javax.swing.JLabel labelApellidoPC1;
    private javax.swing.JLabel labelApellidoPC2;
    private javax.swing.JLabel labelApellidoPC3;
    private javax.swing.JLabel labelBalanceDiaCorte;
    private javax.swing.JLabel labelCliente;
    private javax.swing.JLabel labelCliente1;
    private javax.swing.JLabel labelIVA;
    private javax.swing.JLabel labelNombreC;
    private javax.swing.JLabel labelNombreC1;
    private javax.swing.JLabel labelNombreC2;
    private javax.swing.JLabel labelNombreC3;
    private javax.swing.JLabel labelPizza2;
    private javax.swing.JLabel labelTotalIvaPeriodo;
    private javax.swing.JLabel labelTotalPeriodo;
    private javax.swing.JLabel labelUsuarioQueVendio;
    private javax.swing.JTable listaProductosAModificar;
    private javax.swing.JTable listaReporte;
    private javax.swing.JComboBox listaTodosLosUsuarios;
    private javax.swing.JTable listadoRetiro;
    private javax.swing.JTable listadoUsuariosRegistrados;
    private javax.swing.JTextField motivoRetiro;
    private javax.swing.JTextField newApellidoMVenta;
    private javax.swing.JTextField newApellidoMVenta1;
    private javax.swing.JTextField newApellidoMVenta2;
    private javax.swing.JTextField newApellidoPVenta;
    private javax.swing.JTextField newApellidoPVenta1;
    private javax.swing.JTextField newApellidoPVenta2;
    private javax.swing.JTextField newClienteVentaTel;
    private javax.swing.JTextField newDireccionVenta;
    private javax.swing.JTextField newDireccionVenta1;
    private javax.swing.JTextField newDireccionVenta3;
    private javax.swing.JTextField newNameToAdd;
    private javax.swing.JTextField newNameToAdd2;
    private javax.swing.JTextField newNombreVenta;
    private javax.swing.JTextField newNombreVenta1;
    private javax.swing.JTextField newNombreVenta2;
    private javax.swing.JTextField newPassToAdd;
    private javax.swing.JTextField newPassToAdd2;
    private javax.swing.JTextField newTel2;
    private javax.swing.JTextField newTelVenta4;
    private javax.swing.JTextField newUserToAdd;
    private javax.swing.JTextField newUserToAdd1;
    private javax.swing.JTextField nombreBusqueda;
    private javax.swing.JTextField nombreUsuarioFiltro;
    private javax.swing.JTextField pChica;
    private javax.swing.JTextField pFamiliar;
    private javax.swing.JTextField pGrande;
    private javax.swing.JTextField pMediana;
    private javax.swing.JPanel panelAgregarClienteVenta;
    private javax.swing.JPanel panelAgregarClienteVenta1;
    private javax.swing.JPanel panelAltaGeneral;
    private javax.swing.JPanel panelAltaPizza;
    private javax.swing.JPanel panelBusquedaClientes;
    private javax.swing.JPanel panelModificarCliente;
    private javax.swing.JPanel panelModificarProducto;
    private javax.swing.JPanel panelModificarUsuario;
    private javax.swing.JTextField paperCoin1;
    private javax.swing.JTextField paperCoin2;
    private javax.swing.JTextField paperCoin3;
    private javax.swing.JTextField paperCoin4;
    private javax.swing.JTextField paperCoin5;
    private javax.swing.JTextField paperCoin6;
    private javax.swing.JButton preAgregarCliente;
    private javax.swing.JTextField precioCompraAltaText;
    private javax.swing.JTextField precioCompraAltaText1;
    private javax.swing.JTextField precioVentaAltaText;
    private javax.swing.JTextField precioVentaAltaText1;
    private javax.swing.JComboBox presentacionLista;
    private javax.swing.JComboBox presentacionLista1;
    private javax.swing.JTextField produccionRebanada;
    private javax.swing.JButton removerProducto;
    private javax.swing.JComboBox seleccionExtras1;
    private javax.swing.JComboBox seleccionExtras2;
    private javax.swing.JComboBox seleccionPizza1;
    private javax.swing.JComboBox seleccionPizza2;
    private javax.swing.JComboBox seleccionPizza3;
    private javax.swing.JPanel tabConfiguracion;
    private javax.swing.JPanel tabReportes;
    private javax.swing.JTable tablaClientesEncontrados;
    private javax.swing.JTable tablaClientesEncontradosEdicion;
    private javax.swing.JTable tablaDetalleVenta;
    private javax.swing.JComboBox tamanioPizza;
    private javax.swing.JLabel totalBalance;
    private javax.swing.JLabel totalCoins;
    private javax.swing.JLabel totalGasto;
    private javax.swing.JLabel totalVenta;
    private javax.swing.JComboBox uMedidaLista;
    private javax.swing.JComboBox uMedidaLista1;
    private javax.swing.JTextField unidadesEnCajaText;
    private javax.swing.JTextField unidadesEnCajaText1;
    private javax.swing.JTextField usuarioFiltro;
    private javax.swing.JTextField valorD1;
    private javax.swing.JTextField valorD2;
    private javax.swing.JTextField valorD3;
    private javax.swing.JTextField valorD4;
    private javax.swing.JTextField valorD5;
    private javax.swing.JTextField valorDireccion;
    private javax.swing.JTextField valorIVA;
    private javax.swing.JTextField valorRFC;
    private javax.swing.JTextField valorSlogan;
    private javax.swing.JTextField valorSucursal;
    // End of variables declaration//GEN-END:variables

    @Override
    public void valueChanged(ListSelectionEvent e) {
        
        
		if( e.getSource() == tablaClientesEncontrados.getSelectionModel()
						&& e.getFirstIndex() >= 0 )
		{
			TableModel model = (TableModel)tablaClientesEncontrados.getModel();
			
                        try{
                            if(model.getRowCount()>0){
                                
                                if(tablaClientesEncontrados.getSelectedRowCount()>=1){
                                                                    int x=(int)model.getValueAt(
                                    tablaClientesEncontrados.getSelectedRow(),0);
                                
                                    if(x!=0){

                                        setClienteId(""+x);

                                    }
                                }else{
                                
                                     setClienteId("");
                                }
                                
                            }
                            
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
			
		} 
        
    }

    /**
     * @return the usuarioLogueado
     */
    public int getUsuarioLogueado() {
        return usuarioLogueado;
    }

    /**
     * @param usuarioLogueado the usuarioLogueado to set
     */
    public void setUsuarioLogueado(int usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    /**
     * @return the clienteId
     */
    public String getClienteId() {
        return clienteId;
    }

    /**
     * @param clienteId the clienteId to set
     */
    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    /**
     * @return the productoParaCarrritoDesdeOtraVentana
     */
    public Productos getProductoParaCarrritoDesdeOtraVentana() {
        return productoParaCarrritoDesdeOtraVentana;
    }

    /**
     * @param productoParaCarrritoDesdeOtraVentana the productoParaCarrritoDesdeOtraVentana to set
     */
    public void setProductoParaCarrritoDesdeOtraVentana(Productos productoParaCarrritoDesdeOtraVentana) {
        this.productoParaCarrritoDesdeOtraVentana = productoParaCarrritoDesdeOtraVentana;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        Robot robot =null;
        try{
            robot=new Robot();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        if (e.getKeyCode()==KeyEvent.VK_ENTER){
            // Simulate a key press
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
        }
    
        if (e.getKeyCode()==KeyEvent.VK_LEFT){
            // Simulate a key press
            //robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppv.forms;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import ppv.dbConection.DBConect;

/**
 *
 * @author user2
 */
public class Inicio extends javax.swing.JFrame {

    public String usuarioSesion="";
    public String passSesion="";
    /**
     * Creates new form Inicio
     */
    public Inicio() {
        initComponents();
        setLocationRelativeTo(null);
        this.addKeyListener(new KeyListener(){

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
        });
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
        botonInicioSesion = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        user = new javax.swing.JTextField();
        pass = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inicio de sesión");

        botonInicioSesion.setText("Iniciar sesión");
        botonInicioSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInicioSesionActionPerformed(evt);
            }
        });

        jLabel1.setText("Usuario:");

        jLabel2.setText("Contraseña:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(user, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonInicioSesion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                            .addComponent(pass))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                .addGap(3, 3, 3)
                .addComponent(pass, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonInicioSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ppv/forms/logo.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel3)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonInicioSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInicioSesionActionPerformed
      DBConect conexion=new DBConect();  
      
      try{
        Connection conexionMysql = conexion.GetConnection();
       
         Statement statement = conexionMysql.createStatement();
        Statement statementFI = conexionMysql.createStatement();
        String sqlStringID="Select dataInstalled from installdata";
        ResultSet rsID = statementFI.executeQuery(sqlStringID);
        Date fi=null;
        Date ad=new Date();
        while(rsID.next()){
            fi=(Date)rsID.getDate("dataInstalled");
        }
        if(fi==null){
            JOptionPane.showMessageDialog(null,"Datos de instalación dañados");
            return;
        }
        
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String fechaInicioString = df.format(fi);
        try {
            fi = df.parse(fechaInicioString);
        } catch (ParseException ex) {
        }

        String fechaFinalString = df.format(ad);
        try {
            ad = df.parse(fechaFinalString);
        } catch (ParseException ex) {
        }

        long fechaInicialMs = fi.getTime();
        long fechaFinalMs = ad.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
       
        if(dias>365 && dias>0){
           JOptionPane.showMessageDialog(null,"Estimado usuario, le invitamos a adquirir una nueva licencia. \n"
                   + "LA LICENCIA DEL SISTEMA HA VENCIDO, por favor comuniquese a los siguientes numeros:\n\n"
                   + "Cel. 248-156-156-7 o 248-162-47-47");
           return;
        }
        if(dias==365){
           JOptionPane.showMessageDialog(null,"Estimado usuario, le invitamos a adquirir una nueva licencia. \n"
                   + "La licencia ESTA A UN DÍA DE VENCER, mañana no podra utilizar el sistema.\nPor favor comuniquese a los siguientes numeros:\n\n"
                   + "Cel. 248-156-156-7 o 248-156-156-7");
        }
        
        String userVar=user.getText();
        String passVar=pass.getText();
        this.usuarioSesion=userVar;
        this.passSesion=passVar;
        
        if(userVar.equals("") || passVar.equals("")){
            JOptionPane.showMessageDialog(null,"Es necesario proporcionar usuario y contraseña"); 
            return;
        }
        
        String sqlString="Select * from usuarios where nombreUsuario='"+userVar+"' and password='"+passVar+"'";
        ResultSet rs = statement.executeQuery(sqlString);
        
        int resultados=0;
        int idUsuario=0;
        int c1=0;
        int c2=0;
        int c3=0;
        int c4=0;
        int c5=0;
        String usuario="";
        while(rs.next()){
           resultados++;
           idUsuario=rs.getInt("idusuario");
           c1=rs.getInt("c1");
           c2=rs.getInt("c2");
           c3=rs.getInt("c3");
           c4=rs.getInt("c4");
           c5=rs.getInt("c5");
           usuario=rs.getString("nombreUsuario");
        }
        
        if(resultados>0){
            
            if(c1==1 && c2==1 && c3==1 && c4==1 && c5==1){
                // es un administrador, tiene todos los privilegios.
                //SELECCIONAMOS LOS VALORES DE LA CAJA SI ES QUE EXISTEN.
                String sqlString2="Select max(idCaja) from caja ";
                ResultSet rs2 = statement.executeQuery(sqlString2);
                System.out.println(sqlString2);
                Double inicioDia=0.0;
                Double finDia=0.0;
                Date d;
                int maxID=0;
                String fechaFormateada="";
                Date diaDeHoy=new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String diaDeHoyS=sdf.format(diaDeHoy);
                while(rs2.next()){
                    maxID=rs2.getInt(1);  
                   
                }
                
                ///obtenemos los valores del maximo
                String sqlString4="Select * from caja where idCaja="+maxID;
                ResultSet rs3 = statement.executeQuery(sqlString4);
                while(rs3.next()){
                inicioDia=rs3.getDouble("inicioDelDia"); 
                     finDia=rs3.getDouble("finalDelDia");
                     d=rs3.getDate("fecha");
                     if(d!=null){
                        fechaFormateada=sdf.format(d);
                     }
                }
                
                
                if(fechaFormateada.equals("")){
                     String input="";
                     Double valor=0.0;
                     do{
                         input=JOptionPane.showInputDialog(null,"Bienvenido "+usuario+", es necesario que indique la cantidad con la que se iniciara la caja\n Ejemplo: 500.00","",JOptionPane.INFORMATION_MESSAGE);
                         try {
                            valor=Double.parseDouble(input);
                            String sqlString3="INSERT INTO  caja(inicioDelDia) VALUES("+valor+") ";
                            int insert=statement.executeUpdate(sqlString3);
                        } catch (Exception nfe){
                                JOptionPane.showMessageDialog(null,"El número proporcionado no es valido, debe ser un numero como el siguiente\n 200.00");        
                        }
                     }while(valor==0.0);
                     
                }else{
                    
                    if(diaDeHoyS.equals(fechaFormateada)){
                    
                    }else{
                        int dialogResult =JOptionPane.showConfirmDialog(null,"Bienvenido "+usuario+", la ultima cantidad registrada en la caja es la siguiente:\n Inicio del día: $"+inicioDia+", Final del día: $"+finDia+" con fecha: "+fechaFormateada+" \n Desea modificar la cantidad en caja","",JOptionPane.YES_NO_OPTION );
                        if(dialogResult==JOptionPane.YES_OPTION){
                              String input="";
                             Double valor=0.0;
                             do{
                                 input=JOptionPane.showInputDialog(null,"Indique la cantidad con la que se iniciara la caja");
                                 try {
                                    valor=Double.parseDouble(input);
                                    String sqlString3="INSERT INTO  caja(inicioDelDia) VALUES("+valor+") ";
                                    int insert=statement.executeUpdate(sqlString3);

                                } catch (Exception nfe){
                                        JOptionPane.showMessageDialog(null,"El número proporcionado no es valido, debe ser un numero como el siguiente\nEjemplo: 200.00");        
                                }
                             }while(valor==0.0);
                        }else{
                            String sqlString3="INSERT INTO  caja(inicioDelDia) VALUES("+finDia+") ";
                            int insert=statement.executeUpdate(sqlString3);
                        }
                    } 
                }
            }
            
            Venta v=new Venta(c1,c2,c3,c4,c5,this.usuarioSesion,this.passSesion);
            v.setUsuarioLogueado(idUsuario);
            v.setTitle("Terminal de Punto de venta");
            v.setVisible(true);
            v.toFront();

            this.setVisible(false);
        }else{
      
            JOptionPane.showMessageDialog(null,"El usuario no existe"); 
      
        }
        
        
      }catch(Exception e){
          e.printStackTrace();
      } 
      
    }//GEN-LAST:event_botonInicioSesionActionPerformed

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
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inicio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonInicioSesion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField pass;
    private javax.swing.JTextField user;
    // End of variables declaration//GEN-END:variables
}

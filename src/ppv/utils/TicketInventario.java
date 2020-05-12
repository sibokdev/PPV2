/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppv.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
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
import javax.swing.JOptionPane;
import ppv.forms.Venta;

/**
 *
 * @author user2
 */
public class TicketInventario {
  
    private String contentTicket = "\n{{nameLocal}}\n"
            +"Existencia de productos\n"
            + "---------------------------------------------\n"
            + "Fecha de inventario:{{dateTime}}\n"
            + "Cajero en turno: {{cajero}}\n"
            + "Cant. Descripcion                             \n"
            + "----------------------------------------------\n"
            + "{{items}}\n"
            + "----------------------------------------------\n"
            + "----------------------------------------------\n"
            + "\n           .\n";

    String impresionAbrirCaja= ".";
    public TicketInventario(){
        impresionAbrirCaja=".";
    }
    //El constructor que setea los valores a la instancia
    public TicketInventario(String negocio, String cajero, String dateTime, String items) {
        this.contentTicket = this.contentTicket.replace("{{nameLocal}}", negocio);
        this.contentTicket = this.contentTicket.replace("{{cajero}}", cajero);
        this.contentTicket = this.contentTicket.replace("{{dateTime}}", dateTime);
        this.contentTicket = this.contentTicket.replace("{{items}}", items);
        this.contentTicket= this.contentTicket.replace("ñ", "n");
        this.contentTicket=this.contentTicket.trim();
        System.out.println(contentTicket);
    }

    public void Imprimir() {
        //Especificamos el tipo de dato a imprimir
        //Tipo: bytes; Subtipo: autodetectado
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        //Aca obtenemos el servicio de impresion por defatul
       
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();

        PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
        
        //Creamos un arreglo de tipo byte
        byte[] bytesTicket;
        bytesTicket = this.contentTicket.getBytes();
        Doc doc = new SimpleDoc(bytesTicket, flavor, null);
//        //Creamos un trabajo de impresiÃ³n
        DocPrintJob job = service.createPrintJob();
        //Imprimimos dentro de un try de a huevo
        try {
            //El metodo print imprime
            job.print(doc, null);
        } catch (Exception er) {
            JOptionPane.showMessageDialog(null, "Error al imprimir: " + er.getMessage());
        }
    }
    
    public void AbrirCaja() {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob job = service.createPrintJob();
        //DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        byte[] bytes;
        bytes = this.contentTicket.getBytes();
        SimpleDoc doc = new SimpleDoc(new AbrirCaja(), flavor, null);

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(OrientationRequested.PORTRAIT);
        aset.add(MediaSizeName.INVOICE);
        try {
            job.print(doc,aset);
        } catch (PrintException ex) {
            Logger.getLogger(Venta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
}

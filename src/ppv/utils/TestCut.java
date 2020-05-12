/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppv.utils;

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

/**
 *
 * @author user2
 */
public class TestCut {
        public static void main(String [] args){
     //Especificamos el tipo de dato a imprimir
        //Tipo: bytes; Subtipo: autodetectado
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        //Aca obtenemos el servicio de impresion por defatul
       
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();

        PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
        byte[] PAPER_FULL_CUT = {0x1d, 0x56, 0x00}; // Full cut paper
        //byte[] PAPER_PART_CUT = {0x1d, 0x56, 0x01}; // Partial cut paper
        Doc doc2 = new SimpleDoc(PAPER_FULL_CUT, flavor, null);
        DocPrintJob job2 = service.createPrintJob();
       
//Imprimimos dentro de un try de a huevo
        try {

            job2.print(doc2, null);
            
        } catch (Exception er) {
            JOptionPane.showMessageDialog(null, "Error al imprimir: " + er.getMessage());
        }
    
    }
    public void cortar (){
     //Especificamos el tipo de dato a imprimir
        //Tipo: bytes; Subtipo: autodetectado
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        //Aca obtenemos el servicio de impresion por defatul
       
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();

        PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
        byte[] PAPER_FULL_CUT = {0x1d, 0x56, 0x00}; // Full cut paper
        //byte[] PAPER_PART_CUT = {0x1d, 0x56, 0x01}; // Partial cut paper
        byte[] cutP = new byte[] { 0x1d, 'V', 1 };
        Doc doc2 = new SimpleDoc(PAPER_FULL_CUT, flavor, null);
        DocPrintJob job2 = service.createPrintJob();
       
//Imprimimos dentro de un try de a huevo
        try {

            job2.print(doc2, null);
            
        } catch (Exception er) {
            JOptionPane.showMessageDialog(null, "Error al imprimir: " + er.getMessage());
        }
    
    }
    public void clean() throws PrintException{
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob job = service.createPrintJob();
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        SimpleDoc doc = new SimpleDoc(new MyPrintableReboot(), flavor, null);
    
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(OrientationRequested.PORTRAIT);
        aset.add(MediaSizeName.INVOICE);
        job.print(doc,aset);
    }
}

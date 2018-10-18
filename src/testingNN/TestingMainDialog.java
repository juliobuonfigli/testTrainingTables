package testingNN;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.neuroph.core.NeuralNetwork;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

public class TestingMainDialog extends JFrame implements ActionListener {

	 private JTextField  cEntradas, cSalidas, cFilas;
	 private JLabel eEntradas, eSalidas, eFuente, eRed, eFilas, et1, et2, et3;
	 private int  Porcentaje, Entradas, Salidas, Filas;
	 private JButton bFuente, bAceptar, bRed;
	 private File fF, nn;
	 private Reader rF;
	 private Writer wD;
	 private CSVReader crF;
	 private CSVWriter cwD;
	 private NeuralNetwork NN;
	 private boolean flag=false; 
	
	
	public  TestingMainDialog()  
	{
	setLayout(null);
    setTitle("Prueba de Red Neuronal");
    //Etiquetas
    et1=new JLabel("");
    et1.setBounds(260,10,10,15);
    add(et1);
    et2=new JLabel("");
    et2.setBounds(260,30,10,15);
    add(et2);
    et3=new JLabel("");
    et3.setBounds(260,30,10,15);
    add(et3);
    eFuente=new JLabel("CSV fuente: ");
    eFuente.setBounds(10,10,100,15);
    add(eFuente);
    eRed=new JLabel("Red Neuronal: ");
    eRed.setBounds(10,30,100,15);
    add(eRed);
    eEntradas=new JLabel("Entradas: ");
    eEntradas.setBounds(10,50,140,15);
    add(eEntradas);
    eSalidas=new JLabel("Salidas ");
    eSalidas.setBounds(10,70,100,15);
    add(eSalidas);
    eFilas=new JLabel("Filas ");
    eFilas.setBounds(10,90,100,15);
    add(eFilas);
    // Campos de texto
    cEntradas=new JTextField("6");
    cEntradas.setBounds(160,50,100,15);
    add(cEntradas);
    cSalidas=new JTextField("1");
    cSalidas.setBounds(160,70,100,15);
    add(cSalidas);
    cFilas=new JTextField("100");
    cFilas.setBounds(160,90,100,15);
    add(cFilas);
    // Botones
    bFuente=new JButton("Buscar");
    bFuente.setBounds(160,10,90,15);
    add(bFuente);
    bFuente.addActionListener(this);
    bRed=new JButton("Buscar");
    bRed.setBounds(160,30,90,15);
    add(bRed);
    bRed.addActionListener(this);
    bAceptar=new JButton("Aceptar");
    bAceptar.setBounds(10,120,90,15);
    add(bAceptar);
    bAceptar.addActionListener(this);
	}

public File setArchivo(String titulo, JLabel jl)
	   {
	   JFileChooser chooserCSV = new JFileChooser();
    chooserCSV.setCurrentDirectory(new java.io.File("."));
    chooserCSV.setDialogTitle(titulo);
    chooserCSV.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    chooserCSV.setAcceptAllFileFilterUsed(false);
     //chooserImagen.showOpenDialog(null);
    int status = chooserCSV.showOpenDialog(null);
    File file = chooserCSV.getSelectedFile();                   
	   if(status == JFileChooser.APPROVE_OPTION) 
			jl.setText("*");
	   return file;
	   }

@Override
public void actionPerformed(ActionEvent e) 
		{
     if(e.getSource()==bFuente)
			{
			fF=setArchivo("Cargar CSV fuente", et1);
			try {
	            rF = new FileReader(fF); 
	            //wD = new FileWriter(fF); 
				}
	        catch(IOException c) {
	            System.out.println(c.getMessage()); }
			}
	if(e.getSource()==bRed) 
			{
			nn=setArchivo("Cargar Red Neuronal", et3);
			NN = NeuralNetwork.createFromFile(nn);
			}
		
     if(e.getSource()==bAceptar) 
		{
     	Entradas=Integer.parseInt(cEntradas.getText());
     	Salidas=Integer.parseInt(cSalidas.getText());
     	Filas=Integer.parseInt(cFilas.getText());
     	crF=new CSVReader(rF);
     	String[][] SM=new String[Filas][Entradas+Salidas];
     	double[][] DM=new double[Filas][Entradas+Salidas];
     	String[] SA=new String[Entradas+2*Salidas];
     	double[][] ent=new double[Filas][Entradas];
     	double[] sal=new double[Salidas];
     	for(int j=0; j<Filas; j++)
     		{
     		try{
     			SM[j]=crF.readNext();}
     		catch(IOException c) {
     			System.out.println(c.getMessage()); }
     		for(int i=0; i<Entradas+Salidas; i++)
     			DM[j][i]=Double.parseDouble(SM[j][i]);
     		for(int i=0; i<Entradas; i++)
     			ent[j][i]=DM[j][i];
     		}
     	try{
 			crF.close();}
 		catch(IOException c) {
 			System.out.println(c.getMessage()); }
     	cwD=new CSVWriter(wD);
     	try{
     		wD = new FileWriter(fF); }
        catch(IOException c) {
            System.out.println(c.getMessage()); }
     	for(int j=0; j<Filas; j++)
     		{
     		for(int i=0; i<Entradas+Salidas; i++)
     			SA[i]=SM[j][i];
     		NN.setInput(ent[j]);
    		NN.calculate();
    		sal=NN.getOutput();
    		int x=0;
    		for(int i=Entradas+Salidas; i<Entradas+2*Salidas; i++)
    			{
    			SA[i]=Double.toString(sal[x]);
    			System.out.println(sal[x]);
    			x++;
    			}
    		try{
     			cwD.writeNext(SA); }
	    	catch(Exception ee) {
	    		System.out.println("...esto no camina"); }
     		}
            try{
     		cwD.close();}
		    catch(Exception ee) {
		    	System.out.println("...no funka"); }
     	flag=true;
     	System.exit(0);
        }
     }
}



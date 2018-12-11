import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {
	
	static int variables=36;	//numero de variables
	static int Ciclos=200; //Numero de ciclos 
	
	static Colonia colonia = new Colonia(variables);
	static ArrayList<String> comuna = new ArrayList<String>();
	
	 
	public static void main(String[] args) throws Exception {
		
		crearMatriz();
		colonia.iniciar();
		colonia.memorizarMejorFuente();
		for(int i=0; i<Ciclos; i++){
			colonia.mandarAbejasEmpleadas();
			colonia.calcularProbabilidades();
			colonia.mandarAbejasObservadoras();
			colonia.memorizarMejorFuente();
			colonia.mandarAbejasExploradoras();
		}
		for(int j=0; j<variables; j++){
			String acepta="si";
			if(colonia.GlobalParams[j]==0) acepta="no";
			 System.out.println(comuna.get(j)+": "+acepta+" se acepta colocar una antena en esta comuna.");
		}
		System.out.println("Costo minimo = "+colonia.GlobalMin);
	}
	
	public static void crearMatriz() throws Exception{
			ArrayList<String> restricciones = new ArrayList<String>();
			ArrayList<String> costos = new ArrayList<String>();

	        String cadena;
	        boolean flag =false;
	        
	        FileReader file = new FileReader("datos.txt");
	        BufferedReader buffer = new BufferedReader(file);
	        while((cadena = buffer.readLine())!=null) {
	        	if(cadena.equals("*")){
	        		flag=true;
	        		continue;
		        }
	        	if(flag) costos.add(cadena);
	        	else restricciones.add(cadena);		        	

	        }
	        buffer.close();
	     
	        file = new FileReader("comunas.txt");
	        buffer = new BufferedReader(file);
	        while((cadena = buffer.readLine())!=null) comuna.add(cadena);
	        buffer.close();

	        colonia.datos.llenarMatrizProblemaX(restricciones);
	        colonia.datos.llenarCostos(costos);
	}
	
}


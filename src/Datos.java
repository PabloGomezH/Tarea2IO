import java.util.ArrayList;

public class Datos{

	private int[][] matriz;
	private int numRestricciones;
	private int numVariables;
	private double[] costos;
	
	public Datos(int numRestricciones, int numVariables){
		matriz= new int[numRestricciones][numVariables];
		llenarCeros();
		this.numRestricciones=numRestricciones;
		this.numVariables=numVariables;
		costos = new double[numVariables];
	}
	
	
	
	
	/**
	 * Lleva la matriz de restricciones a una String
	 * @return
	 */
	public String aString(){
		String matrizPalabra = new String();
		for(int i=0; i<numRestricciones; i++){
			matrizPalabra=matrizPalabra+"{";
			for(int j=0; j<numVariables; j++){
				if(j<=(numVariables-2)){
					matrizPalabra=matrizPalabra+matriz[i][j]+" ,";
				}else{
					matrizPalabra=matrizPalabra+matriz[i][j]+" ";
				}
			
			}
			matrizPalabra=matrizPalabra+"},\n";
		}
		return matrizPalabra;
	}
	
	/**
	 * Llena la matriz con las restricciones
	 * Es para la restricciones del problema especifico, ya que este no tiene las varibles continuas
	 * @param restricciones
	 */
	public void llenarMatrizProblemaX(ArrayList<String> restricciones){
		String columna = new String(); //columna de matriz
		int i=0;
		for(int j=0; j<restricciones.size();j++){
			columna = restricciones.get(j);
			
			if(columna.equals("-")){
				i++;
			}else{
				int columnaAux = Integer.parseInt(columna);
					if(columnaAux<=31){
						matriz[i][(columnaAux)-2]=1;
					}
					if(columnaAux>31){
						matriz[i][(columnaAux)-3]=1;
					}
			}
		}
	}
	
	/**
	 * Llena la matriz con las restricciones
	 * @param restricciones
	 */
	public void llenarMatriz(ArrayList<String> restricciones){
		String columna = new String(); //columna de matriz
		int i=0;
		for(int j=0; j<restricciones.size();j++){
			columna = restricciones.get(j);
			//
			if(columna.equals("-")){
				i++;
			}else{
				int columnaAux = Integer.parseInt(columna);
				matriz[i][(columnaAux)-1]=1;
				
			}
		
		}
	}
	
	/**
	 * Llena el vector de costos
	 * @param costos
	 */
	public void llenarCostos(ArrayList<String> costos){
		String columna = new String(); //columna de matriz
		for(int i=0; i<costos.size();i++){
			columna = costos.get(i);
			//System.out.println(columna);
				double costo = Double.parseDouble(columna);
				this.costos[i]=costo;
			}
		
		}
	
	
	/**
	 * Comprueba que la solucion cumpla con las restricciones
	 * @param solucion
	 * @return
	 */
	public boolean comprobarSolucion(int[] solucion){
		int contador =0;
		for(int i=0; i<numRestricciones; i++){
			for(int j=0; j<numVariables; j++){
				if(matriz[i][j]==solucion[j] && solucion[j]!=0){
					contador++;
					break;
				}
			}
		}
		if(contador==numRestricciones){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Calcula el costo de una solucion
	 * @param solucion
	 * @return
	 */
	public double calcularCosto(int[]solucion){
		double costo=0;
		for(int i=0; i<costos.length; i++){
			costo = costo+(costos[i]*solucion[i]);
		}
		return costo;
	}
	
	/**
	 * Llena la Matriz de restricciones con ceros
	 */
	public void llenarCeros(){
		for(int i=0; i<numRestricciones; i++){
			for(int j=0; j<numVariables; j++){
				matriz[i][j]=0;
			}
		}
	}
	
	
}

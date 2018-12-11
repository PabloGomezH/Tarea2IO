
public class AbejaEmpleada {
	
	int param2change;
	int intentos[];
	double valorFO, fitnessSol;
	double fitness[];
	int Alimento[][];
	double f[];
	
	public void mandarAbejasEmpleadas(int fuentes, int dim, int[] solucion, int[][]Alimento, Datos datos, double[] fitness, int[] intentos, double[] f){
		  int i,j,vecino;
		  double aux,r;
		  boolean flag=true;
		  
		   for (i=0;i<fuentes;i++){
			   
			   		//Para que la solucion mutada sea factible
			      	while(flag){
				   		/*El parametro a ser cambiado es elegido de manera aleatoria*/
				        r = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
				        param2change=(int)(r*dim);
				        
				        //Para que la el vecino no sea el mismo que i
				        do{
				        	/*Elige una solucion random para ser usada para producir un mutante de la solucion i*/
				        	r = (   (double)Math.random()*32767 / ((double)(32767)+(double)(1)) );
				        	vecino=(int)(r*fuentes);
					    }while(vecino==i);
					        
				        for(j=0;j<dim;j++) solucion[j]=Alimento[i][j];
		
				        /*v_{ij}=x_{ij}+\phi_{ij}*(x_{kj}-x_{ij}) */
				        r = (   (double)Math.random()*32767 / ((double)(32767)+(double)(1)) );
				        aux =Alimento[i][param2change]+(Alimento[i][param2change]-Alimento[vecino][param2change])*(r-0.5)*2;
				        solucion[param2change]= binarizar(aux);
	
				        if(datos.comprobarSolucion(solucion)) flag=false;
				        
			      	}
			      	
			        valorFO=datos.calcularCosto(solucion);
			        fitnessSol=calcularFitness(valorFO);
			        
			        if (fitnessSol>fitness[i]){
				        /*Si la solucion mutante es mejor que la solucion i, se reemplaza y resetea el contador de intentos*/
				        intentos[i]=0;
				        for(j=0;j<dim;j++) Alimento[i][j]=solucion[j];
				        
				        f[i]=valorFO;
				        fitness[i]=fitnessSol;
			        }
			        else   /*Si la solucion no fue mejorada, se incrementa el contador de intentos*/
			            intentos[i]=intentos[i]+1;
		        }
		   
		   this.fitness=fitness;
		   this.Alimento=Alimento;
		   this.f=f;
		   this.intentos=intentos;
		}
	
	public int binarizar(double num){
		double aux= Math.random();
		if(num> aux) return 1;
		else return 0;
	}

	double calcularFitness(double fun){
		 double result=0;
		 
		 if(fun>=0) result=1/(fun+1);
		 else result=1+Math.abs(fun);
		 
		 return result;
	}

	public int getParam2Change() {
		return param2change;
	}

	public double getValorFO() {
		return valorFO;
	}
	
	public double getFitnessSol() {
		return fitnessSol;
	}

	public double[] getFitness() {
		return fitness;
	}

	public int[][] getAlimento(){
			return Alimento;
		}
	
	public int[] getIntentos() {
		return intentos;
	}
	
	public double[] getF() {
		return f;
	}
}

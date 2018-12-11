
public class Colonia {

		Datos datos;
		int dim =36; //dimension
		int tamColonia= 10; //tamaño de la colonia(abejas empleadas + trabajadoras)
		int numFuentesAlimento= tamColonia/2; 
		int limit = 10; // Una fuente de alimentos que no es mejorada en el limite es abandonada por la abeja empleada
		
	

		int Alimento[][]=new int[numFuentesAlimento][dim];        /*Las fuentes de alimentos*/
		double f[]=new double[numFuentesAlimento];        /*Vector que contiene los valores de la funcion objetivo asociado a cada fuente de alimentos*/
		double fitness[]=new double[numFuentesAlimento];      /*Vector que contiene los valores del fitness asociado a cada fuente de alimento*/
		int intentos[]=new int[numFuentesAlimento];         
		double prob[]=new double[numFuentesAlimento];          /*Vector que contiene la probabilidad de cada fuente de alimento de ser escogida */
		
		int solucion[]=new int[dim];         /*Nueva solucion producida por v_{ij}=x_{ij}+\phi_{ij}*(x_{kj}-x_{ij}) */                
		double valorFO;              /*Valor de la nueva solucion en la funcion objetivo*/
		double fitnessSol;              /*fitness de la nueva solucion*/
		int vecino, param2change;                   /*param2change corresponde a j, vecino corresponde a k en ecuacion v_{ij}=x_{ij}+\phi_{ij}*(x_{kj}-x_{ij})*/


		double GlobalMin;                       /*solucion optima*/
		int GlobalParams[]=new int[dim];  /*parametros de la funcion optima*/                 
		double r; /*random*/

	
		public Colonia( int numVariables){
			datos=new Datos(numVariables,numVariables);	 
		}

		
		
		double calcularFitness(double fun){
			 double result=0;
			 
			 if(fun>=0) result=1/(fun+1);
			 else result=1+Math.abs(fun);
			 
			 return result;
		}
		
		/**
		 * La mejor fuente de alimento es memorizada
		 * Si el valor de la función objetivo asociada a la i-fuente es menor que el
		 * valor global, esta será guardada como el valor global
		 */
		public void memorizarMejorFuente() {
		
			int i,j;
		    
			for(i=0;i<numFuentesAlimento;i++)
				if (f[i]<GlobalMin){
						GlobalMin=f[i];
						for(j=0;j<dim;j++) GlobalParams[j]=Alimento[i][j];
			    }
		 }
		
		/**
		 * Transforma un numero a 0 o 1
		 */
		public int binarizar(double num){
			double aux= Math.random();
			if(num> aux) return 1;
			else return 0;
		}
		
		/**
		 * Inicializa una solucion
		 */
		public void init(int index){
		   int j;
		   boolean flag=true;
		   int aux;
		   while(flag){
			   for (j=0;j<dim;j++){
				   		r = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
				   		aux= binarizar(r);
			        	Alimento[index][j] = aux;
			        	solucion[j]=Alimento[index][j];
			   }
			   //Calculara la solucion hasta que sea valida
			   if(datos.comprobarSolucion(solucion)) flag=false;
		   }	
			f[index]=datos.calcularCosto(solucion); 
			fitness[index]=calcularFitness(f[index]); 
			intentos[index]=0;
		}
		
	
		/**
		 * inicializa las soluciones
		 */
		public void iniciar(){
			int i;
			for(i=0;i<numFuentesAlimento;i++) init(i);
			
			GlobalMin=f[0];
		    for(i=0;i<dim;i++) GlobalParams[i]=Alimento[0][i];

		}

		
		public void mandarAbejasEmpleadas(){
		  int i,j;
		  double aux;
		  boolean flag=true;
		  
		  //Fase de abejas empleadas
		   for (i=0;i<numFuentesAlimento;i++){
			   		//Para que la solucion mutada sea factible
			      	do{
				   		/*El parametro a ser cambiado es elegido randomicamente*/
				        r = ((double) Math.random()*32767 / ((double)(32767)+(double)(1)) );
				        param2change=(int)(r*dim);
				        
				        //Para que la el vecino no sea el mismo que i
				        do{
				        	/*Elige una solucion random para ser usada para producir un mutante de la solucion i*/
				        	r = (   (double)Math.random()*32767 / ((double)(32767)+(double)(1)) );
				        	vecino=(int)(r*numFuentesAlimento);
					    }while(vecino==i);
					        
					   
				        //copia la solucion
				        for(j=0;j<dim;j++) solucion[j]=Alimento[i][j];
		
				        /*v_{ij}=x_{ij}+\phi_{ij}*(x_{kj}-x_{ij}) */
				        r = (   (double)Math.random()*32767 / ((double)(32767)+(double)(1)) );
				        aux =Alimento[i][param2change]+(Alimento[i][param2change]-Alimento[vecino][param2change])*(r-0.5)*2;
				        solucion[param2change]= binarizar(aux);
	
				        if(datos.comprobarSolucion(solucion)) flag=false;
				        
			      	}while(flag);
			      	
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
		}

		/**
		 * Calcula las probabilidades de ser elegidas las fuentes de alimento
		 */
		public void calcularProbabilidades(){
		     int i;
		     double maxfit;
		     maxfit=fitness[0];
		     for (i=1;i<numFuentesAlimento;i++){
		           if (fitness[i]>maxfit)
		           maxfit=fitness[i];
		     }

		     for (i=0;i<numFuentesAlimento;i++) prob[i]=(0.9*(fitness[i]/maxfit))+0.1;
		     

		}

		/**
		 * Manda a las abejas observadoras
		 */
		public void mandarAbejasObservadoras(){

			int i,j,t;
			i=0;
			t=0;
			double aux;
			boolean flag=true;
			
			/*fase abejas observadoras*/
			while(t<numFuentesAlimento){

		        r = (   (double)Math.random()*32767 / ((double)(32767)+(double)(1)) );
		        if(r<prob[i]){ /*elige una fuente de alimento dependiendo de su probabilidad para ser elegida*/
			              
		        	t++;
		        	do{  
				        /*parametro a ser cambiado es elegido randomicamente*/
				        r = ((double)Math.random()*32767 / ((double)(32767)+(double)(1)) );
				        param2change=(int)(r*dim);
				        
				        do{/*elige un vecino a i, que sea distinto a este*/
				        r = (   (double)Math.random()*32767 / ((double)(32767)+(double)(1)) );
				        vecino=(int)(r*numFuentesAlimento);
				        }while(vecino==i);
				     
				        for(j=0;j<dim;j++)
				        	solucion[j]=Alimento[i][j];
				        
				        /*v_{ij}=x_{ij}+\phi_{ij}*(x_{kj}-x_{ij}) */
				        r = (   (double)Math.random()*32767 / ((double)(32767)+(double)(1)) );
				        aux =Alimento[i][param2change]+(Alimento[i][param2change]-Alimento[vecino][param2change])*(r-0.5)*2;
				        solucion[param2change]= binarizar(aux);
				        
				        if(datos.comprobarSolucion(solucion)){
				        	flag=false;
				        }
		        	}while(flag);
			
			        valorFO=datos.calcularCosto(solucion);
			        fitnessSol=calcularFitness(valorFO);
			        
			        if (fitnessSol>fitness[i]){
			        
			        	intentos[i]=0;
			        	for(j=0;j<dim;j++)
			        		Alimento[i][j]=solucion[j];
			        	f[i]=valorFO;
			        	fitness[i]=fitnessSol;
			        }
			        else intentos[i]=intentos[i]+1;
			        
		        } 
		        i++;
		     
		        if (i==numFuentesAlimento)
		        	i=0;
			}

		     
		}

	
		/**
		 * Determina que fuentes de alimento exceden el numero de intentos (valor "limit")
		 */
		public void mandarAbejasExploradoras(){
			int maxtrialindex,i;
			maxtrialindex=0;
			for (i=1;i<numFuentesAlimento;i++) 
		         if (intentos[i]>intentos[maxtrialindex])
		        	 maxtrialindex=i;
		        
			if(intentos[maxtrialindex]>=limit) init(maxtrialindex);
		}
	}


		


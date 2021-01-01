
 import edu.princeton.cs.algs4.*;
 import java.util.Scanner;
 import java.io.File;

public class PrimVsKruskal{

	static boolean PrimVsKruskal(double[][] G){
		int n = G.length;
		/* Build the MST by Prim's and the MST by Kruskal's */
		/* (You may add extra methods if necessary) */
   
 EdgeWeightedGraph edgegraph = graph(G);
        int m = n;
      
        Edge[] edgeTo = new Edge[n];
        double[] distTo = new double[n]; 
        boolean[] marked = new boolean[n]; 
        IndexMinPQ<Double> Primpq; 
        double weight;

        for(int x = 0; x < n; x++) {
            distTo[x] = Double.POSITIVE_INFINITY;
        }

     
        Primpq = new IndexMinPQ<Double>(n);
        distTo[0] = 0.0;
        Primpq.insert(0, 0.0);

      
        Queue<Edge> mst = new Queue<Edge>();
		 Queue<Edge> mst1 = new Queue<Edge>();
        MinPQ<Edge> Kruskalpq = new MinPQ<Edge>();
        UF uf;

  
        for(Edge e : edgegraph.edges()) {
            Kruskalpq.insert(e);
        }

 
        uf = new UF(n);

         double primtotal = 0;
		 double kruskaltotal = 0;
		 System.out.println("Prim Tree:");
         while( !Primpq.isEmpty() && mst.size() < n-1) {
                int primDel = Primpq.delMin();
                marked[primDel] = true;

                for (Edge e : edgegraph.adj(primDel)) {
                    int prim1 = e.other(primDel);
                    if (marked[prim1]) continue;
                    if (e.weight() < distTo[prim1]) {
                        edgeTo[prim1] = e;
                        distTo[prim1] = e.weight();
                        if (Primpq.contains(prim1)) Primpq.changeKey(prim1, distTo[prim1]);
                        else Primpq.insert(prim1, distTo[prim1]);
                    }

                }

                if(!Primpq.isEmpty()) {
                    Edge primEdge = edgeTo[Primpq.minIndex()];
                    StdOut.printf("%s\n", primEdge);
                    primDel = primEdge.either();
                    int prim1 = primEdge.other(primDel);
                 primtotal = primtotal + primEdge.weight();
                }

             
         }
		 System.out.println(String.format("%.4f",primtotal));
		 System.out.println(" ");
		 System.out.println("Kruskal Tree:");
		                 while(!Kruskalpq.isEmpty() && mst1.size() < m-1) {
                    Edge kruskalEdge = Kruskalpq.delMin();
                    int left = kruskalEdge.either();
                    int right = kruskalEdge.other(left);
                    if(uf.connected(left, right)) continue;
                    uf.union(left, right);
                    mst1.enqueue(kruskalEdge);
                    StdOut.printf("%s\n", kruskalEdge);
                 kruskaltotal = kruskaltotal + kruskalEdge.weight();
                }
		 System.out.println(String.format("%.4f",kruskaltotal));
       
		
		
		/* Determine if the MST by Prim equals the MST by Kruskal */
		boolean pvk = true;
		 PrimMST prim = new PrimMST(edgegraph);
        KruskalMST kruskal = new KruskalMST(edgegraph);
    
double[][] primArray = new double[n][n];
        for(Edge e : prim.edges()) {
           primArray[e.either()][e.other(e.either())] = e.weight();
           
        }
double[][] kruskalArray = new double[n][n];
        for(Edge e : kruskal.edges()) {
          kruskalArray[e.either()][e.other(e.either())] = e.weight();
            
        }
        for(int i=0; i<n; i++){
      for(int j=0;j<n;j++){
		   if(primArray[i][j]==0){
          if(primArray[j][i]!=0){
            continue;
		  }
		  if(kruskalArray[i][j]>0 ){
            pvk = false;
			break;
		  }
		  if(kruskalArray[j][i]>0){
			   pvk = false;
			break;
		  }
		  else if(kruskalArray[i][j]!=primArray[i][j] && kruskalArray[j][i]!=primArray[i][j] ){
			  pvk = false;
			  break;
		  }
		 
		   }
	  }
		}
		return pvk;

	}
	 public static EdgeWeightedGraph graph(double[][] G) {
        int N = G.length;
        Edge e;
        double weight;

        EdgeWeightedGraph edgegraph = new EdgeWeightedGraph(N);
        for(int i=N-1; i>=0; i--){
      for(int j=N-1; j>=0; j--){
        e = new Edge(i, j, G[i][j]);
        if(G[i][j]>0)
          edgegraph.addEdge(e);
      }
    }
        return edgegraph;
    }
		
	/* main()
	   Contains code to test the PrimVsKruskal function. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below. 
	*/
   public static void main(String[] args) {
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int n = s.nextInt();
		double[][] G = new double[n][n];
		int valuesRead = 0;
		for (int i = 0; i < n && s.hasNextDouble(); i++){
			for (int j = 0; j < n && s.hasNextDouble(); j++){
				G[i][j] = s.nextDouble();
				if (i == j && G[i][j] != 0.0) {
					System.out.printf("Adjacency matrix contains self-loops.\n");
					return;
				}
				if (G[i][j] < 0.0) {
					System.out.printf("Adjacency matrix contains negative values.\n");
					return;
				}
				if (j < i && G[i][j] != G[j][i]) {
					System.out.printf("Adjacency matrix is not symmetric.\n");
					return;
				}
				valuesRead++;
			}
		}
		
		if (valuesRead < n*n){
			System.out.printf("Adjacency matrix for the graph contains too few values.\n");
			return;
		}	
		
        boolean pvk = PrimVsKruskal(G);
        System.out.printf("Does Prim MST = Kruskal MST? %b\n", pvk);
    }
}
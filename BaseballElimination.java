

import edu.princeton.cs.algs4.*;
import java.util.*;
import java.io.File;

public class BaseballElimination{
	
	// We use an ArrayList to keep track of the eliminated teams.
	public ArrayList<String> eliminated = new ArrayList<String>();
	public BaseballElimination(Scanner s){
	int n = s.nextInt(); 
		int[][] edges=new int[n][n]; 
		FlowNetwork flows = new FlowNetwork(0);
		int[] wins=new int[n];
		int[] remain=new int[n];
		String[] teams=new String[n];
		for(int i=0;i<n;i++){
			teams[i]=s.next();
			wins[i]=s.nextInt();
			remain[i]=s.nextInt();
			for(int j=0;j<n;j++){
				edges[i][j]=s.nextInt();
			}
		}
		
		for(int i=0;i<n;i++){
		if(selectTeam(i,edges,wins,remain,flows))
			eliminated.add(teams[i]);
		}
	}
	
	
		public boolean selectTeam(int team,int[][] edges,int[] wins,int []remain,FlowNetwork flow){
		
		for (int j = 0; j < edges.length; j++) {
            if (team!=j && wins[team] + remain[team] < wins[j]) {
              return true;
            } 
		}
 
		ArrayList<Alike> like=new ArrayList<Alike>();
		for(int i=0;i<edges.length;i++){
			for(int j=i+1;j<edges.length;j++){
				if(i!=team&&j!=team && edges[i][j]>0){
					like.add(new Alike(i,j));
				}
			}
			

		}
		
		flow=new FlowNetwork(edges.length+like.size()+2);
		int t = edges.length+like.size()+1; 
        int z = edges.length+like.size();  
        
        for (int i=0; i<edges.length;i++) {
            if (i!= team) {
                FlowEdge sink = new FlowEdge(i,t, wins[team]+remain[team]-wins[i]);
				flow.addEdge(sink);
            }
        }
        for(int i=edges.length;i<like.size()+edges.length;i++){
        	Alike alike=like.get(i-edges.length);
        	FlowEdge flow1 = new FlowEdge(i,alike.either(),Double.POSITIVE_INFINITY);
        	FlowEdge flow2 = new FlowEdge(i,alike.other(),Double.POSITIVE_INFINITY);
        	FlowEdge flow3 = new FlowEdge(z,i,(double)edges[alike.either()][alike.other()]);
			flow.addEdge(flow1);
			flow.addEdge(flow2);
			flow.addEdge(flow3);
			
        }
        FordFulkerson maxflow=new FordFulkerson(flow, z,t);
        int  highestval= 0;
		double val = maxflow.value();

        for (int i=0; i<like.size();i++) {
         	Alike alike=like.get(i);
     		highestval += edges[alike.either()][alike.other()];     	
        }
		return val<highestval;
	}
	public class Alike{
		int x;
		int y; 
		public Alike(int x,int y){
			this.x=x;
			this.y=y;
		}
		public int either(){
			return x;
		}
		public int other(){
			return y;
		}
		
	}

	
		
	/* main()
	   Contains code to test the BaseballElimantion function. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below.
	*/
	public static void main(String[] args){
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
		
		BaseballElimination be = new BaseballElimination(s);		
		
		if (be.eliminated.size() == 0)
			System.out.println("No teams have been eliminated.");
		else
			System.out.println("Teams eliminated: " + be.eliminated);
	}
}
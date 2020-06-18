
// Java implementation of Kosaraju's algorithm to print all SCCs 
import java.io.*; 
import java.util.*; 
import java.util.LinkedList; 
  
// This class represents a directed graph using adjacency list 
// representation 
public class StronglyConnectedComponents 
{ 
	private final DiGraph g;
	private DiGraph gTranspose;
	private ArrayList<LinkedList<Integer>> answer; // Each entry in arraylist contains a linked list of nodes
	public static void main(String[] args) throws FileNotFoundException{
		DiGraph graph = new DiGraph(18);
		graph.addEdge(0, 15);
		graph.addEdge(1, 12);
		graph.addEdge(2, 11);
		graph.addEdge(3, 16);
		graph.addEdge(4, 17);
		graph.addEdge(5, 10);
		graph.addEdge(6, 14);
		graph.addEdge(7, 13);
		graph.addEdge(8, 9);
		graph.addEdge(9, 0);
		graph.addEdge(9, 2);
		graph.addEdge(11, 0);
		graph.addEdge(11, 3);
		graph.addEdge(11, 4);
		graph.addEdge(11, 6);
		graph.addEdge(11, 8);
		graph.addEdge(12, 2);
		graph.addEdge(13, 1);
		graph.addEdge(13, 6);
		graph.addEdge(14, 1);
		graph.addEdge(14, 2);
		graph.addEdge(15, 1);
		graph.addEdge(15, 2);
		graph.addEdge(16, 0);
		graph.addEdge(16, 1);
		graph.addEdge(16, 4);
		graph.addEdge(17, 0);
		graph.addEdge(17, 1);
		graph.addEdge(17, 2);
		graph.addEdge(17, 3);
		StronglyConnectedComponents scc = new StronglyConnectedComponents(graph);
		ArrayList<LinkedList<Integer>> array = scc.getSCCs();
		for(LinkedList<Integer> link: array) {
			for(Integer i:link) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
	}
	public StronglyConnectedComponents(final DiGraph g) {
		this.g = g;
		gTranspose = g.getTranspose();
		answer = new ArrayList<LinkedList<Integer>>();
		printSCCs();
	}
  
    // A recursive function to print DFS starting from v
	//used by transpose of DiGraph
    private void DFSUtil(int v,boolean visited[]) 
    { 
        // Mark the current node as visited and print it 
        visited[v] = true; 
        //System.out.print(v + " "); 
        answer.get(answer.size() - 1).add(v); // add v to last LinkedList in answer ArrayList

        // Recur for all the vertices adjacent to this vertex 
        Iterable<Integer> iterator = gTranspose.adj(v); 
        for(Integer i: iterator) {
        		if(!visited[i]) {
        			DFSUtil(i, visited);
        		}
        }
    } 

  
    private void fillOrder(int v, boolean visited[], Stack<Integer> stack) 
    { 
        // Mark the current node as visited and print it 
        visited[v] = true; 
  
        // Recur for all the vertices adjacent to this vertex 
        Iterable<Integer> iterator = g.adj(v); 
        for(Integer i: iterator) {
	    		if(!visited[i]) {
	    			fillOrder(i, visited, stack);
	    		}
        }  
        // All vertices reachable from v are processed by now, 
        // push v to Stack 
        stack.push(new Integer(v)); 
    } 
  
    // The main function that finds and prints all strongly 
    // connected components 
    private void printSCCs() 
    { 
        Stack<Integer> stack = new Stack<>(); 
  
        // Mark all the vertices as not visited (For first DFS) 
        boolean visited[] = new boolean[g.numVertices()]; 
        for(int i = 0; i < g.numVertices(); i++) 
            visited[i] = false; 
  
        // Fill vertices in stack according to their finishing 
        // times 
        for (int i = 0; i < g.numVertices(); i++) 
            if (visited[i] == false) 
                fillOrder(i, visited, stack); 
  

  
        // Mark all the vertices as not visited (For second DFS) 
        for (int i = 0; i < g.numVertices(); i++) 
            visited[i] = false; 
  
        // Now process all vertices in order defined by Stack 
        while (stack.empty() == false) 
        { 
            // Pop a vertex from stack 
            int v = (int)stack.pop(); 
  
            // Print Strongly connected component of the popped vertex 
            if (visited[v] == false) 
            { 
            		answer.add(new LinkedList<Integer>()); // add a new LinkedList to end of answer ArrayList at each SCC
                DFSUtil(v, visited); 
                //System.out.println(); 
            } 
        }
    }
    public ArrayList<LinkedList<Integer>> getSCCs() {
    		return answer;
    }
   
}
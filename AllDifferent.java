import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class AllDifferent {
	private DiGraph g; //represents copy of digraph passed in to constructor
	//private DiGraph gStandard;
	private int bipartiteseparator; //an integer x that separates bipartite graph from [0, x], [x+1, size-1]
	//the first half [0, x] represents the range of the domain of [x+1, size-1]

	public static void main(String[] args) throws FileNotFoundException{
        DiGraph graph = new DiGraph(18);
        //4 5 6 7
        //0 1 2 3
        /*
        Path path1 = Paths.get("input1.txt");
        Scanner t1scan = new Scanner(path1.toFile());
        while(t1scan.hasNextLine()) {
        		String r = t1scan.nextLine();
        		String[] re = r.split(" ");
        		System.out.println("graph.addEdge(" + re[0] + ", " + re[1] + ");");
        }
        */
        graph.addEdge(9, 2);
        graph.addEdge(10, 1);
        graph.addEdge(10, 3);
        graph.addEdge(10, 6);
        graph.addEdge(11, 8);
        graph.addEdge(12, 0);
        graph.addEdge(12, 1);
        graph.addEdge(12, 4);
        graph.addEdge(13, 1);
        graph.addEdge(13, 4);
        graph.addEdge(14, 0);
        graph.addEdge(14, 4);
        graph.addEdge(15, 5);
        graph.addEdge(16, 1);
        graph.addEdge(16, 6);
        graph.addEdge(16, 7);
        graph.addEdge(17, 0);
        graph.addEdge(17, 1);
        graph.addEdge(17, 3);
        graph.addEdge(17, 4);


        //graph.addEdge(1, 7);


        AllDifferent allpuz = new AllDifferent(graph,8);
        
        allpuz.findMaxMatching();
        /*
        allpuz.prunenumber(14, 3);
        allpuz.prunenumber(9, 1);
        allpuz.prunenumber(15, 0);
        allpuz.prunenumber(15, 2);
        allpuz.prunenumber(15, 6);
        allpuz.prunenumber(17, 1);
        allpuz.prunenumber(17, 3);
        */
        System.out.println(allpuz.g.printEdges());
        /*
        int[][] results = allpuz.prune();
        
        for(int i = 0; i < results.length; i++) {
        		for(int j = 0; j < results.length; j++) {
        			if(results[i][j] == 1) {
        				System.out.println(i + " " + j);
        			}
        		}
        }
        */
        allpuz.prunenumber(14, 0);
        System.out.println(allpuz.g.printEdges());
        //int sol = allpuz.depthsearchMaxMatching(graph, visitnodes, 0, 0);
        
        
        //System.out.println(sol);
	}
	/**
	 * Constructor accepts a Directed Graph that is bipartite as indicated
	 * by the bipartiteseparator. The Graph is partitioned into [0,bipartiteseparator]
	 * and [bipartiteseparator + 1, rest of graph vertices]
	 * @param g Graph that will be copied into this AllDifferent
	 * @param bipartiteseparator the integer that partitions this graph
	 */
	public AllDifferent(final DiGraph g, int bipartiteseparator) {
		this.g = g.getCopy();
		//this.gStandard = g.getCopy();//naive solution to make a copy of g so everytime a prune takes place, maxmatching still works
		if(bipartiteseparator >= g.numVertices() - 1) {
			System.out.println("Error in constructor: bipartiteseparator must be less than number of vertices plus 1");
		}
		this.bipartiteseparator = bipartiteseparator;
	}
	/**
	 * Heper method prints the edges currently in this graph
	 * @return a string containing the edges on each line
	 */
	public String printGraph() {
		return g.printEdges();
	}
	/**
	 * Method to examine but does not allow changes to current graph
	 * @return the current pointer to DiGraph (must be constant and immutable)
	 */
	public final DiGraph getDiGraph() {
		return g;
	}
	/**
	 * 
	 * @return the bipartite separator
	 */
	public int getBipartiteseparator() {
		return bipartiteseparator;
	}
	/**
	 * Finds the maximum matching of an initial graph. Begins by calculating a first
	 * matching and keeps track of which nodes have been reversed. A reversed node
	 * represents that some element in the domain has selected an element in its range
	 * It then goes through the remaining elements in the matching that haven't been selected
	 * and improves them until there is no more matching to improve.
	 */
	public void findMaxMatching() {
		//g = gStandard.getCopy();
		//Reminder: element [0, size-1-x] in reversed corresponds to [x+1, size-1] vertex in graph
		//A true in the domain represents that it was selected
		boolean[] reversed = new boolean[g.numVertices() - 1 - bipartiteseparator];
		for(int i = 0; i < reversed.length; i++) {
			reversed[i] = false;
		}
		firstMatching(reversed);
		boolean changed = true; //flag indicates whether DiGraph g has changed
		while(changed) { //if DiGraph g has changed in previous iteration of while loop
			changed = false; //nothing has changed initially
			for(int i = 0; i< reversed.length; i++) {//iterate through all vertices from [bipartiteseparator+1, size-1]
				if(!reversed[i]) { //if current index hasn't been reversed					
					if(improveMatching(i + bipartiteseparator + 1)) { //improve matching
						reversed[i] = true;
						changed = true; 
					}
				}
			}
		}

	}
	/**
	 * Calculates first matching and matches [x+1, size-1] to [0,x]
	 * The algorithm matches the first available adjacent node from the domain to the range.
	 * If there are no available nodes, then there is no match made and this is indicated
	 * in the reversed array. The value in reversed array persists and is used in 
	 * findMaxMatching function. This function does not guarantee a maximum matching.
	 * @param reversed an array indicating whether this vertex has an arrow pointing to it (i.e. it has been matched)
	 * element [0, size-1-x] in reversed corresponds to [x+1, size-1] vertex in graph
	 * bipartiteseparator is represented as x
	 */
	private void firstMatching(boolean[] reversed) {
		boolean[] selected = new boolean[bipartiteseparator + 1]; //one to one correspondence with [0,x]
		for(int i = 0; i < selected.length; i++) {
			selected[i] = false; //selected represents whether node in [0,x] has been selected by vertex from [x+1, size-1]
		}
		final int[][] graphedges = g.getTwoDimArray();
		for(int i = bipartiteseparator + 1; i < g.numVertices(); i++) { // for all vertices in second half of bipartite graph
			int firstadj = firstadj(i, selected, graphedges); //first adjacent vertex to i
			if(firstadj != -1) {
				g.reverseEdge(i, firstadj); // if there is an adjacent vertex, reverse its direction
				reversed[i - (bipartiteseparator + 1)] = true;
				selected[firstadj] = true;
			}
		}
	}
	/**
	 * searches for a path with an odd length greater than 2
	 * if such a path is found it can be traced backwards 
	 * from the return value using visited array
	 * The recursive function base case is the last node with no neighbors.
	 * Otherwise, it recursively searches the neighbors.
	 * @param g indicates graph to be analyzed
	 * @param visited indicates -1 if not visited or an integer that represents the parent node
	 * @param searchvertex indicates which vertex we are currently at 
	 * @param oddpath indicates whether we are searching for an oddpath (true) or an even path (false)
	 * @return if no path found, return -1, else return end vertex of path
	 */
    private int depthsearchMaxMatching(DiGraph g, int[] visited, int searchvertex, int currentdepth, boolean oddpath){
    		int modulus = 0; //convert oddpath into a 0 or 1 to do modulus operations
    		if(oddpath) {modulus = 1;} 
        ArrayList<Integer> neighbors = g.adj(searchvertex); 
        //this if block only runs if there are no neighbors to current node, meaning that it is a leaf
        if(neighbors.size() == 0 && currentdepth % 2 == modulus) {
    		//return true and stop searching neighbors if legitimate path is found
			return searchvertex;
        }
        for(Integer i:neighbors){ //for every neighbor, assuming there is at least one neighbor
            if(visited[i] == -1){ //if current neighbor hasn't been visited
            		visited[i] = searchvertex; //set visited array value of neighbor to searchvertex
            		int returnvalue = depthsearchMaxMatching(g, visited, i, currentdepth + 1, oddpath);
                if(returnvalue != -1) {
                	//if this path returned true then stop searching neighbors
                		return returnvalue;
                }
            }
        }
        return -1; //returns false if it is a leaf node that does not meet criterion (currentdepth % 2 == 1)
        //returns false if all neighbors (in for block) are visited and have not returned true
    }
    /**
     * improves matching on DiGraph g. If no improvement is found,
     * then it is guaranteed that there are no improvements
     * to be made unless the DiGraph g changes later. If one is 
     * found, searchvertex and all previously "reversed" vertices
     * are now "reversed"
     * It improves a matching after receiving a searchvertex in the domain of
     * the BiPartite Graph. It then searches for odd alternating paths using depthsearchMaxMatching
     * that end in an element in the range. Finally, it reverses the edges and gives us
     * an improved max matching with one extra node in the domain that is matched.
     * @param searchvertex improves matching on DiGraph g for vertex searchvertex
     * @return a boolean indicating whether an improvement was found
     */
	private boolean improveMatching(int searchvertex) {
		int[] visitnodes = new int[g.numVertices()]; //sets up parameter for depthsearchMaxMatching function
        for(int i = 0; i < visitnodes.length; i++) {
        		visitnodes[i] = -1;
        }
        int sol = depthsearchMaxMatching(g, visitnodes, searchvertex, 0, true);
        if(sol == -1) {
        		return false;
        }
        else {
        		//reverse edges returned from depthsearchMaxMatching method
        		int index = sol;
        		while(visitnodes[index] != -1) { //while we haven't reached root node (has no parent)
        			g.reverseEdge(visitnodes[index], index); //reverse edge pointing from parent to child (index)
        			index = visitnodes[index]; //set index to be parent value
        		}
        		return true;
        }
	}
	/**
	 * Helper function for first matching. Finds the first adjacent vertex to v 
	 * that is not in the selected boolean array. The double array contains the 
	 * graphedges to determine the adjacent vertices to v.
	 * @param v vertex to search
	 * @param selected whether a node in the domain [0, x] is selected already
	 * @param graphedges two dimensional array containing all the graph edges (adjacency matrix)
	 * @return first adjacent edge to vertex v or -1 if there are no adjacent edges
	 */
    private int firstadj(int v, final boolean[] selected, final int[][] graphedges) {
		for(int i = 0; i < bipartiteseparator; i++) {//go through double array in row v
			if(!selected[i] && graphedges[v][i] == 1) {
				//if double array has a 1 on that column and node has not been selected, return adjacent edge
				return i;
			}
		}
		return -1;    	
    }

    /**
     * Goes through a series of steps to find all edges in a strongly connected 
     * component of the graph and all even alternating paths on the transpose of
     * the graph starting from an unselected node in the range. The method declares
     * an array with a copy of all the edges in the graph and eliminates all edges
     * in those paths discussed above.
     * @return a two-dimensional array representing the edges to be eliminated
     */
	public int[][] prune(){
		final int[][] garray = g.getTwoDimArray();
		int[][] edgestoprune = new int[g.numVertices()][g.numVertices()];
		for(int i = 0; i < g.numVertices(); i++) {
			for(int j = 0; j < g.numVertices(); j++) {
				edgestoprune[i][j] = garray[i][j];
			}
		}
		/*
		nodes should be next to each other with edges reversed between each of the node pairs
		arrows reversed in scc algorithm.
		each scc is traversed in dfs
		imagine an scc can be redrawn with vertices in a circle with edges pointing in same direction (clockwise, ccw)
		therefore vertices should be traversed in order in a dfs of an scc
		Above logic is incorrect. Set every edge between all vertices in SCC to 0
		 * 
		 */
		StronglyConnectedComponents scc = new StronglyConnectedComponents(g);
		ArrayList<LinkedList<Integer>> link = scc.getSCCs();
		for(LinkedList<Integer> i: link) {
			/*
			for(int j = 0; j < i.size(); j++) {//this block won't run if there are no edges (SCC of size 1)
				if(edgestoprune[i.get(j)][i.get(j-1)] == 1) { //should be 1 according to logic above
					edgestoprune[i.get(j)][i.get(j-1)] = 0; //set it to 0 because an edge in an SCC belongs to a maxmatching
				}
			}
			*/
			for(int j = 0; j < i.size(); j++) {
				int currvert = i.get(j);
				for(Integer sink: i) {
					edgestoprune[currvert][sink] = 0;
				}
			}
		}

		//Find alternating paths step on transpose of graph
		DiGraph graphtranspose = g.getTranspose();
		for(int i = 0; i <= bipartiteseparator; i++) {//Examines all nodes in the range that have no adjacent verticies (meaning it hasn't been selected)
			ArrayList<Integer> adjacents = g.adj(i);
			if(adjacents.size() == 0) {//if max matching vertex from [0,x] has not selected node from [x+1, size-1]
				//do stuff for depthsearch for even alternating paths in graphtranspose
				int[] visited = new int[g.numVertices()];//intialize array for depthsearchMaxMatching parameter
		        for(int j = 0; j < visited.length; j++) {
		        		visited[j] = -1;
		        }
		        	int returnvalue = depthsearchMaxMatching(g, visited, i, 0, false); //even alternating paths from node i
		        	//proccess results from depthsearch
		        if(returnvalue != -1){ //if a path was found
	        		//delete edges returned from depthsearchMaxMatching method in edgestoprune
		        	//because these edges belong to a max matching so they won't be pruned
	        			int index = returnvalue;
	        			while(visited[index] != -1) { //while we haven't reached root node (has no parent)
	        				if(edgestoprune[index][visited[index]] == 1) { 
	        					//remembering that all edges are reversed in this dfs
	        					edgestoprune[index][visited[index]] = 0; //index = child visited[index] = parent
	        					index = visited[index]; //set index to be parent value
	        				}
	        				else {
	        					System.out.println("Error in function prune");
	        				}
	        			}
		        }
			}
		}
		//remove all edges pointing from [0,x] to [x+1,size-1] because they belong to a max matching
		for(int i = 0; i <= bipartiteseparator; i++) {
			for(int j = bipartiteseparator + 1; j < g.numVertices(); j++) {
				edgestoprune[i][j] = 0;
			}
		}
		return edgestoprune;
	}
	
	/**
	 * to propagate constraint from one AllDifferent object to other constraints
	 * removes edge v to w or edge from w to v. Even if the edge exists
	 * in this maximum matching, another constraint has elimated this possibility,
	 * so we need to find a new maximum matching by calling the improveMatching
	 * function after the existing edges is eliminated
	 * @param v source vertex v
	 * @param w sink vertex w
	 * @return a boolean representing whether an edge was actually removed from the graph
	 */
	public boolean prunenumber(int v, int w) {
		//More accurate would be if we remove a edge in the current max matching
		if(g.removeEdge(w, v)) { //if we remove a reversed version, improve the matching
			if(!improveMatching(v)) { //improve matching on v (top value), not w
				System.out.println("Error in prunenumber: edge " + w + " to " + v);
				return false;
			}
			return true;
		}
			
		
		else if(g.removeEdge(v, w)) { //if we remove a current option, no need to improve matching
			return true;
		}
		else {//no edge to remove
			return true;
		}

	}
}
//Sudoku solver make a 3-d array of size 9x9x11 (11th for storing number of 1's in other columns
// 9x9 2-d array with an array of size 11 in each slot. First 10 slots represents possible values
// a 1 or 0 indicates whether this value is possible
//Intialize array with all 1's (except in last column it would be a 10)
//Iterate through unsolved sudoku, removing 1's as neccessary

//create an array of booleans initialized with false (index 0-8 represents rows, 9-17 columns, 18-26 squares) 
//Start with Row 1: Create AllDifferent (and bipartite graph). Call maxmatching and prune
//if prune returns some edges 1. update 3-d array 
//2. call maxmatching and prune on column and row 
//3. remember to update array of booleans
//else continue to next valid (value true in array of bool) row (whatever happens next in index scheme in array of booleans)
//Continue iterating from 0-26 until nothing is pruned -> 3-d array should contain final solution

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author andyshao
 */
import java.util.*;
public class DiGraph {
    private int v;
    private int numedges;
    private int[][] fullgraph;//adjacency matrix
    public DiGraph(int v){
        this.v=v;
        fullgraph = new int[v][v];
        this.numedges=0;
        for(int i=0; i<v;i++){
            for(int j = 0; j < v; j++) {
            		fullgraph[i][j] = 0;
            }
        }
    }
    public static void main(String[] args){
        DiGraph graph = new DiGraph(5);
        graph.addEdge(1, 0);
        graph.addEdge(0, 2);
        graph.addEdge(2, 1);
        graph.addEdge(0, 3);
        graph.addEdge(3, 4);
        graph.reverseEdge(3, 4);
        DiGraph graphtranspose = graph.getCopy();
        System.out.println(graph.printEdges());
        System.out.println(graphtranspose.printEdges());
        
        StronglyConnectedComponents scc = new StronglyConnectedComponents(graph);
        ArrayList<LinkedList<Integer>> list = scc.getSCCs();
        for(LinkedList<Integer> i: list) {
        		for(Integer j: i) {
        			System.out.print(j + " ");
        		}
        		System.out.println();
        }
        
    }
    /**
     * Adds edge pointing from v to w in digraph
     * @param v source vertex
     * @param w sink vertex
     */    
    public void addEdge(int v, int w){
        fullgraph[v][w] = 1; //fullgraph[v][w] denotes an edge pointing from vertex v to w
        numedges++;
    }
    /**
     * Removes edge pointing from v to w in digraph
     * @param v source vertex
     * @param w sink vertex
     * @return returns whether edge was removed
     */
    public boolean removeEdge(int v, int w) {
    		if(fullgraph[v][w] == 1) {
	        fullgraph[v][w] = 0; //fullgraph[v][w] denotes an edge pointing from vertex v to w
	        numedges--;//decrement number of edges	
	        return true;
    		}
    		else {
    			return false;
    		}
    }
    /**
     * Reverses edge pointing from v to w in digraph
     * @param v source vertex
     * @param w sink vertex
     * @return returns whether edge was reversed
     */
    public boolean reverseEdge(int v, int w) {
    		if(removeEdge(v, w)) {
    			addEdge(w, v); // add the reverse edge;
    			return true;
    		}
    		else {
    			return false; //no edge to reverse, edge does not exist
    		}
    }
    /**
     * 
     * @param v vertex v
     * @return returns all adjacent vertices to v in Iterable<Integer>
     */
    public ArrayList<Integer> adj(int v){
    		ArrayList<Integer> array = new ArrayList<>();
    		for(int i = 0; i < this.v; i++) {//go through double array in row v
    			if(fullgraph[v][i] == 1) { //if double array has a 1 on that column, then there exists and adjacent edge
    				array.add(i);
    			}
    		}
        return array;
    }

    public final int[][] getTwoDimArray(){
    		return fullgraph;
    }
    /**
     * 
     * @return transpose of current digraph
     */
    public DiGraph getTranspose() 
    { 
        DiGraph g = new DiGraph(v); 
        for (int x = 0; x < v; x++) 
        { 
            // Recur for all the vertices adjacent to this vertex
        		
            Iterable<Integer> neighbors = adj(x); 
            for(Integer i: neighbors) {
            		g.addEdge(i, x);
            }
        } 
        return g; 
    }
    /**
     * 
     * @return copy of current digraph
     */
    public DiGraph getCopy() 
    { 
        DiGraph g = new DiGraph(v); 
        for (int x = 0; x < v; x++) 
        { 
            // Recur for all the vertices adjacent to this vertex
        		
            Iterable<Integer> neighbors = adj(x); 
            for(Integer i: neighbors) {
            		g.addEdge(x, i);
            }
        } 
        return g; 
    }
    public int numEdges(){return numedges;}
    public int numVertices(){return v;}
    public String printEdges() {
    		String fullans = "";
        for (int x = 0; x < v; x++) 
        { 
            // Recur for all the vertices adjacent to this vertex
        		
            Iterable<Integer> neighbors = adj(x); 
            for(Integer i: neighbors) {
            		fullans += x + " " + i + "\n";
            }
        }
        return fullans;
    }
    /*
    private class Edges implements Iterable<Integer>{
        private int num;
        
        Edge root;
        public Edges(){
            this.num=0;
        }
            private class Edge{
                private Edge next;
                private Integer value;
                public Edge(Integer value){
                    this.value = value;
                }
            }
            //iterates through all the Edge values in Edges
            private class EdgeIterator implements Iterator<Integer>{
                Edge current;
                public EdgeIterator(){
                    this.current=root;
                }
                public boolean hasNext(){
                    return current!=null;
                }
                public Integer next(){
                    Edge returnval = current;
                    this.current=current.next;
                    return returnval.value;
                }
            }
        public void setNext(Integer next){           
            Edge newnext = new Edge(next);
            if(root==null){this.root=newnext;}
            else{Edge rootnext = root;
            this.root=newnext;
            this.root.next=rootnext;}
            num++;
        }
        public Iterator<Integer> iterator(){return new EdgeIterator();}
    }
    */
}
    

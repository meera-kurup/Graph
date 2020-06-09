package graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import net.datastructures.Entry;
import support.graph.CS16AdaptableHeapPriorityQueue;
import support.graph.CS16Edge;
import support.graph.CS16GraphVisualizer;
import support.graph.CS16Vertex;
import support.graph.Graph;
import support.graph.MinSpanForest;

/**
 * In this class you will implement a slightly modified version
 * of the Prim-Jarnik algorithm for generating Minimum Spanning trees.
 * The original version of this algorithm will only generate the 
 * minimum spanning tree of the connected vertices in a graph, given
 * a starting vertex. Like Kruskal's, this algorithm can be modified to 
 * produce a minimum spanning forest with very little effort.
 *
 * See the handout for details on Prim-Jarnik's algorithm.
 * Like Kruskal's algorithm this algorithm makes extensive use of 
 * the decorator pattern, so make sure you know it.
 */
public class MyPrimJarnik<V> implements MinSpanForest<V> {

    /** 
     * This method implements Prim-Jarnik's algorithm and extends 
     * it slightly to account for disconnected graphs. You must return 
     * the collection of edges of the Minimum Spanning Forest (MSF) for 
     * the given graph, g.
     * 
     * This algorithm must run in O((|E| + |V|)log(|V|)) time
     * @param g Your graph
     * @param v Only used if you implement the optional animation.
     * @return returns a data structure that contains the edges of your MSF that implements java.util.Collection
     */
    @Override
    public Collection<CS16Edge<V>> genMinSpanForest(Graph<V> g, CS16GraphVisualizer<V> visualizer) {
    	
    	//All decorators handled here
    	//store the cost of a vertex
    	MyDecorator<CS16Vertex<V>, Integer> costDecorator = new MyDecorator<CS16Vertex<V>, Integer>();
    	//stores the previous edge of a vertex
    	MyDecorator<CS16Vertex<V>, CS16Edge<V>> previousDecorator = new MyDecorator<CS16Vertex<V>, CS16Edge<V>>(); 
    	//stores if the edge is already included in MST
    	MyDecorator<CS16Edge<V>, Boolean> inMST = new MyDecorator<CS16Edge<V>, Boolean>();
    	//stores each entry as int and Vertex
    	MyDecorator<CS16Vertex<V>, Entry<Integer, CS16Vertex<V>>> entryDecorator = new MyDecorator<CS16Vertex<V>, Entry<Integer, CS16Vertex<V>>>();
        	
    	//Setting the HeapPriorityQueue and filling the HPQ with each vertex using the g.ierator() 
    	Iterator<CS16Vertex<V>> iterator = g.vertices();
    	CS16AdaptableHeapPriorityQueue<Integer, CS16Vertex<V>> HPQ = new CS16AdaptableHeapPriorityQueue<Integer, CS16Vertex<V>>();
    	//while there is another vertex in the iterator
    	while (iterator.hasNext()) {
    		CS16Vertex<V> next = iterator.next(); //store the vertex
    		costDecorator.setDecoration(next, Integer.MAX_VALUE); //set the decoration of the vertex to max(inf)
    		HPQ.insert(0, next); //insert the vertex in the HPQ with the key 0
    		entryDecorator.setDecoration(next, HPQ.min()); //set the entry decoration to the minimum in the HPQ
    		HPQ.replaceKey(entryDecorator.getDecoration(next), Integer.MAX_VALUE); //replace HPQ decoration key to max(inf)
    	}
    	
    	//loop while the HPQ is not empty
    	while(HPQ.size() != 0) {
    		CS16Vertex<V> v = HPQ.removeMin().getValue(); //take the minimum vertex from HPQ
    		entryDecorator.removeDecoration(v); //remove the entry decoration from vertex 
    		
    		if(previousDecorator.hasDecoration(v)) { //check if previous decorator exists
    			inMST.setDecoration(previousDecorator.getDecoration(v), true); //if so, set that edge exists in MST
    		}
    		Iterator<CS16Edge<V>> incomingEdges = g.incomingEdges(v); //iterator to store all incoming edges at the specific vertex removed
    		
    		while(incomingEdges.hasNext()) { //iterate through incomingEdges
    			CS16Edge<V> edge = incomingEdges.next(); //store the  incoming edge
    			CS16Vertex<V> u = g.opposite(v, edge); //find the opposite edge and store in variable
    				
    			if(costDecorator.getDecoration(u) > edge.element()) { //if opposite's cost is greater than the edge
    				costDecorator.setDecoration(u, edge.element()); //set the decoration to the edge
   					previousDecorator.setDecoration(u, edge); //previous edge is not edge
    				
   					if(entryDecorator.hasDecoration(u)) { //if entry has a decoration
        				HPQ.replaceKey(entryDecorator.getDecoration(u), edge.element()); //replace the HPQ entry with the edge
       				}
    			}
    				
    		}
    	}
  
    	Vector<CS16Edge<V>> MST = new Vector<>(); //create an MST
    	Iterator<CS16Edge<V>> edge = g.edges(); //store all edges of graph
    	
    	while(edge.hasNext()) { //iterate through edges
    		CS16Edge<V> e = edge.next(); //store next edge e
    		
    		if(inMST.hasDecoration(e) && inMST.getDecoration(e)) { //check if edge in MST and its decoration is edge e
    			MST.add(e); //add to MST
    		}
    	}
    	
        return MST; 
    
      }
    }

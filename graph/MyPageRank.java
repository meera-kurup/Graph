package graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import support.graph.CS16Edge;
import support.graph.CS16Vertex;
import support.graph.Graph;
import support.graph.PageRank;

/**
 * In this class you will implement one of many different versions
 * of the PageRank algorithm. This algorithm will only work on
 * directed graphs. Keep in mind that there are many different ways
 * to handle sinks.
 *
 * Make sure you review the help slides and handout for details on
 * the PageRank algorithm.
 *
 */
public class MyPageRank<V> implements PageRank<V> {
	private Graph<V> _g;
	private List<CS16Vertex<V>> _vertices;
	private Map<CS16Vertex<V>, Double> _vertsToRanks;
	private static final double _dampingFactor = 0.85; //DO NOT CHANGE
	private static final int _maxIterations = 100;
	private static final double _error = 0.01; //DO NOT CHANGE

	/**
	 * TODO: Feel free to add in anything else necessary to store the information
	 * needed to calculate the rank. Maybe make something to keep track of sinks,
	 * your ranks, and your outgoing edges?
	 */
	
	private int[] _outgoingEdges; //array out each vertex's outgoing edges
	private double[] _currentRanks; //array out each vertex's current ranks
	private double[] _previousRanks; //array out each vertex's previous ranks
	private int _numberOfPages; //int to store number of total pages/vertices
	private List<Integer> _sinks; //arrayList for all the sinks
	private boolean _converged; //boolean to see if calculations have converged
	private boolean _maxIterationsBool; //boolean to see if reached max 
	private int _maxInterationsNum; // boolean to 

	/**
	 * The main method that does the calculations! You'll want to call the methods
	 * that initialize your variables here. You'll also want to decide on a
	 * type of loop - for loop, do while, or while loop - for your calculations.
	 *
	 * @return A Map of every Vertex to its corresponding rank
	 *
	 */
	@Override
	public Map<CS16Vertex<V>, Double> calcPageRank(Graph<V> g) {
				
		//Instance variables set here
		_g = g;
		_vertices = new ArrayList<CS16Vertex<V>>();
		_numberOfPages = 0;
		_vertsToRanks = new HashMap<CS16Vertex<V>, Double>();
		_sinks = new ArrayList<Integer>();
		_converged = false; //boolean to check if all ranks have little error 
		_maxIterationsBool = false; //boolean to check if reached max iterations
		_maxInterationsNum = 0; //to keep track of total iterations
		
		//adding all vertices to arrayList and calculating total pages
		Iterator<CS16Vertex<V>> iterator = g.vertices();
    	while(iterator.hasNext()) {
    		CS16Vertex<V> next = iterator.next();
    		_vertices.add(next); //add each vertex to vertices arrayList
    		_numberOfPages++; //increment num of Pages 
    	}
    	
    	//set the sizes of all arrays 
    	_outgoingEdges = new int[_numberOfPages]; //stores all outgoing edges of each vertex
    	_currentRanks = new double[_numberOfPages]; //stores current page ranks of each vertex 
    	_previousRanks = new double[_numberOfPages]; //stores previous page ranks of each vertex, updated in loop
    	
    	
    	for(int i = 0; i < _numberOfPages; i++) {
    		_outgoingEdges[i] = g.numOutgoingEdges(_vertices.get(i)); //store outgoing edges of a vertex in array
    		if(_outgoingEdges[i] == 0) { //if a vertex has no outgoing Edges
    			_sinks.add(i); //add to the sinks array
    		}
    		//set all ranks to 1/N(number of pages) 
    		_currentRanks[i] = (double) 1/_numberOfPages;
    		_previousRanks[i] = (double) 1/_numberOfPages;
    	}
   
    	//handles all the sinks in one helper method call
    	this.handleSinks(); 
    	
    	//handles the blacklist of pages
    	this.removeBlackList(); 
    
    	//PageRank only runs when the number of iterations has not gone over the max and
    	//the difference between all values in arrays previous and currentRanks is less 
    	//than or equal to the error 
    	while(!_converged && !_maxIterationsBool) {
    	
    		//updating both current and previous arrays
    		_previousRanks = _currentRanks.clone(); //close currentRanks in preivous ranks
    		for(int i = 0; i < _numberOfPages; i++) {
    			_currentRanks[i] = 0; //set all currentRanks to 0
    		}
    		
    		//iterate through all pages
        	for(int i = 0; i < _numberOfPages; i++) {
        	
        		Iterator<CS16Edge<V>> incomingEdges = g.incomingEdges(_vertices.get(i)); //iterator of all incomingEdges
        		
        		while(incomingEdges.hasNext()) { //iterate through incoming Edges
        			        			
	        		CS16Edge<V> edge = incomingEdges.next(); //store opposite edge
	        		CS16Vertex<V> opposite = g.opposite(_vertices.get(i), edge); //store opposite vertex of incomingEdge
	        		
	        		int oppositeIndex = _vertices.indexOf(opposite); //store index of opposite vertex
	        		
	        		//calculation for current rank, using previous rank and outgoingEdges a the oppositeIndex
	           		_currentRanks[i] += (_dampingFactor * (_previousRanks[oppositeIndex]/_outgoingEdges[oppositeIndex]));
	           	}
        		
        		_currentRanks[i] += dampingCalculation(); //apply damping calculation, called in the helper method
        	}
        	
        	_maxInterationsNum++; //increment maxIterationsNum
    		
        	if(_maxIterations <= _maxInterationsNum++) { //if incrementing maxIterationsNum is greater than man
        		_maxIterationsBool = true; //set the boolean to true for next iteration
        	}
        		
        	_converged = checkConvergence(); //update converged by calling helper method
        }
    	
    	
    	//Getting each vertex and rank and storing into HashMap
    	for(int i = 0; i < _currentRanks.length; i++) { //loop through all vertices
    		_vertsToRanks.put(_vertices.get(i), _currentRanks[i]); //put each vertex and rank into the HasMa
    	}
    	
		return _vertsToRanks; //return HashMap
	}

	/**
	 * Method used to account for sink pages (those with no outgoing
	 * edges). There are multiple ways you can implement this, check
	 * the lecture and help slides!
	 * 
	 * Handles sinks by adding outgoing edges to each sink 
	 */
	private void handleSinks() {

		for(int sink : _sinks) { //loop through all sinks
			for(int i = 0; i < _numberOfPages; i++) { //loop through number of pages
				//insert new edge on graph at location in sink and the vertex with weight 0 
				_g.insertEdge(_vertices.get(sink), _vertices.get(i), 0);
			}
			_outgoingEdges[sink] = _g.getNumVertices();  //updating sinks outgoing edges 
		}

	}
	
	/**
	 * Updates the converged variable to see if the error between the previous and current ranks arrays
	 * have less than the error. 
	 * 
	 * @return boolean if convergence variable needs to change
	 */
	private boolean checkConvergence() {
		for(int i = 0; i < _currentRanks.length; i++) //loop through array
		{
			if(Math.abs(_previousRanks[i]- _currentRanks[i]) > _error) { //if difference between two are greater than error
				return false; //return false
			}
		}
		return true; // if not, return true
	}

	/**
	 * The first part of the damping calculation (1-DF)/V
	 * 
	 * @return a double that does the first part of the damping calculation
	 */
	private double dampingCalculation() {
		return (1 - _dampingFactor) / _numberOfPages;  //return double (1-DF)/V
	}
	
	/**
	 * Implementing removeBlackList by removing incoming edges
	 * of pages that are on the blacklist
	 */
	private void removeBlackList() {
		
		for(int i = 0; i < _vertices.size(); i++) { //loop through all vertices

			if(PageRank.blacklist.contains(_vertices.get(i).getVertexName())) { //see if page is in blacklist
				Iterator<CS16Edge<V>> incomingEdges = _g.incomingEdges(_vertices.get(i)); //iterator of all incomingEdges
				
				while(incomingEdges.hasNext()) { //iterate through incoming Edges
	        		CS16Edge<V> edge = incomingEdges.next(); //store opposite edge
	        		_g.removeEdge(edge); //remove the incoming edge from graph 
				}
			}
		}
	}


}

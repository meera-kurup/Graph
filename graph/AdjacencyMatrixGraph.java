package graph;

import static support.graph.Constants.MAX_VERTICES;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import support.graph.CS16Edge;
import support.graph.CS16Vertex;
import support.graph.Graph;
import support.graph.GraphEdge;
import support.graph.GraphVertex;
import support.graph.DirectionException;
import support.graph.InvalidEdgeException;
import support.graph.InvalidVertexException;
import support.graph.NoSuchEdgeException;
import support.graph.NoSuchVertexException;
import support.graph.NodeSequence;
    
/**
 * This class defines a Graph that tracks its edges through the use of an
 * adjacency matrix. Please review the lecture slides and the book before
 * attempting to write this program. An adjacency matrix consists of a 2D array
 * of Vertices, with each vertex of the graph appearing in both dimensions.
 *
 * Since we are using an adjacency matrix, each vertex must have a 'number', so
 * that it can represent an index in one of the dimensional arrays. This
 * assignment is not as trivial as it may appear. Remember that your arrays have
 * a maximum index. Thus, you cannot just up the number for each vertex. Why
 * not? Think about what happens when you constantly add and delete new
 * vertices. You will soon exceed the size of your adjacency matrix array. Note
 * further that this number must be unique.
 * 
 * Make sure your AdjacencyMatrixGraph can be both directed and undirected!
 *
 * Good luck, and as always, start early, start today, start yesterday!
 */
public class AdjacencyMatrixGraph<V> implements Graph<V> {

    // The underlying data structure of your graph: the adjacency matrix
    private CS16Edge<V>[][] _adjMatrix;
    // Sets to store the vertices and edges of your graph
    private Set<CS16Vertex<V>> _vertices;
    private Set<CS16Edge<V>> _edges;
    //number of vertices
    private int _numVertices;
    // boolean that keeps track of directedness of graph
    private boolean _directed;
    
    //More Instance Variables
    //stack to store all numbers for vertices if used more than once
    private Stack<Integer> _numStack;
    

    /**
     * Constructor for your Graph, where among other things, you will most
     * likely want to instantiate your matrix array and your Sets.
     *
     * Takes in a boolean that represents whether the graph will be directed.
     *
     * This must run in O(1) time.
     */
    public AdjacencyMatrixGraph(boolean directed) {
        _adjMatrix = this.makeEmptyEdgeArray(); //initialize the adjMatrix
        _numVertices = 0;  //set to 0 
        _vertices = new HashSet(); //hashset for vertices
        _edges = new HashSet(); //hashset for edges
        _directed = directed; //set directed variable
        _numStack = new Stack<>(); //stack for vertexNumbers
    }

    /**
     * Returns an iterator holding all the Vertices of the graph.
     *
     * <p>
     * This must run in O(1) time.
     * </p>
     * * Note that the visualizer uses this method to display the graph's
     * vertices, so you should implement it first.
     *
     * @return an Iterator containing the vertices of the Graph.
     */
    @Override
    public Iterator<CS16Vertex<V>> vertices() {
        return _vertices.iterator(); //return iterator of vertices
    }

    /**
     * Returns an iterator holding all the edges of the graph.
     *
     * <p>
     * This must run in O(|1|) time.
     * </p>
     *
     * Note that the visualizer uses this method to display the graph's edges,
     * so you should implement it first.
     *
     * @return an Iterator containing the edges of the Graph.
     */
    @Override
    public Iterator<CS16Edge<V>> edges() {
        return _edges.iterator(); //return iterator of vertices
    }

    /**
     * Inserts a new Vertex into your Graph. You will want to first generate a
     * unique number for your vertex that falls within the range of your
     * adjacency array. You will then have to add the Vertex to your set of
     * vertices.
     *
     * <p>
     * You will not have to worry about the case where *more* than MAX_VERTICES
     * vertices are in your graph. Your code should, however, be able to hold
     * MAX_VERTICES vertices at any time.
     * </p>
     *
     * <p>
     * This must run in O(1) time.
     * </p>
     * 
     * @param vertElement
     *            the element to be added to the graph as a vertex
     */
    @Override
    public CS16Vertex<V> insertVertex(V vertElement) {
    	
    	CS16Vertex<V> v = new GraphVertex<V>(vertElement); //create new GraphVertex
    	
    	//check numStack to see if number from that stack can be used as a vertexNum for new vertex to add
    	if(_numStack.size() != 0) {
    		v.setVertexNumber(_numStack.pop()); //pop from stack to add
    	}
    	else {
    		v.setVertexNumber(_numVertices); //else, set to numVertices
    	}
    	_numVertices++; //increment total
    	
    	_vertices.add(v); //add to set
    	
        return v; //return vertex
    }

    /**
     * Inserts a new Edge into your Graph. You need to update your adjacency
     * matrix to reflect this new added Edge. In addition, the Edge needs to be
     * added to the edge set. 
     *
     * If the graph is directed, you will only want an edge
     * starting from the first vertex ending at the second vertex. If the graph is
     * undirected, you will want an edge both ways.
     * 
     * <p>
     * This must run in O(1) time.
     * </p>
     * 
     * @param v1
     *            The first vertex of the edge connection.
     * @param v2
     *            The second vertex of the edge connection.
     * @param edgeElement
     *            The element of the newly inserted edge.
     * @return Returns the newly inserted Edge.
     * @throws InvalidVertexException
     *             Thrown when either Vertex is null.
     */
    @Override
    public CS16Edge<V> insertEdge(CS16Vertex<V> v1, CS16Vertex<V> v2, Integer edgeElement)
            throws InvalidVertexException {
    	
    	if(v1 == null || v2 == null) { //checks vertices are valid (not null)
			throw new InvalidVertexException("Invalid Vertex as parameter"); //throws exception
		}
    	
    	CS16Edge<V> e = new GraphEdge<V>(edgeElement); //creates a nwe edge
    	
    	e.setVertexOne(v1); //sets the vertex1 of edge to v1
    	e.setVertexTwo(v2); //sets the vertex2 of edge to v2
    	
    	_edges.add(e); //add to set
    	
    	if(_directed) { //if directed
    		//only add one edge in correct location in adj
    		_adjMatrix[v1.getVertexNumber()][v2.getVertexNumber()] = e; 
    	}
    	else {// if undirected
    		//add edge in both locations in adj
    		_adjMatrix[v1.getVertexNumber()][v2.getVertexNumber()] = e;
    		_adjMatrix[v2.getVertexNumber()][v1.getVertexNumber()] = e;
    	}
    
        return e; //return edge
    }

    /**
     * Removes a Vertex from your graph. You will first have to remove all edges
     * that are connected to this Vertex. (Perhaps you can use other methods you
     * will eventually write to make this easier?) Finally, remove the Vertex
     * from the vertex set.
     * <p>
     * This must run in O(|V|) time.
     * </p>
     *
     * @param vert
     *            The Vertex to remove.
     * @return The element of the removed Vertex.
     * @throws InvalidVertexException
     *             Thrown when the Vertex is null.
     */
    @Override
    public V removeVertex(CS16Vertex<V> vert) throws InvalidVertexException {
    	
    	if(vert == null) {//checks vertex is valid (not null)
    		throw new InvalidVertexException("Invalid Vertex being removed");
    	}
    	for(CS16Vertex<V> w: _vertices) { //loop through all vertices
    		if(vert != w) { //if not the same
    			if(this.areAdjacent(vert, w)) { //check if adjacent
        			this.removeEdge(this.connectingEdge(vert, w)); //remove edge in order of checking
        		}
    			if(this.areAdjacent(w, vert)) { //check if adj opposite way
        			this.removeEdge(this.connectingEdge(w, vert));  //remove edge in order of checking
        		}
    		}
    		
    	}
    	
       	_numStack.push(vert.getVertexNumber()); //push the vertex number onto stack
    	_vertices.remove(vert); //remove from stack
    	_numVertices--; //decreasing num of vertices
        return vert.element(); //return vertex element
    }

    /**
     * Removes an Edge from your Graph. You will want to remove all references
     * to it from your adjacency matrix. Don't forget to remove it from the edge
     * set. Make sure to remove only the correct edge if the graph is directed.
     *
     * <p>
     * This must run in O(1) time.
     * </p>
     *
     * @param edge
     *            The Edge to remove.
     * @return The element of the removed Edge.
     * @throws InvalidEdgeException
     *             Thrown when the Edge is null.
     */
    @Override
    public Integer removeEdge(CS16Edge<V> edge) throws InvalidEdgeException {
    	
    	if(edge == null) {  //check if edge is not null
    		throw new InvalidEdgeException("Invalid Edge to remove"); 
    	}

    	int v1 = edge.getVertexOne().getVertexNumber();  //get the vertexNum of first connecting vertex
    	int v2 = edge.getVertexTwo().getVertexNumber(); //get the vertexNum of second connecting vertex

    	
    	if(_directed) { //if directed, set only correct edge to null
    		_adjMatrix[v1][v2] = null;
       	}
    	else { //else undirected, set both edges to null in adjMatrix
    		_adjMatrix[v1][v2] = null;
    		_adjMatrix[v2][v1] = null;
    	}
    	
    	_edges.remove(edge); //remove from set
    	
        return edge.element(); //returning after removing, possible problem?
    }

    /**
     * Returns the edge that connects the two vertices. You will want to consult
     * your adjacency matrix to see if they are connected. If so, return that
     * edge, otherwise throw a NoSuchEdgeException.
     * 
     * If the graph is directed, then two nodes are connected if there is an
     * edge from the first vertex to the second. 
     * If the graph is undirected, then two nodes are connected if there is an
     * edge from the first vertex to the second and vice versa. 
     *
     * <p>
     * This must run in O(1) time.
     * </p>
     *
     * @param v1
     *            The first vertex that may be connected.
     * @param v2
     *            The second vertex that may be connected.
     * @return The edge that connects the first and second vertices.
     * @throws InvalidVertexException
     *             Thrown when either vertex is null.
     * @throws NoSuchEdgeException
     *             Thrown when no edge connects the vertices.
     */
    @Override
    public CS16Edge<V> connectingEdge(CS16Vertex<V> v1, CS16Vertex<V> v2)
            throws InvalidVertexException, NoSuchEdgeException {
    	
    	if(v1 == null || v2 == null) { //check if vertices are null
    		throw new InvalidVertexException("Invalid vertex in parameter");
    	}
    	
    	
    	if(_directed) { //if directed
    		if(this.areAdjacent(v1,v2)) { //check if adj
    			//if so, return the edge at that location
    			return _adjMatrix[v1.getVertexNumber()][v2.getVertexNumber()];
    		}
    		else {
    			throw new NoSuchEdgeException("No Such Edge Exists"); //throw exception since not adjacent
    		}
    	}
    	else { //for undirected
    		if(this.areAdjacent(v1,v2) && this.areAdjacent(v2,v1)) {  //if adj both ways
    			return _adjMatrix[v1.getVertexNumber()][v2.getVertexNumber()]; //return edge at location
    		}
    		else {
    			throw new NoSuchEdgeException("No Such Edge Exists"); //throw exception since not adj
    		}
    	}	
    }

    /**
     * Returns an Iterator over all the Edges that are incoming to this Vertex.
     * <p>
     * This must run in O(|V|) time;
     * </p>
     * 
     *
     * @param vert
     *            The vertex to find the incoming edges on.
     * @return Returns an Iterator holding the incoming edges on v.
     * @throws InvalidVertexException
     *             Thrown when the Vertex is null.
     */
    @Override
    public Iterator<CS16Edge<V>> incomingEdges(CS16Vertex<V> vert) throws InvalidVertexException {
    	
    	if(vert == null) { //check if vertex is null
    		throw new InvalidVertexException("Invalid Vertex in parameter");
    	}
    	
    	List<CS16Edge<V>> listIncoming = new ArrayList<>(); //create list to store edges
    	
    	for(int i = 0; i < MAX_VERTICES; i++) { //loop through adjMatrix
    		if(_adjMatrix[i][vert.getVertexNumber()] != null) { //if edge exists 
    			listIncoming.add(_adjMatrix[i][vert.getVertexNumber()]); //add to list
    		}
    	}
    	
        return listIncoming.iterator(); //return iterator of list
    }
    
    /**
     * Returns an Iterator of all the Edges that are outgoing from this vertex.
     * <p>
     * This must run in O(|V|) time;
     * </p>

     * @param vert
     *            The vertex to find the outgoing edges on.
     * @return Returns an Iterator holding the outgoing edges on v.
     * @throws InvalidVertexException
     *             Thrown when the Vertex is null.
     */
    @Override
    public Iterator<CS16Edge<V>> outgoingEdges(CS16Vertex vert) throws InvalidVertexException {
    	
    	if(vert == null) { //check if vertex is null
    		throw new InvalidVertexException("Invalid Vertex in parameter");
    	}
    	
    	List<CS16Edge<V>> listOutgoing = new ArrayList<>(); //list to store edges
    	
    	for(int i = 0; i < MAX_VERTICES; i++) { //loop through adjMatrix
    		if(_adjMatrix[vert.getVertexNumber()][i] != null) { //if edge exists
    			listOutgoing.add(_adjMatrix[vert.getVertexNumber()][i]); //add to list
    		}
    	}
    
        return listOutgoing.iterator(); //return iterator of list
    }

    /**
     * Returns an int of the number Edges that are leaving from this Vertex. This should only
     * work if called on a directed graph. This method will be used in MyPageRank.
     * 
     * @param vert
     *            The vertex to to find the outgoing edges on.
     * @return an int
     * @throws InvalidVertexException
     *             Thrown when the Vertex is not valid.
     * @throws DirectionException
     *             Thrown when this method is called on an undirected graph.
     */
    @Override
    public int numOutgoingEdges(CS16Vertex<V> vert) throws InvalidVertexException, DirectionException {
        
    	if(!_directed) { //can't call method if directed
    		throw new DirectionException("Graph is not directed!");
    	}
    	if(vert == null) { //check if vertex is null
    		throw new InvalidVertexException("Invalid Vertex in parameter");
    	}
    	
    	int numOutgoingEdges = 0; //counter for outgoingEdges
    	
    	Iterator<CS16Edge<V>> listOutgoing = this.outgoingEdges(vert); //store outgoing edges in a list
    	while(listOutgoing.hasNext()) { //iterate through outgoing edges
    		numOutgoingEdges++; //increment counter
    		listOutgoing.next();
    	}
    	
    	return numOutgoingEdges; //return counter
    }

    /**
     * Returns the Vertex that is on the other side of Edge e opposite of Vertex
     * v. Consulting the adjacency matrix may result in a running time that is
     * too high.
     * 
     * If the edge is not incident on v, then throw a NoSuchVertexException.
     *
     * <p>
     * This must run in O(1) time.
     * </p>
     *
     * @param vert
     *            The first vertex on Edge e.
     * @param edge
     *            The edge connecting Vertex v and the unknown opposite Vertex.
     * @return The opposite Vertex of v across Edge e.
     * @throws InvalidVertexException
     *             Thrown when the Vertex is not valid.
     * @throws InvalidEdgeException
     *             Thrown when the Edge is not valid.
     * @throws NoSuchVertexException
     *             Thrown when Edge e is not incident on v.
     */
    @Override
    public CS16Vertex<V> opposite(CS16Vertex<V> vert, CS16Edge<V> edge)
            throws InvalidVertexException, InvalidEdgeException, NoSuchVertexException {
    	
    	if(vert == null) { //checks if vertex is null
    		throw new InvalidVertexException("Invalid Vertex");
    	}
    	if(edge == null) { //checks if edge is null
    		throw new InvalidEdgeException("Invalid Edge");
    	}
    	
    	CS16Vertex<V> opposite = null; //creates new opposite vertex to return 
    	
    	
    	//checks if edge's vertex is neither vertex1 or vertex2
    	if((edge.getVertexOne() != vert) && (edge.getVertexTwo() != vert)) { 
    		throw new NoSuchVertexException("No such vertex for edge");
    	}
    	
    	if(edge.getVertexOne() != vert) { //if not vertex1, then opposite is vertex2
    		opposite = edge.getVertexOne();
    	}
    	else { //if not vertex2, then opposite is vertex1
    		opposite = edge.getVertexTwo(); 
    	}
    	
        return opposite; //return oppposite vertex
    }

    /**
     * Returns the two Vertices that the Edge e is connected to.
     * 
     * Checking the adjacency matrix may be too costly for this method.
     *
     * <p>
     * This must run in O(1) time.
     * </p>
     * 
     * Note that the visualizer uses this method to display the graph's edges.
     *
     * @param e
     *            The edge to find the connecting Vertex's on.
     * @return a list of Vertex's holding the two connecting vertices.
     * @throws InvalidEdgeException
     *             Thrown when the Edge e is null.
     */
    @Override
    public List<CS16Vertex<V>> endVertices(CS16Edge<V> e) throws InvalidEdgeException {
    	
    	if(e == null) { //checks if edge is null
    		throw new InvalidEdgeException("Invalid edge");
    	}
    	
    	List<CS16Vertex<V>> endVerticesList = new ArrayList<>(); //create arraylist of end vertices
    	
    	endVerticesList.add(e.getVertexOne()); //store the first vertex in the list
    	endVerticesList.add(e.getVertexTwo()); //store the second vertex in the list
    	
        return endVerticesList; //return the list of endVertices
    }

    /**
     * Returns true if there exists an Edge that starts from Vertex v1 and ends
     * at Vertex v2 for both a directed and undirected graph. For a directed graph
     * two vertices are adjacent if there is an edge from the first vertex to the 
     * second vertex.
     * 
     * <p>
     * This must run in O(1) time.
     * </p>
     * 
     * @param v1
     *            The first Vertex to test adjacency.
     * @param v2
     *            The second Vertex to test adjacency.
     * @return Returns true if the vertices are adjacent.
     * @throws InvalidVertexException
     *             Thrown if either vertex is null.
     */
    @Override
    public boolean areAdjacent(CS16Vertex<V> v1, CS16Vertex<V> v2) throws InvalidVertexException {
    	
    	if(v1 == null || v2 == null) { //check if either vertex is null
    		throw new InvalidVertexException("Invalid vertex in parameter");
    	}
    	
    	if(_directed) { //if graph is directed
    		if(_adjMatrix[v1.getVertexNumber()][v2.getVertexNumber()] != null) { // if its not a null edge
    			return true; //return true that they are adjacent
    		}
    	}
    	else {
    		//if both locations in adjMatrix is not null
    		if(_adjMatrix[v1.getVertexNumber()][v2.getVertexNumber()] != null && _adjMatrix[v2.getVertexNumber()][v1.getVertexNumber()] != null) {
        		return true;  //return true that they are adjacent
        	}
    	}
    	return false; //else remove false
    }

    /**
     * Toggles the directedness of the graph.
     */
    @Override
    public void toggleDirected() {
    	if (_directed == true) { //if true, change to false
    		_directed = false;
    	}
    	else {
    		_directed = true; //else, change to true
    	}
    }

    /**
     * Clears all the vertices and edges from the graph. You will want to also
     * clear the adjacency matrix. Remember the power of Java's garbage
     * collection mechanism. If you re-instantiate something, the instance of
     * what that Object used to be disappears.
     *
     * <p>
     * This must run in O(1) time.
     * </p>
     */
    @Override
    public void clear() {
    	
    	_vertices.clear(); //clear vertices hashSet
    	_edges.clear(); //clear edges hashSet
    	_adjMatrix = this.makeEmptyEdgeArray(); //empty 2d array
    	_numVertices = 0; //set num vertices back to 0
    	_numStack.clear();  //clear the numStack
    	
    }

    /**
     * Returns the number of vertices in the graph.
     */
    @Override
	public int getNumVertices() {
		return _numVertices; //return the instance variable for vertices
	}

    // Do not change this method!
    @SuppressWarnings("unchecked")
    private CS16Edge<V>[][] makeEmptyEdgeArray() {
        return new CS16Edge[MAX_VERTICES][MAX_VERTICES];
    }
}

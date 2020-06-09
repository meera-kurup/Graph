package graph;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static support.graph.Constants.MAX_VERTICES;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.datastructures.EmptyTreeException;
import support.graph.CS16Edge;
import support.graph.CS16Vertex;
import support.graph.DirectionException;
import support.graph.Graph;
import support.graph.InvalidEdgeException;
import support.graph.InvalidVertexException;
import support.graph.NoSuchEdgeException;
import support.graph.NoSuchVertexException;

/**
 * This class tests the functionality of a Graph based on a 'String' type
 * parameter for the vertices. Edge elements are Integers. The general framework
 * of a test case is: - Name the test method descriptively, mentioning what is
 * being tested (it is ok to have slightly verbose method names here) - Set-up
 * the program state (ex: instantiate a heap and insert K,V pairs into it) - Use
 * assertions to validate that the program is in the state you expect it to be
 * See header comments over tests for what each test does
 * 
 * Before each test is run, you can assume that the '_graph' variable is reset to
 * a new instance of the a Graph<String> that you can simply use 'as is'
 * via the methods under the 'DO NOT MODIFY ANYTHING BELOW THIS LINE' line.
 * Of course, please do not modify anything below that, or the above 
 * assumptions may be broken.
 */
@RunWith(Parameterized.class)
public class GraphTest {
    

    // Undirected Graph instance variable
    private Graph<String> _graph;
    // Directed Graph instance variable
    private Graph<String> _dirGraph;
    private String _graphClassName;
	
    /**
     * A simple test for insertVertex, that adds 3 vertices and then checks to
     * make sure they were added by accessing them through the vertices
     * iterator.
     */
    
    @Test(timeout = 10000) //insert test on undirected graph 
    public void testInsertVertex() {
        // insert vertices
        CS16Vertex<String> A = _graph.insertVertex("A");
        CS16Vertex<String> B = _graph.insertVertex("B");
        CS16Vertex<String> C = _graph.insertVertex("C");

        // use the vertex iterator to get a list of the vertices in the actual
        // graph
        List<CS16Vertex<String>> actualVertices = new ArrayList<CS16Vertex<String>>();
        Iterator<CS16Vertex<String>> it = _graph.vertices();
        while (it.hasNext()) {
            actualVertices.add(it.next());
        }

        // assert that the graph state is consistent with what you expect
        assertThat(actualVertices.size(), is(3));
        assertThat(actualVertices.contains(A), is(true));
        assertThat(actualVertices.contains(B), is(true));
        assertThat(actualVertices.contains(C), is(true));
    }
    
    //Tests the clear() method on an undirected graph
    
    @Test(timeout = 10000) //clear() method test
    public void clearTest() {
        // insert vertices
        CS16Vertex<String> A = _graph.insertVertex("A");
        CS16Vertex<String> B = _graph.insertVertex("B");
        CS16Vertex<String> C = _graph.insertVertex("C");
        
     // use the edge iterator to get a list of the edges in the actual graph.
        CS16Edge<String> ab = _graph.insertEdge(A, B, 1);
        CS16Edge<String> bc = _graph.insertEdge(B, C, 2);

        // use the edge iterator to get a list of the edges in the actual graph.
        List<CS16Edge<String>> actualEdges = new ArrayList<CS16Edge<String>>();
        Iterator<CS16Edge<String>> edge = _graph.edges();
        while (edge.hasNext()) {
            actualEdges.add(edge.next());
        }

        // use the vertex iterator to get a list of the vertices in the actual
        // graph
        List<CS16Vertex<String>> actualVertices = new ArrayList<CS16Vertex<String>>();
        Iterator<CS16Vertex<String>> it = _graph.vertices();
        while (it.hasNext()) {
            actualVertices.add(it.next());
        }
        
        // assert that the graph state is consistent with what you expect
        assertThat(actualVertices.size(), is(3));
        assertThat(actualEdges.size(), is(2));
        
        _graph.clear(); //clearing graph
        
        CS16Vertex<String> D = _graph.insertVertex("D"); //only one vertex added
       
        actualVertices = new ArrayList<CS16Vertex<String>>();
        it = _graph.vertices();
        while (it.hasNext()) {
            actualVertices.add(it.next());
        }
        
        actualEdges = new ArrayList<CS16Edge<String>>();
        edge = _graph.edges();
        while (edge.hasNext()) {
            actualEdges.add(edge.next());
        }
        
        assertThat(actualVertices.size(), is(1)); //size vertices total is 1
        assertThat(actualEdges.size(), is(0)); //size edges total is 0
        
    }

    //Tests InsertVertex but on a directed Graph
    @Test(timeout = 10000)
    public void testInsertVertexDirected() {
        // insert vertices
        CS16Vertex<String> A = _dirGraph.insertVertex("A");
        CS16Vertex<String> B = _dirGraph.insertVertex("B");
        CS16Vertex<String> C = _dirGraph.insertVertex("C");

        // use the vertex iterator to get a list of the vertices in the actual
        // graph
        List<CS16Vertex<String>> actualVertices = new ArrayList<CS16Vertex<String>>();
        Iterator<CS16Vertex<String>> it = _dirGraph.vertices();
        while (it.hasNext()) {
            actualVertices.add(it.next());
        }

        // assert that the graph state is consistent with what you expect
        assertThat(actualVertices.size(), is(3));
        assertThat(actualVertices.contains(A), is(true));
        assertThat(actualVertices.contains(B), is(true));
        assertThat(actualVertices.contains(C), is(true));
    }


    /**
     * A simple test for insertEdges that adds 3 vertices, adds two edges to the
     * graph and then asserts that both edges were in fact added using the edge
     * iterator as well as checks to make sure their from and to vertices were
     * set correctly.
     */
    //tests insertEdges and vertices on a undirected graph
    @Test(timeout = 10000)
    public void testInsertEdges() {
        CS16Vertex<String> A = _graph.insertVertex("A");
        CS16Vertex<String> B = _graph.insertVertex("B");
        CS16Vertex<String> C = _graph.insertVertex("C");

        // use the edge iterator to get a list of the edges in the actual graph.
        CS16Edge<String> ab = _graph.insertEdge(A, B, 1);
        CS16Edge<String> bc = _graph.insertEdge(B, C, 2);

        // use the edge iterator to get a list of the edges in the actual graph.
        List<CS16Edge<String>> actualEdges = new ArrayList<CS16Edge<String>>();
        Iterator<CS16Edge<String>> it = _graph.edges();
        while (it.hasNext()) {
            actualEdges.add(it.next());
        }

        // assert that the graph state is consistent with what you expect.
        assertThat(actualEdges.size(), is(2));
        assertThat(actualEdges.contains(ab), is(true));
        assertThat(actualEdges.contains(bc), is(true));
    }


    //tests insertEdges and vertices on a directed graph
    @Test(timeout = 10000)
    public void testInsertEdgesDirected() {
        CS16Vertex<String> A = _dirGraph.insertVertex("A");
        CS16Vertex<String> B = _dirGraph.insertVertex("B");
        CS16Vertex<String> C = _dirGraph.insertVertex("C");

        // use the edge iterator to get a list of the edges in the actual graph.
        CS16Edge<String> ab = _dirGraph.insertEdge(A, B, 1);
        CS16Edge<String> bc = _dirGraph.insertEdge(B, C, 2);

        // use the edge iterator to get a list of the edges in the actual graph.
        List<CS16Edge<String>> actualEdges = new ArrayList<CS16Edge<String>>();
        Iterator<CS16Edge<String>> it = _dirGraph.edges();
        while (it.hasNext()) {
            actualEdges.add(it.next());
        }

        // assert that the graph state is consistent with what you expect.
        assertThat(actualEdges.size(), is(2));
        assertThat(actualEdges.contains(ab), is(true));
        assertThat(actualEdges.contains(bc), is(true));
    }
    
    //test removeVertex and removeEdge  on directed graph
    @Test(timeout = 10000)
    public void removeDirected() {
    	
    	CS16Vertex<String> A = _dirGraph.insertVertex("A");
        CS16Vertex<String> B = _dirGraph.insertVertex("B");
        CS16Vertex<String> C = _dirGraph.insertVertex("C");
    	
      
        CS16Edge<String> ab = _dirGraph.insertEdge(A, B, 1);
        CS16Edge<String> ac = _dirGraph.insertEdge(A, C, 5);
       
        _dirGraph.removeEdge(ab); //removeEdge
        
        // use the edge iterator to get a list of the edges in the actual graph.
        List<CS16Edge<String>> actualEdges = new ArrayList<CS16Edge<String>>();
        Iterator<CS16Edge<String>> it = _dirGraph.edges();
        while (it.hasNext()) {
            actualEdges.add(it.next());
        }
        
        // use the vertex iterator to get a list of the vertices in the actual graph
        List<CS16Vertex<String>> actualVertices = new ArrayList<CS16Vertex<String>>();
        Iterator<CS16Vertex<String>> vert = _dirGraph.vertices();
        while (vert.hasNext()) {
            actualVertices.add(vert.next());
        }

        assertThat(actualEdges.contains(ab), is(false));
        assertThat(actualVertices.contains(A), is(true));
        
        _dirGraph.removeVertex(A); //removeVertex
        
        actualVertices = new ArrayList<CS16Vertex<String>>();
        vert = _dirGraph.vertices();
        while (vert.hasNext()) {
            actualVertices.add(vert.next());
        }
        
        actualEdges = new ArrayList<CS16Edge<String>>();
        it = _dirGraph.edges();
        while (it.hasNext()) {
            actualEdges.add(it.next());
        }
        
        assertThat(actualVertices.contains(A), is(false));
        assertThat(actualEdges.contains(ab), is(false));
        assertThat(actualEdges.contains(ac), is(false));
        
    }
    //test removeVertex and removeEdge  on undirected graph
    
    @Test(timeout = 10000)
    public void removeTest() {
    	CS16Vertex<String> A = _graph.insertVertex("A");
        CS16Vertex<String> B = _graph.insertVertex("B");
        CS16Vertex<String> D = _graph.insertVertex("D");
    	
        CS16Edge<String> ab = _graph.insertEdge(A, B, 1);
        CS16Edge<String> bd = _graph.insertEdge(B, D, 3);
        
        _graph.removeVertex(A);
        
        // use the edge iterator to get a list of the edges in the actual graph.
        List<CS16Edge<String>> actualEdges = new ArrayList<CS16Edge<String>>();
        Iterator<CS16Edge<String>> it = _graph.edges();
        while (it.hasNext()) {
            actualEdges.add(it.next());
        }
        
        // use the vertex iterator to get a list of the vertices in the actual graph
        List<CS16Vertex<String>> actualVertices = new ArrayList<CS16Vertex<String>>();
        Iterator<CS16Vertex<String>> vert = _graph.vertices();
        while (vert.hasNext()) {
            actualVertices.add(vert.next());
        }

        assertThat(actualEdges.size(), is(1)); //should be 1 after removing vertex
        assertThat(actualVertices.size(), is (2)); //should be 2 after removing vertex
        assertThat(actualVertices.contains(A), is(false));
        assertThat(actualEdges.contains(ab), is(false));
        
        _graph.removeEdge(bd);
        
        actualVertices =  new ArrayList<CS16Vertex<String>>();
        vert = _graph.vertices();
        while (vert.hasNext()) {
            actualVertices.add(vert.next());
        }
        
        actualEdges =  new ArrayList<CS16Edge<String>>();
        it = _graph.edges();
        while (it.hasNext()) {
            actualEdges.add(it.next());
        }
        
        assertThat(actualEdges.size(), is(0)); //no more edges left
        assertThat(actualEdges.contains(bd), is(false));
        
    }
    
    //test opposite and connectingEdge on undirected graph
    @Test(timeout = 10000)
    public void oppositeAndConnectingTest() {
    	CS16Vertex<String> A = _graph.insertVertex("A");
        CS16Vertex<String> B = _graph.insertVertex("B");
        CS16Vertex<String> C = _graph.insertVertex("C");
    	
      // use the edge iterator to get a list of the edges in the actual graph.
        CS16Edge<String> ab = _graph.insertEdge(A, B, 1);
        CS16Edge<String> bc = _graph.insertEdge(B, C, 2);
        
        assertThat(_graph.opposite(A, ab), is(B));
        assertThat(_graph.connectingEdge(A, B), is(ab));
 
    }
    
    //test opposite and connectingEdge on directed graph
    @Test(timeout = 10000)
    public void oppositeAndConnectingDirectedTest() {
    	//_dirGraph.toggleDirected();
    	
    	CS16Vertex<String> A = _dirGraph.insertVertex("A");
        CS16Vertex<String> B = _dirGraph.insertVertex("B");    	
      // use the edge iterator to get a list of the edges in the actual graph.
        CS16Edge<String> ab = _dirGraph.insertEdge(A, B, 1);
        
        assertThat(_dirGraph.opposite(A, ab), is(B));
        assertThat(_dirGraph.connectingEdge(A, B), is(ab));
 
    }
 
    //test incoming/outgoingEdges from directed graph
    @Test(timeout = 10000)
    public void outgoingIncomingDirected() {
    	CS16Vertex<String> A = _dirGraph.insertVertex("A");
        CS16Vertex<String> B = _dirGraph.insertVertex("B");
        CS16Vertex<String> C = _dirGraph.insertVertex("C");
    	
      // use the edge iterator to get a list of the edges in the actual graph.
        CS16Edge<String> ab = _dirGraph.insertEdge(A, B, 1);
        CS16Edge<String> bc = _dirGraph.insertEdge(B, C, 2);
        CS16Edge<String> ac = _dirGraph.insertEdge(A, C, 3);
        CS16Edge<String> ca = _dirGraph.insertEdge(C, A, 5);
        
        Iterator<CS16Edge<String>> incoming = _dirGraph.incomingEdges(A);
        Iterator<CS16Edge<String>> outgoing = _dirGraph.outgoingEdges(A);
        ArrayList<CS16Edge<String>> listIncoming = new ArrayList<CS16Edge<String>>();
        ArrayList<CS16Edge<String>> listOutgoing = new ArrayList<CS16Edge<String>>();
       
        while(incoming.hasNext()) {
    		CS16Edge<String> next = incoming.next();
        	listIncoming.add(next);
        }
        
        assertThat(listIncoming.contains(ab), is(false));
        assertThat(listIncoming.contains(bc), is(false));
        assertThat(listIncoming.contains(ca), is(true));
        
        while(outgoing.hasNext()) {
    		CS16Edge<String> next = outgoing.next();
        	listOutgoing.add(next);
        }
        
        assertThat(listOutgoing.size(), is(2));
        assertThat(_dirGraph.numOutgoingEdges(A), is(2));
        assertThat(listOutgoing.contains(ac), is(true));
        assertThat(listOutgoing.contains(ca), is(false));
        assertThat(listOutgoing.contains(bc), is(false));
        
    }
    
    //test incoming/outgoingEdges from undirected graph
    @Test(timeout = 10000)
    public void outgoingIncomingUndirected() {
    	CS16Vertex<String> A = _graph.insertVertex("A");
        CS16Vertex<String> B = _graph.insertVertex("B");
        CS16Vertex<String> C = _graph.insertVertex("C");
    	
      // use the edge iterator to get a list of the edges in the actual graph.
        CS16Edge<String> ab = _graph.insertEdge(A, B, 1);
        CS16Edge<String> bc = _graph.insertEdge(B, C, 2);
        CS16Edge<String> ac = _graph.insertEdge(A, C, 3);
        
        Iterator<CS16Edge<String>> incoming = _graph.incomingEdges(C);
        Iterator<CS16Edge<String>> outgoing = _graph.outgoingEdges(C);
        ArrayList<CS16Edge<String>> listIncoming = new ArrayList<CS16Edge<String>>();
        ArrayList<CS16Edge<String>> listOutgoing = new ArrayList<CS16Edge<String>>();
       
        while(incoming.hasNext()) {
    		CS16Edge<String> next = incoming.next();
        	listIncoming.add(next);
        }
        
        assertThat(listIncoming.contains(ab), is(false));
        assertThat(listIncoming.contains(bc), is(true));
        
        while(outgoing.hasNext()) {
    		CS16Edge<String> next = outgoing.next();
        	listOutgoing.add(next);
        }
        
        assertThat(listOutgoing.contains(ab), is(false));
        assertThat(listOutgoing.contains(ac), is(true));
        assertThat(listOutgoing.contains(bc), is(true));
        
    }
    
    //test incoming/outgoingEdges after removing from undirected graph
    @Test(timeout = 10000)
    public void outgoingIncomingRemoveUndirected() {
    	CS16Vertex<String> A = _graph.insertVertex("A");
        CS16Vertex<String> B = _graph.insertVertex("B");
        CS16Vertex<String> C = _graph.insertVertex("C");
        CS16Vertex<String> D = _graph.insertVertex("D");
    	
      // use the edge iterator to get a list of the edges in the actual graph.
        CS16Edge<String> bc = _graph.insertEdge(B, C, 2);
        CS16Edge<String> cd = _graph.insertEdge(C, D, 3);
        
        _graph.removeVertex(A); //remove vertex
        
        _graph.removeEdge(cd); //remove different edge
        
        Iterator<CS16Edge<String>> incoming = _graph.incomingEdges(C);
        Iterator<CS16Edge<String>> outgoing = _graph.outgoingEdges(C);
        ArrayList<CS16Edge<String>> listIncoming = new ArrayList<CS16Edge<String>>();
        ArrayList<CS16Edge<String>> listOutgoing = new ArrayList<CS16Edge<String>>();
       
        while(incoming.hasNext()) {
    		CS16Edge<String> next = incoming.next();
        	listIncoming.add(next);
        }
        
        assertThat(listIncoming.size(), is(1)); //incoming size changes so check
        
        while(outgoing.hasNext()) {
    		CS16Edge<String> next = outgoing.next();
        	listOutgoing.add(next);
        }
        
        assertThat(listOutgoing.contains(bc), is(true));
        assertThat(listOutgoing.contains(cd), is(false));
        
    }
    
    //test incoming/outgoingEdges after removing from undirected graph
    @Test(timeout = 10000)
    public void outgoingIncomingRemoveDirected() {
    	CS16Vertex<String> A = _dirGraph.insertVertex("A");
        CS16Vertex<String> B = _dirGraph.insertVertex("B");
        CS16Vertex<String> C = _dirGraph.insertVertex("C");
        CS16Vertex<String> D = _dirGraph.insertVertex("D");
    	
      // use the edge iterator to get a list of the edges in the actual graph.
        CS16Edge<String> bc = _dirGraph.insertEdge(B, C, 2);
        CS16Edge<String> cd = _dirGraph.insertEdge(C, D, 3);        
        _dirGraph.removeVertex(A);
        
        _dirGraph.removeEdge(cd);
        
        Iterator<CS16Edge<String>> incoming = _dirGraph.incomingEdges(C);
        Iterator<CS16Edge<String>> outgoing = _dirGraph.outgoingEdges(C);
        ArrayList<CS16Edge<String>> listIncoming = new ArrayList<CS16Edge<String>>();
        ArrayList<CS16Edge<String>> listOutgoing = new ArrayList<CS16Edge<String>>();
       
        while(incoming.hasNext()) {
    		CS16Edge<String> next = incoming.next();
        	listIncoming.add(next);
        }
        
        assertThat(listIncoming.size(), is(1)); //check that size is only 1
        
        while(outgoing.hasNext()) {
    		CS16Edge<String> next = outgoing.next();
        	listOutgoing.add(next);
        }
        
        assertThat(listOutgoing.size(), is(0)); //check that size is 0 (for C)
        assertThat(_dirGraph.numOutgoingEdges(B), is(1)); //check that the outgoing for B is 1
        assertThat(listOutgoing.contains(bc), is(false));
        assertThat(listOutgoing.contains(cd), is(false));
        
    }
    
   //Tests the endVertices method
    @Test(timeout = 10000)
    public void testEndVertices() {
    	CS16Vertex<String> A = _dirGraph.insertVertex("A");
        CS16Vertex<String> B = _dirGraph.insertVertex("B");
    	
       // use the edge iterator to get a list of the edges in the actual graph.
        CS16Edge<String> ab = _dirGraph.insertEdge(A, B, 1);
        
        List<CS16Vertex<String>> endVerticesList = _dirGraph.endVertices(ab); //store endVertices

        assertThat(endVerticesList.size(), is(2)); //check that endVertices size is 2, number of vertices
        assertThat(endVerticesList.contains(A), is(true));
        assertThat(endVerticesList.contains(B), is(true));

    	
    }
    
   //Testing the areAdjacent method
    @Test(timeout = 10000)
    public void testAreAdjacent() {
    	CS16Vertex<String> A = _dirGraph.insertVertex("A");
        CS16Vertex<String> B = _dirGraph.insertVertex("B");
        CS16Vertex<String> C = _graph.insertVertex("C");
        CS16Vertex<String> D = _dirGraph.insertVertex("D");
        
        CS16Edge<String> ab = _dirGraph.insertEdge(A, B, 1);
        CS16Edge<String> cd = _graph.insertEdge(D, C, 1);
        
        assertThat(_dirGraph.areAdjacent(A, B), is(true));
        assertThat(_graph.areAdjacent(C, D), is(true));



    }
    
    
    //Testing the error thrown by insertEdge when null
    @Test(expected = InvalidVertexException.class)
    public void testInsertEdgeThrowsInvalidVertexException() {
    	CS16Vertex<String> A = _dirGraph.insertVertex("A");
        CS16Vertex<String> B = null;
    	CS16Vertex<String> C = null;
    	CS16Vertex<String> D = _graph.insertVertex("D");

    	
      // use the edge iterator to get a list of the edges in the actual graph.
        _dirGraph.insertEdge(A, B, 1);
        _dirGraph.insertEdge(B, A, 1);
        _dirGraph.insertEdge(B, C, 1);
        _graph.insertEdge(D, B, 1);
        _graph.insertEdge(B, D, 1);
        
    }
    
    //Testing the error thrown by removeVertex when null
    @Test(expected = InvalidVertexException.class)
    public void testRemoveVertexThrowInvalidVertexException() {
    	CS16Vertex<String> B = null;
    	_graph.removeVertex(B);
    	_dirGraph.removeVertex(B);
    }
    
   //Testing the error thrown by removeEdge when null
    @Test(expected = InvalidEdgeException.class)
    public void testRemoveEdgeThrowInvalidEdgeException() {
        CS16Edge<String> ab = null;
        CS16Edge<String> cd = null;

    	_graph.removeEdge(cd);
    	_dirGraph.removeEdge(ab);
    }
    
    //Error thrown by connectingEdge method
    @Test(expected = InvalidVertexException.class)
    public void testConnectingEdgeThrowsInvalidVertexException() {

    	CS16Vertex<String> B = null;
    	CS16Vertex<String> C = null;
    	CS16Vertex<String> A = _dirGraph.insertVertex("A");
    	CS16Vertex<String> D = _graph.insertVertex("D");

    	_graph.connectingEdge(B, C);
    	_graph.connectingEdge(D, B);
    	_graph.connectingEdge(B, D);
    	_dirGraph.connectingEdge(B, C);
    	_dirGraph.connectingEdge(B, A);
    	_dirGraph.connectingEdge(A, B);
    }
    
    //Error thrown by connectingEdge method
    @Test(expected = NoSuchEdgeException.class)
    public void testConnectingEdgeThrowsNoSuchEdgeException() {
    	
    	CS16Vertex<String> A = _dirGraph.insertVertex("A");
    	CS16Vertex<String> C = _graph.insertVertex("C");
    	CS16Vertex<String> D = _graph.insertVertex("D");
    	
    	_graph.connectingEdge(C, D);
    	_dirGraph.connectingEdge(A, C);
    
    }
    
   //Error thrown by incomingEdges, outgoingEdges, and numOutgoingEdges method
    @Test(expected = InvalidVertexException.class)
    public void testIncomingOutgoingEdgesThrowsInvalidVertexException() {
    	CS16Vertex<String> A = null;
    	_graph.incomingEdges(A);
    	_dirGraph.incomingEdges(A);
    	_graph.outgoingEdges(A);
    	_dirGraph.outgoingEdges(A);
    	_dirGraph.numOutgoingEdges(A);
    	
    }
    
    //Error through by numOutgoingEdges
    @Test(expected = DirectionException.class)
    public void testNumOutgoingEdgesThrowsDirectionException() {
    	CS16Vertex<String> A = _graph.insertVertex("A");
    	_graph.numOutgoingEdges(A);
    }
    
    //Error thrown by opposite method
    @Test(expected = InvalidVertexException.class)
    public void testOppositeThrowsInvalidVertexException() {
    	CS16Vertex<String> A = null;
    	CS16Vertex<String> B = null;
    	CS16Edge<String> ab = _dirGraph.insertEdge(A, B, 1);
    	CS16Edge<String> ba = _graph.insertEdge(B, A, 1);

    	_graph.opposite(A, ba);
    	_dirGraph.opposite(A, ab);
    	
    }
    
    //Error thrown by opposite method
    @Test(expected = InvalidEdgeException.class)
    public void testOppositeThrowsInvalidEdgeException() {
    	CS16Vertex<String> A = _dirGraph.insertVertex("A");
    	CS16Vertex<String> B = _graph.insertVertex("B");
    	CS16Edge<String> ab = null;
    	_graph.opposite(B, ab);
    	_dirGraph.opposite(A, ab);
    	
    }
    
    //Error thrown by opposite method
    @Test(expected = NoSuchVertexException.class)
    public void testOppositeThrowsNoSuchVertexException() {
    	CS16Vertex<String> A = _dirGraph.insertVertex("A");
    	CS16Vertex<String> B = _dirGraph.insertVertex("B");
    	CS16Vertex<String> C = _dirGraph.insertVertex("C");
    	CS16Vertex<String> D = _graph.insertVertex("D");
    	CS16Vertex<String> E = _graph.insertVertex("E");
    	CS16Vertex<String> F = _graph.insertVertex("F");
    	
    	CS16Edge<String> bc = _graph.insertEdge(B, C, 1);
    	CS16Edge<String> ef = _graph.insertEdge(E, F, 1);
    	
    	_dirGraph.opposite(A, bc);
    	_graph.opposite(D, ef);
    	
    }
    
   //Error thrown by endVertices method
    @Test(expected = InvalidEdgeException.class)
    public void testendVerticesThrowsInvalidEdgeException() {
    	CS16Edge<String> bc = null;
    	CS16Edge<String> de = null;
    	
    	_dirGraph.endVertices(bc);
    	_dirGraph.endVertices(de);
    }
    
   //Error thrown by areAdjacent method
    @Test(expected = InvalidVertexException.class)
    public void testareAdjacentThrowsInvalidVertexException() {
    	CS16Vertex<String> A = null;
    	CS16Vertex<String> B = null;
    	CS16Vertex<String> C = _dirGraph.insertVertex("C");
    	CS16Vertex<String> D = _graph.insertVertex("D");
    	
    	_graph.areAdjacent(A, C);
    	_graph.areAdjacent(C, A);
    	_graph.areAdjacent(A, B);
    	_dirGraph.areAdjacent(A, D);
    	_dirGraph.areAdjacent(D, A);
    	_dirGraph.areAdjacent(A, B);
    }
    
    /*
     * List of graphs for testing!
     */
    @Parameters(name = "with graph: {0}")
    public static Collection<String> graphs() {
        List<String> names = new ArrayList<>();
        names.add("graph.AdjacencyMatrixGraph");
        return names;
    }
    
    /*
     * ####################################################
     * 
     * DO NOT MODIFY ANYTHING BELOW THIS LINE
     * 
     * ####################################################
     */
	
	
    @SuppressWarnings("unchecked")
    @Before
	public void makeGraph() {
        Class<?> graphClass = null;
        try {
            graphClass = Class.forName(_graphClassName);
            Constructor<?> constructor = graphClass.getConstructors()[0];
            _graph = (Graph<String>) constructor.newInstance(false);
	    _dirGraph = (Graph<String>) constructor.newInstance(true);
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            System.err.println("Exception while instantiating Graph class in GraphTest.");
            e.printStackTrace();
        }
	}
	
    public GraphTest(String graphClassName) {
	    this._graphClassName = graphClassName;
	}
}

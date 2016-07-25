/*
 * Ant.java
 * Created 4 October 2011
 * 
 * 25 April 2012 - ensure ALPHA is updated each time 
 */

package engine;

import config.AlgorithmParameters;
import config.Parameters;
import java.util.*;
import pheromone.AlphaTable;
import myUtils.Utility;
import softwareDesign.CLSClass;

/**
 *
 * @author Christopher Simons
 */

public class Ant
{
    /** end of class string */
    protected static final String END_OF_CLASS = "end of class";
    
    /** reference to the list of attribute and method path */
    protected List< Node > amList;
    
    /** size of the amList i.e. number of methods and attributes */
    protected final int amListSize;
    
    /** number of classes in software design */
    protected final int numberOfClasses;
    
    /** reference to the table of pheromone values */
    protected AlphaTable alphaTable;
    
    /** current path, node by node */
    protected Path currentPath; 
    
    /** list of classes that user elects to 'freeze' */
    protected List< CLSClass> freezeList;
    
    /** is this ant handling constraints? */
    protected boolean handlingConstraints;
    
    /** 
     * validity of solution path
     * true if all classes have at least 1 + 1
     * false otherwise
     */
    protected boolean valid; 
    
    /** state of the generated path as we iterate */
    private enum State { start, inAClass, atEoC, invalid };
    
    /** 
     * constructor
     * @param reference to list of attributes and methods
     * @param number of classes
     * @param reference to alpha table
     * @param list of classes that user elects to "freeze"
     * @param handling constraints boolean
     */
    public Ant( List< Node > nodes,
                int numberOfClasses,
                AlphaTable at,
                List< CLSClass > freezeList,
                boolean handlingConstraints )
    {
        assert nodes != null;
        this.amList = nodes;
        assert at != null;
        this.alphaTable = at; 
        assert freezeList != null;
        this.freezeList = freezeList;
        assert numberOfClasses > 0;
        this.numberOfClasses = numberOfClasses;
        
        amListSize = amList.size( );
        
        currentPath = null;
        valid = false;
        
        this.handlingConstraints = handlingConstraints;
    }
    
    /**
     * generate a solution (or path)
     * and then pass the path back to the environment.
     */
    public void generateSolution( )
    {
        assert amList.size( ) > 0;
        
        // create a new path though the environment  
        // to which path can be added
        Path path = new Path( new DesignPathRole( ) );
        
        // create local node variables for path construction
        Node current = null;
        Node next = null;
        
        // the first node is always the nest
        current = new Nest( "nest", 0 );
        path.add( current );
        
        // secondly, construct the path with elements from 'frozen' classes 
        constructFromFrozenClasses( path );
        
        // now create a working list of all possible remaining elements
        // i.e. attributes, methods and ( end of classes - 1 )
        // coz the last node must be an EndOfClass
        // 20 July 2012
        // working list amended to work with all elements
        // EXCEPT those in frozen classes
        final int numberOfElementsFrozen = getNumberOfElementsFrozen( );
        
        List< Node > workingList = createWorkingList( );
        final int workingListSize = workingList.size( );
        final int numberOfEoCUsed = this.freezeList.size( );
        assert workingListSize == 
            amListSize - numberOfElementsFrozen + ( numberOfClasses - 1 - numberOfEoCUsed ):
                "working list size is: " + workingListSize +
                ", am list size is: " + amListSize +
                ", number of classes is: " + numberOfClasses + 
                ", numberOfElementsFrozen is: " + numberOfElementsFrozen;
        
        // then node by node, construct the remaining path 
        // depending on the attractiveness of each possibility
        int nodeCounter = 0;
        while( workingList.isEmpty( ) == false )
        {
            // select the next node, according to attractiveness
            next = selectNextNode( current, workingList, nodeCounter );
            
            // add the next node to the solution path
            path.add( next );
            
            // and so the ant moves through the environment
            current = next;
            
            nodeCounter++;
        }
        
        assert workingList.isEmpty( );
        assert path.size( ) == workingListSize + numberOfElementsFrozen + 1 + numberOfEoCUsed:
            "working list size is: " + workingListSize +
            "path size is " + path.size( );
                
        // the last node is always an end of class
        path.add( new EndOfClass( END_OF_CLASS, workingListSize + 1 ) );
        
        // lastly assert invariance,
        // i.e. the number of EndOfClasses == number of classes
        
        assert( path.size( ) == amList.size( ) + this.numberOfClasses + 1 /* for the nest */ );
        
        if( Parameters.SOLUTION_GENERATION_ROBUSTNESS_CHECK == true )
        {
            Iterator< Node > it = path.iterator( );
            int counter = 0;
            while( it.hasNext( ) ) 
            {
                Node node = it.next( );

                if( node instanceof EndOfClass )
                {
                    counter++;
                }
            }

            String s = "";
            Iterator< Node > it2 = path.iterator( );
            if( counter != this.numberOfClasses )
            {
                while( it2.hasNext( ) )
                {
                    Node node = it2.next( );
                    s += ( node.getName( ) + " " + node.getNumber( ) + " ," ); 
                    if( node instanceof EndOfClass )
                    {
                        System.out.println( s );
                        s = "";
                    }
                }
            }
            assert counter == this.numberOfClasses;
        }
        
        // when solution path is constructed, assign it to 
        // the current vertices instance variable 
        this.currentPath = path;
        
        // 2 Feb 2016
        this.valid = checkValidity( path );
        
        this.currentPath.setValid( this.valid );
        
 

    }
    
    /**
     * create a working list of path elements containing
     * all methods, all attributes, and appropriate
     * number of EndOfClass path.
     * @return working list of path
     */
    protected List< Node > createWorkingList( )
    {
        List< Node > workingList = new ArrayList<  >( );
        
        // easy bit - add the attributes and methods
        assert this.amList.isEmpty( ) == false;
        assert this.amListSize == amList.size( );
        
        for( Node n : amList )
        {
            Node node = null;
                        
            if( n instanceof Attribute )
            {
                node = new Attribute( 
                    n.getName( ), n.getNumber( ) + 1 );
            }
            else if( n instanceof Method )
            {
                node = new Method( 
                    n.getName( ), n.getNumber( ) + 1 );
            }
            else
            {
                assert false: "impossible type!!!";
            }
            
            assert node != null;
            
            if( isInAFrozenClass( node ) == false )
            {       
                workingList.add( node );
            }
        }
        
        // at this point, the working list must be the 
        // same size as the attribute and method list size
        assert workingList.size( ) == amListSize - getNumberOfElementsFrozen( ); 
//        System.out.println("working list size is: " + workingList.size( ) );
        
        
        // tricky bit now - last node is always an EndOfClass
        // and this is done in the calling method. 
        // Thus add (number of classes - 1) EndOfClasses to working list
        // 24 July 2012
        // frozen classes are done first and have already used up their
        // end of class nodes
        int counter = amListSize + 1;
        int i = 0;
        final int numberOfEoCUsed = this.freezeList.size( );
        final int numberOfEndOfClasses = ( this.numberOfClasses - 1 ) - numberOfEoCUsed;
        
        for( i = 0; i < numberOfEndOfClasses; i++ )
        {
            workingList.add( new EndOfClass( END_OF_CLASS, counter++ ) );
        }
        
        assert counter == amListSize + numberOfEndOfClasses + 1;
        assert workingList.size( ) == 
            ( amListSize - getNumberOfElementsFrozen( ) ) + numberOfEndOfClasses;
        
        // for testing
//        System.out.println( "this is the working list" );
//        for( Node n : workingList )
//        {
//            System.out.print( n.getName( ) + " " );
//            System.out.println( n.getNumber( ) );
//        }

        return workingList;
    }
    
    
    /**
     * select the next node based on pheromone 'attractiveness'
     * @param the current node
     * @param the working list of path
     * @return the next node 
     */
    protected Node selectNextNode( 
        Node current, List< Node > workingList, int nodeCounter )
    {
        assert current != null;
        assert workingList != null;
        assert workingList.size( ) > 0;
        assert nodeCounter >= 0;
        final int workingListSize = workingList.size( );
        
        // handle the situation where the ant reaches the end of the path
        // i.e there's only ONE node left in the list 
        if( workingListSize == 1 )
        {
            Node result = workingList.remove( 0 );
            assert workingList.isEmpty( );
            return result;
        }
        
        // else there must be more than one node to select from!
        assert workingList.size( ) > 1;
        
        // and so let's choose it!
        final int currentNodeNumber = current.getNumber( );
        
        // prepare a fitness proportionate node selection mechanism
        // implemented by a "roulette wheel" approach
        double[ ] probabilities = new double[ workingListSize ];
        double sum = 0.0;
        
        // for each node in the working list,
        // get the probability related to
        // 'from' the current node (x axis in the table)
        // 'to' all possible path (y axis in the table)
        for( int i = 0; i < workingListSize; i++ )
        {
            int workingListNodeNumber = workingList.get( i ).getNumber( );
           
            final double temp = this.alphaTable.getProbabilityAt( currentNodeNumber, workingListNodeNumber );
            double prob = temp;
            
            if( AlgorithmParameters.heuristics == true )
            {
                // apply the heuristic information
                prob = applyBeta( temp, current, workingList.get( i ), nodeCounter );
                assert prob >= 0.0 : "probability is: " + prob;
            }
                        
            probabilities[ i ] = prob; 
            
            sum += probabilities[ i ];
        }
        
        // now spin the "roulette wheel" to get a random number...
        double random = Utility.getRandomInRange( 0.0, sum );
        assert random >= 0.0;
        assert random <= sum;
        
        double runningTotal = 0.0;
        boolean finished = false;
        int selectedIndex = 0;
        
        // select a node depending on probability
        for( int j = 0; j < workingListSize && ! finished; j++ )
        {
            runningTotal += probabilities[ j ];
            
            if( runningTotal < random )
            {
                // do nothing, continue...
            }
            else
            {
                finished = true;
                selectedIndex = j;
            }
      }
        
//        System.out.print("out of loop and j is: " + j + "," );
//        System.out.println("and selected index is: " + selectedIndex );
//        System.out.println( " " );
        
        assert selectedIndex >= 0;
        assert selectedIndex < workingListSize :
                "selected index is: " + selectedIndex + 
                " working list size is: " + workingListSize;
        
        // return and remove the selected node in the working list
        return workingList.remove( selectedIndex );
    }
    
    /**
     * generate partial solutions based on classes.
     * requires that the current path are a complete
     * solution (i.e. end to end). 
     * Do not worry about "1 attribute and 1 method" 
     * at this stage. 
     */
    public void generatePartialSolutions( )
    {
        // 28 May 2012 make sure this method isn't called!
        assert true : "generate partial solutions called!";
        
//        assert currentVertices.isEmpty( ) == false;
//        
//        // placeholder for class path role path
//        Path path = new Path( new ClassPathRole( ) );
//        
//        Iterator< Node > it = currentVertices.iterator( );
//        
//        while( it.hasNext( ) )
//        {
//            Node n = it.next( );
//            
//            if( n instanceof Nest )
//            {
//                // do nothing
//            }
//            else if( n instanceof Attribute || n instanceof Method )
//            {
//                path.add( n );
//            }
//            else if( n instanceof EndOfClass )
//            {
//                // ? add an EndOfClass to path ?? probably not
//                
//                // add the partial solution to the environment
//                environment.add( path );
//                
//                // create a new path for next time around
//                path = new Path( new ClassPathRole( ) );
//            }
//            else
//            {
//                assert false : "impossible run-time instance!!";
//            }
//            
//        }   // end while there's another node in path
//        
    }
    
    /**
     * is this current path valid
     * @return true if path is valid, false otherwise
     */
    public boolean isValidPath( )
    {
        return this.valid;
    }
    
    /**
     * check the validity of a path
     * @param path to be checked
     * @return true if path is valid, false otherwise
     */
    protected boolean checkValidity( Path path )
    {
        boolean result = true;
        State state = State.start;
        int attributes = 0;
        int methods = 0;
        
        Iterator< Node > it = path.iterator( );
        
        while( it.hasNext( ) && result == true )
        {
            Node node = it.next( );
            
            if( node instanceof Nest )
            {
                // do nothing, we're at the nest
            }
            else if( node instanceof Attribute )
            {
                if( state == State.start )
                {
                    state = State.inAClass;
                    attributes++;
                }
                else if( state == State.inAClass )
                {
                    attributes++;
                }
                else if( state == State.atEoC )
                {
                    state = State.inAClass;
                    attributes++;
                }
                else
                {
                    assert true : "invalid state";
                }
            }
            else if( node instanceof Method )
            {
                if( state == State.start )
                {
                    state = State.inAClass;
                    methods++;
                }
                else if( state == State.inAClass )
                {
                    methods++;
                }
                else if( state == State.atEoC )
                {
                    state = State.inAClass;
                    methods++;
                }
                else
                {
                    assert true : "invalid state";
                }           
            }
            else if ( node instanceof EndOfClass )
            {
                if( state == State.start )
                {
                    result = false;
                }
                else if( state == State.inAClass )
                {
                    if( attributes >= 1 && methods >= 1 )
                    {
                        state = State.atEoC;
                        attributes = 0;
                        methods = 0;
                    }
                    else
                    {
                        result = false;
                    }
                }
                else if( state == State.atEoC )
                {
                    result = false;
                }
                else
                {
                    assert false : "invalid state";
                }              
            }
        }
        
        return result;
    }
       
    /**
     * get path constructed by ant
     * @return constructed path
     */
    public Path getPath( )
    {
        return this.currentPath;
    }
    
    /**
     * add nodes to the path based on the frozen class(es)
     * @param path 
     */
    protected void constructFromFrozenClasses( Path path )
    {
        assert path != null;
        // precondition: must already contain the "nest"
        assert path.size( ) == 1;
        
        // keep a count of nodes we've added to the path
        int nodeCount = 1;
        
        // the element numbers in the path are one more than 
        // in the amlist because of the "nest"
        
        for( CLSClass c : this.freezeList )
        {
            List< Method > mList = c.getMethodList( );
            for( Method m : mList )
            {
                path.add( new Method( m.getName( ), m.getNumber( ) + 1 ) );
                nodeCount++;
            }
            
            List< Attribute > aList = c.getAttributeList( );
            for( Attribute a : aList )
            {
                path.add( new Attribute( a.getName( ), a.getNumber( ) + 1 ) );
                nodeCount++;
            }
            
            nodeCount++;
            path.add( new EndOfClass( END_OF_CLASS, nodeCount ) );
        }
    }
    
    
    protected int getNumberOfElementsFrozen( )
    {
        assert this.freezeList != null;
        int result = 0;
        
        for( CLSClass c : this.freezeList )
        {
            List< Method > mList = c.getMethodList( );
            result += mList.size( );
            
            List< Attribute > aList = c.getAttributeList( );
            result += aList.size( );
        }
        
        return result;
    }
    
    protected boolean isInAFrozenClass( Node node )
    {
        assert node != null;
        assert this.freezeList != null;
        boolean result = false;
        
        for( CLSClass c : this.freezeList )
        {
            if( node instanceof Method )
            {
                List< Method > mList = c.getMethodList( );
                
                for( Method m : mList )
                {
                    if( m.getNumber( ) == node.getNumber( ) )
                    {
                        result = true;
                        break;
                    }
                }
            }
            else if( node instanceof Attribute )
            {
                List< Attribute > aList = c.getAttributeList( );
                
                for( Attribute a : aList )
                {
                    if( a.getNumber( ) == node.getNumber( ) )
                    {
                        result = true;
                        break;
                    }
                }
            }
            else
            {
                assert false : "impossible type of node";
            }
        }
        
        return result;
    }

    /**
     * apply heuristic information via beta.
     * As this file does not use heuristic information, 
     * the method simply returns the argument i.e. does nothing.
     * However, this method is overridden in HeuristicAnt2 
     * @param temp, the probability value after alpha has been applied
     * @return temp, the probability value after alpha has been applied
     */
    protected double applyBeta( double temp, Node current, Node node, int nodeCounter )
    {
        return temp;
    }
    
}   // end class

//------- end file ----------------------------------------


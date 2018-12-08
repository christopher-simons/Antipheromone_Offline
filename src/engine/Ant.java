/*
 * Ant.java
 * Created 4 October 2011
 * 
 * 25 April 2012 - ensure ALPHA is updated each time 
 */

package engine;

import config.AlgorithmParameters;
import config.Parameters;
import static engine.Ant.END_OF_CLASS;
import java.util.*;
import pheromone.AlphaMatrix;
import myUtils.Utility;
import problem.*;

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
    
    /** reference to list of attributes */
    protected List< Attribute>  attributeList;
    
    /** number of methods in software design */
    protected List< Method > methodList;
    
    /** number of classes in software design */
    protected final int numberOfClasses;
    
    /** reference to the table of pheromone values */
    protected AlphaMatrix alphaTable;
    
    /** current path, node by node */
    protected Path currentPath; 
    
    /** is this ant handling constraints? */
    protected boolean handlingConstraints;
    
    protected final List< Node > tspNodes;
    
    /** 
     * validity of solution path
     * true if all classes have at least 1 + 1
     * false otherwise
     */
    protected boolean valid; 
    
    /** state of the generated path as we iterate */
    private enum State { start, inAClass, atEoC, invalid };

    /** instance variable for state of path being generated */
    protected State state;   
    
    /** 
     * constructor
     * @param nodes - reference to list of nodes
     * @param attributeList - reference to list of attributes
     * @param methodList - reference to list of attributes
     * @param numberOfClasses
     * @param at - reference to alpha matrix
     * @param handlingConstraints - boolean
     * @param tspNodes - list of nodes for TSP
     */
    public Ant( List< Node > nodes,
                List< Attribute > attributeList,
                List< Method > methodList,
                int numberOfClasses,
                AlphaMatrix at,
                boolean handlingConstraints,
                List< Node > tspNodes )
    {
        assert nodes != null;
        this.amList = nodes;
        assert attributeList != null;
        this.attributeList = attributeList;
        assert methodList != null;
        this.methodList = methodList;
        assert numberOfClasses >= 0;
        this.numberOfClasses = numberOfClasses;
        assert at != null;
        this.alphaTable = at; 
        assert tspNodes != null;
        
        amListSize = amList.size( );
        
        currentPath = null;
        valid = false;
        
        this.handlingConstraints = handlingConstraints;
        this.tspNodes = tspNodes;
       
    }
    
    /**
     * generate a solution (or path)
     * and then pass the path back to the environment.
     */
    public void generateSolution( )
    {
        assert amList.size( ) >= 0;
        
        // First, create a working list of all possible elements
        List< Node > workingList = createWorkingList( );
        final int workingListSize = workingList.size( );
        
        if( Parameters.problemNumber == Parameters.TSP_BERLIN52 )
        {
            assert workingListSize == TSP_Berlin52.NUMBER_OF_CITIES;
        }
        else if( Parameters.problemNumber == Parameters.TSP_ST70 )
        {
            assert workingListSize == TSP_ST70.NUMBER_OF_CITIES;
        }
        else if( Parameters.problemNumber == Parameters.TSP_RAT99 )
        {
            assert workingListSize == TSP_RAT99.NUMBER_OF_CITIES;
        }
        else if( Parameters.problemNumber == Parameters.TSP_RAT195 )
        {
            assert workingListSize == TSP_RAT195.NUMBER_OF_CITIES;
        }
        else
        {
            assert workingListSize == amListSize + ( numberOfClasses - 1);
        }
        
        // now create a solution path though the environment  
        // that can be added to 'this'
        
        Path path = null;
        Node current = null;
        Node next = null;
        int nodeCounter = 0;
        int numberOfCities = 0;
        
        // if the problem instance is a TSP...
        if( Parameters.problemNumber == Parameters.TSP_BERLIN52 || 
            Parameters.problemNumber == Parameters.TSP_ST70 ||
            Parameters.problemNumber == Parameters.TSP_RAT99 ||   
            Parameters.problemNumber == Parameters.TSP_RAT195 )  
        {
            if( Parameters.problemNumber == Parameters.TSP_BERLIN52 )
            {
                numberOfCities = TSP_Berlin52.NUMBER_OF_CITIES;
            }
            else if( Parameters.problemNumber == Parameters.TSP_ST70 )
            {
                numberOfCities = TSP_ST70.NUMBER_OF_CITIES;
            }
            else if( Parameters.problemNumber == Parameters.TSP_RAT99 )
            {
                numberOfCities = TSP_RAT99.NUMBER_OF_CITIES;
            }
            else if( Parameters.problemNumber == Parameters.TSP_RAT195 )
            {
                numberOfCities = TSP_RAT195.NUMBER_OF_CITIES;
            }
            else
            {
                assert false : "impossible TSP problem!";
            }
            
            path = new Path( );
            // classic TSP - select first city at random
            final int index = Utility.getRandomInRange( 0, numberOfCities - 1 );
            assert index >= 0;
            assert index < workingList.size( );
            current = workingList.remove( index );
        }    
        else // must be a software design problem instance    
        {   
            path = new Path( new DesignPathRole( ) );
            // the first node is always the nest
            current = new Nest( "nest", 0 );
        }
        path.add( current );
     
        
        while( workingList.isEmpty( ) == false )
        {
            // select the next node, according to attractiveness
            next = selectNextNode( current, workingList, nodeCounter );
            
            // 7 August 2018
//            next = selectNextValidNodeTracking( 
//                current, workingList, nodeCounter, attsLeft, metsLeft, eocLeft );
            
            // add the next node to the solution path
            path.add( next );
            
            // and so the ant moves through the environment
            current = next;
            
            // update the counters
            nodeCounter++;
        }
        
        assert workingList.isEmpty( );
                
        if( Parameters.problemNumber == Parameters.CBS ||
            Parameters.problemNumber == Parameters.GDP ||
            Parameters.problemNumber == Parameters.RANDOMISED ||
            Parameters.problemNumber == Parameters.SC    )
        {
            assert path.size( ) == workingListSize + 1 /* for the nest */:
                "working list size is: " + workingListSize +
                "path size is " + path.size( );
            
            // the last node is always an end of class
            path.add( new EndOfClass( END_OF_CLASS, workingListSize + 1 ) );
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
        }
        else /// must be TSP problem instance
        {
            assert path.size( ) == numberOfCities:
                "number of cities in path is: " + path.size( );
            
            // go show the nodes of a solution path
//            Iterator< Node > it = path.iterator( );
//            while( it.hasNext( ) )
//            {
//                Node n = it.next( );
//                System.out.print( n.getNumber( ) + " " );
//            }
//            System.out.println( " " );        
        }
        
        // after solution path is constructed, assign it to 
        // the current vertices instance variable 
        this.currentPath = path;
        
        // 2 Feb 2016
        if( Parameters.problemNumber != Parameters.TSP_BERLIN52 )
        {
            this.valid = checkValidity( path );
            this.currentPath.setValid( this.valid );
        }
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
        
        // EITHER create a working list for a TSP problem...
        int numberOfCities = 0;
        if( Parameters.problemNumber == Parameters.TSP_BERLIN52 || 
            Parameters.problemNumber == Parameters.TSP_ST70 ||
            Parameters.problemNumber == Parameters.TSP_RAT99 || 
            Parameters.problemNumber == Parameters.TSP_RAT195 )   
        {
            if( Parameters.problemNumber == Parameters.TSP_BERLIN52 )
            {
                numberOfCities = TSP_Berlin52.NUMBER_OF_CITIES;
            }
            else if( Parameters.problemNumber == Parameters.TSP_ST70 )
            {
                numberOfCities = TSP_ST70.NUMBER_OF_CITIES;
            }
            else if( Parameters.problemNumber == Parameters.TSP_RAT99 )
            {
                numberOfCities = TSP_RAT99.NUMBER_OF_CITIES;
            }
            else if( Parameters.problemNumber == Parameters.TSP_RAT195 )
            {
                numberOfCities = TSP_RAT195.NUMBER_OF_CITIES;
            }
            else
            {
                assert false: "impossible TSP Problem!";
            }
            assert numberOfCities > 0;
            
            for( int i = 0; i < numberOfCities; i++ )
            {
                Node node = new Node( );
                node.setNumber( i );
                workingList.add( node );
            }
            
            assert( workingList.size( ) == numberOfCities );
            return workingList;
        }
        
        // ...OR create working list for a software design problem instance
        
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
            
            workingList.add( node );
        }
        
        // at this point, the working list must be the 
        // same size as the attribute and method list size
        assert workingList.size( ) == amListSize; 
        
        // tricky bit now - last node is always an EndOfClass
        // and this is done in the calling method. 
        // Thus add (number of classes - 1) EndOfClasses to working list
        
        // 24 July 2012
        // frozen classes are done first and have already used up their
        // end of class nodes
        int counter = amListSize + 1;
        int i = 0;
        
        //final int numberOfEoCUsed = this.freezeList.size( );
        final int numberOfEndOfClasses = ( this.numberOfClasses - 1 );
        
        for( i = 0; i < numberOfEndOfClasses; i++ )
        {
            workingList.add( new EndOfClass( END_OF_CLASS, counter++ ) );
        }
        
        assert counter == amListSize + numberOfEndOfClasses + 1;
        assert workingList.size( ) == amListSize + numberOfEndOfClasses;
        
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
                    assert false : "invalid state";
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
                    assert false : "invalid state";
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
    

    
    
    
   /**
     * select the next valid node based on 
     * (i) pheromone 'attractiveness' and (ii) 1+1 constraint
     * by tracking numbers of attributes and methods
     * 8 August 2018
     * @param current - the current node
     * @param workingList - the working list of path
     * @param nodeCounter - the node counter
     * @param attCounter - the attribute counter
     * @param metCounter - the method counter
     * @param eocCounter - the EndofClass counter
     * @return the next node 
     */
    protected Node selectNextValidNodeTracking( 
       Node current, 
       List< Node > workingList, 
       int nodeCounter,
       int attCounter,
       int metCounter,
       int eocCounter )
    {
        assert current != null;
        assert workingList != null;
        assert workingList.size( ) > 0;
        assert nodeCounter >= 0;
        assert attCounter >= 0;
        assert metCounter >= 0;
        assert eocCounter >= 0;

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
            
            probabilities[ i ] = prob; 
            
            sum += probabilities[ i ];
        }
        
        boolean invalid = true;
        int selectedIndex = 0;
        
        int loopCounter = 0;
        while( invalid )
        {
            loopCounter++;
            // spin the "roulette wheel" to get a random number...
            double random = Utility.getRandomInRange( 0.0, sum );
            assert random >= 0.0;
            assert random <= sum;

            double runningTotal = 0.0;
            boolean finished = false;
         

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
            
            Node n = workingList.get( selectedIndex );
            String s = "";
            if( n instanceof Method && metCounter <= eocCounter )
            {
                // we try again, leaving invalid flag true
                s = "method";
            }
            else if( n instanceof Attribute && attCounter <= eocCounter )
            {
                // again we try again, leaving invalid flag true
                s = "attribute";
            }
            else
            {
                invalid = false;
            }
            System.out.print( "invalid: " + invalid + ", " );
            System.out.print( "node: " + s + ", " );
            System.out.print( "working list size: " + workingList.size( ) + ", " );
            System.out.print( "loopCounter:  " + loopCounter + ", " );
            System.out.print( "metCounter:  " + metCounter + ", " );
            System.out.print( "attCounter:  " + attCounter + ", " );
            System.out.println( "eocCounter:  " + eocCounter + ", " );
            
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
     * 8 June 2017
     * select a node from either the AM list or the EoC List at random
     * @param current
     * @param amList
     * @param eocList
     * @param nodeCounter
     * @return 
     */
    private Node selectNextFromEither( Node current, List< Node > amList, List< Node > eocList, int nodeCounter )
    {
        assert current != null;
        assert amList != null;
        // assert amList.size( ) > 0;
        assert eocList != null;
        assert eocList.size( ) > 0;
        assert nodeCounter >= 0;
        
        final int amListSize = amList.size( );
        final int eocListSize = eocList.size( );
        final int combinedSize = amListSize + eocListSize;
        
        final int currentNodeNumber = current.getNumber( );
        
        // prepare a fitness proportionate node selection mechanism
        // implemented by a "roulette wheel" approach
        double[ ] probabilities = new double[ amListSize + eocListSize ];
        double sum = 0.0;
        
        // for each node in the list, get the probability related to:
        // 'from' the current node (x axis in the table)
        // 'to' all possible path (y axis in the table)
        for( int i = 0; i < amListSize; i++ )
        {
            int amListNodeNumber = amList.get( i ).getNumber( );
            final double prob = this.alphaTable.getProbabilityAt( currentNodeNumber, amListNodeNumber );
            probabilities[ i ] = prob; 
            sum += probabilities[ i ];
        }
        
        // and the same for the eocList...
        for( int i = 0; i < eocListSize; i++ )
        {
            int oecListNodeNumber = eocList.get( i ).getNumber( );
            final double prob = this.alphaTable.getProbabilityAt( currentNodeNumber, oecListNodeNumber );
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
        for( int j = 0; j < combinedSize && ! finished; j++ )
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
        assert selectedIndex < combinedSize :
                "selected index is: " + selectedIndex + 
                " and combined size is: " + amListSize;
        
        Node result = null;
        
        if( selectedIndex < amListSize )
        {
            result = amList.remove( selectedIndex );
            assert result != null : "error selecting node from AM list. Selected index is: " + selectedIndex;
            this.state = State.inAClass; // state remains the same
        }
        else // must be in the eoc list
        {
            result = eocList.remove( selectedIndex );
            assert result != null : "error selecting node from EoC list. " + "\n"
                                    + "Selected index is: " + selectedIndex + "\n"
                                    + "AM alist size is : " + amListSize + "\n"
                                    + "EoC list size is: " + eocListSize; 
            this.state = State.atEoC;
        }
        
        // return and remove the selected node in the working list
        return result;
    }
    
    /**
     * show the nodes of the current path
     * 21 June 2017
     */
    public void showPath( ) 
    {
        Iterator< Node > it = currentPath.iterator( );
        while( it.hasNext( ) )
        {
            Node n = it.next( );
            
            int number = n.getNumber( );
            String name = n.getName( );
            
            System.out.println( number + " " + name + "\n");
        }
        System.out.println("END OF PATH");
    }
    
   
    
}   // end class

//------- end file ----------------------------------------


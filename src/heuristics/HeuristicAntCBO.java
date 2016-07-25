/*
 * HeuristicAntCBO.java
 * Created 26 December 2012
 */

package heuristics;

/**
 * The CBO heuristic ant is a type of ant that uses heuristic information 
 * relating to the use matrix to help it generate a solution.
 * 
 * This class overrides two methods of the Ant class, namely:
 *      generateSolution( )
 *      selectNextNode( )
 * 
 * At 15 January 2013, this class does not address frozen classes
 * 
 * @author Chris Simons
 */

import config.AlgorithmParameters;
import engine.*;
import java.util.List;
import myUtils.Utility;
import pheromone.AlphaTable;
import softwareDesign.CLSClass;


public class HeuristicAntCBO extends Ant 
{
    private int[ ][ ] useMatrix;
    
    /** 
     * constructor
     * @param reference to list of attributes and methods
     * @param number of classes
     * @param reference to alpha table
     * @param list of classes that user elects to "freeze"
     * @param handling constraints boolean
     */
    public HeuristicAntCBO( 
        List< Node > nodes,
        int numberOfClasses,
        AlphaTable at,
        List< CLSClass > freezeList,
        boolean handlingConstraints,
        int[ ][ ] useMatrix )
    {
        super( nodes, numberOfClasses, at, freezeList, handlingConstraints );
        
        assert useMatrix != null;
        this.useMatrix = useMatrix;
    }
    
    /**
     * Generate a solution path through the environment.
     * 
     * This is essentially the same as the superclass method,
     * but is necessary in this class to get the call to the
     * overridden "selectNextNode( )"
     */
    @Override
    public void generateSolution( )
    {
        // create a new local solution path though the environment  
        Path path = new Path( new DesignPathRole( ) );
        
        // create a local working list of all attributes, methods 
        // and EoC markers in the search space
        List< Node > workingList = super.createWorkingList( );
        final int workingListSize = workingList.size( );
//        show( workingList );
        
        // the first node is always the nest
        Node current = new Nest( "nest", 0 );
        path.add( current );
       
        Node next = null;
       
        // then node by node, construct the remaining path 
        // depending on the attractiveness of each possibility
        while( workingList.isEmpty( ) == false )
        {
            // select the next node, according to attractiveness
            // influenced by the betaCBO value
//            next = this.selectNextNode( current, workingList );
            
            // add the next node to the solution path
            path.add( next );
            
            // and so the ant moves through the environment
            current = next;
        }
        
        // assert some invariance,
        assert workingList.isEmpty( );
        
        
        // the last node is always an end of class
        path.add( new EndOfClass( END_OF_CLASS, workingListSize + 1 ) );
        
//        System.out.println("path list------------------");
//        Iterator< Node > it = path.iterator( );
//        while( it.hasNext( ) )
//        {
//            Node n = it.next( );
//            System.out.print( "node: ");
//            System.out.println( n.getNumber( ) + " " + n.getName( ) );
//        }
//       
        final int pathSize = path.size( );
        assert pathSize == amList.size( ) + this.numberOfClasses + 1 : 
            "path size is: " + pathSize + 
            ", amlist size is " + amList.size( ) +
            ", number of classes is " + this.numberOfClasses;
                
        // when solution path is constructed, assign it to 
        // the current vertices instance variable 
        super.currentPath = path;
                
        // 28 May 2012 
        if( super.handlingConstraints == true )
        {
            super.valid = checkValidity( path );
        }
    }
    
    
     /**
     * select the next node based on pheromone 'attractiveness'
     * raised to the power of AlgorithmParameters.BETA_CBO
     * where there is a 'use'
     * 
     * @param current node
     * @param working list of nodes
     * @return the next node 
     */
//    @Override
//    protected Node selectNextNode( Node current, List< Node > workingList )
//    {
//        assert current != null;
//        assert workingList != null;
//        assert workingList.size( ) > 0;
//        final int workingListSize = workingList.size( );
//        
//        // handle the situation where the ant reaches the end of the path
//        // i.e there's only ONE node left in the list 
//        if( workingListSize == 1 )
//        {
//            Node result = workingList.remove( 0 );
//            assert workingList.isEmpty( );
//            return result;
//        }
//        
//        // else there must be more than one node to select from!
//        assert workingList.size( ) > 1;
//        
//        // and so let's choose it!
//        final int currentNodeNumber = current.getNumber( );
//        
//        // prepare a fitness proportionate node selection mechanism
//        // implemented by a "roulette wheel" approach
//        double[ ] probabilities = new double[ workingListSize ];
//        double sum = 0.0;
//        
//        // for each node in the working list,
//        // get the probability (from ALPHA table)
//        // raised to the power of BETA_CBO, where there's a use
//        //
//        // 'from' the current node (x axis in the table)
//        // 'to' all possible path (y axis in the table)
//        for( int i = 0; i < workingListSize; i ++ )
//        {
//            Node node = workingList.get( i );
//            int number = node.getNumber( );
//           
//            double alphaTableProb = super.alphaTable.getProbabilityAt( currentNodeNumber, number );
//            
//            // 18 Jan 2013 - here's the bit to use BETA_CBO
////            assert AlgorithmParameters.BETA_CBO >= 1.0;
//            double prob = 0.0;
//            
//            int x, y = 0;
//            
//            // if a method uses an attribute
//            if( current instanceof Method && node instanceof Attribute )
//            {
//                // index into use matrix is node number minus one 
//                x = current.getNumber( ) - HeuristicInformation.numberOfAttributes - 1;
//                y = node.getNumber( ) - 1;
//                
//                if( useMatrix[ x ][ y ] == 1 )
//                {
//                    prob = Math.pow( alphaTableProb, AlgorithmParameters.BETA_CBO );
//                    
//                    if( AlgorithmParameters.BETA_CBO == 1.0 ) { assert prob == alphaTableProb; }
//                }
//            }
//            // else if an attribute is used by a method
//            else if( current instanceof Attribute && node instanceof Method )
//            {
//                x = node.getNumber() - HeuristicInformation.numberOfAttributes - 1;
//                y = current.getNumber( ) - 1;
//                
//                if( useMatrix[ x ][ y ] == 1 )
//                {
//                    prob = Math.pow( alphaTableProb, AlgorithmParameters.BETA_CBO );
//                    
//                    if( AlgorithmParameters.BETA_CBO == 1.0 ) { assert prob == alphaTableProb; }
//                }
//            }
//            else
//            {
//                //in all other conditions, the heuristic is not applicable
//                prob = alphaTableProb;
//            }
//               
//            probabilities[ i ] = prob; 
//            
//            sum += probabilities[ i ];
//        }
//        
//        // now spin the "roulette wheel" to get a random number...
//        double random = Utility.getRandomInRange( 0.0, sum );
//        assert random >= 0.0;
//        assert random <= sum;
//        
//        double runningTotal = 0.0;
//        boolean finished = false;
//        int selectedIndex = 0;
//        
//        // select a node depending on probability
//        for( int j = 0; j < workingListSize && ! finished; j++ )
//        {
//            runningTotal += probabilities[ j ];
//            
//            if( runningTotal < random )
//            {
//                // do nothing, continue...
//            }
//            else
//            {
//                finished = true;
//                selectedIndex = j;
//            }
//        }
//        
////        System.out.print("out of loop and j is: " + j + "," );
////        System.out.println("and selected index is: " + selectedIndex );
////        System.out.println( " " );
//        
//        assert selectedIndex >= 0;
//        assert selectedIndex < workingListSize :
//                "selected index is: " + selectedIndex + 
//                " working list size is: " + workingListSize;
//        
//        // return and remove the selected node in the working list
//        return workingList.remove( selectedIndex );
//    }
//    
    
}   // end class

//------------ end of class ---------------------------------
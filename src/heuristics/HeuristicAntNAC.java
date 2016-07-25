/*
 * HeuristicAnt.java
 * Created 26 December 2012
 */

package heuristics;

/**
 * The NAC heuristic ant is a type of ant uses heuristic information 
 * relating to symmetry to help it generate a solution.
 * 
 * This class overrides two methods of the Ant class, namely:
 *      generateSolution( )
 *      createWorkingList( )
 * 
 * At 15 January 2013, this class does not address frozen classes
 * 
 * @author Chris Simons
 */

import engine.*;
import java.util.ArrayList;
import java.util.List;
import myUtils.Utility;
import pheromone.AlphaTable;
import softwareDesign.CLSClass;


public class HeuristicAntNAC extends Ant 
{
    /** 
     * constructor
     * @param reference to list of attributes and methods
     * @param number of classes
     * @param reference to alpha table
     * @param list of classes that user elects to "freeze"
     * @param handling constraints boolean
     */
    public HeuristicAntNAC( 
        List< Node > nodes,
        int numberOfClasses,
        AlphaTable at,
        List< CLSClass > freezeList,
        boolean handlingConstraints )
    {
        super( nodes, numberOfClasses, at, freezeList, handlingConstraints );
    }
    
    /**
     * generate a solution path through the environment
     */
    @Override
    public void generateSolution( )
    {
        // pre-condition: heuristic information must have been set up
        assert HeuristicInformation.idealNumberOfElementsPerClass > 0;
        
        // create a new local solution path though the environment  
        Path path = new Path( new DesignPathRole( ) );
        
        // create a local working list of all attributes and methods in the search space
        List< Node > workingList = createWorkingList( );
        
        // the first node is always the nest
        Node current = new Nest( "nest", 0 );
        path.add( current );
        
        // 14 Jan 2013 pseudocode design
        // firstly, prepare the class numbers
        // secondly, for each class:
        //              allocate correct number of elements according to pheromone levels
        //              allocate EoC marker
        //           end for
        
        // first, prepare the numbers of attributes and methods per class
        int[ ] elements = new int[ super.numberOfClasses ];
        
        for( int i = 0; i < elements.length; i++ )
        {
            elements[ i ] = HeuristicInformation.idealNumberOfElementsPerClass;
        }
        
        for( int j = 0; j < HeuristicInformation.remainderElements; j++ )
        {
            int random = Utility.getRandomInRange( 0, super.numberOfClasses - 1 );
            elements[ random ]++;
        }
        
        // secondly, allocate nodes to the solution path
        int EOCcounter = workingList.size( ) + 1;
        Node next;
        
        for( int i = 0; i < super.numberOfClasses; i++ )
        {
            for( int j = 0; j < elements[ i ]; j++ )
            {
                // select the next node, according to attractiveness
//                next = selectNextNode( current, workingList );
//            
//                // add the next node to the solution path
//                path.add( next );
//            
//                // and so the ant moves through the search space
//                current = next;
            }
            
            Node endOfClass = new EndOfClass( Ant.END_OF_CLASS, EOCcounter++ );
            path.add( endOfClass );
        }
        
        assert workingList.isEmpty( );
        assert EOCcounter == super.amList.size( )+ super.numberOfClasses + 1;
        
        // after a local solution path has been constructed, assign it to 
        // the current path instance variable 
        super.currentPath = path;
        
        // 28 May 2012 
        if( super.handlingConstraints == true )
        {
            super.valid = checkValidity( path );
        }
    }
    
     /**
     * create a working list of path elements containing
     * all methods, all attributes, but no end of class markers
     * @return working list of path elements
     */
    @Override
    protected List< Node > createWorkingList( )
    {
        List< Node > workingList = new ArrayList<  >( );
        
        // add the attributes and methods
        assert super.amList.isEmpty( ) == false;
        assert super.amListSize == amList.size( );
        
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
        
        // for testing
//        System.out.println( "this is the working list" );
//        for( Node n : workingList )
//        {
//            System.out.print( n.getName( ) + " " );
//            System.out.println( n.getNumber( ) );
//        }

        return workingList;
    }
    
}   
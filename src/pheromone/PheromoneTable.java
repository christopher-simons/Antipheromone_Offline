/*
 * PheromoneTable.java
 * 4 October 2011
 */

package pheromone;


import java.text.DecimalFormat;
import java.util.*;
import problem.*;
import engine.*;
import config.*;

/**
 *
 * @author Christopher Simons
 */

public class PheromoneTable
{
    /** initial probability value */
    private static final double INITIAL_PROBABILITY = 1.0;
    
    private static final double MMAS_INITIAL_PROBABILITY = AlgorithmParameters.MMAS_PHEROMONE_MAXIMUM;
    
    private static final double HEURISTIC_LOAD_FACTOR = 1.0;

    /**
     * 'x' coordinate is 'from'
     * 'y' coordinate is 'to'
     */
    private double[ ][ ] table; 
    
    /** list of attributes and methods */
    private List< Node > amList;
    
      
    /** size of both 'x' and 'y' dimensions of table */
    private final int size;
    
    /**
     * construct a pheromone table
     * @param attribute and method list
     * @param numberOfClasses 
     * @param problem controller
     */
    public PheromoneTable( 
        List< Node > amList, int numberOfClasses, ProblemController problemController )
    {
       assert amList != null;
       assert amList.size( ) > 0;
       assert numberOfClasses > 0;
       assert problemController != null;
       
       this.amList = amList;
//       this.numberOfClasses = numberOfClasses; no longer required 17 January 2012
       
       this.size = amList.size( ) + numberOfClasses + 1; // plus one for the nest
       
       table = new double[ size ][ size ];
       
       for( int i = 0; i < size; i++ )
       {
           for( int j = 0; j < size; j++ )
           {
               table[ i ][ j ] = 0.0;
           }
       }
       
       initialisePheromone( problemController );
       
       // for testing
//       show( );
    }  
    
    /**
     * initialise the pheromone with heuristics
     * @param problemController 
     */
    private void initialisePheromone( ProblemController problemController )
    {
        setInitialProbabilities( );
      
        setEndOfClassProbabilities( );
        
        setUseTableHeuristics( problemController );
    } 
   
    /**
     * set up the initial default probabilities
     */
    private void setInitialProbabilities( )
    {
        for( int i = 0; i < size; i++ )
        {
            for( int j = 0; j < size; j++ )
            {
                if( AlgorithmParameters.algorithm == AlgorithmParameters.MMAS )
                {
                    table[ i ][ j ] = MMAS_INITIAL_PROBABILITY;
                }
                else
                {
                    table[ i ][ j ] = INITIAL_PROBABILITY;
                }
           }
       }
       
       // but the same 'x' and 'y' coordinate is not
       // logically feasible, so set to 0.0
       for( int k = 0; k < size; k++ )
       {
           table[ k ][ k ] = 0.0;
       }
       
    }
    
    /**
     * set up the probabilities from one EndOfClass
     * to another to zero
     */
    private void setEndOfClassProbabilities( )
    {
       final int start = amList.size( );
       
       for( int i = start; i < size; i++ )
       {
           for( int j = start; j < size; j++ )
           {
                table[ i ][ j ] = 0.0;
           }
       }   
    }
    
    /**
     * set up the probabilities based on 
     * heuristics derived from the use matrix
     * 
     * Note that in the amList, attributes come first, then methods
     * 
     * Note also that row (i) is 'from', column (j) is 'to'
     * 
     * Although the use matrix defines which methods use which attributes,
     * in the probabilities tables, we have to go both ways, i.e.
     * from methods to attributes, and also from attributes to methods
     * 
     * @param problem controller
     */
    private void setUseTableHeuristics( ProblemController problemController )
    {
        assert problemController != null;
        
        final int actionListSize = problemController.getActionListSize( );
        final int datumListSize = problemController.getDatumListSize( );
        assert actionListSize + datumListSize == this.amList.size( );
        
        final int[ ][ ] useMatrix = problemController.getUseMatrix( );
        assert useMatrix != null;
        
        for( int i = 0; i < actionListSize; i++ )
        {
            for( int j = 0; j < datumListSize; j++ )
            {
                if( useMatrix[ i ][ j ] == 1 )
                {
                    // from methods to attributes firstly
                    table[ i ][ j + actionListSize - 1 ] *= HEURISTIC_LOAD_FACTOR;
                    
                    // from attributes to methods secondly
                    table[ i + datumListSize - 1 ][ j ] *= HEURISTIC_LOAD_FACTOR; 
                }
            }
        }
    }
    
    /**
     * show on output stream
     */
    public void show( )
    {
        DecimalFormat df = new DecimalFormat( "0.0" );
        
        System.out.println( "Pheromone Table " );
        System.out.println( "=============== " );
        
        for( int i = 0; i < size(); i++ )
        {
            System.out.print("\t" + "Row: " + i + ": " );
            for( int j = 0; j < size(); j++ )
            {
                System.out.print( df.format( table[ i ][ j ] ) );
                System.out.print(" ");
            }
            System.out.println( );
        }
    }
    
    /**
     * get a probability from x (from) y (to) coordinate
     * @return value of probability at x, y coordinate
     */
    public double getProbabilityAt( int x, int y )
    {
        assert x >= 0 && x < size : "x is: " + x + ", and y is: " + y;
        assert y >= 0 && y < size : "x is: " + x + ", and y is: " + y;
        
        return table[ x ][ y ];
    }
    
    /**
     * update a probability at x, y coordinate 
     * @param new value of probability at x, y coordinate
     */
    public void setProbabilityAt( int x, int y, double probability )
    {
        assert x >= 0 && x < size : "x is: " + x + ", and y is: " + y;
        assert y >= 0 && y < size : "x is: " + x + ", and y is: " + y;
        assert probability >= 0.0;

        table[ x ][ y ] = probability;
    }

    /**
     * @return the size
     */
    public int size() 
    {
        return size;
    }
    
}   // end class

//------- end file ----------------------------------------


/*
 * BestPathsMatrix.java
 * 7 August 2018
 */
package pheromone;

import config.AlgorithmParameters;
import engine.Node;
import engine.Path;
import java.util.Iterator;

/**
 * A matrix to record the best path(s) of MMAS
 * @author cl-simons
 */
public class BestPathsMatrix 
{
    /**
     * 'x' coordinate is 'from'
     * 'y' coordinate is 'to'
     */
    public int[ ][ ] bpMatrix; 
    
    /** size of both 'x' and 'y' dimensions of table */
    private final int size;
    
    /** pheromone strength */
    private final int strength;
    
    
    public BestPathsMatrix( final int size )
    {
        assert size > 0;
        this.size = size;
        
        bpMatrix = new int[ size ][ size ];
        for( int i = 0; i < size; i++ )
        {
            for( int j = 0; j < size; j++ )
            {
                bpMatrix[ i ][ j ] = 0; 
            }
        }
        strength = AlgorithmParameters.pheromoneStrength;
    }
    
    public void recordPath( final Path path )
    {
        assert path != null;
        assert path.size( ) == size : "path size is: " + path.size( );
        
        int from = 0;
        int to = 0;
        
        from = path.get( 0 ).getNumber( );
        
        for( int i = 0; i < size; i++ )
        {
            if( i == 0 )
            {
                // do nothing
            }
            else
            {
                to = path.get( i ).getNumber( );
                bpMatrix[ from ][ to ] += strength; 
                 // symmetrical update
                bpMatrix[ to ][ from ] += strength;
                
                from = to;
            }
        }
    }
    
    public void show( )
    {
        System.out.println( "" );
        for( int i = 0; i < size; i++ )
        {
            for( int j = 0; j < size; j++ )
            {
                System.out.print( bpMatrix[ i ][ j ] + " " );
            }
            System.out.println( "" );
        }
    }
}

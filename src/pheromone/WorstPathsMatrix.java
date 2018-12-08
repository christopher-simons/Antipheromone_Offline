/*
 * WorstPathsMatrix.java
 * 7 August 2018
 */
package pheromone;

import config.AlgorithmParameters;
import engine.Node;
import engine.Path;
import java.util.Iterator;

/**
 * A matrix to record the worst path(s) of MMAS
 * @author cl-simons
 */
public class WorstPathsMatrix 
{
    /**
     * 'x' coordinate is 'from'
     * 'y' coordinate is 'to'
     */
    public int[ ][ ] wpMatrix; 
    
    /** size of both 'x' and 'y' dimensions of table */
    private final int size;
    
    /** pheromone strength */
    private final int strength;
    
    
    public WorstPathsMatrix( final int size )
    {
        assert size > 0;
        this.size = size;
        
        wpMatrix = new int[ size ][ size ];
        for( int i = 0; i < size; i++ )
        {
            for( int j = 0; j < size; j++ )
            {
                wpMatrix[ i ][ j ] = 0; 
            }
        }
        strength = AlgorithmParameters.antipheromoneStrength;
    }
    
    public void recordPath( final Path path )
    {
        assert path != null;
        assert path.size( ) == size;
        
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
                wpMatrix[ from ][ to ] -= strength; 
                 // symmetrical update
                wpMatrix[ to ][ from ] -= strength;
                
                from = to;
            }
        }
        
//        while( it.hasNext( ) )
//        {
//            if( i == 0 || i == path.size( ) )
//            {
//                // do nothing
//            }
//            else
//            {
//                Node n = it.next( );
//                to = n.getNumber( );
//                
//                wpMatrix[ from ][ to ] -= strength; 
//                 // symmetrical update
//                wpMatrix[ to ][ from ] -= strength; 
//            }
//            
//            from = to;
//            i++;
//        }
    }
    
    public void show( )
    {
        System.out.println( "" );
        for( int i = 0; i < size; i++ )
        {
            for( int j = 0; j < size; j++ )
            {
                System.out.print( wpMatrix[ i ][ j ] + " " );
            }
            System.out.println( "" );
        }
    }
}

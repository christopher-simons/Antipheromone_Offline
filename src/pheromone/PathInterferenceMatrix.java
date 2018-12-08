/*
 * PathInterferenceMatrix.java
 * 7 August 2018
 */
package pheromone;

/**
 * A matrix to record the interference of 
 * best and worst path(s) of MMAS
 * @author cl-simons
 */
public class PathInterferenceMatrix 
{
    /**
     * 'x' coordinate is 'from'
     * 'y' coordinate is 'to'
     */
    private Interference[ ][ ] interferenceMatrix; 
    private int[ ][ ] interferenceScoreMatrix; 
    
    /** size of both 'x' and 'y' dimensions of table */
    private final int size;
    
    /** degree of interference, as a %age of all path edges */
    private double interference; 
    
    /** is the interference calculated? */
    private boolean calculated;
    
    public PathInterferenceMatrix( final int size )
    {
        assert size > 0;
        this.size = size;
        
        interferenceMatrix = new Interference[ size ][ size ];
        interferenceScoreMatrix = new int[ size ][ size ];
        
        for( int i = 0; i < size; i++ )
        {
            for( int j = 0; j < size; j++ )
            {
                interferenceMatrix[ i ][ j ] = new Interference( );
                interferenceScoreMatrix[ i ][ j ] = 0;
            }
        }
        
        interference = 0.0;
        calculated = false;
    }
    
    public void registerBestPathMatrix( final BestPathsMatrix bpm )
    {
        assert bpm != null;
        for( int i = 0; i < size; i++ )
        {
            for( int j = 0; j < size; j++ )
            {
                if( bpm.bpMatrix[ i ][ j ] > 0 )
                {
                    interferenceMatrix[ i ][ j ].best = true;
                    interferenceScoreMatrix[ i ][ j ] += bpm.bpMatrix[ i ][ j ];
                }
            }
        }
    }
    
    public void registerWorstPathMatrix( final WorstPathsMatrix wpm )
    {
        assert wpm != null;
        for( int i = 0; i < size; i++ )
        {
            for( int j = 0; j < size; j++ )
            {
                if( wpm.wpMatrix[ i ][ j ] < 0 )
                {
                    interferenceMatrix[ i ][ j ].worst = true;
                    interferenceScoreMatrix[ i ][ j ] += wpm.wpMatrix[ i ][ j ];
                }
            }
        }
    }
    
    public void calculateInterference( ) 
    {
        int interferenceCount = 0;
        
        for( int i = 0; i < size; i++ )
        {
            for( int j = 0; j < size; j++ )
            {
                if( interferenceMatrix[ i ][ j ].isInterference( ) )
                {
                    interferenceCount++;
                }
            }
        }
        
        this.interference = (double) interferenceCount / (double) (size * size);
        this.calculated = true;
    }
    
    
    public double getInterference( )
    {
        if( calculated == false )
        {
            calculateInterference( );
        }
        return this.interference;
    }
    
    
    public void showInterferenceMatrix( )
    {
        System.out.println( "" );
        
        for( int i = 0; i < size; i++ )
        {
            for( int j = 0; j < size; j++ )
            {
                System.out.print( interferenceMatrix[ i ][ j ].isInterference( ) + " " ); 
            }
            System.out.println( "" );
        }
    }
}

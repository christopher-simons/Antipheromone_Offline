/*
 * AlphaTable.java 
 * 27 April 2012
 */

package pheromone;

/**
 *
 * @author cl-simons
 */


public class AlphaMatrix
{
    /**
     * 'x' coordinate is 'from'
     * 'y' coordinate is 'to'
     */
    private double[ ][ ] alphaMatrix; 
    
    /** size of both 'x' and 'y' dimensions of table */
    private final int size;
    
    
    public AlphaMatrix( PheromoneMatrix pm, double alpha )
    {
        assert pm != null;
        assert alpha >= 0.0;
        assert alpha < 10.0; // some arbitrary upper bound
        size = pm.size( );
        assert size > 0 : "invalid pheromone matrix";
        
        alphaMatrix = new double[ size ][ size ];
        
        for( int i = 0; i < size; i++ )
        {
            for( int j = 0; j < size; j++ )
            {
                double temp = pm.getProbabilityAt( i, j );
                
                alphaMatrix[ i ][ j ] = Math.pow( temp, alpha );
            }
        }
    }

    public double getProbabilityAt( int i, int j )
    {
        assert i >= 0 : "invalid low pheromone table index i " + i;
        assert i < size: "invalid high pheromone table index i " + i;
        
        assert j >= 0 : "invalid low pheromone table index j " + i;
        assert j < size: "invalid high pheromone table index j " + i;
        
        return alphaMatrix[ i ][ j ];
    }
    
}   // end class

//----------- end of file --------------------------------------

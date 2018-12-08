/*
 * TSP_RAT99.java
 * 23 November 2018
 */
package problem;

/**
 * @author Chris Simons
 * Rattled grid (Pulleyblank) 99
 */

public class TSP_RAT99 extends TSP
{
    private static final String fileName = "rat99.tsp";
    public static final int NUMBER_OF_CITIES = 99;
    
    public TSP_RAT99( ) 
    {
        super( NUMBER_OF_CITIES, fileName );
    }
}

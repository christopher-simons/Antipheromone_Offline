/*
 * 22 November 2018
 * TSP.java
 */
package problem;

import java.io.*;
import java.text.DecimalFormat;


/**
 * Base class for all travelling salesman problem instances
 * @author Chris Simons
 */

public class TSP 
{
    private final String FILENAME;
    private final int NUMBER_OF_CITIES;
    
    private double distances[ ][ ];
    private BufferedReader br;
    
    public TSP( int numberOfCities, String fileName )
    {
        assert fileName != null;
        this.FILENAME = fileName;
        assert numberOfCities > 0;
        NUMBER_OF_CITIES = numberOfCities;
        
        distances = new double[ NUMBER_OF_CITIES ][ NUMBER_OF_CITIES ];
        for( int i = 0; i < NUMBER_OF_CITIES; i++ )
        {
            for( int j = 0; j < NUMBER_OF_CITIES; j++ )
            {
                distances[ i ][ j ] = 0.0;
            }
        }
        br = null;
        
    }
    
    public void configure( )
    {
        double x[ ]= new double[ NUMBER_OF_CITIES ];
        double y[ ]= new double[ NUMBER_OF_CITIES ];
        
        File file = new File( FILENAME );
        String line = null;
        int index = 0;
        
        final String dir = System.getProperty( "user.dir" );
        System.out.println( "current execution directory is: " + dir );
        
        try
        {
            FileReader fr = new FileReader( file );
            br = new BufferedReader( fr );
            
            // advance to the city data
            while( ! ( line=  br.readLine( ) ).contains( "NODE_COORD_SECTION" ) )
            {
                ; // do nothing
            }
            
            // read the city locations
            while( ! ( line = br.readLine( ) ).contains( "EOF" ) )
            {
                line = String.copyValueOf(line.toCharArray(), line.indexOf(" "), line.length()-line.indexOf(" "));
                line = line.trim();
                x[ index ] = Double.valueOf(line.split(" ")[0]).doubleValue();
                line = String.copyValueOf(line.toCharArray(), line.indexOf(" "), line.length()-line.indexOf(" "));
                line = line.trim();
                y[ index ] = Double.valueOf(line.split(" ")[0]).doubleValue();
                index++;
            }
        }
        catch( IOException ex )
        {
            System.err.println( ex );
        }
        
//        System.out.print( "x is: " );
//        for( int a = 0; a < NUMBER_OF_CITIES; a++ )
//        {
//            System.out.print( x[ a ] + " " );
//        }
//        System.out.println( " " );
//        System.out.println( " " );
//        System.out.println( " " );
//        
//        System.out.print( "y is: " );
//        for( int b = 0; b < NUMBER_OF_CITIES; b++ )
//        {
//            System.out.print( y[ b ] + " " );
//        }
//        System.out.println( " " );
//        System.out.println( " " );
//        System.out.println( " " );
        
        
        // calculate distances between cities
        for( int i = 0; i < NUMBER_OF_CITIES; i++ )
        {
            for( int j = i + 1; j < NUMBER_OF_CITIES; j++ )
            {
                distances[i][j]=Math.sqrt(Math.pow(x[i]-x[j],2)+Math.pow(y[i]-y[j],2));
                distances[j][i]=distances[i][j];
            }
        } 
    }
    
    public double[ ][ ] getDistances( )
    {
        assert this.distances != null;
        return this.distances;
    }
    
    
    public void showDistances( )
    {
        assert this.distances != null;
        DecimalFormat df = new DecimalFormat( "0.00" );
        
        int rowCounter = 0;
        for( int i = 0; i < this.NUMBER_OF_CITIES; i++ )
        {
            for( int j = 0; j < this.NUMBER_OF_CITIES; j++ )
            {
                System.out.print( df.format( distances[ i ][ j ] ) );
                System.out.print( " " );
            }
            System.out.println( "" );
            rowCounter++;
        }
        System.out.println("row counter is: " + rowCounter );
    }

}

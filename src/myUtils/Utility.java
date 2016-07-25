/*
 * Utility.java
 * 28 September 2011
 */

package myUtils;

/**
 *
 * @author cl-simons
 */

public class Utility 
{
    /**
     * calculate the factorial of a number
     * @param number as integer
     * @return factorial as double
     */
    public static double factorial( int number )
    {
        double result = 1.0;
        
        for( int i = 1; i <= number; i++ )
        {
            result *= i; 
        }
        
        return result;
    }
    
    /** get random integer in integer range 
     *  Taken from the Thomas Wu book, page 354
     *  @param lower bound as integer
     *  @param upper bound as integer
     *  @return a random integer in stated range 
     */
    public static int getRandomInRange( int lowerBound, int upperBound )
    {
        return (int) ( Math.floor( Math.random( ) * 
                    ( upperBound - lowerBound + 1 ) ) + lowerBound );
    }
    
     
    /** get random double in double range 
     *  Taken from the above
     *  @param lower bound as double
     *  @param upper bound as double
     *  @return a random double in stated range 
     */
    public static double getRandomInRange( double lowerBound, double upperBound )
    {
        assert lowerBound >= 0.0;
        assert upperBound >= 0.0;
        
        double result = 0.0;
        
        if( lowerBound == 0.0 && upperBound == 0.0 )
        {
            result = 0.0;
        }
        else
        {
            assert upperBound > lowerBound :
                "Upper bound is: " + upperBound +
                " lower bound is: " + lowerBound;
            double range = upperBound - lowerBound;

            // Math.random returns a pseaudorandom double value 
            // in the range 0.0 to 1.0
            result = ( Math.random( ) * range ) + lowerBound;
        }
        return result;
    }

    
    /**
     * Find the standard deviation of an array
     * of integer numbers.
     * @param numbers, the array of doubles
     * @return standard deviation as double
     */
    public static double standardDeviation( int[ ] numbers )
    {
        // calculate average first
        double total = 0.0;

        for( int i = 0; i < numbers.length; i++ )
        {
            total += numbers[ i ];
        }

        double average = total / (double) numbers.length;

        // then calculate standard deviation
        double squaredValuesSum = 0.0;
        for( int j = 0; j < numbers.length; j++ )
        {
            double number = (double) numbers[ j ];
            double temp = number - average;
            double squaredValue = temp * temp;
            squaredValuesSum += squaredValue;
        }

        double temp = squaredValuesSum / (double) numbers.length ;

        double sd = Math.sqrt( temp );

        return sd;
    }


     /**
     * Find the standard deviation of an array
     * of floating point numbers.
     * @param numbers, the array of doubles
     * @return standard deviation as double
     */
    public static double standardDeviation( double[ ] numbers )
    {
        // calculate average first
        double total = 0;

        for( int i = 0; i < numbers.length; i++ )
        {
            total += numbers[ i ];
        }

        double average = total / (double) numbers.length;

        // then calculate standard deviation
        double squaredValuesSum = 0.0;
        for( int j = 0; j < numbers.length; j++ )
        {
            double number = (double) numbers[ j ];
            double temp = number - average;
            double squaredValue = temp * temp;
            squaredValuesSum += squaredValue;
        }

        double temp = squaredValuesSum / (double) numbers.length ;

        double sd = Math.sqrt( temp );

        return sd;
    }

    /**
     * calculate the average of an array of long numbers
     * @param numbers
     * @return average 
     */
    public static double average( long[ ] numbers )
    {
        assert numbers != null;
        assert numbers.length > 0;
        
        long total = 0;
        
        for( int i = 0; i < numbers.length; i++ )
        {
            total += numbers[ i ];
        }
        return total / (double) numbers.length;
    }
    
}   // end class

//------- end file ----------------------------------------


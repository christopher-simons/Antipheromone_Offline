/*
 * PathComparator.java
 * 5 July 2017
 */
package myUtils;

/**
 *
 * @author cl-simons
 */

import java.util.Comparator;
import engine.Path;

public class PathComparatorForFcombined implements Comparator
{
    /**
     * sorts into ascending order
     * @param o1 - the first object to be compared.
     * @param o2 - the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first 
     *         argument is less than, equal to, or greater than the second.
     */
    @Override
    public int compare(Object o1, Object o2) 
    {
        Path p1 = (Path) o1;
        double Fcomb1 = p1.getCombined( );
        Path p2 = (Path) o2;
        double Fcomb2 = p2.getCombined( );
        
        int result = 0; 
        
        if( Fcomb1 < Fcomb2 )
        {
            result = -1;
        }
        else if( Fcomb1 > Fcomb2 )
        {
            result = 1;
        }
        else // must be equal
        {
            assert Fcomb1 == Fcomb2 : "impossible comparison";
            // result remains at zero
        }
              
        return result;
    }
}

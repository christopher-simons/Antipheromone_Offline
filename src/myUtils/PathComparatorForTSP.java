/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myUtils;

import engine.Path;
import java.util.Comparator;

/**
 *
 * @author cl-simons
 */
public class PathComparatorForTSP implements Comparator
{
    /**
     * sorts into ascending order
     * @param o1 - the first object to be compared.
     * @param o2 - the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first 
     *         argument is less than, equal to, or greater than the second.
     */
    @Override
    public int compare( Object o1, Object o2 ) 
    {
        assert o1 != null;
        assert o2 != null;
        
        Path p1 = null;
        Path p2 = null; 
        
        if( o1 instanceof Path )
        {
            p1 = (Path) o1;
        }
        else
        {
            assert false: "incompatible type for object 1 in PathComparatorForTSP";
        }
        
        if( o2 instanceof Path )
        {
            p2 = (Path) o2;
        }
        else
        {
            assert false: "incompatible type for object 2 in PathComparatorForTSP";
        }
        
        final double cost1 = p1.getTSPPathLength( );
        final double cost2 = p2.getTSPPathLength( );
        
        int result = 0; 
        
        if( cost1 < cost2 )
        {
            result = -1;
        }
        else if( cost1 > cost2 )
        {
            result = 1;
        }
        else // must be equal
        {
            assert cost1 == cost2 : "impossible comparison";
            // result remains at zero
        }
              
        return result;
    }
}
    
//------- end of class ----------------------------------------------

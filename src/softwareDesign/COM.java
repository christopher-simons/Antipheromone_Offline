/*
 * COM.java
 *
 * Created on 18 August 2004, 11:39
 * Greatly refactored and simplified 11 Oct 2011
 *
 */
   
package softwareDesign;

/**
 * Evaluates the fitness of an objects,
 * based on "Cohesiveness of Methods" metric,
 * see Harrison, Councell, amd Nithi (1998),
 * Empirical Software Engineering, 3, pp 255-273.
 * @author  clsimons
 */

import java.util.*;
import problem.*;
import engine.Attribute;
import engine.Method;


public class COM 
{
    /** the value of the COM metric */
    private double value;

    /** the number of uses for the class */
    private int numberOfUses;
    
    /** reference to the class being evaluated */
    private CLSClass c;
    
    /** reference to the table of datum usage */
    private SortedMap< String, List< CLSDatum > > useTable;
    
    private static final double UNINITIALISED = -9999.99999;
    
    
    /** Creates a new instance of COM with a use table */
    public COM( CLSClass c,
                SortedMap< String, List< CLSDatum > > ut ) 
    {
        value = UNINITIALISED;
        numberOfUses = 0;
        
        assert c != null;
        this.c = c;
        assert ut != null;
        useTable = ut;
    }
    
    /** return the value of the COM metric */
    public double getValue( ) 
    {   
        // check the value has been evaluated
        assert value != UNINITIALISED : 
                                "value not evaluated yet";
        // before returning it...
        return value; 
    }
    
    /** 
     * evaluate COM fitness with use table 
     * see pp 259-260 of the article for definition of metric
     */
    public void evaluate( ) 
    {
        assert useTable != null :
            "trying to evaluate COM without valid use table";
        
        int totalNumberOfMethods = c.getNumberOfMethods( );
        int numberOfAttributes = c.getNumberOfAttributes( );

        // handle either zero attributes or zero methods
        if( zeroAttributesOrZeroMethods( ) == true )
        {
            value = 0.0;
            return;     // go no further
        }
        
        int[ ] attributeUseCounts = new int[ numberOfAttributes ];
        for( int i : attributeUseCounts ) { i = 0; }
        
        Iterator< Attribute > iterAttributes = 
            c.getAttributeListIterator( );
        
        int loopCount = 0;
        while( iterAttributes.hasNext( ) ) // while there are more attributes
        {
            String attributeName = iterAttributes.next( ).getName( );
            
            Iterator< Method > iterMethods = 
                c.getMethodListIterator( );
  
            while( iterMethods.hasNext( ) )
            {
                String methodName = iterMethods.next( ).getName( );
                
                // get the list of datum used by the action 
                List< CLSDatum > list = useTable.get( methodName );
                assert( list != null ); // it should be there!!!
                
                for( CLSDatum d : list )
                {
                    if( d.getName( ).equals( attributeName ) )
                    {
                        attributeUseCounts[ loopCount ]++;
                        numberOfUses++;
                        // if we've found it, no point in going on
                        break;
                    }
                }
            
            }   // end while there are more methods for this attribute
        
            loopCount++;
            
        }   // end while there are more attributes     
        
        double temp = 0.0;
        for( int index = 0; index < attributeUseCounts.length; index++ )
        {
            if( attributeUseCounts[ index ] > 0 )
            {
                temp += (double) attributeUseCounts[ index ] / totalNumberOfMethods;
            }
        }
        
        value = temp / numberOfAttributes;  
        
        // interactive / preference function
//        function( );
    }
    
    
    private boolean zeroAttributesOrZeroMethods( )
    {
        boolean result = false;
        
        if( c.isWithoutAttributes( ) )
        {
            result = true;
        }
        else if( c.isWithoutMethods( ) )
        {
            result = true;
        }
        
        return result;
    }
    
    
    /** this is the function that takes our preferences of 
     *  cohesion...
     *  
     *  ? hook into the interactive element
     *
     *  We may wish to in some sense weight the COM value 
     *  for the "size" of the class. 
     *  This is to ensure that, for example, one object of
     *  two attributes and two methods is considered fitter 
     *  than two objects on one attribute and one method.
     *
     *  It could be that we wish to multiply by various functions of 
     *  the number of attributes and methods
     *  e.g. the power 1 - by the number
     *       the power 1/2 - by the square root of the number
     *       the log (base e or 10) of the number
     *
     */
    private void function( )
    {
        final int totalNumberOfMethods = c.getNumberOfMethods( );
        assert totalNumberOfMethods > 0;
        final int numberOfAttributes = c.getNumberOfAttributes( );
        assert numberOfAttributes > 0;
        final int numberOfMandA = 
            totalNumberOfMethods + numberOfAttributes;
        assert numberOfMandA > 0;
        
        // do nothing i.e. keep raw COM
        
        /*  July 2005
         *  weight the fitness value according to the number
         *  of attributes and methods.
         *  Multiply the COM value by the combined number
         *  of attributes and methods
         */
        value *= numberOfMandA;        
        
        /** 12 May 2006
         *  wieght by the square root of he combined number
         *  of attributes and methods
         */
//         value *= Math.sqrt( numberOfMandA );
        
        /** 22 May 2006 
         *  weight by the number of uses in the class
         */
//        value *= numberOfUses;
        
        /** 22 May 2006 
         *  weight by the number of uses in the class
         */
//         value *= Math.sqrt( numberOfUses );
    }
}
  
//------- end of file -------------------------------------
/*
 * EleganceDesign.java
 * 2 May 2011
 */

package softwareDesign;

/**
 * EleganceDesign is a class providing elegance metrics
 *
 * @author clsimons
 */


import engine.*;
import java.util.*;
import myUtils.Utility;
import problem.CLSDatum;


public class EleganceDesign extends SoftwareDesign
{
    /**
     * Standard distribution of attributes and
     * methods among the classes of a design.
     */
    private double eleganceNAC;

   /**
     * Standard distribution of ratio of attributes to methods
     * inside the classes of a design.
     */
    private double eleganceATMR;
    
    
    private double eleganceModularity;
    

    /**
     * Constructor (default)
     */
    public EleganceDesign( )
    {
        eleganceNAC = 0.0;
        eleganceATMR = 0.0;
        eleganceModularity = 0.0;
    }

    /**
     * Get value of elegance NAC - Numbers Among Classes 
     * standard deviation of number of 
     * attributes and methods among classes.
     * @return NAC elegance 
     */
    public double getEleganceNAC( ) { return eleganceNAC; }

    /**
     * Get value of elegance ATMR - Attributes To Methods Ratio
     * standard deviation of of ratio of attributes to methods 
     * within the classes of a design.
     * @return ATMR elegance
     */
    public double getEleganceATMR( ) { return eleganceATMR; }

    public double getEleganceModularity( ) { return eleganceModularity; }
    /**
     * Calculate the standard deviation of attributes
     * and methods among the classes.
     */
    public void calculateEleganceNAC( )
    {
        int[ ] attributeNumbers = new int[ this.numberOfClasses ];
        int[ ] methodNumbers = new int[ this.numberOfClasses ];

//        for( int i = 0; i < numberOfClasses; i++  )
//        {
//            attributeNumbers[ i ] = classes[ i ].getNumberOfAttributes( );
//            methodNumbers[ i ] = classes[ i ].getNumberOfMethods( );
//        }

        int i = 0;
        for( CLSClass c : classList )
        {
            attributeNumbers[ i ] = c.getNumberOfAttributes( );
            methodNumbers[ i ] = c.getNumberOfMethods( );
            i++;
        } 
        
        final double attributesSD = Utility.standardDeviation( attributeNumbers );
        final double methodsSD = Utility.standardDeviation( methodNumbers );
        
        if( attributesSD == 0.0 )
        {
//            System.out.println( "attributes SD is zero!" );
        }
        
        if( methodsSD == 0.0 )
        {
//            System.out.println( "methods SD is zero!" );
        }

        // take the average of the two standard deviations
        eleganceNAC = ( attributesSD + methodsSD ) / 2.0;
//        System.out.println( "NAC elegance is: " + eleganceNAC );
    }

    

   
    /**
     * Calculate the standard deviation of the ratio
     * of attributes to methods within the classes of a design.
     */
    public void calculateEleganceATMR( )
    {
        double[ ] ratios = new double[ this.numberOfClasses ];

        for( int n = 0; n < numberOfClasses; n++ )
        {
            ratios[ n ] = 0.0;
        }

        int i = 0;
        for( CLSClass c : classList )
        {
            final int atts = c.getNumberOfAttributes( );
            final int methods = c.getNumberOfMethods( );
            
            // manual Select Cruises has one class without methods,
            // and one class without attributes
            // ensure no divide by zero
//            assert atts > 0 : "no attributes!";
//            assert methods > 0 : "no methods!";
            
            if( atts == 0 || methods == 0 )
            {
                ratios[ i ] = 10.0; // arbitrary inelegant value
            }
            else
            {
                ratios[ i ] = (double) atts / (double) methods; 
            }
            
            i++;
        }
        
//        for( int i = 0; i < numberOfClasses; i++ )
//        {
//            int atts = classes[ i ].getNumberOfAttributes( );
//            int methods = classes[ i ].getNumberOfMethods( );
//
//            // 21 March 2012
//            // manual Select Cruises has one class without methods,
//            // and one class without attributes
//            // ensure no divide by zero
////            assert atts > 0 : "no attributes!";
////            assert methods > 0 : "no methods!";
//            
//            if( atts == 0 || methods == 0 )
//            {
//                ratios[ i ] = 6.0; // highest inelegant value in design problem
//            }
//            else
//            {
//                ratios[ i ] = (double) atts / (double) methods; 
//            }
//        }

        eleganceATMR = Utility.standardDeviation( ratios );
    }

    /** 
     * calculate the modularity elegance
     * @param use table
     */ 
    public void calculateEleganceModularity( 
        SortedMap< String, List< CLSDatum > > useTable )
    {
        int[ ][ ] classCoupleTable = 
            new int[ this.numberOfClasses ][ this.numberOfClasses ];
        
        for (int i = 0; i < classCoupleTable.length; i++) 
        {
            for (int j = 0; j < classCoupleTable.length; j++) 
            {
                classCoupleTable[ i ][ j ] = 0;
            }
        }
        
        int i = 0;
        
        for( CLSClass c : this.classList )
        {
            for( Method m : c.getMethodList( ) )
            {
                List< CLSDatum > dList = useTable.get( m.getName( ) );
                
                for( CLSDatum d : dList )
                {
                    List< Attribute > aList = c.getAttributeList( );
                    
                    boolean found = false;
                    
                    for( Attribute a : aList )
                    {
                        if( d.getName( ).equals( a.getName( ) ) )
                        {
                            found = true;
                        }
                    }
                    
                    if( found == false ) // must be an "external couple"
                    {
                        // now find the target!!!
                        final int target = findTarget( d.getName( ) );
                        assert i != target : "index can't be target!";
                        assert target < classList.size( ) : "target is " + target;
                        
                        classCoupleTable[ i ][ target ]++;
                    }
                }
            }
            
            i++;
        }
        
        // now calculate the measure for each class
        int[ ] classCouples = new int[ this.numberOfClasses ];
        for( int a = 0; a < classCouples.length; a++ )
        {
            classCouples[ a ] = 0;
        }
        
        for( int j = 0; j < this.numberOfClasses; j++ )
        {
            for( int k = 0; k < this.numberOfClasses; k++ )
            {
                if( j != k )
                {
                    classCouples[ j ] += classCoupleTable[ j ][ k ];
                }
            }
        }
        
        // at last, now calculate the Modularity Elegance
        double temp = myUtils.Utility.standardDeviation( classCouples );
        this.eleganceModularity = temp;
//        System.out.println( "elegance modularity is: " + this.eleganceModularity );
    }
    
    
    /** find the target class that contains an attribute name
     *  @param datum name as string
     *  @return index into the class array list of target class
     */
    private int findTarget( String datumName )
    {
        assert datumName != null;
        int result = 0;
        boolean found = false;

        for( int i = 0; i < classList.size( ) && found == false; i++ )
        {
            CLSClass c = classList.get( i );

            Iterator< Attribute > aIt = c.getAttributeListIterator( );

            while( aIt.hasNext( ) && found == false )
            {
                String name = aIt.next( ).getName( );
                if( name.equals( datumName ) )
                {
                    found = true;
                    result = i;
                }
            }
        }

        return result;
    }
}   // end class

//------- end of file -------------------------------------

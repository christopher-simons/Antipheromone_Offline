/*
 * originally SoftwareDesignChromosome.java
 * Created on 08 July 2004, 12:57
 * 
 * refactored to SoftwareDesign.java
 * 11 October 2011
 */

package softwareDesign;


import engine.Attribute;
import engine.Method;
import java.util.*;
import problem.*;


public class SoftwareDesign
{
    /** reference to list of classes */
    protected List< CLSClass > classList;
    
    /** number of classes in this chromosome */
    protected int numberOfClasses;
    
    /** the external coupling value */
    private double externalCouplingValue; 
    
    /** is external coupling calculated? */
    private boolean externalCouplingCalculated;
    
    /** average COM cohesion for the design */
    private double averageCOMCohesion;
    
    /** constructor */
    public SoftwareDesign( ) 
    {
        numberOfClasses = 0;
        classList = new ArrayList< CLSClass >( );
        
        externalCouplingValue = 0.0;
        externalCouplingCalculated = false;
        
        averageCOMCohesion = 0.0;
    }
    
    /**
     * get number of classes
     * @return number of classes as integer
     */
    public int getNumberOfClasses( ) 
    {
        return numberOfClasses;
    }
    
    
    public void add( CLSClass c )
    {
        assert c != null;
        classList.add( c );
        
        numberOfClasses++;
    }
            
    
    /**
     * get class at an index
     * @param index as integer
     * @return reference to CLSClass
     */
    public CLSClass getClassAt( int index ) 
    {
        assert index >= 0 : "index is " + index;
        assert index < numberOfClasses : "index is " + index;
        assert index < classList.size( );
        return classList.get( index );
    }
    
    
    public Iterator< CLSClass > iterator( )
    {
        assert classList.isEmpty( ) == false;
        return classList.iterator( );
    }
    
    
    public List< CLSClass > getClassList( )
    {
        assert this.classList.isEmpty( ) == false;
        return this.classList;
    }
    
    
    /** isAttributeIn - is an attribute present in the Chromosome?
     *  @param attribute name as String
     *  @return true if present in Chromosome, false otherwise
     */
    public boolean isAttributeIn( String attributeName ) {
        assert attributeName != null;
        boolean result = false;
        for( CLSClass c : classList ) {
            Iterator< Attribute> it = c.getAttributeListIterator( );
            while( it.hasNext( ) ) {
                String name = it.next( ).getName( );
                if( name.equals( attributeName ) ) {
                    result = true;  // we've found it!
                    break;
                }
            }
        }
        return result;
    }
    
    /** isMethodIn - is a method present in the Chromosome?
     *  @param method name as String
     *  @return true if present in Chromosome, false otherwise
     */
    public boolean isMethodIn( String methodName ) {
        assert methodName != null;
        boolean result = false;
        for( CLSClass o : classList ) {
            Iterator< Method > it = o.getMethodListIterator( );
            while( it.hasNext( ) ) {
                String name = it.next( ).getName( );
                if( name.equals( methodName ) ) {
                    result = true;  // we've found it!
                    break;
                }
            }
        }
        return result;
    }
   
    /** calculate the "external" coupling in a software design
     *  where an external couple is the use of an attribute
     *  outside the class containing the method
     *
     *  First checks for either zero attributes or zero methods. 
     * 
     *  If not, external coupling is:
     *      number of external couples / number of uses
     *
     *  @param the use table
     */ 
    public void calculateExternalCoupling( 
        SortedMap< String, List< CLSDatum > > useTable )
    {
        
        assert useTable.isEmpty( ) == false;
        int numberOfUses = 0;
        
        assert classList != null;
        int externalCoupleCounter = 0;
        // Jim 27-2-12 added count of internal couples and cassert later to make sure they all add up
        int internalCoupleCounter = 0; 
        
        for( int index = 0; index < classList.size( ); index++ )
        {
            CLSClass c = classList.get( index );
            
//                      if( zeroAttributesAndZeroMethods( c ) == true )
            if( zeroAttributesAndZeroMethods( c ) == true )
            {
                externalCouplingValue = 1.0;    // worst possible value
                externalCouplingCalculated = true;
                
                return;         // GO NO FURTHER!!!!!!
            }
            
            Iterator< Method > mIt = c.getMethodListIterator( );
            
            while( mIt.hasNext( ) )
            {
                Method m = mIt.next( );
                List< CLSDatum > list = useTable.get( m.getName( ) );
                assert list != null;
                numberOfUses += list.size( );
                
                for( int j = 0; j < list.size( ); j++ )
                {
                    CLSDatum d = list.get( j );
                    final String datumName = d.getName( );
                    
                    Iterator< Attribute > aIt = c.getAttributeListIterator( );
                    boolean found = false;
                    
                    // is this datum to be found in this class??
                    while( aIt.hasNext( ) && found == false )
                    {
                        Attribute a = aIt.next( );
                        final String attributeName = a.getName( );

                        if( attributeName.equals( datumName ) )
                        {
                            // we have an "internal couple" - great!
                            found = true;
                            internalCoupleCounter++;
                        }
                    }
                    
                    if( found == false ) // must be an "external couple"
                    {
                        externalCoupleCounter++;
                    }
                }
            }
            
        }   // end for each class
        
        assert externalCoupleCounter <= numberOfUses:
            "external couples: " + externalCoupleCounter +
            " number of uses: " + numberOfUses;
        assert externalCoupleCounter + internalCoupleCounter == numberOfUses: 
            " external = " + externalCoupleCounter + 
            " internal = " + internalCoupleCounter +
            " number of uses: " + numberOfUses;
        double ecc = (double) externalCoupleCounter;
        double nou = (double) numberOfUses;
        
        externalCouplingValue = ecc / nou;
        externalCouplingCalculated = true;
    }
    
    /**
     * check to see if this class has either zero attributes
     * or zero methods
     * @param the class, c
     * @return true if zero attributes or methods, false otherwise
     */
    public boolean zeroAttributesOrZeroMethods( CLSClass c )
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

    public boolean zeroAttributesAndZeroMethods( CLSClass c )
    {
        boolean result = false;
        if( c.isWithoutAttributes( ) &&  c.isWithoutMethods( ) )
        {
            result = true;
        }
      
        return ( result);    
    }
    
    
    /** get the external coupling value 
     *  @return external coupling value as double
     */
    public double getExternalCouplingValue( )
    {
        assert externalCouplingCalculated == true;
        return externalCouplingValue;
    }
    
    
    @Override
    public String toString( )
    {
        String result = "";
        int i = 0;
        
        for( CLSClass c : classList )
        {
            result += "for class " + i++ + ":";
            result += c.toString( ); 
        }
        return result;
    }
    
    /** calculate COM cohesion fitness value */
    public void calculateCOMFitness(
            SortedMap< String, List< CLSDatum > > useTable ) 
    {
        double runningTotal = 0.0;
        double temp = 0.0;
        
        for( CLSClass c : classList ) 
        {
            COM com = new COM( c, useTable );
            com.evaluate( );
            temp = com.getValue( );
//            System.out.println("cohesion is : " + temp );
            runningTotal += temp;
            c.setCOMFitness( temp );
        }
        
        // average COM Cohesion fitness is the average of
        // its classes' individual fitnesses
        averageCOMCohesion = runningTotal / numberOfClasses;
    }
    
    /**
     * get average COM cohesion for the design
     * @return average COM cohesion
     */
    public double getAverageCOMCohesion( )
    {
        assert averageCOMCohesion >= 0.0;
        return averageCOMCohesion;
    }
    
}   // end of class

//------- end of file -------------------------------------

/*
 * Class.java
 *
 * Created on 08 July 2004, 13:05
 * Greatly refactored for ACO, 11 October 2011
 */

package softwareDesign;
 
/**
 * @author  Christopher Simons
 */
 

import java.util.*;
import problem.CLSDatum;
import engine.Attribute;
import engine.Method;


public class CLSClass 
{
    /** maximum number of methods in a class */
    private static final int MAX_METHODS = 50;
    
    /** maximum number of attributes in a class */
    private static final int MAX_ATTRIBUTES = 50;
    
      
    /** ArrayList of methods */
    private List< Method > methodList;
    
    /** an array of attributes */
    private List< Attribute > attributeList;

    /** COM cohesion fitness of the object */
    private double COMFitness;
    
    /** Creates a new instance of Object */
    public CLSClass() 
    {
        methodList = new ArrayList< Method >( );
        attributeList = new ArrayList< Attribute >( );
        
        COMFitness = 0.0;
    }
    
    /** add a method to the object
     *  @param reference to the method to be added
     */
    public void add( Method m )
    {
        assert m != null;
        methodList.add( m );
    }

    
    /** 19 July 2007 */
    public Method getMethodAt( int index )
    {
        assert index >= 0;
        assert methodList != null;
        assert index < methodList.size( );
        return methodList.get( index );
    }
    
    /** is without attributes 
     *  @return truth as boolean
     */
    public boolean isWithoutAttributes( )
    {
        assert( attributeList != null );
        return attributeList.isEmpty( );
    }
    
    /** is without methods 
     *  @return truth as boolean
     */
    public boolean isWithoutMethods( )
    {
        assert( methodList != null );
        return methodList.isEmpty( );
    }
    
    public Attribute getAttributeAt( int index )
    {
        assert attributeList != null;
        assert index >= 0;
        assert index <= attributeList.size( );
        
        return attributeList.get( index );
    }
    
    /** add an attribute to the object
     *  @param reference to the attribute to be added
     */
    public void add( Attribute a )
    {
        assert( a != null );
        attributeList.add( a );
    }
   
    
    /** get method list iterator
     *      -- read only???
     *  @return iterator of method list
     */
    public Iterator< Method > getMethodListIterator( )
    {
        assert( methodList != null );
        return methodList.iterator( );
    }
    
    
    /** get attribute list iterator
     *      -- read only???
     *  @return iterator of attribute list
     */
    public Iterator< Attribute > getAttributeListIterator( )
    {
        assert( attributeList != null );
        return attributeList.iterator( );
    }
    
    
    /** get number of methods in the object
     *  @return number of methods as integer
     */
    public int getNumberOfMethods( )
    {
        assert( methodList != null );
        return methodList.size( );
    }
    
    /** get number of attributes in the object
     *  @return number of attributes as integer
     */
    public int getNumberOfAttributes( )
    {
        assert( attributeList != null );
        return attributeList.size( );
    }
    
    /** get the COM fitness of the object
     *  @return COM fitness value
     */
    public double getCOMFitness() {
        return COMFitness;
    }

    /** set the value of COM Fitness 
     *  @param COM fitness value 
     */
    public void setCOMFitness( double COMFitness) 
    {
        assert COMFitness >= 0.0;
        this.COMFitness = COMFitness;
    }
    

    public void calculateCOMFitness( 
        SortedMap< String, List< CLSDatum > > useTable )
    {
        assert useTable != null;
        
        COM com = new COM( this, useTable );
        com.evaluate( );
        this.COMFitness = com.getValue( );
//        System.out.println( "\t" + "cohesion is : " + COMFitness );
    }        
            
            
            
    /** get the class to a string based on attributes and methods
     *  @return class as string
     */
    @Override
    public String toString( )
    {
        String result = "Attributes: ";
        
//        Iterator< Attribute > ita = getAttributeListIterator( );
//        while( ita.hasNext( ) )
//        {
//            Attribute a = ita.next();
//            result += a.getName( );
//            result += ", ";
//        }

        for( int i = 0; i < this.attributeList.size( ); i++ )
        {
            result += this.attributeList.get( i ).getName( );
            
            if( i != this.attributeList.size( ) - 1 )
            {
                result += ", ";
            }
            else
            {
                result += ".  ";
            }
        }    
            
            
        result += "Methods: ";
//        Iterator< Method > itm = getMethodListIterator( );
//        while( itm.hasNext( ) )
//        {
//            Method m = itm.next();
//            result += m.getName( );
//            result += "( ), ";
//        }

        for( int i = 0; i < this.methodList.size( ); i++ )
        {
            result += this.methodList.get( i ).getName( );
            result += "()";
            
            if( i != this.methodList.size( ) - 1 )
            {
                result += ", ";
            }
            else
            {
                result += ".";
            }
        } 
        return result;
    }
    
    public List< Method > getMethodList( )
    {
        return this.methodList;
    }
    
    public List< Attribute > getAttributeList( )
    {
        return this.attributeList;
    }
}

//------- end of file -------------------------------------

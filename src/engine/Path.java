/*
 *  Path.java
 *  created 11 October 2011
 */

package engine;

/**
 *
 * @author Christopher Simons
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Path
{
    /** list of components representing a path */
    private List< Node > list;
    
    private Role role;
    
    private int dominationCount;
    
    private int weightedDominationCount;
    
    private boolean valid; // i.e. complies with constraints
    
    /** constructor for a path */
    public Path( Role role )
    {
        list = new ArrayList< Node >( );
        assert role != null;
        this.role = role;
        dominationCount = 0; 
        valid = false;
    }
    
   
    /**
     * add a node to the path
     * @param node 
     */
    public void add( Node node )
    {
        assert node != null;
        assert list != null;
        list.add( node );
    }
    
    /**
     * get an iterator over the components
     * @return iterator over the components
     */
    public Iterator< Node > iterator( )
    {
        assert list != null;
        return list.iterator( ); 
    }

    
    /**
     * does the path have any vertices?
     * @return true if path has no vertices, false otherwise
     */
    public boolean isEmpty( )
    {
        return list.isEmpty( ); 
    }
    
    public int size( )
    {
        return list.size( );
    }
    
    public Node get( int index )
    {
        assert index >= 0;
        assert index < list.size( );
        
        return list.get( index );
    }
    
    public void show( )
    {
        for( Node n : this.list )
        {
            System.out.print( n.getNumber( ) + " " );
            System.out.print( n.getName( ) + " " );
        }
        System.out.println( );
    }

  
    public Role getRole( )
    {
        return role;
    }
    
    
    public Role.Distinction getRoleDistinction( )
    {
        return role.getDistinction( );
    }
    
    
    public void setCBO( double CBO )
    {
        assert role.getDistinction( ) == Role.Distinction.design;
        
        role.setExternalCoupling( CBO );
    }

    
    public void setAverageCOMCohesion( double averageCOMCohesion )
    {
        assert role.getDistinction( ) == Role.Distinction.design;
        
        role.setAverageCOMCohesion( averageCOMCohesion );
    }

    public void setCohesion( double cohesion )
    {
      //jimcoimmented out 22-2-12
        //assert role.getDistinction( ) == Role.Distinction.classs;
        
        role.setCohesion( cohesion );
    }

    /** 
     * get the external coupling value for the vertex
     * assuming its Role is Distinction.Design
 
     * @return external coupling as double
     */
    public double getCBO(  ) 
    { 
        assert role.getDistinction( ) == Role.Distinction.design;
        
        return role.getExternalCoupling( ); 
    }

    /** 
     * get the cohesion value for the vertex
     * assuming its Role is Distinction.Classs
     * 
     * @return cohesion as double
     */
    public double getCohesion( ) 
    { 
//jim commented out 22-2-12
        //assert role.getDistinction( ) == Role.Distinction.classs;
        
        return role.getCohesion( );
    }
   
    public void setEleganceNAC( double eleganceNAC )
    {
        assert role.getDistinction( ) == Role.Distinction.design;
        
        role.setEleganceNAC( eleganceNAC );
    }

    public double getEleganceNAC(  )
    {
        assert role.getDistinction( ) == Role.Distinction.design;
        
        return role.getEleganceNAC( );
    }
    
    
    public void setEleganceATMR( double eleganceATMR )
    {
        assert role.getDistinction( ) == Role.Distinction.design;
        
        role.setEleganceATMR( eleganceATMR );
    }

    
    public double getEleganceATMR(  )
    {
        assert role.getDistinction( ) == Role.Distinction.design;
        
        return role.getEleganceATMR( );
    }
    
    public void setEleganceModularity( double eleganceModularity )
    {
        assert role.getDistinction( ) == Role.Distinction.design;
        
        role.setEleganceModularity( eleganceModularity );
    }

    
    public double getEleganceModularity(  )
    {
        assert role.getDistinction( ) == Role.Distinction.design;
        
        return role.getEleganceModularity( );
    }

    /**
     * @return the dominationCount
     */
    public int getDominationCount() 
    {
        return dominationCount;
    }

    /**
     * @param dominationCount the dominationCount to set
     */
    public void setDominationCount(int dominationCount) 
    {
        this.dominationCount = dominationCount;
    }

    /**
     * @return the weightedDominationCount
     */
    public int getWeightedDominationCount() 
    {
        return weightedDominationCount;
    }
    
    public void incrementDominationCount( )
    {
        this.dominationCount++;
    }

    public void resetDominationCount( )
    {
        this.dominationCount = 0;
    }

    /**
     * @param weightedDominationCount the weightedDominationCount to set
     */
    public void setWeightedDominationCount(int weightedDominationCount) 
    {
        this.weightedDominationCount = weightedDominationCount;
    }
    
    //28 November 2015
    public double getCombined( ) 
    {
        return this.role.getCombined( );
    }

    // 29 November 2015
    public void setCombined( double combined) 
    {
        this.role.setCombined( combined );
    }

    /**
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

}   // end class

//------- end file ----------------------------------------


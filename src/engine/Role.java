/*
 * Role.java
 * created 12 October 2011
 */

package engine;

/**
 *
 * @author Christopher Simons
 */

public abstract class Role 
{
    public enum Distinction { design, classs }
    
    protected Distinction distinction;
    
    public Role( Distinction distinction )
    {
        this.distinction = distinction;
    }
    
    public Distinction getDistinction( )
    {
        return distinction;
    }
    
    public void setExternalCoupling( double externalCoupling ) { }
    public double getExternalCoupling( ) { return 0.0; }
    
    public void setAverageCOMCohesion( double averageCOMCohesion ) { }
    public double getAverageCOMCohesion( ) { return 0.0; }
    
    public void setCohesion( double cohesion ) { }
    public double getCohesion( ) { return 0.0; }
    
    public void setEleganceNAC( double eleganceNAC ) { }
    public double getEleganceNAC( ) { return 0.0; }
    
    public void setEleganceATMR( double eleganceNAC ) { }
    public double getEleganceATMR( ) { return 0.0; }
    
    public void setEleganceModularity( double eleganceModularity ) { }
    public double getEleganceModularity( ) { return 0.0;}
    
    // 29 November 2015
    public void setCombined( double combined ) { }
    public double getCombined( ) { return 0.0;}
    
    
}   // end class

//------- end file ----------------------------------------


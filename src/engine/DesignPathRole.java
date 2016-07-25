/*
 * DesignPathRole.java
 * created 11 October 2011
 */

package engine;

/**
 *
 * @author Christopher Simons
 */

public class DesignPathRole extends Role
{
    private double externalCoupling;
    
    private double averageCOMCohesion;
    
    private double eleganceNAC;
    
    private double eleganceATMR;
    
    private double eleganceModularity;
    
    // 28 November 2015
    private double combined;
    
    public DesignPathRole( )
    {
        super( Distinction.design );
        
        externalCoupling = -99.99; // nonsense value
        averageCOMCohesion = -99.99; // nonsense value
        eleganceNAC = -99.99;
        eleganceATMR = -99.99;
        eleganceModularity = -99.99;
        combined = -99.99;
    }

    /**
     * @return the externalCoupling
     */
    @Override
    public double getExternalCoupling( ) 
    {
        return externalCoupling;
    }

    /**
     * @param externalCoupling the externalCoupling to set
     */
    @Override
    public void setExternalCoupling( double externalCoupling ) 
    {
        this.externalCoupling = externalCoupling;
    }

    /**
     * @return the averageCOMCohesion
     */
    @Override
    public double getAverageCOMCohesion( ) 
    {
        return averageCOMCohesion;
    }

    /**
     * @param averageCOMCohesion the averageCOMCohesion to set
     */
    @Override
    public void setAverageCOMCohesion( double averageCOMCohesion ) 
    {
        this.averageCOMCohesion = averageCOMCohesion;
    }

    /**
     * @return the eleganceNAC
     */
    @Override
    public double getEleganceNAC() 
    {
        return eleganceNAC;
    }

    /**
     * @param eleganceNAC the eleganceNAC to set
     */
    @Override
    public void setEleganceNAC(double eleganceNAC) 
    {
        this.eleganceNAC = eleganceNAC;
    }

    /**
     * @return the eleganceATMR
     */
    @Override
    public double getEleganceATMR() 
    {
        return eleganceATMR;
    }

    /**
     * @param eleganceATMR the eleganceATMR to set
     */
    @Override
    public void setEleganceATMR(double eleganceATMR) 
    {
        this.eleganceATMR = eleganceATMR;
    }

    /**
     * @return the eleganceModularity
     */
    @Override
    public double getEleganceModularity() 
    {
        return eleganceModularity;
    }

    /**
     * @param eleganceModularity the eleganceModularity to set
     */
    @Override
    public void setEleganceModularity(double eleganceModularity) 
    {
        this.eleganceModularity = eleganceModularity;
    }

    @Override
    public double getCombined( ) 
    {
        return combined;
    }

    @Override
    public void setCombined( double combined) 
    {
        this.combined = combined;
    }
    
}   // end class

//------- end file ----------------------------------------


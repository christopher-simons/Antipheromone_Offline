/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package learning;

/**
 *
 * @author cl-simons
 */
public class IterationInformation 
{
    /** mean average percentage error */
    public double mape; 
    
    /** mean absolute deviation */
    public double mad;
    
    public int designerEvaluation;
    
    public boolean archived;
    
    public boolean classFrozen;
    
    public boolean classUnfrozen;
    
    public boolean halting;
    
    public IterationInformation( )
    {
        mape = -999.0;
        mad = -999.0;
        designerEvaluation = -999; // arbitrary value for not set
        archived = false;
        classFrozen = false;
        classUnfrozen = false;
        halting = false;
    }
}

// ---- end of file ------------------------------

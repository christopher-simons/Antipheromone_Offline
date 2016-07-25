/*
 * RegressionAgent.java
 * 10 July 2012
 */

package learning;

import java.util.Hashtable;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Observation;
import net.sourceforge.openforecast.models.MultipleLinearRegressionModel;

/**
 *
 * @author cl-simons
 */

public class RegressionAgent 
{
    /** the independent variables for multiple linear regression */
    public static final String[ ] independentVariables =
    {
        "fCBO", "fNAC", "fATMR"
    };
    
    /** the OpenForecast class*/
    private static MultipleLinearRegressionModel mlrm =
       new MultipleLinearRegressionModel( independentVariables );
           
    /** the dataset of observations */
    private static DataSet ds = new DataSet( );
    
    /** add an observation to the dataset */
    public static void addObservation( Observation o )
    {
        assert o != null;
        ds.add( o );
    }
    
    /** 
     * Initializes the coefficients to use for this regression model. 
     * The coefficients are derived so as to give the best fit hyperplane 
     * for the given data set. 
     */
    public static void init( )
    {
        mlrm.init( ds );
    }
    
    /** get the Akaike Information Criteria */
    public static double getAIC( ) { return mlrm.getAIC( ); }

    /** get the Bias (Arithmetic mean of the errors) */
    public static double getBias( ) { return mlrm.getBias( ); }

    /** get the mean absolute deviation */
    public static double getMAD( ) { return mlrm.getMAD( ); }

    /** get the mean absolute percentage error */
    public static double getMAPE( ) { return mlrm.getMAPE( ); }

    /** get the mean square of the errors */
    public static double getMSE( ) { return mlrm.getMSE( ); }

    /** get the sum of the absolute errors */
    public static double getSAE( ) { return mlrm.getSAE( ); }
    
    /** get the intercept */
    public static double getIntercept( ) { return mlrm.getIntercept( ); }

    /** get the coefficient for CBO */
    public static double getCBOCoefficient( ) 
    {
        Hashtable< String, Double > ht = mlrm.getCoefficients( );
        return ht.get( independentVariables[ 0 ] );
    }

    /** get the coefficient for CBO */
    public static double getNACCoefficient( ) 
    {
        Hashtable< String, Double > ht = mlrm.getCoefficients( );
        return ht.get( independentVariables[ 1 ] );
    }

    /** get the coefficient for CBO */
    public static double getATMRCoefficient( ) 
    {
        Hashtable< String, Double > ht = mlrm.getCoefficients( );
        return ht.get( independentVariables[ 2 ] );
    }




}   // end class

//--- end of file -----------------------------------------

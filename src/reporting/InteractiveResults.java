/*
 * InteractiveResults.java
 * Created 25 October 2011
 */

package reporting;

/**
 * Holder for:
 *  - best design coupling
 *  - average design coupling
 *  - best class cohesion
 *  - average class cohesion
 * 
 * Visibility is public for this 'structure'
 * 
 * @author Christopher Simons
 */

       
import java.io.*;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import config.*;


public class InteractiveResults 
{
    public static final int DUMMY_INTERACTION_VALUE = -999;
    
    /** number of iterations of ant colony before designer halting */
    private int haltingIteration;
    
    /** final values of each metric, for each iteration */
       
    //Jim Smith added 22-2-12
    private double[ ] bestCBO; 
    
    // Chris 4 May 2012
    private double[ ] bestNAC;
    private double[ ] bestATMR;
    
    //Chris 3 August 2012
    private double[ ] bestMod;
     
    // Chris 19 June 2012
    private double[ ] iterationTimes; 
    private boolean[ ] solutionsArchived;
    private boolean[ ] classFrozen;
    private boolean[ ] classUnfrozen;
    
    // Chris 26 July 2012
    private int[ ] designerEvaluations;
    
    /** mean average deviation */
    private double[ ] mad;
    
    private double[ ] weightCBO;
    private double[ ] weightNAC;
    private double[ ] weightATMR;
    
    private boolean[ ] halting;
    
     /** for results output formatting */
    private DecimalFormat df;

    /**
     * constructor
     * @param number of iterations 
     * @param number Of trials 
     */
    public InteractiveResults( )
    {
        this.haltingIteration = AlgorithmParameters.NUMBER_OF_ITERATIONS;
        
        bestCBO = new double[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
        bestNAC = new double[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
        bestATMR = new double[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];        
        bestMod = new double[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
        
        iterationTimes = new double[ AlgorithmParameters.NUMBER_OF_ITERATIONS ]; 
        solutionsArchived = new boolean[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
        classFrozen = new boolean[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
        classUnfrozen = new boolean[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
        
        designerEvaluations = new int[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
        
        mad = new double[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
        
        weightCBO = new double[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
        weightNAC = new double[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
        weightATMR = new double[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
        
        halting = new boolean[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
    
        for( int i = 0; i < AlgorithmParameters.NUMBER_OF_ITERATIONS; i++ )
        {
            bestCBO[ i ] = 0.0;
            bestNAC[ i ] = 0.0;
            bestATMR[ i ] = 0.0;
            designerEvaluations[ i ] = 0;
            mad[ i ] = 0.0;
            weightCBO[ i ] = 0.0;
            weightNAC[ i ] = 0.0;
            weightATMR[ i ] = 0.0;
            iterationTimes[ i ] = 0;
            solutionsArchived[ i ] = false;
            classFrozen[ i ] = false;
            halting[ i ] = false;
        }
        
        df = new DecimalFormat( "0.000" );
    }
    
    /**
     * transfer results for ACO iteration
     */
    public void transferResults(
        int iteration,
        double bestCBO,
        double bestNAC,
        double bestATMR,
        double bestMod,
        double weightCBO,
        double weightNAC,
        double weightATMR, 
        double mad,
        int designerEvaluation,
        double interactionTime,
        boolean solutionArchived,
        boolean classFrozen,
        boolean classUnfrozen,
        boolean halting )
    {
        assert iteration >= 0;
        assert iteration <= AlgorithmParameters.NUMBER_OF_ITERATIONS;
        
        this.bestCBO[ iteration ] = bestCBO;
        this.bestNAC[ iteration ] = bestNAC;
        this.bestATMR[ iteration ] = bestATMR;
        this.bestMod[ iteration ] = bestMod;
        this.weightCBO[ iteration ] = weightCBO;
        this.weightNAC[ iteration ] = weightNAC;
        this.weightATMR[ iteration ] = weightATMR;
        this.mad[ iteration ] = mad;
        this.designerEvaluations[ iteration ] = designerEvaluation;
        this.iterationTimes[ iteration ] = interactionTime;   
        this.solutionsArchived[ iteration ] = solutionArchived;
        this.classFrozen[ iteration ] = classFrozen;
        this.classUnfrozen[ iteration ] = classUnfrozen;
        this.halting[ iteration ] = halting;
        
        if( halting == true )
        {
            haltingIteration = iteration;
        }
    }
    
    
    /**
     * showRawResults on console 
     */
    public void showRawResults( )
    {
        System.out.println( 
            "RAW RESULTS: number of iterations is: " +
            haltingIteration );
        
       
        for( int i = 0; i < haltingIteration; i++ )
        {
            System.out.println( "\t" + "Iteration: " + i );
            System.out.println( "Best CBO is: " + this.bestCBO[ i ] );
            System.out.println( "Best NAC is: " + this.bestNAC[ i ] );
            System.out.println( "Best ATMR is: " + this.bestATMR[ i ] );
            System.out.println( "Best Modularity is: " + this.bestMod[ i ] );
        }
    }
    
    /**
     * write final results to chosen folder path
     */
    public void writeFinalResultsToFile( )
    {
        String filename = Parameters.outputFilePath;
        filename += "\\";
        filename += "IACOdata_";
        filename += Parameters.designer;
        filename += "_";
        filename += Parameters.problemNumber;
        filename += "_";
        filename += Parameters.episodeNumber;
        filename += ".dat";

        System.out.println("filename is: " + filename );
        
        // set up the output files
        PrintWriter out1 = null;
        
        boolean append = true;
        try 
        {
            // don't want to overwrite existing result files
            out1 = new PrintWriter( new FileWriter( new File( filename ), append) );
        } 
        catch( IOException ex ) 
        {
            Logger.getLogger(InteractiveResults.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println( "Can't open " + filename );
            return;
        }

        // write external coupling values
        for( int i = 0; i <= haltingIteration; i++ )
        {
            out1.println( 
                  Parameters.designer + " " +
                  Parameters.problemNumber + " " +
                  Parameters.episodeNumber + " " +
                  ( Parameters.freezing ? 1 : 0 ) + " " +
                  Parameters.colourMetaphor + " " +
                  i + " " +
                  df.format( this.bestCBO[ i ] ) + " " + 
                  df.format( this.bestNAC[ i ] ) + " " + 
                  df.format( this.bestATMR[ i ] ) + " " +
                  df.format( this.bestMod[ i ] ) + " " +  
                  df.format( this.weightCBO[ i ] ) + " " +
                  df.format( this.weightNAC[ i ] ) + " " +
                  df.format( this.weightATMR[ i ] ) + " " +
                  df.format( this.mad[ i ] ) + " " +
                  this.designerEvaluations[ i ] + " " +
                  df.format( this.iterationTimes[ i ] ) + " " +
                  ( this.solutionsArchived[ i ] ? 1 : 0 ) + " " +
                  ( this.classFrozen[ i ] ? 1 : 0 ) + " " +
                  ( this.classUnfrozen[ i ] ? 1 : 0 ) + " " + 
                  ( this.halting[ i ] ? 1: 0 ) );
        }
        
        out1.close( );
    }
     
}   // end class

//------- end file ----------------------------------------


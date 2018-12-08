/*
 * Results.java
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


import config.AlgorithmParameters;
import config.Parameters;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import myUtils.Utility;


public class BatchResults 
{   
    private static final String BEST_COUPLING_FILE_NAME = "BestCoupling.dat";
    private static final String AVERAGE_COUPLING_FILE_NAME = "AverageCoupling.dat";
    private static final String BEST_NAC_FILE_NAME = "BestNAC.dat";
    private static final String BEST_ATMR_FILE_NAME = "BestATMR.dat";
    private static final String BEST_EM_FILE_NAME = "BestEM.dat";
    private static final String SPSS_BEST_OUTPUT_NAME = "AntBestResults.dat";
    private static final String SPSS_AVERAGE_OUTPUT_NAME = "AntAverageResults.dat";
    
    private static final String HEURISTIC_NAC_OUTPUT_NAME = "HeuristicResults.dat";
    
    // 17 Nov 2015
    private static final String BEST_COMBINED_FILE_NAME = "BestCOMBINED.dat";
    
    // 28 June 2017
    private static final String RETRIES_ATTEMPTS_FILE_NAME = "RetriesAttempts.dat";
    
    // 29 June 2017
    private static final String COST_FILE_NAME = "Cost.dat";
    
    // 8 August 2018
    private static final String INTERFERENCE_FILE_NAME = "Interference.dat";
    
    // 28 August 2018
    private static final String SNAPSHOT_FILE_NAME = "Snapshot.dat";
    
    // 3 September 2018
    private static final String INTERFERENCE_ITERATIONS_FOR_SPSS_FILE_NAME = "InterferenceIterationsForSPSS.dat";
    private static final String INTERFERENCE_ITERATIONS_FOR_GNUPLOT_FILE_NAME = "InterferenceIterationsForGnuPlot.dat";
    
    // 18 Septembe 2018
    private static final String BEST_TSP_FILE_NAME = "BestTSP.dat";
    
    /** number of iterations of ant colony */
    private final int numberOfIterations;
    
    /** number of trials conducted */
    private final int numberOfRuns; 
    
    /**
     * RAW average colony design coupling in ACO search
     * [ trial ][ iteration ]
     */
    public double[ ][ ] averageDesignCouplingOverRuns;

    /**
     * RAW standard deviation of average design coupling in ACO search
     * [ trial ][ iteration ]
     */
    public double[ ][ ] averageDesignCouplingOverRunsSD;
    
    /** 
     * RAW best design coupling in ACO search at this iteration
     * [ trial ][ iteration ]
     */
    public double[ ][ ] bestDesignCouplingOverRuns;
    
    /**
     * standard deviation of best design coupling in ACO search
     * [ trial ][ iteration ]
     */
    public double[ ][ ] bestDesignCouplingOverRunsSD;

    /**
     * RAW average colony class cohesion in ACO search
     * [ trial ][ iteration ]
     */
    public double[ ][ ] averageClassCohesionOverRuns;
    
    /**
     * RAW standard deviation of class cohesion in ACO search
     * [ trial ][ iteration ]
     */
    public double[ ][ ] averageClassCohesionOverRunsSD;
    
    /**
     * best class cohesion in ACO search
     * [ trial ][ iteration ]
     */
    public double[ ][ ] bestClassCohesionOverRuns;
    
      /**
     * standard deviation of best class cohesion in ACO search
     * [ trial ][ iteration ]
     */
    public double[ ][ ] bestClassCohesionOverRunsSD;

    
    /** final values of best design coupling, for each iteration */
    private double[ ] finalBestDesignCoupling;  
    
    /** standard deviation of final values of best design coupling */
    private double[ ] finalBestDesignCouplingSD;
    
    /** final values of average design coupling, for each iteration */
    private double[ ] finalAverageDesignCoupling;  
    
    /** standard deviation of final values of average design coupling */
    private double[ ] finalAverageDesignCouplingSD;
    
    /** final values of best class, for each iteration */
    private double[ ] finalBestClassCohesion;  
    
    /** standard deviation of final values of average design coupling */
    private double[ ] finalBestClassCohesionSD;
    
    /** final values of best design coupling, for each iteration */
    private double[ ] finalAverageClassCohesion;  
    
    /** standard deviation of final values of average design coupling */
    private double[ ] finalAverageClassCohesionSD;
    
    /** for results output formatting */
    private DecimalFormat df;

    
    //Jim Smith added 22-2-12
    //1D arrays to hold best fitness found for each run, 
    //and when it was found
    private double[ ] bestCBO; 
    private int [ ] whenCBOfound;
    
    // Chris 4 May 2012, 31 August 2012
    private double[ ] bestNAC;
    private int[ ] whenNACFound;  // Chris 17 January 2013
    
     // 30 November 2015
    private double[ ] bestCombined;
    private int[ ] whenCombinedFound;
    
    // 2 Feb 2016
    public int[ ] maxNumberOfInvalids;
    
    
    private double[ ] bestATMR;
    private int[ ] whenATMRFound; // Chris 30 November 2015 too
    
    private double[ ] bestEM;
    
     // 4 May 2012 Chris, and again 31 August 2012
    /** Raw elegance results [ run ][ iteration ] */
    public double[ ][ ] bestEleganceNACOverRuns;
    public double[ ][ ] bestEleganceNACOverRunsSD;
    public double[ ][ ] bestCombinedOverRuns; // Chris 30 November 2015
    public double[ ][ ] bestEleganceATMROverRuns;
    public double[ ][ ] bestEleganceATMROverRunsSD;
    public double[ ][ ] bestEleganceModularityOverRuns;
    public double[ ][ ] bestEleganceModularityOverRunsSD;

    
      
    /** Final elegance results */
    private double[ ] finalBestEleganceNAC;
    private double[ ] finalBestEleganceNACSD;
    private double[ ] finalBestEleganceATMR;
    private double[ ] finalBestEleganceATMRSD;
    private double[ ] finalBestEleganceModularity;
    private double[ ] finalBestEleganceModularitySD;
    
    
    // 28 June 2017 for adaptive antipheromone
    public int[ ][ ] retriesOverRuns;
    public double[ ][ ] averageAttemptsOverRuns; 
    
    private double[ ] averageRetries;
    private double[ ] averageOfAverageAttempts;
    
    private double retriesStdDev[ ];
    private double attemptsStdDev[ ];
   
    // 29 June 2017 for adaptive antipheromone
    private double bestFcomb[ ];
    private double bestFcombStdDev[ ];
    
    // 8 August 2018 for interference investigations
    public double interference[ ][ ]; 
    
    // 23 August 2018 
    public double[ ] bestCombinedValueAt50OverRuns;
    public double[ ] bestCombinedValueAt100OverRuns;
    public double[ ] bestCombinedValueAt150OverRuns;
    public double[ ] bestCombinedValueAt200OverRuns;
    public double[ ] bestCombinedValueAt300OverRuns;
    public double[ ] bestCombinedValueAt400OverRuns;
    
    public double[ ] areaAt50OverRuns;
    public double[ ] areaAt100OverRuns;
    public double[ ] areaAt150OverRuns;
    public double[ ] areaAt200OverRuns;
    public double[ ] areaAt300OverRuns;
    public double[ ] areaAt400OverRuns;
    
    // 3 Septebmber 2018
    public double[ ] averageInterference;
    public double[ ] averageInterferenceStdDev;
    
    // 18 September 2018
    public double[ ] bestTSPLength;
    public int[ ] whenBestTSPLengthFound;
    
    /**
     * constructor
     * @param number of iterations 
     * @param number Of runs 
     */
    public BatchResults( int iterations, int runs )
    {
        assert iterations > 0;
        assert runs > 0;
        
        this.numberOfIterations =  iterations;
        this.numberOfRuns = runs;
        
        averageDesignCouplingOverRuns = 
            new double[ numberOfRuns ][ numberOfIterations ];
        averageDesignCouplingOverRunsSD = 
            new double[ numberOfRuns ][ numberOfIterations ];
        bestDesignCouplingOverRuns = 
            new double[ numberOfRuns ][ numberOfIterations ];
        bestDesignCouplingOverRunsSD = 
            new double[ numberOfRuns ][ numberOfIterations ];
        averageClassCohesionOverRuns = 
            new double[ numberOfRuns ][ numberOfIterations ];
        averageClassCohesionOverRunsSD = 
            new double[ numberOfRuns ][ numberOfIterations ];
        bestClassCohesionOverRuns = 
            new double[ numberOfRuns ][ numberOfIterations ];
        bestClassCohesionOverRunsSD = 
            new double[ numberOfRuns ][ numberOfIterations ];
        bestEleganceNACOverRuns = 
            new double[ numberOfRuns ][ numberOfIterations ];
        bestEleganceNACOverRunsSD = 
            new double[ numberOfRuns ][ numberOfIterations ];

        // 30 November 2015 
        bestCombinedOverRuns = new double[ numberOfRuns ][ numberOfIterations ];
        
        bestEleganceATMROverRuns = 
            new double[ numberOfRuns ][ numberOfIterations ];
        bestEleganceATMROverRunsSD = 
            new double[ numberOfRuns ][ numberOfIterations ];
        bestEleganceModularityOverRuns = 
            new double[ numberOfRuns ][ numberOfIterations ];
        bestEleganceModularityOverRunsSD = 
            new double[ numberOfRuns ][ numberOfIterations ];
    
    
        finalBestDesignCoupling = new double[ numberOfIterations ];
        finalBestDesignCouplingSD = new double[ numberOfIterations ];
        
        finalAverageDesignCoupling = new double[ numberOfIterations ];
        finalAverageDesignCouplingSD = new double[ numberOfIterations ];
        
        finalBestClassCohesion = new double[ numberOfIterations ];
        finalBestClassCohesionSD = new double[ numberOfIterations ];
        
        finalAverageClassCohesion  = new double[ numberOfIterations ];
        finalAverageClassCohesionSD  = new double[ numberOfIterations ];
        
        bestCBO = new double[ numberOfRuns ];
        whenCBOfound = new int[ numberOfRuns ];
        bestNAC = new double[ numberOfRuns ];
        whenNACFound = new int[ numberOfRuns ];
        bestCombined = new double[ numberOfRuns ]; // Chris 30 Nov 2015
        whenCombinedFound = new int[ numberOfRuns ]; // Chris 30 Nov 2015
        maxNumberOfInvalids = new int[ numberOfRuns ]; // Chris 2 Feb 2016
        bestATMR = new double[ numberOfRuns ];
        whenATMRFound = new int[ numberOfRuns ];
        bestEM = new double[ numberOfRuns ];
         
        finalBestEleganceNAC = new double[ numberOfIterations ];
        finalBestEleganceNACSD= new double[ numberOfIterations ];
        finalBestEleganceATMR= new double[ numberOfIterations ];
        finalBestEleganceATMRSD = new double[ numberOfIterations ];
        finalBestEleganceModularity = new double[ numberOfIterations ];
        finalBestEleganceModularitySD = new double[ numberOfIterations ];
        
        for( int i = 0; i < numberOfRuns; i++ )
        {
            bestCBO[ i ] = 0.0;
            whenCBOfound[ i ] = 0;
            bestNAC[ i ] = 0.0;
            whenNACFound[ i ] = 0;
            bestATMR[ i ] = 0.0;
            bestEM[ i ] = 0.0;
            
            for( int j = 0; j < numberOfIterations; j++ )
            {
                averageDesignCouplingOverRuns[ i ][ j ] = 0.0; 
                averageDesignCouplingOverRunsSD[ i ][ j ] = 0.0; 

                bestDesignCouplingOverRuns[ i ][ j ] = 0.0;
                bestDesignCouplingOverRunsSD[ i ][ j ] = 0.0;
               
                averageClassCohesionOverRuns[ i ][ j ] = 0.0;
                averageClassCohesionOverRunsSD[ i ][ j ] = 0.0;

                bestClassCohesionOverRuns[ i ][ j ] = 0.0;
                bestClassCohesionOverRunsSD[ i ][ j ] = 0.0;
                
                bestEleganceNACOverRuns[ i ][ j ] = 0.0;
                bestEleganceNACOverRunsSD[ i ][ j ] = 0.0;
                
                bestCombinedOverRuns[ i ][ j ] = 0.0;
                
                bestEleganceATMROverRuns[ i ][ j ] = 0.0;
                bestEleganceATMROverRunsSD[ i ][ j ] = 0.0;
                bestEleganceModularityOverRuns[ i ][ j ] = 0.0;
                bestEleganceModularityOverRunsSD[ i ][ j ] = 0.0;
            }
        }

        for( int k = 0; k < numberOfIterations; k++ )
        {
            finalBestDesignCoupling[ k ] = 0.0;
            finalBestDesignCouplingSD[ k ] = 0.0;
            
            finalAverageDesignCoupling[ k ] = 0.0;
            finalAverageDesignCouplingSD[ k ] = 0.0;
            
            finalBestClassCohesion[ k ] = 0.0;
            finalBestClassCohesionSD[ k ] = 0.0;
            
            finalAverageClassCohesion[ k ] = 0.0;
            finalAverageClassCohesionSD[ k ] = 0.0;
            
            finalBestEleganceNAC[ k ] = 0.0;
            finalBestEleganceNACSD[ k ] = 0.0;
            finalBestEleganceATMR[ k ] = 0.0;
            finalBestEleganceATMRSD[ k ] = 0.0;
            finalBestEleganceModularity[ k ] = 0.0;
            finalBestEleganceModularitySD[ k ] = 0.0;
        }
        
        df = new DecimalFormat( "0.000" );
       
        // 28 June 2017 for adaptive antipheromone
        retriesOverRuns = new int[ numberOfRuns ][ numberOfIterations ];
        averageAttemptsOverRuns = new double[ numberOfRuns ][ numberOfIterations ];
        
        averageRetries = new double[ numberOfIterations ];
        averageOfAverageAttempts = new double[ numberOfIterations];
        
        retriesStdDev = new double[ numberOfIterations ];
        attemptsStdDev = new double[ numberOfIterations ];
        
        for( int l = 0; l < numberOfRuns; l++ )
        {
            for( int m = 0; m < numberOfIterations; m++ )
            {
                retriesOverRuns[ l ][ m ] = 0;
                averageAttemptsOverRuns[ l ][ m ] = 0.0;
            }
            
            averageRetries[ l ] = 0.0;
            averageOfAverageAttempts[ l ] = 0.0;
        }
    
        for( int m = 0; m < numberOfIterations; m++ )
        {
            retriesStdDev[ m ] = 0.0;
            attemptsStdDev[ m ] = 0.0;
        }
   
        // 29 June 2017 for adaptive antipheromone
        bestFcomb = new double[ numberOfIterations ];
        bestFcombStdDev= new double[ numberOfIterations ];
        for( int n = 0; n < numberOfIterations; n++ )
        {
            bestFcomb[ n ] = 0.0;
            bestFcombStdDev[ n ] = 0.0;
        }
        
        // 8 August 2018
        interference = new double[ numberOfRuns ][ numberOfIterations];
        for( int m = 0; m < numberOfRuns; m++ )
        {
            for( int n = 0; n < numberOfIterations; n++ )
            {
                interference[ m ][ n ] = 0.0;
            }
        }
        
        // 28 August 2018
        bestCombinedValueAt50OverRuns = new double[ numberOfRuns ];
        bestCombinedValueAt100OverRuns = new double[ numberOfRuns ];
        bestCombinedValueAt150OverRuns = new double[ numberOfRuns ];
        bestCombinedValueAt200OverRuns = new double[ numberOfRuns ];
        bestCombinedValueAt300OverRuns = new double[ numberOfRuns ];
        bestCombinedValueAt400OverRuns = new double[ numberOfRuns ];
        areaAt50OverRuns = new double[ numberOfRuns ];
        areaAt100OverRuns = new double[ numberOfRuns ];
        areaAt150OverRuns = new double[ numberOfRuns ];
        areaAt200OverRuns = new double[ numberOfRuns ];
        areaAt300OverRuns = new double[ numberOfRuns ];
        areaAt400OverRuns = new double[ numberOfRuns ];
        
        for( int x = 0; x < numberOfRuns; x++ )
        {
            bestCombinedValueAt50OverRuns[ x ] = 0.0;
            bestCombinedValueAt100OverRuns[ x ] = 0.0;
            bestCombinedValueAt200OverRuns[ x ] = 0.0;
            bestCombinedValueAt300OverRuns[ x ] = 0.0;
            bestCombinedValueAt400OverRuns[ x ] = 0.0;
            areaAt50OverRuns[ x ] = 0.0;
            areaAt100OverRuns[ x ] = 0.0;
            areaAt150OverRuns[ x ] = 0.0;
            areaAt200OverRuns[ x ] = 0.0;
            areaAt300OverRuns[ x ] = 0.0;
            areaAt400OverRuns[ x ] = 0.0;
        }
        
        // 3 September 2018
        averageInterference = new double[ this.numberOfIterations ];
        averageInterferenceStdDev = new double[ this.numberOfIterations ];
        
        for( int y = 0; y < this.numberOfIterations; y++ )
        {
            averageInterference[ y ] = 0;
            averageInterferenceStdDev[ y ] = 0;
        }
        
        // 18 September 2018 for TSP
        bestTSPLength = new double[ numberOfIterations ];
        whenBestTSPLengthFound = new int[ numberOfIterations ];
        for( int z = 0; z < numberOfIterations; z++ )
        {
            bestFcomb[ z ] = 0.0;
            whenBestTSPLengthFound[ z ] = 0;
        }
    }
    
    /**
     * showRawResults on console 
     */
    public void showRawResults( )
    {
        System.out.println( 
            "RAW RESULTS: number of trials is: " + 
            numberOfRuns + 
            ", and number of iterations is: " +
            numberOfIterations );
        
        for( int i = 0; i < numberOfRuns; i++ )
        {
            for( int j = 0; j < numberOfIterations; j++ )
            {
                System.out.println( "Trial: " + i + " Iteration: " + j );
                
                System.out.print( "AVE coupling is: " );
                System.out.print( df.format( averageDesignCouplingOverRuns[ i ][ j]  ) + " +/- " ); 
                System.out.print( df.format( averageDesignCouplingOverRunsSD[ i ][ j] ) + " " );
                
                System.out.print( "BEST design coupling is: " );
                System.out.print( df.format( bestDesignCouplingOverRuns[ i ][ j ] ) + " +/- " );
                System.out.print( df.format( bestDesignCouplingOverRunsSD[ i ][ j ] ) + " " );
                
                System.out.print( "AVE class cohesion is: " );
                System.out.print( df.format( averageClassCohesionOverRuns[ i ][ j ] ) + " +/- " );
                System.out.print( df.format( averageClassCohesionOverRunsSD[ i ][ j ] ) + " " );
                
                System.out.print( "BEST cohesion is: " );
                System.out.print( df.format( bestClassCohesionOverRuns[ i ][ j ] ) + " +/- " );
                System.out.println( df.format( bestClassCohesionOverRunsSD[ i ][ j ] ) );
                
                System.out.print( "BEST NAC Elegance is: " );
                System.out.print( df.format( bestEleganceNACOverRuns[ i ][ j ] ) + " +/- " );
                System.out.println( df.format( bestEleganceNACOverRuns[ i ][ j ] ) );
                
                System.out.print( "BEST ATMR Elegance is: " );
                System.out.print( df.format( bestEleganceATMROverRuns[ i ][ j ] ) + " +/- " );
                System.out.println( df.format( bestEleganceATMROverRunsSD[ i ][ j ] ) );
            }
        }
    }
    
    /**
     * calculate final results over trials
     */
    public void calculateFinalResults( )
    {
        // best results for each run
        
        for( int run = 0; run < numberOfRuns; run++ )
        {
            this.bestCBO[ run ] = 1.0; 
            this.whenCBOfound[ run ] = 0;
            
            this.bestNAC[ run ] = 1.0;    // minimisation measure, so start high!
            this.whenNACFound[ run ] = 0;
            
            this.bestCombined[ run ] = 1.0;
            this.whenCombinedFound[ run ] = 0;
                  
            for( int iter = 0; iter < numberOfIterations; iter++ )
            {
                double tempCBO = this.bestDesignCouplingOverRuns[ run][ iter ];
                if( tempCBO < this.bestCBO[ run ] )
                {
                    this.bestCBO[ run ] = tempCBO;
                    this.whenCBOfound[ run ] = iter;
                }
                
                double tempNAC = this.bestEleganceNACOverRuns[ run ][ iter ];
                if( tempNAC < this.bestNAC[ run ] )
                {
                    this.bestNAC[ run ] = tempNAC;
                    this.whenNACFound[ run ] = iter;
                }
                
                double tempCombined = this.bestCombinedOverRuns[ run ][ iter ];
                if( tempCombined < this.bestCombined[ run ] )
                {
                    this.bestCombined[ run ] = tempCombined;
                    this.whenCombinedFound[ run ] = iter;
                }
            }   // end for each iteration
        
        }   // end for each run
        
        
        // 28 June 2018 for adaptive antipheromone
        // calculate the average number of retries and attempts for each run
        int runningTotalRetries[ ] = new int[ numberOfIterations ];
        double runningTotalAttempts[ ] = new double[ numberOfIterations ];
        
        for( int r = 0; r < numberOfRuns; r++ )
        {
            for( int iteration = 0; iteration < numberOfIterations; iteration++ )
            {
                runningTotalRetries[ iteration ] += this.retriesOverRuns[ r ][ iteration ];
                runningTotalAttempts[ iteration ] += this.averageAttemptsOverRuns[ r ][ iteration ];
            }
        }
        
        assert numberOfRuns > 0; // prevent divide by zero error
        
        for( int i = 0; i < numberOfIterations; i++ )
        {
            this.averageRetries[ i ] = (double) runningTotalRetries[ i ] / (double) numberOfRuns;
            this.averageOfAverageAttempts[ i ] = runningTotalAttempts[ i ] / (double) numberOfRuns;
            
        }
        
        // 28 June 2017 for adaptive antipheromone
        // calculate the standard deviations for retries and attempts
        int iterationRetries[ ] = new int[ numberOfIterations ];
        double iterationAttempts[ ] = new double[ numberOfIterations ];
        
        // double retriesStdDev[ ] = new double[ numberOfIterations ];
        // double attemptsStdDev[ ] = new double[ numberOfIterations ];
        
        for( int it = 0; it < numberOfIterations; it++ )
        {
            for( int run = 0; run < numberOfRuns; run++ )
            {
                iterationRetries[ run ] = this.retriesOverRuns[ run ][ it ]; 
                iterationAttempts[ run ] = this.averageAttemptsOverRuns[ run ][ it ];
            }
            
            this.retriesStdDev[ it ] = Utility.standardDeviation( iterationRetries );
            this.attemptsStdDev[ it ] = Utility.standardDeviation( iterationAttempts );
        }
    
        // 29 June 2017 for adaptive pheromone
        double runningTotalBestFcomb[ ] = new double[ numberOfIterations ];
        
        for( int r = 0; r < numberOfRuns; r++ )
        {
            for( int iteration = 0; iteration < numberOfIterations; iteration++ )
            {
                runningTotalBestFcomb[ iteration ] += this.bestCombinedOverRuns[ r ][ iteration ];
            }
        }
        
        assert numberOfRuns > 0; // prevent divide by zero error
        
        for( int i = 0; i < numberOfIterations; i++ )
        {
            this.bestFcomb[ i ] = (double) runningTotalBestFcomb[ i ] / (double) numberOfRuns;
        }
    
        // 29 June 2017 for adaptive antipheromone
        // calculate the standard deviations for retries and attempts
        double localBestFcomb[ ] = new double[ numberOfIterations ];
        
        for( int it = 0; it < numberOfIterations; it++ )
        {
            for( int run = 0; run < numberOfRuns; run++ )
            {
                localBestFcomb[ run ] = this.bestCombinedOverRuns[ run ][ it ]; 
            }
            
            this.bestFcombStdDev[ it ] = Utility.standardDeviation( localBestFcomb );
        }
        
        
        
        // 5 September 2018
        double runningTotalInterference[ ] = new double[ numberOfIterations ];
        
        for( int it = 0; it < numberOfIterations; it++ )
        {
            for( int run = 0; run < numberOfRuns; run++ )
            {
                runningTotalInterference[ it ] += this.interference[ run ][ it ]; 
            }
        }

        for( int iter = 0; iter < numberOfIterations; iter++ )
        {
            this.averageInterference[ iter ] = runningTotalInterference[ iter] / (double) numberOfRuns;
        }        
        
        
        
        double localInterference[ ] = new double[ numberOfIterations ];
        
        for( int it = 0; it < numberOfIterations; it++ )
        {
            for( int run = 0; run < numberOfRuns; run++ )
            {
                localInterference[ run ] = this.interference[ run ][ it ]; 
            }
            
            this.averageInterferenceStdDev[ it ] = Utility.standardDeviation( localInterference );
        }
    }
    
    
    
   
    
    
    /**
     * write final results to chosen folder path
     * 2 April 2012
     * Only complete paths are generated using coupling
     * so no need for files relating to cohesion fitness,
     * so out3 and out4 are commented out.
     */
    public void writeFinalResults( 
            String path, long[ ] runtimes, boolean handlingConstraints )
    {
        assert path != null;
        assert path.length( ) > 0;
        assert runtimes != null;
        assert runtimes.length == numberOfRuns;

        //doesn;'t work on macs/unix
        //String averageCouplingFullName = path + "\\" + AVERAGE_COUPLING_FILE_NAME;
        //String bestCouplingFullName = path + "\\" + BEST_COUPLING_FILE_NAME;
        //String averageCohesionFullName = path + "\\" + AVERAGE_COHESION_FILE_NAME;
        //String bestCohesionFullName = path + "\\" + BEST_COHESION_FILE_NAME;
        //String spssOutputname = path + "\\" + SPSS_OUTPUT_NAME;
        
        
        String averageCouplingFullName =  
            Parameters.outputFilePath + "\\" + AVERAGE_COUPLING_FILE_NAME;
        String bestCouplingFullName =  
            Parameters.outputFilePath + "\\" + BEST_COUPLING_FILE_NAME;
        String bestNACFullName = 
            Parameters.outputFilePath + "\\" + BEST_NAC_FILE_NAME;
        String bestATMRFullName = 
            Parameters.outputFilePath + "\\" + BEST_ATMR_FILE_NAME;
        String bestEMFullName = 
             Parameters.outputFilePath + "\\" + BEST_EM_FILE_NAME;   
        String spssBestOutputName =  
            Parameters.outputFilePath + "\\" + SPSS_BEST_OUTPUT_NAME;
        String spssAverageOutputName =  
            Parameters.outputFilePath + "\\" + SPSS_AVERAGE_OUTPUT_NAME;
        
        
        // set up the output files
        PrintWriter out1 = null;
        PrintWriter out2 = null;
        PrintWriter out3 = null;
        PrintWriter out4 = null;
        PrintWriter out5 = null;
        PrintWriter out6 = null;
        PrintWriter out7 = null;
        
        try
        {
            out1 = new PrintWriter( averageCouplingFullName );
            out2 = new PrintWriter( bestCouplingFullName );
            out3 = new PrintWriter( bestNACFullName );
            out4 = new PrintWriter( bestATMRFullName );
            out5 = new PrintWriter( bestEMFullName );
        }
        catch( FileNotFoundException ex )
        {
            System.err.println( "Cant open file!" );
            return;
        }   
            
        boolean append = true;
        try 
        {
            // i dopn't ewant ot overwrite existing result files
            out6 = new PrintWriter (new FileWriter(new File(spssBestOutputName), append));
        } 
        catch( IOException ex ) 
        {
            Logger.getLogger(BatchResults.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Can't open " + spssBestOutputName );
        }

        try 
        {
            // i dopn't ewant ot overwrite existing result files
            out7 = new PrintWriter (new FileWriter(new File(spssAverageOutputName), append));
        } 
        catch( IOException ex ) 
        {
            Logger.getLogger(BatchResults.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Can't open " + spssAverageOutputName );
        }
        

        // 2 July 2012 true for final draft of paper
        boolean maximise = false; // true;
        
         // write external coupling values
        for( int i = 0; i < numberOfIterations; i++ )
        {
            
            if( maximise == false )
            {
                out1.print( i + " " );
                out1.print( df.format( finalAverageDesignCoupling[ i ] ) + " " );
                out1.println( df.format( finalAverageDesignCouplingSD[ i ] ) );
            }
            else
            {
                out1.print( ( i * AlgorithmParameters.NUMBER_OF_ANTS ) + " " ); // evals
                double result = ( 1.0 - finalAverageDesignCoupling[ i ] ) * 100.0;
                out1.print( df.format( result ) + " " ); 
                double sd = finalAverageDesignCouplingSD[ i ] * 100.0;
                out1.println( df.format( sd ) ); 
            }
            
            
            
            if( maximise == false )
            {
                out2.print( i + " " );
                out2.print( df.format( finalBestDesignCoupling[ i ] ) + " " );
                out2.println( df.format( finalBestDesignCouplingSD[ i ] ) );
            }
            else
            {
                out2.print( ( i * AlgorithmParameters.NUMBER_OF_ANTS ) + " " ); // evals
                double result = ( 1.0 - finalBestDesignCoupling[ i ] ) * 100.0;
                out2.print( df.format( result ) + " " ); 
                double sd = finalBestDesignCouplingSD[ i ] * 100.0;
                out2.println( df.format( sd ) );
            }
            
            out3.print( i + " " );
            out3.print( df.format( finalBestEleganceNAC[ i ] ) + " " );
            out3.println( df.format( finalBestEleganceNACSD[ i ] ) );

            out4.print( i + " " );
            out4.print( df.format( finalBestEleganceATMR[ i ] ) + " " );
            out4.println( df.format( finalBestEleganceATMRSD[ i ] ) );
            
            out5.print( i + " " );
            out5.print( df.format( finalBestEleganceModularity[ i ] ) + " " );
            out5.println( df.format( finalBestEleganceModularitySD[ i ] ) );
        }
        
        
        //Jim Added 22-2-12 for loop below

        for( int run = 0; run < numberOfRuns; run++ )
        {
            
            // run times are in milliseconds, convert to seconds
            final double time = runtimes[ run ] / 1000.0;

            // 4 May 2012 Chris added best NAC and best ATMR elegance
            // 28 May 2012 Chris added flag for constraint handling
            
            int hc = handlingConstraints ? 1 : 0;
            
            Date date = new Date( );
            out6.println ( date.toString( ) + " " + 
                          df.format( this.bestCBO[ run] ) + " " + 
                          this.whenCBOfound[ run ] + " " +
                          df.format( this.bestNAC[ run ] ) + " " + 
                          df.format( this.bestATMR[ run ] ) + " " +
                          df.format( this.bestEM[ run ] ) + " " +
                          df.format( time ) + " " +
                          hc ); 
        
            // 22 April 2012 Chris added average coupling values to another file
            // 25 April 2012 Chris added run times to both files too

            double maximised = 1 - this.averageDesignCouplingOverRuns[ run][ numberOfIterations - 1 ];
            
            out7.println ( date.toString( ) + " " + 
                          df.format( maximised * 100 ) + " " + 
                          numberOfIterations + " " + 
                          df.format( time )); 
        }
        
        out1.close( );
        out2.close( );
        out3.close( );
        out4.close( );
        out5.close( );
        out6.close( );
        out7.close( );
    }
    
//    /**
//     * write final results of heuristic ant search
//     * @param path 
//     */
//    public void writeFinalHeuristicResults( String path  )
//    {
//        assert path != null;
//        assert path.length( ) > 0;
//
//        // String heuristicsNACResultsFileFullName =  
//        //    Parameters.outputFilePath + "\\" + HEURISTIC_NAC_OUTPUT_NAME;
//        // 13 Nove 2015
//        String heuristicsNACResultsFileFullName =  
//            Parameters.outputFilePath + "/" + HEURISTIC_NAC_OUTPUT_NAME;
//        
//        System.out.println( "file name is: " + heuristicsNACResultsFileFullName );
//        final String dir = System.getProperty( "user.dir" );
//        System.out.println( "current dir = " + dir );
//        
//        // set up the output files
//        PrintWriter out1 = null;
//        
//        boolean append = true;
//        try 
//        {
//            // don't want to overwrite existing result files
//            out1 = new PrintWriter( new FileWriter( 
//                new File( heuristicsNACResultsFileFullName), append ) );
//        } 
//        catch( IOException ex ) 
//        {
//            Logger.getLogger(BatchResults.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("Can't open " + heuristicsNACResultsFileFullName );
//        }   
//        
//        for( int run = 0; run < numberOfRuns; run++ )
//        {
//           out1.println( ( Parameters.problemNumber + 1 ) + " " +
//                         AlgorithmParameters.weightCBO + " " +  
//                         AlgorithmParameters.weightNAC + " " +
//                         AlgorithmParameters.BETA_CBO + " " +
//                         AlgorithmParameters.BETA_NAC + " " +
//                         ( run + 1 )  + " " + 
//                         df.format( this.bestCBO[ run ] ) + " " + 
//                         this.whenCBOfound[ run ] + " " +
//                         df.format( this.bestNAC[ run ] ) + " " + 
//                         this.whenNACFound[ run ] ); 
//            
//            
//            int heuristics = AlgorithmParameters.heuristics ? 1 : 0;
//            
//            out1.println( ( Parameters.problemNumber + 1 ) + " " +
//                         heuristics + " " +
//                         AlgorithmParameters.BETA_CBO + " " +
//                         AlgorithmParameters.BETA_NAC + " " +
//                         ( run + 1 )  + " " + 
//                         df.format( this.bestCBO[ run ] ) + " " + 
//                         this.whenCBOfound[ run ] + " " +
//                         df.format( this.bestNAC[ run ] ) + " " + 
//                         this.whenNACFound[ run ] ); 
//        }
//            
//        out1.close( );
//    }
     
    
     /**
     * write results of ant search to file
     */
    public void writeResults( )
    {
        String outputFileName = "";
        
        if( AlgorithmParameters.fitness == AlgorithmParameters.CBO )
        {
            outputFileName = BEST_COUPLING_FILE_NAME;
        }
        else if( AlgorithmParameters.fitness == AlgorithmParameters.NAC )
        {
            outputFileName = BEST_NAC_FILE_NAME;
        }
        else if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
        {
            outputFileName = BEST_COMBINED_FILE_NAME;
        }
        else
        {
            System.out.println( "In writeResults for TSP..."); 
            outputFileName = "NotRelevant.dat";
        }
        
        // 13 November 2015 for results 
        // 29 June 2017 for retries, attempts and cost curves 
        // 8 August 2018 and 3 September 2018 for interference
        // 28 August 2018 for snapshots 
        // 18 September 2018 for TSP
        String resultsFileFullName = "";
        String retriesFileFullName = "";
        String costFileFullName = "";
        String interferenceFileFullName = "";
        String snapShotFileFullName = "";
        String interferenceIterationsForSPSSFileFullName = "";
        String interferenceIterationsForGnuPlotFileFullName = "";
        String TSPFileFullName = "";
        
        
        if( Parameters.platform == Parameters.Platform.Windows )
        {
            resultsFileFullName = Parameters.outputFilePath + "\\" + outputFileName;
            retriesFileFullName = Parameters.outputFilePath + "\\" + RETRIES_ATTEMPTS_FILE_NAME;
            costFileFullName = Parameters.outputFilePath + "\\" + COST_FILE_NAME;
            interferenceFileFullName = Parameters.outputFilePath + "\\" + INTERFERENCE_FILE_NAME;
            snapShotFileFullName = Parameters.outputFilePath + "\\" + SNAPSHOT_FILE_NAME;
            interferenceIterationsForSPSSFileFullName = Parameters.outputFilePath + "\\" + INTERFERENCE_ITERATIONS_FOR_SPSS_FILE_NAME;
            interferenceIterationsForGnuPlotFileFullName = Parameters.outputFilePath + "\\" + INTERFERENCE_ITERATIONS_FOR_GNUPLOT_FILE_NAME;
            TSPFileFullName = Parameters.outputFilePath + "\\" + BEST_TSP_FILE_NAME;
        }
        else    // we're on Mac
        {
            resultsFileFullName = Parameters.outputFilePath + "/" + outputFileName;
            retriesFileFullName = Parameters.outputFilePath + "/" + RETRIES_ATTEMPTS_FILE_NAME;
            costFileFullName = Parameters.outputFilePath + "/" + COST_FILE_NAME;
            interferenceFileFullName = Parameters.outputFilePath + "/" + INTERFERENCE_FILE_NAME;
            snapShotFileFullName = Parameters.outputFilePath + "/" + SNAPSHOT_FILE_NAME;
            interferenceIterationsForSPSSFileFullName = Parameters.outputFilePath + "/" + INTERFERENCE_ITERATIONS_FOR_SPSS_FILE_NAME;
            interferenceIterationsForGnuPlotFileFullName = Parameters.outputFilePath + "/" + INTERFERENCE_ITERATIONS_FOR_GNUPLOT_FILE_NAME;
            TSPFileFullName = Parameters.outputFilePath + "/" + BEST_TSP_FILE_NAME;
        }
        
        System.out.println( "fitness results file name is: " + resultsFileFullName );
        System.out.println( "retries and attempts file name is: " + retriesFileFullName );
        System.out.println( "cost information file name is: " + costFileFullName );
        System.out.println( "interference file name is: " + interferenceFileFullName );
        System.out.println( "snapshot file name is: " + snapShotFileFullName );
        System.out.println( "interference iterations for SPSS file name is: " + interferenceIterationsForSPSSFileFullName );
        System.out.println( "interference iterations for GnuPlot file name is: " + interferenceIterationsForGnuPlotFileFullName );
        System.out.println( "TSP file name is: " + TSPFileFullName );
        
        final String dir = System.getProperty( "user.dir" );
        System.out.println( "current execution directory is: " + dir );
        
        // set up the output files
        PrintWriter out1 = null;
        PrintWriter out2 = null;
        PrintWriter out3 = null;
        PrintWriter out4 = null;
        PrintWriter out5 = null;
        PrintWriter out6 = null;
        PrintWriter out7 = null;
        PrintWriter out8 = null;
        
        boolean append = true;
        try 
        {
            // we don't want to overwrite existing result files
            out1 = new PrintWriter( new FileWriter( new File( resultsFileFullName), append ) );
            out2 = new PrintWriter( new FileWriter( new File( retriesFileFullName), append ) );
            out3 = new PrintWriter( new FileWriter( new File( costFileFullName), append ) );
            out4 = new PrintWriter( new FileWriter( new File( interferenceFileFullName), append ) );
            out5 = new PrintWriter( new FileWriter( new File( snapShotFileFullName), append ) );
            out6 = new PrintWriter( new FileWriter( new File( interferenceIterationsForSPSSFileFullName), append ) );
            out7 = new PrintWriter( new FileWriter( new File( interferenceIterationsForGnuPlotFileFullName), append ) );
            out8 = new PrintWriter( new FileWriter( new File( TSPFileFullName), append ) );
        
        } 
        catch( IOException ex ) 
        {
            Logger.getLogger( BatchResults.class.getName()).log(Level.SEVERE, null, ex );
            System.out.println( "Can't open one of the results files!!" );
        }   
        
        // for easier analysis in SPSS 14 Jan 2016
        int antiPheromoneOn = 0; // false
        if( AlgorithmParameters.antiPheromonePhasePercentage > 0 )
        {
            antiPheromoneOn = 1; // true
        }
        
        int MMAS_50_percent_on = 0; // false
        if( AlgorithmParameters.MMAS_REDUCE_BY_HALF == true )
        {
            MMAS_50_percent_on = 1;
        }
        
        int prevent = 0;
        if( AlgorithmParameters.preventInterference == true )
        {
            prevent = 1;
        }
        
        assert AlgorithmParameters.antipheromoneStrength >= AlgorithmParameters.ANTIPHEROMONE_STRENGTH_SINGLE;
        assert AlgorithmParameters.antipheromoneStrength <= AlgorithmParameters.ANTIPHEROMONE_STRENGTH_TRIPLE;
        
        for( int run = 0; run < numberOfRuns; run++ )
        {
//            int evalsWhenCBOFound = this.whenCBOfound[ run ] * AlgorithmParameters.NUMBER_OF_ANTS;
//            int evalsWhenNACFound = this.whenNACFound[ run ] * AlgorithmParameters.NUMBER_OF_ANTS;
            int evalsWhenCombinedFound = this.whenCombinedFound[ run ] * AlgorithmParameters.NUMBER_OF_ANTS;
            assert out1 != null;
            
            int localCopyOfAntipheromoneStrength = 0;
            if( antiPheromoneOn == 0 )
            {
                // do nothing, local copy of antipheromone strength stays at zero
            }
            else
            {
                localCopyOfAntipheromoneStrength = AlgorithmParameters.antipheromoneStrength;
            }
            
            out1.println(   Parameters.problemNumber  + " " +
                            ( run + 1 )  + " " + 
//                            AlgorithmParameters.fitness + " " +
//                            AlgorithmParameters.algorithm + " " +
//                            AlgorithmParameters.NUMBER_OF_ANTS + " " +
                            antiPheromoneOn + " " +
                            AlgorithmParameters.antiPheromonePhasePercentage + " " +
//                            MMAS_50_percent_on + " " + 
//                            df.format( this.bestCBO[ run ] ) + " " + 
//                            evalsWhenCBOFound + " " +
//                            df.format( this.bestNAC[ run ] ) + " " + 
//                            evalsWhenNACFound + " " +
                            df.format( this.bestCombined[ run ] ) + " " + 
                            evalsWhenCombinedFound + " " +
                    
                            // 26 June 2018
                            // this.maxNumberOfInvalids[ run ] );
                            localCopyOfAntipheromoneStrength + " " +
                            AlgorithmParameters.antipheromoneStrength + " " + 
                            
                            // 21 August 2018
                            prevent );
        }
            
        
        // 28 June 2017 for adaptive antipheromone
        // write retry and attempt information to file
        for( int iteration = 0; iteration < numberOfIterations; iteration++ )
        {
            assert out2 != null;
            out2.println(   
                Parameters.problemNumber  + " " +
                AlgorithmParameters.pheromoneStrength + " " +
                AlgorithmParameters.antipheromoneStrength + " " +
                prevent + " " +
                antiPheromoneOn + " " +
                AlgorithmParameters.antiPheromonePhasePercentage + " " +

                ( iteration + 1 )  + " " + 
                df.format( this.averageRetries[ iteration ] ) + " " +
                df.format( this.retriesStdDev[ iteration ] ) + " " +
                df.format( this.averageOfAverageAttempts[ iteration ] ) + " " +
                df.format( this.attemptsStdDev[ iteration ]) );
        }
        
        
        
        // 29 June 2017 
        // write cost information to file
        for( int iteration = 0; iteration < numberOfIterations; iteration++ )
        {
            assert out3 != null;
            out3.println(   
                Parameters.problemNumber  + " " +
                AlgorithmParameters.pheromoneStrength + " " +
                AlgorithmParameters.antipheromoneStrength + " " +
                prevent + " " +
                antiPheromoneOn + " " +
                AlgorithmParameters.antiPheromonePhasePercentage + " " +
                ( iteration + 1 )  + " " + 
                
                df.format( this.bestFcomb[ iteration ] ) + " " +
                df.format( this.bestFcombStdDev[ iteration ] ) );
        }
        
        
        // 8 August 2018
        for( int run = 0; run < numberOfRuns; run++ )
        {
            assert out4 != null;
            out4.println(   
                Parameters.problemNumber + " " + 
                AlgorithmParameters.pheromoneStrength  + " " +            
                AlgorithmParameters.antipheromoneStrength  + " " +
                prevent + " " +
                antiPheromoneOn + " " +
                AlgorithmParameters.antiPheromonePhasePercentage + " " +
                ( run + 1 ) + " " + 
                df.format( this.interference[ run ][ numberOfIterations - 1 ] ) );
        }
        
        // 23 August 2018
        for( int run = 0; run < numberOfRuns; run++ )
        {
            assert out5 != null;
            out5.println(   
                Parameters.problemNumber + " " + 
                AlgorithmParameters.pheromoneStrength  + " " +            
                AlgorithmParameters.antipheromoneStrength  + " " +
                prevent + " " +
                antiPheromoneOn + " " +
                AlgorithmParameters.antiPheromonePhasePercentage + " " +
                ( run + 1 ) + " " + 
                df.format( this.bestCombinedValueAt50OverRuns[ run ] ) + " " +
                df.format( this.bestCombinedValueAt100OverRuns[ run ] ) + " " +
                df.format( this.bestCombinedValueAt150OverRuns[ run ] ) + " " +
                df.format( this.bestCombinedValueAt200OverRuns[ run ] ) + " " +
                df.format( this.bestCombinedValueAt300OverRuns[ run ] ) + " " +
                df.format( this.bestCombinedValueAt400OverRuns[ run ] ) + " " + 
                df.format( this.areaAt50OverRuns[ run ] ) + " " + 
                df.format( this.areaAt100OverRuns[ run ] ) + " " +
                df.format( this.areaAt150OverRuns[ run ] ) + " " +
                df.format( this.areaAt200OverRuns[ run ] ) + " " +
                df.format( this.areaAt300OverRuns[ run ] ) + " " +
                df.format( this.areaAt400OverRuns[ run ] ) );
        }
        
        
        // 3 September 2018 
        // write information regarding interference at iterations to file
        for( int iteration = 0; iteration < numberOfIterations; iteration++ )
        {
            assert out6 != null;
            assert out7 != null;
            
            // for SPSS...
            out6.println(   
                Parameters.problemNumber  + " " +
                AlgorithmParameters.pheromoneStrength + " " +
                AlgorithmParameters.antipheromoneStrength + " " +
                prevent + " " +
                antiPheromoneOn + " " +
                AlgorithmParameters.antiPheromonePhasePercentage + " " +
                ( iteration + 1 )  + " " + 
                df.format( this.averageInterference[ iteration ] ) + " " +
                df.format( this.averageInterferenceStdDev[ iteration ] ) );
            
            // for GnuPlot 
            out7.println(   
                ( iteration + 1 )  + " " + 
                df.format( this.averageInterference[ iteration ] ) + " " +
                df.format( this.averageInterferenceStdDev[ iteration ] ) );
        }
        
        
        // 18 September 2018
        for( int run = 0; run < numberOfRuns; run++ )
        {
            int evalsWhenTSPBestFound = this.whenBestTSPLengthFound[ run ] * AlgorithmParameters.NUMBER_OF_ANTS;
            assert out8 != null;
            
            out8.println(Parameters.problemNumber  + " " +
                            AlgorithmParameters.fitness + " " +
                            AlgorithmParameters.pheromoneStrength + " " +
                            AlgorithmParameters.antipheromoneStrength + " " + 
                            prevent + " " +
                            ( run + 1 )  + " " + 
                            antiPheromoneOn + " " +
                            AlgorithmParameters.antiPheromonePhasePercentage + " " +
                            df.format(this.bestTSPLength[ run ] ) + " " + 
                            evalsWhenTSPBestFound );
        }

        out1.close( );
        out2.close( );
        out3.close( );
        out4.close( );
        out5.close( );
        out6.close( );
        out7.close( );
        out8.close( );
        
    }
    
}   // end class

//------- end file ----------------------------------------


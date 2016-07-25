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
    
    // 17 Nov 1015
    private static final String BEST_COMBINED_FILE_NAME = "BestCOMBINED.dat";
    
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
    //1D arrays to hold best fitness fpund for each run, 
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
        // standard deviation of best designs
        // can only be done at the trial level
        
        // 30 Nov 2015 comment out
//        calculateFinalBestDesignCoupling( );
//        calculateFinalBestClassCohesion( );
        
        //best results for each run
        for( int run = 0; run < numberOfRuns; run++ )
        {
            this.bestCBO[ run ] = 1.0; 
            this.whenCBOfound[ run ] = 0;
            
            this.bestNAC[ run ] = 1.0;    // minimisation measure, so start high!
            this.whenNACFound[ run ] = 0;
            
            this.bestCombined[ run ] = 1.0;
            this.whenCombinedFound[ run ] = 0;
            
            // this.bestATMR[ run ] = 100.0;
            // this.whenATMRFound[ run ] = 0;
            // this.bestEM[ run ] = 100.0; 30 November 2015
            
            for( int iter = 0; iter < numberOfIterations; iter++ )
            {
                // 30 November 2015  - report fCBO as a cost (minimisation) 
                // double tempCBO = 100.0 - 100.0 * this.bestDesignCouplingOverRuns[ run][ iter ];
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
                
                
                // 6 December 2015 comment out
//                double tempATMR = this.bestEleganceATMROverRuns[ run ][ iter ];
//                if( tempATMR < this.bestATMR[ run ] )
//                {
//                    this.bestATMR[ run ] = tempATMR;
//                    this.whenATMRFound[ run ]  = iter;
//                }
//            
                // comment out 30 November 2015
//                double tempEM = this.bestEleganceModularityOverRuns[ run ][ iter ];
//                if( tempEM < this.bestEM[ run ] )
//                {
//                    this.bestEM[ run ] = tempEM;
//                }
            
            }   // end for each iteration
        
        }   // end for each run over all trials
        
        // 24 April 2012
        // again, can only be done at the trial level
        // 30 November 2015 comment out
        // calculateFinalAverageDesignCoupling( );
        // calculateFinalBestElegance( );
        
        // now calculate the average values,
        // taken as an average of raw data
//        double runningAverageCoupling = 0.0;
//        double runningAverageCouplingSD = 0.0;
        
//        double runningAverageCohesion = 0.0;
//        double runningAverageCohesionSD = 0.0;
//        
//        for( int i = 0; i < numberOfIterations; i++ )
//        {
//            for( int j = 0; j < numberOfRuns; j++ )
//            {
//                runningAverageCoupling += averageDesignCouplingOverRuns[ j ][ i ];
//                runningAverageCouplingSD += averageDesignCouplingOverRunsSD[ j ][ i ];
//                runningAverageCohesion += averageClassCohesionOverRuns[ j ][ i ];
//                runningAverageCohesionSD += averageClassCohesionOverRunsSD[ j ][ i ];
//
//            }   // end for each trial
//
//            // calulate final results
//            finalAverageDesignCoupling[ i ] =
//                runningAverageCoupling / (double) AcoController.NUMBER_OF_TRIALS;
//            finalAverageDesignCouplingSD[ i ] =
//                runningAverageCouplingSD / (double) AcoController.NUMBER_OF_TRIALS;
//            
//            finalAverageClassCohesion[ i ] = 
//                runningAverageCohesion / (double) numberOfRuns;
//            finalAverageClassCohesionSD[ i ] = 
//                runningAverageCohesionSD / (double) numberOfRuns;
//                    
//            // reset for next iteration
//            runningAverageCoupling = 0.0;
//            runningAverageCouplingSD = 0.0;
//            runningAverageCohesion = 0.0;
//            runningAverageCohesionSD = 0.0;
//            
//        }   // end for each iteration
        
    }
    
    /**
     * calculate the (average) final best design coupling value
     * for each iteration over a number of trials,
     * and also calculate the standard deviation at the same time
     */
    private void calculateFinalBestDesignCoupling( ) 
    { 
        double runningTotals[ ] = 
            new double[ numberOfIterations ];
        double averages[ ] =
            new double[ numberOfIterations ];
        double[ ] squaredValueSums = 
            new double[ numberOfIterations ];
        
        int i = 0;
        int j = 0;

        for( i = 0; i < numberOfIterations; i++ )
        {
            runningTotals[ i ] = 0.0;
            averages[ i ] = 0.0;
            squaredValueSums[ i ] = 0;
        }
        
        for( i = 0; i < numberOfIterations; i++ )
        {
            for( j = 0; j < numberOfRuns; j++ )
            {
                runningTotals[ i ] += this.bestDesignCouplingOverRuns[ j ][ i ];
                
            }   // end for each trial
        
        }   // end for each iteration
        
        
//        for( i = 0; i < numberOfIterations; i++ )
//        {
//            System.out.println( "running total is: " + runningTotals[ i ] );
//        }
//    
        
        
        // calculate the average at each iteration
        for( i = 0; i < numberOfIterations; i++ )
        {
            averages[ i ] = runningTotals[ i ] / (double) numberOfRuns;
            this.finalBestDesignCoupling[ i ] = averages[ i ];
        }
    
        // calculate the standard deviation at each iteration
        for( i = 0; i < numberOfIterations; i++ )
        {
            for( j = 0; j < numberOfRuns; j++ )
            {
                assert j < numberOfRuns : "j is: " + j;
                double temp = this.bestDesignCouplingOverRuns[ j ][ i ] - averages[ i ];
                double squaredValue = temp * temp;
                squaredValueSums[ i ] += squaredValue;
            }
        }
        
        for( i = 0; i < numberOfIterations; i++ )
        {
            double temp2 = squaredValueSums[ i ] / (double) numberOfRuns;
            this.finalBestDesignCouplingSD[ i ] = Math.sqrt(  temp2 );
        }
        
    //      taken from GA code...
//        for( int c = 0; c < runs; c++ )
//        {
//            for( int d = 0; d < numGen; d++ )
//            {
//                double temp = numbers[ c ][ d ] - meanAverage[ d ];
//                double squaredValue = temp * temp;
//                squaredValueSum[ d ] += squaredValue;
//            }
//        }
//
//        double sd[ ] = new double[ numGen ];
//        for( int e = 0; e < numGen; e++ ) { sd[ e ] = 0.0; }
//
//        for( int f = 0; f < numGen; f++ )
//        {
//            double temp2 = squaredValueSum[ f ] / numGen;
//            sd[ f ] = Math.sqrt( temp2 );
//        }
    
    
    }
    
    
    
    /**
     * calculate the (average) final best class cohesion value
     * for each iteration over a number of trials,
     * and also calculate the standard deviation at the same time
     */
    private void calculateFinalBestClassCohesion( ) 
    { 
        double runningTotals[ ] = 
            new double[ numberOfIterations ];
        double averages[ ] =
            new double[ numberOfIterations ];
        double[ ] squaredValueSums = 
            new double[ numberOfIterations ];
        
        int i = 0;
        int j = 0;

        for( i = 0; i < numberOfIterations; i++ )
        {
            runningTotals[ i ] = 0.0;
            averages[ i ] = 0.0;
            squaredValueSums[ i ] = 0;
        }
        
        for( i = 0; i < numberOfIterations; i++ )
        {
            for( j = 0; j < numberOfRuns; j++ )
            {
                runningTotals[ i ] += this.bestClassCohesionOverRuns[ j ][ i ];
                
            }   // end for each trial
        
        }   // end for each iteration
    
        // calculate the average at each iteration
        for( i = 0; i < numberOfIterations; i++ )
        {
            averages[ i ] = runningTotals[ i ] / (double) numberOfRuns;
            this.finalBestClassCohesion[ i ] = averages[ i ];
            
        }
    
        // calculate the standard deviation at each iteration
        for( i = 0; i < numberOfIterations; i++ )
        {
            for( j = 0; j < numberOfRuns; j++ )
            {
                double temp = this.bestClassCohesionOverRuns[ j ][ i ] - averages[ i ];
                double squaredValue = temp * temp;
                squaredValueSums[ i ] += squaredValue;
            }
        }
        
        for( i = 0; i < numberOfIterations; i++ )
        {
            double temp2 = squaredValueSums[ i ] / (double) numberOfRuns;
            // assert temp2 != 0.0;
            this.finalBestClassCohesionSD[ i ] = Math.sqrt(  temp2 );
        }
    }
     
    
    /**
     * calculate the average of the average design coupling value
     * for each iteration over a number of trials,
     * and also calculate the standard deviation at the same time
     */
    private void calculateFinalAverageDesignCoupling( ) 
    { 
        double runningTotals[ ] = 
            new double[ numberOfIterations ];
        double averages[ ] =
            new double[ numberOfIterations ];
        double[ ] squaredValueSums = 
            new double[ numberOfIterations ];
        
        int i = 0;
        int j = 0;

        for( i = 0; i < numberOfIterations; i++ )
        {
            runningTotals[ i ] = 0.0;
            averages[ i ] = 0.0;
            squaredValueSums[ i ] = 0;
        }
        
        for( i = 0; i < numberOfIterations; i++ )
        {
            for( j = 0; j < numberOfRuns; j++ )
            {
                runningTotals[ i ] += this.averageDesignCouplingOverRuns[ j ][ i ];
                
            }   // end for each trial
        
        }   // end for each iteration
    
        // calculate the average at each iteration
        for( i = 0; i < numberOfIterations; i++ )
        {
            averages[ i ] = runningTotals[ i ] / (double) numberOfRuns;
            this.finalAverageDesignCoupling[ i ] = averages[ i ];
        }
    
        // calculate the standard deviation at each iteration
        for( i = 0; i < numberOfIterations; i++ )
        {
            for( j = 0; j < numberOfRuns; j++ )
            {
                assert j < numberOfRuns : "j is: " + j;
                double temp = this.averageDesignCouplingOverRuns[ j ][ i ] - averages[ i ];
                double squaredValue = temp * temp;
                squaredValueSums[ i ] += squaredValue;
            }
        }
        
        for( i = 0; i < numberOfIterations; i++ )
        {
            double temp2 = squaredValueSums[ i ] / (double) numberOfRuns;
            this.finalAverageDesignCouplingSD[ i ] = Math.sqrt(  temp2 );
        }
        
    }
    
    private void calculateFinalBestElegance( )
    {
        double runningNACTotals[ ] = new double[ numberOfIterations ];
        double averagesNAC[ ] = new double[ numberOfIterations ];
        double[ ] squaredValueSumsNAC = new double[ numberOfIterations ];
        
        double runningATMRTotals[ ] = new double[ numberOfIterations ];
        double averagesATMR[ ] = new double[ numberOfIterations ];
        double[ ] squaredValueSumsATMR = new double[ numberOfIterations ];
        
        double runningEMTotals[ ] = new double[ numberOfIterations ];
        double averagesEM[ ] = new double[ numberOfIterations ];
        double[ ] squaredValueSumsEM = new double[ numberOfIterations ];
        
        int i = 0;
        int j = 0;

        for( i = 0; i < numberOfIterations; i++ )
        {
            runningNACTotals[ i ] = 0.0;
            averagesNAC[ i ] = 0.0;
            squaredValueSumsNAC[ i ] = 0.0;
            runningATMRTotals[ i ] = 0.0;
            averagesATMR[ i ] = 0.0;
            squaredValueSumsATMR[ i ] = 0.0;
            runningEMTotals[ i ] = 0.0;
            averagesEM[ i ] = 0.0;
            squaredValueSumsEM[ i ] = 0.0;
            
        }
        
        for( i = 0; i < numberOfIterations; i++ )
        {
            for( j = 0; j < numberOfRuns; j++ )
            {
                runningNACTotals[ i ] += this.bestEleganceNACOverRuns[ j ][ i ];
                runningATMRTotals[ i ] += this.bestEleganceATMROverRuns[ j ][ i ];
                runningEMTotals[ i ] += this.bestEleganceModularityOverRuns[ j ][ i ];
                
            }   // end for each run
        
        }   // end for each iteration
    
        // calculate the mean at each iteration
        for( i = 0; i < numberOfIterations; i++ )
        {
            averagesNAC[ i ] = runningNACTotals[ i ] / (double) numberOfRuns;
            this.finalBestEleganceNAC[ i ] = averagesNAC[ i ];
            averagesATMR[ i ] = runningATMRTotals[ i ] / (double) numberOfRuns;
            this.finalBestEleganceATMR[ i ] = averagesATMR[ i ];
            averagesEM[ i ] = runningEMTotals[ i ] / (double) numberOfRuns;
            this.finalBestEleganceModularity[ i ] = averagesEM[ i ];
        }
    
        // calculate the standard deviation at each iteration
        double squaredValue = 0.0;
        double temp = 0.0;
        
        for( i = 0; i < numberOfIterations; i++ )
        {
            for( j = 0; j < numberOfRuns; j++ )
            {
                temp = this.bestEleganceNACOverRuns[ j ][ i ] - averagesNAC[ i ];
                squaredValue = temp * temp;
                squaredValueSumsNAC[ i ] += squaredValue;
                
                temp = this.bestEleganceATMROverRuns[ j ][ i ] - averagesATMR[ i ];
                squaredValue = temp * temp;
                squaredValueSumsATMR[ i ] += squaredValue;
                
                temp = this.bestEleganceModularityOverRuns[ j ][ i ] - averagesEM[ i ];
                squaredValue = temp * temp;
                squaredValueSumsATMR[ i ] += squaredValue;
            }
        }
        
        for( i = 0; i < numberOfIterations; i++ )
        {
            temp = squaredValueSumsNAC[ i ] / (double) numberOfRuns;
            this.finalBestEleganceNACSD[ i ] = Math.sqrt(  temp );
            
            temp = squaredValueSumsATMR[ i ] / (double) numberOfRuns;
            this.finalBestEleganceATMRSD[ i ] = Math.sqrt(  temp );
            
            temp = squaredValueSumsEM[ i ] / (double) numberOfRuns;
            this.finalBestEleganceModularitySD[ i ] = Math.sqrt(  temp );
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
    
    /**
     * write final results of heuristic ant search
     * @param path 
     */
    public void writeFinalHeuristicResults( String path  )
    {
        assert path != null;
        assert path.length( ) > 0;

        // String heuristicsNACResultsFileFullName =  
        //    Parameters.outputFilePath + "\\" + HEURISTIC_NAC_OUTPUT_NAME;
        // 13 Nove 2015
        String heuristicsNACResultsFileFullName =  
            Parameters.outputFilePath + "/" + HEURISTIC_NAC_OUTPUT_NAME;
        
        System.out.println( "file name is: " + heuristicsNACResultsFileFullName );
        final String dir = System.getProperty( "user.dir" );
        System.out.println( "current dir = " + dir );
        
        // set up the output files
        PrintWriter out1 = null;
        
        boolean append = true;
        try 
        {
            // don't want to overwrite existing result files
            out1 = new PrintWriter( new FileWriter( 
                new File( heuristicsNACResultsFileFullName), append ) );
        } 
        catch( IOException ex ) 
        {
            Logger.getLogger(BatchResults.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Can't open " + heuristicsNACResultsFileFullName );
        }   
        
        for( int run = 0; run < numberOfRuns; run++ )
        {
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
            
            
            int heuristics = AlgorithmParameters.heuristics ? 1 : 0;
            
            out1.println( ( Parameters.problemNumber + 1 ) + " " +
                         heuristics + " " +
                         AlgorithmParameters.BETA_CBO + " " +
                         AlgorithmParameters.BETA_NAC + " " +
                         ( run + 1 )  + " " + 
                         df.format( this.bestCBO[ run ] ) + " " + 
                         this.whenCBOfound[ run ] + " " +
                         df.format( this.bestNAC[ run ] ) + " " + 
                         this.whenNACFound[ run ] ); 
        }
            
        out1.close( );
    }
     
    
     /**
     * write final results of ant search
     * handles whether we're looking for fCBO, fNAC or fCOMB
     * added 17 Nov 2015
     */
    public void writeResults( )
    {
        String outputFileName = "";
        
        if( AlgorithmParameters.fitness == AlgorithmParameters.CBO )
        {
            outputFileName = BatchResults.BEST_COUPLING_FILE_NAME;
        }
        else if( AlgorithmParameters.fitness == AlgorithmParameters.NAC )
        {
            outputFileName = BatchResults.BEST_NAC_FILE_NAME;
        }
        else if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
        {
            outputFileName = BatchResults.BEST_COMBINED_FILE_NAME;
        }
        else
        {
            assert false; // execution should never reach this point    
        }
        
        // 13 November 2015
        String resultsFileFullName = "";
        
        if( Parameters.platform == Parameters.Platform.Windows )
        {
            resultsFileFullName = Parameters.outputFilePath + "\\" + outputFileName;
        }
        else    // we're on Mac
        {
            resultsFileFullName = Parameters.outputFilePath + "/" + outputFileName;
        }
        
        
        System.out.println( "file name is: " + resultsFileFullName );
        final String dir = System.getProperty( "user.dir" );
        System.out.println( "current execution directory is: " + dir );
        
        // set up the output files
        PrintWriter out1 = null;
        
        boolean append = true;
        try 
        {
            // don't want to overwrite existing result files
            out1 = new PrintWriter( new FileWriter( 
                new File( resultsFileFullName), append ) );
        } 
        catch( IOException ex ) 
        {
            Logger.getLogger( BatchResults.class.getName()).log(Level.SEVERE, null, ex );
            System.out.println( "Can't open " + resultsFileFullName );
        }   
        
        // for easier analysis in SPSS 14 Jan 2016
        int antiPheromoneOn = 0; // false
        if( AlgorithmParameters.ANTIPHEROMONE_PHASE_THRESHOLD_PERCENTAGE > 0 )
        {
            antiPheromoneOn = 1; // true
        }
        
        int MMAS_50_percent_on = 0; // false
        if( AlgorithmParameters.MMAS_SUBTRACTIVE_ANTIPHEROMONE == true )
        {
            MMAS_50_percent_on = 1;
        }
        
        for( int run = 0; run < numberOfRuns; run++ )
        {
            int evalsWhenCBOFound = this.whenCBOfound[ run ] * AlgorithmParameters.NUMBER_OF_ANTS;
            int evalsWhenNACFound = this.whenNACFound[ run ] * AlgorithmParameters.NUMBER_OF_ANTS;
            int evalsWhenCombinedFound = this.whenCombinedFound[ run ] * AlgorithmParameters.NUMBER_OF_ANTS;
        
            out1.println(   Parameters.problemNumber  + " " +
                            ( run + 1 )  + " " + 
                            AlgorithmParameters.fitness + " " +
                            AlgorithmParameters.algorithm + " " +
                            AlgorithmParameters.NUMBER_OF_ANTS + " " +
                            antiPheromoneOn + " " +
                            AlgorithmParameters.ANTIPHEROMONE_PHASE_THRESHOLD_PERCENTAGE + " " +
                            MMAS_50_percent_on + " " + 
                            
                            df.format( this.bestCBO[ run ] ) + " " + 
                            evalsWhenCBOFound + " " +
                            
                            df.format( this.bestNAC[ run ] ) + " " + 
                            evalsWhenNACFound + " " +
                            
                            df.format( this.bestCombined[ run ] ) + " " + 
                            evalsWhenCombinedFound + " " +
                    
                            this.maxNumberOfInvalids[ run ] );
        }
            
        out1.close( );
    }
     
    
}   // end class

//------- end file ----------------------------------------


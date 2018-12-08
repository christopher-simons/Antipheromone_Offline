/*
 * Controller.java
 * Created 14 June 2012
 * Renamed 30 August 2012
 * Substantially reworked for antipheromone experiments Nov / Dec 2015
 * Added to a GitHub Repository 25 July 2016
 * Substantially reworked for further antipheromone experiments June / July 2018 
 */

package engine;

/**
 * The controller of the ACO engine
 * @author Christopher Simons
 */
    
import config.AlgorithmParameters;
import config.Parameters;
import daemonActions.DaemonOperators;
import heuristics.HeuristicInformation;
import java.text.DecimalFormat;
import java.util.*;
import myUtils.PathComparatorForFcombined;
import myUtils.PathComparatorForTSP;
import pheromone.*;
import problem.*;
import reporting.BatchResults;


public class Controller
{
    /** list of attributes in the software design */
    private final List< Attribute > attributeList;
    
    /** list of methods in the software design */
    private final List< Method > methodList;

    /** list of all the attributes and methods in the problem */
    private final List< Node > amList; 
    
    /** number of classes in software design */
    private static int numberOfClasses;

    /** the colony, which contains all the solution paths */
    private final List< Path > colony;
    
    /** for sorting of the colony based on Fcomb 19 July 2017 */
    Path pathArray[ ];
    
    /** the table containing all the pheromone values */
    private PheromoneMatrix pheromoneTable;
    
    /** the elite archive */
    private Stack< Path > eliteArchive;
    
    /** reference to the problem controller, for use table */
    private ProblemController problemController;
    
    // list of nodes for TSP 16 September 2018
    private List< Node > tspNodes;
    
    /** best so far values */
    private double bestSoFarCBO;
    private double bestSoFarEleganceNAC;
    private double bestSoFarEleganceATMR;
    private double bestSoFarCombined;

    /** best so far indices into colony */
    private int bestCBOIndex;
    private int bestNACIndex;
    private int bestATMRIndex;
    private int bestCombinedIndex;
    
    /** worst so far values */
    private double worstSoFarCBO;
    private double worstSoFarNAC;
    private double worstSoFarCombined;
    
    /** worst so far indices into colony */
    private int worstCBOIndex;
    private int worstNACIndex;
    private int worstCombinedIndex;
    
    // for batch mode 
    private double iterationAverageCBO;
    
    // for MMAS 
    private Path bestPathInColonyCBO;
    private Path bestPathInColonyNAC;
    private Path bestPathInColonyATMR;
    private Path bestPathInColonyCombined;
    // 26 June 2018
    private Path secondBestPathInColonyCombined;
    private Path thirdBestPathInColonyCombined;
    
    
    // 07 Jan 2016
    private Path worstPathInColonyCBO;
    private Path worstPathInColonyNAC;
    private Path worstPathInColonyCombined;
    // 25 July 2017
    private Path secondWorstPathInColonyCombined;
    private Path thirdWorstPathInColonyCombined;
    
    // 2 Feb 2016
    private int maxInvalids;
    
    // 30 May 2012
    private int[ ] numberOfRetries;
    private double[ ] averageAttempts;
    
    /** interaction interval */
    private int interval;
   
    /** interactiveResults of batch, non-interactive search */
    private BatchResults batchResults;
    
    /** in batch mode, run time for each iteration */
    private long[ ] iterationRunTimes;
    
    /** in batch mode, average run time for each run */
    private long[ ] averageRunTimes;
    
    // 28 August 2018
    private double areaAt50;
    private double areaAt100;
    private double areaAt150;
    private double areaAt200;
    private double areaAt300;
    private double areaAt400;
    
    // 17 Spetember 2018
    private Path bestTSPPathInColony;
    private int bestTSPIndex;
    private double bestSoFarTSPPathLength;
    
    private Path secondBestTSPPathInColony;
    private Path thirdBestTSPPathInColony;
    
    private Path worstTSPPathInColony;
    private Path secondWorstTSPPathInColony;
    private Path thirdWorstTSPPathInColony;
    
    
    /** the decimal format for all doubles used by the controller */
    private DecimalFormat df;
    
    /**
     * constructor
     * @param problemController
     */
    public Controller( ProblemController problemController )
    {
        assert problemController != null;
        this.problemController = problemController;
        
        amList = new ArrayList<  >( );
        attributeList = new ArrayList<  >( );
        methodList = new ArrayList<  >( );
        
        
        numberOfClasses = problemController.getNumberOfClasses( );
        
        // 14 Jan 2013
        HeuristicInformation.setUp( 
            this.attributeList.size( ), this.methodList.size( ), Controller.numberOfClasses );
        
        colony = new ArrayList< >( );
        
        // 17 July 2017
        pathArray = new Path[ AlgorithmParameters.NUMBER_OF_ANTS ];
        
        eliteArchive = new Stack< >( );
        
        // 16 September 2018
        tspNodes = new ArrayList< >( );
        
        // set best so far values to arbitrary value
        bestSoFarCBO = 100.0;
        bestSoFarEleganceNAC = 100.0;
        bestSoFarEleganceATMR = 100.0;
        bestSoFarCombined = 100.0;
        bestCBOIndex = 0;
        bestNACIndex = 0;
        bestATMRIndex = 0;
        bestCombinedIndex = 0;
        
        iterationAverageCBO = 0.0;
        bestPathInColonyCBO = null;
        bestPathInColonyNAC = null;
        bestPathInColonyATMR = null;
        bestPathInColonyCombined = null;
        secondBestPathInColonyCombined = null;
        thirdBestPathInColonyCombined = null;
        
        worstSoFarCBO = 100.0;
        worstSoFarNAC = 100.0;
        worstSoFarCombined = 100.0;
    
        worstCBOIndex = 0;
        worstNACIndex = 0;
        worstCombinedIndex = 0;
    
        worstPathInColonyCBO = null;
        worstPathInColonyNAC = null;
        worstPathInColonyCombined = null;
        secondWorstPathInColonyCombined = null;
        thirdWorstPathInColonyCombined = null;
        maxInvalids = 0;
        
        // 30 May 2012
        numberOfRetries = new int[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
        averageAttempts = new double[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];
        
        for( int i = 0; i < AlgorithmParameters.NUMBER_OF_ITERATIONS; i++ )
        {
            numberOfRetries[ i ] = 0;
            averageAttempts[ i ] = 0.0;
        }
    
        interval = 0;
             
        // set up results for batch mode
        batchResults = new BatchResults( AlgorithmParameters.NUMBER_OF_ITERATIONS, Parameters.NUMBER_OF_RUNS );

         /** in batch mode, run time for each iteration */
        iterationRunTimes = new long[ AlgorithmParameters.NUMBER_OF_ITERATIONS ];

        /** in batch mode, average run time for each run */
        averageRunTimes = new long[ Parameters.NUMBER_OF_RUNS ];

        // 28 August 2018
        areaAt50 = 0.0;
        areaAt100 = 0.0;
        areaAt150 = 0.0;
        areaAt200 = 0.0;
        areaAt300 = 0.0;
        areaAt400 = 0.0;
        
        // 17 September 2018
        bestTSPPathInColony = null;
        secondBestTSPPathInColony = null;
        thirdBestTSPPathInColony = null;
        
        bestSoFarTSPPathLength = 999999999.0; // arbitrarily high value
        bestTSPIndex = 0; // arbtrarily low value
        
        worstTSPPathInColony = null;
        secondWorstTSPPathInColony = null;
        thirdWorstTSPPathInColony = null;
        
        
        df = new DecimalFormat( "0.000" );
        
        initialiseLists( problemController );
        // for testing only
//        showLists( );   

    }
    
    /**
     * initialise the lists of attributes, methods, vertices
     * @param problemController 
     */
    private void initialiseLists( ProblemController problemController )
    {
        assert problemController != null;   
        
        
        if( Parameters.problemNumber == Parameters.TSP_BERLIN52 )
        {
            assert this.tspNodes != null;
            
            for( int i = 0; i < TSP_Berlin52.NUMBER_OF_CITIES; i++ )
            {
                Node node = new Node( );
                node.setNumber( i );
                boolean added = this.tspNodes.add( node );
                assert added == true;
            }
        }
        else // must be a software design problem instance
        {
            int counter = 0;

            // set up attributes
            Iterator< CLSDatum > datumIt = problemController.getDatumList( );

            while( datumIt.hasNext( ) )
            {
                CLSDatum datum = datumIt.next( );
                final String name = datum.getName( ); 
                attributeList.add( new Attribute( name, counter ) );
                amList.add( new Attribute( name, counter ) );
                counter++;
            }

            assert attributeList.size( ) == 
                problemController.getNumberOfUniqueData( );

            // set up methods
            Iterator< CLSAction > actionIt = problemController.getActionList( );

            while( actionIt.hasNext( ) )
            {
                CLSAction action = actionIt.next( );
                final String name = action.getName( ); 
                methodList.add( new Method( name, counter ) );
                amList.add( new Method( name, counter ) );
                counter++;
            }

            assert methodList.size( ) ==
                problemController.getNumberOfUniqueActions( );
            assert amList.size( ) ==
                problemController.getNumberOfUniqueData( ) +
                problemController.getNumberOfUniqueActions( );    
        }
    }
    
    
    /**
     * showRawResults the contents of the three lists
     */
    private void showLists( )
    {
        int size = amList.size( );
        System.out.println( );
        System.out.println( "Vertex List:" );
        for( int i = 0; i < size; i++ )
        {
            String s = Integer.toString( amList.get( i ).getNumber( ) );
            s += " ";
            s += amList.get( i ).getName( );
            System.out.println( "\t" + s);
        }
        
        size = attributeList.size( );
        System.out.println( );
        System.out.println( "Attribute List:" );
        for( int i = 0; i < size; i++ )
        {
            String s = Integer.toString( attributeList.get( i ).getNumber( ) );
            s += " ";
            s += attributeList.get( i ).getName( );
            System.out.println( "\t" + s );
        }
        
        size = methodList.size( );
        System.out.println( );
        System.out.println( "Method List:" );
        for( int i = 0; i < size; i++ )
        {
            String s = Integer.toString( methodList.get( i ).getNumber( ) );
            s += " ";
            s += methodList.get( i ).getName( );
            System.out.println( "\t" + s );
        }
    }

  
    /**
     * run the ant colony optimisation search
     * @param runNumber of runs for ACO search
     */
    public void run( int runNumber )
    {
        assert runNumber >= 0;
        //System.out.println("in run number: " + runNumber );
        
        long before = 0;
        long after = 0;
        long iterationTime = 0;
        
        long runBefore = System.currentTimeMillis( );

        // create a new Pheromone table for each run
        pheromoneTable = new PheromoneMatrix( amList, numberOfClasses, problemController );
//        pheromoneTable.show( );
    
        // clear out the archive for the new run
        this.eliteArchive.clear( );
        
        // perform ACO search until iterations are terminated
        for( int i = 0; i < AlgorithmParameters.NUMBER_OF_ITERATIONS; i++  )
        {
            before = System.currentTimeMillis( );
            
            AlphaMatrix alphaTable = new AlphaMatrix( this.pheromoneTable, AlgorithmParameters.alpha );

            // the classic ant colony optimisation loop
            
            generateSolutions( i, alphaTable );
            
            // use if want to update with a best-so-far approach, based on CBO or NAC
            // only for software design problem instances
            if( AlgorithmParameters.replacementElitism == true ) { elitistReplace( ); }
            
            daemonActions( );
            
            BestPathsMatrix bpm = new BestPathsMatrix( pheromoneTable.size( ) );
            pheromoneUpdate( i, bpm );
            
            // record iteration information
            after = System.currentTimeMillis( );
            assert after >= before : "after is: " + after + ", before is: " + before;
            iterationTime = after - before;
            this.iterationRunTimes[ i ] = iterationTime;
                
            // copy fitness values to batch results structure for eventual writing to file 
            batchResults.bestDesignCouplingOverRuns[ runNumber ][ i ] = this.bestSoFarCBO;
            batchResults.bestEleganceNACOverRuns[ runNumber ][ i ] = this.bestSoFarEleganceNAC;
            batchResults.bestCombinedOverRuns[ runNumber ][ i ] = this.bestSoFarCombined;
            
            // 28 June 2018
            // copy solution generation retry and attempt information to batch results structure for writing to file
            for( int j = 0; j < this.numberOfRetries.length; j++ )
            {
                batchResults.retriesOverRuns[ runNumber ][ j ] = this.numberOfRetries[ j ];
                batchResults.averageAttemptsOverRuns[ runNumber ][ j ] = this.averageAttempts[ j ];
            }
            
            // 18 September 2018, for software design problem instances only
            if( Parameters.problemNumber == Parameters.CBS ||
                Parameters.problemNumber == Parameters.GDP || 
                Parameters.problemNumber == Parameters.RANDOMISED ||
                Parameters.problemNumber == Parameters.SC )
            {
                // 23 August 2018. Snapshots, and area under the cost curve
                calculateSnapshots( runNumber, i );
                // 8 August 2018 
                investigateInterference( runNumber, i );
            }
            
            // all done, so lastly make ready for next iteration
            if( AlgorithmParameters.replacementElitism == true ) { updateEliteArchive( ); }
            clearEnvironment( ); 
            
        }   // end for each iteration

        
        
        // record run information
        double average = myUtils.Utility.average( this.iterationRunTimes );
        long temp = Math.round( average );
        this.averageRunTimes[ runNumber ] = temp;
            
        // 2 Feb 2016
        batchResults.maxNumberOfInvalids[ runNumber ] = this.maxInvalids;
        
        batchResults.bestCombinedValueAt50OverRuns[ runNumber ] = this.bestSoFarCombined;
        
        // 18 September 2018 for TSP
        batchResults.bestTSPLength[ runNumber ] = this.bestSoFarTSPPathLength;
        batchResults.whenBestTSPLengthFound[ runNumber ] = this.bestTSPIndex;
        
        // at the end, show the pheromone table
        //this.pheromoneTable.show( );
        
        long runAfter = System.currentTimeMillis( );
        long runTime = runAfter - runBefore;
        
        
        System.out.print( " run number " + ( runNumber + 1 ) + " done in " );
        System.out.println( df.format( runTime / 1000.0 ) + " seconds" );
    }
    
    
    
    /**
     * construction phase
     * @param iteration counter
     * @param alpha table
     */
    private void generateSolutions( 
        int iterationCounter, 
        AlphaMatrix alphaMatrix )
    { 
        assert alphaMatrix != null;
        assert problemController != null;
        
        int retries = 0;
        int attemptTotal = 0;
                
        for( int i = 0; i < AlgorithmParameters.NUMBER_OF_ANTS; i++ )
        {
            Ant ant = new Ant( 
                    this.amList,
                    this.attributeList,
                    this.methodList,
                    this.numberOfClasses, 
                    alphaMatrix, 
                    AlgorithmParameters.constraintHandling,
                    this.tspNodes );  

            assert ant != null; 
            int attempts = 0;
             
            if( AlgorithmParameters.constraintHandling == false )
            {
                // each ant then generates a complete solution (path),
                // and adds it to the colony
                ant.generateSolution( );
                colony.add( ant.getPath( ) );
            }
            else // we are handling constraints, so try repeatedly until we find a valid path
            {
                assert( AlgorithmParameters.constraintHandling == true );
                
                ant.generateSolution( ); 
                
                while( ant.isValidPath( ) == false )
                {
                    ant.generateSolution( ); 
                    attempts++;
                }
                
                assert ant.isValidPath( ) == true; 
                                
                if( attempts > 0 )
                {
                    retries++;
                }
                
                colony.add( ant.getPath( ) );
            }
            
            attemptTotal += attempts;
        }
        
        numberOfRetries[ iterationCounter ] = retries;
        
        if( retries > 0 ) // prevent divide by zero
        {    
            averageAttempts[ iterationCounter ] = (double) attemptTotal / (double) retries;
        }
    }
    
    /**
     * Daemon actions "are used to bias the search from a
     * non-local perspective" (Wikipedia)
     * 
     * In this ACO search, the daemon actions transform all
     * solution paths into software designs in order to
     * calculate fitness (and later, visualization for interaction). 
     */
    private void daemonActions( ) 
    {
        double runningTotalCBO = 0.0;
        this.iterationAverageCBO = 0.0;
        
        // reset metrics to arbitrary values...
        this.bestSoFarCBO = 1.0;
        this.bestSoFarEleganceNAC = 100.0; // arbitrary value
        this.bestSoFarCombined = 100.0; // arbitrary value
        this.bestSoFarTSPPathLength = 9999999.0; // arbitrary valie
        
        this.worstSoFarCBO = 0.0;
        this.worstSoFarNAC = 0.0;
        this.worstSoFarCombined = 0.0;
        
        int invalidCounter = 0;
        this.maxInvalids = 0; 
        
        // assert colony is OK
        assert colony.size( ) == AlgorithmParameters.NUMBER_OF_ANTS :
            "environment size is: " + colony.size( );

        int iterationCounter = 0;
        
        // get each path in colony, determine fitness, find single best and worst etc.
        for( Path path : this.colony )
        {
//            path.showRawResults( );
            
            // if it's a TSP problem instance
            if( Parameters.problemNumber == Parameters.TSP_BERLIN52 ||
                Parameters.problemNumber == Parameters.TSP_ST70 ||
                Parameters.problemNumber == Parameters.TSP_RAT99 ||
                Parameters.problemNumber == Parameters.TSP_RAT195 )    
            {
                // 18 September 2018 calculate the solution path cost in TSP
                DaemonOperators.calculateTSPSolutionPathLength( path, problemController );
            }
            else // must be a software design problem instance
            {
                // 29 November 2015 calculate fitness from the software design path
                DaemonOperators.calculateDesignSolutionPathFitness( path, problemController );

                // keep running total for iteration average
                runningTotalCBO += path.getCBO( ); 

                if( path.isValid( ) == false )
                {
                    invalidCounter++;
                }
            }
            
            calculateSingleBestAndWorst( path, iterationCounter );
            
            iterationCounter++;
            
        }   // end for each solution path in the colony

        // 2 Feb 2016
        if( invalidCounter > this.maxInvalids )
        {
            this.maxInvalids = iterationCounter;
        }
        
        
        if( Parameters.problemNumber == Parameters.CBS || 
            Parameters.problemNumber == Parameters.GDP ||     
            Parameters.problemNumber == Parameters.RANDOMISED ||     
            Parameters.problemNumber == Parameters.SC )
        {
            assert runningTotalCBO >= 0.0;

            if( runningTotalCBO == 0.0 )
            {
                 this.iterationAverageCBO = 0.0;
            }
            else
            {
                this.iterationAverageCBO = 
                    runningTotalCBO / (double) this.colony.size( );
            }

            // for MMAS experiment, 6 September 2012
            this.bestPathInColonyCBO = this.colony.get( this.bestCBOIndex );

            // for multi-objective MMAS, 17 September 2012
            this.bestPathInColonyNAC = this.colony.get( this.bestNACIndex );
            this.bestPathInColonyATMR = this.colony.get( this.bestATMRIndex );

            // 29 November 2015
            this.bestPathInColonyCombined = this.colony.get( this.bestCombinedIndex );

            // 7 January 2016
            this.worstPathInColonyCBO = this.colony.get( this.worstCBOIndex );
            this.worstPathInColonyNAC = this.colony.get( this.worstNACIndex );
        }
        
        
        // 26 June 2018, refactored 17 September 2018
        calculateBestAndWorst( ); // for second and third best and worst
    }
    
    // 5 July 2017
    // Refactor calculation of single best and worst solution paths 
    // in colony into a method.
    private void calculateSingleBestAndWorst( final Path path, final int iterationCounter )
    {
        assert path != null;
        assert iterationCounter >= 0;
        assert iterationCounter < AlgorithmParameters.NUMBER_OF_ANTS;
        
        if( Parameters.problemNumber == Parameters.CBS || 
            Parameters.problemNumber == Parameters.GDP ||
            Parameters.problemNumber == Parameters.RANDOMISED ||
            Parameters.problemNumber == Parameters.SC )
        {
            double externalCoupling = path.getCBO( );
            assert externalCoupling >= 0.0 : "impossible CBO for path: " + iterationCounter;

            // check for best so far CBO
            if( externalCoupling < this.bestSoFarCBO )
            {
                this.bestSoFarCBO = externalCoupling;
                this.bestCBOIndex = iterationCounter;
            }

            // check for worst so far CBO 07 Jan 2016
            if( externalCoupling > this.worstSoFarCBO )
            {
                this.worstSoFarCBO = externalCoupling;
                this.worstCBOIndex = iterationCounter;
            }

            // check for best so far NAC
            double eleganceNAC = path.getEleganceNAC( );
            if( eleganceNAC < bestSoFarEleganceNAC )
            {
                this.bestSoFarEleganceNAC = eleganceNAC;
                this.bestNACIndex = iterationCounter;
            }

            // check for worst so far NAC 1 Feb 2016
            if( eleganceNAC > this.worstSoFarNAC )
            {
                this.worstSoFarNAC = eleganceNAC;
                this.worstNACIndex = iterationCounter;
            }

            // 29 November 2015 Combined fitness secondly...
            double combined = path.getCombined( );
            if( combined < bestSoFarCombined )
            {
                this.bestSoFarCombined = combined;
                this.bestCombinedIndex = iterationCounter;
            }

            // check for worst so far Combined 07 Jan 2016
            if( combined > worstSoFarCombined )
            {
                this.worstSoFarCombined = combined;
                this.worstCombinedIndex = iterationCounter;
            }
        }
        else // must be TSP problem instance
        {
            // 18 September 2018 for TSP
            final double pathLength = path.getTSPPathLength( );
            if( pathLength < this.bestSoFarTSPPathLength )
            {
                this.bestSoFarTSPPathLength = pathLength;
                this.bestTSPIndex = iterationCounter;
            }
        }
    }
    
    /**
     * Calculate best and worst paths in the colony 
     * by sorting the paths in the colony first.
     * Refactored 26 June 2018, 17 September 2018
     */
    private void calculateBestAndWorst( )
    {
        assert this.colony != null;
        
        // if the problem instance is a TSP...
        
        if( Parameters.problemNumber == Parameters.TSP_BERLIN52 ||
            Parameters.problemNumber == Parameters.TSP_ST70 || 
            Parameters.problemNumber == Parameters.TSP_RAT99 ||
            Parameters.problemNumber == Parameters.TSP_RAT195 )     
        {
            double bestTSPPathLength = 0.0;
            double secondBestTSPPathLength = 0.0;
            double thirdBestTSPPathLength = 0.0;
            
            this.colony.sort( new PathComparatorForTSP( ) );
            
            this.bestTSPPathInColony = this.colony.get( 0 );
            bestTSPPathLength = this.bestTSPPathInColony.getTSPPathLength( );
            this.secondBestTSPPathInColony = this.colony.get( 1 );
            secondBestTSPPathLength = this.secondBestTSPPathInColony.getTSPPathLength( );
            this.thirdBestTSPPathInColony = this.colony.get( 2 );
            thirdBestTSPPathLength = this.thirdBestTSPPathInColony.getTSPPathLength( );
            
            assert bestTSPPathLength  <= secondBestTSPPathLength;
            assert secondBestTSPPathLength <= thirdBestTSPPathLength;
            
            double worstTSPPathLength = 0.0;
            double secondWorstTSPPathLength = 0.0;
            double thirdWorstTSPPathLength = 0.0;
            
            this.worstTSPPathInColony = this.colony.get( AlgorithmParameters.NUMBER_OF_ANTS - 1 );
            worstTSPPathLength = this.worstTSPPathInColony.getTSPPathLength( );
            this.secondWorstTSPPathInColony = this.colony.get( AlgorithmParameters.NUMBER_OF_ANTS - 2 );
            secondWorstTSPPathLength = this.secondWorstTSPPathInColony.getTSPPathLength( );
            this.thirdWorstTSPPathInColony = this.colony.get( AlgorithmParameters.NUMBER_OF_ANTS - 3 );
            thirdWorstTSPPathLength = this.thirdWorstTSPPathInColony.getTSPPathLength( );
            
            assert worstTSPPathLength >= secondWorstTSPPathLength;
            assert secondWorstTSPPathLength >= thirdWorstTSPPathLength;
        }
        else // must be a software design problem instance
        {
            // fistly sort the colony according to Fcomb
            this.colony.sort( new PathComparatorForFcombined( ) );   
        
            // find the second and third best paths
            // (best path already calulated previously). 
            final double bestCombined = this.colony.get( 0 ).getCombined( );

            this.secondBestPathInColonyCombined = this.colony.get( 1 );
            final double secondBestCombined = this.secondBestPathInColonyCombined.getCombined( );
            assert bestCombined <= secondBestCombined : 
                    "error in second best!" + 
                    " best is: " + bestCombined +
                    " and second best is: " + secondBestCombined;

            this.thirdBestPathInColonyCombined = this.colony.get( 2 );
            final double thirdBestCombined = this.thirdBestPathInColonyCombined.getCombined( );
            assert secondBestCombined <= thirdBestCombined : 
                    "error in third best!" + 
                    " second best is: " + secondBestCombined + 
                    " and third best is: " + thirdBestCombined;

            this.worstPathInColonyCombined = this.colony.get( AlgorithmParameters.NUMBER_OF_ANTS - 1 );

            final double sortedColonyWorstCombinedValue = this.worstPathInColonyCombined.getCombined( );
            // System.out.println( "  update worst is: " + this.worstPathInColonyCombined.toString( )+ " " + sortedColonyWorstCombinedValue );

            // 25 July 2017
            this.secondWorstPathInColonyCombined = this.colony.get( AlgorithmParameters.NUMBER_OF_ANTS - 2 );
            final double sortedColonySecondWorstCombined = this.secondWorstPathInColonyCombined.getCombined( );
            assert sortedColonySecondWorstCombined <= sortedColonyWorstCombinedValue : "error in second worst!";

            this.thirdWorstPathInColonyCombined = this.colony.get( AlgorithmParameters.NUMBER_OF_ANTS - 3 );
            final double sortedColonyThirdWorstCombined = this.thirdWorstPathInColonyCombined.getCombined( );
            assert sortedColonyThirdWorstCombined <= sortedColonySecondWorstCombined : "error in third worst!";
        }
    }    
    
   /**
     * adjust the pheromone levels:
     * 1) apply evaporation
     * 2) update Trail levels based on ant construction
     * @param weights for multi-objective pheromone update
     */ 
    private void pheromoneUpdate( int iteration, BestPathsMatrix bpm )
    { 
        assert iteration >= 0;
        assert bpm != null; 
        
        PheromoneOperators.evaporate( pheromoneTable );
        
//        pheromoneTable.showRawResults( );
        
        PheromoneOperators.update(
            this.pheromoneTable, 
            this.colony, 
            this.bestPathInColonyCBO,
            this.bestPathInColonyNAC,
            this.bestPathInColonyATMR,
            this.bestPathInColonyCombined,
            this.secondBestPathInColonyCombined,
            this.thirdBestPathInColonyCombined,
            this.worstPathInColonyCBO,
            this.worstPathInColonyNAC,
            this.worstPathInColonyCombined,
            this.secondWorstPathInColonyCombined,
            this.thirdWorstPathInColonyCombined,
            iteration,
            bpm,
            this.bestTSPPathInColony,
            this.secondBestTSPPathInColony,
            this.thirdBestTSPPathInColony,
            this.worstTSPPathInColony,
            this.secondWorstTSPPathInColony,
            this.thirdWorstTSPPathInColony );
        
//        pheromoneTable.showRawResults( );
    }

    /**
     * elitist replacement of path(s) into the colony
     */
    private void elitistReplace( )
    {
        final int size = colony.size( );
        
        // for debug
        // final int eliteSize = this.eliteArchive.size( );
        
        while( this.eliteArchive.empty( ) == false ) // handle first iteration where eilte archive is empty
        {
            int randomIndex = myUtils.Utility.getRandomInRange( 0, size - 1 );
            
            Path elitePath = this.eliteArchive.pop( ); // pop the element in the archive
        
            colony.set( randomIndex, elitePath );
        }
    }
    
    /**
     * update the elite archive to hold to fittest path(s)in the colony 
     */
    private void updateEliteArchive(  )
    {
        this.eliteArchive.clear( );

        double bestCBO, bestNAC, bestATMR;
        bestCBO = bestNAC = bestATMR = 100.0; // worst value
        
        Path bestCBOPath = null;
        Path bestNACPath = null; 
        Path bestATMRPath = null;
        
        Iterator< Path > it = colony.iterator( );
        
        while( it.hasNext( ) )
        {
            Path p = it.next( );
            
            if( p.getCBO( ) < bestCBO )
            {
                bestCBO = p.getCBO( );
                bestCBOPath = p;
            }
            
            if( p.getEleganceNAC( ) < bestNAC )
            {
                bestNAC = p.getEleganceNAC( );
                bestNACPath = p;
            }
            
            if( p.getEleganceATMR( ) < bestATMR )
            {
                bestATMR = p.getEleganceATMR( );
                bestATMRPath = p;
            }
        }
        
        assert this.eliteArchive.isEmpty( );
        assert bestCBOPath != null; 
        assert bestNACPath != null;
        assert bestATMRPath != null;
        
        if( AlgorithmParameters.objectiveCBO == true )
        {
            this.eliteArchive.push( bestCBOPath );
        }
        
        if( AlgorithmParameters.objectiveNAC == true )
        {
            this.eliteArchive.push( bestNACPath );
        }
        
        // 10 April 2013 comment out
//        if( AlgorithmParameters.objectiveATMR == 
//            AlgorithmParameters.Toggle.on )
//        {
//            this.eliteArchive.push( bestATMRPath );
//        }
        
        assert this.eliteArchive.size( ) <= 3;
    }
    
    
    /**
     * clear the colony of all path solutions, 
     * both complete and partial
     */ 
    private void clearEnvironment( )
    {
        assert colony.size( ) > 0; // assert that paths exist
        colony.clear( );
    }
      
    /**
     *  Calculate final results and write them to appropriate output file
     *  17 Nov 2015 
     */
    public void writeResultstoFile( )
    {   
        batchResults.calculateFinalResults( );
        batchResults.writeResults( );
    }
    
    // 5 September 2018 refactor for ACO iterations
    private void investigateInterference( final int runNumber, final int iteration )
    {
        assert runNumber >= 0;
        assert iteration >= 0;
        
        BestPathsMatrix bpm = new BestPathsMatrix( pheromoneTable.size( ) );
        switch( AlgorithmParameters.pheromoneStrength )
        {
            case AlgorithmParameters.MMAS_PHEROMONE_TRIPLE:
                bpm.recordPath( this.thirdBestPathInColonyCombined );
            case AlgorithmParameters.MMAS_PHEROMONE_DOUBLE:
                bpm.recordPath( this.secondBestPathInColonyCombined );
            case AlgorithmParameters.MMAS_PHEROMONE_SINGLE:
                bpm.recordPath( this.bestPathInColonyCombined );
                break;
            default:
                assert false : "impossible pheromone strength!";
        }
//        bpm.show( );
        
        WorstPathsMatrix wpm = new WorstPathsMatrix( pheromoneTable.size( ) );
        if( AlgorithmParameters.MMAS_ANTIPHEROMONE == true )
            //    && AlgorithmParameters.antiPheromonePhasePercentage > 0 )
        {
            switch( AlgorithmParameters.antipheromoneStrength )
            {
                case AlgorithmParameters.ANTIPHEROMONE_STRENGTH_TRIPLE:
                    wpm.recordPath( this.thirdWorstPathInColonyCombined );
                case AlgorithmParameters.ANTIPHEROMONE_STRENGTH_DOUBLE:
                    wpm.recordPath( this.secondWorstPathInColonyCombined );
                case AlgorithmParameters.ANTIPHEROMONE_STRENGTH_SINGLE:
                    wpm.recordPath( this.worstPathInColonyCombined );
                    break;
                default:
                    assert false : "impossible pheromone strength!";
            }
        }
//        wpm.show( );
        
        PathInterferenceMatrix pim = new PathInterferenceMatrix( pheromoneTable.size( ) );
        pim.registerBestPathMatrix( bpm );
        pim.registerWorstPathMatrix( wpm );
//        pim.showInterferenceMatrix( );
        
        double interference = pim.getInterference( ) * 100;
//        System.out.println( "interference is: " + df.format( interference ) + "%" ); 
        
        batchResults.interference[ runNumber ][ iteration ] = interference;
    }
    
    private void calculateSnapshots( final int runNumber, final int iteration )
    {
        assert runNumber >= 0;
        assert iteration >= 0;
       
        if( iteration == 50 )
        {
            batchResults.bestCombinedValueAt50OverRuns[ runNumber ] = this.bestSoFarCombined;
            calculateAreaAt50( runNumber );
            batchResults.areaAt50OverRuns[ runNumber ] = this.areaAt50;
        }
        else if( iteration == 100 )
        {
            batchResults.bestCombinedValueAt100OverRuns[ runNumber ] = this.bestSoFarCombined;
            calculateAreaAt100( runNumber );
            batchResults.areaAt100OverRuns[ runNumber ] = this.areaAt100;
        }
        else if( iteration == 150 )
        {
            batchResults.bestCombinedValueAt150OverRuns[ runNumber ] = this.bestSoFarCombined;
            calculateAreaAt150( runNumber );
            batchResults.areaAt150OverRuns[ runNumber ] = this.areaAt150;
        }
        else if( iteration == 200 )
        {
            batchResults.bestCombinedValueAt200OverRuns[ runNumber ] = this.bestSoFarCombined;
            calculateAreaAt200( runNumber );
            batchResults.areaAt200OverRuns[ runNumber ] = this.areaAt200;
        }
        else if( iteration == 300 )
        {
            batchResults.bestCombinedValueAt300OverRuns[ runNumber ] = this.bestSoFarCombined;
            calculateAreaAt300( runNumber );
            batchResults.areaAt300OverRuns[ runNumber ] = this.areaAt300;
        }
        else if( iteration == 400 )
        {
            batchResults.bestCombinedValueAt400OverRuns[ runNumber ] = this.bestSoFarCombined;
            calculateAreaAt400( runNumber );
            batchResults.areaAt400OverRuns[ runNumber ] = this.areaAt400;
        }
        else
        {
            // do nothing!
        }
    }
    
    
    private void calculateAreaAt50( final int runNumber )
    {
        final int start = 1;
        final int end = 50;
        double temp = 0.0;
        
        for( int i = start; i <= end; i++ )
        {
            temp += (double) i * batchResults.bestCombinedOverRuns[ runNumber ][ i ];
        }
        
        this.areaAt50 = temp;
    }
    
    private void calculateAreaAt100( final int runNumber )
    {
        int iteration = 50;
        final int areaSize = 50;
        double temp = 0.0;
        
        for( int i = 1; i <= areaSize; i++  )
        {
            temp += (double) i * batchResults.bestCombinedOverRuns[ runNumber ][ iteration ];
            iteration++;
        }
        
        this.areaAt100 = temp;
    }
    
    private void calculateAreaAt150( final int runNumber )
    {
        int iteration = 100;
        final int areaSize = 50;
        double temp = 0.0;
        
        for( int i = 1; i <= areaSize; i++  )
        {
            temp += (double) i * batchResults.bestCombinedOverRuns[ runNumber ][ iteration ];
            iteration++;
        }
        
        this.areaAt150 = temp;
    }
    
    private void calculateAreaAt200( final int runNumber )
    {
        int iteration = 150;
        final int areaSize = 50;
        double temp = 0.0;
        
        for( int i = 1; i <= areaSize; i++  )
        {
            temp += (double) i * batchResults.bestCombinedOverRuns[ runNumber ][ iteration ];
            iteration++;
        }
        
        this.areaAt200 = temp;
    }
    
    private void calculateAreaAt300( final int runNumber )
    {
        int iteration = 200;
        final int areaSize = 100;
        double temp = 0.0;
        
        for( int i = 1; i <= areaSize; i++  )
        {
            temp += (double) i * batchResults.bestCombinedOverRuns[ runNumber ][ iteration ];
            iteration++;
        }
        
        this.areaAt300 = temp;
    }
    
    private void calculateAreaAt400( final int runNumber )
    {
        int iteration = 300;
        final int areaSize = 100;
        double temp = 0.0;
        
        for( int i = 1; i <= areaSize; i++  )
        {
            temp += (double) i * batchResults.bestCombinedOverRuns[ runNumber ][ iteration ];
            iteration++;
        }
        
        this.areaAt400 = temp;
    }
}   // end class

//------- end file ----------------------------------------


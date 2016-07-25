/*
 * Controller.java
 * Created 14 June 2012
 * Renamed 30 August 2012
 * Substantially reworked for antipheromone experiments Nov / Dec 2015
 * 
 */

package engine;

/**
 * The controller of the ACO engine
 * @author Christopher Simons
 */
    
import config.AlgorithmParameters;
import config.Parameters;
import daemonActions.DaemonOperators;
import heuristics.HeuristicAnt2;
import heuristics.HeuristicInformation;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.JOptionPane;
import learning.Coefficients;
import learning.IterationInformation;
import learning.RegressionAgent;
import myGui.VisualiseEvaluateDialog;
import myUtils.Utility;
import myUtils.Weights;
import net.sourceforge.openforecast.Observation;
import pareto.ParetoOperators;
import pheromone.AlphaTable;
import pheromone.PheromoneOperators;
import pheromone.PheromoneTable;
import problem.CLSAction;
import problem.CLSDatum;
import problem.ProblemController;
import reporting.BatchResults;
import reporting.InteractiveResults;
import reporting.MultiObjectiveResults;
import softwareDesign.CLSClass;
import softwareDesign.EleganceDesign;


public class Controller
{
    /** number of iterations of search */
    private final int NUMBER_OF_ITERATIONS;
        
    /** number of ants in colony */
    private final int NUMBER_OF_ANTS;
        
    /** maximum number of attempts to produce a valid solution */
    // 22 Jan 2013 double number when using coupling heuristics only
    private final int MAXIMUM_ATTEMPTS = 100;

    /** list (set) of attributes of the software design */
    private List< Attribute > attributeList;
    
    /** list (set) of methods in the software design */
    private List< Method > methodList;

    /** list of all the attributes and methods in the colony */
    private List< Node > amList; 
    
    /** number of classes in software design */
    private static int numberOfClasses;

    /** the colony, which contains all the solution paths */
    private List< Path > colony;
    
    /** the table containing all the pheromone values */
    private PheromoneTable pheromoneTable;
    
    /** the elite archive */
    private Stack< Path > eliteArchive;
    
    /** the problem controller, for use table */
    private ProblemController problemController;
    
    /** best so far values */
    private double bestSoFarCBO;
    private double bestSoFarEleganceNAC;
    private double bestSoFarEleganceATMR;
    private double bestSoFarEleganceModularity;
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
    
    // 07 Jan 2016
    private Path worstPathInColonyCBO;
    private Path worstPathInColonyNAC;
    private Path worstPathInColonyCombined;
    
    // 2 Feb 2016
    private int maxInvalids;
    
    
    /** 
     * counter for best in colony to present to user
     * fitness function counter, or ffCounter
     */
    private static int ffCounter = 0;
    
    // 30 May 2012
    private int[ ] numberOfRetries;
    private double[ ] averageAttempts;
    
    /** interaction interval */
    private int interval;
    
    // 31 August 2012
    /** interactiveResults over interactive search */
    private InteractiveResults interactiveResults;
    
    /** interactiveResults of batch, non-interactive search */
    private BatchResults batchResults;
    
    /** in batch mode, run time for each iteration */
    private long[ ] iterationRunTimes;
    
    /** in batch mode, average run time for each run */
    private long[ ] averageRunTimes;
    
    /** the decimal format for all doubles used by the controller */
    private DecimalFormat df;
    
    /**
     * constructor
     * @param problemController
     * @param mode
     */
    public Controller( ProblemController problemController )
    {
        assert problemController != null;
        this.problemController = problemController;
        
        amList = new ArrayList<  >( );
        attributeList = new ArrayList<  >( );
        methodList = new ArrayList<  >( );
        
        // update local parameter information 
        NUMBER_OF_ITERATIONS = AlgorithmParameters.NUMBER_OF_ITERATIONS;
        NUMBER_OF_ANTS = AlgorithmParameters.NUMBER_OF_ANTS;
        
        initialiseLists( problemController );
        // for testing only
//        showLists( );   

        numberOfClasses = problemController.getNumberOfClasses( );
        
        // 14 Jan 2013
        HeuristicInformation.setUp( 
            this.attributeList.size( ), this.methodList.size( ), Controller.numberOfClasses );
        
        colony = new ArrayList< >( );
        eliteArchive = new Stack< >( );
        
        // set best so far values to arbitrary value
        bestSoFarCBO = 100.0;
        bestSoFarEleganceNAC = 100.0;
        bestSoFarEleganceATMR = 100.0;
        bestSoFarEleganceModularity = 0.0;
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
        
        worstSoFarCBO = 100.0;
        worstSoFarNAC = 100.0;
        worstSoFarCombined = 100.0;
    
        worstCBOIndex = 0;
        worstNACIndex = 0;
        worstCombinedIndex = 0;
    
        worstPathInColonyCBO = null;
        worstPathInColonyNAC = null;
        worstPathInColonyCombined = null;
        
        maxInvalids = 0;
        
        // 30 May 2012
        numberOfRetries = new int[ NUMBER_OF_ITERATIONS ];
        averageAttempts = new double[ NUMBER_OF_ITERATIONS ];
        
        for( int i = 0; i < NUMBER_OF_ITERATIONS; i++ )
        {
            numberOfRetries[ i ] = 0;
            averageAttempts[ i ] = 0.0;
        }
    
        interval = 0;
        
        if( Parameters.mode == Parameters.Mode.Interactive )
        {
            interactiveResults = new InteractiveResults( );
            batchResults = null;
            iterationRunTimes = null;
            averageRunTimes = null;        
        }
        else    // must be batch mode
        {
            interactiveResults = null;
            
            // run search multiple times in batch mode
            batchResults = new BatchResults( NUMBER_OF_ITERATIONS, Parameters.NUMBER_OF_RUNS );
            
             /** in batch mode, run time for each iteration */
            iterationRunTimes = new long[ NUMBER_OF_ITERATIONS ];
    
            /** in batch mode, average run time for each run */
            averageRunTimes = new long[ Parameters.NUMBER_OF_RUNS ];
        }
        
        df = new DecimalFormat( "0.000" );
        
    }
    
    /**
     * initialize the lists of attributes, methods, vertices
     * @param problemController 
     */
    private void initialiseLists( ProblemController problemController )
    {
        assert problemController != null;   
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
     * run the ant colony search
     */
    public void run( int runNumber )
    {
        assert runNumber >= 0;
        
        long before = 0;
        long after = 0;
        long iterationTime = 0;
        double iterationTimeInSeconds = 0.0;
        
        long runBefore = System.currentTimeMillis( );

        // create a new Pheromone table for each run
        pheromoneTable = new PheromoneTable( amList, numberOfClasses, problemController );
//        pheromoneTable.show( );
        
        Weights weights = null;
        if( Parameters.mode == Parameters.mode.Interactive )
        {
            // for start of dynamic interactive multi-objective search
            weights = new Weights(
                AlgorithmParameters.INITIAL_wCBO,
                AlgorithmParameters.INITIAL_wNAC,
                AlgorithmParameters.INITIAL_wATMR,
                AlgorithmParameters.INITIAL_wCOMBINED );
        }
        else    // must be in batch mode
        {
            // all weights set to zero initially
            weights = new Weights( );
            
            // 8 December 2015
            // using single-objective in batch mode, so set up weights once
            if( AlgorithmParameters.fitness == AlgorithmParameters.CBO )
            {
                weights.weightCBO = 1.0;
            }    
            else if( AlgorithmParameters.fitness == AlgorithmParameters.NAC )
            {
                weights.weightNAC = 1.0;
            }    
            else if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
            {
                weights.weightCOMBINED = 1.0;
            }    
            else
            {
                assert false: "impossible fitness to weights!";
            }
        }
        
        // list of classes that user elects to "freeze"
        List< CLSClass > freezeList = new ArrayList< >( ); 
        
        // the archive for designs user selects to put away
        List< EleganceDesign > archive = new ArrayList< >( );
        
        // create a new archive for the new run
        this.eliteArchive = new Stack< >( );
        
        int interactionCounter = 1;
        
        boolean halted = false;
        
        // perform ACO search until iterations are terminated
        for( int i = 0; i < NUMBER_OF_ITERATIONS && halted == false; i++  )
        {
            before = System.currentTimeMillis( );
            
            // Iteration information used in interactive mode
            IterationInformation information = new IterationInformation( );
            
            AlphaTable alphaTable = new AlphaTable( this.pheromoneTable, AlgorithmParameters.ALPHA );

            // now begin the classic ant colony optimisation loop
            
            generateSolutions( i, alphaTable, freezeList );
            
            if( AlgorithmParameters.replacementElitism == true ) { elitistInsert( ); }
            
            daemonActions( );
            
            pheromoneUpdate( weights, i );
            
            if( Parameters.mode == Parameters.mode.Interactive )
            {
                if( checkForInteraction( ) == true )
                {
                    int userAction = performInteraction(
                        weights, 
                        freezeList, 
                        i, 
                        interactionCounter, 
                        information,
                        archive );

                    interactionCounter++; 

                    if( userAction == JOptionPane.CANCEL_OPTION ) // we're finished now!
                    {
                        halted = true;
                    }
                }
            }
            else // mode == Mode.batch
            {
                // do nothing, coz there is no interaction in batch mode
            }
            
            after = System.currentTimeMillis( );
            assert after >= before : "after is: " + after + ", before is: " + before;
            iterationTime = after - before;
            iterationTimeInSeconds = iterationTime / 1000.0; // convert to seconds
            
            if( Parameters.mode == Parameters.Mode.Interactive )
            {
                interactiveResults.transferResults(i, 
                    this.bestSoFarCBO, 
                    this.bestSoFarEleganceNAC,
                    this.bestSoFarEleganceATMR,
                    this.bestSoFarEleganceModularity,
                    weights.weightCBO,
                    weights.weightNAC,
                    weights.weightATMR,
                    information.mad,
                    information.designerEvaluation,
                    iterationTimeInSeconds,
                    information.archived,
                    information.classFrozen,
                    information.classUnfrozen,
                    halted );
            }
            else    // must be in batch mode
            {
                this.iterationRunTimes[ i ] = iterationTime;
                
                // commented out 7 December 2015
                // batchResults.averageDesignCouplingOverRuns[ runNumber ][ i ] = this.iterationAverageCBO;
                // batchResults.bestEleganceATMROverRuns[ runNumber ][ i ] = this.bestSoFarEleganceATMR;
                // batchResults.bestEleganceModularityOverRuns[ runNumber ][ i ] = this.bestSoFarEleganceModularity;
                
                
                batchResults.bestDesignCouplingOverRuns[ runNumber ][ i ] = this.bestSoFarCBO;
                batchResults.bestEleganceNACOverRuns[ runNumber ][ i ] = this.bestSoFarEleganceNAC;
                
                // 7 December 2015
                batchResults.bestCombinedOverRuns[ runNumber ][ i ] = this.bestSoFarCombined;
                
                
            }
                
            // make ready for next iteration
            if( AlgorithmParameters.replacementElitism == true ) { updateEliteArchive( ); }
            clearEnvironment( ); 

        }   // end for each iteration

        if( Parameters.mode == Parameters.Mode.Interactive )
        {
    //        interactiveResults.showRawResults( );
            interactiveResults.writeFinalResultsToFile( );
        }
        else    // must be in batch mode
        {
            double average = myUtils.Utility.average( this.iterationRunTimes );
            long temp = Math.round( average );
            this.averageRunTimes[ runNumber ] = temp;
            
            // 19 September 2012 - to assess front diversity
//            doMultiObjectiveValues( freezeList );
            
            // 2 Feb 2016
            batchResults.maxNumberOfInvalids[ runNumber ] = this.maxInvalids;
        }
       
        // at the end, show the pheromone table
        //this.pheromoneTable.show( );
        
        long runAfter = System.currentTimeMillis( );
        long runTime = runAfter - runBefore;
        DecimalFormat df = new DecimalFormat( "0.000" );
        System.out.print( " run number " + runNumber + " done" );
        System.out.println(" in " + df.format( runTime / 1000.0 ) + " seconds" );
    }
    
    /**
     * for use in batch mode only
     * after the iterations are complete, use the pheromone
     * table to generate a set of ants (solutions).
     * Then construct a multi-objective results object
     * and use it to write to file. 
     * @param freezeList 
     */
    private void doMultiObjectiveValues( List< CLSClass > freezeList )
    {
        
        AlphaTable at = new AlphaTable( this.pheromoneTable, AlgorithmParameters.ALPHA );
        generateSolutions( 0, at, freezeList );
        // get the use table in readiness
        SortedMap< String, List< CLSDatum > > useTable =
            problemController.getUseTable( );    
        // for each path in the colony
        Iterator< Path > it = colony.iterator( ); 
        // get the path, and construct a software design
        while( it.hasNext( ) )
        {
            Path path = it.next( );
            // calculate coupling, NAC elegance, ATMR elegance
            DaemonOperators.calculateDesignSolutionPathFitness( 
                path, useTable );
        }
        MultiObjectiveResults moResults = new MultiObjectiveResults( this.colony );
        moResults.transfer( this.colony );
        moResults.writeToFile( ); 
        clearEnvironment( );
    }
    
    /**
     * construction phase
     * @param iteration counter
     * @param alpha table
     * @param list of classes that user elects to freeze
     */
    private void generateSolutions( 
        int iterationCounter, 
        AlphaTable alphaTable, 
        List< CLSClass > freezeList )
    { 
        assert alphaTable != null;
        assert freezeList != null;
        assert problemController != null;
        
        int retries = 0;
        int attemptTotal = 0;
                
        for( int i = 0; i < NUMBER_OF_ANTS; i++ )
        {
            Ant ant = null;
            
            // ant = new HeuristicAnt2( /* 8 January 2016 */
            ant = new Ant( 
                    amList, 
                    numberOfClasses, 
                    alphaTable, 
                    freezeList, 
                    AlgorithmParameters.constraintHandling );  
                    // this.problemController.getUseMatrix( ) );

            assert ant != null; 
            int attempts = 0;
             
            if( AlgorithmParameters.constraintHandling == false )
            {
                // each ant then generates a complete solution (path),
                // and adds it to the colony
                ant.generateSolution( );
                colony.add( ant.getPath( ) );
            }
            else // we are handling constraints
            {
                // 28 May 2012 - experiment into constraint handling
                // see if production of only valid solutions
                // - is possible
                // - and if so, how many attempts?
                
                ant.generateSolution( );
                
                // while( ant.isValidPath( ) == false && attempts < MAXIMUM_ATTEMPTS ) 13 January 2016
                while( ant.isValidPath( ) == false )
                {
                    ant.generateSolution( );
                    attempts++;
                }
//                if( attempts == MAXIMUM_ATTEMPTS )
//                {
//                    assert false : "maximum attempts reached";
//                }
                
                if( attempts > 0 )
                {
                    retries++;
                }
                colony.add( ant.getPath( ) );
//                System.out.println( "number of attempts is: " + attempts );
                
            }
           
            attemptTotal += attempts;
            
            // and then each ant generates partial solutions
            // from the immediately previously constructed solution path
//            ant.generatePartialSolutions( );
        }
        
        numberOfRetries[ iterationCounter ] = retries;
        if( retries > 0 )
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
     * @param multi-objective weights
     */
    private void daemonActions( ) 
    {
        // get the use table and use matrix in readiness
        SortedMap< String, List< CLSDatum > > useTable = problemController.getUseTable( );
        assert useTable != null;
        
        double runningTotalCBO = 0.0;
        this.iterationAverageCBO = 0.0;
        
        // reset metrics to arbitrary values...
        this.bestSoFarCBO = 1.0;
        this.bestSoFarEleganceNAC = 100.0; // arbitrary value
//        this.bestSoFarEleganceATMR = 100.0; // arbitrary value
//        this.bestSoFarEleganceModularity = 0.0;
        this.bestSoFarCombined = 100.0; // arbitrary value
                
        this.worstSoFarCBO = 0.0;
        this.worstSoFarNAC = 0.0;
        this.worstSoFarCombined = 0.0;
        
        int invalidCounter = 0;
        this.maxInvalids = 0; 
        
        // assert colony is OK
        assert colony.size( ) == AlgorithmParameters.NUMBER_OF_ANTS :
            "environment size is: " + colony.size( );

        // for each path in the colony
        Iterator< Path > it = colony.iterator( ); 
        int counter = 0;
        
        // get the path, and determine fitness etc.
        for( Path path : colony )
        {
//            path.showRawResults( );
            
            // 29 November 2015 calculate fitness from the path
            // calculates CBO, NAC, ATMTR and Combined in one go!
            DaemonOperators.calculateDesignSolutionPathFitness( path, problemController );
            
            // CBO related fitness first
            double externalCoupling = path.getCBO( );
            
            if( AlgorithmParameters.constraintHandling == true )
            {
                if( externalCoupling == 0.0 )
                {
                    System.out.println( "impossible CBO for path: " + counter );
                }
            }
            // keep running total for iteration average
            runningTotalCBO += externalCoupling; 
            
            // check for best so far CBO
            if( externalCoupling < this.bestSoFarCBO )
            {
                this.bestSoFarCBO = externalCoupling;
                this.bestCBOIndex = counter;
            }
            
            // check for worst so far CBO 07 Jan 2016
            if( externalCoupling > this.worstSoFarCBO )
            {
                this.worstSoFarCBO = externalCoupling;
                this.worstCBOIndex = counter;
            }
            
            // check for best so far NAC
            double eleganceNAC = path.getEleganceNAC( );
            if( eleganceNAC < bestSoFarEleganceNAC )
            {
                this.bestSoFarEleganceNAC = eleganceNAC;
                this.bestNACIndex = counter;
            }
            
            // check for worst so far NAC 1 Feb 2016
            if( eleganceNAC > this.worstSoFarNAC )
            {
                this.worstSoFarNAC = eleganceNAC;
                this.worstCBOIndex = counter;
            }
            
            
            // 29 November 2015 Combined fitness secondly...
            double combined = path.getCombined( );
            if( combined < bestSoFarCombined )
            {
                this.bestSoFarCombined = combined;
                this.bestCombinedIndex = counter;
            }
            
            // check for worst so far CBO 07 Jan 2016
            if( combined > worstSoFarCombined )
            {
                this.worstSoFarCBO = combined;
                this.worstCombinedIndex = counter;
            }
            counter++;

            if( path.isValid( ) == false )
            {
                invalidCounter++;
            }
        }   // end for each solution path in the colony

        // 2 Feb 2016
        if( invalidCounter > this.maxInvalids )
        {
            this.maxInvalids = counter;
        }
        
        
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
        this.worstPathInColonyCombined = this.colony.get( this.worstCombinedIndex );
        
    }
    
   /**
     * adjust the pheromone levels:
     * 1) apply evaporation
     * 2) update Trail levels based on ant construction
     * @param weights for multi-objective pheromone update
     */ 
    private void pheromoneUpdate( Weights weights, int iteration )
    { 
        assert weights != null;
        assert iteration >= 0;
        
        PheromoneOperators.evaporate( pheromoneTable );
        
//        pheromoneTable.showRawResults( );
        
        PheromoneOperators.update(
            this.pheromoneTable, 
            this.colony, 
            weights, 
            this.bestPathInColonyCBO,
            this.bestPathInColonyNAC,
            this.bestPathInColonyATMR,
            this.bestPathInColonyCombined,
            this.worstPathInColonyCBO,
            this.worstPathInColonyNAC,
            this.worstPathInColonyCombined,
            iteration );
        
//        pheromoneTable.showRawResults( );
    }

    
    /**
     * Does the designer interact at this iteration?
     * Apply the notion of the fitness proportionate
     * interactive interval, based on best CBO
     * @return true if interaction is appropriate, false otherwise
     */
    private boolean checkForInteraction() 
    {
        boolean result = false;
        final double squareValue = this.bestSoFarCBO * this.bestSoFarCBO;
        final double value = 
            squareValue * AlgorithmParameters.INTERACTIVE_INTERVAL_CONSTANT;
        long rounded = Math.round(value);
        
        // ensure the rounded number is at least 1
        if (rounded < 1) 
        {
            rounded = 1;
        }
        
        if (interval < rounded) 
        {
            interval++;
            System.out.println( "interval is: " + interval );
        } 
        else 
        {
            interval = 0;
            result = true;
        }
        return result;
    }
    
    /**
     * elitist insert of path(s) into the colony
     */
    private void elitistInsert( )
    {
        final int size = colony.size( );
        
        while( this.eliteArchive.empty( ) == false ) // handle first iteration where eilte archive is empty
        {
            int randomIndex = myUtils.Utility.getRandomInRange( 0, size - 1 );
            
            Path elitePath = this.eliteArchive.pop( ); // pop the element in the archive
        
            colony.set( randomIndex, elitePath );
        }
    }
    
    /**
     * update the elite archive to hold to fittest path
     * in the colony 
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
        
        // 10 April 2013
        // reset the domination count for the next iteration
        for( Path p : this.eliteArchive )
        {
            p.resetDominationCount( );
        }
        
        // 19 September 2012 - non-dom approach doesn't work
//        Path p = ParetoOperators.selectPathWithLeastWeightedDominationCount(
//            this.colony, weights );
//        this.eliteArchive.add( p );
//        assert this.eliteArchive.empty( ) == false;
        
        // 20 Sept 2012 - this doesn't work either
//        Path p = null;
//        List< Path > nonDoms = ParetoOperators.getNomDoms( colony );
//
//        if( nonDoms.isEmpty( ) )    // more likely? 
//        {        
//            p = ParetoOperators.selectPathWithLeastWeightedDominationCount( 
//            colony, weights );
//        }
//        else    // less likely??
//        {
//            final int numberOfNonDoms = nonDoms.size( );
//            assert numberOfNonDoms > 0;
//            final int randomSelection = Utility.getRandomInRange( 1, numberOfNonDoms );
//            assert randomSelection <= numberOfNonDoms;
//            p = nonDoms.get( randomSelection - 1 );
//        }
//        
//        this.eliteArchive.add( p );
//        assert this.eliteArchive.empty( ) == false;
        
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
     * perform interaction
     * @param the three weights
     * @param list of classes user has elected to "freeze"
     * @param iteration
     * @param interaction counter 
     * @param information about this iteration
     * @param archive 
     * @return user action from Dialog
     */
    private int performInteraction( 
        Weights weights, 
        List< CLSClass > freezeList,
        int iteration,
        int interactionCounter,
        IterationInformation information,
        List< EleganceDesign > archive )
        // 6 Nov 2015 ServletController servletController)
    {
        assert weights != null;
        assert freezeList != null;
        assert information != null;
        assert archive != null;
        
        // select a solution path for visualisation
        Path path = null;
//        List< Path > nonDoms = ParetoOperators.getNomDoms( this.colony );
//
//        if( nonDoms.isEmpty( ) == false )    // not so likely? 
//        {        
//            final int numberOfNonDoms = nonDoms.size( );
//            assert numberOfNonDoms > 0;
//            final int randomSelection = Utility.getRandomInRange( 1, numberOfNonDoms );
//            assert randomSelection <= numberOfNonDoms;
//            path = nonDoms.get( randomSelection - 1 );
//        }
//        else    // more likely??
//        {
//            path = ParetoOperators.selectPathWithLeastWeightedDominationCount( 
//                colony, weights );
//            
//            final int randomSelection = Utility.getRandomInRange( 1, 4 );
//            
//            switch( randomSelection )
//            {
//                case 1: { path = this.colony.get( this.bestCBOIndex ); break; } 
//                
//                case 2: { path = this.colony.get( this.bestNACIndex ); break; }
//                
//                case 3: { path = this.colony.get( this.bestATMRIndex ); break; }
//                
//                case 4: { path = 
//                            ParetoOperators.selectPathWithLeastWeightedDominationCount( 
//                                colony, weights ); 
//                          break; }
//                
//                default: { assert false : "impossible random selection!"; }
//            }
//        }
        
//        final int numberOfFitnessFunctionsToView = 2;
//        final int selection = ffCounter % numberOfFitnessFunctionsToView;
//        ffCounter++;
//        
//        switch( selection )
//        {
//            case 0: { path = this.colony.get( this.bestCBOIndex ); break; } 
//
//            case 1: { path = this.colony.get( this.bestNACIndex ); break; }
//
//            case 2: { path = this.colony.get( this.bestATMRIndex ); break; }
//
//            default: { assert false : "impossible selection!"; }
//        }
//        
        boolean done = false;
        int domCount = 0;

        while( ! done )
        {
            List< Path > list = ParetoOperators.getPathsWithDomCount( colony, domCount );
            if( list.isEmpty( ) )
            {
                domCount++;
            }
            else
            {
                int numberOfPaths = list.size( );
                assert numberOfPaths > 0;
                final int randomSelection = Utility.getRandomInRange( 1, numberOfPaths );
                assert randomSelection <= numberOfPaths;
                path = list.get( randomSelection - 1 );
                done = true;
            }
        }
        assert path != null;
        
        /* 6 Nov 2015
        servletController.setPath(path);
        servletController.setDesignName("Fred");
        servletController.setUseTable(this.problemController.getUseTable());
        servletController.setFreezeList(freezeList);
        servletController.setIteration(iteration);
        servletController.setInteractionCounter(interactionCounter);
        servletController.setInformation(information);
        servletController.setArchive(archive);
        */
       
        
//        VisualiseEvaluateDialog ved = new VisualiseEvaluateDialog(
//                path,
//                "Fred",
//                this.problemController.getUseTable( ),
//                freezeList, 
//                iteration, 
//                interactionCounter,
//                information,
//                archive );
        
//        ved.setVisible( true );

//        int userAction = ved.getUserAction( );
        
//        int designerEvaluation = ved.getDesignerEvaluation( );
//        information.designerEvaluation = designerEvaluation;
        
        int userAction = 1;
        
        if( userAction == JOptionPane.OK_OPTION )   // user has clicked "next"
        {
            Coefficients coefficients = new Coefficients( );

            doRegression( path, coefficients, information );
            
            calculateWeights( coefficients, weights );
        }
        
        // 9 April 2013 - for SSBSE paper
        // reset the CBO weight to 1.0
        weights.weightCBO = 1.0;
        weights.weightATMR = 0.0;
        weights.weightNAC = 0.0;
        
        return userAction;
    }
    
    
    /**
     * do the multiple linear regression to update the coefficients
     * using the OpenForecast OpenSource code wrapped in RegressionAgent
     * @param path
     * @param designer evaluation
     * @param coefficients
     * @param iteration information
     */
    private void doRegression( 
        Path path, 
        Coefficients coefficients,
        IterationInformation information )
    {
        assert path != null;
        assert information != null;
        assert information.designerEvaluation >= 0;
        assert information.designerEvaluation <= 100;
        assert coefficients != null;
        
        // construct an observation with the dependent variable
        Observation observation = new Observation( information.designerEvaluation ); 

        // now set up the independent variables i.e. CBO, NAC and ATMR
        observation.setIndependentValue( 
            RegressionAgent.independentVariables[ 0 ], path.getCBO( ) );
        observation.setIndependentValue( 
            RegressionAgent.independentVariables[ 1 ], path.getEleganceNAC( ) );
        observation.setIndependentValue( 
            RegressionAgent.independentVariables[ 2 ], path.getEleganceATMR( ) );

        RegressionAgent.addObservation( observation );
        RegressionAgent.init( );

        System.out.println( "intercept is: " + RegressionAgent.getIntercept( ) );
        coefficients.cCBO = RegressionAgent.getCBOCoefficient( );
        System.out.println( "cCBO is: " + coefficients.cCBO );
        coefficients.cNAC = RegressionAgent.getNACCoefficient( );
        System.out.println( "cNAC is: " + coefficients.cNAC );
        coefficients.cATMR = RegressionAgent.getATMRCoefficient( );
        System.out.println( "cATMR is: " + coefficients.cATMR );
        
        System.out.println( "Mean absolute percentage error is: " + RegressionAgent.getMAPE( ) );
        information.mape = RegressionAgent.getMAPE( );
        
        System.out.println( "Mean absolute deviation is: " + RegressionAgent.getMAD( ) );
        information.mad = RegressionAgent.getMAD( );
    }
         
    /**
     * calculate the MO weights based on the coefficients
     * @param coefficients
     * @param weights 
     */
    private void calculateWeights( Coefficients coefficients, Weights weights )
    {
        assert coefficients != null;
        assert weights != null;
        
        if( coefficients.cCBO == 0.0  ||
            coefficients.cNAC == 0.0  ||
            coefficients.cATMR == 0.0  )
        {
            // this shouldn't happen(!) so reset weights to initial values
            System.err.println( "weights un-initialised" );
            weights.weightCBO = AlgorithmParameters.INITIAL_wCBO;
            weights.weightNAC = AlgorithmParameters.INITIAL_wNAC;
            weights.weightATMR = AlgorithmParameters.INITIAL_wATMR;
        }
        else if( Double.isNaN( coefficients.cCBO ) ||
                 Double.isNaN( coefficients.cNAC ) ||
                 Double.isNaN( coefficients.cATMR ) )
        {
            // this could happen for example when the
            // data set contains only one observation
            // so do nothing...
        }
        else
        {
            double total = Math.abs( coefficients.cCBO ) +
                           Math.abs( coefficients.cNAC ) + 
                           Math.abs( coefficients.cATMR );
            
            weights.weightCBO = Math.abs( coefficients.cCBO ) / total ;
            weights.weightNAC = Math.abs( coefficients.cNAC ) / total;
            weights.weightATMR = Math.abs( coefficients.cATMR ) / total;
            
            weights.checkSum( );
        } 
        
         System.out.println( "weight for CBO is: " + df.format( weights.weightCBO ) );
         System.out.println( "weight for NAC is: " + df.format( weights.weightNAC ) );
         System.out.println( "weight for ATMR is: " + df.format( weights.weightATMR ) );
    }
    
    /**
     * 31 August 2012
     * in batch mode, write the final results to file
     */
    public void writeBatchResultsToFile( )
    {
        batchResults.calculateFinalResults( );
        
//        if( AlgorithmParameters.heuristics == false )
//        {
//            batchResults.writeFinalResults(
//                Parameters.outputFilePath, 
//                averageRunTimes, 
//                AlgorithmParameters.constraintHandling );
//        }
//        else    // must be heuristic ant search
//        {
//            batchResults.writeFinalHeuristicResults( Parameters.outputFilePath );
//        }
        
        // 23 July 2013
        batchResults.writeFinalHeuristicResults( Parameters.outputFilePath );
    }
      
    /*
     *  17 Nov 2015 
     */
    public void writeResultstoFile( )
    {   
        batchResults.calculateFinalResults( );
        batchResults.writeResults( );
    }
    
}   // end class

//------- end file ----------------------------------------


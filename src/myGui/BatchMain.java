/**
 * BatchMain.java
 * @author Chris Simons
 */

package myGui;

import config.AlgorithmParameters;
import config.Parameters;
import engine.Controller;
import problem.ProblemController;


public class BatchMain 
{
    /**
     * BatchMain developed for anti-pheromone experiments
     * November 2015
    */
    public static void main( String[] args ) 
    {
        assert args != null;
        if( args.length != 0 )
        {
            assert false : "no command line arguments required";
        }    
        
        assert Parameters.mode == Parameters.Mode.Batch;
        
        // Nov 2015 set up output file path for appropriate platform
        if( Parameters.platform == Parameters.Platform.Mac )
        {
            Parameters.outputFilePath = "/Users/Chris/Documents/data";
            System.out.println( "selected path for output files is: " + Parameters.outputFilePath );
        }
        else // must be Windows
        {
            // Parameters.outputFilePath = "W:\\Research\\Writings\\2015 Paper 42 - Antipheromone GECCO 2016\\data";
            
            Parameters.outputFilePath = "C:\\Users\\cl-simons\\Dropbox\\2015 10 Antipheromone\\data";
            System.out.println( "selected path for output files is: " + Parameters.outputFilePath );
        }
        
        final boolean reloadMatrix = false;
        AlgorithmParameters.NUMBER_OF_ANTS = 100;
        AlgorithmParameters.NUMBER_OF_ITERATIONS = 
            AlgorithmParameters.NUMBER_OF_EVALUATIONS / AlgorithmParameters.NUMBER_OF_ANTS;
        
        AlgorithmParameters.algorithm = AlgorithmParameters.MMAS;
        AlgorithmParameters.constraintHandling = false;
        
        AlgorithmParameters.fitness = AlgorithmParameters.NAC;
            
        for( int problem = Parameters.CBS; problem <= Parameters.RANDOMISED; problem++ )
        {
            
            // firstly, set MMAS to usual i.e. reduce to minimum of MAXMIN
            AlgorithmParameters.MMAS_SUBTRACTIVE_ANTIPHEROMONE = false;
                
            for( int x = 0; x < 10; x++ )
            {
                AlgorithmParameters.ANTIPHEROMONE_PHASE_THRESHOLD_PERCENTAGE = x;
                System.out.println( "******* problem is: " + problem + 
                    ", fitness is: " + AlgorithmParameters.fitness + 
                    ", MMAS subtractive AP is: " + AlgorithmParameters.MMAS_SUBTRACTIVE_ANTIPHEROMONE +
                    ", AP Threshold is: " + x + " ******"  );
                doAntSearch( problem, reloadMatrix );

            }   // end for threshold values 0..9

            for( int y = 10; y <= 100; y += 10 )
            {
                AlgorithmParameters.ANTIPHEROMONE_PHASE_THRESHOLD_PERCENTAGE = y;
                System.out.println( "******* problem is: " + problem + 
                    ", fitness is: " + AlgorithmParameters.fitness +
                    ", MMAS subtractive AP is: " + AlgorithmParameters.MMAS_SUBTRACTIVE_ANTIPHEROMONE + 
                    ", AP Threshold is: " + y + " ******"  );
                doAntSearch( problem, reloadMatrix );

            }   // end for threshold values 10..100
            
            
         
        }   // end for each fitness measure 
        
    }
    
    /**
     * do the ant search
     * only re-load the use matrix if 
     * @param problemNumber
     * @param useMatrixReloadSelected 
     */
    
    public static void doAntSearch( int problemNumber, boolean useMatrixReloadSelected )
    {
        assert problemNumber >= 0;
        assert problemNumber <= Parameters.NUMBER_OF_PROBLEMS;
        
        Parameters.problemNumber = problemNumber;
        
        System.out.println( "Number of Ants is : " + AlgorithmParameters.NUMBER_OF_ANTS ); 
        System.out.println( "Number of Iterations is : " + AlgorithmParameters.NUMBER_OF_ITERATIONS );
        
        ProblemController problemController = new ProblemController( );
        
        if( problemNumber == Parameters.CBS ) // Cinema Booking System
        {
            problemController.createDesignProblem5( );
            problemController.setNumberOfClasses( 5 );
            problemController.generateUseMatrix( );
           // problemController.writeUseMatrixToFile( path, "CBS" );
        }
        else if( problemNumber == Parameters.GDP ) // GDP
        {
            problemController.createDesignProblem7( );
            problemController.setNumberOfClasses( 5 );
            problemController.generateUseMatrix( );
            //problemController.writeUseMatrixToFile( path, "GDP" );
        }
        else if( problemNumber == Parameters.SC ) // Select Cruises
        {
            problemController.createDesignProblem6( );
            problemController.setNumberOfClasses( 16 );
            // 28 May 2012 test of constraint handling
//            problemController.setNumberOfClasses( 5 );
            
            problemController.generateUseMatrix( );
            //problemController.writeUseMatrixToFile( path, "SC" );
        }
        else if( problemNumber == Parameters.RANDOMISED ) // Randomised
        {
            problemController.createDesignProblem8( );
            problemController.setNumberOfClasses( 8 );
            
//            if( useMatrixReloadSelected == false  )
//            {
//                problemController.generateUseMatrix( );
//              //  problemController.writeUseMatrixToFile( path, "Randomised" );
////                problemController.showActionsAndData( );
//                problemController.showUseMatrix( );
//            }
//            else // reload previous matrix
//            {
//                System.out.println( "reload selected" );
//                problemController.reloadUseMatrix( ".");
//                problemController.reloadUseTable( );
//                problemController.showActionsAndData( );
//            }
            
            // 21 January 2016
            problemController.initialiseWithPreGenerated( );
            // problemController.showUseMatrix( );
        }        
        else
        {
            assert false : "impossible design problem!!";
        }

        // 17 January 2013
        Controller controller = new Controller( problemController );
        
        for( int i = 0; i < Parameters.NUMBER_OF_RUNS; i++ )
        {
           controller.run( i );       
        }
        
        controller.writeResultstoFile( );
        System.out.println( "batch ACO complete" );
    }

    /**
     * 30 March 2012
     * do Batch Ant Search
     */
    private static void doBatchAntSearch( ) 
    {
        // 30 March 2012
//        double[ ] alpha = { 0.0, 0.5, 1.0, 1.5, 2.0 };
//        double[ ] mu = { 0.0, 0.5, 1.0, 1.5, 2.0 };
//        double[ ] rho = {  0.0, 0.05, 0.1, 0.25, 1.0 };
//        int[ ] ants = { 25, 100 /* 25, 100, 200 */ };
//        final int NUMBER_OF_RUNS = 50;
//        final int NUMBER_OF_ITERATIONS = 1000;
//        int[ ] problems = { 0, 1, 2 };
        
        // 18 April 2012 - CBS only
//        double[ ] alpha = { 0.0, 0.5, 1.0, 1.5, 2.0, 2.5 };
//        double[ ] mu = { 0.0, 0.5, 1.0, 1.5, 2.0, 2.5 };
//        double[ ] rho = {  0.0, 0.001, 0.01, 0.05, 0.1, 0.25, 1.0 };
//        int[ ] ants = { 25, 100 };
//        final int NUMBER_OF_RUNS = 50;
//        final int NUMBER_OF_ITERATIONS = 1000;
//        final int NUMBER_OF_EVALUATIONS = 100000;
//        int[ ] problems = { /* 0, */ 1 /* 2 */ };
        
        
//        double[ ] alpha = { 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0 };
//        double[ ] mu = { 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5 };
//        double[ ] rho = {  0.0, 0.01, 0.1, 0.25, 0.5, 1.0 };
//        int[ ] ants = { 25, 100, 250 };
//        final int NUMBER_OF_RUNS = 50;
//        final int NUMBER_OF_ITERATIONS = 100;
//        final int NUMBER_OF_EVALUATIONS = 100000;
        
        
        double[ ] alpha = { 1.0 };
        double[ ] mu = { 3.0 };
        double[ ] rho = {  0.01 };
        int[ ] ants = { 25 };
        final int NUMBER_OF_RUNS = 50;
//        final int NUMBER_OF_ITERATIONS = 100;
        final int NUMBER_OF_EVALUATIONS = 250;
      
        int[ ] problems = { 0, 1 /* 2 */ };   
        
        
        int counter = 0;
        
        for( int i = 0; i < alpha.length; i++ )
        {
            for( int j = 0; j < mu.length; j++ )
            {
                for( int k = 0; k < rho.length; k++ )
                {
                    for( int l = 0; l < ants.length; l++ )
                    {
                        for( int m = 0; m < problems.length; m++ )
                        {
//                            Parameters.reset( );
//                            
//                            Parameters.ALPHA = alpha[ k ];
//                            Parameters.MU = mu[ k ];
//                            Parameters.RHO = rho[ k ];
//                            Parameters.numberOfAnts = ants[ l ];
//                            
//                            Parameters.numberOfIterations = NUMBER_OF_EVALUATIONS / ants[ l ];
////                            Parameters.numberOfIterations = NUMBER_OF_ITERATIONS;
//                            Parameters.numberOfTrials = NUMBER_OF_RUNS;
//                            
//                            Parameters.problemNumber = problems[ m ];
//                            
//                            Parameters.runIdentifier = problems[ m ] + " " + ants[ l ] + " " + alpha[ k ] + " " + mu[ k ] + " " + rho[ k ]  ;
//        
//                            
//                            System.out.println( Parameters.runIdentifier );
                            counter++; 
                            
                            doAntSearch( Parameters.problemNumber, false );  
                        }
                    }
                }
            }
        }
        
        System.out.println( "number of run combinations is: " + counter );
    }
    
    /**
     * do heuristic ant search in batch 
     */
    private static void doBatchHeuristicAntSearch( )
    {
        int[ ] problems = { 0 };
        double[ ] betaCBOs = { 1.0, 1.5, 2.0, 2.5, 3.0 };
//        double[ ] betaNACs = { 0.0 };
        double[ ] weightsCBO = { 1.0  };
        
        Parameters.outputFilePath = "W:\\Research\\Experimentation2013Q2\\temp";
//        Parameters.outputFilePath = "F:\\temp";
        
        System.out.println( "selected path for output files is: " + Parameters.outputFilePath );
        
        for( int i = 0; i < problems.length; i++ )
        {
            Parameters.problemNumber = problems[ i ];
            System.out.println( "Problem number is: " + i );
            
            for( int j = 0; j < betaCBOs.length; j++ )
            {
                AlgorithmParameters.BETA_CBO = betaCBOs[ j ];
                        
//                for( int k = 0; k <betaNACs.length; k++ )
//                {
//                    AlgorithmParameters.BETA_NAC = betaNACs[ k ];
//                    
                    for( int l = 0; l < weightsCBO.length; l++ )
                    {
                        AlgorithmParameters.weightCBO = weightsCBO[ 0 ];
                        AlgorithmParameters.weightNAC = 1.0 - AlgorithmParameters.weightCBO;
                        doAntSearch( problems[ i ], false );
                    }    
                        
//                }
            }
        }
    }
    
    
    private static void experiment_2013_06_26( String[ ] args )
    {
        int problemNumber = Integer.parseInt( args[ 0 ] );
        assert problemNumber >= 0;
        Parameters.problemNumber = problemNumber;

        double[ ] betaCBOs = { 1.0, 1.25, 1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0 };

        Parameters.outputFilePath = "W:\\Research\\Experimentation2013Q3\\2013_06_26";
//            Parameters.outputFilePath = ".";
//            Parameters.outputFilePath = "F:\\temp";

        System.out.println( "selected path for output files is: " + Parameters.outputFilePath );

        for( int j = 0; j < betaCBOs.length; j++ )
        {
            AlgorithmParameters.BETA_CBO = betaCBOs[ j ];
            doAntSearch( problemNumber, false );
        }
    }

    private static void experiment_2013_07_02( )
    {
        double[ ] betaCBOs = { 1.0, 1.25, 1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0, 3.25 };

        Parameters.outputFilePath = "W:\\Research\\Experimentation2013Q3\\2013_07_02";

        System.out.println( "selected path for output files is: " + Parameters.outputFilePath );

        // problem: 0 is CBS, 1 is GDP, 2 is SC
        for( int problem = 0; problem <= 2; problem++ )
        {
            Parameters.problemNumber = problem;
            
            for( int j = 0; j < betaCBOs.length; j++ )
            {
                AlgorithmParameters.BETA_CBO = betaCBOs[ j ];
                System.out.println( "\t" + "doing ACO for problem " + problem +
                                    " with a betaCBO value of " + betaCBOs[ j ] );
                doAntSearch( problem, false );
            }
        }
    }
    
    private static void experiment_2013_07_07( )
    {
        double[ ] betaCBOs = { 1.0 };
        
            // 1.25, 1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0, 3.25 };

        Parameters.outputFilePath = "D:\\output";

        System.out.println( "selected path for output files is: " + Parameters.outputFilePath );

        // problem: 0 is CBS, 1 is GDP, 2 is SC
        for( int problem = 0; problem <= 2; problem++ )
        {
            Parameters.problemNumber = problem;
            
            for( int j = 0; j < betaCBOs.length; j++ )
            {
                AlgorithmParameters.BETA_CBO = betaCBOs[ j ];
                System.out.println( "\t" + "doing ACO for problem " + problem +
                                    " with a betaCBO value of " + betaCBOs[ j ] );
                doAntSearch( problem, false );
            }
        }
    }
    
    private static void experiment_2013_07_09( )
    {
     
        Parameters.outputFilePath = "D:\\output";

        System.out.println( "selected path for output files is: " + Parameters.outputFilePath );

        // problem: 0 is CBS, 1 is GDP, 2 is SC
        for( int problem = 0; problem <= 1; problem++ )
        {
            Parameters.problemNumber = problem;
            
            System.out.println( "\t" + "doing ACO for problem " + problem );
            doAntSearch( problem, false );
        }
    }

    private static void experiment_2013_07_22( )
    {
     
        Parameters.outputFilePath = "W:\\Research\\Experimentation2013Q3\\2013_07_22";

        System.out.println( "selected path for output files is: " + Parameters.outputFilePath );

        // first without the NAC heuristics
        AlgorithmParameters.heuristics = false;
        
        // problem: 0 is CBS, 1 is GDP, 2 is SC
        for( int problem = 0; problem <= 0; problem++ )
        {
            Parameters.problemNumber = problem;
            
            System.out.println( "\t" + "doing ACO for problem " + problem );
            doAntSearch( problem, false );
        }
        
        // secondly with the NAC heuristics
         double[ ] betaNACs = { 1.0 }; //, 1.5, 2.0, 2.5, 3.0 };
         AlgorithmParameters.heuristics = true;
         
         // problem: 0 is CBS, 1 is GDP, 2 is SC
        for( int problem = 0; problem <= 0; problem++ )
        {
            Parameters.problemNumber = problem;
            
            for( int j = 0; j < betaNACs.length; j++ )
            {
                AlgorithmParameters.BETA_NAC = betaNACs[ j ];
                System.out.println( "\t" + "doing ACO for problem " + problem +
                                    " with a betaNAC value of " + betaNACs[ j ] );
                doAntSearch( problem, false );
            }
        }
    }

    private static void experiment_2013_07_23( )
    {
        Parameters.outputFilePath = "W:\\Research\\Experimentation2013Q3\\2013_07_23";

        System.out.println( "selected path for output files is: " + Parameters.outputFilePath );

        // first without the NAC heuristics
        AlgorithmParameters.heuristics = false;
        
        // problem: 0 is CBS, 1 is GDP, 2 is SC
        for( int problem = 0; problem <= 0; problem++ )
        {
            Parameters.problemNumber = problem;
            
            System.out.println( "\t" + "doing ACO for problem " + problem );
            doAntSearch( problem, false );
        }
        
        // secondly with the NAC heuristics
        AlgorithmParameters.heuristics = true;
        double[ ] betaNACs = { 1.0, 1.25, 1.5, 1.75, 2.0 };    
        
         // problem: 0 is CBS, 1 is GDP, 2 is SC
        for( int problem = 0; problem <= 0; problem++ )
        {
            Parameters.problemNumber = problem;
            
            for( int i = 0; i < betaNACs.length; i++ )
            {
                AlgorithmParameters.BETA_NAC = betaNACs[ i ];
                System.out.println( "\t" + "doing ACO for problem " + problem +
                                    " with a betaNAC value of " + betaNACs[ i ] );
                doAntSearch( problem, false );
            }
        }
    }
    
    private static void experiment_2013_07_24( )
    {
        Parameters.outputFilePath = "W:\\Research\\Experimentation2013Q3\\2013_07_24";

        System.out.println( "selected path for output files is: " + Parameters.outputFilePath );

        
        
        // problem: 0 is CBS, 1 is GDP, 2 is SC
        for( int problem = 0; problem <= 1; problem++ )
        {
            Parameters.problemNumber = problem;
            
            // first without the NAC heuristics
            AlgorithmParameters.heuristics = false;
            System.out.println( "\t" + "doing ACO for problem " + problem );
            doAntSearch( problem, false );
            
            // secondly with the NAC heuristics
            AlgorithmParameters.heuristics = true;
            double[ ] betaNACs = { 1.0, 2.0, 3.0, 4.0, 5.0 };    
        
            for( int i = 0; i < betaNACs.length; i++ )
            {
                AlgorithmParameters.BETA_NAC = betaNACs[ i ];
                System.out.println( "\t" + "doing ACO for problem " + problem +
                                    " with a betaNAC value of " + betaNACs[ i ] );
                doAntSearch( problem, false );
            }
        }
        
        
    }
    
    
    private static void experiment_2013_07_25( )
    {
        Parameters.outputFilePath = "W:\\Research\\Experimentation2013Q3\\2013_07_25";

        System.out.println( "selected path for output files is: " + Parameters.outputFilePath );

        
        
        // problem: 0 is CBS, 1 is GDP, 2 is SC
        for( int problem = 0; problem <= 2; problem++ )
        {
            Parameters.problemNumber = problem;
            
            // first without the NAC heuristics
            AlgorithmParameters.heuristics = false;
            System.out.println( "\t" + "doing ACO for problem " + problem );
            doAntSearch( problem, false );
            
            // secondly with the NAC heuristics
            AlgorithmParameters.heuristics = true;
            double[ ] betaNACs = { 1.0, 2.0, 3.0, 4.0, 5.0 };    
        
            for( int i = 0; i < betaNACs.length; i++ )
            {
                AlgorithmParameters.BETA_NAC = betaNACs[ i ];
                System.out.println( "\t" + "doing ACO for problem " + problem +
                                    " with a betaNAC value of " + betaNACs[ i ] );
                doAntSearch( problem, false );
            }
        }
    }
     
}   // end of class

// ------------- end of file ---------------------------------
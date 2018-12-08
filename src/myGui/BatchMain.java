/**
 * BatchMain.java
 * 6 June 2017
 * @author Chris Simons
 */

package myGui;

import config.AlgorithmParameters;
import config.Parameters;
import engine.Controller;
import problem.ProblemController;

public class BatchMain 
{
    private static ProblemController problemController;
    
    // starting point for anti-pheromone experiments
    public static void main( String[] args ) 
    {
        assert args != null;
        if( args.length != 0 )
        {
            assert false : "no command line arguments required";
        }    
        
        // set up output file path for appropriate platform here
        Parameters.platform = Parameters.platform.Windows;
        // Parameters.platform = Parameters.platform.Mac;
        
        if( Parameters.platform == Parameters.Platform.Mac )
        {
            Parameters.outputFilePath = "/Users/Chris/data";
        }
        else // must be Windows
        {
            Parameters.outputFilePath = "C:\\Users\\cl-simons\\ACO_results";
        }
        
        // set up parameters for each algorithm
        if( AlgorithmParameters.algorithm == AlgorithmParameters.SIMPLE_ACO )
        {
            AlgorithmParameters.alpha = AlgorithmParameters.ALPHA_SD;
            AlgorithmParameters.mu = AlgorithmParameters.MU_SD;
            AlgorithmParameters.rho = AlgorithmParameters.SimpleACO_RHO;
        }
        else if( AlgorithmParameters.algorithm == AlgorithmParameters.MMAS )
        {
            if( AlgorithmParameters.fitness == AlgorithmParameters.CBO || 
                AlgorithmParameters.fitness == AlgorithmParameters.NAC ||
                AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
            {
                AlgorithmParameters.alpha = AlgorithmParameters.ALPHA_SD;
                AlgorithmParameters.mu = AlgorithmParameters.MU_SD;
                AlgorithmParameters.rho = AlgorithmParameters.MMAS_RHO_SD;
                AlgorithmParameters.MMAS_Mmax = AlgorithmParameters.MMAS_PHEROMONE_MAXIMUM_SD;
                AlgorithmParameters.MMAS_Mmin = AlgorithmParameters.MMAS_PHEROMONE_MINIMUM_SD;
            }
            else if( AlgorithmParameters.fitness == AlgorithmParameters.TSP_PATH_LENGTH )
            {
                AlgorithmParameters.alpha = AlgorithmParameters.ALPHA_TSP;
                AlgorithmParameters.mu = AlgorithmParameters.MU_TSP;
                AlgorithmParameters.rho = AlgorithmParameters.MMAS_RHO_TSP;
                AlgorithmParameters.MMAS_Mmax = AlgorithmParameters.MMAS_PHEROMONE_MAXIMUM_TSP;
                AlgorithmParameters.MMAS_Mmin = AlgorithmParameters.MMAS_PHEROMONE_MINIMUM_TSP;
            }
            else
            {
                assert false : "impossible MMAS fitness!";
            }
        }   
        else
        {
            assert false : "impossible algorithm to set RHO!";
        }
        
        // 22 November 2018
        Parameters.problemNumber = Parameters.TSP_RAT195; 
        generateProblem( Parameters.problemNumber );
        checkSetUpForTSP( );
        
        // 28 November 2018 
        assert AlgorithmParameters.pheromoneStrength == AlgorithmParameters.MMAS_PHEROMONE_SINGLE;
        assert AlgorithmParameters.MMAS_ANTIPHEROMONE == true;
        assert AlgorithmParameters.preventInterference == true;
        showParameters( );
        
        
        // for pheromone runs
//        for( int P_Strength = AlgorithmParameters.MMAS_PHEROMONE_SINGLE; 
//             P_Strength <= AlgorithmParameters.MMAS_PHEROMONE_TRIPLE;
//             P_Strength++ )
//        {
//            AlgorithmParameters.pheromoneStrength = P_Strength;
//            System.out.println( "problem is: " + getProblemNumberAsString( Parameters.problemNumber ) );
//            System.out.println( "pheromone strength is: " + AlgorithmParameters.pheromoneStrength + "\n" );
//            
//            doAntSearch( );
//        }
        
        // for antipheromone runs
        for( int AP_Strength = AlgorithmParameters.ANTIPHEROMONE_STRENGTH_DOUBLE; 
             AP_Strength <= AlgorithmParameters.ANTIPHEROMONE_STRENGTH_TRIPLE;
             AP_Strength++ )
        {
            AlgorithmParameters.antipheromoneStrength = AP_Strength;
//            System.out.println( "problem is: " + getProblemNumberAsString( Parameters.problemNumber ) );
//            System.out.println( "antipheromone  strength is: " + AlgorithmParameters.antipheromoneStrength + "\n" );
            
            for( int ap = 0; ap <= 10; ap++ )
            {
                AlgorithmParameters.antiPheromonePhasePercentage = ap;
                
                System.out.println( 
                    "******* problem is: " + getProblemNumberAsString( Parameters.problemNumber ) + 
                    ", antipheromone strength is: " + AlgorithmParameters.antipheromoneStrength +        
                    ", AP percent phase is: " + AlgorithmParameters.antiPheromonePhasePercentage + " ******"  );

                doAntSearch( );
            }
        }   
        
    }   // end main
    
    /**
     * do the ant search
     * Design problem have been set up
     * 14 June 2017
     */
    public static void doAntSearch( )
    {
        assert problemController != null;
        
        Controller controller = new Controller( problemController );
        
        for( int i = 0; i < Parameters.NUMBER_OF_RUNS; i++ )
        {
           controller.run( i );    
        }
        
        controller.writeResultstoFile( );
        System.out.println( "ACO complete!" );
    }

    /**
     * Generate the correct design problem for the current batch run,
     * and generate it only once. 
     * 14 June 2017
     * @param problemNumber as int
     */
    private static void generateProblem( final int problemNumber )
    {
        assert problemNumber >= 0;
        assert problemNumber < Parameters.NUMBER_OF_PROBLEMS;
        
        problemController = new ProblemController( );
        assert problemController != null;
        
        // set up the Cinema Booking System (CBS) design problem
        if( problemNumber == Parameters.CBS ) 
        {
            problemController.createDesignProblem5( );
            problemController.setNumberOfClasses( 5 );
            problemController.generateUseMatrix( );
        }
        // set up the GDP design problem
        else if( problemNumber == Parameters.GDP ) 
        {
            problemController.createDesignProblem7( );
            problemController.setNumberOfClasses( 5 );
            problemController.generateUseMatrix( );
        }
        // set up the Randomised design problem
        else if( problemNumber == Parameters.RANDOMISED  ) 
        {
            problemController.createDesignProblem8( );
            problemController.setNumberOfClasses( 8 );
                   
            // 21 January 2016
            problemController.initialiseWithPreGenerated( );
            // problemController.showUseMatrix( );
        } 
        // set up the Select Cruises (SC) design problem
        else if( problemNumber == Parameters.SC ) 
        {
            problemController.createDesignProblem6( );
            problemController.setNumberOfClasses( 16 );
            // 28 May 2012 test of constraint handling
//            problemController.setNumberOfClasses( 5 );
            
            problemController.generateUseMatrix( );
        }
        else if( problemNumber == Parameters.TSP_BERLIN52 )
        {
            problemController.createTSPBerlin52Problem( );
        }
        else if( problemNumber == Parameters.TSP_ST70 )
        {
            problemController.createTSPST70Problem( );
        }
        else if( problemNumber == Parameters.TSP_RAT99 )
        {
            problemController.createTSPRAT99Problem( );
        }
        else if( problemNumber == Parameters.TSP_RAT195 )
        {
            problemController.createTSPRAT195Problem( );
        }
        else
        {
            assert false : "impossible design problem!!";
        }
    }
    
    /**
     * for convenience, express problem number as string
     * 14 June 2018
     * @param problem number 
     */
    private static String getProblemNumberAsString( final int problem )
    {
        String result = "";
        switch( problem )
        {
            case Parameters.CBS:
                result = "CBS";
                break;
            case Parameters.GDP:
                result = "GDP";
                break;
            case Parameters.RANDOMISED:
                result = "Randomised";
                break;
            case Parameters.SC:
                result = "SC";
                break;
            case Parameters.TSP_BERLIN52:
                result = "TSP Berlin 52 Cities";
                break;
            case Parameters.TSP_ST70:
                result = "TSP ST 70 Cities";
                break;
            case Parameters.TSP_RAT99:
                result = "TSP RAT 90 Cities";
                break;
            case Parameters.TSP_RAT195:
                result = "TSP RAT 195 Cities";
                break;
            default:
                assert false: "unknown problem instance!!";
                break;
        }
        return result;
    }
    
    /**
     * show the configured run parameters and algorithms parameters
     * 13 June 2018
     */
    private static void showParameters( )
    {
        System.out.println( "RUN PARAMETERS" );
        System.out.println( "\tproblem instance: " + getProblemNumberAsString( Parameters.problemNumber ) );
        System.out.println( "\tselected path for output files: " + Parameters.outputFilePath );
        System.out.println( "\tnumber of runs: " + Parameters.NUMBER_OF_RUNS ); 
   
        System.out.println( "ALGORITHM PARAMETERS");
        System.out.println( "\tants: " + AlgorithmParameters.NUMBER_OF_ANTS );
        System.out.println( "\tevaluations: " + AlgorithmParameters.NUMBER_OF_EVALUATIONS );
        
        String s1 = "";
        switch( AlgorithmParameters.fitness )
        {
            case 1: s1 = "CBO"; break;
            case 2: s1 = "NAC"; break;
            case 3: s1 = "Combined"; break;
            case 4: s1 = "TSP cost"; break;
            default: s1 = "Unknown!!"; break;
        }
        System.out.println( "\tfitness: " + s1 );
        
        System.out.println( "\tconstraint handling: " + AlgorithmParameters.constraintHandling );
        System.out.println( "\theuristics: " + AlgorithmParameters.heuristics );
           
        System.out.println( "\tALPHA: " + AlgorithmParameters.alpha );
        System.out.println( "\tMU: " + AlgorithmParameters.mu );
        
        if( AlgorithmParameters.algorithm == AlgorithmParameters.SIMPLE_ACO ) 
        {
            System.out.println( "\talgorithm: Simple-ACO" );
            System.out.println( "\tRHO: " + AlgorithmParameters.rho );
            String s2 = AlgorithmParameters.SIMPLE_ACO_SUBTRACTIVE_ANTIPHEROMONE == false ? "OFF" : "ON"; 
            System.out.println( "\tSimple ACO subtractive antipheromone: " + s2 );
            if( AlgorithmParameters.SIMPLE_ACO_SUBTRACTIVE_ANTIPHEROMONE == true )
            {
                System.out.println( "\tPHI: " + AlgorithmParameters.PHI );
            }
        }
        else    // must be MMAS
        {
            assert AlgorithmParameters.algorithm == AlgorithmParameters.MMAS;
            System.out.println( "\talgorithm: MMAS" );
            System.out.println( "\trho: " + AlgorithmParameters.rho );
            System.out.println( "\tpheromone strength: " + AlgorithmParameters.pheromoneStrength );
            String s3 = AlgorithmParameters.MMAS_ANTIPHEROMONE == false ? "OFF" : "ON";       
            System.out.println( "\tMMAS antipheromone: " + s3 );
            if( AlgorithmParameters.MMAS_ANTIPHEROMONE == true )
            {
                if( AlgorithmParameters.MMAS_REDUCE_BY_HALF == true )
                {
                    System.out.println( "\tMMAS antipheromone => reduce by half" );
                }
                else
                {
                    System.out.println( "\tMMAS antipheromone => reduce to Min" );
                }
                System.out.println( "\tAntipheromone strength: " + AlgorithmParameters.antipheromoneStrength );
            }
            
            System.out.println("\tinterference prevention: " + AlgorithmParameters.preventInterference );
        }
    }
    
    private static void checkSetUpForTSP( )
    {
        if( Parameters.problemNumber != Parameters.TSP_BERLIN52 &&
            Parameters.problemNumber != Parameters.TSP_ST70 &&
            Parameters.problemNumber != Parameters.TSP_RAT99 &&
            Parameters.problemNumber != Parameters.TSP_RAT195 )
        {
            assert false : "TSP problem instance not selected";
        }
        
        assert AlgorithmParameters.fitness == AlgorithmParameters.TSP_PATH_LENGTH;   
        
        assert AlgorithmParameters.algorithm == AlgorithmParameters.MMAS;
        assert AlgorithmParameters.alpha == AlgorithmParameters.ALPHA_TSP;
        assert AlgorithmParameters.mu == AlgorithmParameters.MU_TSP;
        assert AlgorithmParameters.rho == AlgorithmParameters.MMAS_RHO_TSP;
        assert AlgorithmParameters.MMAS_Mmax ==  AlgorithmParameters.MMAS_PHEROMONE_MAXIMUM_TSP;
        assert AlgorithmParameters.MMAS_Mmin ==  AlgorithmParameters.MMAS_PHEROMONE_MINIMUM_TSP;
        
        if( AlgorithmParameters.MMAS_ANTIPHEROMONE == true )
        {
            assert AlgorithmParameters.preventInterference == true;
        }
        else
        {
            assert AlgorithmParameters.preventInterference == false;
        }
        
        assert AlgorithmParameters.constraintHandling == false;
    }
  
}   // end of class

// ------------- end of file ---------------------------------
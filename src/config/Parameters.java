/*
 * Parameters.java
 */

package config;

/**
 * @author j4-smith
 * Jim adding this line as a comment to test subversion 8-3-12
 * Chris - changed problem numbers. 1 is GDP, 2 is SC
 * 25 April 2012, Chris, reset method added
 * 24 November 2015, Chris, Batch and interactive parameters separated 
 * 
 */

public class Parameters 
{
    // specify platform for correct results file path
    public enum Platform { Windows, Mac }
    public static Platform platform;
    
    public static String outputFilePath;

    public static int problemNumber;   
    
    public static final int TEST = 0;
    public static final int CBS = 1;
    public static final int GDP = 2;
    public static final int RANDOMISED = 3;
    public static final int SC = 4;
    public static final int TSP_BERLIN52 = 5;
    public static final int TSP_ST70 = 6;
    public static final int TSP_RAT99 = 7;
    public static final int TSP_RAT195 = 8;
    public static final int NUMBER_OF_PROBLEMS = TSP_RAT195 + 1;

    public static int NUMBER_OF_RUNS = 50;
   
    // 8 December 2015 - for full robustness check in the generation of solutions
    // turn off for faster execution when happy with solution generation
    public static final boolean SOLUTION_GENERATION_ROBUSTNESS_CHECK = false; 
    
}   

//----------- end of file --------------------------------------------

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
    // 24 Nov 2015
    // PARAMETERS FOR BOTH BATCH MODE and INTERACTIVE MODE -----------
    public enum Platform { Windows, Mac }
    public static Platform platform = Platform.Windows;
    // public static final Platform platform = Platform.Mac;

    public enum Mode { Batch, Interactive }
    public static final Mode mode = Mode.Batch;
    
    public static String outputFilePath;

    public static int problemNumber;   
    
    public static final int TEST = 0;
    public static final int CBS = 1;
    public static final int GDP = 2;
    public static final int SC = 3;
    public static final int RANDOMISED = 4;
    public static final int NUMBER_OF_PROBLEMS = RANDOMISED + 1;

    public static int NUMBER_OF_RUNS = 100;
   
    // 8 December 2015 - for full robustness check in the generation of solutions
    // turn off for faster execution when happy with solution generation
    public static final boolean SOLUTION_GENERATION_ROBUSTNESS_CHECK = true; 
    
    // 24 Nov 2015
    // INTERACTIVE MODE ONLY PARAMETERS -----------------------------
    
    
   
    // the following parameters are set at the start of each design episode
    
    public static int designer;  
   
    public static int episodeNumber; 
            
    public static boolean freezing;
            
    public static int colourMetaphor;
    
    private static final int MARTIN = 0;
    private static final int STEWART = 1;
    private static final int JANE = 2;
    private static final int LARRY = 3;
    private static final int JIM = 4;
    private static final int JULIA = 5;
    private static final int BARRY = 6;
    private static final int ANDY = 7;
    private static final int DANNY = 8;
    private static final int WILL = 9;
    private static final int DEHLIA = 10;
    private static final int CHRIS = 11;
    public static final int NUMBER_OF_DESIGNERS = CHRIS + 1;
    
    public static final int WATER_TAP = 0;
    public static final int TRAFFIC_LIGHTS = 1;
    
}   // end of class

//----------- end of file --------------------------------------------

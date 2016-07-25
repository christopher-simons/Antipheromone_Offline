/*
 * AlgorithmParameters.java
 */

package config;

/**
 * @author Chris Simons
 */

public class AlgorithmParameters 
{
    public static final int SIMPLE_ACO = 1;
    public static final int MMAS = 2;
    public static int algorithm = SIMPLE_ACO;

    // MMAS MAX and MIN Pheromone levels
    public static final double MMAS_PHEROMONE_MINIMUM = 0.5;
    public static final double MMAS_PHEROMONE_MAXIMUM = 3.5;
    
    public enum PheromoneUpdate { SO, ParetoBased };
//    public static PheromoneUpdate pheromoneUpdate = PheromoneUpdate.ParetoBased;
    public static PheromoneUpdate pheromoneUpdate = PheromoneUpdate.SO;
    
//    public enum SOUpdate { CBO, NAC, COMBINED };
//    public static SOUpdate sOUpdate = SOUpdate.CBO;
//    public static SOUpdate sOUpdate = SOUpdate.NAC;
//    public static SOUpdate sOUpdate = SOUpdate.COMBINED;
    
    // 27 November 2017
    public static final int CBO = 1;
    public static final int NAC = 2;
    public static final int COMBINED = 3;
    public static int fitness = CBO;
    
    
    // used in PheromoneOperators to calculate delta
    public static boolean objectiveCBO = true;
    public static boolean objectiveNAC = true;
    public static boolean objectiveATMR = false;
    
    public static boolean evaporationElitism = true;
    public static boolean replacementElitism = true;
    
    // to ensure only valid paths are generated
    public static boolean constraintHandling = false;
    
    
    private static final double DEFAULT_ALPHA = 1.5; 
    public static double ALPHA = DEFAULT_ALPHA; 
    
    private static final double DEFAULT_MU = 3.0; 
    public static double MU = DEFAULT_MU;   
    
    public static final double SimpleACO_RHO = 0.1;  // for S-ACO
    // for MMAS. Dorogo and Stutzle, page 91, suggest 0.02
    public static final double MMAS_RHO = 0.035; 
    public static double RHO = MMAS_RHO;
    
    private static final int DEFAULT_NUMBER_OF_ANTS = 100;
    public static int NUMBER_OF_ANTS = DEFAULT_NUMBER_OF_ANTS;
    
    public static final int NUMBER_OF_EVALUATIONS = 100000;
    
    //private static final int MAX_ITERATIONS = 1000;
    public static int NUMBER_OF_ITERATIONS = NUMBER_OF_EVALUATIONS / NUMBER_OF_ANTS;
    // public static int NUMBER_OF_ITERATIONS = 1;
    
    
    // INTERACTIVE MODE ONLY parameters -----------------------------
    // 9 April 2013
    // public static final double INTERACTIVE_INTERVAL_CONSTANT = 100;
    public static final double INTERACTIVE_INTERVAL_CONSTANT = 200;
    
    // for start of dynamic interactive multi-objective search
    public static final double INITIAL_wCBO = 1.0;
    public static final double INITIAL_wNAC = 0.0;
    public static final double INITIAL_wATMR = 0.0;
    public static final double INITIAL_wCOMBINED = 0.0;

    // BATCH MODE ONLY parameters -----------------------------------
    // testing in batch mode, 19 September 2012
    // set these values when in pareto based pheromone update mode
    public static double weightCBO = 0.0;
    public static double weightNAC = 0.0;
    public static double weightATMR = 0.0;
    public static double weightCOMBINED = 0.0;
    
   
    
    // 15 January 2013
//    public static final int HEURISTICS_OFF = 0;
//    public static final int HEURISTICS_CBO_ONLY = 1;
//    public static final int HEURISTICS_NAC_ONLY = 2;
//    public static final int HEURISTICS_BOTH = 3;
//    
    // flag to signal exploitation of heuristic information
    public static boolean heuristics = false;
    
    // parameters controlling influence of heuristic information
    public static double BETA_CBO = 1.0;
    public static double BETA_NAC = 1.0;
    
    
    // 4 December 2015
    // parameters to scale (normalise) NAC to a range 0...1.0
    //
    // scale is a reflection of the number of elements to classes
    // which roughly relates to the scale of the NAC
    // Problem Elements Classes E:C     MaxNAC (observed)
    // CBS      31      5       6.1     6.99
    // GDP      55      5       10.1    10.8
    // SC       91      16      5.6     10.17
    // Random   58      8       7.25    8.2
    
    public final static double NACScaleForCBS = 6;
    public final static double NACScaleForGDP = 10;
    public final static double NACScaleForSC = 10;
    public final static double NACScaleForRANDOMISED = 8;
    
    // 8 January 2016
    public static int ANTIPHEROMONE_PHASE_THRESHOLD_PERCENTAGE = 0;
    
    // 15 January 2016
    public static boolean SIMPLE_ACO_MONTGOMERY_SUBTRACTIVE_ANTIPHEROMONE = false;
    public static final int SIMPLE_ACO_MONTGOMERY_SUBTRACTIVE_ANTIPHEROMONE_HALF = 0;
    public static final int SIMPLE_ACO_MONTGOMERY_SUBTRACTIVE_ANTIPHEROMONE_MINIMAL = 1;
    public static int Simple_ACO_Montgomery_Subtractive_Antipheromone_Value = SIMPLE_ACO_MONTGOMERY_SUBTRACTIVE_ANTIPHEROMONE_HALF;
    
    
    public static double SIMPLE_ACO_RHO_ANTIPHEROMONE_HALF = 0.5;
    public static double SIMPLE_ACO_RHO_ANTIPHEROMONE_MINIMAL = 0.1;
    public static double SIMPLE_ACO_RHO_ANTIPHEROMONE_ZERO = 0.0;
    
    // 20 January 2016
    public static boolean MMAS_SUBTRACTIVE_ANTIPHEROMONE = true;
    public static double MMAS_RHO_ANTIPHEROMONE_HALF = 0.5;
    
    
}   // end class

// ------ end of file -----------------------------------------

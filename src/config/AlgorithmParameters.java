/*
 * AlgorithmParameters.java
 */

package config;

/**
 * @author Chris Simons
 */
public class AlgorithmParameters 
{
    public static final int NUMBER_OF_ANTS = 100;
    
    public static final int NUMBER_OF_EVALUATIONS = 100000;
    
    public static int NUMBER_OF_ITERATIONS = NUMBER_OF_EVALUATIONS / NUMBER_OF_ANTS;
    
    public static final int SIMPLE_ACO = 1;
    public static final int MMAS = 2;
    public static int algorithm = MMAS;
    
    public static final int CBO = 1;
    public static final int NAC = 2;
    public static final int COMBINED = 3;
    public static final int TSP_PATH_LENGTH = 4;
    public static int fitness = TSP_PATH_LENGTH;

    // to ensure only valid paths are generated
    public static boolean constraintHandling = false;
    
    // alpha - parameter controlling pheromone attractiveness during solution path generation
    public static final double ALPHA_SD = 1.5; 
    public static final double ALPHA_TSP = 1.5; 
    public static double alpha = 0.0; // alpha value is set up in BatchMain
            
    // mu - parameter controlling MMAS update
    public static final double MU_SD = 3.0;
    public static final double MU_TSP = 1.0; 
    public static double mu = 0.0; // mu value is set up in BatchMain   
    
    // RHO - pheromone decay coeffient
    public static final double SimpleACO_RHO = 0.1;  // for S-ACO
    // for MMAS, Dorogo and Stutzle book, page 91, suggests 0.02(!)
    public static final double MMAS_RHO_SD = 0.035; 
    public static final double MMAS_RHO_TSP = 0.045;
    public static double rho = 0.0; // rho value is set up in BatchMain
    
    // MMAS MAX and MIN Pheromone limit levels
    public static final double MMAS_PHEROMONE_MAXIMUM_SD = 3.5;
    public static final double MMAS_PHEROMONE_MINIMUM_SD = 0.5;
    public static final double MMAS_PHEROMONE_MAXIMUM_TSP = 2.5;
    public static final double MMAS_PHEROMONE_MINIMUM_TSP = 0.0;
    public static double MMAS_Mmax = 0.0; // set up in BatchMain 
    public static double MMAS_Mmin = 0.0; // set up in BatchMain 
    
    // flag to signal exploitation of heuristic information
    public static boolean heuristics = false;
    
    // parameters controlling influence of heuristic information
    public static double BETA_CBO = 1.0;
    public static double BETA_NAC = 1.0;
    
    // used in PheromoneOperators to calculate delta
    // and by the elitist replacement archive
    public static boolean objectiveCBO = true;
    public static boolean objectiveNAC = true;
    public static boolean objectiveATMR = false;
    
    public static boolean evaporationElitism = true;
    public static boolean replacementElitism = false;
    
    
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
    
    // parameter controlling the percentage of iterations at which 
    // antipheromone is applied, for both algorithms
    public static int antiPheromonePhasePercentage = 0;
    
    // Simple-ACO related parameters
    public static boolean SIMPLE_ACO_SUBTRACTIVE_ANTIPHEROMONE = false;
    
    public static final double SIMPLE_ACO_PHI_HALF = 0.5;
    public static final double SIMPLE_ACO_PHI_MINIMAL = 0.1;
    public static final double SIMPLE_ACO_PHI_ZERO = 0.0;
    public static final double PHI = SIMPLE_ACO_PHI_MINIMAL;
    
    // MMAS related pheromone parameters 
    public static final int MMAS_PHEROMONE_SINGLE = 1;
    public static final int MMAS_PHEROMONE_DOUBLE = 2;
    public static final int MMAS_PHEROMONE_TRIPLE = 3;
    public static int pheromoneStrength = MMAS_PHEROMONE_SINGLE;
    
    // MMAS related antipheromone parameters
    public static final boolean MMAS_ANTIPHEROMONE = true;
    public static final boolean MMAS_REDUCE_BY_HALF = false;
   
    public static final int ANTIPHEROMONE_STRENGTH_SINGLE = 1;
    public static final int ANTIPHEROMONE_STRENGTH_DOUBLE = 2;
    public static final int ANTIPHEROMONE_STRENGTH_TRIPLE = 3;
    public static int antipheromoneStrength = ANTIPHEROMONE_STRENGTH_SINGLE;
    
    // MMAS related antipheromone interference prevention parameter
    public static boolean preventInterference = true;
}  

// ------ end of file -----------------------------------------

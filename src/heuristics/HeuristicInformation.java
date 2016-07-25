/*
 * HeuristicInformation.java
 * Created 14 January 2012
 */

package heuristics;

/**
 * The heuristic information used by a Heuristic Ant.
 * Implemented as a Singleton pattern, as there is only
 * one set of heuristic information per design problem. 
 * 
 * @author Christopher Simons
 */

public class HeuristicInformation 
{
    public static int numberOfAttributes;
    public static int numberOfMethods;
    
    public static int idealNumberOfElementsPerClass;
    public static int remainderElements;
    
    /**
     * set up the heuristic information.
     * assuming no 'frozen' classes (TODO later)
     * 
     * @param numberOfAttributes
     * @param numberOfMethods
     * @param numberOfClasses 
     */
    public static void setUp( 
        int numAttributes, int numMethods, int numberOfClasses )
    {
        assert numAttributes > 0; 
        assert numMethods > 0;
        assert numberOfClasses > 0;
        
        numberOfAttributes = numAttributes;
        numberOfMethods = numMethods;
        
        final int total = numberOfAttributes + numberOfMethods;
        
        // yes! I know this is integer division; but we want an integer
        idealNumberOfElementsPerClass = total / numberOfClasses;
        remainderElements = total % numberOfClasses;
    }

}   // end class

//------- end of file -------------------------------------

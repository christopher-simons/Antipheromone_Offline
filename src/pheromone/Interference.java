/*
 * Interference.java
 * 7 Augut 2018
 */
package pheromone;

/**
 * to record the presence or absence of interference
 * @author cl-simons
 */
public class Interference 
{
    public boolean best;
    public boolean worst;
    
    public Interference( ) 
    {
        best = false;
        worst = false;
    }
    
    public boolean isInterference( )
    {
        return best && worst;
    }
  
}

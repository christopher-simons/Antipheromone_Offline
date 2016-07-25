/*
 * ClassPathRole.java
 * created 11 October 2011
 */

package engine;

/**
 *
 * @author Christopher Simons
 */

public class ClassPathRole extends Role
{
    private double cohesion;
    
    public ClassPathRole( )
    {
        super( Distinction.classs );
        
        cohesion = -99.99; // nonsense value
    }

    /**
     * @return the cohesion
     */
    @Override
    public double getCohesion() 
    {
        return cohesion;
    }

    /**
     * @param cohesion the cohesion to set
     */
    @Override
    public void setCohesion( double cohesion ) 
    {
        this.cohesion = cohesion;
    }
    
    
}   // end class

//------- end file ----------------------------------------


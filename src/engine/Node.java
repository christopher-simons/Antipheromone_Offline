/*
 * Node.java
 * Created 2 October 2011
 * Renamed from "Vertex" to "Node" 30 November
 * 
 */

package engine;

/**
 * The Node class, a superclass for Attribute, Method,
 * Nest and EndOfClass
 * 
 * @author Christopher Simons
 */

public class Node
{
    
    protected String name;
    private int number;
    
    public Node( )
    {
        name = "unitialised";
        number = -999;
    }
    
    public Node( String name, int number ) 
    {
        assert name != null;
        this.name = name;
        
        assert number >= 0;
        this.number = number;
    }
    
    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName( ) 
    {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) 
    {
        this.name = name;
    }

    /**
     * @return the number
     */
    public int getNumber() 
    {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) 
    {
        this.number = number;
    }
    
     
    
}   // end class

//------- end file ----------------------------------------


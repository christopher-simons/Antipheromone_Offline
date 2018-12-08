/*
 * HeuristicAnt2.java
 * 22 July 2013
 */
package heuristics;

import config.AlgorithmParameters;
import engine.Ant;
import engine.Attribute;
import engine.EndOfClass;
import engine.Method;
import engine.Node;
import java.util.List;
import pheromone.AlphaMatrix;
import pheromone.PheromoneOperators;
import problem.ProblemController;
import softwareDesign.CLSClass;

/**
 *
 * @author clsimons
 */
public class HeuristicAnt2 extends Ant 
{
    private int[ ][ ] useMatrix;    // required for CBO heuristic
    
    /** 
     * constructor
     * @param reference to list of attributes and methods
     * @param number of classes
     * @param reference to alpha table
     * @param list of classes that user elects to "freeze"
     * @param handling constraints boolean
     */
    public HeuristicAnt2( 
        List< Node > nodes,
        List< Attribute > attributeList,
        List< Method > methodList,
        int numberOfClasses,
        AlphaMatrix at,
        List< CLSClass > freezeList,
        boolean handlingConstraints,
        int[ ][ ] useMatrix,
        List< Node > tspNodes )
    {
        //super( nodes, numberOfClasses, at, freezeList, handlingConstraints );
        super( nodes, attributeList, methodList, numberOfClasses, at, handlingConstraints, tspNodes );
        this.useMatrix = useMatrix; 
    }

    /**
     * apply heuristic information to adjust the pheromome values (probabbilities)
     * @param current probability value (taken from the alpha table)
     * @param current node (in the outer loop)
     * @param node (next possible node in the tour from the inner working list loop)
     * @param nodeCounter from the outer loop
     * @return 
     */
    @Override
    protected double applyBeta( double probability, Node current, Node node, int nodeCounter )
    {
        assert probability >= 0.0;
        assert current != null;
        assert node != null;
        assert nodeCounter >= 0;
        assert probability >= 0.0;
        
        double postBetaNAC = applyBetaNAC( probability, current, node, nodeCounter );
        
        return applyBetaCBO( postBetaNAC );        
    }
    
    /**
     * apply beta heuristic with respect to Numbers Among Classes (NAC) elegance
     * @param probability from (alpha'd) pheromone table
     * @param current node in the tour
     * @param next possible node in the tour
     * @param nodeCounter in the outer loop count of possible nodes
     * @return probability after beta NAC heuristic applied
     */
    private double applyBetaNAC( double probability, Node current, Node nextPossible, int nodeCounter )
    {
        double result = probability;
        final double zeroDistanceConstant = 1.35;
        
        if( current.getNumber( ) == nextPossible.getNumber( ) )
        {
            // in outer loop, when current and node are the same, 
            // use of beta not applicable, so do nothing
        }
        else if( ( current instanceof EndOfClass ) == false &&
                 ( nextPossible instanceof EndOfClass ) == true )
        {   
            final int distance = calculateNACDistance( nodeCounter );
            assert distance >= 0;
            double distanceFactor = 1.0;
            
            if( distance == 0 ) // we're at the 'ideal' place for an EoC
            {
                distanceFactor = zeroDistanceConstant; 
            }
            else    // the distance is greater than zero
            {
                // experiment 23 July 2013
                // not effective, causes infeasible solution tours
                // distanceFactor = 1.0 / (double) distance;    2013
            
                // experiment 25 July 2013
                // try something more subtle
                final double distanceConstant = 0.001;
                distanceFactor = 1.0 - ( distance * distanceConstant );
            }
            
            double heuristicNAC = Math.pow( distanceFactor, AlgorithmParameters.BETA_NAC );
            result = probability * heuristicNAC;
        }
        
        return result;
    }
    
    /**
     * calculate the NAC distance between this node and the next 'ideal' EoC 
     * @param nodeCounter
     * @return NAC distance
     */
    private int calculateNACDistance( int nodeCounter )
    {
        int modulus = nodeCounter % HeuristicInformation.idealNumberOfElementsPerClass;
        int NACDistance = 0;
        
        if( modulus == 0 )
        {
            // do nothing, the NACDistance is zero
        }
        else
        {
            NACDistance = HeuristicInformation.idealNumberOfElementsPerClass - modulus;
        }
        
//        System.out.println( "node Counter is: " + nodeCounter + " distance is: " + NACDistance );
        return NACDistance;
    }
    
    private double applyBetaCBO( double postBetaNAC )
    {
        return postBetaNAC;
        
        // here Chris
        
//        if( useMatrix[ current.getNumber( ) - 1][ node.getNumber( ) - 1 ] == 1 )
//        {
//            result = Math.pow( temp, AlgorithmParameters.BETA ); 
//        }
//        
        
    }
}   // end class

//------- end of file -------------------------------------

/*
 * PheromoneOperators.java
 * created 18 October 2011
 * 25 April 2012 - mu and rho passed as parameters to operators
 */

package pheromone;

import java.util.*;
import config.*;
import myUtils.Utility;
import engine.*;
import myUtils.Weights;
import pareto.ParetoOperators;
import softwareDesign.CLSClass;

/**
 * This class offers the capabilities related to Pheromone
 * i.e. pheromone deposit for a state transition in a solution path, 
 * and pheromone decay (evaporation)
 *
 * See "Ant Colony Optimisation", by Dorigo and Stutzle,
 * 2004, MIT Press.
 * 
 * In the Dorigo book, the evaporation coefficient is known
 * as "rho", which stands for "rate"
 * 
 * I've introduced a new parameter "mu" for pheromone update.
 * Pheromone is updated in proportion to coupling fitness for
 * full path designs. The updates are raised to to power of "mu".
 * 
 * @author Christopher Simons
 */

public class PheromoneOperators 
{
    // 24 April 2012 factor for elistist decay / evaporation
    private static final double ELITIST_FACTOR = 0.02;
    
    // 10 July 2012
    private static final boolean SINGLE_OBJECTIVE = false;
    
    // 20 July 2012
    /** the ludicrously high delta to freeze a class */
    private static final double FREEZE_DELTA = 1000000.0;
    
    /**
     * let evaporation of the pheromone take place
     * 19 April 2012 a touch of elitism added
     * @param reference to the pheromoneTable
     */
    public static void evaporate( PheromoneTable pheromoneTable )
    {
        assert pheromoneTable != null;
        final int pheromoneTableSize = pheromoneTable.size( );
        
        // 19 April 2012 - fitness proportionate decay
        double lowest = 1000000.0;
        double highest = 0.0;
        double prob = 0.0;
        double median = 0.0;
        
        // 24 April 2012, switch to toggle elist evaporation
        if( AlgorithmParameters.evaporationElitism == true )
        {
            for( int i = 0; i < pheromoneTableSize; i++) 
            {
                for( int j = 0; j < pheromoneTableSize; j++) 
                {
                    prob = pheromoneTable.getProbabilityAt( i, j );

                    if( prob < lowest )
                    {
                        lowest = prob;
                    }

                    if( prob > highest )
                    {
                        highest = prob;
                    }
                }
            }
        
            assert highest >= lowest;
            assert highest - lowest != 0.0;
            // bug fix 7 September 2012
//            median = ( highest - lowest ) / 2.0;
            median = lowest + ( ( highest - lowest ) / 2.0 );
        }
        
        // 27 November 2015: use the value of the algorithm paramter RHO directly
        final double evaporationFactor = 1.0 - AlgorithmParameters.RHO;
        assert evaporationFactor >= 0.0;
        assert evaporationFactor <= 1.0;
        
        for( int i = 0; i < pheromoneTableSize; i++) 
        {
            for( int j = 0; j < pheromoneTableSize; j++) 
            {
                prob = pheromoneTable.getProbabilityAt( i, j );
                double newProb = 0.0;
                
                if( AlgorithmParameters.evaporationElitism == true )
                {
                    double multiplier = 1.0;

                    if( prob > median ) // decay is proportionately less
                    {
                        double difference = highest - prob;
                        multiplier = evaporationFactor * ( 1 - ( difference / median ) * ELITIST_FACTOR );
                    }
                    else if( prob < median ) // decay is proportionately more
                    {
                        double difference = median - prob;
                        multiplier = evaporationFactor * ( 1 + ( difference / median ) * ELITIST_FACTOR );
                    }
                    else // prob == median
                    {
                        // do nothing, multiplier staus at 1.0
                    }

                    newProb = prob * multiplier;
                }
                else // uniform (normal) evaporation 
                {
                    newProb = prob * evaporationFactor;  
                }
                        
                pheromoneTable.setProbabilityAt( i , j, newProb );
            }
        }
    }
    
    /**
     * Update the pheromone levels in the pheromone table.
     * then the pheromone is updated with respect to class cohesion.
     * 
     * @param pheromoneTable
     * @param colony
     * @param weights
     * @param bestInColonyCBO best path in the colony w.r.t. CBO
     * @param bestInColonyNAC best path in the colony w.r.t. NAC
     * @param bestInColonyATMR best path in the colony w.r.t. ATMR
     * @param bestInColonyCombined best path in the colony w.r.t. Combined
     * @param worstInColonyCBO worst path in the colony w.r.t. CBO
     * @param worstInColonyNAC
     * @param worstInColonyCombined worst path in the colony w.r.t. Combined
     * @param iteration iteration count
     */
    public static void update( 
        PheromoneTable pheromoneTable, 
        List< Path > colony, 
        Weights weights,
        Path bestInColonyCBO,
        Path bestInColonyNAC,
        Path bestInColonyATMR,
        Path bestInColonyCombined,
        Path worstInColonyCBO,
        Path worstInColonyNAC,
        Path worstInColonyCombined,
        int iteration )
    {
        assert pheromoneTable != null;
        assert colony != null;
        assert colony.size( ) > 0;
        assert weights != null;
        assert bestInColonyCBO != null;
        assert bestInColonyNAC != null;
        assert bestInColonyATMR != null;
        assert bestInColonyCombined != null;
        assert worstInColonyCBO != null;
        assert worstInColonyNAC != null;
        assert worstInColonyCombined != null;
        assert iteration >= 0;
        
        if( AlgorithmParameters.algorithm == AlgorithmParameters.SIMPLE_ACO ) 
        {
            // Simple-ACO, so every ant lays pheromone
            performSimpleACOUpdate( 
                colony, 
                pheromoneTable, 
                weights,
                worstInColonyCBO,
                worstInColonyCombined,
                iteration );
        }
        else if( AlgorithmParameters.algorithm == AlgorithmParameters.MMAS ) 
        {
            // MAX-MIN Ant System, so only best any lays pheromone
            performMMASUpdate( 
                colony, 
                pheromoneTable, 
                weights, 
                bestInColonyCBO, 
                bestInColonyNAC,
                bestInColonyCombined,
                worstInColonyCBO,
                worstInColonyNAC,
                worstInColonyCombined,
                iteration );
        }
        else
        {
            assert false : "impossible algorithm parameter in pheromone update";
        }   

//        pheromoneTable.show();
    }
 
    /**
     * perform simple ACO update
     * @param colony
     * @param pheromoneTable
     * @param weights
     * @param worstInColonyCBO
     * @param worstInColonyCombined
     * @param iteration 
     */
    private static void performSimpleACOUpdate( 
        List< Path > colony, 
        PheromoneTable pheromoneTable, 
        Weights weights,
        Path worstInColonyCBO,
        Path worstInColonyCombined,
        int iteration )
    {
        // firstly, lay pheromone
        Iterator< Path > it = colony.iterator( );

        while( it.hasNext( ) ) 
        {
            Path p = it.next( );
            layPheromoneForPath( p, pheromoneTable, weights );
        }
            
        // secondly, is there antipheromone to lay?
        if( AlgorithmParameters.ANTIPHEROMONE_PHASE_THRESHOLD_PERCENTAGE > 0 )
        {
            // check which phase we're in
            assert iteration <= AlgorithmParameters.NUMBER_OF_ITERATIONS;
            double progress = (double) iteration / (double) AlgorithmParameters.NUMBER_OF_ITERATIONS;
            assert progress <= 1.0;
            progress *= 100.0;
            assert progress >= 0.0 && progress <= 100.0 : "Progress Percentage is: " + progress;
            final double temp = Math.floor( progress );
            final int progressPercentage = (int) temp;
            assert progressPercentage >= 0 && progressPercentage <= 100: "Progress Percentage is: " + progressPercentage;

            // if so, are we at the early exploratory stage? 
            if( progressPercentage < AlgorithmParameters.ANTIPHEROMONE_PHASE_THRESHOLD_PERCENTAGE )
            {
                if( AlgorithmParameters.fitness == AlgorithmParameters.CBO )
                {
                    layAntiPheromoneForPath( 
                        AlgorithmParameters.SIMPLE_ACO,
                        worstInColonyCBO, 
                        pheromoneTable );
                }
                else if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
                {
                    layAntiPheromoneForPath( 
                        AlgorithmParameters.SIMPLE_ACO,
                        worstInColonyCombined, 
                        pheromoneTable );
                }
                else
                {
                    assert false : "impossible fitness measure while laying antipheromone";
                }
            }
        }
    }
    
    /**
     * update the pheromone table with respect to 
     * @param reference to path
     * @param reference to pheromoneTable
     * @param multi-objective weights
     */
    private static void layPheromoneForPath( 
        Path path, PheromoneTable pheromoneTable, Weights weights )
    {
        assert path != null;
        assert pheromoneTable != null;
        assert weights != null;
        
        double delta = calculateDelta( path, weights );
//        double delta = calculateDelta2( path, mu );
        
        int from = 0, to = 0;
        
        // final node must be an "end of class"
        final int finalNode = path.size( ) - 1;
            
        // and now iterate along the node list in the path
        Iterator< Node > it = path.iterator( );
        
        for( int i = 0; it.hasNext( ); i++ )
        {
            Node node = it.next( );
            
            if( i == 0 )    // the "nest"
            {    
                from = node.getNumber( );
            }
            else if( i == finalNode ) // the last "end of class" marker
            {
                // do nothing, because the probability of moving from
                // the last end of class marker is always zero
                
                assert node instanceof EndOfClass;
            }
            else
            {
                to = node.getNumber( );
            
                double probability = pheromoneTable.getProbabilityAt( from, to );

                probability += delta;
                
                if( AlgorithmParameters.algorithm == AlgorithmParameters.MMAS )
                {
                    // In MAX-MIN Ant System, the range of pheromone levels
                    // is limited to an interval [Tmin, Tmax], which
                    // ensures a minimum degree of search diversification.
                    
                    if( probability < AlgorithmParameters.MMAS_PHEROMONE_MINIMUM ) 
                    {
                        probability = AlgorithmParameters.MMAS_PHEROMONE_MINIMUM;
                    }
                    
                    if( probability > AlgorithmParameters.MMAS_PHEROMONE_MAXIMUM ) 
                    {
                        probability = AlgorithmParameters.MMAS_PHEROMONE_MAXIMUM;
                    }
                }

                // in Simple-ACO, there is no enforcement of any range
                // of pheromone levels in the pheromone matrix
                
                pheromoneTable.setProbabilityAt( from, to, probability );
                
                // 18 April 2012 symmetrical pheromone update 
                pheromoneTable.setProbabilityAt( to, from, probability );

                // advance to next vertex
                from = to;
            }
            
        }   // end for each vertex in the solution path        
    }
    

    /**
     * 8 January 2016
     * lay antipheromone for a path
     * @param algorithmParameter
     * @param path
     * @param pheromoneTable 
     */
    private static void layAntiPheromoneForPath( 
        int algorithmParameter,
        Path path, 
        PheromoneTable pheromoneTable )
    {
        assert algorithmParameter >= 0;
        assert path != null;
        assert pheromoneTable != null;  
        
        int from = 0, to = 0;
        
        // final node must be an "end of class"
        final int finalNode = path.size( ) - 1;
            
        // and now iterate along the node list in the path
        Iterator< Node > it = path.iterator( );
        
        for( int i = 0; it.hasNext( ); i++ )
        {
            Node node = it.next( );
            
            if( i == 0 )    // the "nest"
            {    
                from = node.getNumber( );
            }
            else if( i == finalNode ) // the last "end of class" marker
            {
                // do nothing, because the probability of moving from
                // the last end of class marker is always zero
                
                assert node instanceof EndOfClass;
            }
            else
            {
                to = node.getNumber( );
                
                if( algorithmParameter == AlgorithmParameters.SIMPLE_ACO )
                {
                    // set antipheromone rho to zero initially
                    double rhoAntipheromoneSimpleACO = AlgorithmParameters.SIMPLE_ACO_RHO_ANTIPHEROMONE_ZERO;
                    
                    if( AlgorithmParameters.SIMPLE_ACO_MONTGOMERY_SUBTRACTIVE_ANTIPHEROMONE == true )
                    {
                        if( AlgorithmParameters.Simple_ACO_Montgomery_Subtractive_Antipheromone_Value == 
                                AlgorithmParameters.SIMPLE_ACO_MONTGOMERY_SUBTRACTIVE_ANTIPHEROMONE_HALF )
                        {
                            // set antipheromone rho to half
                            rhoAntipheromoneSimpleACO = AlgorithmParameters.SIMPLE_ACO_RHO_ANTIPHEROMONE_HALF;
                        }
                        else if( AlgorithmParameters.Simple_ACO_Montgomery_Subtractive_Antipheromone_Value == 
                                    AlgorithmParameters.SIMPLE_ACO_MONTGOMERY_SUBTRACTIVE_ANTIPHEROMONE_MINIMAL )
                        {
                            // set antipheromone rho to minimal
                            rhoAntipheromoneSimpleACO = AlgorithmParameters.SIMPLE_ACO_RHO_ANTIPHEROMONE_MINIMAL;
                        }
                        else
                        {
                            assert false : "impossible Montgomery antipheromone level";
                        }
                    }
                    
                    double probability = pheromoneTable.getProbabilityAt( from, to );
                    probability *= rhoAntipheromoneSimpleACO; 
                    
                    pheromoneTable.setProbabilityAt( from, to, probability );
                    // 18 April 2012 symmetrical pheromone update 
                    pheromoneTable.setProbabilityAt( to, from, probability );
                }
                else if( algorithmParameter == AlgorithmParameters.MMAS )
                {
                    if( AlgorithmParameters.MMAS_SUBTRACTIVE_ANTIPHEROMONE == true )
                    {
                        double rhoAntipheromoneMMAS = AlgorithmParameters.MMAS_RHO_ANTIPHEROMONE_HALF;
                        
                        double probability = pheromoneTable.getProbabilityAt( from, to );
                        probability *= rhoAntipheromoneMMAS; 
                        
                        if( probability < AlgorithmParameters.MMAS_PHEROMONE_MINIMUM )
                        {
                            probability = AlgorithmParameters.MMAS_PHEROMONE_MINIMUM;
                        }
                        
                        pheromoneTable.setProbabilityAt( from, to, probability );
                        // 18 April 2012 symmetrical pheromone update 
                        pheromoneTable.setProbabilityAt( to, from, probability);
                    }
                    else // lay down the minimum pheromone
                    {
                        pheromoneTable.setProbabilityAt( from, to, AlgorithmParameters.MMAS_PHEROMONE_MINIMUM );
                        // 18 April 2012 symmetrical pheromone update 
                        pheromoneTable.setProbabilityAt( to, from, AlgorithmParameters.MMAS_PHEROMONE_MINIMUM );
                    }
                }
                else
                {
                    assert false: "impossible algorithm parameter in antipheromone update";
                }
                
                // advance to next vertex
                from = to;
            }
            
        }   // end for each vertex in the solution path    
    }
    
    /**
     * calculate the delta for update
     * @param path
     * @param weights
     * @return delta
     */
    private static double calculateDelta( Path path, Weights weights )
    {
        assert path != null;
        assert weights != null;
        
        double coupling = path.getCBO( ); 
        double maximisedCBO = 1.0 - coupling; // transform to maximisation
        assert maximisedCBO >= 0.0;
        assert maximisedCBO <= 1.0;
     
        double eleganceNAC = path.getEleganceNAC( );
        assert eleganceNAC >= 0.0;
            
        // 7 December 2015 NAC is already scaled when calculated
        double maximisedNAC = 1 - eleganceNAC;
        assert maximisedNAC >= 0.0;
        assert maximisedNAC <= 1.0;
        
         // 29 November 2015
        double maximisedCombined = 1.0 - path.getCombined( );
        assert maximisedCombined >= 0.0;
        assert maximisedCombined <= 1.0;
        
        double rawValue = 0.0;
        double maximisedATMR = 0.0; // 8 December 2015

        // as used in interactive multi-objective ACO
        // 20 November 2015 - put up with this for the moment in batch mode
        rawValue = calculateRawFactorWithWeights( 
                  maximisedCBO, maximisedNAC, maximisedATMR, maximisedCombined, weights );

        // raise the raw factor to the power of mu
        double delta = Math.pow( rawValue, AlgorithmParameters.MU ); 
        
        return delta;
    }

    /**
     * AS USED IN THE MULTI-OBJECTIVE SIMULATION
     * 
     * Calculate the raw multi-objective factor with a fixed CBO weight of 0.8.
     * @param maximisedCBO - coupling
     * @param normalisedNAC 
     * @param normalisedATMR 
     * @param mu - the update parameter
     * @return rawFactor
     */
    private static double calculateRawFactorWithFixedCBOWeight( 
        double maximisedCBO, 
        double normalisedNAC,
        double normalisedATMR )
    {
        // set up the weights
        final double CBOWeight = 0.80; // applies to all design problems

        final double NACWeight = Utility.getRandomInRange( 0.0, 1.0 - CBOWeight );
        assert NACWeight >= 0.0;
        assert NACWeight <= ( 1.0 - CBOWeight );
        final double ATMRWeight = 1.0 - ( CBOWeight + NACWeight );
        assert CBOWeight + NACWeight + ATMRWeight == 1.0;

        double temp = 
           ( maximisedCBO * CBOWeight ) +
           ( normalisedNAC * NACWeight ) +
           ( normalisedATMR * ATMRWeight );
        assert temp >= 0.0 : "temp is:" + temp;
        
        return temp;
    }
    
    
    private static double calculateRawValueTest(
        double maximisedCBO, 
        double maximisedNAC, 
        double maximisedATMR )
    {
        assert maximisedCBO >= 0.0;
        assert maximisedNAC >= 0.0;
        assert maximisedATMR >= 0.0;
        
        double result = 0.0;
        
        boolean CBO = false;
        boolean NAC = false;
        boolean ATMR = false;
        
        double CBOWeight = 0.0;
        double NACWeight = 0.0;
        double ATMRWeight = 0.0;
        
        if( AlgorithmParameters.objectiveCBO == true )
        {
            CBO = true;
        }
     
        if( AlgorithmParameters.objectiveNAC == true )
        {
            NAC = true;
        }
        
        if( AlgorithmParameters.objectiveATMR == true )
        {
            ATMR = true;
        }
     
     
        // handle single objective first
    
        if( CBO == true && NAC == false && ATMR == false )
        {
            result = maximisedCBO;
            
        }
        else if( CBO == false && NAC == true && ATMR == false )
        {
            result = maximisedNAC;
            
        }
        else if( CBO == false && NAC == false && ATMR == true )
        {
            result = maximisedATMR;
            
        }
        
        // handle multi objective second
        
        else if( CBO == true && NAC == true && ATMR == false )
        {
            CBOWeight = 0.5;
            NACWeight = 0.5;
            
            result = ( maximisedCBO * CBOWeight ) * ( maximisedNAC * NACWeight );
        }
        else if( CBO == true && NAC == true && ATMR == true )
        {
            CBOWeight = 0.33;
            NACWeight = 0.33;
            ATMRWeight = 0.33;
            
            result = ( maximisedCBO * CBOWeight ) * 
                     ( maximisedNAC * NACWeight ) *
                     ( maximisedATMR * ATMRWeight );
        }
        else
        {
            assert false : "impossible objective combination!!";
        }
        
        
        return result;
    }
    
    
    /**
     * calculate the rawFactor using coefficients
     * @param maximised CBO
     * @param maximised ATMR
     * @param maximised NAC
     * @param maximised COMBINED
     * @param weights
     * @return rawFactor
     */
    private static double calculateRawFactorWithWeights( 
        double maximisedCBO, 
        double maximisedNAC,
        double maximisedATMR,
        double maximisedCombined,
        Weights weights )
    {
        assert maximisedCBO >= 0.0 && maximisedCBO <= 1.0;
        assert maximisedNAC >= 0.0 && maximisedNAC <= 1.0;
        assert maximisedATMR >= 0.0 && maximisedATMR <= 1.0;
        assert maximisedATMR >= 0.0 && maximisedATMR <= 1.0;
        assert weights != null;
        weights.checkSum( );
        
        boolean weightedSum = true;
        double rawFactor = 0.0;
        
        if( weightedSum )
        {
            rawFactor =
                ( weights.weightCBO * maximisedCBO ) +
                ( weights.weightNAC * maximisedNAC ) +
                ( weights.weightATMR * maximisedATMR ) +
                ( weights.weightCOMBINED * maximisedCombined ); 
        }
        else    // must be weighted product
        {
            rawFactor =
                ( weights.weightCBO * maximisedCBO ) *
                ( weights.weightNAC * maximisedNAC ) *
                ( weights.weightATMR * maximisedATMR ) *
                ( weights.weightCOMBINED * maximisedCombined ); 
        }
        
        return rawFactor;
    }
    
    
    /**
     * calculate the delta using the reciprocal of the weighted
     * domination count. 
     * @param path
     * @param mu 
     * @return delta - the amount by which to increase the pheromone
     */
    private static double calculateDelta2( Path path, double mu  )
    {
        assert path != null;
        assert mu >= 0.0;
        
        final int weightedDominationCount = path.getWeightedDominationCount( );
        assert weightedDominationCount >= 0;
        
        double rawFactor = 1.0 / weightedDominationCount;
        
         // raise the raw factor to the power of mu
        double delta = Math.pow( rawFactor, mu ); 
        
        return delta;
    }
    
   
    /**
     * perform an update of the pheromone table to effectively
     * freeze the class.
     * @param pheromone table
     * @param class to be frozen
     */
    public static void freezeUpdate( PheromoneTable pt, List< CLSClass > freezeList )
    {
        assert pt != null;
        assert freezeList != null;
        
        for( CLSClass c : freezeList )
        {
            List< Method > mList = c.getMethodList( );
            List< Attribute > aList = c.getAttributeList( );

            System.out.println( "class in freeze list... ");
            for( int i = 0; i < mList.size( ); i++ )
            {
                System.out.print( " method is: " + 
                                    mList.get( i ).getNumber( ) + " " +
                                    mList.get( i ).getName( ) );
            }

            for( int j = 0; j < aList.size( ); j++ )
            {
                System.out.print( " attribute is: " + 
                                    aList.get( j ).getNumber( ) + " " +
                                    aList.get( j ).getName( ) + " " );
            }
            System.out.println( "" );
        }
        
        for( CLSClass c : freezeList )
        {
            List< Method > mList = c.getMethodList( );
            List< Attribute > aList = c.getAttributeList( );

            for( int i = 0; i < mList.size( ); i++ )
            {
                final int methodNumber = mList.get( i ).getNumber( );

                for( int j = 0; j < aList.size( ); j++ )
                {
                   final int attributeNumber = aList.get( j ).getNumber( );

                   pt.setProbabilityAt( methodNumber, attributeNumber, FREEZE_DELTA );
                   // and for the symmtrical update...
                   pt.setProbabilityAt( attributeNumber, methodNumber, FREEZE_DELTA );
                }
            }
        }
         
        pt.show( );
        
    }
    
    /**
     * perform pheromone update when using MMAS
     * @param colony i.e. all tours generated by the colony
     * @param pheromoneTable
     * @param weights
     * @param best Path In Colony CBO
     * @param best Path In Colony Combined
     * @param best NAC
     * @param worst NAC
     * @param worst Path In Colony CBO
     * @param worst Path In Colony Combined
     * @param iteration of the search
     */
    private static void performMMASUpdate( 
        List< Path > colony, 
        PheromoneTable pheromoneTable, 
        Weights weights,
        Path bestPathInColonyCBO,
        Path bestPathInColonyNAC,
        Path bestPathInColonyCombined,
        Path worstPathInColonyCBO,
        Path worstPathInColonyNAC,
        Path worstPathInColonyCombined,
        int iteration )
    {
        if( AlgorithmParameters.pheromoneUpdate == AlgorithmParameters.PheromoneUpdate.ParetoBased )
        {
            performParetoDominationBasedUpdate( colony, pheromoneTable, AlgorithmParameters.MU, weights );
        }
        else if( AlgorithmParameters.pheromoneUpdate == AlgorithmParameters.PheromoneUpdate.SO )
        {
            // 29 November 2015 Update
            if( AlgorithmParameters.fitness == AlgorithmParameters.CBO )    
            {
                layPheromoneForPath( bestPathInColonyCBO, pheromoneTable, weights );
            }
            else if( AlgorithmParameters.fitness == AlgorithmParameters.NAC )
            {
                layPheromoneForPath( bestPathInColonyNAC, pheromoneTable, weights );
            }
            else if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
            {
                layPheromoneForPath( bestPathInColonyCombined, pheromoneTable, weights );
            }
            else
            {
                assert true : "impossible single objective update parameter";
            }
            
            assert iteration <= AlgorithmParameters.NUMBER_OF_ITERATIONS;
            double progress = (double) iteration / (double) AlgorithmParameters.NUMBER_OF_ITERATIONS;
            assert progress <= 1.0;
            progress *= 100.0;
            assert progress >= 0.0;
            assert progress <= 100.0 : "Progress Percentage is: " + progress;
            final double temp = Math.floor( progress );
            final int progressPercentage = (int) temp;
            assert progressPercentage >= 0 : "Progress Percentage is: " + progressPercentage;
            assert progressPercentage <= 100 : "Progress Percentage is: " + progressPercentage;
            
            // 8 January 2016 antipheromone update
            if( progressPercentage < AlgorithmParameters.ANTIPHEROMONE_PHASE_THRESHOLD_PERCENTAGE )
            {
                if( AlgorithmParameters.fitness == AlgorithmParameters.CBO )    
                {
                    layAntiPheromoneForPath( 
                        AlgorithmParameters.MMAS,
                        worstPathInColonyCBO, 
                        pheromoneTable );
                }
                else if( AlgorithmParameters.fitness == AlgorithmParameters.NAC )
                {
                    layAntiPheromoneForPath(
                        AlgorithmParameters.MMAS,
                        worstPathInColonyNAC, 
                        pheromoneTable );
                }
                
                else if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
                {
                    layAntiPheromoneForPath(
                        AlgorithmParameters.MMAS,
                        worstPathInColonyCombined, 
                        pheromoneTable );
                }
                else
                {
                    assert false : "impossible single objective update parameter";
                }
            }
        }
        else
        {   
            assert false : "impossible pheromone update parameter";
        }
    }
    
    
    private static void performParetoDominationBasedUpdate( 
        List< Path > colony, PheromoneTable pheromoneTable, double mu, Weights weights )
    {
         //domination counts are already calculated in the deamon actions
//          ParetoOperators.calculateDominationCount2( colony );

        // MAX_MIN Ant System (MMAS), so
        // only best in colony lays pheromone
        boolean done = false;
        int domCount = 0;

        while( ! done )
        {
            List< Path > list = ParetoOperators.getPathsWithDomCount( colony, domCount );
            if( list.isEmpty( ) )
            {
                domCount++;
            }
            else
            {
                int numberOfPaths = list.size( );
                assert numberOfPaths > 0;
                // select one at random from this list
                final int randomSelection = Utility.getRandomInRange( 1, numberOfPaths );
                assert randomSelection <= numberOfPaths;
                Path path = list.get( randomSelection - 1 );


                assert path != null;

                layPheromoneForPath( path, pheromoneTable, weights );

                // if you wish to update with non-dominated solutions, use this...
//                    for( Path p : list )
//                    {
//                        layPheromoneForWholePathSolution( p, pheromoneTable, mu, weights );
//                    }

                done = true;
            }
        }
    }
        
}   // end class

//------- end file ----------------------------------------


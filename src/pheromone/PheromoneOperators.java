/*
 * PheromoneOperators.java
 * created 18 October 2011
 * 25 April 2012 - mu and rho passed as parameters to operators
 */

package pheromone;

import java.util.*;
import config.*;
import engine.*;
import myUtils.Weights;
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
    
    // 20 July 2012
    /** the ludicrously high delta to freeze a class */
    private static final double FREEZE_DELTA = 1000000.0;
    
    /**
     * let evaporation of the pheromone take place
     * 19 April 2012 a touch of elitism added
     * @param reference to the pheromoneTable
     */
    public static void evaporate( PheromoneMatrix pheromoneTable )
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
        final double evaporationFactor = 1.0 - AlgorithmParameters.rho;
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
     * @param bestInColonyCBO best path in the colony w.r.t. CBO
     * @param bestInColonyNAC best path in the colony w.r.t. NAC
     * @param bestInColonyATMR best path in the colony w.r.t. ATMR
     * @param bestInColonyCombined best path in the colony w.r.t. Combined
     * @param secondBestInColonyCombined second best path in the colony w.r.t. Combined
     * @param thirdBestInColonyCombined third best path in the colony w.r.t. Combined
     * @param worstInColonyCBO worst path in the colony w.r.t. CBO
     * @param worstInColonyNAC
     * @param worstInColonyCombined worst path in the colony w.r.t. Combined
     * @param secondWorstInColonyCombined second worst path in the colony w.r.t. Combined
     * @param thirdWorstInColonyCombined third worst path in the colony w.r.t. Combined
     * @param iteration iteration count
     * @param bpm best paths matrix for iteration
     * @param bestTSPPathInColony
     * @param worstTSPPathInColony
     */
    public static void update( 
        PheromoneMatrix pheromoneTable, 
        List< Path > colony, 
        Path bestInColonyCBO,
        Path bestInColonyNAC,
        Path bestInColonyATMR,
        Path bestInColonyCombined,
        Path secondBestInColonyCombined,
        Path thirdBestInColonyCombined,
        Path worstInColonyCBO,
        Path worstInColonyNAC,
        Path worstInColonyCombined,
        Path secondWorstInColonyCombined,
        Path thirdWorstInColonyCombined,
        int iteration,
        BestPathsMatrix bpm,
        Path bestTSPPathInColony,
        Path secondBestTSPPathInColony,
        Path thirdBestTSPPathInColony,
        Path worstTSPPathInColony,
        Path secondWorstTSPPathInColony,
        Path thirdWorstTSPPathInColony )
    {
        assert pheromoneTable != null;
        assert colony != null;
        assert colony.size( ) > 0;
        assert iteration >= 0;
        assert bpm != null;
        
        if( Parameters.problemNumber == Parameters.CBS || 
            Parameters.problemNumber == Parameters.GDP ||
            Parameters.problemNumber == Parameters.RANDOMISED ||
            Parameters.problemNumber == Parameters.SC )
        {
            assert bestInColonyCBO != null;
            assert bestInColonyNAC != null;
            assert bestInColonyATMR != null;
            assert bestInColonyCombined != null;
            assert secondBestInColonyCombined != null;
            assert thirdBestInColonyCombined != null;
            assert worstInColonyCBO != null;
            assert worstInColonyNAC != null;
            assert worstInColonyCombined != null;
            assert secondWorstInColonyCombined != null;
            assert thirdWorstInColonyCombined != null;
        }
        else // must be a TSP problem instance
        {
            assert bestTSPPathInColony != null; 
            assert secondBestTSPPathInColony != null; 
            assert thirdBestTSPPathInColony != null; 
            assert worstTSPPathInColony != null;
            assert secondWorstTSPPathInColony != null;
            assert thirdWorstTSPPathInColony != null;
        }
        
        if( AlgorithmParameters.algorithm == AlgorithmParameters.SIMPLE_ACO ) 
        {
            // Simple-ACO (every ant lays pheromone), with antipheromone extensions
            performSimpleACOUpdate( 
                colony, 
                pheromoneTable, 
                worstInColonyCBO,
                worstInColonyCombined,
                iteration,
                bpm );
        }
        else if( AlgorithmParameters.algorithm == AlgorithmParameters.MMAS ) 
        {
            // MAX-MIN Ant System,with antipheromone extensions
            performMMASUpdate( 
                colony, 
                pheromoneTable, 
                bestInColonyCBO, 
                bestInColonyNAC,
                bestInColonyCombined,
                secondBestInColonyCombined,
                thirdBestInColonyCombined,
                worstInColonyCBO,
                worstInColonyNAC,
                worstInColonyCombined,
                secondWorstInColonyCombined,
                thirdWorstInColonyCombined,
                iteration,
                bpm, 
                bestTSPPathInColony,
                secondBestTSPPathInColony,
                thirdBestTSPPathInColony,
                worstTSPPathInColony,
                secondWorstTSPPathInColony,
                thirdWorstTSPPathInColony );
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
     * @param worstInColonyCBO
     * @param worstInColonyCombined
     * @param iteration 
     */
    private static void performSimpleACOUpdate( 
        List< Path > colony, 
        PheromoneMatrix pheromoneTable, 
        Path worstInColonyCBO,
        Path worstInColonyCombined,
        int iteration,
        BestPathsMatrix bpm )
    {
        assert colony != null;
        assert pheromoneTable != null;
        assert worstInColonyCBO != null;
        assert worstInColonyCombined != null;
        assert iteration >= 0;
        assert bpm != null;
        
        
        // firstly, lay pheromone for every ant in the colony
        Iterator< Path > it = colony.iterator( );

        while( it.hasNext( ) ) 
        {
            Path p = it.next( );
            layPheromoneForPath( p, pheromoneTable );
        }
            
        // secondly, is there antipheromone to lay?
        if( AlgorithmParameters.SIMPLE_ACO_SUBTRACTIVE_ANTIPHEROMONE == true )
        {
            // if so, then check to see if we're in the antipheromone phase...
            if( AlgorithmParameters.antiPheromonePhasePercentage > 0 )
            {
                // ... and if we are, lay antipheromone
                assert iteration <= AlgorithmParameters.NUMBER_OF_ITERATIONS;
                double progress = (double) iteration / (double) AlgorithmParameters.NUMBER_OF_ITERATIONS;
                assert progress <= 1.0;
                progress *= 100.0;
                assert progress >= 0.0 && progress <= 100.0 : "Progress Percentage is: " + progress;
                final double temp = Math.floor( progress );
                final int progressPercentage = (int) temp;
                assert progressPercentage >= 0 && progressPercentage <= 100: "Progress Percentage is: " + progressPercentage;

                // if so, are we at the early exploratory stage? 
                if( progressPercentage < AlgorithmParameters.antiPheromonePhasePercentage )
                {
                    if( AlgorithmParameters.fitness == AlgorithmParameters.CBO )
                    {
                        layAntiPheromoneForPath( 
                            AlgorithmParameters.SIMPLE_ACO,
                            worstInColonyCBO, 
                            pheromoneTable,
                            bpm );
                    }
                    else if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
                    {
                        layAntiPheromoneForPath( 
                            AlgorithmParameters.SIMPLE_ACO,
                            worstInColonyCombined, 
                            pheromoneTable,
                            bpm );
                    }
                    else
                    {
                        assert false : "impossible fitness measure while laying antipheromone";
                    }
                }
            }
        }
    }
    
    /**
     * update the pheromone table with respect to 
     * @param reference to path
     * @param reference to pheromoneTable
     */
    private static void layPheromoneForPath( Path path, PheromoneMatrix pheromoneTable )
    {
        assert path != null;
        assert pheromoneTable != null;
        
        final double delta = calculateDelta( path );
        
        int from = 0, to = 0;
        
        // in software design problem instances,
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
                // the last node is always zero
                
                // assert node instanceof EndOfClass;
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
                    
                    if( probability < AlgorithmParameters.MMAS_Mmin ) 
                    {
                        probability = AlgorithmParameters.MMAS_Mmin;
                    }
                    
                    if( probability > AlgorithmParameters.MMAS_Mmax ) 
                    {
                        probability = AlgorithmParameters.MMAS_Mmax;
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
        PheromoneMatrix pheromoneTable,
        BestPathsMatrix bpm )
    {
        assert algorithmParameter >= 0;
        assert path != null;
        assert pheromoneTable != null;
        assert bpm != null;
        
        int from = 0, to = 0;
        
        int nodeCounter = 0;
        int preventCounter = 0;
        
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
                
                // comment out 22 September 2018
                // assert node instanceof EndOfClass;
            }
            else
            {
                to = node.getNumber( );
                
                if( algorithmParameter == AlgorithmParameters.SIMPLE_ACO )
                {               
                    assert AlgorithmParameters.SIMPLE_ACO_SUBTRACTIVE_ANTIPHEROMONE == true;
                    
                    double probability = pheromoneTable.getProbabilityAt( from, to );
                    // 15 June 2018
                    probability *= AlgorithmParameters.PHI; 
                    
                    pheromoneTable.setProbabilityAt( from, to, probability );
                    // 18 April 2012 symmetrical pheromone update 
                    pheromoneTable.setProbabilityAt( to, from, probability );
                }
                else if( algorithmParameter == AlgorithmParameters.MMAS )
                {
                    assert AlgorithmParameters.MMAS_ANTIPHEROMONE == true;
                    
                    if( AlgorithmParameters.MMAS_REDUCE_BY_HALF == true )
                    {
                        double rhoAntipheromoneMMAS = 0.5;

                        double probability = pheromoneTable.getProbabilityAt( from, to );
                        probability *= rhoAntipheromoneMMAS; 

                        if( probability < AlgorithmParameters.MMAS_PHEROMONE_MINIMUM_SD )
                        {
                            probability = AlgorithmParameters.MMAS_PHEROMONE_MINIMUM_SD;
                        }

                        pheromoneTable.setProbabilityAt( from, to, probability );
                        // 18 April 2012 symmetrical pheromone update 
                        pheromoneTable.setProbabilityAt( to, from, probability);
                    }
                    else // lay down the minimum pheromone
                    {
                        if( AlgorithmParameters.preventInterference == false ) // 21 August 2018
                        {
                            pheromoneTable.setProbabilityAt(from, to, AlgorithmParameters.MMAS_PHEROMONE_MINIMUM_SD );
                            // 18 April 2012 symmetrical pheromone update 
                            pheromoneTable.setProbabilityAt(to, from, AlgorithmParameters.MMAS_PHEROMONE_MINIMUM_SD );
                        }
                        else // we are preventing interference
                        {
                            if( bpm.bpMatrix[ from ][ to ] ==  0 )
                            {
                                pheromoneTable.setProbabilityAt(from, to, AlgorithmParameters.MMAS_PHEROMONE_MINIMUM_SD );
                                // symmetrical pheromone update 
                                pheromoneTable.setProbabilityAt(to, from, AlgorithmParameters.MMAS_PHEROMONE_MINIMUM_SD ); 
                            }
                            else
                            {
                                preventCounter++;
                            }
                        }
                    }
                }
                else
                {
                    assert false: "impossible algorithm parameter in antipheromone update";
                }
                
                // advance to next vertex
                from = to;
            }
            
            nodeCounter++;
            
        }   // end for each vertex in the solution path    
        
        // 21 August 2018, for testing...
//        if( preventCounter > 0 )
//        {
//            System.out.println( "nodes in path: " + nodeCounter );
//            System.out.println( "node interference prevented: " + preventCounter );
//            System.out.println( " " );
//        }
    }
  

    /**
     * calculate the delta for update
     * @param path
     * @param weights
     * @return delta
     */
    private static double calculateDelta( Path path )
    {
        assert path != null;
        double rawValue = 0.0;
        
        switch( AlgorithmParameters.fitness )
        {
            case AlgorithmParameters.CBO:
                rawValue = 1 - path.getCBO( );
                break;
                
            case AlgorithmParameters.NAC:
                 // NAC is already scaled (done when calculated)
                rawValue = 1 - path.getEleganceNAC( );
                break;
                
            case AlgorithmParameters.COMBINED:
                rawValue = 1 - path.getCombined( );
                break;
                
            case AlgorithmParameters.TSP_PATH_LENGTH:
                // see Dorigo & Stutzle, page 72
                // Delta is one over the total path cost
                rawValue = 1.0 / path.getTSPPathLength( );
                break;
                
            default:
                assert false : "impossible fitness parameter";
                break;               
        }  
     
        assert rawValue >= 0.0: "raw value is: " + rawValue;
        assert rawValue <= 1.0: "raw value is: " + rawValue;
     
        // calculate delta by raising the raw factor to the power of MU
        double delta = Math.pow(rawValue, AlgorithmParameters.mu ); 
        
        return delta;
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
     * perform an update of the pheromone table to effectively
     * freeze the class.
     * @param pheromone table
     * @param class to be frozen
     */
    public static void freezeUpdate( PheromoneMatrix pt, List< CLSClass > freezeList )
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
     * @param secondBestPathInColonyCombined as it says on the tin : ) 
     * @param thirdBestPathInColonyCombined again, as it says on the tin : ) 
     * @param best NAC
     * @param worst NAC
     * @param worst Path In Colony CBO
     * @param worst Path In Colony Combined
     * @param second worst Path in Colony Combined
     * @param third worst Path in Colony Combined
     * @param iteration of the search
     * @param bpm best paths matrix for this iteration
     */
    private static void performMMASUpdate( 
        List< Path > colony, 
        PheromoneMatrix pheromoneTable, 
        Path bestPathInColonyCBO,
        Path bestPathInColonyNAC,
        Path bestPathInColonyCombined,
        Path secondBestPathInColonyCombined,
        Path thirdBestPathInColonyCombined,
        Path worstPathInColonyCBO,
        Path worstPathInColonyNAC,
        Path worstPathInColonyCombined,
        Path secondWorstPathInColonyCombined,
        Path thirdWorstPathInColonyCombined,
        int iteration,
        BestPathsMatrix bpm,
        Path bestTSPPathInColony,
        Path secondBestTSPPathInColony,
        Path thirdBestTSPPathInColony,
        Path worstTSPPathInColony,
        Path secondWorstTSPPathInColony,
        Path thirdWorstTSPPathInColony )
    {
        assert colony != null; 
        assert pheromoneTable != null; 
        assert iteration >= 0;
        assert bpm != null;
        
        if( Parameters.problemNumber == Parameters.CBS ||
            Parameters.problemNumber == Parameters.GDP ||
            Parameters.problemNumber == Parameters.RANDOMISED ||
            Parameters.problemNumber == Parameters.SC )
        {
            assert bestPathInColonyCBO != null;
            assert bestPathInColonyNAC != null;
            assert bestPathInColonyCombined != null;
            assert secondBestPathInColonyCombined != null;
            assert thirdBestPathInColonyCombined != null;
            assert worstPathInColonyCBO != null;
            assert worstPathInColonyNAC != null;
            assert worstPathInColonyCombined != null;
            assert secondWorstPathInColonyCombined != null;
            assert thirdWorstPathInColonyCombined != null;
        }
        else if( Parameters.problemNumber == Parameters.TSP_BERLIN52 ||
                 Parameters.problemNumber == Parameters.TSP_ST70 ||
                 Parameters.problemNumber == Parameters.TSP_RAT99 ||
                 Parameters.problemNumber == Parameters.TSP_RAT195 )
        {
            assert bestTSPPathInColony != null; 
            assert secondBestTSPPathInColony != null; 
            assert thirdBestTSPPathInColony != null; 
            assert worstTSPPathInColony != null;
            assert secondWorstTSPPathInColony != null;
            assert thirdWorstTSPPathInColony != null;
        }
        else
        {
            assert false : "impossible fitness in performMMASUPdate( )";
        }
        
        if( AlgorithmParameters.fitness == AlgorithmParameters.CBO )    
        {
            layPheromoneForPath( bestPathInColonyCBO, pheromoneTable );
        }
        else if( AlgorithmParameters.fitness == AlgorithmParameters.NAC )
        {
            layPheromoneForPath( bestPathInColonyNAC, pheromoneTable );
        }
        else if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED ||
                 AlgorithmParameters.fitness == AlgorithmParameters.TSP_PATH_LENGTH )
        {
            assert AlgorithmParameters.pheromoneStrength <= AlgorithmParameters.MMAS_PHEROMONE_TRIPLE;
            assert AlgorithmParameters.pheromoneStrength >= AlgorithmParameters.MMAS_PHEROMONE_SINGLE;
                    
            switch( AlgorithmParameters.pheromoneStrength )
            {
                case AlgorithmParameters.MMAS_PHEROMONE_TRIPLE:
                    if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
                    {
                        layPheromoneForPath( thirdBestPathInColonyCombined, pheromoneTable );
                        bpm.recordPath( thirdBestPathInColonyCombined);
                    }
                    else if( AlgorithmParameters.fitness == AlgorithmParameters.TSP_PATH_LENGTH )
                    {
                        layPheromoneForPath( thirdBestTSPPathInColony, pheromoneTable );
                        bpm.recordPath( thirdBestTSPPathInColony );
                    }
                    else
                    {
                        assert false: "impossible fitness!";
                    }

                case AlgorithmParameters.MMAS_PHEROMONE_DOUBLE:
                    if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
                    {
                        layPheromoneForPath( secondBestPathInColonyCombined, pheromoneTable );
                        bpm.recordPath( secondBestPathInColonyCombined);
                    }
                    else if( AlgorithmParameters.fitness == AlgorithmParameters.TSP_PATH_LENGTH )
                    {
                        layPheromoneForPath( secondBestTSPPathInColony, pheromoneTable );
                        bpm.recordPath( secondBestTSPPathInColony );
                    }
                    else
                    {
                        assert false: "impossible fitness!";
                    }

                case AlgorithmParameters.MMAS_PHEROMONE_SINGLE:
                    if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
                    {
                        layPheromoneForPath( bestPathInColonyCombined, pheromoneTable );
                        bpm.recordPath( bestPathInColonyCombined );
                    }
                    else if( AlgorithmParameters.fitness == AlgorithmParameters.TSP_PATH_LENGTH )
                    {
                        layPheromoneForPath( bestTSPPathInColony, pheromoneTable );
                        bpm.recordPath( bestTSPPathInColony );
                    }
                    else
                    {
                        assert false: "impossible fitness!";
                    }
                    break;

                default:
                    assert false : "impossible antipheromone strength!";
                    break;
            }
        }
        else
        {
            assert true : "impossible fitness!";
        }
        
        // optionally perform antipheromone update
        if( AlgorithmParameters.MMAS_ANTIPHEROMONE == true )
        {
            assert iteration <= AlgorithmParameters.NUMBER_OF_ITERATIONS;
            double progress = (double) iteration / (double) AlgorithmParameters.NUMBER_OF_ITERATIONS;
            assert progress >= 0.0;
            assert progress <= 1.0;
            progress *= 100.0;
            assert progress >= 0.0;
            assert progress <= 100.0 : "Progress Percentage is: " + progress;
            final double temp = Math.floor( progress );
            final int progressPercentage = (int) temp;
            assert progressPercentage >= 0 : "Progress Percentage is: " + progressPercentage;
            assert progressPercentage <= 100 : "Progress Percentage is: " + progressPercentage;
            
            if( progressPercentage < AlgorithmParameters.antiPheromonePhasePercentage )
            {
                if( AlgorithmParameters.fitness == AlgorithmParameters.CBO )    
                {
                    layAntiPheromoneForPath( AlgorithmParameters.MMAS, worstPathInColonyCBO, pheromoneTable, bpm );
                }
                else if( AlgorithmParameters.fitness == AlgorithmParameters.NAC )
                {
                    layAntiPheromoneForPath( AlgorithmParameters.MMAS, worstPathInColonyNAC, pheromoneTable, bpm );
                }
                else if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED ||
                         AlgorithmParameters.fitness == AlgorithmParameters.TSP_PATH_LENGTH )
                {
                    assert AlgorithmParameters.antipheromoneStrength <= AlgorithmParameters.ANTIPHEROMONE_STRENGTH_TRIPLE;
                    assert AlgorithmParameters.antipheromoneStrength >= AlgorithmParameters.ANTIPHEROMONE_STRENGTH_SINGLE:
                            "antipheromone strength is: " + AlgorithmParameters.antipheromoneStrength; 
                    
                    switch( AlgorithmParameters.antipheromoneStrength )
                    {
                        case AlgorithmParameters.ANTIPHEROMONE_STRENGTH_TRIPLE:
                            if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
                            {
                                layAntiPheromoneForPath(
                                    AlgorithmParameters.MMAS, thirdWorstPathInColonyCombined, pheromoneTable, bpm );
                            }
                            else if( AlgorithmParameters.fitness == AlgorithmParameters.TSP_PATH_LENGTH )
                            {
                                layAntiPheromoneForPath( 
                                    AlgorithmParameters.MMAS, thirdWorstTSPPathInColony, pheromoneTable, bpm );
                            }
                            else
                            {
                                assert false;
                            }
                            
                        case AlgorithmParameters.ANTIPHEROMONE_STRENGTH_DOUBLE:
                            if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
                            {
                                layAntiPheromoneForPath( 
                                    AlgorithmParameters.MMAS, secondWorstPathInColonyCombined, pheromoneTable, bpm );
                            }
                            else if( AlgorithmParameters.fitness == AlgorithmParameters.TSP_PATH_LENGTH )
                            {
                                layAntiPheromoneForPath( 
                                    AlgorithmParameters.MMAS, secondWorstTSPPathInColony, pheromoneTable, bpm );
                            }
                        
                        case AlgorithmParameters.ANTIPHEROMONE_STRENGTH_SINGLE:
                            if( AlgorithmParameters.fitness == AlgorithmParameters.COMBINED )
                            {
                                layAntiPheromoneForPath( 
                                    AlgorithmParameters.MMAS, worstPathInColonyCombined, pheromoneTable, bpm );
                            }
                            else if( AlgorithmParameters.fitness == AlgorithmParameters.TSP_PATH_LENGTH )
                            {
                                layAntiPheromoneForPath( 
                                    AlgorithmParameters.MMAS, worstTSPPathInColony, pheromoneTable, bpm );
                            }
                            else
                            {
                                assert false : "impossible fitness in antipheromone update!";
                            }
                            break;
                            
                        default:
                            assert false : "impossible antipheromone strength!";
                            break;
                    }
                }
                else
                {
                    assert false : "impossible single objective update parameter";
                }
            }
        }
    }
        
}   // end class

//------- end file ----------------------------------------


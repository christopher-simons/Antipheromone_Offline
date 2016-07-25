/*
 * EditAlgorithmParametersDialog.java
 */
package myGui;

import config.AlgorithmParameters;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author cl-simons
 */
public class EditAlgorithmParametersDialog extends JDialog
{
    private JRadioButton SACORadioButton;
    private JRadioButton MMASRadioButton;
    
    private JCheckBox CBOCheckBox;
    private JCheckBox NACCheckBox;
    private JCheckBox ATMRCheckBox;
    
    private JCheckBox evaporationCheckBox;
    private JCheckBox replaceCheckBox;
    
    private JCheckBox chCheckBox;
    
    private JButton okButton;
    private JButton cancelButton;
    
    public EditAlgorithmParametersDialog( )
    {
        super( (JFrame) null, // parent component
                "Algorithm Parameters", // dialog title
                true ); // true for modal, false for modeless
        
        setSize( 225, 335 );
        setLocation( 300, 300 );
        setUpComponents( );
    }
    
    private void setUpComponents( )
    {
         // set up the algorithm panel
        JPanel algorithmPanel = new JPanel( );
        algorithmPanel.setBorder(
            BorderFactory.createTitledBorder( "Algorithm" ) );
        algorithmPanel.setBackground( Color.white );
       
        SACORadioButton = new JRadioButton( "S-ACO" );
        SACORadioButton.setBackground( Color.white );
        
        MMASRadioButton = new JRadioButton( "MMAS" );
        MMASRadioButton.setBackground( Color.white );
        
        if( AlgorithmParameters.algorithm == AlgorithmParameters.SIMPLE_ACO )
        {
            SACORadioButton.setSelected( true );
        }
        else // must be MMAS
        {
            MMASRadioButton.setSelected( true );
        }
        
        ButtonGroup algorithmGroup = new ButtonGroup( );
        algorithmGroup.add( SACORadioButton );
        algorithmGroup.add( MMASRadioButton );
        
        algorithmPanel.add( SACORadioButton );
        algorithmPanel.add( MMASRadioButton );
        
        // set up the objectives panel
        JPanel objectivesPanel = new JPanel( );
        objectivesPanel.setBorder(
            BorderFactory.createTitledBorder( "Objectives" ) );
        objectivesPanel.setBackground( Color.white );
        
        CBOCheckBox = new JCheckBox( "CBO" );
        CBOCheckBox.setBackground( Color.white );
        
        if( AlgorithmParameters.objectiveCBO == true )
        {
            CBOCheckBox.setSelected( true );
        }
        
        NACCheckBox = new JCheckBox( "NAC" );
        NACCheckBox.setBackground( Color.white );
        
        if( AlgorithmParameters.objectiveNAC == true )
        {
            NACCheckBox.setSelected( true );
        }
        
        ATMRCheckBox = new JCheckBox( "ATMR" );
        ATMRCheckBox.setBackground( Color.white );
        
        if( AlgorithmParameters.objectiveATMR == true )
        {
            ATMRCheckBox.setSelected( true );
        }
        
        objectivesPanel.add( CBOCheckBox );
        objectivesPanel.add( NACCheckBox );
        objectivesPanel.add( ATMRCheckBox );
        
        
        // set up the elitism panel
        JPanel elitismPanel = new JPanel( );
        elitismPanel.setBorder(
            BorderFactory.createTitledBorder( "Elitism" ) );
        elitismPanel.setBackground( Color.white );
        
        evaporationCheckBox = new JCheckBox( "evaporation" );
        evaporationCheckBox.setBackground( Color.white );
        
        if( AlgorithmParameters.evaporationElitism == true )
        {
            evaporationCheckBox.setSelected( true );
        }
        
        replaceCheckBox = new JCheckBox( "replace" );
        replaceCheckBox.setBackground( Color.white );
        
        if( AlgorithmParameters.replacementElitism == true )
        {
            replaceCheckBox.setSelected( true );
        }
        
        elitismPanel.add( evaporationCheckBox );
        elitismPanel.add( replaceCheckBox );
        
        // set up the constraint handling panel 
        JPanel constraintHandlingPanel = new JPanel( );
        constraintHandlingPanel.setBorder(
            BorderFactory.createTitledBorder( "Constraint Handling" ) );
        constraintHandlingPanel.setBackground( Color.white );
        chCheckBox = new JCheckBox( "handle constraints" );
        chCheckBox.setBackground( Color.white );
        
        if( AlgorithmParameters.constraintHandling == true )
        {
            chCheckBox.setSelected( true );
        }
        
        constraintHandlingPanel.add( chCheckBox );
                
        // set up button panel
        JPanel bottomPanel = new JPanel( new FlowLayout(  ) );
        bottomPanel.setBackground( Color.white );
        okButton = new JButton( "OK" );
        ButtonListener buttonListener = new ButtonListener( );
        okButton.addActionListener( buttonListener );
        
        cancelButton = new JButton( "Cancel" );
        cancelButton.addActionListener( buttonListener );
        
        bottomPanel.add( okButton );
        bottomPanel.add( cancelButton );
        
        // add sub-components to dialog
        Container contentPane = getContentPane( );
        contentPane.setBackground( Color.white );
        contentPane.setLayout( new FlowLayout( ) );
        contentPane.add( algorithmPanel );
        contentPane.add( objectivesPanel );
        contentPane.add( elitismPanel );
        contentPane.add( constraintHandlingPanel );
        contentPane.add( bottomPanel );  
    }
    
    
    private class ButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent event )
        {
            JButton source = (JButton) event.getSource( ); 
            
            if( source == okButton )
            {
                setUpAlgorithmParameters( );
                setVisible( false );
            }
            else if( source == cancelButton )
            {
                setVisible( false );
            }
            else
            {
                //  must be impossible button press event???
                assert false : "impossible button press event";
            }
        }
    }
       
    private void setUpAlgorithmParameters( )
    {
        if( this.SACORadioButton.isSelected( ) )
        {
            AlgorithmParameters.algorithm = AlgorithmParameters.SIMPLE_ACO;
            AlgorithmParameters.RHO = AlgorithmParameters.SimpleACO_RHO;
            System.out.println( "rho is: "  + AlgorithmParameters.RHO );
        }
        else // must be MMAS button selected
        {
            AlgorithmParameters.algorithm = AlgorithmParameters.MMAS;
            AlgorithmParameters.RHO = AlgorithmParameters.MMAS_RHO;
            System.out.println( "rho is: "  + AlgorithmParameters.RHO );
        }
        
        //----------------------------------------------------
        
        if( this.CBOCheckBox.isSelected( ) )
        {
            AlgorithmParameters.objectiveCBO = true;
        }
        else
        {
            AlgorithmParameters.objectiveCBO = false;
        }
        
        //----------------------------------------------------
        
        if( this.NACCheckBox.isSelected( ) )
        {
            AlgorithmParameters.objectiveNAC = true;
        }
        else
        {
            AlgorithmParameters.objectiveNAC = false;
        }
        
        //----------------------------------------------------
        
        if( this.ATMRCheckBox.isSelected( ) )
        {
            AlgorithmParameters.objectiveATMR = true;
        }
        else
        {
            AlgorithmParameters.objectiveATMR = false;
        }
        
        //----------------------------------------------------
        
        if( this.evaporationCheckBox.isSelected( ) )
        {
            AlgorithmParameters.evaporationElitism = true;
        }
        else
        {
            AlgorithmParameters.evaporationElitism = false;
        }
        //-----------------------------------------------------
        
        if( this.replaceCheckBox.isSelected( ) )
        {
            AlgorithmParameters.replacementElitism = true;
        }
        else
        {
            AlgorithmParameters.replacementElitism = false;
        }
        
        //-----------------------------------------------------
        
        if( this.chCheckBox.isSelected( ) )
        {
            AlgorithmParameters.constraintHandling = true; 
        }
        else
        {
            AlgorithmParameters.constraintHandling = false;
        }
    }

}   // end class

//------------------- end of file ------------------------------------

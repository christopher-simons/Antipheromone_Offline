/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myGui;


/**
 *
 * @author cl-simons
 */


import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import learning.IterationInformation;
import softwareDesign.CLSClass;


public class ViewUnfreezeFrozenDialog extends JDialog
{
    private List< CLSClass > frozenClassList;
    private int numberOfFrozenClasses;
    
    private IterationInformation information;
    
    private JCheckBox[ ] checkBoxes;
    
    private JLabel label;
    
    JButton unfreezeButton;
    JButton cancelButton;
    
    
    public ViewUnfreezeFrozenDialog( 
        int start_x, 
        int start_y,
        List< CLSClass > frozenClassList,
        IterationInformation information )
    {
        super( (JFrame) null, // parent component
            "View/Unfreeze Frozen Classes Dialog", // dialog title
            true ); // true for modal, false for modeless
        
        assert frozenClassList != null;
        this.frozenClassList = frozenClassList;
        numberOfFrozenClasses = frozenClassList.size( );
        
        assert information != null;
        this.information = information;
    
        checkBoxes = new JCheckBox[ numberOfFrozenClasses ];
        
        setLocation( start_x, start_y );
        int ySize = 100 + ( numberOfFrozenClasses * 40 );
        setSize( 400, ySize );
        setUpGraphics( );
    }
    
    private void setUpGraphics( )
    {
        JPanel checkPanel = null;
        
         if( numberOfFrozenClasses == 0 )
        {
            label = new JLabel( "no classes frozen!" );
        }
        else
        {
            BoxListener boxListener = new BoxListener(  );
            
            for( int i = 0; i < numberOfFrozenClasses; i++ )
            {
                checkBoxes[ i ] = new JCheckBox( this.frozenClassList.get( i ).toString( ) );
                checkBoxes[ i ].addItemListener( boxListener );
            } 

            checkPanel = new JPanel( 
                new GridLayout( numberOfFrozenClasses, 1 ) );
            checkPanel.setBorder(
                BorderFactory.createTitledBorder( "Frozen classes" ) );

            for( int j = 0; j < numberOfFrozenClasses; j++ )
            {
                checkPanel.add( checkBoxes[ j ] );
            }
        }
         
         
        ButtonListener buttonListener = new ButtonListener( );
        
        unfreezeButton = new JButton( "Unfreeze" );
        cancelButton = new JButton( "Cancel" );
        cancelButton.addActionListener( buttonListener );

        JPanel buttonPanel = new JPanel( new FlowLayout( ) );
        if( numberOfFrozenClasses > 0 )
        {
            unfreezeButton.addActionListener( buttonListener );
            unfreezeButton.setEnabled( false );
            buttonPanel.add( unfreezeButton );
        }
        buttonPanel.add( cancelButton );
        
               
        Container contentPane = getContentPane( );
        contentPane.setBackground( Color.white );
        contentPane.setLayout( new BorderLayout( ) );
        if( numberOfFrozenClasses > 0 )
        {
            contentPane.add( checkPanel, BorderLayout.CENTER );
        }
        else
        {
            contentPane.add( label );
        }
        
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
    }
    
    private class BoxListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent e) 
        {
//            Object source = e.getItemSelectable( );
//            
//            for( int i = 0; i < checkBoxes.length; i++ )
//            {
//                if( source == checkBoxes[ i ] )
//                {
//                    // etc. etc.
//                }
//            }
            
            unfreezeButton.setEnabled( true );
        }
    }
    
     /** 
     * the listener for the buttons, implemented as a nested class
     */
    private class ButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent event )
        {
            JButton source = (JButton) event.getSource( ); 
            
            if( source == unfreezeButton )
            {
                doUnfreeze( );
                
                // after done button click, hide the dialog
                setVisible( false );
            }
            else if( source == cancelButton )
            {
                System.out.println( "cancel button clicked" );
                // after cancel button click, hide the dialog
                setVisible( false );
            }
            else
            {
                assert false : "impossible button press event";
            }
        }
    }

    private void doUnfreeze( )
    {
        for( int i = numberOfFrozenClasses - 1; i >= 0; i-- )
        {
            if( checkBoxes[ i ].isSelected( ) )
            {
                frozenClassList.remove( i );
                information.classUnfrozen = true;
            }
        }
    }
    
}   // end class

//------ end of file --------------------------------------

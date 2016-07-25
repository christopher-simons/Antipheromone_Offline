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
import javax.swing.*;



public class FreezeDialog extends JDialog 
{
    public static final int FREEZE = 0;
    public static final int UNFREEZE = 1;
    
    private JRadioButton onRadioButton;
    private JRadioButton offRadioButton;
    
    private JButton okButton;
    private JButton cancelButton;
    
    private int userAction;
    private int intention; 
            
    public FreezeDialog( int start_x, int start_y )
    {
        super( (JFrame) null, // parent component
        "Freeze Dialog", // dialog title
        true ); // true for modal, false for modeless
        
        setLocation( start_x, start_y );
        setSize( 150, 100 );
        setUndecorated( true );
        setupGraphics( );
        
        userAction = intention = -99; // something extraordinary
    }
    
    private void setupGraphics( )
    {
        // set up the freezing panel
        onRadioButton = new JRadioButton( "Yes" );
        onRadioButton.setBackground( Color.white );
        onRadioButton.setSelected( true );
        offRadioButton = new JRadioButton( "No" );
        offRadioButton.setBackground( Color.white );
        
        ButtonGroup freezeGroup = new ButtonGroup( );
        freezeGroup.add( onRadioButton );
        freezeGroup.add( offRadioButton );
        
        JPanel freezePanel = new JPanel( new FlowLayout( ) );
        freezePanel.setBorder(
            BorderFactory.createTitledBorder( "Freeze this class?" ) );
        freezePanel.setBackground( Color.white );
        freezePanel.add( onRadioButton );
        freezePanel.add( offRadioButton );
        
        okButton = new JButton( "OK" );
        ButtonListener buttonListener = new ButtonListener( );
        okButton.addActionListener( buttonListener );
        
        cancelButton = new JButton( "Cancel" );
        cancelButton.addActionListener( buttonListener );

        // set up the button panel
        JPanel buttonPanel = new JPanel( );
        buttonPanel.setLayout( new FlowLayout( ) );
        buttonPanel.setBackground( Color.white );
        buttonPanel.add( okButton );
        buttonPanel.add( cancelButton );

        Container contentPane = getContentPane( );
        contentPane.setLayout( new FlowLayout( ) );
        contentPane.setBackground( Color.WHITE );
        
        contentPane.add( freezePanel );
        contentPane.add( buttonPanel  );
    }
    
    /** 
     * the listener for the buttons, implemented as a nested class
     * the run-time parameters are set here
     */
    private class ButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent event )
        {
            JButton source = (JButton) event.getSource( ); 
            
            if( source == okButton )
            {
                userAction = JOptionPane.OK_OPTION;
                
                if( onRadioButton.isSelected( ) == true )
                {
                    intention = FREEZE;
                }
                else
                {
                    intention = UNFREEZE;
                }
                // after done button click, hide the dialog
                setVisible( false );
            }
            else if( source == cancelButton )
            {
                userAction = JOptionPane.CANCEL_OPTION;
                // after cancel button click, hide the dialog
                setVisible( false );
            }
            else
            {
                assert false : "impossible button press event";
            }
        }
    }

    /**
     * get the user action 
     * @return OK_OPTION or CANCEL_OPTION
     */
    public int getUserAction( )
    {
        return userAction;
    }
    
    /**
     * get the intention of the dialog
     * @return zero if intend to freeze, one otherwise
     */
    public int getIntention( )
    {
        return intention;
    }
    
    
}   // end class

//---------- end of file -----------------------------------

/*
 * ACOFrame.java
 * Created 14 June 2012
 */

package myGui;

/**
 *
 * @author cl-simons
 */

import config.*;
import engine.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import problem.ProblemController;


public class ACOFrame extends JFrame
{
    private JLabel pathLabel;
    private JComboBox< String > designerCombo;
    private JComboBox< String > designProblemCombo;
    private JComboBox< String > episodeNumberCombo;

    private JRadioButton freezeOnRadioButton;
    private JRadioButton freezeOffRadioButton;
    
    private JRadioButton trafficLightRadioButton;
    private JRadioButton waterTapRadioButton;
    
    private JButton defaultPathButton;
    private JButton pathButton;
    private JButton interactiveButton;
    private JButton batchButton;
    private JButton configButton;
    private JButton exitButton;

    private String[ ] designers =
    {
        "0 - Martin",
        "1 - Stewart", 
        "2 - Jane", 
        "3 - Larry", 
        "4 - Jim", 
        "5 - Julia",
        "6 - Barry",
        "7 - Andy",
        "8 - Will",
        "9 - Delia",
        "10 - Chris",
        "11 - Neil"
    };
    
    private String[ ] designProblems = 
    {
        "Test (randomised)",
        "Cinema Booking System",
        "Graduate Development Program",
        "Select Cruises"
    };
        
    private String[ ] episodes =
    {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
    };
    
    private String path = "";    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        ACOFrame frame = new ACOFrame( );
        frame.setVisible( true );
    }   

    /**
     * Constructor
     */
    public ACOFrame( )
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch( ClassNotFoundException | 
               InstantiationException | 
               IllegalAccessException | 
               UnsupportedLookAndFeelException ex )
        {
            Logger.getLogger(ACOFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        setTitle( "Software Design with ACO" );
        setSize( 325, 640 );
        setResizable( false );
        setLocation( 100, 200 );
        setDefaultCloseOperation( EXIT_ON_CLOSE );

        setUpGui( );
    }
    
    
     /**
     * set up the graphical user interface
     */
    private void setUpGui( )
    {
        // set up designer combo
        
        designerCombo = new JComboBox< >( designers );
        JPanel designerPanel = new JPanel( );
        designerPanel.setLayout( new FlowLayout( ) );
        designerPanel.setBorder(
            BorderFactory.createTitledBorder( "Designer" ) );
        designerPanel.setBackground( Color.white );
        designerPanel.add( designerCombo );

         // set up design problem combo
                
        designProblemCombo = new JComboBox< >( designProblems );
        JPanel designProblemPanel = new JPanel( );
        designProblemPanel.setLayout( new FlowLayout( ) );
        designProblemPanel.setBorder( 
            BorderFactory.createTitledBorder( "Design Problem" ) );
        designProblemPanel.setBackground( Color.white );
        designProblemPanel.add( designProblemCombo );

         // set up episode number combo
        episodeNumberCombo = new JComboBox< >( episodes );
  
        JPanel episodeNumberPanel = new JPanel( );
        episodeNumberPanel.setLayout( new FlowLayout( ) );
        episodeNumberPanel.setBorder( 
            BorderFactory.createTitledBorder( "Episode Number" ) );
        episodeNumberPanel.setBackground( Color.white );
        episodeNumberPanel.add( episodeNumberCombo );

        // set up the freezing panel
        freezeOffRadioButton = new JRadioButton( "OFF" );
        freezeOffRadioButton.setBackground( Color.white );
        freezeOffRadioButton.setSelected( true );
        freezeOnRadioButton = new JRadioButton( "ON" );
        freezeOnRadioButton.setBackground( Color.white );
        
        ButtonGroup freezeGroup = new ButtonGroup( );
        freezeGroup.add( freezeOffRadioButton );
        freezeGroup.add( freezeOnRadioButton );
        
        JPanel freezingPanel = new JPanel( new FlowLayout( ) );
        freezingPanel.setBorder(
            BorderFactory.createTitledBorder( "Hints: freezing & Archiving" ) );
        freezingPanel.setBackground( Color.white );
        freezingPanel.add( freezeOffRadioButton );
        freezingPanel.add( freezeOnRadioButton );
        
        // set up the colour metaphor panel
        trafficLightRadioButton = new JRadioButton( "Traffic Lights" );
        trafficLightRadioButton.setBackground( Color.white );
        trafficLightRadioButton.setSelected( true );
        waterTapRadioButton = new JRadioButton( "Water Tap" );
        waterTapRadioButton.setBackground( Color.white );
        
        ButtonGroup colourGroup = new ButtonGroup( );
        colourGroup.add( trafficLightRadioButton );
        colourGroup.add( waterTapRadioButton );
        
        JPanel colourPanel = new JPanel( new FlowLayout( ) );
        colourPanel.setBorder(
            BorderFactory.createTitledBorder( "Colour Metaphor" ) );
        colourPanel.setBackground( Color.white );
        colourPanel.add( trafficLightRadioButton );
        colourPanel.add( waterTapRadioButton );
        
        // set up the path panel
        JPanel pathPanel = new JPanel( );
        pathPanel.setLayout( new FlowLayout( ) );
        pathPanel.setBorder(
            BorderFactory.createTitledBorder( "Select Output File(s) Path" ) );
        pathPanel.setBackground( Color.white );
        
        defaultPathButton = new JButton( "Default Path" );
        ButtonListener buttonListener = new ButtonListener( );
        defaultPathButton.addActionListener( buttonListener );
        
        pathButton = new JButton( "Select Path..." );
        pathButton.addActionListener( buttonListener );
        
        pathLabel = new JLabel( " " );
               
        pathPanel.add( defaultPathButton );
        pathPanel.add( pathButton );
        pathPanel.add( pathLabel );
        
        // set up the "go" button and "exit" button designPanel
        JPanel designPanel = new JPanel( );
        designPanel.setLayout( new GridLayout( 2, 1 ) );
        designPanel.setBackground( Color.white );

        JPanel spacer1 = new JPanel( );
        spacer1.setBackground( Color.white );
        JPanel spacer2 = new JPanel( );
        spacer2.setBackground( Color.white );
        
        String s = "<HTML><font color='#0000FF'>GO</font></HTML>";
        interactiveButton = new JButton( s );
        interactiveButton.addActionListener( buttonListener );
        
        configButton = new JButton( "config" );
        configButton.addActionListener( buttonListener );
        configButton.setEnabled( false );
        
        batchButton= new JButton( "batch" );
        batchButton.addActionListener( buttonListener );
        batchButton.setEnabled( true );
        
        exitButton = new JButton( "exit" );
        exitButton.addActionListener( buttonListener );

        // set up the button panel
        JPanel buttonPanel = new JPanel( );
        buttonPanel.setLayout( new FlowLayout( ) );
        buttonPanel.setBackground( Color.white );
        buttonPanel.add( interactiveButton );
        buttonPanel.add( configButton );
        buttonPanel.add( batchButton );
        buttonPanel.add( exitButton );

        designPanel.add( spacer1 );
        designPanel.add( buttonPanel );
//        designPanel.add( spacer2 );
        
        // add the panels to the frame
        Container contentPane = getContentPane( );
        contentPane.setLayout( new GridLayout( 7, 1 ) );
        contentPane.setBackground( Color.white );
        
        contentPane.add( designerPanel );
        contentPane.add( designProblemPanel );
        contentPane.add( episodeNumberPanel );
        contentPane.add( freezingPanel );
        contentPane.add( colourPanel );
        contentPane.add( pathPanel );
        contentPane.add( designPanel );
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
            
            if( source == defaultPathButton )
            {
                path = "W:\\Research\\Experimentation2013Q2\\temp";
                String s = "<HTML><font color='#0000FF'>";
                s += path;
                s += "</font></HTML>";
                pathLabel.setText( s );
            }
            else if( source == pathButton )
            {
                path = selectPath( );
                assert path.length( ) > 0;
//                pathButton.setEnabled( false );
                String s = "<HTML><font color='#0000FF'>";
                s += path;
                s += "</font></HTML>";
                pathLabel.setText( s );
            }
            else if( source == configButton )
            {
                EditAlgorithmParametersDialog editAlgorithmParametersDialog =
                    new EditAlgorithmParametersDialog( );
                editAlgorithmParametersDialog.setVisible( true );
            }
            else if( source == batchButton )
            {
                if( path.length( ) == 0 )
                {
                    JOptionPane.showMessageDialog( null, "Output file path not selected!" );
                    return;
                }
                batchButton.setEnabled( false );
                doBatchACO( );
            }
            else if( source == interactiveButton )
            {
                if( path.length( ) == 0 )
                {
                    JOptionPane.showMessageDialog( null, "Output file path not selected!" );
                    return;
                }
                
                interactiveButton.setEnabled( false );
                doInteractiveACO( );
            }
            else if( source == exitButton )
            {
                System.exit( 0 );
            }
            else
            {
                //  must be impossible button press event???
                assert false : "impossible button press event";
            }
        }
        
        /**
         * select path for output file(s)
         * @return results of selecting path via JFileChooser
         */
        private String selectPath( )
        {
            System.out.println( "path button clicked" );
            JFileChooser chooser = new JFileChooser( );
            chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
            String path = "";

            final int outcome = chooser.showOpenDialog( null ); // ex this
            if( outcome == JFileChooser.APPROVE_OPTION )
            {
                File directory = chooser.getCurrentDirectory( );
                File file = chooser.getSelectedFile( );

                path = file.getPath( );

//                String s = "directory name: ";
//                s += directory.getName( );
//                s += ", file name: ";
//                s += file.getName( );
//                s += ", path: ";
//                s += file.getPath( );
//                System.out.println( s );
            }
            
            return path;
        }
    
    }   // end class ButtonListener
    
    
    
    /**
     * 18 June 2012 do Interactive ACO
     */
    private void doInteractiveACO( )
    {
        setUpParameters( );
        
        ProblemController problemController = setUpProblemController( );
      
        // construct an interactive ACO search controller...
        Controller controller = new Controller( problemController );
       
        controller.run( 0 );    
    }
    
    /**
     * 31 August 2012 do the ACO search in batch mode
     */
    private void doBatchACO( )
    {
        setUpParameters( );
        
        ProblemController problemController = setUpProblemController( );
        
        Controller controller = new Controller( problemController );
        
        for( int i = 0; i < Parameters.NUMBER_OF_RUNS; i++ )
        {
           //controller.run( i );    // COMMENTED OUT FOR WEB SERVICE
        }
        
        controller.writeBatchResultsToFile( );
        System.out.println( "batch ACO complete" );
    }
    
    /**
     * set up the parameters based on user information
     */
    private void setUpParameters( )
    {
        Parameters.designer = designerCombo.getSelectedIndex( );
        System.out.println( "Designer number is: " + Parameters.designer );
        assert Parameters.designer >= 0;
        assert Parameters.designer <= Parameters.NUMBER_OF_DESIGNERS;

        Parameters.problemNumber = designProblemCombo.getSelectedIndex( );
        System.out.println( "Problem selected is:" + Parameters.problemNumber );
        assert Parameters.problemNumber >= 0;
        assert Parameters.problemNumber <= Parameters.NUMBER_OF_PROBLEMS;

        Parameters.episodeNumber = episodeNumberCombo.getSelectedIndex( ) + 1;
        System.out.println( "Episode number is:" + Parameters.episodeNumber );
        assert Parameters.episodeNumber > 0;

        Parameters.freezing = freezeOnRadioButton.isSelected( );
        System.out.println( "Freezing is: " + Parameters.freezing );

        if( trafficLightRadioButton.isSelected( ) )
        {
            Parameters.colourMetaphor = Parameters.TRAFFIC_LIGHTS;
            System.out.println( "Colour metaphor is traffic lights" );
        }
        else
        {
            Parameters.colourMetaphor = Parameters.WATER_TAP;
            System.out.println( "Colour Metaphor is water tap" );
        }

        assert path != null;
        Parameters.outputFilePath = path;
        System.out.println( "selected path for output files is: " + path );
    }
    
    /**
     * set up the problem controller based on the design problem
     * @return problem controller
     */
    public ProblemController setUpProblemController( )
    {
        ProblemController problemController = new ProblemController( );
        
        if( Parameters.problemNumber == 0 ) // test
        {
            problemController.createDesignProblem8( ); // randomised
            problemController.setNumberOfClasses( 7 );
            problemController.generateUseMatrix( );
        }
        else if( Parameters.problemNumber == 1 ) // Cinema Booking System
        {
            problemController.createDesignProblem5( );
            problemController.setNumberOfClasses( 5 );
            problemController.generateUseMatrix( );
//            problemController.showActionsAndData( );
        }
        else if( Parameters.problemNumber == 2 ) // GDP
        {
            problemController.createDesignProblem7( );
            problemController.setNumberOfClasses( 5 );
            problemController.generateUseMatrix( );
        }
        else if( Parameters.problemNumber == 3 ) // SC
        {
            problemController.createDesignProblem6( );
            problemController.setNumberOfClasses( 16 );
            problemController.generateUseMatrix( );
        }
        else
        {
            String message = "impossible design problem!!";
            message += " problem number is : ";
            message += Integer.toString( Parameters.problemNumber );
        
            assert false : message;
        }       
        
        return problemController;
    }
    
    
}   // end class

//--------- end of file -------------------------------------------

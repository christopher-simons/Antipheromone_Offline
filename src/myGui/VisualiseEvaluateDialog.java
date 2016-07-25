/**
 * VisualiseEvaluateDialog.java
 *
 * Created on 12 February 2007, 16:47 ported for Elegance Research, 9 March 2010
 * reworked extensively for Interactive ACO, July 2012
 */
package myGui;

/**
 * @author clsimons
 */
import config.Parameters;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.text.DecimalFormat;
import org.jgraph.*;
import org.jgraph.graph.*;
import com.jgraph.layout.*;
import com.jgraph.layout.graph.JGraphSimpleLayout;
//import com.jgraph.layout.organic.*;
//import com.jgraph.layout.graph.*;
//import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;
import daemonActions.DaemonOperators;
import engine.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
// 6 Nov 2015 import javax.servlet.http.HttpServlet;
import learning.IterationInformation;
import problem.*;
import softwareDesign.*;

public class VisualiseEvaluateDialog extends JDialog {

    private static final int START_X = 475;
    private static final int START_Y = 25;

    private static final int NON_EXISTENT = -1;

    private static final int EVAL_SLIDER_MIN = 0;
    private static final int EVAL_SLIDER_MAX = 100;
    private static final int EVAL_SLIDER_INIT = 50;

    private SortedMap< String, List< CLSDatum>> useTable;

    private EleganceDesign eleganceDesign;

    private List< CLSClass> classList;

    private List< Couple> coupleList;

    private List< CLSClass> freezeList;

    private List< EleganceDesign> archive;

    private IterationInformation information;

    private int numberOfNodes;
    private int numberOfEdges;
    private int numberOfNodesAndEdges;

    /**
     * the cells, can be either a vertex (node) or an edge
     */
    private DefaultGraphCell[] cells;

    /**
     * the model - the "M" structure in MVC
     */
    private GraphModel model;

    /**
     * the view - the "V" in MVC
     */
    private GraphLayoutCache view;

    /**
     * the graph - the controller on MVC?
     */
    private JGraph graph;

    private JSlider evalSlider;

    private JButton nextButton;
    private JButton viewfreezeListButton;
    private JButton viewArchiveButton;
    private JButton finishedButton;

    private int userAction;     // what button is clicked

    private DecimalFormat df;

    /**
     * constructor
     *
     * @param design for visualization
     * @param design name as String
     * @param use table
     * @param list of classes that user elects to "freeze"
     * @param iteration
     * @param iteraction counter
     * @param mean average percentage error
     * @param archive
     */
    public VisualiseEvaluateDialog(
            Path path,
            String designName,
            SortedMap< String, List< CLSDatum>> useTable,
            List< CLSClass> freezeList,
            int iteration,
            int interactionCounter,
            IterationInformation information,
            List< EleganceDesign> archive) {
        super((JFrame) null, // parent component
                "Class Design Evaluation", // dialog title
                true); // true for modal, false for modeless

        assert path != null;
        assert designName != null;
        assert useTable != null;
        this.useTable = useTable;
        assert freezeList != null;
        this.freezeList = freezeList;
        assert information != null;
        this.information = information;
        assert archive != null;
        this.archive = archive;

        numberOfNodes = 0;
        numberOfEdges = 0;
        numberOfNodesAndEdges = 0;
        df = new DecimalFormat("0.000");

//        String s1 = "";
//        if( designName.equals( "" ) == false )
//        {
//            s1 += "Design name: " + designName;
//        }
//        final double coupling = path.getCBO( );
//        df = new DecimalFormat( "0.000" );
//        s1 += " ( CBO Coupling = ";
//        s1 += df.format( coupling );
//        s1 += ")";
//        setTitle( s1 ); // 12 May 2010, make this a model dialog, so title passed to super
        // set up the dimensions of the frame
        setUpFrame();

        // prepare design display information
        eleganceDesign = DaemonOperators.constructDesignFromPath(path);
        eleganceDesign.calculateCOMFitness(useTable);
        classList = eleganceDesign.getClassList();

        // now build up a list of all couples
        coupleList = new ArrayList< Couple>();
        calculateCoupling(useTable);

        // set up the graphical display
        setUpGraphics(path, iteration, interactionCounter, information);

        userAction = -99; // something extraordinary

        if (Parameters.problemNumber == Parameters.TEST
                || Parameters.problemNumber == Parameters.CBS) {
            pack();
        }

        boolean result = evalSlider.requestFocusInWindow();
//        System.out.println("result is: " + result );
    }


    /**
     * set up the dimensions of the frame
     */
    private void setUpFrame() {
        setLocation(START_X, START_Y);

        int frameHeight = 0;
        int frameWidth = 0;

        switch (Parameters.problemNumber) {
            case Parameters.TEST:
                frameHeight = 825;
                frameWidth = 780;
                break;

            case Parameters.CBS:
                frameHeight = 825;
                frameWidth = 900;
                break;

            case Parameters.GDP:
                frameHeight = 925;
                frameWidth = 825;
                break;

            case Parameters.SC:
                frameHeight = 945;
                frameWidth = 1150;
                break;

            default:
                assert false : "impossible problem number";
                break;
        }
        setSize(frameWidth, frameHeight);
    }

    /**
     * set up the graphics for the design
     *
     * @param design for display
     * @param iteration
     * @param interactionCounter
     * @param iteration information
     */
    private void setUpGraphics(
            Path design,
            int iteration,
            int interactionCounter,
            IterationInformation information) {
        assert design != null;
        assert information != null;

        model = new DefaultGraphModel();
        view = new GraphLayoutCache(model, new DefaultCellViewFactory());
        graph = new JGraph(model, view);
//        graph.setGridEnabled( true ); // 10 May 2010
        graph.setGridSize(20.0);    // 10 May 2010
        graph.setGridVisible(true); // 10 May 2010

        // 5 July 2012
//        graph.addGraphSelectionListener( new MySelectionListener( ) );
        graph.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int buttonClick = e.getButton();

                if (buttonClick == MouseEvent.BUTTON1) {
//                    System.out.println( "LEFT Mouse Button (1) clicked: " + e );
                } else if (buttonClick == MouseEvent.BUTTON2) {
//                    System.out.println( "Mouse Button 2 clicked: " + e );
                } else if (buttonClick == MouseEvent.BUTTON3) {
//                    System.out.println( "RIGHT Mouse Button 3 clicked: " + e );

                    handleRightMouseClick(e);
                }
            }

            // to check for double click, try
            // if( e.getClickCount( ) == 2 ) etc.
            @Override
            public void mousePressed(MouseEvent e) {
//                System.out.println( "Mouse pressed: " + e );
            }

            @Override
            public void mouseReleased(MouseEvent e) {
//                System.out.println( "Mouse released: " + e );
            }

            @Override
            public void mouseEntered(MouseEvent e) {
//                System.out.println("Mouse entered: " + e );
            }

            @Override
            public void mouseExited(MouseEvent e) {
//                System.out.println("Mouse exited: " + e );
            }
        });

        this.numberOfNodes = classList.size();
        assert this.numberOfNodes > 0;
        this.numberOfEdges = coupleList.size();
        assert this.numberOfEdges > 0;
        this.numberOfNodesAndEdges = numberOfNodes + numberOfEdges;

        cells = new DefaultGraphCell[numberOfNodesAndEdges];

        setUpNodes();
        setUpEdges();

        graph.getGraphLayoutCache().insert(cells);

        //  set up layout for our UML class diagram
        JGraphFacade graphFacade = new JGraphFacade(graph);
        graphFacade.setIgnoresUnconnectedCells(false);
        graphFacade.setDirected(false);

        // 3 July 2012 
        // if going for a cicle layout, need to set the radius
        // factor in the graph facade BEFORE running it with 
        // the simple layout set to "TYPE_CIRCLE"
        double radiusFactor = 1.0;
        switch (Parameters.problemNumber) {
            case Parameters.TEST:
                radiusFactor = 5.5;
                break;

            case Parameters.CBS:
                radiusFactor = 7.0;
                break;

            case Parameters.GDP:
                radiusFactor = 8.5;
                break;

            case Parameters.SC:
                radiusFactor = 4.5;
                break;

            default:
                assert false : "impossible problem number!";
        }

        graphFacade.setCircleRadiusFactor(radiusFactor);

//        Rectangle2D bounds = graphFacade.getGraphBounds( );
//        System.out.println( "height is: " + bounds.getHeight( ) );
//        System.out.println( "width is: " + bounds.getWidth( ) );
//        System.out.println( "center X is: " + bounds.getCenterX( ) );
//        System.out.println( "center Y is: " + bounds.getCenterY( ) );
        // 3 July 2012 layout classes in a circle
        JGraphLayout layout = new JGraphSimpleLayout(JGraphSimpleLayout.TYPE_CIRCLE);

//        JGraphSelfOrganizingOrganicLayout layout =
//            new JGraphSelfOrganizingOrganicLayout( );
//        System.out.println("cooling factor is " + layout.getCoolingFactor( ) ); // 1.0
//        System.out.println("density default is " + layout.getDensityFactor( ) ); // 0.0
//        System.out.println("max adaption is " + layout.getMaxAdaption( ) ); // 0.8
//        System.out.println("max iterationsm multiple is " + layout.getMaxIterationsMultiple( ) ); // 20
//        System.out.println("min adaption is " + layout.getMinAdaption( ) ); // 0.1
//        System.out.println("min radius is " + layout.getMinRadius( ) ); // 1
//        System.out.println("start radius is "  + layout.getStartRadius( ) ); // 0
        // density factor seems to be about right for CBS
//        layout.setDensityFactor( 120000.00 );
//        layout.setStartRadius( 250 );
//        layout.setMinRadius( 100 ); // 10 May 2010
//        layout.setCoolingFactor( 0.5 ); // 10 May 2010
        // run the layout on the graphFacade
        // NB - the layouts do not implement the Runnable interface!
        layout.run(graphFacade);

        // obtain a map of the resulting attribute changes from the graphFacade
        // 3 July 2012 ...somehow... the second parameter controls whether
        // layout starts at top left of scrollpane (true)
        // or more central (false)
        Map nested = graphFacade.createNestedMap(true, false);

        // apply the results to the actual graph
        graph.getGraphLayoutCache().edit(nested);

//        GraphConstants.setInset( nested, WIDTH );
//        GraphConstants.setAutoSize( nested, true );
        graph.clearSelection();

        // -------------------------------------------------------------
        // now set up the graphical user interface components
        //--------------------------------------------------------------
        // set up design elegance information panel
        JPanel infoPanel = new JPanel(new FlowLayout());
//        String designInformationBorderLabel = "<HTML><font color='#000088' size='4'>";
        String designInformationBorderLabel = "Design Information";
//        designInformationBorderLabel += "</font></HTML>";
        infoPanel.setBorder(
                BorderFactory.createTitledBorder(designInformationBorderLabel));

        String s = "<HTML><font color=";
        s += "'#000088'>"; // dark blue
        s += "Coupling Between Objects (CBO): ";
        s += df.format(design.getCBO());
        s += "<br><br>";

        s += "Numbers Among Classes (NAC) : ";
        s += df.format(design.getEleganceNAC());
        s += "<br><br>";

        s += "Attributes to Methods Ratio (ATMR) : ";
        s += df.format(design.getEleganceATMR());
        s += "</font></HTML>";

        JLabel infoLabel = new JLabel(s);
        infoPanel.add(infoLabel);

        // set up evaluation panel with slider
        evalSlider = new JSlider(
                JSlider.HORIZONTAL,
                EVAL_SLIDER_MIN,
                EVAL_SLIDER_MAX,
                EVAL_SLIDER_INIT);

        // turn on labels at major tick marks
        evalSlider.setMajorTickSpacing(10);
        evalSlider.setMinorTickSpacing(2);
        evalSlider.setPaintTicks(true);
        evalSlider.setPaintLabels(true);

        JPanel evalPanel = new JPanel(new GridLayout(2, 1));
//        String ratingBorderLabel = "<HTML><font color='#800080' size='4'>";
        String ratingBorderLabel = "Your Rating (%age)";
//        ratingBorderLabel += "</HTML>";
        evalPanel.setBorder(
                BorderFactory.createTitledBorder(ratingBorderLabel));
        String nextButtonLabel = "<HTML><font color='#800080'>";
        nextButtonLabel += "next design";
        nextButtonLabel += "</font></HTML>";
        nextButton = new JButton(nextButtonLabel);
        ButtonListener buttonListener = new ButtonListener();
        nextButton.addActionListener(buttonListener);
        JPanel nextButtonPanel = new JPanel(new FlowLayout());
        nextButtonPanel.add(nextButton);
        evalPanel.add(evalSlider);
        evalPanel.add(nextButtonPanel);

        // set up progress panel...
        JPanel progressPanel = new JPanel(new FlowLayout());
//        String progressBorderLabel = "<HTML><font color='#000088' size='4'>";
        String progressBorderLabel = "Progress";
//        progressBorderLabel += "</font></HTML>";
        progressPanel.setBorder(
                BorderFactory.createTitledBorder(progressBorderLabel));
        String progressString = "<HTML><font color='#000088'>";
        progressString += "Iteration: ";
        progressString += iteration;
        progressString += "<br><br>";
        progressString += "Interaction: ";
        progressString += interactionCounter;
//        progressString += "<br><br>";
//        progressString += "MAPE: ";
//        String mapeString = " ";
//        if( information.mape == -999.0 )
//        {
//            mapeString = "N/A";
//        }
//        else
//        {
//            mapeString = this.df.format( information.mape );
//        }
//        progressString += mapeString;
        progressString += "</HTML>";
        JLabel temp = new JLabel(progressString);
        progressPanel.add(temp);

        // set up hint panel
        viewfreezeListButton = new JButton("View / Unfreeze Frozen...");
        viewfreezeListButton.addActionListener(buttonListener);
        viewArchiveButton = new JButton("View Archive...");
        viewArchiveButton.addActionListener(buttonListener);

        JPanel hintPanel = new JPanel(new GridLayout(5, 1));
        hintPanel.setBorder(
                BorderFactory.createTitledBorder("Designer's hints"));

        hintPanel.add(new JPanel());
        hintPanel.add(viewfreezeListButton);
        hintPanel.add(new JPanel());
        hintPanel.add(viewArchiveButton);
        hintPanel.add(new JPanel());

        // set up the finished panel
        JPanel finishedPanel = new JPanel(new FlowLayout());
        finishedButton = new JButton("FINISHED");
        finishedButton.addActionListener(buttonListener);
        finishedPanel.add(finishedButton);

        // set up panel under design visualisation
        JPanel bottomPanel = new JPanel(new FlowLayout());
//        bottomPanel.add( infoPanel );
        bottomPanel.add(evalPanel);
        bottomPanel.add(progressPanel);
        bottomPanel.add(hintPanel);
        bottomPanel.add(finishedPanel);

        // add sub-components to dialog
        Container contentPane = getContentPane();
        contentPane.setBackground(Color.white);
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(graph), BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * do the freezing mechanism
     */
    private void doFreezing(MouseEvent e) {
        // tolerance of how close the mouse click must be to the cell
        // int tolerance = graph.getTolerance( );

        // default is 4 pixels
        // System.out.println( "tolerance is: " + tolerance );
        // so get the cell under the mouse
        int x = e.getX(), y = e.getY();
        DefaultGraphCell cell = (DefaultGraphCell) graph.getFirstCellForLocation(x, y);

        if (cell != null) // only do stuff if valid cell returned, inside tolerance
        {
            String label = graph.convertValueToString(cell);
            System.out.println(label);
            AttributeMap am = cell.getAttributes();
            Set s = am.keySet();
            Iterator it = s.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                System.out.println("key is: " + key);
            }

            CLSClass c = (CLSClass) am.get("attachedClass");
            List< Attribute> aList = c.getAttributeList();
            List< Method> mList = c.getMethodList();
            System.out.print("class is... ");
            for (Attribute a : aList) {
                System.out.print(" attribute: " + a.getNumber() + " " + a.getName());
            }
            for (Method m : mList) {
                System.out.print(" method: " + m.getNumber() + " " + m.getName());
            }
            System.out.println(" ");

            doFreezePopUp(x, y, c);
        }
    }

    /**
     * the listener for all buttons, implemented as a nested class
     */
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == nextButton) {
                userAction = JOptionPane.OK_OPTION;
                // after 'next' button click, hide the dialog
                setVisible(false);
            } else if (event.getSource() == viewArchiveButton) {
                viewArchivedDesigns();
            } else if (event.getSource() == viewfreezeListButton) {
                viewUnfreezeFrozenClasses();
            } else if (event.getSource() == finishedButton) {
                userAction = JOptionPane.CANCEL_OPTION;
                // after cancel button click, hide the dialog
                setVisible(false);
            } else // impossible ???
            {
                assert false : "impossible button event!";
            }
        }
    }

    /**
     * set up the class boxes for display in the diagram
     */
    private void setUpNodes() {
        assert classList != null;

        double maxCOM = getMaximumCOMFitnessValue();

        for (int index = 0; index < classList.size(); index++) {
            CLSClass c = classList.get(index);

            String s = getDisplayString(c);
            Color color = Color.WHITE;

            if (Parameters.colourMetaphor == Parameters.WATER_TAP) {
                color = getWaterTapDisplayColour(c, maxCOM);
            } else if (Parameters.colourMetaphor == Parameters.TRAFFIC_LIGHTS) {
                color = getTrafficLightDisplayColour(c, maxCOM);
            }

            AttributeMap am = new AttributeMap();
            am.applyValue("attachedClass", c);

            DefaultGraphCell dgc = new DefaultGraphCell(s);
            dgc.setAttributes(am);

            cells[index] = dgc;
            GraphConstants.setResize(
                    cells[index].getAttributes(), true);
            GraphConstants.setGradientColor(
                    cells[index].getAttributes(), color);
            GraphConstants.setOpaque(
                    cells[index].getAttributes(), true);
            GraphConstants.setBorder(
                    cells[index].getAttributes(),
                    BorderFactory.createLineBorder(Color.BLACK));
    //        GraphConstants.setFont(
            //            cells[ 0 ].getAttributes( ),
            //            new Font( "Helvetica", Font.PLAIN, 36 ) );

            // set up the port for potential edges (couples)
            DefaultPort port = new DefaultPort();
            cells[index].add(port);
        }
    }

    /**
     * calculate the coupling going on in the class design coupling here is
     * represented as external "use" i.e. a method within a class "uses" an
     * attributes outside of this class. a list of couples (source, target) is
     * set up
     *
     * @param use table
     */
    private void calculateCoupling(
            SortedMap< String, List< CLSDatum>> useTable) {
        assert useTable != null;

        for (int index = 0; index < classList.size(); index++) {
            CLSClass c = classList.get(index);

            Iterator< Method> mIt = c.getMethodListIterator();

            while (mIt.hasNext()) {
                Method m = mIt.next();
                List< CLSDatum> list = useTable.get(m.getName());
                assert list != null;

                for (int j = 0; j < list.size(); j++) {
                    CLSDatum d = list.get(j);
                    final String datumName = d.getName();

                    Iterator< Attribute> aIt = c.getAttributeListIterator();
                    boolean found = false;

                    // is this datum to be found in this class??
                    while (aIt.hasNext() && found == false) {
                        Attribute a = aIt.next();
                        final String attributeName = a.getName();

//                        if( a.getName( ).equals( d.getName( ) ) )
                        if (attributeName.equals(datumName)) {
                            // we have an "internal couple" - great!
                            found = true;
                        }
                    }

                    if (found == false) // must be an "external couple"
                    {
                        // now find the target!!!
                        final int target = findTarget(datumName);
                        assert index != target : "index can't be target!";
                        assert target < classList.size() : "target is " + target;

                        // does the couple already exist?
                        final int coupleNumber = getCoupleNumber(index, target);

                        if (coupleNumber == NON_EXISTENT) {
                            // create a couple
                            Couple couple = new Couple();
                            couple.source = index;
                            couple.target = target;
                            couple.counter = 1;
                            coupleList.add(couple);
                        } else // these classes already have a couple
                        {
                            // get the appropriate couple
                            Couple couple = coupleList.get(coupleNumber);
                            // record that we've found another couple
                            couple.counter++;
                        }
                    }
                }
            }

        }   // end for each class
    }

    /**
     * find the target class that contains an attribute name
     *
     * @param datum name as string
     * @return index into the class array list of target class
     */
    private int findTarget(String datumName) {
        assert datumName != null;
        int result = 0;
        boolean found = false;

        for (int i = 0; i < classList.size() && found == false; i++) {
            CLSClass c = classList.get(i);

            Iterator< Attribute> aIt = c.getAttributeListIterator();

            while (aIt.hasNext() && found == false) {
                String name = aIt.next().getName();
                if (name.equals(datumName)) {
                    found = true;
                    result = i;
                }
            }
        }

        return result;
    }

    /**
     * try to find if a couple already exists between a source class and a
     * target class.
     *
     * @param index of source in class list
     * @param index of target in class list
     * @return index in couple list if found, NON_EXISTENT otherwise
     */
    private int getCoupleNumber(int source, int target) {
        int result = NON_EXISTENT;
        boolean found = false;

        assert this.coupleList != null;

        for (int index = 0; index < coupleList.size() && found == false; index++) {
            Couple couple = coupleList.get(index);

            if (couple.source == source && couple.target == target) {
                result = index;
                found = true;
            }
        }

        return result;
    }

    /**
     * set up the edges in the DefaultGraphCell array starting from the number
     * of nodes index
     */
    private void setUpEdges() {
        assert numberOfNodes > 0;
        assert numberOfEdges > 0;

        final int coupleListSize = coupleList.size();

        int i, j;
        for (i = 0, j = numberOfNodes; i < coupleListSize; i++, j++) {
            // find the source and target of this couple
            final int source = coupleList.get(i).source;
            assert source >= 0 && source < numberOfNodes :
                    "Source value is: " + source
                    + " number of vertices is:" + numberOfNodes;
            final int target = coupleList.get(i).target;
            assert target >= 0 && target < numberOfNodes :
                    "target value is: " + target
                    + " number of vertices is: " + numberOfNodes;

            // set up the edge
            DefaultEdge edge = new DefaultEdge();
            edge.setSource(cells[source].getChildAt(0));
            edge.setTarget(cells[target].getChildAt(0));
            assert j < cells.length : "too many iterations making edges";
            cells[j] = edge;

            // set up the arrow to look like a UML directional association
            //  but the width of the arrows is proportional to the number of couples
            final int arrow = GraphConstants.ARROW_SIMPLE;
            GraphConstants.setLineEnd(edge.getAttributes(), arrow);
            final float width = coupleList.get(i).counter;
            GraphConstants.setLineWidth(edge.getAttributes(), width);
        }
    }

    /**
     * get the maximum COM value in this class
     *
     * @return maximum COM fitness value
     */
    private double getMaximumCOMFitnessValue() {
        double maxCOM = 0.0;
        for (CLSClass c : classList) {
            final double COM = c.getCOMFitness();
            if (COM > maxCOM) {
                maxCOM = COM;
            }
        }
        return maxCOM;
    }

    // displays HTML translation of the class when double clicking the class box!
    private String getDisplayString(CLSClass c) {
        DecimalFormat localFormat = new DecimalFormat("0.00");
        String s = "<HTML> Cohesion = " + localFormat.format(c.getCOMFitness()) + "<br>";
        s += "<hr>";
        Iterator< Attribute> aIt = c.getAttributeListIterator();
        while (aIt.hasNext()) {
            Attribute a = aIt.next();
            s += a.getName();
            s += "<br>";
        }
        s += "<hr>";
        Iterator< Method> mIt = c.getMethodListIterator();
        while (mIt.hasNext()) {
            Method m = mIt.next();
            s += m.getName();
            s += "( )";
            s += "<br>";
        }
        s += "</HTML>";
        return s;
    }

    /**
     * get the display colour according to COM WATER TAP metaphor - red is hot,
     * blue is cool
     *
     * @param c class to get the display colour for
     * @param maxCOM maximum COM value for this class
     * @return the colour
     */
    private Color getWaterTapDisplayColour(CLSClass c, final double maxCOM) {
        assert maxCOM > 0.0;
        Color result = Color.WHITE;
        final int numberOfColours = 5;
        final double increment = maxCOM / numberOfColours;
        final double COM = c.getCOMFitness();
        if (COM < increment) {
            result = Color.CYAN;
        } else if (COM < (increment * 2)) {
            result = Color.LIGHT_GRAY;
        } else if (COM < (increment * 3)) {
            result = Color.YELLOW;
        } else if (COM < (increment * 4)) {
            result = Color.PINK;
        } //  25 July 2007 make number 5.1 to handle
        //  any potential rounding errors
        else if (COM <= (increment * 5.1)) {
            result = Color.RED;
        } else {
            assert false : "impossible colour";
        }
        return result;
    }

    /**
     * get the display colour according to COM Traffic Light metaphor - red,
     * amber, green
     *
     * @param c class to get the display colour for
     * @param maxCOM maximum COM value for this class
     * @return the colour
     */
    private Color getTrafficLightDisplayColour(CLSClass c, final double maxCOM) {
        assert maxCOM > 0.0;
        Color result = Color.WHITE;
        final int numberOfColours = 3;
        final double increment = maxCOM / numberOfColours;
        final double COM = c.getCOMFitness();
        assert COM >= 0.0;
        if (COM < increment) {
            result = Color.RED;
        } else if (COM < (increment * 2)) {
            result = Color.ORANGE;
        } else { // COM <= ( increment * 3 ) )
            result = Color.GREEN;
        }
        return result;
    }

    /**
     * getUserAction get result of button click, one of either OK_OPTION or
     * CANCEL_OPTION
     *
     * @return userAction
     */
    public int getUserAction() {
        return userAction;
    }

    /**
     * get the designer evaluation of the software design as a integer
     * percentage
     *
     * @return designer evaluation as percentage
     */
    public int getDesignerEvaluation() {
//        System.out.println("designer evaluation is: " + evalSlider.getValue( ) );
        return evalSlider.getValue();
    }

    /**
     * the "freeze a class" pop-up window launched from a right mouse click
     *
     * @param the x start coordinate
     * @param the y start coordinate
     * @param c, the class which the user can decide to freeze
     */
    private void doFreezePopUp(int x, int y, CLSClass c) {
        FreezeDialog freezeDialog = new FreezeDialog(x + START_X + 10, y + 10);
        freezeDialog.setVisible(true);
        int userFreezeAction = freezeDialog.getUserAction();
        if (userFreezeAction == JOptionPane.OK_OPTION) {  // user has clicked "OK"
            System.out.println("user click OK!!");
            int intention = freezeDialog.getIntention();
            if (intention == FreezeDialog.FREEZE) {
                System.out.println("intention is to freeze");
                assert c != null;
                freezeList.add(c);
                this.information.classFrozen = true;
            }
        }
    }

    /**
     * view frozen classes using a JOptionPane
     */
    private void viewUnfreezeFrozenClasses() {
//        String result = "<HTML><font color='#000088'>";
//        
//        if( this.freezeList.isEmpty( ) )
//        {
//            result += "no classes have been frozen!";
//        }
//        else
//        {
//            for( CLSClass c : this.freezeList )
//            {
//                result += "FROZEN CLASS ";
//                result += c.toString( );
//                result += "<br><br>";
//            }
//        }
//        result += "</font></HTML>";
//        
//        JOptionPane.showMessageDialog( this, result );

        ViewUnfreezeFrozenDialog vufd = new ViewUnfreezeFrozenDialog(
                400, 200, this.freezeList, this.information);
        vufd.setVisible(true);
    }

    /**
     * handle the right mouse click on the design scroll-pane
     *
     * @param mouse event
     */
    private void handleRightMouseClick(MouseEvent me) {
        assert me != null;
        // get the x and y coordinates of the right mouse click
        int x = me.getX(), y = me.getY();

        // tolerance of how close the mouse click must be to the cell
        // int tolerance = graph.getTolerance( );
        // default is 4 pixels
        // System.out.println( "tolerance is: " + tolerance );
        // so get the cell under the mouse
        DefaultGraphCell cell = (DefaultGraphCell) graph.getFirstCellForLocation(x, y);

        if (cell != null) // only do stuff if valid cell returned, inside tolerance
        {
            if (Parameters.freezing == true) // check to see if freezing is turned ON
            {
                String label = graph.convertValueToString(cell);
                System.out.println(label);
                AttributeMap am = cell.getAttributes();
                Set s = am.keySet();
                Iterator it = s.iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    System.out.println("key is: " + key);
                }

                CLSClass c = (CLSClass) am.get("attachedClass");
                List< Attribute> aList = c.getAttributeList();
                List< Method> mList = c.getMethodList();
                System.out.print("class is... ");
                for (Attribute a : aList) {
                    System.out.print(" attribute: " + a.getNumber() + " " + a.getName());
                }
                for (Method m : mList) {
                    System.out.print(" method: " + m.getNumber() + " " + m.getName());
                }
                System.out.println(" ");

                doFreezePopUp(x, y, c);
            }
        } else // right mouse click must be in the background of the visualisation
        {
            String message = "Do you wish to place this design in the archive?";

            int result = JOptionPane.showConfirmDialog(
                    this, message, "Archive Design", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                System.out.println("yes clicked");
                this.archive.add(this.eleganceDesign);
                this.information.archived = true;

            } else if (result == JOptionPane.NO_OPTION) {
                // do nothing
                // System.out.println("no clicked");
            }
        }
    }

    /**
     * view the designs in the archive
     */
    private void viewArchivedDesigns() {
        System.out.println("viewing archiving now...");

        String result = "<HTML><font color='#000088'>";

        if (this.archive.isEmpty()) {
            result += "no designs have been archived!";
        } else {
            for (EleganceDesign ed : archive) {
                List< CLSClass> cList = ed.getClassList();
                result += "DESIGN -  ";
                ed.calculateExternalCoupling(this.useTable);
                result += "CBO is: " + df.format(ed.getExternalCouplingValue());
                ed.calculateEleganceNAC();
                result += ", NAC is: " + df.format(ed.getEleganceNAC());
                ed.calculateEleganceATMR();
                result += ", ATMR is: " + df.format(ed.getEleganceATMR());
                result += "<br>";

                result += "<ul>";
                for (CLSClass c : cList) {
                    result += "<li>";
                    result += "Class - ";
                    result += c.toString();
                    result += "</li>";
                }
                result += "</ul>";
                result += "<br>";
            }
        }
        result += "</font></HTML>";

        JOptionPane.showMessageDialog(this, result);
    }
}

//------- end of file -------------------------------------

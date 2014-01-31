package edu.iastate.cs228.hw5.viewer;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import edu.iastate.cs228.hw5.api.BidirectionalGraphAnimation;
import edu.iastate.cs228.hw5.api.Graph;
import edu.iastate.cs228.hw5.api.GraphAnimation;
import edu.iastate.cs228.hw5.api.PathFinderFactory;
/**
 * GUI for animation of graph search algorithms.  Requires a factory for 
 * creating instances of the GraphAnimation interface.  The factory is set 
 * via the system property edu.iastate.cs228.factoryname, which can be set 
 * on the command line using the VM -D option, e.g.
 * 
 *  -Dedu.iastate.cs228.factoryname=edu.iastate.cs228.hw5.MyFactory
 *  
 * @author Steve Kautz
 */
public class GraphAnimationViewer extends JPanel
{
  public static final String FACTORY_PROPERTY = "edu.iastate.cs228.factoryname";
  
  private static final int NODE_WIDTH = 4; // size for rendering nodes
  private static final int NODE_HEIGHT = 4;
  private static final int CELLSIZE = 7;   // scale factor for spreading out points
  private static final int MAX_COORDINATE = 100; 
  
  private static Color INITIAL_COLOR = Color.GREEN;
  private static Color OPEN_COLOR = Color.RED;
  private static Color CLOSED_COLOR = Color.BLACK;
  private static Color OPEN_COLOR_REVERSE = Color.MAGENTA;
  private static Color CLOSED_COLOR_REVERSE = Color.DARK_GRAY;
  private static Color START_COLOR = Color.BLACK;
  private static Color GOAL_COLOR = Color.DARK_GRAY;
  private static Color PATH_COLOR = Color.BLUE;
  private static Color LAST_ADDED_COLOR = Color.CYAN;
  
  // default attributes for graph creation, should be modified only from dialog
  
  private int numberOfPoints = 1000; // default number of points
  private int maxEdgeLength = 5;     // max length of edges to be included
  private int density = 100;         // probability of adding an edge
  private long lastSeed = 0;         // most recently used seed 
  private Point start = new Point(MAX_COORDINATE / 3, 2 * MAX_COORDINATE / 3);
  private Point goal = new Point(MAX_COORDINATE - 10, 10);

  private PathFinderFactory factory;
  private Point[] vertices;
  private Graph<Point> graph;
  private Point lastAddedToClosedSet;

  // this is nulled out on reset
  private GraphAnimation<Point> graphAnimator;
  
  // this should be true only if graphAnimator is a BidirectionalGraphAnimation
  private boolean bidirectional = false;
  
  private Timer timer;
  private boolean animating = false;
  private int millis = 40; // about 24 frames/sec?

  private String[] algStrings = {"BFS", "DFS", "Dijkstra", "AStar"};
  private JButton stepButton;
  private JButton runButton;
  private JButton pauseButton;
  private JButton resetButton;
  private JButton createGraphButton;
  private JComboBox algorithmBox;
  private JCheckBox bidirectionalBox;
  
  // construction is deferred because the dialog needs to refer to the frame
  private GraphSelectionDialog dialog;

  public GraphAnimationViewer(PathFinderFactory factory)
  {    
    this.factory = factory;
    timer = new Timer(millis, new TimerAction());
    JPanel buttonPanel = new JPanel();
    stepButton = new JButton("Step");
    stepButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        if (graphAnimator == null)
        {
          createGraphAnimator(start, goal);
        }
        lastAddedToClosedSet = graphAnimator.step();
        repaint();
      }
    });
    
    runButton = new JButton("Run");
    runButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        if (graphAnimator == null)
        {
          createGraphAnimator(start, goal);
        }
        timer.restart();
        animating = true;
      }
    });
    
    pauseButton = new JButton("Pause");
    pauseButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        timer.stop();
        animating = false;
      }
    });

    resetButton = new JButton("Reset");
    resetButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        timer.stop();
        animating = false;
        graphAnimator = null;
        bidirectional = false;
        lastAddedToClosedSet = null;
        repaint();
      }
    });
    
    createGraphButton = new JButton("Create graph");
    createGraphButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        timer.stop();
        animating = false;
        graph = null;
        graphAnimator = null;
        bidirectional = false;
        lastAddedToClosedSet = null;

        if (dialog == null)
        {
          dialog = new GraphSelectionDialog(GraphAnimationViewer.this.getTopLevelAncestor());
          dialog.pack();
        }
        dialog.setVisible(true);
      }
    });


    buttonPanel.add(stepButton);
    buttonPanel.add(runButton);
    buttonPanel.add(pauseButton);
    buttonPanel.add(resetButton);
    buttonPanel.add(createGraphButton);
    
    algorithmBox = new JComboBox(algStrings);
    algorithmBox.setSelectedIndex(0);
    buttonPanel.add(algorithmBox);
    
    bidirectionalBox = new JCheckBox("Bidirectional", false);
    buttonPanel.add(bidirectionalBox);

    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    buttonPanel.setPreferredSize(new Dimension(20, 40));
    this.add(buttonPanel, BorderLayout.NORTH);
    MyPanel canvas = new MyPanel();
    canvas.setPreferredSize(new Dimension(MAX_COORDINATE * CELLSIZE, MAX_COORDINATE * CELLSIZE));
    JScrollPane scrollPane = new JScrollPane(canvas);
    this.add(scrollPane);
  }
  
  
  /**
   * Static helper method creates the frame and
   * makes it visible.
   */
  public static void createAndShow(PathFinderFactory factory)
  {
    // create the frame and give it a title
    JFrame frame = new JFrame("Points");
    
    // create an instance of our JPanel subclass and 
    // add it to the frame   
    GraphAnimationViewer test = new GraphAnimationViewer(factory);
    frame.getContentPane().add(test);
    
    // give it a nonzero size
    frame.pack();

    // we want to shut down the application if the 
    // "close" button is pressed on the frame
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // make the frame visible and start the UI machinery
    frame.setVisible(true);
  }
  
  private void createGraphAnimator(Point start, Point goal)
  {
    int selection = algorithmBox.getSelectedIndex();
    if (bidirectionalBox.isEnabled() && bidirectionalBox.isSelected())
    {
      bidirectional = true;
      BidirectionalGraphAnimation<Point> animator;

      // {"BFS", "DFS", "Dijkstra", "AStar"};
      switch (selection)
      {
        case 0: 
          //System.out.println("Using BFS");
          animator = factory.createBidirectionalBFSFinder(graph, start, goal);
          break;
        case 1: 
          //System.out.println("Using DFS");
          animator = factory.createBidirectionalDFSFinder(graph, start, goal);
          break;
        case 2: 
          //System.out.println("Using Dijkstra");
          animator = factory.createBidirectionalDijkstraFinder(graph, start, goal);
          break;
        case 3: 
          //System.out.println("Using AStar");
          animator = factory.createBidirectionalAStarFinder(graph, start, goal);
          break;    
        default:
          // should never happen
          String msg = "No factory option for " + algorithmBox.getSelectedItem();
          throw new RuntimeException(msg);
      }
      graphAnimator = animator;
    }
    else
    {
      switch (selection)
      {
        case 0: 
          //System.out.println("Using BFS");
          graphAnimator = factory.createBFSFinder(graph, start, goal);
          break;
        case 1: 
          //System.out.println("Using DFS");
          graphAnimator = factory.createDFSFinder(graph, start, goal);
          break;
        case 2: 
          //System.out.println("Using Dijkstra");
          graphAnimator = factory.createDijkstraFinder(graph, start, goal);
          break;
        case 3: 
          //System.out.println("Using AStar");
          graphAnimator = factory.createAStarFinder(graph, start, goal);
          break;    
        default:
          // should never happen
          String msg = "No factory option for " + algorithmBox.getSelectedItem();
          throw new RuntimeException(msg);
      }
    }

  }
  
  
  private void initGraph()
  {
    Random gen = new Random(lastSeed);

    PointGraph g = new PointGraph();
    vertices = new Point[numberOfPoints];
    int initial = 0;
    if (start != null)
    {
      vertices[initial] = start;
      ++initial;
    }
    if (goal != null)
    {
      vertices[initial] = goal;
      ++initial;
    }
    
    // generate 'size' distinct random points
    for (int i = initial; i < numberOfPoints; ++i)
    {   
      boolean collides;
      do
      {
        collides = false;
        int x = gen.nextInt(MAX_COORDINATE);
        int y = gen.nextInt(MAX_COORDINATE);
        Point p = new Point(x, y);
        for (int j = 0; j < i; ++j)
        {
          if (vertices[j].equals(p))
          {
            collides = true;
            break;
          }
        }
        if (!collides)
        {
          vertices[i] = p;
        }
      } while (collides);
    }
    for (int i = 0; i < numberOfPoints; ++i)
    {
      g.addVertex(vertices[i]);
    }
    
    Map<Point, Integer> indexMap = new HashMap<Point, Integer>();
    for (int i = 0; i < numberOfPoints; ++i)
    {
      indexMap.put(vertices[i], i);
    }
        
    for (int i = 0; i < numberOfPoints; ++i)
    {
      for (int j = 0; j < numberOfPoints; ++j)
      {
        if (i != j)
        {
          int distance = (int) Math.round(vertices[i].distance(vertices[j]) * 100);
          if (distance < maxEdgeLength * 100)
          {
            double flip = gen.nextDouble();
            if (flip * 100 < density)
            {
              // add edges (i, j) and (j, i), if not already present
              Iterator<Graph.Edge<Point>> iter = g.getNeighbors(vertices[i]);
              boolean found = false;
              while (iter.hasNext())
              {
                Point p = iter.next().vertex;
                if (p.equals(vertices[j]))
                {
                  found = true;
                  break;
                }
              }
              if (!found)
              {
                g.addEdge(vertices[i], vertices[j], distance);
                g.addEdge(vertices[j], vertices[i], distance);
              }
            }
          }
        }        
      }
    }
    
    graph = g;
  }
  
  private class MyPanel extends JComponent
  {
    @Override
    public void paintComponent(Graphics g)
    {
      // downcast the Graphics object
      Graphics2D g2 = (Graphics2D) g;
      g2.setBackground(Color.WHITE);
      g2.clearRect(0, 0, getWidth(), getHeight());
      
      if (graph == null) return;

      // paint all edges
      g2.setPaint(INITIAL_COLOR);
      g2.setStroke(new BasicStroke(1));
      for (int i = 0; i < vertices.length; ++i)
      {
        Iterator<Graph.Edge<Point>> iter = graph.getNeighbors(vertices[i]);
        while (iter.hasNext())
        {
          Point src = vertices[i];
          Point dest = iter.next().vertex;
          g2.drawLine(src.x * CELLSIZE, src.y * CELLSIZE, dest.x * CELLSIZE, dest.y * CELLSIZE);
        }
      }
      
      // all vertices black to start out
      g2.setPaint(INITIAL_COLOR);
      for (int i = 2; i < vertices.length; ++i)
      {
        Rectangle box2 = new Rectangle(vertices[i].x * CELLSIZE - NODE_WIDTH/2, vertices[i].y * CELLSIZE - NODE_WIDTH/2, NODE_WIDTH, NODE_HEIGHT);
        g2.fill(box2);
      }
      
      int iters = 0;
      GraphAnimation<Point> animator = graphAnimator;
      if (graphAnimator != null)
      {
        iters = bidirectional ? 2 : 1;
      }
      for (int count = 0; count < iters; ++count)
      {
        // paint edges in open set
        g2.setPaint(count == 0 ? OPEN_COLOR : OPEN_COLOR_REVERSE);
        g2.setStroke(new BasicStroke(2));
        Iterator<Point> iter = count == 0 ? animator.openSet() : ((BidirectionalGraphAnimation<Point>) animator).openSetReverse();
        while (iter.hasNext())
        {
          Point p = iter.next();
          Point pred = count == 0 ? animator.getPredecessor(p) : ((BidirectionalGraphAnimation<Point>) animator).getPredecessorReverse(p);
          if (pred != null)
          {
            g2.drawLine(pred.x * CELLSIZE, pred.y * CELLSIZE, p.x * CELLSIZE, p.y * CELLSIZE);
          }
        }
      }
      
      // edges in closed sets
      for (int count = 0; count < iters; ++count)
      {
        g2.setPaint(count == 0 ? CLOSED_COLOR : CLOSED_COLOR_REVERSE);
        g2.setStroke(new BasicStroke(2));
        Collection<Point> closedSet = count == 0 ? animator.closedSet() : ((BidirectionalGraphAnimation<Point>) animator).closedSetReverse();
        Iterator<Point> iter = closedSet.iterator();
        while (iter.hasNext())
        {
          Point p = iter.next();
          Point pred = count == 0 ? animator.getPredecessor(p) : ((BidirectionalGraphAnimation<Point>) animator).getPredecessorReverse(p);
          if (pred == null)
          {
            //System.out.println("Null predecessor for " + p);
          }
          else
          {
            g2.drawLine(pred.x * CELLSIZE, pred.y * CELLSIZE, p.x * CELLSIZE, p.y * CELLSIZE);
          }
        }
      }
      
      // vertices in open set
      for (int count = 0; count < iters; ++count)
      {       
        g2.setPaint(count == 0 ? OPEN_COLOR : OPEN_COLOR_REVERSE);
        Iterator<Point> iter = count == 0 ? animator.openSet() : ((BidirectionalGraphAnimation<Point>) animator).openSetReverse();
        while (iter.hasNext())
        {
          Point p = iter.next();
          Rectangle box2 = new Rectangle(p.x * CELLSIZE - NODE_WIDTH/2, p.y * CELLSIZE - NODE_WIDTH/2, NODE_WIDTH, NODE_HEIGHT);
          g2.fill(box2);
        }
      }
      
      // vertices in closed set
      for (int count = 0; count < iters; ++count)
      {       
        g2.setPaint(count == 0 ? CLOSED_COLOR : CLOSED_COLOR_REVERSE);
        Collection<Point> closedSet = count == 0 ? animator.closedSet() : ((BidirectionalGraphAnimation<Point>) animator).closedSetReverse(); 
        Iterator<Point> iter = closedSet.iterator();
        while (iter.hasNext())
        {
          Point p = iter.next();
          Rectangle box2 = new Rectangle(p.x * CELLSIZE - NODE_WIDTH/2, p.y * CELLSIZE - NODE_WIDTH/2, NODE_WIDTH, NODE_HEIGHT);
          g2.fill(box2);
        }
      }  // for animator : tmp
      
      // paint the shortest path
      if (graphAnimator != null && graphAnimator.done())
      {
        timer.stop();
        animating = false;
        if (goal != null)
        {
          g2.setPaint(PATH_COLOR);
          g2.setStroke(new BasicStroke(3));
          List<Point> path = bidirectional ? ((BidirectionalGraphAnimation<Point>) graphAnimator).getCompletePath() : graphAnimator.getPath(goal);
          Iterator<Point> li = path.iterator();
          Point current = null;
          if (li.hasNext()) 
          {
            current = li.next();
          }
          while (li.hasNext())
          {
            Point next = li.next();
            g2.drawLine(current.x * CELLSIZE, current.y * CELLSIZE, next.x * CELLSIZE, next.y * CELLSIZE);
            current = next;
          }
        }
      }
      
      // start and goal
      g2.setPaint(START_COLOR);
      int x = vertices[0].x * CELLSIZE;
      int y = vertices[0].y * CELLSIZE;
      g2.fillOval(x - NODE_WIDTH, y - NODE_HEIGHT, NODE_WIDTH * 2, NODE_HEIGHT * 2);
      g2.setStroke(new BasicStroke(1));
      g2.drawOval(x - NODE_WIDTH * 4, y - NODE_HEIGHT * 4, NODE_WIDTH * 8, NODE_HEIGHT * 8);

      if (goal != null)
      {
        g2.setPaint(GOAL_COLOR);
//        g2.fillOval(vertices[1].x * CELLSIZE - NODE_WIDTH/2, vertices[1].y * CELLSIZE - NODE_HEIGHT/2, NODE_WIDTH * 3, NODE_HEIGHT * 3);
        x = vertices[1].x * CELLSIZE;
        y = vertices[1].y * CELLSIZE;
        g2.fillOval(x - NODE_WIDTH, y - NODE_HEIGHT, NODE_WIDTH * 2, NODE_HEIGHT * 2);
        g2.setStroke(new BasicStroke(1));
        g2.drawOval(x - NODE_WIDTH * 4, y - NODE_HEIGHT * 4, NODE_WIDTH * 8, NODE_HEIGHT * 8);
      }
      
      // last added
      if (!animating)
      {        
        if (lastAddedToClosedSet != null)
        {
          g2.setPaint(LAST_ADDED_COLOR);
          g2.setStroke(new BasicStroke(2));
          g2.drawOval(lastAddedToClosedSet.x * CELLSIZE - NODE_WIDTH * 4, lastAddedToClosedSet.y * CELLSIZE - NODE_HEIGHT * 4,
              NODE_WIDTH * 8, NODE_HEIGHT * 8);
        }
      }
    }
  }  
  
  private class TimerAction implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent event)
    {
      if (graphAnimator != null && !graphAnimator.done())
      {
//        if (graphAnimatorReverse != null)
//        {
//          lastAddedToClosedSet = graphAnimatorReverse.step();
//        }
        lastAddedToClosedSet = graphAnimator.step();
        repaint();
      }
    }   
  }
  
  class GraphSelectionDialog extends JDialog
  {
    final int FIELD_WIDTH = 12;
    JTextField seedField = new JTextField(FIELD_WIDTH);
    JTextField pointsField = new JTextField(FIELD_WIDTH);
    JTextField lengthField = new JTextField(FIELD_WIDTH);
    JTextField densityField = new JTextField(FIELD_WIDTH);
    JButton okButton = new JButton("OK");
    JButton cancelButton = new JButton("Cancel");
    
    JCheckBox goalBox = new JCheckBox("Use goal node", true);
    //JCheckBox bidirectionalBox = new JCheckBox("Bidirectional", false);

    JPanel goalNodePanel = new JPanel();
    JTextField startX = new JTextField(4);
    JTextField startY = new JTextField(4);
    JTextField goalX = new JTextField(4);
    JTextField goalY = new JTextField(4);

    JRadioButton randomizeButton = new JRadioButton("Randomize");
    JRadioButton existingSeedButton = new JRadioButton("Use last seed");
    JRadioButton newSeedButton = new JRadioButton("Enter seed");
    Container topLevelContainer;
    GraphSelectionDialog(Container container)
    {
      super((Window) container);
      topLevelContainer = container;
      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
            
      // radio buttons for seed selection
      JPanel tempPanel = new JPanel();
      ButtonGroup randButtons = new ButtonGroup();
      randButtons.add(randomizeButton);
      randButtons.add(existingSeedButton);
      randButtons.add(newSeedButton);
      tempPanel.add(randomizeButton);
      tempPanel.add(existingSeedButton);
      tempPanel.add(newSeedButton);
      ActionListener randomizeListener = new RandomizeButtonHandler();
      randomizeButton.addActionListener(randomizeListener);
      existingSeedButton.addActionListener(randomizeListener);
      newSeedButton.addActionListener(randomizeListener);
      randomizeButton.setSelected(true);
      seedField.setEnabled(false);
      mainPanel.add(tempPanel);
      
      // field for seed
      tempPanel = new JPanel();
      tempPanel.add(new JLabel("Seed:"));
      tempPanel.add(seedField);
      mainPanel.add(tempPanel);
      
      // field for number of points
      tempPanel = new JPanel();
      tempPanel.add(new JLabel("Number of points"));
      pointsField.setText(numberOfPoints + "");
      tempPanel.add(pointsField);
      mainPanel.add(tempPanel);
      
      // field for edge length
      tempPanel = new JPanel();
      tempPanel.add(new JLabel("Max edge length"));
      lengthField.setText(maxEdgeLength + "");
      tempPanel.add(lengthField);
      mainPanel.add(tempPanel);
   
      // field for density
      tempPanel = new JPanel();      
      tempPanel.add(new JLabel("Density (0 - 100%)"));
      densityField.setText(density + "");
      tempPanel.add(densityField);
      mainPanel.add(tempPanel);
      
      // start node
      tempPanel = new JPanel();      
      tempPanel.add(new JLabel("Start node x:"));
      startX.setText(start.x + "");
      tempPanel.add(startX);
      tempPanel.add(new JLabel("Start node y:"));
      startY.setText(start.y + "");
      tempPanel.add(startY);
      mainPanel.add(tempPanel);

      
      // check box for goal
      tempPanel = new JPanel();      
      goalBox.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          goalX.setEnabled(goalBox.isSelected());
          goalY.setEnabled(goalBox.isSelected());
          // only allow bidirectional search if there is a goal
          bidirectionalBox.setEnabled(goalBox.isSelected());
        }       
      });
      tempPanel.add(goalBox);
      mainPanel.add(tempPanel);
    
      // goal node
      goalX.setText((goal != null ? goal.x : "") + "");
      goalY.setText((goal != null ? goal.y : "") + "");    
      goalNodePanel.add(new JLabel("Goal node x:"));
      goalNodePanel.add(goalX);
      goalNodePanel.add(new JLabel("Goal node y:"));     
      goalNodePanel.add(goalY);
      mainPanel.add(goalNodePanel);
      
      // buttons
      JPanel bottomPanel = new JPanel();
      ClickHandler handler = new ClickHandler();
      cancelButton.addActionListener(handler);
      bottomPanel.add(cancelButton);
      okButton.addActionListener(handler);
      bottomPanel.add(okButton);
      mainPanel.add(bottomPanel);
      this.add(mainPanel);      
    }
    
    class RandomizeButtonHandler implements ActionListener
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        if (event.getSource() == randomizeButton)
        {
          seedField.setText("");
          seedField.setEnabled(false);
        }
        else if (event.getSource() == existingSeedButton)
        {
          seedField.setText(lastSeed + "");
          seedField.setEnabled(false);
        }
        else
        {
          seedField.setEnabled(true);
        }
      }
    }
    
    class ClickHandler implements ActionListener
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        if (event.getSource() == okButton)
        {
          if (randomizeButton.isSelected())
          {
            lastSeed = System.currentTimeMillis();
            seedField.setText(lastSeed + "");
          }
          else if (newSeedButton.isSelected())
          {
            try
            {
              lastSeed = Long.parseLong(seedField.getText());
            }
            catch (NumberFormatException e)
            {
              seedField.setText(lastSeed + "");
            }
          }
          
          try
          {
            numberOfPoints = Integer.parseInt(pointsField.getText());
          }
          catch (NumberFormatException e)
          {
            pointsField.setText(numberOfPoints + "");
          }
      
          try
          {
            density = Integer.parseInt(densityField.getText());
          }
          catch (NumberFormatException e)
          {
            densityField.setText(density + "");
          }

          try
          {
            maxEdgeLength = Integer.parseInt(lengthField.getText());
          }
          catch (NumberFormatException e)
          {
            lengthField.setText(maxEdgeLength + "");
          }

          try
          {
            int x = Integer.parseInt(startX.getText());
            int y = Integer.parseInt(startY.getText());
            start.x = x;
            start.y = y;
          }
          catch (NumberFormatException e)
          {
            startX.setText(start.x + "");
            startY.setText(start.y + "");
          }
          try
          {
            if (goalBox.isSelected())
            {                    
              int x = Integer.parseInt(goalX.getText());
              int y = Integer.parseInt(goalY.getText());
              goal = new Point(x, y);
            }
            else
            {
              goal = null;
            }
          }          
          catch (NumberFormatException e)
          {
            goalX.setText((goal != null ? goal.x : "") + "");
            goalY.setText((goal != null ? goal.y : "") + "");
          }
          
          initGraph();
          topLevelContainer.repaint();
        }
        // else cancel button was pressed, just disappear
        
        GraphSelectionDialog.this.setVisible(false);
      }
      
    }
  }
}


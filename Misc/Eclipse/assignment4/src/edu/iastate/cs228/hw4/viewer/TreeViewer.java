
package edu.iastate.cs228.hw4.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import edu.iastate.cs228.hw4.AstNode;

/**
 * A simple Swing application for viewing an SPL Abstract Syntax Tree in tree
 * form. To use, invoke the start method with the root of the tree to view.
 */
public class TreeViewer extends JPanel
{
  private JTree tree;

  public TreeViewer(AstNode root)
  {
    super(new BorderLayout());

    // Construct the tree and add it to this panel.
    tree = new JTree(new TreeViewerModel(root));
    JScrollPane scrollPane = new JScrollPane(tree);
    scrollPane.setPreferredSize(new Dimension(200, 200));
    add(scrollPane, BorderLayout.CENTER);
  }

  private static void createAndShow(AstNode root)
  {
    // Create and set up the window.
    JFrame frame = new JFrame("AST Viewer");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create and set up the content pane.
    TreeViewer newContentPane = new TreeViewer(root);
    newContentPane.setOpaque(true);
    frame.setContentPane(newContentPane);

    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  public static void start(final AstNode root)
  {
    // Create and start the application on the Swing
    // event-dispatching thread
    javax.swing.SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        createAndShow(root);
      }
    });
  }
}

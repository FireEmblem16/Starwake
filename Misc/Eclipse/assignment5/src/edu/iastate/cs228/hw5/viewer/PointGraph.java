package edu.iastate.cs228.hw5.viewer;
import java.awt.Point;
import edu.iastate.cs228.hw5.ListGraphImpl;

/**
 * Graph of 2D points with a heuristic function h
 * based on straight-line distance.
 */
public class PointGraph extends ListGraphImpl<Point>
{
  @Override
  public int h(Point p, Point q)
  {
    // standard Euclidean distance formula
    double distance = p.distance(q);
    return (int) Math.round(distance * 100);
  }
}


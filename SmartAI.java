import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class SmartAI implements AIModule
{
  public class Node implements Comparable<Node>
  {
    double h;
    double g;
    double f;
    Point state;
    Node parent;

    public Node(Point state, Node parent, TerrainMap map)
    {
      this.state = state;
      this.parent = parent;

      if (this.parent == null){
        this.g = this.h = this.f = 0.0;
      }
      else {
        this.g = map.getCost(state, map.getEndPoint());
        this.h = 0; //getHeuristic();
        this.f = g + h;
      }
    }

    @Override
    //possible error here
    public int compareTo(Node n)
    {
      if (this.f > n.f){
        return 1;
      }
      else if (this.f < n.f){
        return -1;
      }
      else {
        // if (this.h > n.h){
        //   return -1;
        // }
        // else if (this.h < n.h){
        //   return 1;
        // }
        // else {
        return 0;
        // }
      }
    }

    public boolean equals(Object obj)
    {
      if (obj instanceof Node)
      {
        Node node = (Node) obj;
        if (this.state.equals(node.state)){
          return true;
        }
      }
      return false;
    }

    public Point getState()
    {
      return this.state;
    }

    public Node getParent()
    {
      return this.parent;
    }

    public double getH()
    {
      return this.h;
    }

    public double getG()
    {
      return this.g;
    }

    public double getF()
    {
      return this.f;
    }

    public int print()
    {
      System.out.print("point: \t");
      System.out.print("x: " + this.state.x + "\t");
      System.out.print("y: " + this.state.y);
      if (this.parent != null){
        System.out.print("\tparent: ");
        System.out.print("x: " + this.parent.state.x + "\t");
        System.out.print("y: " + this.parent.state.y);
      }
      System.out.print("\tfcost: ");
      System.out.println(this.f + "\t");
      return 0;
    }
  }

  public List<Point> createPath(TerrainMap map)
  {
    //Node end = new Node(map.getEndPoint(), null, map);
    // System.out.println("!!!");
    // end.print();
    int loops = 0;
    Node start = new Node(map.getStartPoint(), null, map);
    PriorityQueue<Node> frontier = new PriorityQueue<Node>();
    List<Point> explored = new ArrayList<Point>();
    frontier.add(start);
    while (frontier.size() > 0){
      //System.out.println();
      Node current = frontier.poll();
      if (explored.contains(current.state)){
        continue;
      }
      loops++;
      current.print();
      System.out.println("loop #" + loops);
      //System.out.println(current.state.equals(map.getEndPoint()));
      if (current.state.equals(map.getEndPoint())){
        List<Point> path = buildPath(current);
        //Collections.reverse(path);
        for (int i = 0; i < path.size(); i++){
          System.out.println(path.get(i));
        }
        return path;
      }

      explored.add(current.state);
      Point[] n = map.getNeighbors(current.state);
      for (Point neighbor : n){
        Node temp = new Node(neighbor, current, map);

        if (!frontier.contains(temp) && !explored.contains(neighbor)){
          frontier.add(temp);
        }
        else if (frontier.contains(temp)){
          Node[] others = frontier.toArray(new Node[frontier.size()]);
          Node other = findVal(others, temp);

          if (temp.f == other.f){
            if (temp.h > other.h){
              frontier.remove(other);
              frontier.add(temp);
            }
          }
          else if (temp.f < other.f){
            frontier.remove(other);
            frontier.add(temp);
          }
        }
      }
    }
    return null;
  }

  public Node findVal(Node[] pQueue, Node find)
  {
    for(Node node: pQueue){
      if (node.equals(find)){
        return node;
      }
    }
    return null;
  }

  public List<Point> buildPath(Node current)
  {
    List<Point> path = new ArrayList<>();
    while (current.parent != null){
      //current.print();
      path.add(current.state);
      current = current.parent;
    }
    return path;
  }

  public double getHeuristic(final Point current, TerrainMap map) //todo (later)
  {
    double dx = Math.abs(current.x - map.getEndPoint().x);
    double dy = Math.abs(current.y - map.getEndPoint().y);
    double costStraight = 1;
    double costDiagonal = Math.sqrt(2);

    //return costStraight*Math.max(dx,dy)+(costDiagonal-costStraight)*Math.min(dx,dy);
    return 0;
  }
}

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
        this.g = map.getCost(this.state, map.getEndPoint());
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

      return 0;
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
    int loops = 0;
    Node start = new Node(map.getStartPoint(), null, map);
    PriorityQueue<Node> frontier = new PriorityQueue<Node>(1, new Comparator<Node>(){
      public int compare(Node node1, Node node2)
      {
        return node1.compareTo(node2);
      }
    });
    List<Point> explored = new ArrayList<Point>();
    frontier.add(start);

    while (frontier.size() > 0){
      Node current = frontier.poll();

      if (explored.contains(current.state)){
        continue;
      }
      loops++;
      current.print();
      System.out.println("loop #" + loops);

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
          Node[] contents = frontier.toArray(new Node[frontier.size()]);
          Node other = findVal(contents, temp);
          if (temp.f < other.f){
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
    path.add(current.state);
    return path;
  }

  public double getHeuristic(final Point current, TerrainMap map)
  {
    double dx = Math.abs(current.x - map.getEndPoint().x);
    double dy = Math.abs(current.y - map.getEndPoint().y);
    double costStraight = 1;
    double costDiagonal = Math.sqrt(2);

    //return costStraight*Math.max(dx,dy)+(costDiagonal-costStraight)*Math.min(dx,dy);
    return 0;
  }
}

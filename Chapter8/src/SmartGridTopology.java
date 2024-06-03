
import java.util.*;

class SmartGridTopology {

  static class Node {
    private String name;
    private List<Node> neighbors;
    private double powerConsumed;

    Node(String name) {
      this.name = name;
      this.neighbors = new ArrayList<>();
      this.powerConsumed = 0.0;
    }

    void addNeighbor(Node node) {
      this.neighbors.add(node);
    }

    void setPowerConsumed(double powerConsumed) {
      this.powerConsumed = powerConsumed;
    }

    String getName() {
      return this.name;
    }

    List<Node> getNeighbors() {
      return this.neighbors;
    }

    double getPowerConsumed() {
      return this.powerConsumed;
    }
  }

  public static void main(String[] args) {
    Node node1 = new Node("Node 1");
    Node node2 = new Node("Node 2");
    Node node3 = new Node("Node 3");
    Node node4 = new Node("Node 4");
    Node node5 = new Node("Node 5");

    node1.addNeighbor(node2);
    node1.addNeighbor(node3);
    node2.addNeighbor(node3);
    node2.addNeighbor(node4);
    node3.addNeighbor(node5);
    node4.addNeighbor(node5);

    node1.setPowerConsumed(100.0);
    node2.setPowerConsumed(80.0);
    node3.setPowerConsumed(120.0);
    node4.setPowerConsumed(70.0);
    node5.setPowerConsumed(90.0);

    System.out.println("Node\tNeighbors\tPower Consumed");
    System.out.println("----\t---------\t--------------");
    System.out.println(node1.getName() + "\t" + node1.getNeighbors().size() + "\t\t" + node1.getPowerConsumed());
    System.out.println(node2.getName() + "\t" + node2.getNeighbors().size() + "\t\t" + node2.getPowerConsumed());
    System.out.println(node3.getName() + "\t" + node3.getNeighbors().size() + "\t\t" + node3.getPowerConsumed());
    System.out.println(node4.getName() + "\t" + node4.getNeighbors().size() + "\t\t" + node4.getPowerConsumed());
    System.out.println(node5.getName() + "\t" + node5.getNeighbors().size() + "\t\t" + node5.getPowerConsumed());
  }
}

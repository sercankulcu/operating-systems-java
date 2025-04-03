import java.util.*;
import java.util.stream.Collectors;

/*
 * This enhanced program models a smart grid topology with:
 * - Bidirectional node connections
 * - Validation for neighbors and power consumption
 * - Formatted output with neighbor details
 * - Basic grid analysis (total power)
 */
class SmartGridTopology {
    static class Node {
        private String name;
        private Set<Node> neighbors;  // Using Set to avoid duplicates
        private double powerConsumed;

        Node(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Node name cannot be null or empty");
            }
            this.name = name;
            this.neighbors = new HashSet<>();  // Set for unique neighbors
            this.powerConsumed = 0.0;
        }

        void addNeighbor(Node node) {
            if (node == null) {
                throw new IllegalArgumentException("Cannot add null neighbor");
            }
            if (node != this && !neighbors.contains(node)) {  // Prevent self-loops and duplicates
                this.neighbors.add(node);
                node.neighbors.add(this);  // Bidirectional connection
            }
        }

        void setPowerConsumed(double powerConsumed) {
            if (powerConsumed < 0) {
                throw new IllegalArgumentException("Power consumed cannot be negative");
            }
            this.powerConsumed = powerConsumed;
        }

        String getName() {
            return this.name;
        }

        Set<Node> getNeighbors() {
            return this.neighbors;
        }

        double getPowerConsumed() {
            return this.powerConsumed;
        }

        String getNeighborNames() {
            return neighbors.stream()
                .map(Node::getName)
                .collect(Collectors.joining(", "));
        }
    }

    public static void main(String[] args) {
        // Create nodes
        List<Node> nodes = new ArrayList<>();
        try {
            Node node1 = new Node("Node 1");
            Node node2 = new Node("Node 2");
            Node node3 = new Node("Node 3");
            Node node4 = new Node("Node 4");
            Node node5 = new Node("Node 5");

            // Add to list for easy iteration
            nodes.addAll(Arrays.asList(node1, node2, node3, node4, node5));

            // Set up topology (bidirectional)
            node1.addNeighbor(node2);
            node1.addNeighbor(node3);
            node2.addNeighbor(node3);
            node2.addNeighbor(node4);
            node3.addNeighbor(node5);
            node4.addNeighbor(node5);

            // Set power consumption
            node1.setPowerConsumed(100.0);
            node2.setPowerConsumed(80.0);
            node3.setPowerConsumed(120.0);
            node4.setPowerConsumed(70.0);
            node5.setPowerConsumed(90.0);

            // Print topology
            System.out.println("Smart Grid Topology Report");
            System.out.printf("%-10s %-15s %-20s%n", "Node", "Power Consumed", "Neighbors");
            System.out.println("--------------------------------------------");
            for (Node node : nodes) {
                System.out.printf("%-10s %-15.1f %-20s%n", 
                    node.getName(), 
                    node.getPowerConsumed(), 
                    node.getNeighborNames());
            }

            // Calculate and display total power
            double totalPower = nodes.stream()
                .mapToDouble(Node::getPowerConsumed)
                .sum();
            System.out.println("\nTotal Power Consumption: " + totalPower + " units");

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CityGraph {
    private Map<String, List<String>> adjacencyList;

    public CityGraph() {
        adjacencyList = new HashMap<>();
    }

    // Function to read city information from the text file and build the adjacency list.
    public void readCityInformation(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                String[] cities = line.split(" ");
                String city1 = cities[0];
                String city2 = cities[1];

                // Add bidirectional edges to the adjacency list
                addEdge(city1, city2);
                addEdge(city2, city1);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to add an edge to the adjacency list.
    private void addEdge(String city1, String city2) {
        if (!adjacencyList.containsKey(city1)) {
            adjacencyList.put(city1, new ArrayList<>());
        }
        adjacencyList.get(city1).add(city2);
    }

    // Function to perform depth-first search (DFS).
    public void depthFirstSearch(String startCity, String endCity) {
        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();
        path.add(startCity);

        boolean foundPath = dfsHelper(startCity, endCity, visited, path);

        if (!foundPath) {
            System.out.println("No path found from " + startCity + " to " + endCity);
        }
    }

    private boolean dfsHelper(String currentCity, String endCity, Set<String> visited, List<String> path) {
        if (currentCity.equals(endCity)) {
            System.out.println("Path from " + path.get(0) + " to " + endCity + ": " + String.join(" -> ", path));
            return true;
        }

        visited.add(currentCity);

        if (adjacencyList.containsKey(currentCity)) {
            for (String neighbor : adjacencyList.get(currentCity)) {
                if (!visited.contains(neighbor)) {
                    path.add(neighbor);
                    if (dfsHelper(neighbor, endCity, visited, path)) {
                        return true; // Path found, stop searching
                    }
                    path.remove(path.size() - 1); // Backtrack
                }
            }
        }

        visited.remove(currentCity);
        return false; // No path found from this city
    }

    public void breadthFirstSearch(String startCity, String endCity) {
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();

        queue.add(startCity);
        visited.add(startCity);

        while (!queue.isEmpty()) {
            String currentCity = queue.poll();

            if (currentCity.equals(endCity)) {
                // Reconstruct and print the path from endCity to startCity
                List<String> path = new ArrayList<>();
                path.add(endCity);
                String parent = parentMap.get(endCity);
                while (parent != null) {
                    path.add(parent);
                    parent = parentMap.get(parent);
                }
                Collections.reverse(path);
                System.out.println("Path from " + startCity + " to " + endCity + ": " + String.join(" -> ", path));
                return;
            }

            if (adjacencyList.containsKey(currentCity)) {
                for (String neighbor : adjacencyList.get(currentCity)) {
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                        parentMap.put(neighbor, currentCity);
                    }
                }
            }
        }

        System.out.println("No path found from " + startCity + " to " + endCity);
    }

    public void iterativeDeepeningDFS(String startCity, String endCity) {
        int maxDepth = 0;
        while (true) {
            Set<String> visited = new HashSet<>();
            List<String> path = new ArrayList<>();
            path.add(startCity);

            boolean found = iddfsHelper(startCity, endCity, visited, path, maxDepth);

            if (found) {
                System.out.println("Path from " + startCity + " to " + endCity + ": " + String.join(" -> ", path));
                return; // Path found, exit the search
            }

            maxDepth++;
            if (maxDepth >= adjacencyList.size()) {
                System.out.println("No path found from " + startCity + " to " + endCity);
                return; // No path found within the entire graph
            }
        }
    }

    private boolean iddfsHelper(String currentCity, String endCity, Set<String> visited, List<String> path, int maxDepth) {
        if (currentCity.equals(endCity)) {
            return true; // Path found
        }

        if (path.size() > maxDepth) {
            return false; // Reached the depth limit
        }

        visited.add(currentCity);

        if (adjacencyList.containsKey(currentCity)) {
            for (String neighbor : adjacencyList.get(currentCity)) {
                if (!visited.contains(neighbor)) {
                    path.add(neighbor);
                    if (iddfsHelper(neighbor, endCity, visited, path, maxDepth)) {
                        return true; // Path found
                    }
                    path.remove(path.size() - 1); // Backtrack
                }
            }
        }

        visited.remove(currentCity);
        return false; // No path found
    }

    // Function to perform Best-First Search (BFS) with a heuristic.
    public void bestFirstSearch(String startCity, String endCity) {
        PriorityQueue<CityNode> priorityQueue = new PriorityQueue<>(new Comparator<CityNode>() {
            @Override
            public int compare(CityNode node1, CityNode node2) {
                // Compare based on heuristic value (estimated distance to the endCity).
                return Double.compare(node1.heuristic, node2.heuristic);
            }
        });

        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();

        // Create a starting node with a heuristic value (distance estimate).
        CityNode startNode = new CityNode(startCity, 0);
        priorityQueue.add(startNode);
        visited.add(startCity);

        while (!priorityQueue.isEmpty()) {
            CityNode currentNode = priorityQueue.poll();

            // Check if we have reached the destination city.
            if (currentNode.city.equals(endCity)) {
                // Reconstruct and print the path from endCity to startCity.
                List<String> path = new ArrayList<>();
                path.add(endCity);
                String parent = parentMap.get(endCity);
                while (parent != null) {
                    path.add(parent);
                    parent = parentMap.get(parent);
                }
                Collections.reverse(path);
                System.out.println("Path from " + startCity + " to " + endCity + ": " + String.join(" -> ", path));
                return;
            }

            // Explore neighbors of the current city.
            if (adjacencyList.containsKey(currentNode.city)) {
                for (String neighbor : adjacencyList.get(currentNode.city)) {
                    if (!visited.contains(neighbor)) {
                        double heuristic = computeHeuristic(neighbor, endCity); // Heuristic value
                        CityNode neighborNode = new CityNode(neighbor, heuristic);
                        priorityQueue.add(neighborNode);
                        visited.add(neighbor);
                        parentMap.put(neighbor, currentNode.city); // Store parent for path reconstruction.
                    }
                }
            }
        }

        System.out.println("No path found from " + startCity + " to " + endCity);
    }

    // Function to compute the heuristic value (straight-line distance) between two cities.
    private double computeHeuristic(String city1, String city2) {

        return 0;
    }
    private class CityNode {
        String city;
        double heuristic;

        public CityNode(String city, double heuristic) {
            this.city = city;
            this.heuristic = heuristic;
        }
    }


    public void aStarSearch(String startCity, String endCity) {
        PriorityQueue<CityCost> priorityQueue = new PriorityQueue<>(new Comparator<CityCost>() {
            @Override
            public int compare(CityCost city1, CityCost city2) {
                // Compare based on the sum of the path cost and heuristic value.
                double cost1 = city1.pathCost + computeHeuristic(city1.name, endCity);
                double cost2 = city2.pathCost + computeHeuristic(city2.name, endCity);
                return Double.compare(cost1, cost2);
            }
        });

        Map<String, Double> costSoFar = new HashMap<>();
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();

        // Create a starting node with a heuristic value (distance estimate) and path cost.
        CityCost startNode = new CityCost(startCity, 0);
        priorityQueue.add(startNode);
        costSoFar.put(startCity, 0.0);
        visited.add(startCity);

        while (!priorityQueue.isEmpty()) {
            CityCost currentCity = priorityQueue.poll();

            // Check if we have reached the destination city.
            if (currentCity.name.equals(endCity)) {
                // Reconstruct and print the path from endCity to startCity.
                List<String> path = new ArrayList<>();
                path.add(endCity);
                String parent = parentMap.get(endCity);
                while (parent != null) {
                    path.add(parent);
                    parent = parentMap.get(parent);
                }
                Collections.reverse(path);
                System.out.println("Path from " + startCity + " to " + endCity + ": " + String.join(" -> ", path));
                return;
            }

            // Explore neighbors of the current city.
            if (adjacencyList.containsKey(currentCity.name)) {
                for (String neighbor : adjacencyList.get(currentCity.name)) {
                    if (!visited.contains(neighbor)) {
                        double cost = costSoFar.get(currentCity.name) + 1; // Assuming equal edge costs
                        CityCost neighborCity = new CityCost(neighbor, cost);
                        priorityQueue.add(neighborCity);
                        costSoFar.put(neighbor, cost);
                        visited.add(neighbor);
                        parentMap.put(neighbor, currentCity.name); // Store parent for path reconstruction.
                    }
                }
            }
        }

        System.out.println("No path found from " + startCity + " to " + endCity);
    }

    private class CityCost {
        String name;
        double pathCost;

        public CityCost(String name, double pathCost) {
            this.name = name;
            this.pathCost = pathCost;
        }
    }

    // Function to perform brute-force search to find a path between two cities.
    public void bruteForceSearch(String startCity, String endCity) {
        List<String> path = new ArrayList<>();
        path.add(startCity);

        boolean foundPath = bruteForceHelper(startCity, endCity, path);

        if (foundPath) {
            System.out.println("Path from " + startCity + " to " + endCity + ": " + String.join(" -> ", path));
        } else {
            System.out.println("No path found from " + startCity + " to " + endCity);
        }
    }

    private boolean bruteForceHelper(String currentCity, String endCity, List<String> path) {
        if (currentCity.equals(endCity)) {
            return true; // Path found
        }

        if (adjacencyList.containsKey(currentCity)) {
            for (String neighbor : adjacencyList.get(currentCity)) {
                if (!path.contains(neighbor)) {
                    path.add(neighbor);
                    if (bruteForceHelper(neighbor, endCity, path)) {
                        return true; // Path found
                    }
                    path.remove(path.size() - 1); // Backtrack
                }
            }
        }

        return false; // No path found from this city
    }




}

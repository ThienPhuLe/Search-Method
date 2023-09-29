import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CoordinateCity {
    private Map<String, City> cities = new HashMap<>();

    public class City {
        String name;
        double latitude;
        double longitude;
        List<City> neighbors;

        public City(String name, double latitude, double longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.neighbors = new ArrayList<>();
        }

        public void addNeighbor(City neighbor) {
            neighbors.add(neighbor);
        }
    }

    // Calculate distance between two cities using Haversine formula
    private double calculateDistance(City city1, City city2) {
        double lat1 = Math.toRadians(city1.latitude);
        double lon1 = Math.toRadians(city1.longitude);
        double lat2 = Math.toRadians(city2.latitude);
        double lon2 = Math.toRadians(city2.longitude);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double radius = 6371; // Earth's radius in kilometers

        return radius * c;
    }

    // Constructor to read coordinates from the file and create the graph
    public CoordinateCity(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                double latitude = Double.parseDouble(parts[1].trim());
                double longitude = Double.parseDouble(parts[2].trim());

                City city = new City(name, latitude, longitude);
                cities.put(name, city);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Establish adjacency based on distance threshold
        double distanceThreshold = 100.0; // Adjust this threshold as needed
        for (City city1 : cities.values()) {
            for (City city2 : cities.values()) {
                if (city1 != city2 && calculateDistance(city1, city2) <= distanceThreshold) {
                    city1.addNeighbor(city2);
                }
            }
        }
    }

    private void printPath(Map<City, City> parentMap, City start, City end) {
        List<String> path = new ArrayList<>();
        City current = end;

        while (current != start) {
            path.add(current.name);
            current = parentMap.get(current);
        }

        path.add(start.name);
        Collections.reverse(path);

        System.out.println("Path: " + String.join(" -> ", path));
    }


    // Depth-First Search
    public void dfs(String startCity, String endCity) {
        City start = cities.get(startCity);
        City end = cities.get(endCity);


        Stack<City> stack = new Stack<>();
        stack.push(start);
        Map<City, Boolean> visited = new HashMap<>();
        visited.put(start, true);

        Map<City, City> parentMap = new HashMap<>();

        while (!stack.isEmpty()) {
            City currentCity = stack.pop();
            System.out.println("Visiting: " + currentCity.name);

            if (currentCity == end) {
                printPath(parentMap, start, end);
                return;
            }

            for (City neighbor : currentCity.neighbors) {
                if (!visited.containsKey(neighbor)) {
                    stack.push(neighbor);
                    visited.put(neighbor, true);
                    parentMap.put(neighbor, currentCity);
                }
            }
        }

    }


    // Breadth-first search
    public void bfs(String startCity, String endCity) {
        City start = cities.get(startCity);
        City end = cities.get(endCity);


        Queue<City> queue = new LinkedList<>();
        queue.add(start);
        Map<City, Boolean> visited = new HashMap<>();
        visited.put(start, true);

        Map<City, City> parentMap = new HashMap<>();

        while (!queue.isEmpty()) {
            City currentCity = queue.poll();
            System.out.println("Visiting: " + currentCity.name);
            if (currentCity == end) {
                printPath(parentMap, start, end);
                return;
            }

            for (City neighbor : currentCity.neighbors) {
                if (!visited.containsKey(neighbor)) {
                    queue.add(neighbor);
                    visited.put(neighbor, true);
                    parentMap.put(neighbor, currentCity);
                }
            }
        }

    }

    // Iterative Deepening Depth-First Search (ID-DFS)
    public void idDfs(String startCity, String endCity) {
        City start = cities.get(startCity);
        City end = cities.get(endCity);


        int maxDepth = 100; // Set a maximum depth limit, adjust as needed

        for (int depthLimit = 0; depthLimit <= maxDepth; depthLimit++) {
            Map<City, Boolean> visited = new HashMap<>();
            Map<City, City> parentMap = new HashMap<>();
            if (idDfsRecursive(start, end, depthLimit, visited, parentMap)) {
                printPath(parentMap, start, end);
                return;
            }
        }

    }

    private boolean idDfsRecursive(City currentCity, City endCity, int depthLimit, Map<City, Boolean> visited, Map<City, City> parentMap) {
        if (depthLimit < 0) {
            return false; // Depth limit reached without finding the destination
        }

        if (currentCity == endCity) {
            return true; // Destination found
        }

        visited.put(currentCity, true);

        for (City neighbor : currentCity.neighbors) {
            if (!visited.containsKey(neighbor)) {
                parentMap.put(neighbor, currentCity);
                if (idDfsRecursive(neighbor, endCity, depthLimit - 1, visited, parentMap)) {
                    System.out.println("Visiting: " + currentCity.name);
                    return true;
                }
            }
        }

        return false; // Destination not found within the depth limit
    }

    // Best-First Search (BFS) using Euclidean distance as the heuristic
    public void bestFirstSearch(String startCity, String endCity) {
        City start = cities.get(startCity);
        City end = cities.get(endCity);


        PriorityQueue<City> priorityQueue = new PriorityQueue<>(new Comparator<City>() {
            @Override
            public int compare(City city1, City city2) {
                // Use Euclidean distance as the heuristic
                double distance1 = calculateEuclideanDistance(city1, end);
                double distance2 = calculateEuclideanDistance(city2, end);
                return Double.compare(distance1, distance2);
            }
        });

        Map<City, Boolean> visited = new HashMap<>();
        priorityQueue.add(start);
        visited.put(start, true);

        Map<City, City> parentMap = new HashMap<>();

        while (!priorityQueue.isEmpty()) {
            City currentCity = priorityQueue.poll();
            System.out.println("Visiting: " + currentCity.name);
            if (currentCity == end) {
                printPath(parentMap, start, end);
                return;
            }

            for (City neighbor : currentCity.neighbors) {
                if (!visited.containsKey(neighbor)) {
                    priorityQueue.add(neighbor);
                    visited.put(neighbor, true);
                    parentMap.put(neighbor, currentCity);
                }
            }
        }

    }

    // Calculate Euclidean distance between two cities
    private double calculateEuclideanDistance(City city1, City city2) {
        double dx = city1.latitude - city2.latitude;
        double dy = city1.longitude - city2.longitude;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // A* Search using calculateDistance as the path cost function and Euclidean distance as the heuristic
    // A* Search using calculateDistance as the path cost function and Euclidean distance as the heuristic
    public void aStarSearch(String startCity, String endCity) {
        City start = cities.get(startCity);
        City end = cities.get(endCity);

        if (start == null || end == null) {
            return;
        }

        PriorityQueue<City> priorityQueue = new PriorityQueue<>(new Comparator<City>() {
            @Override
            public int compare(City city1, City city2) {
                // Calculate the total cost (f(n)) for each city using g(n) + h(n)
                double cost1 = calculateDistance(city1, start) + calculateEuclideanDistance(city1, end);
                double cost2 = calculateDistance(city2, start) + calculateEuclideanDistance(city2, end);
                return Double.compare(cost1, cost2);
            }
        });

        Map<City, Double> pathCosts = new HashMap<>();
        Map<City, City> parentMap = new HashMap<>();

        priorityQueue.add(start);
        pathCosts.put(start, 0.0);

        while (!priorityQueue.isEmpty()) {
            City currentCity = priorityQueue.poll();
            System.out.println("Visiting: " + currentCity.name);

            if (currentCity == end) {
                printPath(parentMap, start, end);
                return;
            }

            for (City neighbor : currentCity.neighbors) {
                double newCost = pathCosts.get(currentCity) + calculateDistance(currentCity, neighbor);

                if (!pathCosts.containsKey(neighbor) || newCost < pathCosts.get(neighbor)) {
                    pathCosts.put(neighbor, newCost);

                    // No need to calculate priority here
                    priorityQueue.add(neighbor);

                    // Update the parent map
                    parentMap.put(neighbor, currentCity);
                }
            }
        }
    }

    // Function to perform brute-force search to find a path between two cities.
    public void bruteForceSearch(String startCity, String endCity) {
        City start = cities.get(startCity);
        City end = cities.get(endCity);

        List<String> path = new ArrayList<>();
        path.add(startCity);

        boolean foundPath = bruteForceHelper(start, end, path);

        if (foundPath) {
            System.out.println("Path from " + startCity + " to " + endCity + ": " + String.join(" -> ", path));
        } else {
            System.out.println("No path found from " + startCity + " to " + endCity);
        }
    }

    private boolean bruteForceHelper(City currentCity, City endCity, List<String> path) {
        if (currentCity == endCity) {
            return true; // Path found
        }

        for (City neighbor : currentCity.neighbors) {
            if (!path.contains(neighbor.name)) {
                path.add(neighbor.name);
                if (bruteForceHelper(neighbor, endCity, path)) {
                    System.out.println("Visiting: " + currentCity.name);
                    return true; // Path found
                }
                path.remove(path.size() - 1); // Backtrack
            }
        }

        return false; // No path found from this city
    }
}
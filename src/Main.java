import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        CityGraph cityGraph = new CityGraph();
        CoordinateCity cityData = new CoordinateCity("coordinates.csv");
        String answer = "X";
        boolean z = true;
        cityGraph.readCityInformation("Adjacencies.txt");
        System.out.println("Please select the following option: ");
        System.out.println("1. Adjacencies.txt file ");
        System.out.println("2. coordinates.csv file ");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter: ");
        int selection = scanner.nextInt();
        switch (selection){
            case 1:
                while (z == true)
                {
                    System.out.println("Please select the following option: ");
                    System.out.println("1. undirected (blind) brute-force approach ");
                    System.out.println("2. breadth-first search ");
                    System.out.println("3. depth-first search ");
                    System.out.println("4. ID-DFS search ");
                    System.out.println("4. best-first search ");
                    System.out.println("4. A* search ");
                    scanner = new Scanner(System.in);
                    System.out.print("Enter: ");
                    selection = scanner.nextInt();

                    scanner = new Scanner(System.in);
                    System.out.print("Enter the starting city: ");
                    String startCity = scanner.nextLine();
                    System.out.print("Enter the ending city: ");
                    String endCity = scanner.nextLine();


                    switch (selection){
                        case 1:
                            cityGraph.bruteForceSearch(startCity,endCity);
                            break;
                        case 2:
                            cityGraph.breadthFirstSearch(startCity, endCity);
                            break;
                        case 3:
                            cityGraph.depthFirstSearch(startCity, endCity);
                            break;
                        case 4:
                            cityGraph.iterativeDeepeningDFS(startCity, endCity);
                            break;
                        case 5:
                            cityGraph.bestFirstSearch(startCity, endCity);
                            break;
                        case 6:cityGraph.aStarSearch(startCity, endCity);
                            break;
                    }
                    System.out.println("Please select the following option: (Y = continue) or (N = Exit) ");
                    scanner = new Scanner(System.in);
                    System.out.print("Enter: ");
                    answer = scanner.nextLine();

                    if (answer.equalsIgnoreCase("N"))
                    {
                        z = false;
                    }
                }
                break;

            case 2:
                while (z == true) {
                    System.out.println("Please select the following option: ");
                    System.out.println("1. undirected (blind) brute-force approach ");
                    System.out.println("2. breadth-first search ");
                    System.out.println("3. depth-first search ");
                    System.out.println("4. ID-DFS search ");
                    System.out.println("4. best-first search ");
                    System.out.println("4. A* search ");
                    scanner = new Scanner(System.in);
                    System.out.print("Enter: ");
                    selection = scanner.nextInt();

                    scanner = new Scanner(System.in);
                    System.out.print("Enter the starting city: ");
                    String startCity = scanner.nextLine();
                    System.out.print("Enter the ending city: ");
                    String endCity = scanner.nextLine();
                    switch (selection){
                        case 1:
                            cityData.bruteForceSearch(startCity,endCity);
                            break;
                        case 2:
                            cityData.bfs(startCity, endCity);
                            break;
                        case 3:
                            cityData.dfs(startCity, endCity);
                            break;
                        case 4:
                            cityData.idDfs(startCity, endCity);
                            break;
                        case 5:
                            cityData.bestFirstSearch(startCity, endCity);
                            break;
                        case 6:
                            cityData.aStarSearch(startCity, endCity);
                            break;
                    }
                    System.out.println("Please select the following option: (Y = continue) or (N = Exit) ");
                    scanner = new Scanner(System.in);
                    System.out.print("Enter: ");
                    answer = scanner.nextLine();


                    if (answer.equalsIgnoreCase("N"))
                    {
                        z = false;
                    }

                }
                break;

        }
    }
}
//Wichita Manhattan
//Wichita Topeka

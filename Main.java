import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main{


	private static <V,E> void runGame(String moviePath, String actorPath, String movieActorPath) throws Exception {
		boolean isValid = false, play = true;

		Scanner input = new Scanner(System.in);
		Graph<String, Set<String>> graph = GraphLibrary.makeGraph(GraphLibrary.fileToMap(moviePath), GraphLibrary.fileToMap(actorPath), movieActorPath);
    Graph<String, Set<String>> bfs = GraphLibrary.bfs(graph, CoU);


		String strIn;
		String CoU = "";

		while(play) {
			System.out.println("What would you like to do?\n Commands:\r\n" +
					"c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation\r\n" +
					"d <low> <high>: list actors sorted by degree, with degree between low and high\r\n" +
					"i: list actors with infinite separation from the current center\r\n" +
					"p <name>: find path from <name> to current center of the universe\r\n" +
					"s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high\r\n" +
					"u <name>: make <name> the center of the universe\r\n" +
					"q: quit game");

			strIn = input.nextLine();
			char toDo = strIn.charAt(0);
			while(!isValid) {
				if(toDo == 'c') {
          int order = Integer.parseInt(strIn.substring(2));
          if (order < bfs.numVertices()){
             String direction = "top";
             if (order < 0) {
               direction = "bottom";
             }
				   	 System.out.println("List of " + direction + " " + order + " centers of the universe, sorted by average separation: ");
             System.out.println(GraphLibrary.sortByAvStep(graph, order));
           }
           else{
             System.out.println("That number is too big. Try a smaller one.")
           }
      	}else if(toDo == 'd') {
					int low = Integer.parseInt(strIn.substring(2, strIn.lastIndexOf(" ")));
					int high = Integer.parseInt(strIn.substring(strIn.lastIndexOf(" ") + 1));
					GraphLibrary.sortByDegree(graph, low, high);
				}else if(toDo == 'i') {
					System.out.println("The following actors do not appear in a movie with " + CoU + " : " + GraphLibrary.missingVertices(graph, bfs));
				}else if(toDo == 'p') {
					String person = strIn.substring(2);
          if (bfs.hasVertex(person)){
					  List<String> list = GraphLibrary.getPath(bfs, person);
            int num = list.size();
					  System.out.println("p " + person);
            System.out.println(person + "\\'s number is " + num);
            for (int i = 0; i < list.size()-1; i ++){
              System.out.println(list.get(i) + " appeared in " + bfs.getLabel(list.get(i), list.get(i+1)) + " with " + list.get(i+1));
            }
          }
          else{
            System.out.println("Sorry, that person is not connected to " + CoU);
          }
				}else if(toDo == 's') {
          int low = Integer.parseInt(strIn.substring(2, strIn.lastIndexOf(" ")));
          int high = Integer.parseInt(strIn.substring(strIn.lastIndexOf(" ") + 1));
					GraphLibrary.sortByDegree(graph, low, high);
				}else if(toDo == 'u') {
					CoU = strIn.substring(2);
          if (bfs.hasVertex(CoU)){
            System.out.println(CoU + "game >");
          }
          else{
            System.out.println("Sorry, that actor is not in this dataset. Try someone new.");
          }
				}else if(toDo == 'q') {
					play = false;
				}else {
					System.out.println("Please use a valid command.");
				}
			}
		}
		input.close();
	}

	private static void handDrawn() throws Exception {
		Graph<String, String> relationships = new AdjacencyMapGraph<String, String>();
	    String startNode = "Kevin Bacon";

			relationships.insertVertex("Dartmouth (Earl thereof)");
			relationships.insertVertex(startNode);
			relationships.insertVertex("Alice");
			relationships.insertVertex("Bob");
			relationships.insertVertex("Charlie");
	    relationships.insertVertex("Nobody");
	    relationships.insertVertex("Nobody's Friend");
			relationships.insertUndirected("Charlie", "Dartmouth (Earl thereof)", "B Movie");
			relationships.insertUndirected("Bob", "Alice", "A Movie");
			relationships.insertUndirected("Charlie", "Bob", "C Movie");
			relationships.insertUndirected("Alice", "Charlie", "D Movie");
			relationships.insertUndirected(startNode, "Bob", "A Movie");
			relationships.insertUndirected("Alice", startNode, "A Movie"); // not symmetric!
	    relationships.insertUndirected("Alice", startNode, "E Movie"); // not symmetric!

			System.out.println("The graph:");
			System.out.println(relationships);

	    System.out.println(GraphLibrary.bfs(relationships, startNode));
	}


	public static void main(String[]args) throws Exception{
		handDrawn();
		//runGame("moviesTest.txt", "actorsTest.txt", "movie-actor.txt");
		//runGame("movies.txt", "actors.txt", "movie-actors.txt");
	}
}

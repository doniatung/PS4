import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main{


	private static <V,E> void runGame(String moviePath, String actorPath, String movieActorPath) throws Exception {
		boolean isValid = false, play = true;
		Scanner input = new Scanner(System.in);
		Graph<String, Set<String>> graph = GraphLibrary.makeGraph(GraphLibrary.fileToMap(moviePath), GraphLibrary.fileToMap(actorPath), movieActorPath);
		Graph<String, Set<String>> bfs = null;
		String strIn;
		String CoU = "";
		
		System.out.println("What would you like to do?\nCommands:\r\n" +
				"c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation\r\n" +
				"d <low> <high>: list actors sorted by degree, with degree between low and high\r\n" +
				"i: list actors with infinite separation from the current center\r\n" +
				"p <name>: find path from <name> to current center of the universe\r\n" +
				"s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high\r\n" +
				"u <name>: make <name> the center of the universe\r\n" +
				"q: quit game");
		
		while(play) {
			strIn = input.nextLine();
			char toDo = strIn.charAt(0);
			isValid = false;
			while(!isValid) {
				if(toDo == 'c') {
					
					int order = Integer.parseInt(strIn.substring(strIn.indexOf("<") + 1, strIn.indexOf(">")));
					if (order < bfs.numVertices()){
						String direction = "top";
						if (order < 0) {
							direction = "bottom";
						}
				   	 	System.out.println("List of " + direction + " " + Math.abs(order) + " centers of the universe, sorted by average separation: ");
				   	 	GraphLibrary.sortByAvSep(bfs, order);
					}
					else{
						System.out.println("That number is too big. Try a smaller one.");
					}
					isValid = true;
					
				}else if(toDo == 'd') {
				
					int low = Integer.parseInt(strIn.substring(strIn.indexOf("<") + 1, strIn.indexOf(">")));
					
					int high = Integer.parseInt(strIn.substring(
							strIn.indexOf("<", strIn.indexOf(">")) + 1, 
							strIn.indexOf(">", strIn.indexOf(">"))));
					
					GraphLibrary.sortByDegree(graph, low, high);
					isValid = true;
					
				}else if(toDo == 'i') {
				
					System.out.println("The following actors do not appear in a movie with " + CoU + " : " + GraphLibrary.missingVertices(graph, bfs));
					isValid = true;
					
				}else if(toDo == 'p') {
					
					String person = strIn.substring(strIn.indexOf("<") + 1, strIn.indexOf(">"));
					if (bfs != null && bfs.hasVertex(person)){
						List<String> list = GraphLibrary.getPath(bfs, person);
						int num = list.size() - 1;
						System.out.println("p " + person);
						System.out.println(person + "\\'s number is " + num);
						for (int i = 0; i < list.size(); i ++){
							System.out.println(list.get(i) + " appeared in " + bfs.getLabel(list.get(i), list.get(i+1)) + " with " + list.get(i+1));
						}
					}else if(bfs == null){
						System.out.println("No universe created.");
					}
					else{
						System.out.println("Sorry, that person is not connected to " + CoU);
					}
					isValid = true;
				}else if(toDo == 's') {
					
					int low = Integer.parseInt(strIn.substring(strIn.indexOf("<") + 1, strIn.indexOf(">")));
					int high = Integer.parseInt(strIn.substring(strIn.indexOf("<", strIn.indexOf(">")) + 1, strIn.indexOf(">", strIn.indexOf(strIn.indexOf(">")))));
					GraphLibrary.sortByDegree(graph, low, high);
					isValid = true;
					
				}else if(toDo == 'u') {
					
					CoU = strIn.substring(strIn.indexOf("<") + 1, strIn.indexOf(">"));
					if (graph.hasVertex(CoU)){
						System.out.println(CoU + " game >");
						bfs = GraphLibrary.bfs(graph, CoU);
					}else{
						
						System.out.println("Sorry, that actor is not in this dataset. Try someone new.");
						
					}
					isValid = true;
					
				}else if(toDo == 'q') {
					
					isValid = true;
					play = false;
					System.out.println("Thanks for playing!");
					
				
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
		//handDrawn();
		runGame("PS4\\bacon\\moviesTest.txt", "PS4\\bacon\\actorsTest.txt", "PS4\\bacon\\movie-actorsTest.txt");
		//runGame("movies.txt", "actors.txt", "movie-actors.txt");
	}
}
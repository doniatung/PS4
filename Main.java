import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * This method runs the Kevin Bacon game.
 * @author Donia Tung, CS10, Dartmouth Fall 2018
 * @author Lucas Rathgeb, CS10, Dartmouth Fall 2018
 *
 */
public class Main{


	/**
	 * This method runs the main functionality of the game, allowing the user to input what functionality he or she
	 * wants to use.
	 * @param moviePath
	 * @param actorPath
	 * @param movieActorPath
	 * @throws Exception
	 */
	private static <V,E> void runGame(String moviePath, String actorPath, String movieActorPath) throws Exception {
		boolean isValid = false, play = true;
		Graph<String, Set<String>> graph = GraphLibrary.makeGraph(GraphLibrary.fileToMap(moviePath), GraphLibrary.fileToMap(actorPath), movieActorPath);
		Graph<String, Set<String>> bfs = null;
		Scanner input = new Scanner(System.in);
		String CoU = "";
		//Print instructions
		System.out.println("What would you like to do?\nCommands:\r\n" +
				"c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation\r\n" +
				"d <low> <high>: list actors sorted by degree, with degree between low and high\r\n" +
				"i: list actors with infinite separation from the current center\r\n" +
				"p <name>: find path from <name> to current center of the universe\r\n" +
				"s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high\r\n" +
				"u <name>: make <name> the center of the universe\r\n" +
				"q: quit game");
		//While at play, receive commands
		while(play) {
			System.out.print("Input command: ");
			input = new Scanner(System.in);
			String str = input.nextLine();
			char toDo = str.charAt(0);
			isValid = false;

			while(!isValid) {
				//sorts all point in graph by average separation
				if(toDo == 'c') {
						isValid = true;
						System.out.print("Please input number of vertices: ");
						int order = input.nextInt();
						if (order <= graph.numVertices()){
							String direction = "top";
							if (order < 0) {
								direction = "bottom";
							}
					   	 	System.out.println("List of " + direction + " " + Math.abs(order) + " centers of the universe, sorted by average separation: ");
					   	 	GraphLibrary.sortByAvSep(graph, order);
						}
						else{
							System.out.println("That number is too big. Try a smaller one.");
						}

				}
				//sorts points by in degree
				else if(toDo == 'd') {

					isValid = true;
					System.out.print("Input low bound: ");
					int low = input.nextInt();
					System.out.print("Input high bound: ");
					int high = input.nextInt();

					GraphLibrary.sortByDegree(graph, low, high);


				}
				//return points not connected to input center of the universe
				else if(toDo == 'i') {

					isValid = true;
					if(CoU.length() == 0) {
						System.out.println("Please set a center.");
					}else {
						System.out.println("The following actors are not connected to " + CoU + ": " + GraphLibrary.missingVertices(graph, bfs));
					}

				}
				//return the path from an input point to the center of the universe
				else if(toDo == 'p') {

					isValid = true;
					System.out.print("Please enter a name: ");
					String person = input.nextLine();

					if (bfs != null && bfs.hasVertex(person)){
						List<String> list = GraphLibrary.getPath(bfs, person);
						int num = list.size() - 1;
						System.out.println("p " + person);
						System.out.println(person + "\'s number is " + num);
						for (int i = 0; i < list.size() - 1; i ++){
							//System.out.println(bfs.hasEdge(list.get(i),  list.get(i + 1)));
							System.out.println(list.get(i) + " appeared in " + bfs.getLabel(list.get(i), list.get(i+1)) + " with " + list.get(i+1));
						}
					}else if(bfs == null){
						System.out.println("No universe created.");
					}
					else{
						System.out.println("Sorry, that person is not connected to " + CoU);
					}

				}
				//sorts points by their distance from the center of the universe
				else if(toDo == 's') {

					isValid = true;
					if(bfs == null) {
						System.out.println("Please set a center.");
					}else {
						System.out.print("Input low bound: ");
						int low = input.nextInt();
						System.out.print("Input high bound: ");
						int high = input.nextInt();
						GraphLibrary.sortByPathSep(bfs, low, high);
					}

				}
				//sets center of the universe
				else if(toDo == 'u') {

					isValid = true;
					System.out.print("Please enter name: ");
					CoU = input.nextLine();

					if (graph.hasVertex(CoU)){
						System.out.println(CoU + " game >");
						bfs = GraphLibrary.bfs(graph, CoU);
					}else{
						System.out.println("Sorry, that actor is not in this dataset. Try someone new.");
					}

				}
				//quits game
				else if(toDo == 'q') {

					isValid = true;
					play = false;
					System.out.println("Thanks for playing!");


				}
				//catches invalid input
				else {

					System.out.println("Please use a valid command.");
					break;
				}
			}
		}
		input.close();
	}

	/**
	 * This method creates a hand drawn graph to test some functionality.
	 * @throws Exception
	 */
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
			relationships.insertUndirected("Alice", startNode, "A Movie");
	    relationships.insertUndirected("Alice", startNode, "E Movie");

			System.out.println("The graph:");
			System.out.println(relationships);

			System.out.println(GraphLibrary.bfs(relationships, startNode));
	}


	/**
	 * Runs different testing methods
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[]args) throws Exception{
		//handDrawn();
		//runGame("PS4\\bacon\\moviesTest.txt", "PS4\\bacon\\actorsTest.txt", "PS4\\bacon\\movie-actorsTest.txt");

		runGame("bacon/movies.txt", "bacon/actors.txt", "bacon/movie-actors.txt");
	}
}

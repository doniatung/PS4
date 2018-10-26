public class Main{

  




  public static void main(String[]args){
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
}

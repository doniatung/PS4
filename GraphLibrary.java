import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
/**
 * Class containing a library of methods to be used on graphs
 * @author Donia Tung, CS10, Dartmouth Fall 2018
 * @author Lucas Rathgeb, CS10, Dartmouth Fall 2018
*/

public class GraphLibrary{

  /**
   * Creates a graph of the shortest path for points connected to input graph.
   * @param g Input graph
   * @param source Center of the graph universe
   * @throws Exception
   * @return  Graph of shortest path tree for a given center of the universe. Graph returned
   * contains pointers from children up to parents
   */
 	public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source) throws Exception{
 		SimpleQueue<V> holder = new SLLQueue<V>();
 		Set<V> visited = new HashSet<V>();
 		Graph<V,E> pathTree = new AdjacencyMapGraph<V,E>();

 		holder.enqueue(source);
 		pathTree.insertVertex(source);

 		while (!holder.isEmpty()){
 			V u = holder.dequeue();
 			for (V v : g.outNeighbors(u)){
 				if (!visited.contains(v)){
 					visited.add(v);
 					holder.enqueue(v);
 					pathTree.insertVertex(v);
 					pathTree.insertDirected(v, u, null);
 				}
 			}
 		}
 		return pathTree;
   }

 	 /**
   * Method to return a path from a given vertex back to the center of the given graph
   * @param tree  shortest path tree (received from BFS method)
   * @param v     vertex of the graph
   * @return      a list of points reached as one passes from the vertex to the center of the graph
 	  */
 	  public static <V,E> List<V> getPath(Graph<V,E> tree, V v){
 	    List<V> path = new ArrayList<V>();
 	    path.add(v);
 	    getPathHelpter(tree, v, path);
 	    return path;
 	  }

 	  /**
 	  * Helper method for function getPath(). Uses recursion to construct the path that
 	  * will lead from a given node to the center of the BFS tree.
    * @param tree
    * @param v
 	  */
 	  public static <V,E> List<V> getPathHelpter(Graph<V,E> tree, V v, List<V> path){
 	    if (tree.outDegree(v) == 0){
 	      return path;
 	    }
 	    else{
 	      for (V vertex: tree.outNeighbors(v)){//should be only one outNeighbor
 	        path.add(vertex);
 	        getPathHelpter(tree, v, path);
 	      }
 	    }
 	    return path;
 	  }

 	  /**
    * Method to determine which vertices are in the given graph, but not in the subgraph
    * @param graph      a graph of Vertices
    * @param subGraph   the shortest path tree (BFS)
    * @return       a Set of vertices that are in the given graph, but not in the subgraph (i.e. not reached by BFS)
 	  */
 	  public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph){
 	    HashSet<V> missingV = new HashSet<V>();
 	    Set<V> bigGVertices = (HashSet<V>) graph.vertices();
 	    for (V vertex: subgraph.vertices()){
 	      bigGVertices.remove(vertex);
 	    }
 	    for (V remaining: bigGVertices){
 	      missingV.add(remaining);
 	    }
 	    return missingV;
 	  }

    /**
    *
    * @param tree
    * @param root
    */
 	 public static <V,E> double averageSeparation(Graph<V,E> tree, V root){
 		 int sumDist = 0;
 		 sumDist = averageHelper(tree, root, sumDist);
 		 return ((double)sumDist)/(tree.numVertices()-1);
 	 }

   /**
   *
   * @param tree
   * @param vertex
   * @param sumDist
   */
 	 private static <V,E> int averageHelper(Graph<V,E> tree, V vertex, int sumDist) {
 		 if(tree.inDegree(vertex) == 0) {
 			 return sumDist;
 		 }else{
 			 for(V v : tree.inNeighbors(vertex)) {
 				sumDist = averageHelper(tree, v, sumDist + 1) + sumDist;
 			 }
 		 }
 		 return sumDist;
 	 }

   /**
   * Method to read in a given file and store its data in a Map
   *
   * @param pathName  name of the file containing the data to be parsed
   * @return      a Map with Key as the ID in the data file and a String of the actor name or movie title
   */
   public static Map<Integer, String> fileToMap(String pathName)throws Exception{
     Map<Integer, String> ids = new HashMap<Integer, String>();
     try{
       BufferedReader input = new BufferedReader(new FileReader(pathName));
       String line;
       while ((line = input.readLine()) != null){
         if (line.contains("|")){
           String[] id = line.split("\\|");
           int num = Integer.parseInt(id[0]);
           if (!ids.containsKey(num)){
             ids.put(num, id[1]);
           }
         }
       }
     }
     catch (FileNotFoundException e){
       System.out.println("File Not Found");
     }
     catch(IOException e){
       e.printStackTrace();
     }
     finally{
      input.close();
     }
     return ids;
   }


   /**
   * Method to create a Graph given two Maps matching ids to names, and a path to a file
   * matching ids for movies and actors
   *
   * @param movies    HashMap containing movies' ID numbers as Keys and Strings of the movie titles as Values
   * @param actors    HashMap containing actors' ID numbers as Keys and Strings of their names as Values
   * @param pathName  path of file containing data with movies and their respective actors (stored as ints of ids)
   * @return    a constructed AdjacencyMapGraph with Keys as movie title Strings and Values of ArrayLists of actors within the movies
   */
   public static AdjacencyMapGraph<String, ArrayList<String>> makeGraph (Map<Integer,String> movies, Map<Integer,String> actors, String pathName) throws Exception{
     AdjacencyMapGraph<String,ArrayList<String>> actorMovieGraph = new AdjacencyMapGraph<String, ArrayList<String>>();
     HashMap<String, ArrayList<String>> mToA = new HashMap<String, ArrayList<String>>();
     try{
       BufferedReader input = new BufferedReader(new FileReader(pathName));
       String line;
       while ((line.readLine()) != null){
         if (line.contains["|"]){
           String[] id = line.split("\\|");
           int movieInt = Integer.parseInt(id[0]);
           int actorInt = Integer.parseInt(id[1]);
           String movie = movies.get(movieInt);
           String actor = actors.get(actorInt);
           ArrayList<String> list = new ArrayList<String>();
           if (mToA.containsKey(movie)){
             mToA.get(movie).add(actor);
             actorMovieGraph.insertVertex(actor);
           }
           else{
             list.add(actor);
             mToA.put(movie, list);
           }
         }
       }
        for (String key: mToA.ketSet()){
          for (String actor: mToA.get(key)){
            for (String actor2: mToA.get(key)){
              actorMovieGraph.insertUndirected(actor, actor2, key);
            }
          }
        }
     }
     catch(FileNotFoundException e ){
       System.out.println("File Not Found");
     }
     catch(IOException e){
       e.pritnStackTrace();
     }
     finally{
       input.close();
     }
     return actorMovieGraph;
   }


}

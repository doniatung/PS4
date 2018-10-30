import java.util.*;
import java.io.*;

/**
 * Class containing a library of methods to be used on graphs.
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
 		visited.add(source);
 		pathTree.insertVertex(source);
 		
 		while (!holder.isEmpty()){
 			V u = holder.dequeue();
 			for (V v : g.outNeighbors(u)){
 	 			if (!visited.contains(v)){
 	 				visited.add(v);
 	 				holder.enqueue(v);
 	 				pathTree.insertVertex(v);
 	 				pathTree.insertDirected(v, u, g.getLabel(v, u));
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
 	    getPathHelper(tree, v, path);
 	    return path;
 	  }

 	  /**
 	  * Helper method for function getPath(). Uses recursion to construct the path that
 	  * will lead from a given node to the center of the BFS tree.
    * @param tree
    * @param v
 	  */
 	  public static <V,E> List<V> getPathHelper(Graph<V,E> tree, V v, List<V> path){
 	    if (tree.outDegree(v) == 0){
 	      return path;
 	    }
 	    else{
 	      for (V vertex: tree.outNeighbors(v)){
 	        path.add(vertex);
 	        getPathHelper(tree, vertex, path);
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
 	    for (V vertex: graph.vertices()){
 	    	if (!subgraph.hasVertex(vertex)) missingV.add(vertex);
 	    }
 	    return missingV;
 	  }

    /**
    *	Finds the average separation of all points connected to a certain root point.
    * @param tree
    * @param root
    */
 	 public static <V,E> double averageSeparation(Graph<V,E> tree, V root){
 		 int sumDist = 0;
 		 sumDist = averageHelper(tree, root, sumDist);
 		 return ((double)sumDist)/(tree.numVertices()-1);
 	 }

   /**
   *	Helper method used to calculate average separation.
   * @param tree
   * @param vertex
   * @param sumDist
   */
 	 private static <V,E> int averageHelper(Graph<V,E> tree, V vertex, int sumDist) {
 		 int temp = sumDist;
 		 if(tree.inDegree(vertex) == 0) {
 			 return sumDist;
 		 }else{
 			 for(V v : tree.inNeighbors(vertex)) {
 				temp += averageHelper(tree, v, sumDist + 1);
 			 }
 		 }
 		 return temp;
 	 }

   /**
   * Method to read in a given file and store its data in a Map
   *
   * @param pathName  name of the file containing the data to be parsed
   * @return      a Map with Key as the ID in the data file and a String of the actor name or movie title
   */
   public static Map<Integer, String> fileToMap(String pathName)throws Exception{
     Map<Integer, String> ids = new HashMap<Integer, String>();
     BufferedReader input = null;
     try{
       input = new BufferedReader(new FileReader(pathName));
       String line = input.readLine();
       while (line != null){
         if (line.contains("|")){
           String[] id = line.split("\\|");
           int num = Integer.parseInt(id[0]);
           if (!ids.containsKey(num)){
             ids.put(num, id[1]);
           }
         }
         line = input.readLine();
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
   public static AdjacencyMapGraph<String, Set<String>> makeGraph (Map<Integer,String> movies, Map<Integer,String> actors, String pathName) throws Exception{
     
	 AdjacencyMapGraph<String,Set<String>> actorMovieGraph = new AdjacencyMapGraph<String, Set<String>>();
     HashMap<String, ArrayList<String>> mToA = new HashMap<String, ArrayList<String>>();
     BufferedReader input = null;
     try{
       input = new BufferedReader(new FileReader(pathName));
       String line = input.readLine();
       while ( line != null){
         if (line.contains("|")){
           String[] id = line.split("\\|");
           int movieInt = Integer.parseInt(id[0]);
           int actorInt = Integer.parseInt(id[1]);
           String movie = movies.get(movieInt);
           String actor = actors.get(actorInt);
           ArrayList<String> list = new ArrayList<String>();
           if (mToA.containsKey(movie)){
             mToA.get(movie).add(actor);
             if(!actorMovieGraph.hasVertex(actor)) actorMovieGraph.insertVertex(actor);
           }
           else{
        	 if(!actorMovieGraph.hasVertex(actor)) actorMovieGraph.insertVertex(actor);
             list.add(actor);
             mToA.put(movie, list);
           }
         }
         line = input.readLine();
       }
        for (String key: mToA.keySet()){
        	ArrayList<String> actArr = mToA.get(key);
          for (String actor: actArr){
            for (String actor2: actArr){
            	
            	if(actorMovieGraph.hasVertex(actor) && 
            	   actorMovieGraph.hasVertex(actor2) && 
            	   !actorMovieGraph.hasEdge(actor, actor2) &&
            	   actor != actor2) {
            		HashSet<String> temp = new HashSet<String>();
            		temp.add(key);
            		actorMovieGraph.insertUndirected(actor, actor2, temp);
            	}else if(actorMovieGraph.hasVertex(actor) && actorMovieGraph.hasVertex(actor2) && actor != actor2){	
            		actorMovieGraph.getLabel(actor, actor2).add(key);
            	}
            
            }
          }
        }
     }
     catch(FileNotFoundException e){
       System.out.println("File Not Found");
     }
     catch(IOException e){
       e.printStackTrace();
     }
     finally{
       input.close();
     }
     return actorMovieGraph;
   }
   
   
   
   
   /**
    * This method sorts the points of the constructed actor-movie graph by average separation. Smallest to largest if input
    * order is positive and largest to smallest if negative.
    * @param graph
    * @param order
    * @throws Exception
    */
   public static <V, E> void sortByAvSep(Graph<String,Set<String>> graph, int order) throws Exception{
	   ArrayList<Object[]> sortedAv = new ArrayList<Object[]>();
	   if (order == 0) return;
	   for(String v: graph.vertices()) {
		   
		   Object[] arr = {v, averageSeparation(bfs(graph, v), v)};
		   sortedAv.add(arr);
	   }
	   if(order > 0) {
		   sortedAv.sort((Object[] o1, Object[] o2)
				   -> (int)(((double)o1[1] - (double)o2[1])/Math.abs((double)o1[1] - (double)o2[1])));
	   }else {
		   sortedAv.sort((Object[] o1, Object[] o2) 
				   -> (int)(((double)o2[1] - (double)o1[1])/Math.abs((double)o2[1] - (double)o1[1])));
	   }
	   for(int i = 0; i < Math.abs(order); i++) {
		   System.out.println(sortedAv.get(i)[0] + " has an average separation of " + sortedAv.get(i)[1]);
	   }
   }
   
   
   
   /**
    * This method sorts all point in the graph by the number of inNeighbors they have, between a certain low bound and high bound.
    * @param graph
    * @param low
    * @param high
    */
   public static <V, E> void sortByDegree(Graph<String,Set<String>> graph, int low, int high) {
	   ArrayList<Object[]> sortedDeg = new ArrayList<Object[]>();
	   for(String v: graph.vertices()) {
		   Object[] arr = {v, graph.inDegree(v)};
		   if( (int)arr[1] <= high && (int)arr[1] >= low) sortedDeg.add(arr); 
	   }
	   sortedDeg.sort((Object[] o1, Object[] o2) -> (int)o1[1] - (int)o2[1]);
	   for(Object[] o : sortedDeg) {
		   System.out.println(o[0] + " has a degree of " + o[1]);
	   }
   }
   
   
   
   
   
   /**
    * This method sorts all point in the graph by their distance from the root, between a certain low bound and high bound.
    * @param graph
    * @param low
    * @param high
    */
   public static <V,E> void sortByPathSep(Graph<String,Set<String>> graph, int low, int high) {
	   ArrayList<Object[]> sortedSep = new ArrayList<Object[]>();
	   for(String v: graph.vertices()) {
		   Object[] arr = {v, getPath(graph, v).size() - 1};
		   if((int)arr[1] <= high && (int)arr[1] >= low) sortedSep.add(arr);			   
	   }
	   sortedSep.sort((Object[] o1, Object[] o2) -> (int)o1[1] - (int)o2[1]);
	   for(Object[] o : sortedSep) {
		   System.out.println(o[0] + " has a separation of " + o[1]);
	   }
   }

}

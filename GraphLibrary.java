import java.util.*;
import java.io.*;

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
 	    if (tree.hasVertex(v) && tree.outDegree(v) == 0){
 	      return path;
 	    }
 	    else{
 	      for (V vertex: tree.outNeighbors(v)){//should be only one outNeighbor
 	        path.add(vertex);
 	        getPathHelper(tree, v, path);
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
        	System.out.println(actArr);
          for (String actor: actArr){
            for (String actor2: actArr){
            	
            	if(actorMovieGraph.hasVertex(actor) && actorMovieGraph.hasVertex(actor2) && !actorMovieGraph.hasEdge(actor, actor2)) {
            		actorMovieGraph.insertUndirected(actor, actor2, new HashSet<String>());
            		actorMovieGraph.getLabel(actor, actor2).add(key);
            	}else if(actorMovieGraph.hasVertex(actor) && actorMovieGraph.hasVertex(actor2)){
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
   
   public static <V, E> void sortByAvSep(Graph<String,Set<String>> graph, int order){
	   ArrayList<Object[]> sortedAv = new ArrayList<Object[]>();
	   for(String v: graph.vertices()) {
		   Object[] arr = {v, averageSeparation(graph, v)};
		   for(int i = 0; i < sortedAv.size(); i++) {
			   if ((int)arr[1] < (int)sortedAv.get(i)[1]) {
				   sortedAv.add(i, arr);
			   }else if(i == Math.abs(order) - 1) {
				   sortedAv.add(arr);
				   break;
			   }
		   }
			   
	   }
	   if(order >= 0) {
		   for(int i = 0; i < sortedAv.size(); i++) {
				System.out.println(sortedAv.get(i)[0] + " has an average separation of " + sortedAv.get(i)[1]);
		   }
	   }else {
		   for(int i = 0; i < sortedAv.size(); i++) {
			   System.out.println(sortedAv.get(sortedAv.size()-i)[0] + " has an average separation of " + sortedAv.get(sortedAv.size()-i)[1]);
		   }
	   }
   }
   
   public static <V, E> void sortByDegree(Graph<String,Set<String>> graph, int low, int high) {
	   ArrayList<Object[]> sortedDeg = new ArrayList<Object[]>();
	   for(String v: graph.vertices()) {
		   Object[] arr = {v, graph.inDegree(v)};
		   for(int i = 0; i < sortedDeg.size(); i++) {
			   if((int)arr[1] > high || (int)arr[1] < low) {
				   break;
			   }
			   if ((int)arr[1] > (int)sortedDeg.get(i)[1]) {
				   sortedDeg.add(i, arr);
			   }else if(i == sortedDeg.size() - 1) {
				   sortedDeg.add(arr);
			   }
		   }
			   
	   }
	   for(Object[] o : sortedDeg) {
		   System.out.println(o[0] + " has a degree of " + o[1]);
	   }
   }
   
   public static <V,E> void sortByPathSep(Graph<String,Set<String>> graph, int low, int high) {
	   ArrayList<Object[]> sortedSep = new ArrayList<Object[]>();
	   for(String v: graph.vertices()) {
		   Object[] arr = {v, getPath(graph, v).size() - 1};
		   for(int i = 0; i < sortedSep.size(); i++) {
			   if((int)arr[1] > high || (int)arr[1] < low) {
				   break;
			   }
			   if ((int)arr[1] > (int)sortedSep.get(i)[1]) {
				   sortedSep.add(i, arr);
			   }else if(i == sortedSep.size() - 1) {
				   sortedSep.add(arr);
			   }
		   }
			   
	   }
	   for(Object[] o : sortedSep) {
		   System.out.println(o[0] + " has a degree of " + o[1]);
	   }
   }

}

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
   * Creates a graph of all points connected to input source.
   * @param Graph<V,E> g Input graph
   * @param V source Center of the graph universe
   * @throws Exception 
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
 	  */
 	  public static <V,E> List<V> getPath(Graph<V,E> tree, V v){
 	    List<V> path = new ArrayList<V>();
 	    path.add(v);
 	    getPathHelpter(tree, v, path);
 	    return path;
 	  }

 	  /**
 	  Helper method for function getPath(). Uses recursion to construct the path that
 	  will lead from a given node to the center of the BFS tree.
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
 	  
 	 public static <V,E> double averageSeparation(Graph<V,E> tree, V root){
 		 int sumDist = 0;
 		 sumDist = averageHelper(tree, root, sumDist);
 		 return ((double)sumDist)/(tree.numVertices()-1);
 	 }
 	 
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
}


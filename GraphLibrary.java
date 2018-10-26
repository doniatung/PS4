import java.util.*;

/**
@author Donia Tung, CS10, Dartmouth Fall 2018
@author Lucas Rathgeb, CS10, Dartmouth Fall 2018

Class containing a library of methods to be used on graphs
*/

public class GraphLibrary{

  /**
  */
  public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source){
    SLLQueue<V> queue = new SLLQueue<V>();
    Graph<V,E> pathTree = new AdjacencyMapGraph<V,E>();
    SLLQueue<V> visited = new SLLQueue<V>();
    pathTree.insertVertex(source);
    queue.enqueue(source);
    visited.enqueue(source);
    while (!queue.isEmpty()){
      V u = queue.remove();
      for (V v : g.outNeighbors(u)){
        if (!visited.contains(v)){
          visited.enqueue(v);
          queue.enqueue(v);
          pathTree.insertVertex(v);
          pathTree.insertDirected(v,u,g.getLabel(u,v));
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
    getPathHelper(tree, v, path);
    return path;
  }

  /**
  Helper method for function getPath(). Uses recursion to construct the path that
  will lead from a given node to the center of the BFS tree.
  */
  public static <V,E> List<V> getPathHelper(Graph<V,E> tree, V v, List<V> path){
    if (tree.outNeighbors(v) == null){
      return path;
    }
    else{
      for (V vertex: tree.outNeighbors(v)){//should be only one outNeighbor
        path.add(vertex);
        getPathHelpter(tree, v, path);
      }
    }
  }

  /*This is a non-recursive way of doing it, but I'm just confused
because I don't know how to access the element inside of the
outNeighbors iterable method. I also technically don't know if
outNeighbors has a method called isEmpty() l o l

  public static <V,E> List<V> getPath(Graph<V,E> tree, V v){
    List<V> path = new List<V>();
    V v2 = v;
    path.add(v);
    while (!g.outNeighbors(v2).isEmpty()){
      path.add(v2);
      v2 = g.outNeighbors(v2).get
    }
    return path;
  }*/

  /**
  */

  public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph){
    Set<V> missingV = new HashSet<V>();
    Graph<V,E> bigGraph = graph;
    Iterable<V> bigGVertices = graph.vertices();
    for (V vertex: subgraph.vertices()){
      bigGraph.removeVertex(vertex);
    }
    for (V remaining: bigGVertices){
      missingV.add(remaining);
    }
    return missingV;
  }


  /**
  */
  public static <V,E> double averageSeparation(Graph<V,E> tree, V root){



  }


}

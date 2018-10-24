/**

@author Donia Tung, CS10, Dartmouth Fall 2018

Class containing a library of methods to be used on graphs
*/

public class GraphLibrary{

  /**
  */
  public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source){
    Queue<V> queue = new Queue<V>();
    Graph<V,E> pathTree = new Graph<V,E>();
    Queue<V> visited = new Queue<V>();
    pathTree.insertVertex(source);
    holder.enqueue(source);
    visited.enqueue(source);
    while (!queue.isEmpty()){
      V u = queue.remove();
      for (V v : g.outNeighbors(u)){
        if (!visited.contains(v)){
          visited.add(v);
          queue.add(v);
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
    List<V> path = new List<V>();
    path.add(v);
    getPathHelpter(tree, v, path);
    return path;
  }

  public static void getPathHelpter(Graph<V,E> tree, V v, List<V> path){
    if (g.outNeighbors(v).isEmpty()){
      return path;
    }
    else{
      for (V vertex: g.outNeighbors(v)){//should be only one outNeighbor
        path.add(vertex)
        getPathHelpter(tree, v, path);
      }
    }
  }

  /**
  */
  public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph){



  }


  /**
  */
  public static <V,E> double averageSeparation(Graph<V,E> tree, V root){



  }


}

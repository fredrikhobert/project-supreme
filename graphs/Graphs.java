package graphs;

import java.util.*;

public class Graphs {


	private static <T1> void depthFirstSearch(ListGraph<T1> graph, T1 fromNode, Set<T1> besökta){
		besökta.add(fromNode);
		for(Edge<T1> e : graph.getEdgesFrom(fromNode))
			if (!besökta.contains(e.getDest()))
				depthFirstSearch(graph, e.getDest(), besökta);
	}


	public static <T1> boolean pathExists(ListGraph<T1> graph, T1 fromNode, T1 to){
		Set<T1> besökta = new HashSet<T1>();
		depthFirstSearch(graph, fromNode, besökta);
		return besökta.contains(to);
	}


	public static <T1> ArrayList<Edge<T1>> getPath(ListGraph<T1> graph, T1 start, T1 end){

		if(!pathExists(graph, start, end))
			return null;

		HashMap<T1, Integer> weight = new HashMap<T1, Integer>();
		HashMap<T1, Boolean> settled = new HashMap<T1, Boolean>();
		HashMap<T1, T1> pathFrom = new HashMap<T1, T1>();

		for(T1 n : graph.getNodes()){
			weight.put(n, Integer.MAX_VALUE);
			settled.put(n, false);
		}

		weight.put(start, 0);
		settled.put(start, true);
		T1 currentNode = start;

		while(currentNode != end){

			List<Edge<T1>> currentNodeEdges = graph.getEdgesFrom(currentNode);

			for(Edge<T1> e : currentNodeEdges){
				if(weight.get(currentNode) + e.getWeight() < weight.get(e.getDest())){
					weight.put((T1) e.getDest(), weight.get(currentNode) + e.getWeight());
					pathFrom.put((T1) e.getDest(), currentNode);
				}
			}
			int minVal = Integer.MAX_VALUE;
			T1 node = null;
			for(T1 n : weight.keySet()){
				if(weight.get(n) < minVal && settled.get(n) == false){
					minVal = weight.get(n);
					node = n;
				}
			}

			settled.put(node, true);
			currentNode = node;
		}

		ArrayList<T1> nodeTraversal = new ArrayList<T1>();
		T1 endNode = end;
		nodeTraversal.add(endNode);
		while(pathFrom.get(endNode ) != null){
			endNode  = pathFrom.get(endNode );
			nodeTraversal.add(endNode);
		}

		Collections.reverse(nodeTraversal);

		ArrayList<Edge<T1>> edges = new ArrayList<Edge<T1>>();
		for(int x=0, y=1; y < nodeTraversal.size(); x++, y++){
			List<Edge<T1>> le = graph.getEdgesBetween(nodeTraversal.get(x), nodeTraversal.get(y));			
			int minVal = Integer.MAX_VALUE;
			Edge<T1> test = null;
			for(Edge<T1> e : le){
				if(e.getWeight() < minVal){
					minVal = e.getWeight();
					test = e;
				}
			}
			edges.add(test);
		}	
		return edges;
	}

}

package graphs;

import java.util.ArrayList;
import java.util.List;

public interface Graph<T1> {

	public abstract void addNode(T1 node);

	public abstract void connectNodes(T1 from, T1 to, String name, int weight);

	public abstract void setConnectionWeight(T1 from, T1 to, String name, int weight);

	public abstract List<T1> getNodes();

	public abstract List<Edge<T1>> getEdgesFrom(T1 node);

	public abstract ArrayList<Edge<T1>> getEdgesBetween(T1 nodeOne, T1 nodeTwo);

}
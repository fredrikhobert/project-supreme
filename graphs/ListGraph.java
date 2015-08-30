package graphs;

import static javax.swing.JOptionPane.*;

import java.io.Serializable;
import java.util.*;

public class ListGraph <T1> implements Graph<T1>, Serializable{

	
	private Map<T1, List<Edge<T1>>> nodes; 


	public ListGraph(){
		nodes = new HashMap<T1, List<Edge<T1>>>(); 
	}


	public void addNode(T1 node){
		nodes.put(node, new ArrayList<Edge<T1>>());	
	}

	
	public void connectNodes(T1 from, T1 to, String name, int weight){
		try{
			if(nodes.containsKey(from) == false || nodes.containsKey(to) == false)
				throw new NoSuchElementException();

			if(weight < 0)
				throw new IllegalArgumentException();

			for(Map.Entry<T1, List<Edge<T1>>> entry : nodes.entrySet()){
				List<Edge<T1>> edges = entry.getValue();
				for(Edge<T1> e : edges)
					if(e.getName().equals(name))
						throw new IllegalStateException();
			}

			List<Edge<T1>> fromList = nodes.get(from);
			List<Edge<T1>> toList= nodes.get(to);
			Edge<T1> fromEdge = new Edge<T1>(to , name, weight);
			Edge<T1> toEdge = new Edge<T1>(from , name, weight);
			fromList.add(fromEdge);
			toList.add(toEdge);

		} 
		catch(NoSuchElementException nsee){
			showMessageDialog(null, "Nod/Noder saknas i grafen", "Fel", ERROR_MESSAGE);
		}
		catch(IllegalArgumentException iae){
			showMessageDialog(null, "Vikten får ej vara negativ", "Fel", ERROR_MESSAGE);
		}
		catch(IllegalStateException ise){
			showMessageDialog(null, "Namnet på bågen finns redan", "Fel", ERROR_MESSAGE);
		}
	}


	
	public void setConnectionWeight(T1 from, T1 to, String name, int weight){
		try{
			if(nodes.containsKey(from) == false || nodes.containsKey(to) == false)
				throw new NoSuchElementException();

			if(weight < 0)
				throw new IllegalArgumentException();

			List<Edge<T1>> fromEdges = nodes.get(from);
			List<Edge<T1>> toEdges = nodes.get(to);

			int controlValue = 0;
			for(Edge<T1> e : fromEdges){
				if(e.getName().equals(name)){
					e.setWeight(weight);
					controlValue++;
				}
			}

			for(Edge<T1> e : toEdges){
				if(e.getName().equals(name)){
					e.setWeight(weight);
					controlValue++;
				}
			}
			
			if(controlValue != 2)
				throw new NoSuchElementException();
		} 
		catch (NoSuchElementException nsee){
			showMessageDialog(null, "Nod/Noder eller förbindelse saknas i grafen", "Fel", ERROR_MESSAGE);
		}
		catch(IllegalArgumentException iae){
			showMessageDialog(null, "Vikten får ej vara negativ", "Fel", ERROR_MESSAGE);
		}
	}


	
	public List<T1> getNodes(){
		ArrayList<T1> nodeList = new ArrayList<T1>();
		for(Map.Entry<T1, List<Edge<T1>>> entry : nodes.entrySet()){
			T1 node = entry.getKey();
			nodeList.add(node);
		}
		return nodeList;
	}


	public List<Edge<T1>> getEdgesFrom(T1 node){
		List<Edge<T1>> edgeList = new ArrayList<Edge<T1>>();
		try{
			if(nodes.containsKey(node) == false)
				throw new NoSuchElementException();
			edgeList = nodes.get(node);
			
		}catch(NoSuchElementException nsee){
			showMessageDialog(null, "Nod/Noder eller förbindelse saknas i grafen", "Fel", ERROR_MESSAGE);
		}
		return edgeList;
	}

	
	public ArrayList<Edge<T1>> getEdgesBetween(T1 nodeOne, T1 nodeTwo){
		List<Edge<T1>> nodeOneEdges = new ArrayList<Edge<T1>>();
		List<Edge<T1>> nodeTwoEdges = new ArrayList<Edge<T1>>();
		ArrayList<Edge<T1>> edgesBetween = new ArrayList<Edge<T1>>();

		try{
			if(nodes.containsKey(nodeOne) == false || nodes.containsKey(nodeTwo) == false)
				throw new NoSuchElementException();

			nodeOneEdges = nodes.get(nodeOne);
			nodeTwoEdges = nodes.get(nodeTwo);

			for(Edge<T1> noe : nodeOneEdges){
				for(Edge<T1> nte  : nodeTwoEdges){
					if(noe.getName().equals(nte.getName()))
						edgesBetween.add(noe);
				}
			}		

		} catch(NoSuchElementException nsee){
			showMessageDialog(null, "Nod/Noder eller förbindelse saknas i grafen", "Fel", ERROR_MESSAGE);
		}
		return edgesBetween;
	}	


	public void disconnect(T1 nodeOne, T1 nodeTwo){
		try{
		if(!nodes.containsKey(nodeOne) || !nodes.containsKey(nodeTwo))
			throw new NoSuchElementException();	
				
		List<Edge<T1>> edgesToRemove = getEdgesBetween(nodeOne, nodeTwo);
		if(edgesToRemove.size() > 0){
			nodes.get(nodeOne).removeAll(edgesToRemove);
			nodes.get(nodeTwo).removeAll(edgesToRemove);
		}
		}catch(NoSuchElementException nsee){}	
	}	
	
	
	public void remove(T1 node){
		
		if(!nodes.containsKey(node))
			return;
		
		List<Edge<T1>> edgesToRemove = nodes.get(node);
		nodes.remove(node);
		
		for(Map.Entry<T1, List<Edge<T1>>> entry : nodes.entrySet()){
			List<Edge<T1>> edges = entry.getValue();
			edges.removeAll(edgesToRemove);
		}
	}
	
	
	public String toString(){
		String str="";
		for(Map.Entry<T1, List<Edge<T1>>> me : nodes.entrySet()){
			str += "Nod: " + me.getKey() + " ";
			for(Edge<T1> e : me.getValue())
				str += e.toString() + " / ";
			str += "\n";
		}
		return str;
	} 
}

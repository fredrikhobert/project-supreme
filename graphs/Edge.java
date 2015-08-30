package graphs;

import java.io.Serializable;

public class Edge<T1> implements Serializable {
	

	private T1 dest;
	private String name;
	private int weight;
	
	
	protected Edge(T1 dest, String name, int weight){ 
		this.dest = dest;
		this.name = name;
		this.weight = weight;
	}
	
	
	public T1 getDest(){
		return dest;
	}
	
	
	public String getName(){
		return name;
	} 
	
	
	public int getWeight(){
		return weight;
	}
	
	
	public void setWeight(int weight){
		try{
			if(weight < 0)
				throw new IllegalArgumentException();
			this.weight = weight;
		} catch(IllegalArgumentException iae){}
	}
	
	
	public String toString(){
		return "till " + dest + " med " + name + ", vikt: " + weight;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dest == null) ? 0 : dest.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + weight;
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge<T1> other = (Edge<T1>) obj;
		if (dest == null) {
			if (other.dest != null)
				return false;
		} else if (!dest.equals(other.dest))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}
}

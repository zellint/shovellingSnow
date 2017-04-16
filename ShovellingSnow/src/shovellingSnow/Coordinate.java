package shovellingSnow;

import java.util.ArrayList;
import java.util.List;


public class Coordinate implements Comparable<Coordinate>{
	enum TYPE{
		HOUSE, SNOW, FREEWAY, OBSTACLE
	}
	int x;
	int y;
	int value;
	String name=null;
	Coordinate parent;
	public Coordinate(int x, int y, int value, String name){
		this.x = x;
		this.y = y;
		this.value = value;
		this.name = name;
		parent = null;
	}
	public Coordinate getParent(){
		return parent;
	}
	public void setParent(Coordinate p){
		parent= (p);
	}
	public Coordinate clone(Coordinate p){
		Coordinate copy = new Coordinate(this.x,this.y, this.value, this.name);
		copy.setParent(p);
		return copy;
	}
	public TYPE getType(){
		if(isHome()) return TYPE.HOUSE;
		else if(value ==1) return TYPE.SNOW;
		else if(value == Map.BIG_NUMBER) return TYPE.OBSTACLE;
		else return TYPE.FREEWAY; 
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getValue() {
		return value;
	}
	public String getName(){
		return name;
	}
	public boolean isHome() {
		return name!=null;
	}
 
	public String getCoor() {
		
		return "[ "+this.getX()+" , "+this.getY()+" ]" ;
	}

	@Override
	public int compareTo(Coordinate o) {
		if(this.x == o.getX()){
			return this.y < o.getY() ? -1 : 1;
		}else{
			return this.x < o.getX() ? -1 : 1;
		}
	}

	
	
	public String trace(){
		StringBuilder sb = new StringBuilder();
		Coordinate parent = this;
		while((parent = parent.getParent())!=null){
			sb.append( parent.getCoor()+ "  ");
		}
		return sb.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
//		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + value;
		result = prime * result + x;
		result = prime * result + y;
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
		Coordinate other = (Coordinate) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (value != other.value)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	public boolean isEqual(Coordinate o){
		if(this.x == o.getX() && this.y == o.getY()) return true;
		else return false;
	}
	
	public boolean isSameTrack(Coordinate o){
		if(this.isEqual(o)  && this.trace().equals(o.trace())) return true;
		else return false;
	}
	
}

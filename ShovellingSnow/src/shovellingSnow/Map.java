package shovellingSnow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import shovellingSnow.Coordinate.TYPE;

public class Map {
	public static int BIG_NUMBER = 10000;
	
	int width;
	int height;
	List<List<Coordinate> > map;
	Set<Coordinate> origins;
	public Map (int w, int h){
		width = w;
		height = h;
		map = new ArrayList<List<Coordinate>>();
		origins = new HashSet<Coordinate>();
	}
	/*
	 *  Brute-force attempt:
	 *  From each origin (house), try to walk around in increasing radius.
	 *  A walk is to arrive at a new snow cell. If we meet a freeway, continue walking. 
	 *  Each step will need to keep track of the previous cell.
	 *  Once we cover all 4 friends, we calculate the shortest path
	 * 
	 */
	public HashMap<String, List<Coordinate>> probeMap2(Coordinate origin){
		/*
		 *  We take 1 step at a time to probe the map, starting from one of the house.
		 *  At each step, we find all possible cells that cost 1 snow plow from the set of previously reached cells. 
		 *  The possiblePaths map between friend's name & possible walk to that friend.
		 *  justVist keeps the newest reachable cells at this very step, while path keeps all visited cells
		 *  Once we found all friends, stop and check if there's any duplicated tracks
		 */
		Set<Coordinate> path = new HashSet<Coordinate>();
		path.add(origin);
		HashMap<String, List<Coordinate>> possiblePaths = new HashMap<String, List<Coordinate>>();
		possiblePaths.put(origin.getName(), new ArrayList<Coordinate>());
		while(possiblePaths.size()<4){
			Set<Coordinate> justVisit = new HashSet<Coordinate>();
			for(Coordinate c: path){
				Set<Coordinate> myNeighbor = this.getWalkableNeighbor(c);
				for(Coordinate mn : myNeighbor){
					if(!hasSeenParent(path, mn) || mn.getType()==TYPE.HOUSE && !mn.isEqual(origin) ){
						justVisit.add(mn);
						
					}
				}
			}
			path.addAll(justVisit);
			/*
			 * Let's see if the step will bring us to any of the houses
			 */
			for(Coordinate c : justVisit){
				if(c.isEqual(origin)) continue;
				if(c.getType() == TYPE.HOUSE){
					if(!possiblePaths.containsKey(c.getName())){ //if this is a new House
						possiblePaths.put(c.getName(), new ArrayList<Coordinate>());
					}
					possiblePaths.get(c.getName()).add(c); //add this path to the list
				}
			}

		}
		// there might be some duplicated track
		for(String house : possiblePaths.keySet()){
			List<Coordinate> uniqueTrack = new ArrayList<Coordinate>();
			for(Coordinate c : possiblePaths.get(house)) {
				boolean unique = true;
				for( Coordinate ut : uniqueTrack){
					if(c.isSameTrack(ut)) {
						unique = false;
					}
				}
				if(unique){
					uniqueTrack.add(c);
				}
				
			}
			possiblePaths.put(house, uniqueTrack);
		}
		return possiblePaths;
	}
	@Deprecated
	public HashMap<String, List<Coordinate>> probeMap(Coordinate origin){
		
		List<Set<Coordinate> > path = new ArrayList<Set<Coordinate>>();
		int step =0;
		path.add(new HashSet<Coordinate>());
		path.get(0).add(origin);
		HashMap<String, List<Coordinate>> possiblePaths = new HashMap<String, List<Coordinate>>();
		possiblePaths.put(origin.getName(), new ArrayList<Coordinate>());
		while(possiblePaths.size()<4){  //break condition: once we find all friends
			step++;
			path.add(new HashSet<Coordinate>());
//			System.out.println("Step "+ (step-1));
			for(Coordinate c : path.get(step-1)){  
//				if(step<4) System.out.println("\t" + c.getCoor());
				Set<Coordinate> myNeighbor = this.getWalkableNeighbor(c);
				
				for(Coordinate mn : myNeighbor){
					if(!hasSeenParent(path.get(step), mn) || mn.getType()==TYPE.HOUSE && !mn.isEqual(origin) ){
						path.get(step).add(mn);
					}
				}
			}
			System.out.println(step + "    "+ path.get(step).size());
			boolean foundHouse = false;
			for(Coordinate c : path.get(step)){
				if(c.isEqual(origin)) continue;
				if(c.getType() == TYPE.HOUSE){
					if(!possiblePaths.containsKey(c.getName())){
						possiblePaths.put(c.getName(), new ArrayList<Coordinate>());
						foundHouse = true;
					}
					possiblePaths.get(c.getName()).add(c);
				}
				
				
			}
			if(foundHouse){
				step--;
				path.remove(path.size()-1);
			}
			
		}
		//remove duplicated tracks
		
		for(String house : possiblePaths.keySet()){
			List<Coordinate> uniqueTrack = new ArrayList<Coordinate>();
			for(Coordinate c : possiblePaths.get(house)) {
				boolean unique = true;
				for( Coordinate ut : uniqueTrack){
					if(c.isSameTrack(ut)) {
						unique = false;
					}
				}
				if(unique){
					uniqueTrack.add(c);
				}
				
			}
			possiblePaths.put(house, uniqueTrack);
		}
		
		return possiblePaths;
	}
	public int findLazyPath(){
		/*
		 * Try go from all 4 friends, and return the smallest
		 */
		int min = 1000;
		for(Coordinate origin : origins){
			min = Math.min(min, this.findShortestPath(origin));
		}
		return min;
	}
	public int findShortestPath(Coordinate origin){
		
		HashMap<String, List<Coordinate>> possiblePaths = this.probeMap2(origin);
		
		// find the smallest combination
		int min = 10000;
		int totalCombi = 1;
		/*
		 *  There can be several path to reach each friend's house. for example: from B, 2 different paths to D, 2 to A, 5 to C (just example) 
		 *  We need all combinations of all paths (2*2*5 = 20)
		 */
		List<String> names = new ArrayList<String>();
		for(String house : possiblePaths.keySet()){
			if(house.equals(origin.getName())) continue;
			totalCombi*= possiblePaths.get(house).size();
			names.add(house);
		}
		for(int combi=0;combi<totalCombi;combi++){
			/*
			 * try to make a combination by using division remainder
			 * Example: combi = 5 
			 * 	D is the first name -> idx(D) = 5% 2 (2 path to D) = 1
			 *  A is the second name -> idx(A) = 2 (result of 5/2 from above) % 2 (2 paths to A) = 0
			 *   idx(C) =  1% 5 = 1
			 *   -> DAC = 1 0 1;
			 *   
			 *   Just a simple Combination calculation, no science rocket here.
			 */
			int remainder = combi;
			List<Coordinate> houses = new ArrayList<Coordinate>();
			for(int i = 0; i<names.size();i++){
				if(names.get(i).equals(origin.getName())) continue;
				int length = possiblePaths.get(names.get(i)).size();
				int idx = remainder%length;
				remainder = remainder/length;
				houses.add(possiblePaths.get(names.get(i)).get(idx));
				
			}
			int snow = findNumberOfSnowPlowed(origin, houses);
			min = Math.min(min, snow );
			
		}
		
		
		
		return min;
	}
	public int findNumberOfSnowPlowed(Coordinate origin, List<Coordinate> houses){
		/*
		 * Simply trace back to origin and add 1 if we see a new snow cell. 
		 */
		int ret = 0;
		Set<Coordinate> walked = new HashSet<Coordinate>();
		for(Coordinate c : houses){
			if(c.equals(origin)) continue;
			Coordinate parent = c;
			while(!(parent=parent.getParent()).equals(origin)){
				if(walked.add(parent)){
					ret+= parent.getValue();
				}
			}
		}
		return ret;
	}
	public Set<Coordinate> getWalkableNeighbor(Coordinate origin){
		/*
		 *  Get the walkable neighbors (those requires only 1 dig in the snow)
		 * 
		 */
		Set<Coordinate> walkable = this.getNeighbor(origin);
		HashSet<Coordinate> seen = new HashSet<Coordinate>();
		List<Coordinate> freeways = new ArrayList<Coordinate>();
		for(Coordinate entry : walkable){
			if(entry.getType()==TYPE.FREEWAY){
				freeways.add(entry);
			}
		}
		while(freeways.size()>0){
			Set<Coordinate> nextNeighbors = this.getNeighbor(freeways.get(0));
			for(Coordinate c : nextNeighbors){
				
				if(!c.isEqual(origin)) {
					if(!hasSeen(walkable, c))
						walkable.add(c);
				}
				if(!c.isEqual(origin) && c.getType()==TYPE.FREEWAY){
					
					if(!hasSeen(seen,c)){
						freeways.add(c);
					}
						
				}
			}
			seen.add(freeways.get(0));
			freeways.remove(0);
		}
		
		return walkable;
	}
	
	/*
	 * We need this 2 helper function since equals & hashCode takes all the fields in Coordinate
	 * 
	 */
	private boolean hasSeenParent(Set<Coordinate> seen, Coordinate node ){
		/*
		 * Equal if same coordinator & parent
		 */
		boolean hasSeen = false;
		for(Coordinate s : seen){
			if(s.isEqual(node) && (s.getParent()!=null && node.getParent()!=null && s.getParent().isEqual(node.getParent()))){
				hasSeen = true;
				break;
			}
		}
		return hasSeen;
	}
	private boolean hasSeen(Set<Coordinate> seen, Coordinate node ){
		/*
		 * Equal if same coordinator
		 */
		boolean hasSeen = false;
		for(Coordinate s : seen){
			if(s.isEqual(node)){
				hasSeen = true;
				break;
			}
		}
		return hasSeen;
	}
	public Set<Coordinate> getNeighbor(Coordinate origin){
		/*
		 *  Get the 4 surrounding neighbor. Remove those that are obstacles
		 * 
		 */
		Set<Coordinate> neighbors = new HashSet<Coordinate>();
		int[] step = {-1, 1};
		for(int i=0;i<2;i++){
			//walk vertically
			int x_new = origin.getX()+step[i];
			int y_new = origin.getY();
			if(x_new>=0 && x_new <height && map.get(x_new).get(y_new).getType()!= TYPE.OBSTACLE){
				Coordinate neighbor = map.get(x_new).get(y_new).clone(origin); 
				neighbors.add(neighbor);
			}
			
			//walk horizontally
			x_new = origin.getX();
			y_new = origin.getY()+step[i];
			if(y_new>=0 && y_new <width && map.get(x_new).get(y_new).getType()!= TYPE.OBSTACLE){
				Coordinate neighbor = map.get(x_new).get(y_new).clone(origin);
				neighbors.add(neighbor);
			}
			
		}
		
		return neighbors;
		
	}
	
	public Set<Coordinate> walkFurthestPossible(Coordinate origin, int steps){
		/*
		 *  UNUSED:
		 *  Find the outmost circle of walkable neighbor with certain number of steps (# of snow shovelling)
		 * 
		 */
		Set<Coordinate> ret = null;
		Set<Coordinate> ori = new HashSet<Coordinate>();
		Set<Coordinate> toremove = new HashSet<Coordinate>();
		ori.add(origin);
		for(int i=1;i<=steps;i++){
			toremove.clear();
			Set<Coordinate> neighbors = new HashSet<Coordinate>();
			for(Coordinate entry: ori){
				 neighbors.addAll(this.getWalkableNeighbor(entry));
			}
			
			for(Coordinate entry: neighbors){
				if(ori.contains(entry)) {
					toremove.add(entry);
				}else{
					ori.add(entry);
				}
			}
			for(Coordinate entry: toremove){
				neighbors.remove(entry);
			}
			ret = neighbors;
		}
		
		return ret;
	}
	
	
	public void addLine(int idx, String line){
		/*
		 *  Read in each line of the input. Store each character as a pixel in the map
		 * 
		 */
		if(line.length()!= width){
			System.out.println("Malform input map!");
			System.exit(0);
		}
		map.add(new ArrayList<Coordinate>());
		if(idx!= map.size()-1) {
			System.out.println("Malform input map!");
			System.exit(0);
		}
		for(int i=0;i<width;i++){
			switch (line.charAt(i)) {
				case 'A' : {
					Coordinate a = new Coordinate(idx, i, -1, "A"); 
					map.get(idx).add(a);
					this.origins.add(a);
					break;
				}
				case 'B' : {
					Coordinate b = new Coordinate(idx, i, -1, "B"); 
					map.get(idx).add(b);
					this.origins.add(b);
					break;
				}
				case 'C' : {
					Coordinate c = new Coordinate(idx, i, -1, "C"); 
					map.get(idx).add(c);
					this.origins.add(c);
					break;
				}
				case 'D' : {
					Coordinate d = new Coordinate(idx, i, -1, "D"); 
					map.get(idx).add(d);
					this.origins.add(d);
					break;
				}
				case 'o' : {
					map.get(idx).add(new Coordinate(idx, i, 1, null));
					break;
				}
				case '.' : {
					map.get(idx).add(new Coordinate(idx, i, 0, null));
					break;
				}
				case '#' : {
					map.get(idx).add(new Coordinate(idx, i, BIG_NUMBER, null));
					break;
				}
			}
		}
	}
	
	public void printMap(){
		/*
		 * Testing function 
		 * 
		 */
		for(int i =0;i<map.size();i++){
			for(int j =0;j<map.get(i).size();j++){
				if(map.get(i).get(j).getType()== TYPE.HOUSE){
					System.out.print(map.get(i).get(j).getName()+" ");
				}else if(map.get(i).get(j).getType()== TYPE.SNOW){
					System.out.print("o ");
				}
				else if(map.get(i).get(j).getType()== TYPE.FREEWAY){
					System.out.print(". ");
				}
				else if(map.get(i).get(j).getType()== TYPE.OBSTACLE){
					System.out.print("# ");
				}
			}
			System.out.println();
		}
		
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public List<List<Coordinate>> getMap() {
		return map;
	}

	public Set<Coordinate> getOrigins() {
		return origins;
	}

	public Coordinate getNode(int x, int y){
		return map.get(x).get(y);
	}
	
}

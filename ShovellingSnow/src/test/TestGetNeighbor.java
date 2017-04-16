package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import shovellingSnow.Coordinate;
import shovellingSnow.Map;

public class TestGetNeighbor {
	static Map map = new shovellingSnow.Map(8, 8);
	@Before
	public void setUp() throws Exception {
		
		String current = null;
		int idx = 0;
		try(BufferedReader br = new BufferedReader(new FileReader("/Users/zellist/Documents/workspace/ShovellingSnow/temp"))){
			while((current=br.readLine())!=null){
				map.addLine(idx, current);
				idx++;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}

//	@Test
//	public void testNeighbor() {
//		Coordinate test  = map.getNode(2,0);
//		Set<Coordinate> adjunction = map.getNeighbor(test);
//		Set<Coordinate> neigbors = map.getWalkableNeighbor(test);
//		
//		
//		List<Coordinate> list = new ArrayList<Coordinate>();
//		list.addAll(neigbors);
//		Collections.sort(list);
//		System.out.println(list.size());
//		for(Coordinate entry : list){
//			System.out.println("\t"+entry.getCoor());
//		}
//		
//		
//	}
	
//	@Test
//	public void testWalk() {
//		for( Coordinate node : map.getOrigins()){
//			
//			System.out.println(node.getName()+ " at "+ node.getCoor() + " : "+map.findShortestPath(node));
//		}
//		
//	}
	
	
	@Test
	public void testProbe(){
		System.out.println(map.findLazyPath());
//		for( Coordinate origin : map.getOrigins()){
//			HashMap<String, List<Coordinate>> possiblePaths = map.probeMap2(origin);
////			System.out.println(origin.getName()+ " "+origin.getCoor());
//			for(String house : possiblePaths.keySet()){
//				if(house.equals(origin.getName())) continue;
////				System.out.println("     "+house);
//				for(Coordinate c : possiblePaths.get(house)) {
//						System.out.println("          "+c.getCoor() + " : "+c.trace());
//				}
//			}
////			System.out.print(map.findShortestPath(origin));
//			
//			break;
//		}
	}

}

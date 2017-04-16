package shovellingSnow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Snow {

	
	public static void main(String[] args) {
		BufferedReader br = null;
		String current = null;
		try{
			br = new BufferedReader(new FileReader(args[0]));
			while((current=br.readLine())!=null){
				if(current.isEmpty()) continue;  //skip blank line
				
				//try to read map size 
				String[] tokens = current.split(" ");
				int n=0,m=0;
				n = Integer.parseInt(tokens[0]);
				m = Integer.parseInt(tokens[1]);
				if(n==0 && m==0) break;
				Map map = new Map(n, m); //create new map
				for(int i=0;i<m;i++){
					map.addLine(i, br.readLine()); //read each line and parse the data 
				}
				//cal to findLazyPath
				System.out.println(map.findLazyPath());
			}
			
			
			
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if(br!=null) br. close();
			}catch (Exception e) {
			}
			
		}
		
				
				
				
		

	}

}

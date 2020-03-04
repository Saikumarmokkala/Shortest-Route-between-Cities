import java.util.*;
import java.io.*;

public class find_route  implements Comparator<find_route>  {
	
	int dist;
	find_route parent;
	String name;
	int hist;

	public find_route() {
		
	}
	public find_route(String name,int dist,find_route parent,int hist) {
		this.name = name;
		this.dist = dist;
		this.parent = parent;
		this.hist = hist;
	}
	@Override
	public int compare(find_route o1, find_route o2) {
		//return Integer.compare(o1.dist, o2.dist);
				if((o1.dist + o1.hist)>=(o2.dist +o2.hist))
					return 1;
				else 
					return -1;
	}

	static List<String> source = new ArrayList<String>();
	static List<String> destination = new ArrayList<String>();
	static List<Integer> distance = new ArrayList<Integer>();
	static String startPoint;
	static String endPoint;
	static HashMap<String,Integer> heuristicValue = new HashMap<String,Integer>();
	
	static boolean pathFound = false;
	
	
	public static void findPath(int flag) {
		if(startPoint == null || endPoint ==null) 
			System.out.print("--------------start point or end point not specified--------------");
		PriorityQueue<find_route> que = new PriorityQueue<find_route>(100,new find_route());
		List<String> visitedCities = new ArrayList<String>();
		int numberOfSource = source.size();
		int hValueSize = heuristicValue.size();
		
		
		//adding source
		find_route node1 = new find_route(startPoint,0,null,0);
		que.add(node1);
		
		int nodeExpandedCount = 0;
		int nodeGenCount = 0;
		int maxNodeCount = 0;
		find_route temp = new find_route(null,0,null,0);
		while(que.size()!=0) {
			int lenOfQueue = que.size();
			if(maxNodeCount<lenOfQueue) maxNodeCount = lenOfQueue;
			temp = que.poll();
			nodeExpandedCount++;
			if(temp.name.contentEquals(endPoint)) {
				pathFound = true;
				break;
			}else if(!(visitedCities.contains(temp.name))) {
				for(int i=0;i<numberOfSource;++i) {
					if(temp.name.contentEquals(source.get(i))) {
						
						int hValueOfCity =0;
						// for Informed search
						// getting the h value from the hashMAp
						if(flag ==1) {
							hValueOfCity = heuristicValue.get(destination.get(i));
						}
						
						int currDist = temp.dist+distance.get(i);
						find_route node2 = new find_route(destination.get(i),currDist,temp,hValueOfCity);
						nodeGenCount +=1;
						que.add(node2);
					}
				}
				visitedCities.add(temp.name);
				
			}
			
		}
		if(!pathFound) {
			System.out.print("nodes expanded: " + nodeExpandedCount+"\n");
			System.out.print("nodes generated: "+nodeGenCount+"\n");
			System.out.print("max nodes in memory: "+maxNodeCount+"\n");
			System.out.print("distance : infinity"+"\n");
			System.out.print("route:"+"\n");
			System.out.print("none"+"\n");
			
		}else {
			System.out.print("nodes expanded: " + nodeExpandedCount+"\n");
			System.out.print("nodes generated: "+nodeGenCount+"\n");
			System.out.print("max nodes in memory: "+maxNodeCount+"\n");
			System.out.print("distance :" + temp.dist+"\n");
			System.out.print("route:"+"\n");
			
			List<String> arr = new ArrayList<String>();
			while(temp!=null) {
				arr.add(temp.name);
				temp = temp.parent;
			}
			Collections.reverse(arr);
			for(int i=0;i<arr.size()-1;++i) {
				for(int j=0;j<numberOfSource;++j) {
					if(source.get(j).contentEquals(arr.get(i)) && destination.get(j).contentEquals(arr.get(i+1))) {
						System.out.print(source.get(j)+" to " + destination.get(j)+ "," + distance.get(j)+" km" +"\n");
					}
					
				}
			}
		}
		
	}
	
	public static void readFileUninformed(String[] args,int flag) throws NumberFormatException, IOException {
		// flag-> 0 Uninformed search
		// flag --> 1 Informed search
		
		//https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
		File file = new File(args[0]);
		BufferedReader fileReader = new BufferedReader(new FileReader(file)); 
		String line;
		
 		while((line = fileReader.readLine())!=null) {
			if(line.equalsIgnoreCase("end of Input")) {
				break;
			}
 			String[] splitString = line.split(" ");
			source.add(splitString[0]);
			destination.add(splitString[1]);
			distance.add(Integer.parseInt(splitString[2]));
			
			//duplicate in reverse order
			source.add(splitString[1]);
			destination.add(splitString[0]);
			distance.add(Integer.parseInt(splitString[2]));
		}
 		fileReader.close();
 		if(flag==1) {
 			File file1 = new File(args[3]);
 			fileReader = new BufferedReader(new FileReader(file1));
 			String Hline;
 			while((Hline = fileReader.readLine())!=null) {
 				if(Hline.equalsIgnoreCase("end of input")) {
 					break;
 				}
 				String[] splitLine = Hline.split(" ");
 				heuristicValue.put(splitLine[0],Integer.parseInt(splitLine[1]));
 			}
 			
 		}
 		fileReader.close();
 		
 		// intializing source and destination
 		startPoint = args[1];
 		endPoint = args[2];
 		
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length<4) {
			//Uninformed search ALgorithm
			readFileUninformed(args,0);
			findPath(0);
			
		}else {
			//Informed search 
			readFileUninformed(args,1);
			findPath(1);
			return;
		}

	}
	

}

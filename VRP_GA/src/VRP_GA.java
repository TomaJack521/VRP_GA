//This program is written by Runyu Zhang in 2018-2019 UNNC Year3 GRP project Team 10

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONObject; 
import net.sf.json.JSONArray;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VRP_GA {

	//used to constrain the program
	private final static int MAX_LOCATION = 1000;
	private final static int MAX_VEHICLE = 10;
	private final static int LOOP = 10;
	
	private static int LocationNumber;
	private static int VehicleNumber;
	private double shortestDistance;
	private double[][] distanceMatrix;
	private static Location[] LocationSet;
	private static List<Chromosome> population;
	
	//constructor only used for VehicleNumber and LocationNumber
	public VRP_GA(int VehicleNumber, int LocationNumber) {
		
		VRP_GA.VehicleNumber = VehicleNumber;
		VRP_GA.LocationNumber = LocationNumber;
		
		this.distanceMatrix = new double[LocationNumber][LocationNumber];
		population = new ArrayList<Chromosome>();
		
	}

	
	public static void main(String[] args) {
		
		//main function is used for testing
		Chromosome c;
		VRP_GA vrp = new VRP_GA(1,16);
		vrp.init();
		vrp.selectMode();
		c = population.get(0);

		for(int i = 0; i < 15; i++) {
			vrp = new VRP_GA(2,16);
			vrp.init();
			vrp.selectMode();
			if(population.get(0).fitness < c.fitness) {
				c = population.get(0);
			}
		}
		
		for(int i = 0; i< c.order.length; i++) {
			for(int j = 0; j< c.order[i].length; j++) {
				System.out.println(c.order[i][j]);
			}
		}
		System.out.println(c.fitness);

		/*SAMPLE 
		 * Chromosome c;
		 * VRP_GA vrp = new VRP(2, 50);
		 * vrp.init();
		 * vrp.selectMode();
		 * c = vrp.population.get(0);
		 * c.order is the result.
		 */
	}
	
	//the back end used interface
	public static String vrpAlgorithm(String s) {
		
		Chromosome c;
		Location l;
		
		try {
			
			//get basic info by object
			JsonParser parser = new JsonParser();
			c = new Chromosome();
			l = new Location();
			JsonObject object = (JsonObject)parser.parse(s);
			VehicleNumber = object.get("Vehicle").getAsInt();
			LocationNumber = object.get("Location").getAsInt();
			LocationSet = new Location[LocationNumber];
			
			//constructor for algotrithm
			VRP_GA vrp = new VRP_GA(VehicleNumber,LocationNumber);
			
			//get location info
			JsonArray array = object.get("Locations").getAsJsonArray();
			for(int i = 0; i < LocationNumber; i++) {
				LocationSet[i] = new Location();
				
				JsonObject subObject = array.get(i).getAsJsonObject();
				l.latitude = subObject.get("latitude").getAsDouble();
				l.longitude = subObject.get("longtitude").getAsDouble();
				
				LocationSet[i] = l;
				
			}
			
			vrp.init();
			vrp.selectMode();
			c = population.get(0);
			
			for(int i = 0; i < LOOP; i++) {
				vrp.init();
				vrp.selectMode();
				c = (c.fitness > population.get(0).fitness)?c:population.get(0);
			}
			
			return transferJson(c);
		}catch(JsonIOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	//mode select function
	public void selectMode() {
		if(VehicleNumber == 1 && LocationNumber <= 50) {
			mode1();
		}
		if(VehicleNumber == 2 && LocationNumber < 50) {
			mode2();
		}
		if(VehicleNumber == 1 && LocationNumber > 50) {
			mode3();
		}
		else {
			modeL();
		}
	}
	
	//details implement of different mode
	public void mode1() {
		int geneNumber = 10;
		int geneSize = 20;
		int roll;
		Random rand = new Random();
		int couter = 0;
		
		for(int i = 0; i < geneNumber; i++) { //determine the generation
			for(int j = 0; j < geneSize; j++) { //determine the size of each generation
				
				roll = rand.nextInt(2); //select the each chromosome gene type
				if(roll == 0) {
					population.add(population.get(j).singleVehicleMutation());
				}
				if(roll == 1) {
					population.add(population.get(j).singleVehicleCut());
				}
				
				population.get(population.size()-1).setFitness(calFit(population.get(population.size()-1)));
			}
			
			//order the population
			orderPop(geneSize);
			//deletePop(geneSize);
			removePop(geneSize);
			System.out.println(population.size());
			shortestDistance = population.get(0).getFitness();
			System.out.println(shortestDistance);
		}
		
	}
	
	public void mode2() {
		int geneNumber = 10;
		int geneSize = 20;
		int roll;
		Random rand = new Random();
		
		for(int i = 0; i < geneNumber; i++) {
			for(int j = 0; j < geneSize; j++) {
				roll = rand.nextInt(4);
				if(roll == 0) {
					population.add(population.get(j).singleVehicleMutation());
				}else if(roll == 1) {
					population.add(population.get(j).singleVehicleCut());
				}else if(roll == 2) {
					population.add(population.get(j).doubleVehicleMutation());
				}else {
					population.add(population.get(j).doubleVehicleCut());
				}
				
				population.get(population.size()-1).setFitness(calFit(population.get(population.size()-1)));
			}
			
			//order the population
			orderPop(geneSize);
			//deletePop(geneSize);
			removePop(geneSize);
			//System.out.println(population.size());
//			shortestDistance = population.get(0).getFitness();
//			for(int m = 0; m < population.get(0).order.length; m++) {
//				for(int n = 0; n < population.get(0).order[m].length; n++) {
//					System.out.print(population.get(0).order[m][n] +" ");
//				}
//				System.out.println();
//			}

		}
	}
	
	public void mode3() {
		int geneNumber = 15;
		int geneSize = 30;
		int roll;
		Random rand = new Random();
		int couter = 0;
		
		for(int i = 0; i < geneNumber; i++) {
			
			
			for(int j = 0; j < geneSize; j++) {
				roll = rand.nextInt(2);
				if(roll == 0) {
					population.add(population.get(j).singleVehicleMutation());
				}
				if(roll == 1) {
					population.add(population.get(j).singleVehicleCut());
				}
				
				population.get(population.size()-1).setFitness(calFit(population.get(population.size()-1)));
			}
			
			//order the population
			orderPop(geneSize);
			//deletePop(geneSize);
			removePop(geneSize);
			System.out.println(population.size());
			shortestDistance = population.get(0).getFitness();
			System.out.println(shortestDistance);
//			for(int m = 0; m<population.size(); m++) {
//				for(int n = 0; n < population.get(m).order[0].length; n++) {
//					System.out.print(population.get(m).order[0][n] + " ");
//				}
//				System.out.print("  " + population.get(m).fitness);
//				System.out.println();
//			}

		}
	}
	
	public void modeL() {
		int geneNumber = 20;
		int geneSize = 50;
		int roll;
		Random rand = new Random();
		
		for(int i = 0; i < geneNumber; i++) {
			for(int j = 0; j < geneSize; j++) {
				roll = rand.nextInt(4);
				if(roll == 0) {
					population.add(population.get(j).singleVehicleMutation());
				}else if(roll == 1) {
					population.add(population.get(j).singleVehicleCut());
				}else if(roll == 2) {
					population.add(population.get(j).doubleVehicleMutation());
				}else {
					population.add(population.get(j).doubleVehicleCut());
				}
				
				population.get(population.size()-1).setFitness(calFit(population.get(population.size()-1)));
			}
			
			//order the population
			orderPop(geneSize);
			//deletePop(geneSize);
			removePop(geneSize);
			//System.out.println(population.size());
//			shortestDistance = population.get(0).getFitness();
//			for(int m = 0; m < population.get(0).order.length; m++) {
//				for(int n = 0; n < population.get(0).order[m].length; n++) {
//					System.out.print(population.get(0).order[m][n] +" ");
//				}
//				System.out.println();
//			}

		}
	}
	
	//used to sort by fitness
	public void orderPop(int geneSize) {
		
		int next = 0;
		if(population.size() < geneSize) {
			geneSize = population.size();
		}
		double min = population.get(next).getFitness();
		for(int i = 0; i < geneSize; i++) {
			for(int j = i; j < population.size(); j++) {
				if(min > population.get(j).getFitness()) {
					next = j;
					min = population.get(j).getFitness();
				}
			}
			
			population.add(i, population.get(next));
			population.remove(next+1);
		}
			
	}
	
	//used to delete the same chromosome
	public void deletePop(int geneSize) {
		if(population.size() < geneSize) {
			geneSize = population.size();
		}
		int i, j;
		i = 0;
		j = i+1;
		while(i< population.size()) {
			while(j < population.size()) {
				if(population.get(i).fitness == population.get(j).fitness) {
					population.remove(j);
				}else {
					j++;
				}
			}
			i++;
			j = i+1;
		}
	}
	
	//remove the chromosome more than the popSize
	public void removePop(int geneSize) {
		while(population.size()>geneSize) {
			population.remove(population.size()-1);
		}
	}
	
	//transfer the info to Json string
	public static String transferJson(Chromosome c) {
		JSONArray jsonArray = new JSONArray();
		JSONObject json1 = new JSONObject();
		json1.accumulate("distance", c.fitness);
		jsonArray.add(json1);
		
		JSONObject json;
		for(int i = 0; i < c.order.length; i++) {
			json = new JSONObject();
			for(int j = 0; j < c.order[i].length; j++) {
				String str = String.valueOf(j);
				json.accumulate(str, c.order[i][j]);
			}
			jsonArray.add(json);
		}
		
		return jsonArray.toString();
	}
	
	//test if the Location and Vehicle number are over the MAX recommend
	public boolean test() {

		return (LocationNumber < MAX_LOCATION && VehicleNumber < MAX_VEHICLE? true: false);
		
	}
	
	//init dataset after explaining the json string
	public void init() {
		distanceMatrix = new double[LocationNumber][LocationNumber];
		distanceMatrix = calculateDistance(LocationSet);
		Chromosome c = new Chromosome(VehicleNumber, LocationNumber);
		c.setFitness(calFit(c));
		population.add(c);
		shortestDistance = population.get(0).getFitness();

	}
	
	//calculate the chromosome fitness means the all vehicles and locations distance
	public double calFit(Chromosome c) {
		double fitness = 0;
		int i = 0,j = 0;

		for(; i < c.order.length; i++) { //select the vehicle
			for(j = 1; j < c.order[i].length; j++) { //select the location
				fitness = fitness + distanceMatrix[c.order[i][j-1]][c.order[i][j]];
			}
			
			//add distance to start location
			fitness = fitness + distanceMatrix[0][c.order[i][0]] + distanceMatrix[0][c.order[i][c.order[i].length-1]];
		}
		
		return fitness;
	}
	
	//details functions to build distance matrix
	public double[][] calculateDistance(Location[] locationSet) {
		
		int i,j;
		
		double[][] distanceMatrix = new double[LocationNumber][LocationNumber];
		
		for(i = 0; i < LocationNumber; i++) {
			for(j = i; j < LocationNumber; j++) {
				
				if(j == i) {
					distanceMatrix[i][i] = 0;
				}
				
				distanceMatrix[i][j] = 
						Math.sqrt((locationSet[i].latitude-locationSet[j].latitude)*(locationSet[i].latitude-locationSet[j].latitude)+(locationSet[i].longitude-locationSet[j].longitude)*(locationSet[i].longitude-locationSet[j].longitude));
				distanceMatrix[j][i] = distanceMatrix[i][j];
				
			}
		}
		
		return distanceMatrix;
		
	}
	
	//some setter and getter
	public int getLocationNumber() {
		return LocationNumber;
	}

	public void setLocationNumber(int locationNumber) {
		LocationNumber = locationNumber;
	}

	public int getVehicleNumber() {
		return VehicleNumber;
	}

	public void setVehicleNumber(int vehicleNumber) {
		VehicleNumber = vehicleNumber;
	}
}

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//import org.json.JSONObject;
import net.sf.json.JSONObject;  
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;


public class VRP_GA {

	private final static int MAX_LOCATION = 1000;
	private final static int MAX_VEHICLE = 10;
	private final static int LOOP = 10;
	
	private int LocationNumber;
	private int VehicleNumber;
	private double shortestDistance;
	private double[][] distanceMatrix;
	private Location[] LocationSet;
	private List<Chromosome> population;

	
	//test if the Location and Vehicle number are over the MAX recommend
	public boolean test() {

		
		return (LocationNumber < MAX_LOCATION && VehicleNumber < MAX_VEHICLE? true: false);
	}
	
	public VRP_GA(int VehicleNumber, int LocationNumber) {
		
		this.VehicleNumber = VehicleNumber;
		this.LocationNumber = LocationNumber;
		
		
		this.distanceMatrix = new double[VehicleNumber][LocationNumber];
		this.population = new ArrayList<Chromosome>();
	}
	
	public void init() {
		
		distanceMatrix = calculateDistance(LocationSet);
		Chromosome c = new Chromosome(VehicleNumber, LocationNumber);
		c.setFitness(calFit(c));
		population.add(c);
		shortestDistance = population.get(0).getFitness();
			
	}
	
	
	public double calFit(Chromosome c) {
		//this function is used to calculate the single chromosome fitness
		double fitness = 0;
		int i = 0,j = 0;

		for(; i < c.order.length; i++) {
			for(j = 1; j < c.order[i].length; j++) {
				fitness = fitness + distanceMatrix[c.order[i][j-1]][c.order[i][j]];
			}
			fitness = fitness + distanceMatrix[0][c.order[i][0]] + distanceMatrix[0][c.order[i][c.order[i].length-1]];
		}
		
		return fitness;
	}
	
	
	public double[][] calculateDistance(Location[] locationSet) {
		
		int i,j;
		int counter = 0;
		
		
		double[][] distanceMatrix = new double[LocationNumber][LocationNumber];
		for(i = 0; i < LocationNumber; i++) {
			//System.out.println(locationSet[i].latitude + "    " + locationSet[i].longitude);;
			for(j = i; j < LocationNumber; j++) {
				
				distanceMatrix[i][j] = Math.sqrt((locationSet[i].latitude-locationSet[j].latitude)*(locationSet[i].latitude-locationSet[j].latitude)+(locationSet[i].longitude-locationSet[j].longitude)*(locationSet[i].longitude-locationSet[j].longitude));
				distanceMatrix[j][i] = distanceMatrix[i][j];
				counter++;
			}
		}
		
		return distanceMatrix;
		
	}
	
	public static void main(String[] args) {
		
		//main function is used for testing
		Chromosome c;
		VRP_GA vrp = new VRP_GA(2,16);
		vrp.init();
		vrp.selectMode();
		c = vrp.population.get(0);

		for(int i = 0; i < 15; i++) {
			vrp = new VRP_GA(2,16);
			vrp.init();
			vrp.selectMode();
			if(vrp.population.get(0).fitness < c.fitness) {
				c = vrp.population.get(0);
			}
		}
		
		for(int i = 0; i< c.order.length; i++) {
			for(int j = 0; j< c.order[i].length; j++) {
				System.out.println(c.order[i][j]);
			}
		}
		System.out.println(c.fitness);

	}
	
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
	
	public void mode1() {
		int geneNumber = 10;
		int geneSize = 20;
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
		//printResult();
		
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
	
	public void orderPop(int geneSize) {
		int next = 0;
		if(population.size() < geneSize) {
			geneSize = population.size();
		}
		double min = population.get(next).getFitness();
		for(int i = 0; i < geneSize; i++) {
			for(int j = i; j < geneSize; j++) {
				if(min > population.get(j).getFitness()) {
					next = j;
					min = population.get(j).getFitness();
				}
			}
			population.add(i, population.get(next));
			population.remove(next+1);
		}
			
		
	}
	
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
	
	public void removePop(int geneSize) {
		while(population.size()>geneSize) {
			population.remove(population.size()-1);
		}
	}
	
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
	
	public String transferJson(Chromosome c) {
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

	public String vrpAlgorithm(String s) {
		
		Chromosome c;
		Location l;
		String result;
		
		try {
			
			//get basic info by object
			JsonParser parser = new JsonParser();
			c = new Chromosome();
			l = new Location();
			JsonObject object = (JsonObject)parser.parse(s);
			this.VehicleNumber = object.get("Vehicle").getAsInt();
			this.LocationNumber = object.get("Location").getAsInt();
			this.LocationSet = new Location[LocationNumber];
			
			//constructor for algotrithm
			VRP_GA vrp = new VRP_GA(VehicleNumber,LocationNumber);
			
			//get location info
			JsonArray array = object.get("Locations").getAsJsonArray();
			for(int i = 0; i < LocationNumber; i++) {
				vrp.LocationSet[i] = new Location();
				
				JsonObject subObject = array.get(i).getAsJsonObject();
				l.latitude = subObject.get("latitude").getAsDouble();
				l.longitude = subObject.get("longtitude").getAsDouble();
				
				vrp.LocationSet[i] = l;
				
			}
			
			vrp.init();
			vrp.selectMode();
			c = population.get(0);
			
			for(int i = 0; i < LOOP; i++) {
				vrp.init();
				vrp.selectMode();
				c = (c.fitness > vrp.population.get(0).fitness)?c:vrp.population.get(0);
			}
			
			return transferJson(c);
		}catch(JsonIOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
}

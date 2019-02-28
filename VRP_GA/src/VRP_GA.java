import java.util.ArrayList;
import java.util.List;


public class VRP_GA {

	private final static int MAX_LOCATION = 1000;
	private final static int MAX_VEHICLE = 10;
	
	private int LocationNumber;
	private int VehicleNumber;
	private int popSize;
	private double shortestDistance;
	private int whichChromosome;
	private double[][] distanceMatrix;
	private Location[] LocationSet;
	private Vehicle[] VehicleSet;
	
	private List<Chromosome> population = new ArrayList<Chromosome>();

	
	//test if the Location and Vehicle number are over the MAX recommend
	public boolean test() {
//		if(LocationNumber < MAX_LOCATION && VehicleNumber < MAX_VEHICLE) {
//			return true;
//		}else {
//			return false;
//		}
		
		return (LocationNumber < MAX_LOCATION && VehicleNumber < MAX_VEHICLE? true: false);
	}
	
	public VRP_GA(int VehicleNumber, int LocationNumber) {
		
		this.VehicleNumber = VehicleNumber;
		this.LocationNumber = LocationNumber;
		
		this.VehicleSet = new Vehicle[VehicleNumber];
		this.LocationSet = new Location[LocationNumber];
		
		this.distanceMatrix = new double[VehicleNumber][LocationNumber];
		
	}
	
	public void init() {
		
			Chromosome c = new Chromosome();
			c.setFitness(calFit(c));
			population.add(c);
			shortestDistance = population.get(0).getFitness();
			whichChromosome = 0;
	}
	
	public  static void addData() {
		
		//use this function to get location and vehicle from the backend
		
	}
	
	public double calFit(Chromosome c) {
		//this function is used to calculate the single chromosome fitness
		double fitness = 0;
		int i,j;
		
		for(i = 0; i < c.order.length; i++) {
			for(j = 0; j < c.order[i].length; j++) {
				if(j == 0) {
					
					fitness = distanceMatrix[0][c.order[i][j]];
					
				}else if(c.order[i][j] == 0) {
					
					fitness += distanceMatrix[0][c.order[i][j-1]];
					break;
				}else {
				
					fitness += distanceMatrix[c.order[i][j-1]][c.order[i][j]];
				
				}
			}
		}
		
		
		return fitness;
	}
	
	public void addLocationData(Location l, int number) {
		LocationSet[number] = l;
	}
	public void addVehicleData(Vehicle v, int number) {
		VehicleSet[number] = v;
	}
	
	public double[][] calculateDistance(Location[] locationSet) {
		
		int i,j;
		
		if(locationSet[0].ifStart != true) {
			Location exchange = new Location();
			exchange = locationSet[0];
			for(i = 1; i < LocationNumber; i++) {
				if(locationSet[i].ifStart == true) {
					locationSet[0] = locationSet[i];
					locationSet[i] = exchange;
					break;
				}
				else {
					continue;
				}
			}
		}
		
		double[][] distanceMatrix = new double[VehicleNumber][LocationNumber];
		for(i = 0; i < LocationNumber; i++) {
			for(j = i; j < LocationNumber; j++) {
				distanceMatrix[i][j] = 
						Math.sqrt((locationSet[i].latitude-locationSet[j].latitude)*(locationSet[i].latitude-locationSet[j].latitude)+(locationSet[i].longitude-locationSet[j].longitude)*(locationSet[i].longitude-locationSet[j].longitude));
				distanceMatrix[j][i] = distanceMatrix[i][j];
			}
		}
		
		return distanceMatrix;
		
	}
	
	public static void main(String[] args) {
		addData();

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

	
	public void setPopSize(int popSize) {
		this.popSize = popSize;
	}
}

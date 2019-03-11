import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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
	private List<Chromosome> population;

	
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
		this.LocationSet = new Location[LocationNumber];
		this.VehicleSet = new Vehicle[VehicleNumber];
		
		this.distanceMatrix = new double[VehicleNumber][LocationNumber];
		this.population = new ArrayList<Chromosome>();
	}
	
	public void init() {
		
			Chromosome c = new Chromosome();
			c.setFitness(calFit(c));
			population.add(c);
			shortestDistance = population.get(0).getFitness();
			whichChromosome = 0;
			distanceMatrix = calculateDistance(LocationSet);
	}
	
	public  static Location addData() {
		
		//use this function to get location and vehicle from the backend
		Location l = new Location();
		
		return l;
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
		
		VRP_GA vrp = new VRP_GA(1,15);
		
		for(int i = 0; i < vrp.VehicleNumber; i++) {
			vrp.LocationSet[i] = addData();
		}
		vrp.init();
		//after the data input into the function, it will decide which algorithm and strategy to use.
		vrp.selectMode();
		//the population[0]is the best solution for the problem

	}
	
	public void selectMode() {
		if(VehicleNumber == 1 && LocationNumber <= 50) {
			mode1();
		}
		if(VehicleNumber >= 2 && LocationNumber < 50) {
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
		int geneNumber = 500;
		int geneSize = 20;
		int roll;
		Random rand = new Random();
		for(int i = 0; i < geneNumber; i++) {
			//order the population
			orderPop(geneSize);
			removePop(geneSize);
			for(int j = 0; j < geneSize; j++) {
				roll = rand.nextInt(2);
				if(roll == 0) {
					population.add(population.get(j).singleVehicleMutation());
				}
				if(roll == 1) {
					population.add(population.get(j).singleVehicleCut());
				}
				population.get(geneSize+i).setFitness(calFit(population.get(geneSize+i)));
			}
		}
		
	}
	
	public void mode2() {
		
	}
	
	public void mode3() {
		
	}
	
	public void modeL() {
		
	}
	
	public void orderPop(int geneSize) {
		int next = 0;
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

	
	public void setPopSize(int popSize) {
		this.popSize = popSize;
	}
}

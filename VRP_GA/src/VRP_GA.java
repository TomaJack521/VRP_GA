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
	private double[][] distanceMatrix;
	private Location[] LocationSet;
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
		
		
		this.distanceMatrix = new double[VehicleNumber][LocationNumber];
		this.population = new ArrayList<Chromosome>();
	}
	
	public void init() {
		
		LocationSet = new Location[LocationNumber];
		for(int i = 0; i < LocationNumber; i++) {
			LocationSet[i] = new Location();
			LocationSet[i].latitude = 5*100*i;
			LocationSet[i].longitude = 2*100*i;
			
		}
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
//		for(i = 0; i < c.order.length; i++) {
//			for(j = 0; j < c.order[i].length; j++) {
//				if(j == 0) {
//					
//					fitness += distanceMatrix[0][c.order[i][0]];
//					
//				}else {
//					if(c.order[i][j] == 0) {
//						break;
//					}
//					fitness += distanceMatrix[c.order[i][j-1]][c.order[i][j]];
//				
//				}
//			}
//			fitness += distanceMatrix[0][c.order[i][j-1]];
//		}
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
		
		VRP_GA vrp = new VRP_GA(1,15);
		
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
			shortestDistance = population.get(0).getFitness();
			System.out.println(i+ ": " + shortestDistance);
			
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
			
			
			
		}
		//printResult();
		
	}
	
	public void mode2() {
		
	}
	
	public void mode3() {
		
	}
	
	public void modeL() {
		
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
	
//	public void printResult() {
//		
//		for(int i = 0; i < population.get(0).order.length; i++) {
//			for(int j = 0; j < population.get(0).order[i].length; i++) {
//				System.out.print(population.get(0).order[i][j]+" ");
//				System.out.println("");
//			}
//		}
//	}
}

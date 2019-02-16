import java.util.ArrayList;

public class VRP_GA {

	private final static int MAX_LOCATION = 1000;
	private final static int MAX_VEHICLE = 10;
	
	private int LocationNumber = 0;
	private int VehicleNumber = 0;
	

	Location[] locationSet = new Location[LocationNumber];
	Vehicle[] vehicleSet = new Vehicle[VehicleNumber];
	double[][] distanceMatrix = new double[LocationNumber][LocationNumber];
	int[][] oldMatrix = new int[VehicleNumber][LocationNumber];
	int[][] newMatrix = new int[VehicleNumber][LocationNumber];
	
	//test if the Location and Vehicle number are over the MAX recommend
	public boolean test() {
//		if(LocationNumber < MAX_LOCATION && VehicleNumber < MAX_VEHICLE) {
//			return true;
//		}else {
//			return false;
//		}
		
		return (LocationNumber < MAX_LOCATION && VehicleNumber < MAX_VEHICLE? true: false);
	}
	
	public void calculateDistance() {
		
		int i,j;
		
		if(locationSet[0].ifStart != true) {
			Location exchange = new Location();
			exchange = locationSet[0];
			for(i = 1; i < LocationNumber; i++) {
				if(locationSet[i].ifStart == true) {
					locationSet[0] = locationSet[i];
					locationSet[i] = exchange;
					return;
				}
				else {
					continue;
				}
			}
		}
		
		for(i = 0; i < LocationNumber; i++) {
			for(j = i; j < LocationNumber; j++) {
				distanceMatrix[i][j] = 
						Math.sqrt((locationSet[i].latitude-locationSet[j].latitude)*(locationSet[i].latitude-locationSet[j].latitude)+(locationSet[i].longitude-locationSet[j].longitude)*(locationSet[i].longitude-locationSet[j].longitude));
				distanceMatrix[j][i] = distanceMatrix[i][j];
			}
		}
		
	}
	
	public static void main(String[] args) {
		VRP_GA algorithm = new VRP_GA();
		if(!algorithm.test()) {
			System.out.println("No start location");
		}
		algorithm.calculateDistance();
		

	}

}

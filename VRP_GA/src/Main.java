
public class Main extends VRP_GA{

	public Main(int VehicleNumber, int LocationNumber) {
		super(VehicleNumber, LocationNumber);
	}
	
	public static void main(String[] args) {
		
		Main.vrpAlgorithm(args[1]);
	}

}

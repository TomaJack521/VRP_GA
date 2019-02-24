
public class Chromosome {
	
	private double fitness;
	public int[][] order;
	
	public double getFitness() {
		return fitness;
	}
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public Chromosome() {
		
	}
	
	public Chromosome(int VehicleNumber, int LocationNumber, int size) {
		if(VehicleNumber <= 0 || LocationNumber <= 0) {
			return;
		}
		init(VehicleNumber, LocationNumber);
		for(int i = 0; i < VehicleNumber; i++) {
			for(int j = 0; j < LocationNumber; j++) {
				order[i][j] = 0;
			}
		}
		
		int set = 1;
		for(int i = 0; i < VehicleNumber; i++) {
			if(i < (LocationNumber%VehicleNumber)) {
				for(int j = 0; j < Math.ceil(LocationNumber/VehicleNumber); j++) {
					order[i][j] = set;
					set++;
				}
			}else {
				for(int j = 0; j < LocationNumber/VehicleNumber; j++) {
					order[i][j] = set;
					set++;
				}
			}
		}
		
	}
	
	
	public void init(int VehicleNumber, int LocationNumber) {
		order = new int[VehicleNumber][LocationNumber];
		fitness = 0;
		
	}
	
	public Chromosome clone(Chromosome c) {
		if(c == null || c.order == null) {
			return null;
		}
		
		Chromosome copy = new Chromosome();
		copy.init(c.order.length, c.order[0].length);
		
		for(int i = 0; i < c.order.length; i++) {
			for(int j = 0; j < c.order[0].length; j++) {
				copy.order[i][j] = c.order[i][j];
				copy.fitness = c.fitness;
			}
		}
		
		return copy;
	}
	
	public Chromosome singleVehicleMutation(Chromosome c) {
		int vehicle, location1, location2;
		int replace;
		Chromosome newOne = new Chromosome();
		newOne = newOne.clone(c);
		
		vehicle = (int)(0+Math.random()*((c.order.length-1)-1+1));
		location1 = (int)(0+Math.random()*((c.order[vehicle].length-1)-1+1));
		location2 = (int)(0+Math.random()*((c.order[vehicle].length-1)-1+1));
		while(location1==location2) {
			location2 = (int)(0+Math.random()*((c.order[vehicle].length-1)-1+1));
		}
		replace = newOne.order[vehicle][location1];
		newOne.order[vehicle][location1] = newOne.order[vehicle][location2];
		newOne.order[vehicle][location2] = replace;
		
		return newOne;
	}
	
	public Chromosome doubleVehicleMutation(Chromosome c) {
		int vehicle1, vehicle2, location1, location2;
		int replace;
		Chromosome newOne = new Chromosome();
		newOne = newOne.clone(c);
		
		vehicle1 = (int)(0+Math.random()*((c.order.length-1)-1+1));
		vehicle2 = (int)(0+Math.random()*((c.order.length-1)-1+1));
		while(vehicle1 == vehicle2) {
			vehicle2 = (int)(0+Math.random()*((c.order.length-1)-1+1));
		}
		
		location1 = (int)(0+Math.random()*((c.order[vehicle1].length-1)-1+1));
		location2 = (int)(0+Math.random()*((c.order[vehicle2].length-1)-1+1));
		
		replace = newOne.order[vehicle1][location1];
		newOne.order[vehicle1][location1] = newOne.order[vehicle2][location2];
		newOne.order[vehicle2][location2] = replace;
		
		return newOne;
	}
	
	public Chromosome singleVehicleCut(Chromosome c) {
		int size, vehicle, location1, location2;
		int[] replace;
		Chromosome newOne = new Chromosome();
		
		size = (int)(2+Math.random()*(4-2+1));
		
	}
	
	public void mutation(Chromosome c) {
		int size, c1, c2;
		size = (int)(1+Math.random()*(3-1+1));
		c1 = (int)(Math.random()*((c.order.length))-1+1);
		c2 = (int)(Math.random()*((c.order.length))-1+1);
		while(size > c.order[c1].length || size > c.order[c2].length) {
			break;
		}
			size =(int)(1+Math.random()*(3-1+1));
		
	}
}

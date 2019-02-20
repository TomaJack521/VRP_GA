
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
	
	public void mutation(Chromosome c) {
		int size, c1, c2;
		size = (int)(1+Math.random()*(3-1+1));
		c1 = (int)(Math.random()*((c.order.length))-1+1);
		c2 = (int)(Math.random()*((c.order.length))-1+1);
		while(true) {
			break;
		}
			size =(int)(1+Math.random()*(3-1+1));
		
	}
}

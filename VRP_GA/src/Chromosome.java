//This program is written by Runyu Zhang in 2018-2019 UNNC Year3 GRP project Team 10

import java.util.Random;

public class Chromosome {
	
	double fitness; //amount distance
	int[][] order; //record the order of each car
	
	public double getFitness() {
		return fitness;
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public Chromosome() {
		
	}
	
	public Chromosome(int VehicleNumber, int LocationNumber) {
		if(VehicleNumber <= 0 || LocationNumber <= 0) {
			return;
		}
		
		//build the order by seperate it average
		order = new int[VehicleNumber][];
		int set = 1;
		for(int i = 0; i < order.length; i++) {
			if(i < (LocationNumber-1)%VehicleNumber) {
				order[i] = new int[(int)Math.ceil((LocationNumber-1)/VehicleNumber)];
				for(int j = 0; j < order[i].length; j++) {
					order[i][j] = set;
					set++;
				}
			}else {
				order[i] = new int[(int)(LocationNumber-1)/VehicleNumber];
				for(int j = 0; j < order[i].length; j++) {
					order[i][j] = set;
					set++;
				}
			}
		}
		
	}
	
	//clone a absolute same chromosome
	public Chromosome clone() {
		
		Chromosome copy = new Chromosome();

		copy.fitness = this.fitness;
		copy.order = new int[this.order.length][];
		for(int i = 0; i < this.order.length; i++) {
			copy.order[i] = new int[this.order[i].length];
			for(int j = 0; j < copy.order[i].length; j++) {
				copy.order[i][j] = this.order[i][j];
			}
		}
		
		return copy;
	}
	
	//detail functions in four ways
	public Chromosome singleVehicleMutation() {
		int vehicle, location1, location2;
		int replace;
		Chromosome newOne = new Chromosome();
		newOne = this.clone();
		Random rand = new Random();
		
		vehicle = rand.nextInt(newOne.order.length);
		location1 = rand.nextInt(newOne.order[vehicle].length);
		location2 = rand.nextInt(newOne.order[vehicle].length);
		while(location1==location2) {
			
			location2 = rand.nextInt(newOne.order[vehicle].length);
		}
		replace = newOne.order[vehicle][location1];
		newOne.order[vehicle][location1] = newOne.order[vehicle][location2];
		newOne.order[vehicle][location2] = replace;
		System.out.println("0");
		return newOne;
	}
	
	public Chromosome doubleVehicleMutation() {
		int vehicle1, vehicle2, location1, location2;
		int replace;
		Chromosome newOne = new Chromosome();
		newOne = this.clone();
		Random rand = new Random();
		
		vehicle1 = rand.nextInt(newOne.order.length);
		vehicle2 = rand.nextInt(newOne.order.length);
		while(vehicle1 == vehicle2) {
			vehicle2 = rand.nextInt(newOne.order.length);
		}
		
		location1 = rand.nextInt(newOne.order[vehicle1].length);
		location2 = rand.nextInt(newOne.order[vehicle2].length);
		
		replace = newOne.order[vehicle1][location1];
		newOne.order[vehicle1][location1] = newOne.order[vehicle2][location2];
		newOne.order[vehicle2][location2] = replace;
		System.out.println("1");
		return newOne;
	}

	
	public Chromosome singleVehicleCut() {
		int size, vehicle, location1, location2;
		int replace;
		Chromosome newOne = new Chromosome();
		newOne = this.clone();
		Random rand = new Random();
		
		//size = 2 + rand.nextInt(3);
		vehicle = rand.nextInt(newOne.order.length);
		size = rand.nextInt(Math.max(2, newOne.order[vehicle].length/4));
		//vehicle = rand.nextInt(newOne.order.length);

		location1 = rand.nextInt(newOne.order[vehicle].length - size);
		location2 = rand.nextInt(newOne.order[vehicle].length - size);
		while(location1-size <= location2 && location2 <= location1+size) {
			location2 = rand.nextInt(newOne.order[vehicle].length - size);
		}
		
		for(int i = 0; i < size; i++) {
			replace = newOne.order[vehicle][location1+i];
			newOne.order[vehicle][location1+i] = newOne.order[vehicle][location2+i];
			newOne.order[vehicle][location2+i] = replace;
		}

		return newOne;
		
	}
	
	public Chromosome doubleVehicleCut() {
		int size, vehicle1, vehicle2, location1, location2;
		int replace;
		Chromosome newOne = new Chromosome();
		newOne = this.clone();
		Random rand = new Random();
		
		vehicle1 = rand.nextInt(newOne.order.length);
		vehicle2 = rand.nextInt(newOne.order.length);
		while(vehicle1 == vehicle2) {
			vehicle2 = rand.nextInt(newOne.order.length);
		}
		
		size = rand.nextInt(Math.max(2, newOne.order[vehicle1].length/4));
		
		location1 = rand.nextInt(newOne.order[vehicle1].length - size);
		location2 = rand.nextInt(newOne.order[vehicle2].length - size);
		
		for(int i = 0; i < size; i++) {
			replace = newOne.order[vehicle1][location1+i];
			newOne.order[vehicle1][location1+i] = newOne.order[vehicle2][location2+i];
			newOne.order[vehicle2][location2+i] = replace;
		}

		return newOne;
	}
	
}

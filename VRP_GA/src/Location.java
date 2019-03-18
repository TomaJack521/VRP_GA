
public class Location {
	public double longitude;
	public double latitude;
	
	
	public Location() {
		this.longitude = 0;
		this.latitude = 0;
	}
	
	public Location(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public Location(double longitude, double latitude, boolean ifStart, boolean ifEnd) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
}

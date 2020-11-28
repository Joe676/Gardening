package DAO;

public class Pair {
	public long first;
	public int second;
	
	public Pair(long f, int s) {
		first = f;
		second = s;
	}
	
	public Pair(String p) {
		first = Long.parseLong(p.split("\\|")[0]);
		second = Integer.parseInt(p.split("\\|")[1]);
	}
	
	public String toString() {
		return first + "|" + second; 
	}
}

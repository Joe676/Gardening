package DAO;

public class Offer {
	private long id;
	private String name;
	private String unit;
	private int cost;
	
	public Offer(long id_, String name_, String unit_, int cost_) {
		setId(id_);
		setName(name_);
		setUnit(unit_);
		setCost(cost_);
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public String toString() {
		return id+";"+name+";"+unit+";"+cost;
	}
}

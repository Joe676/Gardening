package DAO;


public class Client {
	private long id;
	private String name;
	
	public Client(long id, String name) {
		setId(id);
		setName(name);
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
	
	public String toString() {
		return id+";"+name;
	}
	
}

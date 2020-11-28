package DAO;

public class ToDo {
	private long id;
	private long orderId;
	private long workerId;
	
	public ToDo(long id, long orderId, long workerId) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.workerId = workerId;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public long getWorkerId() {
		return workerId;
	}
	public void setWorkerId(long workerId) {
		this.workerId = workerId;
	}
	
	public String toString() {
		return id+";"+orderId+";"+workerId;
	}
}

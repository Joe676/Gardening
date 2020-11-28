package DAO;

import java.util.List;


public class Order {
	private long id;
	private String startDate;
	private long clientID;
	private int orderStatus;
	private List<Pair> orderedService;
	
	
	public Order(long id, String startDate, long client, int orderStatus, List<Pair> orderedService) {
		this.id = id;
		this.startDate = startDate;
		this.clientID = client;
		this.orderStatus = orderStatus;
		this.orderedService = orderedService;
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String date) {
		this.startDate = date;
	}
	public long getClientID() {
		return clientID;
	}
	public void setClient(long client) {
		this.clientID = client;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public List<Pair> getOrderedService() {
		return orderedService;
	}
	public void setOrderedService(List<Pair> orderedService) {
		this.orderedService = orderedService;
	}
	
	public String getOrderStatusS() {
		switch (orderStatus) {
		case 0:
			return "Order placed";
		case 1:
			return "Order accepted";
		case 2:
			return "Order in progress";
		case 3:
			return "Order finished";
		case 4:
			return "Order awaiting payment";
		case 5:
			return "Order paid and finalized";
		default:
			return "Error: Invalid order status";
		}
	}
	
	public String toNiceString() {
		String out = new String(); 
		out += (id + ";" + startDate + ";" + clientID + ";" + getOrderStatusS() + "\nOrdered Service: \n[Service ID|Quantity]\n");
		for(int i = 0; i<orderedService.size(); i++) {
			out += (orderedService.get(i).toString()+"\n");
		}
		return out;
	}

	public String toString() {
		String out = new String(); 
		out += (id + ";" + startDate + ";" + clientID + ";" + orderStatus + ";");
		for(int i = 0; i<orderedService.size(); i++) {
			out += (orderedService.get(i).toString());
			if(i<orderedService.size()-1)out+=":";
		}
		return out;
	}
}

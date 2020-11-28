package DAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDao implements Dao<Order> {
	
	HashMap<Long, Order> ordersBuffer = new HashMap<Long, Order>();
	long nextID = 1;
	int currentOrders = 0;
	String ordersFileName = "Orders.csv";
	
	public OrderDao() {
		updateBuffer();
		updateFile(ordersFileName);
	}
	
	public void updateBuffer() {
		ordersBuffer = fileToOrders(ordersFileName);
	}

	private HashMap<Long, Order> fileToOrders(String fileName) {
		List<List<String>> tmp = this.getFile(fileName);
		HashMap<Long, Order> out = new HashMap<Long, Order>();
		if(tmp==null) {
			return out;
		}
		nextID = Integer.parseInt(tmp.get(1).get(1));
		currentOrders = Integer.parseInt(tmp.get(1).get(0));
		
		for(int i = 3; i < currentOrders+3; i++) {
			List<String> line = tmp.get(i);
			List<Pair> s = new ArrayList<Pair>();
			String h[] = line.get(4).split(":");
			for(int j = 0; j<h.length; j++) {
				s.add(new Pair(h[j]));
			}
			
			out.put(Long.parseLong(line.get(0)),new Order(Long.parseLong(line.get(0)), line.get(1), Long.parseLong(line.get(2)), Integer.parseInt(line.get(3)), s));
		}
		
		return out;
	}
	
	private void updateFile(String fileName) {
		List<List<String>> data = new ArrayList<List<String>>();
		data.add(Arrays.asList("NumberOfOrdersCurrently;NextID".split(";")));
		List<String> meta = new ArrayList<String>();
		meta.add(String.valueOf(currentOrders));
		meta.add(String.valueOf(nextID));
		data.add(meta);
		data.add(Arrays.asList("OrderID;StartDate;ClientID;OrderStatus;Services[OfferID|Quantity]".split(";")));
		ordersBuffer.values().forEach(order -> {
			data.add(Arrays.asList(order.toString().split(";")));
		});
		try {
			this.overwriteFile(fileName, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void add(Order t) {
		ordersBuffer.put(t.getId(), t);
		currentOrders++;
		updateFile(ordersFileName);
	}

	@Override
	public Order get(long id) {
		updateBuffer();
		return ordersBuffer.get(id);
	}

	@Override
	public List<Order> getAll() {
		updateBuffer();
		return new ArrayList<Order>(ordersBuffer.values());
	}

	@Override
	public void update(Order t) {
		ordersBuffer.replace(t.getId(), t);
		updateFile(ordersFileName);
	}

	@Override
	public void delete(Order t) {
		delete(t.getId());
	}

	@Override
	public void delete(long id) {
		ordersBuffer.remove(id);
		currentOrders--;
		updateFile(ordersFileName);
	}
	
	public long getNextID() {
		nextID++;
		return nextID-1;
	}
	
	public List<Order> filter(long clientId){
		return ordersBuffer.values().stream().filter(o -> o.getClientID() == clientId).collect(Collectors.toList());
	}

	public boolean isOrderMadebyClient(long OrderId, long clientId) {
		return ordersBuffer.get(OrderId).getClientID() == clientId;
	}
}

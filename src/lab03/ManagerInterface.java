package lab03;

import java.util.Scanner;

import DAO.Offer;
import DAO.OfferDao;
import DAO.Order;
import DAO.OrderDao;
import DAO.ToDo;
import DAO.ToDoDao;
import DAO.WorkerDao;

public class ManagerInterface {
	static WorkerDao wd = new WorkerDao();
	static OfferDao od = new OfferDao();
	static OrderDao ord = new OrderDao();
	static ToDoDao td = new ToDoDao();

	public static void main(String[] args) {
		
		System.out.println(" --- MENU --- ");
		System.out.println("H - Help");
		Scanner sc  = new Scanner(System.in);
		String input;
		long id;
		while(true) {
			input = "";
			
			if(sc.hasNext()) {
				input = sc.next();
				
				switch (input) {
				case "Q":
					sc.close();
					return;
				case "H":
					help();
					break;
				//Show
				case "Show":
					input = sc.next();
					
					switch (input) {
					
					case "offers":
						System.out.println("ID;Name;Unit;Price/unit");
						od.getAll().forEach(offer -> {
							System.out.println(offer);
						});
						break;
						
					case "offer":
						try{
							id = Long.parseLong(sc.next());
							System.out.println(od.get(id)==null ? "There is no such offer": od.get(id));
						}catch (NumberFormatException e) {
							System.out.println("ID has to be a number");
						}
						
						break;
					
					case "orders":
						System.out.println("ID;Start Date;Client ID;Order Status");
						ord.getAll().forEach(order -> {
							System.out.println(order.toNiceString());
						});
						break;
					case "order":
						try {
							id = Long.parseLong(sc.next());
							System.out.println(ord.get(id)==null? "There is no such order": ord.get(id).toNiceString());
						} catch (NumberFormatException e) {
							System.out.println("ID has to be a number");
						}
						break;
					case "todos":
						System.out.println("ID;Order ID;Worker ID");
						td.getAll().forEach(todo -> {
							System.out.println(todo.toString());
						});
						break;
					case "todo":
						try{
							id = Long.parseLong(sc.next());
							System.out.println(td.get(id));
						}catch (NumberFormatException e) {
							System.out.println("ID has to be a number");
						}
						
						break;
					case "workers":
						System.out.println("ID;Name");
						wd.getAll().forEach(worker -> {
							System.out.println(worker);
						});
						break;
					default:
						System.out.println("Invalid command, please try again");
						break;
					}
					break;
				
				case "Add":
					input = sc.next();
					if(input.equals("offer")) {
						System.out.println("Name: ");
						String name = sc.next();
						System.out.println("Unit: ");
						String unit = sc.next();
						System.out.println("Price per unit: ");
						
						try{
							int price = Integer.parseInt(sc.next());
							od.add(new Offer(od.getNextID(), name, unit, price));
							System.out.println("Adding new offer");
						}catch (NumberFormatException e) {
							System.out.println("Price has to be a number");
						}
						
						
					}
					if(input.equals("todo")) {
						try {
							id = Long.parseLong(sc.next());
							if(ord.get(id)!=null) {
								long id2 = Long.parseLong(sc.next());
								if(wd.get(id2)!=null){
									ToDo t = new ToDo(td.getNextID(), id, id2);
									td.add(t);
									System.out.println("Adding new todo giving order number "+id+" to worker number "+id2);
									
								}else {
									System.out.println("There is no such worker");
								}
								
							}else {
								System.out.println("There is no such order");
							}
						} catch (NumberFormatException e) {
							System.out.println("ID and price have to be numbers");
						}
					}
					break;
					
				case "Edit":
					input = sc.next();
					if (input.equals("offer")) {
						try{
							id = Long.parseLong(sc.next());
							Offer current = od.get(id);
							System.out.println("Editing offer number " + id);
							System.out.println("Current name: " + current.getName());
							System.out.println("New name: ");
							String name = sc.next();
							current.setName(name);
							System.out.println("Current unit: " + current.getUnit());
							System.out.println("New unit: ");
							String unit = sc.next();
							current.setUnit(unit);
							System.out.println("Current price per unit: " + current.getCost());
							System.out.println("New price: ");
							int price = Integer.parseInt(sc.next());
							current.setCost(price);
							od.update(current);
						}catch (NumberFormatException e) {
							System.out.println("ID and price have to be numbers");
						}
						
					}
					break;
					
				case "Delete":
					input = sc.next();
					if (input.equals("offer")) {
						try{
							id = Long.parseLong(sc.next());
							System.out.println("Are you sure you want to delete offer number " + id + "? Y/N");
							input = sc.next();
							if(input.equals("Y")) {
								System.out.println("Deleting offer number " + id);
								od.delete(id);
							}
						}catch (NumberFormatException e) {
							System.out.println("ID has to be a number");
						}
						
					}
					break;
					
				case "Accept":
					input = sc.next();
					if (input.equals("order")) {
						try{
							id = Long.parseLong(sc.next());
							Order s = ord.get(id);
							if(s == null) {
								System.out.println("There is no such order");
							}else if(s.getOrderStatus()==0) {
								s.setOrderStatus(1);
								ord.update(s);
								System.out.println("Order number " + id + " is now accepted");
							}else {System.out.println("Order has already been accepted");}
						}catch (NumberFormatException e) {
							System.out.println("ID has to be a number");
						}
						
					}
					break;
				case "Bill":
					try{
						id = Long.parseLong(sc.next());
						Order s = ord.get(id);
						if(s == null) {
							System.out.println("There is no such order");
						}else if(s.getOrderStatus()==3) {
							System.out.println("Are you sure you want to create a bill and finalize order number " + id + "? Y/N");
							input = sc.next();
							if(input.equals("Y")) {
								s.setOrderStatus(4);
								ord.update(s);
								System.out.println("Billing order number " + id);
							}
						}else if(s.getOrderStatus()<3) {
							System.out.println("Order not finished yet");
						}else {
							System.out.println("Order already billed");
						}
					}catch (NumberFormatException e) {
						System.out.println("ID has to be a number");
					}
					break;
				default:
					System.out.println("Invalid command, please try again");
					break;
				}
			}
		}
		
		
	}

	private static void help() {
		System.out.println(" --- HELP --- ");
		System.out.println("H - Help");
		System.out.println("Q - Quit");
		
		System.out.println(" --- Show --- ");
		System.out.println("Show offers - shows all offers available");
		System.out.println("Show offer [id] - shows offer number [id]");
		System.out.println("Show orders - shows all orders");
		System.out.println("Show order [id] - shows order number [id]");
		System.out.println("Show todos - shows all todos");
		System.out.println("Show todo [id] - shows todo number [id]");
		System.out.println("Show workers - shows all current employees");
		
		System.out.println(" --- Add --- ");
		System.out.println("Add offer - lets you create a new offer");
		System.out.println("Add todo [OrderID] [WorkerID] - lets you create a Todo, thus connecting a worker to an order he has to do");
		
		System.out.println(" --- Edit --- ");
		System.out.println("Edit offer [id] - lets you edit parts of offer number [id]");
		
		System.out.println(" --- Delete --- ");
		System.out.println("Delete offer [id] - deletes offer number [id]");
		
		System.out.println(" --- Accept --- ");
		System.out.println("Accept order [id] - accepts order number [id]");
		
		System.out.println(" --- Bill --- ");
		System.out.println("Bill [id] - bills order number [id]");
		
	}

}

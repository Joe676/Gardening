package lab03;

import java.util.Scanner;

import DAO.ClientDao;
import DAO.OfferDao;
import DAO.Order;
import DAO.OrderDao;
import DAO.ToDoDao;
import DAO.Worker;
import DAO.WorkerDao;

public class WorkerInterface {
	static WorkerDao wd = new WorkerDao();
	static OfferDao od = new OfferDao();
	static OrderDao ord = new OrderDao();
	static ToDoDao td = new ToDoDao();
	static ClientDao cd = new ClientDao();
	
	
	public static void main(String[] args) {
		
		Scanner sc  = new Scanner(System.in);
		String input;
		Worker user;
		long id;
		
		System.out.println("Do you have an account? [Y/N]");
		input = sc.next();
		if(input.equals("N")) {
			System.out.println("What is your name?[type with no spaces example: JanKowalski]");
			input = sc.next();
			System.out.println("Creating new account...");
			user = new Worker(wd.getNextID(), input);
			wd.add(user);
			System.out.println("Remember yor ID!");
		}else {
			do {
			System.out.println("What's your ID?");
			id = Long.parseLong(sc.next());
			user = wd.get(id);
			if(user==null)System.out.println("Try again, there is no such user");
			}while(user==null);
		}
		System.out.println("You are logged in as " + user);
		
		
		System.out.println(" --- MENU --- ");
		System.out.println("H - Help");
		
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
						td.filter(user.getId()).forEach(todo -> {
							System.out.println(todo.toString());
						});
						break;
					case "todo":
						try{
							id = Long.parseLong(sc.next());
							if(td.get(id).getWorkerId()==user.getId()) {
								System.out.println("ID;Order ID;Worker ID");
								System.out.println(td.get(id)==null? "There is no such ToDo": td.get(id));
							}
							else {
								System.out.println("This todo is not assigned to you");
							}
						}catch (NumberFormatException e) {
							System.out.println("ID has to be a number");
						}
						
						break;
					case "client":
						try{
							id = Long.parseLong(sc.next());
							if(cd.get(id)!=null) {
								System.out.println("ID;Name");
								System.out.println(cd.get(id));	
							}else {
								System.out.println("There is no such client");
							}
						}catch (NumberFormatException e) {
							System.out.println("ID has to be a number");
						}
						break;
					default:
						System.out.println("Invalid command, please try again");
						break;
					}
					break;
				
					
				case "Edit":
					input = sc.next();
					if (input.equals("name")) {
						System.out.println("Are you sure you want to change your name? [Y/N]");
						input = sc.next();
						if(input.equals("Y")) {
							System.out.println("Your previous name: "+user.getName());
							System.out.println("New name: ");
							input = sc.next();
							user.setName(input);
							wd.update(user);
						}
						
					}
					break;
					
				case "Delete":
					input = sc.next();
					if (input.equals("account")) {
						System.out.println("Are you sure you want to delete your account? [Y/N]");
						input = sc.next();
						if(input.equals("Y")) {
							System.out.println("Deleting your account and closing program...");
							wd.delete(user);
							sc.close();
							return;
						}
					}
					break;
					
				case "Start":
					try{
						id = Long.parseLong(sc.next());
						Order s = ord.get(id);
						if(s == null || !td.isOrderAssignedToWorker(id, user.getId())) {
							System.out.println("There is no such order or it is not assigned to you");
						}else if(s.getOrderStatus()==1) {
							s.setOrderStatus(2);
							ord.update(s);
						}else {System.out.println("Order has already been started or has not been accepted");}
					}catch (NumberFormatException e) {
						System.out.println("ID has to be a number");
					}
					break;
				case "Finish":
					try{
						id = Long.parseLong(sc.next());
						Order s = ord.get(id);
						if(s == null || !td.isOrderAssignedToWorker(id, user.getId())) {
							System.out.println("There is no such order or it is not assigned to you");
						}else if(s.getOrderStatus()==2) {
							s.setOrderStatus(3);
							ord.update(s);
							System.out.println("Finishing order number " + id);
						}else {
							System.out.println("Order has not been started or has already been finished");
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
		System.out.println("Show todos - shows all your todos");
		System.out.println("Show todo [id] - shows todo number [id] if it's assigned to you");
		System.out.println("Show client [id] - shows client number [id]");
		
		System.out.println(" --- Edit --- ");
		System.out.println("Edit name - lets you edit your name");
		
		System.out.println(" --- Delete --- ");
		System.out.println("Delete account - deletes your account");
		
		System.out.println(" --- Start --- ");
		System.out.println("Start [id] - Changes status of order number [id] to 'in progres' (if it's assigned to you)");
		
		System.out.println(" --- Finish --- ");
		System.out.println("Finish [id] - Changes status of order number [id] to 'finished' (if it's assigned to you)");
		
	}

}

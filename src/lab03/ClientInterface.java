package lab03;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import DAO.Client;
import DAO.ClientDao;
import DAO.OfferDao;
import DAO.Order;
import DAO.OrderDao;
import DAO.Pair;
import DAO.ToDoDao;
import DAO.WorkerDao;

public class ClientInterface {
	static WorkerDao wd = new WorkerDao();
	static OfferDao od = new OfferDao();
	static OrderDao ord = new OrderDao();
	static ToDoDao td = new ToDoDao();
	static ClientDao cd = new ClientDao();
	

	public static void main(String[] args) {
		
		Scanner sc  = new Scanner(System.in);
		String input;
		Client user;
		long id;
		
		List<Pair> cart = new ArrayList<Pair>();
		
		System.out.println("Do you have an account? [Y/N]");
		input = sc.next();
		if(input.equals("N")) {
			System.out.println("What is your name?[type with no spaces example: JanKowalski]");
			input = sc.next();
			System.out.println("Creating new account...");
			user = new Client(cd.getNextID(), input);
			cd.add(user);
			System.out.println("Remember yor ID!");
		}else {
			do {
			System.out.println("What's your ID?");
			id = Long.parseLong(sc.next());
			user = cd.get(id);
			if(user==null)System.out.println("Try again, there is no such user");
			}while(user==null);
		}
		System.out.println("You are logged in as " + user);
		
		System.out.println(" --- MENU --- ");
		System.out.println("H - Help");
		
		while(true) {
			if(sc.hasNext()) {
				input = sc.next();
				switch (input) {
				case "Q":
					if(!cart.isEmpty()) {
						System.out.println("Contents of cart are NOT saved between sessions. Are you sure you want to close the program? [Y/N]");
						input = sc.next();
						if(input.equals("Y")) {
							sc.close();
							return;
						}
					}else {
						sc.close();
						return;
					}
					break;
					
				case "H":
					help();
					break;
					
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
						ord.filter(user.getId()).forEach(order -> {
							System.out.println(order.toNiceString());
						});
						break;
					case "order":
						try {
							id = Long.parseLong(sc.next());
							Order o = ord.get(id);
							if(o==null || !ord.isOrderMadebyClient(id, user.getId())) {
								System.out.println("There is no such order");
							}else {
								System.out.println(o.toNiceString());	
							}
						} catch (NumberFormatException e) {
							System.out.println("ID has to be a number");
						}
						break;
					case "cart":
						System.out.println("[ItemNumber: ServiceID|Quantity]");
						for(int i = 0; i < cart.size(); i++) {
							System.out.println(i+": "+cart.get(i).toString());
						}
							
					}
					break;
				case "Add":
					try{
						id = Long.parseLong(sc.next());
						int quantity = Integer.parseInt(sc.next());
						cart.add(new Pair(id, quantity));
						System.out.println("Added " + quantity + " units of service number " + id + " to your cart");
					}catch(NumberFormatException e) {
						System.out.println("ID and quantity have to be numbers");
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
							cd.update(user);
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
							cd.delete(user);
							sc.close();
							return;
						}
					}else if(input.equals("cart")) {
						System.out.println("Are you sure you want to empty your cart? [Y/N]");
						input = sc.next();
						if(input.equals("Y")) {
							System.out.println("Emptying your cart...");
							cart.clear();
						}
					}else {
						try {
							id = Integer.parseInt(input);
							System.out.println("Are you sure you want to delete item number "+id+" from your cart? [Y/N]");
							input = sc.next();
							if(input.equals("Y")) {
								cart.remove((int)id);
							}
						} catch (NumberFormatException e) {
							System.out.println("Index has to be a number");
						}
					}
					break;
					
				case "Buy":
					System.out.println("Do you want to order services from your cart? Y/N");
					input = sc.next();
					if(input.equals("Y")) {
						ord.add(new Order(ord.getNextID(), (new SimpleDateFormat("dd.MM.yyyy")).format(new Date()), user.getId(), 0, cart));
						System.out.println("You placed a new order");
						cart.clear();
					}
					break;
					
				case "Pay":
					try{
						id = Long.parseLong(sc.next());
						Order o = ord.get(id);
						if(o==null || !ord.isOrderMadebyClient(id, user.getId())) {
							System.out.println("There is no such order");
						}else if(o.getOrderStatus() == 4) {
							o.setOrderStatus(5);
							ord.update(o);
							System.out.println("Payment for order number " + id + " complete!");
						}else {
							System.out.println("Order has not been billed yet");
						}
					}catch (NumberFormatException e) {
						System.out.println("ID has to be a number");
					}
					
					break;
				default:
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
		System.out.println("Show orders - shows all of your orders");
		System.out.println("Show order [id] - shows your order number [id]");
		System.out.println("Show cart - shows your cart");
		
		System.out.println(" --- Add --- ");
		System.out.println("Add [OfferID] [Quantity] - Adds an offer to your cart");
		
		System.out.println(" --- Edit --- ");
		System.out.println("Edit name - lets you edit your name");
		
		System.out.println(" --- Delete --- ");
		System.out.println("Delete account - deletes your account");
		System.out.println("Delete [id] - deletes item number [id] from your cart");
		System.out.println("Delete cart - empties your cart");
		
		System.out.println(" --- Buy --- ");
		System.out.println("Buy - makes an order with the contents of your cart");
		
		System.out.println(" --- Pay --- ");
		System.out.println("Pay [id] - Pay for your order number [id]");
		
	}

}

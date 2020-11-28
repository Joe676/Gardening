package DAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ClientDao implements Dao<Client> {

	HashMap<Long, Client> clientsBuffer = new HashMap<Long, Client>();
	long nextID = 1;
	int currentClients = 0;
	String clientsFileName = "Clients.csv";
	
	public ClientDao() {
		updateBuffer();
	}
	
	@Override
	public void updateBuffer() {
		clientsBuffer = fileToClients(clientsFileName);
		updateFile(clientsFileName);
	}
	
	private HashMap<Long, Client> fileToClients(String fileName) {
		List<List<String>> tmp = this.getFile(fileName);
		HashMap<Long, Client> out = new HashMap<Long, Client>();
		if(tmp == null) {
			return out;
		}
		nextID = Long.parseLong(tmp.get(1).get(1));
		currentClients = Integer.parseInt(tmp.get(1).get(0));
		for(int i = 3; i < currentClients+3; i++) {
			List<String> line = tmp.get(i);
			out.put(Long.parseLong(line.get(0)), new Client(Long.parseLong(line.get(0)), line.get(1)));
		}
		
		return out;
	}
	
	private void updateFile(String fileName) {
		List<List<String>> data = new ArrayList<List<String>>();
		data.add(Arrays.asList("NumberofClientsCurrently;NextID".split(";")));
		List<String> meta = new ArrayList<String>();
		meta.add(String.valueOf(currentClients));
		meta.add(String.valueOf(nextID));
		data.add(meta);
		data.add(Arrays.asList("ClientID;ClientName"));
		clientsBuffer.values().forEach(client -> {
			data.add(Arrays.asList(client.toString().split(";")));
		});
		try {
			this.overwriteFile(fileName, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void add(Client t) {
		clientsBuffer.put(t.getId(), t);
		currentClients++;
		updateFile(clientsFileName);
	}

	@Override
	public Client get(long id) {
		updateBuffer();
		return clientsBuffer.get(id);
	}

	@Override
	public List<Client> getAll() {
		updateBuffer();
		return new ArrayList<Client>(clientsBuffer.values());
	}

	@Override
	public void update(Client t) {
		clientsBuffer.replace(t.getId(), t);
		updateFile(clientsFileName);
	}

	@Override
	public void delete(Client t) {
		delete(t.getId());
	}

	@Override
	public void delete(long id) {
		clientsBuffer.remove(id);
		currentClients--;
		updateFile(clientsFileName);
	}

	public long getNextID() {
		return nextID++;
	}

}

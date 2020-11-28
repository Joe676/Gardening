package DAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WorkerDao implements Dao<Worker> {
	
	HashMap<Long, Worker> workersBuffer = new HashMap<Long, Worker>();
	long nextID = 1;
	int currentWorkers = 0;
	String workersFileName = "Workers.csv";
	
	public WorkerDao() {
		updateBuffer();
	}

	@Override
	public void updateBuffer() {
		workersBuffer = fileToWorkers(workersFileName);
		updateFile(workersFileName);
	}
	
	private HashMap<Long, Worker> fileToWorkers(String fileName){
		List<List<String>> tmp = this.getFile(fileName);
		HashMap<Long, Worker> out = new HashMap<Long, Worker>();
		if(tmp == null) {
			return out;
		}
		nextID = Long.parseLong(tmp.get(1).get(1));
		currentWorkers = Integer.parseInt(tmp.get(1).get(0));
		for(int i = 3; i < currentWorkers+3; i++) {
			List<String> line = tmp.get(i);
			out.put(Long.parseLong(line.get(0)), new Worker(Long.parseLong(line.get(0)), line.get(1)));
		}
		
		return out;
	}
	
	private void updateFile(String fileName) {
		List<List<String>> data = new ArrayList<List<String>>();
		data.add(Arrays.asList("NumberOfWorkersCurrently;NextID".split(";")));
		List<String> meta = new ArrayList<String>();
		meta.add(String.valueOf(currentWorkers));
		meta.add(String.valueOf(nextID));
		data.add(meta);
		data.add(Arrays.asList("WorkerID;WorkerName"));
		workersBuffer.values().forEach(worker -> {
			data.add(Arrays.asList(worker.toString().split(";")));
		});
		try {
			this.overwriteFile(fileName, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void add(Worker worker) {
		workersBuffer.put(worker.getId(), worker);
		currentWorkers++;
		updateFile(workersFileName);
	}

	@Override
	public void delete(long id) {
		workersBuffer.remove(id);
		currentWorkers--;
		updateFile(workersFileName);
	}
	
	@Override
	public void delete(Worker worker) {
		delete(worker.getId());
	}

	@Override
	public Worker get(long id) {
		updateBuffer();
		return workersBuffer.get(id);
	}

	@Override
	public List<Worker> getAll() {
		updateBuffer();
		return new ArrayList<Worker>(workersBuffer.values());
	}

	@Override
	public void update(Worker t) {
		workersBuffer.replace(t.getId(), t);
		updateFile(workersFileName);
	}
	
	public long getNextID() {
		return nextID++;
	}
}

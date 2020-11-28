package DAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ToDoDao implements Dao<ToDo> {

	HashMap<Long, ToDo> todoBuffer = new HashMap<Long, ToDo>();
	long nextID = 1;
	int currentTodos = 0;
	String todosFileName = "ToDos.csv";
	
	
	
	public ToDoDao() {
		updateBuffer();
	}

	@Override
	public void updateBuffer() {
		todoBuffer = fileToTodos(todosFileName);
		updateFile(todosFileName);
		
	}


	private void updateFile(String fileName) {
		List<List<String>> data = new ArrayList<List<String>>();
		data.add(Arrays.asList("CurrentTodos;NextID".split(";")));
		List<String> meta = new ArrayList<String>();
		meta.add(String.valueOf(currentTodos));
		meta.add(String.valueOf(nextID));
		data.add(meta);
//		data.add(List.of(Long.toString(nextID)));
		data.add(Arrays.asList("ToDoID;OrderID;WorkerID".split(";")));
		todoBuffer.values().forEach(todo -> {
			data.add(Arrays.asList(todo.toString().split(";")));
		});
		try {
			this.overwriteFile(fileName, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private HashMap<Long, ToDo> fileToTodos(String fileName) {
		List<List<String>> tmp = this.getFile(todosFileName);
		HashMap<Long, ToDo> out = new HashMap<Long, ToDo>();
		if(tmp==null) {
			return out;
		}
		nextID = Long.parseLong(tmp.get(1).get(1));
		currentTodos = Integer.parseInt(tmp.get(1).get(0));
		
		for(int i = 3; i < currentTodos+3; i++) {
			List<String> line = tmp.get(i);
			out.put(Long.parseLong(line.get(0)), new ToDo(Long.parseLong(line.get(0)), Long.parseLong(line.get(1)), Long.parseLong(line.get(2))));
		}
		return out;
	}

	@Override
	public void add(ToDo t) {
		todoBuffer.put(t.getId(), t);
		currentTodos++;
		updateFile(todosFileName);
	}

	@Override
	public ToDo get(long id) {
		updateBuffer();
		return todoBuffer.get(id);
	}

	@Override
	public List<ToDo> getAll() {
		updateBuffer();
		return new ArrayList<ToDo>(todoBuffer.values());
	}

	@Override
	public void update(ToDo t) {
		todoBuffer.replace(t.getId(), t);
		updateFile(todosFileName);
	}

	@Override
	public void delete(ToDo t) {
		delete(t.getId());
		
	}

	@Override
	public void delete(long id) {
		todoBuffer.remove(id);
		currentTodos--;
		updateFile(todosFileName);
	}

	public long getNextID() {
		nextID++;
		return nextID-1;
	}
	
	public List<ToDo> filter(long workerId) {
		return todoBuffer.values().stream().filter(t -> t.getWorkerId() == workerId).collect(Collectors.toList());
		
	}
	
	public boolean isOrderAssignedToWorker(long orderId, long workerId) {
		return !todoBuffer.values().stream().filter(t -> t.getOrderId() == orderId).filter(t -> t.getWorkerId() == workerId).collect(Collectors.toList()).isEmpty();
	}
}

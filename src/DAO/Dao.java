package DAO;

import java.util.List;

public interface Dao<T> extends DataReader, DataWriter {
	
	void updateBuffer();
	
	void add(T t);
	
	T get(long id);
	
	List<T> getAll();
	
	void update(T t);
	
	void delete(T t);
	
	void delete(long id);
}

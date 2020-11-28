package DAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface DataReader {
	default List<List<String>> getFile(String fileName){
		List<List<String>> out = new ArrayList<List<String>>();
		
		try{
			File f = new File(fileName);
			if(f.createNewFile())return null;
			BufferedReader br = new BufferedReader(new FileReader(fileName));
		    while(br.ready()) {
		    	out.add(Arrays.asList(br.readLine().split(";")) );
		    }
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}
}

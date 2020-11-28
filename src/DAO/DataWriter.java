package DAO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public interface DataWriter {

	
	default void overwriteFile(String fileName, List<List<String>> data) throws IOException {

		try (BufferedWriter br = new BufferedWriter(new FileWriter(fileName, false));){
			for(int j = 0; j < data.size(); j++) {
				List<String> line = data.get(j);
				for(int i = 0; i < line.size(); i++) {
					br.write(line.get(i));
					if(i<line.size()-1) {
						br.write(";");
					}else {
						br.newLine();
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
}

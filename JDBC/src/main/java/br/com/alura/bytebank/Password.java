package br.com.alura.bytebank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Password {
	public static String getPassword() {
		String filePath = "C:\\Users\\Cesar\\Desktop\\Curso_Dev\\Alura\\passwords.txt";
		String searchWord = "password mysql";

		try (BufferedReader buffer = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = buffer.readLine()) != null) {
				if (line.contains(searchWord)) {
					String password = line.substring(line.indexOf("= ") + 2, line.length());
					return password;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}

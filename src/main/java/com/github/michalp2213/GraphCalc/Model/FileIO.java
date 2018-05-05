package com.github.michalp2213.GraphCalc.Model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FileIO {
	public static Graph readFromFile(String fileName) throws IOException {
		if (fileName == null)
			return null;
		
		Graph ret = null;
		
		ObjectInputStream input = null;
		try {
			input = new ObjectInputStream(new FileInputStream(fileName));
			
			ret = (Graph) input.readObject();
			
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
		
		return ret;
	}
	
	public static void writeToFile(String fileName, Graph data) throws IOException {
		if (fileName == null || data == null)
			return;
		
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(new FileOutputStream(fileName));
			
			output.writeObject(data);
		} finally {
			if (output != null)
				output.close();
		}
	}
}

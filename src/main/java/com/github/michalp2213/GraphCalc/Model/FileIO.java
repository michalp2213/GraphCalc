package com.github.michalp2213.GraphCalc.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileIO {
	public static <T> Graph <T> readFromFile(File file) throws IOException {
		if (file == null)
			return null;
		
		Graph <T> ret = null;
		
		ObjectInputStream input = null;
		try {
			input = new ObjectInputStream(new FileInputStream(file));
			
			Object tmp = input.readObject();
			
			//TODO: figure out a check if read Graph really stores T
			if (tmp instanceof Graph) {
				ret = (Graph <T>) tmp;
			}
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
	
	public static <T> void writeToFile(File file, Graph <T> data) throws IOException {
		if (file == null || data == null)
			return;
		
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(new FileOutputStream(file));
			
			output.writeObject(data);
		} finally {
			if (output != null)
				output.close();
		}
	}
}

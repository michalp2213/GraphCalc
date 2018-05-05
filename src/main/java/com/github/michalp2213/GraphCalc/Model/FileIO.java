package com.github.michalp2213.GraphCalc.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.scene.shape.Circle;

public class FileIO {
	public static Graph <Circle> readFromFile(File file) throws IOException {
		if (file == null)
			return null;
		
		Graph <Circle> ret = null;
		
		ObjectInputStream input = null;
		try {
			input = new ObjectInputStream(new FileInputStream(file));
			
			ret = (Graph <Circle>) input.readObject();
			
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
	
	public static void writeToFile(File file, Graph <Circle> data) throws IOException {
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

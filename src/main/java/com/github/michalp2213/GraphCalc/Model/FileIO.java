package com.github.michalp2213.GraphCalc.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.scene.layout.Pane;

public class FileIO {
	
	public static Graph<SerializableCircle> readFromFile(File file, Pane workspace) throws IOException {
		if (file == null)
			return null;
		
		Graph<SerializableCircle> ret = null;
		
		ObjectInputStream input = null;
		try {
			input = new ObjectInputStream(new FileInputStream(file));
			
			Object tmp = input.readObject();
			
			if (tmp instanceof Graph) {
				ret = (Graph <SerializableCircle>) tmp;
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
	
	public static void writeToFile(File file, SavableCircleGraph data) throws IOException {
		if (file == null || data == null)
			return;
		
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(new FileOutputStream(file));
			
			output.writeObject(data.getSerializable());
		} finally {
			if (output != null)
				output.close();
		}
	}
}

package com.github.michalp2213.GraphCalc.Model;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FileIO {

    public static void saveToFile(Graph g, HashMap<Vertex, Circle> circles, File f) throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));

        try {
            out.writeObject(g);
            out.writeObject(circles.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> new SerializableCircle(e.getValue())
                )));
        } finally {
            out.close();
        }
    }

    public static readData readFromFile(File f) throws IOException {
        readData ret = new readData();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));

        try {
            Object r = in.readObject();

            if (r instanceof Graph) {
                ret.g = (Graph) r;
            } else {
                throw new IOException("Invalid file format");
            }

            r = in.readObject();

            if (r instanceof HashMap) {
                ret.circles = (HashMap) ((HashMap) r).entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> ((Map.Entry<Vertex, SerializableCircle>)e).getKey(),
                                e -> ((Map.Entry<Vertex, SerializableCircle>) e).getValue().getCircle()
                        ));
            } else {
                throw new IOException("Invalid file format");
            }

        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        } finally {
            in.close();
        }

        return ret;
    }

    public static class readData {
        public Graph g;
        public HashMap <Vertex, Circle> circles;
    }
}

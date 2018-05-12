package com.github.michalp2213.GraphCalc.Model.Algorithms;

import com.github.michalp2213.GraphCalc.Model.AlgorithmEvents.*;
import com.github.michalp2213.GraphCalc.Model.Vertex;

import java.util.ArrayList;

abstract class GraphAlgorithm {
    static ArrayList<AlgorithmEvent> events = new ArrayList<>();

    static ArrayList<AlgorithmEvent> getEvents(){
        return events;
    }

    static void addEvent(AlgorithmEvent event){
        events.add(event);
    }

    static void clearEvents(){
        events.clear();
    }
}

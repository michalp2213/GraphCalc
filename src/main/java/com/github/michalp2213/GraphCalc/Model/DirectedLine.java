package com.github.michalp2213.GraphCalc.Model;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

public class DirectedLine extends Group {
    private Line line;
    private Polygon arrow;

    public DirectedLine(Line line, Polygon arrow){
        super(line, arrow);
        this.line = line;
        this.arrow = arrow;
    }

    public DirectedLine(double startX, double startY, double endX, double endY){
        this(new Line(startX, startY, endX, endY), getArrow(startX, startY, endX, endY));
    }

    private static Polygon getArrow(double startX, double startY, double endX, double endY){
        Polygon toReturn = new Polygon();
        toReturn.getPoints().addAll(
                endX, endY,
                endX-8, endY+4,
                endX-8, endY-4
        );
        double angle = Math.toDegrees(Math.asin((endY - startY)/
                Math.sqrt((endX-startX)*(endX-startX) + (startY-endY)*(startY-endY))));
        if (startX-endX >= 0){
            angle = 180-angle;
        }
        toReturn.getTransforms().clear();
        toReturn.getTransforms().add(new Rotate(angle, endX, endY));
        return toReturn;
    }

    private void fixArrow(){
        double angle = Math.toDegrees(Math.asin((line.getEndY() - line.getStartY())/
                Math.sqrt((line.getEndX()-line.getStartX())*(line.getEndX()-line.getStartX()) + (line.getStartY()-line.getEndY())*(line.getStartY()-line.getEndY()))));
        if (line.getStartX()-line.getEndX() >= 0){
            angle = 180-angle;
        }
        arrow.getTransforms().clear();
        arrow.getTransforms().add(new Rotate(angle, line.getEndX(), line.getEndY()));
    }

    public double getStartX(){
        return line.getStartX();
    }

    public double getStartY(){
        return line.getStartY();
    }

    public double getEndX(){
        return line.getEndX();
    }

    public double getEndY(){
        return line.getEndY();
    }

    public void setStartX(double value){
        line.setStartX(value);
        fixArrow();
    }

    public void setStartY(double value){
        line.setStartY(value);
        fixArrow();
    }

    public void setEndX(double value){
        line.setEndX(value);
        fixArrow();
    }

    public void setEndY(double value){
        line.setEndY(value);
        fixArrow();
    }
}

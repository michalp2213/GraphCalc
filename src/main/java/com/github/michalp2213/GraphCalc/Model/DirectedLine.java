package com.github.michalp2213.GraphCalc.Model;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

public class DirectedLine extends Group {
    private Line line;
    private Polygon endArrow;
    private Polygon middleArrow;

    private DirectedLine(Line line, Polygon endArrow, Polygon middleArrow){
        super(line, endArrow, middleArrow);
        this.line = line;
        this.endArrow = endArrow;
        this.middleArrow = middleArrow;
    }

    public DirectedLine(double startX, double startY, double endX, double endY){
        this(new Line(startX, startY, endX, endY), getArrow(startX, startY, endX, endY), getMiddleArrow(startX, startY, endX, endY));
    }

    private static Polygon getArrow(double startX, double startY, double endX, double endY){
        Polygon toReturn = new Polygon();
        toReturn.getPoints().addAll(
                endX, endY,
                endX-12, endY+4,
                endX-12, endY-4
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

    private static Polygon getMiddleArrow(double startX, double startY, double endX, double endY){
        Polygon toReturn = new Polygon();
        double middleX = (startX + endX)/2;
        double middleY = (startY + endY)/2;
        toReturn.getPoints().addAll(
                middleX, middleY,
                middleX-9, middleY+3,
                middleX-9, middleY-3
        );
        double angle = Math.toDegrees(Math.asin((endY - startY)/
                Math.sqrt((endX-startX)*(endX-startX) + (startY-endY)*(startY-endY))));
        if (startX-endX >= 0){
            angle = 180-angle;
        }
        toReturn.getTransforms().clear();
        toReturn.getTransforms().add(new Rotate(angle, middleX, middleY));
        return toReturn;
    }

    private void fixArrows(){
        double angle = Math.toDegrees(Math.asin((line.getEndY() - line.getStartY())/
                Math.sqrt((line.getEndX()-line.getStartX())*(line.getEndX()-line.getStartX()) + (line.getStartY()-line.getEndY())*(line.getStartY()-line.getEndY()))));
        if (line.getStartX()-line.getEndX() >= 0){
            angle = 180-angle;
        }
        endArrow.getTransforms().clear();
        endArrow.getTransforms().add(new Rotate(angle, line.getEndX(), line.getEndY()));
        endArrow.getPoints().clear();
        endArrow.getPoints().addAll(
                line.getEndX(), line.getEndY(),
                line.getEndX()-12, line.getEndY()+4,
                line.getEndX()-12, line.getEndY()-4
        );
        middleArrow.getTransforms().clear();
        middleArrow.getPoints().clear();
        double middleX = (line.getStartX() + line.getEndX())/2;
        double middleY = (line.getStartY() + line.getEndY())/2;
        middleArrow.getPoints().addAll(
                middleX, middleY,
                middleX-9, middleY+3,
                middleX-9, middleY-3
        );
        middleArrow.getTransforms().add(new Rotate(angle, (line.getEndX() + line.getStartX())/2, (line.getStartY() + line.getEndY())/2));
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
        fixArrows();
    }

    public void setStartY(double value){
        line.setStartY(value);
        fixArrows();
    }

    public void setEndX(double value){
        line.setEndX(value);
        fixArrows();
    }

    public void setEndY(double value){
        line.setEndY(value);
        fixArrows();
    }
}

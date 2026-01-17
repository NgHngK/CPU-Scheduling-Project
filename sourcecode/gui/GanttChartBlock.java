package gui;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GanttChartBlock {
	private StackPane root = new StackPane();
	private Rectangle rectangle = new Rectangle();
	private Label label = new Label();
	
	public GanttChartBlock(double width, double height, String text, Color color) {
		
		this.label.setText(text);
	
		this.root.setPrefSize(width, height);
		this.rectangle = new Rectangle(width, height);
		this.rectangle.setFill(color);
		
		this.root.getChildren().addAll(rectangle, label);
	}
	public StackPane getStackPane() {
		return this.root;
	}
	public String getText() {
		return this.label.getText();
	}
}

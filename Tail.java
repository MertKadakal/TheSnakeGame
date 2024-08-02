package TheSnakeGame;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tail {
    Rectangle tail;
    int order;

    public Tail(StackPane pane, int order) {
        this.order = order;
        tail = new Rectangle(18,18, Color.DARKRED);
        VBox vBox = new VBox();
        vBox.getChildren().add(tail);
        vBox.setAlignment(Pos.TOP_LEFT);
        pane.getChildren().add(vBox);
    }

    public void move() {
        tail.setTranslateX(20+20*TheGame.tailIndexes.get(this.order-1)[0]);
        tail.setTranslateY(20+20*TheGame.tailIndexes.get(this.order-1)[1]);
    }
}

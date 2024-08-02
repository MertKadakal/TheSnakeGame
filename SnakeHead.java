package TheSnakeGame;

import java.io.File;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SnakeHead {
    ImageView head;
    boolean got = false;
    int x = 0;
    int y = 0;
    StackPane pane;

    public SnakeHead(StackPane pane) {
        this.pane = pane;
        String imagePath = "TheSnakeGame\\images\\head.png";
        File imageFile = new File(imagePath);
        Image image = new Image(imageFile.toURI().toString());
        head = new ImageView(image);
        head.setFitHeight(18);
        head.setFitWidth(18);
        VBox vBox = new VBox();
        vBox.getChildren().add(head);
        vBox.setAlignment(Pos.TOP_LEFT);
        Rectangle kare = new Rectangle(460,460, Color.GREEN);
        vBox.setMargin(head, new Insets(20,0,0,20));
        pane.getChildren().addAll(kare, vBox);
    }

    public void changeRotate(double rot) {
        head.setRotate(rot);
    }

    public void move() {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.seconds(0.01));
        translateTransition.setNode(head);

        if (head.getRotate()==0) {
            x++;
            translateTransition.setToX(x*20);
        }
        if (head.getRotate()==90) {
            y++;
            translateTransition.setToY(y*20);
        }
        if (head.getRotate()==180) {
            x--;
            translateTransition.setToX(x*20);
        }
        if (head.getRotate()==270) {
            y--;
            translateTransition.setToY(y*20);
        }

        translateTransition.play();

        if ((x + " " + y).equals(TheGame.circlex + " " + TheGame.circley)) {
            got = true;
        }

        if ((x == -1) || (y == -1) || (x == 23) || (y == 23)) {
            VBox vBox = new VBox();
            Text text1 = new Text("GAME OVER");
            text1.setFont(Font.font(40));
            vBox.getChildren().add(text1);
            vBox.setAlignment(Pos.CENTER);
            Rectangle rectangle = new Rectangle(500, 500, Color.DARKRED);
            pane.getChildren().addAll(rectangle, vBox);

            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    TheGame.stage.close();
                });
            }).start();
        }
    }
}

package TheSnakeGame;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class TheGame extends Application {
    static long circlex;
    static long circley;
    Circle circle;

    static int tailNumber = 1;
    static ArrayList<Tail> tails = new ArrayList<>();
    static ArrayList<int[]> tailIndexes = new ArrayList<>();
    
    public static void main(String[] args) {
        launch(args);
    }

    static StackPane pane = new StackPane();
    static Scene scene;
    static Stage stage;

    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;

        SnakeHead snake = new SnakeHead(pane);
        int[] array = {0,0};
        tailIndexes.add(array);
        scene = new Scene(pane, 500, 500, Color.DARKGREEN);
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(event -> {
            if ((event.getCode() == KeyCode.DOWN) && !(snake.head.getRotate() == 270)) {
                snake.changeRotate(90);
            }
            if ((event.getCode() == KeyCode.UP) && !(snake.head.getRotate() == 90)) {
                snake.changeRotate(270);
            }
            if ((event.getCode() == KeyCode.RIGHT) && !(snake.head.getRotate() == 180)) {
                snake.changeRotate(0);
            }
            if ((event.getCode() == KeyCode.LEFT) && !(snake.head.getRotate() == 0)) {
                snake.changeRotate(180);
            }
        });

        Text text = new Text();
        text.setFont(Font.font(50));
        VBox vbox = new VBox();
        vbox.getChildren().add(text);
        vbox.setAlignment(Pos.CENTER);
        pane.getChildren().add(vbox);

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 0_500_000_000) {

                    snake.move();

                    for (int i = 1; i<tailIndexes.size(); i++) {
                        if ((tailIndexes.get(i)[0] == snake.x) && (tailIndexes.get(i)[1] == snake.y) && (snake.got == false)) {
                            VBox vBox = new VBox();
                            Text text1 = new Text("GAME OVER");
                            text1.setFont(Font.font(40));
                            vBox.getChildren().add(text1);
                            vBox.setAlignment(Pos.CENTER);
                            Rectangle rectangle = new Rectangle(500, 500, Color.DARKRED);
                            pane.getChildren().addAll(rectangle, vBox);
                        }
                    }

                    if (tailIndexes.size() == 1) {
                        tailIndexes.get(0)[0] = snake.x;
                        tailIndexes.get(0)[1] = snake.y;
                    }
                    else {
                        for (int i = tailIndexes.size()-1; i>0; i--) {
                            tailIndexes.get(i)[0] = tailIndexes.get(i-1)[0];
                            tailIndexes.get(i)[1] = tailIndexes.get(i-1)[1];
                        }
                        tailIndexes.get(0)[0] = snake.x;
                        tailIndexes.get(0)[1] = snake.y;
                    }

                    for (int i = 0; i<tails.size(); i++) {
                        tails.get(i).move();
                    }

                    if (snake.got == true) {
                        circlex = Math.round(Math.random()*22);
                        circley = Math.round(Math.random()*22);
                        vbox.setMargin(circle, new Insets(25+circley*20, 0, 0, 25+circlex*20));
                        
                        tails.add(new Tail(pane, ++tailNumber));
                        int[] array2 = {snake.x, snake.y};
                        tailIndexes.add(array2);
                        snake.got = false;
                    }

                    lastUpdate = now;
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = new Runnable() {
            private int count = 4;
            
            @Override
            public void run() {
                count--;
                text.setText(String.valueOf(count));
                if (count <= 0) {
                    text.setText("Start!");
                }
                if (count <= -1) {
                    executor.shutdown();
                    Platform.runLater(() -> {
                        vbox.getChildren().clear();
                        circle = new Circle(5);
                        vbox.setAlignment(Pos.TOP_LEFT);
                        vbox.getChildren().add(circle);
                        circlex = Math.round(Math.random()*22);
                        circley = Math.round(Math.random()*22);
                        vbox.setMargin(circle, new Insets(25+circley*20, 0, 0, 25+circlex*20));
                        timer.start();
                    });
                }
            }
        };
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
    }
}

package com.agors.application.window;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Клас, що реалізує екран завантаження (splash screen) для застосунку Histotrek.
 * <p>
 * При відображенні показує анімацію пульсації тексту та падаючого піску,
 * після чого переходить на головне меню.
 * </p>
 *
 * @author agors
 * @version 1.0
 */
public class SplashScreen {
    /**
     * Вікно splash screen.
     */
    private final Stage stage;

    /**
     * Створює екземпляр SplashScreen з налаштуванням стилю та сцени.
     * <p>
     * Ініціалізує безрамкове вікно, текст та фоновий градієнт.
     * </p>
     */
    public SplashScreen() {
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        Text text = new Text("HISTOTREK");
        text.setFont(new Font("Arial", 40));
        text.setFill(Color.web("#8B4513"));

        Pane sandPane = new Pane();
        sandPane.setPickOnBounds(false);

        StackPane root = new StackPane(sandPane, text);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #EDC9AF, #e29264);");

        Scene scene = new Scene(root, 500, 350);
        stage.setScene(scene);

        stage.setOnShown(e -> {
            playTextPulse(text);
            playSandAnimation(sandPane);
        });
    }

    /**
     * Відображає splash screen, а після затримки закриває його та показує наступний екран.
     *
     * @param nextStage вікно головного меню, яке буде показане після завершення анімації
     */
    public void show(Stage nextStage) {
        stage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(2.8));
        delay.setOnFinished(e -> {
            stage.close();
            Platform.runLater(() -> new MenuScreen().show(nextStage));
        });
        delay.play();
    }

    /**
     * Запускає безкінечну анімацію пульсації тексту.
     *
     * @param text текстовий елемент для анімації пульсу
     */
    private void playTextPulse(Text text) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(1.5), text);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);

        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(1.5), text);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        SequentialTransition pulse = new SequentialTransition(scaleUp, scaleDown);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.play();
    }

    /**
     * Запускає безкінечну анімацію частинок піску, які піднімаються вгору та зникають.
     *
     * @param sandPane контейнер для частинок піску
     */
    private void playSandAnimation(Pane sandPane) {
        Timeline sandTimeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            Circle sand = new Circle(2, Color.web("#000000", 0.6));
            sand.setCenterX(Math.random() * 500);
            sand.setCenterY(350);

            sandPane.getChildren().add(sand);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(2), sand);
            tt.setByY(-400);
            tt.setByX(Math.random() * 100 - 50);
            tt.setOnFinished(ev -> sandPane.getChildren().remove(sand));
            tt.play();
        }));
        sandTimeline.setCycleCount(Animation.INDEFINITE);
        sandTimeline.setRate(1.5);
        sandTimeline.play();
    }
}

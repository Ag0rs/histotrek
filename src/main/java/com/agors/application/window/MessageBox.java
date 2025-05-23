package com.agors.application.window;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Універсальне модальне вікно для відображення повідомлень користувачу.
 * <p>
 * Показує заголовок та текст повідомлення з кнопкою "OK",
 * з плавною анімацією появи та масштабування.
 * </p>
 *
 * @author agors
 * @version 1.0
 */
public class MessageBox {

    /**
     * Відображає модальне вікно з повідомленням.
     * <p>
     * Вікно блокує родительську сцену до закриття,
     * показує заголовок та текст повідомлення,
     * застосовує анімацію появи.</p>
     *
     * @param title   заголовок вікна повідомлення
     * @param message текст повідомлення для користувача
     */
    public static void show(String title, String message) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(30));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        box.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.25)));

        Text titleText = new Text(title);
        titleText.setFont(Font.font("Arial", 22));
        titleText.setFill(Color.web("#1a3e2b"));

        Text messageText = new Text(message);
        messageText.setFont(Font.font("Arial", 14));
        messageText.setFill(Color.web("#333333"));
        messageText.setWrappingWidth(280);
        messageText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Button okButton = new Button("OK");
        okButton.setFont(Font.font("Arial", 14));
        okButton.setPrefWidth(100);
        okButton.setStyle("-fx-background-color: #c2b280; -fx-text-fill: white; -fx-background-radius: 8;");
        okButton.setOnMouseEntered(e -> okButton.setStyle("-fx-background-color: #a99e75; -fx-text-fill: white; -fx-background-radius: 8;"));
        okButton.setOnMouseExited(e -> okButton.setStyle("-fx-background-color: #c2b280; -fx-text-fill: white; -fx-background-radius: 8;"));

        okButton.setOnAction(e -> dialog.close());

        box.getChildren().addAll(titleText, messageText, okButton);

        Scene scene = new Scene(box, 350, 200);
        dialog.setScene(scene);
        dialog.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        // Налаштування початкової трансформації для анімації
        box.setScaleX(0.85);
        box.setScaleY(0.85);
        box.setOpacity(0);

        FadeTransition fade = new FadeTransition(Duration.millis(300), box);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(300), box);
        scale.setFromX(0.85);
        scale.setFromY(0.85);
        scale.setToX(1);
        scale.setToY(1);

        fade.play();
        scale.play();

        dialog.showAndWait();
    }
}
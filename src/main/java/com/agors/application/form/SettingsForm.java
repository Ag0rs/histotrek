package com.agors.application.form;

import com.agors.application.window.MessageBox;
import com.agors.domain.entity.User;
import com.agors.domain.validation.LoginValidator;
import com.agors.infrastructure.persistence.impl.UserDaoImpl;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Форма налаштувань користувача.
 * <p>
 * Відображає різні секції налаштувань (Профіль, Переваги, Адміністрування,
 * Приватність, Інформація, Допомога), дозволяє змінювати тему, мову,
 * розмір шрифту та інші опції, а також повернутись назад.
 * </p>
 *
 * @author agors
 * @version 1.0
 */
public class SettingsForm {

    /** Батьківське вікно (MenuScreen), до якого повернемось */
    private final Stage parentStage;

    /**
     * Конструктор форми налаштувань.
     *
     * @param parentStage головне вікно програми
     */
    public SettingsForm(Stage parentStage) {
        this.parentStage = parentStage;
    }

    /**
     * Показує вікно налаштувань і приховує батьківське.
     *
     * @param settingsStage новий Stage для відображення налаштувань
     */
    public void show(Stage settingsStage) {
        parentStage.hide();
        settingsStage.setFullScreen(parentStage.isFullScreen());
        settingsStage.setFullScreenExitHint("");

        Button backBtn = createBackButton();
        backBtn.setOnAction(e -> {
            settingsStage.close();
            parentStage.setFullScreen(settingsStage.isFullScreen());
            parentStage.show();
        });

        Text title = new Text("Settings");
        title.setFont(Font.font("Arial", 28));
        title.setFill(Color.BLACK);

        HBox topBar = new HBox(10, backBtn, title);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #d3d3d3;");

        VBox content = new VBox(20,
            createSection("Profile",
                createStyledButton("Змінити ім'я користувача"),
                createStyledButton("Оновити електронну пошту"),
                createStyledButton("Змінити пароль")
            ),
            createSection("Preferences",
                createThemeToggle(),
                createLanguageChoice(),
                createFontSizeControl()
            ),
            createSection("Administration",
                createStyledButton("Access the Citadel", ev -> {
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Administration");
                    info.setHeaderText(null);
                    info.setContentText("Admin menu coming soon.");
                    info.showAndWait();
                })
            ),
            createSection("Privacy & Security",
                createStyledCheckBox("Двохфакторна автентифікація"),
                createStyledButton("Видалити обліковий запис")
            ),
            createSection("About",
                createStyledLabel("Версія додатку: 1.0.0"),
                createStyledLink("Ліцензія / Умови використання"),
                createStyledLink("Контакти підтримки")
            ),
            createSection("Help & Feedback",
                createStyledLink("Часті питання"),
                createStyledButton("Надіслати відгук"),
                createStyledLink("Зв’язатися з підтримкою")
            )
        );
        content.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        BorderPane root = new BorderPane(scrollPane);
        root.setTop(topBar);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #fdf6e3, #e29264);");

        Scene scene = new Scene(root, 800, 600);
        // F11 для перемикання повноекранного режиму
        scene.setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.F11) {
                settingsStage.setFullScreen(!settingsStage.isFullScreen());
            }
        });

        settingsStage.setScene(scene);
        settingsStage.setTitle("Settings");
        settingsStage.setMinWidth(800);
        settingsStage.setMinHeight(600);
        settingsStage.show();
    }

    /**
     * Створює розділ налаштувань із заголовком та контролами.
     *
     * @param heading  назва секції
     * @param controls елементи інтерфейсу для секції
     * @return VBox, що містить секцію
     */
    private VBox createSection(String heading, javafx.scene.Node... controls) {
        Text h = new Text(heading);
        h.setFont(Font.font("Arial", 20));
        h.setFill(Color.BLACK);
        VBox box = new VBox(10, h);
        box.setPadding(new Insets(10));
        box.setStyle(
            "-fx-background-color: white; "
                + "-fx-border-color: #d3d3d3; "
                + "-fx-border-radius: 8; "
                + "-fx-background-radius: 8;"
        );
        box.getChildren().addAll(controls);
        return box;
    }

    /**
     * Створює стилізовану кнопку з базовим та hover-ефектом.
     *
     * @param text    текст на кнопці
     * @param handler (опційно) обробник події натискання
     * @return конфігурований Button
     */
    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", 14));
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: #c2b280; -fx-text-fill: black; -fx-background-radius: 8;");
        btn.setCursor(Cursor.HAND);
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #a99e75; -fx-text-fill: black; -fx-background-radius: 8;"));
        btn.setOnMouseExited (e -> btn.setStyle("-fx-background-color: #c2b280; -fx-text-fill: black; -fx-background-radius: 8;"));
        btn.setEffect(new DropShadow(4, Color.rgb(0,0,0,0.2)));
        if (handler != null) btn.setOnAction(handler);
        return btn;
    }
    private Button createStyledButton(String text) {
        return createStyledButton(text, null);
    }

    /**
     * Створює стилізований CheckBox.
     *
     * @param text підпис поруч з прапорцем
     * @return налаштований CheckBox
     */
    private CheckBox createStyledCheckBox(String text) {
        CheckBox cb = new CheckBox(text);
        cb.setFont(Font.font("Arial", 14));
        cb.setTextFill(Color.BLACK);
        return cb;
    }

    /**
     * Створює перемикач теми (Dark/Light) та управляє фоном програми.
     *
     * @return HBox з лейблом та ToggleButton
     */
    private HBox createThemeToggle() {
        Label lbl = new Label("Theme:");
        lbl.setFont(Font.font("Arial", 14));
        lbl.setTextFill(Color.BLACK);

        ToggleButton toggle = new ToggleButton("Dark");
        toggle.setFont(Font.font("Arial", 14));
        toggle.setCursor(Cursor.HAND);
        toggle.setStyle("-fx-background-color: #c2b280; -fx-text-fill: black; -fx-background-radius: 12;");
        toggle.setOnAction(e -> {
            if (toggle.isSelected()) {
                toggle.setText("Light");
                rootSetBackground(toggle, "#2b2b2b");
            } else {
                toggle.setText("Dark");
                rootSetBackground(toggle, "linear-gradient(to bottom right, #fdf6e3, #e29264)");
            }
        });

        return new HBox(10, lbl, toggle);
    }

    /**
     * Допоміжний метод для зміни фону кореневого контейнера.
     *
     * @param ctrl елемент з якого беремо Scene
     * @param bg   CSS-фон
     */
    private void rootSetBackground(Control ctrl, String bg) {
        ctrl.getScene().getRoot().setStyle("-fx-background-color: " + bg + ";");
    }

    /**
     * Створює ChoiceBox для вибору мови.
     *
     * @return налаштований ChoiceBox з мовами
     */
    private ChoiceBox<String> createLanguageChoice() {
        ChoiceBox<String> cb = new ChoiceBox<>();
        cb.getItems().addAll("Українська", "English");
        cb.setValue("Українська");
        cb.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        return cb;
    }

    /**
     * Створює контрол для налаштування розміру шрифту.
     *
     * @return HBox з Label, Slider і відображенням значення
     */
    private HBox createFontSizeControl() {
        Label lbl = new Label("Font Size:");
        lbl.setFont(Font.font("Arial", 14));
        lbl.setTextFill(Color.BLACK);

        Slider slider = new Slider(10, 24, 14);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(2);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);
        slider.setPrefWidth(200);

        Label valueLabel = new Label(String.valueOf((int) slider.getValue()));
        valueLabel.setFont(Font.font("Arial", 14));
        valueLabel.setTextFill(Color.BLACK);

        slider.valueProperty().addListener((obs, o, n) -> {
            valueLabel.setText(String.valueOf(n.intValue()));
        });

        return new HBox(10, lbl, slider, valueLabel);
    }

    /**
     * Створює простий Label.
     *
     * @param text текст для відображення
     * @return налаштований Label
     */
    private Label createStyledLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", 14));
        l.setTextFill(Color.BLACK);
        return l;
    }

    /**
     * Створює Hyperlink.
     *
     * @param text текст посилання
     * @return налаштований Hyperlink
     */
    private Hyperlink createStyledLink(String text) {
        Hyperlink hl = new Hyperlink(text);
        hl.setFont(Font.font("Arial", 14));
        hl.setTextFill(Color.BLACK);
        return hl;
    }

    /**
     * Створює кнопку повернення назад у вигляді кола зі стрілкою.
     *
     * @return кнопка Back
     */
    private Button createBackButton() {
        StackPane circle = new StackPane();
        circle.setPrefSize(36, 36);
        circle.setStyle("-fx-background-color: white; -fx-background-radius: 18;");
        circle.setEffect(new DropShadow(4, Color.rgb(0,0,0,0.2)));

        Text arrow = new Text("\u2190");
        arrow.setFont(Font.font("Arial", 18));
        arrow.setFill(Color.web("#3e2723"));
        circle.getChildren().add(arrow);

        Button btn = new Button();
        btn.setGraphic(circle);
        btn.setBackground(Background.EMPTY);
        btn.setCursor(Cursor.HAND);

        ScaleTransition enter = new ScaleTransition(Duration.millis(150), circle);
        enter.setToX(1.1); enter.setToY(1.1);
        ScaleTransition exit  = new ScaleTransition(Duration.millis(150), circle);
        exit.setToX(1.0); exit.setToY(1.0);
        btn.setOnMouseEntered(e -> enter.playFromStart());
        btn.setOnMouseExited(e -> exit.playFromStart());

        return btn;
    }
}

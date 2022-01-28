package ru.shadar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Objects;
import java.util.Optional;

/**
 * Точка входа в прриложение.
 * Но, т.к. приложение не дописано, после загрузки XML файла будет выдавать только сообщение о обработке данных)
 */
public class Main extends Application {
    private static Stage stage;

    private void setPrimaryStage(Stage stage) {
        Main.stage = stage;
    }

    public static Stage getPrimaryStage() {
        return stage;
    }

    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("Main.fxml"))
        );
        primaryStage.setTitle("Documents Creator");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(true);
        primaryStage.show();
        primaryStage.getScene().getWindow().addEventFilter(
                WindowEvent.WINDOW_CLOSE_REQUEST, this::close);
    }

    public static void main(String[] args) {
        launch();
    }


    /**
     * Метод генерации окна предупреждения при закрытии приложения
     * p.s. для кнопки "Отмена" взят прототип OK_DONE т.к. в системе линукс кнопка NO
     * отображается раньше кнопки YES, что сбивает при тестировании на UNIX и Win системах.
     * @param event приложение
     */
    private void close(WindowEvent event) {
        System.out.println("Запрос на закрытие программы");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        ButtonType btnY = new ButtonType("Да", ButtonBar.ButtonData.YES);
        ButtonType btnN = new ButtonType("Отмена", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().add(btnN);
        alert.getButtonTypes().add(btnY);
        //TODO: add next line to report file;
        alert.setTitle("Запрос на закрытие программы");
        alert.setHeaderText("");
        alert.setContentText(String.format("Закрыть программу?"));
        alert.initOwner(stage.getOwner());
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent()) {
            if (res.get().equals(btnN))
                event.consume();
        }
    }
}


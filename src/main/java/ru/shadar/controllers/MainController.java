package ru.shadar.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.shadar.Main;
import ru.shadar.models.MainModel;
import ru.shadar.objects.Xml;
import ru.shadar.views.MainView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Контроллер приложения для реализации MVC
 */
public class MainController extends MainView implements Initializable {
    private MainModel model = MainModel.getInstance();
//    private NodsListAdapter nodsListAdapter;

    /**
     * Инициализатор
     *
     * @param location  расположение
     * @param resources ресурсы
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.setController(this);
        model.setController(this);

        super.setChoiceOnAction();
        super.setToolTips();

        super.setOnBtnOpen();
        super.setOnBtnGenerator();
        super.setOnBtnOpenWindowEditNods();
        super.setOnBtnResult();

    }

    /* Обработчики */

    /**
     * Обработчик кнопки открытия файла
     *
     * @param button Копка открыть файл
     */
    public void setOnBtnOpen(Button button) {
        button.setOnAction(a -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Открыть XML файл");
            File file = fileChooser.showOpenDialog(Main.getPrimaryStage());
            try {
                readFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Обработчик значения выбранного в выпадающем списке
     *
     * @param cb выпадающий список.
     */
    public void setOnActionChoice(ChoiceBox<String> cb) {
        cb.setOnAction(a -> {
            model.addEntryToMap(cb.getId(), cb.getValue());
            //TODO внутренние данные, добавить в лог
            System.out.println("MainController: 80 " + cb.getId() + " установлено значение " + cb.getValue());
            //TODO dell next Line
            System.out.println(model.getElementSheet());
            if (cb.getId().equals("Address") || cb.getId().equals("Node") || cb.getId().equals("Position")) {
                super.setBtnGenElemEnable(cb.getValue() != null);
            }
        });
    }

    /**
     * Обработчик кнопки генерации элементов
     *
     * @param button кнопка генерации
     */
    public void setOnBtnGenerator(Button button) {
        button.setOnAction(a -> {
            super.setBtnGenNodsEnable(true);
        });
    }

    /**
     * Обработчик абстрактной кнопки открытия окна
     *
     * @param button кнопка окна
     */
    private void setOnBtnOpenView(Button button, String fxmlFileName, String title) {
        button.setOnAction(a -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainController.class.getResource(fxmlFileName));

            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 600, 400);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("loading" + fxmlFileName);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.getScene().getWindow().addEventFilter(
                    WindowEvent.WINDOW_CLOSE_REQUEST, this::close);
            stage.show();
        });
    }

    private void close(WindowEvent event) {

    }

    /**
     * Метод открытия окна настройки типов элементов
     *
     * @param button кнопка открытия окна
     */
    public void setOnBtnOpenNodsEdit(Button button) {
        setOnBtnOpenView(button, "NodsList.fxml", "Окно настройки узлов");
    }

    @Deprecated
    public void setOnBtnResult(Button button) {
//        button.setOnAction(a -> {
//            for (Element el : model.getElements()) {
//                System.out.println(el);
//                System.out.println(System.lineSeparator());
//            }
//            System.out.println("====");
//            model.testDevice();
//        });

    }

    /*  Внутреннее взаимодействие */

    /**
     * Метод обработки входного файла.
     *
     * @param file файл
     */
    private void readFile(File file) throws IOException {
        Xml xml = validate(file);
        if (xml != null) {
            xmlValidate(xml);
            model.generateDevice(xml.getParseResult());
            model.createWriter(model.createRecord(),
                    Path.of(file.getAbsolutePath()).getParent().toString());
        }
    }

    /**
     * Валидация файла. Проверка на формат .xml
     *
     * @param file файл
     */
    private Xml validate(File file) {
        Xml xml = null;
        if (file == null) {
            fileLoadError("Файл отсутствует");
        } else if (!file.getName().endsWith(".xml")) {
            super.setPathOpenFile(file.getAbsolutePath());
            fileLoadError("Текущий файл другого формата");
        } else {
            super.setPathOpenFile(file.getAbsolutePath());
            xml = new Xml(file.getPath());
            System.out.println("Файл загружен");
        }
        return xml;
    }

    /**
     * Парсинг и валидация данных объекта xml
     *
     * @param xml объект с данными
     */
    private void xmlValidate(Xml xml) {
        System.out.println("Парсинг начат");
        xml.parse();
        if (xml.getParseResult() == null) {
            xml = null;
            System.out.println("Результат отсутствует");
        } else {
            super.setUserNotify("XML загружен. Начата обработка данных");
            super.setUserText("Подождите, данные обрабатываются");
        }
    }

    /* Обработка ошибок */

    /**
     * Метод отображающий ошибку загрузки файла
     *
     * @param message сообщение в окне ошибки
     */
    public static void fileLoadError(String message) {
        System.out.println("Ошибка: " + message);
        showAlert(
                Alert.AlertType.ERROR,
                "Ошибка: "
                        + "Загрузите XML файл",
                message
        );
    }

    /**
     * Метод, отображающий ошибки при работе программы
     *
     * @param alertType тип ошибки
     * @param title     заголовок окна
     * @param context   контекст ошибки
     */
    public static void showAlert(Alert.AlertType alertType, String title, String context) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(context);
        Optional<ButtonType> result = alert.showAndWait();
        switch (alertType) {
            case CONFIRMATION: {
                if (result.get() == ButtonType.OK) {
                    Main.getPrimaryStage().close();
                }
                if (result.get() == ButtonType.CANCEL) {
                    alert.close();
                }
            }
            break;
            case ERROR:
            case INFORMATION: {
                if (result.get() == ButtonType.OK) {
                    alert.close();
                }
            }
            break;
            default:
                break;
        }
    }
}

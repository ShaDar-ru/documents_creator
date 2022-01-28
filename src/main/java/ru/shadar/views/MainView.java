package ru.shadar.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import ru.shadar.controllers.MainController;

import java.util.List;
import java.util.Locale;

public class MainView {
    private MainController controller;

    private ObservableList<String> choiceBoxData;

    @FXML
    private Pane openPane;
    @FXML
    private Pane choiceBoxPane;
    @FXML
    private Pane editorPane;
    @FXML
    private Pane surnamePane;
    @FXML
    private Pane resultViewPane;
    @FXML
    private Pane saverPane;

    @FXML
    private TextField txtFieldOpenFile;
    @FXML
    private Button btnOpen;
    @FXML
    private Button btnGenerator;
    @FXML
    private CheckBox checkTypeSize;
    @FXML
    private Button btnOpenViewEditElemTypes;
    @FXML
    private Button btnOpenViewEditNods;
    @FXML
    private Button btnOpenViewCheck;
    @FXML
    private Button btnOpenViewEditSurnames;
    @FXML
    private Button btnOpenViewScroll;
    @FXML
    private Button btnOpenViewBill;
    @FXML
    private Button btnOpenViewSpecific;
    @FXML
    private TextField txtFieldSaveFiles;
    @FXML
    private Button btnSaveFiles;
    @FXML
    private Label userNotify;
    @FXML
    private Label listProcesses;
    @FXML
    private Label listOfUserActions;
    @FXML
    private Label textToUserActions;
    @FXML
    protected ChoiceBox<String> choice1;
    @FXML
    protected ChoiceBox<String> choice2;
    @FXML
    protected ChoiceBox<String> choice3;
    @FXML
    protected ChoiceBox<String> choice4;
    @FXML
    protected ChoiceBox<String> choice5;
    @FXML
    protected ChoiceBox<String> choice6;
    @FXML
    protected ChoiceBox<String> choice7;

    /**
     * Устанавливает связь вью-контроллер
     *
     * @param controller ссылка на контроллер
     */
    public void setController(MainController controller) {
        this.controller = controller;
    }

    /**
     * Показывает путь к файлу в текстовом поле txtFieldOpenFile
     *
     * @param path путь к файлу
     */
    @FXML
    public void setPathOpenFile(String path) {
        txtFieldOpenFile.setText(path);
    }

    /**
     * Установщик данных выполняемых процессов
     *
     * @param text текст доя отображения
     */
    @FXML
    public void setUserNotify(String text) {
        userNotify.setText(text);
    }

    /**
     * Установщик уточненных данных для пользователя
     *
     * @param message сообщение для отображения
     */
    public void setUserText(String message) {
        listOfUserActions.setText(message);
    }

    /**
     * Включает работоспособность кнопки генерации элементов
     *
     * @param bool on|off
     */
    public void setBtnGenElemEnable(boolean bool) {
        btnGenerator.setDisable(!bool);
    }

    /**
     * Включает работоспособность кнопки генерации узлов
     *
     * @param bool on|off
     */
    public void setBtnGenNodsEnable(boolean bool) {
        btnOpenViewEditNods.setDisable(!bool);
    }

    /* Кнопки */

    /**
     * Метод устанавливающий связь кнопки (отображения) и кнопки (действия) открытия файлов
     */
    public void setOnBtnOpen() {
        controller.setOnBtnOpen(btnOpen);
    }

    /**
     * Метод устанавливающий связь кнопки (отображения) и кнопки (действия) генерации документов
     */
    public void setOnBtnGenerator() {
        controller.setOnBtnGenerator(btnGenerator);
    }

    public void setOnBtnOpenWindowEditNods() {
        controller.setOnBtnOpenNodsEdit(btnOpenViewEditNods);
    }

    public void setOnBtnResult(){
        controller.setOnBtnResult(btnOpenViewCheck);
    }

    /* //TODO input header here */

    /**
     * Обновление списка выпадающих меню.
     *
     * @param list список для выпадающего меню
     */
    public void updateChoiceBoxes(List<String> list) {
        updateAndSetDefault(choice1, list, "name");
        updateAndSetDefault(choice2, list, "address");
        updateAndSetDefault(choice3, list, "node");
        updateAndSetDefault(choice4, list, "position");
        updateAndSetDefault(choice5, list, "type");
        updateAndSetDefault(choice6, list, "datasheet");
        updateAndSetDefault(choice7, list, "case");
    }

    private void updateAndSetDefault(ChoiceBox<String> c, List<String> list, String value) {
        updater(c, list);
        for (String s : list) {
            if (s.toLowerCase(Locale.ROOT).equals(value)) {
                c.setValue(s);
            }
        }
    }

    /**
     * Метод, обновляющий данные в указанном выпадающем списке
     *
     * @param c    ChoiceBox
     * @param list новый список для отображения
     */
    private void updater(ChoiceBox<String> c, List<String> list) {
        c.setValue(null);
        c.getItems().removeAll(c.getItems());
        c.getItems().addAll(FXCollections.observableArrayList(list));
    }

    /**
     * Метод для передачи в контроллер выпадающие списки
     */
    public void setChoiceOnAction() {
        controller.setOnActionChoice(choice1);
        controller.setOnActionChoice(choice2);
        controller.setOnActionChoice(choice3);
        controller.setOnActionChoice(choice4);
        controller.setOnActionChoice(choice5);
        controller.setOnActionChoice(choice6);
        controller.setOnActionChoice(choice7);
    }

    /**
     * Включает работоспособность панели Выпадающих списков
     *
     * @param bool on|off
     */
    public void showChoiceBoxPane(boolean bool) {
        choiceBoxPane.setDisable(!bool);
    }

    /**
     * Метод создающий подсказки над полями view
     */
    public void setToolTips() {
        choice1.setTooltip(new Tooltip("Укажите поле соответствующее значению \"Наименование\" элемента."
                + System.lineSeparator()
                + "Р1-8В 0,1 10 кОм +-5% -М-А - наименование"));
        choice2.setTooltip(new Tooltip("Укажите поле соответствующее значению \"Расположение\" элемента"
                + System.lineSeparator()
                + "A1-R1 - расположение"));
        choice3.setTooltip(new Tooltip("Укажите поле соответствующее значению \"Наименование\" элемента. "
                + System.lineSeparator()
                + "A1 - узел"));
        choice4.setTooltip(new Tooltip("Укажите поле соответствующее значению \"Позиция\" элемента. "
                + System.lineSeparator()
                + "R1 - позиция"));
        choice5.setTooltip(new Tooltip("Укажите поле соответствующее значению \"Подтип\" элемента. "
                + System.lineSeparator()
                + "Пример подтипа: Р1-8В"));
        choice6.setTooltip(new Tooltip("Укажите поле соответствующее значению \"ТУ\" элемента"));
        choice7.setTooltip(new Tooltip("Укажите поле соответствующее значению \"Типоразмер\" элемента"
                + System.lineSeparator()
                + "0,063 - типоразмер"));
    }


    //TODO сделать отдельные методы отображения конкретных слоев pane.
    //TODO всю иницализацию запихнуть внутрь этих методов
    //todo создать внутренние переменные, решить в каких случаях выгоднее засылать туда данные а в каких передавать методами


}

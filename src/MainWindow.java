import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.ListSelectionEvent;

public class MainWindow extends JFrame {
    // Списки для хранения значений X и Y
    private final List<Double> X = new ArrayList<>();
    private final List<Double> Y = new ArrayList<>();

    // Размер массивов X и Y
    private final int n = 10;

    // Индекс выбранного элемента в списке или комбо-боксе
    private int selectedIndex = -1;

    // Компоненты интерфейса
    private JList<Double> listX;
    private JComboBox<Double> comboY;
    private JTextField textFieldX;
    private JTextField textFieldY;
    private JButton btnX;
    private JButton btnY;

    public MainWindow() {
        // Начальная инициализация массивов X и Y
        for (int i = 0; i < n; i++) {
            X.add(1.0);
            Y.add(1.0);
        }

        // Настройка главного окна
        setTitle("Обработчик массивов"); // Устанавливаем заголовок окна
        setSize(400, 300); // Устанавливаем размер окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Устанавливаем действие при закрытии окна (завершение приложения)

        // Создание меню
        JMenuBar menuBar = new JMenuBar();

        // Меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem newItem = new JMenuItem("Новый");
        JMenuItem saveItem = new JMenuItem("Сохранить");
        JMenuItem loadItem = new JMenuItem("Загрузить");
        JMenuItem exitItem = new JMenuItem("Выход");

        // Обработчики событий для пунктов меню "Файл"
        newItem.addActionListener(e -> onNew());
        saveItem.addActionListener(e -> onSave());
        loadItem.addActionListener(e -> onLoad());
        exitItem.addActionListener(e -> System.exit(0)); // Завершение приложения

        fileMenu.add(newItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator(); // Разделитель в меню
        fileMenu.add(exitItem);

        // Меню "Работа"
        JMenu workMenu = new JMenu("Работа");
        JMenuItem calculateItem = new JMenuItem("Вычислить");

        // Обработчик события для пункта меню "Вычислить"
        calculateItem.addActionListener(e -> onCalculate());

        workMenu.add(calculateItem);

        // Добавление меню в панель меню
        menuBar.add(fileMenu);
        menuBar.add(workMenu);

        setJMenuBar(menuBar); // Устанавливаем панель меню для главного окна

        // Добавление компонентов на форму
        JLabel labelX = new JLabel("Массив X:");
        JLabel labelY = new JLabel("Массив Y:");
        JLabel labelX1 = new JLabel("Значение X:");
        JLabel labelY2 = new JLabel("Значение Y:");
        listX = new JList<>(X.toArray(new Double[0])); // Список для отображения значений X
        comboY = new JComboBox<>(Y.toArray(new Double[0])); // Комбо-бокс для отображения значений Y
        textFieldX = new JTextField(10); // Поле для ввода значения X
        textFieldY = new JTextField(10); // Поле для ввода значения Y
        btnX = new JButton("Изменить X"); // Кнопка для применения значения X
        btnY = new JButton("Изменить Y"); // Кнопка для применения значения Y

        // Обработчики событий для списка и комбо-бокса
        listX.addListSelectionListener(e -> onListSelection(e, listX, textFieldX, btnX, X));
        comboY.addActionListener(e -> {
            selectedIndex = comboY.getSelectedIndex();
            if (selectedIndex != -1) {
                textFieldY.setText(String.valueOf(Y.get(selectedIndex)));
            }
        });

        // Обработчики событий для кнопок
        btnX.addActionListener(e -> onButtonClick(textFieldX, btnX, X));
        btnY.addActionListener(e -> onButtonClick(textFieldY, btnY, Y));

        JPanel panel = new JPanel();
        panel.add(labelX);
        panel.add(add(listX));
        panel.add(labelY);
        panel.add(comboY);
        panel.add(labelX1);
        panel.add(textFieldX);
        panel.add(labelY2);
        panel.add(textFieldY);
        panel.add(btnX);
        panel.add(btnY);
        add(panel); // Добавление панели с компонентами на форму
    }

    // Обработчик события "Новый"
    private void onNew() {
        // Вычисление нового значения для массива X и его отображение в списке и комбо-боксе
        for (int i = 0; i < n; i++) {
            double sum = 3.0;
            for (int j = 1; j <= i + 1; ++j) {
                sum = Math.sqrt(sum + 3.0 * X.get(i));
            }
            X.set(i, sum);
        }
        listX.setListData(X.toArray(new Double[0]));// Показывает актуальную инфу в listbox x
        comboY.setModel(new DefaultComboBoxModel<>(Y.toArray(new Double[0])));// Показывает актуальную инфу в combo y
    }

    // Обработчик события "Сохранить"
    private void onSave() {
        // Сохранение массива Y в текстовый файл
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы", "txt"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter out = new PrintWriter(file)) {
                for (Double value : Y) {
                    out.println(value);
                }
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Обработчик события "Загрузить"
    private void onLoad() {
        // Загрузка массива X из текстового файла
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы", "txt"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                X.clear();
                while ((line = reader.readLine()) != null) {
                    try {
                        double value = Double.parseDouble(line);
                        X.add(value);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Неверное значение в файле: " + line, "Ошибка", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Ошибка при загрузке файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
        listX.setListData(X.toArray(new Double[0]));// Показывает актуальную инфу в listbox x
        comboY.setModel(new DefaultComboBoxModel<>(Y.toArray(new Double[0])));// Показывает актуальную инфу в combo y
    }

    // Обработчик события "Вычислить"
    private void onCalculate() {
        // Вычисление нового значения для массива Y и его отображение в комбо-боксе
        Y.clear();
        Y.addAll(X); // Инициализация Y копией значений из X для сохранения размера

        // Цикл по всем элементам массива X
        for (int i = 0; i < n; i++) {
            double sum = 3.0;
            for (int j = 1; j <= i + 1; j++) {
                sum = Math.sqrt(sum + 3.0 * X.get(i));
            }
            Y.set(i, sum); // Присвоение вычисленного значения sum элементу массива Y
        }
        listX.setListData(X.toArray(new Double[0]));// Показывает актуальную инфу в listbox x
        comboY.setModel(new DefaultComboBoxModel<>(Y.toArray(new Double[0])));// Показывает актуальную инфу в combo y
    }

    // Обработчик события "Выбор элемента в списке"
    private void onListSelection(ListSelectionEvent e, JList<Double> list, JTextField textField, JButton btn, List<Double> arr) {
        if (!e.getValueIsAdjusting()) {
            selectedIndex = list.getSelectedIndex();
            if (selectedIndex != -1) {
                textField.setText(String.valueOf(arr.get(selectedIndex)));
            }
        }
    }

    // Обработчик события "Выбор элемента в комбо-боксе"
    private void onComboSelection(JComboBox<Double> combo, JTextField textField, JButton btn, List<Double> arr) {
        int index = combo.getSelectedIndex();
        if (index != -1) {
            textField.setText(String.valueOf(arr.get(index)));
        }
    }

    // Обработчик события "Нажатие кнопки"
    private void onButtonClick(JTextField textField, JButton btn, List<Double> arr) {
        try {
            double value = Double.parseDouble(textField.getText());
            if (selectedIndex != -1) {
                arr.set(selectedIndex, value);
                updateListsAndCombos();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Неверное значение", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Обновление списка и комбо-бокса
    private void updateListsAndCombos() {
        listX.setListData(X.toArray(new Double[0]));// Показывает актуальную инфу в listbox x
        comboY.setModel(new DefaultComboBoxModel<>(Y.toArray(new Double[0])));// Показывает актуальную инфу в combo y
    }

    // Точка входа в приложение
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}
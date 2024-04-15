package oop.gui;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import javax.swing.*;


import oop.log.Logger;
import oop.serialization.StateIO;
import oop.serialization.StateRestoreManager;
import oop.serialization.StateSaverManager;
import oop.serialization.Storable;
import oop.model.Robot;

/*
  Что требуется сделать:
  1. Метод создания меню перегружен функционалом и трудно читается.
  Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */

/**
 * Главное окно приложения.
 */
public class MainApplicationFrame extends JFrame implements Storable {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final StateIO stateIO = new StateIO();
    private final String name = "MainApplicationFrame";
    private final Robot robot = new Robot();

    /**
     * Конструктор для создания главного окна приложения
     */
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);
        setContentPane(desktopPane);

        addWindow(createLogWindow());
        addWindow(createGameWindow(robot));
        addWindow(createCoordinatesWindow());
        setJMenuBar(generateMenuBar());
        stateIO.loadStates(getAllWindows());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                runExitDialog();
            }
        });

    }
    /**
     * Создает окно координат робота
     *
     * @return Окно с координатами
     */
    private JInternalFrame createCoordinatesWindow() {
        CoordinatesWindow coordinatesWindow = new CoordinatesWindow(robot);
        coordinatesWindow.setSize(350, 350);
        coordinatesWindow.setLocation(10, 20);
        return coordinatesWindow;
    }

    /**
     * Создает окно игры
     *
     * @return Окно игры
     */
    private JInternalFrame createGameWindow(Robot robot) {
        GameWindow gameWindow = new GameWindow(robot);
        gameWindow.setSize(400, 400);
        gameWindow.setLocation(0, 0);
        return gameWindow;
    }

    /**
     * Получает список всех внутренних окон
     *
     * @return Список внутренних окон
     */
    private List<Storable> getAllWindows() {
        List<Storable> allWindows = new ArrayList<>();
        for (JInternalFrame jInternalFrame : desktopPane.getAllFrames()) {
            if (jInternalFrame instanceof Storable storable) {
                allWindows.add(storable);
            }
        }
        allWindows.add(this);
        return allWindows;
    }

    /**
     * Обработчик операции выхода из приложения, уточняет действительно ли пользователь хочет его закрыть
     */
    private void runExitDialog() {
        int userChoice = JOptionPane.showConfirmDialog(
                this,
                "Вы уверены?",
                "Выйти",
                JOptionPane.YES_NO_OPTION);
        if (userChoice == JOptionPane.YES_OPTION) {
            stateIO.saveStates(getAllWindows());
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    /**
     * Создает окно лога
     *
     * @return Окно лога
     */
    private LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    /**
     * Добавляет внутренне окно на главное окно
     *
     * @param frame Внутреннее окно
     */
    private void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
//
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        return menuBar;
//    }

    /**
     * Создает меню приложения
     *
     * @return Меню приложения
     */
    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> Logger.debug("Новая строка"));
            testMenu.add(addLogMessageItem);
        }
        JMenu quitMenu = new JMenu("Выход");
        quitMenu.setMnemonic(KeyEvent.VK_T);
        quitMenu.getAccessibleContext().setAccessibleDescription("Выйти из программы");
        {
            JMenuItem addLogMessageItem = new JMenuItem("Выход", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                WindowEvent closeEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
            });
            quitMenu.add(addLogMessageItem);
        }
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(quitMenu);
        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    @Override
    public void save(Map<String, String> states) {
        StateSaverManager stateSaverManager = new StateSaverManager(name);
        stateSaverManager.save(states, this);
    }

    @Override
    public void restore(Map<String, String> states) {
        StateRestoreManager stateRestoreManager = new StateRestoreManager(name);
        stateRestoreManager.restore(states, this);
    }
}

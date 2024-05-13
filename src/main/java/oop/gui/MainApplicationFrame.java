package oop.gui;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;
import javax.swing.*;


import oop.locale.LangManager;
import oop.log.Logger;
import oop.serialization.StateIO;
import oop.serialization.StateRestoreManager;
import oop.serialization.StateSaverManager;
import oop.serialization.Storable;
import oop.model.Robot;


/**
 * Главное окно приложения.
 */
public class MainApplicationFrame extends JFrame implements Storable, Observer {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final StateIO stateIO = new StateIO();
    private final String name = "MainApplicationFrame";
    private final Robot robot = new Robot();
    private final LangManager control = LangManager.getInstance();
    private static Locale currentLang = new Locale("ru");
    private JMenu switchLang, lookAndFeelMenu, testMenu, quitMenu;
    private JMenuItem switchLangRu, switchLangEn, quitMenuItem, systemLookAndFeelItem,
            crossplatformLookAndFeelItem, logMessageItem;
    /**
     * Конструктор для создания главного окна приложения
     */
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        control.addObserver(this);
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);
        try {
            UIManager.put("OptionPane.yesButtonText",
                    control.getLocale("YES_BUTTON"));
            UIManager.put("OptionPane.noButtonText",  control.getLocale("NO_BUTTON"));

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                control.getLocale("QUEST_AGREE"),
                control.getLocale("QUEST_EXIT"),
                JOptionPane.YES_NO_OPTION);
        if (userChoice == JOptionPane.YES_OPTION) {
            stateIO.saveStates(getAllWindows());
            this.dispose();
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
        Logger.debug(control.getLocale("FRAME_WORKING_PROTOCOL"));
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

        switchLang = new JMenu(control.getLocale("FRAME_SWITCH_LANG"));
        switchLang.setMnemonic(KeyEvent.VK_V);
        switchLang.getAccessibleContext().setAccessibleDescription(control.getLocale("FRAME_SWITCH_LANG"));
        {
            switchLangRu = new JMenuItem(control.getLocale("LANG_RU"), KeyEvent.VK_S);
            switchLangRu.setMnemonic(KeyEvent.VK_V);
            switchLangRu.addActionListener((event) -> {
                currentLang = new Locale("ru");
                control.setLocale(currentLang);
            });
            switchLangEn = new JMenuItem(control.getLocale("LANG_EN"), KeyEvent.VK_S);
            switchLangEn.setMnemonic(KeyEvent.VK_V);
            switchLangEn.addActionListener((event) -> {
                currentLang = new Locale("en");
                control.setLocale(currentLang);
            });
            switchLang.add(switchLangRu);
            switchLang.add(switchLangEn);
        }

        lookAndFeelMenu = new JMenu(control.getLocale("FRAME_DISPLAY_MODE"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                control.getLocale("FRAME_MANAGE_DISPLAY_MODE"));

        {
            systemLookAndFeelItem = new JMenuItem(control.getLocale("FRAME_SYS_SCHEME"), KeyEvent.VK_S);
            systemLookAndFeelItem.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeelItem);
        }

        {
            crossplatformLookAndFeelItem = new JMenuItem(control.getLocale("FRAME_UNI_SCHEME"), KeyEvent.VK_S);
            crossplatformLookAndFeelItem.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeelItem);
        }

        testMenu = new JMenu(control.getLocale("FRAME_TESTS"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                control.getLocale("FRAME_TEST_COMMANDS"));

        {
            logMessageItem = new JMenuItem(control.getLocale("FRAME_MES_LOG"), KeyEvent.VK_S);
            logMessageItem.addActionListener((event) -> Logger.debug(control.getLocale("LOG_MES")));
            testMenu.add(logMessageItem);
        }
        quitMenu = new JMenu(control.getLocale("FRAME_QUIT"));
        quitMenu.setMnemonic(KeyEvent.VK_T);
        quitMenu.getAccessibleContext().setAccessibleDescription(control.getLocale("FRAME_APP_QUIT"));
        {
            quitMenuItem = new JMenuItem(control.getLocale("FRAME_APP_QUIT"), KeyEvent.VK_S);
            quitMenuItem.addActionListener((event) -> {
                WindowEvent closeEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
            });
            quitMenu.add(quitMenuItem);
        }
        menuBar.add(switchLang);
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

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an {@code Observable} object's
     * {@code notifyObservers} method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     *            method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof LangManager && LangManager.PROPERTY_LANG.equals(arg)) {
                translate();
        }
    }
    /**
     * Перевод элементов на текущий язык.
     */
    private void translate(){
        this.switchLang.setText(control.getLocale("FRAME_SWITCH_LANG"));
        this.lookAndFeelMenu.setText(control.getLocale("FRAME_DISPLAY_MODE"));
        this.testMenu.setText(control.getLocale("FRAME_TESTS"));
        this.quitMenu.setText(control.getLocale("FRAME_QUIT"));
        this.switchLangRu.setText(control.getLocale("LANG_RU"));
        this.switchLangEn.setText(control.getLocale("LANG_EN"));
        this.quitMenuItem.setText(control.getLocale("FRAME_APP_QUIT"));
        this.systemLookAndFeelItem.setText(control.getLocale("FRAME_SYS_SCHEME"));
        this.crossplatformLookAndFeelItem.setText(control.getLocale("FRAME_UNI_SCHEME"));
        this.logMessageItem.setText(control.getLocale("FRAME_MES_LOG"));
    }
}

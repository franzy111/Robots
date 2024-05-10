package oop.gui;

import java.awt.*;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import oop.locale.LangManager;
import oop.log.LogChangeListener;
import oop.log.LogEntry;
import oop.log.LogWindowSource;
import oop.serialization.StateRestoreManager;
import oop.serialization.StateSaverManager;
import oop.serialization.Storable;

public class LogWindow extends JInternalFrame implements LogChangeListener, Storable, Observer {
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;
    private final String name = "LogWindow";
    private static final LangManager control = LangManager.getInstance();

    public LogWindow(LogWindowSource logSource) {
        super(control.getLocale("LOG_WINDOW"), true, true, true, true);
        control.addObserver(this);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
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
            setTitle(control.getLocale("LOG_WINDOW"));
        }
    }
}

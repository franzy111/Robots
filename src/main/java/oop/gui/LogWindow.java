package oop.gui;

import java.awt.*;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import oop.log.LogChangeListener;
import oop.log.LogEntry;
import oop.log.LogWindowSource;
import oop.serialization.StateRestoreManager;
import oop.serialization.StateSaverManager;
import oop.serialization.Storable;

public class LogWindow extends JInternalFrame implements LogChangeListener, Storable {
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;
    private final String name = "LogWindow";

    public LogWindow(LogWindowSource logSource) {
        super("Протокол работы", true, true, true, true);
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
}

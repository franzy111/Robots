package oop.gui;


import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

/*
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
*/

            UIManager.put("OptionPane.yesButtonText", "Да");
            UIManager.put("OptionPane.noButtonText", "Нет");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.setVisible(true);
        });
    }
}
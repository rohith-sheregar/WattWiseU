package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Splash extends JFrame {
    private static Login loginInstance;
    private static final Logger LOGGER = Logger.getLogger(Splash.class.getName());

    Splash() {
        int size = (int) (Math.min(Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height) * 0.5);

        try {
            Image image = new ImageIcon(ClassLoader.getSystemResource("icon/splash/Splash.jpg")).getImage();
            add(new JLabel(new ImageIcon(image.getScaledInstance(size, size, Image.SCALE_SMOOTH))));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading splash image: ", e);
        }

        setSize(size, size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        new Timer(3000, e -> {
            dispose();
            if (loginInstance == null) {
                loginInstance = new Login();
                loginInstance.setVisible(true);
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Splash::new);
    }
}
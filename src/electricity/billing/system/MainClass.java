package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainClass extends JFrame implements ActionListener {  // Implement ActionListener interface
    // Assuming you're using the userId for some purpose
    int userId;

    public MainClass(int userId) {
        this.userId = userId;

        // Set up frame properties
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize frame
        setUndecorated(false);                  // Keeps frame decorations like close, minimize
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the app on frame close

        // App Name and Logo (Light Theme)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE); // Set light background
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center-align the content

        ImageIcon appIcon = new ImageIcon(ClassLoader.getSystemResource("icon/splash/Splash.jpg")); // Add your app icon here
        Image scaledAppIcon = appIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel appIconLabel = new JLabel(new ImageIcon(scaledAppIcon));

        JLabel appNameLabel = new JLabel("WattWiseU");
        appNameLabel.setFont(new Font("Serif", Font.BOLD, 30));
        appNameLabel.setForeground(Color.BLACK); // Set text color to black for contrast

        headerPanel.add(appIconLabel);
        headerPanel.add(appNameLabel);

        // Add headerPanel to the top of the frame
        add(headerPanel, BorderLayout.NORTH);

        // Background image setup
        JLabel bgLabel = new JLabel();
        ImageIcon bgImageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/ebs.png"));
        Image bgImage = bgImageIcon.getImage().getScaledInstance(1530, 830, Image.SCALE_DEFAULT);
        bgLabel.setIcon(new ImageIcon(bgImage));
        bgLabel.setLayout(new BorderLayout()); // Set layout for bgLabel to add menu below
        add(bgLabel, BorderLayout.CENTER);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE); // Set light background for menu bar

        // Center-aligned layout for the menu bar
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS)); // Align menus in a horizontal box layout

        // Add a small space (RigidArea) before the menus to shift them slightly to the right
        menuBar.add(Box.createHorizontalStrut(700)); // Adjust this value to shift more/less to the right

        // Information Menu
        JMenu info = new JMenu("Information");
        info.setFont(new Font("Serif", Font.PLAIN, 15));

        JMenuItem viewInfo = new JMenuItem("View Information");
        viewInfo.setFont(new Font("Monospaced", Font.PLAIN, 14));
        ImageIcon viewInfoImg = new ImageIcon(ClassLoader.getSystemResource("icon/information.png"));
        Image viewInfoImage = viewInfoImg.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        viewInfo.setIcon(new ImageIcon(viewInfoImage));
        viewInfo.addActionListener(this);  // Pass 'this' as the ActionListener
        info.add(viewInfo);

        JMenuItem editInfo = new JMenuItem("Edit Information");
        editInfo.setFont(new Font("Monospaced", Font.PLAIN, 14));
        ImageIcon editInfoImg = new ImageIcon(ClassLoader.getSystemResource("icon/refresh.png"));
        Image editInfoImage = editInfoImg.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        editInfo.setIcon(new ImageIcon(editInfoImage));
        editInfo.addActionListener(this);  // Pass 'this' as the ActionListener
        info.add(editInfo);

        // Bill Menu
        JMenu bill = new JMenu("Bill");
        bill.setFont(new Font("Serif", Font.PLAIN, 15));

        JMenuItem viewConsumption = new JMenuItem("View Consumption");
        viewConsumption.setFont(new Font("Monospaced", Font.PLAIN, 14));
        ImageIcon viewConsumptionImg = new ImageIcon(ClassLoader.getSystemResource("icon/detail.png"));
        Image viewConsumptionImage = viewConsumptionImg.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        viewConsumption.setIcon(new ImageIcon(viewConsumptionImage));
        viewConsumption.addActionListener(this);  // Pass 'this' as the ActionListener
        bill.add(viewConsumption);

        JMenuItem genBill = new JMenuItem("Generate Bill");
        genBill.setFont(new Font("Monospaced", Font.PLAIN, 14));
        ImageIcon genBillImg = new ImageIcon(ClassLoader.getSystemResource("icon/bill.png"));
        Image genBillImage = genBillImg.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        genBill.setIcon(new ImageIcon(genBillImage));
        genBill.addActionListener(this);  // Pass 'this' as the ActionListener
        bill.add(genBill);

        // Exit Menu
        JMenu exit = new JMenu("Exit");
        exit.setFont(new Font("Serif", Font.PLAIN, 15));

        JMenuItem eexit = new JMenuItem("Exit");
        eexit.setFont(new Font("Monospaced", Font.PLAIN, 14));
        ImageIcon eexitImg = new ImageIcon(ClassLoader.getSystemResource("icon/exit.png"));
        Image eexitImage = eexitImg.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        eexit.setIcon(new ImageIcon(eexitImage));
        eexit.addActionListener(this);  // Pass 'this' as the ActionListener
        exit.add(eexit);

        // Add menus to menu bar
        menuBar.add(info);
        menuBar.add(bill);
        menuBar.add(exit);

        // Add the menu bar to the top of the frame
        bgLabel.add(menuBar, BorderLayout.NORTH);

        // Set frame visible
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = e.getActionCommand();
        if (msg.equals("View Information")) {
            new Viewinformation(userId); // Your ViewInformation class
        } else if (msg.equals("Edit Information")) {
            new Editinformation(userId); // Your EditInformation class
        } else if (msg.equals("View Consumption")) {
            new ViewConsumption(userId); // Your ViewConsumption class
        } else if (msg.equals("Generate Bill")) {
            new GenerateBill(userId); // Your GenerateBill class
        } else if (msg.equals("Exit")) {
            setVisible(false);
            //new Login(); // Your Login class
        }
    }


}

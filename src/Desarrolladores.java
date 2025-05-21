import java.awt.*;
import javax.swing.*;

public class Desarrolladores extends JFrame {

    public Desarrolladores() {
        setTitle("Calculeme Esta - Desarrolladores");
        setSize(600, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(245, 245, 245));
        // Panel principal para el contenido scrolleable
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Desarrolladores");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setForeground(new Color(40, 40, 40));

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(titulo);
        mainPanel.add(Box.createVerticalStrut(20));

        // Datos de los desarrolladores
        Object[][] devs = {
            {"Jhoan David Naranjo Robledo", "230242068", "jhoan.naranjo01@uceva.edu.co", "imagenes.jpg/WhatsApp Image 2025-05-08 at 11.01.19 PM.jpeg"},
            {"Esteban Lozano Jaramillo", "230242048", "esteban.lozano01@uceva.edu.co", "imagenes.jpg/WhatsApp Image 2025-03-21 at 10.49.23 AM.jpeg"},
            {"Santiago Echeverri Torres", "230242047", "santiago.echeverri01@uceva.edu.co", "imagenes.jpg/WhatsApp Image 2025-05-08 at 8.45.01 AM.jpeg"},
            {"Juan David Gonzales Ospina", "230241015", "juan.gonzalez19@uceva.edu.co", "imagenes.jpg/WhatsApp Image 2025-05-16 at 8.06.45 AM.jpeg"}
        };

        for (Object[] dev : devs) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.setBackground(new Color(245, 245, 245));
            panel.setAlignmentX(CENTER_ALIGNMENT);
            panel.setMaximumSize(new Dimension(520, 110));

            // Imagen
            String imgPath = (String) dev[3];
            ImageIcon icon = new ImageIcon(imgPath);
            Image img = icon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            imgLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 20));

            // Info
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(new Color(245, 245, 245));
            JLabel nameLabel = new JLabel((String) dev[0]);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            JLabel codeLabel = new JLabel("Código: " + dev[1]);
            codeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            JLabel emailLabel = new JLabel("Correo: " + dev[2]);
            emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            infoPanel.add(nameLabel);
            infoPanel.add(codeLabel);
            infoPanel.add(emailLabel);

            panel.add(imgLabel);
            panel.add(infoPanel);
            mainPanel.add(panel);
            mainPanel.add(Box.createVerticalStrut(10));
        }
        // JScrollPane para permitir scroll horizontal y vertical
        JScrollPane scrollPane = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setContentPane(scrollPane);
    }
}

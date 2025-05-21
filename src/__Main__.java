import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;

public class __Main__ extends JFrame {
    public __Main__() {
        setTitle("Calculeme Esta - Menú Principal");
        setSize(320, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(13, 13, 13)); 
        
        // Usamos null layout para poder posicionar los botones manualmente
        setLayout(null);
        
        // Creamos el primer botón con posición explícita
        JButton botonCalculadora = new JButton("Calculadora");
        botonCalculadora.setFont(new Font("SansSerif", Font.PLAIN, 16));
        botonCalculadora.setFocusPainted(false);
        botonCalculadora.setBackground(new Color(60, 90, 150));
        botonCalculadora.setForeground(Color.WHITE);
        
        // Posicionamos directamente con coordenadas explícitas (x, y, ancho, alto)
        // Hacemos los botones más anchos (200px en lugar de 150px) y ajustamos la posición X
        botonCalculadora.setBounds(60, 40, 200, 40);
        add(botonCalculadora);
        
        // Creamos el segundo botón con posición explícita
        JButton botonDesarrolladores = new JButton("Desarrolladores");
        botonDesarrolladores.setFont(new Font("SansSerif", Font.PLAIN, 16));
        botonDesarrolladores.setFocusPainted(false);
        botonDesarrolladores.setBackground(new Color(60, 90, 150));
        botonDesarrolladores.setForeground(Color.WHITE);
        
        // Posicionamos directamente con coordenadas explícitas (x, y, ancho, alto)
        // Hacemos los botones más anchos (200px en lugar de 150px) y ajustamos la posición X
        botonDesarrolladores.setBounds(60, 110, 200, 40);
        add(botonDesarrolladores);
        
        // Agregamos los action listeners
        botonCalculadora.addActionListener(e -> {
            new MenuCalculadora(this).setVisible(true);
            this.setVisible(false);
        });
        
        botonDesarrolladores.addActionListener(e -> {
            Desarrolladores ventana = new Desarrolladores();
            ventana.setVisible(true);
        });
    }
  
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FlatOneDarkIJTheme.setup();
                } catch(Exception e) {
                    System.out.println("Error al cargar el tema: " + e.getMessage());
                }
                
                // Inicia primero SpashScreen
                new SpashScreen(1);
                
                // Main ahora se abrirá cuando SpashScreen se cierre
            }
        });
    }
}
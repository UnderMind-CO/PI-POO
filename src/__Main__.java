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
        getContentPane().setBackground(new Color(245, 245, 245)); 
        setLayout(new GridLayout(3, 1, 15, 15));
        
        JButton botonCalculadora = crearBoton("Calculadora");
        JButton botonDesarrolladores = crearBoton("Desarrolladores");
        //JButton botonOpcionVacia = crearBoton("Opción Vacía");
        
        add(botonCalculadora);
        add(botonDesarrolladores);
        //add(botonOpcionVacia);
        
        botonCalculadora.addActionListener(e -> {
            new MenuCalculadora(this).setVisible(true);
            this.setVisible(false);
        });
        
        botonDesarrolladores.addActionListener(e -> {
            Desarrolladores ventana = new Desarrolladores();
            ventana.setVisible(true);
        });
    }
    
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(60, 90, 150));
        boton.setForeground(Color.WHITE);
        return boton;
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
            
            // __Main__ ahora se abrirá cuando SpashScreen se cierre
        }
    });
}
}
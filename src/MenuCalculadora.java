import javax.swing.*;
import java.awt.*;

public class MenuCalculadora extends JFrame {

    public MenuCalculadora(JFrame ventanaAnterior) {
        setTitle("Calculeme Esta - Calculadora");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(13, 13, 13));
        setLayout(null);

        JButton botonDerivada = crearBoton("Calcular Derivada");
        botonDerivada.setBounds(60, 40, 300, 40);
        add(botonDerivada);

        JButton botonIntegral = crearBoton("Calcular Integral");
        botonIntegral.setBounds(60, 100, 300, 40);
        add(botonIntegral);

       JButton botonLineal = crearBoton("Calcular funciones lineal");
       botonLineal.setBounds(60,160,300,40);
       add(botonLineal);

        JButton botonRegresar = new JButton("Regresar");
        botonRegresar.setBounds(10, 250, 100, 25);
        botonRegresar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        botonRegresar.setBackground(new Color(120, 120, 120));
        botonRegresar.setForeground(Color.WHITE);
        botonRegresar.addActionListener(e -> {
            this.dispose();
            ventanaAnterior.setVisible(true);
        });
        add(botonRegresar);

        botonDerivada.addActionListener(e -> {
            new VentanaDerivada(this).setVisible(true);
            this.setVisible(false);
        });

        botonIntegral.addActionListener(e -> {
            new VentanaIntegral(this).setVisible(true);
            this.setVisible(false);
        });
        
        botonLineal.addActionListener(e -> {
            new VentanaLineal(this).setVisible(true);
            this.setVisible(false);
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
}

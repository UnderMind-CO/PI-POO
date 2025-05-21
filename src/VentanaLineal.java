import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaLineal extends JFrame {

    private JTextField campoA, campoB, campoC;
    private JLabel resultado;

    public VentanaLineal(JFrame ventanaAnterior) {
        setTitle("Ecuación Lineal");
        setSize(450, 320); // Aumenta el tamaño de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel labelTitulo = new JLabel("Resolver ecuación: Ax + B = C");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        labelTitulo.setBounds(90, 20, 300, 30);
        add(labelTitulo);

        campoA = new JTextField();
        campoA.setBounds(40, 70, 50, 30);
        add(campoA);

        JLabel labelX = new JLabel("x +");
        labelX.setFont(new Font("Arial", Font.PLAIN, 16));
        labelX.setBounds(95, 70, 40, 30);
        add(labelX);

        campoB = new JTextField();
        campoB.setBounds(130, 70, 50, 30);
        add(campoB);

        JLabel labelIgual = new JLabel("=");
        labelIgual.setFont(new Font("Arial", Font.PLAIN, 16));
        labelIgual.setBounds(185, 70, 20, 30);
        add(labelIgual);

        campoC = new JTextField();
        campoC.setBounds(210, 70, 50, 30);
        add(campoC);

        JButton botonResolver = new JButton("Resolver");
        botonResolver.setBounds(300, 70, 100, 30);
        add(botonResolver);

        resultado = new JLabel("Resultado: ");
        resultado.setBounds(40, 120, 340, 25);
        add(resultado);

        JButton botonGraficar = new JButton("Graficar");
        botonGraficar.setBounds(300, 120, 100, 30);
        add(botonGraficar);

        JButton botonRegresar = new JButton("Regresar");
        botonRegresar.setBounds(170, 235, 100, 30);
        add(botonRegresar);

        botonResolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double a = Double.parseDouble(campoA.getText());
                    double b = Double.parseDouble(campoB.getText());
                    double c = Double.parseDouble(campoC.getText());

                    if (a == 0) {
                        if (b == c) {
                            resultado.setText("Resultado: Infinitas soluciones");
                        } else {
                            resultado.setText("Resultado: Sin solución");
                        }
                    } else {
                        double x = (c - b) / a;
                        resultado.setText("Resultado: x = " + x);
                    }
                } catch (NumberFormatException ex) {
                    resultado.setText("Por favor ingrese valores válidos.");
                }
            }
        });

        botonGraficar.addActionListener(e -> {
            String FuncA = campoA.getText().trim();
            String FuncB = campoB.getText().trim();
            String FuncC = campoC.getText().trim();
            if (FuncA.isEmpty()|| FuncB.isEmpty() || FuncC.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por Favor Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double a = Double.parseDouble(campoA.getText());
                double b = Double.parseDouble(campoB.getText());
                double c = Double.parseDouble(campoC.getText());
                
                // Pasamos directamente los coeficientes A, B, C a la ventana gráfica
                // y la solución calculada (si existe)
                double solucion = Double.NaN; // Valor por defecto para casos sin solución única
                if (a != 0) {
                    solucion = (c - b) / a;
                }
                
                // Abre la ventana de graficación dedicada con los coeficientes originales
                new VentanaGraficaLineal(a, b, c, solucion).setVisible(true);
            } catch (NumberFormatException ex) {
                resultado.setText("Por favor ingrese valores válidos.");
            }
        });

        botonRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventanaAnterior.setVisible(true);
                dispose();
            }
        });
    }
}
import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class VentanaIntegral extends JFrame {
    private JTextField campoFuncion, campoA, campoB;
    private JLabel etiquetaResultado;

    public VentanaIntegral(JFrame ventanaAnterior) {
        setTitle("Calculeme Esta - Integral");
        setSize(450, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(new Color(15, 15, 15));

        JLabel etiquetaFuncion = new JLabel("Función f(x):");
        etiquetaFuncion.setBounds(50, 20, 300, 30);
        etiquetaFuncion.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(etiquetaFuncion);

        campoFuncion = new JTextField();
        campoFuncion.setBounds(50, 50, 300, 30);
        add(campoFuncion);

        campoA = new JTextField();
        campoB = new JTextField();

        JLabel etiquetaA = new JLabel("Límite inferior (a):");
        etiquetaA.setBounds(50, 90, 150, 25);
        add(etiquetaA);
        campoA.setBounds(200, 90, 100, 25);
        add(campoA);

        JLabel etiquetaB = new JLabel("Límite superior (b):");
        etiquetaB.setBounds(50, 120, 150, 25);
        add(etiquetaB);
        campoB.setBounds(200, 120, 100, 25);
        add(campoB);

        JButton boton = new JButton("Calcular Integral");
        boton.setBounds(50, 160, 300, 35);
        boton.setBackground(new Color(60, 90, 150));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        boton.addActionListener(e -> calcularIntegral());
        add(boton);

        etiquetaResultado = new JLabel("Resultado:");
        etiquetaResultado.setBounds(50, 210, 300, 30);
        add(etiquetaResultado);

        JButton botonGraficar = new JButton("Graficar");
        botonGraficar.setBounds(200, 280, 100, 30);
        add(botonGraficar);

        JButton botonRegresar = new JButton("Regresar");
        botonRegresar.setBounds(100, 280, 100, 30);
        botonRegresar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        botonRegresar.setBackground(new Color(120, 120, 120));
        botonRegresar.setForeground(Color.WHITE);
        botonRegresar.addActionListener(e -> {
            this.dispose();
            ventanaAnterior.setVisible(true);
        });
        add(botonRegresar);

        botonGraficar.addActionListener(e -> {
            String funcion = campoFuncion.getText().trim();
            try {
                double a = Double.parseDouble(campoA.getText().trim());
                double b = Double.parseDouble(campoB.getText().trim());
                new VentanaGraficaIntegral(funcion, a, b).setVisible(true);
            } catch (NumberFormatException ex) {
                etiquetaResultado.setText("a y b deben ser números válidos");
            }
        });
    }

    private void calcularIntegral() {
        String funcion = campoFuncion.getText().trim();
        try {
            double a = Double.parseDouble(campoA.getText().trim());
            double b = Double.parseDouble(campoB.getText().trim());

            Operacion integral = new Integral(funcion, a, b);
            double resultado = Double.parseDouble(integral.calcular());
            etiquetaResultado.setText(String.format("Resultado: %.4f", resultado));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "a y b deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ExcepcionMatematica ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// === Clases auxiliares integradas ===

class ExcepcionMatematica extends Exception {
    public ExcepcionMatematica(String mensaje) {
        super(mensaje);
    }
}

interface Operacion {
    String calcular() throws ExcepcionMatematica;
}

class Integral implements Operacion {
    private final String funcion;
    private final double a, b;
    private final MotorMatematico motor;

    public Integral(String funcion, double a, double b) {
        // Normaliza la función para compatibilidad con el parser
        this.funcion = normalizarFuncion(funcion);
        this.a = a;
        this.b = b;
        this.motor = new MotorMatematico();
    }

    private String normalizarFuncion(String funcion) {
        String f = funcion;
        f = f.replaceAll(" ", "");
        f = f.replaceAll("(?<=[0-9])x", "*x");
        f = f.replaceAll("([+-])x\\^([+-]?[0-9]+)", "$1 1*x^$2");
        f = f.replaceAll("([+-]?[0-9]+)x\\^([+-]?[0-9]+)", "$1*x^$2");
        f = f.replaceAll("([+-])x([^a-zA-Z0-9^]|$)", "$11*x$2");
        f = f.replaceAll("\\+\\+", "+");
        f = f.replaceAll("--", "+");
        f = f.replaceAll("-\\+", "-");
        f = f.replaceAll("\\+-", "-");
        return f;
    }

    @Override
    public String calcular() throws ExcepcionMatematica {
        int n = 1000;
        double h = (b - a) / n;
        double suma = 0.5 * (motor.evaluarFuncion(funcion, a) + motor.evaluarFuncion(funcion, b));
        for (int i = 1; i < n; i++) {
            suma += motor.evaluarFuncion(funcion, a + i * h);
        }
        return String.valueOf(suma * h);
    }
}

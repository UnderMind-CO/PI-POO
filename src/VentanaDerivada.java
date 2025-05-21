import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class VentanaDerivada extends JFrame {
    private JTextField campoFuncion;
    private JLabel etiquetaResultado;

    public VentanaDerivada(JFrame ventanaAnterior) {
        setTitle("Calculeme Esta - Derivada");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(new Color(15, 15, 15));

        JLabel etiqueta = new JLabel("Función f(x):");
        etiqueta.setBounds(50, 30, 300, 30);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(etiqueta);

        campoFuncion = new JTextField();
        campoFuncion.setBounds(50, 60, 300, 30);
        add(campoFuncion);

        JButton boton = new JButton("Calcular Derivada");
        boton.setBounds(50, 100, 300, 35);
        boton.setBackground(new Color(60, 90, 150));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        boton.addActionListener(e -> calcularDerivada());
        add(boton);

        etiquetaResultado = new JLabel("Resultado:");
        etiquetaResultado.setBounds(50, 150, 300, 30);
        add(etiquetaResultado);

        JButton botonGraficar = new JButton("Graficar");
        botonGraficar.setBounds(193, 200, 150, 30);
        add(botonGraficar);

        JButton botonRegresar = new JButton("Regresar");
        botonRegresar.setBounds(94, 200, 100, 30);
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
            if (funcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese una función válida", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Operacion derivada = new Derivada(funcion);
                String derivadaStr = derivada.calcular();
                new VentanaGraficaDerivada(derivadaStr).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo graficar la derivada: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void calcularDerivada() {
        String funcion = campoFuncion.getText().trim();
        if (funcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una función válida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Operacion derivada = new Derivada(funcion);
            String resultado = derivada.calcular();
            etiquetaResultado.setText("Resultado: " + resultado);
        } catch (ExcepcionMatematica ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método auxiliar para evaluar la función derivada en x
    private double evaluarFuncion(String funcion, double x) {
        try {
            String expr = funcion.replaceAll("x", "(" + x + ")");
            return new MotorMatematico().evaluarFuncion(expr, x);
        } catch (Exception e) {
            return Double.NaN;
        }
    }
}

// Clase Derivada queda aquí porque es específica de esta ventana
class Derivada implements Operacion {
    private final String funcion;

    public Derivada(String funcion) {
        this.funcion = funcion.replaceAll("\\s+", "");
    }

    @Override
    public String calcular() throws ExcepcionMatematica {
        // Normaliza la función para polinomios y trigonométricas
        String f = funcion.replaceAll(" ", "");
        // Trigonométricas simples
        if (f.equals("sin(x)")) return "cos(x)";
        if (f.equals("cos(x)")) return "-sin(x)";
        if (f.equals("tan(x)")) return "sec(x)^2";
        if (f.equals("-sin(x)")) return "-cos(x)";
        if (f.equals("-cos(x)")) return "sin(x)";
        if (f.equals("sin(x)^2")) return "2*sin(x)*cos(x)";
        if (f.equals("cos(x)^2")) return "-2*sin(x)*cos(x)";
        // Polinomios
        if (f.matches("^[+-]?\\d*\\.?\\d*x(\\^[-+]?\\d+)?([+-]\\d*\\.?\\d*x(\\^[-+]?\\d+)?)*([+-]\\d+)?$")) {
            String[] terminos = f.replace("-", "+-").split("\\+");
            StringBuilder resultado = new StringBuilder();
            for (String termino : terminos) {
                termino = termino.trim();
                if (termino.isEmpty()) continue;
                String derivado = derivarTermino(termino);
                if (!derivado.isEmpty()) {
                    if (resultado.length() > 0 && !derivado.startsWith("-")) resultado.append("+");
                    resultado.append(derivado);
                }
            }
            String res = resultado.toString();
            if (res.startsWith("+")) res = res.substring(1);
            return res.isEmpty() ? "0" : res;
        }
        // Constante
        if (f.matches("^[+-]?\\d*\\.?\\d+$")) return "0";
        return "Derivación simbólica solo implementada para polinomios y funciones trigonométricas básicas (sin(x), cos(x), tan(x), sin(x)^2, cos(x)^2)";
    }

    private String derivarTermino(String termino) {
        termino = termino.trim();
        if (termino.isEmpty()) return "";
        if (!termino.contains("x")) return ""; // Constante
        double coef = 1;
        int exp = 1;
        if (termino.equals("x")) {
            // coef y exp ya están en 1
        } else {
            int xIndex = termino.indexOf("x");
            String coefStr = termino.substring(0, xIndex);
            if (coefStr.equals("-") || coefStr.equals("+")) coef = coefStr.equals("-") ? -1 : 1;
            else if (!coefStr.isEmpty()) coef = Double.parseDouble(coefStr);
            else coef = 1;
            if (termino.contains("^")) {
                int expIndex = termino.indexOf("^");
                exp = Integer.parseInt(termino.substring(expIndex + 1));
            } else {
                exp = 1;
            }
        }
        double nuevoCoef = coef * exp;
        int nuevoExp = exp - 1;
        if (nuevoExp == 0) return String.valueOf(nuevoCoef);
        if (nuevoExp == 1) return (nuevoCoef == 1 ? "" : nuevoCoef == -1 ? "-" : String.valueOf(nuevoCoef)) + "x";
        return (nuevoCoef == 1 ? "" : nuevoCoef == -1 ? "-" : String.valueOf(nuevoCoef)) + "x^" + nuevoExp;
    }
}

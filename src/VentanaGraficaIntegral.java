import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.BasicStroke;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaGraficaIntegral extends JFrame {
    private String funcion;
    private double a, b;
    private JTextField campoXmin, campoXmax;
    private ChartPanel chartPanel;

    public VentanaGraficaIntegral(String funcion, double a, double b) {
        this.funcion = funcion;
        this.a = a;
        this.b = b;
        setTitle("Gráfica de la integral");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel labelRango = new JLabel("Rango X:");
        labelRango.setBounds(30, 20, 70, 30);
        add(labelRango);

        campoXmin = new JTextField(String.valueOf(a));
        campoXmin.setBounds(100, 20, 60, 30);
        add(campoXmin);
        JLabel labelA = new JLabel("a");
        labelA.setBounds(165, 20, 10, 30);
        add(labelA);
        campoXmax = new JTextField(String.valueOf(b));
        campoXmax.setBounds(180, 20, 60, 30);
        add(campoXmax);

        JButton botonActualizar = new JButton("Actualizar");
        botonActualizar.setBounds(260, 20, 120, 30);
        add(botonActualizar);

        chartPanel = new ChartPanel(null);
        chartPanel.setBounds(30, 70, 520, 360);
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(chartPanel);

        botonActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graficar();
            }
        });

        graficar();
    }

    private String normalizarFuncion(String funcion) {
        String f = funcion;
        f = f.replaceAll(" ", "");
        // Asegura que 3x se convierte en 3*x
        f = f.replaceAll("(?<=[0-9])x", "*x");
        // Asegura que -x^2 se convierte en -1*x^2
        f = f.replaceAll("([+-])x\\^([+-]?[0-9]+)", "$1 1*x^$2");
        // Asegura que 3x^2 se convierte en 3*x^2
        f = f.replaceAll("([+-]?[0-9]+)x\\^([+-]?[0-9]+)", "$1*x^$2");
        // Asegura que x^2 se mantiene como x^2
        // No elimines el ^ para que MotorMatematico lo procese correctamente
        f = f.replaceAll("([+-])x([^a-zA-Z0-9^]|$)", "$11*x$2");
        f = f.replaceAll("\\+\\+", "+");
        f = f.replaceAll("--", "+");
        f = f.replaceAll("-\\+", "-");
        f = f.replaceAll("\\+-", "-");
        return f;
    }

    private void graficar() {
        double xmin, xmax;
        try {
            xmin = Double.parseDouble(campoXmin.getText());
            xmax = Double.parseDouble(campoXmax.getText());
            if (xmin >= xmax) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Rango inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String funcionEval = normalizarFuncion(funcion.trim());
        XYSeries series = new XYSeries("Función");
        double step = (xmax - xmin) / 100.0;
        if (step == 0) step = 1;
        MotorMatematico motor = new MotorMatematico();
        for (double x = xmin; x <= xmax; x += step) {
            try {
                double y = motor.evaluarFuncion(funcionEval, x);
                if (!Double.isNaN(y) && !Double.isInfinite(y)) {
                    series.add(x, y);
                }
            } catch (Exception e) {
                // Si hay error en la evaluación, ignora ese punto
            }
        }
        if (series.getItemCount() == 1) {
            double x2 = xmax;
            try {
                double y2 = motor.evaluarFuncion(funcionEval, x2);
                if (!Double.isNaN(y2) && !Double.isInfinite(y2)) {
                    series.add(x2, y2);
                }
            } catch (Exception e) {}
        }
        if (series.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No se pudo graficar la función en el rango dado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Gráfica de la función",
            "x",
            "f(x)",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        chartPanel.setChart(chart);
    }

    private double evaluarFuncion(String funcion, double x) {
        try {
            String funcionNorm = normalizarFuncion(funcion);
            return new MotorMatematico().evaluarFuncion(funcionNorm, x);
        } catch (Exception e) {
            return Double.NaN;
        }
    }
}

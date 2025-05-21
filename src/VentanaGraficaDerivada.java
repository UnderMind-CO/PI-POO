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

public class VentanaGraficaDerivada extends JFrame {
    private String funcionDerivada;
    private JTextField campoXmin, campoXmax;
    private ChartPanel chartPanel;

    public VentanaGraficaDerivada(String funcionDerivada) {
        this.funcionDerivada = funcionDerivada;
        setTitle("Gráfica de la derivada");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel labelRango = new JLabel("Rango X:");
        labelRango.setBounds(30, 20, 70, 30);
        add(labelRango);

        campoXmin = new JTextField("-10");
        campoXmin.setBounds(100, 20, 60, 30);
        add(campoXmin);
        JLabel labelA = new JLabel("a");
        labelA.setBounds(165, 20, 10, 30);
        add(labelA);
        campoXmax = new JTextField("10");
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
        f = f.replaceAll("(?<=[0-9])x", "*x");
        f = f.replaceAll("([+-])x\\^([+-]?[0-9]+)", "$1 1*x^$2");
        f = f.replaceAll("([+-]?[0-9]+)x\\^([+-]?[0-9]+)", "$1*x^$2");
        f = f.replaceAll("([+-])x([^a-zA-Z0-9^]|$)", "$11*x$2");
        f = f.replaceAll("\\+\\+", "+");
        f = f.replaceAll("--", "+");
        f = f.replaceAll("-\\+", "-");
        f = f.replaceAll("\\+-", "-");
        // No elimines el ^ para que MotorMatematico lo procese correctamente
        return f;
    }

    private void graficar() {
        double xmin, xmax;
        try {
            xmin = Double.parseDouble(campoXmin.getText());
            xmax = Double.parseDouble(campoXmax.getText());
            if (xmin >= xmax) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Rango inválido", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        String funcionEval = normalizarFuncion(funcionDerivada);
        org.jfree.data.xy.XYSeries series = new org.jfree.data.xy.XYSeries("Derivada");
        double step = (xmax - xmin) / 100.0;
        if (step == 0) step = 1;
        MotorMatematico motor = new MotorMatematico();
        for (double x = xmin; x <= xmax; x += step) {
            try {
                double y = motor.evaluarFuncion(funcionEval, x);
                if (Double.isFinite(y)) series.add(x, y);
            } catch (Exception ex) {
                // No agregar puntos inválidos
            }
        }
        if (series.getItemCount() == 1) {
            double x2 = xmax;
            try {
                double y2 = motor.evaluarFuncion(funcionEval, x2);
                if (Double.isFinite(y2)) series.add(x2, y2);
            } catch (ExcepcionMatematica ex) {
                // No agregar punto si hay error de evaluación
            }
        }
        if (series.getItemCount() == 0) {
            series.add(xmin, 0);
            series.add(xmax, 0);
        }
        org.jfree.data.xy.XYSeriesCollection dataset = new org.jfree.data.xy.XYSeriesCollection();
        dataset.addSeries(series);
        org.jfree.chart.JFreeChart chart = org.jfree.chart.ChartFactory.createXYLineChart(
            "Gráfica de la derivada",
            "x",
            "f'(x)",
            dataset,
            org.jfree.chart.plot.PlotOrientation.VERTICAL,
            false, true, false
        );
        org.jfree.chart.plot.XYPlot plot = chart.getXYPlot();
        org.jfree.chart.renderer.xy.XYLineAndShapeRenderer renderer = new org.jfree.chart.renderer.xy.XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, java.awt.Color.BLUE);
        renderer.setSeriesStroke(0, new java.awt.BasicStroke(3.0f));
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        plot.setDomainGridlinePaint(java.awt.Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(java.awt.Color.LIGHT_GRAY);
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

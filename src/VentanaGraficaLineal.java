import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.plot.XYPlot;

public class VentanaGraficaLineal extends JFrame {
    private double a, b, c;
    private JTextField campoXmin, campoXmax;
    private ChartPanel chartPanel;

    public VentanaGraficaLineal(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
        setTitle("Gráfica de la ecuación lineal");
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
        XYSeries series = new XYSeries("Recta");
        double step = (xmax - xmin) / 100.0;
        if (step == 0) step = 1;
        // Si a==0, la función es constante (y = b), graficar línea horizontal
        if (a == 0) {
            for (double x = xmin; x <= xmax; x += step) {
                double y = b;
                if (!Double.isNaN(y) && !Double.isInfinite(y)) {
                    series.add(x, y);
                }
            }
        } else {
            for (double x = xmin; x <= xmax; x += step) {
                double y = a * x + b;
                if (!Double.isNaN(y) && !Double.isInfinite(y)) {
                    series.add(x, y);
                }
            }
        }
        // Si solo hay un punto, agrega el segundo extremo
        if (series.getItemCount() == 1) {
            double x2 = xmax;
            double y2 = a * x2 + b;
            if (!Double.isNaN(y2) && !Double.isInfinite(y2)) {
                series.add(x2, y2);
            }
        }
        // Si no hay puntos válidos, agrega dos puntos en y=0
        if (series.getItemCount() == 0) {
            series.add(xmin, 0);
            series.add(xmax, 0);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Gráfica de la ecuación lineal",
            "x",
            "y",
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
}

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
import org.jfree.chart.axis.NumberAxis;

public class VentanaGraficaLineal extends JFrame {
    private double a, b, c, solucion;
    private JTextField campoXmin, campoXmax;
    private ChartPanel chartPanel;
    private JLabel ecuacionLabel;

    public VentanaGraficaLineal(double a, double b, double c, double solucion) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.solucion = solucion;
        
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

        // Etiqueta para mostrar la ecuación original
        ecuacionLabel = new JLabel();
        ecuacionLabel.setBounds(400, 20, 180, 30);
        actualizarEcuacionLabel();
        add(ecuacionLabel);

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

    private void actualizarEcuacionLabel() {
        // Mostrar la ecuación en la forma original Ax + B = C
        ecuacionLabel.setText(String.format("%.1fx + %.1f = %.1f", a, b, c));
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
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        // 1. Graficar la ecuación Ax + B = C como y = (C - B - Ax)
        XYSeries ecuacionSeries = new XYSeries("Ax + B = C");
        double step = (xmax - xmin) / 100.0;
        
        for (double x = xmin; x <= xmax; x += step) {
            if (a == 0) {
                // Si A=0, entonces es una línea horizontal en y = C-B (si B=C) o no hay solución
                double y = b;
                ecuacionSeries.add(x, y);
            } else {
                // Para cualquier punto x, calculamos el valor de y para el cual Ax + B + y = C
                // Despejando: y = C - Ax - B
                double y = c - a * x - b;
                ecuacionSeries.add(x, y);
            }
        }
        dataset.addSeries(ecuacionSeries);
        
        // 2. Agregar una línea vertical en x = solución (si existe)
        if (!Double.isNaN(solucion) && !Double.isInfinite(solucion)) {
            // La línea vertical va desde el valor mínimo de y visible hasta 0
            XYSeries solucionSeries = new XYSeries("Solución x = " + String.format("%.2f", solucion));
            
            // Calculamos el valor y para el punto de solución
            double ySol = c - a * solucion - b;
            
            // Si el punto de solución está dentro del rango visible de x
            if (solucion >= xmin && solucion <= xmax) {
                // Agregamos la línea vertical (solo puntos en x=solucion, con varios valores y)
                double yMin = -30; // Un valor bajo que probablemente sea visible
                double yMax = 30;  // Un valor alto que probablemente sea visible
                
                // Agregar puntos para formar la línea vertical
                for (double y = yMin; y <= yMax; y += (yMax-yMin)/50) {
                    solucionSeries.add(solucion, y);
                }
                
                dataset.addSeries(solucionSeries);
            }
        }
        
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Gráfica de la ecuación lineal",
            "x",
            "y",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        
        // Configurar la recta de la ecuación
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesShapesVisible(0, false);
        
        // Configurar la línea vertical de solución
        if (dataset.getSeriesCount() > 1) {
            renderer.setSeriesPaint(1, Color.BLUE);
            renderer.setSeriesStroke(1, new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 
                                                     1.0f, new float[] {6.0f, 3.0f}, 0.0f));
            renderer.setSeriesShapesVisible(1, false);
        }
        
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        // Ajustar ejes para asegurar que se vea la solución
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setRange(xmin, xmax);
        
        // Determinar rango de y basado en los valores y de la ecuación en el rango visible de x
        double yMin = Double.MAX_VALUE;
        double yMax = -Double.MAX_VALUE;
        for (double x = xmin; x <= xmax; x += step) {
            double y = c - a * x - b;
            if (y < yMin) yMin = y;
            if (y > yMax) yMax = y;
        }
        
        // Añadir un margen al rango de y
        double yMargin = (yMax - yMin) * 0.1;
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setRange(yMin - yMargin, yMax + yMargin);
        
        chartPanel.setChart(chart);
    }
}
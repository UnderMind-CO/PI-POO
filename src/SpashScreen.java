import java.awt.Desktop;
import java.awt.Image;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;

public class SpashScreen extends JFrame {

    JEditorPane editor;
    URL urls[] = new URL[3];
    String capitulos[] = new String[3];
    JButton jbPri = new JButton("< Inicio");
    JButton jbAnt = new JButton("< Anterior");
    JButton jbSig = new JButton("Siguiente >");
    JButton jbUlt = new JButton("Final >");
    int index;    
    
    JButton jbContinuar = new JButton("Continuar a la aplicación");
    
    public SpashScreen(int i) {
        super("INFORMACIÓN IMPORTANTE");
        setSize(800, 590); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLayout(null);         
        setResizable(false);
        Image img = new ImageIcon(getClass().getResource("./img/calculator.png")).getImage();
        
        setIconImage(img);
        index = i-1;
        Capitulos();
        crearGUI();
        
        setVisible(true);
    }
    
    public void Capitulos() {        
        urls[0] = getClass().getResource("./img/calculator.png"); // imagen del cap 1
        capitulos[0] = "<div align='center'><font face='Arial' size='5' color='red'><b>Calc Esta</b></font></div><br>" +
                "<strong>Definición</strong><br><br>" +
                "<br>Calc-Esta es una aplicación desarrollada como proyecto integrador de la carrera de Ingeniería de Sistemas, que funciona como una calculadora que te permite resolver, funciones lineales de primer grado, Derivadas e integrales.<br> Su objetivo principal es ofrecer una herramienta sencilla, visual e intuitiva para ayudar tanto a estudiantes como a profesionales a resolver, entender y graficar este tipo de ecuaciones, fortaleciendo así el pensamiento lógico-matemático.<br><br>" +         
                "<div align='center'><img src=" + urls[0] + " width=200 height=200></div><br>" + 
                "+ info: <a href = 'https://limewire.com/d/Cj8np#l5MSmVmeSH'><b>Download The Documentation Here!</b></a><br>";
        
        urls[1] = getClass().getResource("./img/frustrado.png"); // imagen del cap 2
        capitulos[1] = "<div align='center'><font face='Arial' size='5' color='red'><b>¿Por qué se crea?</b></font></div><br>" +
                "<b>Se crea como respuesta a una problemática común entre estudiantes... </b><br><br>" +
                "muchas personas tienen dificultades para entender las ecuaciones lineales debido a la falta de representación visual clara y herramientas accesibles. Aunque existen programas como MATLAB, GeoGebra o Wolfram Alpha, suelen ser complejos o poco amigables para quienes apenas se están iniciando en el tema.<br>Su principal propicito es servir como apoyo pedagógico, haciendo que el aprendizaje sea más práctico y menos frustrante<br>" +         
                "<div align='center'><img src=" + urls[1] + "></div><br>";
        
        urls[2] = getClass().getResource("./img/splash.png"); // imagen del cap 3
        capitulos[2] = "<div align='center'><font face='Arial' size='5' color='red'><b>Funcionamiento</b></font></div><br>" +
                "<b>¿Como Funciona Calc-Esta?</b><br><br>" +
                "Esta aplicación en Java es una calculadora gráfica que permite resolver ecuaciones lineales, calcular derivadas e integrales definidas. Está hecha con la librería <b>Swing y Flatlaf</b>, por lo tanto, tiene una interfaz gráfica compuesta por campos de texto, etiquetas y botones. <br><br>" +         
                "La interfaz permite al usuario ingresar una función matemática en forma de texto <b>Funciones lineales, Derivadas e integrales</b>, así como dos valores numéricos que se utilizan dependiendo de la operación que se quiera realizar. Estos valores sirven como coeficientes de una ecuación lineal o como límites inferior y superior para una integral definida. Los botones disponibles en la interfaz ejecutan las operaciones respectivas:<br><br> resolver la ecuación, derivar la función o integrar la función en un intervalo.<br><br>"+
                "<div align='center'><img src=" + urls[2] + " width=400 height=400></div><br>" + 
                "🌐 <a href = 'https://github.com/UnderMind-CO/PI-POO'<b>Codigo Fuente...</b></a>";
    }
     
    public void crearGUI() {        
        editor = new JEditorPane();
        editor.setContentType("text/html");
        editor.setText(capitulos[index]);
        editor.setEditable(false);        
        editor.addHyperlinkListener((HyperlinkEvent e) -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException | URISyntaxException e1) { }
                }
            }
        });
        
        jbContinuar.setBounds(280, 490, 200, 30);
        jbContinuar.addActionListener((e) -> {
            dispose(); // Cierra esta ventana
            abrirAplicacionPrincipal(); // Abre la aplicación principal
        });
        add(jbContinuar);
    
        JScrollPane js = new JScrollPane(editor);
        js.setBounds(40, 30, 710, 400);
        add(js);
                
        jbPri.setBounds(40, 450, 150, 30);
        jbPri.addActionListener((e) -> firts());        
        add(jbPri);
        
        jbAnt.setBounds(200, 450, 150, 30);
        jbAnt.addActionListener((e) -> before());
        add(jbAnt);
                
        jbSig.setBounds(360, 450, 150, 30);
        jbSig.addActionListener((e) -> next());
        add(jbSig);
                
        jbUlt.setBounds(520, 450, 150, 30);
        jbUlt.addActionListener((e) -> skip_last());
        add(jbUlt);
    }
      
    private void firts() {
        index = 0;
        setCapitulo();
    }
        
    private void before() {        
        if(index - 1 >= 0){
            index--;
            setCapitulo();
        }  
    }

    private void next() {
        if(index + 1 < capitulos.length){
            index++;
            setCapitulo();
        }  
    }
    
    private void skip_last() {
        index = capitulos.length-1;
        setCapitulo();
    }
    
    public void setCapitulo(){
        editor.setText(capitulos[index]);
        editor.setCaretPosition(0);
    }
    
    private void abrirAplicacionPrincipal() {
        SwingUtilities.invokeLater(() -> {
            try {
                FlatOneDarkIJTheme.setup();
            } catch(Exception e) {
                System.out.println("Error al cargar la aplicación principal: " + e.getMessage());
            }
            new __Main__().setVisible(true);
        });
    }
}
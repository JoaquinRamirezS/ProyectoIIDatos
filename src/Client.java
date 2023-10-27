import javax.swing.*;
import org.opencv.core.Core;
//import org.opencv.features2d.FlannBasedMatcher;
import org.opencv.videoio.VideoCapture;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This class represents the main client application for a calculator.
 * It provides a graphical user interface for inputting expressions and viewing results.
 */
public class Client extends JFrame {
    Font customFont = new Font("Arial", Font.BOLD, 18);
    JTextField tfname, tfexpression;
    DataInputStream input;
    DataOutputStream output;
    Socket SocketClient;
    VideoCapture camera;
    JFrame cameraFrame; 
    private History historyFrame;
    
    /**
     * Initializes the client application and sets up the graphical user interface.
     */
    public void initialize() {
        //Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(0, 0, 0 ));
        mainPanel.setLayout(null);

        //Label Date
        JLabel lbdate = new JLabel("Date:");
        lbdate.setFont(customFont);
        lbdate.setForeground(Color.WHITE);
        lbdate.setBounds(50, 20, 100, 30);
        mainPanel.add(lbdate);
        
        //Date of the system
        Calendar date = new GregorianCalendar();
        String year = Integer.toString(date.get(Calendar.YEAR));
        String month = Integer.toString(date.get(Calendar.MONTH)+1);
        String day = Integer.toString(date.get(Calendar.DAY_OF_MONTH));
        String cdate = day + "/" + month + "/" + year;
        
        //Put the complete date on a label
        JLabel pdate = new JLabel(cdate);
        pdate.setForeground(Color.WHITE);
        pdate.setFont(customFont);
        pdate.setBounds(100, 20, 200, 30);
        mainPanel.add(pdate);

        //Label Name
        JLabel lbname = new JLabel("Name:");
        lbname.setFont(customFont);
        lbname.setForeground(Color.WHITE);
        lbname.setBounds(225, 20, 200, 30);
        mainPanel.add(lbname);

        //TextField for name
        tfname = new JTextField(20); 
        tfname.setFont(customFont);
        tfname.setForeground(Color.BLACK);
        tfname.setBackground(new Color (197, 198, 208));
        tfname.setBounds(285, 20, 115, 30);
        mainPanel.add(tfname);


        //TextField Expression
        tfexpression = new JTextField(20); 
        tfexpression.setFont(customFont);
        tfexpression.setForeground(Color.BLACK);
        tfexpression.setBackground(new Color (197, 198, 208));
        tfexpression.setBounds(45, 70, 355, 30);
        mainPanel.add(tfexpression);

        //TextArea to show the result of the expression
        JTextArea taresult = new JTextArea(10, 40); 
        taresult.setFont(customFont);
        taresult.setForeground(Color.BLACK);
        taresult.setBackground(new Color (197, 198, 208));
        taresult.setBounds(45, 75, 355, 90);
        mainPanel.add(taresult);

        
        //Button "Camera"
        ImageIcon icon = new ImageIcon ("images/camera.png");
        JButton bcamera = new JButton(icon);
        bcamera.setBackground(new Color(255,255,255));
        //bcamera.setOpaque(false);
        //bcamera.setContentAreaFilled(false);
        //bcamera.setBorderPainted(false);
        bcamera.setBounds(345, 240, 55, 50);
        mainPanel.add(bcamera);
        bcamera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Camera opencamera = new Camera();
                opencamera.setVisible(true);
            }
        });

        //Buttom "Result"
        JButton bresult = new JButton("=");
        bresult.setFont(customFont);
        bresult.setForeground(Color.BLACK);
        bresult.setBackground(new Color(255,255,255));
        bresult.setBounds(345, 300, 55, 170);
        mainPanel.add(bresult);
        bresult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the date, name, and expression values 
                String date = pdate.getText();
                String name = tfname.getText();
                String expresion = tfexpression.getText();

                 // Clean the expression
                expresion = expresion.replaceAll("\\s+", " ");  // Clean the expression
                expresion = expresion.trim();  // Remove leading and trailing spaces

        
                try {
                    Socket socket = new Socket("127.0.0.1", 1800);
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    DataInputStream input = new DataInputStream(socket.getInputStream());
        
                    // Send the expression to the server
                    output.writeUTF(date);
                    output.writeUTF(name);
                    output.writeUTF(expresion);
                    
                    System.out.println("Datos enviados al servidor.");
        
                    // Show the result from the server
                    double result = input.readDouble();
                    taresult.setText("\nResultado: " + result);


                    socket.close();
        
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Error al enviar datos al servidor: " + ex.getMessage());
                }
            }
        });        

        //Buttom "History"
        ImageIcon iconexp = new ImageIcon ("images/expand.png");
        JButton bexprest = new JButton(iconexp);
        bexprest.setOpaque(false);
        bexprest.setContentAreaFilled(false);
        bexprest.setBorderPainted(false);
        bexprest.setBounds(5, 60, 55, 50);
        mainPanel.add(bexprest);
        bexprest.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                if (historyFrame == null) {
                    historyFrame = new History();
                }
                historyFrame.setVisible(true);
                historyFrame.setLocation(100, 100);
                String dateEntry = "Fecha: " + pdate.getText();
                String expressionEntry = "Expresión: " + tfexpression.getText()+"\n";
                String resultEntry = "Resultado: " + taresult.getText() + "\n";
                
                historyFrame.addToHistory(dateEntry);//Add date to History
                historyFrame.addToHistory(expressionEntry);//Add expression to history
                historyFrame.addToHistory(resultEntry);//   Add the result
            }
        });

        //Buttom "clean"
        JButton bclean = new JButton("C");
        bclean.setFont(customFont);
        bclean.setForeground(Color.BLACK);
        bclean.setBackground(new Color(255,255,255));
        bclean.setBounds(225, 240, 115, 50);
        mainPanel.add(bclean);
        bclean.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                tfexpression.setText(""); //Clean the expresion
                taresult.setText("");//Clean the result
            }
        });

        //Buttom "zero"
        JButton bzero = new JButton("0");
        bzero.setFont(customFont);
        bzero.setForeground(Color.BLACK);
        bzero.setBackground(new Color(255,255,255));
        bzero.setBounds(45, 420, 175,50);
        mainPanel.add(bzero);
        bzero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "0";
                tfexpression.setText(newText);
            }
        });
        
        //Buttom "one"
        JButton bone = new JButton("1");
        bone.setFont(customFont);
        bone.setForeground(Color.BLACK);
        bone.setBackground(new Color(255,255,255));
        bone.setBounds(45, 360, 55, 50);
        mainPanel.add(bone);
        bone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "1";
                tfexpression.setText(newText);
            }
        });

        //Buttom "two"
        JButton btwo = new JButton("2");
        btwo.setFont(customFont);
        btwo.setForeground(Color.BLACK);
        btwo.setBackground(new Color(255,255,255));
        btwo.setBounds(105, 360, 55, 50);
        mainPanel.add(btwo);
        btwo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "2";
                tfexpression.setText(newText);
            }
        });

        //Buttom "three"
        JButton bthree = new JButton("3");
        bthree.setFont(customFont);
        bthree.setForeground(Color.BLACK);
        bthree.setBackground(new Color(255,255,255));
        bthree.setBounds(165, 360, 55, 50);
        mainPanel.add(bthree);
        bthree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "3";
                tfexpression.setText(newText);
            }
        });
        
        //Buttom "four"
        JButton bfour = new JButton("4");
        bfour.setFont(customFont);
        bfour.setForeground(Color.BLACK);
        bfour.setBackground(new Color(255,255,255));
        bfour.setBounds(45, 300, 55, 50);
        mainPanel.add(bfour);
        bfour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "4";
                tfexpression.setText(newText);
            }
        });

        //Buttom "five"
        JButton bfive = new JButton("5");
        bfive.setFont(customFont);
        bfive.setForeground(Color.BLACK);
        bfive.setBackground(new Color(255,255,255));
        bfive.setBounds(105, 300, 55, 50);
        mainPanel.add(bfive);
        bfive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "5";
                tfexpression.setText(newText);
            }
        });

        //Buttom "six"
        JButton bsix = new JButton("6");
        bsix.setFont(customFont);
        bsix.setForeground(Color.BLACK);
        bsix.setBackground(new Color(255,255,255));
        bsix.setBounds(165, 300, 55, 50);
        mainPanel.add(bsix);
        bsix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "6";
                tfexpression.setText(newText);
            }
        });

        //Buttom "seven"
        JButton bseven = new JButton("7");
        bseven.setFont(customFont);
        bseven.setForeground(Color.BLACK);
        bseven.setBackground(new Color(255,255,255));
        bseven.setBounds(45, 240, 55, 50);
        mainPanel.add(bseven);
        bseven.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "7";
                tfexpression.setText(newText);
            }
        });

        //Buttom "eight"
        JButton beight = new JButton("8");
        beight.setFont(customFont);
        beight.setForeground(Color.BLACK);
        beight.setBackground(new Color(255,255,255));
        beight.setBounds(105, 240, 55, 50);
        mainPanel.add(beight);
        beight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "8";
                tfexpression.setText(newText);
            }
        });

        //Buttom "nine"
        JButton bnine = new JButton("9");
        bnine.setFont(customFont);
        bnine.setForeground(Color.BLACK);
        bnine.setBackground(new Color(255,255,255));
        bnine.setBounds(165, 240, 55, 50);
        mainPanel.add(bnine);
        bnine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "9";
                tfexpression.setText(newText);
            }
        });

        //Buttom "Plus"
        JButton bplus = new JButton("+");
        bplus.setFont(customFont);
        bplus.setForeground(Color.BLACK);
        bplus.setBackground(new Color(255,255,255));
        bplus.setBounds(225, 420, 55, 50);
        mainPanel.add(bplus);
        bplus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "+";
                tfexpression.setText(newText);
            }
        });

        //Buttom "Minus"
        JButton bminus = new JButton("-");
        bminus.setFont(customFont);
        bminus.setForeground(Color.BLACK);
        bminus.setBackground(new Color(255,255,255));
        bminus.setBounds(285, 420, 55, 50);
        mainPanel.add(bminus);
        bminus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "-";
                tfexpression.setText(newText);
            }
        });

        //Buttom "Multiplication"
        JButton bmultp = new JButton("*");
        bmultp.setFont(customFont);
        bmultp.setForeground(Color.BLACK);
        bmultp.setBackground(new Color(255,255,255));
        bmultp.setBounds(285, 300, 55, 50);
        mainPanel.add(bmultp);
        bmultp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "*";
                tfexpression.setText(newText);
            }
        });

        //Buttom "Division"
        JButton bdiv = new JButton("/");
        bdiv.setFont(customFont);
        bdiv.setForeground(Color.BLACK);
        bdiv.setBackground(new Color(255,255,255));
        bdiv.setBounds(225, 300, 55, 50);
        mainPanel.add(bdiv);
        bdiv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "/";
                tfexpression.setText(newText);
            }
        });

        //Buttom "Percent"
        JButton bperc = new JButton("%");
        bperc.setFont(customFont);
        bperc.setForeground(Color.BLACK);
        bperc.setBackground(new Color(255,255,255));
        bperc.setBounds(285, 360, 55, 50);
        mainPanel.add(bperc);
        bperc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "%";
                tfexpression.setText(newText);
            }
        });

        //Buttom "Exponentiation"
        JButton bexp = new JButton("**");
        bexp.setFont(customFont);
        bexp.setForeground(Color.BLACK);
        bexp.setBackground(new Color(255,255,255));
        bexp.setBounds(225, 360, 55, 50);
        mainPanel.add(bexp);
        bexp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "e";
                tfexpression.setText(newText);
            }
        });

        //Buttom "Vertical Bar(OR)"
        JButton bor = new JButton("|");
        bor.setFont(customFont);
        bor.setForeground(Color.BLACK);
        bor.setBackground(new Color(255,255,255));
        bor.setBounds(45, 180, 55, 50);
        mainPanel.add(bor);
        bor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "|";
                tfexpression.setText(newText);
            }
        });

        //Buttom "Caret(XOR)"
        JButton bxor = new JButton("^");
        bxor.setFont(customFont);
        bxor.setForeground(Color.BLACK);
        bxor.setBackground(new Color(255,255,255));
        bxor.setBounds(105, 180, 55, 50);
        mainPanel.add(bxor);
        bxor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "^";
                tfexpression.setText(newText);
            }
        });

        //Buttom "Open Parenthesis"
        JButton bopar = new JButton("(");
        bopar.setFont(customFont);
        bopar.setForeground(Color.BLACK);
        bopar.setBackground(new Color(255,255,255));
        bopar.setBounds(165, 180, 55, 50);
        mainPanel.add(bopar);
        bopar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "(";
                tfexpression.setText(newText);
            }
        });

        //Buttom "Close Parenthesis"
        JButton bcpar = new JButton(")");
        bcpar.setFont(customFont);
        bcpar.setForeground(Color.BLACK);
        bcpar.setBackground(new Color(255,255,255));
        bcpar.setBounds(225, 180, 55, 50);
        mainPanel.add(bcpar);
        bcpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + ")";
                tfexpression.setText(newText);
            }
        });

        //Buttom "Ampersand(AND)"
        JButton band = new JButton("&");
        band.setFont(customFont);
        band.setForeground(Color.BLACK);
        band.setBackground(new Color(255,255,255));
        band.setBounds(285, 180, 55, 50);
        mainPanel.add(band);
        band.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "&";
                tfexpression.setText(newText);
            }
        });

        //Buttom "Tilde(NOT)"
        JButton bnot = new JButton("~");
        bnot.setFont(customFont);
        bnot.setForeground(Color.BLACK);
        bnot.setBackground(new Color(255,255,255));
        bnot.setBounds(345, 180, 55, 50);
        mainPanel.add(bnot);
        bnot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = tfexpression.getText();
                String newText = currentText + "~";
                tfexpression.setText(newText);
            }
        });

        add(mainPanel);

        // Define the window
        setTitle("Calculat");
        setSize(450, 550);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Client frame = new Client(); 
        frame.initialize();
    }
}
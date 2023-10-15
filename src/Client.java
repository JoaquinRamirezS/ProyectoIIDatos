import javax.swing.*;
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


public class Client extends JFrame {
    Font customFont = new Font("Arial", Font.BOLD, 18);
    JTextField tfname, tfexpression;
    DataInputStream input;
    DataOutputStream output;
    Socket SocketClient;

    public void initialize() {
        //Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(80, 153, 193 ));
        mainPanel.setLayout(null);

        //Label Date
        JLabel lbdate = new JLabel("Fecha: ");
        lbdate.setFont(customFont);
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
        pdate.setFont(customFont);
        pdate.setBounds(120, 20, 200, 30);
        mainPanel.add(pdate);

        //Label Name
        JLabel lbname = new JLabel("Ingrese su nombre");
        lbname.setFont(customFont);
        lbname.setBounds(50, 70, 200, 30);
        mainPanel.add(lbname);

        //TextField for name
        tfname = new JTextField(20); 
        tfname.setFont(customFont);
        tfname.setBackground(new Color (197, 198, 208));
        tfname.setBounds(250, 70, 200, 30);
        mainPanel.add(tfname);

        //Label for Expression
        JLabel lbexpression = new JLabel("Ingrese una expresion:");
        lbexpression.setFont(customFont);
        lbexpression.setBounds(50, 120, 300, 30);
        mainPanel.add(lbexpression);

        //TextField Expression
        tfexpression = new JTextField(20); 
        tfexpression.setFont(customFont);
        tfexpression.setBackground(new Color (197, 198, 208));
        tfexpression.setBounds(50, 150, 400, 30);
        mainPanel.add(tfexpression);

        //TextArea to show the result of the expression
        JTextArea taresult = new JTextArea(10, 40); 
        taresult.setFont(customFont);
        taresult.setBackground(new Color (197, 198, 208));
        taresult.setBounds(50, 190, 400, 200);
        mainPanel.add(taresult);

        //Buttom "Aceptar"
        JButton bresult = new JButton("Aceptar");
        bresult.setFont(customFont);
        bresult.setBounds(90, 400, 150, 40);
        mainPanel.add(bresult);
        bresult.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                //Se obtienen los valores ingresados
                String name = tfname.getText();
                String expresion = tfexpression.getText();

                try {
                    Socket socket = new Socket("127.0.0.1", 1800);
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    DataInputStream input = new DataInputStream(socket.getInputStream());
        
                    //Send all the dates to the server
                    output.writeUTF(name);
                    output.writeUTF(expresion);

                    System.out.println("Datos enviados al servidor.");

                    //Show the result from de server
                    String result = input.readUTF();
                    taresult.setText("\nResultado: " + result);

                    socket.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Error al enviar datos al servidor: " + ex.getMessage());
                }
            }
        });

        //Buttom "Limpiar"
        JButton bclean = new JButton("Limpiar");
        bclean.setFont(customFont);
        bclean.setBounds(250, 400, 150, 40);
        mainPanel.add(bclean);
        bclean.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                tfexpression.setText("");
                taresult.setText("");
            }
        });

        add(mainPanel);
        
        // Define the window
        setTitle("Calculator");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public static void main(String[] args) {
        Client frame = new Client(); 
        frame.initialize();
      
    }
}

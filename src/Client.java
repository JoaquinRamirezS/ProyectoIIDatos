import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;


public class Client extends JFrame {
    Font customFont = new Font("Arial", Font.BOLD, 18);
    JTextField tfname, tfexpression;

    public void initialize() {
        //Label Name
        JLabel lbname = new JLabel("Ingrese su nombre");
        lbname.setFont(customFont);

        //TextField for name 
        tfname = new JTextField(20); 
        tfname.setFont(customFont);
        tfname.setBackground(new Color (197, 198, 208));

        //Label for Expression
        JLabel lbexpression = new JLabel("Ingrese la expresion que desee resolver:");
        lbexpression.setFont(customFont);

        //TextField Expression
        tfexpression = new JTextField(20); 
        tfexpression.setFont(customFont);
        tfexpression.setBackground(new Color (197, 198, 208));

        //TextArea to show the result of the expression
        JTextArea taresult = new JTextArea(10, 20); 
        taresult.setFont(customFont);
        taresult.setBackground(new Color (197, 198, 208));

        //Button Acept
        JButton bresult = new JButton();
        bresult.setFont(customFont);
        bresult.setText("Aceptar");
        bresult.setPreferredSize(new Dimension (150, 40));
        bresult.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                String resultexp = tfexpression.getText();
                taresult.setText("Resultado: " + resultexp);
            }
        });

        //Buttom Clean
        JButton bclean = new JButton();
        bclean.setFont(customFont);
        bclean.setText("Limpiar");
        bclean.setPreferredSize(new Dimension (150, 40));
        bclean.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                tfexpression.setText("");
                taresult.setText("");
            }
        });

        //Buttoms position
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(bresult);  
        buttonPanel.add(bclean); 

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(true);
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(80, 153, 193));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(lbname, gbc);
        gbc.gridy = 1;
        mainPanel.add(tfname, gbc);
        gbc.gridy = 2;
        mainPanel.add(lbexpression, gbc);
        gbc.gridy = 3;
        mainPanel.add(tfexpression, gbc);
        gbc.gridy = 4;
        mainPanel.add(taresult, gbc);
        gbc.gridy=5;
        mainPanel.add(buttonPanel, gbc); 

        add(mainPanel);

        //Define the window
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

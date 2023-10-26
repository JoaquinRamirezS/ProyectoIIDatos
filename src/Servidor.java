import javax.swing.*;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Servidor extends JFrame {
    Font customFont = new Font("Arial", Font.BOLD, 13);
    private JTextArea serverTextArea;

    public Servidor() {
        serverTextArea = new JTextArea(10, 40);
        serverTextArea.setEditable(false);
        serverTextArea.setFont(customFont);
        add(serverTextArea);

        JScrollPane scrollPane = new JScrollPane(serverTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);

        setTitle("Servidor");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public void logMessage(String message) {
        serverTextArea.append(message + "\n");
    }

    public double evaluateExpression(String expression) {
        List<String> postfixExpression = ExpressionTree.infixToPostfix(expression);
        return ExpressionTree.evaluatePostfix(postfixExpression);
    }

    public static void main(String[] args) {
        Servidor server = new Servidor();
        server.logMessage("Servidor esperando conexiones en el puerto 1800...\n");

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(1800);
            while (true) {

                Socket socket = serverSocket.accept();
                server.logMessage("Cliente conectado desde: " + socket.getInetAddress());

                // Initialize a thread for each client (Each client is handled in a separate thread to allow the server to serve multiple clients simultaneously)
                Thread clientThread = new Thread(new clientHandler(socket, server));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

//Manages the interactions of the client with the server
class clientHandler implements Runnable {
    private Socket socket;
    private Servidor server;


    public clientHandler(Socket socket, Servidor server) {
        this.socket = socket;
        this.server = server;

    }

    @Override
    public void run() {
        DataInputStream input = null;
        DataOutputStream output = null;

        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            // Receive the data from the client
            String date = input.readUTF();
            String name = input.readUTF();
            String expression = input.readUTF();

            //Show the data on the server
            server.logMessage("Datos recibidos del cliente:");
            server.logMessage("Fecha:" + date);
            server.logMessage("Nombre: " + name);
            server.logMessage("Expresión: " + expression);
            
            // Pending
            double result = server.evaluateExpression(expression);
            server.logMessage("Resultado: " + result + "\n");

            // Send the result to the client
             output.writeDouble(result);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) input.close();
                if (output != null) output.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

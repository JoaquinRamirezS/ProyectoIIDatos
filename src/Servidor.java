import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends JFrame {
    private JTextArea textArea;

    public Servidor() {
        setTitle("Servidor");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void startServer() {
        int puerto = 1800;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(puerto);
            textArea.append("El servidor está escuchando en el puerto " + puerto + "\n");

            while (true) {
                Socket socketCliente = serverSocket.accept();
                textArea.append("Cliente conectado desde " + socketCliente.getInetAddress() + "\n");

                DataInputStream input = new DataInputStream(socketCliente.getInputStream());
                DataOutputStream output = new DataOutputStream(socketCliente.getOutputStream());

                String nombre = input.readUTF();
                String expresion = input.readUTF();

                textArea.append("Nombre: " + nombre + "\n");
                textArea.append("Expresión: " + expresion + "\n");

                String resultado =  expresion;
                output.writeUTF(resultado);

                socketCliente.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Servidor server = new Servidor();
            server.setVisible(true);
            server.startServer();
        });
    }
}

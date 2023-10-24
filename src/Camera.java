import javax.swing.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;


public class Camera extends JFrame {
    private VideoCapture camera;
    private CameraPanel cameraPanel;
    private JTextField tfexpression;

    public Camera() {
       System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        setTitle("Camera");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false); 

        cameraPanel = new CameraPanel();
        cameraPanel.setPreferredSize(new Dimension(500, 500)); // Ajusta el tamaño del panel
        add(cameraPanel, BorderLayout.CENTER);

        JButton acceptButton = new JButton("Aceptar");
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Agrega la lógica para el botón "Aceptar" aquí
                recognizeTextAndSetExpression();
            }
        });
        add(acceptButton, BorderLayout.SOUTH);

        pack(); // Ajusta automáticamente el tamaño del frame
        setVisible(true);


        //Inicialize the camera
        camera = new VideoCapture(0);

        if (camera.isOpened()) {
            new CameraThread().start();
        }
    }

    private class CameraThread extends Thread {
        @Override
        public void run() {
            Mat frame = new Mat();
            while (true) {
                //Capture the frames and show the video in the panel
                camera.read(frame);
                cameraPanel.updateImage(frame);
                cameraPanel.repaint();
            }
        }
    }


    private void recognizeTextAndSetExpression() {
        Mat frame = new Mat();
        camera.read(frame);
        
        try {
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("lib/tess4j.jar"); // Reemplaza con la ubicación de tus datos de entrenamiento de Tesseract
            String recognizedText = tesseract.doOCR(matToBufferedImage(frame));
            tfexpression.setText(recognizedText);
        } catch (TesseractException e) {
            System.err.println("Error al reconocer el texto: " + e.getMessage());
        }
    }



    private File matToBufferedImage(Mat frame) {
                    return null;
}


public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        SwingUtilities.invokeLater(() -> new Camera());

    }
}

class CameraPanel extends JPanel {
    private BufferedImage image; // Almacena la imagen a mostrar en el panel

    public void updateImage(Mat mat) {
        image = matToBufferedImage(mat); // Actualiza la imagen del panel a partir de un objeto Mat
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this); // Dibuja la imagen en el panel en las coordenadas (0, 0)
        }
    }

    public static BufferedImage matToBufferedImage(Mat mat) {
        // Determina el tipo de imagen adecuado (escala de grises o color)
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        // Calcula el tamaño del búfer necesario para almacenar los datos de la imagen
        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] b = new byte[bufferSize];

        // Obtiene los datos de la imagen desde el objeto Mat
        mat.get(0, 0, b);

        // Crea un objeto BufferedImage con el tamaño y tipo adecuados
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);

        // Copia los datos de la imagen desde el búfer al BufferedImage
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);

        return image; // Devuelve el BufferedImage listo para su visualización
    }
}
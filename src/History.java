import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.Font;


/**
 * Represents a window for displaying a history of entries.
 */
public class History extends JFrame {
    Font customFont = new Font("Arial", Font.BOLD, 13);
    private JTextArea historyTextArea;

    /**
     * Constructs a new History window with a JTextArea to display entries.
     */
    public History() {
        //TextArea History Window
        historyTextArea = new JTextArea(10, 40);
        historyTextArea.setEditable(false);
        historyTextArea.setFont(customFont);
        historyTextArea.setBackground(new Color(255,255,255));
        add(historyTextArea);
        
        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);

        //Customize window
        setTitle("History");
        setSize(250, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   
    }
    /**
     * Appends a new entry to the history window.
     *
     * @param entry The entry to be added to the history.
     */  
    public void addToHistory(String entry) {
        historyTextArea.append(entry + "\n");
    }
    public static void main(String[] args) {
        History frame = new History();
        frame.setVisible(true);
    }
}
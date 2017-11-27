package quiztime;

import Client.Client;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUIStart {
    JFrame frame;
    Client client;
    //En metod som startar upp JFrame mm.
    public GUIStart() {
        frame = new JFrame("QuizTime");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.WHITE);
        start();
        frame.setSize(600, 600);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    //Startar upp GUI:t för startsidan på spelet.
    public void start() {
            JPanel main = new JPanel();
                main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
                main.setBackground(Color.WHITE);
            JPanel p0 = new JPanel();
                p0.setBackground(Color.GRAY);
                JLabel question = new JLabel("QuizTime");
                    question.setHorizontalAlignment(JLabel.CENTER);
            JPanel p00 = new JPanel();
                p00.setBackground(Color.GRAY);
                JLabel top = new JLabel("Välkommen till QuizTime!");
                    top.setHorizontalAlignment(JLabel.CENTER);
            JPanel p000 = new JPanel();
                p000.setBackground(Color.GRAY);
                JLabel empty = new JLabel();
                    empty.setHorizontalAlignment(JLabel.CENTER);
        JPanel p0000 = new JPanel();
            p0000.setBackground(Color.GRAY);
                JButton spela = new JButton("SPELA");
                    spela.setHorizontalAlignment(JLabel.CENTER);
        p0.add(question);
        p00.add(top);
        p0000.add(spela);
        main.add(p0);
        main.add(p00);
        main.add(p0000);
        frame.add(main);
        //Actionlistener som ser till att det sker ngt när man tycker på knappen i GUI:t.
        //Detta gör det möjligt för användarna att starta spelet.
        spela.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == spela) {
                    if(client == null) {
                        client = new Client(frame);
                        client.start();
                    }
                }
            }
        });
    }
    //Startar hela start GUI:t
    public static void main(String[] args) {
        GUIStart start = new GUIStart();
    }
}

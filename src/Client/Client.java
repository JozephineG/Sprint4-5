
package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Client extends Thread {
    String hostName = "127.0.0.1";
    int port = 56489;
    String name;
    String answer;
    JFrame frame;
    //Sätter upp en JFrame 
    public Client(JFrame frame){
        this.frame = frame;
    }

    // överskriver metoden run() från Thread
    @Override
    public void run(){
        //Öppnar upp socketen och därmed strömmen från clientsidan med det specifika portnummret som är definerat ovanför 
        try{
            Socket socket = new Socket(hostName, port);    
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        
            String fromServer;
            String fromPlayer;
            //Deklaration av svaren som "tomma" stringvariabler.
            String question = "";
            String answerOne = "";
            String answerTwo = "";
            String answerThree = "";
            String answerFour = "";
            boolean afterQuestion = false;
            int count = 0;
            
            while((fromServer = in.readLine()) != null){
                // första anropet berättar för klienten vilket id den har fått av servern
                if(fromServer.contains("Player:")) {
                    name = fromServer.split(":")[1];
                }
                
                // resultatet från servern
                if(fromServer.contains("Resultat:")) {
                    JOptionPane.showMessageDialog(null, fromServer);
                }
                
                // om den har ett frågetecken är det en fråga och därefter följer 4 svarsalternativ
                if(fromServer.contains("?")) {
                    afterQuestion = true;
                    question = fromServer;
                    continue;
                }
                if(afterQuestion && count == 0) {
                    answerOne = fromServer;
                    count++;
                } else if(afterQuestion && count == 1) {
                    answerTwo = fromServer;
                    count++;
                } else if(afterQuestion && count == 2) {
                    answerThree = fromServer;
                    count++;
                } else if(afterQuestion && count == 3) {
                    answerFour = fromServer;
                    count++;
                    // här anropar vi game som byter till spelvyn och visar upp frågan
                    game(question, answerOne, answerTwo, answerThree, answerFour, out);
                } else {
                    afterQuestion = false;
                }
            }
        
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    } //GUI:t Inbakad i en metod, där svaren är inmatade som inputparametrar(String-datatyper).
    public void game(String q, String answerOne, String answerTwo, String answerThree, String answerFour, PrintWriter out) {
        // tar bort den tidigare vyn
        frame.getContentPane().removeAll();
        //GUI:t som skapar användargränssnittet för användaren.
        JPanel main = new JPanel();
            main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
            JPanel p0 = new JPanel();
                p0.setLayout(new GridLayout(2, 1));
                    JLabel question = new JLabel("Question");
                        question.setHorizontalAlignment(JLabel.CENTER);
                        question.setFont(new Font("Serif", Font.BOLD, 18));
                        p0.setBackground(Color.GRAY);
            JPanel p00 = new JPanel();
                p00.setLayout(new GridLayout(2, 2));
                p00.setBackground(Color.GRAY);
                    JLabel text = new JLabel(q);
                        text.setHorizontalAlignment(JLabel.CENTER);
                        text.setBackground(Color.GRAY);
            JPanel p000 = new JPanel();
                p000.setBackground(Color.GRAY);
                    JButton buttonOne = new JButton(answerOne);
                        buttonOne.setHorizontalAlignment(JLabel.RIGHT);
                        buttonOne.setBackground(Color.GRAY);
                    JButton buttonTwo = new JButton(answerTwo);
                        buttonTwo.setHorizontalAlignment(JLabel.LEFT);
                        buttonTwo.setBackground(Color.GRAY);
            JPanel p0000 = new JPanel();
                p0000.setBackground(Color.GRAY);
                    JButton buttonThree = new JButton(answerThree);
                        buttonThree.setHorizontalAlignment(JLabel.RIGHT);
                        buttonThree.setBackground(Color.GRAY);
                    JButton buttonFour = new JButton(answerFour);
                        buttonFour.setHorizontalAlignment(JLabel.LEFT);
                        buttonFour.setBackground(Color.GRAY);
                        buttonFour.setSize(60,60);

                p0.add(question);
                p00.add(text);
                p000.add(buttonOne);
                p000.add(buttonTwo);
                p0000.add(buttonThree);
                p0000.add(buttonFour);
                main.add(p0);
                main.add(p00);
                main.add(p000);
                main.add(p0000);
                frame.add(main);
                //Actionlistener som ser till att det sker ngt när man tycker på knappen i GUI:t.
                //Detta gör det möjligt för användarna att svara på frågan. Denna lyssnare skickar svaret till servern.
        ActionListener listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                answer = ((JButton)e.getSource()).getText();
                // skickar även med klientens namn så att servern vet vem som svarade vad.
                out.println(name + ":" + answer);
            }
        };
        buttonOne.addActionListener(listener);
        buttonTwo.addActionListener(listener);
        buttonThree.addActionListener(listener);
        buttonFour.addActionListener(listener);
        
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
    }
}

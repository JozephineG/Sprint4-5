
package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //Lyssnar på connections från användare från clienten och startar i sådant fall ett spel och tilldelar spelare antingen "Spelare ett" eller "Spelare two". 
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(56489);
        try {
            Game.Player waiting = null;
            Game game = new Game();
            while (true) {
                if(waiting == null) {
                    waiting = game.new Player(listener.accept(), "one");
                } else {
                    Game.Player playerTwo = game.new Player(listener.accept(), "two");
                    waiting.setOpponent(playerTwo);
                    playerTwo.setOpponent(waiting);
                    waiting.start();
                    playerTwo.start(); 
                }
            }
        } finally {
            listener.close();
        }
    }
}
class Game {
    Database database;
    //Startar en databas.
    public Game() {
        database = new Database();
    }
    //Öppnar upp thread gentemot spelare, från server-sidan.
    class Player extends Thread {
        Player opponent;
        Socket socket;
        BufferedReader input;
        PrintWriter output;
        String name;
        String answer;
        boolean answered;
   
        public Player(Socket socket, String name) {
            this.socket = socket;
            this.name = name;
            try {
                input = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
            }
        }
        //En metod som sätter motståndare.
        public void setOpponent(Player opponent) {
            this.opponent = opponent;
        }
        //Metoden som skickar ut fråga och svar samt beräknar resultatet och skickar ut, via threads, svaret till användaren. 
        public void run() {
            try {
                String correctAnswer = "";
                output.println("Player:" + name);
                output.println(database.getQuestion().getQuestion());
                for(Database.Answer answer : database.getQuestion().answers) {
                    if(answer.correct) {
                        correctAnswer = answer.answer;
                    }
                    output.println(answer.answer);
                }

                while (true) {
                    String command = input.readLine();
                    if(command.contains(name + ":")) {
                        answered = true;
                        answer = command.split(":")[1];
                    }
                    if (answered && opponent.answered) {
                        String[] info = command.split(":");
                        if (answer.equals(correctAnswer) && opponent.answer.equals(correctAnswer)) {
                            output.println("Resultat: LIKA");
                            opponent.output.println("Resultat: LIKA");
                        } else if(answer.equals(correctAnswer) && !opponent.answer.equals(correctAnswer)) {
                            output.println("Resultat: DU VANN");
                            opponent.output.println("Resultat: DU FÖRLORADE");
                        } else if(!answer.equals(correctAnswer) && opponent.answer.equals(correctAnswer)) {
                            output.println("Resultat: DU FÖRLORADE");
                            opponent.output.println("Resultat: DU VANN");
                        } else {
                            output.println("Resultat: DU FÖRLORADE");
                            opponent.output.println("Resultat: DU FÖRLORADE");
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }
    }
}
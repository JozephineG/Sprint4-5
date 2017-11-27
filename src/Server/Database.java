
package Server;

import java.util.List;
import java.util.ArrayList;

public class Database {
    //Lista med frågorna
    List<Question> mainQuestions = new ArrayList<>();
    //Lägger till frågorna
    public Database(){
        mainQuestions.add(new Question("Vilket är året?", new Answer("1536", false), new Answer("1963", false), new Answer("2018", false), new Answer("2017", true)));
    }
    
    // Frågan samt en lista med svaren på frågan.
    public class Question{
        String question;
        List<Answer> answers = new ArrayList<>();
        //Lägger till frågan och svaren
        public Question(String question, Answer one, Answer two, Answer three, Answer four){
            this.question = question;
            this.answers.add(one);
            this.answers.add(two);
            this.answers.add(three);
            this.answers.add(four);
        }
        // En metod som "gettar" frågan
        public String getQuestion(){
            return question;
        }
    } 
    // Representerar ett svar och om det är korrekt blir boolean true  
    public class Answer{
        String answer;
        boolean correct;
        
        public Answer(String answer, boolean correct){
        this.answer = answer;
        this.correct = correct;
    }
    }// En metod som "gettar" frågan i position 0.(Första positionen)
    public Question getQuestion(){
         return mainQuestions.get(0);
    }
}


package university;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Student extends Application {

    private HandlerClassInst handler = new HandlerClassInst();
    
    private student_ID  instR = new student_ID();
    
    private Student_SignUp instS = new Student_SignUp();
    
    private Button loginbtn , signbtn , back;
    
    private Scene scene;
    
    @Override
    public void start(Stage primaryStage){
        
        Pane root = new Pane();
        
        loginbtn = new Button("Login");
        signbtn = new Button("Sign _Up");
        back = new Button("Back");
        loginbtn.setScaleX(2);
        signbtn.setScaleX(2);
        loginbtn.setScaleY(1.5);
        signbtn.setScaleY(1.5);
        loginbtn.setTranslateX(250);
        loginbtn.setTranslateY(50);
        signbtn.setTranslateX(240);
        signbtn.setTranslateY(150);
        

        loginbtn.setOnAction(handler);
        signbtn.setOnAction(handler);
        back.setOnAction(handler);
        
        root.getChildren().addAll(loginbtn , signbtn , back);
        scene = new Scene(root , 800 , 500);
        primaryStage.setTitle("Student");
        scene.getStylesheets().add(Student.class.getResource("Login.css").toExternalForm());
    }
     public Scene getScene()
    {
        start(University.MAINSTAGE);
        return scene;
    }
    
    
     private class HandlerClassInst implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent e) {
            if(e.getSource() == loginbtn)
            {
                SceneManager.Go_TO(University.MAINSTAGE, instR.getScene());
            }else if(e.getSource() == signbtn)
            {
                SceneManager.Go_TO(University.MAINSTAGE , instS.getScene());
            }else if(e.getSource() == back)
            {
                SceneManager.GO_BACK(University.MAINSTAGE);
            }
        }
        
     }
}

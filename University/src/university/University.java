
package university;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class University extends Application {

    
    private final HandlerClass handler = new HandlerClass();
    private final Instructor Inst = new Instructor();
    private final Student Std = new Student();
    private final Administrator Admin = new Administrator();
    
    
//    private Connector connect;
    
    private Button conbtn , Stdbtn , Instbtn , admin;
    private Label checklabel;
    private Scene scene;
    
    boolean flag = false;
    int sum = 0;
    
    public static Stage MAINSTAGE;
    
    public static void main(String[] args) {
        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        
        Pane root = new Pane();
       
        VBox vbox = new VBox(20);
        
        checklabel = new Label("Tester");
        checklabel.setTranslateX(200);
        
        admin = new Button("Administrator");
        conbtn = new Button("Connect");
        Stdbtn = new Button("Student");
        Instbtn = new Button("Instructor");
        
        Stdbtn.setDisable(true);
        Instbtn.setDisable(true);
        admin.setDisable(true);
        
        conbtn.setOnAction(handler);
        Stdbtn.setOnAction(handler);
        Instbtn.setOnAction(handler);
        admin.setOnAction(handler);
        
        conbtn.setOnMouseClicked(e->{
            
            sum+=e.getClickCount();
            if(e.getClickCount() >0 && sum %2 != 0){
                if(Connector.connect() != null)
                {
                    Connection con = Connector.connect();
                    checklabel.setTextFill(Color.GREEN);
                    checklabel.setText("Connected Successfully ... :) ");
                    
                    Stdbtn.setDisable(false);
                    Instbtn.setDisable(false);
                    admin.setDisable(false);
                    flag = true;
                    conbtn.setText("DisConnecte !!");
                }else{
                    checklabel.setTextFill(Color.RED);
                    checklabel.setText("Connected Failed ...");
                }
            }
            else if(e.getClickCount() >0 && sum % 2 == 0)
            {
                try {
                    Connector.getConnection().close();
                    Stdbtn.setDisable(true);
                    Instbtn.setDisable(true);
                    admin.setDisable(true);
                    checklabel.setTextFill(Color.BROWN);
                    checklabel.setText("Dis_Connected... x");
                    conbtn.setText("Connect");
                } catch (SQLException ex) {
                    Logger.getLogger(University.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        vbox.getChildren().addAll(checklabel , conbtn , Stdbtn , Instbtn , admin);
        
        root.getChildren().addAll(vbox);
        
        scene = new Scene(root , 800 , 500);
        
        SceneManager.AddScene(scene); // First One Only
        scene.getStylesheets().add(University.class.getResource("Login.css").toExternalForm());
        MAINSTAGE = primaryStage;
        MAINSTAGE.setTitle("University");
        MAINSTAGE.setScene(scene);
        MAINSTAGE.show();
    }
    
    private class HandlerClass implements EventHandler<ActionEvent>
    {

        @Override
        public void handle(ActionEvent e) {           
            
            if(e.getSource() == Instbtn)
            {
                SceneManager.Go_TO(MAINSTAGE , Inst.getScene());
            }else if(e.getSource() == Stdbtn)
            {
                SceneManager.Go_TO(MAINSTAGE, Std.getScene());
            }else if(e.getSource() == admin)
            {
                SceneManager.Go_TO(MAINSTAGE, Admin.getScene());
            }
        }
    }
    
}

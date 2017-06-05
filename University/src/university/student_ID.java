package university;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class student_ID extends Application {

     private static Student_Login  INSTLOG;
     private static PreparedStatement pst;
     private static ResultSet rst;
     public  static int Student_id;
     private Scene scene;
     private static boolean flag = false;
    @Override
    public void start(Stage stage){
        Pane root = new Pane();
        
        Button back = new Button("Back"); 
        back.setOnAction(e->{
        go_Back();
        }); 
        
        VBox vbox = new VBox();
        
        TextField std_id = new TextField();
        std_id.setPromptText("Enter  your id");
        
        Button login = new Button("Login");
        
        
        vbox.getChildren().addAll(back , std_id , login);
        
        login.setOnAction(e->{
            if(std_id.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Enter ID !!");
            }else{
                try{
                    try{
                        Student_id = Integer.parseInt(std_id.getText().trim());
                    }catch(NumberFormatException ex)
                    {
                        JOptionPane.showMessageDialog(null, "Enter Legal ID");
                        return;
                    }
                  INSTLOG  = new Student_Login(Student_id);
                    String query = "select std_id from univ.students";
                    pst = Connector.connect().prepareStatement(query);
                    rst = pst.executeQuery();
                    while(rst.next())
                    {
                        if(rst.getInt(1) == Student_id)
                        {
                            flag = true;
                            break;
                        }
                    }
                    if(flag)
                    {
                         SceneManager.Go_TO(University.MAINSTAGE, INSTLOG.getScene());
                    }else{
                    JOptionPane.showMessageDialog(null, "Not Found Such Id");
                    }
                }catch(SQLException ex)
                {
                    System.out.println(ex);
                }
            }
        });
        
        
        
        root.getChildren().addAll(vbox);
        
        scene = new Scene(root,800 , 500);
        scene.getStylesheets().add(student_ID.class.getResource("Login.css").toExternalForm());
    }
    
     public Scene getScene()
    {
        start(University.MAINSTAGE);
        return scene;
    }

    public void go_Back(){
        SceneManager.GO_BACK(University.MAINSTAGE);
    }


}

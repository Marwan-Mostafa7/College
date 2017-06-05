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

public class Instructor_ID extends Application { 

     private  static Instructor_Login  INSTLOG ;
     private static PreparedStatement pst;
     private static ResultSet rst;
     public  static int inst_id;
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
        
        TextField instructor_id = new TextField();
        instructor_id.setPromptText("Enter  your id");
        
        Button login = new Button("Login");
        
        
        vbox.getChildren().addAll(back , instructor_id , login);
        
        login.setOnAction(e->{
            if(instructor_id.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Enter ID !!");
            }else{
                try{
                    try{
                        inst_id = Integer.parseInt(instructor_id.getText().trim());
                    }catch(NumberFormatException ex)
                    {
                        JOptionPane.showMessageDialog(null, "Enter Legal ID");
                        return;
                    }
                    
                    String query = "select inst_id from univ.instructors";
                    pst = Connector.connect().prepareStatement(query);
                    rst = pst.executeQuery();
                    while(rst.next())
                    {

                        if(rst.getInt(1) == inst_id)
                        {
                            flag = true;
                            break;
                        }
                    }
                    if(flag)
                    {
                         INSTLOG = new Instructor_Login(inst_id);
                         SceneManager.Go_TO(University.MAINSTAGE, INSTLOG.getScene());
                    }else{
                    JOptionPane.showMessageDialog(null, "Not Found Such Id");
                    }
                }catch(SQLException ex)
                {
                    System.out.println(ex);
                }finally{
            try {
                pst.close();
                rst.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            } 
        }
            }
        });
        
        
        
        root.getChildren().addAll(vbox);
        
        scene = new Scene(root,800 , 500);
        scene.getStylesheets().add(Instructor_ID.class.getResource("Login.css").toExternalForm());
    }
    
     public Scene getScene()
    {
        start(University.MAINSTAGE);
        return scene;
    }
     
         
    public void go_Back(){
        SceneManager.GO_BACK(University.MAINSTAGE);
    }    
    
    public int getInstID()
    {
         return inst_id;
    }
    

    
}

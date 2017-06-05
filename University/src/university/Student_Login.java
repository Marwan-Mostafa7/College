package university;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Student_Login extends Application {

    private Scene scene;
    private PreparedStatement pst  = null;
    private ResultSet rst = null;
      
    private final static Std_Courses std = new Std_Courses();
    
    public static String CToken[] = new String[10];
    public static String GToken[] = new String[10];
    
    private ArrayList<String> CoursesID = new ArrayList<>();
    private ArrayList<String> CoursesGrade = new ArrayList<>();
    private ListView list1 , list2;
    private ObservableList obsList1 , obsList2;
    private  static int id;
    
    public Student_Login(int id)
    {
        Student_Login.id = id;
    }
    
    @Override
    public void start(Stage stage){
        
        for(int i= 0; i<CToken.length; i++)
        {
            CToken[i] = "";
            GToken[i] = "";
        }

        Pane root = new Pane();

        Button addCourse = new Button("Add / Remove  Courses"); 
        
        addCourse.setOnAction(e->{
            add_Course();
        });

          Button back = new Button("Logout"); 
        back.setOnAction(e->{
        go_Back();
    });
        
       
        Text the_id = new Text("Your id :: " + id);
        the_id.setFont(Font.font("cursive" , FontWeight.BOLD , 20));
        
        int i = 0 , j = 0;
        String query = "select c_id , grade from univ.students where std_id = ?";   // c_id .. grade
        try {
            pst = Connector.connect().prepareStatement(query);
            pst.setInt(1, id);
            rst = pst.executeQuery();
            while(rst.next())
            {
                CoursesID.add(rst.getString(1));
                CToken[i] = rst.getString(1);
                CoursesGrade.add(rst.getString(2));
                GToken[i] = rst.getString(2);
                i++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Student_Login.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try{
               pst.close();
               rst.close();
            }catch(SQLException ex)
            {System.out.println(ex);}
        }
        
        obsList1 = FXCollections.observableArrayList(CoursesID);
        obsList2 = FXCollections.observableArrayList(CoursesGrade);
        
        list1 = new ListView();
        list1.getItems().addAll(obsList1);
        list1.setPrefHeight(200);
        list2 = new ListView();
        list2.getItems().addAll(obsList2);
        list2.setPrefHeight(200);
        
        
        HBox info = new HBox(50);

        info.getChildren().addAll(list1 , list2);
        
        VBox vb = new VBox(30);
        vb.getChildren().addAll(back ,the_id ,info ,  addCourse);
        
        root.getChildren().addAll(vb);
        scene = new Scene(root,800 , 500);
        scene.getStylesheets().add(Student_Login.class.getResource("Login.css").toExternalForm());
    }
    
    
 public void go_Back(){
     
     for(int i=0; i<Student_Login.CToken.length; i++)
     {
         Student_Login.CToken[i] = "";
         Student_Login.GToken[i] = ""; 
     }
        
     SceneManager.GO_BACK(University.MAINSTAGE);
    }    
 public Scene getScene()
 {
     start(University.MAINSTAGE);
     return scene;
 }
 
public void add_Course()
{
    SceneManager.Go_TO(University.MAINSTAGE, std.getScene());
}
}

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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;


public class Instructor_Login extends Application{

    private Instructor_Edit Inedit = new Instructor_Edit();
    private Inst_Courses instCourses = new Inst_Courses();
    private PreparedStatement pst;
    private ResultSet rst;
    private ListView list1 , list2; 
    private ObservableList obsList1 , obsList2;
    private Button showDetails , addCourse;
    public static String selectedCourse;
    private static int id;
    
    public static ArrayList<String> IToken = new ArrayList<>();
    
    public Instructor_Login(int id)
    {
        Instructor_Login.id = id;
    }
    
    private Scene scene;
    @Override
    public void start(Stage stage){

        Pane root = new Pane(); 
        VBox vbox = new VBox(20); 
        HBox hbox = new HBox(30); 
        HBox grid = new HBox();
        
        Text Inst_ID = new Text("Your ID :: " + id);
        Inst_ID.setFont(Font.font("cursive" , FontWeight.BOLD , 20));
        addCourse = new Button("Add Course");
        
        addCourse.setOnAction(e->{
            SceneManager.Go_TO(University.MAINSTAGE, instCourses.getScene());
        });
        
        Button back = new Button("Log_out"); 
        back.setOnAction(e->{
        go_Back();
    });
        showDetails = new Button("Show details");
        
        Text hint  = new Text("Hint : select course and click show details... for more information about the Course");
        
        showDetails.setOnAction(e->{
            if(list1.getSelectionModel().getSelectedItem() == null)
            {
                JOptionPane.showMessageDialog(null, "Choose Course First !! ");
                return;
            }
            SceneManager.Go_TO(University.MAINSTAGE, Inedit.getScene());
        });
        
        ArrayList<String> inst_Courses = new ArrayList<>();
        ArrayList<Integer> number_std = new ArrayList<>();
        
        
        Text course_id = new Text("Course ID\t\t\t");
        Text N_std = new Text("Number Of Students");
        course_id.setFont(Font.font("cursive" , FontWeight.BOLD , 25));
        N_std.setFont(Font.font("cursive" , FontWeight.BOLD , 25));
        
        grid.getChildren().addAll(course_id , N_std);
        grid.setPadding(new Insets(0,0,0,20));
        
// Showing All Courses Of this Instructor
        String inst_qurey = "select co_id from univ.instructors where inst_id=?"; 
        try {
            pst = Connector.connect().prepareStatement(inst_qurey);
            pst.setInt(1 , id);
            rst = pst.executeQuery();
            while(rst.next())
            {
                inst_Courses.add(rst.getString("co_id"));
                IToken.add(rst.getString("co_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Instructor_Login.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                pst.close();
                rst.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            } 
        }

        // Counting Number Of Students Took the Course
            String std_query = "select std_id from univ.students where c_id =?";
            try {
                pst = Connector.connect().prepareStatement(std_query);
            } catch (SQLException ex) {System.out.println(ex);}
            
      try {
        for(int i=0; i<inst_Courses.size(); i++)
        {
            int n = 0;
                pst.setString(1, inst_Courses.get(i));
                rst = pst.executeQuery();
                while(rst.next())
                {
                    n++;
                }
                number_std.add(n);
        }
            } catch (SQLException ex) {
                System.out.println(ex);
            }finally{
            try {
                pst.close();
                rst.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            } 
        }
        
        obsList1 = FXCollections.observableArrayList(inst_Courses);
        
        list1 = new ListView();
        list2 = new ListView();
        
        list1.getItems().addAll(obsList1);
        
        
        list1.setOnMouseClicked(e->{
            selectedCourse = (String)list1.getSelectionModel().getSelectedItem();
        });
        
        obsList2 =FXCollections.observableArrayList(number_std);
        
        list2.getItems().addAll(obsList2);
        
        list1.setPrefHeight(150);
        
        list2.setPrefHeight(150);
        
        hbox.setPadding(new Insets(20));
        hbox.getChildren().addAll(list1 , list2);
        
        vbox.getChildren().addAll(back ,addCourse , Inst_ID , hint ,  grid, hbox , showDetails);
        root.getChildren().addAll(vbox);
        
        scene = new Scene(root , 800 ,500);

        scene.getStylesheets().add(Instructor_Login.class.getResource("Login.css").toExternalForm());
        stage.setScene(scene);
    }

    
 public void go_Back(){
        IToken.clear();
        SceneManager.GO_BACK(University.MAINSTAGE);
    }    
 
 public Scene getScene()
 {
     start(University.MAINSTAGE);
     return scene;
 }

  
}

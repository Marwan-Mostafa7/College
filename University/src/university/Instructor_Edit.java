package university;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;


public class Instructor_Edit extends Application {

    
    private PreparedStatement pst;
    private ResultSet rst;
    private ListView list;
    private ObservableList obsList;
    private Button del;
    private static Integer id;
    private Scene scene;
    
    @Override
    public void start(Stage primaryStage)  {

        Pane root = new Pane();
        VBox vbox = new VBox(30);
        
        Label happen = new Label();
        happen.setTextFill(Color.CRIMSON);
        
        Button back = new Button("back");
        del = new Button("remove");

        back.setOnAction(e->{
            go_Back();
});
        
        Text  Stud = new Text("Your Students' IDs");
        Stud.setFont(Font.font("cursive" , FontWeight.EXTRA_BOLD , 25));
        
        ArrayList<String> students = new ArrayList<>();
                
        String course = Instructor_Login.selectedCourse;
        String query = "select std_id from univ.students where c_id = ?";
        try {
            pst = Connector.connect().prepareStatement(query);
            pst.setString(1 , course);
            rst = pst.executeQuery();
            while(rst.next())
            {
                students.add(rst.getString("std_id"));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        obsList = FXCollections.observableArrayList(students);
        list = new ListView();
        list.getItems().addAll(obsList);
        list.setPrefHeight(200);
        
        // deleting Student
        del.setOnAction(e->{
           
            String del_query = "delete from univ.students where std_id=? And c_id=?";
           
            if(list.getSelectionModel().getSelectedItem()== null){
                JOptionPane.showMessageDialog(null, "Select Student !");
                return;
            }
            id = Integer.parseInt((String)list.getSelectionModel().getSelectedItem());
            
            list.getItems().remove(id.toString());
            try{
               pst = Connector.connect().prepareStatement(del_query);
               pst.setInt(1 ,id);
               pst.setString(2, course);
               pst.execute();
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
            happen.setText("Deleted Successfully");
        });

        vbox.getChildren().addAll(back , happen , Stud , list , del);
        root.getChildren().add(vbox);
        scene = new Scene(root , 800 , 500);
        scene.getStylesheets().add(Instructor_Edit.class.getResource("Login.css").toExternalForm());
        University.MAINSTAGE = primaryStage;
    }
public Scene getScene() {
    
    start(University.MAINSTAGE);
    return scene;
}
  
public void go_Back()
{
        SceneManager.GO_BACK(University.MAINSTAGE);
}    

}

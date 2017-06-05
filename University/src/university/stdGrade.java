package university;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class stdGrade extends Application {
    
    private Scene scene;
    private static int id;
    
    private PreparedStatement pst;
    private ResultSet rst;

    private static ArrayList<String> arrCourse = new ArrayList<>(), arrGrade = new ArrayList<>()    // Old Grades... default
                                   , arrGrade2 = new ArrayList<>() , arrcomboGrade = new ArrayList<>(); // A , B , C , D , F
                                   //el New Grades
    private static ObservableList obsCourse , obsGrade , comboGrades;
    private static ListView listCourse , listGrade;
    private static ComboBox Grades;
    private static Button setGrade;
    private static Label label;
    private final static String[] GRADES  = {"A" , "B" , "C" , "D" , "F"};
    
    public stdGrade(int id)
    {
        stdGrade.id = id;
    }

    @Override
    public void start(Stage stage)
    {
        Pane root = new Pane();
            
        Button back = new Button("back");
        back.setOnAction(e->{
            go_back();
        });
        
        label = new Label("Choose Your Grade");
        label.setTextFill(Color.BROWN);

        String query1 = "select c_id , grade from univ.students where std_id=?";
        try{
            pst = Connector.connect().prepareStatement(query1);
            pst.setInt(1, id);
            rst = pst.executeQuery();
            while(rst.next())
            {
                arrCourse.add(rst.getString("c_id"));
                arrGrade.add(rst.getString("grade"));
            }
        } catch (SQLException ex) {System.out.println(ex);}
        finally{
            try {
                pst.close();
                rst.close();
            } catch (SQLException ex) {System.out.println(ex);}
        }
            obsCourse = FXCollections.observableList(arrCourse);
            obsGrade = FXCollections.observableList(arrGrade);

            listCourse = new ListView();
            listCourse.getItems().addAll(obsCourse);
            listCourse.setPrefHeight(200);
            
            listGrade = new ListView();
            listGrade.getItems().addAll(obsGrade);
            listGrade.setPrefHeight(200);
            
            comboGrades = FXCollections.observableArrayList(Arrays.asList(GRADES));
            
            Grades = new ComboBox(comboGrades);
            Grades.setPromptText("Grades");
            Grades.setDisable(true);
            
            setGrade = new Button("Set Grade");
            setGrade.setOnAction(e->{
            try {
                if(listCourse.getSelectionModel().getSelectedItem() == null)
                {
                    JOptionPane.showMessageDialog(null, "Select a Course First");
                    return;
                }
                listGrade.getItems().set((Integer)listCourse.getSelectionModel().getSelectedIndex() , (String)Grades.getSelectionModel().getSelectedItem());
                
                String query4 = "update univ.students set grade = ? where std_id = ? and c_id = ?";
                
                pst = Connector.connect().prepareStatement(query4);
                pst.setString(1, (String)Grades.getSelectionModel().getSelectedItem());
                pst.setInt(2, id);
                pst.setString(3, (String)listCourse.getSelectionModel().getSelectedItem());
                pst.execute();
                
                label.setText("Course Grade is Updated");
                label.setTextFill(Color.GREEN);

            } catch (SQLException ex){
                System.out.println(ex);
            }
            finally{
                try {
                    pst.close();
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
            });
            
            listCourse.setOnMouseClicked(e->{
                if(listCourse.getSelectionModel().getSelectedItem() != null){
                       Grades.setDisable(false);
                   }else{
                    Grades.setDisable(true);
                }
            });
            label.setText("Choose Course To Update ");
            label.setTextFill(Color.BLUE);
        HBox hbox = new HBox(50);
        
        hbox.getChildren().addAll(listCourse , listGrade , Grades);
        
        VBox vbox = new VBox(70);
        
        vbox.getChildren().addAll(back  , label, hbox , setGrade);

        root.getChildren().addAll(vbox);
        scene = new Scene(root , 800 , 500);
    }

    public void go_back(){
        arrGrade.clear();
        arrCourse.clear();
        SceneManager.GO_BACK(University.MAINSTAGE);
    }
    
    public Scene getScene()
    {
        start(University.MAINSTAGE);
        return scene;
    }
    
    
}

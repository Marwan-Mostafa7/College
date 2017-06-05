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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class Administrator  extends Application{
 
    private static stdGrade grade;
    
    private ObservableList obsList1 , obsList2;
    private ListView list1 , list2;
    private ArrayList<Integer> std = new ArrayList<>(), inst = new ArrayList<>();
    
    private ResultSet rst;
    private PreparedStatement pst;
    private Scene scene;
    
    private Button removeStd , AddStd , AddCoToStd , AddGradeToStd , ShowStdDetails;
    private Button removeInst , AddInst , AddCoToInst , ShowInstDetails;
    
    private HandlerClass handler = new HandlerClass();
    
    private AddStudent addingstd = new AddStudent();
    private Student_Login stdLogin;
    private Instructor_Login instLogin;
    private Instructor_SignUp addingInst = new Instructor_SignUp();
    private static int id;
    
    @Override
    public void start(Stage primaryStage) {
        
        Pane root = new Pane();
        
        VBox stdBox = new VBox(10);
        VBox InstBox = new VBox(10);
        
//Students Properties
        
        ShowStdDetails = new Button("Student Details");
        removeStd = new Button("Remove Student");
        AddStd = new Button("Add Student");
        AddCoToStd = new Button("Add Course to Student");
        AddGradeToStd = new Button("Add Grade To Student"); 
        stdBox.getChildren().addAll(removeStd , AddStd , AddCoToStd , AddGradeToStd , ShowStdDetails);

// Events Of Students
        removeStd.setOnAction(handler);
        AddStd.setOnAction(handler);
        AddCoToStd.setOnAction(handler);
        AddGradeToStd.setOnAction(handler);
        ShowStdDetails.setOnAction(handler);
        
//Instructors Properties        
        removeInst = new Button("Remove Instructor");
        AddInst = new Button("Add Instructor");
        AddCoToInst = new Button("Add Course to Instructor");
        ShowInstDetails = new Button("Instructor Details");
        InstBox.getChildren().addAll(removeInst , AddInst , AddCoToInst , ShowInstDetails);

// Events Of Instructor
        removeInst.setOnAction(handler);
        AddInst.setOnAction(handler);
        AddCoToInst.setOnAction(handler);
        ShowInstDetails.setOnAction(handler);
        
        HBox prop = new HBox(200);
        prop.getChildren().addAll(stdBox , InstBox);
        
        Button back = new Button("back");
        back.setOnAction(e->{
            go_back();
        });
        
        Text student = new Text("Students' ID");
        student.setFont(Font.font("cursive", FontWeight.BOLD, 20));
        Text instruc = new Text("Instructors' ID");
        instruc.setFont(Font.font("cursive", FontWeight.BOLD, 20));
        
        // Showing WHOLE Studetns in Your College

        
        String query = "select std_id from univ.students;";
        try {
             pst = Connector.connect().prepareStatement(query);
             
             rst = pst.executeQuery();
            
             while(rst.next())
             {
                if(!std.contains(rst.getInt("std_id")))   // unique Students' id s
                    std.add(rst.getInt("std_id"));
             }
        }catch (SQLException ex) {
            System.out.println(ex);
        }finally{
            try {
                pst.close();
                rst.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
        
        // Showing WHOLE Instructors in Your College
        String query2 = "select inst_id from univ.instructors";
        try{
        pst = Connector.connect().prepareStatement(query2);
        rst = pst.executeQuery();
        while(rst.next())
        {
            if(!inst.contains(rst.getInt("inst_id")))
                inst.add(rst.getInt("inst_id"));
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
        
        obsList1 = FXCollections.observableArrayList(std);
        list1 = new ListView();
        list1.getItems().addAll(obsList1);
        list1.setPrefHeight(200);
        
        obsList2 = FXCollections.observableArrayList(inst);
        list2 = new ListView();
        list2.getItems().addAll(obsList2);
        list2.setPrefHeight(200);

        HBox labels = new HBox(200);
        labels.getChildren().addAll(student ,instruc);
        
        HBox hbox = new HBox(50);
        hbox.getChildren().addAll(list1 , list2);
        
        VBox vbox = new VBox();
        
        vbox.getChildren().addAll(back ,labels ,  hbox , prop);
        
        root.getChildren().add(vbox);
        
        scene = new Scene(root , 800 , 500);       
         scene.getStylesheets().add(Administrator.class.getResource("Login.css").toExternalForm());
    }
    
    public void go_back(){
        SceneManager.GO_BACK(University.MAINSTAGE);
    }
    
    public Scene getScene(){
        start(University.MAINSTAGE);
        return scene;
    }
    
    public class HandlerClass implements EventHandler<ActionEvent>{
        
        @Override
        public void handle(ActionEvent e)
        {
            if(e.getSource() == AddStd)
            {
                SceneManager.Go_TO(University.MAINSTAGE, addingstd.getScene());
            }else if(e.getSource() == AddInst)
            {
                SceneManager.Go_TO(University.MAINSTAGE, addingInst.getScene());
            
            }else if(e.getSource() == AddCoToStd ||e.getSource() == ShowStdDetails)
            {
                if(list1.getSelectionModel().getSelectedItem() == null)
                {
                    JOptionPane.showMessageDialog(null, "Select a Student first");
                    return;
                }
                
                 id = (Integer)list1.getSelectionModel().getSelectedItem();
                stdLogin = new Student_Login(id);
                SceneManager.Go_TO(University.MAINSTAGE, stdLogin.getScene());
                
            }else if(e.getSource() == AddCoToInst ||e.getSource() == ShowInstDetails)
            {
                if(list2.getSelectionModel().getSelectedItem() == null)
                {
                    JOptionPane.showMessageDialog(null, "Select an Instructor first");
                    return;
                }
                
                 id = (Integer)list2.getSelectionModel().getSelectedItem();
                instLogin = new Instructor_Login(id);
                SceneManager.Go_TO(University.MAINSTAGE, instLogin.getScene());
            }
            else if(e.getSource() == removeStd)
            {
                if(list1.getSelectionModel().getSelectedItem() == null)
                {
                    JOptionPane.showMessageDialog(null, "Select a Student first");
                    return;
                }
                int std_ID = (Integer)list1.getSelectionModel().getSelectedItem();
                    std.remove((Integer)list1.getSelectionModel().getSelectedItem());
                     /// SOUT
                String queryDelStd = "delete from univ.students where std_id=?";
                
                try{
                    pst = Connector.connect().prepareStatement(queryDelStd);
                    pst.setInt(1, std_ID);
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Successfully Deleted");
                } catch (SQLException ex) {
                    System.out.println("Is'nt Deleted  " + ex);                    /// SOUT
                }finally{
                    try {
                        pst.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Administrator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }else if(e.getSource() == removeInst)
            {
                if(list2.getSelectionModel().getSelectedItem() == null)
                {
                    JOptionPane.showMessageDialog(null, "Select an Instructor first");
                    return;
                }
                int Inst_ID = (Integer)list2.getSelectionModel().getSelectedItem();
                list2.getItems().remove((Integer)list2.getSelectionModel().getSelectedItem());
                String queryDelStd = "delete from univ.instructors where inst_id=?";
                
                try{
                    pst = Connector.connect().prepareStatement(queryDelStd);
                    pst.setInt(1, Inst_ID);
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Successfully Deleted");
                } catch (SQLException ex) {
                    System.out.println("Is'nt Deleted");
                }finally{
                    try {
                        pst.close();
                    } catch (SQLException ex) {
                        System.out.println(ex);
                    }
                }
                
            }else if(e.getSource() == AddGradeToStd)
            {
                 if(list1.getSelectionModel().getSelectedItem() == null)
                {
                    JOptionPane.showMessageDialog(null, "Select a Student first");
                    return;
                }
                int id = (Integer)list1.getSelectionModel().getSelectedItem();
                
                grade = new stdGrade(id);
                SceneManager.Go_TO(University.MAINSTAGE, grade.getScene());
                
            }
        }
    }
    
}

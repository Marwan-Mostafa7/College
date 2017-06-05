package university;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class Instructor_SignUp extends Application{

    
    private ListView CoursesList;
    private ComboBox ComboFields;
    private ObservableList obsCourses ;
    private PreparedStatement pst ;
    private ResultSet rst;
    private VBox vbox;
    private static boolean flag = true;
     
    
    private Scene scene ;
    @Override
    public void start(Stage stage){

        

        vbox = new VBox(25);
        vbox.setPadding(new Insets(0, 0, 0, 40));
        TextField txtName = new TextField();
        txtName.setPromptText("Enter Name");
        TextField txtId = new TextField();
        txtId.setPromptText("Enter ID");
        DatePicker txtdate  = new DatePicker();
         Label happen = new Label();
        happen.setTextFill(Color.CRIMSON);
        //  Fields
        String[] Fields = { "CS" ,"Pre-Cs","physics" , "Pre-Physics" ,"Math" , "Pre-Math" , "Geo" , "Pre-Geo"};
        List<String> Subjects =new ArrayList<>();
        Subjects.addAll(Arrays.asList(Fields));
        ObservableList obsList = FXCollections.observableArrayList(Subjects);
        ComboFields = new ComboBox(obsList);
        
        ComboFields.setPromptText("Departments");
        
        // Courses Of Each Field
        
        Button next = new Button(">");
        Button prev = new Button("<");
        ListView CheckedList = new ListView();
        CheckedList.setPrefHeight(100);
        vbox.getChildren().addAll(txtName , txtId , txtdate, ComboFields);
        
// **** Comboooo        
        ComboFields.setOnKeyReleased(e->{
        
        List<String> Courses = new ArrayList<>();
        
        String[] physics = {"phy102" , "phy205" ,"phy303" , "phy309" ,"phy403"};
        String[] Math = {"M102" , "M205" ,"M303" , "M309" ,"M403"};
        String[] CS = {"C102" , "C205" ,"C303" , "C309" ,"C403"};
        String[] Geo = {"G102" , "G205" ,"G303" , "G309" ,"G403"};
        String[] PRE_Cs= {"PC102","PC205","PC303","PC309","PC403"};
        String[] PRE_Geo= {"PG102","PG205","PG303","PG309","PG403"};
        String[] PRE_Math= {"PM102","PM205","PM303","PM309","PM403"};    
        String[] PRE_physics= {"Pphy102","Pphy205","Pphy303","Pphy309","Pphy403"};
        
        int  index = (Integer)ComboFields.getSelectionModel().getSelectedIndex();
        
        String[][] A = new String[8][5];
        A[0] = CS;
        A[1] = PRE_Cs;
        A[2] = physics;
        A[3] = PRE_physics;
        A[4] = Math;
        A[5] = PRE_Math;
        A[6] = Geo;
        A[7] = PRE_Geo;
        
        for(int i=0; i<A.length; i++)
        {
            if(index == i)
            {
                Courses.addAll(Arrays.asList(A[i]));
                break;
            }
        }
        obsCourses = FXCollections.observableArrayList(Courses);
        addObservList(obsCourses);
        });
// **** Comboooo

       CoursesList = new ListView();
       CoursesList.setPrefHeight(100);
      
         // end of Handling ComboBox
         
    // Handling Next And Prev Buttons
    
    next.setOnAction(e->{
        
            String course = (String) CoursesList.getSelectionModel().getSelectedItem();
            if(!CheckedList.getItems().contains(course) && CheckedList.getItems().size() < 3)
                CheckedList.getItems().add(course);
        });
    
     prev.setOnAction(e->{
            String course = (String) CheckedList.getSelectionModel().getSelectedItem();
            CheckedList.getItems().remove(course);
        });
    // end of Handling Buttons
    
    // Submit button
       TextField noOfStd = new TextField();

        Button submit = new Button("Submit");
        submit.setOnAction(e->{
            // Check that id field is not empty
           try{ 
            if(txtId.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Enter ID... ");
                return;
            }
           }catch(NumberFormatException ex)
           {
               JOptionPane.showMessageDialog(null, "Enter a Numeric ID... ");
                return;
           }
           if(CheckedList.getItems().isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Choose your Subjects... ");
                return;
            }
           // Check if the ID is Unique Or Not :)) ...
           String CheckIDquery = "select inst_id from univ.instructors";
           try{
                pst = Connector.connect().prepareStatement(CheckIDquery);
                rst = pst.executeQuery();
                while(rst.next())
                {
                    if(Integer.parseInt(txtId.getText()) == rst.getInt("inst_id"))
                    {
                        flag = false;
                        break;
                    }else{
                        flag = true;
                    }
                }
            }catch(SQLException ex)
            {
                System.out.println(ex);
            }
            if(flag == false)
            {
                JOptionPane.showMessageDialog(null, "This ID is Already used !!");
                return;
            }
                String query = "insert into univ.instructors values(?, ?)";
               int id = Integer.parseInt(txtId.getText());
               try {
                   pst = Connector.connect().prepareStatement(query);
               } catch (SQLException ex){
                    System.out.println(ex);
               }
                   try{
                    for(int i=0; i<CheckedList.getItems().size(); i++)
                    {
                       pst.setInt(1, id);
                       pst.setString(2, (String)CheckedList.getItems().get(i));
                       pst.execute();  
                    }
                   } catch (SQLException ex) {System.out.println(ex);}
                   finally{
                       try{
                           pst.close();
                       }catch(SQLException ex){}
                   }
               JOptionPane.showMessageDialog(null, "Insertion Done...");
           
        });
        
        Button back = new Button("Back");
        
        back.setOnAction(e->{
        go_Back();
    });
        
       HBox hbox = new HBox(50);    // 2 (ListView  + Buttons)
       
       VBox btnBox = new VBox(35);  // 2 Buttons
       
       btnBox.getChildren().addAll(next , prev);
       
       hbox.getChildren().addAll(CoursesList , btnBox , CheckedList);
        
       vbox.getChildren().addAll( happen, hbox , noOfStd , submit , back);
        
       Pane root = new Pane();
       
       root.getChildren().addAll(vbox);

       scene = new Scene(root ,800 , 500);
       
       scene.getStylesheets().add(Instructor_SignUp.class.getResource("Login.css").toExternalForm());
    
       University.MAINSTAGE.setTitle("Instructor >> Sign-Up");  
       
    }

    
    public void go_Back(){
        
        try {
            Connector.getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(Instructor_SignUp.class.getName()).log(Level.SEVERE, null, ex);
        }
        SceneManager.GO_BACK(University.MAINSTAGE);
    }

    public void addObservList(ObservableList list)
    {
        CoursesList.getItems().clear();
        CoursesList.getItems().addAll(list);
    }
    
    public Scene getScene()
    {
        start(University.MAINSTAGE);
        return scene;
    }
}

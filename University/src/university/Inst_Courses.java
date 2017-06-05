package university;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class Inst_Courses  extends Application{

    private Scene scene;
    
    private   HandlerClass handler = new HandlerClass();
    private ListView list1 , list2 , list3;
    private Button next , prev , save , remove;
    private Label label;
    private ObservableList obsList ,obsList3;
    private PreparedStatement pst;
    
    private final static String[] MAIN_COURSES = {
        "C102","C205","C303","C309","C403",
        "G102","G205","G303","G309","G403",
        "M102","M205","M303","M309","M403",
        "phy102","phy205","phy303","phy309","phy403"
    };
    private final static String[] PRE_COURSES = {
        "PC102","PC205","PC303","PC309","PC403",
        "PG102","PG205","PG303","PG309","PG403",
        "PM102","PM205","PM303","PM309","PM403",
        "Pphy102","Pphy205","Pphy303","Pphy309","Pphy403"
    };
    
    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        label = new Label("Choose Your Course ...");
        label.setTextFill(Color.DARKMAGENTA);
        Button back = new Button("Back");
         back.setOnAction(e->{
             go_Back();
         });
         
         save = new Button("Save");

         save.setOnAction(handler);
         
         VBox big = new VBox(10);
         HBox hbox = new HBox(20);
         VBox buBox = new VBox(20);

        next = new Button(">");
        prev = new Button("<");
        
        Text oldC = new Text("Your Courses");
        
        oldC.setFont(Font.font("cursive" , FontWeight.BOLD , 20));

        List<String> MainCoursesList = new ArrayList<>();
        MainCoursesList.addAll(Arrays.asList(MAIN_COURSES));
        MainCoursesList.addAll(Arrays.asList(PRE_COURSES));
        
         
        obsList = FXCollections.observableArrayList(MainCoursesList);

        list1 = new ListView();
        list1.getItems().addAll(obsList);
        list1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        list2 = new ListView();
        list3 = new ListView();
        obsList3 = FXCollections.observableArrayList(Instructor_Login.IToken);

        list3.getItems().addAll(obsList3);

        next.setOnAction(handler);


        prev.setOnAction(handler);
        
        HBox save_del = new HBox(50);
        
        remove = new Button("Remove");
        
        remove.setOnAction(handler);
        
        save_del.getChildren().addAll(save , remove);
        
        buBox.getChildren().addAll(next , prev);
        hbox.getChildren().addAll(list1 , buBox , list2 , oldC , list3);
        big.getChildren().addAll(back  , label, hbox , save_del );
        root.getChildren().addAll(big);
        
        scene = new Scene(root , 800, 500); 
        scene.getStylesheets().add(Inst_Courses.class.getResource("Login.css").toExternalForm());
    }

 public void go_Back(){
     SceneManager.GO_BACK(University.MAINSTAGE);
    }
 
 public Scene getScene()
 {
     start(University.MAINSTAGE);
     return scene;
 }
 
 private class HandlerClass implements EventHandler<ActionEvent>
 {
     @Override
     public void handle(ActionEvent e)
     {
         if(e.getSource() == prev)
         {
              String course = (String) list2.getSelectionModel().getSelectedItem();
              list2.getItems().remove(course);
              label.setTextFill(Color.BROWN);
                label.setText("You  Have canceled this Course... ");
         }
         else if(e.getSource() == next)
         {
            String course = (String) list1.getSelectionModel().getSelectedItem();
            if(Instructor_Login.IToken.contains(course))
            {
             label.setText("Already enrolled in that Course !!");
             label.setTextFill(Color.TOMATO);
            }
            if(!list2.getItems().contains(course) && !Instructor_Login.IToken.contains(course))
            {
                list2.getItems().add(course);
                label.setTextFill(Color.BLUE);
                label.setText("You  Have Taken this Course... ");
            }
         }else if(e.getSource() == save)
         {
             if(list2.getItems().isEmpty())
             {
                 JOptionPane.showMessageDialog(null, "Insert Your Courses !!");
                 return;
             }
             String query ="insert into univ.instructors values(? , ?)";       // inst_id , c_id
             try{
                 pst = Connector.connect().prepareStatement(query);
                 for(int i=0; i<list2.getItems().size(); i++)
                 {
                     pst.setInt(1, Instructor_ID.inst_id);
                     pst.setString(2,(String)list2.getItems().get(i));
                     pst.executeUpdate();
                     list3.getItems().add((String)list2.getItems().get(i));
                 }
                 JOptionPane.showMessageDialog(null, "Insertion Done!");
             }catch(SQLException ex)
             {
                 System.out.println("Exception");    
             }finally{
                       try{
                           pst.close();
                       }catch(SQLException ex){System.out.println(ex);}
                   }
         }else if(e.getSource() == remove)
         {
             String Ditem = (String)list3.getSelectionModel().getSelectedItem();
             String query = "delete from univ.instructors where co_id=? and inst_id =?";
             list3.getItems().remove(Ditem);
             try{
                 pst = Connector.connect().prepareStatement(query);
                 pst.setString(1, Ditem);
                 pst.setInt(2 , Instructor_ID.inst_id);
                 pst.executeUpdate();
                 label.setText("Deleted successfully...");
                 label.setTextFill(Color.GREEN);
                 
             }catch(SQLException ex)
             {
                 System.out.println(ex);
             }finally{
                       try{
                           pst.close();
                       }catch(SQLException ex){}
                   }
         }
         
     } // end of Handle function
 }
}

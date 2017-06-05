package university;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class Student_SignUp extends Application {

    private Scene scene;
    private ListView list1 , list2;
    private ObservableList obsList1, obsList2;
    private PreparedStatement pst;
    private static int id;
    private final static String[] PRE_COURSES = {
        "PC102","PC205","PC303","PC309","PC403",
        "PG102","PG205","PG303","PG309","PG403",
        "PM102","PM205","PM303","PM309","PM403",
        "Pphy102","Pphy205","Pphy303","Pphy309","Pphy403"
    };
    @Override
    public void start(Stage primaryStage){

        Pane root = new Pane();
        VBox bigV = new VBox(40); 
        HBox hbox = new HBox(20);
        VBox buBox = new VBox(20);

        Button back = new Button("Back"); 
        back.setOnAction(e->{
        go_Back();
    });

        TextField txtName = new TextField();
        txtName.setPromptText("Enter Name");

        TextField txtId = new TextField();
        txtId.setPromptText("Enter ID");
        
        
        list1 = new ListView();
        list2 = new ListView();

        // Array[] ...> ArrayList ....> ObservableList ....> ListView...| ^ | Done!

        List<String> arList = new ArrayList<>(Arrays.asList(PRE_COURSES));
        obsList1 = FXCollections.observableArrayList(arList);
        list1.getItems().addAll(obsList1);
        list1.setMaxSize(150, 150);

        obsList2 = FXCollections.observableArrayList();
        list2.getItems().addAll(obsList2);
        list2.setMaxSize(150, 150);
          
        Button next = new Button(">");
        Button prev = new Button("<");
        
        Button save = new Button("Save");
        
        save.setOnAction(e->{
            if(txtId.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Enter ID yabny");
                return;
            }
            if(list2.getItems().isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Choose Your Subject First");
                return;
            }
            try{
                 id =  Integer.parseInt(txtId.getText());
            }catch(NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(null, "Only Number yabny");
            }
            
            String query ="insert into univ.students values(? , ? , ?)";       // std_id , c_id , grade
            try{
                pst = Connector.connect().prepareStatement(query);
                for(int i=0; i<list2.getItems().size(); i++)
                {
                    pst.setInt(1, id);
                    pst.setString(2 ,(String)list2.getItems().get(i));
                    pst.setString(3, "");
                    pst.executeUpdate();
                }
                JOptionPane.showMessageDialog(null , "Insertion Done!");
            }catch(SQLException ex)
            {
                System.out.println(ex);
            }finally{
                       try{
                           pst.close();
                       }catch(SQLException ex){}
                   }
        });
        
        next.setOnAction(e->{
            String item = (String)list1.getSelectionModel().getSelectedItem();
            if(!list2.getItems().contains(item) && list2.getItems().size() < 9)
                list2.getItems().add(item);
        });

        prev.setOnAction(e->{
            list2.getItems().remove(list2.getSelectionModel().getSelectedItem());
        });


        buBox.getChildren().addAll(next , prev);

        hbox.getChildren().addAll(list1 , buBox , list2);
        bigV.getChildren().addAll( back,save , txtName ,txtId , hbox );
        root.getChildren().addAll(bigV);
        scene = new Scene(root, 800 , 500);
        scene.getStylesheets().add(Student_SignUp.class.getResource("Login.css").toExternalForm());
        University.MAINSTAGE =primaryStage;
    }

 public void go_Back(){
        SceneManager.GO_BACK(University.MAINSTAGE);
    }
   public Scene getScene()
    {
        start(University.MAINSTAGE);
        return scene;
    }
}
package university;

import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager{

    private static int index = -1;
    private final static ArrayList<Scene> ALL_SCE = new ArrayList<>();
    
    public static void AddScene(Scene x)
    {
        ALL_SCE.add(x);
        index++;
    }
    
    
    public static void Go_TO(Stage A , Scene b)
    {
        AddScene(b);
        A.setScene(ALL_SCE.get(index));
        A.show();        
    }
    
    public static void GO_BACK(Stage A)
    {
        index--;
        A.setScene(ALL_SCE.get(index));
        ALL_SCE.remove(index+1);
        A.show();
    }
}

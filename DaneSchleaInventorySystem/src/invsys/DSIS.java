package invsys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Dane Schlea
 */
public class DSIS extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DSIS.class.getResource("/View_Controller/MainScreen.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

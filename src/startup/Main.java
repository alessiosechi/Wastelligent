package startup;

import boundary.LoginViewController;
import boundary.ViewLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewLoader.setStage(primaryStage);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundary/LoginView.fxml"));

        
		LoginViewController loginViewController=LoginViewController.getInstance();
		loader.setController(loginViewController); // imposto il controller grafico della vista LoginView
		Parent root= loader.load();
        

        primaryStage.setResizable(false); // non si può ridimensionare la finestra
        Scene scene = new Scene(root); // contenitore di tutto ciò che viene visualizzato nella finestra
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();  
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    


}

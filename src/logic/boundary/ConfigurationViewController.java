package logic.boundary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import logic.config.PersistenceConfigurator;
import logic.config.PersistenceProvider;

public class ConfigurationViewController {
    @FXML
    private RadioButton demoModeRadioButton; 

    @FXML
    private RadioButton memoryModeRadioButton; 


    @FXML
    private void initialize() {
    	ToggleGroup modeToggleGroup = new ToggleGroup();
        demoModeRadioButton.setToggleGroup(modeToggleGroup);
        memoryModeRadioButton.setToggleGroup(modeToggleGroup);

        demoModeRadioButton.setSelected(true);
    }


    @FXML
    private void handleConfirmButtonAction(ActionEvent event) {

		PersistenceProvider provider = demoModeRadioButton.isSelected() ? PersistenceProvider.IN_MEMORY : PersistenceProvider.DATABASE;
		PersistenceConfigurator.configurePersistence(provider);
		
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.hide();
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
		

    }
}

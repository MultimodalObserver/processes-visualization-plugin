package mo.visualization.process.plugin.view;


import mo.core.I18n;
import mo.core.ui.Utils;
import mo.visualization.process.plugin.model.VisualizationConfiguration;

import javax.swing.*;
import java.awt.*;

public class ProcessesVisualizationConfigurationDialog extends JDialog{

    private VisualizationConfiguration temporalConfig;
    private boolean accepted;
    private JLabel configurationNameLabel;
    private JTextField configurationNameTextField;
    private JLabel configurationNameErrorLabel;
    private JButton saveConfigButton;

    public ProcessesVisualizationConfigurationDialog(){
        super(null,"", Dialog.ModalityType.APPLICATION_MODAL);
        this.temporalConfig = null;
        this.accepted = false;
        I18n i18n = new I18n(ProcessesVisualizationConfigurationDialog.class);
        this.setTitle(i18n.s("configurationDialogTitleText"));
        this.configurationNameLabel = new JLabel(i18n.s("configurationNameLabelText"));
        this.configurationNameTextField = new JTextField();
        this.configurationNameErrorLabel = new JLabel(i18n.s("configurationNameErrorLabelText"));
        this.configurationNameErrorLabel.setVisible(false);
        this.saveConfigButton = new JButton(i18n.s("saveConfigButtonText"));
        this.addComponents();
        this.addActionListeners();
    }

    private void addComponents(){
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new GridBagLayout());
        /* Configuration Name Label */
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.setConstraintsForLeftSide(constraints, true);
        contentPane.add(this.configurationNameLabel, constraints);

        /* Configuration Name EditText*/
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        this.setConstraintsForRightSide(constraints, false);
        contentPane.add(this.configurationNameTextField, constraints);

        /* Configuration Name Error Label*/
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        this.setConstraintsForRightSide(constraints, true);
        contentPane.add(this.configurationNameErrorLabel, constraints);

        /* Save BUtton*/
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        this.setConstraintsForSaveButton(constraints);
        contentPane.add(this.saveConfigButton, constraints);
    }

    public void showDialog(){
        this.setMinimumSize(new Dimension(400, 150));
        this.setPreferredSize(new Dimension(400, 300));
        this.pack();
        Utils.centerOnScreen(this);
        this.setVisible(true);
    }

    private void addActionListeners(){
        this.saveConfigButton.addActionListener(e -> {
            if(this.isInvalidData()){
                return;
            }
            String configurationName = this.configurationNameTextField.getText();
            this.temporalConfig = new VisualizationConfiguration(configurationName);
            this.accepted = true;
            this.setVisible(false);
            this.dispose();
        });
    }

    public VisualizationConfiguration getTemporalConfig() {
        return this.temporalConfig;
    }

    public boolean isAccepted() {
        return this.accepted;
    }

    private boolean isInvalidData(){
        this.configurationNameErrorLabel.setVisible(false);
        String configurationName = this.configurationNameTextField.getText();
        if(configurationName.isEmpty()){
            this.configurationNameErrorLabel.setVisible(true);
            return true;
        }
        return false;
    }

    private void setConstraintsForLeftSide(GridBagConstraints constraints, boolean hasErrorLabel){
        constraints.gridwidth=1;
        constraints.gridheight= hasErrorLabel ? 2 : 1;
        constraints.weighty=1.0;
        constraints.insets= new Insets(5,10,5,5);
        constraints.anchor=GridBagConstraints.FIRST_LINE_START;
    }

    private void setConstraintsForRightSide(GridBagConstraints constraints, boolean errorLabel){
        constraints.gridheight=1;
        constraints.gridwidth=GridBagConstraints.REMAINDER;
        constraints.weightx=1.0;
        constraints.fill= GridBagConstraints.HORIZONTAL;
        int topInset = errorLabel ? 0 : 5;
        int bottomInset = errorLabel ? 5 : 0;
        constraints.insets= new Insets(topInset,5,bottomInset,10);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
    }

    private void setConstraintsForSaveButton(GridBagConstraints constraints){
        constraints.gridheight=1;
        constraints.gridwidth=2;
        constraints.weightx=0.0;
        constraints.weighty=0.0;
        constraints.fill=GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets= new Insets(10,10,10,10);
    }
}

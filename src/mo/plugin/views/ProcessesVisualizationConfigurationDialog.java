package mo.plugin.views;


import mo.core.I18n;
import mo.core.ui.Utils;
import mo.plugin.models.VisualizationConfiguration;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProcessesVisualizationConfigurationDialog extends JDialog implements DocumentListener {

    private VisualizationConfiguration temporalConfig;
    private boolean accepted;
    private JLabel configurationNameLabel;
    private JTextField configurationNameTextField;
    private JLabel configurationNameErrorLabel;
    private JButton saveConfigButton;
    private I18n i18n;

    public ProcessesVisualizationConfigurationDialog(){
        super(null,"", Dialog.ModalityType.APPLICATION_MODAL);
        this.temporalConfig = null;
        this.accepted = false;
        this.i18n = new I18n(ProcessesVisualizationConfigurationDialog.class);
        this.setTitle(this.i18n.s("configurationDialogTitleText"));
        this.configurationNameLabel = new JLabel(this.i18n.s("configurationNameLabelText"));
        this.configurationNameTextField = new JTextField();
        this.configurationNameErrorLabel = new JLabel(this.i18n.s("configurationNameErrorLabelText"));
        this.saveConfigButton = new JButton(this.i18n.s("saveConfigButtonText"));
        this.centerComponents();
        this.addComponents();
        this.addActionListeners();
    }

    private void centerComponents(){
        this.configurationNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.configurationNameTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.configurationNameErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.saveConfigButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void addComponents(){
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(this.configurationNameLabel);
        contentPane.add(this.configurationNameTextField);
        contentPane.add(this.configurationNameErrorLabel);
        contentPane.add(this.saveConfigButton);
    }

    public void showDialog(){
        this.setMinimumSize(new Dimension(400, 150));
        this.setPreferredSize(new Dimension(400, 300));
        this.pack();
        Utils.centerOnScreen(this);
        this.setVisible(true);
    }

    private void watchFormErrors(){
        if(this.configurationNameTextField.getText().isEmpty()){
            this.configurationNameErrorLabel.setVisible(true);
        }
        else{
            this.configurationNameErrorLabel.setVisible(false);
        }
    }



    /**
     * Gives notification that there was an insert into the document.  The
     * range given by the DocumentEvent bounds the freshly inserted region.
     *
     * @param e the document event
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        this.watchFormErrors();
    }

    /**
     * Gives notification that a portion of the document has been
     * removed.  The range is given in terms of what the view last
     * saw (that is, before updating sticky positions).
     *
     * @param e the document event
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        this.watchFormErrors();
    }

    /**
     * Gives notification that an attribute or set of attributes changed.
     *
     * @param e the document event
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
        this.watchFormErrors();
    }

    private void addActionListeners(){
        this.saveConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String configurationName = ProcessesVisualizationConfigurationDialog.this.configurationNameTextField.getText();
                ProcessesVisualizationConfigurationDialog.this.temporalConfig = new VisualizationConfiguration(configurationName);
                ProcessesVisualizationConfigurationDialog.this.accepted = true;
                ProcessesVisualizationConfigurationDialog.this.setVisible(false);
                ProcessesVisualizationConfigurationDialog.this.dispose();
            }
        });
    }

    public VisualizationConfiguration getTemporalConfig() {
        return this.temporalConfig;
    }

    public boolean isAccepted() {
        return this.accepted;
    }
}

package mo.visualization.process.plugin.view;

import mo.core.I18n;
import mo.visualization.process.plugin.ProcessesPlayer;
import mo.visualization.process.plugin.model.Process;
import mo.visualization.process.plugin.model.Separator;
import mo.visualization.process.plugin.model.Snapshot;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/* En esta clase se realiza el rendereo de los datos de un snapshot de procesos en sí, que funciona junto con el player de MO*/
public class ProcessesPlayerPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private I18n i18n;
    private float[] columnWidths;

    public ProcessesPlayerPanel(){
        this.i18n = new I18n(ProcessesPlayerPanel.class);
        this.tableModel = new DefaultTableModel();
        this.table = new JTable(this.tableModel);
        this.columnWidths = new float[]{0.24f,0.08f, 0.16f,0.16f,0.16f, 0.08f};
        this.addHeaders(this.getHeaders());
        JScrollPane scrollPane = new JScrollPane(this.table);
        this.table.setFillsViewportHeight(true);
        this.table.setCellSelectionEnabled(true);
        this.table.setShowHorizontalLines(false);
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10,10,10,10);
        this.add(scrollPane, constraints);
        this.resizeColumns();
        this.setVisible(true);
    }

    public void updateData(Snapshot snapshot){
        if(snapshot == null || snapshot.getProcesses().isEmpty()){
            return;
        }
        SwingUtilities.invokeLater(new Runnable(){public void run(){
            ProcessesPlayerPanel.this.clearTable();
            for(Process process : snapshot.getProcesses()){
                Object[] values = new Object[]{
                        ProcessesPlayerPanel.this.getApplicationName(process),
                        process.getPid(),
                        process.getUserName(),
                        process.getStartInstant(),
                        process.getTotalCpuDuration(),
                        process.getParentPid()
                };
                ProcessesPlayerPanel.this.tableModel.addRow(values);
            }
        }});
    }

    private List<String> getHeaders(){
        List<String> headers = new ArrayList<>();
        headers.add(this.i18n.s("applicationColumnName"));
        headers.add(this.i18n.s("pidColumnName"));
        headers.add(this.i18n.s("usernameColumnName"));
        headers.add(this.i18n.s("startInstantColumnName"));
        headers.add(this.i18n.s("totalCpuDurationColumnName"));
        headers.add(this.i18n.s("parentPidColumnName"));
        return headers;
    }
    private void addHeaders(List<String> tableHeaders){
        if(tableHeaders == null || tableHeaders.isEmpty()){
            return;
        }
        for(String header: tableHeaders){
            this.tableModel.addColumn(header);
        }
    }

    private void resizeColumns() {
        TableColumn column;
        TableColumnModel jTableColumnModel = this.table.getColumnModel();
        int tableWidth = jTableColumnModel.getTotalColumnWidth();
        int cantCols = jTableColumnModel.getColumnCount();
        for (int i = 0; i < cantCols; i++) {
            column = jTableColumnModel.getColumn(i);
            int columnWidth = Math.round(this.columnWidths[i] * tableWidth);
            column.setPreferredWidth(columnWidth);
        }
    }

    private String getApplicationName(Process process){
        if(process == null || process.getCommand() == null || process.getCommand().isEmpty()){
            return null;
        }
        String separator = process.getCommand().contains(Separator.WINDOWS_FILE.getValue()) ? Separator.REGEX_WINDOW_FILE.getValue()
                : Separator.FILE.getValue();
        String[] parts = process.getCommand().split(separator);
        return parts[parts.length - 1];
    }

    private void clearTable(){
        this.tableModel.setRowCount(0);
    }
}

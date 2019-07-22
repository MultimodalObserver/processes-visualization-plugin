package mo.plugin.views;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mo.plugin.models.Process;
import mo.plugin.models.Snapshot;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/* En esta clase se realiza el rendereo de los datos de un snapshot de procesos en sí, que funciona junto con el player de MO*/
public class ProcessesPlayerPanel extends JPanel {

    private JScrollPane scrollPane;
    private JTable table;
    private DefaultTableModel tableModel;

    /* Estos textos deben ser internacionalizados*/
    private static final String[] TABLE_HEADERS = {"PID", "Username",
            "Start instant", "Total CPU Duration", "command", "Parent PID"};

    public ProcessesPlayerPanel(){
        this.table = null;
        this.scrollPane = null;
    }

    public void createTable(Snapshot snapshot){
        Object[][] formattedProcessesData = this.parseData(snapshot);
        this.tableModel = new DefaultTableModel(formattedProcessesData, TABLE_HEADERS);
        this.table = new JTable(this.tableModel);
        this.scrollPane = new JScrollPane(table);
        this.table.setFillsViewportHeight(true);
        this.add(this.scrollPane);
        this.setVisible(true);
    }

    public void updateData(Snapshot snapshot){
        Object[][] formattedProcessesData = this.parseData(snapshot);
        this.tableModel.setDataVector(formattedProcessesData, TABLE_HEADERS);
        this.tableModel.fireTableRowsUpdated(1, formattedProcessesData.length);
    }

    private Object[][] parseData(Snapshot snapshot){
        List<Process> processes = snapshot.getProcesses();
        int size = processes.size();
        Object[][] res = new Object[size][];
        for(int i=0 ; i < size; i++){
            Process process = processes.get(i);
            String[] processData = new String[]{
                String.valueOf(process.getPid()),
                process.getUserName(),
                    process.getStartInstant(),
                    String.valueOf(process.getTotalCpuDuration()),
                    process.getCommand(),
                    String.valueOf(process.getParentPid())
            };
            res[i] = processData;
        }
        return res;
    }

    public void removeTable(){
        if(this.scrollPane == null){
            return;
        }
        this.remove(this.scrollPane);
        this.scrollPane = null;
        this.table = null;
    }
}

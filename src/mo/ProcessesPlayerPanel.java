package mo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/* En esta clase se realiza el rendereo de los datos de un snapshot de procesos en sí, que funciona junto con el player de MO*/
public class ProcessesPlayerPanel extends JPanel {

    private JsonObject processesSnapshotData;
    private static final String CAPTURE_MILLISECONDS_KEY = "captureMilliseconds";
    private static final String PID_KEY = "pid";
    private static final String USER_NAME_KEY = "userName";
    private static final String START_INSTANT_KEY = "startInstant";
    private static final String TOTAL_CPU_DURATION_KEY = "totalCpuDuration";
    private static final String COMMAND_KEY = "command";
    private static final String SUPPORTS_NORMAL_TERMINATION_KEY = "supportsNormalTermination";
    private static final String PARENT_PID_KEY = "parentPid";
    private static final String HAS_CHILDREN_KEY = "hasChildren";
    private static final String PROCESSES_KEY = "processes";
    private JScrollPane scrollPane;
    private JTable table;
    /* Estos textos deben ser internacionalizados*/
    private static final String[] TABLE_HEADERS = {"PID", "Username",
            "Start instant", "Total CPU Duration", "command", "Parent PID"};


    public void createTable(JsonObject processesSnapshotData){
        Object[][] formattedProcessesData = this.parseData(processesSnapshotData);
        this.table = new JTable(formattedProcessesData, TABLE_HEADERS);
        this.scrollPane = new JScrollPane(table);
        this.table.setFillsViewportHeight(true);
        this.add(this.scrollPane);
        this.setVisible(true);
    }

    public void updateData(JsonObject processesSnapshotData){
        Object[][] formattedProcessesData = this.parseData(processesSnapshotData);
        this.table.setModel(new DefaultTableModel(formattedProcessesData, TABLE_HEADERS));
    };

    private Object[][] parseData(JsonObject processesSnapshotData){
        JsonArray processes = processesSnapshotData.get(PROCESSES_KEY).getAsJsonArray();
        int size = processes.size();
        Object[][] res = new Object[size][];
        for(int i=0 ; i < size; i++){
            JsonObject process = processes.get(i).getAsJsonObject();
            String[] processData = new String[]{
                process.get(PID_KEY).getAsString(),
                process.get(USER_NAME_KEY).getAsString(),
                    process.get(START_INSTANT_KEY).getAsString(),
                    process.get(TOTAL_CPU_DURATION_KEY).getAsString(),
                    process.get(COMMAND_KEY).getAsString(),
                    process.get(PARENT_PID_KEY).getAsString()
            };
            res[i] = processData;
        }
        return res;
    }

    public void removeTable(){
        this.remove(this.table);
        this.setVisible(false);
    }
}

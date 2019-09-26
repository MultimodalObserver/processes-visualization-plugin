package mo.visualization.process.plugin.model;

public class Process {

    private long pid;
    private String userName;
    private String startInstant;
    private long totalCpuDuration;
    private String command;
    private long parentPid;
    private int hasChildren;
    private int supportsNormalTermination;
    private long captureTimestamp;


    public Process() {
    }

    public Process(String csvLine){
        String[] fields = csvLine.split(",");
        this.pid = Long.parseLong(fields[0]);
        this.captureTimestamp = Long.parseLong(fields[1]);
        this.userName = fields[2];
        this.startInstant = fields[3];
        this.totalCpuDuration = Long.parseLong(fields[4]);
        this.command = fields[5];
        this.supportsNormalTermination = Integer.parseInt(fields[6]);
        this.parentPid = Long.parseLong(fields[7]);
        this.hasChildren = Integer.parseInt(fields[8]);
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStartInstant() {
        return startInstant;
    }

    public void setStartInstant(String startInstant) {
        this.startInstant = startInstant;
    }

    public long getTotalCpuDuration() {
        return totalCpuDuration;
    }

    public void setTotalCpuDuration(long totalCpuDuration) {
        this.totalCpuDuration = totalCpuDuration;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getParentPid() {
        return parentPid;
    }

    public void setParentPid(long parentPid) {
        this.parentPid = parentPid;
    }

    public int getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(int hasChildren) {
        this.hasChildren = hasChildren;
    }

    public int getSupportsNormalTermination() {
        return supportsNormalTermination;
    }

    public void setSupportsNormalTermination(int supportsNormalTermination) {
        this.supportsNormalTermination = supportsNormalTermination;
    }

    public long getCaptureTimestamp() {
        return captureTimestamp;
    }

    public void setCaptureTimestamp(long captureTimestamp) {
        this.captureTimestamp = captureTimestamp;
    }
}

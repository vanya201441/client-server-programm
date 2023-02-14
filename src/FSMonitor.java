public interface FSMonitor {

    public static final int CREATED = 1;
    public static final int REMOVED = 2;

    public void fireEvent(String fName, int kind);

}

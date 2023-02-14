public class Client implements FSMonitor{

    @Override
    public void fireEvent(String fName, int kind) {
        switch (kind){
            case FSMonitor.CREATED -> {
                System.out.printf(fName + "created");
                break;
            }
            case FSMonitor.REMOVED -> {
                System.out.printf(fName + "removed");
                break;
            }
            default -> System.out.printf("ERROR");
        }
    }
}

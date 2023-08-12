import java.util.Date;

public interface SimulationInterface {

    void run(int id, Date date);

    int getId();

    Date getDate();
}

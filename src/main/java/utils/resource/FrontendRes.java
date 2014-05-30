package utils.resource;

/**
 * Created by Andrey
 * 24.04.14.
 */
public class FrontendRes implements Resource {

    private String refresh_period;
    private int log_interval;
    private int db_wait_time;

    public int getDb_wait_time() {
        return db_wait_time;
    }

    public String getRefresh_period() {
        return refresh_period;
    }

    public int getLog_interval() {
        return log_interval;
    }
}

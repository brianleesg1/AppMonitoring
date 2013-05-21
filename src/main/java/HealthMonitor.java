import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.boris.winrun4j.EventLog;

import java.io.IOException;


/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 21/5/13
 * Time: 8:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class HealthMonitor implements Runnable{
    Logger log = LogManager.getLogger(HealthMonitor.class);

    String url;
    String servicename;

    public HealthMonitor(String url,String servicename) {
        this.url = url;
        this.servicename = servicename;
    }

    private boolean connect() {
        HttpClient client = new HttpClient();

        HttpMethod method = new GetMethod(url);

        try {
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
                log.error("Connection failed - " + statusCode + " , " + method.getStatusLine());
                return false;
            }

            return true;

        } catch (HttpException he) {
            he.printStackTrace();
            log.error(he.getMessage());
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            log.error(ioe.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }


    }

    @Override
    public void run() {

        boolean status = this.connect();
        if (!status) {
            try {
                AppMonitorService.stopService(servicename);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Cannot stop " + servicename + " service");
            }

            try {
                AppMonitorService.startService(servicename);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Cannot start " + servicename + " service");
            }
        }
        else {
            log.info("JBOSS OK");
        }

    }
}

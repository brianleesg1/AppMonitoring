import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.boris.winrun4j.AbstractService;
import org.boris.winrun4j.EventLog;
import org.boris.winrun4j.ServiceException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 21/5/13
 * Time: 8:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class AppMonitorService extends AbstractService {
    Logger log = LogManager.getLogger(AppMonitorService.class);

    static final String AppMonitorService = "AppMonitorService";

    @Override
    public int serviceMain(String[] strings) throws ServiceException {

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


        int mon_interval = Integer.parseInt(strings[0]);
        String monitor_url = strings[1].trim();
        String monitor_servicename = strings[2].trim();
        int deploy_interval = Integer.parseInt(strings[3]);
        String deploy_tmp_folder = strings[4].trim();
        String deploy_folder = strings[5].trim();
        String deploy_filenames = strings[6].trim();

        log.info("\r\n" + "mon_interval = " + mon_interval + "\r\n" +
                 "monitor_url = " + monitor_url + "\r\n" +
                 "monitor_servicename = " + monitor_servicename + "\r\n" +
                "deploy_interval = " + deploy_interval + "\r\n" +
                "deploy_tmp_folder = " + deploy_tmp_folder + "\r\n" +
                "deploy_folder = " + deploy_folder + "\r\n" +
                "deploy_filenames = " + deploy_filenames + "\r\n");



        HealthMonitor mon = new HealthMonitor(monitor_url,monitor_servicename);
        scheduler.scheduleWithFixedDelay(mon,0,mon_interval,TimeUnit.MINUTES);

        DeploymentMonitor deploy = new DeploymentMonitor(deploy_tmp_folder,deploy_folder,deploy_filenames,monitor_servicename);
        scheduler.scheduleWithFixedDelay(deploy,0,deploy_interval,TimeUnit.MINUTES);

        while (!shutdown) {
        }

        scheduler.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.error("service cannot be terminated");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            scheduler.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        return 0;
    }


    public static final void main(String[] args) throws Exception {
        System.out.println("start");

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        int deploy_interval=1;
        String deploy_tmp_folder = "C:\\temp\\deploy";
        String deploy_folder = "C:\\jboss-as-7.1.1.Final\\standalone\\deployments";
        String deploy_filenames = "FileExplorer.war|vitas-runtime-ear.ear";
        String monitor_servicename = "JBOSS7";
        DeploymentMonitor deploy = new DeploymentMonitor(deploy_tmp_folder,deploy_folder,deploy_filenames,monitor_servicename);
        scheduler.scheduleWithFixedDelay(deploy,0,deploy_interval,TimeUnit.MINUTES);

    }

    public static void stopService(String servicename) throws Exception {
        Logger log = LogManager.getLogger(AppMonitorService.class);

        Process process = Runtime.getRuntime().exec("net stop " + servicename);
        process.waitFor();

        System.out.println("service stopped");
        log.info(servicename + " service stopped");


    }

    public static void startService(String servicename) throws Exception {
        Logger log = LogManager.getLogger(AppMonitorService.class);
        Process process = Runtime.getRuntime().exec("net start " + servicename);
        process.waitFor();

        System.out.println("service started");
        log.info(servicename + " service started");

    }

}

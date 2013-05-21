import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.boris.winrun4j.EventLog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 21/5/13
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeploymentMonitor implements Runnable {
    Logger log = LogManager.getLogger(DeploymentMonitor.class);
    String deploy_tmp_folder;
    String deploy_folder;
    ArrayList<String> filenames = new ArrayList<String>();
    String servicename;

    public DeploymentMonitor(String deploy_tmp_folder, String deploy_folder, String deploy_filenames, String servicename) {
        this.deploy_tmp_folder = deploy_tmp_folder;
        this.deploy_folder = deploy_folder;
        this.servicename = servicename;

        StringTokenizer token = new StringTokenizer(deploy_filenames,"|");
        while (token.hasMoreTokens()) {
            filenames.add(token.nextToken());
        }
    }

    @Override
    public void run()  {
        //log.info("DeploymentMonitor start");

        //EventLog.report(AppMonitorService.AppMonitorService, EventLog.INFORMATION, "Deploy monitor start");
        System.out.println(filenames.toString());

        for (int i=0; i<filenames.size(); i++) {
            String filename = filenames.get(i);

            File sourceFile = new File(deploy_tmp_folder + File.separator + filename);


            if (sourceFile.exists()) {
                try {
                    AppMonitorService.stopService(servicename);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    String backupFolderStr = deploy_tmp_folder + File.separator + SystemUtil.getSystemId();

                    File backupFolder = new File(backupFolderStr);

                    backupFolder.mkdir();

                    log.info("backup folder created - > " + backupFolderStr);
                    File backupFile = new File(backupFolderStr + File.separator + filename);

                    File destFile = new File(deploy_folder + File.separator + filename);
                    FileUtils.copyFile(sourceFile, destFile, true);
                    log.info("file transfered to deployment folder");
                    sourceFile.renameTo(backupFile);
                    log.info("Deployment completed");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    //EventLog.report(AppMonitorService.AppMonitorService, EventLog.ERROR,"Deployment failed");
                    log.error("Deployment failed - " + ioe.getMessage());
                } catch (Exception e) {
                    log.error(e.getMessage());
                }

                try {
                    AppMonitorService.startService(servicename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


}

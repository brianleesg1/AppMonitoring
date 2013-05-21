import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Brian
 *
 */
public class SystemUtil {

    private static final ConcurrentLinkedQueue<String> systemIdHistory = new ConcurrentLinkedQueue<String>();
    public static int MAX_SIZE_OF_ID_HISTORY=5000;


    private static final ThreadLocal<String> requestID =
            new ThreadLocal<String> () {
                @Override protected String initialValue() {
                    return getSystemId();
                }
            };


    @SuppressWarnings("unused")
    public static final String getSystemId() {

        String generatedId = null;

        // ammend by Chang Ming 10 Sep 2011
        //( to void the issue API will return duplicated ID when we loop to generate large numbers of system ID)
        while( true ){

            String timestamp = DateUtil.parseDate(new Date(System.currentTimeMillis()),"yyyyMMddHHmmssSSS");
            String randomNo = Integer.toString(RandomGenerator.nextInt(1000));
            generatedId =  timestamp + StringUtil.lPad(randomNo, 3, "0");

            if( !systemIdHistory.contains(generatedId) ){
                systemIdHistory.add(generatedId);

                if(systemIdHistory.size() > MAX_SIZE_OF_ID_HISTORY ){
                    systemIdHistory.poll();
                }

                break;
            }


        }

        return generatedId;

    }

    //This method returns a ThreadLocal id with tied to a thread. Normally used for Transaction ID/Request ID
    public static String getCurrentRequestID() {
        return requestID.get();
    }


    public static boolean isValidEmail(String email)
    {
        String trimedEmail;
        int x;
        int i;
        int j;
        int k = 0;
        int len;

        if (email == null||email == "") {
            return false;
        }
        trimedEmail = email.trim();
        len = trimedEmail.length();

        for (i = 0; (i < len) && (trimedEmail.charAt(i) != '@'); i++)
        {
            ;
        }

        for (x = 0; (x < len) && (trimedEmail.charAt(x) != '.'); x++)
        {
            ;
        }

        if ((i == 0) || (i >= len) || (x == 0) || (x >= len))
        {
            return false;
        }
        else
        {
            if (trimedEmail.charAt(i) == '.')
            {
                return false;
            }
            else
            {
                j = i + 1;

                for (i = j; i < len; i++)
                {
                    if (trimedEmail.charAt(i) == '.')
                    {
                        if (((i - 1) == k) || (i == (len - 1)) ||
                                (i == (len - 2)))
                        {
                            return false;
                        }

                        k = i;
                    }
                }
            }
        }

        return true;
    }

}

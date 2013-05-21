/*
 * ===========================================================
 * Copyright @2011 LTA
 *
 * Change Revision
 * -----------------------------------------------------------
 *   Date | Author | Remarks
 *   Jun 21, 2011 | Brian | xxxxxxxx
 *
 * ===========================================================
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.TreeMap;


/**
 * This class provide a single static instance of the Secure Random object  to
 * enable random number generation across different threads.<BR><BR>
 * NOTES ON UNIQUENESS <BR> 
 * ===================<BR>
 * It should be noted that randomness does not guarantee unqiueness. Mathematically speaking,
 * random numbers can ideally be unique if  <BR>
 * (1) the random number generation is uniform throughout the number space <BR>
 * (2) the boundaries of the random number is set infinitely large compared to the number of <BR>
 * generation. Nonetheless in reality, the random number has a limited size and the number of <BR>
 * generation is normally not known at the time of coding. <BR>
 * Hence it is adviced that the random number should be complimented with another sequential 
 * property (eg. timestamp) to ensure uniqueness. <BR><BR>
 * NOTES ON SECURITY <BR>
 * ================= <BR>
 * This class uses Computer Name + Class Loading Time + Another random number as the seed for
 * generation. For security reason, the seed generation algo + the pseudo random number 
 * generation algorithm should be kept a secret. The class uses Secure Hash Algorithm-1 
 * (SHA-1) for generating random number SHA-1 (with 160 bit) is more secure than SHA and MD5 
 * (128 bit) <BR><BR>
 * NOTES ON SPEED<BR>
 * ================= <BR>
 * The default provider from Java is slower. However, third party providers has other 
 * implications including licensing and security issues. For this reason, developer should 
 * weigh the different issues before deciding on the security provider.<BR><BR>
 *
 * Additional method:<BR>
 * ==================<BR>
 * A generateRandomPositions() method is added.<BR>
 * This caters to the reshuffling of positions of a given array of values.<BR><BR>
 *
 * @author Brian
 * @version 1.0
 *
 * @class RandomGenerator
 *
 */
public class RandomGenerator
{
    /** The single instance of the Random object */
    private static SecureRandom randomGenerator;

    /** The host name of the local machine */
    private static String hostName;

    /** The basis for generating the random number */
    private static String seed;

    /** A Log instance for this class */
    private static Log log = LogFactory.getLog(RandomGenerator.class);

    static
    {
        // Use the "SHA1PRNG" algorithm where possible
        // If not available, then use whatever is available
        try
        {
            randomGenerator = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException nsae)
        {
            randomGenerator = new SecureRandom();
        }

        //force system to seed itself
        randomGenerator.nextBytes(new byte[1]);

        // Create new seed = random + hostname + currenttime 
        seed = new String(randomGenerator.generateSeed(12));
        seed += System.currentTimeMillis();

        try
        {
            hostName = InetAddress.getLocalHost().toString();
        }
        catch (Exception e)
        {
            hostName = new String(randomGenerator.generateSeed(8));
        }

        seed += hostName;

        // Compliment System seed with our seed
        randomGenerator.setSeed(seed.getBytes());
        log.info("Provider is:" + randomGenerator.getProvider());
    }

    // Disallowed the users from accessing this object directly

    /*
       public static Random getRandomGenerator() {
               return randomGenerator;
       }
     */

    /**
     * Sets the seed of this random number generator using a single long seed.
     * The general contract of setSeed is  that it alters the state of this
     * random number generator object so as to be in exactly the same state as
     * if  it had just been created with the argument seed as a seed.
     *
     * @param seed the initial seed.
     *
     * @since 06/01/2003
     */
    public static synchronized void setSeed(long seed)
    {
        randomGenerator.setSeed(seed);
    }

    /**
     * Returns the specified array of values as an array of random order.
     *
     * @param inputs The list of values in which their order is to be randomised
     *
     * @return the specified array of values as an array of random order.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static synchronized Object[] generateRandomPositions(Object[] inputs)
    {
        HashMap unsortedMap = new HashMap();
        int inputSize = inputs.length;
        //Object [] sortedInputs = new Object[inputSize];

        for (int i = 0; i < inputSize; i++)
        {
            int keyValue = randomGenerator.nextInt();
            unsortedMap.put(new Integer(keyValue), inputs[i]);
        }

        // This is to ensure that the random positions' index contains no duplicate.
        if (inputSize!=unsortedMap.size()) {
            log.info("Original: inputSize: " + inputSize + "mapSize " + unsortedMap.size());
            return generateRandomPositions(inputs);
        }
        log.info("MapSize: " + unsortedMap.size());
        TreeMap sortedMap = new TreeMap(unsortedMap);
        log.info("SortedMap values: " + sortedMap.values().toString());

        return sortedMap.values().toArray();
    }
/*
 * For testing the logic at a smaller-scale.
	public static synchronized Object[] generateRandomPositionsTest(Object[] inputs)
    {
        HashMap unsortedMap = new HashMap();
        int inputSize = inputs.length;		
		Random r = new Random();
        for (int i = 0; i < inputSize; i++)        {	
            int keyValue = r.nextInt(10);
            unsortedMap.put(new Integer(keyValue), inputs[i]);
        }
        
        // This is to ensure that the random positions' index contains no duplicate.
        if (inputSize!=unsortedMap.size()) {
        	log.info("POHLIAN: inputSize: " + inputSize + "mapSize " + unsortedMap.size());
        	return generateRandomPositionsTest(inputs);        	
        }

        TreeMap sortedMap = new TreeMap(unsortedMap);
        log.info("SortedMap keys and values: " + sortedMap.toString());
        log.info("SortedMap values: " + sortedMap.values().toString());        

        return sortedMap.values().toArray();
    }
*/
    /**
     * Returns the next pseudorandom, uniformly distributed boolean value from
     * this random number generator's  sequence. The general contract of
     * nextBoolean is that one boolean value is pseudorandomly generated and
     * returned. The values true and false are produced with (approximately)
     * equal probability.
     *
     * @return the next pseudorandom, uniformly distributed boolean value from
     *         this random number generator's sequence.
     *
     * @since 06/01/2003
     */
    public static synchronized boolean nextBoolean()
    {
        return randomGenerator.nextBoolean();
    }

    /**
     * Generates random bytes and places them into a user-supplied byte array.
     * The number of random bytes produced is equal to the length of the byte
     * array.
     *
     * @param bytes the non-null byte array in which to put the random bytes.
     *
     * @since 06/01/2003
     */
    public static synchronized void nextBytes(byte[] bytes)
    {
        randomGenerator.nextBytes(bytes);
    }

    /**
     * Returns the next pseudorandom, uniformly distributed double value
     * between 0.0 and 1.0 from this random number generator's sequence.  The
     * general contract of nextDouble is that one double value, chosen
     * (approximately) uniformly from the range  0.0d (inclusive) to 1.0d
     * (exclusive), is pseudorandomly generated and returned.  All 253
     * possible float values of the form m x 2-53 , where m is a positive
     * integer less than 253, are  produced with (approximately) equal
     * probability.
     *
     * @return the next pseudorandom, uniformly distributed double value
     *         between 0.0 and 1.0 from this random number  generator's
     *         sequence.
     *
     * @since 06/01/2003
     */
    public static synchronized double nextDouble()
    {
        return randomGenerator.nextDouble();
    }

    /**
     * Returns the next pseudorandom, uniformly distributed float value between
     * 0.0 and 1.0 from this random number  generator's sequence.  The general
     * contract of nextFloat is that one float value, chosen (approximately)
     * uniformly from the range  0.0f (inclusive) to 1.0f (exclusive), is
     * pseudorandomly generated and returned.  All 224 possible float values
     * of the form m x 2-24, where m is a positive integer less than 224 , are
     * produced with (approximately) equal probability.
     *
     * @return the next pseudorandom, uniformly distributed float value between
     *         0.0 and 1.0 from this random number  generator's sequence.
     *
     * @since 06/01/2003
     */
    public static synchronized float nextFloat()
    {
        return randomGenerator.nextFloat();
    }

    /**
     * Returns the next pseudorandom, Gaussian ("normally") distributed double
     * value with mean 0.0 and standard  deviation 1.0 from this random number
     * generator's sequence.  The general contract of nextGaussian is that one
     * double value, chosen from (approximately) the usual normal
     * distribution with mean 0.0 and standard deviation 1.0, is
     * pseudorandomly generated and returned.
     *
     * @return the next pseudorandom, Gaussian ("normally") distributed double
     *         value with mean 0.0 and standard  deviation 1.0 from this
     *         random number generator's sequence.
     *
     * @since 06/01/2003
     */
    public static synchronized double nextGaussian()
    {
        return randomGenerator.nextGaussian();
    }

    /**
     * Generates a random unique ID
     *
     * @return a random unique ID
     *
     * @since 06/01/2003
     */
    public static synchronized String nextID()
    {
        long randomNo = randomGenerator.nextLong();
        String strRandomNo = Long.toHexString(randomNo);
        String strTimeStampVal = Long.toHexString(System.currentTimeMillis());
        char pad = '0';

        if (strRandomNo.length() < 16)
        {
            strRandomNo = StringUtil.rightAlignStr(strRandomNo, 16, pad);
        }

        if (strTimeStampVal.length() < 16)
        {
            strTimeStampVal = StringUtil.rightAlignStr(strTimeStampVal,
                    16, pad);
        }

        /* Randomize the sequence of the 2 Strings: Random Number and strTimeStamp Value.
         * To make sure that they will not be recognizable.
         */
        String temp = strRandomNo + strTimeStampVal;
        int len = temp.length();

        StringBuffer tempID = new StringBuffer();
        StringBuffer ID = new StringBuffer();

        for (int i = 1; i < len; i += 2)
        {
            /* Interchange the position for every 2 characters of the string
             * Eg: Before = abcd, After = badc
             */
            tempID.append(String.valueOf(temp.charAt(i))).append(String.valueOf(
                    temp.charAt(i - 1)));
        }

        /* Splits the above String into 4 string proportions and rearrange the sequence of the 4 string
         * proportions
         */
        ID.append(tempID.substring(8, 16)).append(tempID.substring(16, 26))
                .append(tempID.substring(0, 8)).append(tempID.substring(26, 32));

        return ID.toString();
    }

    /**
     * Returns the next pseudorandom, uniformly distributed int value from this
     * random number generator's sequence.  The general contract of nextInt is
     * that one int value is pseudorandomly generated and returned.  All 232
     * possible int values are produced with (approximately) equal
     * probability.
     *
     * @return the next pseudorandom, uniformly distributed int value from this
     *         random number generator's sequence.
     *
     * @since 06/01/2003
     */
    public static synchronized int nextInt()
    {
        return randomGenerator.nextInt();
    }

    /**
     * Returns a pseudorandom, uniformly distributed int value between 0
     * (inclusive) and the specified value ( exclusive), drawn from this
     * random number generator's sequence. The general contract of nextInt is
     * that one  int value in the specified range is pseudorandomly generated
     * and returned.  All n possible int values are produced with
     * (approximately) equal probability.
     *
     * @param n the bound on the random number to be returned. Must be
     *        positive.
     *
     * @return a pseudorandom, uniformly distributed int value between 0
     *         (inclusive) and n (exclusive).
     *
     * @since 06/01/2003
     */
    public static synchronized int nextInt(int n)
    {
        return randomGenerator.nextInt(n);
    }

    /**
     * Returns the next pseudorandom, uniformly distributed long value from
     * this random number generator's sequence.  The general contract of
     * nextLong is that one long value is pseudorandomly generated and
     * returned.  All 264 possible long values are produced with
     * (approximately) equal probability.
     *
     * @return the next pseudorandom, uniformly distributed long value from
     *         this random number generator's sequence.
     *
     * @since 06/01/2003
     */
    public static synchronized long nextLong()
    {
        return randomGenerator.nextLong();
    }
}

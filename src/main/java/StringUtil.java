
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


import java.util.*;


/**
 * @author Brian
 *
 */
public class StringUtil {
    static Logger logger = LogManager.getLogger(DeploymentMonitor.class);

    /**
     * Returns a string which is filled with a specified string. <br>
     * Example: dup("", 5) returns "", dup("OK", 3) returns "OKOKOK") repeated
     * for given number of times
     *
     * @param str
     *            String to be repeated/duplicated
     * @param n
     *            Number of time the string to be repeated/duplicated
     *
     * @return the resulted string
     *
     * @since 17/03/2000
     */

    // Special character set.
    public final static String[] SPL_CHARSET = new String[] { "~", "`", "!",
            "@", "#", "$", "%", "^", "&", "*", "-", "_", "=", "+", "{", "}",
            "[", "]", "|", "'", "\"", ":", ";", "?", ">", "<", ".", ",", " ",
            "(", ")", "\\", "/" };

    public static final String dup(String str, int n) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < n; i++) {
            result.append(str);
        }

        return (result.toString());
    }

    // end of dup()

    /**
     * Pads the given string with a user specified character on the left up to
     * the given length .<br>
     * Example: padString("ABCD", 10, 'X') returns "XXXXXXABCD" which has a
     * length of 10. This method has built-in 'intelligence' to handle cases
     * where calling method tries to be funny and supply the following<br>
     * - lPad("abc", 10, "123") it will return, "1231231abc"
     *
     * @param str
     *            String to be padded.
     * @param length
     *            The required length of the resulted string.
     * @param padStr
     *            The required padding string.
     *
     * @return the padded string. If <I>str</I> already longer than
     *         <I>length</I>, return <I>str</I> itself.
     *
     * @since 17/03/2000
     */
    public static final String lPad(String str, int length, String padStr) {
        int lOriginal = str.length();
        int lPadStr = padStr.length();
        int times2Pad = 0;
        int lPadded = 0;

        if (lOriginal >= length) {
            return str;
        }

        StringBuffer sb = new StringBuffer();
        String padded;
        times2Pad = (length - lOriginal) / lPadStr; // will give (1) if 3/2
        padded = dup(padStr, times2Pad);
        lPadded = padded.length();
        sb.append(padded); // pad in the repetitive characters

        // if still insufficient by the modulus Example: 30/20 is 10
        if ((lOriginal + lPadded) < length) {
            int more = length - (lOriginal + lPadded);

            // add in the difference which is less entire length of padStr
            sb.append(padStr.substring(0, more));
        }

        sb.append(str); // pad the original string behind

        return sb.toString();
    }

    // end of lPad()
    // ****************************************************************************//
    // ******************************* FORMATTING
    // *********************************//
    // ****************************************************************************//

    /**
     * Pads the given string with spaces up to the given length. <br>
     * Example: rPad("ABCD", 10, ' ') returns "ABCD      " which has a length of
     * 10. This method has built-in 'intelligence' to handle cases where calling
     * method tries to be funny and supply the following<br>
     * - rPad("abc", 10, "123") it will return, "abc1231231"
     *
     * @param str
     *            String to be padded
     * @param length
     *            The required length of the resulted string
     * @param padStr
     *            The required padding character.
     *
     * @return the padded string. If str already <I>longer</I> than
     *         <I>length</I>, return str itself.
     *
     * @since 17/03/2000
     */
    public static final String rPad(String str, int length, String padStr) {
        int lOriginal = str.length();
        int lPadStr = padStr.length();
        int times2Pad = 0;
        int lPadded = 0;

        if (lOriginal >= length) {
            return str;
        }

        StringBuffer sb = new StringBuffer(str); // add the original str first
        String padded;
        times2Pad = (length - lOriginal) / lPadStr; // will give (1) if 3/2
        padded = dup(padStr, times2Pad);
        lPadded = padded.length();
        sb.append(padded); // pad in the repetitive characters

        // if still insufficient by the modulus. Example: 30/20 is 10
        if ((lOriginal + lPadded) < length) {
            int more = length - (lOriginal + lPadded);

            // add in the difference which is less entire length of padStr
            sb.append(padStr.substring(0, more));
        }

        return sb.toString();
    }

    // end of rPad()

    /**
     * Indicates whether the byte array's values are all zeros by specifying the
     * byte array.
     *
     * @param byteArray
     *            An array of byte type
     *
     * @return True if the entire byte array values are all zeros, False
     *         otherwise
     */
    public static boolean isByteArrayZero(byte[] byteArray) {
        for (int i = 0; i < byteArray.length; i++) {
            if (byteArray[i] != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if the entire String s content is of alphabetical values (is
     * letters)
     *
     * @param s
     *            a string value
     *
     * @return True if the entire string s is of alphabetical values, False
     *         otherwise
     *
     * @since 20/04/2001
     */
    public static boolean isCharString(String s) {
        char a;

        try {
            for (int i = 0; i < s.trim().length(); i++) {
                a = s.charAt(i);

                if ((!Character.isWhitespace(a)) && (!Character.isLetter(a))) {
                    return false;
                }
            }

            return true;
        } catch (NullPointerException npe) {
            return true;
        }
    }

    /**
     * Indicates whether the specified string has the specified length.
     *
     * @param str
     *            The string.
     * @param intSize
     *            The desired size of the string.
     *
     * @return <CODE>true</CODE> if the specified string is of the specified
     *         length;<CODE>false</CODE> otherwise.
     */
    public static boolean isCorrectLength(String str, int intSize) {
        if (str == null) {
            return false;
        }

        int intLen = str.length();

        return (intLen == intSize) ? true : false;
    }

    // end of isCorrectLength ()

    /**
     * Indicates whether the specified string's length is less than or equal to
     * the specified length.
     *
     * @param str
     *            The string.
     * @param intSize
     *            The maximum length of the string.
     *
     * @return <CODE>true</CODE> if the specified string is less than or equal
     *         to the specified length;<CODE>false</CODE> otherwise.
     */
    public static boolean isLessMaxLength(String str, int intSize) {
        if (str == null) {
            return false;
        }

        int intLen = str.length();

        return (intLen <= intSize) ? true : false;
    }

    /**
     * Returns true if each character of the string s is of numerical value (is
     * digit)
     *
     * @param s
     *            a string value
     *
     * @return True if each character of the string s is of numerical value,
     *         False otherwise
     *
     * @since 20/04/2001
     */
    public static boolean isNumericString(String s) {
        char a;

        try {
            for (int i = 0; i < s.trim().length(); i++) {
                a = s.charAt(i);

                // if ((!Character.isWhitespace(a)) && (!Character.isDigit(a)))
                if ((!Character.isDigit(a))) {
                    return false;
                }
            }

            return true;
        } catch (NullPointerException npe) {
            return true;
        }
    }

    /**
     * Returns true if the string s contains only either letters or numbers.
     *
     * @param s
     *            A string value
     *
     * @return True if the string s contains only either letters or numbers ,
     *         False otherwise
     *
     * @since 20/04/2001
     */
    public static boolean isString(String s) {
        char a;

        try {
            for (int i = 0; i < s.trim().length(); i++) {
                a = s.charAt(i);

                if ((!Character.isWhitespace(a))
                        && (!Character.isLetterOrDigit(a)) && (a != '#')
                        && (a != '-')) {
                    return false;
                }
            }

            return true;
        } catch (NullPointerException npe) {
            return true;
        }
    }

    // end of isLessMaxLength ()

    /**
     * Indicates whether the string is a valid Double.
     *
     * @param str
     *            The string.
     * @param booNeg
     *            boolean value, <CODE>true</CODE> if negative value is allowed,
     *            <CODE>false</CODE> otherwise.
     *
     * @return <CODE>true</CODE> if the string is a valid Double;
     *         <CODE>false</CODE> otherwise.
     */
    public static boolean isValidDouble(String str, boolean booNeg) {
        double dblString;

        if (str == null) {
            return false;
        }

        try {
            dblString = Double.valueOf(str.trim()).doubleValue();
        } catch (Exception exc) {
            return false;
        }

        if ((!booNeg) && (dblString < 0)) {
            return false;
        }

        return true;
    }

    /**
     * Indicates if the substring of the specified string is white spaces or
     * not. Example: "F   1234A" -> F is followed by 3 whitespaces.<BR>
     * int start = 1;<br>
     * int limit = 4;<br>
     * By giving these start and limit values, only the 3 whitespaces will then
     * be checked on.
     *
     * @param start
     *            Index position of the start of the substring
     * @param limit
     *            Index position of the end of the substring
     * @param str
     *            The string that contains the substring to be checked
     *
     * @return True, if the substring of the specified string is white spaces.
     *         False, otherwise.
     */
    public static boolean isWhiteSpace(int start, int limit, String str) {
        String strSpace = str.substring(start, limit);

        char a;

        try {
            for (int i = 0; i < strSpace.length(); i++) {
                a = strSpace.charAt(i);

                if (!Character.isWhitespace(a)) {
                    return false;
                }
            }

            return true;
        } catch (NullPointerException npe) {
            return true;
        }
    }

    // end of isValidDouble ()

    /**
     * Converts an integer value to a String with the specified length.
     * Resulting String would be padded with leading zeros when the size of the
     * integer value is smaller than the specified length. Original length of
     * the String will be return if it's length is greater than the specified
     * length.
     *
     * @param value
     *            the integer value to be converted to string
     * @param length
     *            the length of the resulting string
     *
     * @return a string that is an integer value of a specified length or the
     *         String's original length.
     */
    public static String convertIntToStrOfLength(int value, int length) {
        String intStr = new Integer(value).toString();
        int lenDiff = length - intStr.length();

        for (int i = 0; i < lenDiff; i++) {
            intStr = "0" + intStr;
        }

        return intStr;
    }

    /**
     * Converts a long value to a String with the specified length. Resulting
     * String would be padded with leading zeros when the size of the integer
     * value is smaller than the specified length. Original length of the String
     * will be return if it's length is greater than the specified length.
     *
     * @param value
     *            the long value to be converted to string
     * @param length
     *            the length of the resulting string
     *
     * @return a string that is a long value of a specified length or the
     *         String's original length.
     */
    public static String convertLongToStrOfLength(long value, int length) {
        String longStr = new Long(value).toString();
        int lenDiff = length - longStr.length();

        for (int i = 0; i < lenDiff; i++) {
            longStr = "0" + longStr;
        }

        return longStr;
    }

    /**
     * Converts an object to its string representation, or an empty string if it
     * is null.
     *
     * @param obj
     *            The conversion object
     *
     * @return The string representation
     */
    public static String deNull(Object obj) {
        if (obj == null) {
            return "";
        }

        return obj.toString();
    }

    /**
     * Converts an object to its string representation, or the string argument
     * if it is null.
     *
     * @param obj
     *            The conversion object
     * @param substitute
     *            The substitution string for a null object parameter
     *
     * @return The string representation
     */
    public static String deNull(Object obj, String substitute) {
        if (obj == null) {
            return substitute;
        }

        return obj.toString();
    }

    /**
     * Converts a string to a specified length. Resulting String would be padded
     * with trailing of the character specified.
     *
     * @param value
     *            The string to be left align
     * @param length
     *            The length of the resulting string
     * @param character
     *            The specificed padding character
     *
     * @return a string value that is align on the left
     */
    public static String leftAlignStr(String value, int length, char character) {
        while (value.length() < length) {
            value += character;
        }

        return value;
    }

    /**
     * Remove all the special characters from the specified string
     *
     * @param str
     *            The string to which the special characters are to be removed.
     *
     * @return The string with all the special characters being removed from it.
     */
    public static String removeSpecialCharacters(String str) {
        if (str == null || str.trim().equals("")) {
            return "";
        }

        String tempVal1 = "";
        String tempVal2 = "";

        for (int i = 0; i < SPL_CHARSET.length; i++) {
            tempVal2 = tempVal1 + " ";
            tempVal1 = tempVal2;
        }

        str = str + tempVal1;

        for (int i = 0; i < SPL_CHARSET.length; i++) {
            str = replaceStr(str, SPL_CHARSET[i], "");
        }

        return str;
    }

    /**
     * Replace the searchkey within the existing string with the required value
     *
     * @param msgToReplace
     *            Existing String to be replace
     * @param searchKey
     *            Key within the String to be searched and replaced
     * @param value
     *            The replaced value
     *
     * @return the resulted string
     */
    public static String replaceStr(String msgToReplace, String searchKey,
                                    String value) {
        return replaceStr(msgToReplace, searchKey, value, "", "");
    }

    /**
     * Replace the searchkey within the existing string with the required value
     * If the any of the input parameters (searchKey/msgToReplace/value) is
     * null, the original message is returned. (Case-Sensitive)
     *
     * @param msgToReplace
     *            Existing String to be replace
     * @param searchKey
     *            Key within the String to be searched and replaced
     * @param value
     *            The replaced value
     * @param msgPrefix
     *            Prefix defined (if any) before the searchKey
     * @param msgSuffix
     *            Suffix defined (if any) after the searchKey
     *
     * @return the resulted string
     */
    public static String replaceStr(String msgToReplace, String searchKey,
                                    String value, String msgPrefix, String msgSuffix) {
        if (msgPrefix == null) {
            msgPrefix = "";
        }
        if (msgSuffix == null) {
            msgSuffix = "";
        }
        if (searchKey == null || msgToReplace == null || value == null) {
            return msgToReplace;
        }

        int count = 0;
        String msg = new String(msgToReplace);
        String key = msgPrefix + searchKey + msgSuffix;
        int pos = -1;

        while ((pos = msg.indexOf(key, count)) >= 0) {
            // peong 17102000, added 1 line
            StringBuffer sb = new StringBuffer(msg);
            sb.replace(pos, pos + key.length(), value);
            count = pos + value.length();

            // peong 17102000, added 1 line
            msg = sb.toString();
        }

        return msg;
    }

    /**
     * Converts a string to a specified length. Resulting String would be padded
     * with leading of the character specified.
     *
     * @param value
     *            The string to be right align
     * @param length
     *            The length of the resulting string
     * @param character
     *            The specificed padding character
     *
     * @return a string value that is align on the right
     */
    public static String rightAlignStr(String value, int length, char character) {
        while (value.length() < length) {
            value = character + value;
        }

        return value;
    }

    /**
     * Tokenizes the specified string into a Collection.
     *
     * @param tokenizeStr
     *            The string to be tokenized.
     * @param delimiter
     *            The separator used within the string
     *
     * @return an Collection of the specified string
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Collection tokenize(String tokenizeStr, String delimiter) {
        StringTokenizer st = new StringTokenizer(tokenizeStr, delimiter);
        ArrayList originalTypes = new ArrayList();

        while (st.hasMoreTokens()) {
            String type = st.nextToken();
            originalTypes.add(type);
        }

        return originalTypes;
    }

    /**
     * Tokenize the Specified String into the ArrayList
     *
     * @author A.Thennarasu
     * @since 27 Oct 2004
     * @param tokenizeStr
     *            The string to be tokenized.
     * @param delimiter
     *            The separator used within the string
     *
     * @return an Collection of the specified string
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Collection tokenize(String tokenizeStr) {
        // Instantiate the ArrayList
        ArrayList originalTypes = new ArrayList();

        // Iterate the String
        for (int i = 0; i < tokenizeStr.length(); i++) {
            // Declare the String variable
            String nameResult = null;

            // check the size of the variable i
            if (i == 0) {
                // subString the String
                nameResult = tokenizeStr.substring(0, 1);

            }// end if
            else {
                // subString the Variable
                nameResult = tokenizeStr.substring(i, i + 1);

            }// end else

            // add the String into the ArrayList
            originalTypes.add(nameResult);

        }// end for loop

        // return ArrayList
        return originalTypes;

    }// end tokenize() method

    /**
     * Trims the string input if it is not null.
     *
     * @param str
     *            The string to be trimmed
     *
     * @return The trimmed string or null, if the input string is null
     */
    public static String trim(String str) {
        if ((str == null) || (str.length() == 0)) {
            return str;
        }

        return str.trim();
    }

    /**
     * Converts the input string to UpperCase if it is not null.
     *
     * @param str
     *            The string to be trimmed
     *
     * @return The uppercase converted string or null, if the input string is
     *         null
     */
    public static String toUpperCase(String str) {
        if ((str == null) || (str.length() == 0)) {
            return str;
        }

        return str.toUpperCase();
    }

    /**
     * Indicate if the specified string is null or is an empty string.
     *
     * @param str
     *            The string to be test
     *
     * @return True, if the specified string is null or is an empty string;
     *         False, Otherwise.
     */
    public static boolean isNullOrEmpty(String str) {
        if ((str == null) || (str.trim().length() == 0)) {
            return true;
        }
        return false;
    }

    /**
     * Remove line feed (hex 0a) from the string
     *
     * @param str
     *            The string to which the line feed characters are to be
     *            removed.
     *
     * @return The string without the line feed characters.
     */

    public static String removeLineFeed(String str) {
        if (isNullOrEmpty(str))
            return str;
        String tmpStr = "";
        int l = str.length();

        if ((str == null) || (str.length() == 0)) {
            return tmpStr;
        }
        for (int i = 0; i < l; i++) {
            if (str.charAt(i) != '\n') {
                tmpStr = tmpStr + str.charAt(i);
            }
        }
        return tmpStr;
    }

    /**
     * Return the masked string by specifying the string to be masked, and the
     * character of the masked character and the length of the last few
     * characters that should not be masked.
     *
     * @param toBeMasked
     *            The string to be masked
     * @param maskedChar
     *            The character of the masked representation
     * @param lenOfUnmask
     *            The length of the last few characters that should not be
     *            masked
     * @return the masked string by specifying the string to be masked, and the
     *         character of the masked character and the length of the last few
     *         characters that should not be masked.
     */
    public static String maskStringExceptLastFewChar(String toBeMasked,
                                                     String maskedChar, int lenOfUnmaskStr, int lenOfUnmaskEnd) {
        String maskedStr = StringUtil.trim(toBeMasked);
        if (!StringUtil.isNullOrEmpty(toBeMasked)) {
            int lenOfStr = toBeMasked.length();
            int lenOfStrToBeMasked = lenOfStr - lenOfUnmaskEnd - lenOfUnmaskStr;
            if (lenOfStrToBeMasked > 0) {
                String tmpMaskedStr = "";
                maskedStr = StringUtil.lPad(tmpMaskedStr, lenOfStrToBeMasked,
                        maskedChar);
                String unmaskedStrStart = toBeMasked.substring(0,
                        lenOfUnmaskStr);
                String unmaskedStrEnd = toBeMasked.substring(lenOfStrToBeMasked
                        + lenOfUnmaskStr);
                maskedStr = unmaskedStrStart + maskedStr + unmaskedStrEnd;
            } else {
                return maskedStr;
            }
        }
        return maskedStr;
    }

    /**
     * Replace the specified string for characters that are senstive to HTML
     * interpreters, returning the string with these characters replaced by the
     * corresponding character entities.
     *
     * @param value
     *            The string to be filtered and returned
     */
    public static String replaceHTMLSensitive(String value) {

        if (value == null)
            return (null);

        char content[] = new char[value.length()];
        value.getChars(0, value.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                case '\'':
                    result.append("&#39;");
                    break;
                default:
                    result.append(content[i]);
            }
        }
        return (result.toString());

    }

    /**
     * Returns true if each character of the string s is of numerical value (is
     * digit) Trim() method specially not done for the string to cater for
     * spaces found after the numeric digits in the ID
     *
     * @param s
     *            a string value
     *
     * @return True if each character of the string s is of numerical value,
     *         False otherwise
     *
     * @since 27/12/2005
     */
    public static boolean isNumericStringForID(String s) {
        char a;

        try {
            for (int i = 0; i < s.length(); i++) {
                a = s.charAt(i);

                if ((!Character.isDigit(a))) {
                    return false;
                }
            }

            return true;
        } catch (NullPointerException npe) {
            return true;
        }
    }

    /**
     * Parse the string into a map
     *
     * @param s
     *            the source string
     * @param separator
     *            the elements' separator
     * @return the parsed map
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map parseMap(String s, String separator) {
        if (s == null || separator == null) {
            return null;
        }
        Map map = new HashMap();
        if (s.length() == 0) {
            return map;
        }
        if (s.charAt(0) == '{') {
            s = s.substring(1);
        }
        if (s.charAt(s.length() - 1) == '}') {
            s = s.substring(0, s.length() - 1);
        }
        Collection c = StringUtil.tokenize(s, separator);
        List param = new ArrayList();
        param.addAll(c);
        for (int i = 0; i < param.size(); i++) {
            String ss = (String) param.get(i);
            c = StringUtil.tokenize(ss, "=");
            List l = new ArrayList();
            l.addAll(c);
            if (l != null && l.size() >= 1) {
                Object key = l.get(0);
                Object value = null;
                if (l.size() == 2) {
                    value = l.get(1);
                }
                if (key != null) {
                    if (value != null && !value.equals("")) {
                        map.put(key, value);
                    } else {
                        map.put(key, null);
                    }
                }
            }
        }
        return map;
    }

    /** Formats a map */
    @SuppressWarnings("rawtypes")
    public static String formatMap(Map map) {
        if (map != null) {
            return map.toString();
        }
        return null;
    }

    /**
     * Formats a map into string
     *
     * @param map
     *            the map to be formatted
     * @param separator
     *            the separator to separate the elements
     * @param enclosing
     *            to include the enclosing braces or not
     * @return the formatted string
     */
    @SuppressWarnings("rawtypes")
    public static String formatMap(Map map, String separator, boolean enclosing) {
        StringBuffer sb = new StringBuffer();
        if (map == null || separator == null) {
            return null;
        }
        Iterator iter = map.keySet().iterator();
        if (enclosing) {
            sb.append("{");
        }
        while (iter.hasNext()) {
            Object key = iter.next();
            Object value = map.get(key);
            if (key != null) {
                sb.append(key).append("=");
                if (value != null) {
                    sb.append(value);
                }
                sb.append(separator);
            }
        }
        int len = separator.length();
        if (sb.length() > len) {
            int startIndex = sb.length() - len;
            int endIndex = sb.length();
            String s = sb.substring(startIndex, endIndex);
            if (s.equals(separator)) {
                for (int i = 0; i < len; i++) {
                    sb.deleteCharAt(sb.length() - 1);
                }
            }
        }
        if (enclosing) {
            sb.append("}");
        }
        return sb.toString();
    }

    // This function extract the first occurrence of integer value from string.
    // Example "255 ABC" return 255, "AB 255 C" return 255, "ABC 255" return
    // 255, else return null
    public static Integer extract(String str) {
        if (str == null) {
            return null;
        }

        StringTokenizer token = new StringTokenizer(str.trim(), " ");

        while (token.hasMoreTokens()) {
            String tmp = token.nextToken();
            if (StringUtil.isNumericString(tmp)) {
                return Integer.valueOf(tmp);
            }
        }

        return null;
    }

    /**
     * convert less or bigger to html value
     *
     * @param String
     * @return String
     */
    public static String htmlWrite(String s) {
        String ret = "";

        if (s == null || "".equals(s)) {
            return ret;
        }

        ret = s.replaceAll("<", "&lt;");
        ret = ret.replaceAll(">", "&gt;");

        return ret;
    }

    /**
     * This method is restrictedly used ONLY for special handling for APPBASE block
     * message.
     *
     * @param String
     * @return String
     */
    public static String htmlWriteSpecialHandleBlockMsg(String s) {
        String s1 = "";
        if (s == null || "".equals(s.trim())) {
            return s1;
        }

        int beginIndex = 0;
        for (int i = 0; i <= s.length(); i++) {
            if (i > 7) { // check for <script>
                if ("<".equalsIgnoreCase(s.substring(i - 8, i - 7))
                        && "s".equalsIgnoreCase(s.substring(i - 7, i - 6))
                        && "c".equalsIgnoreCase(s.substring(i - 6, i - 5))
                        && "r".equalsIgnoreCase(s.substring(i - 5, i - 4))
                        && "i".equalsIgnoreCase(s.substring(i - 4, i - 3))
                        && "p".equalsIgnoreCase(s.substring(i - 3, i - 2))
                        && "t".equalsIgnoreCase(s.substring(i - 2, i - 1))
                        && ">".equalsIgnoreCase(s.substring(i - 1, i))) {
                    s1 = s1 + s.substring(beginIndex, i - 8) + "&lt;"
                            + s.substring(i - 7, i - 1) + "&gt;";
                    beginIndex = i;
                }
            }
        }
        if (beginIndex < s.length()) {
            s1 = s1 + s.substring(beginIndex, s.length());
        }

        s = s1;
        s1 = "";
        beginIndex = 0;
        for (int i = 0; i <= s.length(); i++) {
            if (i > 8) { // check for <script>
                if ("<".equalsIgnoreCase(s.substring(i - 9, i - 8))
                        && "/".equalsIgnoreCase(s.substring(i - 8, i - 7))
                        && "s".equalsIgnoreCase(s.substring(i - 7, i - 6))
                        && "c".equalsIgnoreCase(s.substring(i - 6, i - 5))
                        && "r".equalsIgnoreCase(s.substring(i - 5, i - 4))
                        && "i".equalsIgnoreCase(s.substring(i - 4, i - 3))
                        && "p".equalsIgnoreCase(s.substring(i - 3, i - 2))
                        && "t".equalsIgnoreCase(s.substring(i - 2, i - 1))
                        && ">".equalsIgnoreCase(s.substring(i - 1, i))) {
                    s1 = s1 + s.substring(beginIndex, i - 9) + "&lt;"
                            + s.substring(i - 8, i - 1) + "&gt;";
                    beginIndex = i;
                }
            }
        }
        if (beginIndex < s.length()) {
            s1 = s1 + s.substring(beginIndex, s.length());
        }

        return s1;

		/*
		 * String msg = htmlWrite(str);
		 * 
		 * if (msg==null || msg.equals("")) { return ""; }
		 * 
		 * return msg.replaceAll("&lt;br&gt;", "<br>");
		 */
    }

    public static String addBlankSpace(String source, int intTotLength)
    {
        if (isNullOrEmpty(source)){
            source = Constants.EMPTY_STR;
            for (int i = 0; i < intTotLength; i++){
                source = source + Constants.SPACE;;
            }
            return source;
        }
        source = trim(source);
        int intSourceLen = source.length();
        int intAddSpace = intTotLength - intSourceLen;
        for (int i = 0; i < intAddSpace; i++){
            source = source + Constants.SPACE;;
        }
        return source;
    }

    public static String addZero(String source, int intTotLength)
    {

        if (isNullOrEmpty(source)){
            return Constants.EMPTY_STR;
        }
        source = trim(source);
        int intSourceLen = source.length();
        int intAddSpace = intTotLength - intSourceLen;
        for (int i = 0; i < intAddSpace; i++){
            source = Constants.ZERO_VALUE + source;
        }
        return source;
    }


    /**
     *
     * parse hex value to binary string,
     *
     * for example "2aaaa9c72aaaa9c72aaaa9c7", 
     * API will return: 1010101010101010101001110001110010101010101010101010011100011100101010101010101010100111000111
     *
     * @param hexValue
     * @return
     */
    public static String hexToBinaryStr(String hexValue){

        if( StringUtils.isBlank(hexValue) ){
            return "0";
        }

        // Note, he maxLeng cannot greater than 15, because the value will exceeds max value of Long can store.
        int maxLen = 10;
        int len = hexValue.length();
        int times = len / maxLen;
        if( (len % maxLen) > 0 ){
            times ++;
        }

        StringBuffer bineryValueSb = new StringBuffer();

        for( int i = 0 ; i < times ; i++){

            int start = i*maxLen;
            int end = (i+1)*maxLen;
            String tmpVal = StringUtils.substring(hexValue, start , end);
            long parsedLongValue = Long.parseLong(tmpVal, 16);

            String binaryValue = Long.toBinaryString(parsedLongValue);

            if( i != 0 ){
                binaryValue = StringUtils.leftPad(binaryValue, tmpVal.length() * 4 , "0");
            }

            bineryValueSb.append(binaryValue);

        }

        return bineryValueSb.toString();

    }

    /**
     *
     * Method will return the position list (bit value equals = 1)
     *
     *  For example:   Hex:"6AAAA9C7"  Binary Value:"01101010101010101010100111000111",
     *
     *                 API will return {0,1,2,6,7,8,11,13,15,17 , 19 , 21 , 23 , 25 ,27 , 29 , 30}
     *
     * @param hexValue
     * @return
     */
    public static List<Integer> getTrueValuePosition(String hexValue){

        String binaryVal = hexToBinaryStr(hexValue);

        List<Integer> trueBitPostList = new ArrayList<Integer>();

        String reversedBinaryVal = StringUtils.reverse(binaryVal);

        for( int i = 0 ; i < reversedBinaryVal.length() ; i++ ){

            char tmpChar = reversedBinaryVal.charAt(i);

            if(logger.isInfoEnabled()){
                logger.info("Char["+i+"]:"+tmpChar);
            }

            if( tmpChar == '1' ){
                trueBitPostList.add(i);
                if(logger.isInfoEnabled()) logger.info("Error code position:"+i) ;
            }

        }

        return trueBitPostList;

    }


    /**
     * Merge the given String arrays into one.
     * @param array1 the first array (can be <code>null</code>)
     * @param array2 the second array (can be <code>null</code>)
     * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
     */
    public static String[] mergeStringArrays(String[] array1, String[] array2) {
        if ( null == array1 ) {
            return array2;
        }
        if ( null == array2 ) {
            return array1;
        }
        List<String> result = new ArrayList<String>();
        result.addAll(Arrays.asList(array1));
        result.addAll(Arrays.asList(array2));

        return result.toArray(new String[0]);
    }

    /**
     * [Null]   ==> true
     * [Object] ==> false
     * ""		==> true
     * " "      ==> true
     *
     * @param obj
     * @return
     */
    public static boolean isNullObjOrEmptyString(Object obj){

        if( null == obj ) return true;

        if( obj instanceof String ) {
            return isNullOrEmpty((String)obj);
        }

        return false;

    }


}

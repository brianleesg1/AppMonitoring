/*
 * =============================================
 * Copyright @2002 Land Transport Authority
 *
 * Change Revision
 * ---------------------------------------------
 *   Date      	  Author      	Remarks
 *   12/12/2002  Chee Poh Lian  Reusable Components - DateUtil class
 *				Consolidated and Tested from Framework Team
 *				Codes contributed by Chee Ying, 
 *				Raymond and Rudy.
 *
 *   29/04/2004  NCS VRLS Team  Modify the method toMMFormat()
 *                              to include checks for months in uppercase as well
 *                              The old code can convert "Jan" to "01" for month
 *                              but not "JAN"
 * 
 *   11/05/2004  Jun Shan       Add the getDurationDates() method to get the target date(s)
 *                              after(before) a specified period from a marked date

 *
 * =============================================
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 * This DateUtil class provides methods for formatting, validating and
 * retrieving different types of dates. <BR><BR>  Note: Struts validator also
 * provides a similar Date validator, one may use either one of  one's
 * preference for validating the validility of the date.
 *
 * @author Framework Team
 * @version 1.0
 *
 * @class DateUtil
 * @since 12/12/2002
 */
public class DateUtil
{
    /** A log instance for this class */
    private static Log log = LogFactory.getLog(DateUtil.class);

    // Dates constants

    /** "JAN" to represent January */
    public static final String MTH_JAN = "Jan";

    /** "FEB" to represent February */
    public static final String MTH_FEB = "Feb";

    /** "MAR" to represent March */
    public static final String MTH_MAR = "Mar";

    /** "APR" to represent April */
    public static final String MTH_APR = "Apr";

    /** "MAY" to represent May */
    public static final String MTH_MAY = "May";

    /** "JUNE" to represent June */
    public static final String MTH_JUN = "Jun";

    /** "JUL" to represent July */
    public static final String MTH_JUL = "Jul";

    /** "AUG" to represent August */
    public static final String MTH_AUG = "Aug";

    /** "SEP" to represent September */
    public static final String MTH_SEP = "Sep";

    /** "OCT" to represent October */
    public static final String MTH_OCT = "Oct";

    /** "NOV" to represent November */
    public static final String MTH_NOV = "Nov";

    /** "DEC" to represent December */
    public static final String MTH_DEC = "Dec";

    /** The date pattern in this format 'ddMMyyyy  HHmm' */
    public static String DATETIME_FORMAT = "ddMMyyyy  HHmm";

    //Add By Xi Wei on 21 Dec 2004
    public static String DATE_FORMAT = "dd MMM yyyy";
    public static String TIMESTAMP_FORMAT = "dd MMM yyyy HH:mm:ss";

    // Added by Lee for SR001 - Bus Seat Belt
    public static String INPUT_DATE_FORMAT = "ddMMyyyy";

    // Added by richardg 19 Dec 2009 for car park bidding time
    public static String TIME_FORMAT = "HHmm";

    //Added by Daniel Han June 03 2010
    public static String DATETIME_FORMAT_A = "yyyyMMddHHmm";
    //Added by Daniel Han June 03 2010
    public final static String DATETIME_FORMAT_B = "dd/MM/yyyy HH:mm:ss.SSS";
    //Added by Daniel Han June 03 2010
    public final static String DATETIME_FORMAT_C = "yyyy,MM,dd,HH,mm";

    /**
     * Returns a date object from input string indicating year, month and day
     *
     * @param year Year Indicator
     * @param month Month indicator, 1=jan 2=feb ...
     * @param day Date indicator eg: any day from 1...31.
     *
     * @return date java.util.Date object in millisecond.
     *
     * @since 15/05/2000
     */
    public static Date getDate(int year, int month, int day)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, 0, 0, 0);

        return cal.getTime();
    }

    /**
     * Compares the 2 dates: Returns true if the 2 dates are equal.
     *
     * @param date1 Date to compare
     * @param date2 Date to compare
     *
     * @return true if <code>date1</code> equals to <code>date2</code>.
     *
     * @since 24/04/2001
     */
    public static boolean isDateEqual(Date date1, Date date2)
    {
        if ((date1 == null) || (date2 == null))
        {
            return false;
        }

        return resetTime(date1).compareTo(resetTime(date2)) == 0;
    }

    /**
     * Sets the default timezone to the specified timezone ID.  Note: The
     * system time will remain unchanged. Only the Time zone for the current
     * thread is set.
     *
     * @param timeZoneID The timezone ID. Example: "America/Los_Angeles", "CCT"
     *        which stands for China/Taiwan = S'pore
     */
    public static void setDefaultTimeZone(String timeZoneID)
    {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZoneID));
    }

    /**
     * Calculates the elapsed time between 2 dates. The elapsed time calculated
     * could either be in years, months or days
     *
     * @param type (int) The variable type determines the calculation of the
     *        elapsed time to be based on either years, months or days. To
     *        compute the elapsed time in year input type set to Calendar.YEAR
     *        To compute the elapsed time in month input type set to
     *        Calendar.MONTH  By default the elapsed time will compute in days
     * @param startDate start date
     * @param endDate end date
     *
     * @return the elapsed time (int)
     */
    public static final int getElapsedTime(int type, Date startDate,
                                           Date endDate)
    {
        int elapsed = 0;

        if ((startDate == null) || (endDate == null))
        {
            return -1;
        }

        if (startDate.after(endDate))
        {
            return -1;
        }

        GregorianCalendar gc1 = (GregorianCalendar) GregorianCalendar.getInstance();
        GregorianCalendar gc2 = (GregorianCalendar) gc1.clone();
        gc1.setTime(startDate);
        gc2.setTime(endDate);

        gc1.clear(Calendar.MILLISECOND);
        gc1.clear(Calendar.SECOND);
        gc1.clear(Calendar.MINUTE);
        gc1.clear(Calendar.HOUR_OF_DAY);
        gc2.clear(Calendar.MILLISECOND);
        gc2.clear(Calendar.SECOND);
        gc2.clear(Calendar.MINUTE);
        gc2.clear(Calendar.HOUR_OF_DAY);

        if ((type != Calendar.MONTH) && (type != Calendar.YEAR))
        {
            type = Calendar.DATE;
        }

        if (type == Calendar.MONTH)
        {
            gc1.clear(Calendar.DATE);
            gc2.clear(Calendar.DATE);
        }

        if (type == Calendar.YEAR)
        {
            gc1.clear(Calendar.DATE);
            gc2.clear(Calendar.DATE);
            gc1.clear(Calendar.MONTH);
            gc2.clear(Calendar.MONTH);
        }

        while (gc1.before(gc2))
        {
            gc1.add(type, 1);
            elapsed++;
        }

        return elapsed;
    }

    /**
     * This method will determine if the date is the last day of the month
     *
     * @param date date
     *
     * @return returns true if the date falls on the last day of the month else
     *         returns false
     */
    public static final boolean isEndOfTheMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (cal.get(Calendar.DATE) == maxDay)
        {
            return true;
        }

        return false;
    }

    /**
     * This method will determine if the date is the last day of the year
     *
     * @param date date
     *
     * @return returns true if the date falls on the last day of the year else
     *         returns false
     */
    public static final boolean isEndOfTheYear(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if ((cal.get(Calendar.MONTH) == 11) && (cal.get(Calendar.DATE) == 31))
        {
            return true;
        }

        return false;
    }

    /**
     * This method will return the last day of the months
     *
     * @param date date
     *
     * @return returns the last day of the month
     */
    public static final int getLastDayOfTheMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Returns the numeric value of the specified month
     *
     * @param month The 3 letter month representation. Example: "Jan", "Feb", etc
     *
     * @return the numeric value of the specified month
     */
    public static int getMthInInt(String month)
    {
        if (month.equalsIgnoreCase(MTH_JAN))
        {
            return 1;
        }
        else if (month.equalsIgnoreCase(MTH_FEB))
        {
            return 2;
        }
        else if (month.equalsIgnoreCase(MTH_MAR))
        {
            return 3;
        }
        else if (month.equalsIgnoreCase(MTH_APR))
        {
            return 4;
        }
        else if (month.equalsIgnoreCase(MTH_MAY))
        {
            return 5;
        }
        else if (month.equalsIgnoreCase(MTH_JUN))
        {
            return 6;
        }
        else if (month.equalsIgnoreCase(MTH_JUL))
        {
            return 7;
        }
        else if (month.equalsIgnoreCase(MTH_AUG))
        {
            return 8;
        }
        else if (month.equalsIgnoreCase(MTH_SEP))
        {
            return 9;
        }
        else if (month.equalsIgnoreCase(MTH_OCT))
        {
            return 10;
        }
        else if (month.equalsIgnoreCase(MTH_NOV))
        {
            return 11;
        }
        else if (month.equalsIgnoreCase(MTH_DEC))
        {
            return 12;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Return the date of the next working day
     *
     * @return the date of the next working day
     */
    public static Date getNextWorkingDay()
    {
        Date nextWorkingDay = DateUtil.addDaysToDate(DateUtil.getSystemDate(), 1);
        Calendar c = Calendar.getInstance();
        c.setTime(nextWorkingDay);

        int day = c.get(Calendar.DAY_OF_WEEK);

        if (day == Calendar.SUNDAY)
        {
            nextWorkingDay = DateUtil.addDaysToDate(nextWorkingDay, 1);
        }

        return nextWorkingDay;
    }

    /**
     * Compares the 2 dates: Returns true if the start date is before the end
     * date.
     *
     * @param startDate Starting date of a particular time period.
     * @param endDate Ending date of a particular time period.
     *
     * @return true if the <code>startDate</code> is before
     *         <code>endDate</code>.
     *
     * @since 24/03/2001
     */
    public static boolean isStartBeforeEndDate(Date startDate, Date endDate)
    {
        if ((startDate == null) || (endDate == null))
        {
            return false;
        }

        return resetTime(startDate).compareTo(resetTime(endDate)) < 0;
    }

    /**
     * This method will determine if the date occurs on the beginning of the
     * month
     *
     * @param date date
     *
     * @return returns true if date is on the beginning of the month else
     *         returns false
     */
    public static final boolean isStartOfTheMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if (cal.get(Calendar.DATE) == 1)
        {
            return true;
        }

        return false;
    }
    /**
     * This method will return
     * month
     *
     * @param date date
     *
     * @return returns month in int
     *
     */
    public static final int getMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.MONTH) ;

    }

    public static final int getYear(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.YEAR) ;

    }

    public static final int getDay(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.DAY_OF_MONTH) ;

    }

    public static int getWeekDay(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * This method will determine if the date occurs at the beginning of the
     * year
     *
     * @param date date
     *
     * @return returns true if the date occurs on the beginning of the year
     *         else returns false
     */
    public static final boolean isStartOfTheYear(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if ((cal.get(Calendar.MONTH) == 1) && (cal.get(Calendar.DATE) == 1))
        {
            return true;
        }

        return false;
    }

    /**
     * Returns the corresponding 3 letter string value of the month specified by numeric month value
     *
     * @param month The numeric value of the specified month
     *
     * @return the corresponding 3 letter string value of the month specified by numeric month value
     */
    public static String getStrMth(int month)
    {
        if (month == 1)
        {
            return MTH_JAN;
        }
        else if (month == 2)
        {
            return MTH_FEB;
        }
        else if (month == 3)
        {
            return MTH_MAR;
        }
        else if (month == 4)
        {
            return MTH_APR;
        }
        else if (month == 5)
        {
            return MTH_MAY;
        }
        else if (month == 6)
        {
            return MTH_JUN;
        }
        else if (month == 7)
        {
            return MTH_JUL;
        }
        else if (month == 8)
        {
            return MTH_AUG;
        }
        else if (month == 9)
        {
            return MTH_SEP;
        }
        else if (month == 10)
        {
            return MTH_OCT;
        }
        else if (month == 11)
        {
            return MTH_NOV;
        }
        else if (month == 12)
        {
            return MTH_DEC;
        }
        else
        {
            return "";
        }
    }

    /**
     * Calculates the duration in years, months and days.
     *
     * @param startDate Start Date of a period.
     * @param endDate End date of a period.
     *
     * @return int [] result    [0]=duration in years, [1]=duration in months,
     *         [2]=duration in days.
     */
    public static final int[] computeDuration(Date startDate, Date endDate)
    {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.setTime(startDate);
        to.setTime(endDate);

        int birthYYYY = from.get(Calendar.YEAR);
        int birthMM = from.get(Calendar.MONTH);
        int birthDD = from.get(Calendar.DAY_OF_MONTH);
        int asofYYYY = to.get(Calendar.YEAR);
        int asofMM = to.get(Calendar.MONTH);
        int asofDD = to.get(Calendar.DAY_OF_MONTH);
        int ageInYears = asofYYYY - birthYYYY;
        int ageInMonths = asofMM - birthMM;
        int ageInDays = asofDD - birthDD + 1;

        if (ageInDays < 0)
        {
            /*
             * Guaranteed after this single treatment, ageInDays will be >= 0.
             * that is ageInDays = asofDD - birthDD + daysInBirthMM.
             */
            ageInDays += from.getActualMaximum(Calendar.DAY_OF_MONTH);
            ageInMonths--;
        }

        if (ageInDays == to.getActualMaximum(Calendar.DAY_OF_MONTH))
        {
            ageInDays = 0;
            ageInMonths++;
        }

        if (ageInMonths < 0)
        {
            ageInMonths += 12;
            ageInYears--;
        }

        if ((birthYYYY < 0) && (asofYYYY > 0))
        {
            ageInYears--;
        }

        if (ageInYears < 0)
        {
            ageInYears = 0;
            ageInMonths = 0;
            ageInDays = 0;
        }

        int[] result = new int[3];
        result[0] = ageInYears;
        result[1] = ageInMonths;
        result[2] = ageInDays;

        return result;
    }

    /**
     * Returns the current SQL date.
     *
     * @return Date
     */
    public static java.sql.Date getSystemDate()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        java.sql.Date sysDate = new java.sql.Date(cal.getTime().getTime());

        return sysDate;
    }

    /**
     * Returns the current timestamp.
     *
     * @return Timestamp
     */
    public static Timestamp getSystemTimestamp()
    {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Returns true if the length of the year is of 4 digits
     *
     * @param s String value Year
     *
     * @return True   if the year length is of 4 digits, False  otherwise
     *
     * @since 20/04/2001
     */
    public static boolean isValidYearFormat(String s)
    {
        if (s == null)
        {
            return false;
        }
        else if (s.trim().length() == 4)
        {
            return true;
        }

        return false;
    }

    /**
     * This method convert the date to string
     *
     * @param date date
     * @param strFormat the string format
     *
     * @return date as string format
     */
    public static String getDate(Date date, String strFormat)
    {
        return DateUtil.parseDate(date, strFormat);
    }

    /**
     * Returns true if the String is a valid date.
     *
     * @param strDate The date in format ddmmyyyy.
     *
     * @return True, if it is a valid date. False, otherwise.
     */
    public static boolean isValidDate(String strDate)
    {
        return DateUtil.toDate(strDate, "ddMMyyyy") != null;
    }

    /**
     * Returns true if the String is a valid date by specifying the date format to be verified.
     *
     * @param strDate The date.
     * @param dateStrFormat The date format of the specified strDate
     *
     * @return True, if it is a valid date. False, otherwise.
     */
    public static boolean isValidDate(String strDate, String dateStrFormat)
    {
        return DateUtil.toDate(strDate, dateStrFormat) != null;
    }

    /**
     * Add year, month or day to a date
     * To subtract the specified number of Days to the specified Date object, juz use a negative number
     * Example: DateUtil.addDaysToDate(date, -5) ==  subtracting 5 days from the specified date.
     * The same applies to month and year.
     *
     * @param type (int).  Indicates the input num value is in year, month, or
     *        days. Valid values are Calendar.YEAR, Calendar.MONTH,
     *        Calendar.DATE
     * @param date (java.sql.Date).
     * @param num (int).  The value to be added to the input date.
     *
     * @return java.sql.Date.
     */
    public static final Date addDate(int type, Date date, int num)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(type, num);

        return new Date(cal.getTime().getTime());
    }

    /**
     * Adds the specified number of Days to the specified Date object
     * To subtract the specified number of Days to the specified Date object, juz use a negative number
     * Example: DateUtil.addDaysToDate(date, -5) ==  subtracting 5 days from the specified date.
     *
     * @param date Date to be add
     * @param numDays Number of days to add
     *
     * @return date  Added Date
     */
    public static Date addDaysToDate(Date date, int numDays)
    {
        if (date == null)
        {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, numDays);

        return c.getTime();
    }

    /**
     * Adds the specified number of Hours to the specified Date object
     * To subtract the specified number of hours to the specified Date object, juz use a negative number
     * Example: DateUtil.addDaysToDate(date, -5) ==  subtracting 5 hours from the specified date.
     *
     * @param date Date to be add
     * @param numHours A valued byte that could possibly be of negative value.
     *
     * @return date  Added Date
     *
     * @since 27/10/2001
     */
    public static Date addHoursToDate(Date date, int numHours)
    {
        if (date == null)
        {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, numHours);

        return c.getTime();
    }

    /**
     * Adds the specified number of Minutes to the specified Date object
     * To subtract the specified number of Minutes to the specified Date object, juz use a negative number
     * Example: DateUtil.addDaysToDate(date, -5) ==  subtracting 5 minutes from the specified date.
     *
     * @param date Date to be add
     * @param numMins Number of minutes to add
     *
     * @return date  Added Date
     *
     * @since 27/10/2001
     */
    public static Date addMinutesToDate(Date date, int numMins)
    {
        if (date == null)
        {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, numMins);

        return c.getTime();
    }

    /**
     * Adds the specified number of Months to the specified Date object
     *
     * @param date Date to be add
     * @param numMonths Number of months to add
     *
     * @return date  Added Date
     */
    public static Date addMonthsToDate(Date date, int numMonths)
    {
        if (date == null)
        {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, numMonths);
        return c.getTime();
    }

    /**
     * Adds the specified number of Years to the specified Date object
     *
     * @param date Date to be add
     * @param numYears Number of years to add
     *
     * @return date  Added Date
     */
    public static Date addYearsToDate(Date date, int numYears)
    {
        if (date == null)
        {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, numYears);
        return c.getTime();
    }

    /**
     * The method will compares 2 dates (excluding the HH MM SS)
     *
     * @param date1 1st date parameter
     * @param date2 2nd date parameter
     *
     * @return returns -1 if 1st date parameter is earlier than 2nd date
     *         parameter retuns 0 if both dates parameter is the same day
     *         retuns 1 if 1st date parameter is later than 2nd date parameter
     */
    public static int compareDates(Date date1, Date date2)
    {
        if ((date1 == null) && (date2 == null))
        {
            return 0;
        }

        if (date1 == null)
        {
            return -1;
        }

        if (date2 == null)
        {
            return 1;
        }

        String strFormat = "yyyyMMdd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);

        int intDate1 = Integer.parseInt(dateFormat.format(date1));
        int intDate2 = Integer.parseInt(dateFormat.format(date2));

        if (intDate1 == intDate2)
        {
            return 0;
        }

        if (intDate1 > intDate2)
        {
            return 1;
        }

        return -1;
    }

    /**
     * Parses Date object to formatted string
     *
     * @param date date to be converted
     * @param formatStr Date/Time pattern. Example: ddMMyyyy or HHmmss or any
     *        other patterns
     *
     * @return String in required format  Format  : dd = Day MM = Month yyyy =
     *         Year HH = Hour mm = Minute ss = Second All format same as
     *         SimpleDateFormat. Null is returned if the date object is null.
     *
     * @since 22/03/2001
     */
    public static String parseDate(Date date, String formatStr)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);

        if (date == null)
        {
            return null;
        }
        else
        {
            return dateFormat.format(date);
        }
    }

    /**
     * Parses Date object to date-time formatted string
     *
     * @param date THe date to be converted
     *
     * @return String in required format. Null is returned if the date object
     *         is null. (All format same as SimpleDateFormat)
     *
     * @since 25/10/2001
     */
    public static String parseDate(Date date)
    {
        return parseDate(date, DATETIME_FORMAT);
    }

    /**
     * Resets time fields of date to 00:00
     *
     * @param date Date to be reset the time to zero
     *
     * @return date  Converted Date
     */
    public static Date resetTime(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * Converts the specified date-time string to Date object based on the
     * specified date-time format. <CODE>null</CODE> is returned if the
     * specified date is invalid.
     *
     * @param strDateTime The date string in this format 'ddMMyyyy  HHmm'.
     * @param dateTimeFormat The date pattern in this format 'ddMMyyyy  HHmm'
     *
     * @return the Date representation.  Returns null if the date object or the
     *         strDateTime or the dateTimeFormat is null.
     */
    public static Date toDate(String strDateTime, String dateTimeFormat)
    {
        if ((strDateTime == null) || (strDateTime.length() == 0) ||
                (dateTimeFormat == null) || (dateTimeFormat.length() == 0))
        {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
        Date date = dateFormat.parse(strDateTime, new ParsePosition(0));

        if (date == null)
        {
            return null;
        }

        String dateStr = parseDate(date, dateTimeFormat);

        if (!strDateTime.equals(dateStr))
        {
            return null;
        }

        return date;
    }

    /**
     * Converts the specified date-time string to Date object based on the
     * specified date-time format. <CODE>null</CODE> is returned if the
     * specified date is invalid.
     *
     * @param strDateTime The date string in this format 'ddMMyyyy  HHmm'.
     *
     * @return the Date representation.  Returns null if the date object or the
     *         strDateTime or the dateTimeFormat is null.
     */
    public static Date toDate(String strDateTime)
    {
        return toDate(strDateTime, DATETIME_FORMAT);
    }

    /**
     * This function create the Date object with only time compoent offset from
     * the Epoch 01 Jan 1970 GMT (Gregorian)
     *
     * @param hour
     * @param minute
     * @param second
     * @param millisecond
     * @return java.util.Date
     */
    public static Date toDate(int hour, int minute, int second, int millisecond) {
        Calendar cal = Calendar.getInstance();

        cal.set(1970, 0, 1);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, millisecond);

        return new Date(cal.getTime().getTime());
    }
    /**
     * Gets an integer string representation of the specified month.
     *
     * @param mthMMM Month of three letter string.  For example, "JAN",
     *        "FEB",..
     *
     * @return a string number equivalent of the specified month string.  If
     *         the specified month is unknown, zero string is returned that is
     *         "00".
     *
     * @since 27/03/2000
     */
    public static String toMMFormat(String mthMMM)
    {
        if (mthMMM.equalsIgnoreCase(MTH_JAN))
        {
            return "01";
        }
        else if (mthMMM.equalsIgnoreCase(MTH_FEB))
        {
            return "02";
        }
        else if (mthMMM.equalsIgnoreCase(MTH_MAR))
        {
            return "03";
        }
        else if (mthMMM.equalsIgnoreCase(MTH_APR))
        {
            return "04";
        }
        else if (mthMMM.equalsIgnoreCase(MTH_MAY))
        {
            return "05";
        }
        else if (mthMMM.equalsIgnoreCase(MTH_JUN))
        {
            return "06";
        }
        else if (mthMMM.equalsIgnoreCase(MTH_JUL))
        {
            return "07";
        }
        else if (mthMMM.equalsIgnoreCase(MTH_AUG))
        {
            return "08";
        }
        else if (mthMMM.equalsIgnoreCase(MTH_SEP))
        {
            return "09";
        }
        else if (mthMMM.equalsIgnoreCase(MTH_OCT))
        {
            return "10";
        }
        else if (mthMMM.equalsIgnoreCase(MTH_NOV))
        {
            return "11";
        }
        else if (mthMMM.equalsIgnoreCase(MTH_DEC))
        {
            return "12";
        }

        return null;
    }

    /**
     * Gets a specified month string as JAN, FEB..
     *
     * @param mthMM The month as 2 digits For example, "01", "02",..
     *
     * @return a specified month string.  If the specified month is unknown,
     *         empty string ("") is returned.
     *
     * @since 27/03/2000
     */
    public static String toMMMFormat(String mthMM)
    {
        if ("01".equals(mthMM))
        {
            return MTH_JAN;
        }
        else if ("02".equals(mthMM))
        {
            return MTH_FEB;
        }
        else if ("03".equals(mthMM))
        {
            return MTH_MAR;
        }
        else if ("04".equals(mthMM))
        {
            return MTH_APR;
        }
        else if ("05".equals(mthMM))
        {
            return MTH_MAY;
        }
        else if ("06".equals(mthMM))
        {
            return MTH_JUN;
        }
        else if ("07".equals(mthMM))
        {
            return MTH_JUL;
        }
        else if ("08".equals(mthMM))
        {
            return MTH_AUG;
        }
        else if ("09".equals(mthMM))
        {
            return MTH_SEP;
        }
        else if ("10".equals(mthMM))
        {
            return MTH_OCT;
        }
        else if ("11".equals(mthMM))
        {
            return MTH_NOV;
        }
        else if ("12".equals(mthMM))
        {
            return MTH_DEC;
        }

        return null;
    }

    /**
     * Converts the specified date-time string to SQL Date object based on the
     * specified date-time format. <CODE>null</CODE> is returned if the
     * specified date is invalid.
     *
     * @param strDateTime The date string in this format 'ddMMyyyy  HHmm'.
     * @param dateTimeFormat The date pattern in this format 'ddMMyyyy  HHmm'
     *
     * @return the SQL Date representation.  Returns null if the date object or
     *         the strDateTime or the dateTimeFormat is null.
     */
    public static java.sql.Date toSQLDate(String strDateTime,
                                          String dateTimeFormat)
    {
        Date date = toDate(strDateTime, dateTimeFormat);

        if (date == null)
        {
            return null;
        }

        return new java.sql.Date(date.getTime());
    }

    /**
     * Converts the Date object to SQL Date object.
     *
     * @param date The date to be converted.
     *
     * @return the SQL Date representation.
     */
    public static java.sql.Date toSQLDate(Date date)
    {
        if (date == null)
        {
            return null;
        }

        return new java.sql.Date(date.getTime());
    }

    /**
     * Converts the specified date-time string to SQL Date object based on the
     * specified date-time format. <CODE>null</CODE> is returned if the
     * specified date is invalid.
     *
     * @param strDateTime The date string in this format 'ddMMyyyy  HHmm'.
     *
     * @return the SQL Date representation.  Returns null if the date object or
     *         the strDateTime or the dateTimeFormat is null.
     */
    public static java.sql.Date toSQLDate(String strDateTime)
    {
        return toSQLDate(strDateTime, DATETIME_FORMAT);
    }

    /**
     * Converts the specified date-time string to Timestamp.
     *
     * @param dateTimeStr The String object in this ddMMyyyy  HHmm format
     *
     * @return Timestamp object. Returns null if dateTimeStr is null Format
     *         used is meant for Oracle dbs only
     */
    public static Timestamp toTimestamp(String dateTimeStr)
    {
        return toTimestamp(toDate(dateTimeStr));
    }

    /**
     * Converts the specified date-time string to Timestamp.
     *
     * @param dateTimeStr The String object in this ddMMyyyy  HHmm format
     * @param dateTimeFormat The date pattern in this format 'ddMMyyyy  HHmm'
     *
     * @return Timestamp object. Returns null if dateTimeStr is null Format
     *         used is meant for Oracle dbs only
     */
    public static Timestamp toTimestamp(String dateTimeStr,
                                        String dateTimeFormat)
    {
        return toTimestamp(toDate(dateTimeStr, dateTimeFormat));
    }

    /**
     * Converts the specified Calendar to Timestamp.
     *
     * @param date The Date object.
     *
     * @return Timestamp object.  Returns null if date object is null Format
     *         used is meant for Oracle dbs only
     */
    public static Timestamp toTimestamp(Date date)
    {
        if (date == null)
        {
            return null;
        }

        return new Timestamp(date.getTime());
    }

    /**
     * Converts the specified Calendar to Timestamp.
     *
     * @param timeStamp The TimeStamp object.
     *
     * @return Date object.  Returns null if timestamp object is null Format
     *         used is meant for Oracle dbs only
     */
    public static Date toDate(Timestamp timeStamp)
    {
        if (timeStamp == null)
        {
            return null;
        }

        return new Date(timeStamp.getTime());
    }



    /**
     * Converts date/month to two digits.
     *
     * @param number The date/month used, ie. 1, 2,..
     *
     * @return a string The date/month in two digits form
     *
     * @since 19/02/2000
     */
    public static String toTwoDigits(String number)
    {
        return StringUtil.lPad(number, 2, "0");
    }

    /**
     * get the target date(s) after(before) a specified period from a marked date
     *
     * @param originalJobDate the date to start job
     * @param months          user input to control the the period
     * @param days            user input to control the the period
     * @return expiredDate  the date that road tax has expired
     */
    public static java.util.Date[] getDurationDates(Date originalJobDate, float months, int days)
    {
        if(originalJobDate == null)
        {
            return null;
        }

        java.util.Date[] expireDates;
        Calendar markedDate = Calendar.getInstance();
        markedDate.setTime(originalJobDate);

        int iMonths = (int)months;
        int iDays = days + Math.round(30 * (months - iMonths));

        if(months < 0)
        {
            markedDate.add(Calendar.DATE, iDays);
            Calendar tempDate = (Calendar)markedDate.clone();
            markedDate.add(Calendar.MONTH, iMonths);

            int dayTmp = tempDate.get(Calendar.DATE);
            int maxDaysTmp = tempDate.getActualMaximum(Calendar.DATE);
            int day = markedDate.get(Calendar.DATE);
            int maxDays = markedDate.getActualMaximum(Calendar.DATE);

            if(dayTmp > day)
            {
                expireDates = null;
            } else if(dayTmp == maxDaysTmp && day < maxDays)
            {
                expireDates = new Date[maxDays - day + 1];
                for(int i = 0; i < expireDates.length; i++)
                {
                    expireDates[i] = markedDate.getTime();
                    markedDate.set(Calendar.DATE, ++day);
                }
            } else
            {
                expireDates = new Date[1];
                expireDates[0] = markedDate.getTime();
            }
        } else
        {
            expireDates = new Date[1];
            markedDate.add(Calendar.MONTH, iMonths);
            markedDate.add(Calendar.DATE, iDays);
            expireDates[0] = markedDate.getTime();
        }

        return expireDates;
    }




    /**
     * to determine is the date is infinite. ie is the year is 99999.
     * 9999 is used to detnote the expiry date does not expire for ever
     * Added user : smurali	 ext : 7943
     *
     * @param  dateToCheck Date the date to check is it infinite
     * @return result      Boolean if the date is infinity it will return true. or it will return false
     */
    public static Boolean isInfinity(Date dateToCheck)
    {
        if(dateToCheck == null)
        {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToCheck);
        int year = cal.get(Calendar.YEAR);
        log.info("The date is infinity. The given year :"+year);
        if (year >= 9999)
        {
            log.info("The date is infinity");
            return new Boolean(true);
        }

        log.info("The date is not infinity");
        return new Boolean(false);

    }

    /**
     * Author : Lee
     * Convert the date string to the input format
     *
     * @param dateIn 		  date (String) that need to do conversion
     * @param formatIn        format of the input date (String)
     * @param formatOut       format of the out date (String)
     * @return date (String)    converted date in String
     */
    public static String DateConverter(String dateIn, String formatIn, String formatOut) {
        try{
            if( "now" == dateIn )
            {
                return (new SimpleDateFormat(formatOut).format(new Date()));
            }
            else
            {
                return (new SimpleDateFormat(formatOut)).format(
                        (new SimpleDateFormat(formatIn)).parse(
                                dateIn, new ParsePosition(0)));
            }
        }
        catch(Exception e){
            return "";
        }
    }

    /**
     * Author : Richardg
     * Get the next start and end month and year
     *
     * @return Timestamp
     */

    public static Timestamp getNextStartMonthYear(Date currentDate) {
        //Date currentDate = getSystemDate();
        int currentMonth = getMonth(currentDate)+1;
        int nextYear = getYear(currentDate);
        int nextMonth = 1; //January by default

        if(currentMonth<=11) { // if current month is jan to nov
            nextMonth = currentMonth + 1;
        }

        if(nextMonth == 1) { //means that the currentMonth is dec.
            nextYear++;
        }




        return toTimestamp(getDate(nextYear, nextMonth, 1));
    }

    public static Timestamp getNextEndMonthYear(Date currentDate) {
        Date nextStartMonthYear = getNextStartMonthYear(currentDate);
        int lastDay = getLastDayOfTheMonth(nextStartMonthYear);
        return toTimestamp(getDate(getYear(nextStartMonthYear) , getMonth(nextStartMonthYear)+1, lastDay));
    }

    /**
     * Author : Richardg
     * validate the time string input
     *
     * @param strTime, strTimeStart, strTimeEnd	  String time format (ddMMyyyy)
     * @return boolean
     */
    public static boolean isValidTime(String strTime) {
        boolean isValid = false;
        if(strTime.length()==4) {
            if(Integer.parseInt(strTime)>=0 && Integer.parseInt(strTime)<=2359) {
                isValid = true;
            }
        }
        return isValid;
    }

    public static boolean isValidTimeRange(String strTimeStart, String strTimeEnd) {
        boolean isValid = true;
        if(Integer.parseInt(strTimeEnd)<Integer.parseInt(strTimeStart)) {
            isValid = false;
        }
        return isValid;
    }

    public static Timestamp toTimestampStringDateTime(String strDate, String strTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_FORMAT);
        Timestamp timestamp = null;
        java.util.Date parsedDate;
        try {
            parsedDate = dateFormat.parse(strDate+"  "+strTime);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return timestamp;
    }


    /**
     * Author: Han Chee Teng Daniel
     * Returns the sql date instance based on the current system date and default timezone.
     * <p/>
     *
     * @return Current System date.
     */
    public static java.sql.Date getCurrentSQLDate() {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date jsqlD = new java.sql.Date( calendar.getTime().getTime() );
        return jsqlD;
    }

    public static int compareDateWithTime(Date date1, Date date2) {

        if ((date1 == null) && (date2 == null))
        {
            return 0;
        }

        if (date1 == null)
        {
            return -1;
        }

        if (date2 == null)
        {
            return 1;
        }

        if (date1.getTime() < date2.getTime()) {
            return -1;
        }

        if (date1.getTime() > date2.getTime()) {
            return 1;
        }

        return 0;

    }

    //returns in difference in minutes
    public static long getDateTimeDifference(Date startDate, Date endDate) {

        if ((startDate == null) || (endDate == null))
        {
            return -1;
        }

        if (startDate.after(endDate))
        {
            return -1;
        }

        GregorianCalendar gc1 = (GregorianCalendar) GregorianCalendar.getInstance();
        GregorianCalendar gc2 = (GregorianCalendar) gc1.clone();
        gc1.setTime(startDate);
        gc2.setTime(endDate);
        long milis1 = gc1.getTimeInMillis();
        long milis2 = gc2.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = milis2 - milis1;

        // Calculate difference in seconds
        //long diffSeconds = diff / 1000;

        // Calculate difference in minutes
        long diffMinutes = diff / (60 * 1000);

        // Calculate difference in hours
        // long diffHours = diff / (60 * 60 * 1000);

        // Calculate difference in days
        //long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffMinutes;
    }

}

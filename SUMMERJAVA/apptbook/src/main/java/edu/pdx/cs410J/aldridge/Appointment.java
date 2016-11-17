package edu.pdx.cs410J.aldridge;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.ParserException;

import java.text.DateFormat;  //includes SimpleDateFormat
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * imlpements Comparable to allow easy sorting of appointsments based on
 * start date, then end date, then description if necessary
 */
public class Appointment extends AbstractAppointment implements Comparable <Appointment> {
    //implements Comparable <Appointment> via compareTo method
  protected String description;
  protected Date startDateTime;
  protected Date endDateTime;

    /**
     *
     * @param description - an appointment description String
     * @param startDateTime - an appointment start in this format: mm/dd/yyyy hh:mm am/pm
     * @param endDateTime - an appointment end in this format: mm/dd/yyyy hh:mm am/pm
     */
  public Appointment(String description, Date startDateTime, Date endDateTime) {
      this.description = description;
      this.startDateTime = startDateTime;
      this.endDateTime = endDateTime;
  }


    /**
     *
      * @param toCopy copies this args members
     */
  public Appointment(Appointment toCopy) {
      if(toCopy == null) {
          this.description = null;
          this.startDateTime = null;
          this.endDateTime = null;
      }
      else {
          this.description = toCopy.getDescription();
          this.startDateTime = toCopy.getStartDateTime();
          this.endDateTime = toCopy.getEndDateTime();
      }
  }

    /**
     *
     * Copy constructor to take in Strings rather than Dates
     * @param description of an appointment in String form
     * @param beginTime start of an appointment
     * @param endTime end of an appointment
     *
     * Time in format mm/dd/yyyy hh:mm am/pm
     */
    public Appointment(String description, String beginTime, String endTime) {

        if (beginTime == null || endTime == null) {
            System.out.println("Appointment Constructor: start or end args were NULL!");
            this.description = description;
            this.startDateTime = this.endDateTime = null;
            return;
        }

        this.description = description;

        //String[] start = beginTime.split(" ");
        //String[] end = endTime.split(" ");
        int count = 0;
        int matches = 0;

        String[] validDateFormats = {"MM/dd/yyyy hh:mm a",
                "MM/d/yyyy hh:mm a",
                "M/dd/yyyy hh:mm a",
                "M/d/yyyy hh:mm a"};

        for (String dates : validDateFormats) {
            try {
                Date myDate = null;
                SimpleDateFormat sdf = new SimpleDateFormat(dates);
                sdf.setLenient(false);

                if (count == 0)
                    myDate = sdf.parse(beginTime);
                else
                    myDate = sdf.parse(endTime);

                if (myDate != null) {
                    if (count == 0) //first iteration, set the start
                        startDateTime = myDate;
                    else
                        endDateTime = myDate;
                    ++count;
                }

                if (count == 2) break;

            } catch (ParseException pe) {
                System.out.println("Didn't match format #" + matches++ + ", lets try another format.");
            }

            if(matches == 5) {
                System.err.println("Didn't match any format");
                return;
            }
        }
    }


    /**
     * Function checks the the description provided is not an empty string and/or just whitespace
     * @param description description of an appointment
     * @return true if description is not blank, false otherwise
     *
     */
    static public boolean validDescriptionCheck(String description) {
      return !description.matches("( )*");
  }

    /**
     * FUnction checks that the start and end Times provided are valid as indicated by the README
     * @param startTime start of an appointment Time
     * @param endTime end  of an appointment Time
     * @return true if formats are correct
     */
    // 01:00 - 12:59
  static public boolean checkTimeFormat(String startTime, String endTime) {
      return (startTime.matches("((^[0]?[1-9])|(^1[0-2])):[0-5][0-9]\\s((([a]|[A])|([p]|[P]))([m]|[M]))") &&
              endTime.matches("((^[0]?[1-9])|(^1[0-2])):[0-5][0-9]\\s((([a]|[A])|([p]|[P]))([m]|[M]))"));
  }

    /**
     * Function checks that the start and end DATES provided are valid as indicated by the README
     * @param startDate start of an appointment Date
     * @param endDate end  of an appointment Date
     * @return
     */
    // 01/05/1995, 1/5/1995, 12/31/2016
  static public boolean checkDateFormat(String startDate, String endDate) {
      return (startDate.matches("(^0?[1-9]|^1[0-2])/(0?[1-9]|[1-2][0-9]|3[0-1])/([0-9][0-9][0-9][0-9])") &&
              endDate.matches("(^0?[1-9]|^1[0-2])/(0?[1-9]|[1-2][0-9]|3[0-1])/([0-9][0-9][0-9][0-9])"));
  }


    /**
     *
     * @return A String representing the formatted time mm/dd/yyyy @ hh:mm am/pm
     */
  @Override
  public String getBeginTimeString() {
      String blank = "Blank Begin Time!";

      if(startDateTime == null)
          return blank;

      DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
      DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);
      return df.format(startDateTime) + " @ " + tf.format(startDateTime);
  }

    /**
     *
     * @return A String representing the PRETTY formatted time mm/dd/yyyy @ hh:mm am/pm
     */
    public String getFullBeginTimeString() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
        DateFormat tf = DateFormat.getTimeInstance(DateFormat.FULL);
        return df.format(startDateTime) + " @ " + tf.format(startDateTime);
    }

    /**
     *
     * @return A String representing the formatted time mm/dd/yyyy @ hh:mm am/pm
     */
  @Override
  public String getEndTimeString() {
      String blank = "Blank End Time!";

      if(endDateTime == null)
          return blank;

      DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
      DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);
    return df.format(endDateTime) + " @ " + tf.format(endDateTime);
  }

    /**
     *
     * @return A String representing the PRETTY formatted time mm/dd/yyyy @ hh:mm am/pm
     */
    public String getFullEndTimeString() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
        DateFormat tf = DateFormat.getTimeInstance(DateFormat.FULL);
        return df.format(endDateTime) + " @ " + tf.format(endDateTime);
    }


    /**
     *
     * @return String of the appointment description
     */
  @Override
  public String getDescription() {
      return description;
  }


    /**
     * prints all the appointment information
     */
    public void printAllInfo() {
      System.out.println("\nFull Appointment Description: ");
      System.out.println("Start: " + getBeginTimeString() + "\nEnd: " +
      getEndTimeString() + "\nDescription: " +
              (validDescriptionCheck(description) ? "\"" + getDescription() + "\"" : "<BLANK>"));
  }


    /**
     *
     * @return "Getters" retrieve the start date or end date respectively
     */
  protected Date getStartDateTime() {return this.startDateTime;}
  protected Date getEndDateTime() {return this.endDateTime;}


    /**
     * This function sorts by start, if start matches then it sorts by end,
     * if end matches it sorts by description
     * @param anotherAppointment an appointment to compare *this* to
     * @return
     */
    @Override
    public int compareTo(Appointment anotherAppointment) {

        int order = 0;
        //long duration = 0;
        //int zeroCount = 0;
        //startDateTime.getTime() - endDateTime.getTime();
 /*
        long diffInSeconds = 0; // = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = 0; // = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = 0; // = TimeUnit.MILLISECONDS.toHours(duration);

        //GET START DIFFERENCE

        if(anotherAppointment != null && startDateTime != null) {
            System.out.println("\n\nComparing " + getBeginTimeString() + " to " + anotherAppointment.getBeginTimeString());
            duration = (anotherAppointment.getStartDateTime().getTime() - startDateTime.getTime());
            diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
            diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
            System.out.println("Difference is: " + diffInHours + " hours, " + diffInMinutes + " minutes, " +
            diffInSeconds  + " seconds.");

            //IF START IS SAME GET END DIFFERENCE
            if (diffInHours == 0 && diffInMinutes == 0 && diffInSeconds == 0) {
                System.out.println("The duration was: " + duration + " meaning the start times matched...checking end times");
                System.out.println("Comparing " + getEndTimeString() + " to " + anotherAppointment.getEndTimeString());
                duration = (anotherAppointment.getEndDateTime().getTime() - endDateTime.getTime());
                diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
                diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
            }
            //IF START AND END THE SAME, GET DESCRIPTION DIFFERENCE
            if (diffInHours == 0 && diffInMinutes == 0 && diffInSeconds == 0) {
                System.out.println("The duration was: " + duration + " meaning the end times matched...checking description");
                duration = anotherAppointment.getDescription().compareTo(description);
            }

            if(duration == 0)
                System.out.println("Wow the duration was: " + duration + " meaning the description matched too...duplicate appointment!!");

            System.out.println("\n\nTest -> The (long)diffInMinutes difference is: " + diffInMinutes + " and the (int) is " + (int)diffInMinutes);
        }
        return (int)diffInMinutes;
*/
        if(anotherAppointment != null && startDateTime != null) {
            if (startDateTime.before(anotherAppointment.getStartDateTime()))
                order = -1;
            else if (startDateTime.after(anotherAppointment.getStartDateTime()))
                order = 1;
            else if (endDateTime.before(anotherAppointment.getEndDateTime()))
                order = -1;
            else if (endDateTime.after(anotherAppointment.getEndDateTime()))
                order = 1;
            else {
                order = description.compareTo(anotherAppointment.getDescription());
            }
        }
        return order;

    }


    /**
     *
     * @return A PRETTY formatted version of the appointment duration in days, hours, minutes
     */
    public String getDuration() {

        long milliDuration, minutes, hours, days, months, years;

        milliDuration = (endDateTime.getTime() - startDateTime.getTime());

        //seconds = (milliDuration / 1000) % 60;
        minutes = (milliDuration / (1000 * 60)) % 60;
        hours = (milliDuration / (1000 * 60 * 60)) % 24;
        days = (milliDuration / (1000 * 60 * 60 * 24)) % 365;
        //months = (milliDuration / (1000 * 60 * 60 * 24 * 31)) % 12;
        //+ months + " month" + ((months > 1 || months == 0) ? "s, " : ", ")

        return "Duration: "
                + days + " day" + ((days > 1 || days == 0) ? "s, " : ", ")
                + hours + ":" + (minutes < 10 ? "0" : "")
                + minutes + " (" + hours + " hour"
                + ((hours > 1 || hours == 0) ? "s, " : ", ") + minutes + " minute"
                + ((minutes > 1 || minutes == 0) ? "s)" : ")");
    }

}

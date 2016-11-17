package edu.pdx.cs410J.aldridge;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.ParserException;

import java.text.DateFormat;  //includes SimpleDateFormat
import java.text.ParseException;
import java.util.Date;


public class Appointment extends AbstractAppointment implements Comparable <Appointment> {
    //implements Comparable <Appointment> via compareTo method
  protected String description;
  protected Date startDateTime;
  protected Date endDateTime;


  public Appointment(String description, Date startDateTime, Date endDateTime) {
      this.description = description;
      this.startDateTime = startDateTime;
      this.endDateTime = endDateTime;
  }

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

  static public boolean validDescriptionCheck(String description) {
      return !description.matches("( )*");
  }

    // 01:00 - 12:59
  static public boolean checkTimeFormat(String startTime, String endTime) {
      return (startTime.matches("((^[0]?[1-9])|(^1[0-2])):[0-5][0-9]\\s((([a]|[A])|([p]|[P]))([m]|[M]))") &&
              endTime.matches("((^[0]?[1-9])|(^1[0-2])):[0-5][0-9]\\s((([a]|[A])|([p]|[P]))([m]|[M]))"));
  }

    // 01/05/1995, 1/5/1995, 12/31/2016
  static public boolean checkDateFormat(String startDate, String endDate) {
      return (startDate.matches("(^0?[1-9]|^1[0-2])/(0?[1-9]|[1-2][0-9]|3[0-1])/([0-9][0-9][0-9][0-9])") &&
              endDate.matches("(^0?[1-9]|^1[0-2])/(0?[1-9]|[1-2][0-9]|3[0-1])/([0-9][0-9][0-9][0-9])"));
  }

  @Override
  public String getBeginTimeString() {
      DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
      DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);
      return df.format(startDateTime) + " @ " + tf.format(startDateTime);
  }

    public String getFullBeginTimeString() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
        DateFormat tf = DateFormat.getTimeInstance(DateFormat.FULL);
        return df.format(startDateTime) + " @ " + tf.format(startDateTime);
    }

  @Override
  public String getEndTimeString() {
      DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
      DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);
    return df.format(endDateTime) + " @ " + tf.format(endDateTime);
  }

    public String getFullEndTimeString() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
        DateFormat tf = DateFormat.getTimeInstance(DateFormat.FULL);
        return df.format(endDateTime) + " @ " + tf.format(endDateTime);
    }

  @Override
  public String getDescription() {
      return description;
  }

  public void printAllInfo() {
      System.out.println("\nFull Appointment Description: ");
      System.out.println("Start: " + getBeginTimeString() + "\nEnd: " +
      getEndTimeString() + "\nDescription: " +
              (validDescriptionCheck(description) ? "\"" + getDescription() + "\"" : "<BLANK>"));
  }


  protected Date getStartDateTime() {return this.startDateTime;}
  protected Date getEndDateTime() {return this.endDateTime;}


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

    public String getDuration() {

        long milliDuration, minutes, hours;

        milliDuration = (endDateTime.getTime() - startDateTime.getTime());

        //seconds = (milliDuration / 1000) % 60;
        minutes = (milliDuration / (1000 * 60)) % 60;
        hours = (milliDuration / (1000 * 60 * 60)) % 24;

        return "Duration: " + hours + ":" + (minutes < 10 ? "0" : "") + minutes + " (" + hours + " hour" +
                                ((hours > 1 || hours == 0) ? "s, " : ", ") + minutes + " minute" +
                                                                ((minutes > 1 || minutes == 0) ? "s)" : ")");
    }

}

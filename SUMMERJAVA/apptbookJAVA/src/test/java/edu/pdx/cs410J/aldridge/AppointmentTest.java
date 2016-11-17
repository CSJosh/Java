package edu.pdx.cs410J.aldridge;

import jdk.nashorn.internal.runtime.ParserException;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.Date;

/**
 * Unit tests for the {@link Appointment} class.
 */

public class AppointmentTest {
/*
  @Test(expected = UnsupportedOperationException.class)
  public void getBeginTimeStringNeedsToBeImplemented() {
    //Appointment appointment = new Appointment();
    //appointment.getBeginTimeString();
  }

  @Test
  public void initiallyAllAppointmentsHaveTheSameDescription() {
    //Appointment appointment = new Appointment();
    //assertThat(appointment.getDescription(), containsString("not implemented"));
  }

  @Test
  public void forProject1ItIsOkayIfGetBeginTimeReturnsNull() {
    //Appointment appointment = new Appointment();
    //assertThat(appointment.getBeginTime(), is(nullValue()));
  }
*/

  @Test
  public void testForInvalidDescriptionYesWhitespace() {
    //Appointment testAppointment = new Appointment("  ", "1/0/2016", "12:30", "1/1/2016", "13:30");
    assert(!Appointment.validDescriptionCheck("  "));
  }

  @Test
  public void testForInvalidDescriptionNoWhitespace() {
      //Appointment testAppointment = new Appointment("", "1/0/2016", "12:30", "1/1/2016", "13:30");
      assert(!Appointment.validDescriptionCheck(""));
  }

  @Test
  public void testThatTimePrintsCorrectly() {
      SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy hh:mm a");

      try {
          Date start = sdf.parse("1/1/2016" + " " + "12:30 pm");
          Date end = sdf.parse("1/1/2016" + " " + "1:30 pm");
          Appointment testAppointment = new Appointment("description", start, end);
          assertThat(testAppointment.getBeginTimeString() + " " + testAppointment.getEndTimeString(),
                  containsString("1/1/16 @ 12:30 PM 1/1/16 @ 1:30 PM"));

      }catch (ParseException pe) {
          System.err.println("There was a parse exception in a test function");
      }
  }

    /*
  @Test
  public void testDescriptionInitilization() {
      Appointment testAppointment = new Appointment("description", "1/0/2016", "12:30", "1/1/2016", "13:30");
      assertThat(testAppointment.getDescription(), containsString("description"));
  }
*/

  @Test
  public void invalidSingleDigitDayDateTest() {
      //Appointment testAppointment = new Appointment("description", "1/0/2016", "12:30", "1/1/2016", "13:30");
      assert(!Appointment.checkDateFormat("1/0/2016", "1/1/2016"));
  }

/*
  @Test
  public void invalidCharacterMonthDateTest() {
    boolean exception = false;
    try {
        Appointment testAppointment = new Appointment("description", "W/1/2016", "12:30", "1/1/2016", "13:30");
    } catch (ParserException pe) {
        exception = true;
    }
      catch (NumberFormatException nfe) {
          exception = true;
      }
      //assert(!testAppointment.checkDateFormat());
      assert(exception);
  }
*/


    @Test
  public void validSingleDigitDateTest() {
      //Appointment testAppointment = new Appointment("description", "1/1/2016", "12:30", "1/1/2016", "13:30");
      assert(Appointment.checkDateFormat("1/1/2016", "1/1/2016"));
  }

    /*

  @Test
  public void invalidDateTestYearHasInvalidCharacter() {
     Appointment testAppointment = new Appointment("description", "1/1/20w6", "12:30", "1/1/2016", "13:30");
     assert(!testAppointment.checkDateFormat());
  }

  */

  @Test
  public void validTimeTest() {
     //Appointment testAppointment = new Appointment("description", "1/1/2016", "12:30", "1/1/2016", "13:30");
     assert(Appointment.checkTimeFormat("12:30 pm", "1:30 pm"));
  }

/*
  @Test
  public void validTimeTest1400() {
     Appointment testAppointment = new Appointment("description", "1/1/2016", "14:00", "1/1/2016", "14:59");
     assert(testAppointment.checkTimeFormat());
  }

  @Test
  public void validTimeTest1900to2100() {
     Appointment testAppointment = new Appointment("description", "1/1/2016", "19:00", "1/1/2016", "21:00");
     assert(testAppointment.checkTimeFormat());
  }
*/

  @Test
  public void invalidTimeTestNumbersOutOfTimeRange() {
     //Appointment testAppointment = new Appointment("description", "1/1/2016", "25:30", "1/1/2016", "13:30");
     assert(!Appointment.checkTimeFormat("25:30 pm", "13:30 pm"));
  }

    /*
  @Test
  public void invalidTimeTestInvalidCharacters() {
    Appointment testAppointment = new Appointment("description", "1/1/2016", "2f:30", "1/1/2016", "13:30");
    assert(!testAppointment.checkTimeFormat());
  }
  */

}
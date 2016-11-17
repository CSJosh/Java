package edu.pdx.cs410J.aldridge;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by joshuaaldridge on 7/18/16.
 */
public class PrettyPrinter implements AppointmentBookDumper {

    protected String file;
    private final PrintWriter writer;


    //constructor take in a filename argument; will write out to this file

    /**
     *
     * @param fileName save 
     */
    public PrettyPrinter(String fileName) {
        file = fileName;
        writer = null;

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException e) {
            System.err.println("There was an IOException in the TEXTDUMPER constructor " + e.getMessage());
        }
    }

    public PrettyPrinter(PrintWriter pw) {
        this.file = null;
        this.writer = pw;
    }


    //print appointments that START and END in the time range provided to STANDARD OUT
    //these are printed from the appointment book argument provided to the method
    /**
     *
     * @param aBook, startDate, endDate
     * @throws IOException exception thrown
     */
    public void rangePrint(AbstractAppointmentBook aBook, Date startDate, Date endDate) {
        ArrayList<Appointment> apptList;
        ArrayList<Appointment> sortedList;
        AppointmentBook appointmentBook;
        String descript;

        if(aBook == null || startDate == null || endDate == null || this.writer == null)
            return;

        sortedList = new ArrayList<>();
        appointmentBook = (AppointmentBook)aBook;
        apptList = appointmentBook.getAppointments();

        for(Appointment appt: apptList) {
            if(appt.getStartDateTime().after(startDate) || appt.getStartDateTime().compareTo(startDate) == 0) {
                if(appt.getEndDateTime().before(endDate) || appt.getEndDateTime().compareTo(endDate) == 0)
                    sortedList.add(appt);
            }
        }

        if(sortedList.size() != 0) {
            this.writer.println("\n\nRANGE PRINT of Full Appointment Descriptions: ");
            for (Appointment appt : sortedList) {
                descript = appt.getDescription();
                this.writer.println("\nDescription: " + (!descript.matches("( )*") ? "\"" +
                        appt.getDescription() + "\"" : "<BLANK>") + "\nStart: " + appt.getFullBeginTimeString() +
                        "\nEnd: " + appt.getFullEndTimeString() + "\n" + appt.getDuration());
            }
        }
        else
            this.writer.println("\n\nThere are NO APPOINTMENTS in the date/time range provided...");

    }


    /**
     *
     * @param abstractAppointmentBook prints this book
     * @throws IOException
     */
    //Print a full printout version of each
    //added new methods to Appointment to print a FULL description of an appointment
    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {
        Collection<Appointment> apptList;
        AppointmentBook apptBook = (AppointmentBook)abstractAppointmentBook;
        apptList = apptBook.getAppointments();
        BufferedWriter bufferedWriter;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(apptBook.getOwnerName());
            bufferedWriter.write('\n'); //tested and works

            for (Appointment appt : apptList) {
                bufferedWriter.write("\nDescription: " + (!appt.getDescription().matches("( )*") ? "\"" +
                          appt.getDescription() + "\"" : "<BLANK>") + "\nStart: " + appt.getFullBeginTimeString() +
                        "\nEnd: " + appt.getFullEndTimeString() + "\n" + appt.getDuration() + "\n");
            }

            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException e) {
            System.err.println("There was an IOException in the dump function " + e.getMessage());
        }
    }

//ADDED P4 *******************************

    /**
     *
     * @param aBook book to pretty print
     */
    public void dumpPretty(AppointmentBook aBook) {
        if (aBook != null && this.writer != null) {
            this.writer.println(aBook.getOwnerName());

            for (Appointment appointment : aBook.getAppointments()) {
                this.writer.print("\nDescription: " + (!appointment.getDescription().matches("( )*") ? "\"" +
                        appointment.getDescription() + "\"" : "<BLANK>") + "\nStart: " + appointment.getFullBeginTimeString() +
                        "\nEnd: " + appointment.getFullEndTimeString() + "\n" + appointment.getDuration() + "\n");
            }
        }
    }


    /**
     * prints error message if no owner by that name
     */
    public void noOwnerPrint() {
        if(this.writer != null) {
            this.writer.println("\n\nThere is NO OWNER by that name yet...");
        }
    }



}

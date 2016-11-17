package edu.pdx.cs410J.aldridge;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by joshuaaldridge on 7/18/16.
 */
public class PrettyPrinter implements AppointmentBookDumper {

    protected String file;


    //constructor take in a filename argument; will write out to this file
    public PrettyPrinter(String fileName) {
        file = fileName;

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException e) {
            System.err.println("There was an IOException in the TEXTDUMPER constructor " + e.getMessage());
        }
    }


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


}

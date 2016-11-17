package edu.pdx.cs410J.aldridge;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
//import java.io.OutputStream;

/**
 * Created by joshuaaldridge on 7/7/16.
 */
public class TextDumper implements AppointmentBookDumper {

    //protected File file;
    protected String file;

    public TextDumper(String fileName) {
        //open file
        //if doesn't exist, create a new file
        //File file = new File(fileName);
        //file = new File(fileName);
        file = fileName;


        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException e) {
            System.err.println("There was an IOException in the TEXTDUMPER constructor " + e.getMessage());
        }
    }

    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {
        //get owner name and store in local string
        //get a collection of appointments and store in local collection list
        //OPEN file specified (may already be opened by constructor)
        //iterate through the collection list, calling getDescription(), getBeginTime(), getEndTime()
        //methods to get data to store in file.
        String owner;
        Collection<Appointment> apptList;
        int count;
        owner = abstractAppointmentBook.getOwnerName();
        //*******************
        AppointmentBook appointmentBook = (AppointmentBook)abstractAppointmentBook;
        apptList = appointmentBook.getAppointments();
        //*******************
        //count = apptList.size();
        //FileWriter writer;
        BufferedWriter bufferedWriter;

        try {
            //writer = new FileWriter(file);

            //added
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            //end added

            //bufferedWriter.write(Integer.toString(count));
            bufferedWriter.write(owner);
            bufferedWriter.write('\n'); //tested and works
            //writer.write("test");

            for(Appointment appt : apptList) {
                bufferedWriter.write(appt.getDescription() + "#" +
                appt.getBeginTimeString() + "#" + appt.getEndTimeString() + "\n");
            }

            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException e) {
            System.err.println("There was an IOException in the dump function " + e.getMessage());
        }
    }
}

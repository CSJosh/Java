package edu.pdx.cs410J.aldridge;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by joshuaaldridge on 7/7/16.
 */
public class TextParser implements AppointmentBookParser {

    protected String file;

    public TextParser(String fileName) {
        file = fileName;
    }


    //this function will return an AppointmentBook object that is null (file was empty or invalid) or filled with
    //the contents of the file indicated
    @Override
    public AppointmentBook parse() throws ParserException {
        int lineCount = 0;
        int argCount = 0;
        int j;
        String descrip;
        String owner;
        String startTime;
        String endTime;
        String startDate;
        String endDate;
        String lineRead;
        String[] apptArgs;
        String[] dateTime;
        AppointmentBook apptBook;
        Appointment appt;

        try {
            BufferedReader myReader = new BufferedReader(new FileReader(file));

            //count the # of lines in the file
            while(myReader.readLine() != null)
                ++lineCount;

            myReader.close();
            myReader = new BufferedReader(new FileReader(file));

            if(lineCount == 0) {
                System.out.println("Parse Function: file is empty" +
                        "\nClosing File...");
                myReader.close();
                return null;
            }

            owner = myReader.readLine(); //read in first line of file which should be the owner name

            //Create Appointment Book w/ specified owner
            apptBook = new AppointmentBook(owner);

            --lineCount;
            while(lineCount > 0) {
                lineRead = myReader.readLine();

                if(lineRead != null) {
                    apptArgs = lineRead.split("#");   //divide arguments provided into (start Date/Time), (end Date/Time)
                    argCount = apptArgs.length;

                    if (argCount < 3) {
                        myReader.close();
                        System.err.println("Not Enough Arguments in File provided");
                        return null;
                    } else
                        descrip = apptArgs[0];
                }
                else {
                    System.out.println("The lineRead variable is null");
                    return null;
                }

                dateTime = apptArgs[1].split(" @ "); //divide start DATE/TIME into DATE and TIME

                if(dateTime.length != 2) {
                    myReader.close();
                    return null;
                }
                    startDate = dateTime[0];
                    startTime = dateTime[1];

                    dateTime = apptArgs[2].split(" @ "); //divide end DATE/TIME into DATE and TIME

                if(dateTime.length != 2) {
                    myReader.close();
                    return null;
                }
                    endDate = dateTime[0];
                    endTime = dateTime[1];
//p3****************************************************
   //This section is added due to the fact that Date is written out using DateFormat.SHORT (per assignment guidelines),
   //which only displays the 2-digit year (2016 = 16); This section of code adds in the '20' to the year;
    //NOTE that there is a bug where 99 will become 2099 and not 1999, hopefully no one needs to schedule something then
                int startLen = startDate.length();
                int endLen = endDate.length();

                String startCopy = new String();

                for(j = 0; j < startLen - 2; ++j)
                    startCopy += startDate.charAt(j);
                startCopy += "20";
                startCopy += startDate.charAt(startLen-2);
                startCopy += startDate.charAt(startLen-1);

                String endCopy = new String();

                for(j = 0; j < endLen - 2; ++j)
                    endCopy += endDate.charAt(j);
                endCopy += "20";
                endCopy += endDate.charAt(endLen-2);
                endCopy += endDate.charAt(endLen-1);

                //System.out.println("\n\nStartCopy: " + startCopy + "EndCopy: " + endCopy);
//p3****************************************************

                if(!Appointment.checkDateFormat(startCopy, endCopy)) {
                    System.out.println("ERROR -> bad date format");
                    myReader.close();
                    return null;
                }
                if(!Appointment.checkTimeFormat(startTime, endTime)) {
                    System.out.println("ERROR -> bad time format");
                    myReader.close();
                    return null;
                }

                //***********************P3****************************
                int count = 0;
                int matches = 1;
                Date start = null, end = null;

                //all valid forms of date and time
                String[] validDateFormats = { "MM/dd/yyyy hh:mm a",
                        "MM/d/yyyy hh:mm a" ,
                        "M/dd/yyyy hh:mm a",
                        "M/d/yyyy hh:mm a" };

                for(String dates: validDateFormats) {
                    try {
                        Date myDate = null;
                        SimpleDateFormat sdf = new SimpleDateFormat(dates);
                        sdf.setLenient(false);

                        if(count == 0)
                            myDate = sdf.parse(startCopy + " " + startTime); //+beginTime later
                        else
                            myDate = sdf.parse(endCopy + " " + endTime); //+endTime later

                        if(myDate != null) {
                            if(count == 0)
                                start = myDate;
                            else
                                end = myDate;
                            ++count;
                        }

                        if(count == 2) break;

                    } catch (ParseException pe) {
                        System.out.println("Didn't match format #" + matches++ + ", lets try another format.");
                    }

                    if(matches == 5) {
                        System.err.println("Didn't match any format");
                        myReader.close();
                        return null;
                    }
                }

                //****************P3**************************
                appt = new Appointment(descrip, start, end);
                apptBook.addAppointment(appt);
                --lineCount;
            }

            //end added
            myReader.close();

        } catch (IOException e) {
            throw new ParserException("Parse function: The file was not found...creating file called " +
            file + " that the appointment book will be written to" + "\n");
        }
        return apptBook;
    }
}

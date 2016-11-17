package edu.pdx.cs410J.aldridge;

import edu.pdx.cs410J.web.HttpRequestHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project4 {

    public static void main(String... args) {

        String hostName = null;
        String portString = null;
        int port = 0;
        ArrayList<String> argList = new ArrayList<>();
        ArrayList<String> optList = new ArrayList<>();

        parseCommandLine(argList, optList, args);
        //printArgs(argList, optList); //FOR TESTING **************

        boolean hostNameFound = false;
        boolean portFound = false;
        boolean rangePrint = false;
        boolean printLatest = false;
        boolean readMe = false;
        final int argLengthWithDescription = 8;
        final int argLengthNoDescription = 7;
        int argCount;

        for (String options : optList) {
            if (options.compareTo("-host") == 0)
                hostNameFound = true;
            else if (options.compareTo("-port") == 0)
                portFound = true;
            else if (options.compareTo("-search") == 0)
                rangePrint = true;
            else if (options.compareTo("-print") == 0)
                printLatest = true;
            else if (options.compareTo("-README") == 0)
                readMe = true;
            else if(hostNameFound) {
                hostName = options;
                hostNameFound = false;
            }
            else if(portFound) {
                portString = options;
                portFound = false;
            }
            //rangePrint, printLatest, readMe will be used later to execute respective functionality
        }

        if(readMe) {
            printReadMe();
            System.exit(0);
        }

        if(hostName == null && portString != null) {
            System.err.println("ERROR -> missing HOSTNAME");
            System.exit(1);
        }
        else if(hostName != null && portString == null) {
            System.err.println("ERROR -> missing PORT");
            System.exit(1);
        }

        if(portString != null) {
            try {
                port = Integer.parseInt(portString);
            } catch (NumberFormatException ex) {
                System.err.println("Port \"" + portString + "\" must be an integer");
                System.out.println("\nExiting...");
                System.exit(1);
            }
            //System.out.println("Port: " + port);
            //System.out.println("Hostname: " + hostName);
        }

        if(port > 63737) {
            System.err.println("The port number, " + port + ", that you provided may be too large...try 8080");
            System.exit(1);
        }

        if(rangePrint)
            argCount = argLengthNoDescription; //allows 7 arguments max
        else
            argCount = argLengthWithDescription; //allows for 8 arguments max

        if(argList.size() > argCount)
            tooManyArgs(argCount);
        else if(argList.size() < argCount)
            notEnoughArgs(argCount);

        String owner;
        String description = null;
        String beginDate;
        String beginTime;
        String endDate;
        String endTime;
        String beginDateAndTime;
        String endDateAndTime;
        boolean errorFound = false;

        //parse argList into respective fields
        if(rangePrint) {
            owner = argList.get(0);
            beginDate = argList.get(1);
            beginTime = argList.get(2);
            beginTime += " ";
            beginTime += argList.get(3);
            endDate = argList.get(4);
            endTime = argList.get(5);
            endTime += " ";
            endTime += argList.get(6);
        }else {
            //!rangePrint so need description field filled
            owner = argList.get(0);
            description = argList.get(1);
            beginDate = argList.get(2);
            beginTime = argList.get(3);
            beginTime += " ";
            beginTime += argList.get(4);
            endDate = argList.get(5);
            endTime = argList.get(6);
            endTime += " ";
            endTime += argList.get(7);

            if (!Appointment.validDescriptionCheck(description)) {
                System.err.println("\nERROR -> The DESCRIPTION field is empty");
                errorFound = true;
            }
        }

        if (!Appointment.checkDateFormat(beginDate, endDate)) {
            System.err.println("\nERROR -> INVALID DATE FORMAT: " + checkReadme("DATES"));
            errorFound = true;
        }
        if (!Appointment.checkTimeFormat(beginTime, endTime)) {
            System.err.println("\nERROR -> INVALID TIME FORMAT: " + checkReadme("TIMES"));
            errorFound = true;
        }
        if (owner.length() == 0 || owner.matches("( )*")) {
            System.err.println("\nERROR -> The OWNER field is empty");
            errorFound = true;
        }

        /*
        System.out.println("Owner: " + owner + "\ndescription: " +
                ((description == null) ? "<BLANK>" : description) + "\nbeginTime: " + beginDate + " " + beginTime +
        "\nendTime: " + endDate + " " + endTime);
        */

        if (errorFound) {
            System.err.println("Exiting due to ERROR...");
            System.exit(1);
        }

        //concatenate dates with times
        beginDateAndTime = beginDate + " " + beginTime;
        endDateAndTime = endDate + " " + endTime;

        //these objects will be used to test the date ordering
        Date start = stringToDate(beginDateAndTime);
        Date end = stringToDate(endDateAndTime);

        /*
        if(start != null && end != null) {
            System.out.println("\nValid start date: " + start.toString() + "\nValid end date: " + end.toString());
        }
        else {
            System.out.println("Invalid Date/Time provided...exiting");
            System.exit(1);
        }
        */

        if (start.after(end)) {
            System.err.println("ERROR -> The START date/time you provided comes AFTER the END date/time...exiting");
            System.exit(1);
        }

        //format hostName + port into URL String field private to AppointmentBookRestClient
        AppointmentBookRestClient client = new AppointmentBookRestClient(hostName, port);

        //response object
        HttpRequestHelper.Response response;

        //P4
        try {
            if(rangePrint) {

                System.out.println("\nSearching for appointments between " + beginDateAndTime + " and " +
                        endDateAndTime + " (inclusive) for Owner: " + owner);
                response = client.prettyPrintAppointmentBookRange(owner, beginDateAndTime, endDateAndTime);
                System.out.println(response.getContent()); //print whatever the 'response' object got
                System.exit(0);//exit here as search function is complete, and there's nothing left to do

            }else {
                //rangePrint was null, indicating a new appointment is going to be added
                response = client.createAppointment(owner, description, beginDateAndTime, endDateAndTime);
            }
        } catch (IOException ex) {
            System.err.println("ERROR with connecting...due to provided hostname and/or port -> " + ex.getMessage());
            System.exit(1);
        }

        if(printLatest) {
            System.out.println("\nHere is the appointment you just added for owner, " + owner);
            Appointment appointment = new Appointment(description, beginDateAndTime, endDateAndTime);
            appointment.printAllInfo();
        }

        /*
        if (response != null) {
            System.out.println(response.getContent()); //print whatever the 'response' object got
        }
        */

        System.out.println("\nExiting...");
        System.exit(0);
    }
    //END OF MAIN METHOD




    /**
     * Makes sure that the give response has the expected HTTP status code
     *
     * @param code     The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode(int code, HttpRequestHelper.Response response) {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                    response.getCode(), response.getContent()));
        }
    }


    /**
     *
     * @param message print this message, to standard error, then exit
     */
    private static void error(String message) {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * Prints usage information for this program and exits
     *
     * @param message An error message to print
     */
    private static void usage(String message) {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [key] [value]");
        err.println("  host    Host of web server");
        err.println("  port    Port of web server");
        err.println("  key     Key to query");
        err.println("  value   Value to add to server");
        err.println();
        err.println("This simple program posts key/value pairs to the server");
        err.println("If no value is specified, then all values are printed");
        err.println("If no key is specified, all key/value pairs are printed");
        err.println();

        System.exit(1);
    }

    /**
     * Parses the command line, and fills argList with arguments and optList with options
     * This method DOES NOT check for valid options/arguments --> main will do this error checking
     *
     * @param argList this will fill the  argList with arguments provided
     * @param optList this will fill the optList with the options provided
     * @param args command line arguments
     */
    public static void parseCommandLine(ArrayList<String> argList, ArrayList<String> optList, String[] args) {
        boolean hostNameFound = false;
        boolean portFound = false;

        for (String arg : args) {
            if (arg.length() != 0 && arg.charAt(0) == '-') {
                if (arg.compareTo("-host") == 0)
                    hostNameFound = true;
                    //else if (arg.compareTo("-pretty-") == 0)
                    //prettyPrintOut = true;
                else if (arg.compareTo("-port") == 0)
                    portFound = true;
                optList.add(arg);
            } else if (hostNameFound) {
                optList.add(arg);
                hostNameFound = false;
            } else if (portFound) {
                optList.add(arg);
                portFound = false;
            } else {
                argList.add(arg);
            }
        }
    }


    /**
     * Used for testing - prints the respective lists
     * @param argList provided arguments
     * @param optList options provided
     */

    public static void printArgs(ArrayList<String> argList, ArrayList<String> optList) {
        System.out.print("\nOptions provided: ");
        for (String options : optList) {
            System.out.print(options + " ");
        }
        System.out.println();
        //1 - owner, 2 - description, 3/4 - beginDate + beginTime
        // 5/6 - endDate + endTime
        System.out.print("\nArguments provided: ");
        for (String arguments : argList) {
            System.out.print(arguments + " ");
        }
        System.out.println();
    }



    /**
     * ERROR display function
     */
    public static void notEnoughArgs(int argCount) {
        System.err.println("\nERROR --> There are NOT ENOUGH arguments! There should be " + argCount +
                " arguments " + "(not including the options)\n View the README for specifics on the argument list expected.");
        System.exit(1);
    }


    /**
     * ERROR display function
     */
    public static void tooManyArgs(int argCount) {
        System.err.println("\nERROR --> There are TOO MANY arguments! There should " + argCount + " arguments " +
                "(not including the options)\n View the README for specifics on the argument list expected.");
        System.exit(1);
    }


    /**
     * prints the README
     */
    public static void printReadMe() {
        System.out.println("\n\n***HERE IS THE README***: Josh Aldridge, Project 4, CS410J\n" +
                "\nThis Appontment Book Application will allow a user to enter in appointment information" +
                "\non the command line, and request the program to print out this information." +
                "\n\nusage: java -jar target/apptbook.jar [options] <args>\n" +
                "  args are (in this order):\n" +
                "    owner - The person whose owns the appt book\n" +
                "    description - A nonblank description of the appointment (DO NOT PROVIDE if \"-search\" used)\n" +
                "    beginTime - (date AND time) When the appt begins (12-hour time)\n" +
                "    endTime - (date AND time) When the appt ends (12-hour time)\n" +
                "  options are (options may appear in any order):\n" +
                "    -print                   Prints a description of the new appointment\n" +
                "    -README                  Prints a README for this project and exits\n" +
                "    -host hostname           Host computer on which the server runs\n" +
                "    -port port               Port on which the server is listening\n" +
                "    -search                  Will display ALL appointments in the time range provided (inclusive)\n" +
                "  *NOTE*  Appointments will be sorted by start, then end, then description" +
                " (ordered earliest appointment first)\n" +
                "  *NOTE*  If \"-search\" option is provided, DO NOT provide a DESCRIPTION or program will error and exit\n" +
                "  *NOTE*  Date and time should be in the format: mm/dd/yyyy hh:mm am/pm (or mm/dd/yy hh:mm am/pm)\n" +
                "\n***END OF README***\n");
    }

    /**
     *
     * @param arg is used to indicate which error occurred
     * @return the fill String indicating the error is returned
     */
    public static String checkReadme(String arg) {
        return "CHECK THE README FOR VALID " + arg;
    }


    /**
     *
     * @param aDateAndTime String of Date and Time in format 01/01/2016 12:00 pm
     * @return this function returns the Date representation of the provided String
     *
     */
    public static Date stringToDate(String aDateAndTime) {
        Date myDate = null;
        //System.out.println("\nstringTodate method: aDateAndTime = " + aDateAndTime);
        //Convert user entered date/time string to a Date object
        String[] validDateFormats = {"MM/dd/yyyy hh:mm a",
                "MM/d/yyyy hh:mm a",
                "M/dd/yyyy hh:mm a",
                "M/d/yyyy hh:mm a"};

        for (String dates : validDateFormats) {
            try {

                SimpleDateFormat sdf = new SimpleDateFormat(dates);
                sdf.setLenient(false);
                myDate = sdf.parse(aDateAndTime);

            } catch (ParseException pe) {
                System.out.println("\nchecking date/time formats...");
            }
        }
        return myDate;
    }

}
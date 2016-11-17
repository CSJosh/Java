package edu.pdx.cs410J.aldridge;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.ParserException;
import sun.security.pkcs.ParsingException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The main class for the CS410J appointment book Project
 */

/**
 * README DISPLAY function
 */
public class Project1 {
  public static void printReadMe() {
    System.out.println("\n\n***HERE IS THE README***: Josh Aldridge, Project 3, CS410J" +
            "\nThis Appontment Book Application will allow a user to enter in appointment information" +
            "\non the command line, and request the program to print out this information." +
            "\n\nusage: java -jar target/apptbook-1.0-SNAPSHOT.jar [options] <args>\n" +
            "  args are (in this order):\n" +
            "owner - The person whose owns the appt book\n" +
            "description - A nonblank description of the appointment\n" +
            "beginTime - (date AND time) When the appt begins (12-hour time)\n" +
            "endTime - (date AND time) When the appt ends (12-hour time)\n" +
            "  options are (options may appear in any order):\n" +
            "    -print                   Prints a description of the new appointment\n" +
            "    -README                  Prints a README for this project and exits\n" +
            "    -textFile aFileName.txt  Loads from (if possible) and Saves to \"afileName.txt\"\n" +
            "    -pretty aFileName.txt    Prints a verbose version including duration to file specified\n" +
            "    -pretty -                Prints a verbose version including duration to standard out\n" +
            "  *NOTE*  Both \"pretty\" options will order the appointments by start, then end, then description" +
            " (ordered oldest appointment first)\n" +
            "  Date and time should be in the format: mm/dd/yyyy hh:mm am/pm (or mm/dd/yy hh:mm am/pm)\n" +
            "\n***END OF README***\n");
  }

  /**
   * ERROR display function
   */
  public static void notEnoughArgs() {
    System.err.println("\nERROR --> There are NOT ENOUGH arguments! There should be 6 arguments " +
            "(not including the options)\n View the README for specifics on the argument list expected.");
  }


  /**
   * ERROR display function
   */
  public static void tooManyArgs() {
    System.err.println("\nERROR --> There are TOO MANY arguments! There should 6 arguments " +
            "(not including the options)\n View the README for specifics on the argument list expected.");
  }


  /**
   * This function iterates through the argument list and puts options in the optList, and
   * arguments in the argList
   * @param argList is populated with arguments provided on the command line
   * @param optList is populated with options provided on the command line
   * @param args is the command line input
     */
  public static void parseCommandLine(ArrayList<String> argList, ArrayList<String> optList, String[] args) {
    boolean fileFound = false;
    boolean prettyPrintSomewhere = false;

    for (String arg : args) {
      if (arg.length() != 0 && arg.charAt(0) == '-') {
        if (arg.compareTo("-textFile") == 0)
          fileFound = true;
        //else if (arg.compareTo("-pretty-") == 0)
          //prettyPrintOut = true;
        else if (arg.compareTo("-pretty") == 0)
          prettyPrintSomewhere = true;
        else if (arg.length() == 1 && prettyPrintSomewhere) {//must be just a lone  '-' indicating standard out
          prettyPrintSomewhere = false;
          //continue;
        }
        optList.add(arg);
      } else if (fileFound) {
        optList.add(arg);
        fileFound = false;
      } else if(prettyPrintSomewhere) {
        optList.add(arg);
        prettyPrintSomewhere = false;
      } else {
        argList.add(arg);
      }
    }
  }

  public static String checkReadme(String arg) {
    return "CHECK THE README FOR VALID " + arg;
  }

  public static void main(String[] args) {
    ArrayList<String> arguments = new ArrayList<>();
    ArrayList<String> options = new ArrayList<>();
    boolean printDescription = false;
    boolean errorFound = false;
    boolean fileEntered = false;
    boolean usingFile = false;
    boolean prettyPrintLocationFound = false;
    boolean prettyFile = false;
    boolean prettyOut = false;
    boolean prettyPrintSomeWhere = false;
    int badOptCount = 0;
    String fileName = null;
    String prettyFileName = null;
    TextDumper textDumper;
    TextParser textParser;

    //System.out.println("\nHELLO\n");

    if (args.length == 0) {
      System.err.println("Missing command line arguments");
      System.exit(1);
    }

    /*//COPY CONSTRUCTOR TEST P4
    Appointment testAppointment = new Appointment("testD", "1/1/2016 12:00 pm", "1/1/2016 1:00 pm");
    testAppointment.printAllInfo();
    System.exit(1);
    */

    parseCommandLine(arguments, options, args);

/*
     //*******FOR TESTING ********
    //**PRINT ARGUMENTS AND OPTIONS PROVIDED
    System.out.print("\nOptions provided: ");
    for (String arg : options) {
      System.out.print(arg + " ");
    }
    System.out.println();
    //1 - owner, 2 - description, 3/4 - beginDate + beginTime
    // 5/6 - endDate + endTime
    System.out.print("\nArguments provided: ");
    for (String arg : arguments) {
      System.out.print(arg + " ");
    }
    System.out.println();
*/

    //**CHECK FOR OPTIONS
    for (String opt : options) {
      if (fileEntered) {
        if (opt.charAt(0) == '-') { //files dont begin with a '-'!!!!
          System.err.println("ERROR -> Invlaid file name found: " + fileName);
          ++badOptCount;
          break;
        }
          fileName = opt;
          System.out.println("The file name you provided: " + fileName);

        if (!fileName.contains(".txt")) {
          fileName += ".txt";
          System.out.println("The file name you provided with .txt appended: " + fileName);
        }
        fileEntered = false;
      } else if (prettyPrintSomeWhere) {

        if (opt.charAt(0) == '-') {
          if (opt.length() != 1) {
            System.err.println("ERROR -> Invlaid pretty file name found: " + prettyFileName);
            ++badOptCount;
            break;
          }
          else {
            //opt.length() == 1 indicating just a '-'
            prettyOut = true;
            prettyPrintLocationFound = true;
          }
        }
        else {
          //There was a filename provided
          prettyFileName = opt;
          System.out.println("The pretty file name you provided: " + prettyFileName);

          if (!prettyFileName.contains(".txt")) {
            prettyFileName += ".txt";
            System.out.println("The pretty file name you provided with .txt appended: " + prettyFileName);
          }
          prettyFile = true;
          prettyPrintLocationFound = true;
        }
        //prettyFile = false;
      } else {
        if (opt.compareTo("-README") == 0) {
          printReadMe();
          System.exit(1);
        } else if (opt.compareTo("-print") == 0)
          printDescription = true;
        else if (opt.compareTo("-textFile") == 0) {
          fileEntered = usingFile = true;
        } //else if (opt.compareTo("-pretty-") == 0) {
          //prettyOut = true;}
         else if (opt.compareTo("-pretty") == 0) {
          //prettyFile = usingPrettyFile = true;
          prettyPrintSomeWhere = true;
          //do something else
        } else
          ++badOptCount;
      }
    }

    if(prettyPrintSomeWhere && !prettyPrintLocationFound) {
      System.err.println("\nERROR -> NO PRETTY PRINT destination provided!");
      System.exit(1);
    }

    if (badOptCount > 0) {
      System.err.println("ERROR -> " + badOptCount +
              " invalid OPTION" + ((badOptCount > 1) ? "S" : "") +
              " provided: " + checkReadme("OPTIONS"));
      System.exit(1);
    }

    //**ERROR CHECKING
    if (arguments.size() < 8) {
      notEnoughArgs();
      System.exit(1);
    } else if (arguments.size() > 8) {
      tooManyArgs();
      System.exit(1);
    } else {
      String owner = arguments.get(0);
      String description = arguments.get(1);
      String beginDate = arguments.get(2);
      String beginTime = arguments.get(3);
      beginTime += " ";
      beginTime += arguments.get(4);
      String endDate = arguments.get(5);
      String endTime = arguments.get(6);
      endTime += " ";
      endTime += arguments.get(7);

      //System.out.println("Start: " + beginDate + " " + beginTime +
      //        "\nEnd: " + endDate + " " + endTime);

      if (!Appointment.validDescriptionCheck(description)) {
        System.err.println("\nERROR -> The DESCRIPTION field is empty");
        errorFound = true;
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

      if (errorFound)
        System.exit(1);

      //P3
      int matches = 1;
      int count = 0;
      Date start = null;
      Date end = null;


      //Convert user entered date/time string to a Date object
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
            myDate = sdf.parse(beginDate + " " + beginTime); //combine beginDate and beginTime
          else
            myDate = sdf.parse(endDate + " " + endTime); //combine endDate and endTime

          if (myDate != null) {
            if (count == 0)
              start = myDate;
            else
              end = myDate;
            ++count;
          }

          if (count == 2) break;

        } catch (ParseException pe) {
          System.out.println("Didn't match format #" + matches++ + ", lets try another format.");
        }

        if (matches == 5) {
          errorFound = true;
          System.err.println("Didn't match any format");
        }
      }

      if (errorFound) {
        System.err.println("ERROR found with date parsing...exiting program");
        System.exit(1);
      }

      //PRETTY PRINTING FORMAT
      DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
      DateFormat tf = DateFormat.getTimeInstance(DateFormat.FULL);

      //compare .format to toString() output
      if (start != null && end != null) {
        //System.out.println("Begin time: " + df.format(start) + " " + tf.format(start) + "\nEnd time: " + end.toString());


        if (start.after(end)) {
          System.err.println("ERROR -> The START date/time you provided comes AFTER the END date/time...exiting");
          System.exit(1);
        }
      } else
        System.exit(1);

      //create a new appointment object with formatted user input
      Appointment newAppointment = new Appointment(description, start, end);

      AppointmentBook myBook = null;
      AppointmentBook sortedBook = null;

      if (usingFile && fileName != null) {

        textParser = new TextParser(fileName);

        try {
          myBook = textParser.parse();
        } catch (ParserException p) {
          System.err.println("Main: " + p.getMessage());
        }

        //TextParser did not return NULL
        if (myBook != null) {

          if (!myBook.compareOwner(owner)) {
            System.err.println("The owner name provided on the command line does NOT match the " +
                    "owner name in the text file specified. The new appointment information will NOT be saved...exiting");
            System.exit(1);
          }

          myBook.addAppointment(newAppointment);
        } else {//myBook IS NULL
          myBook = new AppointmentBook(owner, newAppointment);
          //System.out.println("myBook was null after parsing");
        }


        textDumper = new TextDumper(fileName);

        try {
          textDumper.dump(myBook); //saved to file but last item unsorted (for some reason)

          //P3 work around to sort last item
          textParser = new TextParser(fileName);
          myBook = textParser.parse();
          textDumper = new TextDumper(fileName);
          sortedBook = myBook.sort();
          textDumper.dump(sortedBook);
          //END P3 work around

        } catch (IOException e) {
          System.err.println("There was an IOexpection in Main with the dump function");
        }
        catch (ParserException p) {
          System.err.println("Main: " + p.getMessage());
        }

      }

      if(prettyFile) {
        PrettyPrinter prettyPrinter = new PrettyPrinter(prettyFileName);

        if(sortedBook == null) {
          sortedBook = new AppointmentBook(owner, newAppointment);
        }

        try {
          System.out.println("Pretty Printer: about to print to file: " + prettyFileName);
          prettyPrinter.dump(sortedBook); //saved to file but last item unsorted (for some reason)
        } catch (IOException e) {
          System.err.println("There was an IOexpection in Main with the dump function");
        }

        //************P4***************
        //System.out.println("Here's the RANGE PRINT");
        //prettyPrinter.rangePrint(sortedBook, start, end);

      }

      if (printDescription) {
        System.out.println("\n---NORMAL---PRINTOUT---");

        if (sortedBook != null) {
          sortedBook.displayAllAppointments();
        }
      }

      if(prettyOut) {
        System.out.println("\n***PRETTY***PRINTOUT***");
        sortedBook.displayAllAppointmentsFull();
      } //else {
        //System.out.println("\n**********Newly added appointment*********** ");
        //newAppointment.printAllInfo();}
    }
  }
}

package edu.pdx.cs410J.aldridge;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class AppointmentBookServlet extends HttpServlet
{
    private final Map<String, AppointmentBook> appointmentBooks = new HashMap<>();

    /**
     * Handles an HTTP GET request from a client by writing the value of the key
     * specified in the "key" HTTP parameter to the HTTP response.  If the "key"
     * parameter is not specified, all of the key/value pairs are written to the
     * HTTP response.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        //owner provided, pretty print the book for this owner
        //both times provided, pretty print in a range
        //if one time provided, but not the other....missing arg, and error
        //if owner null, no owner by that name

        //Appointment parameters are gotten from the getParameter method

        response.setContentType( "text/plain" );

        //String key = getParameter( "key", request );
        String owner = getParameter("owner", request);  //ask for the 'owner' parameter

        //to get around integration test (from Maven) if owner is NULL
        //this means the server doesn't have any appointment books loaded yet
        if(owner == null) {
            //System.out.println("The owner was null");
            noOwnerPrint(response.getWriter());
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        AppointmentBook aBook = getAppointmentBookForOwner(owner);  //give me an appointment book with THAT owner

        if(aBook == null) {
            //System.out.println("The owner was null");
            noOwnerPrint(response.getWriter());
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }


        String beginTime = getParameter("beginTime", request);
        String endTime = getParameter("endTime", request);

        if(beginTime != null && endTime == null) {
            missingRequiredParameter(response, "endTime");
            return;
        }
        else if(beginTime == null && endTime != null) {
            missingRequiredParameter(response, "beginTime");
            return;
        }
        else if(beginTime == null && endTime == null) {
            prettyPrint(aBook, response.getWriter()); //copied from below; pretty print ALL from this book
            return;
        }
        else {
            //beingTime && endTime are NOT null
            prettyPrintRange(aBook, response.getWriter(), beginTime, endTime); //pretty print in a range
        }

        //pretty print entire book if begin/end time are not provided
        //pretty print only in doGet method
        //this method pretty prints the appointment book to the localhost -- tested
        //prettyPrint(aBook, response.getWriter());  //pretty print the appointment book (aBook) to the printWriter

        /*
        if (owner != null) {
            writeValue(owner, response);

        } else {
            writeAllMappings(response);
        }
        */

        response.setStatus(HttpServletResponse.SC_OK);
    }


    /**
     *
     * @param aBook Book to print
     * @param writer writer to use
     * @param beginTime Date/Time
     * @param endTime Date/Time
     */
    private void prettyPrintRange(AppointmentBook aBook, PrintWriter writer, String beginTime, String endTime) {
        PrettyPrinter pretty = new PrettyPrinter(writer);
        int count = 0;
        Date startDateTime = null;
        Date endDateTime = null;

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
                System.err.println("ERROR -> " + pe.getMessage());
            }

        }

        if(startDateTime != null && endDateTime != null)
            pretty.rangePrint(aBook, startDateTime, endDateTime);
    }


    /**
     *
     * @param pw print writer
     * @throws IOException
     */
    private void noOwnerPrint(PrintWriter pw) throws IOException{
        PrettyPrinter pretty = new PrettyPrinter(pw);
        pretty.noOwnerPrint();
    }

    /**
     *
     * @param aBook book to print
     * @param pw print writer
     * @throws IOException
     */
    private void prettyPrint(AppointmentBook aBook, PrintWriter pw) throws IOException {
        PrettyPrinter pretty = new PrettyPrinter(pw);
        pretty.dumpPretty(aBook);
    }


    /**
     *
     * @param owner name of owner
     * @return a book with this owner
     */
    private AppointmentBook getAppointmentBookForOwner(String owner) {
        return this.appointmentBooks.get(owner);
    }

    /**
     * Handles an HTTP POST request by storing the key/value pair specified by the
     * "key" and "value" request parameters.  It writes the key/value pair to the
     * HTTP response.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        String owner = getParameter("owner", request);
        if(owner == null) {
            missingRequiredParameter(response, "owner");
            return;
        }

        String description = getParameter("description", request);
        if(description == null) {
            //System.out.println("DoPost: description is missing!");
            missingRequiredParameter(response, "description");
            return;
        }

        String beginTime = getParameter("beginTime", request);
        if(beginTime == null) {
            missingRequiredParameter(response, "beginTime");
            return;
        }

        String endTime = getParameter("endTime", request);
        if(endTime == null) {
            missingRequiredParameter(response, "endTime");
            return;
        }

        //aBook will still be null if the owner does not exist
        AppointmentBook aBook = getAppointmentBookForOwner(owner);
        System.out.println("DoPost: About to call Appointment Constructor");
        Appointment anAppointment = new Appointment(description, beginTime, endTime);

        if(aBook == null)
            aBook = new AppointmentBook(owner);

        aBook.addAppointment(anAppointment);
        aBook.sort(); //checked and works

        this.appointmentBooks.put(owner, aBook);
        prettyPrint(aBook, response.getWriter()); //this "prints" this new book to the response object
        //main can use response.getContent() to actually pretty print to standard out

        response.setStatus(HttpServletResponse.SC_OK);


/*
        String key = getParameter( "key", request );
        if (key == null) {
            missingRequiredParameter(response, "key");
            return;
        }

        String value = getParameter( "value", request );
        if ( value == null) {
            missingRequiredParameter( response, "value" );
            return;
        }

        this.data.put(key, value);

        PrintWriter pw = response.getWriter();
        pw.println(Messages.mappedKeyValue(key, value));
        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK);
        */
    }

    /**
     * Handles an HTTP DELETE request by removing all key/value pairs.  This
     * behavior is exposed for testing purposes only.  It's probably not
     * something that you'd want a real application to expose.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        this.appointmentBooks.clear();

        PrintWriter pw = response.getWriter();
        pw.println(Messages.allMappingsDeleted());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Writes the value of the given key to the HTTP response.
     *
     * The text of the message is formatted with {@link Messages#getMappingCount(int)}
     * and {@link Messages#formatKeyValuePair(String, String)}
     *
     * e.g. key -> value will be displayed
     */

    private void writeValue( String owner, HttpServletResponse response ) throws IOException
    {
        //String value = this.appointmentBooks.get(key);
        AppointmentBook aBook = this.appointmentBooks.get(owner);

        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount( aBook != null ? 1 : 0 ));
        pw.println(Messages.formatKeyValuePair(owner, (aBook != null ?
                (aBook.getOwnerName() + "'s Appointment Book") : "Null Owner Name!")));

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }


    /**
     * Writes all of the key/value pairs to the HTTP response.
     *
     * The text of the message is formatted with
     * {@link Messages#formatKeyValuePair(String, String)}
     */

    private void writeAllMappings( HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount(appointmentBooks.size()));

        for (Map.Entry<String, AppointmentBook> entry : this.appointmentBooks.entrySet()) {
            pw.println(Messages.formatKeyValuePair(entry.getKey(), entry.getValue().getOwnerName()));
        }

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }


    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

    /*
    @VisibleForTesting
    void setValueForKey(String key, String value) {
        this.data.put(key, value);
    }
*/
/*
    @VisibleForTesting
    String getValueForKey(String key) {
        return this.appointmentBooks.get();
    }
*/

}

package edu.pdx.cs410J.aldridge;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;


//helper for main, rest client will


//visual is coming from the rest client
//get the request and tell the servlet what to do with the request

import java.io.IOException;

/**
 * A helper class for accessing the rest client
 */
public class AppointmentBookRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "apptbook";
    private static final String SERVLET = "appointments";

    private final String url;


    /**
     * Creates a client to the appointment book REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AppointmentBookRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

    /**
     * Returns all keys and values from the server
     */
    public Response getAllKeysAndValues() throws IOException
    {
        return get(this.url );
    }

    /**
     * Returns all values for the given key
     */
    public Response getValues( String key ) throws IOException
    {
        return get(this.url, "key", key);
    }

    public Response addKeyValuePair( String key, String value ) throws IOException
    {
        return postToMyURL("key", key, "value", value);
    }

    @VisibleForTesting
    Response postToMyURL(String... keysAndValues) throws IOException {
        return post(this.url, keysAndValues);
    }

    public Response removeAllMappings() throws IOException {
        return delete(this.url);
    }


    //***************ADDED P4***************
    //project 4 main will call the correct function

    /**
     *
     * @param owner call get method, and pass in owner argument, and the private url
     * @return returns a call of the get method
     * @throws IOException
     */
    //this calls get, which calls doGet; will call this if no -search is provided
    public Response prettyPrintAppointmentBook(String owner) throws IOException {
        return get(this.url, "owner", owner);
    }


    /**
     *Pretty prints the Appointments in Range
     *
     * @param owner owner name
     * @param start Date/Time
     * @param end Date/Time
     * @return a Response
     * @throws IOException
     */
    //this calls get, which calls doGet; will call this if a -search option IS provided
    public Response prettyPrintAppointmentBookRange(String owner, String start, String end) throws IOException {
        return get(this.url, "owner", owner, "beginTime", start, "endTime", end);
    }


    /**
     *
     * @param owner owner name
     * @param description description of appointment
     * @param beginTime Date/Time
     * @param endTime Date/Time
     * @return returns a Response
     * @throws IOException
     */
    //call this in main to test a post, this will call doPost
    public Response createAppointment(String owner, String description, String beginTime, String endTime) throws IOException{
        return post(this.url, "owner", owner, "description", description, "beginTime", beginTime, "endTime", endTime);
    }
}

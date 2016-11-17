package edu.pdx.cs410J.aldridge;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.*;

/**
 * Created by joshuaaldridge on 7/2/16.
 */

public class AppointmentBook extends AbstractAppointmentBook {

    protected String owner;
    protected ArrayList<Appointment> appointments;

    /**
     * Adds owner param to private field, and creates a blank appointment book
     * @param owner String name of the owner of the Appointment Book
     */
    public AppointmentBook(String owner) {
        appointments = new ArrayList<>();
        this.owner = owner;
    }

    /**
     *      * Adds owner param to private field, and creates an appointment book with the appList arg
     * @param owner Adds owner param to private field, and creates a blank appointment book
     * @param apptList copies this list into private appointment list
     */
    public AppointmentBook (String owner, Collection<Appointment> apptList) {
        this.owner = owner;
        this.appointments = new ArrayList<>();
        this.appointments.addAll(apptList);
    }

    /**
     * Adds owner param to private field, and creates an appointment book with the new single appointment
     * @param owner Adds owner param to private field
     * @param toAdd add this to appointment list
     */
    public AppointmentBook (String owner, Appointment toAdd) {
        this.owner = owner;
        this.appointments = new ArrayList<>();
        this.appointments.add(toAdd);
    }


    /**
     * Comapares this.owner to argument
     * @param ownerName
     * @return true if a match
     */
    public boolean compareOwner(String ownerName) {
        return this.owner.equalsIgnoreCase(ownerName);
    }


    /**
     *
     * @return String of the owner name
     */
    @Override
    public String getOwnerName() {
        return owner;
    }

    /**
     *
     * @return an ArrayList of the current appointments (or null)
     */
    @Override
    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    /**
     *
     * @param anAppointment add arg to existing appointmentbook after casting
     */
    @Override
    public void addAppointment(AbstractAppointment anAppointment) {
        appointments.add((Appointment)anAppointment);
    }


    /**
     *
     * @param owner -> String name of an owner
     * @return returns the book if the owner arg matches this.owner; returns null otherwise
     */
    public AppointmentBook getAppointmentsForOwner(String owner) {
        if(this.appointments == null || !this.owner.equals(owner))
            return null;

        return this;
    }


    /**
     * display all appointments (NON-PRETTY)
     */
    public void displayAllAppointments() {
        String descript;

        if(appointments.size() != 0) {
            for(Appointment appt : appointments) {
                descript = appt.getDescription();
                System.out.println("\nFull Appointment Description: ");
                System.out.println("Start: " + appt.getBeginTimeString() + "\nEnd: " +
                        appt.getEndTimeString() + "\nDescription: " +
                        (!descript.matches("( )*") ? "\"" + appt.getDescription() + "\"" : "<BLANK>"));
            }
        }
    }


    /**
     * display all appointments in PRETTY FORMAT
     */
    public void displayAllAppointmentsFull() {
        String descript;
        if(appointments.size() != 0) {
            System.out.println("\nFull Appointment Descriptions: ");
            for(Appointment appt : appointments) {
                descript = appt.getDescription();
                System.out.println("\nDescription: " + (!descript.matches("( )*") ? "\"" +
                        appt.getDescription() + "\"" : "<BLANK>") + "\nStart: " + appt.getFullBeginTimeString() +
                        "\nEnd: " + appt.getFullEndTimeString() + "\n" + appt.getDuration());
            }
        }
    }


    /**
     *
     * @return sorts appointments by start, then end, then description
     * uses built-in merge sort method from Collections
     */
    public AppointmentBook sort() {
        Collections.sort(appointments);
        return this;
    }

}
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

    public AppointmentBook(String owner) {
        appointments = new ArrayList<>();
        this.owner = owner;
    }

    public AppointmentBook (String owner, Collection<Appointment> apptList) {
        this.owner = owner;
        this.appointments = new ArrayList<>();
        this.appointments.addAll(apptList);
    }

    public AppointmentBook (String owner, Appointment toAdd) {
        this.owner = owner;
        this.appointments = new ArrayList<>();
        this.appointments.add(toAdd);
    }

    public boolean compareOwner(String ownerName) {
        return this.owner.equalsIgnoreCase(ownerName);
    }


    @Override
    public String getOwnerName() {
        return owner;
    }

    @Override
    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    @Override
    public void addAppointment(AbstractAppointment anAppointment) {
        appointments.add((Appointment)anAppointment);
    }



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

    public AppointmentBook sort() {
        Collections.sort(appointments);
        return this;
    }

}
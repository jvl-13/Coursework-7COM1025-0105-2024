package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class BPCSystem {
    private List<Patient> patients = new ArrayList<>();
    private List<Physiotherapist> physiotherapists = new ArrayList<>();
    private List<Treatment> treatments = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private final int TOTAL_ROOMS = 5;

    private void initializeSampleData() {
        // Add some expertise
        Expertise physiotherapy = new Expertise("Physiotherapy");
        Expertise sportsInjury = new Expertise("Sports Injury");
        Expertise pediatric = new Expertise("Pediatric");
        Expertise neurology = new Expertise("Neurology");

        // Add physiotherapists
        Physiotherapist drAdam = new Physiotherapist("Dr. Adam", "123 Health St", "123-456-7890");
        drAdam.addExpertise(physiotherapy);
        drAdam.addExpertise(sportsInjury);

        Physiotherapist drEve = new Physiotherapist("Dr. Eve", "456 Wellness Ave", "098-765-4321");
        drEve.addExpertise(pediatric);
        drEve.addExpertise(neurology);

        // Add physiotherapists to the system
        physiotherapists.add(drAdam);
        physiotherapists.add(drEve);

        // Add some patients
        Patient johnDoe = new Patient("John Doe", "789 Main Rd", "111-222-3333");
        Patient janeSmith = new Patient("Jane Smith", "101 Oak St", "444-555-6666");

        // Add patients to the system
        patients.add(johnDoe);
        patients.add(janeSmith);

        // Add some treatments
        Treatment treatment1 = new Treatment("Sports Massage", "2025-04-17 10:00", drAdam, 1);
        Treatment treatment2 = new Treatment("Pediatric Therapy", "2025-04-17 12:00", drEve, 2);

        // Add treatments to the system
        treatments.add(treatment1);
        treatments.add(treatment2);

        // Add some bookings
        Booking booking1 = new Booking(johnDoe, treatment1);
        Booking booking2 = new Booking(janeSmith, treatment2);

        // Set status of some bookings (one is booked, one is cancelled)
        booking2.setStatus("cancelled");

        // Add bookings to the system
        bookings.add(booking1);
        bookings.add(booking2);

        System.out.println("Sample data initialized.");
    }


    public void run() {
        Scanner sc = new Scanner(System.in);
        initializeSampleData();
        boolean flag = true;

        while (flag) {
            System.out.println("\n--- BOOST PHYSIO CLINIC SYSTEM ---");
            System.out.println("1. Add Patient");
            System.out.println("2. Remove Patient");
            System.out.println("3. Add Physiotherapist");
            System.out.println("4. Remove Physiotherapist");
            System.out.println("5. Book Appointment");
            System.out.println("6. Change/Cancel Appointment");
            System.out.println("7. Attend Appointment");
            System.out.println("8. Print Report");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addPatient(sc);
                    break;
                case 2:
                    removePatient(sc);
                    break;
                case 3:
                    addPhysiotherapist(sc);
                    break;
                case 4:
                    removePhysiotherapist(sc);
                    break;
                case 5:
                    bookAppointment(sc);
                    break;
                case 6:
                    changeOrCancelBooking(sc);
                    break;
                case 7:
                    attendAppointment(sc);
                    break;
                case 8:
                    printReport();
                    break;
                case 9:
                    System.out.println("Goodbye!");
                    flag = false; // Exit loop when user selects option 9
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            if (flag) {
                System.out.println("\nReturning to main menu...\n");
            }
        }
    }


    private void addPatient(Scanner sc) {
        System.out.print("Enter patient name: ");
        String name = sc.nextLine();
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        System.out.print("Enter phone: ");
        String phone = sc.nextLine();

        Patient p = new Patient(name, address, phone);
        patients.add(p);
        System.out.println("Added new patient: " + p);
        System.out.println(patients);
    }

    private void addPhysiotherapist(Scanner sc) {
        System.out.print("Enter physiotherapist name: ");
        String name = sc.nextLine();
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        System.out.print("Enter phone: ");
        String phone = sc.nextLine();

        Physiotherapist ph = new Physiotherapist(name, address, phone);

        while (true) {
            System.out.print("Enter area of expertise (or type 'done' to finish): ");
            String expertiseName = sc.nextLine();
            if (expertiseName.equalsIgnoreCase("done")) break;
            ph.addExpertise(new Expertise(expertiseName));
        }

        physiotherapists.add(ph);
        System.out.println("Added new physiotherapist: " + ph);
    }

    private void removePatient(Scanner sc) {
        System.out.print("Enter patient ID or name to remove: ");
        String keyword = sc.nextLine().toLowerCase();

        Patient toRemove = null;
        for (Patient p : patients) {
            if (p.getId().equalsIgnoreCase(keyword) || p.getName().toLowerCase().contains(keyword)) {
                toRemove = p;
                break;
            }
        }

        if (toRemove == null) {
            System.out.println("Patient not found.");
            return;
        }

        // remove upcoming booked treatment and release that booking
        for (Booking b : bookings) {
            if (b.getPatient().equals(toRemove) && !b.getStatus().equals("attended")) {
                // change status to "cancelled" and release it
                b.setStatus("cancelled");
                Treatment treatment = b.getTreatment();
                System.out.println("Booking " + b.getId() + " for patient " + b.getPatient().getName() +
                        " has been cancelled, and the treatment slot is now available.");
            }
        }

        // remove patient
        patients.remove(toRemove);
        System.out.println("Patient " + toRemove.getName() + " and their un-attended bookings were removed (cancelled).\n");
    }

    private void removePhysiotherapist(Scanner sc) {
        System.out.print("Enter physiotherapist ID or name to remove: ");
        String keyword = sc.nextLine().toLowerCase();

        Physiotherapist toRemove = null;
        for (Physiotherapist ph : physiotherapists) {
            if (ph.getId().equalsIgnoreCase(keyword) || ph.getName().toLowerCase().contains(keyword)) {
                toRemove = ph;
                break;
            }
        }

        if (toRemove == null) {
            System.out.println("Physiotherapist not found.");
            return;
        }

        // Remove treatments not attended
        Iterator<Treatment> iter = treatments.iterator();
        while (iter.hasNext()) {
            Treatment t = iter.next();
            if (t.getPhysiotherapist().equals(toRemove)) {
                boolean isAttended = bookings.stream()
                        .anyMatch(b -> b.getTreatment().equals(t) && b.getStatus().equals("attended"));
                if (!isAttended) {
                    // Cancel associated bookings
                    for (Booking b : bookings) {
                        if (b.getTreatment().equals(t)) {
                            b.setStatus("cancelled");
                        }
                    }
                    iter.remove();
                }
            }
        }

        physiotherapists.remove(toRemove);
        System.out.println("Physiotherapist " + toRemove.getName() + " removed along with all future appointments.");
    }

    private void bookAppointment(Scanner sc) {
        System.out.print("Enter patient ID: ");
        String patientId = sc.nextLine();
        Patient patient = patients.stream()
                .filter(p -> p.getId().equalsIgnoreCase(patientId))
                .findFirst()
                .orElse(null);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("Book by:\n1. Expertise\n2. Physiotherapist");
        int option = sc.nextInt();
        sc.nextLine();

        List<Treatment> available = new ArrayList<>();
        if (option == 1) {
            System.out.print("Enter expertise: ");
            String exp = sc.nextLine();
            for (Treatment t : treatments) {
                if (t.getPhysiotherapist().hasExpertise(exp) &&
                        bookings.stream().noneMatch(b -> b.getTreatment().equals(t) && b.getStatus().equals("booked"))) {
                    available.add(t);
                }
            }
        } else if (option == 2) {
            System.out.print("Enter physiotherapist name: ");
            String name = sc.nextLine().toLowerCase();
            for (Treatment t : treatments) {
                if (t.getPhysiotherapist().getName().toLowerCase().contains(name) &&
                        bookings.stream().noneMatch(b -> b.getTreatment().equals(t) && b.getStatus().equals("booked"))) {
                    available.add(t);
                }
            }
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        if (available.isEmpty()) {
            System.out.println("No available appointments found.");
            return;
        }

        System.out.println("Available treatments:");
        for (int i = 0; i < available.size(); i++) {
            System.out.println((i + 1) + ". " + available.get(i));
        }

        System.out.print("Select a treatment to book (enter number): ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        if (index < 0 || index >= available.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Treatment selected = available.get(index);

        // Check if patient already booked another appointment at same time
        boolean conflict = bookings.stream()
                .anyMatch(b -> b.getPatient().equals(patient)
                        && b.getTreatment().getDateTime().equals(selected.getDateTime())
                        && b.getStatus().equals("booked"));

        if (conflict) {
            System.out.println("Patient already has another appointment at that time.");
            return;
        }

        // Check if room already taken at that time
        boolean roomConflict = bookings.stream()
                .anyMatch(b -> b.getTreatment().getRoomNumber() == selected.getRoomNumber()
                        && b.getTreatment().getDateTime().equals(selected.getDateTime())
                        && b.getStatus().equals("booked"));

        if (roomConflict) {
            System.out.println("Room " + selected.getRoomNumber() + " is already booked at that time.");
            return;
        }

        Booking newBooking = new Booking(patient, selected);
        bookings.add(newBooking);
        System.out.println("Appointment booked: " + newBooking);
    }

    private void changeOrCancelBooking(Scanner sc) {
        System.out.print("Enter booking ID to change or cancel: ");
        String bookingId = sc.nextLine();

        Booking bookingToModify = bookings.stream()
                .filter(b -> b.getId().equalsIgnoreCase(bookingId))
                .findFirst()
                .orElse(null);

        if (bookingToModify == null) {
            System.out.println("Booking not found.");
            return;
        }

        System.out.println("Booking found: " + bookingToModify);
        System.out.println("1. Change appointment\n2. Cancel appointment");
        int option = sc.nextInt();
        sc.nextLine(); // Consume newline

        if (option == 1) {
            System.out.println("Changing appointment...");
            // Cancel current booking and release the room
            cancelBooking(bookingToModify);

            // Re-book a new appointment
            bookAppointment(sc);  // Re-booking logic, calling bookAppointment method again
        } else if (option == 2) {
            System.out.println("Cancelling appointment...");
            cancelBooking(bookingToModify);
        } else {
            System.out.println("Invalid option.");
        }
    }

    private void cancelBooking(Booking booking) {
        if (booking.getStatus().equals("attended")) {
            System.out.println("Cannot cancel an appointment that has already been attended.");
            return;
        }

        // Mark the booking as cancelled
        booking.setStatus("cancelled");
        System.out.println("Booking " + booking.getId() + " has been cancelled.");

        // Release the treatment slot (room is now available for booking)
        Treatment treatment = booking.getTreatment();
        treatment = null;  // Set treatment to null to indicate the slot is now available
        System.out.println("Room " + treatment.getRoomNumber() + " is now available.");
    }

    private void attendAppointment(Scanner sc) {
        System.out.print("Enter booking ID to mark as attended: ");
        String bookingId = sc.nextLine();

        Booking bookingToAttend = bookings.stream()
                .filter(b -> b.getId().equalsIgnoreCase(bookingId))
                .findFirst()
                .orElse(null);

        if (bookingToAttend == null) {
            System.out.println("Booking not found.");
            return;
        }

        if (bookingToAttend.getStatus().equals("attended")) {
            System.out.println("This appointment has already been attended.");
            return;
        }

        bookingToAttend.setStatus("attended");
        System.out.println("Booking " + bookingToAttend.getId() + " has been marked as attended.");
    }

    private void printReport() {
        System.out.println("\n--- REPORT ---");

        // Print all patients
        System.out.println("\nPatients:");
        if (patients.isEmpty()) {
            System.out.println("No patients available.");
        } else {
            for (Patient p : patients) {
                System.out.println(p);
            }
        }

        // Print all physiotherapists
        System.out.println("\nPhysiotherapists:");
        if (physiotherapists.isEmpty()) {
            System.out.println("No physiotherapists available.");
        } else {
            for (Physiotherapist ph : physiotherapists) {
                System.out.println(ph);
            }
        }

        // Print all treatments
        System.out.println("\nTreatments:");
        if (treatments.isEmpty()) {
            System.out.println("No treatments available.");
        } else {
            for (Treatment t : treatments) {
                System.out.println(t);
            }
        }

        // Print all bookings
        System.out.println("\nBookings:");
        if (bookings.isEmpty()) {
            System.out.println("No bookings available.");
        } else {
            for (Booking b : bookings) {
                System.out.println(b);
            }
        }
    }



    public static void main(String[] args) {
        new BPCSystem().run();
    }
}
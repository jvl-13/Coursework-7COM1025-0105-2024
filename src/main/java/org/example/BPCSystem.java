package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class BPCSystem {
    private List<Patient> patients = new ArrayList<>();
    private List<Physiotherapist> physiotherapists = new ArrayList<>();
    private List<Treatment> treatments = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private final int TOTAL_ROOMS = 5;

    public List<Patient> getPatients() {
        return patients;
    }

    public List<Physiotherapist> getPhysiotherapists() {
        return physiotherapists;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    private void initializeSampleData() {
        // ===== Expertise =====
        Expertise physiotherapy = new Expertise("E1", "Physiotherapy");
        Expertise sportsInjury = new Expertise("E2", "Sports Injury");
        Expertise pediatric = new Expertise("E3", "Pediatric");
        Expertise neurology = new Expertise("E4", "Neurology");

        ExpertiseRegistry.addExpertise(physiotherapy);
        ExpertiseRegistry.addExpertise(sportsInjury);
        ExpertiseRegistry.addExpertise(pediatric);
        ExpertiseRegistry.addExpertise(neurology);

        // ===== Physiotherapists =====
        Physiotherapist ph1 = new Physiotherapist("Alice Smith", "123 Wellness Ave", "0901234567");
        ph1.addExpertise("E1"); ph1.addExpertise("E2");

        Physiotherapist ph2 = new Physiotherapist("Bob Johnson", "456 Recovery Rd", "0902345678");
        ph2.addExpertise("E2"); ph2.addExpertise("E3");

        Physiotherapist ph3 = new Physiotherapist("Carol Lee", "789 Therapy Ln", "0903456789");
        ph3.addExpertise("E1"); ph3.addExpertise("E4");

        Physiotherapist ph4 = new Physiotherapist("David Kim", "321 Flex St", "0904567890");
        ph4.addExpertise("E3"); ph4.addExpertise("E4");

        physiotherapists.addAll(Arrays.asList(ph1, ph2, ph3, ph4));

        // ===== Patients =====
        for (int i = 1; i <= 12; i++) {
            patients.add(new Patient("Patient " + i, "Address " + i, "090" + (1000000 + i)));
        }

        // ===== 4-week Treatment Schedule =====
        String[] treatmentNames = {
                "Neural mobilisation", "Acupuncture", "Massage",
                "Mobilisation of the spine and joints", "Pool rehabilitation"
        };

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 9);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        Random rand = new Random();
        for (Physiotherapist ph : physiotherapists) {
            for (int week = 0; week < 4; week++) {
                for (int day = 0; day < 3; day++) { // 3 working days/week
                    cal.add(Calendar.DATE, 1); // next day
                    for (int slot = 0; slot < 2; slot++) {
                        String treatmentName = treatmentNames[rand.nextInt(treatmentNames.length)];
                        Date date = cal.getTime();
                        Treatment t = new Treatment(treatmentName, date, ph, rand.nextInt(TOTAL_ROOMS) + 1); // only 5 rooms
                        t.setStatus("available");
                        treatments.add(t);
                        cal.add(Calendar.HOUR_OF_DAY, 2); // next time slot
                    }
                    cal.add(Calendar.DATE, 1); // skip one day
                }
                cal.add(Calendar.DATE, 2); // skip to next week
            }
        }
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
            sc.nextLine();

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

    public void addPatient(Scanner sc) {
        try {
            System.out.print("Enter patient name: ");
            String name = sc.nextLine();
            System.out.print("Enter address: ");
            String address = sc.nextLine();
            System.out.print("Enter phone: ");
            String phone = sc.nextLine();

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                throw new InvalidInputException("Fields cannot be empty.");
            }

            if (!phone.matches("^\\+?[0-9]{8,15}$")) {
                throw new InvalidInputException("Invalid phone number format.");
            }

            Patient p = new Patient(name, address, phone);
            patients.add(p);
            System.out.println("Added new patient: " + p);
        } catch (InvalidInputException e) {
            System.out.println("Error adding patient: " + e.getMessage());
        }
    }

    public void addPhysiotherapist(Scanner sc) {
        try {
            System.out.print("Enter physiotherapist name: ");
            String name = sc.nextLine();
            System.out.print("Enter address: ");
            String address = sc.nextLine();
            System.out.print("Enter phone: ");
            String phone = sc.nextLine();

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                throw new InvalidInputException("Name, address, or phone cannot be empty.");
            }

            if (!phone.matches("^\\+?\\d{9,15}$")) {
                throw new InvalidInputException("Invalid phone number format.");
            }

            Physiotherapist ph = new Physiotherapist(name, address, phone);

            List<Expertise> allExpertise = new ArrayList<>(ExpertiseRegistry.getAll());
            if (allExpertise.isEmpty()) {
                System.out.println("No expertise available in the system. Cannot assign expertise.");
            } else {
                //List all exist expertises
                System.out.println("Available Expertise:");
                for (int i = 0; i < allExpertise.size(); i++) {
                    System.out.println((i + 1) + ". " + allExpertise.get(i).getName());
                }
                // Choose expertise for physiotherapist, can choose more than 1
                System.out.print("Enter the numbers of expertise to add (e.g. 1,3): ");
                String[] parts = sc.nextLine().split(",");
                for (String part : parts) {
                    try {
                        int index = Integer.parseInt(part.trim()) - 1;
                        if (index >= 0 && index < allExpertise.size()) {
                            String expId = allExpertise.get(index).getId();
                            ph.addExpertise(expId);
                        } else {
                            System.out.println("Invalid expertise number: " + (index + 1));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input: " + part);
                    }
                }
            }

            physiotherapists.add(ph);
            System.out.println("Added new physiotherapist: " + ph);
        } catch (InvalidInputException e) {
            System.out.println("Error adding physiotherapist: " + e.getMessage());
        }
    }

    public void removePatient(Scanner sc) {
        try {
            if (patients.isEmpty()) {
                System.out.println("No patients in the system.");
                return;
            }
            // List all patients in the system to choose
            System.out.println("Current patients:");
            for (Patient p : patients) {
                System.out.println("- ID: " + p.getId() + ", Name: " + p.getName());
            }

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

            // Cancel all future bookings that haven't attended yet, these bookings will be
            // released for other patients to book.
            for (Booking b : bookings) {
                if (b.getPatient().equals(toRemove) && !b.getStatus().equals("attended")) {
                    b.setStatus("cancelled");
                    Treatment treatment = b.getTreatment();
                    System.out.println("Booking " + b.getId() + " for patient " + b.getPatient().getName() +
                            " has been cancelled, and the treatment slot is now available.");
                }
            }

            patients.remove(toRemove);
            System.out.println("Patient " + toRemove.getName() + " and their un-attended bookings were removed (cancelled).\n");

        } catch (Exception e) {
            System.out.println("Error occurred while removing patient: " + e.getMessage());
        }
        System.out.println(patients);
    }

    public void removePhysiotherapist(Scanner sc) {
        try {
            if (physiotherapists.isEmpty()) {
                System.out.println("No physiotherapists in the system.");
                return;
            }
            // List all physiotherapists in the system
            System.out.println("Current physiotherapists:");
            for (Physiotherapist ph : physiotherapists) {
                System.out.println("- ID: " + ph.getId() + ", Name: " + ph.getName());
            }

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
                        for (Booking b : bookings) {
                            if (b.getTreatment().equals(t)) {
                                b.setStatus("cancelled");
                                System.out.println("Cancelled booking " + b.getId() + " associated with treatment " + t.getId());
                            }
                        }
                        iter.remove();
                        System.out.println("Removed treatment " + t.getId() + " not yet attended.");
                    }
                }
            }

            physiotherapists.remove(toRemove);
            System.out.println("Physiotherapist " + toRemove.getName() + " removed along with all future appointments.\n");

        } catch (Exception e) {
            System.out.println("Error occurred while removing physiotherapist: " + e.getMessage());
        }
        System.out.println(physiotherapists);
    }

    public void bookAppointment(Scanner sc) {
        try {
            if (patients.isEmpty()) {
                System.out.println("No patients available in the system.");
                return;
            }

            // List all patients
            System.out.println("\n--- List of Patients ---");
            for (Patient p : patients) {
                System.out.println("- ID: " + p.getId() + ", Name: " + p.getName());
            }

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

            System.out.println("\nBook by:");
            System.out.println("1. Expertise");
            System.out.println("2. Physiotherapist");
            System.out.print("Enter choice (1 or 2): ");
            int option = Integer.parseInt(sc.nextLine());

            List<Treatment> available = new ArrayList<>();

            if (option == 1) {
                // List available expertise
                List<Expertise> allExpertise = ExpertiseRegistry.getAll();
                if (allExpertise.isEmpty()) {
                    System.out.println("No expertise available.");
                    return;
                }

                System.out.println("\n--- List of Expertise ---");
                for (Expertise exp : allExpertise) {
                    System.out.println("- " + exp.getName());
                }

                System.out.print("Enter expertise name: ");
                String expertiseName = sc.nextLine();

                for (Treatment t : treatments) {
                    if (t.getPhysiotherapist().hasExpertise(expertiseName)
                            && bookings.stream().noneMatch(b -> b.getTreatment().equals(t) && b.getStatus().equals("booked"))) {
                        available.add(t);
                    }
                }
            } else if (option == 2) {
                // List physiotherapists
                System.out.println("\n--- List of Physiotherapists ---");
                for (Physiotherapist ph : physiotherapists) {
                    System.out.println("- " + ph.getName());
                }

                System.out.print("Enter physiotherapist name: ");
                String name = sc.nextLine().toLowerCase();

                for (Treatment t : treatments) {
                    if (t.getPhysiotherapist().getName().toLowerCase().contains(name)
                            && bookings.stream().noneMatch(b -> b.getTreatment().equals(t) && b.getStatus().equals("booked"))) {
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

            // Display available treatments
            System.out.println("\n--- Available Treatments ---");
            for (int i = 0; i < available.size(); i++) {
                System.out.println((i + 1) + ". " + available.get(i));
            }

            System.out.print("Select a treatment to book (enter number): ");
            int index = Integer.parseInt(sc.nextLine()) - 1;

            if (index < 0 || index >= available.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            Treatment selected = available.get(index);

            // Check patient's time conflict
            boolean conflict = bookings.stream()
                    .anyMatch(b -> b.getPatient().equals(patient)
                            && b.getTreatment().getDateTime().equals(selected.getDateTime())
                            && b.getStatus().equals("booked"));

            if (conflict) {
                System.out.println("Patient already has another appointment at that time.");
                return;
            }

            // Check room conflict
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
            selected.setStatus("booked"); // Mark treatment slot as booked
            System.out.println("Appointment booked: " + newBooking);

        } catch (Exception e) {
            System.out.println("Error occurred while booking appointment: " + e.getMessage());
        }
    }

    public void changeOrCancelBooking(Scanner sc) {
        try {
            List<Booking> pendingBookings = bookings.stream()
                    .filter(b -> !b.getStatus().equalsIgnoreCase("attended") && !b.getStatus().equalsIgnoreCase("cancelled"))
                    .toList();

            if (pendingBookings.isEmpty()) {
                System.out.println("No bookings available for change or cancellation.");
                return;
            }

            System.out.println("Pending bookings:");
            for (Booking b : pendingBookings) {
                System.out.println("- ID: " + b.getId() + ", Patient: " + b.getPatient().getName() +
                        ", DateTime: " + b.getTreatment().getDateTime() +
                        ", Physiotherapist: " + b.getTreatment().getPhysiotherapist().getName());
            }

            System.out.print("Enter booking ID to change or cancel: ");
            String bookingId = sc.nextLine();

            Booking bookingToModify = pendingBookings.stream()
                    .filter(b -> b.getId().equalsIgnoreCase(bookingId))
                    .findFirst()
                    .orElse(null);

            if (bookingToModify == null) {
                System.out.println("Booking not found.");
                return;
            }

            System.out.println("Booking found: " + bookingToModify);
            System.out.println("1. Change appointment\n2. Cancel appointment");
            int option = Integer.parseInt(sc.nextLine());

            if (option == 1) {
                Patient patient = bookingToModify.getPatient();
                boolean changedSuccessfully = false;

                while (!changedSuccessfully) {
                    List<Treatment> available = treatments.stream()
                            .filter(t -> !t.equals(bookingToModify.getTreatment()) && t.getStatus().equalsIgnoreCase("available"))
                            .toList();

                    if (available.isEmpty()) {
                        System.out.println("No available appointments to rebook.");
                        return;
                    }

                    System.out.println("Available appointments:");
                    for (int i = 0; i < available.size(); i++) {
                        System.out.println((i + 1) + ". " + available.get(i));
                    }
                    // Change appointment but id of booking is still remained
                    System.out.print("Select a new appointment number to change to (or type 0 to cancel): ");
                    int selected = Integer.parseInt(sc.nextLine()) - 1;

                    if (selected == -1) {
                        System.out.println("Change operation cancelled. Returning to main menu.");
                        return;
                    }

                    if (selected < 0 || selected >= available.size()) {
                        System.out.println("Invalid selection. Try again.");
                        continue;
                    }

                    Treatment newTreatment = available.get(selected);

                    boolean timeConflict = bookings.stream()
                            .anyMatch(b -> !b.getId().equals(bookingToModify.getId()) &&
                                    b.getPatient().equals(patient) &&
                                    b.getStatus().equalsIgnoreCase("booked") &&
                                    b.getTreatment().getDateTime().equals(newTreatment.getDateTime()));

                    boolean roomConflict = bookings.stream()
                            .anyMatch(b -> b.getTreatment().getRoomNumber() == newTreatment.getRoomNumber() &&
                                    b.getTreatment().getDateTime().equals(newTreatment.getDateTime()) &&
                                    b.getStatus().equalsIgnoreCase("booked"));

                    if (timeConflict) {
                        System.out.println("Time conflict: patient already has another appointment at this time.");
                        continue;
                    }

                    if (roomConflict) {
                        System.out.println("Room conflict: selected room is already booked.");
                        continue;
                    }

                    // Perform change
                    bookingToModify.getTreatment().setStatus("available");
                    newTreatment.setStatus("booked");
                    bookingToModify.setTreatment(newTreatment);
                    bookingToModify.setStatus("booked");

                    System.out.println("Booking " + bookingToModify.getId() + " successfully changed.");
                    changedSuccessfully = true;
                }

            } else if (option == 2) {
                cancelBooking(bookingToModify);
            } else {
                System.out.println("Invalid option.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while modifying booking: " + e.getMessage());
        }
    }

    public void cancelBooking(Booking booking) {
        if (booking.getStatus().equals("attended")) {
            System.out.println("Cannot cancel an appointment that has already been attended.");
            return;
        }

        booking.setStatus("cancelled");
        // Release the slot of treatment
        booking.getTreatment().setStatus("available");

        System.out.println("Booking " + booking.getId() + " has been cancelled.");
        System.out.println("Room " + booking.getTreatment().getRoomNumber() + " is now available.");
    }

    public void attendAppointment(Scanner sc) {
        try {
            List<Booking> pendingBookings = bookings.stream()
                    .filter(b -> b.getStatus().equalsIgnoreCase("booked"))
                    .toList();

            if (pendingBookings.isEmpty()) {
                System.out.println("No pending appointments to attend.");
                return;
            }

            System.out.println("Pending Bookings:");
            for (Booking b : pendingBookings) {
                System.out.println(b);
            }

            System.out.print("Enter booking ID to mark as attended: ");
            String bookingId = sc.nextLine();

            Booking bookingToAttend = pendingBookings.stream()
                    .filter(b -> b.getId().equalsIgnoreCase(bookingId))
                    .findFirst()
                    .orElse(null);

            if (bookingToAttend == null) {
                System.out.println("Booking not found or is not eligible for attending (must be 'booked').");
                return;
            }

            bookingToAttend.setStatus("attended");
            System.out.println("Booking " + bookingToAttend.getId() + " has been marked as attended.");

        } catch (Exception e) {
            System.out.println("An error occurred while attending the booking: " + e.getMessage());
        }
    }

    public void printReport() {
        if (physiotherapists.isEmpty()) {
            System.out.println("No physiotherapists found in the system.");
            return;
        }

        System.out.println("=== TREATMENT REPORT (4-WEEK PERIOD) ===\n");

        // Map to count attended appointments per physiotherapist
        Map<Physiotherapist, Integer> attendedCountMap = new HashMap<>();

        for (Physiotherapist ph : physiotherapists) {
            System.out.println("Physiotherapist: " + ph.getName());
            boolean hasAppointment = false;

            for (Treatment t : treatments) {
                if (t.getPhysiotherapist().equals(ph)) {
                    for (Booking b : bookings) {
                        if (b.getTreatment().equals(t)) {
                            hasAppointment = true;

                            System.out.println(" - Treatment: " + t.getName());
                            System.out.println("   Patient: " + b.getPatient().getName());
                            System.out.println("   Time: " + t.getDateTime());
                            System.out.println("   Status: " + b.getStatus());

                            if (b.getStatus().equalsIgnoreCase("attended")) {
                                attendedCountMap.put(ph, attendedCountMap.getOrDefault(ph, 0) + 1);
                            }
                        }
                    }
                }
            }

            if (!hasAppointment) {
                System.out.println("   No appointments in the last 4 weeks.");
            }

            System.out.println();
        }

        // Sort and display physiotherapists by attended count
        System.out.println("=== PHYSIOTHERAPISTS RANKED BY ATTENDED APPOINTMENTS ===");
        attendedCountMap.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))  // Descending order
                .forEach(entry -> {
                    System.out.println(entry.getKey().getName() + ": " + entry.getValue() + " attended appointments");
                });

        System.out.println("==========================================\n");
    }

    public static void main(String[] args) {
        new BPCSystem().run();
    }
}
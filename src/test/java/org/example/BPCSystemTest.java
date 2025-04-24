package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BPCSystemTest {

    private BPCSystem system;
    private Scanner scanner;

    @BeforeEach
    public void setUp() {
        system = new BPCSystem();
        scanner = new Scanner(System.in);
    }

    @Test
    void testAddPatient_validInput() {
        String input = "Alice\n123 Street\n+84987654321\n";
        Scanner sc = new Scanner(input);

        system.addPatient(sc);

        assertEquals(1, system.getPatients().size());
        Patient p = system.getPatients().get(0);
        assertEquals("Alice", p.getName());
    }

    @Test
    void testAddPatient_invalidPhone() {
        String input = "Bob\n456 Avenue\nabc123\n";
        Scanner sc = new Scanner(input);

        system.addPatient(sc);

        assertEquals(0, system.getPatients().size());
    }

    @Test
    void testAddPhysiotherapist_withExpertise() {
        // Setup expertise
        ExpertiseRegistry.addExpertise(new Expertise("E1", "Orthopedic"));
        ExpertiseRegistry.addExpertise(new Expertise("E2", "Neurological"));

        String input = "Charlie\n789 Blvd\n+84911223344\n1,2\n";
        Scanner sc = new Scanner(input);

        system.addPhysiotherapist(sc);

        assertEquals(1, system.getPhysiotherapists().size());
        Physiotherapist ph = system.getPhysiotherapists().get(0);
        assertTrue(ph.hasExpertise("Orthopedic"));
        assertTrue(ph.hasExpertise("Neurological"));
    }

    @Test
    void testRemovePatient_existing() {
        Patient p = new Patient("David", "Test St", "+84912345678");
        system.getPatients().add(p);

        String input = p.getName() + "\n";
        Scanner sc = new Scanner(input);

        system.removePatient(sc);

        assertTrue(system.getPatients().isEmpty());
    }

    @Test
    void testBookAppointment_byExpertise() {
        Patient p = new Patient("Frank", "Ward", "+84900000000");
        system.getPatients().add(p);

        Expertise exp = new Expertise("E1", "Sport");
        ExpertiseRegistry.addExpertise(exp);

        Physiotherapist ph = new Physiotherapist("Grace", "Location", "+84911119999");
        ph.addExpertise("E1");
        system.getPhysiotherapists().add(ph);

        Date date = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            date = sdf.parse("2025-04-30 10:00");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Treatment t = new Treatment( "Stretching", date, ph, 1);
        system.getTreatments().add(t);

        String input = p.getId() + "\n1\nSport\n1\n";
        Scanner sc = new Scanner(input);

        system.bookAppointment(sc);

        assertEquals(1, system.getBookings().size());
        assertEquals("booked", system.getBookings().get(0).getStatus());
    }

    @Test
    void testChangeBooking_success() {
        Patient p = new Patient("Hank", "Hood", "+84988888888");
        Physiotherapist ph = new Physiotherapist("Ian", "Med", "+84999999999");

        Date date1 = null;
        Date date2 = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            date1 = sdf.parse("2025-04-30 10:00");
            date2 = sdf.parse("2025-05-01 08:00");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Treatment t1 = new Treatment("T1", date1,  ph, 1);
        Treatment t2 = new Treatment("T2", date2,  ph, 2);

        system.getPatients().add(p);
        system.getPhysiotherapists().add(ph);
        system.getTreatments().add(t1);
        system.getTreatments().add(t2);

        Booking booking = new Booking(p, t1);
        booking.setStatus("booked");
        t1.setStatus("booked");

        system.getBookings().add(booking);

        String input = booking.getId() + "\n1\n1\n";
        Scanner sc = new Scanner(input);

        system.changeOrCancelBooking(sc);

        assertEquals(t2, booking.getTreatment());
    }

    @Test
    void testCancelBooking() {
        Patient p = new Patient("Jess", "Zone", "+84944444444");
        Physiotherapist ph = new Physiotherapist("Kelly", "Clinic", "+84955555555");
        Date date = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            date = sdf.parse("2025-05-03 09:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Treatment t = new Treatment("T1", date, ph, 1);
        system.getPatients().add(p);
        system.getPhysiotherapists().add(ph);
        system.getTreatments().add(t);

        Booking b = new Booking(p, t);
        b.setStatus("booked");
        t.setStatus("booked");

        system.getBookings().add(b);

        system.cancelBooking(b);

        assertEquals("cancelled", b.getStatus());
        assertEquals("available", t.getStatus());
    }

    @Test
    void testAttendAppointment() {
        Patient p = new Patient("Leo", "Space", "+84900011111");
        Physiotherapist ph = new Physiotherapist("Mira", "Place", "+84900022222");
        Date date = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            date = sdf.parse("2025-05-03 09:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Treatment t = new Treatment("T1", date, ph, 1);
        Booking b = new Booking(p, t);
        b.setStatus("booked");

        system.getPatients().add(p);
        system.getPhysiotherapists().add(ph);
        system.getTreatments().add(t);
        system.getBookings().add(b);

        String input = b.getId() + "\n";
        Scanner sc = new Scanner(input);

        system.attendAppointment(sc);

        assertEquals("attended", b.getStatus());
    }

    @Test
    void testBookAppointment_byExpertise_withTimeAndRoomConflict() {
        // Creating a new patient
        Patient p = new Patient("Frank", "Ward", "+84900000000");
        system.getPatients().add(p);

        // Create expertise and add to registry
        Expertise exp = new Expertise("E1", "Sport");
        ExpertiseRegistry.addExpertise(exp);

        // Creating a physiotherapist with expertise
        Physiotherapist ph = new Physiotherapist("Grace", "Location", "+84911119999");
        ph.addExpertise("E1");
        system.getPhysiotherapists().add(ph);

        // Create two treatments for the same expertise and same time/room
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            date = sdf.parse("2025-04-30 10:00");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // First treatment that gets booked
        Treatment t1 = new Treatment("Stretching", date, ph, 1);
        system.getTreatments().add(t1);

        // Second treatment that is conflicting (same time and room as t1)
        Treatment t2 = new Treatment("Massage", date, ph, 1);
        system.getTreatments().add(t2);

        // Book the first treatment for patient 'p'
        String input = p.getId() + "\n1\nSport\n1\n";
        Scanner sc = new Scanner(input);
        system.bookAppointment(sc);

        // At this point, the first treatment (t1) should be booked
        assertEquals(1, system.getBookings().size());
        assertEquals("booked", system.getBookings().get(0).getStatus());

        // Now, attempt to book the second treatment which has a time and room conflict
        input = p.getId() + "\n1\nSport\n2\n";  // Selecting second treatment (t2) with time/room conflict
        sc = new Scanner(input);
        system.bookAppointment(sc);

        // Ensure the second treatment was not booked due to the time/room conflict
        assertEquals(1, system.getBookings().size()); // The booking count should still be 1, meaning conflict prevented

        // Verify the status of the conflicting treatment (t2) is still available
        assertEquals("available", t2.getStatus());
    }

    @Test
    void testBookingReleaseAfterCancel() {
        Patient p = new Patient("Jess", "Zone", "+84944444444");
        system.getPatients().add(p);

        Expertise exp = new Expertise("E1", "Sport");
        ExpertiseRegistry.addExpertise(exp);

        Physiotherapist ph = new Physiotherapist("Kelly", "Clinic", "+84955555555");
        ph.addExpertise("E1");
        system.getPhysiotherapists().add(ph);

        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            date = sdf.parse("2025-05-03 09:00");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Treatment to be booked
        Treatment t = new Treatment("Sport Massage", date, ph, 1);
        system.getTreatments().add(t);

        String input = p.getId() + "\n1\nSport\n1\n";
        Scanner sc = new Scanner(input);
        system.bookAppointment(sc);

        // Assert that the treatment has been booked successfully
        assertEquals(1, system.getBookings().size(), "Booking count should be 1 after first booking");
        assertEquals("booked", system.getBookings().get(0).getStatus(), "Booking status should be 'booked'");
        assertEquals("booked", t.getStatus(), "Treatment status should be 'booked' after booking");

        // Cancel the booking
        Booking booking = system.getBookings().get(0);
        system.cancelBooking(booking);

        // Assert that the booking status is now cancelled
        assertEquals("cancelled", booking.getStatus(), "Booking status should be 'cancelled' after cancellation");
        assertEquals("available", t.getStatus(), "Treatment status should be 'available' after cancellation");

        // Try to book the same treatment again (it should now be available)
        input = p.getId() + "\n1\nSport\n1\n";  // Selecting the treatment again since it is now available
        sc = new Scanner(input);
        system.bookAppointment(sc);

        // Assert that 2 booking exists (this time the booking was re-made after cancellation)
        assertEquals(2, system.getBookings().size(), "Booking count should be 1 after rebooking");
        assertEquals("cancelled", system.getBookings().get(0).getStatus(), "Booking status should be 'booked' after rebooking");
        assertEquals("booked", t.getStatus(), "Treatment status should be 'booked' after rebooking");
    }

    @Test
    void testBookingIdUnchangedWhenChangingAppointment() {
        Patient patient = new Patient("Max", "Test District", "+84911111111");
        Physiotherapist ph = new Physiotherapist("Nina", "Therapy Center", "+84922222222");
        system.getPatients().add(patient);
        system.getPhysiotherapists().add(ph);

        Date date1 = null, date2 = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            date1 = sdf.parse("2025-06-01 10:00");
            date2 = sdf.parse("2025-06-02 14:00");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Treatment t1 = new Treatment("Massage", date1, ph, 1);
        Treatment t2 = new Treatment("Massage", date2, ph, 2);
        system.getTreatments().add(t1);
        system.getTreatments().add(t2);

        Booking booking = new Booking(patient, t1);
        booking.setStatus("booked");
        t1.setStatus("booked");
        system.getBookings().add(booking);

        String originalBookingId = booking.getId();

        String input = booking.getId() + "\n1\n2\n";
        Scanner sc = new Scanner(input);
        system.changeOrCancelBooking(sc);

        assertEquals(originalBookingId, booking.getId(), "Booking ID should remain unchanged after changing appointment");
        assertEquals("booked", booking.getStatus(), "Booking status should remain 'booked'");
    }
}
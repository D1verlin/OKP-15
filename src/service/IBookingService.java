package service;

import exception.RoomAlreadyBookedException;
import model.Booking;
import model.Room;

import java.time.LocalDate;
import java.util.List;

public interface IBookingService {
    List<Room> getAllRooms();

    List<Booking> getAllBookings();

    List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut);

    Booking bookRoom(int roomNumber, String guestName, LocalDate checkIn, int nights) throws RoomAlreadyBookedException;

    Booking bookRoom(int roomNumber, String guestName, LocalDate checkIn, int nights, double discountPercent) throws RoomAlreadyBookedException;

    boolean cancelBooking(int bookingId);

    double calculateCost(int roomNumber, int nights);
}

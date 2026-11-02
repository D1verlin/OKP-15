package service;

import exception.RoomAlreadyBookedException;
import model.Booking;
import model.Room;
import storage.FileStorage;
import util.Container;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HotelService implements IBookingService {
    private final Container<Room> rooms = new Container<>();
    private final List<Booking> bookings = new ArrayList<>();
    private final String storagePath;
    private int nextBookingId = 1;

    public HotelService(String storagePath) {
        this.storagePath = storagePath;
        try {
            List<Booking> loaded = FileStorage.loadBookings(storagePath);
            bookings.addAll(loaded);
            for (Booking b : loaded) {
                if (b.getId() >= nextBookingId) {
                    nextBookingId = b.getId() + 1;
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    private Room findRoom(int roomNumber) {
        for (Room r : rooms.getAll()) {
            if (r.getRoomNumber() == roomNumber) {
                return r;
            }
        }
        return null;
    }

    @Override
    public List<Room> getAllRooms() {
        return rooms.getAll();
    }

    @Override
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    @Override
    public List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Дата выезда должна быть позже даты заезда.");
        }
        return rooms.filter(room -> isRoomAvailable(room.getRoomNumber(), checkIn, checkOut));
    }

    private boolean isRoomAvailable(int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        for (Booking b : bookings) {
            if (b.getRoomNumber() == roomNumber && b.overlaps(checkIn, checkOut)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Booking bookRoom(int roomNumber, String guestName, LocalDate checkIn, int nights) throws RoomAlreadyBookedException {
        return bookRoom(roomNumber, guestName, checkIn, nights, 0.0);
    }

    @Override
    public Booking bookRoom(int roomNumber, String guestName, LocalDate checkIn, int nights, double discountPercent) throws RoomAlreadyBookedException {
        if (nights <= 0) {
            throw new IllegalArgumentException("Количество суток должно быть больше нуля.");
        }
        Room room = findRoom(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Номер " + roomNumber + " не найден в отеле.");
        }
        LocalDate checkOut = checkIn.plusDays(nights);
        if (!isRoomAvailable(roomNumber, checkIn, checkOut)) {
            throw new RoomAlreadyBookedException(roomNumber, checkIn, checkOut);
        }

        double baseCost = room.calculatePrice(nights);
        double finalCost = baseCost;
        if (discountPercent > 0 && discountPercent <= 100) {
            finalCost = baseCost * (1.0 - discountPercent / 100.0);
        }

        Booking booking = new Booking(nextBookingId++, roomNumber, guestName, checkIn, checkOut, finalCost, true);
        bookings.add(booking);
        persist();
        return booking;
    }

    @Override
    public boolean cancelBooking(int bookingId) {
        for (Booking b : bookings) {
            if (b.getId() == bookingId && b.isActive()) {
                b.cancel();
                persist();
                return true;
            }
        }
        return false;
    }

    @Override
    public double calculateCost(int roomNumber, int nights) {
        Room room = findRoom(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Номер " + roomNumber + " не найден.");
        }
        return room.calculatePrice(nights);
    }

    private void persist() {
        if (storagePath != null) {
            try {
                FileStorage.saveBookings(storagePath, bookings);
            } catch (IOException e) {
                System.err.println("Ошибка при сохранении данных: " + e.getMessage());
            }
        }
    }
}

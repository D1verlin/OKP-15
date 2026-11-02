package model;

import java.time.LocalDate;

public class Booking {
    private final int id;
    private final int roomNumber;
    private final String guestName;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final double cost;
    private boolean active;

    public Booking(int id, int roomNumber, String guestName, LocalDate checkIn, LocalDate checkOut, double cost, boolean active) {
        if (id <= 0 || roomNumber <= 0) {
            throw new IllegalArgumentException("ID и номер комнаты должны быть положительными.");
        }
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя гостя не может быть пустым.");
        }
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Дата выезда должна быть позже даты заезда.");
        }
        this.id = id;
        this.roomNumber = roomNumber;
        this.guestName = guestName.trim();
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.cost = cost;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getGuestName() {
        return guestName;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public double getCost() {
        return cost;
    }

    public boolean isActive() {
        return active;
    }

    public void cancel() {
        this.active = false;
    }

    public boolean overlaps(LocalDate in, LocalDate out) {
        if (!active) {
            return false;
        }
        return in.isBefore(this.checkOut) && this.checkIn.isBefore(out);
    }

    public String toFileString() {
        return id + ";" + roomNumber + ";" + guestName + ";" + checkIn + ";" + checkOut + ";" + cost + ";" + active;
    }

    @Override
    public String toString() {
        return "Бронь #" + id + " | Номер: " + roomNumber + " | Гость: " + guestName +
                " | " + checkIn + " - " + checkOut + " | Сумма: " + cost + " руб. | " +
                (active ? "АКТИВНА" : "ОТМЕНЕНА");
    }
}

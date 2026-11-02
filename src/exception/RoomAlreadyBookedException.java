package exception;

import java.time.LocalDate;

public class RoomAlreadyBookedException extends Exception {
    private final int roomNumber;
    private final LocalDate checkIn;
    private final LocalDate checkOut;

    public RoomAlreadyBookedException(int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        super("Номер " + roomNumber + " уже занят на период с " + checkIn + " по " + checkOut + ".");
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }
}

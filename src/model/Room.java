package model;

public abstract class Room {
    private final int roomNumber;
    private final double basePrice;

    public Room(int roomNumber, double basePrice) {
        if (roomNumber <= 0) {
            throw new IllegalArgumentException("Номер комнаты должен быть больше нуля.");
        }
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Цена за сутки должна быть больше нуля.");
        }
        this.roomNumber = roomNumber;
        this.basePrice = basePrice;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public abstract double calculatePrice(int nights);

    public abstract String getCategory();

    @Override
    public String toString() {
        return "Номер " + roomNumber + " [" + getCategory() + "], цена: " + basePrice + " руб/сутки";
    }
}

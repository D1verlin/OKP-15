package model;

public class StandardRoom extends Room {
    public StandardRoom(int roomNumber, double basePrice) {
        super(roomNumber, basePrice);
    }

    @Override
    public double calculatePrice(int nights) {
        if (nights <= 0) {
            throw new IllegalArgumentException("Количество суток должно быть больше нуля.");
        }
        return getBasePrice() * nights;
    }

    @Override
    public String getCategory() {
        return "Стандарт";
    }
}

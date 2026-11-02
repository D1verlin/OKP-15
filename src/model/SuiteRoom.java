package model;

public class SuiteRoom extends Room {
    private final double luxuryCoeff;

    public SuiteRoom(int roomNumber, double basePrice, double luxuryCoeff) {
        super(roomNumber, basePrice);
        if (luxuryCoeff <= 1.0) {
            throw new IllegalArgumentException("Коэффициент комфорта должен быть больше 1.0.");
        }
        this.luxuryCoeff = luxuryCoeff;
    }

    public double getLuxuryCoeff() {
        return luxuryCoeff;
    }

    @Override
    public double calculatePrice(int nights) {
        if (nights <= 0) {
            throw new IllegalArgumentException("Количество суток должно быть больше нуля.");
        }
        return getBasePrice() * luxuryCoeff * nights;
    }

    @Override
    public String getCategory() {
        return "Повышенная комфортность";
    }
}

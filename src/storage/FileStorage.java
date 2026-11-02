package storage;

import model.Booking;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {

    public static void saveBookings(String filePath, List<Booking> bookings) throws IOException {
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            StringBuilder sb = new StringBuilder();
            for (Booking b : bookings) {
                sb.append(b.toFileString()).append(System.lineSeparator());
            }
            fos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            fos.flush();
        }
    }

    public static List<Booking> loadBookings(String filePath) throws IOException {
        List<Booking> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return list;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = fis.readAllBytes();
            String content = new String(bytes, StandardCharsets.UTF_8);
            String[] lines = content.split("\\r?\\n");
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(";");
                if (parts.length == 7) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        int roomNumber = Integer.parseInt(parts[1]);
                        String guestName = parts[2];
                        LocalDate checkIn = LocalDate.parse(parts[3]);
                        LocalDate checkOut = LocalDate.parse(parts[4]);
                        double cost = Double.parseDouble(parts[5]);
                        boolean active = Boolean.parseBoolean(parts[6]);
                        list.add(new Booking(id, roomNumber, guestName, checkIn, checkOut, cost, active));
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return list;
    }
}

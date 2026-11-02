package app;

import exception.RoomAlreadyBookedException;
import model.Booking;
import model.Room;
import model.StandardRoom;
import model.SuiteRoom;
import service.HotelService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DATA_FILE = "data/bookings.txt";

    public static void runAutomatedDemo(HotelService hotel) {
        System.out.println("=== 1. Список номеров отеля ===");
        for (Room r : hotel.getAllRooms()) {
            System.out.println("  " + r);
        }
        System.out.println();

        System.out.println("=== 2. Расчет стоимости проживания (полиморфизм) ===");
        int nights = 3;
        System.out.println("Номер 101 (Стандарт) за " + nights + " сут: " + hotel.calculateCost(101, nights) + " руб.");
        System.out.println("Номер 201 (Люкс) за " + nights + " сут: " + hotel.calculateCost(201, nights) + " руб.");
        System.out.println();

        System.out.println("=== 3. Создание бронирований ===");
        LocalDate today = LocalDate.now();
        try {
            Booking b1 = hotel.bookRoom(101, "Иванов Иван", today, 4);
            System.out.println("Успешно создана бронь: " + b1);

            Booking b2 = hotel.bookRoom(201, "Петров Петр", today.plusDays(1), 3, 10.0);
            System.out.println("Успешно создана бронь со скидкой (перегрузка): " + b2);
        } catch (RoomAlreadyBookedException e) {
            System.out.println("Ошибка бронирования: " + e.getMessage());
        }
        System.out.println();

        System.out.println("=== 4. Попытка повторного бронирования на пересекающийся период ===");
        try {
            System.out.println("Попытка забронировать номер 101 на дату " + today.plusDays(2) + " на 2 суток...");
            hotel.bookRoom(101, "Сидоров Алексей", today.plusDays(2), 2);
        } catch (RoomAlreadyBookedException e) {
            System.out.println("Перехвачено исключение (RoomAlreadyBookedException): " + e.getMessage());
        } finally {
            System.out.println("[finally] Проверка доступности номера завершена.");
        }
        System.out.println();

        System.out.println("=== 5. Поиск доступных номеров ===");
        LocalDate searchIn = today.plusDays(1);
        LocalDate searchOut = today.plusDays(3);
        List<Room> available = hotel.findAvailableRooms(searchIn, searchOut);
        System.out.println("Доступно номеров с " + searchIn + " по " + searchOut + ": " + available.size());
        for (Room r : available) {
            System.out.println("  " + r);
        }
        System.out.println();

        System.out.println("=== 6. Отмена бронирования ===");
        boolean cancelled = hotel.cancelBooking(1);
        System.out.println("Бронь #1 отменена: " + cancelled);
        List<Room> afterCancel = hotel.findAvailableRooms(searchIn, searchOut);
        System.out.println("Доступно номеров после отмены брони #1: " + afterCancel.size());
        System.out.println();
    }

    public static void main(String[] args) {
        HotelService hotel = new HotelService(DATA_FILE);

        if (hotel.getAllRooms().isEmpty()) {
            hotel.addRoom(new StandardRoom(101, 2500.0));
            hotel.addRoom(new StandardRoom(102, 2800.0));
            hotel.addRoom(new StandardRoom(103, 3000.0));
            hotel.addRoom(new SuiteRoom(201, 5000.0, 1.5));
            hotel.addRoom(new SuiteRoom(202, 6000.0, 1.8));
        }

        runAutomatedDemo(hotel);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("--- Меню бронирования номеров (ЛР №15) ---");
            System.out.println("1. Список номеров");
            System.out.println("2. Поиск свободных номеров");
            System.out.println("3. Забронировать номер");
            System.out.println("4. Расчет стоимости");
            System.out.println("5. Отменить бронирование");
            System.out.println("6. Все бронирования");
            System.out.println("7. Выход");
            System.out.print("Выберите пункт: ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    for (Room r : hotel.getAllRooms()) {
                        System.out.println("  " + r);
                    }
                    System.out.println();
                    break;
                case "2":
                    try {
                        System.out.print("Дата заезда (ГГГГ-ММ-ДД): ");
                        LocalDate in = LocalDate.parse(scanner.nextLine().trim());
                        System.out.print("Дата выезда (ГГГГ-ММ-ДД): ");
                        LocalDate out = LocalDate.parse(scanner.nextLine().trim());
                        List<Room> free = hotel.findAvailableRooms(in, out);
                        System.out.println("Свободные номера: " + free.size());
                        for (Room r : free) {
                            System.out.println("  " + r);
                        }
                    } catch (Exception e) {
                        System.out.println("Ошибка ввода: " + e.getMessage());
                    }
                    System.out.println();
                    break;
                case "3":
                    try {
                        System.out.print("Номер комнаты: ");
                        int roomNum = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Имя гостя: ");
                        String guest = scanner.nextLine().trim();
                        System.out.print("Дата заезда (ГГГГ-ММ-ДД): ");
                        LocalDate in = LocalDate.parse(scanner.nextLine().trim());
                        System.out.print("Количество суток: ");
                        int nights = Integer.parseInt(scanner.nextLine().trim());

                        Booking b = hotel.bookRoom(roomNum, guest, in, nights);
                        System.out.println("Успешно забронировано: " + b);
                    } catch (RoomAlreadyBookedException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Некорректные данные: " + e.getMessage());
                    } finally {
                        System.out.println("[finally] Операция бронирования завершена.");
                    }
                    System.out.println();
                    break;
                case "4":
                    try {
                        System.out.print("Номер комнаты: ");
                        int roomNum = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Количество суток: ");
                        int nights = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println("Итоговая стоимость: " + hotel.calculateCost(roomNum, nights) + " руб.");
                    } catch (Exception e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                    System.out.println();
                    break;
                case "5":
                    try {
                        System.out.print("ID бронирования: ");
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        boolean ok = hotel.cancelBooking(id);
                        System.out.println(ok ? "Бронь успешно отменена." : "Бронь с таким ID не найдена или уже отменена.");
                    } catch (Exception e) {
                        System.out.println("Ошибка ввода: " + e.getMessage());
                    }
                    System.out.println();
                    break;
                case "6":
                    List<Booking> all = hotel.getAllBookings();
                    System.out.println("Всего записей: " + all.size());
                    for (Booking b : all) {
                        System.out.println("  " + b);
                    }
                    System.out.println();
                    break;
                case "7":
                    System.out.println("Выход из программы.");
                    running = false;
                    break;
                default:
                    System.out.println("Неверный пункт.");
                    break;
            }
        }
    }
}

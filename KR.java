import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class KR {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите данные в произвольном порядке (Фамилия Имя Отчество датарождения номертелефона пол):");
        String input = scanner.nextLine();
        scanner.close();

        try {
            processUserData(input);
        } catch (UserDataException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void processUserData(String input) throws UserDataException, IOException {
        String[] data = input.split("\\s+");

        if (data.length != 6) {
            throw new UserDataException("Неверное количество данных. Ожидается 6 элементов.");
        }

        String surname = data[0];
        String name = data[1];
        String patronymic = data[2];
        String birthDateStr = data[3];
        String phoneNumberStr = data[4];
        String genderStr = data[5];

        // Парсинг даты рождения
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date birthDate;
        try {
            birthDate = dateFormat.parse(birthDateStr);
        } catch (ParseException e) {
            throw new UserDataException("Ошибка парсинга даты рождения. Ожидается формат dd.mm.yyyy.", e);
        }

        // Парсинг номера телефона
        long phoneNumber;
        try {
            phoneNumber = Long.parseLong(phoneNumberStr);
        } catch (NumberFormatException e) {
            throw new UserDataException("Ошибка парсинга номера телефона. Ожидается целое беззнаковое число.", e);
        }

        // Проверка корректности пола
        if (!genderStr.equals("f") && !genderStr.equals("m")) {
            throw new UserDataException("Неверное значение пола. Ожидается 'f' или 'm'.");
        }

        // Создание строки для записи в файл
        String userDataString = surname + " " + name + " " + patronymic + " " + dateFormat.format(birthDate) +
                " " + phoneNumber + " " + genderStr;

        // Запись в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(surname + ".txt", true))) {
            writer.write(userDataString);
            writer.newLine();
        } catch (IOException e) {
            throw new IOException("Ошибка при записи в файл.", e);
        }

        System.out.println("Данные успешно обработаны и записаны в файл " + surname + ".txt");
    }
}

class UserDataException extends Exception {
    public UserDataException(String message) {
        super(message);
    }

    public UserDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
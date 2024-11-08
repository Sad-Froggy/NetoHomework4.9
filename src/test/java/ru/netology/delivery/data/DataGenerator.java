package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }
// test
    public static String getMinDate(long plusDays) {
        return LocalDate.now().plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String getCity() {
        String[] cities = new String[]{"Смоленск", "Уфа", "Тверь", "Санкт-Петербург", "Москва", "Пермь", "Калуга",
                "Рязань", "Вологда"};
        return cities[new Random().nextInt(cities.length - 1)];
    }

    public static String getName() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String getPhoneNum() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.phoneNumber().phoneNumber();
    }

    public static String getWrongPhoneNum() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.numerify("####");
    }

    public static class Registration {
        private Registration() {
        }

        public static UserData getUser() {
            return new UserData(getCity(), getName(), getPhoneNum());
        }

    }

    @Value
    public static class UserData {
        String city;
        String name;
        String phone;
    }


}

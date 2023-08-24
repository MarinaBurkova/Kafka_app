package org.example;
import com.google.gson.JsonObject;

import java.time.LocalDate;

public class TransformUtil {
    public static String transformJson(String inputJson) {
        JsonObject jsonObject = new JsonObject();
        // Парсим входной JSON и извлекаем login и password
        // Далее выполняем преобразования
        String login = jsonObject.get("login").getAsString();
        String password = jsonObject.get("password").getAsString();
        String email = login + "@email.com";
        String date = LocalDate.now().toString();

        // Создаем новый JSON объект с login, password, email и date
        JsonObject transformedObject = new JsonObject();
        transformedObject.addProperty("login", login);
        transformedObject.addProperty("password", password);
        transformedObject.addProperty("email", email);
        transformedObject.addProperty("date", date);

        return transformedObject.toString();
    }
}


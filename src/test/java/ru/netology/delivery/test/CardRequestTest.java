package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CardRequestTest {

    long PLUS_DAYS = 3;
    String APP_ADDRESS = "http://localhost:9999";

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void close() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void preTest() {
        Selenide.open(APP_ADDRESS);
    }

    @Test
    public void shouldSuccessfullySendRequest() {
        var randomUser = DataGenerator.Registration.getUser();

        $("[data-test-id='city'] input").setValue(randomUser.getCity());
        String inputDate = DataGenerator.getMinDate(PLUS_DAYS);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(inputDate);
        $("[data-test-id='name'] input").setValue(randomUser.getName());
        $("[data-test-id='phone'] input").setValue(randomUser.getPhone());
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + inputDate))
                .shouldBe(visible);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        inputDate = DataGenerator.getMinDate(PLUS_DAYS + 1);
        $("[data-test-id='date'] input").setValue(inputDate);
        $("button.button").click();
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно запланирована на " + inputDate));
    }

    @Test
    public void shouldFailSendRequest() {
        preTest();
        var randomUser = DataGenerator.Registration.getUser();

        $("[data-test-id='city'] input").setValue(randomUser.getCity());
        String inputDate = DataGenerator.getMinDate(PLUS_DAYS);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(inputDate);
        $("[data-test-id='name'] input").setValue(randomUser.getName());
        $("[data-test-id='phone'] input").setValue(DataGenerator.getWrongPhoneNum());
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='phone'].input_invalid . input_sub")
                .shouldHave(Condition.text("Неверный формат номера мобильного телефона"))
                .shouldBe(visible);
    }


}

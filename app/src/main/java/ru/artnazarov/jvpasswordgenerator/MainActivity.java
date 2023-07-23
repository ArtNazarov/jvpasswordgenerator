package ru.artnazarov.jvpasswordgenerator;


import androidx.appcompat.app.AppCompatActivity;
// Импортируем библиотеки (можно нажать alt+enter)
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.nio.charset.Charset;
import java.util.Random;


import java.nio.charset.Charset;
import java.util.Random;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switchToMain();




    }

    public void switchToMain(){
        setContentView(R.layout.activity_main);
        eventHandlersForMain();
    }

    public void switchToSettings(){
        setContentView(R.layout.settings_layout);
        eventHandlersForSettings();
    }

    public void eventHandlersForMain(){
        setContentView(R.layout.activity_main);
        // Получаем указатель на кнопку по id
        Button button = (Button)findViewById(R.id.adder);
        // Назначаем обработчик
        button.setOnClickListener(this::onClick);

        Button button2 = (Button)findViewById(R.id.btn_goto_settings);
        button2.setOnClickListener(this::onClick2);
    }

    public  void  eventHandlersForSettings(){
        setContentView(R.layout.settings_layout);

        Button button3 = (Button)findViewById(R.id.btn_return_main);
        button3.setOnClickListener(this::onClick3);

    }


    // Определяем функцию для генерации случайной строки
    public static String generateRandomString(int n, int m) {
        int length = new Random().nextInt(m - n + 1) + n;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String alphanumeric = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphanumeric.length());
            sb.append(alphanumeric.charAt(index));
        }

        return sb.toString();
    }

    public void onClick(View v) {
        // Обработчик события - получатель события по id
        EditText p1 = (EditText) findViewById(R.id.password1);
        EditText p2 = (EditText) findViewById(R.id.password2);
        EditText p3 = (EditText) findViewById(R.id.password3);
        EditText p4 = (EditText) findViewById(R.id.password4);
        EditText p5 = (EditText) findViewById(R.id.password5);

        // Получаем значение из элемента управления
        p1.setText(generateRandomString(10, 12));
        p2.setText(generateRandomString(10, 12));
        p3.setText(generateRandomString(10, 12));
        p4.setText(generateRandomString(10, 12));
        p5.setText(generateRandomString(10, 12));


    }

    public void onClick2(View v) {

        switchToSettings();

    }

    public void onClick3(View v) {


        switchToMain();

    }

}
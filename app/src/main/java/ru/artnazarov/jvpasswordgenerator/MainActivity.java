package ru.artnazarov.jvpasswordgenerator;


import androidx.appcompat.app.AppCompatActivity;
// Импортируем библиотеки (можно нажать alt+enter)
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.nio.charset.Charset;
import java.util.Random;


import java.nio.charset.Charset;
import java.util.Random;



public class MainActivity extends AppCompatActivity {

    protected boolean useBigLetters = true;
    protected boolean useSmallLetters = true;
    protected boolean useNumbers = true;
    protected boolean useSpecialChars = true;


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

        CheckBox chk_Big = (CheckBox) findViewById(R.id.chk_use_big_letters);
        CheckBox chk_Sma = (CheckBox) findViewById(R.id.chk_use_small_letters);
        CheckBox chk_Num = (CheckBox) findViewById(R.id.chk_use_numbers);
        CheckBox chk_Spe = (CheckBox) findViewById(R.id.chk_use_special_chars);

        chk_Big.setChecked(this.useBigLetters);
        chk_Sma.setChecked(this.useSmallLetters);
        chk_Num.setChecked(this.useNumbers);
        chk_Spe.setChecked(this.useSpecialChars);


        chk_Big.setOnCheckedChangeListener(this::onChangeBig);
        chk_Sma.setOnCheckedChangeListener(this::onChangeSma);
        chk_Num.setOnCheckedChangeListener(this::onChangeNum);
        chk_Spe.setOnCheckedChangeListener(this::onChangeSpe);



    }


    // Определяем функцию для генерации случайной строки
    public static String generateRandomString(int n, int m,
                                              boolean needBig,  boolean needSmall,
                                              boolean needNumbers, boolean needSpecial) {

        String numbers_chars = "0123456789";
        String small_chars = "abcdefghijklmnopqrstuvwxyz";
        String big_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String special_chars = "(%*)?@#$~";


        int length = new Random().nextInt(m - n + 1) + n;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();



        String alphanumeric = "~";
        if (needNumbers) { alphanumeric = alphanumeric.concat(numbers_chars); };
        if (needSmall) {alphanumeric = alphanumeric.concat(small_chars); };
        if (needBig) { alphanumeric = alphanumeric.concat(big_chars);};
        if (needSpecial) { alphanumeric = alphanumeric.concat(special_chars);};

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
        p1.setText(generateRandomString(10, 12, this.useBigLetters, this.useSmallLetters, this.useNumbers, this.useSpecialChars));
        p2.setText(generateRandomString(10, 12, this.useBigLetters, this.useSmallLetters, this.useNumbers, this.useSpecialChars));
        p3.setText(generateRandomString(10, 12, this.useBigLetters, this.useSmallLetters, this.useNumbers, this.useSpecialChars));
        p4.setText(generateRandomString(10, 12, this.useBigLetters, this.useSmallLetters, this.useNumbers, this.useSpecialChars));
        p5.setText(generateRandomString(10, 12, this.useBigLetters, this.useSmallLetters, this.useNumbers, this.useSpecialChars));


    }

    public void onClick2(View v) {

        switchToSettings();

    }

    public void onClick3(View v) {


        switchToMain();

    }

    public void onChangeBig(CompoundButton b, boolean isChecked){
        CheckBox chk_Big = (CheckBox) findViewById(R.id.chk_use_big_letters);
        this.useBigLetters = chk_Big.isChecked();

    }

    public void onChangeSma(CompoundButton b, boolean isChecked){

        CheckBox chk_Sma = (CheckBox) findViewById(R.id.chk_use_big_letters);
        this.useSmallLetters = chk_Sma.isChecked();

    }

    public void onChangeNum(CompoundButton b, boolean isChecked){

        CheckBox chk_Num = (CheckBox) findViewById(R.id.chk_use_big_letters);
        this.useNumbers = chk_Num.isChecked();

    }

    public void onChangeSpe(CompoundButton b, boolean isChecked){

            CheckBox chk_Spe = (CheckBox) findViewById(R.id.chk_use_special_chars);
            this.useSpecialChars = chk_Spe.isChecked();

        }



}
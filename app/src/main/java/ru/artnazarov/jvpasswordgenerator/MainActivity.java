package ru.artnazarov.jvpasswordgenerator;


import androidx.appcompat.app.AppCompatActivity;
// Импортируем библиотеки (можно нажать alt+enter)
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.CompoundButton;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    protected boolean useBigLetters = true;
    protected boolean useSmallLetters = true;
    protected boolean useNumbers = true;
    protected boolean useSpecialChars = true;

    protected static HashMap<Character, List<String>> dict = load_list();


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



        String alphanumeric = "";
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







        String pass1 = generateRandomString(10, 12, this.useBigLetters, this.useSmallLetters, this.useNumbers, this.useSpecialChars);
        String pass2 = generateRandomString(10, 12, this.useBigLetters, this.useSmallLetters, this.useNumbers, this.useSpecialChars);
        String pass3 = generateRandomString(10, 12, this.useBigLetters, this.useSmallLetters, this.useNumbers, this.useSpecialChars);

        p1.setText(pass1);
        p2.setText(pass2);
        p3.setText(pass3);


        // Work with mnemonics

        EditText mnemonics1 = (EditText) findViewById(R.id.mnemonics1);
        EditText mnemonics2 = (EditText) findViewById(R.id.mnemonics2);
        EditText mnemonics3 = (EditText) findViewById(R.id.mnemonics3);

        String mnemo1 = getMnemonicByPassword(pass1, MainActivity.dict);
        String mnemo2 = getMnemonicByPassword(pass2, MainActivity.dict);
        String mnemo3 = getMnemonicByPassword(pass3, MainActivity.dict);

        mnemonics1.setText(mnemo1);
        mnemonics2.setText(mnemo2);
        mnemonics3.setText(mnemo3);





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

        CheckBox chk_Num = (CheckBox) findViewById(R.id.chk_use_numbers);
        this.useNumbers = chk_Num.isChecked();

    }

    public void onChangeSpe(CompoundButton b, boolean isChecked){

            CheckBox chk_Spe = (CheckBox) findViewById(R.id.chk_use_special_chars);
            this.useSpecialChars = chk_Spe.isChecked();

        }


    // Вернет случайное слово из словаря
    public static String getRandomWord(HashMap<Character, List<String>> hash, char ch) {
        // Получаем список слов на заданную букву
        List<String> words = hash.get(ch);
        // если список пуст
        if (words == null || words.isEmpty()) {
            // вернуть пустое значение
            return null;
        }
        // генератор случайных чисел
        Random random = new Random();
        // Вернуть случайный индекс исходя из размера списка
        int index = random.nextInt(words.size());
        // Вернуть слово из списка по заданному индексу
        return words.get(index);

    }


    public static HashMap<Character, List<String>> load_list(){

        HashMap<Character, List<String>> d = new HashMap<>();

        d.put('a',  Arrays.asList("ant", "ask", "ace"));
        d.put('b',  Arrays.asList("bat", "box", "bag"));
        d.put('c',  Arrays.asList("cat", "car", "cow"));
        d.put('d',  Arrays.asList("dog", "day", "dip"));
        d.put('e',  Arrays.asList("egg", " ear ", "elm"));
        d.put('f',  Arrays.asList("fan", " fly ", "fox"));
        d.put('g',  Arrays.asList("gum", " gas ", "gem"));
        d.put('h',  Arrays.asList("hat", " hot ", "hop"));
        d.put('i',  Arrays.asList("ice", " ink ", "ivy"));
        d.put('j',  Arrays.asList("jam", " jet ", "joy"));
        d.put('k',  Arrays.asList("key", " kid ", "kit"));
        d.put('l',  Arrays.asList("log", " lap ", "lot"));
        d.put('m',  Arrays.asList("man", " map ", "mud"));
        d.put('n',  Arrays.asList("nut", " new ", "nap"));
        d.put('o',  Arrays.asList("owl", " oak ", "orb"));
        d.put('p',  Arrays.asList("pen", " pig ", "pan"));
        d.put('q',  Arrays.asList("quip", " quay ", "quid"));
        d.put('r',  Arrays.asList("rat", " red ", "rug"));
        d.put('s',  Arrays.asList("sun", " sea ", "sky"));
        d.put('t',  Arrays.asList("tea", " toe ", "top"));
        d.put('u',  Arrays.asList("urn", " use ", "ups"));
        d.put('v',  Arrays.asList("van", " vet ", "vie"));
        d.put('w',  Arrays.asList("wig", " win ", "web"));
        d.put('x',  Arrays.asList("xis", " xiv ", "xii"));
        d.put('y',  Arrays.asList("yak", " yam ", "yew"));
        d.put('z',  Arrays.asList("zoo", " zap ", "zip"));
 


        return d;
    };



    public static String getMnemonicByPassword(String password, HashMap<Character, List<String>> dict){
        String result = "";
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if ( (ch >= 'a') && (ch <= 'z') ){

                result = result.concat( getRandomWord(dict, ch) );

            } else if ( (ch >= 'A') && (ch <= 'Z') ) {

                Character ch2 = ch;

                char lower_ch = ch2.toString().toLowerCase().charAt(0);

                String word = getRandomWord(dict, lower_ch);

                if (word != null)
                        {
                            result = result.concat( word.toUpperCase() ); }
                        else
                        {
                            result = result.concat("!error!");
                        };
                }


            else
            {

                result += ch;

            };
            if ( (i+1) != password.length() ) result = result.concat(" ");
        };
        return result;
    }


}
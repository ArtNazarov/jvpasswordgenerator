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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
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

    protected static int voc_count = -1; // число загруженных слов
    // словарь
    public static HashMap<Character, List<String>> remote_dict = null;
    // загружено?
    public static boolean is_loaded = false;
    // сообщение о сетевой ошибке
    public static String network_error = "";




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


        try {
            HashMap<Character, List<String>> d = load_list();

            if (!d.containsKey('a')){
                mnemonics3.setText("cant find a");
            } else {
                mnemonics3.setText("load key a");
            };

            if (d.isEmpty()){
                mnemonics2.setText("dict is empty");
            } else {
                mnemonics2.setText("dict not empty");
            };

                mnemonics3.setText(
                        MainActivity.network_error.concat(
                                "; voc_count".concat( String.valueOf( MainActivity.voc_count)))) ;

        }
            catch (Exception e){
                mnemonics1.setText(e.toString());
                mnemonics2.setText(e.getStackTrace().toString());
            };

        
        if (MainActivity.is_loaded && !remote_dict.isEmpty() && remote_dict.containsKey('a')){
            mnemonics1.setText(getMnemonicByPassword(pass1, remote_dict));
            mnemonics2.setText(getMnemonicByPassword(pass2, remote_dict));
            mnemonics3.setText(getMnemonicByPassword(pass3, remote_dict));

        }









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


    // Загрузчик в словарь с сортировкой в списки, слов согласно первой букве
    public static HashMap<Character, List<String>> load_list(){




        Thread thread = new Thread(new Runnable() {
            public void run(){

                HashMap<Character, List<String>> d = new HashMap<>();
                try {
                    URL url = new URL("https://raw.githubusercontent.com/ArtNazarov/etc/master/words.xml");
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(url.openStream());
                    doc.getDocumentElement().normalize();

                    NodeList nodeList = doc.getDocumentElement().getChildNodes();
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            String words = node.getTextContent();
                            System.out.println(words);
                            String[] wordArray = words.split(" ");
                            for (String word : wordArray) {




                                char firstLetter = word.charAt(0);
                                if (!d.containsKey(firstLetter)) {
                                    d.put(firstLetter, new ArrayList<String>());
                                }
                                if ((firstLetter >= 'a') && (firstLetter <= 'z')) {
                                    d.get(firstLetter).add(word);
                                };


                            }
                        }


                    }; // end for
                    MainActivity.is_loaded = true;
                    MainActivity.remote_dict = d;
                    MainActivity.network_error = "no errors";
                }
                catch (Exception e) {
                    MainActivity.is_loaded = false;
                    MainActivity.remote_dict = null;
                    MainActivity.network_error = e.toString();
                }

            }
        });

        try {
            thread.start();
            thread.join(); // wait ended
        }
        catch (Exception e){
            System.out.println(e.toString());
        };



        return MainActivity.remote_dict;
    };



    public static String getMnemonicByPassword(String password, HashMap<Character, List<String>> dict){
        String result = "";
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if ( (ch >= 'a') && (ch <= 'z') ){

                result = result.concat( getRandomWord(dict, ch) );

            } else if ( (ch >= 'A') && (ch <= 'Z') ) {

                Character ch2 = ch;

                ch = ch2.toString().toLowerCase().charAt(0);

                result = result.concat( getRandomWord(dict, ch).toUpperCase() );

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
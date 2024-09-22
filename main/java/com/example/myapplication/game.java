package com.example.myapplication;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class game extends AppCompatActivity {
    private EditText searchEditText;
    private Button okButton;
    private LinearLayout selectedWordsContainer;
    private List<String> allWords;
    private List<String> displayedWords;
    private Random random;
    private List<TextView> parallaxTextViews;
    private boolean isSearching = false;
    private Button mainScreenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        searchEditText = findViewById(R.id.searchEditText);
        okButton = findViewById(R.id.okButton);
        mainScreenButton = findViewById(R.id.mainScreenButton);
        selectedWordsContainer = findViewById(R.id.selectedWordsContainer);
        random = new Random();
        parallaxTextViews = new ArrayList<>();

        // Заполняем список всеми словами
        initializeWords();
        // Начальное отображение слов в паралаксе
        displayedWords = new ArrayList<>(allWords);
        displayParallaxWords(displayedWords);

        // Слушатель для поиска
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isSearching = true;
                filterWords(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Слушатель для кнопки "Окей"
        okButton.setOnClickListener(v -> {
            String selectedWord = searchEditText.getText().toString();
            addWordToSelected(selectedWord);
        });

        mainScreenButton.setOnClickListener(v -> {
            Intent intent = new Intent(game.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void initializeWords() {
        allWords = new ArrayList<>();
        allWords.add("Спорт");
        allWords.add("Музыка");
        allWords.add("Искусство");
        allWords.add("Наука");
        allWords.add("Путешествия");
        allWords.add("Порно");
        allWords.add("Хуи пинание");
        allWords.add("Ничего");
        allWords.add("Сон");
        allWords.add("Ебля мам");
        allWords.add("Ебля собак");
        // Добавьте больше слов по желанию
    }

    private void displayParallaxWords(List<String> words) {
        // Очистка предыдущих слов
        for (TextView textView : parallaxTextViews) {
            ((ViewGroup) textView.getParent()).removeView(textView);
        }
        parallaxTextViews.clear();

        for (String word : words) {
            TextView textView = new TextView(this);
            textView.setText(word);
            textView.setTextSize(30);
            textView.setOnClickListener(v -> addWordToSelected(word));
            parallaxTextViews.add(textView);
            ((ViewGroup) findViewById(R.id.parallaxWordsContainer)).addView(textView);
            animateOrbit(textView);
        }
    }

    private void animateOrbit(TextView textView) {
        // Случайный радиус и начальная позиция
        float radius = random.nextInt(300) + 200; // Случайный радиус от 100 до 300
        float startAngle = random.nextFloat() * 360; // Случайный начальный угол
        float distanceTraveled = random.nextFloat() * 360; // Случайное расстояние, которое уже прошло


        // Устанавливаем начальные координаты
        float centerX = 300; // Центр вращения по X
        float centerY = 1000; // Центр вращения по Y
        float startX = centerX + radius * (float) Math.cos(Math.toRadians(startAngle));
        float startY = centerY + radius * (float) Math.sin(Math.toRadians(startAngle));
        textView.setX(startX);
        textView.setY(startY);

        // Случайная длительность анимации
        int duration = random.nextInt(6000) + 6000; // Случайная длительность от 2000 до 5000 мс

        // Создаем анимацию вращения
        ValueAnimator animator = ValueAnimator.ofFloat(0, 360);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();

            // Вычисляем новые координаты
            float x = centerX + radius * (float) Math.cos(Math.toRadians(animatedValue + distanceTraveled));
            float y = centerY + radius * (float) Math.sin(Math.toRadians(animatedValue + distanceTraveled));

            // Изменение масштаба (приближение и удаление)
            float scale = 1 + (float) Math.sin(Math.toRadians(animatedValue)) * 0.5f; // Изменение от 1 до 1.5
            textView.setX(x);
            textView.setY(y);
            textView.setScaleX(scale);
            textView.setScaleY(scale);
        });
        animator.start();
    }

    private void filterWords(String query) {
        List<String> filteredWords = new ArrayList<>();
        for (String word : allWords) {
            if (word.toLowerCase().contains(query.toLowerCase())) {
                filteredWords.add(word);
            }
        }
        // Если нет совпадений, используем все слова
        displayedWords = filteredWords.isEmpty() ? new ArrayList<>(allWords) : filteredWords;

        // Останавливаем анимацию при поиске
        if (isSearching) {
            for (TextView textView : parallaxTextViews) {
                ((ViewGroup) textView.getParent()).removeView(textView);
            }
            parallaxTextViews.clear();
        }
        displayParallaxWords(displayedWords);
    }

    private void addWordToSelected(String word) {
        if (!word.isEmpty() && !isWordInSelected(word)) {
            TextView textView = new TextView(this);
            textView.setText(word);
            selectedWordsContainer.addView(textView);
            searchEditText.setText("");
        }
    }

    private boolean isWordInSelected(String word) {
        for (int i = 0; i < selectedWordsContainer.getChildCount(); i++) {
            TextView textView = (TextView) selectedWordsContainer.getChildAt(i);
            if (textView.getText().toString().equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }

}
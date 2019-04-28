package com.example.udacity_6_news_app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.chip.Chip;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity {

    // Declaring
    private FlexboxLayout chips_layout;
    private EditText interests_edittext;
    private Button go_button;
    private TextView number_of_interests, welcome_textview;

    // Variables
    private int interest_counter = 1;
    private String Interest;
    private ArrayList<String> Chips = new ArrayList<>();
    final Chip[] chip = new Chip[11];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve users interest and add it to the chips ArrayList
        String interest = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Interest", "Movies");
        Chips.add(interest);

        // Initialize Views
        init();

        // Setting Animation
        final Animation animShake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);

        // Set number of interests to default amount
        String converted_interest_counter = getResources().getString(R.string.interests_left, interest_counter);
        number_of_interests.setText(converted_interest_counter);

        // Justifying chips_layout
        chips_layout.setJustifyContent(JustifyContent.FLEX_START);
        chips_layout.setFlexDirection(FlexDirection.ROW);
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);

        // This section was originally designed for the ArrayList
        // Due to time constraints I had to keep it to a single string.
        for (int i = 0; i < Chips.size(); i++) {
            final int Index = i;
            // Initialize a new chip instance
            chip[Index] = new Chip(this, null, R.style.Widget_MaterialComponents_Chip_Filter);
            chip[Index].setId(i);
            chip[Index].setText(Chips.get(i));
            chip[Index].setHeight(20);
            chip[Index].setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.dark_blue)));
            chip[Index].setTextColor(getResources().getColor(R.color.white));
            chip[Index].setLayoutParams(params);
            chip[Index].setPadding(3, 3, 3, 3);
            chip[Index].setChipIcon(getResources().getDrawable(R.drawable.ic_close_12dp));
            chip[Index].isClickable();

            chip[Index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chip[Index].getId() == v.getId()) {
                        chip[Index].setVisibility(View.GONE);
                        String tempChipText = String.valueOf(chip[Index].getText());
                        Chips.remove(tempChipText);

                        decreaseInterest();
                        if (interest_counter == 0) {
                            go_button.setEnabled(false);
                        } // if(interest_counter == 0)
                    }
                }
            });

            // Finally, add the chip to chip group
            chips_layout.addView(chip[Index]);
        } // End for loop

        // This button adds a chip if the EditText is filled
        // If the EditText is empty and the current interests is not 0 then go to next Activity
        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Take the value from the EditText and give to the string "interest"
                if (interest_counter == 1 && go_button.getText().equals("Add item")) {
                    hideEverything(view);
                    Toast.makeText(MainActivity.this, "Whoa whoa!\n One interest at a time!", Toast.LENGTH_SHORT).show();
                } else if (interests_edittext.getText().toString().trim().length() == 0) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("Interest", Chips.get(0)).apply();

                    // Retrieving news
                    Toast.makeText(MainActivity.this, "Retrieving news", Toast.LENGTH_SHORT).show();

                    // Bring user to News Menu
                    Intent i = new Intent(MainActivity.this, NewsMenu.class);
                    startActivity(i);
                } else {
                    Interest = String.valueOf(interests_edittext.getText()).trim();
                    addChip(Interest);
                    increaseInterest();
                    hideEverything(view);
                }
            } // End onClick
        }); // End go_button onClick

        // When the user clicks off the EditText, hide the keyboard
        interests_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

        // Listen to the EditText
        interests_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Give the EditText a shake to prompt the users attention
                if (s.toString().trim().length() == 15) {
                    interests_edittext.startAnimation(animShake);
                    Toast.makeText(MainActivity.this, "Maximum characters has been reached!", Toast.LENGTH_SHORT).show();
                }

                // If edit text is empty hide add_button
                if (s.toString().trim().length() == 0 && interest_counter == 0) {
                    go_button.setEnabled(false);
                    go_button.setText(R.string.go_button_text);
                } else if (s.toString().trim().length() == 0) {
                    go_button.setEnabled(true);
                    go_button.setText(R.string.go_button_text);
                } else {
                    go_button.setEnabled(true);
                    go_button.setText(R.string.add_button_text);
                }
            } // End onTextChanged

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    } // End onCreate

    public void addChip(String Interest) {
        Chips.add(Interest);
        final int Index = Chips.size();

        // Initialize a new chip instance
        chip[Index] = new Chip(this, null, R.style.Widget_MaterialComponents_Chip_Filter);
        chip[Index].setId(Index);
        chip[Index].setText(Interest);
        chip[Index].setHeight(20);
        chip[Index].setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.dark_blue)));
        chip[Index].setTextColor(getResources().getColor(R.color.white));
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        chip[Index].setLayoutParams(params);
        chip[Index].setPadding(3, 3, 3, 3);
        chip[Index].setChipIcon(getResources().getDrawable(R.drawable.ic_close_12dp));
        chip[Index].isClickable();

        chip[Index].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip[Index].getId() == v.getId()) {
                    //Toast.makeText(MainActivity.this, "Current Item: + " + Chips.get(Index), Toast.LENGTH_SHORT).show();
                    chip[Index].setVisibility(View.GONE);
                    String tempChipText = String.valueOf(chip[Index].getText());
                    Chips.remove(tempChipText);

                    decreaseInterest();
                    if (interest_counter == 0) {
                        go_button.setEnabled(false);
                    } // if(interest_counter == 0)
                }
            }
        });

        // Finally, add the chip to chip group
        chips_layout.addView(chip[Index]);
    } // End add chip

    public void hideEverything(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        interests_edittext.setText(null);
        Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
        interests_edittext.startAnimation(anim);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void increaseInterest() {
        interest_counter++;
        String converted_interest_counter = getResources().getString(R.string.interests_left, interest_counter);
        number_of_interests.setText(converted_interest_counter);
    }

    public void decreaseInterest() {
        interest_counter--;
        String converted_interest_counter = getResources().getString(R.string.interests_left, interest_counter);
        number_of_interests.setText(converted_interest_counter);
    }

    public void init() {
        welcome_textview = findViewById(R.id.welcome_textview);
        interests_edittext = findViewById(R.id.interests_edittext);
        go_button = findViewById(R.id.go_button);
        number_of_interests = findViewById(R.id.number_of_interests);
        chips_layout = findViewById(R.id.chips_layout);
    }

} // End MainActivity

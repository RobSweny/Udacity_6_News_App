package com.example.udacity_6_news_app;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
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
    private TextView number_of_interests;

    // Variables
    private int interest_counter = 6;
    private String Interest;
    private ArrayList<String> Chips = new ArrayList<>();
    final Chip[] chip = new Chip[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing
        final Animation animShake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
        TextView welcome_textview = findViewById(R.id.welcome_textview);
        interests_edittext = findViewById(R.id.interests_edittext);
        go_button = findViewById(R.id.go_button);
        number_of_interests = findViewById(R.id.number_of_interests);

        String converted_interest_counter = getResources().getString(R.string.interests_left, interest_counter);
        number_of_interests.setText(converted_interest_counter);

        chips_layout = findViewById(R.id.chips_layout);
        chips_layout.setJustifyContent(JustifyContent.FLEX_START);
        chips_layout.setFlexDirection(FlexDirection.ROW);

        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);

        Chips.add("LifeStyle");
        Chips.add("Science");
        Chips.add("Politics");
        Chips.add("Culture");
        Chips.add("Tech");
        Chips.add("Movies");

        for (int i = 0; i < Chips.size(); i++) {
            final int Index = i;

            // Initialize a new chip instance
            chip[Index] = new Chip(this, null, R.style.Widget_MaterialComponents_Chip_Filter);
            chip[Index].setId(i);
            chip[Index].setText(Chips.get(i));
            chip[Index].setHeight(20);

            // Change color to dark blue
            chip[Index].setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.dark_blue)));
            chip[Index].setTextColor(getResources().getColor(R.color.white));
            chip[Index].setLayoutParams(params);
            chip[Index].setPadding(3, 3, 3, 3);

            // Make the chip clickable
            chip[Index].isClickable();

            chip[Index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chip[Index].getId() == v.getId()) {
                        if (chip[Index].getChipIcon() == null) {
                            chip[Index].setChipIcon(getResources().getDrawable(R.drawable.ic_close_12dp));
                            decreaseInterest();

                            if (interest_counter == 0) {
                                go_button.setEnabled(false);
                            } // if(interest_counter == 0)

                        } else {
                            chip[Index].setChipIcon(null);
                            increaseInterest();

                            go_button.setEnabled(true);
                        }
                    }
                }
            });

            // Set chip close icon click listener
            chip[Index].setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Smoothly remove chip from chip group
                    TransitionManager.beginDelayedTransition(chips_layout);
                    chips_layout.removeView(chip[Index]);
                }
            });

            // Finally, add the chip to chip group
            chips_layout.addView(chip[Index]);
        }

        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Take the value from the EditText and give to the string "interest"
                if (interest_counter > 9) {
                    hideEverything(view);
                    Toast.makeText(MainActivity.this, "Whoa whoa!\n Too many interests", Toast.LENGTH_SHORT).show();
                } else if (interests_edittext.getText().toString().trim().length() == 0) {

                    Toast.makeText(MainActivity.this, "Start next activity", Toast.LENGTH_SHORT).show();


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
        chip[Index] = new Chip(this, null, R.style.Widget_MaterialComponents_Chip_Entry);
        chip[Index].setId(Index);
        chip[Index].setText(Interest);
        chip[Index].setHeight(20);

        // Change color to dark blue
        chip[Index].setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.dark_blue)));
        chip[Index].setTextColor(getResources().getColor(R.color.white));

        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        chip[Index].setLayoutParams(params);
        chip[Index].setPadding(3, 3, 3, 3);

        // Make the chip clickable
        chip[Index].isClickable();

        chip[Index].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip[Index].getId() == v.getId()) {
                    //Toast.makeText(MainActivity.this, "Current Item: + " + Chips.get(Index), Toast.LENGTH_SHORT).show();
                    if (chip[Index].getChipIcon() == null) {
                        chip[Index].setChipIcon(getResources().getDrawable(R.drawable.ic_close_12dp));
                        decreaseInterest();

                        if (interest_counter == 0) {
                            go_button.setEnabled(false);
                        } // if(interest_counter == 0)

                    } else {
                        chip[Index].setChipIcon(null);
                        increaseInterest();
                        go_button.setEnabled(true);
                    }
                }
            }
        });

        // Set chip close icon click listener
        chip[Index].setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Smoothly remove chip from chip group
                TransitionManager.beginDelayedTransition(chips_layout);
                chips_layout.removeView(chip[Index]);
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

} // End MainActivity

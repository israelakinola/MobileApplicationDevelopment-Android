package com.seneca.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Calculator extends AppCompatActivity {

    private TextView textResult;
    private LinearLayout historyContainer;
    private boolean lastWasOperator = false;
    private boolean isAdvancedMode = false;
    private final List<String> userInputs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textResult = findViewById(R.id.textResult);
        historyContainer = findViewById(R.id.historyContainer);

        setupButtons();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupButtons() {
        Log.i("Calculator", "Setting Up");

        //all the number button ids
        int[] numberButtons = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
                R.id.btn8, R.id.btn9
        };

        //All the operators button ids
        int[] operatorButtons = {
                R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide
        };

        //Push the text value of a operand button and indicate its not an operator
        View.OnClickListener numberClick = v -> {
            Button b = (Button) v;
            push(b.getText().toString(), false);
        };

        //Push the text value of a operator button and indicate its an operator
        View.OnClickListener operatorClick = v -> {
            Button b = (Button) v;
            push(b.getText().toString(), true);
        };

        //For each button click in the number buttons, attach the numberClick listener
        for (int id : numberButtons) {
            Button btn = findViewById(id);
            if (btn != null) {
                btn.setOnClickListener(numberClick);
            } else {
                Log.e("MainActivity", "Null: " + getResources().getResourceName(id));
            }
        }

        //For each button click in the operator buttons, attach the numberClick listener
        for (int id : operatorButtons) {
            Button btn = findViewById(id);
            if (btn != null) {
                btn.setOnClickListener(operatorClick);
            } else {
                Log.e("MainActivity", "Null: " + getResources().getResourceName(id));
            }
        }
        //Clear the input and Results
        findViewById(R.id.btnClear).setOnClickListener(v -> {
            userInputs.clear();
            lastWasOperator = false;
            textResult.setText("");
        });

        //Do the Calculation when user press "=" button
        findViewById(R.id.btnEqual).setOnClickListener(v -> {
            if (lastWasOperator || userInputs.isEmpty()) return;

            int result = calculate(userInputs);
            String expression = buildExpression() + " = " + result; //Build Result String

            textResult.setText(String.valueOf(result)); //Display Result

            // Save to history only in advanced mode
            if (isAdvancedMode) {
                addHistory(expression);
            }

            // Reset for next calculation
            userInputs.clear();
            lastWasOperator = false;
        });

        //Advanced Mode Functionalities
        findViewById(R.id.btnAdvanced).setOnClickListener(v -> {
            Button btnAdvanced = (Button) v;
            isAdvancedMode = !isAdvancedMode;

            //Make History Visible dppending on the current mode
            if (isAdvancedMode) {
                btnAdvanced.setText("Standard - No History");
                historyContainer.setVisibility(View.VISIBLE);
            } else {
                btnAdvanced.setText("Advance - With History");
                historyContainer.setVisibility(View.GONE);
                historyContainer.removeAllViews();
            }
        });
    }

    private void addHistory(String expression) {
        TextView entry = new TextView(this);
        entry.setText(expression);
        entry.setTextSize(16);
        entry.setPadding(0, 4, 0, 4);
        historyContainer.addView(entry);
    }

    private String buildExpression() {
        StringBuilder sb = new StringBuilder();
        for (String s : userInputs) {
            sb.append(s).append(" ");
        }
        return sb.toString().trim();
    }


    public void push(String value, boolean isOperator) {
        // Block: operator as first input
        if (isOperator && userInputs.isEmpty()) return;

        // Block: two operators back to back
        if (isOperator && lastWasOperator) return;

        // Block: two digits back to back (single digit operands only)
        if (!isOperator && !lastWasOperator && !userInputs.isEmpty()) return;

        userInputs.add(value);
        lastWasOperator = isOperator;

        textResult.setText(buildExpression());
    }

    public int calculate(List<String> values) {
        if (values.isEmpty()) return 0;

        int result;

        //Set result to the first operand(number) value entered
        try {
            result = Integer.parseInt(values.get(0));
        } catch (NumberFormatException e) {
            Log.e("Calculator", "First value is not a number: " + values.get(0));
            return 0;
        }

        //Start a loop from operator to operator
        for (int i = 1; i < values.size(); i += 2) {
            if (i + 1 >= values.size()) break;

            //Get the current operator and the next value (operand)
            String op = values.get(i);
            int next;
            try {
                next = Integer.parseInt(values.get(i + 1));
            } catch (NumberFormatException e) {
                Log.e("Calculator", "Expected number at index " + (i + 1) + ", got: " + values.get(i + 1));
                break;
            }

            //Apply the current operator on the current result and next value(operand)
            switch (op) {
                case "+": result += next; break;
                case "-": result -= next; break;
                case "*": result *= next; break;
                case "/": result = (next != 0) ? result / next : 0; break;
            }
        }

        return result;
    }
}
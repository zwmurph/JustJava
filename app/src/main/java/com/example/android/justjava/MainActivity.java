/**
 * IMPORTANT: Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */
package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Declare global variables
     */
    int quantity = 1;
    double costOfCoffee = 1.95;
    double costOfCream = 0.20;
    double costOfChocolate = 0.30;
    double priceOfOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        String customerName = getNameFromField();

        if (customerName == null) {
            String orderSummary = createOrderSummary(getString(R.string.orderSummaryNoName));
            displaySummary(orderSummary);
        } else {
            String orderSummary = createOrderSummary(customerName);
            if (quantity == 0) {
                displaySummary(orderSummary);
            } else {
                displaySummary(getString(R.string.processingOrder));
                composeEmail(orderSummary);
            }
        }
    }

    /**
     * Creates an order summary for the user
     **/
    private String createOrderSummary(String customerName) {
        String quantityText = getString(R.string.quantity, quantity);
        String creamText = getString(R.string.cream, isCreamChecked());
        String chocolateText = getString(R.string.chocolate, isChocolateChecked());
        String totalPriceText = getString(R.string.total, formatPrice());
        String thankYou = getString(R.string.thankYouMessage);

        if (quantity == 0) {
            return customerName + "\n" + getString(R.string.noOrderText);
        } else {
            return customerName + "\n" + quantityText + "\n" + creamText + "\n" + chocolateText + "\n" + totalPriceText + "\n" + thankYou;
        }
    }

    /**
     * Gets the  contents of the Name entry field
     */
    private String getNameFromField() {
        EditText nameField = (EditText) findViewById(R.id.name_view);
        String userName = nameField.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            nameField.setError(getString(R.string.errorText));
            return null;
        } else {
            return getString(R.string.orderSummaryName, userName);
        }
    }

    /**
     * Gets the status of the Whipped Cream checkbox
     */
    private String isCreamChecked() {
        CheckBox whippedCreamBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        if (whippedCreamBox.isChecked()) {
            return getString(R.string.choiceYes);
        } else {
            return getString(R.string.choiceNo);
        }
    }

    /**
     * Gets the status of the Chocolate checkbox
     */
    private String isChocolateChecked() {
        CheckBox chocolateBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        if (chocolateBox.isChecked()) {
            return getString(R.string.choiceYes);
        } else {
            return getString(R.string.choiceNo);
        }
    }

    /**
     * This method displays the summary of the order to the user
     */
    private void displaySummary(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.orderSummaryTextView);
        orderSummaryTextView.setText(message);
    }

    /**
     * Formats the price for the user using their local currency
     */
    private String formatPrice() {
        return NumberFormat.getCurrencyInstance().format(calculatePrice());
    }

    /**
     * Calculates the price for the user
     */
    private double calculatePrice() {
        priceOfOrder = costOfCoffee;
        if (isCreamChecked() == getString(R.string.choiceYes)) {
            priceOfOrder += costOfCream;
        }
        if (isChocolateChecked() == getString(R.string.choiceYes)) {
            priceOfOrder += costOfChocolate;
        }
        priceOfOrder = priceOfOrder * quantity;
        return priceOfOrder;
    }

    /**
     * Increments the value of quantity by 1
     */
    public void increment(View view) {
        if (quantity == 99) {
            quantity = 99;
        } else {
            quantity = quantity + 1;
        }
        displayQuantity(quantity);
    }

    /**
     * Decrements the value of quantity by 1
     */
    public void decrement(View view) {
        if (quantity == 0) {
            quantity = 0;
        } else {
            quantity = quantity - 1;
        }
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * Calls the email intent an creates an email with the order summary
     */
    public void composeEmail(String emailContents) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.orderSummaryHeader));
        intent.putExtra(Intent.EXTRA_TEXT, emailContents);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
package to.popin.demoapplication;
import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

public class InputDialog {

    // Interface for callback
    public interface InputDialogListener {
        void onInputSubmitted(String name, String mobile);
    }

    public static void showNameAndMobileDialog(Context context, InputDialogListener listener) {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Details");

        // Create a linear layout to hold the EditTexts
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        // Add an EditText for Name
        final EditText nameInput = new EditText(context);
        nameInput.setHint("Name");
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(nameInput);

        // Add an EditText for Mobile Number
        final EditText mobileInput = new EditText(context);
        mobileInput.setHint("Mobile Number");
        mobileInput.setInputType(InputType.TYPE_CLASS_PHONE);
        layout.addView(mobileInput);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Update", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String mobile = mobileInput.getText().toString().trim();

            if (!name.isEmpty() && !mobile.isEmpty()) {
                // Pass data back to the activity via the listener
                listener.onInputSubmitted(name, mobile);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Display the dialog
        builder.show();
    }
}

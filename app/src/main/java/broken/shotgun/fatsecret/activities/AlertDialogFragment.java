package broken.shotgun.fatsecret.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by white on 10/19/2017.
 */

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Something is wrong..")
                .setMessage("Check Network")
                .setPositiveButton("Retry",null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}

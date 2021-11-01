package divyansh.tech.hikaku.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class PermissionChecker {

    private final Activity activity;
    private final View layout;
    private final int requestCode;
    private final String permission;
    private final String description;

    public PermissionChecker(@NonNull Activity activity, @NonNull View layout,
                             int requestCode,
                             @NonNull String permission,
                             @NonNull String description) {
        this.activity = activity;
        this.layout = layout;

        this.requestCode = requestCode;
        this.permission = permission;
        this.description = description;
    }

    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(layout, description, Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermission(activity, permission, requestCode);
                }
            }).show();
        } else {
            // No explanation needed, we can request the permission.
            requestPermission(activity, permission, requestCode);
        }
    }

    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean onRequestPermissionsResult(int requestCode, int[] grantResults) {
        return requestCode == this.requestCode
                && ((grantResults.length > 0)
                && (grantResults[0] == PackageManager.PERMISSION_GRANTED));
    }

    private void requestPermission(Activity activityContext, String permission, int requestCode) {
        ActivityCompat.requestPermissions(activityContext, new String[] { permission }, requestCode);
    }
}

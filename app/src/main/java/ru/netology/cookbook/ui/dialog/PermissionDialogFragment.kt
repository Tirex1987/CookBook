package ru.netology.cookbook.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.netology.cookbook.R

class PermissionDialogFragment(
    private val listener: () -> Unit
): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        return builder
            .setTitle(getString(R.string.permissions))
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(getString(R.string.dialogPermissions))
            .setPositiveButton(android.R.string.ok) { dialog, id ->
                listener()
                dialog.cancel()
            }
            .create()
    }
}
package com.proyect.ds6.ui.components // Adjust package as needed

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/**
 * A generic Composable for selecting an option from a dropdown list.
 * Uses Material 3 ExposedDropdownMenuBox.
 *
 * @param T The type of the items in the list.
 * @param label The label for the dropdown field.
 * @param options The list of available options.
 * @param selectedOption The currently selected option (or null if none selected).
 * @param onOptionSelected Callback function when an option is selected.
 * @param optionToString Function to convert an option object to a String for display.
 * @param enabled Whether the dropdown is enabled for interaction.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownSelector(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T?) -> Unit,
    optionToString: (T) -> String, // Function to get the string representation of an option
    enabled: Boolean = true // Enable/disable the dropdown
) {
    var expanded by remember { mutableStateOf(false) }
    // Get the text to display in the text field (name of selected option or empty)
    val selectedText = selectedOption?.let(optionToString) ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        // Toggle expanded state when the text field is clicked, only if enabled
        onExpandedChange = { expanded = !expanded && enabled },
        modifier = Modifier.fillMaxWidth()
    ) {
        // The text field part of the dropdown
        OutlinedTextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            readOnly = true, // Make the text field read-only
            value = selectedText, // Display the selected option's string
            onValueChange = {}, // No direct text input
            label = { Text(label) }, // Label for the text field
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, // Dropdown arrow icon
            colors = ExposedDropdownMenuDefaults.textFieldColors(), // Default Material 3 colors
            enabled = enabled // Pass enabled state to the TextField
        )

        // The dropdown menu itself
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } // Close the menu when dismissed
        ) {
            // Iterate through options and create a menu item for each
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(optionToString(selectionOption)) }, // Display option as string
                    onClick = {
                        onOptionSelected(selectionOption) // Call the callback with the selected option
                        expanded = false // Close the menu
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding // Default padding
                )
            }
            // Option to clear the selection (only show if something is selected)
            if (selectedOption != null) {
                DropdownMenuItem(
                    text = { Text("Limpiar selección") },
                    onClick = {
                        onOptionSelected(null) // Call the callback with null to clear selection
                        expanded = false // Close the menu
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
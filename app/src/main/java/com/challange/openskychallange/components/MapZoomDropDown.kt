package com.challange.openskychallange.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val zoomOptions = listOf(
    3f, 5f, 7f, 9f, 11f, 13f, 15f, 17f, 19f
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapZoomDropdown(
    modifier: Modifier = Modifier,
    selectedZoom: Float,
    onZoomSelected: (Float) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(
                    type = MenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth(),
            value = selectedZoom.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Map Zoom") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            zoomOptions.forEach { zoom ->
                DropdownMenuItem(
                    text = { Text(zoom.toString()) },
                    onClick = {
                        expanded = false
                        onZoomSelected(zoom)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MapZoomDropdownPreview() {
    var selectedZoom by remember { mutableFloatStateOf(13f) }

    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            MapZoomDropdown(
                selectedZoom = selectedZoom,
                onZoomSelected = { selectedZoom = it }
            )
        }
    }
}


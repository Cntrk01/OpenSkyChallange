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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDropDownMenu(
    modifier: Modifier = Modifier,
    countries: List<String> = emptyList(),
    selectedCountry: String = "Turkey",
    onCountrySelected: (String) -> Unit = {},
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
            value = selectedCountry,
            onValueChange = {},
            readOnly = true,
            label = { Text("Origin Country") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } // Burası menü dışındaki bir alana tıklandığında kapatacak
        ) {
            countries.forEach { country ->
                DropdownMenuItem(
                    text = { Text(country) },
                    onClick = {
                        onCountrySelected(country)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CountryDropDownMenuPreview() {
    var selectedCountry by remember { mutableStateOf("Turkey") }

    val countries = listOf(
        "Turkey",
        "United Kingdom",
        "Germany",
        "France",
        "Netherlands"
    )

    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            CountryDropDownMenu(
                countries = countries,
                selectedCountry = selectedCountry,
                onCountrySelected = { selectedCountry = it }
            )
        }
    }
}

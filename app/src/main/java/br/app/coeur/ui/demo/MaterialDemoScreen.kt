package br.app.coeur.ui.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Slider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialDemoScreen(onLogout: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Material Components") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { SectionTitle("Botões") }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {}) { Text("Filled") }
                    ElevatedButton(onClick = {}) { Text("Elevated") }
                    FilledTonalButton(onClick = {}) { Text("Tonal") }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = {}) { Text("Outlined") }
                    TextButton(onClick = {}) { Text("Text") }
                }
            }

            item { SectionTitle("Seleção") }
            item {
                var checked by remember { mutableStateOf(true) }
                var switchOn by remember { mutableStateOf(false) }
                var selectedRadio by remember { mutableStateOf(0) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = checked, onCheckedChange = { checked = it })
                    Text("Checkbox")
                    Switch(checked = switchOn, onCheckedChange = { switchOn = it })
                    Text("Switch")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selectedRadio == 0, onClick = { selectedRadio = 0 })
                    Text("Opção 1")
                    RadioButton(selected = selectedRadio == 1, onClick = { selectedRadio = 1 })
                    Text("Opção 2")
                }
            }

            item { SectionTitle("Chips") }
            item {
                var filterSelected by remember { mutableStateOf(false) }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = {}, label = { Text("Assist") })
                    FilterChip(
                        selected = filterSelected,
                        onClick = { filterSelected = !filterSelected },
                        label = { Text("Filter") }
                    )
                }
            }

            item { SectionTitle("Entrada de texto") }
            item {
                var text by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Exemplo") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { SectionTitle("Progresso") }
            item {
                var progress by remember { mutableFloatStateOf(0.6f) }
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )
                Slider(value = progress, onValueChange = { progress = it })
            }

            item { SectionTitle("Cards e listas") }
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = { Text("Item de lista") },
                        supportingContent = { Text("Descrição de suporte") },
                        trailingContent = {
                            BadgedBox(badge = { Badge { Text("3") } }) {
                                Icon(Icons.Filled.Favorite, contentDescription = "Favorito")
                            }
                        }
                    )
                }
            }

            item { HorizontalDivider() }
            item {
                TextButton(onClick = onLogout) { Text("Sair") }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium)
}

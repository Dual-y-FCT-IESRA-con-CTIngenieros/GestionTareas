package com.es.appmovil.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class RecordUi(val id: String, val title: String, val subtitle: String)

@Composable
fun RecentRecordsList(records: List<RecordUi>, onRecordClick: (RecordUi) -> Unit = {}) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(records) { record ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onRecordClick(record) }) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(record.title, style = MaterialTheme.typography.titleMedium)
                    Text(record.subtitle, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

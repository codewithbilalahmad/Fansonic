package com.muhammad.fansonic.folder_picker

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun FolderPickerScreen() {
    val context = LocalContext.current
    val contentResolver = context.contentResolver
    var selectedFolderPath by remember { mutableStateOf<Uri?>(null) }
    val folderPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) {uri ->
        if(uri != null){
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            if(checkUriPersisted(contentResolver, uri)){
                contentResolver.releasePersistableUriPermission(uri, flags)
            }
            contentResolver.takePersistableUriPermission(uri, flags)
            selectedFolderPath = uri
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Button(onClick = {
            folderPickerLauncher.launch(Uri.EMPTY)
        }) {
            Text(text = "Pick Folder")
        }
        if(selectedFolderPath != null){
            Text("Selected Uri path : ${selectedFolderPath.toString()}")
        }
    }
}

fun checkUriPersisted(contentResolver : ContentResolver, uri : Uri) : Boolean{
    return contentResolver.persistedUriPermissions.any{perm -> perm.uri == uri}
}
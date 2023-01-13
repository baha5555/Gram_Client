@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.gramclient.presentation.screens.profile

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.gramclient.R
import com.example.gramclient.presentation.components.CustomButton
import com.example.gramclient.presentation.components.CustomSwitch
import com.example.gramclient.presentation.components.CustomTopBar
import com.example.gramclient.presentation.screens.authorization.LoadingIndicator
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.FontSilver
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.lang.Exception


@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@ExperimentalCoilApi
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val stateGetProfileInfo by viewModel.stateGetProfileInfo
    val profileInfo = stateGetProfileInfo.response
    val getProfileFirstName = stateGetProfileInfo.response?.first_name
    val getProfileLastName = stateGetProfileInfo.response?.last_name
    val getProfileEmail = stateGetProfileInfo.response?.email
    val profileFirstName = remember { mutableStateOf(getProfileFirstName) }
    val profileEmail = remember { mutableStateOf(getProfileEmail) }
    val profileLastName = remember { mutableStateOf(getProfileLastName) }
    val profileImage = viewModel.stateGetProfileInfo.value.response?.avatar_url
    var selectImage by mutableStateOf<Uri?>(null)
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            selectImage = it
        }
    val state = rememberSwipeRefreshState(stateGetProfileInfo.isLoading)

    LaunchedEffect(key1 = true) {
        viewModel.getProfileInfo()
    }

    Scaffold(
        topBar = { CustomTopBar(title = "Профиль", navController = navController, actionNum = 3) }
    ) {
        when {
            stateGetProfileInfo.isLoading -> {
                LoadingIndicator(isLoading = true)
            }
        }
        profileInfo?.let { taxiOrdersNonNull ->
            SwipeRefresh(
                modifier = Modifier.fillMaxSize(),
                state = state,
                onRefresh = {
                    viewModel.getProfileInfo()
                    profileFirstName.value = getProfileFirstName
                    profileLastName.value = getProfileLastName
                    profileEmail.value = getProfileEmail
                }
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(BackgroundColor)
                        .fillMaxSize()
                ) {
                    item() {
                        Spacer(modifier = Modifier.height(75.dp))
                        if (selectImage != null) {
                            Image(
                                painter = rememberImagePainter(selectImage),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        launcher.launch("image/*")
                                    },
//                    imageVector = ImageVector.vectorResource(id = R.drawable.camera_plus),
                                contentDescription = "",
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = profileImage ?: R.drawable.avatar
                                ),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        launcher.launch("image/*")
                                    },
//                    imageVector = ImageVector.vectorResource(id = R.drawable.camera_plus),
                                contentDescription = "",
                            )
                        }
                        Spacer(modifier = Modifier.height(75.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 27.dp, end = 21.dp)
                        ) {

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                value = (profileFirstName.value?:getProfileFirstName?:""),
                                onValueChange = { profileFirstName.value = it },
                                label = { Text(text = "Имя*") },
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = BackgroundColor,
                                    unfocusedLabelColor = FontSilver,
                                    focusedLabelColor = FontSilver,
                                    unfocusedIndicatorColor = FontSilver,
                                    focusedIndicatorColor = FontSilver,
                                    cursorColor = FontSilver,
                                )
                            )
                            Spacer(modifier = Modifier.height(35.dp))
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    value = (profileLastName.value ?: getProfileLastName ?: ""),
                                    onValueChange = { profileLastName.value = it },
                                    label = { Text(text = "Фамилия*") },
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = BackgroundColor,
                                        unfocusedLabelColor = FontSilver,
                                        focusedLabelColor = FontSilver,
                                        unfocusedIndicatorColor = FontSilver,
                                        focusedIndicatorColor = FontSilver,
                                        cursorColor = FontSilver,
                                    )
                                )
                            Spacer(modifier = Modifier.height(35.dp))
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    value = (profileEmail.value ?: getProfileEmail ?: ""),
                                    onValueChange = { profileEmail.value = it },
                                    label = { Text(text = "Email*") },
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = BackgroundColor,
                                        unfocusedLabelColor = FontSilver,
                                        focusedLabelColor = FontSilver,
                                        unfocusedIndicatorColor = FontSilver,
                                        focusedIndicatorColor = FontSilver,
                                        cursorColor = FontSilver,
                                    )
                                )

                            Spacer(modifier = Modifier.height(35.dp))
                            TextField(
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                modifier = Modifier
                                    .fillMaxWidth(),
                                value = viewModel.stateGetProfileInfo.value.response?.phone.toString(),
                                onValueChange = { },
                                label = { Text(text = "Телефон") },
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = BackgroundColor,
                                    unfocusedLabelColor = FontSilver,
                                    focusedLabelColor = FontSilver,
                                    unfocusedIndicatorColor = FontSilver,
                                    focusedIndicatorColor = FontSilver,
                                    cursorColor = FontSilver,
                                ),
                                textStyle = TextStyle(color = Color.Gray)
                            )

                            Spacer(modifier = Modifier.height(49.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CustomButton(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.Black)
                                        .width(363.dp)
                                        .height(55.dp)
                                        .padding(top = 0.dp),
                                    text = "Cохранить",
                                    textSize = 18,
                                    textBold = true,
                                    onClick = {
                                        scope.launch {
                                            lateinit var photos: MutableState<File?>
                                            selectImage?.let {
                                                try {
                                                    val photo by  mutableStateOf(
                                                        File(
                                                            getRealPathFromURI(
                                                                context,
                                                                selectImage!!
                                                            )
                                                        )
                                                    )
                                                    photos = photo as MutableState<File?>

                                                    viewModel.sendProfile(
                                                        (profileFirstName.value
                                                            ?: getProfileFirstName
                                                            ?: "").toRequestBody(),
                                                        (profileLastName.value ?: getProfileLastName
                                                        ?: "").toRequestBody(),
                                                        profileEmail.value ?: getProfileEmail ?: "",
                                                        photos,
                                                        context
                                                    )
                                                } catch (e: HttpException) {
                                                    Log.e(
                                                        "HELO",
                                                        e.localizedMessage
                                                            ?: "Произошла непредвиденная ошибка"
                                                    )
                                                } catch (e: IOException) {
                                                    Log.e(
                                                        "HELO",
                                                        "NOT Network"
                                                    )
                                                }catch (e:Exception) {
//                                    navController.popBackStack()
                                                    if (selectImage == null)
                                                        Toast.makeText(
                                                            context,
                                                            "Выберите аватар",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    else {
                                                        Log.e("HELLO","$e")
                                                        Toast.makeText(
                                                            context,
                                                            "Произошла ошибка! Повторите еще раз $e ",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                            }

                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(49.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween

                            ) {
                                Text(
                                    text = "Получение рассылки",
                                    color = Color.Black,
                                    fontSize = 15.sp
                                )
                                Box(modifier = Modifier.padding(end = 5.dp)) {
                                    val switchON = remember {
                                        mutableStateOf(false) // Initially the switch is ON
                                    }
                                    CustomSwitch(switchON) {}
                                }

                            }
                            Spacer(modifier = Modifier.height(49.dp))

                        }
                    }
                }
            }
        }
    }
}
fun getRealPathFromURI(context: Context, uri: Uri): String? {
    when {
        // DocumentProvider
        DocumentsContract.isDocumentUri(context, uri) -> {
            when {
                // ExternalStorageProvider
                isExternalStorageDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    // This is for checking Main Memory
                    return if ("primary".equals(type, ignoreCase = true)) {
                        if (split.size > 1) {
                            Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                        } else {
                            Environment.getExternalStorageDirectory().toString() + "/"
                        }
                        // This is for checking SD Card
                    } else {
                        "storage" + "/" + docId.replace(":", "/")
                    }
                }
                isDownloadsDocument(uri) -> {
                    val fileName = getFilePath(context, uri)
                    if (fileName != null) {
                        return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName
                    }
                    var id = DocumentsContract.getDocumentId(uri)
                    if (id.startsWith("raw:")) {
                        id = id.replaceFirst("raw:".toRegex(), "")
                        val file = File(id)
                        if (file.exists()) return id
                    }
                    val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                    return getDataColumn(context, contentUri, null, null)
                }
                isMediaDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    when (type) {
                        "image" -> {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        "video" -> {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        "audio" -> {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            }
        }
        "content".equals(uri.scheme, ignoreCase = true) -> {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
        }
        "file".equals(uri.scheme, ignoreCase = true) -> {
            return uri.path
        }
    }
    return null
}

fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                  selectionArgs: Array<String>?): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
        column
    )
    try {
        if (uri == null) return null
        cursor = context.contentResolver.query(uri, projection, selection, selectionArgs,
            null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
}


fun getFilePath(context: Context, uri: Uri?): String? {
    var cursor: Cursor? = null
    val projection = arrayOf(
        MediaStore.MediaColumns.DISPLAY_NAME
    )
    try {
        if (uri == null) return null
        cursor = context.contentResolver.query(uri, projection, null, null,
            null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}
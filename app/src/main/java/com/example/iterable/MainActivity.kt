package com.example.iterable


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.iterable.ui.theme.IterableTheme
import com.iterable.iterableapi.IterableApi
import org.json.JSONObject
import android.webkit.WebView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.fillMaxWidth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set Email for testing
        val testUserEmail = "removed for privacy"
        IterableApi.getInstance().setEmail(testUserEmail)


        setContent {
            IterableTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun MainContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 300.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Profile and events buttons
        UpdateProfileButton(
            FirstName = "removed for privacy"
        )
        CustomEventButton(
            EventName = "mobileSATestEvent",
            FirstName = "removed for privacy"
        )

        // Testing Buttons to view and show messages
        GetAllInAppMessagesButton()
        ShowMessageButton()

        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Composable
fun UpdateProfileButton(FirstName: String) {
    Button(onClick = {
        updateIterableUserProfile(FirstName)
    }) {
        Text("Update Profile")
    }
}

@Composable
fun CustomEventButton(EventName: String, FirstName: String? = null) {
    Button(onClick = {
        Log.d("MainActivityIterable", "Event Name: $EventName")

        if (EventName === "mobileSATestEvent") {
            MobileSATestEvent(FirstName)
            Log.d("MainActivityIterable", "Tracking event '$EventName' with custom data")
        } else {
            IterableApi.getInstance().track(EventName)
            Log.d("MainActivityIterable", "Tracking event '$EventName'")
        }
    }) {
        Text("Custom Event")
    }
}


fun updateIterableUserProfile(FirstName: String) {
    val dataFields = JSONObject()
    try {
        dataFields.put("firstName", FirstName)
        dataFields.put("isRegisteredUser", true)
        dataFields.put("SA_User_Test_Key", "completed")

        IterableApi.getInstance().updateUser(dataFields)
        Log.d("MainActivityIterable", "Update sent: $dataFields")

    } catch (e: org.json.JSONException) {
        Log.e("MainActivityIterable", "Error with user update", e)
    }
}

fun MobileSATestEvent(FirstName: String?) {
    val eventName = "mobileSATestEvent"
    val dataFields = JSONObject()

    try {
        dataFields.put("platform", "Android")
        dataFields.put("isTestEvent", true)
        dataFields.put("url", "https://iterable.com/sa-test/$FirstName")
        dataFields.put("secret_code_key", "Code_123")

        IterableApi.getInstance().track(eventName, dataFields)
        // Log.d("MainActivityIterable", "Custom Event '$eventName' data: $dataFields")

    } catch (e: org.json.JSONException) {
        Log.e("MainActivityIterable", "Error with tracking event '$eventName'", e)
    }
}

@Composable
fun GetAllInAppMessagesButton() {
    Button(onClick = {
        try {
            // Get all messages
            val allMessages = IterableApi.getInstance().getInAppManager().getMessages()
            val readMessages = allMessages.filter { it.isRead }
            val unreadMessages = allMessages.filter { !it.isRead }


            Log.d("MainActivityIterable", "Total messages: ${allMessages.size}")
            Log.d("MainActivityIterable", "Read messages: ${readMessages.size}")
            Log.d("MainActivityIterable", "Unread messages: ${unreadMessages.size}")



            readMessages.forEachIndexed { index, message ->
                Log.d("MainActivityIterable", "Read Message $index:")
                Log.d("MainActivityIterable", "  ID: ${message.messageId}")
                Log.d("MainActivityIterable", "  Campaign ID: ${message.campaignId}")
                Log.d("MainActivityIterable", "  Preview: ${message.content.toString().take(100)}...")
            }


            unreadMessages.forEachIndexed { index, message ->
                Log.d("MainActivityIterable", "Unread Message $index:")
                Log.d("MainActivityIterable", "  ID: ${message.messageId}")
                Log.d("MainActivityIterable", "  Campaign ID: ${message.campaignId}")
                Log.d("MainActivityIterable", "  Preview: ${message.content.toString().take(100)}...")
            }
        } catch (e: Exception) {
            Log.e("MainActivityIterable", "Error getting messages: ${e.message}", e)
        }
    }) {
        Text("Get All Messages")
    }
}

@Composable
fun ShowMessageButton() {
    var showWebView by remember { mutableStateOf(false) }
    var htmlContent by remember { mutableStateOf("") }

    Button(onClick = {
        val messages = IterableApi.getInstance().inAppManager.messages
        if (messages.isNotEmpty()) {
            val message = messages[0]
            Log.i("MainActivityIterable", "HTML LOGGED: ${message.content?.html ?: "<p>No HTML found</p>"}")
            htmlContent = message.content?.html ?: "<p>No HTML found</p>"
            showWebView = true
        } else {
            Log.d("MainActivityIterable", "No messages")
        }
    }) {
        Text("Show Message")
    }

    if (showWebView) {
        AlertDialog(
            onDismissRequest = { showWebView = false },
            title = { Text("Rendered Message") },
            text = {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )
            },
            confirmButton = {
                Button(onClick = { showWebView = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    IterableTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MainContent(
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}



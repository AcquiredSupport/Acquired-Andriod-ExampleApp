package com.acquired.exampleapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.acquired.exampleapp.data.ExampleAppViewModel
import com.acquired.exampleapp.data.ToastEvent
import com.acquired.exampleapp.data.WebViewEvent
import com.acquired.exampleapp.ui.theme.ACQPaymentGatewayTheme
import com.acquired.exampleapp.ui.theme.Purple200
import com.acquired.sdk.core.paymentmethods.CardMethod
import com.acquired.sdk.core.paymentmethods.GooglePayMethod
import com.acquired.sdk.core.paymentmethods.PaymentMethod

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ACQPaymentGatewayTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainContent()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val model: ExampleAppViewModel by viewModels()
        model.handleOnActivityResult(requestCode, resultCode, data)
    }
}

@Composable
fun MainContent(model: ExampleAppViewModel = viewModel()) {
    val navController = rememberNavController()
    val webView = WebView(LocalContext.current)
    NavHost(
        navController = navController,
        startDestination = "paymentScreen"
    ) {
        composable("paymentScreen") {
            //resets the web view to a blank page
            webView.loadUrl("about:blank")
            PaymentScreen(navController, model, webView)
        }
        composable("webpageContent") {
            WebPageContent(webView)
        }
        composable("billingInfo") {
            BillingInfo(navController = navController, mainViewModel = model)
        }
    }
}

@Composable
fun PaymentScreen(navController: NavController, model: ExampleAppViewModel, webView: WebView) {

    val list: List<PaymentMethod> by model.paymentMethodsLiveData.observeAsState(listOf())
    val context = LocalContext.current

    val showErrorDialog by model.showErrorDialog.observeAsState(false)
    val currency by model.currency.observeAsState("")

    model.eventLiveData.observeForever {
        it?.let {
            when (it) {
                is ToastEvent -> {
                    it.get()?.let { message ->
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                }
                is WebViewEvent -> {
                    it.get()?.let { active ->
                        if (active) {
                            navController.navigate("webpageContent")
                        } else {
                            navController.navigate("paymentScreen")
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Row {
                Text(
                    text = "Amount",
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "12.34 $currency",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Divider()
        LazyColumn {
            items(items = list) {
                PaymentMethodItem(
                    navController = navController,
                    paymentMethod = it,
                    model = model,
                    webView = webView
                )
            }
        }
        Row {
            Button(
                onClick = { model.retrieveData() }
            ) {
                Text(text = "Retrieve payment data")
            }
        }
    }

    val dialogTitle by model.dialogTitle.observeAsState()
    val dialogMessage by model.dialogMessage.observeAsState()

    if (showErrorDialog) {
        AlertDialog(onDismissRequest = {
            model.showErrorDialog.value = false
        },
            title = {
                Text(text = dialogTitle ?: "")
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(dialogMessage ?: "")
                }
            },
            confirmButton = {},
            dismissButton = {
                Button(
                    onClick = {
                        model.showErrorDialog.value = false
                    }) {
                    Text("OK")
                }
            })
    }
}

@Composable
fun PaymentMethodItem(
    navController: NavController,
    paymentMethod: PaymentMethod,
    model: ExampleAppViewModel,
    webView: WebView
) {
    val context = LocalContext.current as Activity
    val isGooglePayEnabled by model.isGooglePayReadyLiveData.observeAsState(true)

    if (paymentMethod.isActive) {
        //Setup the payment method with the needed ui requirements.
        when (paymentMethod) {
            is CardMethod -> {
                paymentMethod.webView = webView
            }
            is GooglePayMethod -> {
                paymentMethod.activity = context
                model.isGooglePayReady(paymentMethod)
            }
        }

        val outlineButtonColor = ButtonDefaults.outlinedButtonColors(
            contentColor = Purple200
        )
        Row {
            Text(
                text = paymentMethod.nameKey,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(2f)
            )

            if (paymentMethod is GooglePayMethod) {
                OutlinedButton(
                    colors = outlineButtonColor,
                    onClick = {
                        model.pay(paymentMethod)
                    },
                    enabled = isGooglePayEnabled,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(color = Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.googlepay_button_content),
                        contentDescription = ""
                    )
                }
            } else {
                OutlinedButton(
                    colors = outlineButtonColor,
                    onClick = {
                        model.cardMethod = paymentMethod as CardMethod
                        navController.navigate("billingInfo")
                    },
                    enabled = isGooglePayEnabled,
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text(text = "Use", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun WebPageContent(webView: WebView) {
    AndroidView(
        factory = {
            webView.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }
    )
}
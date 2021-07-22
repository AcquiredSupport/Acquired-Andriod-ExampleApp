package com.acquired.exampleapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.acquired.exampleapp.data.ExampleAppViewModel
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

    val list: List<PaymentMethod> by model.paymentMethodsLiveData.observeAsState(listOf())
    val context = LocalContext.current

    model.toastMessageEventLiveData.observeForever {
        it?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
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
                    text = "12.34 $",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Divider()

        LazyColumn {
            items(items = list) {
                PaymentMethodItem(paymentMethod = it)
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
}

@Composable
fun PaymentMethodItem(paymentMethod: PaymentMethod, model: ExampleAppViewModel = viewModel()) {
    val context = LocalContext.current as Activity
    val isGooglePayEnabled by model.isGooglePayReadyLiveData.observeAsState(true)

    if (paymentMethod.isActive) {
        //Setup the payment method with the needed ui requirements.
        when (paymentMethod) {
            is CardMethod -> {
                paymentMethod.webView = WebView(context)
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
            Text(text = paymentMethod.nameKey,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(2f))

            if (paymentMethod is GooglePayMethod){

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
                    Image(painter = painterResource(id = R.drawable.googlepay_button_content), contentDescription = "")

                }
            } else {
                OutlinedButton(
                    colors = outlineButtonColor,
                    onClick = {
                        model.pay(paymentMethod)
                    },
                    enabled = isGooglePayEnabled,
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text(text = "pay", modifier = Modifier.padding(8.dp))
                }
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ACQPaymentGatewayTheme {
        MainContent()
    }
}
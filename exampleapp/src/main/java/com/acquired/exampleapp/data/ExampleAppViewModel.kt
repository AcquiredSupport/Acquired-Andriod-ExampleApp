package com.acquired.exampleapp.data

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acquired.paymentgateway.PaymentGateway
import com.acquired.sdk.core.paymentmethods.CardMethod
import com.acquired.sdk.core.paymentmethods.GooglePayMethod
import com.acquired.sdk.core.paymentmethods.PaymentMethod
import com.acquired.sdk.core.publicapi.Configuration
import com.acquired.sdk.core.publicapi.SubscriptionType
import com.acquired.sdk.core.publicapi.Transaction
import com.acquired.sdk.core.publicapi.TransactionType
import com.acquired.sdk.core.publicapi.failure.Failure
import com.acquired.sdk.core.publicapi.failure.Failure.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*

class ExampleAppViewModel : ViewModel() {

    val paymentMethodsLiveData: LiveData<List<PaymentMethod>>
        get() = paymentMethods
    private val paymentMethods = MutableLiveData<List<PaymentMethod>>()

    val eventLiveData: LiveData<Event<Any>>
        get() = event
    private val event = MutableLiveData<Event<Any>>()

    val isGooglePayReadyLiveData: LiveData<Boolean>
        get() = isGooglePayReady
    private val isGooglePayReady = MutableLiveData(false)

    var cardMethod: CardMethod? = null

    val dialogTitle = MutableLiveData<String>()
    val dialogMessage = MutableLiveData<String>()
    val showErrorDialog = MutableLiveData<Boolean>()
    val currency = MutableLiveData<String>()

    private val configuration = Configuration(
        companyId = "459",
        companyPass = "re3vKdCG",
        companyHash = "cXaFMLbH",
        companyMidId = "1687",
        baseUrlAddress = URL("https://qaapi.acquired.com/"),
        baseHppUrlAddress = URL("https://qahpp.acquired.com"),
        requestRetryAttempts = 3
    )

    private val paymentGateway = PaymentGateway(configuration)

    // Retrieves the payment data with a coroutine
    fun retrieveData() {
        viewModelScope.launch {
            paymentGateway.getPayment().fold(
                onSuccess = {
                    currency.value = it.currency.iso3CurrencyCode
                    paymentMethods.value = it.availablePaymentMethods
                    cancel()
                },
                onFailure = {
                    handleThrowable(it)
                    cancel()
                }
            )
        }
    }

    fun pay(paymentMethod: PaymentMethod) {
        val merchantOrderID = Date().time.toString()
        val transaction = Transaction(
            transactionType = TransactionType.AUTH_CAPTURE,
            subscriptionType = SubscriptionType.INIT,
            merchantOrderId = merchantOrderID,
            merchantCustomerId = "5678",
            merchantContactUrl = URL("https://www.acquired.com"),
            merchantCustom1 = "custom1",
            merchantCustom2 = "custom2",
            merchantCustom3 = "custom3"
        )

        viewModelScope.launch {
            if (paymentMethod is CardMethod) {
                paymentGateway.pay(paymentMethod, transaction, 1234).fold(
                    onSuccess = { orderData ->
                        event.value = WebViewEvent(false)
                        val orderDataFormatted = orderData.toString().replace("(", "(\n").replace(", ", ",\n")
                        setDialogInfo("Success", orderDataFormatted)
                        showErrorDialog.value = true
                    },
                    onFailure = {
                        event.value = WebViewEvent(false)
                        handleThrowable(it)
                    }
                )
            } else {
                paymentGateway.pay(paymentMethod, transaction, 1234).fold(
                    onSuccess = { orderData ->
                        val orderDataFormatted = orderData.toString().replace("(", "(\n").replace(", ", ",\n")
                        setDialogInfo("Success", orderDataFormatted)
                        showErrorDialog.value = true
                    },
                    onFailure = {
                        handleThrowable(it)
                    }
                )
            }
        }
    }

    private fun handleThrowable(throwable: Throwable) {
        when (throwable) {
            is Failure -> { handleFailure(throwable) }
            else -> {
                handleUnknownErrors(throwable)
            }
        }
    }

    private fun handleUnknownErrors(error: Throwable) {
        event.value = ToastEvent("Uncaught error ${error.message}")
    }

    private fun handleFailure(failure: Failure) {
        when(failure) {
            is CanceledByUserFailure -> { setDialogInfo("Info", "The payment was canceled by the user") }
            is DataFailure -> { setDialogInfo("Data Error", failure.message) }
            is PaymentFailure -> { setDialogInfo("Payment Error", "${failure.errorMessage}, with status code ${failure.errorCode}") }
            is NetworkFailure ->  { setDialogInfo("Network Error", failure.errorMessage) }
            is ResponseFailure -> { handlePaymentAuthorizationFailure(failure) }
            is UnknownFailure -> { setDialogInfo("Unknown Error", failure.message) }
            is ApiFailure -> { setDialogInfo("Api Error", "Code: ${failure.errorCode}\nMessage: ${failure.errorMessage}") }
        }
        showErrorDialog.value = true
    }

    private fun handlePaymentAuthorizationFailure(failure: ResponseFailure) {
        when (failure) {
            is ResponseFailure.Blocked -> { setDialogInfo("Blocked", failure.toErrorMessage()) }
            is ResponseFailure.Declined -> { setDialogInfo("Declined", failure.toErrorMessage()) }
            is ResponseFailure.Error -> { setDialogInfo("Error", failure.toErrorMessage()) }
            is ResponseFailure.InvalidCode -> { setDialogInfo("Invalid Code", failure.toErrorMessage()) }
            is ResponseFailure.Pending -> { setDialogInfo("Pending", failure.toErrorMessage()) }
            is ResponseFailure.Quarantined -> { setDialogInfo("Quarantined", failure.toErrorMessage()) }
            is ResponseFailure.TdsFailure -> { setDialogInfo("Tds Failure", failure.toErrorMessage()) }
            is ResponseFailure.Unknown -> { setDialogInfo("Unknown Error", failure.toErrorMessage()) }
        }
    }

    private fun setDialogInfo(title: String, message: String){
        dialogTitle.value = title
        dialogMessage.value = message
    }

    private fun ResponseFailure.toErrorMessage(): String{
        return "Code: $responseCode Message: $responseMessage"
    }

    fun handleOnActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        when (requestCode) {
            5678 -> {
            }// handle the apps own onActivityResults
            else -> {
                paymentGateway.handleOnActivityResult(resultCode, data)
            }
        }
    }

    fun isGooglePayReady(paymentMethod: GooglePayMethod) {
        viewModelScope.launch {
            paymentGateway.isGooglePayReady(paymentMethod).fold(
                onSuccess = { isGooglePayReady.value = it },
                onFailure = {
                    isGooglePayReady.value = false
                    handleThrowable(it)
                }
            )
        }
    }
}
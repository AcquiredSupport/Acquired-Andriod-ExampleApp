package com.acquired.exampleapp.data

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acquired.paymentgateway.PaymentGateway
import com.acquired.sdk.core.paymentmethods.GooglePayMethod
import com.acquired.sdk.core.paymentmethods.PaymentMethod
import com.acquired.sdk.core.publicapi.Configuration
import com.acquired.sdk.core.publicapi.SubscriptionType
import com.acquired.sdk.core.publicapi.Transaction
import com.acquired.sdk.core.publicapi.TransactionType
import com.acquired.sdk.core.publicapi.failure.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*

class ExampleAppViewModel : ViewModel() {

    val paymentMethodsLiveData: LiveData<List<PaymentMethod>>
        get() = paymentMethods
    private val paymentMethods = MutableLiveData<List<PaymentMethod>>()

    val toastMessageEventLiveData: LiveData<String>
        get() = toastMessageEvent
    private val toastMessageEvent = MutableLiveData<String>()

    val isGooglePayReadyLiveData: LiveData<Boolean>
        get() = isGooglePayReady
    private val isGooglePayReady = MutableLiveData(false)

    private val configuration = Configuration(
        companyId = "459",
        companyPass = "re3vKdCG",
        companyHash = "cXaFMLbH",
        companyMidId = "1687",
        baseUrlAddress = URL("https://qaapi2.acquired.com/"),
        requestRetryAttempts = 3
    )

    private val paymentGateway = PaymentGateway(configuration)

    // Retrieves the payment data with a coroutine
    fun retrieveData() {

        viewModelScope.launch {
            paymentGateway.getPayment().fold(
                onSuccess = {
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
            if (paymentMethod !is GooglePayMethod) {
                toastMessageEvent.value = "${paymentMethod.nameKey} payment method not yet supported"
            } else {
                paymentGateway.pay(paymentMethod, transaction, 1234).fold(
                    onSuccess = { orderData ->
                        toastMessageEvent.value = orderData.transactionDetails?.responseMessage ?: ""
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
        toastMessageEvent.value = "Uncaught error ${error.message}"
    }

    private fun handleFailure(failure: Failure) {
        when(failure) {
            is CanceledByUserFailure -> { toastMessageEvent.value = "The payment was canceled by the user"}
            is DataFailure -> { toastMessageEvent.value = failure.message }
            is PaymentFailure -> {toastMessageEvent.value = "${failure.errorMessage}, with status code ${failure.errorCode}" }
            is NetworkFailure ->  { toastMessageEvent.value = failure.errorMessage}
            is PaymentAuthorizationFailure -> { handlePaymentAuthorizationFailure(failure) }
            is UnknownFailure -> { toastMessageEvent.value = failure.message}
        }
    }

    private fun handlePaymentAuthorizationFailure(failure: PaymentAuthorizationFailure) {
        toastMessageEvent.value = when (failure) {
            is PaymentAuthorizationFailure.Blocked -> { "Blocked: ${failure.toErrorMessage()}" }
            is PaymentAuthorizationFailure.Declined -> { "Declined: ${failure.toErrorMessage()}" }
            is PaymentAuthorizationFailure.Error -> { "Error: ${failure.toErrorMessage()}" }
            is PaymentAuthorizationFailure.InvalidCode -> { "InvalidCode: ${failure.toErrorMessage()}" }
            is PaymentAuthorizationFailure.Pending -> { "Pending: ${failure.toErrorMessage()}" }
            is PaymentAuthorizationFailure.Quarantined -> { "Quarantined: ${failure.toErrorMessage()}" }
            is PaymentAuthorizationFailure.TdsFailure -> { "TdsFailure: ${failure.toErrorMessage()}" }
            is PaymentAuthorizationFailure.Unknown -> { "Unknown: ${failure.toErrorMessage()}" }
        }
    }

    private fun PaymentAuthorizationFailure.toErrorMessage(): String{
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
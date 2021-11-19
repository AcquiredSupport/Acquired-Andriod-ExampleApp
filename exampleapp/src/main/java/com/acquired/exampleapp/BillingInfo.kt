package com.acquired.exampleapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.acquired.exampleapp.data.BillingViewModel
import com.acquired.exampleapp.data.ExampleAppViewModel


@Composable
fun BillingInfo(
    navController: NavController,
    billingViewModel: BillingViewModel = viewModel(),
    mainViewModel: ExampleAppViewModel
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Button(
            onClick = {
                mainViewModel.cardMethod?.let {  paymentMethod ->
                    paymentMethod.contact = billingViewModel.createContactModel()
                    mainViewModel.pay(paymentMethod)
                    navController.navigate("webpageContent")
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Pay")
        }
        val firstName by billingViewModel.givenName.observeAsState()
        val lastName by billingViewModel.familyName.observeAsState()
        val phoneNumber by billingViewModel.phoneNumber.observeAsState()
        val street by billingViewModel.street.observeAsState()
        val street2 by billingViewModel.street2.observeAsState()
        val city by billingViewModel.city.observeAsState()
        val subAdministrativeArea by billingViewModel.subAdministrativeArea.observeAsState()
        val administrativeArea by billingViewModel.administrativeArea.observeAsState()
        val postalCode by billingViewModel.postalCode.observeAsState()
        val country by billingViewModel.country.observeAsState()
        val isoCountryCode by billingViewModel.isoCountryCode.observeAsState()
        val emailAddress by billingViewModel.emailAddress.observeAsState()

        InputField(
            label = "First Name",
            data = firstName ?: "",
            onValueChanged = { billingViewModel.givenName.postValue(it) })
        InputField(
            label = "Last Name",
            data = lastName ?: "",
            onValueChanged = { billingViewModel.familyName.postValue(it) })
        InputField(
            label = "Phone Number",
            data = phoneNumber ?: "",
            onValueChanged = { billingViewModel.phoneNumber.postValue(it) })
        InputField(
            label = "Street",
            data = street ?: "",
            onValueChanged = { billingViewModel.street.postValue(it) })
        InputField(
            label = "Street 2",
            data = street2 ?: "",
            onValueChanged = { billingViewModel.street2.postValue(it) })
        InputField(
            label = "City",
            data = city ?: "",
            onValueChanged = { billingViewModel.city.postValue(it) })
        InputField(
            label = "Sub Adm. Area",
            data = subAdministrativeArea ?: "",
            onValueChanged = { billingViewModel.subAdministrativeArea.postValue(it) })
        InputField(
            label = "Adm. State",
            data = administrativeArea ?: "",
            onValueChanged = { billingViewModel.administrativeArea.postValue(it) })
        InputField(
            label = "Postal Code",
            data = postalCode ?: "",
            onValueChanged = { billingViewModel.postalCode.postValue(it) })
        InputField(
            label = "Country",
            data = country ?: "",
            onValueChanged = { billingViewModel.country.postValue(it) })
        InputField(
            label = "ISO Country Code",
            data = isoCountryCode ?: "",
            onValueChanged = { billingViewModel.isoCountryCode.postValue(it) })
        InputField(
            label = "Email",
            data = emailAddress ?: "",
            onValueChanged = { billingViewModel.emailAddress.postValue(it) })
    }
}

@Composable
fun InputField(label: String, data: String, onValueChanged: (String) -> Unit = {}) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = label)
        TextField(value = data, onValueChange = onValueChanged)
    }
}
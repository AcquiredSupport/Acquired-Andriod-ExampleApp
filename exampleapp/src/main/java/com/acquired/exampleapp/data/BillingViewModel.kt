package com.acquired.exampleapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acquired.sdk.core.models.Contact
import com.acquired.sdk.core.models.Name
import com.acquired.sdk.core.models.PhoneNumber
import com.acquired.sdk.core.models.PostalAddress

class BillingViewModel : ViewModel() {
    val phoneNumber = MutableLiveData("12345678")
    val emailAddress = MutableLiveData("test@test.com")
    val givenName = MutableLiveData("firstName")
    val familyName = MutableLiveData("surName")
    val street = MutableLiveData("street")
    val street2 = MutableLiveData("subLocality")
    val city = MutableLiveData("city")
    val subAdministrativeArea = MutableLiveData("subAdministrativeArea")
    val administrativeArea = MutableLiveData("state")
    val postalCode = MutableLiveData("postalCode")
    val country = MutableLiveData("country")
    val isoCountryCode = MutableLiveData("dk")

    fun createContactModel() : Contact {
        val postalAddress = PostalAddress(
            street = street.value ?: "",
            street2 = street2.value ?: "",
            city = city.value ?: "",
            subAdministrativeArea = subAdministrativeArea.value ?: "",
            administrativeArea = administrativeArea.value ?: "",
            postalCode = postalCode.value ?: "",
            country = country.value ?: "",
            isoCountryCode = isoCountryCode.value ?: ""
        )
        val phoneNumber = PhoneNumber.createPhoneNumber(phoneNumber.value ?: "")


        return  Contact(
            name = Name(
                givenName = givenName.value ?: "",
                familyName = familyName.value ?: ""
            ),
            postalAddress = postalAddress,
            phoneNumber = phoneNumber,
            emailAddress = emailAddress.value ?: ""
        )
    }
}
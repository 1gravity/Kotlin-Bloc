package com.onegravity.knot.sample.sms

interface ISmsRepository {
    fun checkSms(sms: String): Boolean
}
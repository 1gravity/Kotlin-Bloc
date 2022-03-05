package com.genaku.reduce.sms

interface ISmsRepository {
    fun checkSms(sms: String): Boolean
}
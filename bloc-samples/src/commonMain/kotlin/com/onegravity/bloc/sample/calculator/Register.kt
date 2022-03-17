/*
 * Copyright 2021 Mikołaj Leszczyński & Appmattus Limited
 * Copyright 2020 Babylon Partners Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * File modified by Mikołaj Leszczyński & Appmattus Limited
 * See: https://github.com/orbit-mvi/orbit-mvi/compare/c5b8b3f2b83b5972ba2ad98f73f75086a89653d3...main
 */

package com.onegravity.bloc.sample.calculator

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

class Register(private val value: String = "") {
    private val asBigDecimal: BigDecimal
        get() = if (value.isEmpty()) BigDecimal.ZERO else value.toBigDecimal()

    override fun toString() = when {
        value.isEmpty() -> "0"
        value == "-" -> "-0"
        else -> value
    }

    fun isEmpty() = value.isEmpty()

    fun appendDigit(digit: Int) =
        Register(value + digit)

    fun appendPeriod() =
        if (!value.contains('.')) Register("$value.") else this

    fun plusMinus() =
        Register(if (value.startsWith('-')) value.substring(1) else "-$value")

    operator fun plus(register: Register) =
        Register((this.asBigDecimal + register.asBigDecimal).toPlainString())

    operator fun minus(register: Register) =
        Register((this.asBigDecimal - register.asBigDecimal).toPlainString())

    operator fun div(register: Register) =
        Register(this.asBigDecimal
            .divide(register.asBigDecimal, DecimalMode.DEFAULT)
            .roundToDigitPosition(SCALE, RoundingMode.ROUND_HALF_TO_EVEN)
            .toPlainString()
        )

    operator fun times(register: Register) =
        Register((this.asBigDecimal * register.asBigDecimal).toPlainString())

    companion object {
        private const val SCALE = 7L
    }
}
package xyz.block.moneyaddress.typed.x

import xyz.block.moneyaddress.typed.Currency
import xyz.block.moneyaddress.typed.Currency.Companion.asCurrency
import xyz.block.moneyaddress.typed.MOBILE_MONEY
import xyz.block.moneyaddress.typed.MoneyAddress
import xyz.block.moneyaddress.typed.MoneyAddressRegistry
import xyz.block.moneyaddress.typed.hasProtocol
import xyz.block.moneyaddress.typed.x.Carrier.Companion.asCarrier
import java.util.regex.Pattern
import xyz.block.moneyaddress.MoneyAddress as UntypedMoneyAddress

data class MobileMoneyAddress(
  override val currency: Currency,
  override val address: MomoAddress,
  override val id: String,
) : MoneyAddress<MomoAddress>(address, currency, MOBILE_MONEY, id) {
  init {
    MoneyAddressRegistry.register(MOBILE_MONEY) { address -> from(address) }
  }

  companion object {
    fun from(untypedMoneyAddress: UntypedMoneyAddress): MobileMoneyAddress {
      if (!untypedMoneyAddress.hasProtocol(MOBILE_MONEY)) {
        throw NotAMobileMoneyAddress(untypedMoneyAddress)
      }
      val momoAddress = MomoAddress.parse(untypedMoneyAddress.pss)
      return MobileMoneyAddress(
        untypedMoneyAddress.currency.asCurrency(),
        momoAddress,
        untypedMoneyAddress.id
      )
    }

    fun UntypedMoneyAddress.toMobileMoneyAddress(): MobileMoneyAddress = from(this)
  }
}

class NotAMobileMoneyAddress(moneyAddress: UntypedMoneyAddress) :
  Throwable("Not a mobile money address [$moneyAddress]")

data class MomoAddress(
  val carrier: Carrier,
  val phone: String,
) {
  companion object {
    private const val SEPARATOR = ":"
    private val MOMO_ADDRESS_PATTERN =
      Pattern.compile("""^([^$SEPARATOR]+)$SEPARATOR(.+)$""")

    fun parse(value: String): MomoAddress {
      val matcher = MOMO_ADDRESS_PATTERN.matcher(value)
      matcher.find()
      if (!matcher.matches()) {
        throw InvalidMomoAddressException
      }
      val carrier = matcher.group(1)
      val phone = matcher.group(2)

      return MomoAddress(carrier.asCarrier(), phone)
    }
  }
}

open class Carrier(open val scheme: String) {
  override fun toString(): String = scheme

  companion object {
    fun String.asCarrier(): Carrier =
      when (this) {
        MPESA.scheme -> MPESA
        else -> UNRECOGNIZED_CARRIER(this)
      }
  }
}

data object MPESA : Carrier("mpesa")
data class UNRECOGNIZED_CARRIER(override val scheme: String) : Carrier(scheme)

object InvalidMomoAddressException :
  Throwable("Invalid MOMO address")

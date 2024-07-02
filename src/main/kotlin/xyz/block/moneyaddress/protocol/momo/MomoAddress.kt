package xyz.block.moneyaddress.protocol.momo

import xyz.block.moneyaddress.MoneyAddress
import xyz.block.moneyaddress.currency.Currency
import xyz.block.moneyaddress.currency.Currency.Companion.asCurrency
import xyz.block.moneyaddress.protocol.MOMO
import xyz.block.moneyaddress.protocol.momo.Carrier.Companion.asCarrier
import java.util.regex.Pattern


data class MomoAddress(
  val currency: Currency,
  val carrier: Carrier,
  val phone: String,
  val id: String
) {
  override fun toString(): String =
    "Momo($currency, momo, $carrier, $phone, $id)"
}

fun MoneyAddress.isMomoAddress(): Boolean =
  this.protocol == MOMO.scheme

// TODO - this can throw if the momo-specific part is not valid
fun MoneyAddress.asMomoAddressOrNull(): MomoAddress? =
  if (this.isMomoAddress()) this.asMomoAddressOrThrow() else null

fun List<MoneyAddress>.findMomoAddresses(): List<MomoAddress> =
  mapNotNull { it.asMomoAddressOrNull() }

fun MoneyAddress.asMomoAddressOrThrow(): MomoAddress {
  if (isMomoAddress()) {
    val matcher = MOMO_ADDRESS_PATTERN.matcher(this.pss)
    matcher.find()
    if (!matcher.matches()) {
      throw InvalidMomoAddressException
    }
    val carrier = matcher.group(1)
    val phone = matcher.group(2)
    return MomoAddress(this.currency.asCurrency(), carrier.asCarrier(), phone, id)
  }
  else throw NotAMomoAddressException(this)
}

private const val SEPARATOR = ":"

private val MOMO_ADDRESS_PATTERN =
  Pattern.compile("""^([^$SEPARATOR]+)$SEPARATOR(.+)$""")

fun List<MoneyAddress>.asMomoAddressesOrThrow(): List<MomoAddress> =
  this.map { it.asMomoAddressOrThrow() }

class NotAMomoAddressException(moneyAddress: MoneyAddress) :
  Throwable("Not a momo address [$moneyAddress]")

object InvalidMomoAddressException :
  Throwable("Invalid MOMO address")


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

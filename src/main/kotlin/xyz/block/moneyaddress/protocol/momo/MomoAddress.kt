package xyz.block.moneyaddress.protocol.momo

import xyz.block.moneyaddress.MoneyAddress
import xyz.block.moneyaddress.protocol.Protocol
import java.util.regex.Pattern

data class MomoAddress(
  val currency: String,
  val carrier: String,
  val phone: String,
  val moneyAddress: MoneyAddress,
) {
  override fun toString(): String =
    "Momo($currency, momo, $carrier, $phone, ${moneyAddress.id})"
}

fun MoneyAddress.isMomoAddress(): Boolean =
  this.protocol == Protocol.MOMO.scheme

fun MoneyAddress.asMomoAddress(): MomoAddress {
  if (isMomoAddress()) {
    val matcher = MOMO_ADDRESS_PATTERN.matcher(this.pss)
    matcher.find()
    if (!matcher.matches()) {
      throw InvalidMomoAddressException
    }
    val carrier = matcher.group(1)
    val phone = matcher.group(2)
    return MomoAddress(this.currency, carrier, phone, this)
  }
  else throw NotAMomoAddressException(this)
}

private const val SEPARATOR = ":"

private val MOMO_ADDRESS_PATTERN =
  Pattern.compile("""^([^$SEPARATOR]+)$SEPARATOR(.+)$""")

fun List<MoneyAddress>.asMomoAddresses(): List<MomoAddress> =
  this.map { it.asMomoAddress() }

fun MoneyAddress.asMomoAddressOrNull(): MomoAddress? =
  if (this.isMomoAddress()) this.asMomoAddress() else null

fun List<MoneyAddress>.findMomoAddresses(): List<MomoAddress> =
  mapNotNull { it.asMomoAddressOrNull() }

object InvalidMomoAddressException :
  Throwable("Invalid MOMO address")

class NotAMomoAddressException(moneyAddress: MoneyAddress) :
  Throwable("Not a momo address [$moneyAddress]")

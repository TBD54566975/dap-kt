package xyz.block.moneyaddress.typed

import xyz.block.moneyaddress.Currency
import xyz.block.moneyaddress.Currency.Companion.asCurrency
import xyz.block.moneyaddress.MOBILE_MONEY
import xyz.block.moneyaddress.TypedMoneyAddressRegistry
import xyz.block.moneyaddress.TypedMoneyAddress
import xyz.block.moneyaddress.hasProtocol
import xyz.block.moneyaddress.typed.Carrier.Companion.asCarrier
import java.util.regex.Pattern
import xyz.block.moneyaddress.MoneyAddress as UntypedMoneyAddress

/**
 * A typed representation of a MobileMoney address.
 *
 * @property address - a [MomoAddress].
 * @property currency - any [Currency]. Not validated to be a currency supported by MobileMoney.
 * @property protocol - always [MOBILE_MONEY].
 */
data class MobileMoneyAddress(
  override val currency: Currency,
  override val address: MomoAddress,
  override val id: String,
) : TypedMoneyAddress<MomoAddress>(address, currency, MOBILE_MONEY, id) {
  companion object {
    fun register(typedMoneyAddressRegistry: TypedMoneyAddressRegistry) {
      typedMoneyAddressRegistry.register(MOBILE_MONEY) { address -> from(address) }
    }

    /**
     * Converts an [UntypedMoneyAddress] to a [MobileMoneyAddress].
     *
     * @throws NotAMobileMoneyAddress if the protocol is not [MOBILE_MONEY].
     * @throws InvalidMomoAddressException if the address is not a valid MobileMoney address.
     */
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

/**
 * A typed representation of a Momo address.
 *
 * @property carrier - a type-safe representation of the carrier. See [Carrier].
 * @property phone - the string representation of the phone number. Not validated.
 */
data class MomoAddress(
  val carrier: Carrier,
  val phone: String,
) {
  companion object {
    private const val SEPARATOR = ":"
    private val MOMO_ADDRESS_PATTERN =
      Pattern.compile("""^([^$SEPARATOR]+)$SEPARATOR(.+)$""")

    /**
     * Parse a String into a [MomoAddress].
     * The expected format is "carrier:phone".
     * Does not perform validation of the `carrier` or `phone` values.
     *
     * @throws InvalidMomoAddressException if the address is not a valid MobileMoney address.
     */
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

/**
 * Represents the carrier of a MomoAddress.
 * @property scheme - the string representation of the carrier as used in the DID Documents.
 *
 * Carriers mentioned in the DAP specification have concrete implementations.
 * This allows type-safe matching of MomoAddress objects without matching on strings.
 *
 * Carriers not included in the DAP specification are represented by [UNRECOGNIZED_CARRIER].
 */
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

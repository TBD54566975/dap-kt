package xyz.block.dap

import java.util.regex.Pattern

data class Dap(
  val handle: String,
  val domain: String
) {
  override fun toString(): String {
    return "$PREFIX$handle$SEPARATOR$domain'"
  }

  companion object {
    const val PREFIX = "@"
    const val SEPARATOR = "/"

    const val SERVICE_TYPE = "DAPRegistry"

    private const val DAP_REGEX = """^$PREFIX([^$PREFIX$SEPARATOR]+)$SEPARATOR([^$PREFIX$SEPARATOR]+)$"""
    private val DAP_PATTERN = Pattern.compile(DAP_REGEX)

    fun parse(dap: String): Dap {
      val matcher = DAP_PATTERN.matcher(dap)
      matcher.find()
      if (!matcher.matches()) {
        throw InvalidDapException
      }
      val handle = matcher.group(1)
      val domain = matcher.group(2)

      return Dap(handle, domain)
    }
  }
}

object InvalidDapException : Throwable(message = "Invalid DAP")

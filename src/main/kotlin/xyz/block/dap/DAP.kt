package xyz.block.dap

import java.util.regex.Pattern

data class DAP(
  val handle: String,
  val domain: String
) {
  override fun toString(): String {
    return "$PREFIX$handle$SEPARATOR$domain'"
  }

  companion object Parser {
    const val PREFIX = "@"
    const val SEPARATOR = "/"

    private const val DAP_REGEX = """^$PREFIX([^$PREFIX/]+)/([^$PREFIX/]+)$"""
    private val DAP_PATTERN = Pattern.compile(DAP_REGEX)

    fun parse(dap: String): DAP {
      val matcher = DAP_PATTERN.matcher(dap)
      matcher.find()
      if (!matcher.matches()) {
        throw DAPParserException(dap)
      }
      val handle = matcher.group(1)
      val domain = matcher.group(2)

      return DAP(handle, domain)
    }
  }
}

data class DAPParserException(val invalidValue: String) :
  Throwable(message = "Invalid DAP: $invalidValue")
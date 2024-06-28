package xyz.block.moneyaddress.protocol

enum class Protocol(val scheme: String) {
  ADDR("addr"),
  ETH("eth"),
  LNADDR("lnaddr"),
  MOMO("momo"),
  SPADDR("spaddr"),
  XLM("xlm"),
}
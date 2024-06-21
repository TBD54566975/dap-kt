package xyz.block.dap

data class DAP(
    val handle: String,
    val domain: String
) {
    override fun toString(): String {
        return "@$handle/$domain'"
    }
}

package healthcare.astrocyte.common.main

data class AstrocyteItem(
    val id: Long = 0L,
    val order: Long = 0L,
    val text: String = "",
    val isDone: Boolean = false
)

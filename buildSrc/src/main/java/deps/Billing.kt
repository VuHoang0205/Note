package deps

object Billing : Dependency() {

    object Versions {
        const val billing = "5.1.0"
    }

    private const val billing = "com.android.billingclient:billing-ktx:${Versions.billing}"


    override fun implementations() = listOf<String>(
        billing
    )
}
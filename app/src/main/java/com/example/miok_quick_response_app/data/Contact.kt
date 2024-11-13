import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val name: String,
    val phone_number: String,
    val relationship: Relationship,
    val status: Boolean
) : Parcelable {
    val statusText: String
        get() = if (status) "Status: Approved" else "Status: Not Approved"

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        Relationship.valueOf(parcel.readString() ?: Relationship.OTHER.name),
        parcel.readByte() != 0.toByte()
    )
}


enum class Relationship {
    PARENT_GUARDIAN,
    CAREGIVER,
    AUNT_UNCLE,
    GRANDPARENT,
    SOCIAL_WORKER,
    POLICE,
    OTHER
}
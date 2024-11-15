import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val id: Int,
    val name: String,
    val phone_number: String,
    val relationship: Relationship,
    val status: Boolean,
    val emerg_contact: Boolean
) : Parcelable {

    val statusText: String
        get() = if (status) "Status: Approved" else "Status: Not Approved"

    val emergContactText: String
        get() = if (emerg_contact) "EMERGENCY CONTACT" else ""

    constructor(parcel: Parcel) : this(
        parcel.readInt(),  // Read 'id' as Int
        parcel.readString() ?: "",  // Read 'name' as String, default to "" if null
        parcel.readString() ?: "",  // Read 'phone_number' as String, default to "" if null
        Relationship.valueOf(parcel.readString() ?: Relationship.OTHER.name),  // Read 'relationship' as Enum, default to Relationship.OTHER if null
        parcel.readByte() != 0.toByte(),  // Read 'status' as Boolean, 0 is false, anything else is true
        parcel.readByte() != 0.toByte()
    )
}

enum class Relationship {
    PARENT_GUARDIAN,
    CAREGIVER,
    AUNT_UNCLE,
    GRANDPARENT,
    SOCIAL_WORKER,
    IWI_SOCIAL_WORKER,
    POLICE,
    OTHER
}

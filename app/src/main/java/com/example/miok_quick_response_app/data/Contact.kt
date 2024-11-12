import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val name: String,
    val dob: String, // Date of Birth
    val relationship: Relationship
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        Relationship.valueOf(parcel.readString() ?: Relationship.OTHER.name)
    )
}

enum class Relationship {
    PARENT_GUARDIAN,
    CAREGIVER,
    AUNT_UNCLE,
    GRANDPARENT,
    OTHER
}

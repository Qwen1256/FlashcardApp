import android.os.Parcel
import android.os.Parcelable


// Flashcard data class represents a single flashcard with a question and a true/false answer.
// It implements Parcelable to allow passing instances between Android components.
data class Flashcard(
    val ques: String,
    val ans: Boolean,
    var userAnswer: Boolean? = null // user's answer (null = not answered)
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte().let { if (it == 2.toByte()) null else it != 0.toByte() }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ques)
        parcel.writeByte(if (ans) 1 else 0)
        parcel.writeByte(
            when (userAnswer) {
                true -> 1
                false -> 0
                null -> 2
            }
        )
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Flashcard> {
        override fun createFromParcel(parcel: Parcel): Flashcard = Flashcard(parcel)
        override fun newArray(size: Int): Array<Flashcard?> = arrayOfNulls(size)
    }
}

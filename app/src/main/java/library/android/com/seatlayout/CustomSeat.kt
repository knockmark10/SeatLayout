package library.android.com.seatlayout

import android.graphics.Color
import library.android.com.library.room.RoomScheme
import library.android.com.library.room.Seat

data class CustomSeat(

    var id: Int = -1,

    var color: Int = Color.RED,

    var marker: String = "",

    var selectedSeatMarker: String = "",

    var seatStatus: RoomScheme.SeatStatus = RoomScheme.SeatStatus.FREE

) : Seat {

    override fun id(): Int = id

    override fun color(): Int = color

    override fun marker(): String = marker

    override fun selectedSeat(): String = selectedSeatMarker

    override fun status(): RoomScheme.SeatStatus = seatStatus

    override fun setStatus(status: RoomScheme.SeatStatus?) {
        this.seatStatus = status ?: RoomScheme.SeatStatus.FREE
    }

}
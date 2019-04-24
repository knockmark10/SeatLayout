package library.knockmark.com.seatlayout

import android.graphics.Color
import library.knockmark.com.library.room.RoomScheme
import library.knockmark.com.library.room.Seat

data class CustomSeat(

    var id: Int = -1,

    var color: Int = Color.RED,

    var marker: String = "",

    var seatStatus: RoomScheme.SeatStatus = RoomScheme.SeatStatus.FREE

) : Seat {

    override fun id(): Int = id

    override fun marker(): String = marker

    override fun status(): RoomScheme.SeatStatus = seatStatus

    override fun setStatus(status: RoomScheme.SeatStatus?) {
        this.seatStatus = status ?: RoomScheme.SeatStatus.FREE
    }

}
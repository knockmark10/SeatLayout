package library.knockmark.com.seatlayout

import android.graphics.Color
import library.knockmark.com.library.room.RoomScheme
import library.knockmark.com.library.room.Seat

data class CustomSeat(

    var id: String = "",

    var color: Int = Color.RED,

    var indicator: String = "",

    var seatNumber: String = "",

    var seatStatus: RoomScheme.SeatStatus = RoomScheme.SeatStatus.FREE

) : Seat {

    override fun id(): String = id

    override fun indicator(): String = indicator

    override fun passengerName(): String = seatNumber

    override fun status(): RoomScheme.SeatStatus = seatStatus

    override fun setStatus(status: RoomScheme.SeatStatus?) {
        this.seatStatus = status ?: RoomScheme.SeatStatus.FREE
    }

    override fun updatePassengerName(passengerName: String) {
        seatNumber = passengerName
    }

}
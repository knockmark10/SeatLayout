package library.android.com.seatlayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import library.android.com.library.room.RoomScheme
import library.android.com.library.room.Seat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scheme = RoomScheme.Builder(this, this.seat_map, getSeats())
            .setFreeSeatIcon(R.drawable.ic_flight_seat_free)
            .setChosenSeatIcon(R.drawable.ic_flight_seat_chosen)
            .setBusySeatIcon(R.drawable.ic_flight_seat_busy)
            .setMaxSelectedTickets(10)
            .build()

    }

    private fun getSeats(): Array<Array<Seat>> {
        var seats = arrayOf<Array<Seat>>()
        for (i in 0..9) {
            var array = arrayOf<Seat>()
            for (j in 0..9) {
                val seat = CustomSeat()
                seat.id = i * 10 + (j + 1)
                seat.selectedSeatMarker = (j + 1).toString()
                if (i == 3 && j == 3 || i == 5 && j == 2) {
                    seat.seatStatus = RoomScheme.SeatStatus.BUSY
                    seat.marker = seat.id.toString()
                } else if (i == 8) {
                    seat.seatStatus = RoomScheme.SeatStatus.BUSY
                } else if (i == 9 && j == 0) {
                    seat.seatStatus = RoomScheme.SeatStatus.EMPTY
                } else {
                    seat.seatStatus = RoomScheme.SeatStatus.FREE
                }
                array += seat
            }
            seats += array
        }
        return seats
    }
}

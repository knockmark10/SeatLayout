package library.knockmark.com.seatlayout

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import library.knockmark.com.library.room.RoomScheme
import library.knockmark.com.library.room.Seat
import library.knockmark.com.seatlayout.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scheme = RoomScheme.Builder(this, this.seat_map, getSeats())
            .setFreeSeatIcon(R.drawable.ic_flight_seat_free)
            .setSpecialSeatIcon(R.drawable.ic_doted_line)
            .setChosenSeatIcon(R.drawable.ic_flight_seat_chosen)
            .setBusySeatIcon(R.drawable.ic_flight_seat_busy)
            .setHallIcon(R.drawable.ic_doted_line, 50)
            .setMaxSelectedTickets(9)
            .setSeatGap(20)
            .build()

        scheme.setBackgroundColor(ContextCompat.getColor(this, R.color.background))
        scheme.setMarkerColor(ContextCompat.getColor(this, R.color.colorMarker))
    }

    private fun getSeats(): Array<Array<Seat>> {
        var seats = arrayOf<Array<Seat>>()
        for (i in 0..9) {
            var array = arrayOf<Seat>()
            for (j in 0..7) {
                val seat = CustomSeat()
                seat.id = i * 10 + (j + 1)
                if (i == 3 && j == 3 || i == 5 && j == 2) {
                    seat.seatStatus = RoomScheme.SeatStatus.BUSY
                    seat.marker = seat.id.toString()
                } else if (i == 8) {
                    seat.seatStatus = RoomScheme.SeatStatus.BUSY
                } else if (i == 9 && j == 0) {
                    seat.seatStatus = RoomScheme.SeatStatus.EMPTY
                } else if(i == 2 && j == 2) {
                    seat.seatStatus = RoomScheme.SeatStatus.SPECIAL
                } else {
                    seat.seatStatus = RoomScheme.SeatStatus.FREE
                }

                if (i == 0) {
                    if (j == 4) {
                        seat.seatStatus = RoomScheme.SeatStatus.EMPTY
                    } else {
                        seat.seatStatus = RoomScheme.SeatStatus.INFO
                        when (j) {
                            1 -> seat.marker = "A"
                            2 -> seat.marker = "B"
                            3 -> seat.marker = "C"
                            5 -> seat.marker = "D"
                            6 -> seat.marker = "E"
                            7 -> seat.marker = "F"
                        }
                    }
                }

                if (j == 0) {
                    if (i == 0) {
                        seat.seatStatus = RoomScheme.SeatStatus.EMPTY
                    } else {
                        seat.seatStatus = RoomScheme.SeatStatus.INFO
                        seat.marker = i.toString()
                    }
                }

                if (j == 4) {
                    if (i == 0) {
                        seat.seatStatus = RoomScheme.SeatStatus.EMPTY
                    } else {
                        seat.seatStatus = RoomScheme.SeatStatus.HALL
                    }
                }
                array += seat
            }
            seats += array
        }
        return seats
    }
}

package library.knockmark.com.seatlayout

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import library.knockmark.com.library.room.RoomScheme
import library.knockmark.com.library.room.Seat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scheme = RoomScheme.Builder(this, this.seat_map)
            .setSeats(getSeats())
            .setFreeSeatIcon(R.drawable.ic_flight_seat_free)
            .setSpecialSeatIcon(R.drawable.ic_doted_line)
            .setChosenSeatIcon(R.drawable.ic_flight_seat_chosen)
            .setBusySeatIcon(R.drawable.ic_flight_seat_busy)
            .setHallIcon(R.drawable.ic_doted_line, 50)
            .setMaxSelectedTickets(9)
            .setSeatGap(25)
            .build()

        scheme.setBackgroundColor(ContextCompat.getColor(this, R.color.background))
        scheme.setIndicatorColor(ContextCompat.getColor(this, R.color.colorMarker))
        scheme.setPassengerNameColor(ContextCompat.getColor(this, R.color.colorAccent))
    }

    private fun getSeats(): Array<Array<Seat>> {
        var seats = arrayOf<Array<Seat>>()
        for (i in 0..9) {
            var array = arrayOf<Seat>()
            for (j in 0..7) {
                val seat = CustomSeat()
                seat.id = (i * 10 + (j + 1)).toString()
                seat.seatNumber = "$i$j"

                //Setting dummy seats
                if (i == 4 && j == 3 || i == 6 && j == 2) {
                    seat.seatStatus = RoomScheme.SeatStatus.BUSY
                } else if (i == 8) {
                    seat.seatStatus = RoomScheme.SeatStatus.BUSY
                } else if (i == 10 && j == 0) {
                    seat.seatStatus = RoomScheme.SeatStatus.EMPTY
                } else if (i == 2 && j == 2) {
                    seat.seatStatus = RoomScheme.SeatStatus.SPECIAL
                } else {
                    seat.seatStatus = RoomScheme.SeatStatus.FREE
                }

                if (j == 0) {
                    if (i == 0) {
                        seat.seatStatus = RoomScheme.SeatStatus.EMPTY
                    } else {
                        seat.seatStatus = RoomScheme.SeatStatus.INDICATOR
                        seat.indicator = i.toString()
                    }
                }

                if (i == 0) {
                    if (j == 4) {
                        seat.seatStatus = RoomScheme.SeatStatus.EMPTY
                    } else {
                        seat.seatStatus = RoomScheme.SeatStatus.INDICATOR
                        when (j) {
                            1 -> seat.indicator = "A"
                            2 -> seat.indicator = "B"
                            3 -> seat.indicator = "C"
                            5 -> seat.indicator = "D"
                            6 -> seat.indicator = "E"
                            7 -> seat.indicator = "F"
                        }
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

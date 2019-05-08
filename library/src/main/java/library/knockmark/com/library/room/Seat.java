package library.knockmark.com.library.room;

import android.support.annotation.NonNull;

public interface Seat {

    String id();

    String indicator();

    String passengerName();

    RoomScheme.SeatStatus status();

    void setStatus(RoomScheme.SeatStatus status);

    void updatePassengerName(@NonNull String passengerName);
}

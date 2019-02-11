package library.android.com.library.room;

public interface Seat {
    int id();

    String marker();

    RoomScheme.SeatStatus status();

    void setStatus(RoomScheme.SeatStatus status);
}

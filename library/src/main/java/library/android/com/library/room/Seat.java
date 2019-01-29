package library.android.com.library.room;

public interface Seat {
    int id();

    String marker();

    String selectedSeat();

    RoomScheme.SeatStatus status();

    void setStatus(RoomScheme.SeatStatus status);
}

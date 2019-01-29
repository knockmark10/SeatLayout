package library.android.com.library.room;

public interface Seat {
    int id();

    int color();

    String marker();

    String selectedSeat();

    RoomScheme.SeatStatus status();

    void setStatus(RoomScheme.SeatStatus status);
}

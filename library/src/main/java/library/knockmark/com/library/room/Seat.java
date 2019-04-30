package library.knockmark.com.library.room;

public interface Seat {

    String id();

    String indicator();

    String passengerName();

    RoomScheme.SeatStatus status();

    void setStatus(RoomScheme.SeatStatus status);
}

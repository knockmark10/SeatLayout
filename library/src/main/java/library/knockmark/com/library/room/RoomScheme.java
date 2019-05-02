package library.knockmark.com.library.room;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import library.knockmark.com.library.R;
import library.knockmark.com.library.manager.BitmapManager;
import library.knockmark.com.library.widget.SeatLayoutView;

import java.util.ArrayList;
import java.util.List;

public class RoomScheme {

    private int width, height;
    private Seat[][] seats;

    private final Rect textBounds = new Rect();
    private Paint textPaint, backgroundPaint, indicatorPaint, passengerNamePaint, scenePaint;
    private int seatWidth, offset;
    private int seatGap = 5;
    private int schemeBackgroundColor, sceneBackgroundColor;
    private int selectedSeats, maxSelectedSeats;
    private Typeface typeface;
    private String sceneName;
    private Bitmap freeSeatIcon;
    private Bitmap specialSeatIcon;
    private Bitmap busySeatIcon;
    private Bitmap chosenSeatIcon;
    private Bitmap hallIcon;

    private Scene scene;
    private SeatLayoutView image;
    private SeatListener listener;
    private MaxSeatsClickListener maxSeatsClickListener;
    private List<Zone> zones;
    private ZoneListener zoneListener;

    private RoomScheme(SeatLayoutView image, Seat[][] seats) {
        this.image = image;
        this.seats = seats;
    }

    private void drawHall(Context context) {
        this.selectedSeats = 0;
        this.maxSelectedSeats = -1;

        nullifyMap();

        image.setClickListener(new ImageClickListener() {
            @Override
            public void onClick(Point p) {
                p.x -= scene.getLeftYOffset();
                p.y -= scene.getTopXOffset();
                clickScheme(p);
            }
        });

        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        sceneName = context.getString(R.string.scene);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            schemeBackgroundColor = context.getResources().getColor(R.color.light_grey);
            indicatorPaint = initTextPaint(context.getResources().getColor(R.color.black_gray));
            passengerNamePaint = initTextPaint(context.getResources().getColor(R.color.black_gray));
            scenePaint = initTextPaint(context.getResources().getColor(R.color.black_gray));
        } else {
            schemeBackgroundColor = context.getColor(R.color.light_grey);
            indicatorPaint = initTextPaint(context.getColor(R.color.black_gray));
            passengerNamePaint = initTextPaint(context.getColor(R.color.black_gray));
            scenePaint = initTextPaint(context.getColor(R.color.black_gray));
        }
        textPaint = initTextPaint(Color.WHITE);
        scenePaint.setTextSize(35);

        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(schemeBackgroundColor);
        backgroundPaint.setStrokeWidth(0);

        seatWidth = 30;
        offset = 30;
        height = seats.length;
        width = seats[0].length;
        this.scene = new Scene(ScenePosition.NONE, 0, 0, offset / 2);
        image.setImageBitmap(getImageBitmap());
    }

    public void setScenePosition(ScenePosition position) {
        scene = new Scene(position, width, height, offset / 2);
        image.setImageBitmap(getImageBitmap());
    }

    public void setSceneName(String name) {
        sceneName = name;
        image.setImageBitmap(getImageBitmap());
    }

    public void setBackgroundColor(@ColorInt int color) {
        schemeBackgroundColor = color;
        image.setImageBitmap(getImageBitmap());
    }

    public void setIndicatorColor(@ColorInt int color) {
        indicatorPaint.setColor(color);
        image.setImageBitmap(getImageBitmap());
    }

    public void setPassengerNameColor(@ColorInt int color) {
        passengerNamePaint.setColor(color);
        image.setImageBitmap(getImageBitmap());
    }

    public void setSceneTextColor(@ColorInt int color) {
        scenePaint.setColor(color);
        image.setImageBitmap(getImageBitmap());
    }

    public void setSceneBackgroundColor(@ColorInt int color) {
        sceneBackgroundColor = color;
        image.setImageBitmap(getImageBitmap());
    }

    public void setSeatListener(SeatListener listener) {
        this.listener = listener;
    }

    public void setMaxSeatsClickListener(MaxSeatsClickListener maxSeatsClickListener) {
        this.maxSeatsClickListener = maxSeatsClickListener;
    }

    /**
     * Set custom typeface to scheme.
     * Be careful when using. Bold typefaces can be drawn incorrectly.
     *
     * @param typeface - Typeface to be set
     */
    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        indicatorPaint.setTypeface(typeface);
        passengerNamePaint.setTypeface(typeface);
        scenePaint.setTypeface(typeface);
        textPaint.setTypeface(typeface);
        image.setImageBitmap(getImageBitmap());
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
        image.setImageBitmap(getImageBitmap());
    }

    public void setZoneListener(ZoneListener zoneListener) {
        this.zoneListener = zoneListener;
    }

    private Paint initTextPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeMiter(0);
        paint.setTextSize(25);
        paint.setTypeface(typeface);
        return paint;
    }

    private void clickScheme(Point point) {
        if (findZoneClick(point))
            return;
        Point p = new Point(point.x - offset / 2, point.y - offset / 2);
        int row = p.x / (seatWidth + seatGap);
        int seat = p.y / (seatWidth + seatGap);
        if (canSeatPress(p, row, seat)) {
            clickScheme(row, seat);
        }
    }

    private boolean findZoneClick(Point p) {
        for (Zone zone : zones) {
            int topX = offset / 2 + zone.leftTopX() * (seatWidth + seatGap);
            int topY = offset / 2 + zone.leftTopY() * (seatWidth + seatGap);
            int bottomX = offset / 2 + (zone.leftTopX() + zone.width()) * (seatWidth + seatGap) - seatGap;
            int bottomY = offset / 2 + (zone.leftTopY() + zone.height()) * (seatWidth + seatGap) - seatGap;
            if (p.x >= topX && p.x <= bottomX && p.y >= topY && p.y <= bottomY) {
                if (zoneListener != null)
                    zoneListener.onZoneClicked(zone.id());
                return true;
            }
        }
        return false;
    }

    public void clickSchemeProgrammatically(int row, int seat) {
        if (SeatStatus.canSeatBePressed(seats[row][seat].status())) {
            clickScheme(seat, row);
        }
    }

    private void clickScheme(int row, int seat) {
        Seat pressedSeat = seats[seat][row];
        if (updateSelectedSeatCount(pressedSeat)) {
            notifySeatListener(pressedSeat);
            pressedSeat.setStatus(pressedSeat.status().pressSeat());
            image.setImageBitmap(getImageBitmap());
        } else if (maxSeatsClickListener != null) {
            maxSeatsClickListener.onMaxSeatsReached(pressedSeat.id());
        }
    }

    /**
     * Increases or decreases current selected seats count
     *
     * @param seat Seat that has been clicked
     * @return false If selected seats reached max limit, false otherwise
     */
    private boolean updateSelectedSeatCount(Seat seat) {
        if (seat.status() == SeatStatus.FREE || seat.status() == SeatStatus.SPECIAL) {
            if (maxSelectedSeats == -1 || selectedSeats + 1 <= maxSelectedSeats) {
                selectedSeats++;
            } else {
                return selectedSeats + 1 <= maxSelectedSeats;
            }
            return true;
        }
        selectedSeats--;
        return true;
    }

    private boolean canSeatPress(Point p, int row, int seat) {
        if (row >= width || (p.x % (seatWidth + seatGap) >= seatWidth)
                || p.x <= 0) {
            return false;
        }
        if (seat >= height || (p.y % (seatWidth + seatGap) >= seatWidth)
                || p.y <= 0) {
            return false;
        }
        return SeatStatus.canSeatBePressed(seats[seat][row].status());
    }

    private Bitmap getImageBitmap() {
        int bitmapHeight = height * (seatWidth + seatGap) - seatGap + offset + scene.getTopXOffset() + scene.getBottomXOffset();
        int bitmapWidth = width * (seatWidth + seatGap) - seatGap + offset + scene.getLeftYOffset() + scene.getRightYOffset();

        Bitmap tempBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(tempBitmap);
        backgroundPaint.setColor(schemeBackgroundColor);
        tempCanvas.drawRect(0, 0, bitmapWidth, bitmapHeight, backgroundPaint);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                switch (seats[i][j].status()) {
                    case FREE:
                        tempCanvas.drawBitmap(freeSeatIcon,
                                offset / 2 + (seatWidth + seatGap) * j + scene.getLeftYOffset(),
                                offset / 2 + (seatWidth + seatGap) * i + scene.getTopXOffset(),
                                backgroundPaint);
                        break;
                    case SPECIAL:
                        tempCanvas.drawBitmap(specialSeatIcon,
                                offset / 2 + (seatWidth + seatGap) * j + scene.getLeftYOffset(),
                                offset / 2 + (seatWidth + seatGap) * i + scene.getTopXOffset(),
                                backgroundPaint);
                        break;
                    case BUSY:
                        tempCanvas.drawBitmap(busySeatIcon,
                                offset / 2 + (seatWidth + seatGap) * j + scene.getLeftYOffset(),
                                offset / 2 + (seatWidth + seatGap) * i + scene.getTopXOffset(),
                                backgroundPaint);
                        break;
                    case CHOSEN_FREE:
                    case CHOSEN_SPECIAL:
                        tempCanvas.drawBitmap(chosenSeatIcon,
                                offset / 2 + (seatWidth + seatGap) * j + scene.getLeftYOffset(),
                                offset / 2 + (seatWidth + seatGap) * i + scene.getTopXOffset(),
                                backgroundPaint);
                        drawTextCentred(tempCanvas, passengerNamePaint, seats[i][j].passengerName(),
                                offset / 2 + (seatWidth + seatGap) * j + seatWidth / 2 + scene.getLeftYOffset(),
                                offset / 10 + (seatWidth + seatGap) * i + seatWidth / 10 + scene.getTopXOffset());
                        break;
                    case INDICATOR:
                        drawTextCentred(tempCanvas, indicatorPaint, seats[i][j].indicator(),
                                offset / 2 + (seatWidth + seatGap) * j + seatWidth / 2 + scene.getLeftYOffset(),
                                offset / 2 + (seatWidth + seatGap) * i + seatWidth / 2 + scene.getTopXOffset());
                        break;
                    case HALL:
                        tempCanvas.drawBitmap(hallIcon,
                                offset / 2 + (seatWidth + seatGap - 2) * j + scene.getLeftYOffset(),
                                offset / 2 + (seatWidth + seatGap) * i + scene.getTopXOffset(),
                                backgroundPaint);
                    case EMPTY:
                        break;
                }
            }
        }

        for (Zone zone : zones) {
            if (zone.width() == 0 || zone.height() == 0
                    || zone.leftTopX() + zone.width() > width
                    || zone.leftTopY() + zone.height() > height)
                continue;
            backgroundPaint.setColor(zone.color());
            int topX = offset / 2 + zone.leftTopX() * (seatWidth + seatGap) + scene.getLeftYOffset();
            int topY = offset / 2 + zone.leftTopY() * (seatWidth + seatGap) + scene.getTopXOffset();
            int bottomX = offset / 2 + (zone.leftTopX() + zone.width()) * (seatWidth + seatGap) - seatGap + scene.getLeftYOffset();
            int bottomY = offset / 2 + (zone.leftTopY() + zone.height()) * (seatWidth + seatGap) - seatGap + scene.getTopXOffset();
            tempCanvas.drawRect(topX, topY, bottomX, bottomY, backgroundPaint);
        }

        drawScene(tempCanvas);

        return tempBitmap;
    }

    private void drawScene(Canvas canvas) {
        if (scene.position == ScenePosition.NONE) {
            return;
        }
        backgroundPaint.setColor(sceneBackgroundColor);
        int topX = 0, topY = 0, bottomX = 0, bottomY = 0;
        switch (scene.position) {
            case NORTH:
                int totalWidth = width * (seatWidth + seatGap) - seatGap + offset;
                topX = offset / 2;
                topY = totalWidth / 2 - width * 6;
                bottomX = topX + scene.dimension;
                bottomY = topY + scene.dimensionSecond;
                break;
            case SOUTH:
                int totalWidthSouth = width * (seatWidth + seatGap) - seatGap + offset;
                topX = height * (seatWidth + seatGap) - seatGap + offset;
                topY = totalWidthSouth / 2 - width * 6;
                bottomX = topX + scene.dimension;
                bottomY = topY + scene.dimensionSecond;
                break;
            case EAST:
                int totalHeight = height * (seatWidth + seatGap) - seatGap + offset;
                topX = totalHeight / 2 - height * 6;
                topY = offset / 2;
                bottomX = topX + scene.dimensionSecond;
                bottomY = topY + scene.dimension;
                break;
            case WEST:
                int totalHeightWest = height * (seatWidth + seatGap) - seatGap + offset;
                topX = totalHeightWest / 2 - height * 6;
                topY = width * (seatWidth + seatGap) - seatGap + offset;
                bottomX = topX + scene.dimensionSecond;
                bottomY = topY + scene.dimension;
                break;
        }

        canvas.drawRect(topY, topX, bottomY, bottomX, backgroundPaint);
        canvas.save();
        if (scene.position == ScenePosition.EAST) {
            canvas.rotate(270, (topY + bottomY) / 2, (topX + bottomX) / 2);
        } else if (scene.position == ScenePosition.WEST) {
            canvas.rotate(90, (topY + bottomY) / 2, (topX + bottomX) / 2);
        }
        drawTextCentred(canvas, scenePaint, sceneName, (topY + bottomY) / 2, (topX + bottomX) / 2);
        canvas.restore();
    }

    private void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy) {
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
    }

    private void nullifyMap() {
        width = 0;
        height = 0;
        zones = new ArrayList<>();
        image.setShouldOnMeasureBeCalled(true);
    }

    private void notifySeatListener(Seat seat) {
        if (seat.status() == SeatStatus.FREE) {
            if (listener != null)
                listener.onSeatSelected(seat.id());
        } else {
            if (listener != null)
                listener.onSeatUnselected(seat.id());
        }
    }

    public static class Scene {

        private ScenePosition position;
        private int dimension;
        private int dimensionSecond;
        public int width, height, offset;

        private void setScenePosition(ScenePosition position, int offset) {
            this.position = position;
            this.offset = offset;
            dimension = 90;
            switch (position) {
                case NORTH:
                    dimensionSecond = width * 12;
                    break;
                case SOUTH:
                    dimensionSecond = width * 12;
                    break;
                case EAST:
                    dimensionSecond = height * 12;
                    break;
                case WEST:
                    dimensionSecond = height * 12;
                    break;
                case NONE:
                    dimensionSecond = 0;
                    dimension = 0;
                    break;
                default:
                    dimensionSecond = 0;
                    dimension = 0;
                    this.position = ScenePosition.NONE;
                    break;
            }
        }

        private Scene(ScenePosition position, int width, int height, int offset) {
            this.width = width;
            this.height = height;
            setScenePosition(position, offset);
        }

        private int getTopXOffset() {
            if (position == ScenePosition.NORTH) {
                return dimension + offset;
            }
            return 0;
        }

        private int getLeftYOffset() {
            if (position == ScenePosition.EAST) {
                return dimension + offset;
            }
            return 0;
        }

        private int getBottomXOffset() {
            if (position == ScenePosition.SOUTH) {
                return dimension + offset;
            }
            return 0;
        }

        private int getRightYOffset() {
            if (position == ScenePosition.WEST) {
                return dimension + offset;
            }
            return 0;
        }

        @Override
        public String toString() {
            return String.format("Left - %d, Right- %d, Top - %d, Bottom - %d", getLeftYOffset(), getRightYOffset(), getTopXOffset(), getBottomXOffset());
        }

    }

    public enum SeatStatus {
        FREE, BUSY, EMPTY, CHOSEN_FREE, CHOSEN_SPECIAL, SPECIAL, INDICATOR, HALL;

        public static boolean canSeatBePressed(SeatStatus status) {
            return (status == FREE || status == CHOSEN_FREE || status == CHOSEN_SPECIAL || status == SPECIAL);
        }

        public SeatStatus pressSeat() {
            switch (this) {
                case FREE:
                    return CHOSEN_FREE;
                case SPECIAL:
                    return CHOSEN_SPECIAL;
                case CHOSEN_FREE:
                    return FREE;
                case CHOSEN_SPECIAL:
                    return SPECIAL;
                default:
                    return this;
            }
        }
    }

    @Override
    public String toString() {
        return String.format("height = %d; width = %d", height, width);
    }

    public static class Builder {

        private Bitmap freeSeatIcon;
        private Bitmap specialSeatIcon;
        private Bitmap busySeatIcon;
        private Bitmap chosenSeatIcon;
        private Bitmap hallIcon;
        private Resources mResources;
        private SeatLayoutView mImage;
        private Context mContext;
        private Seat[][] mSeats;
        private int maxTickets = -1;
        private int seatGap = 0;

        public Builder(Context context, SeatLayoutView image) {
            mResources = context.getResources();
            mImage = image;
            mContext = context;
        }

        public Builder setSeats(Seat[][] seats) {
            mSeats = seats;
            return this;
        }

        public Builder setFreeSeatIcon(@DrawableRes int resId) {
            Bitmap seatBitmap = BitmapManager.convertDrawableToBitmap(mResources, resId);
            this.freeSeatIcon = BitmapManager.changeBitmapSize(seatBitmap, 30, 30);
            return this;
        }

        public Builder setSpecialSeatIcon(@DrawableRes int resId) {
            Bitmap seatBitmap = BitmapManager.convertDrawableToBitmap(mResources, resId);
            this.specialSeatIcon = BitmapManager.changeBitmapSize(seatBitmap, 30, 30);
            return this;
        }

        public Builder setBusySeatIcon(@DrawableRes int resId) {
            Bitmap seatBitmap = BitmapManager.convertDrawableToBitmap(mResources, resId);
            this.busySeatIcon = BitmapManager.changeBitmapSize(seatBitmap, 30, 30);
            return this;
        }

        public Builder setChosenSeatIcon(@DrawableRes int resId) {
            Bitmap seatBitmap = BitmapManager.convertDrawableToBitmap(mResources, resId);
            this.chosenSeatIcon = BitmapManager.changeBitmapSize(seatBitmap, 30, 30);
            return this;
        }

        public Builder setHallIcon(@DrawableRes int resId, int desiredSize) {
            Bitmap seatBitmap = BitmapManager.convertDrawableToBitmap(mResources, resId);
            this.hallIcon = BitmapManager.changeBitmapSize(seatBitmap, desiredSize, desiredSize);
            return this;
        }

        public Builder setMaxSelectedTickets(int maxTickets) {
            this.maxTickets = maxTickets;
            return this;
        }

        public Builder setSeatGap(int seatGap) {
            this.seatGap = seatGap;
            return this;
        }

        public RoomScheme build() {
            checkNotNull();
            RoomScheme scheme = new RoomScheme(mImage, mSeats);
            scheme.freeSeatIcon = this.freeSeatIcon;
            scheme.specialSeatIcon = this.specialSeatIcon;
            scheme.busySeatIcon = this.busySeatIcon;
            scheme.chosenSeatIcon = this.chosenSeatIcon;
            scheme.hallIcon = this.hallIcon;
            scheme.drawHall(mContext);
            if (maxTickets >= 0) {
                scheme.maxSelectedSeats = maxTickets;
            }
            if (seatGap > 0) {
                scheme.seatGap = seatGap;
            }
            return scheme;
        }

        private void checkNotNull() {
            if (freeSeatIcon == null || busySeatIcon == null || chosenSeatIcon == null || hallIcon == null || specialSeatIcon == null) {
                throw new IllegalArgumentException("Seat icons must be set before calling build method.");
            }
        }
    }

}
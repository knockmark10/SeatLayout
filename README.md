

SeatLayout 
====================  
  
[![](https://jitpack.io/v/knockmark10/SeatLayout.svg)](https://jitpack.io/#knockmark10/SeatLayout)
  
### Description  
  
SeatLayout is a lightweight simple library for creating seat maps. Very easy to use and fully customizable.
  
### Integration  
  
**1)** Add it in your root ``build.gradle`` at the end of repositories:
  
```groovy  
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```  

**2)** Add the dependency
```groovy  
dependencies {
	        implementation 'com.github.knockmark10:SeatLayout:1.0.1-beta1'
	}
```  
  
**3)** Add ``library.knockmark.com.library.widget.SeatLayoutView`` to your layout XML file. Content is automatically centered within free space.  
  
```xml  
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"  
  xmlns:tools="http://schemas.android.com/tools"  
  android:layout_width="match_parent"  
  android:layout_height="match_parent"  
  tools:context=".MainActivity">  
  
 <library.knockmark.com.library.widget.SeatLayoutView  
  android:id="@+id/seat_map"  
  android:layout_width="match_parent"  
  android:layout_height="match_parent" />  
  
</android.support.constraint.ConstraintLayout> 
```  
  
**4)** Customize `Seat` interface:  
  
```  
public class CustomSeat implements Seat {  
  
    public int id;  
    public String marker;  
    public HallScheme.SeatStatus status;  
  
    @Override  
    public int id() {  
        return id;  
    }  
   
    @Override  
    public String marker() {  
        return marker;  
    }  
  
    @Override  
    public HallScheme.SeatStatus status() {  
        return status;  
    }  
  
    @Override  
    public void setStatus(HallScheme.SeatStatus status) {  
        this.status = status;  
    }  
  
}  
```  
  
**5)** Create a 2 dimension array for displaying the seats.
  
```java
Seat seats[][] = new Seat[10][10];  
```  

**6)** Display the seats on the `SeatLayoutView`
```java
Seat seats[][] = new Seat[10][10];  
SeatLayoutView view = (SeatLayoutView) findViewById(R.id.seat_map);
RoomScheme scheme = new RoomScheme.Builder(mContext, view, seats)
//Set the icon to display a free seat
.setFreeSeatIcon(R.drawable.ic_flight_seat_free) 
//Set the icon to display the seat you have selected
.setChosenSeatIcon(R.drawable.ic_flight_seat_chosen)  
//Set the icon to display an ocupied seat
.setBusySeatIcon(R.drawable.ic_flight_seat_busy)  
//Set the icon to display a representation of the hall
.setHallIcon(R.drawable.ic_hall_icon, desiredSize)  
//Set the max tickets allowed to select
.setMaxSelectedTickets(10)
//Set the space between seats
.setSeatGap(20)
```  
  
**6)** Set `SeatListener` to `RoomScheme` to handle click events on seats:  
  
```java
scheme.setSeatListener(new SeatListener() {  
  
            @Override  
            public void selectSeat(int id) {  
                //The seat you have selected
            }  
  
            @Override  
            public void unSelectSeat(int id) {  
                //The unselected seat
            }  
  
        });  
```  
  
### Seat interface  
To use the library you should implement custom `Seat`. It has the following methods:  
  
**a)** `int id()` should return current `id` for current `Seat`. It will also be later returned when you attach `SeatListener` for scheme.  
  
**b)** `String marker()` should return text representation for empty `Seat`. For example you can use it to see row and column.  
  
**c)** `String selectedSeat();` should return `Seat` number when the `Seat` is checked.  
  
**d)** `HallScheme.SeatStatus status();` should return current state of the `Seat`. The following statuses are supported:  
  
+ `FREE` - seat is free and can be checked;  
+ `CHOSEN` - seat is chosen and can be unchecked;  
+ `BUSY` - seat cannot be used;  
+ `INFO` - the seat is empty, but you can use to show some text there;  
+ `EMPTY` - the seat is empty.  
+ `SPECIAL` - the seat is special and can be checked.  
+ `HALL` - the seat is treated as hall.     

**e)** `void setStatus(HallScheme.SeatStatus status);` update current `Seat` status.   
  
### Features:  
  
**a)** Zoomable image.  
  
Zooming can be enabled/disabled either from code:  
  
`imageView.setZoomByDoubleTap(false);`  
  
**b)** Fling while touching image.  
  
**c)** Smooth animation available either on double tap or on gesture move.  
  
**d)** Showing scheme scene from any of 4 sides.  
  
`hallScheme.setScenePosition(ScenePosition.NORTH);`  
  
+ `NORTH`  
+ `SOUTH`  
+ `EAST`  
+ `WEST`  
  
**e)** Adding text to show row or hall numbers.  
  
**f)** Handling clicking on seats on your scheme.  
  
**g)** Full customization for drawing:  
  
+ Setting background color for scheme:  
  
    ```java  
    scheme.setBackgroundColor(Color.RED);  
    ```  

+ Setting color for `INFO` seats:  
  
    ```java  
    scheme.setMarkerColor(Color.RED);  
    ```  
+ Setting text color for Scene:  
  
    ```java  
    scheme.setScenePaintColor(Color.RED);  
    ```  
+ Setting background color for Scene:  
  
    ```java  
    scheme.setSceneBackgroundColor(Color.RED);  
    ```  
+ Setting custom typeface for Scene:  
  
    ```java  
    scheme.setTypeface(typeface);  
    ```  
  
**h)** Setting custom name for scene: 
 ```java  
    scheme.setSceneName("Custom name");    
```   
     
 
**i)** Setting limit of checked seats and set listener for this event:  
```java
 scheme.setMaxSelectedSeats(4); 
 scheme.setMaxSeatsClickListener(new MaxSeatsClickListener() {
  @Override public void maxSeatsReached(int id) {
   /*
   * Do something. By default it is unlimitted. 
   * So user can check as many seats as he wants
   */
   } 
   });  .  
```   
**j)** Programmatically click on scheme:  
```java 
scheme.clickSchemeProgrammatically(3, 4);
```  
So if seat was checked it becomes unchecked and `Seatlistener` will be notified.  
If seat was unchecked and limit is not reached seat becomes checked and `SeatListener` will be notified, otherway(when limit reached) seat will not be checked and `MaxSeatsListener` will be notified.  
  
### License  
  
```  
The MIT License (MIT)  
  
Copyright (c) 2019 Marco Chavez
  
Permission is hereby granted, free of charge, to any person obtaining a copy  
of this software and associated documentation files (the "Software"), to deal  
in the Software without restriction, including without limitation the rights  
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
copies of the Software, and to permit persons to whom the Software is  
furnished to do so, subject to the following conditions:  
  
The above copyright notice and this permission notice shall be included in all  
copies or substantial portions of the Software.  
  
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER  
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  
SOFTWARE.  
```

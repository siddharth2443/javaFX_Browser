
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;

/**
 * A basic multi-window web browser.  This class is responsible for
 * creating new windows and for maintaining a list of currently open
 * windows.  The program ends when all windows have been closed.
 * The windows are of type sid_BrowserWindow.  The program also requires
 * the class SimpleDialogs.  The first window, which opens when the
 * program starts, goes to "https://www.core2web.in/privacypolicy.html.
 */
public class sid_WebBrowser extends Application {

    public static void main(String[] sid_args) {
        launch(sid_args);
    }
    //----------------------------------------------------------------------------------------------------
    
    private ArrayList<sid_BrowserWindow> sid_openWindows;  // list of currently open web browser windows
    private Rectangle2D sid_screenRect;                // usable area of the primary screen
    private double sid_locationX, sid_locationY;           // location for next window to be opened
    private double sid_windowWidth, sid_windowHeight;      // window size, computed from sid_screenRect
    private int sid_untitledCount;                     // how many "Untitled" window titles have been used
    
    
    /* Opens a window that will load the sid_URL https://...........
     * (the front page of the textbook in which this program is an example).
     * Note that the Stage parameter to this method is never used.
     */
    public void start(Stage stage) {
        
        sid_openWindows = new ArrayList<sid_BrowserWindow>();  // List of open windows.
        
        sid_screenRect = Screen.getPrimary().getVisualBounds();
        
           // (sid_locationX,sid_locationY) will be the location of the upper left
           // corner of the next window to be opened.  For the first window,
           // the window is moved a little down and over from the top-left
           // corner of the primary screen's visible bounds.
        sid_locationX = sid_screenRect.getMinX() + 30;
        sid_locationY = sid_screenRect.getMinY() + 20;
        
           // The window size depends on the height and width of the screen's
           // visual bounds, allowing some extra space so that it will be
           // possible to stack several windows, each displaced from the
           // previous one.  (For aesthetic reasons, limit the width to be
           // at most 1.6 times the height.)
        sid_windowHeight = sid_screenRect.getHeight() - 160;
        sid_windowWidth = sid_screenRect.getWidth() - 130;
        if (sid_windowWidth > sid_windowHeight*1.6)
            sid_windowWidth = sid_windowHeight*1.6;
        
           // Open the first window, showing the front page of this textbook.
        sid_newBrowserWindow("https://fancy-donut-8bec8d.netlify.app");

    } // end start()
    
    /**
     * Get the list of currently open windows.  The browser windows use this
     * list to construct their Window menus.
     * A package-private method that is meant for use only in sid_BrowserWindow.java.
     */
    ArrayList<sid_BrowserWindow> getOpenWindowList() {
        return sid_openWindows;
    }
    
    /**
     * Get the number of window titles of the form "Untitled XX" that have been
     * used.  A new window that is opened with a null sid_URL gets a title of
     * that form.  This method is also used in sid_BrowserWindow to provide a
     * title for any web page that does not itself provide a title for the page.
     * A package-private method that is meant for use only in sid_BrowserWindow.java.
     */
    int sid_getNextUntitledCount() {
        return ++sid_untitledCount;
    }
    
    /**
     * Open a new browser window.  If sid_url is non-null, the window will load that sid_URL.
     * A package-private method that is meant for use only in sid_BrowserWindow.java.
     * This method manages the locations for newly opened windows.  After a window
     * opens, the next window will be offset by 30 pixels horizontally and by 20
     * pixels vertically from the location of this window; but if that makes the
     * window extend outside sid_screenRect, the horizontal or vertical position will
     * be reset to its minimal value.
     */
    void sid_newBrowserWindow(String sid_url) {
        sid_BrowserWindow window = new sid_BrowserWindow(this,sid_url);
        sid_openWindows.add(window);   // Add new window to open window list.
        window.setOnHidden( e -> {
                // Called when the window has closed.  Remove the window
                // from the list of open windows.
            sid_openWindows.remove( window );
            System.out.println("Number of open windows is " + sid_openWindows.size());
            if (sid_openWindows.size() == 0) {
                // Program ends automatically when all windows have been closed.
                System.out.println("Program will end because all windows have been closed");
            }
        });
        if (sid_url == null) {
            window.setTitle("sid_Untitled " + sid_getNextUntitledCount());
        }
        window.setX(sid_locationX);         // set location and size of the window
        window.setY(sid_locationY);
        window.setWidth(sid_windowWidth);
        window.setHeight(sid_windowHeight);
        window.show();
        sid_locationX += 30;    // set up location of NEXT window
        sid_locationY += 20;
        if (sid_locationX + sid_windowWidth + 10 > sid_screenRect.getMaxX()) {
                // Window would extend past the right edge of the screen,
                // so reset sid_locationX to its original value.
            sid_locationX = sid_screenRect.getMinX() + 30;
        }
        if (sid_locationY + sid_windowHeight + 10 > sid_screenRect.getMaxY()) {
                // Window would extend past the bottom edge of the screen,
                // so reset sid_locationY to its original value.
            sid_locationY = sid_screenRect.getMinY() + 20;
        }
    }
    
    
} // end WebBrowser
package usp.each.si.ach2006.codesport;

import com.google.android.gms.maps.model.Marker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import usp.each.si.ach2006.codesport.models.event.Event;
import usp.each.si.ach2006.codesport.models.user.User;

import static org.junit.Assert.assertTrue;

/**
 * Created by caioa_000 on 19/11/2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class EventTest {


    @Before
    public void setUp() throws Exception {
        // setup
    }

    @Test
    public void TestEvent() throws AssertionError{

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        User user = new User("1", "Caio", "Alves", "caio.margutti.alves@gmail.com", new Date(), 21, "male");
        Event event = new Event(dateFormat.format(date).toString(), user);

        assertTrue(testBatch(event));
    }

    @Test
    public boolean testBatch(Event Event)throws AssertionError{
        testGetOwner(Event);
        testGetMarker(Event);
        testGetDate(Event);
        testGetDescription(Event);;
        return true;
    }

    @Test
    public void testGetOwner(Event event) throws AssertionError {
        assertTrue(event.getOwner() instanceof User);
    }

    @Test
    public void testGetDate(Event event) throws AssertionError {
        assertTrue(event.getDate() instanceof String);
    }

    @Test
    public void testGetMarker(Event event) throws AssertionError {
        assertTrue(event.getMarker() instanceof Marker);
    }

    @Test
    public void testGetDescription(Event event) throws AssertionError {
            assertTrue(event.getDescription() instanceof String);
    }


}
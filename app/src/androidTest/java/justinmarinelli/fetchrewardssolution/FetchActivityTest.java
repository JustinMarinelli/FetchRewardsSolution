package justinmarinelli.fetchrewardssolution;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class FetchActivityTest {

    // The activity we're testing
    private FetchActivity mActivity = null;
    // A list of each datum's listId
    private List<Integer> mListIds = new ArrayList<Integer>();
    // A list of each datum's name
    private List<String> mNames = new ArrayList<String>();

    @Rule
    public ActivityTestRule<FetchActivity> mActivityTestRule = new ActivityTestRule<FetchActivity>(FetchActivity.class);

    @Before
    public void setUp() throws Exception
    {
        mActivity = mActivityTestRule.getActivity();
        Button fetchButton = (Button)mActivity.findViewById(R.id.fetchButton);
        onView(withId(R.id.fetchButton))            // withId(R.id.my_view) is a ViewMatcher
                .perform(click());

        LinearLayout dataView = mActivity.findViewById(R.id.dataContainer);

        int childCount = dataView.getChildCount();
        for(int i = 0; i < childCount; i++) {
            ViewGroup row = (ViewGroup)dataView.getChildAt(i);

            // Assert each row has 3 children
            assert row.getChildCount() == 3;

            TextView listIdView = (TextView)row.getChildAt(1);
            TextView nameView = (TextView)row.getChildAt(2);

            mListIds.add(Integer.parseInt(listIdView.getText().toString()));
            mNames.add(nameView.getText().toString());
        }
    }

    /**
     * Test if the listIds are in sorted order
     */
    @Test
    public void listIdSortTest()
    {
        for (int i = 0; i < mListIds.size() - 1; i++)
        {
            int cur = mListIds.get(i);
            int next = mListIds.get(i + 1);
            assertTrue(cur <= next);
        }
    }

    /**
     * Test that the names are in sorted order
     */
    @Test
    public void nameSortTest()
    {
        for (int i = 0; i < mNames.size() - 1; i++)
        {
            if (mListIds.get(i) < mListIds.get(i + 1))
            {
                // If the list ID is different, we don't want to compare the
                // names, as sorting by name is the second criteria in our sort
                continue;
            }
            int cur = Integer.parseInt(mNames.get(i).substring(4).trim());
            int next = Integer.parseInt(mNames.get(i + 1).substring(4).trim());
            assertTrue(cur <= next);
        }
    }

    @After
    public void tearDown() throws Exception
    {
        mActivity = null;
    }

}
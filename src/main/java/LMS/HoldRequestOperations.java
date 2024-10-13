package LMS;

import java.util.ArrayList;

/**
 * The HoldRequestOperations class provides methods to manage hold requests in a library management system.
 * It allows adding and removing hold requests from a list.
 */
public class HoldRequestOperations {

    static ArrayList<HoldRequest> holdRequests;

    public HoldRequestOperations()
    {
        holdRequests= new ArrayList<>();
    }

    /** Adding a hold request. */
    public void addHoldRequest(HoldRequest holdRequest)
    {
        this.holdRequests.add(holdRequest);
    }

    /** Removing a hold request. */
    public void removeHoldRequest()
    {
        if(!holdRequests.isEmpty())
        {
            holdRequests.remove(0);
        }
    }
}

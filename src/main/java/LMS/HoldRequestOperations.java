package LMS;

import java.util.ArrayList;

/**
 * The HoldRequestOperations class provides methods to manage hold requests in a library management system.
 * It allows adding and removing hold requests from a list.
 */
public class HoldRequestOperations {

    private static ArrayList<HoldRequest> holdRequests;

    public HoldRequestOperations()
    {
        holdRequests = new ArrayList<>();
    }

    /** Adding a hold request. */
    public void addHoldRequest(HoldRequest holdRequest)
    {
        holdRequests.add(holdRequest);
    }

    /** Removing a hold request. */
    public void removeHoldRequest()
    {
        if(!holdRequests.isEmpty())
        {
            holdRequests.remove(0);
        }
    }

    /**
     * Retrieves a list of all hold requests.
     *
     * @return a list containing all hold requests.
     */
    public ArrayList<HoldRequest> getHoldRequests() {
        return new ArrayList<>(holdRequests);
    }
}

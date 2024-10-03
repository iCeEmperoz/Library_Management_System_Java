package LMS;

import java.util.ArrayList;

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

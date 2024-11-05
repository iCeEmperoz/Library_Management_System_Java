package LMS;

import java.util.ArrayList;

/**
 * The HoldRequestOperations class provides methods to manage hold requests in a library management system.
 * It allows adding and removing hold requests from a list.
 */
public class HoldRequestOperations {

    private static ArrayList<HoldRequest> holdRequests;

    public HoldRequestOperations() {
        holdRequests = new ArrayList<>();
    }

    /**
     * Adds a hold request to the list.
     *
     * @param holdRequest the hold request to be added
     */
    public void addHoldRequest(HoldRequest holdRequest) {
        holdRequests.add(holdRequest);
    }

    /**
     * Removes the first hold request from the list if the list is not empty.
     */
    public void removeHoldRequest() {
        if (!holdRequests.isEmpty()) {
            holdRequests.removeFirst();
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

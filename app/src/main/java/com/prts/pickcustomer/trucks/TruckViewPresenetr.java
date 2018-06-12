package com.prts.pickcustomer.trucks;


/**
 * Created by LOGICON on 19-12-2017.
 */

public interface TruckViewPresenetr {


    void getTrucksFromServer();

    void getSelectedRateCard(int lookupID, int lookupID1);

    void getTripEstaimate(TripEstimate tripEstimate);
}

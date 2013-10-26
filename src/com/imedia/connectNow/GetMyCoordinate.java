/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imedia.connectNow;

import com.codename1.io.Log;
import com.codename1.io.Storage;
import com.codename1.location.Location;
import com.codename1.location.LocationListener;
import com.codename1.location.LocationManager;
import java.io.IOException;

/**
 *
 * @author agada
 */
public class GetMyCoordinate extends Thread {

    @Override
    public void run() {
        Location loc = null;
        LocationManager l = LocationManager.getLocationManager();
        
        if (l.getStatus() != LocationManager.OUT_OF_SERVICE) {
            for (int i = 0; i < 5; i++) {
                try {
                    sleep(0);
                    
                    try {
                        loc = l.getCurrentLocation();
                        sleep(0);
                    } catch (IOException ex) {
                       Log.e(ex);
                        System.out.println("ERROR");
                    }

                    l.setLocationListener(new LocationListener() {
                        public void locationUpdated(Location loc) {
                            if (Math.abs(loc.getLongitude()) > 0) {
                                Storage.getInstance().writeObject("Altitude", loc.getAltitude());
                                Storage.getInstance().writeObject("Latitude", loc.getLatitude());
                                Storage.getInstance().writeObject("Longitude", loc.getLongitude());

                                System.out.println(Storage.getInstance().readObject("Altitude").toString());
                                System.out.println(Storage.getInstance().readObject("Latitude").toString());
                                System.out.println(Storage.getInstance().readObject("Longitude").toString());
                                System.out.println("UPDATE ");
                            }


                        }

                        public void providerStateChanged(int newState) {
                        }
                    });
                } catch (InterruptedException e) {
                }
            }
        }      
    }
}

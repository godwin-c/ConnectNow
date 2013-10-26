/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imedia.connectNow;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author agada
 */
public class SetMyLocation implements Runnable {

    MyLocation myLocation;
    String status;
    private double latt;
    private double longg;

    /**
     * @return the latt
     */
    public double getLatt() {
        return latt;
    }

    /**
     * @param latt the latt to set
     */
    public void setLatt(double latt) {
        this.latt = latt;
    }

    /**
     * @return the longg
     */
    public double getLongg() {
        return longg;
    }

    /**
     * @param longg the longg to set
     */
    public void setLongg(double longg) {
        this.longg = longg;
    }
    Thread runner;

    public SetMyLocation(double lat, double lon) {
        this.latt = lat;
        this.longg = lon;
    }

    public SetMyLocation(String threadName) {
        runner = new Thread(this, threadName);
        runner.start();
    }

    public void run() {
        

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        final ConnectionRequest request = new ConnectionRequest() {
            
            @Override
            protected void readResponse(InputStream input) throws IOException {

                JSONParser p = new JSONParser();
                InputStreamReader inp = new InputStreamReader(input);
                Hashtable h = p.parse(inp);
                // System.out.println("Result : "+h.toString());

                status = (String) h.get("status");
                System.out.println(" THE STATUS " + status);
                final Vector v = (Vector) h.get("results");

                for (int i = 0; i < v.size(); i++) {
                    Hashtable entry = (Hashtable) v.elementAt(0);
                    Hashtable geo = (Hashtable) entry.get("geometry");

                    Vector add = (Vector) entry.get("address_components");

                    Hashtable areas = new Hashtable();

                    System.out.println("Element at 0 : " + add);
                    for (int j = 0; j < add.size(); j++) {
                        Hashtable hash = (Hashtable) add.elementAt(j);
                        System.out.println("the hash :::::::::::::::::::::::" + hash);
                        if (((Vector) hash.get("types")).contains("route")) {
                            //if ("route".equals(((Vector) hash.get("types")).elementAt(0))) {
                            System.out.println("+++++++ Route : " + hash.get("long_name"));
                            //rout = .toString();
                            areas.put("route", hash.get("long_name"));
                        }

                        if (((Vector) hash.get("types")).contains("sublocality")) {
                            System.out.println("+++++++ Sub locality : " + hash.get("long_name"));
                            //sublocality = .toString();
                            areas.put("sublocality", hash.get("long_name"));
                        }


                    }
                    if (!areas.isEmpty()) {
                        String rout = null;
                        String sublocality;

                        if (areas.get("route").toString() != null) {
                            rout = areas.get("route").toString();
                        }
                        if (areas.get("sublocality").toString() != null) {
                            sublocality = areas.get("sublocality").toString();
                        } else {
                            sublocality = rout;
                        }
//                       
                        Storage.getInstance().writeObject("MySetLocation", areas);
                        //myLocation = new MyLocation(rout, sublocality);
                    }
                    System.out.println("My Area : " + areas);
                }


            }
        };


        final NetworkManager manager = NetworkManager.getInstance();
        
        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + this.getLatt() + "," + this.getLongg() + "&sensor=false";
        request.setUrl(url);
        request.setContentType("application/json");

        request.setFailSilently(true);
        request.setPost(false);
        request.setDuplicateSupported(true);

        manager.start();
        manager.addToQueueAndWait(request);
    }
}

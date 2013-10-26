/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imedia.connectNow;

/**
 *
 * @author agada
 */
public class MyLocation {
    private String route;
    //private String locality;
    private String sublocality;
    

    /**
     * @return the route
     */
    public String getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(String route) {
        this.route = route;
    }

//    /**
//     * @return the locality
//     */
//    public String getLocality() {
//        return locality;
//    }
//
//    /**
//     * @param locality the locality to set
//     */
//    public void setLocality(String locality) {
//        this.locality = locality;
//    }

    /**
     * @return the sublocality
     */
    public String getSublocality() {
        return sublocality;
    }

    /**
     * @param sublocality the sublocality to set
     */
    public void setSublocality(String sublocality) {
        this.sublocality = sublocality;
    }

   
    
    public MyLocation(String route, String sublocality){
        
        this.route = route;
        this.sublocality = sublocality;
        //this.locality = locality;     
    }
}

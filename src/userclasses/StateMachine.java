/**
 * Your application code goes here
 */
package userclasses;

import com.codename1.location.Location;
import com.codename1.location.LocationListener;
import com.codename1.location.LocationManager;
import com.codename1.components.InfiniteProgress;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.maps.Coord;
import com.codename1.maps.MapComponent;
import com.codename1.maps.layers.PointLayer;
import com.codename1.maps.layers.PointsLayer;
import com.codename1.maps.providers.GoogleMapsProvider;
import com.codename1.processing.Result;
import generated.StateMachineBase;
import com.codename1.ui.*;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.util.Resources;
import com.imedia.connectNow.MyLocation;
import com.imedia.connectNow.SetMyLocation;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author Agada Godwin C.
 */
public class StateMachine extends StateMachineBase {

    double longitude;
    double latitude;
    float direction;
    String myKey = "AIzaSyB7t94-6U-G2PTWC_czQ-LwsNMbUaTqmO4";
    private boolean locAvaillable;
    private String myAppId;
    private String resApiKey;
    private String category;
    private Vector<Hashtable> services;
    private String status;
    MyLocation myLocation;
    private String thisplace;

    public StateMachine(String resFile) {
        super(resFile);
        // do not modify, write code in initVars and initialize class members there,
        // the constructor might be invoked too late due to race conditions that might occur
    }

    /**
     * this method should be used to initialize variables instead of the
     * constructor/class scope to avoid race conditions
     */
    @Override
    protected void initVars(Resources res) {
        myAppId = "49CeCWHYjMK2Zf1X4Ty3L487zrESR9jgeGPsl41J";
        resApiKey = "ZUNqpcMadbwqhUPQW9VEHQoVeVa0Ceg3kNHnvWYS";
    }

    private void getMyPosition() {

//        InfiniteProgress ip = new InfiniteProgress();
//        final Dialog dlg = ip.showInifiniteBlocking();
        LocationManager l = LocationManager.getLocationManager();
        if (l.getStatus() != LocationManager.OUT_OF_SERVICE) {
            locAvaillable = true;
            l.setLocationListener(new LocationListener() {
                public void locationUpdated(Location location) {
//                    if (Display.getInstance().getCurrent() == dlg) {
//                        dlg.dispose();
//                    }
                    System.out.println("My Location : " + location.getLongitude() + " and " + location.getLatitude());
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    //direction = location.getDirection();

                }

                public void providerStateChanged(int newState) {
//                    if (Display.getInstance().getCurrent() == dlg) {
//                        dlg.dispose();
//                    }
                    System.out.println("New State : " + newState);
                    //yourCustomPositionUpdateMethod();
                }
            });
        } else {
            locAvaillable = false;
        }

    }

    private void searchFor(String city, String categry) {

        final ConnectionRequest request = new ConnectionRequest() {
            // **************** Get the status of the connection        
            @Override
            protected void readHeaders(Object connection) throws IOException {

                status = getHeader(connection, "status");

                // System.out.println("The status of the connection: " + status);
            }
            //*****************

            @Override
            protected void readResponse(InputStream input) throws IOException {

                JSONParser p = new JSONParser();
                InputStreamReader inp = new InputStreamReader(input);
                Hashtable h = p.parse(inp);

                services = (Vector) h.get("results");
                //System.out.println(membersList.toString());

            }
        };


        Hashtable forCity = new Hashtable();
        forCity.put("$options", "im");
        forCity.put("$regex", city);

        Hashtable forCategory = new Hashtable();
        forCategory.put("$options", "im");
        forCategory.put("$regex", categry);

        Hashtable forAll = new Hashtable();
        forAll.put("City", forCity);
        forAll.put("Category", forCategory);
        //forAll.put("registeration_code", forRegCode);

        String json = Result.fromContent(forAll).toString();

        //System.out.println(json);
        final NetworkManager manager = NetworkManager.getInstance();
        Command cancel = new Command("Cancel") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                manager.killAndWait(request);
                //do Option2
            }
        };

        InfiniteProgress ip = new InfiniteProgress();
        Dialog d = new Dialog();
        d.setDialogUIID("Label");
        d.setLayout(new BorderLayout());
        Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Label l = new Label("searching for " + categry + " at " + city);
        l.getStyle().getBgTransparency();
        cnt.addComponent(l);
        cnt.addComponent(ip);
        d.addComponent(BorderLayout.CENTER, cnt);
        d.setTransitionInAnimator(CommonTransitions.createEmpty());
        d.setTransitionOutAnimator(CommonTransitions.createEmpty());
        d.showPacked(BorderLayout.CENTER, false);
        d.setBackCommand(cancel);

        String url = "https://api.parse.com/1/classes/Info_Dir";
        request.setUrl(url);
        request.setContentType("application/json");
        request.addRequestHeader("X-Parse-Application-Id", myAppId);
        request.addRequestHeader("X-Parse-REST-API-Key", resApiKey);

        request.setFailSilently(true);//stops user from seeing error mess//String searchText = findSearchText(c.getComponentForm()).getText();age on failure
        request.setPost(false);
        request.addArgument("where", json);
        request.addArgument("limit", "10");
        request.setDuplicateSupported(true);
        //request.setDisposeOnCompletion(progress);
        request.setDisposeOnCompletion(d);

        manager.start();
        manager.addToQueueAndWait(request);
    }

    private void getMyLocation(double lat, double lon) {
        final ConnectionRequest request = new ConnectionRequest() {
            // **************** Get the status of the connection        
            @Override
            protected void readHeaders(Object connection) throws IOException {

                status = getHeader(connection, "status");

                // System.out.println("The status of the connection: " + status);
            }
            //*****************

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
                        myLocation = new MyLocation(rout, sublocality);
                    }
                    System.out.println("My Area : " + areas);
                }


            }
        };


        final NetworkManager manager = NetworkManager.getInstance();
        Command cancel = new Command("Cancel") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                manager.killAndWait(request);
                //do Option2
            }
        };

        InfiniteProgress ip = new InfiniteProgress();
        Dialog d = new Dialog();
        d.setDialogUIID("Label");
        d.setLayout(new BorderLayout());
        Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Label l = new Label("searching for location");
        l.getStyle().getBgTransparency();
        cnt.addComponent(l);
        cnt.addComponent(ip);
        d.addComponent(BorderLayout.CENTER, cnt);
        d.setTransitionInAnimator(CommonTransitions.createEmpty());
        d.setTransitionOutAnimator(CommonTransitions.createEmpty());
        d.showPacked(BorderLayout.CENTER, false);
        d.setBackCommand(cancel);
        //and 
        //String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + 6.42335453 + "," + 3.47641147 + "&sensor=false";
        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "&sensor=false";
        request.setUrl(url);
        request.setContentType("application/json");

        request.setFailSilently(true);
        request.setPost(false);
        request.setDuplicateSupported(true);
        request.setDisposeOnCompletion(d);

        manager.start();
        manager.addToQueueAndWait(request);
    }

    @Override
    protected boolean processBackground(Form f) {

        super.processBackground(f);
        System.out.println("llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll");
        System.out.println("Name :" + f.getName());
        if ("SplashScreen".equals(f.getName())) {
            System.out.println("llllllllllkkkkkkkkkkkkkkkkkkkkkkkkkkllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll");
            System.out.println("Name :" + f.getName());
            // getMyPosition();
            if ((Storage.getInstance().exists("Longitude")) && (Storage.getInstance().exists("Latitude"))) {
                //Double text1 = 
                latitude = (Double)Storage.getInstance().readObject("Latitude");
                //String text2 = 
                longitude = (Double) Storage.getInstance().readObject("Latitude");
                //getMyLocation(latitude, longitude);
                Thread thread = new Thread(new SetMyLocation(latitude, longitude), "backgroungthread");
                thread.start();
//                
            }
        }
        return true;
    }

    @Override
    protected void onMain_ATMButtonAction(Component c, ActionEvent event) {
        //showForm("MyLocation", null);
        category = "atm";
        //final String myPresentLoc = null;
        // getMyPosition();
        System.out.println("My location for ATM :"+myLocation.getRoute());
        if (locAvaillable == true) {
            if ((latitude == 0.0) && (longitude == 0.0)) {
                if (Storage.getInstance().exists("MyPresentLocation")) {
                    try {
                        thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                        //myPresentLoc = "Nigeria";
                        thisplace = null;
                    } finally {
                        if (thisplace != null) {

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //do Option2
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText("is your present location " + thisplace + "?");
                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Confirm", text, cmds);

                        } else {
                            showForm("EnterCity", null);
                        }

                    }

                } else {
                    showForm("EnterCity", null);
                }

            } else {
                getMyLocation(latitude, longitude);
                if ((status != null) && ("OK".equals(status))) {

                    Label l = findWhereIamLabel(c.getComponentForm());
                    l.setText("You are at " + myLocation.getRoute());
                    c.getComponentForm().revalidate();

                    searchFor(myLocation.getRoute(), category);
                    if ((status != null) && ("200 OK".equals(status))) {
                        if (!services.isEmpty()) {
                            showForm("FetchedServices", null);
                        } else {
// The first branching that will be called many times.

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
//                            if (myLocation.getRoute().equals(myLocation.getSublocality())) {
//                                if (myLocation.getSublocality().equals(myLocation.getLocality())) {
////                                    if (myLocation.getLocality().equals(myLocation.getAdminLevel2())) {
////                                        if (myLocation.getAdminLevel2().equals(myLocation.getAdminLevel1())) {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getCountry() + "?");
////                                            thisplace = myLocation.getCountry();
////
////                                        } else {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getAdminLevel1() + "?");
////                                            thisplace = myLocation.getAdminLevel1();
////                                        }
////                                    } else {
////                                        text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                + "would you like to check " + myLocation.getAdminLevel2() + "?");
////                                        thisplace = myLocation.getAdminLevel2();
////                                    }
//                                } else {
//                                    text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
//                                            + "would you like to check " + myLocation.getLocality() + "?");
//                                    thisplace = myLocation.getLocality();
//                                }
//                            } else {
//                            }
                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
                                    + "would you like to check " + myLocation.getSublocality() + "?");
                            thisplace = myLocation.getSublocality();
                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Oh dear", text, cmds);

                            //, "OK", null);
                        }
                    } else {
                        Dialog.show("Oh dear", "you may have to check your network connection", "OK", null);
                    }
                } else {
                    showForm("EnterCity", null);
                }
            }
        } else {
            if (Storage.getInstance().exists("MyPresentLocation")) {
                try {
                    thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                } catch (Exception e) {
                    Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                    //myPresentLoc = "Nigeria";
                    thisplace = null;
                } finally {
                    if (thisplace != null) {

                        Command[] cmds = new Command[2];
                        cmds[0] = new Command("yes") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                //do Option2
                                searchFor(thisplace, category);
                                if ((status != null) && ("200 OK".equals(status))) {
                                    if (!services.isEmpty()) {
                                        showForm("FetchedServices", null);
                                    } else {
                                        Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                    }

                                } else {
                                    Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                }
                            }
                        };
                        cmds[1] = new Command("no") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                showForm("EnterCity", null);
                            }
                        };

                        TextArea text = new TextArea();
                        text.setText("is your present location " + thisplace + "?");
                        text.setUIID("dialogLabel");
                        text.setEditable(false);
                        Dialog.show("Confirm", text, cmds);

                    } else {
                        showForm("EnterCity", null);
                    }

                }

            } else {
                showForm("EnterCity", null);
            }

        }
        //getMyLocation();
    }

    @Override
    protected void onMain_HotelButtonAction(Component c, ActionEvent event) {
        //getMyLocation(longitude, latitude);

        category = "hotel";
        //final String myPresentLoc = null;
        if (locAvaillable == true) {
            if ((latitude == 0.0) && (longitude == 0.0)) {
                if (Storage.getInstance().exists("MyPresentLocation")) {
                    try {
                        thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                        //myPresentLoc = "Nigeria";
                        thisplace = null;
                    } finally {
                        if (thisplace != null) {

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //do Option2
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText("is your present location " + thisplace + "?");
                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Confirm", text, cmds);

                        } else {
                            showForm("EnterCity", null);
                        }

                    }

                } else {
                    showForm("EnterCity", null);
                }

            } else {
                getMyLocation(latitude, longitude);
                if ((status != null) && ("OK".equals(status))) {
                    Label l = findWhereIamLabel(c.getComponentForm());
                    l.setText("You are at " + myLocation.getRoute());
                    c.getComponentForm().revalidate();
                    searchFor(myLocation.getRoute(), category);
                    if ((status != null) && ("200 OK".equals(status))) {
                        if (!services.isEmpty()) {
                            showForm("FetchedServices", null);
                        } else {
// The first branching that will be called many times.

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    // showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
//                            if (myLocation.getRoute().equals(myLocation.getSublocality())) {
//                                if (myLocation.getSublocality().equals(myLocation.getLocality())) {
////                                    if (myLocation.getLocality().equals(myLocation.getAdminLevel2())) {
////                                        if (myLocation.getAdminLevel2().equals(myLocation.getAdminLevel1())) {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getCountry() + "?");
////                                            thisplace = myLocation.getCountry();
////
////                                        } else {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getAdminLevel1() + "?");
////                                            thisplace = myLocation.getAdminLevel1();
////                                        }
////                                    } else {
////                                        text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                + "would you like to check " + myLocation.getAdminLevel2() + "?");
////                                        thisplace = myLocation.getAdminLevel2();
////                                    }
//                                } else {
//                                    text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
//                                            + "would you like to check " + myLocation.getLocality() + "?");
//                                    thisplace = myLocation.getLocality();
//                                }
//                            } else {
//                            }

                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
                                    + "would you like to check " + myLocation.getSublocality() + "?");
                            thisplace = myLocation.getSublocality();

                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Oh dear", text, cmds);

                            //, "OK", null);
                        }
                    } else {
                        Dialog.show("Oh dear", "you may have to check your network connection", "OK", null);
                    }
                } else {
                    showForm("EnterCity", null);
                }
            }
        } else {
            if (Storage.getInstance().exists("MyPresentLocation")) {
                try {
                    thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                } catch (Exception e) {
                    Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                    //myPresentLoc = "Nigeria";
                    thisplace = null;
                } finally {
                    if (thisplace != null) {

                        Command[] cmds = new Command[2];
                        cmds[0] = new Command("yes") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                //do Option2
                                searchFor(thisplace, category);
                                if ((status != null) && ("200 OK".equals(status))) {
                                    if (!services.isEmpty()) {
                                        showForm("FetchedServices", null);
                                    } else {
                                        Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                    }

                                } else {
                                    Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                }
                            }
                        };
                        cmds[1] = new Command("no") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                showForm("EnterCity", null);
                            }
                        };

                        TextArea text = new TextArea();
                        text.setText("is your present location " + thisplace + "?");
                        text.setUIID("dialogLabel");
                        text.setEditable(false);
                        Dialog.show("Confirm", text, cmds);

                    } else {
                        showForm("EnterCity", null);
                    }

                }

            } else {
                showForm("EnterCity", null);
            }

        }
    }

    @Override
    protected void onMain_RestaurantButtonAction(Component c, ActionEvent event) {
        category = "restaurant";
        //final String myPresentLoc = null;
        if (locAvaillable == true) {
            if ((latitude == 0.0) && (longitude == 0.0)) {
                if (Storage.getInstance().exists("MyPresentLocation")) {
                    try {
                        thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                        //myPresentLoc = "Nigeria";
                        thisplace = null;
                    } finally {
                        if (thisplace != null) {

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //do Option2
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText("is your present location " + thisplace + "?");
                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Confirm", text, cmds);

                        } else {
                            showForm("EnterCity", null);
                        }

                    }

                } else {
                    showForm("EnterCity", null);
                }

            } else {
                getMyLocation(latitude, longitude);
                if ((status != null) && ("OK".equals(status))) {
                    Label l = findWhereIamLabel(c.getComponentForm());
                    l.setText("You are at " + myLocation.getRoute());
                    c.getComponentForm().revalidate();
                    searchFor(myLocation.getRoute(), category);
                    if ((status != null) && ("200 OK".equals(status))) {
                        if (!services.isEmpty()) {
                            showForm("FetchedServices", null);
                        } else {
// The first branching that will be called many times.

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    // showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
//                            if (myLocation.getRoute().equals(myLocation.getSublocality())) {
//                                if (myLocation.getSublocality().equals(myLocation.getLocality())) {
////                                    if (myLocation.getLocality().equals(myLocation.getAdminLevel2())) {
////                                        if (myLocation.getAdminLevel2().equals(myLocation.getAdminLevel1())) {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getCountry() + "?");
////                                            thisplace = myLocation.getCountry();
////
////                                        } else {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getAdminLevel1() + "?");
////                                            thisplace = myLocation.getAdminLevel1();
////                                        }
////                                    } else {
////                                        text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                + "would you like to check " + myLocation.getAdminLevel2() + "?");
////                                        thisplace = myLocation.getAdminLevel2();
////                                    }
//                                } else {
//                                    text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
//                                            + "would you like to check " + myLocation.getLocality() + "?");
//                                    thisplace = myLocation.getLocality();
//                                }
//                            } else {
//                                
//                            }

                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
                                    + "would you like to check " + myLocation.getSublocality() + "?");
                            thisplace = myLocation.getSublocality();

                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Oh dear", text, cmds);

                            //, "OK", null);
                        }
                    } else {
                        Dialog.show("Oh dear", "you may have to check your network connection", "OK", null);
                    }
                } else {
                    showForm("EnterCity", null);
                }
            }
        } else {
            if (Storage.getInstance().exists("MyPresentLocation")) {
                try {
                    thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                } catch (Exception e) {
                    Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                    //myPresentLoc = "Nigeria";
                    thisplace = null;
                } finally {
                    if (thisplace != null) {

                        Command[] cmds = new Command[2];
                        cmds[0] = new Command("yes") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                //do Option2
                                searchFor(thisplace, category);
                                if ((status != null) && ("200 OK".equals(status))) {
                                    if (!services.isEmpty()) {
                                        showForm("FetchedServices", null);
                                    } else {
                                        Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                    }

                                } else {
                                    Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                }
                            }
                        };
                        cmds[1] = new Command("no") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                showForm("EnterCity", null);
                            }
                        };

                        TextArea text = new TextArea();
                        text.setText("is your present location " + thisplace + "?");
                        text.setUIID("dialogLabel");
                        text.setEditable(false);
                        Dialog.show("Confirm", text, cmds);

                    } else {
                        showForm("EnterCity", null);
                    }

                }

            } else {
                showForm("EnterCity", null);
            }

        }
    }

    @Override
    protected void onMain_ChurchButtonAction(Component c, ActionEvent event) {
        category = "church";
        //final String myPresentLoc = null;
        if (locAvaillable == true) {
            if ((latitude == 0.0) && (longitude == 0.0)) {
                if (Storage.getInstance().exists("MyPresentLocation")) {
                    try {
                        thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                        //myPresentLoc = "Nigeria";
                        thisplace = null;
                    } finally {
                        if (thisplace != null) {

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //do Option2
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText("is your present location " + thisplace + "?");
                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Confirm", text, cmds);

                        } else {
                            showForm("EnterCity", null);
                        }

                    }

                } else {
                    showForm("EnterCity", null);
                }

            } else {
                getMyLocation(latitude, longitude);
                if ((status != null) && ("OK".equals(status))) {
                    Label l = findWhereIamLabel(c.getComponentForm());
                    l.setText("You are at " + myLocation.getRoute());
                    c.getComponentForm().revalidate();
                    searchFor(myLocation.getRoute(), category);
                    if ((status != null) && ("200 OK".equals(status))) {
                        if (!services.isEmpty()) {
                            showForm("FetchedServices", null);
                        } else {
// The first branching that will be called many times.

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //  showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
//                            if (myLocation.getRoute().equals(myLocation.getSublocality())) {
//                                if (myLocation.getSublocality().equals(myLocation.getLocality())) {
////                                    if (myLocation.getLocality().equals(myLocation.getAdminLevel2())) {
////                                        if (myLocation.getAdminLevel2().equals(myLocation.getAdminLevel1())) {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getCountry() + "?");
////                                            thisplace = myLocation.getCountry();
////
////                                        } else {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getAdminLevel1() + "?");
////                                            thisplace = myLocation.getAdminLevel1();
////                                        }
////                                    } else {
////                                        text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                + "would you like to check " + myLocation.getAdminLevel2() + "?");
////                                        thisplace = myLocation.getAdminLevel2();
////                                    }
//                                } else {
//                                    text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
//                                            + "would you like to check " + myLocation.getLocality() + "?");
//                                    thisplace = myLocation.getLocality();
//                                }
//                            } else {
//                            }

                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
                                    + "would you like to check " + myLocation.getSublocality() + "?");
                            thisplace = myLocation.getSublocality();

                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Oh dear", text, cmds);

                            //, "OK", null);
                        }
                    } else {
                        Dialog.show("Oh dear", "you may have to check your network connection", "OK", null);
                    }
                } else {
                    showForm("EnterCity", null);
                }
            }
        } else {
            if (Storage.getInstance().exists("MyPresentLocation")) {
                try {
                    thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                } catch (Exception e) {
                    Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                    //myPresentLoc = "Nigeria";
                    thisplace = null;
                } finally {
                    if (thisplace != null) {

                        Command[] cmds = new Command[2];
                        cmds[0] = new Command("yes") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                //do Option2
                                searchFor(thisplace, category);
                                if ((status != null) && ("200 OK".equals(status))) {
                                    if (!services.isEmpty()) {
                                        showForm("FetchedServices", null);
                                    } else {
                                        Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                    }

                                } else {
                                    Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                }
                            }
                        };
                        cmds[1] = new Command("no") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                showForm("EnterCity", null);
                            }
                        };

                        TextArea text = new TextArea();
                        text.setText("is your present location " + thisplace + "?");
                        text.setUIID("dialogLabel");
                        text.setEditable(false);
                        Dialog.show("Confirm", text, cmds);

                    } else {
                        showForm("EnterCity", null);
                    }

                }

            } else {
                showForm("EnterCity", null);
            }

        }
    }

    @Override
    protected void onMain_MosqueButtonAction(Component c, ActionEvent event) {
        category = "mosque";
        //final String myPresentLoc = null;
        if (locAvaillable == true) {
            if ((latitude == 0.0) && (longitude == 0.0)) {
                if (Storage.getInstance().exists("MyPresentLocation")) {
                    try {
                        thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                        //myPresentLoc = "Nigeria";
                        thisplace = null;
                    } finally {
                        if (thisplace != null) {

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //do Option2
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText("is your present location " + thisplace + "?");
                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Confirm", text, cmds);

                        } else {
                            showForm("EnterCity", null);
                        }

                    }

                } else {
                    showForm("EnterCity", null);
                }

            } else {
                getMyLocation(latitude, longitude);
                if ((status != null) && ("OK".equals(status))) {
                    Label l = findWhereIamLabel(c.getComponentForm());
                    l.setText("You are at " + myLocation.getRoute());
                    c.getComponentForm().revalidate();
                    searchFor(myLocation.getRoute(), category);
                    if ((status != null) && ("200 OK".equals(status))) {
                        if (!services.isEmpty()) {
                            showForm("FetchedServices", null);
                        } else {
// The first branching that will be called many times.

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    // showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
//                            if (myLocation.getRoute().equals(myLocation.getSublocality())) {
//                                if (myLocation.getSublocality().equals(myLocation.getLocality())) {
////                                    if (myLocation.getLocality().equals(myLocation.getAdminLevel2())) {
////                                        if (myLocation.getAdminLevel2().equals(myLocation.getAdminLevel1())) {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getCountry() + "?");
////                                            thisplace = myLocation.getCountry();
////
////                                        } else {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getAdminLevel1() + "?");
////                                            thisplace = myLocation.getAdminLevel1();
////                                        }
////                                    } else {
////                                        text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                + "would you like to check " + myLocation.getAdminLevel2() + "?");
////                                        thisplace = myLocation.getAdminLevel2();
////                                    }
//                                } else {
//                                    text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
//                                            + "would you like to check " + myLocation.getLocality() + "?");
//                                    thisplace = myLocation.getLocality();
//                                }
//                            } else {
//                            }

                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
                                    + "would you like to check " + myLocation.getSublocality() + "?");
                            thisplace = myLocation.getSublocality();

                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Oh dear", text, cmds);

                            //, "OK", null);
                        }
                    } else {
                        Dialog.show("Oh dear", "you may have to check your network connection", "OK", null);
                    }
                } else {
                    showForm("EnterCity", null);
                }
            }
        } else {
            if (Storage.getInstance().exists("MyPresentLocation")) {
                try {
                    thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                } catch (Exception e) {
                    Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                    //myPresentLoc = "Nigeria";
                    thisplace = null;
                } finally {
                    if (thisplace != null) {

                        Command[] cmds = new Command[2];
                        cmds[0] = new Command("yes") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                //do Option2
                                searchFor(thisplace, category);
                                if ((status != null) && ("200 OK".equals(status))) {
                                    if (!services.isEmpty()) {
                                        showForm("FetchedServices", null);
                                    } else {
                                        Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                    }

                                } else {
                                    Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                }
                            }
                        };
                        cmds[1] = new Command("no") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                showForm("EnterCity", null);
                            }
                        };

                        TextArea text = new TextArea();
                        text.setText("is your present location " + thisplace + "?");
                        text.setUIID("dialogLabel");
                        text.setEditable(false);
                        Dialog.show("Confirm", text, cmds);

                    } else {
                        showForm("EnterCity", null);
                    }

                }

            } else {
                showForm("EnterCity", null);
            }

        }
    }

    @Override
    protected void onMain_HospitalButtonAction(Component c, ActionEvent event) {
        category = "hospital";
        //final String myPresentLoc = null;
        if (locAvaillable == true) {
            if ((latitude == 0.0) && (longitude == 0.0)) {
                if (Storage.getInstance().exists("MyPresentLocation")) {
                    try {
                        thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                        //myPresentLoc = "Nigeria";
                        thisplace = null;
                    } finally {
                        if (thisplace != null) {

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //do Option2
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText("is your present location " + thisplace + "?");
                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Confirm", text, cmds);

                        } else {
                            showForm("EnterCity", null);
                        }

                    }

                } else {
                    showForm("EnterCity", null);
                }

            } else {
                getMyLocation(latitude, longitude);
                if ((status != null) && ("OK".equals(status))) {
                    Label l = findWhereIamLabel(c.getComponentForm());
                    l.setText("You are at " + myLocation.getRoute());
                    c.getComponentForm().revalidate();
                    searchFor(myLocation.getRoute(), category);
                    if ((status != null) && ("200 OK".equals(status))) {
                        if (!services.isEmpty()) {
                            showForm("FetchedServices", null);
                        } else {
// The first branching that will be called many times.

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //  showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
//                            if (myLocation.getRoute().equals(myLocation.getSublocality())) {
//                                if (myLocation.getSublocality().equals(myLocation.getLocality())) {
////                                    if (myLocation.getLocality().equals(myLocation.getAdminLevel2())) {
////                                        if (myLocation.getAdminLevel2().equals(myLocation.getAdminLevel1())) {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getCountry() + "?");
////                                            thisplace = myLocation.getCountry();
////
////                                        } else {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getAdminLevel1() + "?");
////                                            thisplace = myLocation.getAdminLevel1();
////                                        }
////                                    } else {
////                                        text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                + "would you like to check " + myLocation.getAdminLevel2() + "?");
////                                        thisplace = myLocation.getAdminLevel2();
////                                    }
//                                } else {
//                                    text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
//                                            + "would you like to check " + myLocation.getLocality() + "?");
//                                    thisplace = myLocation.getLocality();
//                                }
//                            } else {
//                                
//                            }

                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
                                    + "would you like to check " + myLocation.getSublocality() + "?");
                            thisplace = myLocation.getSublocality();

                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Oh dear", text, cmds);

                            //, "OK", null);
                        }
                    } else {
                        Dialog.show("Oh dear", "you may have to check your network connection", "OK", null);
                    }
                } else {
                    showForm("EnterCity", null);
                }
            }
        } else {
            if (Storage.getInstance().exists("MyPresentLocation")) {
                try {
                    thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                } catch (Exception e) {
                    Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                    //myPresentLoc = "Nigeria";
                    thisplace = null;
                } finally {
                    if (thisplace != null) {

                        Command[] cmds = new Command[2];
                        cmds[0] = new Command("yes") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                //do Option2
                                searchFor(thisplace, category);
                                if ((status != null) && ("200 OK".equals(status))) {
                                    if (!services.isEmpty()) {
                                        showForm("FetchedServices", null);
                                    } else {
                                        Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                    }

                                } else {
                                    Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                }
                            }
                        };
                        cmds[1] = new Command("no") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                showForm("EnterCity", null);
                            }
                        };

                        TextArea text = new TextArea();
                        text.setText("is your present location " + thisplace + "?");
                        text.setUIID("dialogLabel");
                        text.setEditable(false);
                        Dialog.show("Confirm", text, cmds);

                    } else {
                        showForm("EnterCity", null);
                    }

                }

            } else {
                showForm("EnterCity", null);
            }

        }
    }

    @Override
    protected void onMain_FuelButtonAction(Component c, ActionEvent event) {
        category = "fuel";
        //final String myPresentLoc = null;
        if (locAvaillable == true) {
            if ((latitude == 0.0) && (longitude == 0.0)) {
                if (Storage.getInstance().exists("MyPresentLocation")) {
                    try {
                        thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                        //myPresentLoc = "Nigeria";
                        thisplace = null;
                    } finally {
                        if (thisplace != null) {

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //do Option2
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText("is your present location " + thisplace + "?");
                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Confirm", text, cmds);

                        } else {
                            showForm("EnterCity", null);
                        }

                    }

                } else {
                    showForm("EnterCity", null);
                }

            } else {
                getMyLocation(latitude, longitude);
                if ((status != null) && ("OK".equals(status))) {
                    Label l = findWhereIamLabel(c.getComponentForm());
                    l.setText("You are at " + myLocation.getRoute());
                    c.getComponentForm().revalidate();
                    searchFor(myLocation.getRoute(), category);
                    if ((status != null) && ("200 OK".equals(status))) {
                        if (!services.isEmpty()) {
                            showForm("FetchedServices", null);
                        } else {
// The first branching that will be called many times.

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //  showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
//                            if (myLocation.getRoute().equals(myLocation.getSublocality())) {
//                                if (myLocation.getSublocality().equals(myLocation.getLocality())) {
////                                    if (myLocation.getLocality().equals(myLocation.getAdminLevel2())) {
////                                        if (myLocation.getAdminLevel2().equals(myLocation.getAdminLevel1())) {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getCountry() + "?");
////                                            thisplace = myLocation.getCountry();
////
////                                        } else {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getAdminLevel1() + "?");
////                                            thisplace = myLocation.getAdminLevel1();
////                                        }
////                                    } else {
////                                        text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                + "would you like to check " + myLocation.getAdminLevel2() + "?");
////                                        thisplace = myLocation.getAdminLevel2();
////                                    }
//                                } else {
//                                    text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
//                                            + "would you like to check " + myLocation.getLocality() + "?");
//                                    thisplace = myLocation.getLocality();
//                                }
//                            } else {
//                               
//                            }

                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
                                    + "would you like to check " + myLocation.getSublocality() + "?");
                            thisplace = myLocation.getSublocality();

                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Oh dear", text, cmds);

                            //, "OK", null);
                        }
                    } else {
                        Dialog.show("Oh dear", "you may have to check your network connection", "OK", null);
                    }
                } else {
                    showForm("EnterCity", null);
                }
            }
        } else {
            if (Storage.getInstance().exists("MyPresentLocation")) {
                try {
                    thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                } catch (Exception e) {
                    Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                    //myPresentLoc = "Nigeria";
                    thisplace = null;
                } finally {
                    if (thisplace != null) {

                        Command[] cmds = new Command[2];
                        cmds[0] = new Command("yes") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                //do Option2
                                searchFor(thisplace, category);
                                if ((status != null) && ("200 OK".equals(status))) {
                                    if (!services.isEmpty()) {
                                        showForm("FetchedServices", null);
                                    } else {
                                        Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                    }

                                } else {
                                    Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                }
                            }
                        };
                        cmds[1] = new Command("no") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                showForm("EnterCity", null);
                            }
                        };

                        TextArea text = new TextArea();
                        text.setText("is your present location " + thisplace + "?");
                        text.setUIID("dialogLabel");
                        text.setEditable(false);
                        Dialog.show("Confirm", text, cmds);

                    } else {
                        showForm("EnterCity", null);
                    }

                }

            } else {
                showForm("EnterCity", null);
            }

        }
    }

    @Override
    protected void onMain_OfficeButtonAction(Component c, ActionEvent event) {
        category = "office";
        //final String myPresentLoc = null;
        if (locAvaillable == true) {
            if ((latitude == 0.0) && (longitude == 0.0)) {
                if (Storage.getInstance().exists("MyPresentLocation")) {
                    try {
                        thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                        //myPresentLoc = "Nigeria";
                        thisplace = null;
                    } finally {
                        if (thisplace != null) {

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //do Option2
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText("is your present location " + thisplace + "?");
                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Confirm", text, cmds);

                        } else {
                            showForm("EnterCity", null);
                        }

                    }

                } else {
                    showForm("EnterCity", null);
                }

            } else {
                getMyLocation(latitude, longitude);
                if ((status != null) && ("OK".equals(status))) {
                    Label l = findWhereIamLabel(c.getComponentForm());
                    l.setText("You are at " + myLocation.getRoute());
                    c.getComponentForm().revalidate();
                    searchFor(myLocation.getRoute(), category);
                    if ((status != null) && ("200 OK".equals(status))) {
                        if (!services.isEmpty()) {
                            showForm("FetchedServices", null);
                        } else {
// The first branching that will be called many times.

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    // showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
//                            if (myLocation.getRoute().equals(myLocation.getSublocality())) {
//                                if (myLocation.getSublocality().equals(myLocation.getLocality())) {
////                                    if (myLocation.getLocality().equals(myLocation.getAdminLevel2())) {
////                                        if (myLocation.getAdminLevel2().equals(myLocation.getAdminLevel1())) {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getCountry() + "?");
////                                            thisplace = myLocation.getCountry();
////
////                                        } else {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getAdminLevel1() + "?");
////                                            thisplace = myLocation.getAdminLevel1();
////                                        }
////                                    } else {
////                                        text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                + "would you like to check " + myLocation.getAdminLevel2() + "?");
////                                        thisplace = myLocation.getAdminLevel2();
////                                    }
//                                } else {
//                                    text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
//                                            + "would you like to check " + myLocation.getLocality() + "?");
//                                    thisplace = myLocation.getLocality();
//                                }
//                            } else {
//                                
//                            }

                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
                                    + "would you like to check " + myLocation.getSublocality() + "?");
                            thisplace = myLocation.getSublocality();

                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Oh dear", text, cmds);

                            //, "OK", null);
                        }
                    } else {
                        Dialog.show("Oh dear", "you may have to check your network connection", "OK", null);
                    }
                } else {
                    showForm("EnterCity", null);
                }
            }
        } else {
            if (Storage.getInstance().exists("MyPresentLocation")) {
                try {
                    thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                } catch (Exception e) {
                    Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                    //myPresentLoc = "Nigeria";
                    thisplace = null;
                } finally {
                    if (thisplace != null) {

                        Command[] cmds = new Command[2];
                        cmds[0] = new Command("yes") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                //do Option2
                                searchFor(thisplace, category);
                                if ((status != null) && ("200 OK".equals(status))) {
                                    if (!services.isEmpty()) {
                                        showForm("FetchedServices", null);
                                    } else {
                                        Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                    }

                                } else {
                                    Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                }
                            }
                        };
                        cmds[1] = new Command("no") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                showForm("EnterCity", null);
                            }
                        };

                        TextArea text = new TextArea();
                        text.setText("is your present location " + thisplace + "?");
                        text.setUIID("dialogLabel");
                        text.setEditable(false);
                        Dialog.show("Confirm", text, cmds);

                    } else {
                        showForm("EnterCity", null);
                    }

                }

            } else {
                showForm("EnterCity", null);
            }

        }
    }

    @Override
    protected void onMain_ShopButtonAction(Component c, ActionEvent event) {
        category = "shop";
        //final String myPresentLoc = null;
        if (locAvaillable == true) {
            if ((latitude == 0.0) && (longitude == 0.0)) {
                if (Storage.getInstance().exists("MyPresentLocation")) {
                    try {
                        thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                        //myPresentLoc = "Nigeria";
                        thisplace = null;
                    } finally {
                        if (thisplace != null) {

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //do Option2
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText("is your present location " + thisplace + "?");
                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Confirm", text, cmds);

                        } else {
                            showForm("EnterCity", null);
                        }

                    }

                } else {
                    showForm("EnterCity", null);
                }

            } else {
                getMyLocation(latitude, longitude);
                if ((status != null) && ("OK".equals(status))) {
                    Label l = findWhereIamLabel(c.getComponentForm());
                    l.setText("You are at " + myLocation.getRoute());
                    c.getComponentForm().revalidate();
                    searchFor(myLocation.getRoute(), category);
                    if ((status != null) && ("200 OK".equals(status))) {
                        if (!services.isEmpty()) {
                            showForm("FetchedServices", null);
                        } else {
// The first branching that will be called many times.

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    // showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
//                            if (myLocation.getRoute().equals(myLocation.getSublocality())) {
//                                if (myLocation.getSublocality().equals(myLocation.getLocality())) {
////                                    if (myLocation.getLocality().equals(myLocation.getAdminLevel2())) {
////                                        if (myLocation.getAdminLevel2().equals(myLocation.getAdminLevel1())) {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getCountry() + "?");
////                                            thisplace = myLocation.getCountry();
////
////                                        } else {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getAdminLevel1() + "?");
////                                            thisplace = myLocation.getAdminLevel1();
////                                        }
////                                    } else {
////                                        text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                + "would you like to check " + myLocation.getAdminLevel2() + "?");
////                                        thisplace = myLocation.getAdminLevel2();
////                                    }
//                                } else {
//                                    text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
//                                            + "would you like to check " + myLocation.getLocality() + "?");
//                                    thisplace = myLocation.getLocality();
//                                }
//                            } else {
//                               
//                            }

                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
                                    + "would you like to check " + myLocation.getSublocality() + "?");
                            thisplace = myLocation.getSublocality();

                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Oh dear", text, cmds);

                            //, "OK", null);
                        }
                    } else {
                        Dialog.show("Oh dear", "you may have to check your network connection", "OK", null);
                    }
                } else {
                    showForm("EnterCity", null);
                }
            }
        } else {
            if (Storage.getInstance().exists("MyPresentLocation")) {
                try {
                    thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                } catch (Exception e) {
                    Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                    //myPresentLoc = "Nigeria";
                    thisplace = null;
                } finally {
                    if (thisplace != null) {

                        Command[] cmds = new Command[2];
                        cmds[0] = new Command("yes") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                //do Option2
                                searchFor(thisplace, category);
                                if ((status != null) && ("200 OK".equals(status))) {
                                    if (!services.isEmpty()) {
                                        showForm("FetchedServices", null);
                                    } else {
                                        Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                    }

                                } else {
                                    Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                }
                            }
                        };
                        cmds[1] = new Command("no") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                showForm("EnterCity", null);
                            }
                        };

                        TextArea text = new TextArea();
                        text.setText("is your present location " + thisplace + "?");
                        text.setUIID("dialogLabel");
                        text.setEditable(false);
                        Dialog.show("Confirm", text, cmds);

                    } else {
                        showForm("EnterCity", null);
                    }

                }

            } else {
                showForm("EnterCity", null);
            }

        }
    }

    @Override
    protected void onMain_HangOutButtonAction(Component c, ActionEvent event) {
        category = "hangout";
        //final String myPresentLoc = null;
        if (locAvaillable == true) {
            if ((latitude == 0.0) && (longitude == 0.0)) {
                if (Storage.getInstance().exists("MyPresentLocation")) {
                    try {
                        thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                        //myPresentLoc = "Nigeria";
                        thisplace = null;
                    } finally {
                        if (thisplace != null) {

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //do Option2
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText("is your present location " + thisplace + "?");
                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Confirm", text, cmds);

                        } else {
                            showForm("EnterCity", null);
                        }

                    }

                } else {
                    showForm("EnterCity", null);
                }

            } else {
                getMyLocation(latitude, longitude);
                if ((status != null) && ("OK".equals(status))) {
                    Label l = findWhereIamLabel(c.getComponentForm());
                    l.setText("You are at " + myLocation.getRoute());
                    c.getComponentForm().revalidate();
                    searchFor(myLocation.getRoute(), category);
                    if ((status != null) && ("200 OK".equals(status))) {
                        if (!services.isEmpty()) {
                            showForm("FetchedServices", null);
                        } else {
// The first branching that will be called many times.

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //  showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
//                            if (myLocation.getRoute().equals(myLocation.getSublocality())) {
//                                if (myLocation.getSublocality().equals(myLocation.getLocality())) {
////                                    if (myLocation.getLocality().equals(myLocation.getAdminLevel2())) {
////                                        if (myLocation.getAdminLevel2().equals(myLocation.getAdminLevel1())) {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getCountry() + "?");
////                                            thisplace = myLocation.getCountry();
////
////                                        } else {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getAdminLevel1() + "?");
////                                            thisplace = myLocation.getAdminLevel1();
////                                        }
////                                    } else {
////                                        text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                + "would you like to check " + myLocation.getAdminLevel2() + "?");
////                                        thisplace = myLocation.getAdminLevel2();
////                                    }
//                                } else {
//                                    text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
//                                            + "would you like to check " + myLocation.getLocality() + "?");
//                                    thisplace = myLocation.getLocality();
//                                }
//                            } else {
//                                
//                            }

                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
                                    + "would you like to check " + myLocation.getSublocality() + "?");
                            thisplace = myLocation.getSublocality();

                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Oh dear", text, cmds);

                            //, "OK", null);
                        }
                    } else {
                        Dialog.show("Oh dear", "you may have to check your network connection", "OK", null);
                    }
                } else {
                    showForm("EnterCity", null);
                }
            }
        } else {
            if (Storage.getInstance().exists("MyPresentLocation")) {
                try {
                    thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                } catch (Exception e) {
                    Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                    //myPresentLoc = "Nigeria";
                    thisplace = null;
                } finally {
                    if (thisplace != null) {

                        Command[] cmds = new Command[2];
                        cmds[0] = new Command("yes") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                //do Option2
                                searchFor(thisplace, category);
                                if ((status != null) && ("200 OK".equals(status))) {
                                    if (!services.isEmpty()) {
                                        showForm("FetchedServices", null);
                                    } else {
                                        Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                    }

                                } else {
                                    Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                }
                            }
                        };
                        cmds[1] = new Command("no") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                showForm("EnterCity", null);
                            }
                        };

                        TextArea text = new TextArea();
                        text.setText("is your present location " + thisplace + "?");
                        text.setUIID("dialogLabel");
                        text.setEditable(false);
                        Dialog.show("Confirm", text, cmds);

                    } else {
                        showForm("EnterCity", null);
                    }

                }

            } else {
                showForm("EnterCity", null);
            }

        }
    }

    @Override
    protected void onMain_CinemaButtonAction(Component c, ActionEvent event) {
        category = "cinema";
        //final String myPresentLoc = null;
        if (locAvaillable == true) {
            if ((latitude == 0.0) && (longitude == 0.0)) {
                if (Storage.getInstance().exists("MyPresentLocation")) {
                    try {
                        thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                        //myPresentLoc = "Nigeria";
                        thisplace = null;
                    } finally {
                        if (thisplace != null) {

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //do Option2
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText("is your present location " + thisplace + "?");
                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Confirm", text, cmds);

                        } else {
                            showForm("EnterCity", null);
                        }

                    }

                } else {
                    showForm("EnterCity", null);
                }

            } else {
                getMyLocation(latitude, longitude);
                if ((status != null) && ("OK".equals(status))) {
                    Label l = findWhereIamLabel(c.getComponentForm());
                    l.setText("You are at " + myLocation.getRoute());
                    c.getComponentForm().revalidate();
                    searchFor(myLocation.getRoute(), category);
                    if ((status != null) && ("200 OK".equals(status))) {
                        if (!services.isEmpty()) {
                            showForm("FetchedServices", null);
                        } else {
// The first branching that will be called many times.

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    // showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
//                            if (myLocation.getRoute().equals(myLocation.getSublocality())) {
//                                if (myLocation.getSublocality().equals(myLocation.getLocality())) {
////                                    if (myLocation.getLocality().equals(myLocation.getAdminLevel2())) {
////                                        if (myLocation.getAdminLevel2().equals(myLocation.getAdminLevel1())) {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getCountry() + "?");
////                                            thisplace = myLocation.getCountry();
////
////                                        } else {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getAdminLevel1() + "?");
////                                            thisplace = myLocation.getAdminLevel1();
////                                        }
////                                    } else {
////                                        text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                + "would you like to check " + myLocation.getAdminLevel2() + "?");
////                                        thisplace = myLocation.getAdminLevel2();
////                                    }
//                                } else {
//                                    text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
//                                            + "would you like to check " + myLocation.getLocality() + "?");
//                                    thisplace = myLocation.getLocality();
//                                }
//                            } else {
//                                
//                            }

                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
                                    + "would you like to check " + myLocation.getSublocality() + "?");
                            thisplace = myLocation.getSublocality();

                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Oh dear", text, cmds);

                            //, "OK", null);
                        }
                    } else {
                        Dialog.show("Oh dear", "you may have to check your network connection", "OK", null);
                    }
                } else {
                    showForm("EnterCity", null);
                }
            }
        } else {
            if (Storage.getInstance().exists("MyPresentLocation")) {
                try {
                    thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                } catch (Exception e) {
                    Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                    //myPresentLoc = "Nigeria";
                    thisplace = null;
                } finally {
                    if (thisplace != null) {

                        Command[] cmds = new Command[2];
                        cmds[0] = new Command("yes") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                //do Option2
                                searchFor(thisplace, category);
                                if ((status != null) && ("200 OK".equals(status))) {
                                    if (!services.isEmpty()) {
                                        showForm("FetchedServices", null);
                                    } else {
                                        Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                    }

                                } else {
                                    Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                }
                            }
                        };
                        cmds[1] = new Command("no") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                showForm("EnterCity", null);
                            }
                        };

                        TextArea text = new TextArea();
                        text.setText("is your present location " + thisplace + "?");
                        text.setUIID("dialogLabel");
                        text.setEditable(false);
                        Dialog.show("Confirm", text, cmds);

                    } else {
                        showForm("EnterCity", null);
                    }

                }

            } else {
                showForm("EnterCity", null);
            }

        }
    }

    @Override
    protected void onMain_BeachButtonAction(Component c, ActionEvent event) {
        category = "beach";
        //final String myPresentLoc = null;
        if (locAvaillable == true) {
            if ((latitude == 0.0) && (longitude == 0.0)) {
                if (Storage.getInstance().exists("MyPresentLocation")) {
                    try {
                        thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                    } catch (Exception e) {
                        Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                        //myPresentLoc = "Nigeria";
                        thisplace = null;
                    } finally {
                        if (thisplace != null) {

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //do Option2
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
                            text.setText("is your present location " + thisplace + "?");
                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Confirm", text, cmds);

                        } else {
                            showForm("EnterCity", null);
                        }

                    }

                } else {
                    showForm("EnterCity", null);
                }

            } else {
                getMyLocation(latitude, longitude);
                if ((status != null) && ("OK".equals(status))) {
                    Label l = findWhereIamLabel(c.getComponentForm());
                    l.setText("You are at " + myLocation.getRoute());
                    c.getComponentForm().revalidate();
                    searchFor(myLocation.getRoute(), category);
                    if ((status != null) && ("200 OK".equals(status))) {
                        if (!services.isEmpty()) {
                            showForm("FetchedServices", null);
                        } else {
// The first branching that will be called many times.

                            Command[] cmds = new Command[2];
                            cmds[0] = new Command("yes") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    searchFor(thisplace, category);
                                    if ((status != null) && ("200 OK".equals(status))) {
                                        if (!services.isEmpty()) {
                                            showForm("FetchedServices", null);
                                        } else {
                                            Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                        }

                                    } else {
                                        Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                    }
                                }
                            };
                            cmds[1] = new Command("no") {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    //  showForm("EnterCity", null);
                                }
                            };

                            TextArea text = new TextArea();
//                            if (myLocation.getRoute().equals(myLocation.getSublocality())) {
//                                if (myLocation.getSublocality().equals(myLocation.getLocality())) {
////                                    if (myLocation.getLocality().equals(myLocation.getAdminLevel2())) {
////                                        if (myLocation.getAdminLevel2().equals(myLocation.getAdminLevel1())) {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getCountry() + "?");
////                                            thisplace = myLocation.getCountry();
////
////                                        } else {
////                                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                    + "would you like to check " + myLocation.getAdminLevel1() + "?");
////                                            thisplace = myLocation.getAdminLevel1();
////                                        }
////                                    } else {
////                                        text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
////                                                + "would you like to check " + myLocation.getAdminLevel2() + "?");
////                                        thisplace = myLocation.getAdminLevel2();
////                                    }
//                                } else {
//                                    text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
//                                            + "would you like to check " + myLocation.getLocality() + "?");
//                                    thisplace = myLocation.getLocality();
//                                }
//                            } else {
//                                
//                            }

                            text.setText(category + " could not be located around " + myLocation.getRoute() + "\n"
                                    + "would you like to check " + myLocation.getSublocality() + "?");
                            thisplace = myLocation.getSublocality();

                            text.setUIID("dialogLabel");
                            text.setEditable(false);
                            Dialog.show("Oh dear", text, cmds);

                            //, "OK", null);
                        }
                    } else {
                        Dialog.show("Oh dear", "you may have to check your network connection", "OK", null);
                    }
                } else {
                    showForm("EnterCity", null);
                }
            }
        } else {
            if (Storage.getInstance().exists("MyPresentLocation")) {
                try {
                    thisplace = (String) Storage.getInstance().readObject("MyPresentLocation");
                } catch (Exception e) {
                    Dialog.show("oh dear!!!", "error occured trying to read your stored location '" + e.getMessage() + "'", "OK", null);
                    //myPresentLoc = "Nigeria";
                    thisplace = null;
                } finally {
                    if (thisplace != null) {

                        Command[] cmds = new Command[2];
                        cmds[0] = new Command("yes") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                //do Option2
                                searchFor(thisplace, category);
                                if ((status != null) && ("200 OK".equals(status))) {
                                    if (!services.isEmpty()) {
                                        showForm("FetchedServices", null);
                                    } else {
                                        Dialog.show("Sorry", "could not locate any " + category + " around " + thisplace, "OK", null);
                                    }

                                } else {
                                    Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
                                }
                            }
                        };
                        cmds[1] = new Command("no") {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                showForm("EnterCity", null);
                            }
                        };

                        TextArea text = new TextArea();
                        text.setText("is your present location " + thisplace + "?");
                        text.setUIID("dialogLabel");
                        text.setEditable(false);
                        Dialog.show("Confirm", text, cmds);

                    } else {
                        showForm("EnterCity", null);
                    }

                }

            } else {
                showForm("EnterCity", null);
            }

        }
    }

    @Override
    protected void beforeMyLocation(Form f) {
        //MapComponent mp = findMyMapComponent(f);
        f.removeAll();
        final MapComponent mc = new MapComponent(new GoogleMapsProvider(myKey));
        f.setScrollable(false);



        Coord lastLocation = new Coord(latitude, longitude);
        // mc.zoomTo(crd, 6);

        PointsLayer pl = new PointsLayer();
        Image redPin = fetchResourceFile().getImage("red_pin.png");
        pl.setPointIcon(redPin);

        System.out.println("My lastlocation is " + lastLocation);

        PointLayer p = new PointLayer(lastLocation, "you are here", redPin);//PointLayer(lastLocation, "Direction is : " + direction, redPin);
        p.setDisplayName(true);
        pl.addPoint(p);
        mc.removeAllLayers();
        mc.addLayer(pl);

        mc.zoomTo(lastLocation, 15);
        f.addComponent(BorderLayout.CENTER, mc);
    }

    @Override
    protected void onEnterCity_SearchCityAction(Component c, ActionEvent event) {
        String city = findCityName(c.getComponentForm()).getText();
        if ("".trim().equals(city)) {
            Dialog.show("oh dear", "please enter your location", "OK", null);
        } else {
            thisplace = city;
            ((Dialog) Display.getInstance().getCurrent()).dispose();
            searchFor(city, category);
            if ((status != null) && ("200 OK".equals(status))) {
                if (!services.isEmpty()) {
                    showForm("FetchedServices", null);
                } else {
                    Dialog.show("Sorry", "could not locate any " + category + " around " + city, "OK", null);
                }

            } else {
                Dialog.show("oh dear", "you might have to check your network connection", "OK", null);
            }
        }
    }

    @Override
    protected void onEnterCity_CancelAction(Component c, ActionEvent event) {
        ((Dialog) Display.getInstance().getCurrent()).dispose();
    }

    @Override
    protected void beforeFetchedServices(Form f) {
        f.setTitle(category + " at " + thisplace);

        Command exit = new Command("Home") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.
                //Display.getInstance().exitApplication();
                back();
            }
        };
        f.addCommand(exit);
    }

    @Override
    protected boolean initListModelServicesMultiList(List cmp) {
        cmp.setModel(new DefaultListModel(services));
        return true;
    }

    @Override
    protected void beforeMain(Form f) {
        Command exit = new Command("Exit") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.
                Display.getInstance().exitApplication();
            }
        };
        f.addCommand(exit);


        Command about = new Command("About") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //super.actionPerformed(evt); //To change body of generated methods, choose Tools | Templates.
                TextArea txt = new TextArea();
                txt.setUIID("dialogLabel");
                txt.setText("Connect now is still under development and will be completed soon");
                Dialog.show("About", txt.getText(), "OK", null);
            }
        };

        f.addCommand(about);
        
        if (Storage.getInstance().exists("MySetLocation")) {
            try {
                Hashtable setoloc = (Hashtable)Storage.getInstance().readObject("MySetLocation");
                myLocation = new MyLocation((String)setoloc.get("route"), (String)setoloc.get("sublocality"));
            } catch (Exception e) {
            }
        }
    }
}

/**
 * This class contains generated code from the Codename One Designer, DO NOT MODIFY!
 * This class is designed for subclassing that way the code generator can overwrite it
 * anytime without erasing your changes which should exist in a subclass!
 * For details about this file and how it works please read this blog post:
 * http://codenameone.blogspot.com/2010/10/ui-builder-class-how-to-actually-use.html
*/
package generated;

import com.codename1.ui.*;
import com.codename1.ui.util.*;
import com.codename1.ui.plaf.*;
import java.util.Hashtable;
import com.codename1.ui.events.*;

public abstract class StateMachineBase extends UIBuilder {
    private Container aboutToShowThisContainer;
    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    /**
    * @deprecated use the version that accepts a resource as an argument instead
    
**/
    protected void initVars() {}

    protected void initVars(Resources res) {}

    public StateMachineBase(Resources res, String resPath, boolean loadTheme) {
        startApp(res, resPath, loadTheme);
    }

    public Container startApp(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("MapComponent", com.codename1.maps.MapComponent.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("InfiniteProgress", com.codename1.components.InfiniteProgress.class);
        UIBuilder.registerCustomComponent("MultiList", com.codename1.ui.list.MultiList.class);
        UIBuilder.registerCustomComponent("Label", com.codename1.ui.Label.class);
        UIBuilder.registerCustomComponent("Dialog", com.codename1.ui.Dialog.class);
        UIBuilder.registerCustomComponent("TextArea", com.codename1.ui.TextArea.class);
        UIBuilder.registerCustomComponent("TextField", com.codename1.ui.TextField.class);
        UIBuilder.registerCustomComponent("Container", com.codename1.ui.Container.class);
        if(loadTheme) {
            if(res == null) {
                try {
                    if(resPath.endsWith(".res")) {
                        res = Resources.open(resPath);
                        System.out.println("Warning: you should construct the state machine without the .res extension to allow theme overlays");
                    } else {
                        res = Resources.openLayered(resPath);
                    }
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        if(res != null) {
            setResourceFilePath(resPath);
            setResourceFile(res);
            initVars(res);
            return showForm(getFirstFormName(), null);
        } else {
            Form f = (Form)createContainer(resPath, getFirstFormName());
            initVars(fetchResourceFile());
            beforeShow(f);
            f.show();
            postShow(f);
            return f;
        }
    }

    protected String getFirstFormName() {
        return "SplashScreen";
    }

    public Container createWidget(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("MapComponent", com.codename1.maps.MapComponent.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("InfiniteProgress", com.codename1.components.InfiniteProgress.class);
        UIBuilder.registerCustomComponent("MultiList", com.codename1.ui.list.MultiList.class);
        UIBuilder.registerCustomComponent("Label", com.codename1.ui.Label.class);
        UIBuilder.registerCustomComponent("Dialog", com.codename1.ui.Dialog.class);
        UIBuilder.registerCustomComponent("TextArea", com.codename1.ui.TextArea.class);
        UIBuilder.registerCustomComponent("TextField", com.codename1.ui.TextField.class);
        UIBuilder.registerCustomComponent("Container", com.codename1.ui.Container.class);
        if(loadTheme) {
            if(res == null) {
                try {
                    res = Resources.openLayered(resPath);
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        return createContainer(resPath, "SplashScreen");
    }

    protected void initTheme(Resources res) {
            String[] themes = res.getThemeResourceNames();
            if(themes != null && themes.length > 0) {
                UIManager.getInstance().setThemeProps(res.getTheme(themes[0]));
            }
    }

    public StateMachineBase() {
    }

    public StateMachineBase(String resPath) {
        this(null, resPath, true);
    }

    public StateMachineBase(Resources res) {
        this(res, null, true);
    }

    public StateMachineBase(String resPath, boolean loadTheme) {
        this(null, resPath, loadTheme);
    }

    public StateMachineBase(Resources res, boolean loadTheme) {
        this(res, null, loadTheme);
    }

    public com.codename1.ui.Container findContainer3(Component root) {
        return (com.codename1.ui.Container)findByName("Container3", root);
    }

    public com.codename1.ui.Container findContainer3() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container3", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container3", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer2(Component root) {
        return (com.codename1.ui.Container)findByName("Container2", root);
    }

    public com.codename1.ui.Container findContainer2() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container2", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container2", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findSearchButton(Component root) {
        return (com.codename1.ui.Button)findByName("searchButton", root);
    }

    public com.codename1.ui.Button findSearchButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("searchButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("searchButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer1(Component root) {
        return (com.codename1.ui.Container)findByName("Container1", root);
    }

    public com.codename1.ui.Container findContainer1() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container1", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container1", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findCityName(Component root) {
        return (com.codename1.ui.TextField)findByName("cityName", root);
    }

    public com.codename1.ui.TextField findCityName() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("cityName", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("cityName", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findCancel(Component root) {
        return (com.codename1.ui.Button)findByName("cancel", root);
    }

    public com.codename1.ui.Button findCancel() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("cancel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("cancel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findOfficeButton(Component root) {
        return (com.codename1.ui.Button)findByName("OfficeButton", root);
    }

    public com.codename1.ui.Button findOfficeButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("OfficeButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("OfficeButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextArea findTextArea(Component root) {
        return (com.codename1.ui.TextArea)findByName("TextArea", root);
    }

    public com.codename1.ui.TextArea findTextArea() {
        com.codename1.ui.TextArea cmp = (com.codename1.ui.TextArea)findByName("TextArea", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextArea)findByName("TextArea", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findChurchButton(Component root) {
        return (com.codename1.ui.Button)findByName("ChurchButton", root);
    }

    public com.codename1.ui.Button findChurchButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("ChurchButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("ChurchButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findHospitalButton(Component root) {
        return (com.codename1.ui.Button)findByName("HospitalButton", root);
    }

    public com.codename1.ui.Button findHospitalButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("HospitalButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("HospitalButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBeachButton(Component root) {
        return (com.codename1.ui.Button)findByName("BeachButton", root);
    }

    public com.codename1.ui.Button findBeachButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("BeachButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("BeachButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findSearchTextField(Component root) {
        return (com.codename1.ui.TextField)findByName("searchTextField", root);
    }

    public com.codename1.ui.TextField findSearchTextField() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("searchTextField", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("searchTextField", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLabel1(Component root) {
        return (com.codename1.ui.Label)findByName("Label1", root);
    }

    public com.codename1.ui.Label findLabel1() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("Label1", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("Label1", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findWhereIamLabel(Component root) {
        return (com.codename1.ui.Label)findByName("whereIamLabel", root);
    }

    public com.codename1.ui.Label findWhereIamLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("whereIamLabel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("whereIamLabel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findHotelButton(Component root) {
        return (com.codename1.ui.Button)findByName("HotelButton", root);
    }

    public com.codename1.ui.Button findHotelButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("HotelButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("HotelButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer(Component root) {
        return (com.codename1.ui.Container)findByName("Container", root);
    }

    public com.codename1.ui.Container findContainer() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.list.MultiList findServicesMultiList(Component root) {
        return (com.codename1.ui.list.MultiList)findByName("servicesMultiList", root);
    }

    public com.codename1.ui.list.MultiList findServicesMultiList() {
        com.codename1.ui.list.MultiList cmp = (com.codename1.ui.list.MultiList)findByName("servicesMultiList", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.list.MultiList)findByName("servicesMultiList", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.maps.MapComponent findMyMapComponent(Component root) {
        return (com.codename1.maps.MapComponent)findByName("myMapComponent", root);
    }

    public com.codename1.maps.MapComponent findMyMapComponent() {
        com.codename1.maps.MapComponent cmp = (com.codename1.maps.MapComponent)findByName("myMapComponent", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.maps.MapComponent)findByName("myMapComponent", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findMosqueButton(Component root) {
        return (com.codename1.ui.Button)findByName("MosqueButton", root);
    }

    public com.codename1.ui.Button findMosqueButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("MosqueButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("MosqueButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.components.InfiniteProgress findInfiniteProgress(Component root) {
        return (com.codename1.components.InfiniteProgress)findByName("InfiniteProgress", root);
    }

    public com.codename1.components.InfiniteProgress findInfiniteProgress() {
        com.codename1.components.InfiniteProgress cmp = (com.codename1.components.InfiniteProgress)findByName("InfiniteProgress", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.components.InfiniteProgress)findByName("InfiniteProgress", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findCinemaButton(Component root) {
        return (com.codename1.ui.Button)findByName("CinemaButton", root);
    }

    public com.codename1.ui.Button findCinemaButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("CinemaButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("CinemaButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findHangOutButton(Component root) {
        return (com.codename1.ui.Button)findByName("HangOutButton", root);
    }

    public com.codename1.ui.Button findHangOutButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("HangOutButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("HangOutButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findFuelButton(Component root) {
        return (com.codename1.ui.Button)findByName("FuelButton", root);
    }

    public com.codename1.ui.Button findFuelButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("FuelButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("FuelButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findSearchCity(Component root) {
        return (com.codename1.ui.Button)findByName("searchCity", root);
    }

    public com.codename1.ui.Button findSearchCity() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("searchCity", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("searchCity", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLabel(Component root) {
        return (com.codename1.ui.Label)findByName("Label", root);
    }

    public com.codename1.ui.Label findLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("Label", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("Label", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findATMButton(Component root) {
        return (com.codename1.ui.Button)findByName("ATMButton", root);
    }

    public com.codename1.ui.Button findATMButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("ATMButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("ATMButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findRestaurantButton(Component root) {
        return (com.codename1.ui.Button)findByName("RestaurantButton", root);
    }

    public com.codename1.ui.Button findRestaurantButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("RestaurantButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("RestaurantButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findShopButton(Component root) {
        return (com.codename1.ui.Button)findByName("ShopButton", root);
    }

    public com.codename1.ui.Button findShopButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("ShopButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("ShopButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    protected void exitForm(Form f) {
        if("MyLocation".equals(f.getName())) {
            exitMyLocation(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ServicesRenderer".equals(f.getName())) {
            exitServicesRenderer(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("EnterCity".equals(f.getName())) {
            exitEnterCity(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(f.getName())) {
            exitMain(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("SplashScreen".equals(f.getName())) {
            exitSplashScreen(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("FetchedServices".equals(f.getName())) {
            exitFetchedServices(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void exitMyLocation(Form f) {
    }


    protected void exitServicesRenderer(Form f) {
    }


    protected void exitEnterCity(Form f) {
    }


    protected void exitMain(Form f) {
    }


    protected void exitSplashScreen(Form f) {
    }


    protected void exitFetchedServices(Form f) {
    }

    protected void beforeShow(Form f) {
    aboutToShowThisContainer = f;
        if("MyLocation".equals(f.getName())) {
            beforeMyLocation(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ServicesRenderer".equals(f.getName())) {
            beforeServicesRenderer(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("EnterCity".equals(f.getName())) {
            beforeEnterCity(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(f.getName())) {
            beforeMain(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("SplashScreen".equals(f.getName())) {
            beforeSplashScreen(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("FetchedServices".equals(f.getName())) {
            beforeFetchedServices(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void beforeMyLocation(Form f) {
    }


    protected void beforeServicesRenderer(Form f) {
    }


    protected void beforeEnterCity(Form f) {
    }


    protected void beforeMain(Form f) {
    }


    protected void beforeSplashScreen(Form f) {
    }


    protected void beforeFetchedServices(Form f) {
    }

    protected void beforeShowContainer(Container c) {
        aboutToShowThisContainer = c;
        if("MyLocation".equals(c.getName())) {
            beforeContainerMyLocation(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ServicesRenderer".equals(c.getName())) {
            beforeContainerServicesRenderer(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("EnterCity".equals(c.getName())) {
            beforeContainerEnterCity(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(c.getName())) {
            beforeContainerMain(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("SplashScreen".equals(c.getName())) {
            beforeContainerSplashScreen(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("FetchedServices".equals(c.getName())) {
            beforeContainerFetchedServices(c);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void beforeContainerMyLocation(Container c) {
    }


    protected void beforeContainerServicesRenderer(Container c) {
    }


    protected void beforeContainerEnterCity(Container c) {
    }


    protected void beforeContainerMain(Container c) {
    }


    protected void beforeContainerSplashScreen(Container c) {
    }


    protected void beforeContainerFetchedServices(Container c) {
    }

    protected void postShow(Form f) {
        if("MyLocation".equals(f.getName())) {
            postMyLocation(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("ServicesRenderer".equals(f.getName())) {
            postServicesRenderer(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("EnterCity".equals(f.getName())) {
            postEnterCity(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(f.getName())) {
            postMain(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("SplashScreen".equals(f.getName())) {
            postSplashScreen(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("FetchedServices".equals(f.getName())) {
            postFetchedServices(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void postMyLocation(Form f) {
    }


    protected void postServicesRenderer(Form f) {
    }


    protected void postEnterCity(Form f) {
    }


    protected void postMain(Form f) {
    }


    protected void postSplashScreen(Form f) {
    }


    protected void postFetchedServices(Form f) {
    }

    protected void postShowContainer(Container c) {
        if("MyLocation".equals(c.getName())) {
            postContainerMyLocation(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("ServicesRenderer".equals(c.getName())) {
            postContainerServicesRenderer(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("EnterCity".equals(c.getName())) {
            postContainerEnterCity(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(c.getName())) {
            postContainerMain(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("SplashScreen".equals(c.getName())) {
            postContainerSplashScreen(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("FetchedServices".equals(c.getName())) {
            postContainerFetchedServices(c);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void postContainerMyLocation(Container c) {
    }


    protected void postContainerServicesRenderer(Container c) {
    }


    protected void postContainerEnterCity(Container c) {
    }


    protected void postContainerMain(Container c) {
    }


    protected void postContainerSplashScreen(Container c) {
    }


    protected void postContainerFetchedServices(Container c) {
    }

    protected void onCreateRoot(String rootName) {
        if("MyLocation".equals(rootName)) {
            onCreateMyLocation();
            aboutToShowThisContainer = null;
            return;
        }

        if("ServicesRenderer".equals(rootName)) {
            onCreateServicesRenderer();
            aboutToShowThisContainer = null;
            return;
        }

        if("EnterCity".equals(rootName)) {
            onCreateEnterCity();
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(rootName)) {
            onCreateMain();
            aboutToShowThisContainer = null;
            return;
        }

        if("SplashScreen".equals(rootName)) {
            onCreateSplashScreen();
            aboutToShowThisContainer = null;
            return;
        }

        if("FetchedServices".equals(rootName)) {
            onCreateFetchedServices();
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void onCreateMyLocation() {
    }


    protected void onCreateServicesRenderer() {
    }


    protected void onCreateEnterCity() {
    }


    protected void onCreateMain() {
    }


    protected void onCreateSplashScreen() {
    }


    protected void onCreateFetchedServices() {
    }

    protected Hashtable getFormState(Form f) {
        Hashtable h = super.getFormState(f);
        if("MyLocation".equals(f.getName())) {
            getStateMyLocation(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("ServicesRenderer".equals(f.getName())) {
            getStateServicesRenderer(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("EnterCity".equals(f.getName())) {
            getStateEnterCity(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Main".equals(f.getName())) {
            getStateMain(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("SplashScreen".equals(f.getName())) {
            getStateSplashScreen(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("FetchedServices".equals(f.getName())) {
            getStateFetchedServices(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

            return h;
    }


    protected void getStateMyLocation(Form f, Hashtable h) {
    }


    protected void getStateServicesRenderer(Form f, Hashtable h) {
    }


    protected void getStateEnterCity(Form f, Hashtable h) {
    }


    protected void getStateMain(Form f, Hashtable h) {
    }


    protected void getStateSplashScreen(Form f, Hashtable h) {
    }


    protected void getStateFetchedServices(Form f, Hashtable h) {
    }

    protected void setFormState(Form f, Hashtable state) {
        super.setFormState(f, state);
        if("MyLocation".equals(f.getName())) {
            setStateMyLocation(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("ServicesRenderer".equals(f.getName())) {
            setStateServicesRenderer(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("EnterCity".equals(f.getName())) {
            setStateEnterCity(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Main".equals(f.getName())) {
            setStateMain(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("SplashScreen".equals(f.getName())) {
            setStateSplashScreen(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("FetchedServices".equals(f.getName())) {
            setStateFetchedServices(f, state);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void setStateMyLocation(Form f, Hashtable state) {
    }


    protected void setStateServicesRenderer(Form f, Hashtable state) {
    }


    protected void setStateEnterCity(Form f, Hashtable state) {
    }


    protected void setStateMain(Form f, Hashtable state) {
    }


    protected void setStateSplashScreen(Form f, Hashtable state) {
    }


    protected void setStateFetchedServices(Form f, Hashtable state) {
    }

    protected boolean setListModel(List cmp) {
        String listName = cmp.getName();
        if("servicesMultiList".equals(listName)) {
            return initListModelServicesMultiList(cmp);
        }
        return super.setListModel(cmp);
    }

    protected boolean initListModelServicesMultiList(List cmp) {
        return false;
    }

    protected void handleComponentAction(Component c, ActionEvent event) {
        Container rootContainerAncestor = getRootAncestor(c);
        if(rootContainerAncestor == null) return;
        String rootContainerName = rootContainerAncestor.getName();
        Container leadParentContainer = c.getParent().getLeadParent();
        if(leadParentContainer != null && leadParentContainer.getClass() != Container.class) {
            c = c.getParent().getLeadParent();
        }
        if(rootContainerName == null) return;
        if(rootContainerName.equals("EnterCity")) {
            if("TextArea".equals(c.getName())) {
                onEnterCity_TextAreaAction(c, event);
                return;
            }
            if("cityName".equals(c.getName())) {
                onEnterCity_CityNameAction(c, event);
                return;
            }
            if("searchCity".equals(c.getName())) {
                onEnterCity_SearchCityAction(c, event);
                return;
            }
            if("cancel".equals(c.getName())) {
                onEnterCity_CancelAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Main")) {
            if("ATMButton".equals(c.getName())) {
                onMain_ATMButtonAction(c, event);
                return;
            }
            if("HotelButton".equals(c.getName())) {
                onMain_HotelButtonAction(c, event);
                return;
            }
            if("RestaurantButton".equals(c.getName())) {
                onMain_RestaurantButtonAction(c, event);
                return;
            }
            if("MosqueButton".equals(c.getName())) {
                onMain_MosqueButtonAction(c, event);
                return;
            }
            if("ChurchButton".equals(c.getName())) {
                onMain_ChurchButtonAction(c, event);
                return;
            }
            if("HospitalButton".equals(c.getName())) {
                onMain_HospitalButtonAction(c, event);
                return;
            }
            if("FuelButton".equals(c.getName())) {
                onMain_FuelButtonAction(c, event);
                return;
            }
            if("OfficeButton".equals(c.getName())) {
                onMain_OfficeButtonAction(c, event);
                return;
            }
            if("ShopButton".equals(c.getName())) {
                onMain_ShopButtonAction(c, event);
                return;
            }
            if("HangOutButton".equals(c.getName())) {
                onMain_HangOutButtonAction(c, event);
                return;
            }
            if("CinemaButton".equals(c.getName())) {
                onMain_CinemaButtonAction(c, event);
                return;
            }
            if("BeachButton".equals(c.getName())) {
                onMain_BeachButtonAction(c, event);
                return;
            }
            if("searchTextField".equals(c.getName())) {
                onMain_SearchTextFieldAction(c, event);
                return;
            }
            if("searchButton".equals(c.getName())) {
                onMain_SearchButtonAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("FetchedServices")) {
            if("servicesMultiList".equals(c.getName())) {
                onFetchedServices_ServicesMultiListAction(c, event);
                return;
            }
        }
    }

      protected void onEnterCity_TextAreaAction(Component c, ActionEvent event) {
      }

      protected void onEnterCity_CityNameAction(Component c, ActionEvent event) {
      }

      protected void onEnterCity_SearchCityAction(Component c, ActionEvent event) {
      }

      protected void onEnterCity_CancelAction(Component c, ActionEvent event) {
      }

      protected void onMain_ATMButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_HotelButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_RestaurantButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_MosqueButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_ChurchButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_HospitalButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_FuelButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_OfficeButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_ShopButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_HangOutButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_CinemaButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_BeachButtonAction(Component c, ActionEvent event) {
      }

      protected void onMain_SearchTextFieldAction(Component c, ActionEvent event) {
      }

      protected void onMain_SearchButtonAction(Component c, ActionEvent event) {
      }

      protected void onFetchedServices_ServicesMultiListAction(Component c, ActionEvent event) {
      }

}

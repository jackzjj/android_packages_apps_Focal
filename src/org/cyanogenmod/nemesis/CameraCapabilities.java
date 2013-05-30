package org.cyanogenmod.nemesis;

import android.content.Context;
import android.hardware.Camera;
import android.view.ViewGroup;

import org.cyanogenmod.nemesis.widgets.EffectWidget;
import org.cyanogenmod.nemesis.widgets.FlashWidget;
import org.cyanogenmod.nemesis.widgets.SceneModeWidget;
import org.cyanogenmod.nemesis.widgets.WhiteBalanceWidget;
import org.cyanogenmod.nemesis.widgets.WidgetBase;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds all the possible widgets of the
 * sidebar. It checks for support prior to adding them
 * effectively in the sidebar.
 */
public class CameraCapabilities {
    private List<WidgetBase> mWidgets;

    /**
     * Default constructor, initializes all the widgets. They will
     * then be sorted by populateSidebar.
     * @param context The CameraActivity context
     */
    public CameraCapabilities(CameraManager cam, Context context) {
        mWidgets = new ArrayList<WidgetBase>();

        // Populate the list of widgets.
        // Basically, if we add a new widget, we just put it here. They
        // will populate the sidebar in the same order as here
        mWidgets.add(new FlashWidget(cam, context));
        mWidgets.add(new WhiteBalanceWidget(cam, context));
        mWidgets.add(new SceneModeWidget(cam, context));
        mWidgets.add(new EffectWidget(cam, context));
    }

    /**
     * Populates the sidebar (through sideBarContainer) with the widgets actually
     * compatible with the device.
     * @param params The Camera parameters returned from the HAL for compatibility check
     * @param sideBarContainer The side bar layout that will contain all the toggle buttons
     */
    public void populateSidebar(Camera.Parameters params, ViewGroup sideBarContainer,
            ViewGroup widgetsContainer) {
        List<WidgetBase> unsupported = new ArrayList<WidgetBase>();

        for (int i = 0; i < mWidgets.size(); i++) {
            final WidgetBase widget = mWidgets.get(i);

            // Add the widget to the sidebar if it is supported by the device.
            // The compatibility is determined by widgets themselves.
            if (widget.isSupported(params)) {
                sideBarContainer.addView(widget.getToggleButton());
                widgetsContainer.addView(widget.getWidget());
            } else {
                unsupported.add(widget);
            }
        }

        for (int i = 0; i < unsupported.size(); i++)
            mWidgets.remove(unsupported.get(i));
    }
}

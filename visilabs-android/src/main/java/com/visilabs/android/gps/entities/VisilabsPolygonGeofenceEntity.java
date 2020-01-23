package com.visilabs.android.gps.entities;

import java.util.ArrayList;
import java.util.List;

public class VisilabsPolygonGeofenceEntity {

    public String guid;
    public String name;
    public List<VisilabsLatLng> points = new ArrayList<>();
    public boolean isInPolygon = false;

    public VisilabsPolygonGeofenceEntity() {

    }

    public boolean PointIsInRegion(double x, double y)
    {
        int crossings = 0;

        VisilabsLatLng point = new VisilabsLatLng(x, y);
        // for each edge
        for (int i = 0; i < points.size(); i++)
        {
            VisilabsLatLng a = points.get(i);
            int j = i + 1;
            if (j >= points.size())
            {
                j = 0;
            }
            VisilabsLatLng b = points.get(j);
            if (RayCrossesSegment(point, a, b))
            {
                crossings++;
            }
        }
        // odd number of crossings?
        return (crossings % 2 == 1);
    }

    public boolean RayCrossesSegment(VisilabsLatLng point, VisilabsLatLng a, VisilabsLatLng b)
    {
        double px = point.Longitude;
        double py = point.Latitude;
        double ax = a.Longitude;
        double ay = a.Latitude;
        double bx = b.Longitude;
        double by = b.Latitude;
        if (ay > by)
        {
            ax = b.Longitude;
            ay = b.Latitude;
            bx = a.Longitude;
            by = a.Latitude;
        }
        // alter longitude to cater for 180 degree crossings
        if (px < 0) { px += 360; };
        if (ax < 0) { ax += 360; };
        if (bx < 0) { bx += 360; };

        if (py == ay || py == by) py += 0.00000001;
        if ((py > by || py < ay) || (px > Math.max(ax, bx))) return false;
        if (px < Math.min(ax, bx)) return true;

        double red = (ax != bx) ? ((by - ay) / (bx - ax)) : Float.MAX_VALUE;
        double blue = (ax != px) ? ((py - ay) / (px - ax)) : Float.MAX_VALUE;
        return (blue >= red);
    }
}
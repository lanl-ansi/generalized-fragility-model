package gov.lanl.micot.application.utilities;

import gov.lanl.micot.application.fragility.core.GeometryObject;
import gov.lanl.micot.application.utilities.asset.PropertyData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Trevor Crawford
 */
public interface AssetData {

    public void setAllValues(String fileLocation);
    public ArrayList<GeometryObject> getGeometryObjects();
    public List<Map<String, PropertyData>> getPropertyObjects();

}

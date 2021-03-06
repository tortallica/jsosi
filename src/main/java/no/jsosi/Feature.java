package no.jsosi;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

public class Feature {

    private final SosiReader reader;
    private final Integer id;
    private final GeometryType geometryType;
    private final Coordinate[] coordinates;
    private final AttributeMap attributes;
    private final RefList refs;

    Feature(SosiReader reader, Integer id, GeometryType geometryType, AttributeMap attributes, Coordinate[] coordinates,
            RefList refs) {
        this.reader = reader;
        this.id = id;
        this.geometryType = geometryType;
        this.coordinates = coordinates;
        this.refs = refs;
        this.attributes = new AttributeMap(attributes);
        this.attributes.computeSubValues();
    }

    public Integer getId() {
        return id;
    }

    public Set<String> getAttributeKeySet() {
        return Collections.unmodifiableSet(attributes.keySet());
    }
    
    public Map<String, Object> getAttributeMap() {
        return attributes.toExternal();
    }

    public Object get(String key) {
        return attributes.get(key);
    }

    public GeometryType getGeometryType() {
        return geometryType;
    }

    public Geometry getGeometry() throws IOException {
        if (geometryType == GeometryType.FLATE) {
            return refs.createGeometry(reader);
        } else {
            return geometryType.createGeometry(reader.getGeometryFactory(), coordinates);
        }
    }

    int getCoordinateCount() throws IOException {
        return getGeometry().getCoordinates().length;
    }

    @Override
    public String toString() {
        return "Feature{attributes=" + attributes + "}";
    }

}

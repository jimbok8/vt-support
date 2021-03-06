/*
 * Copyright (C) 2017 Weather Decision Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.os.vt.mvt.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import uk.os.vt.mvt.VectorTile;
import uk.os.vt.mvt.adapt.jts.JtsAdapter;

public final class JtsGeomStats {

  public static final class FeatureStats {
    public int totalPts;
    public int repeatedPts;

    @Override
    public String toString() {
      return "FeatureStats{"
          + "totalPts=" + totalPts
          + ", repeatedPts=" + repeatedPts
          + "}";
    }
  }

  public Map<VectorTile.Tile.GeomType, Integer> featureCounts;
  public List<FeatureStats> featureStats;

  private JtsGeomStats() {
    final VectorTile.Tile.GeomType[] geomTypes = VectorTile.Tile.GeomType.values();
    featureCounts = new HashMap<>(geomTypes.length);

    for (VectorTile.Tile.GeomType nextGeomType : geomTypes) {
      featureCounts.put(nextGeomType, 0);
    }

    this.featureStats = new ArrayList<>();
  }

  @Override
  public String toString() {
    return "JtsGeomStats{"
        + "featureCounts=" + featureCounts
        + ", featureStats=" + featureStats
        + '}';
  }

  /**
   * Get feature counts and feature statistics (points and repeated points).
   *
   * @param flatGeomList geometry under analysis
   * @return the resulting statistics
   */
  public static JtsGeomStats getStats(List<Geometry> flatGeomList) {
    final JtsGeomStats stats = new JtsGeomStats();

    Map<VectorTile.Tile.GeomType, Integer> featureCounts = new HashMap<>();

    for (Geometry nextGeom : flatGeomList) {
      final VectorTile.Tile.GeomType geomType = JtsAdapter.toGeomType(nextGeom);

      // Count features by type
      if (featureCounts.containsKey(geomType)) {
        featureCounts.put(geomType, featureCounts.get(geomType) + 1);
      } else {
        featureCounts.put(geomType, 1);
      }

      // Get stats per feature
      stats.featureStats.add(getStats(nextGeom, geomType));
    }

    return stats;
  }

  private static FeatureStats getStats(Geometry geom, VectorTile.Tile.GeomType type) {
    FeatureStats featureStats;

    switch (type) {
      case POINT:
        featureStats = pointStats(geom);
        break;
      case LINESTRING:
        featureStats = lineStats(geom);
        break;
      case POLYGON:
        featureStats = polyStats(geom);
        break;
      default:
        featureStats = new FeatureStats();
    }

    return featureStats;
  }

  private static FeatureStats pointStats(Geometry geom) {
    final FeatureStats featureStats = new FeatureStats();

    final HashSet<Point> pointSet = new HashSet<>(geom.getNumPoints());
    featureStats.totalPts = geom.getNumPoints();

    for (int i = 0; i < geom.getNumGeometries(); ++i) {
      final Point p = (Point) geom.getGeometryN(i);
      featureStats.repeatedPts += pointSet.add(p) ? 0 : 1;
    }

    return featureStats;
  }

  private static FeatureStats lineStats(Geometry geom) {
    final FeatureStats featureStats = new FeatureStats();

    for (int i = 0; i < geom.getNumGeometries(); ++i) {
      final LineString lineString = (LineString) geom.getGeometryN(i);
      featureStats.totalPts += lineString.getNumPoints();
      featureStats.repeatedPts += checkRepeatedPoints2d(lineString);
    }

    return featureStats;
  }

  private static FeatureStats polyStats(Geometry geom) {
    final FeatureStats featureStats = new FeatureStats();

    for (int i = 0; i < geom.getNumGeometries(); ++i) {
      final Polygon nextPoly = (Polygon) geom.getGeometryN(i);

      // Stats: exterior ring
      final LineString exteriorRing = nextPoly.getExteriorRing();
      featureStats.totalPts += exteriorRing.getNumPoints();
      featureStats.repeatedPts += checkRepeatedPoints2d(exteriorRing);

      // Stats: interior rings
      for (int ringIndex = 0; ringIndex < nextPoly.getNumInteriorRing(); ++ringIndex) {

        final LineString nextInteriorRing = nextPoly.getInteriorRingN(ringIndex);
        featureStats.totalPts += nextInteriorRing.getNumPoints();
        featureStats.repeatedPts += checkRepeatedPoints2d(nextInteriorRing);
      }
    }

    return featureStats;
  }

  private static int checkRepeatedPoints2d(LineString lineString) {
    int repeatedPoints = 0;

    final CoordinateSequence coordSeq = lineString.getCoordinateSequence();
    Coordinate nextCoord = null;
    Coordinate prevCoord;
    for (int i = 0; i < coordSeq.size(); ++i) {
      prevCoord = nextCoord;
      nextCoord = coordSeq.getCoordinate(i);
      if (nextCoord.equals(prevCoord)) {
        ++repeatedPoints;
      }
    }

    return repeatedPoints;
  }
}

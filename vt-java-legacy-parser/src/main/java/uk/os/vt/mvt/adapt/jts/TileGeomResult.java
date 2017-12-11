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

package uk.os.vt.mvt.adapt.jts;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.List;
import java.util.Objects;
import uk.os.vt.mvt.build.MvtLayerParams;

/**
 * Processing result containing intersection geometry and MVT geometry.
 *
 * @see JtsAdapter#createTileGeom(Geometry, Envelope, GeometryFactory, MvtLayerParams,
 *      IGeometryFilter)
 */
public final class TileGeomResult {

  /**
   * Intersection geometry (projection units and coordinates).
   */
  public final List<Geometry> intGeoms;

  /**
   * Geometry in MVT coordinates (tile extent units, screen coordinates).
   */
  public final List<Geometry> mvtGeoms;

  /**
   * Create TileGeomResult, which contains the intersection of geometry and MVT geometry.
   *
   * @param intGeoms geometry intersecting tile
   * @param mvtGeoms geometry for MVT
   * @throws NullPointerException if intGeoms or mvtGeoms are null
   */
  public TileGeomResult(List<Geometry> intGeoms, List<Geometry> mvtGeoms) {
    Objects.requireNonNull(intGeoms);
    Objects.requireNonNull(mvtGeoms);
    this.intGeoms = intGeoms;
    this.mvtGeoms = mvtGeoms;
  }
}

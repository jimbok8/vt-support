/*
 * Copyright (C) 2016 Ordnance Survey
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

package uk.os.vt.demo.geo;

import static uk.os.vt.demo.geo.Util.createPolygonXy;

import java.util.Arrays;
import java.util.List;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

public class Countries {

  /**
   * All demo country geometry.
   *
   * @return all demo countries
   */
  public static List<Geometry> all() {
    return Arrays.asList(australia(), bermuda(), congo(), unitedKingdom(), unitedStatesOfAmerica());
  }

  /**
   * Polygon of Australia.
   *
   * @return geometry
   */
  public static Polygon australia() {
    return createPolygonXy("ISO AU", "Australia", new double[][]{
        {142.20703125, -10.919617760254685},
        {141.416015625, -12.64033830684679},
        {141.240234375, -16.55196172197251},
        {139.833984375, -17.72775860985227},
        {135.087890625, -14.77488250651626},
        {136.494140625, -11.953349393643416},
        {130.341796875, -12.125264218331578},
        {129.111328125, -14.859850400601037},
        {126.91406249999999, -13.667338259654947},
        {121.28906250000001, -18.979025953255267},
        {113.5546875, -22.187404991398775},
        {113.818359375, -26.273714024406416},
        {115.6640625, -31.802892586706747},
        {114.78515624999999, -34.016241889667015},
        {117.68554687499999, -35.17380831799957},
        {118.91601562499999, -34.59704151614416},
        {119.970703125, -33.9433599465788},
        {123.837890625, -33.797408767572485},
        {125.68359374999999, -32.249974455863295},
        {128.583984375, -32.10118973232094},
        {130.693359375, -31.503629305773018},
        {133.505859375, -32.10118973232094},
        {135.87890625, -35.02999636902566},
        {137.900390625, -32.76880048488168},
        {137.109375, -35.31736632923786},
        {138.33984375, -34.37971258046219},
        {138.779296875, -35.17380831799957},
        {138.076171875, -35.57691652403861},
        {139.21874999999997, -35.63051198300059},
        {140.09765625, -38.06539235133247},
        {143.61328125, -38.75408327579141},
        {144.6240234375, -38.20365531807149},
        {146.49169921875, -39.18117526158747},
        {147.744140625, -37.78808138412045},
        {149.7216796875, -37.71859032558814},
        {150.380859375, -37.37015718405751},
        {150.0732421875, -35.8534396195918},
        {151.7431640625, -33.02708758002873},
        {153.72070312499997, -28.1882436418503},
        {152.7978515625, -24.806681353851978},
        {149.7216796875, -22.228090416784486},
        {148.623046875, -19.890723023996898},
        {145.634765625, -18.60460138845525},
        {146.162109375, -16.8886597873816},
        {145.458984375, -15.876809064146757},
        {145.283203125, -14.264383087562637},
        {142.20703125, -10.919617760254685}
    });
  }

  /**
   * Polygon of Bermuda.
   *
   * @return geometry
   */
  public static Polygon bermuda() {
    return createPolygonXy("ISO BM", "Bermuda", new double[][]{
        {-64.68681335449219, 32.407211836256685},
        {-64.93263244628906, 32.28423019803735},
        {-64.87907409667969, 32.204086355917944},
        {-64.6270751953125, 32.34980253736092},
        {-64.68681335449219, 32.407211836256685}
    });
  }

  /**
   * Polygon of Democratic Republic of the Congo.
   *
   * @return geometry
   */
  public static Polygon congo() {
    return createPolygonXy("ISO CD", "Democratic Republic of Congo", new double[][]{
        {22.763671875, 10.660607953624776},
        {13.271484375, 2.28455066023697},
        {20.654296875, -15.029685756555674},
        {32.958984375, -8.233237111274553},
        {28.037109375, 6.315298538330033},
        {22.763671875, 10.660607953624776}
    });
  }

  /**
   * Polygon of the United Kingdom.
   *
   * @return geometry
   */
  public static Polygon unitedKingdom() {
    return createPolygonXy("ISO GB", "United Kingdom", new double[][]{
        {-3.779296875, 58.90464570302001},
        {-7.207031249999999, 58.53959476664049},
        {-5.625, 50.064191736659104},
        {1.318359375, 51.508742458803326},
        {-3.779296875, 58.90464570302001}
    });
  }

  /**
   * Polygon of the United States of America.
   *
   * @return geometry
   */
  public static Polygon unitedStatesOfAmerica() {
    return createPolygonXy("ISO US", "United States of America", new double[][]{
        {-93.69140625, 49.15296965617042},
        {-124.45312499999999, 49.26780455063753},
        {-124.8046875, 40.97989806962013},
        {-119.53125, 33.137551192346145},
        {-109.951171875, 22.63429269379353},
        {-108.9404296875, 23.160563309048314},
        {-115.6201171875, 31.316101383495624},
        {-113.99414062499999, 31.653381399664},
        {-108.06152343749999, 31.240985378021307},
        {-108.1494140625, 31.840232667909365},
        {-106.3916015625, 31.80289258670676},
        {-104.94140625, 30.56226095049944},
        {-104.5458984375, 29.611670115197377},
        {-103.095703125, 28.998531814051795},
        {-102.1728515625, 29.916852233070173},
        {-97.20703125, 25.799891182088334},
        {-96.50390625, 29.53522956294847},
        {-84.0234375, 30.600093873550072},
        {-81.2109375, 24.5271348225978},
        {-79.62890625, 24.686952411999155},
        {-81.2109375, 31.50362930577303},
        {-75.9375, 35.460669951495305},
        {-74.8828125, 39.774769485295465},
        {-71.015625, 41.77131167976407},
        {-70.13671875, 43.45291889355465},
        {-66.81884765625, 44.715513732021336},
        {-68.02734375, 47.487513008956554},
        {-69.80712890625, 47.35371061951363},
        {-71.630859375, 45.042478050891546},
        {-75.1025390625, 44.98034238084973},
        {-76.9921875, 43.56447158721811},
        {-79.189453125, 43.61221676817573},
        {-78.837890625, 42.87596410238256},
        {-82.880859375, 41.73852846935917},
        {-82.7490234375, 42.58544425738491},
        {-82.0458984375, 43.866218006556394},
        {-82.5732421875, 45.583289756006316},
        {-88.3740234375, 48.3416461723746},
        {-89.56054687499999, 47.931066347509784},
        {-93.07617187499999, 48.69096039092549},
        {-93.69140625, 49.15296965617042},
    });
  }
}

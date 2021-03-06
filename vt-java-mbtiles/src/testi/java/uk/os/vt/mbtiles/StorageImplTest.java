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

package uk.os.vt.mbtiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import uk.os.vt.Entry;
import uk.os.vt.Metadata;
import uk.os.vt.StorageResult;

public class StorageImplTest {

  @Test
  public void setAndGetMetadata() throws IOException, InterruptedException, JSONException {

    final File file = provideNonExistentTestFileOrBlow();
    final StorageImpl storage = new StorageImpl.Builder(file).createIfNotExist().build();

    final Metadata.Layer terrainLayer = new Metadata.Layer.Builder().setId("terrain")
        .setDescription("OS Terrain data at 50m resolution").setMinZoom(3).setMaxZoom(12)
        .setSource("os.os-terrain-50-v2", "OS Terrain 50")
        .addField("height", "height above mean sea level in relation to OSGB36")
        .addField("easting", "the X coordinate in reference to BNG")
        .addField("northing", "the Y coordinate in reference to BNG").build();

    final Metadata.Layer windLayer =
        new Metadata.Layer.Builder().setId("wind").setDescription("Met Office national wind data")
            .setMinZoom(3).setMaxZoom(12).setSource("met.met-wind-v6", "Met Wind Data")
            .addField("mph", "the mean speed sampled over 1 minute")
            .addField("timestamp", "number (int64) since epoch time")
            .addField("direction", "int - degrees from 0 - 360").build();

    final Metadata.Layer buildingLayer = new Metadata.Layer.Builder().setId("building")
        .setDescription("OS Buildings premium").setMinZoom(3).setMaxZoom(12)
        .setSource("os.os-buildings-v3", "OS MasterMap (Topo) Buildings")
        .addField("class", "One of: house, flat, bungalow, commercial, park_home")
        .addField("type",
            "OS tag, more specific than class.  "
                + "e.g. house_detached, house_semi_detached, house_terrace etc.")
        .addField("age", "number")
        .addField("vacant", "Text. Whether building is vacant. One of: 'true', 'false'")
        .addField("name_en", "English name").addField("name_cy", "Welsh name").build();


    final Metadata metadata = new Metadata.Builder()
        .setAttribution("<a href=\"https://os.uk/business-and-government/\" "
            + "target=\"_blank\">© Ordnance Survey</a>")
        .setDescription("A demo data mashup - join the party!").setBounds(-7.56, 49.96, 1.78, 60.84)
        .setCenter(51, 0, 5).setMinZoom(5).setMaxZoom(5).addLayer(terrainLayer).addLayer(windLayer)
        .addLayer(buildingLayer).build();

    storage.putMetadata(Single.just(metadata));

    final Metadata metadataResult = storage.getMetadata().blockingFirst();

    assertNotNull(metadataResult);

    final JSONObject expected = metadata.getTileJson();
    final JSONObject actual = metadataResult.getTileJson();
    JSONAssert.assertEquals(expected, actual, true);
  }


  @Test(expected = IllegalStateException.class)
  public void testWhenNoSuchFileDefaultBehaviourIsToBlowUp() throws IOException {
    final File file = provideNonExistentTestFileOrBlow();
    new StorageImpl.Builder(file).build();
  }

  @Test
  public void testWhenNoSuchFileAndInitialisationFlagTrueThenAllOk() throws IOException {
    final File file = provideNonExistentTestFileOrBlow();
    new StorageImpl.Builder(file).createIfNotExist().build();
  }

  @Test
  public void ensureMandatoryTablesAppearToBeMade() throws IOException {
    final File file = provideNonExistentTestFileOrBlow();
    final StorageImpl storage = new StorageImpl.Builder(file).createIfNotExist().build();

    final TestObserver<Entry> entrySubscriber = new TestObserver<>();
    storage.getEntries().blockingSubscribe(entrySubscriber);
    entrySubscriber.assertNoErrors();
    entrySubscriber.assertComplete();
  }

  @Test
  public void testEmptyStorageCompletesWithMetadataRequest() throws IOException {
    final File file = provideNonExistentTestFileOrBlow();
    final StorageImpl storage = new StorageImpl.Builder(file).createIfNotExist().build();

    final TestObserver<Metadata> metadataSubscriber = new TestObserver<>();
    storage.getMetadata().blockingSubscribe(metadataSubscriber);
    metadataSubscriber.assertNoErrors();
    metadataSubscriber.assertComplete();
  }

  @Test
  public void testEntryEquality() {
    final int zoomLevel = 3;
    final int column = 4;
    final int row = 5;
    final byte[] bytes = getGarbageBytes();

    final Entry first = new Entry(zoomLevel, column, row, bytes);
    final Entry second = new Entry(zoomLevel, column, row, bytes);
    assertEquals(first, second);
  }

  @Test
  public void addAnEntry() throws IOException {
    final File file = provideNonExistentTestFileOrBlow();
    final StorageImpl storage = new StorageImpl.Builder(file).createIfNotExist().build();

    final int zoomLevel = 3;
    final int column = 4;
    final int row = 5;
    final byte[] bytes = getGarbageBytes();

    final Entry in = new Entry(zoomLevel, column, row, bytes);
    storage.putEntries(Observable.just(in));
    final Entry out = storage.getEntries().blockingFirst();

    assertEquals(in, out);
  }

  @Test
  public void addEntries() throws IOException {
    final File file = provideNonExistentTestFileOrBlow();
    final StorageImpl storage = new StorageImpl.Builder(file).createIfNotExist().build();

    final int zoomLevel = 3;
    final int column = 4;
    final int row = 5;
    final byte[] bytes = getGarbageBytes();

    final Entry in = new Entry(zoomLevel, column, row, bytes);
    final StorageResult result = storage.put(Observable.just(in)).blockingFirst();
    final Entry out = storage.getEntries().blockingFirst();
    assertEquals(in, out);
    assertEquals(result.getEntry(), out);

    final int zoomLevel2 = 9;
    final int column2 = 1;
    final int row2 = 1;
    final byte[] bytes2 = getGarbageBytes("test2");

    final Entry in2 = new Entry(zoomLevel2, column2, row2, bytes2);
    final StorageResult result2 = storage.put(Observable.just(in2)).blockingFirst();
    final Entry out2 = storage.getEntry(zoomLevel2, column2, row2).blockingFirst();
    assertEquals(in2, out2);
    assertEquals(result2.getEntry(), out2);
  }

  @Test
  public void deleteAnEntry() throws IOException {
    final File file = provideNonExistentTestFileOrBlow();
    final StorageImpl storage = new StorageImpl.Builder(file).createIfNotExist().build();

    final Entry in = createEntries(1).blockingFirst();
    storage.putEntries(Observable.just(in));
    final Entry out = storage.getEntries().blockingFirst();
    assertEquals(in, out);
    StorageResult first = storage.delete(Observable.just(out)).blockingFirst();
    assertTrue(first.isCompleted());
  }

  @Test
  public void deleteAnEntryNonExistent() throws IOException {
    final File file = provideNonExistentTestFileOrBlow();
    final StorageImpl storage = new StorageImpl.Builder(file).createIfNotExist().build();

    final Entry in = createEntries(1).blockingFirst();
    storage.putEntries(Observable.just(in));
    final Entry out = storage.getEntries().blockingFirst();
    assertEquals(in, out);

    Entry nonExistentEntry = new Entry(17,9,1, getGarbageBytes(""));
    StorageResult first = storage.delete(Observable.just(out, nonExistentEntry)).blockingFirst();
    assertTrue(first.isCompleted());
  }

  @Test
  public void deleteEntries() throws IOException {
    final File file = provideNonExistentTestFileOrBlow();
    final StorageImpl storage = new StorageImpl.Builder(file).createIfNotExist().build();
    storage.putEntries(createEntries(1000));
    storage.delete(createEntries(400)).subscribe();
    long actualRemaining = storage.getEntries().count().blockingGet();
    long expectedRemaining = 600L;
    assertEquals(expectedRemaining, actualRemaining);
  }

  private static Observable<Entry> createEntries(int count) {
    List<Entry> entries = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      entries.add(new Entry(22, i, i, getGarbageBytes("")));
    }
    return Observable.fromIterable(entries);
  }

  @AfterClass
  public static void cleanup() {
    provideNonExistentTestFileOrBlow();
  }

  private static File provideNonExistentTestFileOrBlow() {
    return provideNonExistentTestFileOrBlow("testing.mbtiles");
  }

  private static File provideNonExistentTestFileOrBlow(String filename) {
    final File file = new File(filename);
    if (file.exists() && !file.delete()) {
      throw new IllegalStateException(
          "problem with integration test environment - dirty filesystem");
    }
    return file;
  }

  private static byte[] getGarbageBytes() {
    return getGarbageBytes("test");
  }

  private static byte[] getGarbageBytes(String string) {
    try {
      return string.getBytes("UTF-8");
    } catch (UnsupportedEncodingException uee) {
      return new byte[]{};
    }
  }
}

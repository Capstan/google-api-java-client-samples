/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.storage.examples;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.api.services.samples.storage.examples.BucketsGetExample;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.Storage.Buckets;
import com.google.api.services.storage.Storage.Buckets.Get;
import com.google.api.services.storage.model.Bucket;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

/** Test for example class exemplifying retrieving bucket metadata. */
public class BucketsGetExampleTest {

  @Test
  public void testGet() throws IOException {
    final String bucketName = "mahbukkit";
    Storage storage = Mockito.mock(Storage.class);
    Buckets bucketsCollection = Mockito.mock(Buckets.class);
    Get getRequest = Mockito.mock(Get.class);
    Bucket bucketResponse = new Bucket().setName(bucketName);
    when(storage.buckets()).thenReturn(bucketsCollection);
    when(bucketsCollection.get(bucketName)).thenReturn(getRequest);
    when(getRequest.execute()).thenReturn(bucketResponse);
    Bucket response = BucketsGetExample.get(storage, bucketName);
    assertEquals(bucketResponse, response);
    verify(storage).buckets();
    verify(bucketsCollection).get(bucketName);
    verify(getRequest).setProjection("full");
    verify(getRequest).execute();
    verifyNoMoreInteractions(storage, bucketsCollection, getRequest);
  }

}

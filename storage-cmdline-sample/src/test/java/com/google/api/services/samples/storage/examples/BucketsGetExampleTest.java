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
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.samples.storage.util.CredentialsProvider;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.Storage.Buckets;
import com.google.api.services.storage.Storage.Buckets.Get;
import com.google.api.services.storage.model.Bucket;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

/** Test for example class exemplifying retrieving bucket metadata. */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BucketsGetExample.class, CredentialsProvider.class, Storage.Builder.class})
@PowerMockIgnore("javax.net.ssl.*")
public class BucketsGetExampleTest {
  
  @Mock private Storage storage;
  @Mock private Buckets bucketsCollection;
  @Mock private Get getRequest;

  @Before public void initCommonStubs() throws IOException {
    when(storage.buckets()).thenReturn(bucketsCollection);
    when(bucketsCollection.get(anyString())).thenReturn(getRequest);
  }
  
  @Test
  public void testGetSuccess() throws IOException {
    final String bucketName = "mahbukkit";
    Bucket bucketResponse = new Bucket().setName(bucketName);
    when(getRequest.execute()).thenReturn(bucketResponse);
    Bucket response = BucketsGetExample.get(storage, bucketName);
    assertEquals(bucketResponse, response);
    verify(storage).buckets();
    verify(bucketsCollection).get(bucketName);
    verify(getRequest).setProjection("full");
    verify(getRequest).execute();
    verifyNoMoreInteractions(storage, bucketsCollection, getRequest);
  }
  
  @Test
  public void testGetNotFound() throws IOException {
    final String bucketName = "notthere";
    GoogleJsonResponseException notFound = Mockito.mock(GoogleJsonResponseException.class);
    when(getRequest.execute()).thenThrow(notFound);
    try {
      BucketsGetExample.get(storage, bucketName);
    } catch (GoogleJsonResponseException ex) {
      assertSame(notFound, ex);
    }
    verify(storage).buckets();
    verify(bucketsCollection).get(bucketName);
    verify(getRequest).setProjection("full");
    verify(getRequest).execute();
    verifyNoMoreInteractions(storage, bucketsCollection, getRequest);
  }
  
  @Test
  public void testMain() throws Exception {
    PowerMockito.mockStatic(BucketsGetExample.class, CredentialsProvider.class);
    PowerMockito.doCallRealMethod().when(BucketsGetExample.class);
    BucketsGetExample.main(any(String[].class));
    Credential credential = Mockito.mock(Credential.class);
    when(CredentialsProvider.authorize(any(HttpTransport.class), any(JsonFactory.class)))
        .thenReturn(credential);
    Storage.Builder storageBuilder = PowerMockito.mock(Storage.Builder.class);
    whenNew(Storage.Builder.class).withAnyArguments().thenReturn(storageBuilder);
    when(storageBuilder.setApplicationName(anyString())).thenReturn(storageBuilder);
    when(storageBuilder.build()).thenReturn(storage);
    when(BucketsGetExample.get(any(Storage.class), anyString())).thenReturn(new Bucket());
    BucketsGetExample.main(null);
    PowerMockito.verifyStatic();
    BucketsGetExample.main(any(String[].class));
    PowerMockito.verifyStatic();
    CredentialsProvider.authorize(any(HttpTransport.class), any(JsonFactory.class));
    PowerMockito.verifyNew(Storage.Builder.class).withArguments(any(HttpTransport.class),
        any(JsonFactory.class), eq(credential));
    PowerMockito.verifyStatic();
    BucketsGetExample.get(any(Storage.class), anyString());
  }

}

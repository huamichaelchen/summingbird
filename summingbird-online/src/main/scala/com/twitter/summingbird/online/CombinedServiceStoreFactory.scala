/*
 Copyright 2013 Twitter, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.twitter.summingbird.online

import com.twitter.summingbird.batch.{ Batcher, BatchID }
import com.twitter.storehaus.ReadableStore
import com.twitter.storehaus.algebra.Mergeable

trait CombinedServiceStoreFactory[-K, V] extends MergeableStoreFactory[(K, BatchID), V] with OnlineServiceFactory[K, V]

object CombinedServiceStoreFactory {

  def apply[K, V](mStore: () => Mergeable[(K, BatchID), V], b: Batcher) = {
    new CombinedServiceStoreFactory[K, V] {
      def mergeableStore = mStore
      def mergeableBatcher = b
      def serviceStore = () => ReadableStore.empty
    }
  }

  def apply[K, V](mStore: () => Mergeable[(K, BatchID), V], b: Batcher, sStore: () => ReadableStore[K, V]) = {
    new CombinedServiceStoreFactory[K, V] {
      def mergeableStore = mStore
      def mergeableBatcher = b
      def serviceStore = sStore
    }
  }
}
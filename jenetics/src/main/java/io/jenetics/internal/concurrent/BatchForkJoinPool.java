/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.internal.concurrent;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import io.jenetics.util.Seq;

/**
 * This executor uses a ForkJoinPool.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since 2.0
 */
public final class BatchForkJoinPool extends BatchExec {

	public BatchForkJoinPool(final ForkJoinPool pool) {
		super(pool);
	}

	@Override
	public void execute(final Seq<? extends Runnable> batch) {
		if (batch.nonEmpty()) {
			final var future = ((ForkJoinPool)_executor)
				.submit(new BatchAction(batch));

			final var futures = new ArrayList<Future<?>>();
			futures.add(future);
			Futures.join(futures);
		}
	}

}

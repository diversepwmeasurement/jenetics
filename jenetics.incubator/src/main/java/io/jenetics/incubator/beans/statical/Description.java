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
package io.jenetics.incubator.beans.statical;

/**
 * A {@code Description} object describes the <em>statical</em>, at compile time
 * available property of a Java Bean or a record class.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public sealed interface Description
	extends Comparable<Description>
	permits IndexedDescription, SimpleDescription
{

	/**
	 * The name of the property. Usually the field name.
	 *
	 * @return the name of the property
	 */
	String name();

	/**
	 * The compile time type of the property.
	 *
	 * @return the compile time type of the property
	 */
	Class<?> type();


	/**
	 * Return {@code true} if this property can be updated.
	 *
	 * @return {@code true} if this property can be updated, {@code false}
	 *         otherwise
	 */
	boolean isWriteable();

	@Override
	default int compareTo(final Description o) {
		return name().compareTo(o.name());
	}

}

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
package io.jenetics.incubator.beans.reflect;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

/**
 * Trait which represents a bean type.
 *
 * @param type the type object
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 8.0
 * @since 8.0
 */
public record BeanType(Class<?> type) implements StructType {

	@Override
	public Stream<Component> components() {
		final PropertyDescriptor[] descriptors;
		try {
			descriptors = Introspector.getBeanInfo(type).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException("Can't introspect class '%s'.".formatted(type), e);
		}

		return Stream.of(descriptors)
			.filter(pd -> pd.getReadMethod() != null)
			.filter(pd -> pd.getReadMethod().getReturnType() != Class.class)
			.map(pd -> new Component(
				pd.getReadMethod().getDeclaringClass(),
				pd.getName(),
				pd.getReadMethod().getGenericReturnType(),
				pd.getReadMethod(),
				pd.getWriteMethod())
			);
	}

	/**
	 * Return a {@code BeanType} instance if the given {@code type} is a bean
	 * class.
	 * {@snippet lang = "java":
	 * final Type type = null; // @replace substring='null' replacement="..."
	 * if (BeanType.of(type) instanceof BeanType bt) {
	 *     System.out.println(bt);
	 * }
	 * }
     *
     * @param type the type object
     * @return an {@code ListType} if the given {@code type} is a bean type, or
     * {@code null}
     */
    public static Trait of(final Type type) {
        if (type instanceof ParameterizedType pt &&
            pt.getRawType() instanceof Class<?> rt
        ) {
            return new BeanType(rt);
        } else if (type instanceof Class<?> cls) {
            return new BeanType(cls);
        } else {
            return null;
        }
    }
}

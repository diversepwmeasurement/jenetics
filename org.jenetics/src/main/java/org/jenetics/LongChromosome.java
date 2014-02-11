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
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package org.jenetics;

import static org.jenetics.util.object.hashCodeOf;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.jenetics.util.Array;
import org.jenetics.util.ISeq;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version @__version__@ &mdash; <em>$Date: 2014-02-11 $</em>
 * @since @__version__@
 */
public class LongChromosome
	extends NumericChromosome<Long, LongGene>
	implements Serializable
{
	private static final long serialVersionUID = 1L;


	protected LongChromosome(final ISeq<LongGene> genes) {
		super(genes);
	}

	/**
	 * Create a new random {@code LongChromosome}.
	 *
	 * @param min the min value of the {@link LongGene}s (inclusively).
	 * @param max the max value of the {@link LongGene}s (inclusively).
	 * @param length the length of the chromosome.
	 * @throws NullPointerException if one of the arguments is {@code null}.
	 */
	public LongChromosome(final Long min, final Long max, final int length) {
		this(
			new Array<LongGene>(length).fill(LongGene.of(min, max)).toISeq()
		);
		_valid = true;
	}

	/**
	 * Create a new random {@code LongChromosome} of length one.
	 *
	 * @param min the minimal value of this chromosome (inclusively).
	 * @param max the maximal value of this chromosome (inclusively).
	 * @throws NullPointerException if one of the arguments is {@code null}.
	 */
	public LongChromosome(final Long min, final Long max) {
		this(min, max, 1);
	}

	/**
	 * Create a new random {@code LongChromosome}.
	 *
	 * @param min the min value of the {@link LongGene}s (inclusively).
	 * @param max the max value of the {@link LongGene}s (inclusively).
	 * @param length the length of the chromosome.
	 */
	public static LongChromosome of(final long min, final long max, final int length) {
		return new LongChromosome(min, max, length);
	}

	/**
	 * Create a new random {@code LongChromosome} of length one.
	 *
	 * @param min the minimal value of this chromosome (inclusively).
	 * @param max the maximal value of this chromosome (inclusively).
	 */
	public static LongChromosome of(final long min, final long max) {
		return new LongChromosome(min, max);
	}

	@Override
	public LongChromosome newInstance(final ISeq<LongGene> genes) {
		return new LongChromosome(genes);
	}

	@Override
	public LongChromosome newInstance() {
		return new LongChromosome(_min, _max, length());
	}

	@Override
	public int hashCode() {
		return hashCodeOf(getClass()).and(super.hashCode()).value();
	}

	@Override
	public boolean equals(final Object o) {
		return o == this || o instanceof LongChromosome && super.equals(o);
	}

	/* *************************************************************************
	 *  Java object serialization
	 * ************************************************************************/

	private void writeObject(final ObjectOutputStream out)
		throws IOException
	{
		out.defaultWriteObject();

		out.writeInt(length());
		out.writeLong(_min.longValue());
		out.writeLong(_max.longValue());

		for (LongGene gene : _genes) {
			out.writeLong(gene.longValue());
		}
	}

	private void readObject(final ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		final Array<LongGene> genes = new Array<>(in.readInt());
		_min = in.readLong();
		_max = in.readLong();

		for (int i = 0; i < genes.length(); ++i) {
			genes.set(i, new LongGene(in.readLong(), _min, _max));
		}

		_genes = genes.toISeq();
	}
}

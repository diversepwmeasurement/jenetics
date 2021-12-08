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
package io.jenetics.incubator.grammar;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.jenetics.incubator.grammar.Cfg.Symbol;
import io.jenetics.incubator.grammar.Cfg.Terminal;
import io.jenetics.incubator.grammar.Sentence.Expansion;
import io.jenetics.incubator.grammar.bnf.Bnf;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 */
public class SentenceTest {

	static final Cfg CFG = Bnf.parse("""
		<expr> ::= ( <expr> <op> <expr> ) | <num> | <var> |  <fun> ( <arg>, <arg> )
		<fun>  ::= FUN1 | FUN2
		<arg>  ::= <expr> | <var> | <num>
		<op>   ::= + | - | * | /
		<var>  ::= x | y
		<num>  ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
		"""
	);

	@Test
	public void create() {
		final var random = new Random(-8564585140851778291L);

		var sentence = new LinkedList<Symbol>();
		Sentence.expand(CFG, random::nextInt, sentence, Expansion.LEFT_FIRST);

		var list = sentence.stream()
			.map(Terminal.class::cast)
			.toList();

		var string = list.stream()
			.map(Symbol::value)
			.collect(Collectors.joining());

		//System.out.println(string);

		////////////////////////////////////////////////////////////////////////

		random.setSeed(29022156195143L);
		sentence.clear();
		Sentence.expand(CFG, random::nextInt, sentence, Expansion.LEFT_TO_RIGHT);

		list = sentence.stream()
			.map(Terminal.class::cast)
			.toList();

		string = list.stream()
			.map(Symbol::value)
			.collect(Collectors.joining());

		//System.out.println(string);
		//System.out.println();
	}

	@Test(dataProvider = "sentencesLeftToRight")
	public void compatibleLeftToRightSentenceGeneration(
		final long seed,
		final String sentence
	) {
		compatibleSentenceGeneration(seed, sentence, Expansion.LEFT_TO_RIGHT);
	}

	@Test(dataProvider = "sentencesLeftFirst")
	public void compatibleLeftFirstSentenceGeneration(
		final long seed,
		final String sentence
	) {
		compatibleSentenceGeneration(seed, sentence, Expansion.LEFT_FIRST);
	}

	//@Test
	public void generateSentences() {
		final var random = new Random(124567);
		for (int i = 0; i < 100; ++i) {
			final var seed = random.nextLong();
			final var rand = new Random(seed);
			final List<Terminal> sentence = Sentence.generate(CFG, rand::nextInt, Expansion.LEFT_FIRST);
			final String string = sentence.stream()
				.map(Symbol::value)
				.collect(Collectors.joining());

			System.out.println(seed + "\t" + string);
		}
	}

	private void compatibleSentenceGeneration(
		final long seed,
		final String sentence,
		Expansion expansion
	) {
		final var random = new Random(seed);
		final var terminals = Sentence.generate(CFG, random::nextInt, expansion);

		final String string = terminals.stream()
			.map(Symbol::value)
			.collect(Collectors.joining());
		assertThat(string).isEqualTo(sentence);
	}

	@DataProvider
	public Object[][] sentencesLeftToRight() throws IOException {
		return read("/io/jenetics/incubator/grammar/sentences-left_to_right.csv");
	}

	@DataProvider
	public Object[][] sentencesLeftFirst() throws IOException {
		return read("/io/jenetics/incubator/grammar/sentences-left_first.csv");
	}

	private static Object[][] read(final String resource) throws IOException {
		final List<Object[]> values = new ArrayList<>();
		try (var in = SentenceTest.class.getResourceAsStream(resource);
			 var reader = new InputStreamReader(in);
			 var br = new BufferedReader(reader))
		{
			String line;
			while ((line = br.readLine()) != null) {
				final var parts = line.split("\t");
				values.add(new Object[] {Long.parseLong(parts[0]), parts[1]});
			}
		}

		return values.toArray(Object[][]::new);
	}

}

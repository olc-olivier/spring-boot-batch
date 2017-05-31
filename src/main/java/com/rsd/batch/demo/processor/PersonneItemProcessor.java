package ch.hes.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import ch.hes.domain.Personne;

public class PersonneItemProcessor implements ItemProcessor<Personne, Personne> {

	@Override
	public Personne process(Personne personne) throws Exception {
		//nothing to do...
		return personne;
	}

}

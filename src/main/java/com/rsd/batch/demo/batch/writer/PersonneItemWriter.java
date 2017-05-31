package ch.hes.batch.writer;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import ch.hes.domain.Personne;

public class PersonneItemWriter implements ItemWriter<Personne> {

	@Autowired
	private EntityManager entityManager;

	@Override
	public void write(List<? extends Personne> items) throws Exception {
		if (items.get(0) != null) {
			Personne personne = items.get(0);
			entityManager.persist(personne);
			entityManager.flush();
		}
	}

}

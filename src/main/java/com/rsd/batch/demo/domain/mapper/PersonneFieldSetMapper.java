package ch.hes.domain.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import ch.hes.domain.Personne;

/**
 * Mapping for "id","nom","prenom","civilite".
 */
public class PersonneFieldSetMapper implements FieldSetMapper<Personne> {

	@Override
	public Personne mapFieldSet(FieldSet fieldSet) throws BindException {
		Personne personne = new Personne();
		personne.setId(Integer.valueOf(fieldSet.readString("id")));
		personne.setNom(fieldSet.readString("nom"));
		personne.setPrenom(fieldSet.readString("prenom"));
		personne.setCivilite(fieldSet.readString("civilite"));
		return personne;
	}

}
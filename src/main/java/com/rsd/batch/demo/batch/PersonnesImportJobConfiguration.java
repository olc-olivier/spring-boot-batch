package ch.hes.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import ch.hes.batch.common.listerner.LogProcessListener;
import ch.hes.batch.common.listerner.ProtocolListener;
import ch.hes.batch.processor.PersonneItemProcessor;
import ch.hes.batch.writer.PersonneItemWriter;
import ch.hes.domain.Personne;
import ch.hes.domain.mapper.PersonneFieldSetMapper;

@Configuration
@EnableBatchProcessing
// @Import({DatabaseAccessConfiguration.class, ServicesConfiguration.class})
public class PersonnesImportJobConfiguration {
	
	@Autowired
	private JobBuilderFactory jobs;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
		
	@Bean
	public Job addPersonneJob(){
		return jobs.get("addPersonneJob")
				.listener(protocolListener())
				.start(step())
				.build();
	}	
	
	@Bean
	public Step step(){
		return stepBuilderFactory.get("step")
				.<Personne,Personne>chunk(1) //important to be one in this case to commit after every line read
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.listener(logProcessListener())
				.faultTolerant()
				//.skipLimit(10) //default is set to 0
				//.skip(MySQLIntegrityConstraintViolationException.class)
				.build();
	}
	
	@Bean
	public ItemReader<Personne> reader() {
		FlatFileItemReader<Personne> reader = new FlatFileItemReader<Personne>();
		// reader.setLinesToSkip(1);first line is title definition
		reader.setResource(new ClassPathResource("personnes.txt"));
		reader.setLineMapper(lineMapper());
		return reader;
	}

	@Bean
	public LineMapper<Personne> lineMapper() {
		DefaultLineMapper<Personne> lineMapper = new DefaultLineMapper<Personne>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] { "id", "nom", "prenom", "civilite" });

		BeanWrapperFieldSetMapper<Personne> fieldSetMapper = new BeanWrapperFieldSetMapper<Personne>();
		fieldSetMapper.setTargetType(Personne.class);

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(personneFieldSetMapper());

		return lineMapper;
	}

	@Bean
	public PersonneFieldSetMapper personneFieldSetMapper() {
		return new PersonneFieldSetMapper();
	}

	@Bean
	public ItemProcessor<Personne, Personne> processor() {
		return new PersonneItemProcessor();
	}

	@Bean
	public ItemWriter<Personne> writer() {
		return new PersonneItemWriter();
	}
	
	@Bean
	public ProtocolListener protocolListener(){
		return new ProtocolListener();
	}

	@Bean
	public LogProcessListener logProcessListener(){
		return new LogProcessListener();
	}

}

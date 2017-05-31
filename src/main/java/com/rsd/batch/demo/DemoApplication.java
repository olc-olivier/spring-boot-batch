package com.rsd.batch.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	
	private static final String PERSONNE_JOB = "addPersonneJob";

	public static void main(String[] args) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		Log log = LogFactory.getLog(HesBatchApplication.class);
		SpringApplication app = new SpringApplication(HesBatchApplication.class);
		app.setWebEnvironment(false);
		ConfigurableApplicationContext ctx= app.run(args);
		Job addNewPodcastJob = ctx.getBean(PERSONNE_JOB, Job.class);
    	JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
		log.info("*********** Processing start. **************");
		JobExecution jobExecution = jobLauncher.run(addNewPodcastJob, jobParameters);
		log.info("*********** Processing end. **************");
		System.exit(0);
	}
}

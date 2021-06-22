package scanner.streaming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import scanner.streaming.Model.PdfNotif;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SdaPdfNotifApplication {

/* 
  data is being generated in order to load it to kafka topic, to simulate an real environment.
  Some of the files are pdf, some are not. 
  The files that are not pdf will be ignored, the pdf ones will be checked for vlacklisted iban codes and ignored in case the ibans are present on the blacklist
*/
	private static final Logger log = LoggerFactory.getLogger(SdaPdfNotifApplication.class);

	private List<String> urls = Arrays.asList("pdf1.pdf", "pdf2.pdf", "pdf3.pdf");
	private List<String> fileTypes = Arrays.asList("pdf", "csv", "txt");
	public static void main(String[] args) {
		SpringApplication.run(SdaPdfNotifApplication.class, args);
	}

	@Bean
	public Supplier<PdfNotif> notifPusher(){
  
	  Supplier<PdfNotif> notif = () -> {
		PdfNotif pdfNotif = new PdfNotif(urls.get(new Random().nextInt(urls.size())),
		fileTypes.get(new Random().nextInt(fileTypes.size())));

		log.info("url: {} - fileType: {}", pdfNotif.getUrl(), pdfNotif.getFileType());
		return pdfNotif;
	  };
  
	  return notif;
	}

}

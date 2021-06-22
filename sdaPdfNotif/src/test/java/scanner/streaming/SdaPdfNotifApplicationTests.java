package scanner.streaming;

import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.assertNotNull;
import scanner.streaming.Model.PdfNotif;

class SdaPdfNotifApplicationTests {

	@Test
	public void notifPusher() {
		SdaPdfNotifApplication app = new SdaPdfNotifApplication();
	  Supplier<PdfNotif> pdfNotif = app.notifPusher();
	  assertNotNull(pdfNotif);
	  assertNotNull(pdfNotif.get());
	  assertNotNull(pdfNotif.get().getUrl());
	  assertNotNull(pdfNotif.get().getFileType());
	}
}

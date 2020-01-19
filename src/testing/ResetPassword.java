package testing;

import lebah.mail.MailerDaemon;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import lebah.util.PasswordService;
import portal.module.entity.Users;
import bph.mail.mailer.LupaKataLaluanMailer;

public class ResetPassword extends LebahModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5182765690395072644L;
	private static DbPersistence db = new DbPersistence();

	@Override
	public String start() {
		// TODO Auto-generated method stub
		return getPath() + "/start.vm";
	}

	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/testing/resetPassword";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Reset Password Start...");

		MailerDaemon.getInstance().start("sbbph.bph@gmail.com", "sbbph12345");
		passwordReset();

		System.out.println("Reset Password Finish...");
	}

	public static void passwordReset() {
		String[] myKad = { "faizal" }; /* Masukkan Seberapa banyak IC */

		if (myKad.length > 0) {
			for (int i = 0; i < myKad.length; i++) {
				Users u = db.find(Users.class, myKad[i]);

				db.begin();
				try {
					u.setUserPassword(PasswordService.encrypt(myKad[i]));

					db.commit();

					LupaKataLaluanMailer.get().hantarPassword(u.getEmel(),
							u.getId(), u.getId());

					System.out.println("Password bagi MyKad : " + myKad[i]
							+ " telah berjaya di reset");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("error: " + e.getStackTrace());
				}
			}
		} else {
			System.out
					.println("Sila pastikan No. MyKad di ruangan myKad telah diisi");
		}
	}

	@Command("passwordResetScreen")
	public String passwordResetScreen() {
		boolean success = false;

		String getKelompokMyKad = getParam("kelompokMyKad");

		String[] myKad = getKelompokMyKad.split(",");

		if (myKad.length > 0) {
			for (int i = 0; i < myKad.length; i++) {
				Users u = db.find(Users.class, myKad[i].trim());

				db.begin();
				try {
					u.setUserPassword(PasswordService.encrypt(myKad[i].trim()));

					db.commit();

					success = true;
					context.put("info", "Password bagi Kelompok MyKad : `"
							+ getKelompokMyKad + "` telah berjaya di reset");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					context.put("info", "error: " + e.getStackTrace());
				}
			}
		} else {
			context.put("info",
					"Sila pastikan No. MyKad di ruangan myKad telah diisi");
		}

		context.put("success", success);

		return getPath() + "/status.vm";
	}

}

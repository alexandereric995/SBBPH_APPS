package bph.portal;

import java.sql.SQLException;
import java.util.Random;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.util.PasswordService;
import portal.module.entity.Users;
import bph.mail.mailer.LupaKataLaluanMailer;
import db.persistence.MyPersistence;

public class FrmLupaKataLaluan extends LebahModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7176546284336535061L;
	private MyPersistence mp;
	private String path = "bph/portal/form_lupakatalaluan";

	public static void main(String args[]) {
		try {
			String pwd="12345";
			System.out.println("Encrypted=>"+PasswordService.encrypt(pwd));
		}catch (Exception e) {
			
		}
	}

	@Override
	public String start() {
		return path + "/main.vm";
	}

	@Command("checkIDNo")
	public String checkIDNo() throws Exception {		
		try {
			mp = new MyPersistence();
			String myKad = getParam("myKad");
			Users user = (Users) mp.get("select x from Users x where x.id = '"
					+ myKad.trim() + "' and x.flagDaftarSBBPH = 'Y'");
			context.put("pelanggan", user);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		return path + "/id_checking.vm";
	}

	@Command("checkEmail")
	public String checkEmail() throws Exception {
		try {
			mp = new MyPersistence();
			boolean generatePassword = false;
			String myKad = getParam("myKad");
			String email = getParam("email");
			
			Users user = (Users) mp.find(Users.class, myKad.trim());

			if (email.equalsIgnoreCase(user.getEmel())) {
				context.put("emelChecking", true);
				generatePassword = true;
			} else {
				context.put("emelChecking", false);
				generatePassword = false;
			}
			
			if (generatePassword) {
				Random r = new Random();
				String c = "";

				String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

				for (int i = 0; i < 7; i++) {
					c = c + alphabet.charAt(r.nextInt(alphabet.length()));
				}
				
				try {
					mp.begin();
					user.setUserPassword(PasswordService.encrypt(c));
					mp.commit();

					// GENERATE EMEL
					generateEmel(user, c);

				} catch (SQLException ex) {
					System.out.println("ERROR RESET PASSWORD : " + ex.getMessage());
					ex.printStackTrace();
				}
			}
			context.put("pelanggan", user);			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return path + "/email_checking.vm";
	}

	private void generateEmel(Users user, String password) {
		if (user.getRole().getName().equals("(AWAM) Pengguna Awam")) {
			LupaKataLaluanMailer.get().hantarPassword(user.getEmel(),
					user.getId(), password);
		} else if (user.getRole().getName().equals("(AWAM) Penjawat Awam")) {
			if (user.getFlagSemakanHRMIS().equals("Y")) {
				LupaKataLaluanMailer.get().hantarPassword(user.getEmel(),
						user.getId(), password);
			} else {
				LupaKataLaluanMailer.get().hantarSemakan(user.getEmel());
			}
		} else if (user.getRole().getName().equals("(AWAM) Pesara")) {
			if (user.getFlagSemakanPESARA().equals("Y")) {
				LupaKataLaluanMailer.get().hantarPassword(user.getEmel(),
						user.getId(), password);
			} else {
				LupaKataLaluanMailer.get().hantarSemakan(user.getEmel());
			}
		} else if (user.getRole().getName().equals("(AWAM) Badan Berkanun")
				|| user.getRole().getName()
						.equals("((AWAM) Pesara Polis / Tentera")
				|| user.getRole().getName()
						.equals("((AWAM) Pesara Polis / Tentera")) {
			if (user.getFlagAktif().equals("Y")) {
				LupaKataLaluanMailer.get().hantarPassword(user.getEmel(),
						user.getId(), password);
			} else {
				LupaKataLaluanMailer.get().hantarPasswordBadanBerkanun(
						user.getEmel(), user.getId(), password);
			}
		} else {
			LupaKataLaluanMailer.get().hantarPassword(user.getEmel(),
					user.getId(), password);
		}
	}
}

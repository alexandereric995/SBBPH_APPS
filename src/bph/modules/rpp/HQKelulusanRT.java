package bph.modules.rpp;

import java.util.List;

import bph.entities.rpp.RppPeranginan;
import db.persistence.MyPersistence;

public class HQKelulusanRT extends SUBKelulusanRP{

	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	
	@SuppressWarnings("unchecked")
	@Override
	public void listPeranginan() {
		try {
			mp = new MyPersistence();
			//List<RppPeranginan> lst = db.list("select x from RppPeranginan x where x.jenisPeranginan.id = 'RT' and x.flagKelulusanSub = 'Y' ");
			List<RppPeranginan> list = mp.list("select x from RppPeranginan x where x.jenisPeranginan.id in ('RT') and x.id != '17' order by x.namaPeranginan asc");
			context.put("selectPeranginan", list);
		} catch (Exception e) {
			System.out.println("Error listPeranginan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
	}
	
	@Override
	public void diffFiltering() {
		this.addFilter("rppPeranginan.jenisPeranginan.id = 'RT' ");
	}
	
}

package DAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OfferDao implements Dao<Offer> {
	
	HashMap<Long, Offer> OffersBuffer = new HashMap<Long, Offer>();
	long nextID = 1;
	int currentOffers = 0;
	String offersFileName = "Offers.csv";
	
	public OfferDao() {
		updateBuffer();
	}
	
	public void updateBuffer() {
		OffersBuffer = fileToOffers(offersFileName);
		updateFile(offersFileName);
	}
	
	private HashMap<Long, Offer> fileToOffers(String fileName) {
		List<List<String>> tmp = this.getFile(fileName);
		HashMap<Long, Offer> out = new HashMap<Long, Offer>();
		if(tmp == null) {
			return out;
		}
		nextID = Long.parseLong(tmp.get(1).get(1));
		currentOffers = Integer.parseInt(tmp.get(1).get(0));
		for(int i = 3; i < currentOffers+3; i++) {
			List<String> line = tmp.get(i);
			out.put(Long.parseLong(line.get(0)),new Offer(Long.parseLong(line.get(0)), line.get(1), line.get(2), Integer.parseInt(line.get(3))));
		}
		
		return out;
	}
	
	private void updateFile(String fileName) {
		List<List<String>> data = new ArrayList<List<String>>();
		data.add(Arrays.asList("NumberOfOffersCurrently;NextID".split(";")));
		List<String> meta = new ArrayList<String>();
		meta.add(String.valueOf(currentOffers));
		meta.add(String.valueOf(nextID));
		data.add(meta);
		data.add(Arrays.asList("OfferID;Name;Unit;CostPerUnit".split(";")));
		OffersBuffer.values().forEach(offer -> {
			data.add(Arrays.asList(offer.toString().split(";")));
		});
		try {
			this.overwriteFile(fileName, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void add(Offer t) {
		OffersBuffer.put(t.getId(), t);
		currentOffers++;
		updateFile(offersFileName);
	}

	@Override
	public Offer get(long id) {
		updateBuffer();
		return OffersBuffer.get(id);
	}

	@Override
	public List<Offer> getAll() {
		updateBuffer();
		return new ArrayList<Offer>(OffersBuffer.values());
	}

	@Override
	public void update(Offer t) {
		OffersBuffer.replace(t.getId(), t);
		updateFile(offersFileName);
	}

	@Override
	public void delete(Offer t) {
		delete(t.getId());
	}

	@Override
	public void delete(long id) {
		OffersBuffer.remove(id);
		currentOffers--;
		updateFile(offersFileName);
	}
	
	public long getNextID() {
		return nextID++;
	}

}

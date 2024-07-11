package com.dcode7.iwell;

import java.io.InputStreamReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.dcode7.iwell.common.pincode.Pincode;
import com.dcode7.iwell.common.pincode.PincodeRepository;
import com.dcode7.iwell.user.service.UserServiceImpl;
import com.opencsv.CSVReader;

@Component
public class CSVDumpService implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private PincodeRepository pincodeRepository;

//	@Autowired
//	private IfscRepository ifscRepository;

	ClassPathResource pincodeCSV = new ClassPathResource("/static/pincode.csv");
//	ClassPathResource ifscCSV = new ClassPathResource("/static/IFSC.csv");

	@Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
	private Integer batchSize;

	private void dumpPincodeToDB() {
		long totalPincodeEntities = pincodeRepository.count();

		if (totalPincodeEntities > 0) {
			logger.info(String.format("Total existing Pincode entities: %d", totalPincodeEntities));
			return;
		}

		try (CSVReader csvReader = new CSVReader(new InputStreamReader(pincodeCSV.getInputStream()))) {

			String[] fields = null;

			// skip header fields
			csvReader.readNext();
			int count = 0;
			ArrayList<Pincode> pincodeArray = new ArrayList<>();

			while ((fields = csvReader.readNext()) != null) {
				// data clean-up
				for (int i = 0; i < fields.length; i++) {
					fields[i] = fields[i].trim();
				}
				// circle(0), region(1), division(2), office(3), pincode(4), type(5),
				// delivery(6), district(7), state(8)
				pincodeArray.add(new Pincode(fields[4], fields[3], fields[5], fields[6], fields[2], fields[1],
						fields[0], fields[7], fields[8]));

				if (pincodeArray.size() >= batchSize) {
					count += pincodeArray.size();
					pincodeRepository.saveAll(pincodeArray);
					pincodeArray.clear();
					logger.info(String.format("inserted %d records", count));
				}
			}
			// flush remaining Pin code entities as well
			if (pincodeArray.size() > 0) {
				pincodeRepository.saveAll(pincodeArray);
				pincodeArray.clear();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

//	private void dumpIfscToDB() {
//		long totalIfscEntities = ifscRepository.count();
//		
//		if (totalIfscEntities > 0) {
//			LOG.info(String.format("Total existing ifsc entities: %d", totalIfscEntities));
//			return;
//		}
//		String code;
//		String bank;
//		String branch;
//		String centre; 
//		String district;
//		String state;
//		String address;
//		String contact;
//		boolean imps;
//		boolean rtgs;
//		String city;
//		String iso3166;
//		boolean neft;
//		String micr;
//		boolean upi;
//		String swift;
//
//		try (CSVReader csvReader = new CSVReader(new InputStreamReader(ifscCSV.getInputStream()))) {
//
//			String[] line = null;
//			csvReader.readNext();
//
//			int count = 0;
//
//			ArrayList<Ifsc> ifscArray = new ArrayList<>();
//
//			while ((line = csvReader.readNext()) != null) {
////				logger.debug(String.join(", ", line));
////				System.out.print(String.join(", ", line));
//
////				if (line.length < 10) {
////					int j = 0;
////					for (String l : line) {
////						System.out.format("\tline[%d] = %s\n", j++, l);
////					}
////				} else {
////					System.out.format("count = %d, line.length = %d\n", count, line.length);
////				}
////				count++;
//				// data clean-up
//				for (int i = 0; i < line.length; i++) {
//					line[i] = line[i].trim();
//				}
//				code = line[1];
//				bank = line[0];
//				branch = line[2];
//				centre = line[3]; 
//				district = line[4];
//				state = line[5];
//				address = line[6];
//				contact = line[7];
//				imps = Boolean.parseBoolean(line[8]);
//				rtgs = Boolean.parseBoolean(line[9]);
//				city = line[10];
//				iso3166 = line[11];
//				neft = Boolean.parseBoolean(line[12]);
//				micr = line[13];
//				upi = Boolean.parseBoolean(line[14]);
//				swift = line[15];
//
//				ifscArray.add(new Ifsc(code, bank, branch, centre, district, state, address, contact, imps, rtgs, city, iso3166, neft, micr, upi, swift));
//
//				// flush the ifsc entities to persistence
//				if (ifscArray.size() >= batchSize) {
//					count += ifscArray.size();
//					ifscRepository.saveAll(ifscArray);
//					ifscArray.clear();
//					LOG.info(String.format("inserted %d records", count));
//				}
//			}
//			// flush remaining ifsc entities as well
//			if (ifscArray.size() > 0) {
//				ifscRepository.saveAll(ifscArray);
//				ifscArray.clear();
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		dumpPincodeToDB();
	}
}
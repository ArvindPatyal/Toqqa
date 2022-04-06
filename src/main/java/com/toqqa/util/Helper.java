package com.toqqa.util;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.toqqa.bo.FileBo;
import com.toqqa.domain.Attachment;
import com.toqqa.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class Helper {
	@Autowired
	private StorageService storageService;

	private static final Logger logger = LoggerFactory.getLogger(Helper.class);
	private static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss z";

	public String alphaNumericString(int len) {
		if (len < 6) {
			len = 6;
		}
		String capital = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String symbol = "!@#$";
		Random rnd = new Random();

		StringBuilder sb = new StringBuilder(len);
		sb.append(numericString(3));
		sb.append(symbol.charAt(rnd.nextInt(symbol.length())));
		for (int i = 0; i < len - 4; i++) {
			sb.append(capital.charAt(rnd.nextInt(capital.length())));
		}
		return sb.toString();
	}

	public String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	public Double roundOff(Double a) {
		double roundOff = Math.round(a * 100.0) / 100.0;
		return roundOff;
	}

	public Double roundOffTo4Decimals(Double a) {
		DecimalFormat df = new DecimalFormat("####0.0000");
		df.setRoundingMode(RoundingMode.DOWN);
		return Double.parseDouble(df.format(a));
	}

	public String numericString(int len) {
		String ab = "0123456789";
		Random rnd = new Random();

		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(ab.charAt(rnd.nextInt(ab.length())));
		}
		return sb.toString();
	}

	public Date addHoursToJavaUtilDate(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}

	public Date addMinsToJavaUtilDate(Date date, int mins) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, mins);
		return calendar.getTime();
	}

	/**
	 * true if string is not null and blank else false
	 * 
	 * @param input
	 * @return {@link Boolean}
	 * @author AKS
	 * @since V1.0.0_05112019
	 */
	public boolean notNullAndBlank(String input) {
		return !(input == null || input.isEmpty());
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public boolean notNullAndHavingData(Collection<?> input) {
		return !(input == null || input.isEmpty());
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public <T> boolean notNullAndHavingData(T[] input) {
		return !(input == null || input.length == 0);
	}

	public String dateToFormattedDate(Date d) {
//		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format.format(d);
		return formatted;

	}

	public String dateToEpoch(Date d) {
		SimpleDateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
//		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		DateFormat f = new SimpleDateFormat("yyyyMMddHHmm");
		String date = null;
		try {
//			date = df.parse(d);
			Date d1 = (Date) df.parse(d.toString());
			date = f.format(d1);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	public boolean isValidNumber(String number){
		Pattern pattern = Pattern.compile("^\\d{8,11}$");
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();
	}
	public String prepareResource(String location) {
		if (this.notNullAndBlank(location)) {
			return this.storageService.generatePresignedUrl(location);
		}
		return "";
	}

	public List<FileBo> prepareProductAttachments(List<Attachment> attachments){
		List<FileBo> atts = new ArrayList<>();
		attachments.forEach(att -> {
			atts.add(new FileBo(att.getId(),this.storageService.generatePresignedUrl(att.getLocation())));
		});
		return atts;
	}
	public List<String> prepareAttachments(List<Attachment> attachments){
		List<String> atts = new ArrayList<>();
		attachments.forEach(att -> {
			atts.add(this.storageService.generatePresignedUrl(att.getLocation()));
		});
		return atts;
	}
}

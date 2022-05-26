package com.toqqa.service.impls;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.OrderItemBo;
import com.toqqa.constants.FolderConstants;
import com.toqqa.domain.User;
import com.toqqa.exception.ResourceCreateUpdateException;
import com.toqqa.service.InvoiceService;
import com.toqqa.service.StorageService;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.itextpdf.text.pdf.BaseFont.COURIER;
import static com.itextpdf.text.pdf.BaseFont.COURIER_BOLD;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
	@Autowired
	private StorageService storageService;

	@Autowired
	private Helper helper;

	@Override
	@Async
	public void generateInvoice(OrderInfoBo orderInfoBo, User user) {
		log.info("Inside generate Invoice");
		Document document = new Document();
		File file = null;
		try {
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(orderInfoBo.getId() + ".pdf"));
			document.open();

			List<OrderItemBo> orderItemBos = orderInfoBo.getOrderItemBo();
			String address = orderInfoBo.getFirstName() + " " + orderInfoBo.getLastName() + "\n"
					+ orderInfoBo.getAddress().getAddress() + " ," + orderInfoBo.getAddress().getCity() + "\n"
					+ orderInfoBo.getAddress().getState() + "\n" + orderInfoBo.getAddress().getPostCode();

			/* TABLE FOR BASIC DETAILS */
			PdfPTable detailsTable = new PdfPTable(2);
			detailsTable.setWidthPercentage(100);
			detailsTable.setSpacingBefore(10f);
			detailsTable.setSpacingAfter(10f);

			/* BLANK CEL DECLARATION */
			PdfPCell blankCell = new PdfPCell();
			blankCell.setBorder(Rectangle.NO_BORDER);

			/* TOQQA LOGO */
			Image logo = Image.getInstance("src/main/resources/Images/TOQQALOGO.png");
			logo.setAbsolutePosition(35, 750);
			logo.scaleAbsolute(150, 90);
//            PdfPCell logoCell = new PdfPCell(logo);
//            logoCell.setBorder(Rectangle.NO_BORDER);
//            detailsTable.addCell(logo);
			document.add(logo);

			/* FONT DECLARATION BOLD */
			Font fontBold = new Font();
			fontBold.setFamily(COURIER_BOLD);
			fontBold.setColor(BaseColor.BLACK);
			fontBold.setStyle(0);

			/* FONT DECLARATION LIGHT */
			Font fontLite = new Font();
			fontLite.setColor(BaseColor.BLACK);
			fontLite.setFamily(COURIER);
			fontLite.setStyle(0);

			/* INVOICE */
			Paragraph invoiceText = new Paragraph("Invoice/Bill of supply/Cash memo\n(Original for Recipient)",
					fontBold);
			invoiceText.setAlignment(Element.ALIGN_RIGHT);
//            PdfPCell invoiceCell = new PdfPCell(invoiceText);
//            invoiceCell.setBorder(Rectangle.NO_BORDER);
//            detailsTable.addCell(invoiceCell);
			document.add(invoiceText);

			/* SOLD BY */
			String sellerdetails = orderInfoBo.getSmeBo().getNameOfBusiness() + "\n" + orderInfoBo.getSmeBo().getBusinessAddress();
			Paragraph seller = new Paragraph();
			seller.add(new Chunk("Sold By :", fontBold));
			seller.add(new Chunk("\n" + sellerdetails, fontLite));
			seller.setAlignment(Element.ALIGN_LEFT);
			PdfPCell soldByCell = new PdfPCell(seller);
			soldByCell.setBorder(Rectangle.NO_BORDER);
			detailsTable.addCell(soldByCell);

			/* BILLING ADDRESS */
			Paragraph billingAddress = new Paragraph();
			billingAddress.add(new Chunk("Billing Address :", fontBold));
			billingAddress.add(new Chunk("\n" + address, fontLite));
//            billingAddress.setAlignment(Element.ALIGN_RIGHT);
			PdfPCell billingCell = new PdfPCell(billingAddress);
			billingCell.setBorder(Rectangle.NO_BORDER);
			billingCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			detailsTable.addCell(billingCell);

			/* PAN WITH BLANK CELL */
			Paragraph pan = new Paragraph();
			pan.add(new Chunk("PAN : ", fontBold));
			pan.add(new Chunk("NA", fontLite));
			pan.setAlignment(Element.ALIGN_LEFT);
			PdfPCell panCell = new PdfPCell(pan);
			panCell.setBorder(Rectangle.NO_BORDER);
			detailsTable.addCell(blankCell);

			/* SHIPPING ADDRESS */
			Paragraph shippingAddress = new Paragraph();
			shippingAddress.add(new Chunk("Shipping Address :", fontBold));
			shippingAddress.add(new Chunk("\n" + address, fontLite));
//            shippingAddress.setAlignment(Element.ALIGN_RIGHT);
			PdfPCell shippingCell = new PdfPCell(shippingAddress);
			shippingCell.setBorder(Rectangle.NO_BORDER);
			shippingCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			detailsTable.addCell(shippingCell);

			/* REG NO WITH BLANK CELL */
			Paragraph regNo = new Paragraph();
			regNo.add(new Chunk("REG number", fontBold));
			regNo.add(new Chunk("\nNA", fontLite));
			regNo.setAlignment(Element.ALIGN_LEFT);
			PdfPCell regCell = new PdfPCell(regNo);
			regCell.setBorder(Rectangle.NO_BORDER);
			detailsTable.addCell(blankCell);

			/* BLANK CELL FOR SHIFTING ORDER NUMBER TO LEFT */
			detailsTable.addCell(blankCell);

			/* ORDER NUMBER */
			Paragraph orderNumber = new Paragraph();
			orderNumber.add(new Chunk("Order number :", fontBold));
			orderNumber.add(new Chunk("\n" + orderInfoBo.getId(), fontLite));
			orderNumber.setAlignment(Element.ALIGN_LEFT);
			PdfPCell orderNoCell = new PdfPCell(orderNumber);
			orderNoCell.setBorder(Rectangle.NO_BORDER);
			detailsTable.addCell(orderNoCell);

			/* ORDER DATE */
			Paragraph orderDate = new Paragraph();
			orderDate.add(new Chunk("Order Date :", fontBold));

			Date date = orderInfoBo.getCreatedDate();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy hh:mm");
			String strDate = formatter.format(date);

//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
//            Date formattedDate = simpleDateFormat.parse(String.valueOf(orderInfoBo.getCreatedDate()));

			orderDate.add(new Chunk("\n" + strDate, fontLite));
//            orderDate.setAlignment(Element.ALIGN_LEFT);
			PdfPCell orderDateCell = new PdfPCell(orderDate);
			orderDateCell.setBorder(Rectangle.NO_BORDER);
			orderDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			detailsTable.addCell(orderDateCell);

			document.add(detailsTable);

			/* TABLE FOR PRODUCT DETAILS */
			PdfPTable productDetailsTable = new PdfPTable(5);
			productDetailsTable.setWidthPercentage(100);
			productDetailsTable.setSpacingBefore(10f);
			productDetailsTable.setSpacingAfter(10f);

			/* CELLS FOR PRODUCT NAME,QUANTITY,PRICE PER UNIT,DISCOUNT, AMOUNT */
			PdfPCell productName = new PdfPCell(new Paragraph("Product Name", fontBold));
			productName.setHorizontalAlignment(Element.ALIGN_CENTER);
			productName.setBackgroundColor(new BaseColor(219, 219, 219));
			productDetailsTable.addCell(productName);

			PdfPCell quantity = new PdfPCell(new Paragraph("Quantity", fontBold));
			quantity.setHorizontalAlignment(Element.ALIGN_CENTER);
			quantity.setBackgroundColor(new BaseColor(219, 219, 219));
			productDetailsTable.addCell(quantity);

			PdfPCell pricePerUnit = new PdfPCell(new Paragraph("Price Per Unit"));
			pricePerUnit.setHorizontalAlignment(Element.ALIGN_CENTER);
			pricePerUnit.setBackgroundColor(new BaseColor(219, 219, 219));
			productDetailsTable.addCell(pricePerUnit);

			PdfPCell discountCell = new PdfPCell(new Paragraph("Discount"));
			discountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			discountCell.setBackgroundColor(new BaseColor(219, 219, 219));
			productDetailsTable.addCell(discountCell);


			PdfPCell productAmount = new PdfPCell(new Paragraph("Amount"));
			productAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
			productAmount.setBackgroundColor(new BaseColor(219, 219, 219));
			productDetailsTable.addCell(productAmount);

			AtomicReference<Double> totalDiscount = new AtomicReference<>(0.00);
			/* LOOP FOR ADDING PRODUCT DETAILS TO TABLE */
			orderItemBos.forEach(orderItemBo -> {

				PdfPCell cell1 = new PdfPCell(new Paragraph(orderItemBo.getProduct().getProductName()));
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell1.setBackgroundColor(BaseColor.WHITE);
				productDetailsTable.addCell(cell1);

				PdfPCell cell2 = new PdfPCell(new Paragraph(String.valueOf(orderItemBo.getQuantity())));
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell2.setBackgroundColor(BaseColor.WHITE);
				productDetailsTable.addCell(cell2);

				PdfPCell cell3 = new PdfPCell(
						new Paragraph(String.valueOf(orderItemBo.getProduct().getPricePerUnit())));
				cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell3.setBackgroundColor(BaseColor.WHITE);
				productDetailsTable.addCell(cell3);

				Double discount = (orderItemBo.getProduct().getDiscount() * orderItemBo.getProduct().getPricePerUnit()) / 100;
				totalDiscount.set(totalDiscount.get() + (discount * orderItemBo.getQuantity()));
				PdfPCell cell4 = new PdfPCell(
						new Paragraph(String.valueOf(discount)));
				cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell4.setBackgroundColor(BaseColor.WHITE);
				productDetailsTable.addCell(cell4);


				double productAmt = (orderItemBo.getProduct().getPricePerUnit() * orderItemBo.getQuantity()) - (discount * orderItemBo.getQuantity());
				PdfPCell cell5 = new PdfPCell(
						new Paragraph(String.valueOf(productAmt)));
				cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell5.setBackgroundColor(BaseColor.WHITE);
				productDetailsTable.addCell(cell5);

			});
			/* SHIPPING FEE */
			productDetailsTable.addCell(blankCell);
			productDetailsTable.addCell(blankCell);
			productDetailsTable.addCell(blankCell);

			PdfPCell shippingFeeCell = new PdfPCell(new Paragraph("Shipping Fee"));
			shippingFeeCell.setBorder(Rectangle.NO_BORDER);
			shippingFeeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			productDetailsTable.addCell(shippingFeeCell);

			PdfPCell shippingFeeValue = new PdfPCell(new Paragraph(String.valueOf(orderInfoBo.getShippingFee())));
			shippingFeeValue.setBorder(Rectangle.NO_BORDER);
			shippingFeeValue.setHorizontalAlignment(Element.ALIGN_CENTER);
			productDetailsTable.addCell(shippingFeeValue);

			/*  TOTAL DISCOUNT*/
			productDetailsTable.addCell(blankCell);
			productDetailsTable.addCell(blankCell);
			productDetailsTable.addCell(blankCell);

			PdfPCell discountFeeCell = new PdfPCell(new Paragraph("Total discount"));
			discountFeeCell.setBorder(Rectangle.NO_BORDER);
			discountFeeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			productDetailsTable.addCell(discountFeeCell);

			PdfPCell discountFeeValue = new PdfPCell(new Paragraph(String.valueOf(totalDiscount)));
			discountFeeValue.setBorder(Rectangle.NO_BORDER);
			discountFeeValue.setHorizontalAlignment(Element.ALIGN_CENTER);
			productDetailsTable.addCell(discountFeeValue);

			/* GROSS AMOUNT */
			productDetailsTable.addCell(blankCell);
			productDetailsTable.addCell(blankCell);
			productDetailsTable.addCell(blankCell);

			PdfPCell grossAmount = new PdfPCell(new Paragraph("Gross Amount"));
			grossAmount.setBorder(Rectangle.NO_BORDER);
			grossAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
			productDetailsTable.addCell(grossAmount);

			PdfPCell grossAmountValue = new PdfPCell(new Paragraph(String.valueOf(orderInfoBo.getAmount())));
			grossAmountValue.setBorder(Rectangle.NO_BORDER);
			grossAmountValue.setHorizontalAlignment(Element.ALIGN_CENTER);
			productDetailsTable.addCell(grossAmountValue);

			document.add(productDetailsTable);

			/* AUTHORIZED SIGNATORY */
			PdfPTable authorizedSignatoryTable = new PdfPTable(1);
			authorizedSignatoryTable.setWidthPercentage(100);
			authorizedSignatoryTable.setSpacingBefore(10f);
			authorizedSignatoryTable.setSpacingAfter(10f);

			PdfPCell authorizedSignatory = new PdfPCell(new Paragraph("Authorized Signatory", fontBold));
			authorizedSignatory.setBorder(Rectangle.NO_BORDER);
			authorizedSignatory.setHorizontalAlignment(Element.ALIGN_RIGHT);
			authorizedSignatoryTable.addCell(authorizedSignatory);

			document.add(authorizedSignatoryTable);

			document.close();
			pdfWriter.close();
			file = new File(orderInfoBo.getId().concat(".pdf"));

			/* STORING INVOICE ON AMAZON S3 CLOUD STORAGE */
			this.storageService.uploadFile(file, user.getId(), FolderConstants.INVOICE.getValue());

		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			throw new ResourceCreateUpdateException("Invoice generation failed");
		} finally {

			file.deleteOnExit();

		}

	}

	@Override
	public String fetchInvoice(String orderId, String userId) {

		return this.storageService.generatePresignedInvoiceUrl(orderId.concat(".pdf"), userId);
	}

	/*
	 * private void uploadInvoice(OrderInfoBo orderInfoBo ,Document document) { try
	 * { if (document!=null ) { this.storageService.uploadFileAsync(document, ,
	 * FolderConstants.INVOICE.getValue()); } } catch (Exception e) { throw new
	 * RuntimeException(e); } }
	 */
}

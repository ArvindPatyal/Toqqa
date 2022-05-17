package com.toqqa.service.impls;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.OrderItemBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.constants.OrderConstants;
import com.toqqa.constants.PaymentConstants;
import com.toqqa.domain.OrderInfo;
import com.toqqa.domain.OrderItem;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderItemPayload;
import com.toqqa.payload.OrderPayload;
import com.toqqa.repository.*;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.EmailService;
import com.toqqa.service.OrderInfoService;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.itextpdf.text.pdf.BaseFont.TIMES_ROMAN;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
public class OrderInfoServiceImpl implements OrderInfoService {

	@Autowired
	private OrderItemRepository orderItemRepo;

	@Autowired
	private OrderInfoRepository orderInfoRepo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private DeliveryAddressRepository addressRepo;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private Helper helper;

	@Value("${pageSize}")
	private Integer pageSize;

	@Autowired
	private EmailService emailService;

	@Autowired
	private CartRepository cartRepository;

	@Override
	public OrderInfoBo placeOrder(OrderPayload orderPayload) {
		log.info("Inside Add OrderInfo");
		User user = this.authenticationService.currentUser();
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setAmount(orderPayload.getAmount());
		orderInfo.setEmail(orderPayload.getEmail());
		orderInfo.setPhone(orderPayload.getPhone());
		orderInfo.setFirstName(orderPayload.getFirstName());
		orderInfo.setLastName(orderPayload.getLastName());
		orderInfo.setAddress(this.addressRepo.findById(orderPayload.getAddressId()).get());
		orderInfo.setUser(this.authenticationService.currentUser());
		orderInfo.setOrderStatus(OrderConstants.ORDER_PLACED.getValue());
		orderInfo.setPaymentType(PaymentConstants.CASH_ON_DELIVERY);
		orderInfo = this.orderInfoRepo.saveAndFlush(orderInfo);
		orderInfo.setOrderItems(this.persistOrderItems(orderPayload.getItems(), orderInfo));
		OrderInfoBo bo = new OrderInfoBo(orderInfo, this.fetchOrderItems(orderInfo));

		this.cartRepository.deleteByUser(user);

		if (orderInfo.getEmail() != null) {
			this.emailService.sendOrderEmail(orderInfo);
		} else {
			// TODO handle else case in future
		}
		return bo;
	}

	private List<OrderItem> persistOrderItems(List<OrderItemPayload> orderItems, OrderInfo order) {
		log.info("persist OrderItems");
		List<OrderItem> oItems = new ArrayList<OrderItem>();
		for (OrderItemPayload item : orderItems) {
			OrderItem parent = new OrderItem();

			parent.setPrice(item.getPrice());
			if (!this.helper.notNullAndBlank(item.getProductId())) {
				throw new BadRequestException("invalid product id " + item.getProductId());
			}
			parent.setProduct(this.productRepo.findById(item.getProductId()).get());
			parent.setQuantity(item.getQuantity());
			parent.setOrderInfo(order);
			parent = this.orderItemRepo.saveAndFlush(parent);
			oItems.add(parent);
		}
		return oItems;
	}

	private List<OrderItemBo> fetchOrderItems(OrderInfo orderInfo) {
		log.info("fetch OrderItems");
		List<OrderItem> items = this.orderItemRepo.findByOrderInfo(orderInfo);
		List<OrderItemBo> itemBos = new ArrayList<OrderItemBo>();
		items.forEach(item -> {
			itemBos.add(new OrderItemBo(item));
		});
		return itemBos;
	}

	@Override
	public OrderInfoBo fetchOrderInfo(String id) {
		log.info("Inside fetch order");
		Optional<OrderInfo> orderInfo = this.orderInfoRepo.findById(id);
		if (orderInfo.isPresent()) {
			return new OrderInfoBo(orderInfo.get(), this.fetchOrderItems(orderInfo.get()));
		}
		throw new BadRequestException("no order found with id= " + id);

	}

	@Override
	public ListResponseWithCount<OrderInfoBo> fetchOrderList(PaginationBo paginationBo) {
		User user = this.authenticationService.currentUser();
		Page<OrderInfo> allOrders = null;
		if (authenticationService.isAdmin()) {
			allOrders = this.orderInfoRepo.findAll(PageRequest.of(paginationBo.getPageNumber(), pageSize));
		} else {
			allOrders = this.orderInfoRepo.findByUser(PageRequest.of(paginationBo.getPageNumber(), pageSize), user);
		}
		List<OrderInfoBo> bos = new ArrayList<OrderInfoBo>();
		allOrders.forEach(orderInfo -> bos.add(new OrderInfoBo(orderInfo, this.fetchOrderItems(orderInfo))));
		return new ListResponseWithCount<OrderInfoBo>(bos, "", allOrders.getTotalElements(),
				paginationBo.getPageNumber(), allOrders.getTotalPages());
	}

	@Override
	public void orderInvoice(String id) {
		log.info("Inside generate Invoice");

		OrderInfoBo orderInfo = this.fetchOrderInfo(id);
		List<OrderItemBo> orderItemBos = orderInfo.getOrderItemBo();
		String address = orderInfo.getAddress().getCity() + " \n" + orderInfo.getAddress().getState() + "\n" + orderInfo.getAddress().getAddress() + "\n" + orderInfo.getAddress().getCountry() + "\n" + orderInfo.getAddress().getPostCode();
		Document document = new Document();
		try {
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("invoice.pdf"));
			document.open();

			Font font = new Font();
			font.setFamily(TIMES_ROMAN);
			font.setColor(BaseColor.BLACK);
			font.setSize(30.0F);
			font.setStyle(1);
			Paragraph paragraph = new Paragraph("INVOICE", font);
			paragraph.setAlignment(1);
			Font font1 = new Font();
			font1.setColor(BaseColor.BLACK);
			font1.setFamily(TIMES_ROMAN);
			font1.setSize(12.0F);
			font1.setStyle(-1);
			document.add(paragraph);
			document.add(new Paragraph(12, (" Name :" + orderInfo.getFirstName() + orderInfo.getLastName() + "\n orderId : " + orderInfo.getId() +
					"\n contact number : " + orderInfo.getPhone() + "\n Email : " + orderInfo.getEmail() + "\n Address : \n" + address), font1));
			PdfPTable pdfPTable = new PdfPTable(3);
			pdfPTable.setWidthPercentage(100);
			pdfPTable.setSpacingBefore(10f);
			pdfPTable.setSpacingAfter(10f);
			pdfPTable.addCell(new PdfPCell(new Paragraph("Product Name")));
			pdfPTable.addCell(new PdfPCell(new Paragraph("Quantity")));
			pdfPTable.addCell(new PdfPCell(new Paragraph("Price Per Unit")));
			orderItemBos.forEach(orderItemBo -> {
				pdfPTable.addCell(new PdfPCell(new Paragraph(orderItemBo.getProduct().getProductName())));
				pdfPTable.addCell(new PdfPCell(new Paragraph(String.valueOf(orderItemBo.getQuantity()))));
				pdfPTable.addCell(new PdfPCell(new Paragraph(String.valueOf(orderItemBo.getProduct().getPricePerUnit()))));
			});
			pdfPTable.addCell(new PdfPCell(new Paragraph("")));
			pdfPTable.addCell(new PdfPCell(new Paragraph("Gross Amount")));
			pdfPTable.addCell(new PdfPCell(new Paragraph(String.valueOf(orderInfo.getAmount()))));
			document.add(pdfPTable);
			document.close();
			pdfWriter.close();
		} catch (FileNotFoundException | DocumentException fileNotFoundException) {
			throw new BadRequestException("Invoice generation failed");
		}
	}
}

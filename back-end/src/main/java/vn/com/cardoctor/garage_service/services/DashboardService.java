package vn.com.cardoctor.garage_service.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.cardoctor.garage_service.entities.dashboards.DataChart;
import vn.com.cardoctor.garage_service.entities.dashboards.Datasets;
import vn.com.cardoctor.garage_service.entities.dashboards.LongTermInventoryProduct;
import vn.com.cardoctor.garage_service.enums.RepairOrderStatus;
import vn.com.cardoctor.garage_service.models.dtos.ProductRefundedDto;
import vn.com.cardoctor.garage_service.models.dtos.RevenueRepairDto;
import vn.com.cardoctor.garage_service.models.responses.DashboardResponse;
import vn.com.cardoctor.garage_service.models.responses.TypeServicePopularResponse;
import vn.com.cardoctor.garage_service.models.responses.dashboards.*;
import vn.com.cardoctor.garage_service.repositories.DashboardRepository;
import vn.com.cardoctor.garage_service.repositories.DistributorRepository;
import vn.com.cardoctor.garage_service.repositories.ProductRepository;
import vn.com.cardoctor.garage_service.repositories.orders.RepairOrderRepository;
import vn.com.cardoctor.garage_service.repositories.orders_part.SellSparePartRepository;
import vn.com.cardoctor.garage_service.services.garages.EmployeeService;
import vn.com.cardoctor.garage_service.services.in_out_bounds.InboundService;
import vn.com.cardoctor.garage_service.services.orders.RepairOrderService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Log4j2
public class DashboardService {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RepairOrderService repairOrderService;

    @Autowired
    private InboundService inboundService;

    @Autowired
    private RepairOrderRepository repairOrderRepository;

    @Autowired
    private SellSparePartRepository sellSparePartRepository;

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private DistributorRepository distributorRepository;

    @Autowired
    private ProductRepository productRepository;

    public DashboardResponse getData(Long garageId, String fromDate, String toDate) {
        DashboardResponse response = new DashboardResponse();

        fromDate = Objects.nonNull(fromDate) ? fromDate : getFirstDayOfMonth();
        toDate = Objects.nonNull(toDate) ? toDate : getCurrentDate();
        String lastFromDate = getDatePreviousMonth(fromDate, 1);
        String lastToDate = getDatePreviousMonth(toDate, 1);

        List<Integer> statusCancelRepairOrder = new ArrayList<>();
        statusCancelRepairOrder.add(RepairOrderStatus.CUSTOMER_CANCELED.getCode());

        BigDecimal revenue = repairOrderService.getRevenue(garageId, statusCancelRepairOrder, fromDate, toDate);
        BigDecimal revenueLastMonth = repairOrderService.getRevenue(garageId, statusCancelRepairOrder, lastFromDate, lastToDate);

        BigDecimal numberServiceDone = repairOrderService.getNumberServiceByStatus(garageId, RepairOrderStatus.DONE.getCode(), fromDate, toDate);
        BigDecimal numberServiceDoneLastMonth = repairOrderService.getNumberServiceByStatus(garageId, RepairOrderStatus.DONE.getCode(), lastFromDate, lastToDate);

        BigDecimal priceSellParePart = sellSparePartRepository.getTotalPriceByGarageId(garageId, fromDate, toDate);
        BigDecimal priceSellParePartLastMonth = sellSparePartRepository.getTotalPriceByGarageId(garageId, lastFromDate, lastToDate);

        BigDecimal numberEmployeeOnRevenue = repairOrderRepository.getNumberEmployeeOnRevenue(garageId, statusCancelRepairOrder, fromDate, toDate);
        BigDecimal numberEmployeeOnRevenueLastMonth = repairOrderRepository.getNumberEmployeeOnRevenue(garageId, statusCancelRepairOrder, lastFromDate, lastToDate);

        BigDecimal refundRate = inboundService.getNumberTicketRefundRate(garageId, fromDate, toDate);
        BigDecimal refundRateLastMonth = inboundService.getNumberTicketRefundRate(garageId, lastFromDate, lastToDate);

        BigDecimal countEmployee = employeeService.getEmployeeCountByGarage(garageId);

        BigDecimal _numberEmployeeOnRevenue = numberEmployeeOnRevenue.intValue() > 0
                ? revenue.divide(countEmployee, RoundingMode.CEILING)
                : BigDecimal.valueOf(0);

        BigDecimal _numberEmployeeOnRevenueLastMonth = numberEmployeeOnRevenueLastMonth.intValue() > 0
                ? revenueLastMonth.divide(countEmployee, RoundingMode.CEILING)
                : BigDecimal.valueOf(0);

        BigDecimal ticketCancelCurrent = repairOrderService.getNumberServiceByStatus(garageId, RepairOrderStatus.CUSTOMER_CANCELED.getCode(), fromDate, toDate);
        BigDecimal ticketCancelLastMonth = repairOrderService.getNumberServiceByStatus(garageId, RepairOrderStatus.CUSTOMER_CANCELED.getCode(), lastFromDate, lastToDate);

        BigDecimal _refundRate = numberServiceDone.intValue() > 0
                ? refundRate.divide(numberServiceDone, RoundingMode.CEILING)
                : BigDecimal.valueOf(0);

        BigDecimal _refundRateLastMonth = numberServiceDoneLastMonth.intValue() > 0
                ? refundRateLastMonth.divide(numberServiceDoneLastMonth, RoundingMode.CEILING)
                : BigDecimal.valueOf(0);

        List<DashboardResponse.TypeOverview> listTypeOverview = new ArrayList<>();

        //revenue
        DashboardResponse.TypeOverview typeRevenue = new DashboardResponse.TypeOverview();
        typeRevenue.setName("revenue");
        typeRevenue.setCurrent(revenue.add(priceSellParePart));
        typeRevenue.setLast(revenueLastMonth.add(priceSellParePartLastMonth));
        typeRevenue.setPercent(calculationPercent(revenue.add(priceSellParePart).doubleValue(), revenueLastMonth.add(priceSellParePartLastMonth).doubleValue()));
        listTypeOverview.add(typeRevenue);

        //number_service
        DashboardResponse.TypeOverview typeNumberServiceDone = new DashboardResponse.TypeOverview();
        typeNumberServiceDone.setName("number_service");
        typeNumberServiceDone.setCurrent(numberServiceDone);
        typeNumberServiceDone.setLast(numberServiceDoneLastMonth);
        typeNumberServiceDone.setPercent(calculationPercent(numberServiceDone.doubleValue(), numberServiceDoneLastMonth.doubleValue()));
        listTypeOverview.add(typeNumberServiceDone);

        //number_employees
        DashboardResponse.TypeOverview typeEmployee = new DashboardResponse.TypeOverview();
        typeEmployee.setName("number_employee");
        typeEmployee.setCurrent(countEmployee);
        typeEmployee.setLast(countEmployee);
        typeEmployee.setPercent(calculationPercent(countEmployee.doubleValue(), countEmployee.doubleValue()));
        listTypeOverview.add(typeEmployee);

        //revenue_on_number_employee
        DashboardResponse.TypeOverview employeeOnRevenue = new DashboardResponse.TypeOverview();
        employeeOnRevenue.setName("income_of_employee");
        employeeOnRevenue.setCurrent(_numberEmployeeOnRevenue);
        employeeOnRevenue.setLast(_numberEmployeeOnRevenueLastMonth);
        employeeOnRevenue.setPercent(calculationPercent(_numberEmployeeOnRevenue.doubleValue(), _numberEmployeeOnRevenueLastMonth.doubleValue()));
        listTypeOverview.add(employeeOnRevenue);

        //ticket_cancel
        DashboardResponse.TypeOverview ticketCancel = new DashboardResponse.TypeOverview();
        ticketCancel.setName("number_service_cancel");
        ticketCancel.setCurrent(ticketCancelCurrent);
        ticketCancel.setLast(ticketCancelLastMonth);
        ticketCancel.setPercent(calculationPercent(ticketCancelCurrent.doubleValue(), ticketCancelLastMonth.doubleValue()));
        listTypeOverview.add(ticketCancel);

        //refund_rate
        DashboardResponse.TypeOverview typeRefundRate = new DashboardResponse.TypeOverview();
        typeRefundRate.setName("refund_rate");
        typeRefundRate.setCurrent(_refundRate);
        typeRefundRate.setLast(_refundRateLastMonth);
        typeRefundRate.setPercent(calculationPercent(_refundRate.doubleValue(), _refundRateLastMonth.doubleValue()));
        listTypeOverview.add(typeRefundRate);

        response.setOverview(listTypeOverview);
        response.setPopularService(repairOrderRepository.getListPopularServices(garageId));
        response.setExcellentStaff(repairOrderRepository.getListExcellentStaffs(garageId, getFirstDayOfMonth(), getCurrentDate()));
        return response;
    }

    public DataChart getRevenue(String groupByType, String fromDate, String toDate, Long garageId) {
        List<RevenueRepairDto> revenueRepairDtos = this.repairOrderRepository.findRevenueOfRepairV2(groupByType, garageId);
        List<RevenueSellSparePart> revenueSellSpareParts = sellSparePartRepository.findRevenueOfSellSparePartV2(groupByType, garageId);
        DataChart dataChart = new DataChart();
        List<String> labels = new ArrayList<>();
        List<Datasets> datasets = new ArrayList<>();
        List<BigDecimal> revenuePerUnit = new ArrayList<>();
        List<BigDecimal> revenueSellSparePartPerUnit = new ArrayList<>();
        revenueRepairDtos.forEach(a -> {
            revenuePerUnit.add(a.getRevenueOfRepair().setScale(2, RoundingMode.HALF_EVEN));
            if (groupByType.equals("week")) {
                labels.add(String.valueOf(a.getDate()));
            } else if (groupByType.equals("month")) {
//                StringBuilder date = new StringBuilder();
//                date.append(this.translator.toLocale("sos.tracking.garage.week") + " ");
//                date.append(a.getStringDate().substring(0, a.getStringDate().indexOf("-")));
//                date.append(" " + this.translator.toLocale("sos.tracking.garage.year") + " ");
//                date.append(a.getStringDate().substring(a.getStringDate().lastIndexOf("-") + 1));
//                labels.add(date.toString());
                labels.add(a.getStringDate());
            } else {
//                labels.add("thg " + a.getStringDate());
                labels.add(a.getStringDate());
            }
        });
        revenueSellSpareParts.forEach(a -> {
            revenueSellSparePartPerUnit.add(a.getRevenueSellSparePart().setScale(2, RoundingMode.HALF_EVEN));
        });

        Datasets revenueRepair = new Datasets();
        revenueRepair.setLabel("Doanh thu phiếu dịch vụ");
        revenueRepair.setBackgroundColor("rgba(4, 116, 211, 1)");
        revenueRepair.setType("line");
        revenueRepair.setOrder(0);
        revenueRepair.setData(revenuePerUnit);

        Datasets revenueSellSparePart = new Datasets();
        revenueSellSparePart.setLabel("Doanh thu phiếu bán hàng");
        revenueSellSparePart.setBackgroundColor("rgba(237,31,66,1)");
        revenueSellSparePart.setType("line");
        revenueSellSparePart.setOrder(0);
        revenueSellSparePart.setData(revenueSellSparePartPerUnit);

        datasets.add(revenueRepair);
        datasets.add(revenueSellSparePart);

        dataChart.setLabels(labels);
        dataChart.setDatasets(datasets);
        return dataChart;
    }

    public TypeServicePopularResponse getListTypeServicePopular(Long garageId, String time) {
        TypeServicePopularResponse response = new TypeServicePopularResponse();
        String fromDate = getTimeFilterTypeService(time).get(0);
        String toDate = getTimeFilterTypeService(time).get(1);
        List<TypeServicePopularResponse.ListTypeServicePopularResponse> typeServices = repairOrderRepository.getListTypeServicePopular(garageId, fromDate, toDate);
        int total = 0;
        for (TypeServicePopularResponse.ListTypeServicePopularResponse item : typeServices) {
            total += item.getQuantity().intValue();
        }
        response.setTotal(total);
        response.setListTypeServicePopulars(typeServices);
        return response;
    }

    private String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

    private String getFirstDayOfMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return firstDayOfMonth.format(formatter);
    }

    private String getDatePreviousMonth(String time, Integer monthSubtract) {
        LocalDate currentDate = LocalDate.parse(time);
        LocalDate lastMonthDate = currentDate.minusMonths(monthSubtract);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return lastMonthDate.format(formatter);
    }

    private List<String> getTimeFilterTypeService(String time) {
        List<String> results = new ArrayList<>();
        String fromDate;
        String toDate = getCurrentDate();

        if (Objects.nonNull(time)) {
            switch (time) {
                case "3m":
                    fromDate = getDatePreviousMonth(toDate, 3);
                    break;
                case "6m":
                    fromDate = getDatePreviousMonth(toDate, 6);
                    break;
                case "1Y":
                    fromDate = getDatePreviousMonth(toDate, 12);
                    break;
                default:
                    fromDate = getDatePreviousMonth(toDate, 1);
            }
        } else {
            fromDate = getDatePreviousMonth(toDate, 1);
        }

        Collections.addAll(results, fromDate, toDate);
        return results;
    }

    private String calculationPercent(Double currentData, Double oldData) {
        if (currentData == 0 && oldData == 0) {
            return "+0.0%";
        }

        if (currentData >= 0 && oldData == 0) {
            return "+100%";
        }

        double result = ((currentData - oldData) / oldData) * 100;
        String resultFormat = String.format("%.1f", result);

        if (result >= 0) {
            resultFormat = "+" + resultFormat;
        }

        return resultFormat + "%";
    }

    public DashboardInventoryResponse getSummaryDashboardInventory(Long garageId, Date fromDate, Date toDate) {
        DashboardInventoryResponse dashboardInventoryResponse = new DashboardInventoryResponse();
        CountProductResponse countProductResponse = this.dashboardRepository.getTotalProduct(garageId, fromDate, toDate);
        dashboardInventoryResponse.setTotalProductCode(countProductResponse.getTotalProductCode());
        dashboardInventoryResponse.setTotalProductQuantity(countProductResponse.getTotalProductQuantity());
        dashboardInventoryResponse.setInventoryValue(this.dashboardRepository.getInventoryValue(garageId, fromDate, toDate));
        dashboardInventoryResponse.setTotalTicketHasNotDelivery(this.dashboardRepository.getTotalTicketHasNotDelivery(garageId, fromDate, toDate));
        return dashboardInventoryResponse;
    }

    public DataChart getOutputOrderDistributors(Long garageId, Long quantityLabel) {
        List<OutputOrderDistributor> outputOrderDistributors = this.distributorRepository.findAllOutputOrderDistributors(garageId, quantityLabel);
        DataChart dataChart = new DataChart();
        List<String> labels = new ArrayList<>();
        List<Datasets> datasets = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();
        outputOrderDistributors.forEach(a -> {
            labels.add(a.getDistributorName());
            data.add(a.getOutput());
        });
        Datasets datasets1 = new Datasets();
        datasets1.setOrder(0);
        datasets1.setData(data);
        datasets1.setType("bar");
        datasets1.setLabel("Sản lượng đặt hàng NPP");
        datasets1.setBackgroundColor("rgba(255,166,102,1)");

        datasets.add(datasets1);

        dataChart.setLabels(labels);
        dataChart.setDatasets(datasets);
        return dataChart;

    }

    public DataChart getMostProductRefunded(Long garageId) {
        List<ProductRefundedDto> mostProductRefunded = this.dashboardRepository.getMostProductRefunded(garageId);
        DataChart dataChart = new DataChart();
        List<String> labels = new ArrayList<>();
        List<Datasets> datasets = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();
        Datasets dataset = new Datasets();
        mostProductRefunded.forEach(product -> {
            labels.add(product.getCode() + " - " + product.getName());
            data.add(product.getRefundQuantity());
        });
        dataset.setOrder(0);
        dataset.setData(data);
        dataset.setLabel("Hàng đổi trả nhiều");
        dataset.setType("bar");
        dataset.setBackgroundColor("rgba(67, 85, 245, 1)");
        datasets.add(dataset);

        dataChart.setDatasets(datasets);
        dataChart.setLabels(labels);
        return dataChart;
    }

    public DataChart getLowInventoryProducts(Long garageId, Long quantityLabel) {
        List<LowInventoryProduct> lowInventoryProducts = this.productRepository.findAllLowInventoryProducts(garageId, quantityLabel);
        DataChart dataChart = new DataChart();
        List<String> labels = new ArrayList<>();
        List<Datasets> datasets = new ArrayList<>();
        List<BigDecimal> quantityInventories = new ArrayList<>();
        List<BigDecimal> consumptionPerMonths = new ArrayList<>();
        lowInventoryProducts.forEach(a -> {
            labels.add(a.getProductName());
            quantityInventories.add(a.getQuantity());
            consumptionPerMonths.add(a.getConsumptionPerMonth());
        });
        Datasets datasets1 = new Datasets();
        datasets1.setOrder(0);
        datasets1.setData(quantityInventories);
        datasets1.setType("bar");
        datasets1.setLabel("Tồn kho");
        datasets1.setBackgroundColor("rgba(25,205,53,1)");

        Datasets datasets2 = new Datasets();
        datasets2.setOrder(0);
        datasets2.setData(consumptionPerMonths);
        datasets2.setType("line");
        datasets2.setLabel("Sản lượng tiêu thụ/ tháng");
        datasets2.setBackgroundColor("rgba(237,31,66,1)");

        datasets.add(datasets1);
        datasets.add(datasets2);

        dataChart.setLabels(labels);
        dataChart.setDatasets(datasets);
        return dataChart;

    }

    public Page<LongTermInventoryProduct> getLongTermInventoryProducts(Long garageId, Pageable pageable) {
        return this.productRepository.findAllLongTermInventoryProducts(garageId, pageable);
    }
}

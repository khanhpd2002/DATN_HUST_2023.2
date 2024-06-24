package vn.com.cardoctor.garage_service.services.excels;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BaseExcelService {

    protected String getStringFromRow(XSSFRow row, int colIndex) {
        String result = null;
        if (row.getCell(colIndex) == null) {
            return result;
        }
        if (row.getCell(colIndex).getCellType() == CellType.NUMERIC) {
            result = NumberToTextConverter.toText(row.getCell(colIndex).getNumericCellValue());
        } else {
            result = row.getCell(colIndex).getStringCellValue();
        }
        return result;
    }

    protected <T> Predicate<T> distinctByKeys(List<Function<? super T, ?>> keyExtractors) {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();
        return t -> {
            final List<?> keys = keyExtractors.parallelStream().map(ke -> ke.apply(t)).collect(Collectors.toList());
            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }
}

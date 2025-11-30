package com.moneymap.storage;

import com.moneymap.model.Income;
import com.moneymap.model.Expense;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class CsvStorageTest {

    @Test
    void testCsvStorageIncomeAndExpense() throws Exception {
        Path tmp = Files.createTempDirectory("moneymap-test");
        Path incomeFile = tmp.resolve("income_data.csv");
        CsvStorage s = new CsvStorage(incomeFile);

        s.saveIncome(new Income(12.5, "TestInc", "2025-11-28"));
        s.saveIncome(new Income(7.5, "Another", "2025-11-27"));

        List<Income> loaded = s.loadIncomes();
        assertEquals(2, loaded.size());
        assertEquals(12.5, loaded.get(0).getAmount(), 1e-9);

        // expenses are stored to expense_data.csv next to income file
        s.saveExpense(new Expense(3.0, "TestExp", "2025-11-28"));
        List<Expense> ex = s.loadExpenses();
        assertEquals(1, ex.size());
        assertEquals(3.0, ex.get(0).getAmount(), 1e-9);
    }
}

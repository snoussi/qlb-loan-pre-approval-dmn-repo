package com.redhat.btison.dm7.dmn.qlb;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.kie.dmn.api.core.DMNDecisionResult.DecisionEvaluationStatus.SUCCEEDED;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;

public class InterestRateCalculationTest {

    @Test
    public void testInterestRateCalculation() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();

        DMNRuntime dmnRuntime = kieSession.getKieRuntime(DMNRuntime.class);

        String dmnModelNamespace = "http://www.trisotech.com/dmn/definitions/_c55e5995-0cc9-40b8-b783-88468c69ebca";
        String dmnModelName = "qlb-loan-application";

        DMNModel dmnModel = dmnRuntime.getModel(dmnModelNamespace, dmnModelName);

        assertThat(dmnModel, notNullValue());
        assertThat( dmnModel.hasErrors(), is(false) );

        DMNContext dmnContext = dmnRuntime.newContext();
        dmnContext.set("Loan", loadLoan(290000, 7));

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Interest Rate Calculation");
        assertThat(decisionResult.getEvaluationStatus(), equalTo(SUCCEEDED));
        assertThat(decisionResult.getResult(), instanceOf(Map.class));
        assertThat(decisionResult.getResult(), equalTo(loadExpectedResult(290000, 7, new BigDecimal("0.47"), new BigDecimal("3468.607142857142857142857142857143"))));
    }

    private Map<String, Object> loadLoan(int amount, int duration) {

        Map<String, Object> loan = new HashMap<>(  );
        loan.put("amount", number(amount));
        loan.put("duration", number(duration));
        return loan;
    }

    private Map<String, Object> loadExpectedResult(int amount, int duration, BigDecimal interestRate, BigDecimal monthlyRepayment) {

        Map<String, Object> loan = new HashMap<>(  );
        loan.put("amount", number(amount));
        loan.put("duration", number(duration));
        loan.put("interest rate", interestRate);
        loan.put("monthly repayment", monthlyRepayment);
        return loan;
    }

    private BigDecimal number(Number n ) {
        return BigDecimal.valueOf( n.longValue() );
    }

}

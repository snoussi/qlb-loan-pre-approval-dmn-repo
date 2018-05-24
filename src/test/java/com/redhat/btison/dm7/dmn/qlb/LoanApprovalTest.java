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

public class LoanApprovalTest {

    @Test
    public void testLoanApprovalNotEligible() {
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
        dmnContext.set("Applicant", loadApplicant( 17, 180000, 500));
        dmnContext.set("Loan", loadLoan(290000, 7));

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Loan Approval");
        assertThat(decisionResult.getEvaluationStatus(), equalTo(SUCCEEDED));
        assertThat(decisionResult.getResult(), instanceOf(Map.class));
        assertThat(decisionResult.getResult(), equalTo(loadExpectedResult(290000, 7,
                new BigDecimal("0.47"), new BigDecimal("3468.607142857142857142857142857143"),
                false, "applicant not eligible")));
    }

    @Test
    public void testLoanApprovalAmountTooLow() {
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
        dmnContext.set("Applicant", loadApplicant( 18, 180000, 500));
        dmnContext.set("Loan", loadLoan(1000, 7));

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Loan Approval");
        assertThat(decisionResult.getEvaluationStatus(), equalTo(SUCCEEDED));
        assertThat(decisionResult.getResult(), instanceOf(Map.class));
        assertThat(decisionResult.getResult(), equalTo(loadExpectedResult(1000, 7,
                new BigDecimal("0.47"), new BigDecimal("11.96071428571428571428571428571429"),
                false, "amount too low")));
    }

    @Test
    public void testLoanApprovalAmountTooHigh() {
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
        dmnContext.set("Applicant", loadApplicant( 18, 180000, 500));
        dmnContext.set("Loan", loadLoan(1000001, 7));

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Loan Approval");
        assertThat(decisionResult.getEvaluationStatus(), equalTo(SUCCEEDED));
        assertThat(decisionResult.getResult(), instanceOf(Map.class));
        assertThat(decisionResult.getResult(), equalTo(loadExpectedResult(1000001, 7,
                new BigDecimal("0.98"), new BigDecimal("12021.44059285714285714285714285714"),
                false, "amount too high")));
    }

    @Test
    public void testLoanApprovalDebtRatioTooHigh() {
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
        dmnContext.set("Applicant", loadApplicant(120000, 500));
        dmnContext.set("Loan", loadLoan(290000, 7));

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Loan Approval");
        assertThat(decisionResult.getEvaluationStatus(), equalTo(SUCCEEDED));
        assertThat(decisionResult.getResult(), instanceOf(Map.class));
        assertThat(decisionResult.getResult(), equalTo(loadExpectedResult(290000, 7,
                new BigDecimal("0.47"), new BigDecimal("3468.607142857142857142857142857143"),
                false, "debt ratio too high")));
    }

    @Test
    public void testLoanApproval100KInsufficientCreditScore() {
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
        dmnContext.set("Applicant", loadApplicant(120000, 300));
        dmnContext.set("Loan", loadLoan(100000, 7));

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Loan Approval");
        assertThat(decisionResult.getEvaluationStatus(), equalTo(SUCCEEDED));
        assertThat(decisionResult.getResult(), instanceOf(Map.class));
        assertThat(decisionResult.getResult(), equalTo(loadExpectedResult(100000, 7,
                new BigDecimal("0.47"), new BigDecimal("1196.071428571428571428571428571429"),
                false, "insufficient credit score")));
    }

    @Test
    public void testLoanApproval100KSufficientCreditScore() {
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
        dmnContext.set("Applicant", loadApplicant(120000, 301));
        dmnContext.set("Loan", loadLoan(100000, 7));

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Loan Approval");
        assertThat(decisionResult.getEvaluationStatus(), equalTo(SUCCEEDED));
        assertThat(decisionResult.getResult(), instanceOf(Map.class));
        assertThat(decisionResult.getResult(), equalTo(loadExpectedResult(100000, 7,
                new BigDecimal("0.47"), new BigDecimal("1196.071428571428571428571428571429"),
                true, "sufficient credit score")));
    }

    @Test
    public void testLoanApproval400KInsufficientCreditScore() {
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
        dmnContext.set("Applicant", loadApplicant(120000, 400));
        dmnContext.set("Loan", loadLoan(100001, 7));

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Loan Approval");
        assertThat(decisionResult.getEvaluationStatus(), equalTo(SUCCEEDED));
        assertThat(decisionResult.getResult(), instanceOf(Map.class));
        assertThat(decisionResult.getResult(), equalTo(loadExpectedResult(100001, 7,
                new BigDecimal("0.47"), new BigDecimal("1196.083389285714285714285714285714"),
                false, "insufficient credit score")));
    }

    @Test
    public void testLoanApproval400KSufficientCreditScore() {
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
        dmnContext.set("Applicant", loadApplicant(120000, 401));
        dmnContext.set("Loan", loadLoan(100001, 7));

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Loan Approval");
        assertThat(decisionResult.getEvaluationStatus(), equalTo(SUCCEEDED));
        assertThat(decisionResult.getResult(), instanceOf(Map.class));
        assertThat(decisionResult.getResult(), equalTo(loadExpectedResult(100001, 7,
                new BigDecimal("0.47"), new BigDecimal("1196.083389285714285714285714285714"),
                true, "sufficient credit score")));
    }

    @Test
    public void testLoanApproval500KInsufficientCreditScore() {
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
        dmnContext.set("Applicant", loadApplicant(240000, 500));
        dmnContext.set("Loan", loadLoan(500000, 7));

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Loan Approval");
        assertThat(decisionResult.getEvaluationStatus(), equalTo(SUCCEEDED));
        assertThat(decisionResult.getResult(), instanceOf(Map.class));
        assertThat(decisionResult.getResult(), equalTo(loadExpectedResult(500000, 7,
                new BigDecimal("0.70"), new BigDecimal("5994.047619047619047619047619047619"),
                false, "insufficient credit score")));
    }

    @Test
    public void testLoanApproval500KSufficientCreditScore() {
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
        dmnContext.set("Applicant", loadApplicant(240000, 501));
        dmnContext.set("Loan", loadLoan(500000, 7));

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Loan Approval");
        assertThat(decisionResult.getEvaluationStatus(), equalTo(SUCCEEDED));
        assertThat(decisionResult.getResult(), instanceOf(Map.class));
        assertThat(decisionResult.getResult(), equalTo(loadExpectedResult(500000, 7,
                new BigDecimal("0.70"), new BigDecimal("5994.047619047619047619047619047619"),
                true, "sufficient credit score")));
    }

    @Test
    public void testLoanApproval1MInsufficientCreditScore() {
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
        dmnContext.set("Applicant", loadApplicant(240000, 700));
        dmnContext.set("Loan", loadLoan(500001, 7));

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Loan Approval");
        assertThat(decisionResult.getEvaluationStatus(), equalTo(SUCCEEDED));
        assertThat(decisionResult.getResult(), instanceOf(Map.class));
        assertThat(decisionResult.getResult(), equalTo(loadExpectedResult(500001, 7,
                new BigDecimal("0.70"), new BigDecimal("5994.059607142857142857142857142857"),
                false, "insufficient credit score")));
    }

    @Test
    public void testLoanApproval1MSufficientCreditScore() {
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
        dmnContext.set("Applicant", loadApplicant(240000, 701));
        dmnContext.set("Loan", loadLoan(500001, 7));

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Loan Approval");
        assertThat(decisionResult.getEvaluationStatus(), equalTo(SUCCEEDED));
        assertThat(decisionResult.getResult(), instanceOf(Map.class));
        assertThat(decisionResult.getResult(), equalTo(loadExpectedResult(500001, 7,
                new BigDecimal("0.70"), new BigDecimal("5994.059607142857142857142857142857"),
                true, "sufficient credit score")));
    }

    private Map<String, Object> loadLoan(int amount, int duration) {

        Map<String, Object> loan = new HashMap<>(  );
        loan.put("amount", number(amount));
        loan.put("duration", number(duration));
        return loan;
    }

    private Map<String, Object> loadApplicant(int yearlyIncome, int creditScore) {
        return loadApplicant(25, yearlyIncome, creditScore);
    }

    private Map<String, Object> loadApplicant(int age, int yearlyIncome, int creditScore) {
        Map<String, Object> applicant = new HashMap<>();
        applicant.put("name", "John Doe");
        applicant.put("age", number(age));
        applicant.put("credit score", number(creditScore));
        applicant.put("yearly income", number(yearlyIncome));
        return applicant;
    }

    private Map<String, Object> loadExpectedResult(int amount, int duration, BigDecimal interestRate, BigDecimal monthlyRepayment, boolean approved, String comment) {

        Map<String, Object> loan = new HashMap<>();
        loan.put("amount", number(amount));
        loan.put("duration", number(duration));
        loan.put("interest rate", interestRate);
        loan.put("monthly repayment", monthlyRepayment);
        Map<String, Object> approval = new HashMap<>();
        approval.put("approved", approved);
        approval.put("reason", comment);
        loan.put("approval", approval);
        return loan;
    }

    private BigDecimal number(Number n ) {
        return BigDecimal.valueOf( n.longValue() );
    }

}

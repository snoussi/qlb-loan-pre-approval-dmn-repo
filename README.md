# Quick Loan Bank - Loan Pre-Approval DMN Decision Service

DMN implementation of qlb loan application demo, kudos to [@btison](https://github.com/btison)

Reference: https://github.com/snoussi/qlb-loan-pre-approval-repo.git

## Decision Service definition

![DMN model](global/dmn.png)

## Decision service request examlpe

```json
{
  "model-namespace": "http://www.qlb.com/dmn/definitions/_c55e5995-0cc9-40b8-b783-88468c69ebca",
  "model-name": "loan-pre-approval",
  "decision-name": "Loan Approval",
  "decision-id": [],
  "dmn-context": {
    "Loan": {
      "duration": 7,
      "amount": 100001
    },
    "Applicant": {
      "yearly income": 120000,
      "credit score": 401,
      "name": "John Doe",
      "age": 17
    }
  }
}
```

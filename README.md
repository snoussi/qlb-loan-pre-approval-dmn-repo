# Quick Loan Bank - Loan Pre-Approval DMN Decision Service

DMN implementation of qlb loan application demo
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
      "Amount": 300000,
      "Duration": 20
    },
    "Applicant": {
      "Monthly Income": 4000,
      "Credit Score": 300,
      "Name": "Lucien Bramard",
      "Age": 16
    }
  }
}
```

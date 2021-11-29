# Quick Loan Bank - Loan Pre-Approval DMN Decision Service

DMN implementation of qlb loan application demo
Reference: https://github.com/snoussi/qlb-loan-pre-approval-repo.git

## Decision Service definition

![DMN model](src/main/resources/com/redhat/demo/qlb/loan-preapproval-svg.svg)

## Decision service request examlpe

```json
{
  "model-namespace": "http://www.qlb.com/dmn/_96E3B174-69BB-4689-A66B-3A8DAA10D45D",
  "model-name": "loan-preapproval",
  "decision-name": "Pre Approval",
  "decision-id": [],
  "dmn-context": {
    "Applicant": {
      "Name": "Lucien Bramard",
      "Age": 17,
      "Monthly Income": 4000,
      "Credit Score": 300
    },
    "Loan": {
      "Amount": 300000,
      "Duration": 25
    }
  }
}
```

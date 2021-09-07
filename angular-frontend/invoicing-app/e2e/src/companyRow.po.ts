import { by, ElementFinder, WebElement } from "protractor";

export class CompanyRow {

    constructor(private companyRow: ElementFinder) {
    }
  
    deleteBtn(): WebElement {
      return this.companyRow.element(by.css('.btn-danger'))
    }
}

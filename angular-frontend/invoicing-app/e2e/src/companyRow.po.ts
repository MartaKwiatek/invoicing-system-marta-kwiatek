import { by, ElementFinder, WebElement } from "protractor";

export class CompanyRow {

    constructor(private companyRow: ElementFinder) {
    }
  
    deleteBtn(): WebElement {
      return this.companyRow.element(by.css('.btn-danger'))
    }

    async taxIdNumberValue(): Promise<string> {
        return this.companyRow.element(by.id('taxIdNumber')).getText()
      }
    
      async nameValue(): Promise<string> {
        return this.companyRow.element(by.id('name')).getText()
      }
    
      async addressValue(): Promise<string> {
        return this.companyRow.element(by.id('address')).getText()
      }
    
      async pensionInsuranceValue(): Promise<string> {
        return this.companyRow.element(by.id('pensionInsurance')).getText()
      }
    
      async healthInsuranceValue(): Promise<string> {
        return this.companyRow.element(by.id('healthInsurance')).getText()
      }

    async assertRowValues(taxIdNumber: string, name: string, address: string, pensionInsurance: string, healthInsurance: string) {
        expect(await this.taxIdNumberValue()).toEqual(taxIdNumber)
        expect(await this.nameValue()).toEqual(name)
        expect(await this.addressValue()).toEqual(address)
        expect(await this.pensionInsuranceValue()).toEqual(pensionInsurance)
        expect(await this.healthInsuranceValue()).toEqual(healthInsurance)
      }
}

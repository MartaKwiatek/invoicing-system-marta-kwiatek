import { browser, by, element, ElementArrayFinder, ElementFinder, WebElement } from "protractor";

export class CompanyPage {

    async navigateTo(): Promise<unknown> {
        return browser.get(browser.baseUrl);
    }

    async taxIdHeaderValue(): Promise<string> {
        return element(by.id('taxIdHeader')).getText();
    }

    async addressHeaderValue(): Promise<string> {
        return element(by.id('addressHeader')).getText();
    }

    async nameHeaderValue(): Promise<string> {
        return element(by.id('nameHeader')).getText();
    }

    async healthInsuranceHeader(): Promise<string> {
        return element(by.id('healthInsuranceHeader')).getText();
    }

    async pensionInsuranceHeader(): Promise<string> {
        return element(by.id('pensionInsuranceHeader')).getText();
    }

    companyRows(): ElementArrayFinder {
        return element.all(by.css('.companyRow'))
    }

    anyCompanyRow(): ElementFinder {
        return element(by.css('.companyRow'))
    }

    async addNewCompany(taxId: string, name: string, address: string, pensionInsurance: number, healthInsurance: number) {
        await this.taxIdInput().sendKeys(taxId)
        await this.addressInput().sendKeys(address)
        await this.nameInput().sendKeys(name)

        await this.healthInsuranceInput().clear()
        await this.healthInsuranceInput().sendKeys(healthInsurance)

        await this.pensionInsuranceInput().clear()
        await this.pensionInsuranceInput().sendKeys(pensionInsurance)

        await element(by.id("createCompanyButton")).click()
    }

    private addressInput() {
        return element(by.css('input[name=address]'));
    }

    private nameInput() {
        return element(by.css('input[name=name]'));
    }

    private taxIdInput() {
        return element(by.css('input[name=taxIdNumber]'));
    }

    private healthInsuranceInput() {
        return element(by.css('input[name=healthInsurance]'));
    }

    private pensionInsuranceInput() {
        return element(by.css('input[name=pensionInsurance]'));
    }
}

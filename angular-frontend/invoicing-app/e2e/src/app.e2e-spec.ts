import { browser, ExpectedConditions, logging } from 'protractor';
import { CompanyPage } from './company.po';
import { CompanyRow } from './companyRow.po';

describe('Company page E2E test', () => {
  let page: CompanyPage;

  beforeEach(async () => {
    page = new CompanyPage();

    await page.navigateTo();

    await page.companyRows()
    .each(async (row) => {
      let companyRow = new CompanyRow(row);
      await companyRow.deleteBtn().click()
    })

    browser.wait(ExpectedConditions.not(ExpectedConditions.presenceOf(page.anyCompanyRow())));

    expect(await page.companyRows()).toEqual([])
  });

  it('should display correct values for table headers', async () => {
    await page.navigateTo();
    expect(await page.taxIdHeaderValue()).toEqual('Tax identification number');
    expect(await page.addressHeaderValue()).toEqual('Address');
    expect(await page.nameHeaderValue()).toEqual('Name');
    expect(await page.healthInsuranceHeader()).toEqual('Health Insurance');
    expect(await page.pensionInsuranceHeader()).toEqual('Pension Insurance');
  });

  it('can add company', async () => {
    await page.addNewCompany("123-456-78-90", "Test Ltd.", "123 Wall Street", 1234, 123)

    await page.companyRows().then(async rows => { expect(rows.length).toEqual(1);
        // await new CompanyRow(rows[0]).assertRowValues("123", "123 Inc.", "123 Wall Street", "1234", "123")
    })

});

  it('can delete company', async () => {
    await page.addNewCompany("123-456-78-90", "Test1 Ltd.", "123 Wall Street", 1234, 123)
    await page.addNewCompany("987-654-32-10", "Test2 Ltd.", "321 Wall Street", 4321, 321)

    await page.companyRows().then(async rowsBeforeDelete => {
      expect(rowsBeforeDelete.length).toEqual(2);
      await new CompanyRow(rowsBeforeDelete[0]).deleteBtn().click()

      await page.companyRows().then(async rowsAfterDelete => {
          expect(rowsAfterDelete.length).toEqual(1);
          await new CompanyRow(rowsAfterDelete[0]).assertRowValues("987-654-32-10", "Test2 Ltd.", "321 Wall Street", "4321", "321")
      });
    })
  })

  it('can update company', async () => {
    await page.addNewCompany("123-456-78-90", "Test1 Ltd.", "123 Wall Street", 1234, 123)

    await page.companyRows().then(async rows => {
        const companyRow = new CompanyRow(rows[0]);
        await companyRow.updateCompany("987-654-32-10", "Test2 Ltd.", "321 Wall Street", 4321, 321)
        await companyRow.assertRowValues("987-654-32-10", "Test2 Ltd.", "321 Wall Street", "4321", "321")
    })
});

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});

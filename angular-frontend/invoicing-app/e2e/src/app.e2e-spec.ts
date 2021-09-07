import { browser, logging } from 'protractor';
import { CompanyPage } from './company.po';

describe('Company page E2E test', () => {
  let page: CompanyPage;

  beforeEach(() => {
    page = new CompanyPage();
  });

  it('should display correct values for table headers', async () => {
    await page.navigateTo();
    expect(await page.taxIdHeaderValue()).toEqual('Tax identification number');
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});

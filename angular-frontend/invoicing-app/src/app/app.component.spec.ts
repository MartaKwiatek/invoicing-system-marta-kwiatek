import { TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { AppComponent } from './app.component';
import { CompanyService } from './company.service';
import { Company } from './model/company';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        { provide: CompanyService, useClass: MockCompanyService }
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'Invoicing Application'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('Invoicing Application');
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('div.greeting').textContent).toContain('Hello invoicing app!');
  });

class MockCompanyService {

  companies: Company[] = [
    new Company(
      1,
      "111-111-11-11",
      "ul. First 1",
      "First Company Ltd",
      1111.11,
      999.99
    ),
    new Company(
      2,
      "222-222-22-22",
      "ul. Second 2",
      "Second Company Ltd",
      2222.22,
      1212.12
    )
  ];

  getCompanies() {
    return of(this.companies);
  }
}

});

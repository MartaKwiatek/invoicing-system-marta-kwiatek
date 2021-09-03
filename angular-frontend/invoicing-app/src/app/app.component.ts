import { Component } from '@angular/core';
import { Company } from './model/company';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Invoicing Application';

  companies: Company[] = [
    new Company(
      "111-111-11-11",
      "ul. First 1",
      "First Company Ltd.",
      111.11,
      111.11
    ),
    new Company(
      "222-222-22-22",
      "ul. Second 2",
      "Second Company Ltd.",
      222.22,
      222.22
    )
  ];

  newCompany = new Company("", "", "", 0, 0);

  addCompany() {
    this.companies.push(this.newCompany);
    this.newCompany = new Company("", "", "", 0, 0);
  }
}

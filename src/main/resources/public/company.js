const loadCompanies = async () => {
    const response = await fetch('http://localhost:7777/companies');
    const companies = await response.json();

    const companiesTable = document.getElementById("companiesTable");
    companies.forEach(company => {
        const row = companiesTable.insertRow(-1);

        const tinCell = row.insertCell(0);
        tinCell.innerText = company.taxIdNumber;

        const addressCell = row.insertCell(1);
        addressCell.innerText = company.address;
        
        const nameCell = row.insertCell(2);
        nameCell.innerText = company.name;

        const healthInsuranceCell = row.insertCell(3);
        healthInsuranceCell.innerText = company.healthInsurance;

        const pensionInsuranceCell = row.insertCell(4);
        pensionInsuranceCell.innerText = company.pensionInsurance;
    });
}

const serializeFormToJson = form => JSON.stringify(
    Array.from(new FormData(form).entries())
        .reduce((m, [key, value]) =>
            Object.assign(m, {[key]: value}), {})
);

function handleAddCompanyFormSubmit() {
    const form = $("#addCompanyForm");
    form.on('submit', function (e) {
        e.preventDefault();

        const csrfToken = document.cookie
            .split('; ')
            .find(row => row.startsWith('XSRF-TOKEN='))
            .split('=')[1];

        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('X-XSRF-TOKEN', csrfToken);
        });

        $.ajax({
            url: 'companies',
            type: 'post',
            contentType: 'application/json',
            data: serializeFormToJson(this),
            success: function (data) {
                $("#companiesTable").find("tr:gt(0)").remove();
                alert(data)
                loadCompanies()
            },
            error: function (jqXhr, textStatus, errorThrown) {
                alert(jqXhr.status + " " + errorThrown)
            }
        });
    });
}

window.onload = function () {
    loadCompanies();
    handleAddCompanyFormSubmit()
};
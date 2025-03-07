$('#transportForm').show();
$('#carForms').show();
$('#logoutSection').show();
$('#addDataSection').show();

$('#vehicleDropdown').on('change', function() {
    const selectedVehicle = $(this).val();
    $('#vehicleDropdown').val(selectedVehicle);
    if (selectedVehicle === 'car') {
        $('#carForms').show();
        $('#busForms').hide();
        $('#planeForms').hide();
        $('#transportForm').show();
        $('#logoutSection').show();
        $('#addDataSection').show();
    } else if (selectedVehicle === 'bus') {
        $('#busForms').show();
        $('#carForms').hide();
        $('#planeForms').hide();
        $('#transportForm').show();
        $('#logoutSection').show();
        $('#addDataSection').show();
    }else if(selectedVehicle === 'plane'){
        $('#planeForms').show();
        $('#carForms').hide();
        $('#busForms').hide();
        $('#transportForm').hide();
        $('#logoutSection').show();
        $('#addDataSection').show();
    } else{
        $('#carForms').hide();
        $('#busForms').hide();
        $('#planeForms').hide();
        $('#transportForm').show();
        $('#logoutSection').show();
        $('#addDataSection').show();
    }
});

$('#year, #carName').on('input', function() {
    const year = $('#year').val();
    const carName = $('#carName').val();

    if ((year.length === 4) && carName) {
        $.get('/getMakes', { year: year, carName: carName }, function(data) {
            $('#make').html('<option value="">-- Choose a Make --</option>');
            data.forEach(function(make) {
                $('#make').append('<option value="' + make + '">' + make + '</option>');
            });
        });
    }
});


$('#makeForm').on('change', function(e) {
    e.preventDefault();
    const year = $('#yearHidden').val();
    const make = $('#make').val();
    $.get('/getModels', { year: year, make: make }, function(data) {
        $('#model').html('<option value="">-- Choose a Model --</option>');
        data.forEach(function(model) {
            $('#model').append('<option value="' + model + '">' + model + '</option>');
        });
    });

});

$('#modelForm').on('change', function(e) {
    e.preventDefault();
    const year = $('#year').val();
    const make = $('#make').val();
    const model = $('#model').val();

    $.get('/getFuelSize', { year: year, make: make, model: model }, function(data) {
        $('#tank').html('<option value="">-- Choose a Tank Size --</option>');

        $.each(data, function(label, value) {
            $('#tank').append('<option value="' + value + '">' + label + '</option>');
        });
    });
});

$('#offsetTourForm').on('submit', function(e){
    e.preventDefault();
    let checkedTours = [];
    document.querySelectorAll('.checkbox-wrapper-31 input:checked').forEach(function(checkbox) {
        let tourLabel = checkbox.closest('.spawnStyle').querySelector('label');

        if (tourLabel) {
            let tourName = tourLabel.textContent.split(':')[0].trim();
            checkedTours.push(tourName);
        }
    });

    $.get('/offsetTourMethod', {checkedTours : checkedTours}, function(data){
        window.location.reload();
    });
});


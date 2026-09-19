using ClinicManagement.Infrastructure;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();

// Mock connection string for migrations (in reality this would be in appsettings.json)
builder.Configuration["ConnectionStrings:DefaultConnection"] = "Host=localhost;Database=ClinicDb;Username=postgres;Password=postgres";

builder.Services.AddMediatR(cfg => cfg.RegisterServicesFromAssembly(typeof(ClinicManagement.Application.Visits.Commands.CallNextPatient.CallNextPatientCommand).Assembly));
builder.Services.AddInfrastructure(builder.Configuration);

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
}

app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

using (var scope = app.Services.CreateScope())
{
    var context = scope.ServiceProvider.GetRequiredService<ClinicManagement.Infrastructure.Persistence.ApplicationDbContext>();
    context.Database.EnsureCreated(); // Or Migrate()
    
    Guid doc1Id = Guid.NewGuid();
    Guid doc2Id = Guid.NewGuid();

    if (!System.Linq.Enumerable.Any(context.Doctors))
    {
        context.Doctors.Add(new ClinicManagement.Domain.Entities.Doctor(doc1Id, "Dr. Emily Chen", "Cardiology"));
        context.Doctors.Add(new ClinicManagement.Domain.Entities.Doctor(doc2Id, "Dr. Mark Lee", "Dentist"));
        context.SaveChanges();
    }
    else 
    {
        var docs = System.Linq.Enumerable.ToList(context.Doctors);
        if (docs.Count > 1) {
            doc1Id = docs[0].Id;
            doc2Id = docs[1].Id;
        }
    }

    if (!System.Linq.Enumerable.Any(context.DoctorShifts))
    {
        context.DoctorShifts.Add(new ClinicManagement.Domain.Entities.DoctorShift(doc1Id, DateTime.UtcNow, "Morning Shift (Seeded)", new TimeSpan(8, 0, 0), new TimeSpan(14, 0, 0), 50));
        context.DoctorShifts.Add(new ClinicManagement.Domain.Entities.DoctorShift(doc2Id, DateTime.UtcNow, "Evening Shift (Seeded)", new TimeSpan(16, 0, 0), new TimeSpan(22, 0, 0), 50));
        context.SaveChanges();
    }
}

app.Run();

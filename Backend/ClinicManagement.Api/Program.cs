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

app.MapControllers();

using (var scope = app.Services.CreateScope())
{
    var context = scope.ServiceProvider.GetRequiredService<ClinicManagement.Infrastructure.Persistence.ApplicationDbContext>();
    context.Database.EnsureCreated(); // Or Migrate()
    
    if (!System.Linq.Enumerable.Any(context.DoctorShifts))
    {
        var docId = Guid.NewGuid();
        context.DoctorShifts.Add(new ClinicManagement.Domain.Entities.DoctorShift(docId, DateTime.UtcNow, "Morning Shift (Seeded)", new TimeSpan(8, 0, 0), new TimeSpan(14, 0, 0), 50));
        context.DoctorShifts.Add(new ClinicManagement.Domain.Entities.DoctorShift(docId, DateTime.UtcNow, "Evening Shift (Seeded)", new TimeSpan(16, 0, 0), new TimeSpan(22, 0, 0), 50));
        context.SaveChanges();
    }
}

app.Run();

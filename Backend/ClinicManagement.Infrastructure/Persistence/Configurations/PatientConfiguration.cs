using ClinicManagement.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace ClinicManagement.Infrastructure.Persistence.Configurations
{
    public class PatientConfiguration : IEntityTypeConfiguration<Patient>
    {
        public void Configure(EntityTypeBuilder<Patient> builder)
        {
            builder.HasKey(p => p.Id);
            builder.HasIndex(p => p.PhoneNumber).IsUnique();
            builder.Property(p => p.PhoneNumber).IsRequired().HasMaxLength(20);
            builder.Property(p => p.FullName).IsRequired().HasMaxLength(150);
            builder.Property(p => p.NationalId).HasMaxLength(50);
        }
    }
}

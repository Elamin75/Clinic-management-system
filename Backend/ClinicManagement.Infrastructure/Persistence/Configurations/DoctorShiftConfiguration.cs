using ClinicManagement.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace ClinicManagement.Infrastructure.Persistence.Configurations
{
    public class DoctorShiftConfiguration : IEntityTypeConfiguration<DoctorShift>
    {
        public void Configure(EntityTypeBuilder<DoctorShift> builder)
        {
            builder.HasKey(s => s.Id);
            builder.Property(s => s.ShiftName).IsRequired().HasMaxLength(50);
            
            builder.HasMany(s => s.Visits)
                   .WithOne()
                   .HasForeignKey(v => v.ShiftId)
                   .OnDelete(DeleteBehavior.Restrict);
        }
    }
}

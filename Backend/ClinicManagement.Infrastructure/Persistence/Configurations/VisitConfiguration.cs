using ClinicManagement.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace ClinicManagement.Infrastructure.Persistence.Configurations
{
    public class VisitConfiguration : IEntityTypeConfiguration<Visit>
    {
        public void Configure(EntityTypeBuilder<Visit> builder)
        {
            builder.HasKey(v => v.Id);
            builder.Property(v => v.BankTransactionId).HasMaxLength(100);
            builder.Property(v => v.PaymentReceiptUrl).HasMaxLength(500);
            
            // Queue number can be null initially until payment is confirmed
            builder.Property(v => v.QueueNumber).IsRequired(false);
        }
    }
}

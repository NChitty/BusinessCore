package me.nchitty.bc.entities.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Converter(autoApply = true)
public class PlayerConverter implements AttributeConverter<Player, UUID> {
  @Override
  public UUID convertToDatabaseColumn(Player attribute) {
    return attribute.getUniqueId();
  }

  @Override
  public Player convertToEntityAttribute(UUID dbData) {
    return Bukkit.getPlayer(dbData);
  }
}

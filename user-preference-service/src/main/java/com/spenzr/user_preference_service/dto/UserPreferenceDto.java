package com.spenzr.user_preference_service.dto;

import com.spenzr.user_preference_service.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferenceDto {
    private Currency currency;
}

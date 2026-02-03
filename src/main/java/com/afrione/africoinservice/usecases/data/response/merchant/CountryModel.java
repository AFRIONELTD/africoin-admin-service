package com.afrione.africoinservice.usecases.data.response.merchant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by felixadewale on
 * 03/02/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryModel {
    Long id;
    private String code;
    private String name;
    private String dialingCode;
    private String flagUrl;
}

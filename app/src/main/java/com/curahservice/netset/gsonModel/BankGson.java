package com.curahservice.netset.gsonModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankGson {

    @SerializedName("bank")
    @Expose
    private Bank bank;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public class Bank {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("bank_name")
        @Expose
        private String bankName;
        @SerializedName("merchant_id")
        @Expose
        private String merchantId;
        @SerializedName("currencyIsoCode")
        @Expose
        private String currencyIsoCode;
        @SerializedName("routing_number")
        @Expose
        private String routingNumber;
        @SerializedName("account_number")
        @Expose
        private String accountNumber;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getCurrencyIsoCode() {
            return currencyIsoCode;
        }

        public void setCurrencyIsoCode(String currencyIsoCode) {
            this.currencyIsoCode = currencyIsoCode;
        }

        public String getRoutingNumber() {
            return routingNumber;
        }

        public void setRoutingNumber(String routingNumber) {
            this.routingNumber = routingNumber;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

    }
}

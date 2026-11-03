
undefined8 FUN_ffff0000b80279e0(undefined8 param_1,int param_2,int param_3)

{
  int iVar1;
  
  if ((param_2 == 2) || (param_3 == 1)) {
    iVar1 = FUN_ffff0000b802634c(DAT_ffff0000b811cf30,0x7f1,0,0xf,0);
    if (iVar1 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b744(s__RTC_MT6685_RGDCXO_AAC_ELR_1_wri_ffff0000b809f86b);
    }
    iVar1 = FUN_ffff0000b802634c(DAT_ffff0000b811cf30,0x9b2,0,1,7);
    if (iVar1 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b744(s__RTC_MT6685_RG_UVLO2V0_EN_ADDR_w_ffff0000b808ba16);
    }
  }
  return 0;
}



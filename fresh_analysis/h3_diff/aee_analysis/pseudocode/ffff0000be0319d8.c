
undefined8 FUN_ffff0000be0319d8(undefined8 param_1,int param_2,int param_3)

{
  int iVar1;
  
  if ((param_2 == 2) || (param_3 == 1)) {
    iVar1 = FUN_ffff0000be030344(DAT_ffff0000be119720,0x7f1,0,0xf,0);
    if (iVar1 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee24(s__RTC_MT6685_RGDCXO_AAC_ELR_1_wri_ffff0000be0c41b6);
    }
    iVar1 = FUN_ffff0000be030344(DAT_ffff0000be119720,0x9b2,0,1,7);
    if (iVar1 < 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee24(s__RTC_MT6685_RG_UVLO2V0_EN_ADDR_w_ffff0000be0ab876);
    }
  }
  return 0;
}



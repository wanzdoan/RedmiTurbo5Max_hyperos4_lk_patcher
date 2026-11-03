
int FUN_ffff0000b8003e9c(uint param_1,undefined8 param_2)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000b80262bc(DAT_ffff0000b810c038,param_1,param_2,2);
  if (iVar1 < 0) {
    FUN_ffff0000b804c33c
              (s__RTC__s__d___s__fail__addr___0x__ffff0000b8091493,
               s_mt6685_rtc_read_ffff0000b809f19f,0x95,s_mt6685_rtc_read_ffff0000b809f19f,
               param_1 & 0xffff,iVar1);
  }
  else {
    iVar1 = 0;
  }
  return iVar1;
}



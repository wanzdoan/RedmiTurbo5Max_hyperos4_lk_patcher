
int FUN_ffff0000be003aec(uint param_1,undefined8 param_2)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000be0302b4(DAT_ffff0000be0f9018,param_1,param_2,2);
  if (iVar1 < 0) {
    FUN_ffff0000be05fd98
              (s__RTC__s__d___s__fail__addr___0x__ffff0000be0b2a47,
               s_mt6685_rtc_read_ffff0000be0c3a6e,0x95,s_mt6685_rtc_read_ffff0000be0c3a6e,
               param_1 & 0xffff,iVar1);
  }
  else {
    iVar1 = 0;
  }
  return iVar1;
}



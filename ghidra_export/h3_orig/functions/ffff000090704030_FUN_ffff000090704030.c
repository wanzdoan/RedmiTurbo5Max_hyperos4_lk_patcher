// ffff000090704030  size=116  FUN_ffff000090704030

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090704030(uint param_1,undefined8 param_2)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000907401d4(_DAT_ffff00009083d0a8,param_1,param_2,2);
  if (iVar1 < 0) {
    FUN_ffff00009077de54
              (s__RTC__s__d___s__fail__addr___0x__ffff0000907e59ae,
               s_mt6685_rtc_read_ffff0000907fe74d,0x95,s_mt6685_rtc_read_ffff0000907fe74d,
               param_1 & 0xffff,iVar1);
  }
  else {
    iVar1 = 0;
  }
  return iVar1;
}



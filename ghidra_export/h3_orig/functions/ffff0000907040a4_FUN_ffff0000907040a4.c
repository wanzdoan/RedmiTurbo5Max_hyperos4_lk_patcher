// ffff0000907040a4  size=156  FUN_ffff0000907040a4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907040a4(uint param_1,undefined2 param_2)

{
  int iVar1;
  undefined2 local_24 [2];
  
  local_24[0] = param_2;
  iVar1 = FUN_ffff00009074021c(_DAT_ffff00009083d0a8,param_1,local_24,2);
  if (iVar1 < 0) {
    FUN_ffff00009077de54
              (s__RTC__s__d___s__fail__addr___0x__ffff0000907e59ae,
               s_mt6685_rtc_write_ffff0000907ef146,0xa9,s_mt6685_rtc_write_ffff0000907ef146,
               param_1 & 0xffff,iVar1);
  }
  else {
    if (((param_1 & 0xffff) == 0x618) || ((param_1 & 0xffff) == 0x5c2)) {
      FUN_ffff00009077cbd8(1000);
    }
    iVar1 = 0;
  }
  return iVar1;
}



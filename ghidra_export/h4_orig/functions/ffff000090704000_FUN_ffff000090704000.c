// ffff000090704000  size=156  FUN_ffff000090704000

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090704000(uint param_1,undefined2 param_2)

{
  int iVar1;
  undefined2 local_24 [2];
  
  local_24[0] = param_2;
  iVar1 = FUN_ffff0000907404b4(_DAT_ffff00009083d048,param_1,local_24,2);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c
              (s__RTC__s__d___s__fail__addr___0x__ffff0000907e5ab2,
               s_mt6685_rtc_write_ffff0000907ef2b7,0xa9,s_mt6685_rtc_write_ffff0000907ef2b7,
               param_1 & 0xffff,iVar1);
  }
  else {
    if (((param_1 & 0xffff) == 0x618) || ((param_1 & 0xffff) == 0x5c2)) {
      FUN_ffff00009077cea0(1000);
    }
    iVar1 = 0;
  }
  return iVar1;
}



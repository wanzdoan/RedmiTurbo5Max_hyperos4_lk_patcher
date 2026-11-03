
int FUN_ffff000090703f8c(uint param_1,undefined8 param_2)

{
  int iVar1;
  
  iVar1 = FUN_ffff00009074046c(DAT_ffff00009083d048,param_1,param_2,2);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c
              (s__RTC__s__d___s__fail__addr___0x__ffff0000907e5ab2,
               s_mt6685_rtc_read_ffff0000907fe8f5,0x95,s_mt6685_rtc_read_ffff0000907fe8f5,
               param_1 & 0xffff,iVar1);
  }
  else {
    iVar1 = 0;
  }
  return iVar1;
}



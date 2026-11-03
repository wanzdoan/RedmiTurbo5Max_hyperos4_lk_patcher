// ffff000090703e58  size=308  FUN_ffff000090703e58

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090703e58(undefined8 param_1,int param_2,int param_3)

{
  int iVar1;
  undefined8 uVar2;
  undefined1 local_28 [4];
  byte local_24 [4];
  
  if ((param_2 != 2) && (param_3 != 1)) {
    return 0;
  }
  local_28[0] = 0;
  iVar1 = FUN_ffff00009074046c(_DAT_ffff00009083d048,0x129,local_28,1);
  if (iVar1 < 0) {
    uVar2 = 0x70;
  }
  else {
    local_28[0] = 1;
    iVar1 = FUN_ffff0000907404b4(_DAT_ffff00009083d048,0x129,local_28,1);
    if (-1 < iVar1) {
      local_24[0] = 0;
      iVar1 = FUN_ffff00009074046c(_DAT_ffff00009083d048,0x12f,local_24,1);
      if (iVar1 < 0) {
        uVar2 = 0x70;
      }
      else {
        local_24[0] = local_24[0] | 1;
        iVar1 = FUN_ffff0000907404b4(_DAT_ffff00009083d048,0x12f,local_24,1);
        if (-1 < iVar1) {
          return 0;
        }
        uVar2 = 0x79;
      }
      FUN_ffff00009077e11c
                (s__RTC__s__d___s__fail__addr___0x__ffff0000907e5ab2,
                 s_rtc_config_interface_ffff0000907fca49,uVar2,
                 s_rtc_config_interface_ffff0000907fca49,0x12f,iVar1);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cee0(s__RTC_RG_SEQ_OFF_write_fail__ffff0000907ef2c8);
    }
    uVar2 = 0x79;
  }
  FUN_ffff00009077e11c
            (s__RTC__s__d___s__fail__addr___0x__ffff0000907e5ab2,
             s_rtc_config_interface_ffff0000907fca49,uVar2,s_rtc_config_interface_ffff0000907fca49,
             0x129,iVar1);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0(s__RTC_TOP_RST_MISC_CLR_write_fail_ffff0000907e72da);
}



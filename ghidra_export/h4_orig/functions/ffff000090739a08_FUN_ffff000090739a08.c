// ffff000090739a08  size=248  FUN_ffff000090739a08

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090739a08(void)

{
  uint uVar1;
  char *pcVar2;
  
  if (_DAT_ffff00009086dec0 == (uint *)0x0) {
    pcVar2 = s_Failed_ffff0000907d7b14;
  }
  else {
    FUN_ffff000090739b00(_DAT_ffff00009086dec0,*_DAT_ffff00009086dec0 & 0xfffffffe | 0x22000000);
    pcVar2 = s_Succeeded_ffff0000907f64f3;
  }
  FUN_ffff00009077e11c(s__s_to_disable_DDR_RSV_MODE_when_l_ffff0000908027be,pcVar2);
  if (_DAT_ffff00009086dec0 == (uint *)0x0) {
    pcVar2 = s_Failed_ffff0000907d7b14;
  }
  else {
    FUN_ffff000090739b00
              (_DAT_ffff00009086dec0 + 0x10,_DAT_ffff00009086dec0[0x10] & 0x6afffffe | 0x95000000);
    pcVar2 = s_Succeeded_ffff0000907f64f3;
  }
  FUN_ffff00009077e11c(s__s_to_disable_DRM_LATCH_EN_when_l_ffff0000907e1475,pcVar2);
  if (_DAT_ffff00009086dec0 == (uint *)0x0) {
    pcVar2 = s_Failed_ffff0000907d7b14;
  }
  else {
    uVar1 = 0x6afdffff;
    if (DAT_ffff00009081942c != 1) {
      uVar1 = 0x6af7ffff;
    }
    FUN_ffff000090739b00
              (_DAT_ffff00009086dec0 + 0x11,uVar1 & _DAT_ffff00009086dec0[0x11] | 0x95000000);
    pcVar2 = s_Succeeded_ffff0000907f64f3;
  }
  FUN_ffff00009077e11c(s__s_to_disable_DFD_count_en_when_l_ffff0000907ee340,pcVar2);
  return 0;
}




undefined8 FUN_ffff0000be02aaac(undefined8 param_1,int param_2,int param_3)

{
  uint uVar1;
  char *pcVar2;
  
  if ((param_2 == 1) && (param_3 == 7)) {
    if (DAT_ffff0000be118180 == (uint *)0x0) {
      pcVar2 = s_Failed_ffff0000be0a90cd;
    }
    else {
      FUN_ffff0000be02abb8(DAT_ffff0000be118180,*DAT_ffff0000be118180 & 0xfffffffe | 0x22000000);
      pcVar2 = s_Succeeded_ffff0000be0be340;
    }
    FUN_ffff0000be05fd98(s__s_to_disable_DDR_RSV_MODE_when_l_ffff0000be0c62fb,pcVar2);
    if (DAT_ffff0000be118180 == (uint *)0x0) {
      pcVar2 = s_Failed_ffff0000be0a90cd;
    }
    else {
      FUN_ffff0000be02abb8
                (DAT_ffff0000be118180 + 0x10,DAT_ffff0000be118180[0x10] & 0x6afffffe | 0x95000000);
      pcVar2 = s_Succeeded_ffff0000be0be340;
    }
    FUN_ffff0000be05fd98(s__s_to_disable_DRM_LATCH_EN_when_l_ffff0000be0af9c8,pcVar2);
    if (DAT_ffff0000be118180 == (uint *)0x0) {
      pcVar2 = s_Failed_ffff0000be0a90cd;
    }
    else {
      uVar1 = 0x6afdffff;
      if (DAT_ffff0000be0d6764 != 1) {
        uVar1 = 0x6af7ffff;
      }
      FUN_ffff0000be02abb8
                (DAT_ffff0000be118180 + 0x11,uVar1 & DAT_ffff0000be118180[0x11] | 0x95000000);
      pcVar2 = s_Succeeded_ffff0000be0be340;
    }
    FUN_ffff0000be05fd98(s__s_to_disable_DFD_count_en_when_l_ffff0000be0b8ada,pcVar2);
  }
  return 0;
}



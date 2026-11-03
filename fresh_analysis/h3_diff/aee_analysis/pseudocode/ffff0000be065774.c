
undefined8 FUN_ffff0000be065774(void)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined8 uVar4;
  
  lVar2 = FUN_ffff0000be01fa8c(s_aee_enable_ffff0000be0aac7c);
  iVar1 = FUN_ffff0000be01fbbc();
  if (iVar1 != 0) {
    if (lVar2 == 0) {
      FUN_ffff0000be05fd98
                (s__s__d____s____AEE_DEFAULT_SETTIN_ffff0000be0b14e1,
                 s_aee_check_enable_ffff0000be0c23f3,0x12d,s_aee_check_enable_ffff0000be0c23f3);
    }
    else {
      iVar1 = FUN_ffff0000be060cb8(lVar2,&DAT_ffff0000be0bd9d8);
      if (iVar1 == 0) {
        pcVar3 = s__s__d__lk_aee_dcfg___mini_ffff0000be0b5bac;
        uVar4 = 0x11b;
      }
      else {
        iVar1 = FUN_ffff0000be060cb8(lVar2,&DAT_ffff0000be0b4903);
        if (iVar1 == 0) {
          FUN_ffff0000be05fd98
                    (s__s__d__lk_aee_dcfg___full_ffff0000be0ba510,
                     s_aee_check_enable_ffff0000be0c23f3,0x122);
          uVar4 = 0xfc11;
          goto LAB_ffff0000be065830;
        }
        iVar1 = FUN_ffff0000be060cb8(lVar2,&DAT_ffff0000be0bac01);
        if (iVar1 == 0) {
          FUN_ffff0000be05fd98
                    (s__s__d__lk_aee_dcfg___no_ffff0000be0b2666,s_aee_check_enable_ffff0000be0c23f3,
                     0x126);
          uVar4 = 0;
          goto LAB_ffff0000be065830;
        }
        pcVar3 = s__s__d__lk_aee_dcfg_not_recognize_ffff0000be0acf79;
        uVar4 = 0x129;
      }
      FUN_ffff0000be05fd98(pcVar3,s_aee_check_enable_ffff0000be0c23f3,uVar4);
    }
  }
  uVar4 = 0x3121;
LAB_ffff0000be065830:
  FUN_ffff0000be05fd98
            (s__s__d__enable__d_ffff0000be0a879e,s_aee_check_enable_ffff0000be0c23f3,0x131,uVar4);
  return uVar4;
}



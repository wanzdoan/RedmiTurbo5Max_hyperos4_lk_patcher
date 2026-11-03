// File offset entry: 0x4fef8

undefined8 FUN_ffff0000b804fef8(void)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined8 uVar4;
  
  lVar2 = FUN_ffff0000b8013f6c(s_aee_enable_ffff0000b8089fbd);
  iVar1 = FUN_ffff0000b801409c();
  if (iVar1 != 0) {
    if (lVar2 == 0) {
      FUN_ffff0000b804beb0
                (s__s__d____s____AEE_DEFAULT_SETTIN_ffff0000b808f2d2,
                 s_aee_check_enable_ffff0000b809cd02,0x12d,s_aee_check_enable_ffff0000b809cd02);
    }
    else {
      iVar1 = FUN_ffff0000b804cdbc(lVar2,&DAT_ffff0000b8099176);
      if (iVar1 == 0) {
        pcVar3 = s__s__d__lk_aee_dcfg___mini_ffff0000b8092c35;
        uVar4 = 0x11b;
      }
      else {
        iVar1 = FUN_ffff0000b804cdbc(lVar2,&DAT_ffff0000b8091c19);
        if (iVar1 == 0) {
          FUN_ffff0000b804beb0
                    (s__s__d__lk_aee_dcfg___full_ffff0000b8096737,
                     s_aee_check_enable_ffff0000b809cd02,0x122);
          uVar4 = 0xfc11;
          goto LAB_ffff0000b804ffb4;
        }
        iVar1 = FUN_ffff0000b804cdbc(lVar2,&DAT_ffff0000b8097509);
        if (iVar1 == 0) {
          FUN_ffff0000b804beb0
                    (s__s__d__lk_aee_dcfg___no_ffff0000b809015c,s_aee_check_enable_ffff0000b809cd02,
                     0x126);
          uVar4 = 0;
          goto LAB_ffff0000b804ffb4;
        }
        pcVar3 = s__s__d__lk_aee_dcfg_not_recognize_ffff0000b808bb4e;
        uVar4 = 0x129;
      }
      FUN_ffff0000b804beb0(pcVar3,s_aee_check_enable_ffff0000b809cd02,uVar4);
    }
  }
  uVar4 = 0x3121;
LAB_ffff0000b804ffb4:
  FUN_ffff0000b804beb0
            (s__s__d__enable__d_ffff0000b8088548,s_aee_check_enable_ffff0000b809cd02,0x131,uVar4);
  return uVar4;
}



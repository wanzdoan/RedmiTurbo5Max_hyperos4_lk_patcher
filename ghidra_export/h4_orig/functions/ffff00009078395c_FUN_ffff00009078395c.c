// ffff00009078395c  size=300  FUN_ffff00009078395c

undefined8 FUN_ffff00009078395c(void)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined8 uVar4;
  
  lVar2 = FUN_ffff000090721d30(s_aee_enable_ffff0000907d9dc3);
  iVar1 = FUN_ffff000090721e60();
  if (iVar1 != 0) {
    if (lVar2 == 0) {
      FUN_ffff00009077e11c
                (s__s__d____s____AEE_DEFAULT_SETTIN_ffff0000907e3a34,
                 s_aee_check_enable_ffff0000907fc510,0x12d,s_aee_check_enable_ffff0000907fc510);
    }
    else {
      iVar1 = FUN_ffff00009077f0ac(lVar2,&DAT_ffff0000907f554a);
      if (iVar1 == 0) {
        pcVar3 = s__s__d__lk_aee_dcfg___mini_ffff0000907ea021;
        uVar4 = 0x11b;
      }
      else {
        iVar1 = FUN_ffff00009077f0ac(lVar2,&DAT_ffff0000907e84a1);
        if (iVar1 == 0) {
          FUN_ffff00009077e11c
                    (s__s__d__lk_aee_dcfg___full_ffff0000907f0620,
                     s_aee_check_enable_ffff0000907fc510,0x122);
          uVar4 = 0xfc11;
          goto LAB_ffff000090783a18;
        }
        iVar1 = FUN_ffff00009077f0ac(lVar2,&DAT_ffff0000907f0cf7);
        if (iVar1 == 0) {
          FUN_ffff00009077e11c
                    (s__s__d__lk_aee_dcfg___no_ffff0000907e5525,s_aee_check_enable_ffff0000907fc510,
                     0x126);
          uVar4 = 0;
          goto LAB_ffff000090783a18;
        }
        pcVar3 = s__s__d__lk_aee_dcfg_not_recognize_ffff0000907dd35b;
        uVar4 = 0x129;
      }
      FUN_ffff00009077e11c(pcVar3,s_aee_check_enable_ffff0000907fc510,uVar4);
    }
  }
  uVar4 = 0x3121;
LAB_ffff000090783a18:
  FUN_ffff00009077e11c
            (s__s__d__enable__d_ffff0000907d6c67,s_aee_check_enable_ffff0000907fc510,0x131,uVar4);
  return uVar4;
}



// ffff000090783694  size=300  FUN_ffff000090783694

undefined8 FUN_ffff000090783694(void)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined8 uVar4;
  
  lVar2 = FUN_ffff000090721af0(s_aee_enable_ffff0000907d9d2c);
  iVar1 = FUN_ffff000090721c20();
  if (iVar1 != 0) {
    if (lVar2 == 0) {
      FUN_ffff00009077de54
                (s__s__d____s____AEE_DEFAULT_SETTIN_ffff0000907e3963,
                 s_aee_check_enable_ffff0000907fc374,0x12d,s_aee_check_enable_ffff0000907fc374);
    }
    else {
      iVar1 = FUN_ffff00009077ede4(lVar2,&DAT_ffff0000907f53dd);
      if (iVar1 == 0) {
        pcVar3 = s__s__d__lk_aee_dcfg___mini_ffff0000907e9edd;
        uVar4 = 0x11b;
      }
      else {
        iVar1 = FUN_ffff00009077ede4(lVar2,&DAT_ffff0000907e835d);
        if (iVar1 == 0) {
          FUN_ffff00009077de54
                    (s__s__d__lk_aee_dcfg___full_ffff0000907f04af,
                     s_aee_check_enable_ffff0000907fc374,0x122);
          uVar4 = 0xfc11;
          goto LAB_ffff000090783750;
        }
        iVar1 = FUN_ffff00009077ede4(lVar2,&DAT_ffff0000907f0b86);
        if (iVar1 == 0) {
          FUN_ffff00009077de54
                    (s__s__d__lk_aee_dcfg___no_ffff0000907e5421,s_aee_check_enable_ffff0000907fc374,
                     0x126);
          uVar4 = 0;
          goto LAB_ffff000090783750;
        }
        pcVar3 = s__s__d__lk_aee_dcfg_not_recognize_ffff0000907dd285;
        uVar4 = 0x129;
      }
      FUN_ffff00009077de54(pcVar3,s_aee_check_enable_ffff0000907fc374,uVar4);
    }
  }
  uVar4 = 0x3121;
LAB_ffff000090783750:
  FUN_ffff00009077de54
            (s__s__d__enable__d_ffff0000907d6bd1,s_aee_check_enable_ffff0000907fc374,0x131,uVar4);
  return uVar4;
}



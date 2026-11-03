// File offset entry: 0x50384

undefined8 FUN_ffff0000b8050384(void)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined8 uVar4;
  
  lVar2 = FUN_ffff0000b80143f4(s_aee_enable_ffff0000b808b01d);
  iVar1 = FUN_ffff0000b8014524();
  if (iVar1 != 0) {
    if (lVar2 == 0) {
      FUN_ffff0000b804c33c
                (s__s__d____s____AEE_DEFAULT_SETTIN_ffff0000b8090332,
                 s_aee_check_enable_ffff0000b809de15,0x12d,s_aee_check_enable_ffff0000b809de15);
    }
    else {
      iVar1 = FUN_ffff0000b804d248(lVar2,&DAT_ffff0000b809a24f);
      if (iVar1 == 0) {
        pcVar3 = s__s__d__lk_aee_dcfg___mini_ffff0000b8093d04;
        uVar4 = 0x11b;
      }
      else {
        iVar1 = FUN_ffff0000b804d248(lVar2,&DAT_ffff0000b8092cc0);
        if (iVar1 == 0) {
          FUN_ffff0000b804c33c
                    (s__s__d__lk_aee_dcfg___full_ffff0000b8097832,
                     s_aee_check_enable_ffff0000b809de15,0x122);
          uVar4 = 0xfc11;
          goto LAB_ffff0000b8050440;
        }
        iVar1 = FUN_ffff0000b804d248(lVar2,&DAT_ffff0000b80985e2);
        if (iVar1 == 0) {
          FUN_ffff0000b804c33c
                    (s__s__d__lk_aee_dcfg___no_ffff0000b80911e5,s_aee_check_enable_ffff0000b809de15,
                     0x126);
          uVar4 = 0;
          goto LAB_ffff0000b8050440;
        }
        pcVar3 = s__s__d__lk_aee_dcfg_not_recognize_ffff0000b808cb94;
        uVar4 = 0x129;
      }
      FUN_ffff0000b804c33c(pcVar3,s_aee_check_enable_ffff0000b809de15,uVar4);
    }
  }
  uVar4 = 0x3121;
LAB_ffff0000b8050440:
  FUN_ffff0000b804c33c
            (s__s__d__enable__d_ffff0000b808957d,s_aee_check_enable_ffff0000b809de15,0x131,uVar4);
  return uVar4;
}



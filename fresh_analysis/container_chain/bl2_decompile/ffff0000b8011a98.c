
ulong FUN_ffff0000b8011a98(void)

{
  int iVar1;
  int iVar2;
  long lVar3;
  ulong uVar4;
  
  iVar1 = FUN_ffff0000b800e1a4();
  iVar2 = FUN_ffff0000b800e190();
  FUN_ffff0000b804c33c
            (s__s__d___XMCHG_LK__boot_mode____d_ffff0000b808c449,
             s_mtk_charger_kpoc_check_ffff0000b8095232,0x64f,iVar1,iVar2);
  lVar3 = FUN_ffff0000b8027c48(s_off_mode_charge_ffff0000b809e28f);
  if (lVar3 == 0) {
    FUN_ffff0000b800fcac(1);
  }
  else {
    uVar4 = FUN_ffff0000b804bee4();
    if ((int)uVar4 == 0) {
      return uVar4;
    }
  }
  return (ulong)(iVar1 == 0 && iVar2 == 1);
}



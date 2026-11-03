// ffff000090720518  size=128  FUN_ffff000090720518

ulong FUN_ffff000090720518(void)

{
  int iVar1;
  int iVar2;
  long lVar3;
  ulong uVar4;
  
  iVar1 = FUN_ffff00009071c3a4();
  iVar2 = FUN_ffff00009071c390();
  FUN_ffff00009077de54
            (s__s__d___XMCHG_LK__boot_mode____d_ffff0000907dc3ba,
             s_mtk_charger_kpoc_check_ffff0000907ec088,0x5b0,iVar1,iVar2);
  lVar3 = FUN_ffff000090741944(s_off_mode_charge_ffff0000907fc98e);
  if (lVar3 == 0) {
    FUN_ffff000090720444(1);
  }
  else {
    uVar4 = FUN_ffff00009077d464();
    if ((int)uVar4 == 0) {
      return uVar4;
    }
  }
  return (ulong)(iVar1 == 0 && iVar2 == 1);
}



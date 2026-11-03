
ulong FUN_ffff0000907206a0(void)

{
  int iVar1;
  int iVar2;
  long lVar3;
  ulong uVar4;
  
  iVar1 = FUN_ffff00009071c4e8();
  iVar2 = FUN_ffff00009071c4d4();
  FUN_ffff00009077e11c
            (s__s__d___XMCHG_LK__boot_mode____d_ffff0000907dc490,
             s_mtk_charger_kpoc_check_ffff0000907ec20b,0x64f,iVar1,iVar2);
  lVar3 = FUN_ffff000090741bdc(s_off_mode_charge_ffff0000907fcb2a);
  if (lVar3 == 0) {
    FUN_ffff0000907205cc(1);
  }
  else {
    uVar4 = FUN_ffff00009077d72c();
    if ((int)uVar4 == 0) {
      return uVar4;
    }
  }
  return (ulong)(iVar1 == 0 && iVar2 == 1);
}



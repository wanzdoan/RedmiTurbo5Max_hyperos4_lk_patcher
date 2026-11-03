// ffff00009078b2b0  size=384  FUN_ffff00009078b2b0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009078b2b0(void)

{
  bool bVar1;
  uint *puVar2;
  char *pcVar3;
  undefined *puVar4;
  bool bVar5;
  int iVar6;
  ulong uVar7;
  uint uVar8;
  uint local_4;
  
  local_4 = 0;
  iVar6 = FUN_ffff00009070479c(&local_4);
  if (iVar6 == 0) {
    bVar5 = 0x3d0ce7 < (ulong)local_4;
    uVar7 = (ulong)local_4 / 1000;
  }
  else {
    FUN_ffff00009077de54(s__USB__fail_to_get_vbus_ffff0000908016d0);
    uVar7 = 0;
    bVar5 = false;
  }
  if ((DAT_ffff00009088a018 == '\0') || (bVar5)) {
    bVar1 = false;
    if (DAT_ffff00009088a018 == '\0') {
      bVar1 = bVar5;
    }
    if (bVar1) {
      _DAT_ffff00009088a01c = 0;
      FUN_ffff00009077de54(s__USB__detect_plug_in__vbus__d_ffff0000907d8628,uVar7);
      FUN_ffff00009078b7a8();
    }
    else {
      _DAT_ffff00009088a01c = _DAT_ffff00009088a01c + 1;
      if (_DAT_ffff00009088a01c == 0x78) {
        puVar4 = &DAT_ffff0000907e02c6;
        if (DAT_ffff00009088a018 != '\0') {
          puVar4 = &DAT_ffff0000907ffc62;
        }
        _DAT_ffff00009088a01c = 0;
        FUN_ffff00009077de54(s__USB__remain_power__s__vbus__d_ffff000090802e8e,puVar4);
      }
    }
  }
  else {
    _DAT_ffff00009088a01c = 0;
    FUN_ffff00009077de54(s__USB__detect_plug_out__vbus__d_ffff0000907ed264,uVar7);
    FUN_ffff00009071639c(0x277);
    bVar5 = (2 < _DAT_ffff00009088a034 & DAT_ffff00009088a185 >> 1) == 0;
    puVar2 = (uint *)&DAT_ffff00001670241c;
    if (bVar5) {
      puVar2 = (uint *)&DAT_ffff000016703404;
    }
    uVar8 = 0xfffffffe;
    if (bVar5) {
      uVar8 = 0xffffffbe;
    }
    pcVar3 = s__USB__U3_pullup_D_s_ffff0000907fa810;
    if (bVar5) {
      pcVar3 = s__USB__U2_pullup_D_s_ffff0000907db8e0;
    }
    *puVar2 = uVar8 & *puVar2;
    FUN_ffff00009077de54(pcVar3,&DAT_ffff0000907dd911);
    FUN_ffff00009078e624();
    DAT_ffff00009088a018 = '\0';
  }
  return;
}



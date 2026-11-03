
void FUN_ffff00009078b570(void)

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
  iVar6 = FUN_ffff0000907046f8(&local_4);
  if (iVar6 == 0) {
    bVar5 = 0x3d0ce7 < (ulong)local_4;
    uVar7 = (ulong)local_4 / 1000;
  }
  else {
    FUN_ffff00009077e11c(s__USB__fail_to_get_vbus_ffff0000908018ac);
    uVar7 = 0;
    bVar5 = false;
  }
  if ((DAT_ffff00009088a0b0 == '\0') || (bVar5)) {
    bVar1 = false;
    if (DAT_ffff00009088a0b0 == '\0') {
      bVar1 = bVar5;
    }
    if (bVar1) {
      DAT_ffff00009088a0b4 = 0;
      FUN_ffff00009077e11c(s__USB__detect_plug_in__vbus__d_ffff0000907d86be,uVar7);
      FUN_ffff00009078ba68();
    }
    else {
      DAT_ffff00009088a0b4 = DAT_ffff00009088a0b4 + 1;
      if (DAT_ffff00009088a0b4 == 0x78) {
        puVar4 = &DAT_ffff0000907e0369;
        if (DAT_ffff00009088a0b0 != '\0') {
          puVar4 = &DAT_ffff0000907ffe0a;
        }
        DAT_ffff00009088a0b4 = 0;
        FUN_ffff00009077e11c(s__USB__remain_power__s__vbus__d_ffff000090803092,puVar4);
      }
    }
  }
  else {
    DAT_ffff00009088a0b4 = 0;
    FUN_ffff00009077e11c(s__USB__detect_plug_out__vbus__d_ffff0000907ed3e7,uVar7);
    FUN_ffff0000907164e0(0x277);
    bVar5 = (2 < DAT_ffff00009088a0cc & DAT_ffff00009088a21d >> 1) == 0;
    puVar2 = (uint *)&DAT_ffff00001670241c;
    if (bVar5) {
      puVar2 = (uint *)&DAT_ffff000016703404;
    }
    uVar8 = 0xfffffffe;
    if (bVar5) {
      uVar8 = 0xffffffbe;
    }
    pcVar3 = s__USB__U3_pullup_D_s_ffff0000907fa9ac;
    if (bVar5) {
      pcVar3 = s__USB__U2_pullup_D_s_ffff0000907db9eb;
    }
    *puVar2 = uVar8 & *puVar2;
    FUN_ffff00009077e11c(pcVar3,&DAT_ffff0000907dd9b4);
    FUN_ffff00009078e8e4();
    DAT_ffff00009088a0b0 = '\0';
  }
  return;
}



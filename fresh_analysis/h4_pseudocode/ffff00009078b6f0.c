
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 * FUN_ffff00009078b6f0(int param_1,uint param_2)

{
  long lVar1;
  int iVar2;
  int iVar3;
  uint uVar4;
  undefined *puVar5;
  undefined8 *puVar6;
  undefined1 uVar7;
  uint uVar8;
  long lVar9;
  
  if (param_1 == 0x82) {
    iVar3 = 1;
    uVar7 = 1;
    puVar5 = &DAT_ffff0000907dbf3a;
  }
  else {
    if (param_1 != 2) {
      return (undefined8 *)0x0;
    }
    uVar7 = 0;
    iVar3 = 0x10000;
    puVar5 = &DAT_ffff0000907d038f;
  }
  uVar8 = iVar3 << 1;
  if ((_DAT_ffff00009088a1f8 & uVar8) == 0) {
    if (DAT_ffff00009088a168 == '\0') {
      lVar9 = 1;
    }
    else {
      if (DAT_ffff00009088a1b0 != '\0') goto LAB_ffff00009078b760;
      lVar9 = 2;
    }
    lVar1 = lVar9 * 0x48 + -0xffff6f775ef0;
    puVar6 = &DAT_ffff00009088a0e0 + lVar9 * 9;
    iVar2 = FUN_ffff00009077f654(lVar1,s_ep_d_s_ffff0000907e6dec,1,puVar5);
    if (iVar2 < 0) {
      FUN_ffff00009077e11c(s__USB__ept_>name__copy_failed_ffff0000907f2031);
    }
    iVar2 = FUN_ffff00009078cfa0(puVar6);
    if (iVar2 == 0) {
      uVar4 = 1;
      goto LAB_ffff00009078b8ac;
    }
    FUN_ffff00009077e11c(s__USB___s_gpd_alloc_failed_ffff0000907ded79,lVar1);
  }
LAB_ffff00009078b760:
  uVar8 = iVar3 << 2;
  if ((_DAT_ffff00009088a1f8 & uVar8) == 0) {
    if (DAT_ffff00009088a168 == '\0') {
      lVar9 = 1;
    }
    else {
      if (DAT_ffff00009088a1b0 != '\0') {
        return (undefined8 *)0x0;
      }
      lVar9 = 2;
    }
    lVar1 = lVar9 * 0x48 + -0xffff6f775ef0;
    puVar6 = &DAT_ffff00009088a0e0 + lVar9 * 9;
    iVar3 = FUN_ffff00009077f654(lVar1,s_ep_d_s_ffff0000907e6dec,2,puVar5);
    if (iVar3 < 0) {
      FUN_ffff00009077e11c(s__USB__ept_>name__copy_failed_ffff0000907f2031);
    }
    iVar3 = FUN_ffff00009078cfa0(puVar6);
    if (iVar3 == 0) {
      uVar4 = 2;
LAB_ffff00009078b8ac:
      lVar9 = lVar9 * 0x48;
      *puVar6 = 0;
      *(undefined2 *)(lVar9 + -0xffff6f775ede) = 0x102;
      iVar3 = 0x10000;
      if (param_1 == 0x82) {
        iVar3 = 1;
      }
      *(char *)(lVar9 + -0xffff6f775ee0) = (char)uVar4;
      *(uint *)(lVar9 + -0xffff6f775ee4) = param_2 & 0xffff;
      *(undefined1 *)(lVar9 + -0xffff6f775edf) = uVar7;
      *(int *)(lVar9 + -0xffff6f775edc) = iVar3 << (ulong)uVar4;
      _DAT_ffff00009088a1f8 = _DAT_ffff00009088a1f8 | uVar8;
      return puVar6;
    }
    FUN_ffff00009077e11c(s__USB___s_gpd_alloc_failed_ffff0000907ded79,lVar1);
  }
  return (undefined8 *)0x0;
}



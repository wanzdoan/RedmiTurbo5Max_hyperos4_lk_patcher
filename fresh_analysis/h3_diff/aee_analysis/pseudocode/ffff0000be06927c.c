
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 * FUN_ffff0000be06927c(int param_1,uint param_2)

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
    puVar5 = &DAT_ffff0000be0ac5ea;
  }
  else {
    if (param_1 != 2) {
      return (undefined8 *)0x0;
    }
    uVar7 = 0;
    iVar3 = 0x10000;
    puVar5 = &DAT_ffff0000be0a3cce;
  }
  uVar8 = iVar3 << 1;
  if ((_DAT_ffff0000be134880 & uVar8) == 0) {
    if (DAT_ffff0000be1347f0 == '\0') {
      lVar9 = 1;
    }
    else {
      if (DAT_ffff0000be134838 != '\0') goto LAB_ffff0000be0692ec;
      lVar9 = 2;
    }
    lVar1 = lVar9 * 0x48 + -0xffff41ecb868;
    puVar6 = &DAT_ffff0000be134768 + lVar9 * 9;
    iVar2 = FUN_ffff0000be061134(lVar1,s_ep_d_s_ffff0000be0b3672,1,puVar5);
    if (iVar2 < 0) {
      FUN_ffff0000be05fd98(s__USB__ept_>name__copy_failed_ffff0000be0bb5c8);
    }
    iVar2 = FUN_ffff0000be06ab2c(puVar6);
    if (iVar2 == 0) {
      uVar4 = 1;
      goto LAB_ffff0000be069438;
    }
    FUN_ffff0000be05fd98(s__USB___s_gpd_alloc_failed_ffff0000be0adee3,lVar1);
  }
LAB_ffff0000be0692ec:
  uVar8 = iVar3 << 2;
  if ((_DAT_ffff0000be134880 & uVar8) == 0) {
    if (DAT_ffff0000be1347f0 == '\0') {
      lVar9 = 1;
    }
    else {
      if (DAT_ffff0000be134838 != '\0') {
        return (undefined8 *)0x0;
      }
      lVar9 = 2;
    }
    lVar1 = lVar9 * 0x48 + -0xffff41ecb868;
    puVar6 = &DAT_ffff0000be134768 + lVar9 * 9;
    iVar3 = FUN_ffff0000be061134(lVar1,s_ep_d_s_ffff0000be0b3672,2,puVar5);
    if (iVar3 < 0) {
      FUN_ffff0000be05fd98(s__USB__ept_>name__copy_failed_ffff0000be0bb5c8);
    }
    iVar3 = FUN_ffff0000be06ab2c(puVar6);
    if (iVar3 == 0) {
      uVar4 = 2;
LAB_ffff0000be069438:
      lVar9 = lVar9 * 0x48;
      *puVar6 = 0;
      *(undefined2 *)(lVar9 + -0xffff41ecb856) = 0x102;
      iVar3 = 0x10000;
      if (param_1 == 0x82) {
        iVar3 = 1;
      }
      *(char *)(lVar9 + -0xffff41ecb858) = (char)uVar4;
      *(uint *)(lVar9 + -0xffff41ecb85c) = param_2 & 0xffff;
      *(undefined1 *)(lVar9 + -0xffff41ecb857) = uVar7;
      *(int *)(lVar9 + -0xffff41ecb854) = iVar3 << (ulong)uVar4;
      _DAT_ffff0000be134880 = _DAT_ffff0000be134880 | uVar8;
      return puVar6;
    }
    FUN_ffff0000be05fd98(s__USB___s_gpd_alloc_failed_ffff0000be0adee3,lVar1);
  }
  return (undefined8 *)0x0;
}



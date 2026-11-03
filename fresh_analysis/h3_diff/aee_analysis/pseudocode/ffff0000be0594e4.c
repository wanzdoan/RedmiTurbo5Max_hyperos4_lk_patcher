
undefined4 FUN_ffff0000be0594e4(long *param_1,int param_2)

{
  long lVar1;
  long lVar2;
  uint uVar3;
  int iVar4;
  int iVar5;
  byte bVar6;
  ulong uVar7;
  long lVar8;
  long lVar9;
  
  while ((*(int *)((long)param_1 + 0xb4) != 0 ||
         (FUN_ffff0000be057a94(param_1), *(int *)((long)param_1 + 0xb4) != 0))) {
    uVar3 = *(uint *)((long)param_1 + 0x170c);
    *(undefined4 *)(param_1 + 0x14) = 0;
    bVar6 = *(byte *)(param_1[0xc] + (ulong)*(uint *)((long)param_1 + 0xac));
    *(uint *)((long)param_1 + 0x170c) = uVar3 + 1;
    *(undefined1 *)(param_1[0x2e0] + (ulong)uVar3) = 0;
    uVar3 = *(uint *)((long)param_1 + 0x170c);
    *(uint *)((long)param_1 + 0x170c) = uVar3 + 1;
    *(undefined1 *)(param_1[0x2e0] + (ulong)uVar3) = 0;
    uVar3 = *(uint *)((long)param_1 + 0x170c);
    *(uint *)((long)param_1 + 0x170c) = uVar3 + 1;
    *(byte *)(param_1[0x2e0] + (ulong)uVar3) = bVar6;
    iVar4 = *(int *)((long)param_1 + 0xb4);
    iVar5 = *(int *)((long)param_1 + 0x170c);
    lVar8 = param_1[0x2e2];
    uVar3 = *(int *)((long)param_1 + 0xac) + 1;
    *(short *)((long)param_1 + (ulong)bVar6 * 4 + 0xd4) =
         *(short *)((long)param_1 + (ulong)bVar6 * 4 + 0xd4) + 1;
    *(int *)((long)param_1 + 0xb4) = iVar4 + -1;
    *(uint *)((long)param_1 + 0xac) = uVar3;
    if (iVar5 == (int)lVar8) {
      uVar7 = param_1[0x13];
      if ((long)uVar7 < 0) {
        lVar8 = 0;
      }
      else {
        lVar8 = param_1[0xc] + (uVar7 & 0xffffffff);
      }
      FUN_ffff0000be05b044(param_1,lVar8,uVar3 - uVar7,0);
      lVar9 = *param_1;
      lVar8 = *(long *)(lVar9 + 0x38);
      param_1[0x13] = (ulong)*(uint *)((long)param_1 + 0xac);
      FUN_ffff0000be05ae50(lVar8);
      uVar3 = *(uint *)(lVar9 + 0x20);
      if (*(uint *)(lVar8 + 0x28) <= *(uint *)(lVar9 + 0x20)) {
        uVar3 = *(uint *)(lVar8 + 0x28);
      }
      uVar7 = (ulong)uVar3;
      if (uVar3 != 0) {
        FUN_ffff0000be0607b0(*(undefined8 *)(lVar9 + 0x18),*(undefined8 *)(lVar8 + 0x20),uVar7);
        lVar1 = *(long *)(lVar8 + 0x20);
        lVar2 = *(long *)(lVar8 + 0x28);
        *(ulong *)(lVar9 + 0x18) = *(long *)(lVar9 + 0x18) + uVar7;
        lVar2 = lVar2 - uVar7;
        *(ulong *)(lVar9 + 0x28) = *(long *)(lVar9 + 0x28) + uVar7;
        *(uint *)(lVar9 + 0x20) = *(int *)(lVar9 + 0x20) - uVar3;
        *(ulong *)(lVar8 + 0x20) = lVar1 + uVar7;
        *(long *)(lVar8 + 0x28) = lVar2;
        if (lVar2 == 0) {
          *(undefined8 *)(lVar8 + 0x20) = *(undefined8 *)(lVar8 + 0x10);
        }
      }
      if (*(int *)(*param_1 + 0x20) == 0) {
        return 0;
      }
    }
  }
  if (param_2 == 0) {
    return 0;
  }
  *(undefined4 *)((long)param_1 + 0x172c) = 0;
  if (param_2 != 4) {
    if (*(int *)((long)param_1 + 0x170c) != 0) {
      uVar7 = param_1[0x13];
      if ((long)uVar7 < 0) {
        lVar8 = 0;
      }
      else {
        lVar8 = param_1[0xc] + (uVar7 & 0xffffffff);
      }
      FUN_ffff0000be05b044(param_1,lVar8,*(uint *)((long)param_1 + 0xac) - uVar7,0);
      lVar9 = *param_1;
      lVar8 = *(long *)(lVar9 + 0x38);
      param_1[0x13] = (ulong)*(uint *)((long)param_1 + 0xac);
      FUN_ffff0000be05ae50(lVar8);
      uVar3 = *(uint *)(lVar9 + 0x20);
      if (*(uint *)(lVar8 + 0x28) <= *(uint *)(lVar9 + 0x20)) {
        uVar3 = *(uint *)(lVar8 + 0x28);
      }
      uVar7 = (ulong)uVar3;
      if (uVar3 != 0) {
        FUN_ffff0000be0607b0(*(undefined8 *)(lVar9 + 0x18),*(undefined8 *)(lVar8 + 0x20),uVar7);
        lVar1 = *(long *)(lVar8 + 0x20);
        lVar2 = *(long *)(lVar8 + 0x28);
        *(ulong *)(lVar9 + 0x18) = *(long *)(lVar9 + 0x18) + uVar7;
        lVar2 = lVar2 - uVar7;
        *(ulong *)(lVar9 + 0x28) = *(long *)(lVar9 + 0x28) + uVar7;
        *(uint *)(lVar9 + 0x20) = *(int *)(lVar9 + 0x20) - uVar3;
        *(ulong *)(lVar8 + 0x20) = lVar1 + uVar7;
        *(long *)(lVar8 + 0x28) = lVar2;
        if (lVar2 == 0) {
          *(undefined8 *)(lVar8 + 0x20) = *(undefined8 *)(lVar8 + 0x10);
        }
      }
      if (*(int *)(*param_1 + 0x20) == 0) {
        return 0;
      }
    }
    return 1;
  }
  uVar7 = param_1[0x13];
  if ((long)uVar7 < 0) {
    lVar8 = 0;
  }
  else {
    lVar8 = param_1[0xc] + (uVar7 & 0xffffffff);
  }
  FUN_ffff0000be05b044(param_1,lVar8,*(uint *)((long)param_1 + 0xac) - uVar7,1);
  lVar9 = *param_1;
  lVar8 = *(long *)(lVar9 + 0x38);
  param_1[0x13] = (ulong)*(uint *)((long)param_1 + 0xac);
  FUN_ffff0000be05ae50(lVar8);
  uVar3 = *(uint *)(lVar9 + 0x20);
  if (*(uint *)(lVar8 + 0x28) <= *(uint *)(lVar9 + 0x20)) {
    uVar3 = *(uint *)(lVar8 + 0x28);
  }
  uVar7 = (ulong)uVar3;
  if (uVar3 != 0) {
    FUN_ffff0000be0607b0(*(undefined8 *)(lVar9 + 0x18),*(undefined8 *)(lVar8 + 0x20),uVar7);
    lVar1 = *(long *)(lVar8 + 0x20);
    lVar2 = *(long *)(lVar8 + 0x28);
    *(ulong *)(lVar9 + 0x18) = *(long *)(lVar9 + 0x18) + uVar7;
    lVar2 = lVar2 - uVar7;
    *(ulong *)(lVar9 + 0x28) = *(long *)(lVar9 + 0x28) + uVar7;
    *(uint *)(lVar9 + 0x20) = *(int *)(lVar9 + 0x20) - uVar3;
    *(ulong *)(lVar8 + 0x20) = lVar1 + uVar7;
    *(long *)(lVar8 + 0x28) = lVar2;
    if (lVar2 == 0) {
      *(undefined8 *)(lVar8 + 0x20) = *(undefined8 *)(lVar8 + 0x10);
    }
  }
  if (*(int *)(*param_1 + 0x20) != 0) {
    return 3;
  }
  return 2;
}



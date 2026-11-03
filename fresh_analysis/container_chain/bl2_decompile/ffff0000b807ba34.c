
undefined4 FUN_ffff0000b807ba34(long *param_1,uint param_2)

{
  long lVar1;
  long lVar2;
  int iVar3;
  uint uVar4;
  uint uVar5;
  bool bVar6;
  int iVar7;
  undefined4 uVar8;
  long *plVar9;
  long lVar10;
  uint uVar11;
  uint uVar12;
  ulong uVar13;
  long lVar14;
  ulong uVar15;
  long lVar16;
  
  uVar13 = param_1[3] - 5U;
  if ((ulong)*(uint *)(param_1 + 10) <= param_1[3] - 5U) {
    uVar13 = (ulong)*(uint *)(param_1 + 10);
  }
  iVar7 = *(int *)(*param_1 + 8);
  do {
    plVar9 = (long *)*param_1;
    uVar12 = *(int *)((long)param_1 + 0x1734) + 0x2a >> 3;
    uVar5 = *(uint *)(plVar9 + 4) - uVar12;
    if (*(uint *)(plVar9 + 4) < uVar12) {
      bVar6 = true;
      goto LAB_ffff0000b807bd0c;
    }
    iVar3 = *(int *)((long)param_1 + 0xac);
    lVar10 = param_1[0x13];
    uVar4 = iVar3 - (int)lVar10;
    uVar12 = uVar4 + *(uint *)(plVar9 + 1);
    uVar11 = uVar12;
    if (0xfffe < (ulong)uVar4 + (ulong)*(uint *)(plVar9 + 1)) {
      uVar11 = 0xffff;
    }
    if (uVar5 <= uVar11) {
      uVar11 = uVar5;
    }
    if (uVar11 < (uint)uVar13) {
      bVar6 = true;
      if (((param_2 == 0) || (param_2 != 4 && uVar11 == 0)) || (uVar11 != uVar12))
      goto LAB_ffff0000b807bd0c;
    }
    bVar6 = uVar11 == uVar12;
    FUN_ffff0000b80805ec(param_1,0,0,param_2 == 4 && bVar6);
    *(char *)(param_1[5] + param_1[2] + -4) = (char)uVar11;
    *(char *)(param_1[5] + param_1[2] + -3) = (char)(uVar11 >> 8);
    *(char *)(param_1[5] + param_1[2] + -2) = (char)~uVar11;
    *(char *)(param_1[5] + param_1[2] + -1) = (char)(~uVar11 >> 8);
    lVar16 = *param_1;
    lVar14 = *(long *)(lVar16 + 0x38);
    FUN_ffff0000b8080774(lVar14);
    uVar12 = *(uint *)(lVar16 + 0x20);
    if (*(uint *)(lVar14 + 0x28) <= *(uint *)(lVar16 + 0x20)) {
      uVar12 = *(uint *)(lVar14 + 0x28);
    }
    uVar15 = (ulong)uVar12;
    if (uVar12 != 0) {
      FUN_ffff0000b804cd40(*(undefined8 *)(lVar16 + 0x18),*(undefined8 *)(lVar14 + 0x20),uVar15);
      lVar1 = *(long *)(lVar14 + 0x20);
      lVar2 = *(long *)(lVar14 + 0x28);
      *(ulong *)(lVar16 + 0x18) = *(long *)(lVar16 + 0x18) + uVar15;
      lVar2 = lVar2 - uVar15;
      *(ulong *)(lVar16 + 0x28) = *(long *)(lVar16 + 0x28) + uVar15;
      *(uint *)(lVar16 + 0x20) = *(int *)(lVar16 + 0x20) - uVar12;
      *(ulong *)(lVar14 + 0x20) = lVar1 + uVar15;
      *(long *)(lVar14 + 0x28) = lVar2;
      if (lVar2 == 0) {
        *(undefined8 *)(lVar14 + 0x20) = *(undefined8 *)(lVar14 + 0x10);
      }
    }
    if (iVar3 != (int)lVar10) {
      if (uVar11 <= uVar4) {
        uVar4 = uVar11;
      }
      uVar15 = (ulong)uVar4;
      FUN_ffff0000b804cd40(*(undefined8 *)(*param_1 + 0x18),param_1[0xc] + param_1[0x13],uVar15);
      lVar10 = *param_1;
      uVar11 = uVar11 - uVar4;
      lVar14 = *(long *)(lVar10 + 0x18);
      iVar3 = *(int *)(lVar10 + 0x20);
      lVar16 = *(long *)(lVar10 + 0x28);
      param_1[0x13] = param_1[0x13] + uVar15;
      *(ulong *)(lVar10 + 0x18) = lVar14 + uVar15;
      *(uint *)(lVar10 + 0x20) = iVar3 - uVar4;
      *(ulong *)(lVar10 + 0x28) = lVar16 + uVar15;
    }
    if (uVar11 != 0) {
      plVar9 = (long *)*param_1;
      uVar5 = *(uint *)(plVar9 + 1);
      lVar10 = plVar9[3];
      uVar12 = uVar5;
      if (uVar11 <= uVar5) {
        uVar12 = uVar11;
      }
      uVar15 = (ulong)uVar12;
      if (uVar12 != 0) {
        *(uint *)(plVar9 + 1) = uVar5 - uVar12;
        FUN_ffff0000b804cd40(lVar10,*plVar9,uVar15);
        if (*(int *)(plVar9[7] + 0x30) == 2) {
          lVar10 = FUN_ffff0000b8046ff8(plVar9[0xc],lVar10,uVar15);
LAB_ffff0000b807ba94:
          plVar9[0xc] = lVar10;
        }
        else if (*(int *)(plVar9[7] + 0x30) == 1) {
          lVar10 = FUN_ffff0000b804683c(plVar9[0xc],lVar10,uVar15);
          goto LAB_ffff0000b807ba94;
        }
        *plVar9 = *plVar9 + uVar15;
        plVar9[2] = plVar9[2] + uVar15;
        plVar9 = (long *)*param_1;
        lVar10 = plVar9[3];
      }
      plVar9[3] = lVar10 + (ulong)uVar11;
      *(uint *)(plVar9 + 4) = (int)plVar9[4] - uVar11;
      plVar9[5] = plVar9[5] + (ulong)uVar11;
    }
  } while (param_2 != 4 || !bVar6);
  bVar6 = false;
  plVar9 = (long *)*param_1;
LAB_ffff0000b807bd0c:
  uVar12 = iVar7 - (int)plVar9[1];
  uVar13 = (ulong)uVar12;
  if (uVar12 == 0) {
    uVar13 = (ulong)*(uint *)((long)param_1 + 0xac);
  }
  else {
    uVar5 = *(uint *)(param_1 + 10);
    if (uVar12 < uVar5) {
      uVar15 = (ulong)*(uint *)((long)param_1 + 0xac);
      if (param_1[0xd] - uVar15 <= uVar13) {
        iVar7 = *(uint *)((long)param_1 + 0xac) - uVar5;
        *(int *)((long)param_1 + 0xac) = iVar7;
        FUN_ffff0000b804cd40(param_1[0xc],param_1[0xc] + (ulong)uVar5,iVar7);
        if (*(uint *)(param_1 + 0x2e5) < 2) {
          *(uint *)(param_1 + 0x2e5) = *(uint *)(param_1 + 0x2e5) + 1;
        }
        uVar5 = *(uint *)((long)param_1 + 0xac);
        uVar15 = (ulong)uVar5;
        if (uVar5 < *(uint *)((long)param_1 + 0x172c)) {
          *(uint *)((long)param_1 + 0x172c) = uVar5;
        }
      }
      FUN_ffff0000b804cd40(param_1[0xc] + uVar15,*(long *)*param_1 - uVar13,uVar13);
      uVar11 = (int)param_1[10] - *(int *)((long)param_1 + 0x172c);
      uVar5 = *(int *)((long)param_1 + 0xac) + uVar12;
      if (uVar11 <= uVar12) {
        uVar12 = uVar11;
      }
      *(uint *)((long)param_1 + 0xac) = uVar5;
      *(uint *)((long)param_1 + 0x172c) = uVar12 + *(int *)((long)param_1 + 0x172c);
    }
    else {
      lVar10 = *plVar9;
      *(undefined4 *)(param_1 + 0x2e5) = 2;
      FUN_ffff0000b804cd40(param_1[0xc],lVar10 - (ulong)uVar5);
      uVar5 = *(uint *)(param_1 + 10);
      *(uint *)((long)param_1 + 0xac) = uVar5;
      *(uint *)((long)param_1 + 0x172c) = uVar5;
    }
    uVar13 = (ulong)uVar5;
    param_1[0x13] = uVar13;
  }
  if ((ulong)param_1[0x2e7] < uVar13) {
    param_1[0x2e7] = uVar13;
  }
  if (!bVar6) {
    return 3;
  }
  if ((((param_2 | 4) != 4) && (*(int *)(*param_1 + 8) == 0)) && (param_1[0x13] == uVar13)) {
    return 1;
  }
  uVar12 = (int)param_1[0xd] - (int)uVar13;
  if (uVar12 < *(uint *)(*param_1 + 8)) {
    uVar15 = (ulong)*(uint *)(param_1 + 10);
    if ((long)uVar15 <= param_1[0x13]) {
      param_1[0x13] = param_1[0x13] - uVar15;
      *(uint *)((long)param_1 + 0xac) = (int)uVar13 - *(uint *)(param_1 + 10);
      FUN_ffff0000b804cd40(param_1[0xc],param_1[0xc] + uVar15);
      if (*(uint *)(param_1 + 0x2e5) < 2) {
        *(uint *)(param_1 + 0x2e5) = *(uint *)(param_1 + 0x2e5) + 1;
      }
      uVar5 = *(uint *)((long)param_1 + 0xac);
      uVar13 = (ulong)uVar5;
      uVar12 = (int)param_1[10] + uVar12;
      if (uVar5 < *(uint *)((long)param_1 + 0x172c)) {
        *(uint *)((long)param_1 + 0x172c) = uVar5;
      }
    }
  }
  plVar9 = (long *)*param_1;
  uVar5 = *(uint *)(plVar9 + 1);
  if (uVar5 <= uVar12) {
    uVar12 = uVar5;
  }
  uVar15 = (ulong)uVar12;
  if (uVar12 == 0) goto LAB_ffff0000b807bf6c;
  lVar10 = param_1[0xc] + uVar13;
  *(uint *)(plVar9 + 1) = uVar5 - uVar12;
  FUN_ffff0000b804cd40(lVar10,*plVar9,uVar15);
  if (*(int *)(plVar9[7] + 0x30) == 2) {
    lVar10 = FUN_ffff0000b8046ff8(plVar9[0xc],lVar10,uVar12);
LAB_ffff0000b807bf28:
    plVar9[0xc] = lVar10;
  }
  else if (*(int *)(plVar9[7] + 0x30) == 1) {
    lVar10 = FUN_ffff0000b804683c(plVar9[0xc],lVar10,uVar12);
    goto LAB_ffff0000b807bf28;
  }
  iVar7 = *(int *)((long)param_1 + 0x172c);
  uVar11 = (int)param_1[10] - iVar7;
  uVar5 = uVar12;
  if (uVar11 <= uVar12) {
    uVar5 = uVar11;
  }
  uVar12 = *(int *)((long)param_1 + 0xac) + uVar12;
  uVar13 = (ulong)uVar12;
  *plVar9 = *plVar9 + uVar15;
  plVar9[2] = plVar9[2] + uVar15;
  *(uint *)((long)param_1 + 0xac) = uVar12;
  *(uint *)((long)param_1 + 0x172c) = uVar5 + iVar7;
LAB_ffff0000b807bf6c:
  if ((ulong)param_1[0x2e7] < uVar13) {
    param_1[0x2e7] = uVar13;
  }
  uVar15 = param_1[3] - (ulong)(uint)(*(int *)((long)param_1 + 0x1734) + 0x2a >> 3);
  if (0xfffe < uVar15) {
    uVar15 = 0xffff;
  }
  uVar11 = (uint)uVar15;
  iVar7 = (int)param_1[0x13];
  uVar5 = (int)uVar13 - iVar7;
  uVar12 = *(uint *)(param_1 + 10);
  if (uVar11 <= *(uint *)(param_1 + 10)) {
    uVar12 = uVar11;
  }
  if (uVar5 < uVar12) {
    if (param_2 == 0) {
      return 0;
    }
    if (param_2 != 4 && (int)uVar13 == iVar7) {
      return 0;
    }
    if (*(int *)(*param_1 + 8) != 0) {
      return 0;
    }
    if (uVar11 < uVar5) {
      return 0;
    }
  }
  uVar12 = uVar5;
  if (uVar11 <= uVar5) {
    uVar12 = uVar11;
  }
  if (param_2 == 4) {
    bVar6 = *(int *)(*param_1 + 8) == 0 && uVar5 <= uVar11;
  }
  else {
    bVar6 = false;
  }
  FUN_ffff0000b80805ec(param_1,param_1[0xc] + param_1[0x13],(ulong)uVar12,bVar6);
  lVar14 = *param_1;
  lVar10 = *(long *)(lVar14 + 0x38);
  param_1[0x13] = param_1[0x13] + (ulong)uVar12;
  FUN_ffff0000b8080774(lVar10);
  uVar12 = *(uint *)(lVar14 + 0x20);
  if (*(uint *)(lVar10 + 0x28) <= *(uint *)(lVar14 + 0x20)) {
    uVar12 = *(uint *)(lVar10 + 0x28);
  }
  uVar13 = (ulong)uVar12;
  if (uVar12 != 0) {
    FUN_ffff0000b804cd40(*(undefined8 *)(lVar14 + 0x18),*(undefined8 *)(lVar10 + 0x20),uVar13);
    lVar16 = *(long *)(lVar10 + 0x20);
    lVar1 = *(long *)(lVar10 + 0x28);
    *(ulong *)(lVar14 + 0x18) = *(long *)(lVar14 + 0x18) + uVar13;
    lVar1 = lVar1 - uVar13;
    *(ulong *)(lVar14 + 0x28) = *(long *)(lVar14 + 0x28) + uVar13;
    *(uint *)(lVar14 + 0x20) = *(int *)(lVar14 + 0x20) - uVar12;
    *(ulong *)(lVar10 + 0x20) = lVar16 + uVar13;
    *(long *)(lVar10 + 0x28) = lVar1;
    if (lVar1 == 0) {
      *(undefined8 *)(lVar10 + 0x20) = *(undefined8 *)(lVar10 + 0x10);
    }
  }
  uVar8 = 2;
  if (bVar6 == false) {
    uVar8 = 0;
  }
  return uVar8;
}



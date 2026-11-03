// File offset entry: 0x4a31c

long * FUN_ffff0000b804a31c
                 (long param_1,undefined8 param_2,ulong param_3,ulong param_4,uint param_5,
                 ulong param_6,undefined4 param_7,undefined4 param_8)

{
  long *plVar1;
  ulong uVar2;
  undefined4 uVar3;
  long lVar4;
  long lVar5;
  long lVar6;
  undefined8 *puVar7;
  long *plVar8;
  ulong uVar9;
  ulong uVar10;
  long lVar11;
  
  plVar1 = (long *)FUN_ffff0000b804bd60(1,0x58);
  if (plVar1 == (long *)0x0) {
    return (long *)0x0;
  }
  FUN_ffff0000b804d360(plVar1 + 2,param_2,0x20);
  plVar1[7] = param_4;
  plVar1[8] = param_3;
  *(undefined4 *)(plVar1 + 6) = param_7;
  *(undefined4 *)((long)plVar1 + 0x34) = param_8;
  plVar1[9] = (long)(plVar1 + 9);
  plVar1[10] = (long)(plVar1 + 9);
  if ((param_6 & 1) != 0) {
    if ((((param_3 != 0) && (*(ulong *)(param_1 + 0x38) <= param_4)) &&
        (uVar10 = (*(ulong *)(param_1 + 0x38) + *(long *)(param_1 + 0x40)) - 1, param_4 <= uVar10))
       && ((uVar9 = (param_3 + param_4) - 1, param_4 <= uVar9 && (uVar9 <= uVar10)))) {
      plVar8 = (long *)(param_1 + 0x50);
      lVar11 = *plVar8;
      param_1 = param_1 + 0x48;
      if ((lVar11 == param_1) ||
         ((lVar11 == 0 || (lVar5 = lVar11, uVar9 < *(ulong *)(lVar11 + 0x38))))) {
LAB_ffff0000b804a444:
        plVar1[1] = lVar11;
        puVar7 = (undefined8 *)*plVar8;
        *plVar1 = param_1;
        *plVar8 = (long)plVar1;
        *puVar7 = plVar1;
        return plVar1;
      }
      do {
        lVar11 = *(long *)(lVar5 + 8);
        if (((*(long *)(lVar5 + 0x38) + *(long *)(lVar5 + 0x40)) - 1U < param_4) &&
           (((lVar11 == param_1 || (lVar11 == 0)) || (uVar9 < *(ulong *)(lVar11 + 0x38))))) {
          plVar8 = (long *)(lVar5 + 8);
          param_1 = lVar5;
          goto LAB_ffff0000b804a444;
        }
        lVar5 = lVar11;
      } while (lVar11 != param_1);
    }
    goto LAB_ffff0000b804a5c4;
  }
  param_5 = param_5 & 0xff;
  lVar11 = *(long *)(param_1 + 0x50);
  if (param_5 < 0xd) {
    param_5 = 0xc;
  }
  lVar5 = param_1 + 0x48;
  uVar10 = *(ulong *)(param_1 + 0x38);
  lVar4 = 1L << ((ulong)param_5 & 0x3f);
  if ((lVar11 == lVar5) || (lVar11 == 0)) {
    if (*(long *)(param_1 + 0x40) == 0) goto LAB_ffff0000b804a5c4;
    uVar9 = *(long *)(param_1 + 0x40) + uVar10;
    uVar3 = 0x40;
LAB_ffff0000b804a46c:
    uVar2 = FUN_ffff0000b804a1dc(param_1 + 0x58,uVar10,0x40,uVar9 - 1,uVar3,lVar4,param_3,param_8);
    if (uVar2 < uVar10) goto LAB_ffff0000b804a5c4;
    if ((uVar2 < uVar9 - 1) && (param_3 <= uVar9 - uVar2)) {
      lVar11 = 0;
LAB_ffff0000b804a59c:
      plVar1[7] = uVar2;
      if (lVar11 != 0) {
        lVar5 = lVar11;
      }
      lVar11 = *(long *)(lVar5 + 8);
      *plVar1 = lVar5;
      plVar1[1] = lVar11;
      puVar7 = *(undefined8 **)(lVar5 + 8);
      *(long **)(lVar5 + 8) = plVar1;
      *puVar7 = plVar1;
      return plVar1;
    }
    lVar11 = *(long *)(param_1 + 0x50);
  }
  else {
    uVar9 = *(ulong *)(lVar11 + 0x38);
    if (uVar10 != uVar9) {
      uVar3 = *(undefined4 *)(lVar11 + 0x34);
      goto LAB_ffff0000b804a46c;
    }
  }
  if (lVar11 != lVar5) {
    do {
      lVar6 = *(long *)(lVar11 + 8);
      uVar10 = *(long *)(lVar11 + 0x40) + *(long *)(lVar11 + 0x38);
      if (lVar6 == lVar5 || lVar6 == 0) {
        uVar9 = *(long *)(param_1 + 0x40) + *(long *)(param_1 + 0x38);
        if (uVar10 == uVar9) break;
        uVar3 = 0x40;
LAB_ffff0000b804a55c:
        uVar2 = FUN_ffff0000b804a1dc
                          (param_1 + 0x58,uVar10,*(undefined4 *)(lVar11 + 0x34),uVar9 - 1,uVar3,
                           lVar4,param_3,param_8);
        if (uVar2 < uVar10) break;
        if ((uVar2 < uVar9 - 1) && (param_3 <= uVar9 - uVar2)) goto LAB_ffff0000b804a59c;
        lVar6 = *(long *)(lVar11 + 8);
      }
      else {
        uVar9 = *(ulong *)(lVar6 + 0x38);
        if (uVar10 != uVar9) {
          uVar3 = *(undefined4 *)(lVar6 + 0x34);
          goto LAB_ffff0000b804a55c;
        }
      }
      lVar11 = lVar6;
    } while (lVar6 != lVar5);
  }
LAB_ffff0000b804a5c4:
  thunk_FUN_ffff0000b8082558(plVar1);
  return (long *)0x0;
}



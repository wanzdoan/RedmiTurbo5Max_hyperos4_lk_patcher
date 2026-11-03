// ffff00009079e830  size=236  FUN_ffff00009079e830

undefined8 FUN_ffff00009079e830(long param_1,uint param_2)

{
  uint uVar1;
  undefined8 uVar2;
  long lVar3;
  undefined2 *puVar4;
  uint uVar5;
  ulong uVar6;
  long lVar7;
  ulong uVar8;
  ulong uVar9;
  
  if (param_2 != 0) {
    uVar5 = *(uint *)(param_1 + 8);
    uVar1 = uVar5 + param_2;
    if (uVar5 < uVar1) {
      if ((*(uint *)(param_1 + 4) < uVar1) &&
         (uVar2 = FUN_ffff00009079f2d8(param_1,uVar1), (int)uVar2 != 0)) {
        return uVar2;
      }
      *(uint *)(param_1 + 8) = uVar1;
      uVar5 = uVar1;
    }
    lVar3 = *(long *)(param_1 + 0x10);
    if (-1 < (int)(uVar5 + ~param_2)) {
      puVar4 = (undefined2 *)(lVar3 + (ulong)(uVar5 + ~param_2) * 2);
      do {
        uVar5 = uVar5 - 1;
        *(undefined2 *)(lVar3 + (ulong)uVar5 * 2) = *puVar4;
        puVar4 = puVar4 + -1;
      } while (0 < (int)(uVar5 - param_2));
    }
    if (param_2 < 2) {
      param_2 = 1;
    }
    uVar6 = (ulong)param_2;
    if (param_2 < 2) {
      uVar8 = 0;
    }
    else {
      uVar8 = uVar6 & 0xfffffffe;
      lVar7 = lVar3 + 2;
      uVar9 = uVar8;
      do {
        *(undefined4 *)(lVar7 + -2) = 0;
        lVar7 = lVar7 + 4;
        uVar9 = uVar9 - 2;
      } while (uVar9 != 0);
      if (uVar8 == uVar6) {
        return 0;
      }
    }
    lVar7 = uVar6 - uVar8;
    puVar4 = (undefined2 *)(lVar3 + uVar8 * 2);
    do {
      lVar7 = lVar7 + -1;
      *puVar4 = 0;
      puVar4 = puVar4 + 1;
    } while (lVar7 != 0);
  }
  return 0;
}



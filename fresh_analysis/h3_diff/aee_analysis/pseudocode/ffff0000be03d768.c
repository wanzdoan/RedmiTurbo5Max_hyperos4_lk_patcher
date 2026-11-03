
undefined8 FUN_ffff0000be03d768(int param_1)

{
  long *plVar1;
  ulong uVar2;
  int iVar3;
  uint uVar4;
  ulong uVar5;
  ulong uVar6;
  long lVar7;
  undefined4 *puVar8;
  ulong uVar9;
  long lVar10;
  long *plVar11;
  long lVar12;
  ulong uVar13;
  
  iVar3 = DAT_ffff0000be1223b0;
  if ((DAT_ffff0000be123550 & 1) == 0) {
    if (DAT_ffff0000be1223d8 == 2) {
      if (param_1 == 0x2b) {
        param_1 = 0x2d;
      }
      else if (param_1 == 0x2c) {
        return 0;
      }
    }
    FUN_ffff0000be038028(param_1);
    uVar4 = 0;
    if (param_1 != 0x2b) {
      uVar4 = (uint)(param_1 != 0x2d);
    }
    uVar13 = (ulong)uVar4;
    uVar2 = 1;
    if (param_1 != 0x2b) {
      uVar2 = 2;
    }
    uVar5 = uVar13;
    if (iVar3 == 0) {
      do {
        lVar7 = *(long *)(s_0123456789abcdef_ffff0000be0c8230 + uVar5 * 8 + 0x10);
        uVar5 = uVar5 + 1;
        *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) | 1;
        *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) & 0xfffffffe;
        uVar6 = uVar13;
      } while (uVar2 != uVar5);
      do {
        uVar5 = uVar6 + 1;
        *(uint *)(*(long *)(s_0123456789abcdef_ffff0000be0c8230 + uVar6 * 8 + 0x10) + 0x1d0) =
             *(uint *)(*(long *)(s_0123456789abcdef_ffff0000be0c8230 + uVar6 * 8 + 0x10) + 0x1d0) &
             0xfffffffe;
        uVar6 = uVar5;
      } while (uVar2 != uVar5);
      FUN_ffff0000be0377b0(param_1);
      uVar5 = uVar13;
      do {
        puVar8 = *(undefined4 **)(s_0123456789abcdef_ffff0000be0c8230 + uVar5 * 8 + 0x10);
        uVar5 = uVar5 + 1;
        *puVar8 = 0;
        puVar8[2] = 0;
        puVar8[3] = 0;
      } while (uVar2 != uVar5);
      FUN_ffff0000be03fd7c(param_1);
      lVar7 = uVar2 - uVar13;
      plVar1 = &DAT_ffff0000be0caeb0 + uVar13;
      do {
        plVar11 = plVar1;
        lVar12 = lVar7;
        if ((&DAT_ffff0000be122910)[uVar13 * 0x2f6] == 0) {
          do {
            lVar10 = *plVar11;
            lVar12 = lVar12 + -1;
            *(uint *)(lVar10 + 0x30) = *(uint *)(lVar10 + 0x30) & 0xfffffffe;
            *(uint *)(lVar10 + 0x28) = *(uint *)(lVar10 + 0x28) | 2;
            *(uint *)(lVar10 + 0x28) = *(uint *)(lVar10 + 0x28) & 0xfffffffe;
            *(uint *)(lVar10 + 0x25c) = *(uint *)(lVar10 + 0x25c) | 1;
            *(uint *)(lVar10 + 0x45c) = *(uint *)(lVar10 + 0x45c) | 1;
            *(uint *)(lVar10 + 0x15c) = *(uint *)(lVar10 + 0x15c) | 1;
            *(uint *)(lVar10 + 0x55c) = *(uint *)(lVar10 + 0x55c) | 1;
            *(uint *)(lVar10 + 0x35c) = *(uint *)(lVar10 + 0x35c) | 1;
            *(undefined4 *)(lVar10 + 4) = 0x3fff0180;
            *(undefined4 *)(lVar10 + 4) = 0x3fff0100;
            plVar11 = plVar11 + 1;
          } while (lVar12 != 0);
        }
        else {
          do {
            lVar10 = *plVar11;
            lVar12 = lVar12 + -1;
            *(uint *)(lVar10 + 0x30) = *(uint *)(lVar10 + 0x30) & 0xfffffffe;
            *(uint *)(lVar10 + 0x28) = *(uint *)(lVar10 + 0x28) | 2;
            *(uint *)(lVar10 + 0x28) = *(uint *)(lVar10 + 0x28) & 0xfffffffe;
            *(undefined4 *)(lVar10 + 4) = 0x3fff0080;
            *(undefined4 *)(lVar10 + 4) = 0x3fff0000;
            plVar11 = plVar11 + 1;
          } while (lVar12 != 0);
        }
        uVar13 = uVar13 + 1;
      } while (uVar13 != uVar2);
    }
    else {
      do {
        lVar7 = *(long *)(s_0123456789abcdef_ffff0000be0c8230 + uVar5 * 8 + 0x10);
        uVar5 = uVar5 + 1;
        *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) | 1;
        *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) & 0xfffffffe;
        uVar6 = uVar13;
      } while (uVar2 != uVar5);
      do {
        uVar5 = uVar6 + 1;
        *(uint *)(*(long *)(s_0123456789abcdef_ffff0000be0c8230 + uVar6 * 8 + 0x10) + 0x1d0) =
             *(uint *)(*(long *)(s_0123456789abcdef_ffff0000be0c8230 + uVar6 * 8 + 0x10) + 0x1d0) &
             0xfffffffe;
        uVar6 = uVar5;
        uVar9 = uVar13;
      } while (uVar2 != uVar5);
      do {
        uVar9 = uVar9 + 1;
      } while (uVar2 != uVar9);
      FUN_ffff0000be0377b0(param_1);
      uVar5 = uVar13;
      do {
        puVar8 = *(undefined4 **)(s_0123456789abcdef_ffff0000be0c8230 + uVar5 * 8 + 0x10);
        uVar5 = uVar5 + 1;
        *puVar8 = 0;
        puVar8[2] = 0;
        puVar8[3] = 0;
      } while (uVar2 != uVar5);
      FUN_ffff0000be03fd7c(param_1);
      lVar7 = uVar2 - uVar13;
      plVar1 = &DAT_ffff0000be0caeb0 + uVar13;
      do {
        plVar11 = plVar1;
        lVar12 = lVar7;
        if ((&DAT_ffff0000be122910)[uVar13 * 0x2f6] == 0) {
          do {
            lVar10 = *plVar11;
            lVar12 = lVar12 + -1;
            *(uint *)(lVar10 + 0x30) = *(uint *)(lVar10 + 0x30) & 0xfffffffe;
            *(uint *)(lVar10 + 0x28) = *(uint *)(lVar10 + 0x28) | 2;
            *(uint *)(lVar10 + 0x28) = *(uint *)(lVar10 + 0x28) & 0xfffffffe;
            *(uint *)(lVar10 + 0x25c) = *(uint *)(lVar10 + 0x25c) | 1;
            *(uint *)(lVar10 + 0x45c) = *(uint *)(lVar10 + 0x45c) | 1;
            *(uint *)(lVar10 + 0x15c) = *(uint *)(lVar10 + 0x15c) | 1;
            *(uint *)(lVar10 + 0x55c) = *(uint *)(lVar10 + 0x55c) | 1;
            *(uint *)(lVar10 + 0x35c) = *(uint *)(lVar10 + 0x35c) | 1;
            *(undefined4 *)(lVar10 + 4) = 0x3fff0180;
            *(undefined4 *)(lVar10 + 4) = 0x3fff0100;
            plVar11 = plVar11 + 1;
          } while (lVar12 != 0);
        }
        else {
          do {
            lVar10 = *plVar11;
            lVar12 = lVar12 + -1;
            *(uint *)(lVar10 + 0x30) = *(uint *)(lVar10 + 0x30) & 0xfffffffe;
            *(uint *)(lVar10 + 0x28) = *(uint *)(lVar10 + 0x28) | 2;
            *(uint *)(lVar10 + 0x28) = *(uint *)(lVar10 + 0x28) & 0xfffffffe;
            *(undefined4 *)(lVar10 + 4) = 0x3fff0080;
            *(undefined4 *)(lVar10 + 4) = 0x3fff0000;
            plVar11 = plVar11 + 1;
          } while (lVar12 != 0);
        }
        uVar13 = uVar13 + 1;
      } while (uVar13 != uVar2);
    }
    DAT_ffff0000be123550 = 1;
  }
  return 0;
}



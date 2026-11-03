// ffff000090752534  size=1024  FUN_ffff000090752534

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090752534(int param_1)

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
  
  iVar3 = _DAT_ffff000090878270;
  if ((DAT_ffff000090879410 & 1) == 0) {
    if (_DAT_ffff000090878298 == 2) {
      if (param_1 == 0x2b) {
        param_1 = 0x2d;
      }
      else if (param_1 == 0x2c) {
        return 0;
      }
    }
    FUN_ffff00009074cdf4(param_1);
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
        lVar7 = *(long *)(s_0123456789abcdef_ffff000090804c70 + uVar5 * 8 + 0x10);
        uVar5 = uVar5 + 1;
        *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) | 1;
        *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) & 0xfffffffe;
        uVar6 = uVar13;
      } while (uVar2 != uVar5);
      do {
        uVar5 = uVar6 + 1;
        *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar6 * 8 + 0x10) + 0x1d0) =
             *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar6 * 8 + 0x10) + 0x1d0) &
             0xfffffffe;
        uVar6 = uVar5;
      } while (uVar2 != uVar5);
      FUN_ffff00009074c57c(param_1);
      uVar5 = uVar13;
      do {
        puVar8 = *(undefined4 **)(s_0123456789abcdef_ffff000090804c70 + uVar5 * 8 + 0x10);
        uVar5 = uVar5 + 1;
        *puVar8 = 0;
        puVar8[2] = 0;
        puVar8[3] = 0;
      } while (uVar2 != uVar5);
      FUN_ffff000090754b48(param_1);
      lVar7 = uVar2 - uVar13;
      plVar1 = &DAT_ffff00009080cf78 + uVar13;
      do {
        plVar11 = plVar1;
        lVar12 = lVar7;
        if (*(int *)(&DAT_ffff0000908787d0 + uVar13 * 0xbd8) == 0) {
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
        lVar7 = *(long *)(s_0123456789abcdef_ffff000090804c70 + uVar5 * 8 + 0x10);
        uVar5 = uVar5 + 1;
        *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) | 1;
        *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) & 0xfffffffe;
        uVar6 = uVar13;
      } while (uVar2 != uVar5);
      do {
        uVar5 = uVar6 + 1;
        *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar6 * 8 + 0x10) + 0x1d0) =
             *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar6 * 8 + 0x10) + 0x1d0) &
             0xfffffffe;
        uVar6 = uVar5;
        uVar9 = uVar13;
      } while (uVar2 != uVar5);
      do {
        uVar9 = uVar9 + 1;
      } while (uVar2 != uVar9);
      FUN_ffff00009074c57c(param_1);
      uVar5 = uVar13;
      do {
        puVar8 = *(undefined4 **)(s_0123456789abcdef_ffff000090804c70 + uVar5 * 8 + 0x10);
        uVar5 = uVar5 + 1;
        *puVar8 = 0;
        puVar8[2] = 0;
        puVar8[3] = 0;
      } while (uVar2 != uVar5);
      FUN_ffff000090754b48(param_1);
      lVar7 = uVar2 - uVar13;
      plVar1 = &DAT_ffff00009080cf78 + uVar13;
      do {
        plVar11 = plVar1;
        lVar12 = lVar7;
        if (*(int *)(&DAT_ffff0000908787d0 + uVar13 * 0xbd8) == 0) {
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
    DAT_ffff000090879410 = 1;
  }
  return 0;
}



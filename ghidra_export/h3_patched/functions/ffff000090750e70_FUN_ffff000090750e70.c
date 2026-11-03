// ffff000090750e70  size=560  FUN_ffff000090750e70

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090750e70(int param_1)

{
  ulong uVar1;
  long *plVar2;
  ulong uVar3;
  uint uVar4;
  ulong uVar5;
  long lVar6;
  long lVar7;
  long *plVar8;
  long lVar9;
  ulong uVar10;
  
  if (_DAT_ffff000090878200 == 2) {
    if (param_1 == 0x2b) {
      param_1 = 0x2d;
    }
    else if (param_1 == 0x2c) {
      return 0;
    }
  }
  uVar4 = 0;
  if (param_1 != 0x2b) {
    uVar4 = (uint)(param_1 != 0x2d);
  }
  uVar10 = (ulong)uVar4;
  uVar3 = 1;
  uVar5 = uVar10;
  if (param_1 != 0x2b) {
    uVar3 = 2;
  }
  do {
    uVar1 = uVar5 + 1;
    *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + uVar5 * 8 + 0x10) + 0x34) =
         *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + uVar5 * 8 + 0x10) + 0x34) &
         0xfffffffc;
    uVar5 = uVar1;
  } while (uVar3 != uVar1);
  FUN_ffff00009077de54
            (s___s__set_DISP_REG_DSI_MODE_CON___ffff0000907db0d8,s_DSI_SetMode_ffff0000907e96fc,0);
  uVar5 = uVar10;
  do {
    lVar6 = uVar5 * 8;
    uVar5 = uVar5 + 1;
    *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + lVar6 + 0x10) + 0x1d0) =
         *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + lVar6 + 0x10) + 0x1d0) &
         0xfffffffe;
  } while (uVar3 != uVar5);
  FUN_ffff00009074c2e4(param_1);
  FUN_ffff0000907548b0(param_1);
  lVar6 = uVar3 - uVar10;
  plVar2 = &DAT_ffff00009080cd50 + uVar10;
  do {
    plVar8 = plVar2;
    lVar9 = lVar6;
    if (*(int *)(&DAT_ffff000090878738 + uVar10 * 0xbd8) == 0) {
      do {
        lVar7 = *plVar8;
        lVar9 = lVar9 + -1;
        *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) & 0xfffffffe;
        *(uint *)(lVar7 + 0x28) = *(uint *)(lVar7 + 0x28) | 2;
        *(uint *)(lVar7 + 0x28) = *(uint *)(lVar7 + 0x28) & 0xfffffffe;
        *(uint *)(lVar7 + 0x25c) = *(uint *)(lVar7 + 0x25c) | 1;
        *(uint *)(lVar7 + 0x45c) = *(uint *)(lVar7 + 0x45c) | 1;
        *(uint *)(lVar7 + 0x15c) = *(uint *)(lVar7 + 0x15c) | 1;
        *(uint *)(lVar7 + 0x55c) = *(uint *)(lVar7 + 0x55c) | 1;
        *(uint *)(lVar7 + 0x35c) = *(uint *)(lVar7 + 0x35c) | 1;
        *(undefined4 *)(lVar7 + 4) = 0x3fff0180;
        *(undefined4 *)(lVar7 + 4) = 0x3fff0100;
        plVar8 = plVar8 + 1;
      } while (lVar9 != 0);
    }
    else {
      do {
        lVar7 = *plVar8;
        lVar9 = lVar9 + -1;
        *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) & 0xfffffffe;
        *(uint *)(lVar7 + 0x28) = *(uint *)(lVar7 + 0x28) | 2;
        *(uint *)(lVar7 + 0x28) = *(uint *)(lVar7 + 0x28) & 0xfffffffe;
        *(undefined4 *)(lVar7 + 4) = 0x3fff0080;
        *(undefined4 *)(lVar7 + 4) = 0x3fff0000;
        plVar8 = plVar8 + 1;
      } while (lVar9 != 0);
    }
    uVar10 = uVar10 + 1;
  } while (uVar10 != uVar3);
  uVar4 = 0;
  if (param_1 != 0x2b) {
    uVar4 = (uint)(param_1 != 0x2d);
  }
  lVar6 = 1;
  if (param_1 != 0x2b) {
    lVar6 = 2;
  }
  lVar9 = (ulong)uVar4 * 0xbd8 + -0xffff6f788448;
  lVar6 = lVar6 - (ulong)uVar4;
  do {
    FUN_ffff00009077eb88(lVar9,0,0xbd8);
    lVar9 = lVar9 + 0xbd8;
    lVar6 = lVar6 + -1;
  } while (lVar6 != 0);
  return 0;
}




undefined8 FUN_ffff000090751108(int param_1)

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
  undefined *puVar10;
  ulong uVar11;
  
  if (DAT_ffff000090878298 == 2) {
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
  uVar11 = (ulong)uVar4;
  uVar3 = 1;
  uVar5 = uVar11;
  if (param_1 != 0x2b) {
    uVar3 = 2;
  }
  do {
    uVar1 = uVar5 + 1;
    *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar5 * 8 + 0x10) + 0x34) =
         *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar5 * 8 + 0x10) + 0x34) &
         0xfffffffc;
    uVar5 = uVar1;
  } while (uVar3 != uVar1);
  FUN_ffff00009077e11c
            (s___s__set_DISP_REG_DSI_MODE_CON___ffff0000907db1fa,s_DSI_SetMode_ffff0000907e9857,0);
  uVar5 = uVar11;
  do {
    lVar6 = uVar5 * 8;
    uVar5 = uVar5 + 1;
    *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + lVar6 + 0x10) + 0x1d0) =
         *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + lVar6 + 0x10) + 0x1d0) &
         0xfffffffe;
  } while (uVar3 != uVar5);
  FUN_ffff00009074c57c(param_1);
  FUN_ffff000090754b48(param_1);
  lVar6 = uVar3 - uVar11;
  plVar2 = &DAT_ffff00009080cf78 + uVar11;
  do {
    plVar8 = plVar2;
    lVar9 = lVar6;
    if ((&DAT_ffff0000908787d0)[uVar11 * 0x2f6] == 0) {
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
    uVar11 = uVar11 + 1;
  } while (uVar11 != uVar3);
  uVar4 = 0;
  if (param_1 != 0x2b) {
    uVar4 = (uint)(param_1 != 0x2d);
  }
  lVar6 = 1;
  if (param_1 != 0x2b) {
    lVar6 = 2;
  }
  puVar10 = &DAT_ffff000090877c50 + (ulong)uVar4 * 0xbd8;
  lVar6 = lVar6 - (ulong)uVar4;
  do {
    FUN_ffff00009077ee50(puVar10,0,0xbd8);
    puVar10 = puVar10 + 0xbd8;
    lVar6 = lVar6 + -1;
  } while (lVar6 != 0);
  return 0;
}



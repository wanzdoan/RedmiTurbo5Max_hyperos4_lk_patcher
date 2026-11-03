// ffff000090751338  size=376  FUN_ffff000090751338

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090751338(int param_1)

{
  long lVar1;
  uint uVar2;
  long *plVar3;
  undefined8 *puVar4;
  int iVar5;
  long lVar6;
  ulong uVar7;
  
  if (_DAT_ffff000090878298 == 2) {
    if (param_1 == 0x2b) {
      param_1 = 0x2d;
    }
    else if (param_1 == 0x2c) {
      return 0;
    }
  }
  uVar2 = 0;
  if (param_1 != 0x2b) {
    uVar2 = (uint)(param_1 != 0x2d);
  }
  uVar7 = (ulong)uVar2;
  lVar1 = 1;
  if (param_1 != 0x2b) {
    lVar1 = 2;
  }
  plVar3 = (long *)(s_0123456789abcdef_ffff000090804c70 + uVar7 * 8 + 0x10);
  lVar6 = lVar1 - uVar7;
  do {
    lVar6 = lVar6 + -1;
    *(uint *)(*plVar3 + 0x34) = *(uint *)(*plVar3 + 0x34) & 0xfffffffc;
    plVar3 = plVar3 + 1;
  } while (lVar6 != 0);
  FUN_ffff00009077e11c
            (s___s__set_DISP_REG_DSI_MODE_CON___ffff0000907db1fa,s_DSI_SetMode_ffff0000907e9857,0);
  uVar2 = 0;
  if (param_1 != 0x2b) {
    uVar2 = (uint)(param_1 != 0x2d);
  }
  lVar6 = 1;
  if (param_1 != 0x2b) {
    lVar6 = 2;
  }
  lVar6 = lVar6 - (ulong)uVar2;
  puVar4 = &DAT_ffff00009080cf90 + uVar2;
  do {
    lVar6 = lVar6 + -1;
    *(undefined4 *)*puVar4 = 0;
    puVar4 = puVar4 + 1;
  } while (lVar6 != 0);
  if (((*(byte *)((&DAT_ffff00009080cf90)[uVar7] + 0x34) & 3) == 0) &&
     (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar7 * 8 + 0x10) + 0xc) < 0)) {
    iVar5 = 0;
    do {
      iVar5 = iVar5 + 1;
      lVar6 = lVar1 - uVar7;
      if (((uint)(iVar5 * 0x26e978d5) >> 3 | iVar5 * -0x60000000) < 0x418938) {
        do {
          lVar6 = lVar6 + -1;
        } while (lVar6 != 0);
      }
    } while (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar7 * 8 + 0x10) + 0xc) < 0);
  }
  return 0;
}



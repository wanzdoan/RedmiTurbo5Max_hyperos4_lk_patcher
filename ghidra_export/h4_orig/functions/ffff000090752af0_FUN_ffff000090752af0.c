// ffff000090752af0  size=1060  FUN_ffff000090752af0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090752af0(int param_1)

{
  ulong uVar1;
  bool bVar2;
  long *plVar3;
  uint uVar4;
  int *piVar5;
  undefined4 uVar6;
  long lVar7;
  long lVar8;
  ulong uVar9;
  ulong uVar10;
  int iVar11;
  uint *puVar12;
  
  if (_DAT_ffff000090878298 == 2) {
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
  uVar9 = (ulong)uVar4;
  uVar1 = 1;
  uVar10 = uVar9;
  if (param_1 != 0x2b) {
    uVar1 = 2;
  }
  do {
    lVar7 = *(long *)(s_0123456789abcdef_ffff000090804c70 + uVar10 * 8 + 0x10);
    uVar10 = uVar10 + 1;
    *(uint *)(lVar7 + 0xd0) = *(uint *)(lVar7 + 0xd0) | 2;
    *(uint *)(lVar7 + 0xd0) = *(uint *)(lVar7 + 0xd0) | 4;
  } while (uVar1 != uVar10);
  if (param_1 == 0x2d) {
    _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffffffe;
    _DAT_ffff0000324a0000 = _DAT_ffff0000324a0000 & 0xfffffffe;
    if (_DAT_ffff000090878270 != 0) {
      _DAT_ffff0000324a0030 = _DAT_ffff0000324a0030 | 0x10;
    }
    _DAT_ffff000032490008 = 0;
    _DAT_ffff0000324a0008 = 0;
    FUN_ffff00009077e11c
              (s___s__DISP_MODULE_DSIDUAL_DSI_Set_ffff0000907de4f2,s_ddp_dsi_start_ffff0000907ee65f)
    ;
    _DAT_ffff000032490034 = _DAT_ffff000032490034 & 0xfffffffc | _DAT_ffff000090878270 & 3;
    _DAT_ffff0000324a0034 = _DAT_ffff0000324a0034 & 0xfffffffc | _DAT_ffff000090878270 & 3;
    FUN_ffff00009077e11c
              (s___s__set_DISP_REG_DSI_MODE_CON___ffff0000907db1fa,s_DSI_SetMode_ffff0000907e9857);
    _DAT_ffff0000324a0610 = 0x55;
    _DAT_ffff000032490610 = _DAT_ffff0000324a0610;
    if (_DAT_ffff0000908787d0 != 0) {
      _DAT_ffff000032490610 = 0xaa;
    }
    _DAT_ffff0000324901d0 = _DAT_ffff0000324901d0 | 1;
    if (_DAT_ffff0000908793a8 != 0) {
      _DAT_ffff0000324a0610 = 0xaa;
    }
    _DAT_ffff0000324a01d0 = _DAT_ffff0000324a01d0 | 1;
  }
  else if (param_1 - 0x2bU < 2) {
    uVar10 = (ulong)(param_1 != 0x2b);
    *(undefined4 *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar10 * 8 + 0x10) + 8) = 0;
    lVar7 = 1;
    if (param_1 != 0x2b) {
      lVar7 = 2;
    }
    FUN_ffff00009077e11c(s___s__DSI_SetMode_ffff0000907ff5c5,s_ddp_dsi_start_ffff0000907ee65f);
    lVar7 = lVar7 - uVar10;
    uVar4 = *(uint *)(&DAT_ffff000090878270 + uVar10 * 0xbd8);
    lVar8 = lVar7;
    plVar3 = (long *)(s_0123456789abcdef_ffff000090804c70 + uVar10 * 8 + 0x10);
    do {
      lVar8 = lVar8 + -1;
      *(uint *)(*plVar3 + 0x34) = *(uint *)(*plVar3 + 0x34) & 0xfffffffc | uVar4 & 3;
      plVar3 = plVar3 + 1;
    } while (lVar8 != 0);
    FUN_ffff00009077e11c
              (s___s__set_DISP_REG_DSI_MODE_CON___ffff0000907db1fa,s_DSI_SetMode_ffff0000907e9857);
    piVar5 = (int *)(&DAT_ffff0000908787d0 + uVar10 * 0xbd8);
    plVar3 = (long *)(s_0123456789abcdef_ffff000090804c70 + uVar10 * 8 + 0x10);
    do {
      iVar11 = *piVar5;
      piVar5 = piVar5 + 0x2f6;
      lVar8 = *plVar3;
      uVar6 = 0x55;
      if (iVar11 != 0) {
        uVar6 = 0xaa;
      }
      lVar7 = lVar7 + -1;
      *(undefined4 *)(lVar8 + 0x610) = uVar6;
      *(uint *)(lVar8 + 0x1d0) = *(uint *)(lVar8 + 0x1d0) | 1;
      plVar3 = plVar3 + 1;
    } while (lVar7 != 0);
  }
  uVar4 = 0;
  if (param_1 != 0x2b) {
    uVar4 = (uint)(param_1 != 0x2d);
  }
  uVar10 = (ulong)uVar4;
  lVar7 = 1;
  if (param_1 != 0x2b) {
    lVar7 = 2;
  }
  do {
    uVar4 = *(uint *)(&DAT_ffff000090878334 + uVar9 * 0xbd8);
    if (uVar4 == 0) {
      uVar4 = *(int *)(&DAT_ffff000090878330 + uVar9 * 0xbd8) << 1;
    }
    if (0x5dc < uVar4) {
      puVar12 = *(uint **)(s_0123456789abcdef_ffff000090804c70 + uVar10 * 8 + 0x10);
      uVar4 = puVar12[0x76];
      puVar12[0x76] = 0xaaffff;
      puVar12[0x80] = puVar12[0x80] & 0xffff | 0x60000;
      *puVar12 = 0;
      if (((*(byte *)((&DAT_ffff00009080cf90)[uVar10] + 0x34) & 3) == 0) && ((int)puVar12[3] < 0)) {
        iVar11 = 0;
        do {
          iVar11 = iVar11 + 1;
          lVar8 = lVar7 - uVar10;
          if (((uint)(iVar11 * 0x26e978d5) >> 3 | iVar11 * -0x60000000) < 0x418938) {
            do {
              lVar8 = lVar8 + -1;
            } while (lVar8 != 0);
          }
        } while ((int)puVar12[3] < 0);
      }
      iVar11 = -5000;
      puVar12[0x76] = puVar12[0x76] | 0x1000000;
      puVar12[0x182] = puVar12[0x182] & 0xffff00ff | 0x200;
      puVar12[3] = puVar12[3] & 0xfffff7ff;
      *puVar12 = *puVar12 & 0xffffffef;
      *puVar12 = *puVar12 | 0x10;
      do {
        if ((puVar12[3] >> 0xb & 1) != 0) break;
        FUN_ffff00009077cea0(10);
        bVar2 = iVar11 != -1;
        iVar11 = iVar11 + 1;
      } while (bVar2);
      puVar12[0x76] = uVar4;
      puVar12[0x182] = puVar12[0x182] & 0xffff00ff | 0x100;
    }
    uVar9 = uVar9 + 1;
    if (uVar9 == uVar1) {
      return 0;
    }
  } while( true );
}



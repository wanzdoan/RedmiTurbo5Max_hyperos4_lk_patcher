
void FUN_ffff00009074e9a8(int param_1,undefined8 param_2,long param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  long *plVar7;
  long *plVar8;
  uint *puVar9;
  long lVar10;
  ulong uVar11;
  long lVar12;
  int iVar13;
  uint uVar14;
  uint uVar15;
  uint uVar16;
  
  iVar13 = *(int *)(param_3 + 0xc4);
  if (iVar13 == 0) {
    if (*(int *)(param_3 + 0xc0) == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_mt6991_disp_dd_ffff0000907f3041,0xc72,&DAT_ffff0000907d2506);
    }
    iVar13 = *(int *)(param_3 + 0xc0) << 1;
    *(int *)(param_3 + 0xc4) = iVar13;
  }
  uVar14 = (uint)(iVar13 * 0x50) / 7000;
  uVar1 = uVar14 + 2;
  uVar15 = (uint)(iVar13 * 0x32) / 7000;
  if ((uVar14 + 1 & 1) == 0) {
    uVar1 = (uint)(iVar13 * 0x50) / 7000 + 1;
  }
  uVar14 = uVar15 + 2;
  if ((uVar15 + 1 & 1) == 0) {
    uVar14 = (uint)(iVar13 * 0x32) / 7000 + 1;
  }
  uVar15 = uVar1;
  if (*(byte *)(param_3 + 0x93) != 0) {
    uVar15 = (uint)*(byte *)(param_3 + 0x93);
  }
  if (*(byte *)(param_3 + 0x92) != 0) {
    uVar14 = (uint)*(byte *)(param_3 + 0x92);
  }
  uVar2 = 0x30;
  if (*(byte *)(param_3 + 0x91) != 0) {
    uVar2 = (uint)*(byte *)(param_3 + 0x91);
  }
  uVar3 = 0x20;
  if (*(byte *)(param_3 + 0x90) != 0) {
    uVar3 = (uint)*(byte *)(param_3 + 0x90);
  }
  uVar4 = ((uint)(iVar13 * 0x76) / 7000 & 1 ^ 3) + (uint)(iVar13 * 0x76) / 7000;
  if (*(byte *)(param_3 + 0x9e) != 0) {
    uVar4 = (uint)*(byte *)(param_3 + 0x9e);
  }
  uVar5 = uVar1 * 4;
  if (*(byte *)(param_3 + 0x97) != 0) {
    uVar5 = (uint)*(byte *)(param_3 + 0x97);
  }
  uVar6 = uVar1 * 5;
  if (*(byte *)(param_3 + 0x95) != 0) {
    uVar6 = (uint)*(byte *)(param_3 + 0x95);
  }
  uVar1 = uVar1 * 3 >> 1;
  if (*(byte *)(param_3 + 0x96) != 0) {
    uVar1 = (uint)*(byte *)(param_3 + 0x96);
  }
  uVar16 = 0;
  if (param_1 != 0x2b) {
    uVar16 = (uint)(param_1 != 0x2d);
  }
  uVar11 = (ulong)uVar16;
  lVar10 = 1;
  if (param_1 != 0x2b) {
    lVar10 = 2;
  }
  lVar10 = lVar10 - uVar11;
  puVar9 = (uint *)(&DAT_ffff000090878824 + uVar11 * 0xbd8);
  plVar7 = &DAT_ffff00009080cf90 + uVar11;
  plVar8 = (long *)(s_0123456789abcdef_ffff000090804c70 + uVar11 * 8 + 0x10);
  do {
    lVar12 = *plVar8;
    puVar9[-1] = uVar2 + uVar4 + uVar14 + uVar15 + 5;
    *puVar9 = uVar3;
    puVar9 = puVar9 + 0x2f6;
    lVar10 = lVar10 + -1;
    *(uint *)(lVar12 + 0x600) = *(uint *)(lVar12 + 0x600) & 0xffffff00 | uVar15 & 0xff;
    *(uint *)(lVar12 + 0x600) = *(uint *)(lVar12 + 0x600) & 0xffff00ff | (uVar14 & 0xff) << 8;
    *(uint *)(lVar12 + 0x600) = *(uint *)(lVar12 + 0x600) & 0xff00ffff | uVar2 << 0x10;
    *(uint *)(lVar12 + 0x600) = *(uint *)(lVar12 + 0x600) & 0xffffff | uVar3 << 0x18;
    *(uint *)(lVar12 + 0x604) = *(uint *)(lVar12 + 0x604) & 0xffffff00 | uVar5 & 0xff;
    *(uint *)(lVar12 + 0x604) = *(uint *)(lVar12 + 0x604) & 0xffff00ff | (uVar1 & 0xff) << 8;
    *(uint *)(lVar12 + 0x604) = *(uint *)(lVar12 + 0x604) & 0xff00ffff | (uVar6 & 0xff) << 0x10;
    *(uint *)(lVar12 + 0x604) = *(uint *)(lVar12 + 0x604) & 0xffffff | uVar4 << 0x18;
    *(undefined4 *)(*plVar7 + 0x1e0) = 0x12c0003;
    plVar7 = plVar7 + 1;
    plVar8 = plVar8 + 1;
  } while (lVar10 != 0);
  return;
}



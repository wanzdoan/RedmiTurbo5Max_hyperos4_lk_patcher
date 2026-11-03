// ffff00009074e950  size=4  FUN_ffff00009074e950

void FUN_ffff00009074e950(int param_1,undefined8 param_2,long param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  uint uVar9;
  uint uVar10;
  uint uVar11;
  uint uVar12;
  uint uVar13;
  int iVar14;
  long *plVar15;
  long lVar16;
  long lVar17;
  uint uVar18;
  
  uVar12 = *(uint *)(param_3 + 0xc4);
  if (uVar12 == 0) {
    if (*(int *)(param_3 + 0xc0) == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_mt6991_disp_dd_ffff0000907f2eb5,0xccd,&DAT_ffff0000907d24c9);
    }
    uVar12 = *(int *)(param_3 + 0xc0) << 1;
    *(uint *)(param_3 + 0xc4) = uVar12;
  }
  uVar5 = uVar12 * 0x3b + 4000;
  uVar9 = (uVar12 * 0x50) / 8000;
  uVar1 = uVar9 + 2;
  uVar13 = uVar5 / 8000;
  if ((uVar9 + 1 & 1) == 0) {
    uVar1 = (uVar12 * 0x50) / 8000 + 1;
  }
  uVar9 = uVar13 + 2;
  if ((uVar13 + 1 & 1) == 0) {
    uVar9 = uVar5 / 8000 + 1;
  }
  iVar14 = 0xad0c;
  if (0x2e3 < uVar12) {
    iVar14 = 42000;
  }
  uVar13 = (uVar12 * 0x76) / 8000;
  uVar18 = (uVar12 * 0x39) / 8000;
  uVar5 = uVar18 + 2;
  if ((uVar18 + 1 & 1) == 0) {
    uVar5 = (uVar12 * 0x39) / 8000 + 1;
  }
  uVar18 = uVar1;
  if (*(byte *)(param_3 + 0x93) != 0) {
    uVar18 = (uint)*(byte *)(param_3 + 0x93);
  }
  uVar2 = uVar9;
  if (*(byte *)(param_3 + 0x92) != 0) {
    uVar2 = (uint)*(byte *)(param_3 + 0x92);
  }
  uVar7 = (uint)*(byte *)(param_3 + 0x91);
  if (uVar7 == 0) {
    uVar7 = ((uVar12 * 0xa3 + 11000) / 8000 - uVar9) + 1;
  }
  uVar9 = (uint)*(byte *)(param_3 + 0x90);
  if (uVar9 == 0) {
    uVar9 = (iVar14 + uVar12 * 0x42) / 8000 + 1;
  }
  uVar3 = uVar1 * 5;
  if (*(byte *)(param_3 + 0x95) != 0) {
    uVar3 = (uint)*(byte *)(param_3 + 0x95);
  }
  uVar4 = uVar1 * 3 >> 1;
  if (*(byte *)(param_3 + 0x96) != 0) {
    uVar4 = (uint)*(byte *)(param_3 + 0x96);
  }
  uVar1 = uVar1 * 4;
  if (*(byte *)(param_3 + 0x97) != 0) {
    uVar1 = (uint)*(byte *)(param_3 + 0x97);
  }
  uVar13 = uVar13 + (uVar13 + 1 & 1) + 2;
  if (*(byte *)(param_3 + 0x9e) != 0) {
    uVar13 = (uint)*(byte *)(param_3 + 0x9e);
  }
  uVar8 = (uint)*(byte *)(param_3 + 0x99);
  if (uVar8 == 0) {
    uVar8 = ((uVar12 * 0x14a) / 8000 - uVar5) + 1;
  }
  uVar10 = (uint)*(byte *)(param_3 + 0x98);
  if (uVar10 == 0) {
    uVar10 = (uVar12 * 0x41 + 52000) / 8000 + 1;
  }
  if (*(byte *)(param_3 + 0x9c) != 0) {
    uVar5 = (uint)*(byte *)(param_3 + 0x9c);
  }
  uVar11 = (uint)*(byte *)(param_3 + 0x9f);
  if (uVar11 == 0) {
    uVar11 = (uVar12 * 0x76) / 8000 + 1;
  }
  uVar6 = (uint)*(byte *)(param_3 + 0x9d);
  if (uVar6 == 0) {
    uVar6 = (uVar12 * 0x41 + 53000) / 8000 + 1;
  }
  uVar12 = 0;
  if (param_1 != 0x2b) {
    uVar12 = (uint)(param_1 != 0x2d);
  }
  lVar16 = 1;
  if (param_1 != 0x2b) {
    lVar16 = 2;
  }
  lVar16 = lVar16 - (ulong)uVar12;
  plVar15 = (long *)(s_0123456789abcdef_ffff000090804a48 + (ulong)uVar12 * 8 + 0x10);
  do {
    lVar17 = *plVar15;
    lVar16 = lVar16 + -1;
    *(uint *)(lVar17 + 0x600) =
         uVar9 << 0x18 | uVar18 & 0xff | (uVar2 & 0xff) << 8 | (uVar7 & 0xff) << 0x10;
    *(uint *)(lVar17 + 0x604) =
         (uVar3 & 0xff) << 0x10 | uVar1 & 0xff | (uVar4 & 0xff) << 8 | uVar13 << 0x18;
    *(uint *)(lVar17 + 0x608) = uVar10 << 0x18 | (uVar8 & 0xff) << 0x10 | 0x100;
    *(uint *)(lVar17 + 0x60c) = (uVar11 & 0xff) << 0x10 | uVar5 & 0xff | (uVar6 & 0xff) << 8;
    plVar15 = plVar15 + 1;
  } while (lVar16 != 0);
  return;
}



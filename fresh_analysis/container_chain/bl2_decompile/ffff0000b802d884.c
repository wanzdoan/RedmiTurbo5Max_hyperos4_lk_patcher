
undefined8 FUN_ffff0000b802d884(int param_1,long param_2,undefined8 param_3,ulong param_4)

{
  uint *puVar1;
  int iVar2;
  uint uVar3;
  undefined4 uVar4;
  undefined4 uVar5;
  undefined4 uVar6;
  int *piVar7;
  int *piVar8;
  int *piVar9;
  uint *puVar10;
  uint uVar11;
  undefined4 uVar12;
  undefined4 uVar13;
  undefined4 uVar14;
  undefined4 uVar15;
  uint *puVar16;
  int iVar17;
  undefined4 uVar18;
  
  if (*(int *)(param_2 + 0xc) == 0) {
    return 0;
  }
  iVar17 = *(int *)(param_2 + 0x41c);
  puVar10 = (uint *)0xffff000032460000;
  if (param_1 != 0x43) {
    puVar10 = (uint *)&DAT_ffff000032450000;
  }
  iVar2 = *(int *)(param_2 + 0x420);
  puVar1 = (uint *)((ulong)puVar10 | 0x400);
  if ((param_4 & 1) == 0) {
    puVar1 = puVar10;
  }
  if (*(int *)(param_2 + 0x3d0) == 0) {
    *puVar1 = *puVar1 | 1;
    *puVar1 = *puVar1 | 0x20;
    *(int *)((ulong)puVar1 | 0x1c) = iVar17 + -1;
    *(int *)((ulong)puVar1 | 0x18) = iVar2;
    return 0;
  }
  uVar11 = 0x14081;
  if (*(int *)(param_2 + 0x3f8) != 2) {
    uVar11 = 0x10081;
  }
  puVar10 = *(uint **)(param_2 + 0x480);
  piVar7 = *(int **)(param_2 + 0x490);
  piVar8 = *(int **)(param_2 + 0x4a0);
  piVar9 = *(int **)(param_2 + 0x4b0);
  *puVar1 = uVar11;
  *(int *)((ulong)puVar1 | 0x30) = *(int *)(param_2 + 0x3fc) + 0x10000;
  *(int *)((ulong)puVar1 | 0x18) = iVar2 + -0x10000 + ((iVar2 + 2U) / 3) * 0x10000;
  uVar11 = *(uint *)(param_2 + 0x424);
  uVar3 = 0;
  if (uVar11 != 0) {
    uVar3 = ((iVar17 + uVar11) - 1) / uVar11;
  }
  *(uint *)((ulong)puVar1 | 0x1c) = iVar17 + uVar11 * uVar3 * 0x10000 + -0x10001;
  *(int *)((ulong)puVar1 | 0x20) =
       *(int *)(param_2 + 0x428) + ((*(int *)(param_2 + 0x428) + 2U) / 3) * 0x10000 + -0x10000;
  puVar16 = (uint *)((ulong)puVar1 | 0x24);
  *puVar16 = (*(int *)(param_2 + 0x424) + uVar3 * 0x10000 +
             (uint)((ulong)*(uint *)(param_2 + 0x428) * 0x2aaaaaaab >> 0x21) * 0x40000000) - 0x10001
  ;
  if ((*(int *)(param_2 + 0x3d8) == 0) || (*(int *)(param_2 + 0x4fc) != 0)) {
    *(undefined4 *)((ulong)puVar1 | 0x14) = 0;
    uVar11 = *(uint *)(param_2 + 0x428);
    goto LAB_ffff0000b802db2c;
  }
  switch(*(undefined4 *)(param_2 + 0x52c)) {
  case 0:
    iVar17 = *(int *)(param_2 + 0x428);
    uVar4 = 0x40e10d2;
    goto LAB_ffff0000b802da48;
  case 1:
    iVar17 = *(int *)(param_2 + 0x428);
    uVar4 = 0x40d20e1;
LAB_ffff0000b802da48:
    uVar11 = iVar17 + 1U >> 1;
    break;
  case 2:
    uVar4 = 0x5924186;
    uVar11 = (*(int *)(param_2 + 0x428) * 2 + 2U) / 3;
    break;
  case 3:
    uVar4 = 0x5186924;
    uVar11 = (*(int *)(param_2 + 0x428) * 2 + 2U) / 3;
    break;
  case 4:
    uVar11 = (*(int *)(param_2 + 0x428) * 2 + 2U) / 3;
    uVar4 = 0x5924492;
    break;
  case 5:
    uVar11 = (*(int *)(param_2 + 0x428) * 2 + 2U) / 3;
    uVar4 = 0x5492924;
    break;
  default:
    uVar4 = 0;
    uVar11 = *(uint *)(param_2 + 0x428);
  }
  *(undefined4 *)((ulong)puVar1 | 0x14) = uVar4;
  *(uint *)(param_2 + 0x42c) = (*(int *)(param_2 + 0x418) * uVar11 >> 4) + 7 >> 3;
LAB_ffff0000b802db2c:
  uVar3 = uVar11 + uVar11 * *(int *)(param_2 + 0x3fc);
  if ((*(int *)(param_2 + 0x3d8) != 0) && (*(int *)(param_2 + 0x4fc) == 0)) {
    iVar17 = ((uint)((ulong)(uVar11 + 2) * 0xaaaaaaab >> 0x20) & 0x1fffe) * 0x8000;
    *(int *)((ulong)puVar1 | 0x20) = iVar17 + *(int *)(param_2 + 0x428) + -0x10000;
    *(int *)((ulong)puVar1 | 0x18) = iVar2 + -0x10000 + iVar17 + iVar17 * *(int *)(param_2 + 0x3fc);
    *puVar16 = *puVar16 & 0x3fffffff | (uint)((ulong)uVar11 * 0x2aaaaaaab >> 0x21) << 0x1e;
  }
  if (uVar3 < 0x5a0) {
    *(undefined4 *)((ulong)puVar1 | 0x70) = 0x800002d9;
  }
  *(uint *)((ulong)puVar1 | 0x3c) = uVar11 + uVar3 * 0x10000;
  iVar17 = *(int *)(param_2 + 0x42c);
  *(uint *)((ulong)puVar1 | 0x28) =
       iVar17 + ((iVar17 + iVar17 * *(int *)(param_2 + 0x3fc) + 2U) / 3) * 0x10000;
  *(int *)((ulong)puVar1 | 0x2c) = *(int *)(param_2 + 0x424) * *(int *)(param_2 + 0x42c);
  uVar11 = *(int *)(param_2 + 0x42c) + *(int *)(param_2 + 0x42c) * *(int *)(param_2 + 0x3fc) + 2;
  *(uint *)((ulong)puVar1 | 0x38) = ((uVar11 / 3) * 3 - uVar11) + 2;
  iVar17 = 0x22;
  if (*(int *)(param_2 + 0x404) != 0) {
    iVar17 = *(int *)(param_2 + 0x404);
  }
  *(int *)((ulong)puVar1 | 0x34) = iVar17;
  uVar11 = 0x40;
  if ((*(uint *)(param_2 + 0x3f8) & 0xf) != 2) {
    uVar11 = 0x20;
  }
  *(uint *)((ulong)puVar1 | 0x200) = *(uint *)((ulong)puVar1 | 0x200) & 0xffffff9f | uVar11;
  *(uint *)((ulong)puVar1 | 0x80) =
       (*(uint *)(param_2 + 0x410) & 0xf | *(int *)(param_2 + 0x40c) << 4) +
       *(int *)(param_2 + 0x418) * 0x100 + *(int *)(param_2 + 0x408) * 0x40000 +
       *(int *)(param_2 + 0x414) * 0x80000;
  *(int *)((ulong)puVar1 | 0x84) = *(int *)(param_2 + 0x430) + *(int *)(param_2 + 0x434) * 0x10000;
  *(int *)((ulong)puVar1 | 0x88) = *(int *)(param_2 + 0x438) + *(int *)(param_2 + 0x43c) * 0x10000;
  *(int *)((ulong)puVar1 | 0x8c) = *(int *)(param_2 + 0x440) + *(int *)(param_2 + 0x444) * 0x10000;
  *(int *)((ulong)puVar1 | 0x90) = *(int *)(param_2 + 0x448) + *(int *)(param_2 + 0x44c) * 0x10000;
  *(int *)((ulong)puVar1 | 0x94) = *(int *)(param_2 + 0x450) + *(int *)(param_2 + 0x454) * 0x10000;
  *(int *)((ulong)puVar1 | 0x98) =
       *(int *)(param_2 + 0x458) + *(int *)(param_2 + 0x45c) * 0x100 +
       *(int *)(param_2 + 0x460) * 0x10000;
  *(int *)((ulong)puVar1 | 0x9c) =
       *(int *)(param_2 + 0x464) + *(int *)(param_2 + 0x468) * 0x100 +
       *(int *)(param_2 + 0x46c) * 0x10000 + *(int *)(param_2 + 0x470) * 0x1000000 +
       *(int *)(param_2 + 0x474) * 0x10000000;
  if (*(int *)(param_2 + 0x40c) == 10) {
    uVar12 = 0xd230;
    uVar13 = 0xd20dd1e9;
    uVar14 = 0xd1e9d9c9;
    uVar15 = 0xd9c7e1a7;
    uVar18 = 0xe187e167;
    uVar5 = 0xe967f167;
    uVar6 = 0xf9450125;
    uVar4 = 0x1040900;
  }
  else {
    uVar4 = 0x800880;
    uVar12 = 0xd1ed;
    uVar13 = 0xd1a7d1a5;
    uVar14 = 0xd185d965;
    uVar15 = 0xd943e123;
    uVar18 = 0xe103e0e3;
    uVar5 = 0xe8e3f0e3;
    uVar6 = 0xf8c100a1;
  }
  *(undefined4 *)((ulong)puVar1 | 0xa0) = 0x382a1c0e;
  *(undefined4 *)((ulong)puVar1 | 0xa4) = 0x69625446;
  *(undefined4 *)((ulong)puVar1 | 0xa8) = 0x7b797770;
  *(undefined4 *)((ulong)puVar1 | 0xac) = 0x7e7d;
  *(undefined4 *)((ulong)puVar1 | 0xb0) = uVar4;
  *(undefined4 *)((ulong)puVar1 | 0xb4) = uVar6;
  *(undefined4 *)((ulong)puVar1 | 0xb8) = uVar5;
  *(undefined4 *)((ulong)puVar1 | 0xbc) = uVar18;
  *(undefined4 *)((ulong)puVar1 | 0xc0) = uVar15;
  *(undefined4 *)((ulong)puVar1 | 0xc4) = uVar14;
  *(undefined4 *)((ulong)puVar1 | 200) = uVar13;
  *(undefined4 *)((ulong)puVar1 | 0xcc) = uVar12;
  if ((*(int *)(param_2 + 0x3d8) != 0) && (*(int *)(param_2 + 0x4fc) == 0)) {
    *(undefined4 *)((ulong)puVar1 | 0xcc) = 0xd16a;
    *(undefined4 *)((ulong)puVar1 | 200) = 0xd147d125;
    *(undefined4 *)((ulong)puVar1 | 0xc4) = 0xd125d925;
    *(undefined4 *)((ulong)puVar1 | 0xc0) = 0xd923e103;
    *(undefined4 *)((ulong)puVar1 | 0xb0) = 0x800840;
  }
  if (*(int *)(param_2 + 0x478) != 0) {
    if (puVar10 != (uint *)0x0) {
      *(uint *)((ulong)puVar1 | 0xa0) =
           (puVar10[1] & 0x3fffffc0) * 4 + (*puVar10 >> 6) + (puVar10[2] >> 6) * 0x10000 +
           (puVar10[3] >> 6) * 0x1000000;
      *(uint *)((ulong)puVar1 | 0xa4) =
           (puVar10[5] & 0x3fffffc0) * 4 + (puVar10[4] >> 6) + (puVar10[6] >> 6) * 0x10000 +
           (puVar10[7] >> 6) * 0x1000000;
      *(uint *)((ulong)puVar1 | 0xa8) =
           (puVar10[9] & 0x3fffffc0) * 4 + (puVar10[8] >> 6) + (puVar10[10] >> 6) * 0x10000 +
           (puVar10[0xb] >> 6) * 0x1000000;
      *(uint *)((ulong)puVar1 | 0xac) = (puVar10[0xd] & 0x3fffffc0) * 4 + (puVar10[0xc] >> 6);
    }
    if (((piVar7 != (int *)0x0) && (piVar8 != (int *)0x0)) && (piVar9 != (int *)0x0)) {
      iVar2 = *piVar9 * 0x400;
      iVar17 = iVar2 + 0x10000;
      if (-1 < *piVar9) {
        iVar17 = iVar2;
      }
      *(int *)((ulong)puVar1 | 0xb0) =
           *piVar7 + *piVar8 * 0x20 + piVar7[1] * 0x10000 + iVar17 + piVar8[1] * 0x200000 +
           piVar9[1] * 0x4000000;
      iVar2 = piVar9[2] * 0x400;
      iVar17 = iVar2 + 0x10000;
      if (-1 < piVar9[2]) {
        iVar17 = iVar2;
      }
      *(int *)((ulong)puVar1 | 0xb4) =
           piVar7[2] + piVar8[2] * 0x20 + piVar7[3] * 0x10000 + iVar17 + piVar8[3] * 0x200000 +
           piVar9[3] * 0x4000000;
      iVar2 = piVar9[4] * 0x400;
      iVar17 = iVar2 + 0x10000;
      if (-1 < piVar9[4]) {
        iVar17 = iVar2;
      }
      *(int *)((ulong)puVar1 | 0xb8) =
           piVar7[4] + piVar8[4] * 0x20 + piVar7[5] * 0x10000 + iVar17 + piVar8[5] * 0x200000 +
           piVar9[5] * 0x4000000;
      iVar2 = piVar9[6] * 0x400;
      iVar17 = iVar2 + 0x10000;
      if (-1 < piVar9[6]) {
        iVar17 = iVar2;
      }
      *(int *)((ulong)puVar1 | 0xbc) =
           piVar7[6] + piVar8[6] * 0x20 + piVar7[7] * 0x10000 + iVar17 + piVar8[7] * 0x200000 +
           piVar9[7] * 0x4000000;
      iVar2 = piVar9[8] * 0x400;
      iVar17 = iVar2 + 0x10000;
      if (-1 < piVar9[8]) {
        iVar17 = iVar2;
      }
      *(int *)((ulong)puVar1 | 0xc0) =
           piVar7[8] + piVar8[8] * 0x20 + piVar7[9] * 0x10000 + iVar17 + piVar8[9] * 0x200000 +
           piVar9[9] * 0x4000000;
      iVar2 = piVar9[10] * 0x400;
      iVar17 = iVar2 + 0x10000;
      if (-1 < piVar9[10]) {
        iVar17 = iVar2;
      }
      *(int *)((ulong)puVar1 | 0xc4) =
           piVar7[10] + piVar8[10] * 0x20 + piVar7[0xb] * 0x10000 + iVar17 + piVar8[0xb] * 0x200000
           + piVar9[0xb] * 0x4000000;
      iVar2 = piVar9[0xc] * 0x400;
      iVar17 = iVar2 + 0x10000;
      if (-1 < piVar9[0xc]) {
        iVar17 = iVar2;
      }
      *(int *)((ulong)puVar1 | 200) =
           piVar7[0xc] + piVar8[0xc] * 0x20 + piVar7[0xd] * 0x10000 + iVar17 +
           piVar8[0xd] * 0x200000 + piVar9[0xd] * 0x4000000;
      iVar2 = piVar9[0xe] * 0x400;
      iVar17 = iVar2 + 0x10000;
      if (-1 < piVar9[0xe]) {
        iVar17 = iVar2;
      }
      *(int *)((ulong)puVar1 | 0xcc) = piVar7[0xe] + piVar8[0xe] * 0x20 + iVar17;
    }
  }
  return 0;
}



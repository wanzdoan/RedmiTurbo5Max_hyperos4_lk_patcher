
undefined8 FUN_ffff0000b803a748(int param_1,long param_2)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  byte bVar4;
  uint uVar5;
  int iVar6;
  ulong uVar7;
  uint *puVar8;
  long lVar9;
  int iVar10;
  long lVar11;
  
  uVar5 = param_1 - 0xce;
  iVar10 = 1;
  lVar9 = -0xffffcd730000;
  lVar11 = -0xffffcd730000;
  switch(uVar5) {
  case 0:
    iVar10 = 0;
    break;
  case 1:
    break;
  case 2:
    iVar10 = 2;
    break;
  case 3:
    iVar10 = 3;
    break;
  case 4:
    iVar10 = 4;
    break;
  case 5:
    iVar10 = 5;
    break;
  case 6:
    iVar10 = 6;
    break;
  case 7:
    iVar10 = 7;
    break;
  case 8:
    iVar10 = 8;
    break;
  case 9:
    iVar10 = 9;
    break;
  case 10:
    iVar10 = 0;
  case 0xb:
    goto code_r0xffff0000b803a860;
  case 0xc:
    iVar10 = 2;
    goto code_r0xffff0000b803a860;
  case 0xd:
    iVar10 = 3;
    goto code_r0xffff0000b803a860;
  case 0xe:
    iVar10 = 4;
    goto code_r0xffff0000b803a860;
  case 0xf:
    iVar10 = 5;
    goto code_r0xffff0000b803a860;
  case 0x10:
    iVar10 = 6;
    goto code_r0xffff0000b803a860;
  case 0x11:
    iVar10 = 7;
code_r0xffff0000b803a860:
    lVar11 = -0xffffcd330000;
    break;
  default:
    goto switchD_ffff0000b803a79c_default;
  }
  uVar7 = FUN_ffff0000b804f554();
  if (((uVar7 & 1) != 0) && (iVar6 = FUN_ffff0000b8050384(), iVar6 == 0xfc11)) {
    *(undefined4 *)(lVar11 + (ulong)(uint)(iVar10 << 0x10) | 0x104) = 0xffff7f00;
  }
  lVar1 = 0x928;
  if (*(char *)(param_2 + 0x2d8) != '\0') {
    lVar1 = 0x2c8;
  }
  *(uint *)(lVar11 + (ulong)(uint)(iVar10 << 0x10) | 0x30) =
       *(uint *)(param_2 + lVar1) | *(int *)(param_2 + 0x92c) << 0x10;
  if (0x11 < uVar5) {
switchD_ffff0000b803a79c_default:
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_disp_dd_ffff0000b809a10d,0x51,&DAT_ffff0000b8086efb);
  }
  uVar2 = *(uint *)(param_2 + 0x4c);
  iVar10 = *(int *)(param_2 + 0x50);
  uVar3 = *(uint *)(param_2 + 0x5c);
  iVar6 = 1;
  bVar4 = *(byte *)(param_2 + 0x60);
  switch(uVar5) {
  case 0:
    iVar6 = 0;
    break;
  case 2:
    iVar6 = 2;
    break;
  case 3:
    iVar6 = 3;
    break;
  case 4:
    iVar6 = 4;
    break;
  case 5:
    iVar6 = 5;
    break;
  case 6:
    iVar6 = 6;
    break;
  case 7:
    iVar6 = 7;
    break;
  case 8:
    iVar6 = 8;
    break;
  case 9:
    iVar6 = 9;
    break;
  case 10:
    iVar6 = 0;
  case 0xb:
    goto code_r0xffff0000b803a9b4;
  case 0xc:
    iVar6 = 2;
    goto code_r0xffff0000b803a9b4;
  case 0xd:
    iVar6 = 3;
    goto code_r0xffff0000b803a9b4;
  case 0xe:
    iVar6 = 4;
    goto code_r0xffff0000b803a9b4;
  case 0xf:
    iVar6 = 5;
    goto code_r0xffff0000b803a9b4;
  case 0x10:
    iVar6 = 6;
    goto code_r0xffff0000b803a9b4;
  case 0x11:
    iVar6 = 7;
code_r0xffff0000b803a9b4:
    lVar9 = -0xffffcd330000;
  }
  uVar7 = lVar9 + (ulong)(uint)(iVar6 << 0x10);
  *(undefined4 *)(uVar7 | 0x20) = 1;
  puVar8 = (uint *)(uVar7 | 0x200);
  *(uint *)(uVar7 | 0x48) = uVar2 | iVar10 << 0x10;
  *(undefined4 *)(uVar7 | 0x50) = 2;
  *puVar8 = *puVar8 & 0xffffff00 | (uint)bVar4;
  *puVar8 = *puVar8 & 0xffffe000 | *puVar8 & 0xfff | (uVar3 & 1) << 0xc;
  if (iVar6 == 1) {
    *(uint *)(uVar7 | 0x208) = *(uint *)(uVar7 | 0x208) | 0x1000000;
  }
  FUN_ffff0000b8036b38(param_1);
  return 0;
}



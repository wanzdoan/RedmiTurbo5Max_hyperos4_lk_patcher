// ffff0000907b68b8  size=644  FUN_ffff0000907b68b8

undefined4 FUN_ffff0000907b68b8(uint param_1,undefined8 *param_2,long *param_3)

{
  uint uVar1;
  short sVar2;
  undefined8 uVar3;
  char *pcVar4;
  uint *puVar5;
  int iVar6;
  uint uVar7;
  long lVar8;
  undefined4 uVar9;
  long lVar10;
  undefined4 uVar11;
  
  uVar9 = 0x10a006;
  if ((*(uint *)*param_2 & 0xffffff) != 0x4d4d4d) {
    return 0x10a006;
  }
  FUN_ffff00009077eb88(*param_3,0xff,*(uint *)(param_3 + 1) >> 1);
  FUN_ffff00009077eb88(*param_3 + (ulong)(*(uint *)(param_3 + 1) >> 1),0xff);
  lVar10 = *param_3;
  FUN_ffff00009077eb88(lVar10,0,0x14);
  puVar5 = (uint *)*param_2;
  if (((*puVar5 & 0xffffff) == 0x4d4d4d) && (*(short *)((long)puVar5 + 6) == 0)) {
    if (*(char *)((long)puVar5 + 0x1a) == '\x0e') {
      uVar11 = 0x1000;
      pcVar4 = s_COMBO_BOOT_ffff0000907fabf8;
    }
    else if (param_1 == 1) {
      uVar11 = 0x1000;
      pcVar4 = s_UFS_BOOT_ffff0000907dbc27;
    }
    else {
      if (param_1 != 0) goto LAB_ffff0000907b6960;
      uVar11 = 0x200;
      pcVar4 = s_EMMC_BOOT_ffff0000907fe4dc;
    }
    *(undefined4 *)(lVar10 + 0xc) = 1;
  }
  else {
LAB_ffff0000907b6960:
    pcVar4 = (char *)0x0;
    uVar11 = 0;
  }
  FUN_ffff00009077ef1c(lVar10,pcVar4,0xc);
  puVar5 = (uint *)*param_2;
  *(undefined4 *)(lVar10 + 0x10) = uVar11;
  if (((*puVar5 & 0xffffff) == 0x4d4d4d) && (*(short *)((long)puVar5 + 6) == 0)) {
    lVar10 = 0x200;
    if (*(char *)((long)puVar5 + 0x1a) != '\x0e' && 1 < param_1) {
      lVar10 = 0;
    }
  }
  else {
    lVar10 = 0;
  }
  lVar10 = *param_3 + lVar10;
  FUN_ffff00009077eb88(lVar10,0,0xb4);
  uVar3 = FUN_ffff00009077ef88(s_BRLYT_ffff0000907d3b61);
  FUN_ffff00009077e858(lVar10,s_BRLYT_ffff0000907d3b61,uVar3);
  puVar5 = (uint *)*param_2;
  uVar7 = 0;
  lVar8 = param_3[1];
  uVar1 = *puVar5;
  *(undefined4 *)(lVar10 + 8) = 1;
  *(int *)(lVar10 + 0xc) = (int)lVar8;
  if ((uVar1 & 0xffffff) == 0x4d4d4d) {
    if (*(short *)((long)puVar5 + 6) == 0) {
      iVar6 = 0x1000;
      if ((*(char *)((long)puVar5 + 0x1a) != '\x0e') && (param_1 != 1)) {
        iVar6 = (uint)(param_1 == 0) << 0xb;
      }
    }
    else {
      iVar6 = 0;
    }
    uVar7 = 0;
    do {
      sVar2 = *(short *)((long)puVar5 + 6);
      if (sVar2 == 4) {
        lVar8 = 0x34;
      }
      else if (sVar2 == 1) {
        lVar8 = 0xc;
        *(uint *)(lVar10 + 0x24) = puVar5[2];
      }
      else if (sVar2 == 0) {
        uVar7 = puVar5[9];
        uVar1 = puVar5[6];
        *(undefined1 *)(lVar10 + 0x18) = *(undefined1 *)((long)puVar5 + 0x1a);
        lVar8 = 0x38;
        *(short *)(lVar10 + 0x1a) = (short)uVar1;
        *(int *)(lVar10 + 0x1c) = iVar6;
        *(uint *)(lVar10 + 0x20) = uVar7 + iVar6;
        *(uint *)(lVar10 + 0x10) = uVar7 + iVar6;
        *(undefined4 *)(lVar10 + 0x14) = 0x42424242;
      }
      else {
        lVar8 = 8;
      }
      puVar5 = (uint *)((long)puVar5 + lVar8);
    } while ((*puVar5 & 0xffffff) == 0x4d4d4d);
  }
  if (*(uint *)(param_2 + 1) <= uVar7) {
    uVar9 = 0;
  }
  return uVar9;
}



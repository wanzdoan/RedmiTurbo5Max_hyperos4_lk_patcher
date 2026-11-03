
undefined8 FUN_ffff0000be06ec24(long param_1,undefined8 *param_2)

{
  int iVar1;
  uint uVar2;
  uint uVar3;
  undefined4 uVar4;
  long lVar5;
  uint *puVar6;
  undefined4 *puVar7;
  ushort uVar8;
  uint uVar9;
  uint uVar10;
  uint *puVar11;
  
  if (*(int *)(param_2 + 7) == 2) {
    puVar7 = (undefined4 *)*param_2;
    uVar4 = 0x10000000;
    if (*(char *)((long)param_2 + 0x41) != '\0') {
      uVar4 = 0x11000000;
    }
    iVar1 = *(int *)(param_1 + 0x1b8);
    puVar7[2] = 0xf;
    *puVar7 = uVar4;
    if (iVar1 == 0) {
      FUN_ffff0000be060ae0(param_2[1],0,0x20);
      uVar10 = (*(uint *)((long)param_2 + 0x3c) & 0xff00ff00) >> 8 |
               (*(uint *)((long)param_2 + 0x3c) & 0xff00ff) << 8;
      *(uint *)param_2[1] = uVar10 >> 0x10 | uVar10 << 0x10;
      return 0;
    }
    if (iVar1 != 1) {
      return 0xffffffff;
    }
    puVar6 = (uint *)param_2[1];
    uVar2 = (uint)(*(ushort *)(param_1 + 0x1ca) >> 8);
    uVar3 = *(ushort *)(param_1 + 0x1ca) & 0xff00ff;
    uVar10 = uVar2 | uVar3 << 8;
    uVar9 = (*(uint *)((long)param_2 + 0x3c) & 0xff00ff00 | (uint)*(byte *)(param_2 + 8) << 8 |
            0x16000000) >> 8 | (*(uint *)((long)param_2 + 0x3c) & 0xff00ff) << 8;
    puVar6[1] = (uint)*(byte *)(param_1 + 0x1c0) << 8;
    puVar6[2] = (uVar3 | uVar2 << 8) << 0x10;
    *puVar6 = uVar9 >> 0x10 | uVar9 << 0x10;
    FUN_ffff0000be0607b0(puVar6 + 3,param_1 + 0x1c4,0x14);
    if (*(char *)(param_1 + 0x1c4) == '\x02') {
      FUN_ffff0000be0607b0(puVar6 + 8,*(undefined8 *)(param_1 + 0x1d8),uVar10);
      FUN_ffff0000be007308(puVar6 + 8,uVar10);
    }
  }
  else {
    if ((*(int *)(param_2 + 7) != 0) || (lVar5 = param_2[4], lVar5 == 0)) {
      return 0xffffffff;
    }
    puVar6 = (uint *)*param_2;
    if (*(int *)(lVar5 + 8) == 2) {
      uVar10 = 0x14000000;
      uVar9 = 0x400000;
    }
    else if (*(int *)(lVar5 + 8) == 1) {
      uVar10 = 0x12000000;
      uVar9 = 0x200000;
    }
    else {
      uVar9 = 0;
      uVar10 = 0x10000000;
    }
    if (*(char *)((long)param_2 + 0x41) != '\0') {
      uVar10 = uVar10 | 0x1000000;
    }
    puVar11 = (uint *)param_2[1];
    uVar2 = (*(uint *)(lVar5 + 0x20) & 0xff00ff00) >> 8 | (*(uint *)(lVar5 + 0x20) & 0xff00ff) << 8;
    puVar6[2] = 0xf;
    *puVar6 = uVar10;
    puVar11[3] = uVar2 >> 0x10 | uVar2 << 0x10;
    puVar11[1] = 0;
    puVar11[2] = 0;
    lVar5 = param_2[4];
    uVar8 = *(ushort *)(lVar5 + 0x1e);
    uVar10 = *(uint *)((long)param_2 + 0x3c) | uVar9 | (uint)*(byte *)(param_2 + 8) << 8;
    uVar10 = (uVar10 & 0xff00ff00 | 0x1000000) >> 8 | (uVar10 & 0xff00ff) << 8;
    if (0xf < uVar8) {
      uVar8 = 0x10;
    }
    *puVar11 = uVar10 >> 0x10 | uVar10 << 0x10;
    FUN_ffff0000be0607b0(puVar11 + 4,lVar5 + 0xd,uVar8);
  }
  return 0;
}



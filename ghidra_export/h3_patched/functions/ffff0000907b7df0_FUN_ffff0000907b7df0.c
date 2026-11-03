// ffff0000907b7df0  size=1048  FUN_ffff0000907b7df0

/* WARNING: Type propagation algorithm not settling */

int FUN_ffff0000907b7df0(long *param_1,uint param_2,long param_3)

{
  uint uVar1;
  ulong uVar2;
  ulong uVar3;
  uint uVar4;
  int iVar5;
  long lVar6;
  uint *puVar7;
  int iVar8;
  uint uVar9;
  long lVar10;
  ulong uVar11;
  undefined8 uVar12;
  uint local_54;
  undefined4 local_50;
  undefined8 local_4c;
  int local_44;
  
  local_44 = 0xff;
  if (param_1[2] != 0) {
    if (param_2 - 3 < 0xfffffffe) {
      return 0x7254;
    }
    if ((param_2 == 1) && ((param_1[2] & 0xfU) != 0)) {
      return 0x724f;
    }
    if (1 < *(byte *)((long)param_1 + 0x41)) {
      return 0x7245;
    }
    if (2 < *(uint *)((long)param_1 + 0x44)) {
      return 0x726e;
    }
    uVar12 = *(undefined8 *)(&DAT_ffff000090813380 + (long)(int)*(uint *)((long)param_1 + 0x44) * 8)
    ;
    local_4c = 0;
    local_54 = 0;
    local_50 = 0;
    FUN_ffff00009077eb88((long)&local_4c + 4,0,4);
    FUN_ffff00009077eb88(&local_4c,0,4);
    FUN_ffff00009077eb88(&local_50,0,4);
    FUN_ffff00009077eb88(&local_54,0,4);
    uVar2 = thunk_FUN_ffff00009077b06c(param_1[1]);
    uVar3 = thunk_FUN_ffff00009077b06c(param_1[5]);
    uVar4 = 0x10000000;
    if (*(char *)((long)param_1 + 0x41) != '\x01') {
      uVar4 = 0x18000000;
    }
    uVar1 = (param_2 & 0xf) << 0xf;
    uVar9 = local_4c._4_4_ & 0x7800000 | *(int *)((long)param_1 + 0x44) << 0x1e;
    lVar10 = *param_1;
    if (param_1[7] == 0) {
      local_4c._4_4_ =
           uVar9 | local_4c._4_4_ & 0x7fb0 | uVar1 | uVar4 | 0x80040 | *(byte *)(param_1 + 8) & 0xf;
    }
    else {
      local_4c._4_4_ = uVar9 | local_4c._4_4_ & 0x7fbf | uVar1 | uVar4 | 0x80040;
      thunk_FUN_ffff000090707cc4(param_1[7],uVar12);
      local_4c._4_4_ = local_4c._4_4_ | 0x80;
    }
    uVar11 = uVar2 >> 0x20 & 0xf;
    uVar4 = *(int *)(lVar10 + 0x54) + 1;
    uVar9 = uVar4 & 0xffff;
    if ((uVar4 & 0xfffe) == 0) {
      uVar9 = 1;
    }
    uVar4 = 0x2000000;
    if (param_3 != 0) {
      uVar4 = 0x2800000;
    }
    uVar9 = (uint)local_4c & 0xf9ff0000 | uVar4 | uVar9;
    if ((uVar11 != 0) || (((param_1[2] | uVar3) & 0xf00000000) != 0)) {
      uVar9 = uVar9 | 0x400000;
      local_54 = local_54 | 0x20;
    }
    local_4c = CONCAT44(local_4c._4_4_,uVar9);
    thunk_FUN_ffff000090707cc4(param_1[1]);
    thunk_FUN_ffff000090707cc4(param_1[5],param_1[2]);
    lVar10 = *param_1;
    do {
    } while ((*(uint *)(lVar10 + 0x100) & 0xff) < 0x15);
    lVar6 = param_1[2];
    *(uint *)(lVar10 + 0x104) = local_4c._4_4_;
    *(uint *)(lVar10 + 0x104) = (uint)local_4c;
    *(undefined4 *)(lVar10 + 0x104) = local_50;
    if ((local_4c._2_1_ >> 6 & 1) == 0) {
      iVar5 = 7;
    }
    else {
      iVar5 = 8;
      *(uint *)(lVar10 + 0x104) = local_54;
    }
    *(int *)(lVar10 + 0x104) = (int)uVar2;
    *(int *)(lVar10 + 0x104) = (int)lVar6;
    *(int *)(lVar10 + 0x104) = (int)uVar3;
    *(int *)(lVar10 + 0x104) = (int)(lVar6 + uVar3) + -1;
    if ((char)local_4c._2_1_ < '\0') {
      iVar5 = iVar5 + 1;
      *(int *)(lVar10 + 0x104) = (int)param_3;
    }
    if ((local_4c._4_4_ >> 6 & 1) != 0) {
      puVar7 = (uint *)param_1[3];
      iVar5 = iVar5 + 4;
      uVar4 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
      *(uint *)(lVar10 + 0x104) = uVar4 >> 0x10 | uVar4 << 0x10;
      uVar4 = (puVar7[1] & 0xff00ff00) >> 8 | (puVar7[1] & 0xff00ff) << 8;
      *(uint *)(lVar10 + 0x104) = uVar4 >> 0x10 | uVar4 << 0x10;
      uVar4 = (puVar7[2] & 0xff00ff00) >> 8 | (puVar7[2] & 0xff00ff) << 8;
      *(uint *)(lVar10 + 0x104) = uVar4 >> 0x10 | uVar4 << 0x10;
      uVar4 = (puVar7[3] & 0xff00ff00) >> 8 | (puVar7[3] & 0xff00ff) << 8;
      *(uint *)(lVar10 + 0x104) = uVar4 >> 0x10 | uVar4 << 0x10;
    }
    if ((local_4c._4_4_ >> 7 & 1) != 0) {
      uVar4 = 0;
      puVar7 = (uint *)param_1[7];
      iVar8 = 4;
      if ((local_4c._4_4_ >> 0x1e != 0) && ((local_4c._4_4_ & 0x780000) == 0x80000)) {
        if (local_4c._4_4_ >> 0x1e == 2) {
          uVar9 = 0;
          uVar4 = 0;
          *(undefined4 *)(lVar10 + 0x104) = 0;
        }
        else {
          uVar4 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
          *(uint *)(lVar10 + 0x104) = uVar4 >> 0x10 | uVar4 << 0x10;
          uVar4 = (puVar7[1] & 0xff00ff00) >> 8 | (puVar7[1] & 0xff00ff) << 8;
          uVar9 = uVar4 >> 0x10 | uVar4 << 0x10;
          uVar4 = 8;
        }
        *(uint *)(lVar10 + 0x104) = uVar9;
        uVar9 = *(uint *)((long)puVar7 + (ulong)uVar4);
        uVar9 = (uVar9 & 0xff00ff00) >> 8 | (uVar9 & 0xff00ff) << 8;
        *(uint *)(lVar10 + 0x104) = uVar9 >> 0x10 | uVar9 << 0x10;
        uVar9 = *(uint *)((long)puVar7 + ((ulong)uVar4 | 4));
        uVar9 = (uVar9 & 0xff00ff00) >> 8 | (uVar9 & 0xff00ff) << 8;
        uVar4 = uVar4 + 8;
        iVar8 = 8;
        *(uint *)(lVar10 + 0x104) = uVar9 >> 0x10 | uVar9 << 0x10;
      }
      puVar7 = (uint *)((long)puVar7 + (ulong)uVar4);
      iVar5 = iVar8 + iVar5;
      uVar4 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
      *(uint *)(lVar10 + 0x104) = uVar4 >> 0x10 | uVar4 << 0x10;
      uVar4 = (puVar7[1] & 0xff00ff00) >> 8 | (puVar7[1] & 0xff00ff) << 8;
      *(uint *)(lVar10 + 0x104) = uVar4 >> 0x10 | uVar4 << 0x10;
      uVar4 = (puVar7[2] & 0xff00ff00) >> 8 | (puVar7[2] & 0xff00ff) << 8;
      *(uint *)(lVar10 + 0x104) = uVar4 >> 0x10 | uVar4 << 0x10;
      uVar4 = (puVar7[3] & 0xff00ff00) >> 8 | (puVar7[3] & 0xff00ff) << 8;
      *(uint *)(lVar10 + 0x104) = uVar4 >> 0x10 | uVar4 << 0x10;
    }
    if (((local_4c._2_1_ >> 6 & 1) != 0) && (((byte)local_54 >> 5 & 1) != 0)) {
      iVar5 = iVar5 + 7;
      *(int *)(lVar10 + 0x104) = (int)uVar11;
      *(uint *)(lVar10 + 0x104) = (uint)((ulong)lVar6 >> 0x20) & 0xf;
      *(uint *)(lVar10 + 0x104) = (uint)(uVar3 >> 0x20) & 0xf;
      *(uint *)(lVar10 + 0x104) = (uint)(lVar6 + uVar3 + -1 >> 0x20) & 0xf;
      *(uint *)(lVar10 + 0x104) = (uint)((ulong)param_3 >> 0x20) & 0xf;
      *(undefined4 *)(lVar10 + 0x104) = 0;
      *(undefined4 *)(lVar10 + 0x104) = 0;
    }
    *(uint *)(lVar10 + 0x100) = (*(uint *)(lVar10 + 0x100) & 0xff) - iVar5;
    local_44 = FUN_ffff0000907b88a0(lVar10,param_3,local_4c & 0xffff);
    if (local_44 != 0) {
      return local_44;
    }
    thunk_FUN_ffff000090707d04(param_1[5],param_1[2]);
  }
  return 0;
}



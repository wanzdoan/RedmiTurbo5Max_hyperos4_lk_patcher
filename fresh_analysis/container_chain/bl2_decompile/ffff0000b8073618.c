
/* WARNING: Removing unreachable block (ram,0xffff0000b80737d8) */
/* WARNING: Removing unreachable block (ram,0xffff0000b807366c) */
/* WARNING: Type propagation algorithm not settling */

int FUN_ffff0000b8073618(long *param_1)

{
  ulong uVar1;
  ulong uVar2;
  uint uVar3;
  int iVar4;
  long lVar5;
  uint *puVar6;
  int iVar7;
  uint uVar8;
  long lVar9;
  ulong uVar10;
  undefined8 uVar11;
  uint local_54;
  undefined4 local_50;
  undefined8 local_4c;
  int local_44;
  
  local_44 = 0xff;
  if (param_1[2] != 0) {
    if ((param_1[2] & 0xfU) != 0) {
      return 0x724f;
    }
    if (1 < *(byte *)((long)param_1 + 0x41)) {
      return 0x7245;
    }
    if (2 < *(uint *)((long)param_1 + 0x44)) {
      return 0x726e;
    }
    uVar11 = *(undefined8 *)(&DAT_ffff0000b80aaa38 + (long)(int)*(uint *)((long)param_1 + 0x44) * 8)
    ;
    local_4c = 0;
    local_54 = 0;
    local_50 = 0;
    FUN_ffff0000b804d070((long)&local_4c + 4,0,4);
    FUN_ffff0000b804d070(&local_4c,0,4);
    FUN_ffff0000b804d070(&local_50,0,4);
    FUN_ffff0000b804d070(&local_54,0,4);
    uVar1 = thunk_FUN_ffff0000b8049ec8(param_1[1]);
    uVar2 = thunk_FUN_ffff0000b8049ec8(param_1[5]);
    uVar3 = 0x10000000;
    if (*(char *)((long)param_1 + 0x41) != '\x01') {
      uVar3 = 0x18000000;
    }
    uVar8 = local_4c._4_4_ & 0x7800000 | *(int *)((long)param_1 + 0x44) << 0x1e;
    lVar9 = *param_1;
    if (param_1[7] == 0) {
      local_4c._4_4_ =
           uVar8 | local_4c._4_4_ & 0x7fb0 | 0x8000 | uVar3 | 0x80040 | *(byte *)(param_1 + 8) & 0xf
      ;
    }
    else {
      local_4c._4_4_ = uVar8 | local_4c._4_4_ & 0x7fbf | 0x8000 | uVar3 | 0x80040;
      thunk_FUN_ffff0000b800758c(param_1[7],uVar11);
      local_4c._4_4_ = local_4c._4_4_ | 0x80;
    }
    uVar10 = uVar1 >> 0x20 & 0xf;
    uVar3 = *(int *)(lVar9 + 0x54) + 1;
    uVar8 = uVar3 & 0xffff;
    if ((uVar3 & 0xfffe) == 0) {
      uVar8 = 1;
    }
    uVar8 = (uint)local_4c & 0xf9ff0000 | 0x2000000 | uVar8;
    if ((uVar10 != 0) || (((param_1[2] | uVar2) & 0xf00000000) != 0)) {
      uVar8 = uVar8 | 0x400000;
      local_54 = local_54 | 0x20;
    }
    local_4c = CONCAT44(local_4c._4_4_,uVar8);
    thunk_FUN_ffff0000b800758c(param_1[1]);
    thunk_FUN_ffff0000b800758c(param_1[5],param_1[2]);
    lVar9 = *param_1;
    do {
    } while ((*(uint *)(lVar9 + 0x100) & 0xff) < 0x15);
    lVar5 = param_1[2];
    *(uint *)(lVar9 + 0x104) = local_4c._4_4_;
    *(uint *)(lVar9 + 0x104) = (uint)local_4c;
    *(undefined4 *)(lVar9 + 0x104) = local_50;
    if ((local_4c._2_1_ >> 6 & 1) == 0) {
      iVar4 = 7;
    }
    else {
      iVar4 = 8;
      *(uint *)(lVar9 + 0x104) = local_54;
    }
    *(int *)(lVar9 + 0x104) = (int)uVar1;
    *(int *)(lVar9 + 0x104) = (int)lVar5;
    *(int *)(lVar9 + 0x104) = (int)uVar2;
    *(int *)(lVar9 + 0x104) = (int)(lVar5 + uVar2) + -1;
    if ((char)local_4c._2_1_ < '\0') {
      iVar4 = iVar4 + 1;
      *(undefined4 *)(lVar9 + 0x104) = 0;
    }
    if ((local_4c._4_4_ >> 6 & 1) != 0) {
      puVar6 = (uint *)param_1[3];
      iVar4 = iVar4 + 4;
      uVar3 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      *(uint *)(lVar9 + 0x104) = uVar3 >> 0x10 | uVar3 << 0x10;
      uVar3 = (puVar6[1] & 0xff00ff00) >> 8 | (puVar6[1] & 0xff00ff) << 8;
      *(uint *)(lVar9 + 0x104) = uVar3 >> 0x10 | uVar3 << 0x10;
      uVar3 = (puVar6[2] & 0xff00ff00) >> 8 | (puVar6[2] & 0xff00ff) << 8;
      *(uint *)(lVar9 + 0x104) = uVar3 >> 0x10 | uVar3 << 0x10;
      uVar3 = (puVar6[3] & 0xff00ff00) >> 8 | (puVar6[3] & 0xff00ff) << 8;
      *(uint *)(lVar9 + 0x104) = uVar3 >> 0x10 | uVar3 << 0x10;
    }
    if ((local_4c._4_4_ >> 7 & 1) != 0) {
      uVar3 = 0;
      puVar6 = (uint *)param_1[7];
      iVar7 = 4;
      if ((local_4c._4_4_ >> 0x1e != 0) && ((local_4c._4_4_ & 0x780000) == 0x80000)) {
        if (local_4c._4_4_ >> 0x1e == 2) {
          uVar8 = 0;
          uVar3 = 0;
          *(undefined4 *)(lVar9 + 0x104) = 0;
        }
        else {
          uVar3 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
          *(uint *)(lVar9 + 0x104) = uVar3 >> 0x10 | uVar3 << 0x10;
          uVar3 = (puVar6[1] & 0xff00ff00) >> 8 | (puVar6[1] & 0xff00ff) << 8;
          uVar8 = uVar3 >> 0x10 | uVar3 << 0x10;
          uVar3 = 8;
        }
        *(uint *)(lVar9 + 0x104) = uVar8;
        uVar8 = *(uint *)((long)puVar6 + (ulong)uVar3);
        uVar8 = (uVar8 & 0xff00ff00) >> 8 | (uVar8 & 0xff00ff) << 8;
        *(uint *)(lVar9 + 0x104) = uVar8 >> 0x10 | uVar8 << 0x10;
        uVar8 = *(uint *)((long)puVar6 + ((ulong)uVar3 | 4));
        uVar8 = (uVar8 & 0xff00ff00) >> 8 | (uVar8 & 0xff00ff) << 8;
        uVar3 = uVar3 + 8;
        iVar7 = 8;
        *(uint *)(lVar9 + 0x104) = uVar8 >> 0x10 | uVar8 << 0x10;
      }
      puVar6 = (uint *)((long)puVar6 + (ulong)uVar3);
      iVar4 = iVar7 + iVar4;
      uVar3 = (*puVar6 & 0xff00ff00) >> 8 | (*puVar6 & 0xff00ff) << 8;
      *(uint *)(lVar9 + 0x104) = uVar3 >> 0x10 | uVar3 << 0x10;
      uVar3 = (puVar6[1] & 0xff00ff00) >> 8 | (puVar6[1] & 0xff00ff) << 8;
      *(uint *)(lVar9 + 0x104) = uVar3 >> 0x10 | uVar3 << 0x10;
      uVar3 = (puVar6[2] & 0xff00ff00) >> 8 | (puVar6[2] & 0xff00ff) << 8;
      *(uint *)(lVar9 + 0x104) = uVar3 >> 0x10 | uVar3 << 0x10;
      uVar3 = (puVar6[3] & 0xff00ff00) >> 8 | (puVar6[3] & 0xff00ff) << 8;
      *(uint *)(lVar9 + 0x104) = uVar3 >> 0x10 | uVar3 << 0x10;
    }
    if (((local_4c._2_1_ >> 6 & 1) != 0) && (((byte)local_54 >> 5 & 1) != 0)) {
      iVar4 = iVar4 + 7;
      *(int *)(lVar9 + 0x104) = (int)uVar10;
      *(uint *)(lVar9 + 0x104) = (uint)((ulong)lVar5 >> 0x20) & 0xf;
      *(uint *)(lVar9 + 0x104) = (uint)(uVar2 >> 0x20) & 0xf;
      *(uint *)(lVar9 + 0x104) = (uint)(lVar5 + uVar2 + -1 >> 0x20) & 0xf;
      *(undefined4 *)(lVar9 + 0x104) = 0;
      *(undefined4 *)(lVar9 + 0x104) = 0;
      *(undefined4 *)(lVar9 + 0x104) = 0;
    }
    *(uint *)(lVar9 + 0x100) = (*(uint *)(lVar9 + 0x100) & 0xff) - iVar4;
    local_44 = FUN_ffff0000b80740dc(lVar9,0,local_4c & 0xffff);
    if (local_44 != 0) {
      return local_44;
    }
    thunk_FUN_ffff0000b80075cc(param_1[5],param_1[2]);
  }
  return 0;
}




/* WARNING: Type propagation algorithm not settling */

void FUN_ffff0000b805c9b0(long param_1,long param_2)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  int iVar9;
  uint uVar10;
  uint uVar11;
  uint uVar12;
  long lVar13;
  undefined8 local_168;
  undefined8 local_160;
  undefined8 local_158;
  undefined8 local_150;
  undefined8 local_148;
  undefined8 local_140;
  undefined8 local_138;
  uint local_130;
  uint local_12c;
  int local_128 [50];
  
  iVar2 = *(int *)(param_1 + 0x14);
  uVar4 = *(uint *)(param_1 + 0x18);
  uVar3 = *(uint *)(param_1 + 8);
  uVar5 = *(uint *)(param_1 + 0xc);
  uVar6 = *(uint *)(param_1 + 0x10);
  local_128[0x2c] = 0;
  local_128[0x2d] = 0;
  local_128[0x2e] = 0;
  local_128[0x2f] = 0;
  uVar7 = *(uint *)(param_1 + 0x1c);
  local_128[0x28] = 0;
  local_128[0x29] = 0;
  local_128[0x2a] = 0;
  local_128[0x2b] = 0;
  local_128[0x24] = 0;
  local_128[0x25] = 0;
  local_128[0x26] = 0;
  local_128[0x27] = 0;
  uVar8 = *(uint *)(param_1 + 0x20);
  local_128[0x20] = 0;
  local_128[0x21] = 0;
  local_128[0x22] = 0;
  local_128[0x23] = 0;
  local_128[0x1c] = 0;
  local_128[0x1d] = 0;
  local_128[0x1e] = 0;
  local_128[0x1f] = 0;
  iVar9 = *(int *)(param_1 + 0x24);
  local_128[0x18] = 0;
  local_128[0x19] = 0;
  local_128[0x1a] = 0;
  local_128[0x1b] = 0;
  local_128[0x14] = 0;
  local_128[0x15] = 0;
  local_128[0x16] = 0;
  local_128[0x17] = 0;
  local_128[0x10] = 0;
  local_128[0x11] = 0;
  local_128[0x12] = 0;
  local_128[0x13] = 0;
  local_128[0xc] = 0;
  local_128[0xd] = 0;
  local_128[0xe] = 0;
  local_128[0xf] = 0;
  local_128[8] = 0;
  local_128[9] = 0;
  local_128[10] = 0;
  local_128[0xb] = 0;
  local_128[4] = 0;
  local_128[5] = 0;
  local_128[6] = 0;
  local_128[7] = 0;
  local_128[0] = 0;
  local_128[1] = 0;
  local_128[2] = 0;
  local_128[3] = 0;
  local_138 = 0;
  _local_130 = 0;
  local_148 = 0;
  local_140 = 0;
  local_158 = 0;
  local_150 = 0;
  local_168 = 0;
  local_160 = 0;
  FUN_ffff0000b804cd40(&local_168,param_2,4);
  uVar12 = ((uint)local_168 & 0xff00ff00) >> 8 | ((uint)local_168 & 0xff00ff) << 8;
  local_168._0_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40((ulong)&local_168 | 4,param_2 + 4,4);
  uVar12 = (local_168._4_4_ & 0xff00ff00) >> 8 | (local_168._4_4_ & 0xff00ff) << 8;
  local_168._4_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40(&local_160,param_2 + 8,4);
  uVar12 = ((uint)local_160 & 0xff00ff00) >> 8 | ((uint)local_160 & 0xff00ff) << 8;
  local_160._0_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40((long)&local_160 + 4,param_2 + 0xc,4);
  uVar12 = (local_160._4_4_ & 0xff00ff00) >> 8 | (local_160._4_4_ & 0xff00ff) << 8;
  local_160._4_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40(&local_158,param_2 + 0x10,4);
  uVar12 = ((uint)local_158 & 0xff00ff00) >> 8 | ((uint)local_158 & 0xff00ff) << 8;
  local_158._0_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40((long)&local_158 + 4,param_2 + 0x14,4);
  uVar12 = (local_158._4_4_ & 0xff00ff00) >> 8 | (local_158._4_4_ & 0xff00ff) << 8;
  local_158._4_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40(&local_150,param_2 + 0x18,4);
  uVar12 = ((uint)local_150 & 0xff00ff00) >> 8 | ((uint)local_150 & 0xff00ff) << 8;
  local_150._0_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40((long)&local_150 + 4,param_2 + 0x1c,4);
  uVar12 = (local_150._4_4_ & 0xff00ff00) >> 8 | (local_150._4_4_ & 0xff00ff) << 8;
  local_150._4_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40(&local_148,param_2 + 0x20,4);
  uVar12 = ((uint)local_148 & 0xff00ff00) >> 8 | ((uint)local_148 & 0xff00ff) << 8;
  local_148._0_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40((long)&local_148 + 4,param_2 + 0x24,4);
  uVar12 = (local_148._4_4_ & 0xff00ff00) >> 8 | (local_148._4_4_ & 0xff00ff) << 8;
  local_148._4_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40(&local_140,param_2 + 0x28,4);
  uVar12 = ((uint)local_140 & 0xff00ff00) >> 8 | ((uint)local_140 & 0xff00ff) << 8;
  local_140._0_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40((long)&local_140 + 4,param_2 + 0x2c,4);
  uVar12 = (local_140._4_4_ & 0xff00ff00) >> 8 | (local_140._4_4_ & 0xff00ff) << 8;
  local_140._4_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40(&local_138,param_2 + 0x30,4);
  uVar12 = ((uint)local_138 & 0xff00ff00) >> 8 | ((uint)local_138 & 0xff00ff) << 8;
  local_138._0_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40((long)&local_138 + 4,param_2 + 0x34,4);
  uVar12 = (local_138._4_4_ & 0xff00ff00) >> 8 | (local_138._4_4_ & 0xff00ff) << 8;
  local_138._4_4_ = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40(&local_130,param_2 + 0x38,4);
  uVar12 = (local_130 & 0xff00ff00) >> 8 | (local_130 & 0xff00ff) << 8;
  local_130 = uVar12 >> 0x10 | uVar12 << 0x10;
  FUN_ffff0000b804cd40(&local_12c,param_2 + 0x3c,4);
  uVar12 = (local_12c & 0xff00ff00) >> 8 | (local_12c & 0xff00ff) << 8;
  local_12c = uVar12 >> 0x10 | uVar12 << 0x10;
  lVar13 = 0;
  uVar12 = (uint)local_168;
  do {
    lVar1 = lVar13 + 4;
    uVar10 = *(uint *)((long)&local_130 + lVar13);
    uVar11 = *(uint *)((long)&local_168 + lVar13 + 4);
    *(uint *)((long)local_128 + lVar13) =
         *(int *)((long)&local_148 + lVar13 + 4) + uVar12 +
         ((uVar10 >> 0x11 | uVar10 << 0xf) ^ (uVar10 >> 0x13 | uVar10 << 0xd) ^ uVar10 >> 10) +
         ((uVar11 >> 7 | uVar11 << 0x19) ^ (uVar11 >> 0x12 | uVar11 << 0xe) ^ uVar11 >> 3);
    lVar13 = lVar1;
    uVar12 = uVar11;
  } while (lVar1 != 0xc0);
  iVar9 = iVar9 + ((uVar4 >> 6 | uVar4 << 0x1a) ^ (uVar4 >> 0xb | uVar4 << 0x15) ^
                  (uVar4 >> 0x19 | uVar4 << 7)) + (uVar7 & uVar4 | uVar8 & (uVar4 ^ 0xffffffff)) +
          (uint)local_168 + 0x428a2f98;
  uVar12 = iVar9 + iVar2;
  uVar10 = ((uVar5 | uVar3) & uVar6 | uVar5 & uVar3) +
           ((uVar3 >> 2 | uVar3 << 0x1e) ^ (uVar3 >> 0xd | uVar3 << 0x13) ^
           (uVar3 >> 0x16 | uVar3 << 10)) + iVar9;
  iVar2 = uVar8 + (uVar4 & uVar12 | uVar7 & (uVar12 ^ 0xffffffff)) +
          ((uVar12 >> 6 | uVar12 * 0x4000000) ^ (uVar12 >> 0xb | uVar12 * 0x200000) ^
          (uVar12 >> 0x19 | uVar12 * 0x80)) + local_168._4_4_ + 0x71374491;
  uVar6 = iVar2 + uVar6;
  uVar8 = ((uVar10 >> 2 | uVar10 * 0x40000000) ^ (uVar10 >> 0xd | uVar10 * 0x80000) ^
          (uVar10 >> 0x16 | uVar10 * 0x400)) + ((uVar10 | uVar3) & uVar5 | uVar10 & uVar3) + iVar2;
  iVar2 = uVar7 + (uint)local_160 + (uVar12 & uVar6 | uVar4 & (uVar6 ^ 0xffffffff)) +
          ((uVar6 >> 6 | uVar6 * 0x4000000) ^ (uVar6 >> 0xb | uVar6 * 0x200000) ^
          (uVar6 >> 0x19 | uVar6 * 0x80)) + -0x4a3f0431;
  uVar5 = iVar2 + uVar5;
  uVar7 = ((uVar8 >> 2 | uVar8 * 0x40000000) ^ (uVar8 >> 0xd | uVar8 * 0x80000) ^
          (uVar8 >> 0x16 | uVar8 * 0x400)) + ((uVar8 | uVar10) & uVar3 | uVar8 & uVar10) + iVar2;
  iVar2 = uVar4 + local_160._4_4_ + (uVar6 & uVar5 | uVar12 & (uVar5 ^ 0xffffffff)) +
          ((uVar5 >> 6 | uVar5 * 0x4000000) ^ (uVar5 >> 0xb | uVar5 * 0x200000) ^
          (uVar5 >> 0x19 | uVar5 * 0x80)) + -0x164a245b;
  uVar3 = iVar2 + uVar3;
  uVar4 = ((uVar7 >> 2 | uVar7 * 0x40000000) ^ (uVar7 >> 0xd | uVar7 * 0x80000) ^
          (uVar7 >> 0x16 | uVar7 * 0x400)) + ((uVar7 | uVar8) & uVar10 | uVar7 & uVar8) + iVar2;
  iVar2 = uVar12 + (uint)local_158 + (uVar5 & uVar3 | uVar6 & (uVar3 ^ 0xffffffff)) +
          ((uVar3 >> 6 | uVar3 * 0x4000000) ^ (uVar3 >> 0xb | uVar3 * 0x200000) ^
          (uVar3 >> 0x19 | uVar3 * 0x80)) + 0x3956c25b;
  uVar10 = iVar2 + uVar10;
  uVar12 = ((uVar4 >> 2 | uVar4 * 0x40000000) ^ (uVar4 >> 0xd | uVar4 * 0x80000) ^
           (uVar4 >> 0x16 | uVar4 * 0x400)) + ((uVar4 | uVar7) & uVar8 | uVar4 & uVar7) + iVar2;
  iVar2 = uVar6 + local_158._4_4_ + (uVar3 & uVar10 | uVar5 & (uVar10 ^ 0xffffffff)) +
          ((uVar10 >> 6 | uVar10 * 0x4000000) ^ (uVar10 >> 0xb | uVar10 * 0x200000) ^
          (uVar10 >> 0x19 | uVar10 * 0x80)) + 0x59f111f1;
  uVar8 = iVar2 + uVar8;
  uVar6 = ((uVar12 >> 2 | uVar12 * 0x40000000) ^ (uVar12 >> 0xd | uVar12 * 0x80000) ^
          (uVar12 >> 0x16 | uVar12 * 0x400)) + ((uVar12 | uVar4) & uVar7 | uVar12 & uVar4) + iVar2;
  iVar2 = (uint)local_150 + uVar5 + (uVar10 & uVar8 | uVar3 & (uVar8 ^ 0xffffffff)) +
          ((uVar8 >> 6 | uVar8 * 0x4000000) ^ (uVar8 >> 0xb | uVar8 * 0x200000) ^
          (uVar8 >> 0x19 | uVar8 * 0x80)) + -0x6dc07d5c;
  uVar7 = iVar2 + uVar7;
  uVar5 = ((uVar6 >> 2 | uVar6 * 0x40000000) ^ (uVar6 >> 0xd | uVar6 * 0x80000) ^
          (uVar6 >> 0x16 | uVar6 * 0x400)) + ((uVar6 | uVar12) & uVar4 | uVar6 & uVar12) + iVar2;
  iVar2 = local_150._4_4_ + uVar3 + (uVar8 & uVar7 | uVar10 & (uVar7 ^ 0xffffffff)) +
          ((uVar7 >> 6 | uVar7 * 0x4000000) ^ (uVar7 >> 0xb | uVar7 * 0x200000) ^
          (uVar7 >> 0x19 | uVar7 * 0x80)) + -0x54e3a12b;
  uVar4 = iVar2 + uVar4;
  uVar3 = ((uVar5 >> 2 | uVar5 * 0x40000000) ^ (uVar5 >> 0xd | uVar5 * 0x80000) ^
          (uVar5 >> 0x16 | uVar5 * 0x400)) + ((uVar5 | uVar6) & uVar12 | uVar5 & uVar6) + iVar2;
  iVar2 = (uint)local_148 + uVar10 + (uVar7 & uVar4 | uVar8 & (uVar4 ^ 0xffffffff)) +
          ((uVar4 >> 6 | uVar4 * 0x4000000) ^ (uVar4 >> 0xb | uVar4 * 0x200000) ^
          (uVar4 >> 0x19 | uVar4 * 0x80)) + -0x27f85568;
  uVar12 = iVar2 + uVar12;
  uVar10 = ((uVar3 >> 2 | uVar3 * 0x40000000) ^ (uVar3 >> 0xd | uVar3 * 0x80000) ^
           (uVar3 >> 0x16 | uVar3 * 0x400)) + ((uVar3 | uVar5) & uVar6 | uVar3 & uVar5) + iVar2;
  iVar2 = local_148._4_4_ + uVar8 + (uVar4 & uVar12 | uVar7 & (uVar12 ^ 0xffffffff)) +
          ((uVar12 >> 6 | uVar12 * 0x4000000) ^ (uVar12 >> 0xb | uVar12 * 0x200000) ^
          (uVar12 >> 0x19 | uVar12 * 0x80)) + 0x12835b01;
  uVar6 = iVar2 + uVar6;
  uVar8 = ((uVar10 >> 2 | uVar10 * 0x40000000) ^ (uVar10 >> 0xd | uVar10 * 0x80000) ^
          (uVar10 >> 0x16 | uVar10 * 0x400)) + ((uVar10 | uVar3) & uVar5 | uVar10 & uVar3) + iVar2;
  iVar2 = (uint)local_140 + uVar7 + (uVar12 & uVar6 | uVar4 & (uVar6 ^ 0xffffffff)) +
          ((uVar6 >> 6 | uVar6 * 0x4000000) ^ (uVar6 >> 0xb | uVar6 * 0x200000) ^
          (uVar6 >> 0x19 | uVar6 * 0x80)) + 0x243185be;
  uVar5 = iVar2 + uVar5;
  uVar7 = ((uVar8 >> 2 | uVar8 * 0x40000000) ^ (uVar8 >> 0xd | uVar8 * 0x80000) ^
          (uVar8 >> 0x16 | uVar8 * 0x400)) + ((uVar8 | uVar10) & uVar3 | uVar8 & uVar10) + iVar2;
  iVar2 = local_140._4_4_ + uVar4 + (uVar6 & uVar5 | uVar12 & (uVar5 ^ 0xffffffff)) +
          ((uVar5 >> 6 | uVar5 * 0x4000000) ^ (uVar5 >> 0xb | uVar5 * 0x200000) ^
          (uVar5 >> 0x19 | uVar5 * 0x80)) + 0x550c7dc3;
  uVar3 = iVar2 + uVar3;
  uVar4 = ((uVar7 >> 2 | uVar7 * 0x40000000) ^ (uVar7 >> 0xd | uVar7 * 0x80000) ^
          (uVar7 >> 0x16 | uVar7 * 0x400)) + ((uVar7 | uVar8) & uVar10 | uVar7 & uVar8) + iVar2;
  iVar2 = (uint)local_138 + uVar12 + (uVar5 & uVar3 | uVar6 & (uVar3 ^ 0xffffffff)) +
          ((uVar3 >> 6 | uVar3 * 0x4000000) ^ (uVar3 >> 0xb | uVar3 * 0x200000) ^
          (uVar3 >> 0x19 | uVar3 * 0x80)) + 0x72be5d74;
  uVar10 = iVar2 + uVar10;
  uVar12 = ((uVar4 >> 2 | uVar4 * 0x40000000) ^ (uVar4 >> 0xd | uVar4 * 0x80000) ^
           (uVar4 >> 0x16 | uVar4 * 0x400)) + ((uVar4 | uVar7) & uVar8 | uVar4 & uVar7) + iVar2;
  iVar2 = local_138._4_4_ + uVar6 + (uVar3 & uVar10 | uVar5 & (uVar10 ^ 0xffffffff)) +
          ((uVar10 >> 6 | uVar10 * 0x4000000) ^ (uVar10 >> 0xb | uVar10 * 0x200000) ^
          (uVar10 >> 0x19 | uVar10 * 0x80)) + -0x7f214e02;
  uVar8 = iVar2 + uVar8;
  uVar6 = ((uVar12 >> 2 | uVar12 * 0x40000000) ^ (uVar12 >> 0xd | uVar12 * 0x80000) ^
          (uVar12 >> 0x16 | uVar12 * 0x400)) + ((uVar12 | uVar4) & uVar7 | uVar12 & uVar4) + iVar2;
  iVar2 = local_130 + uVar5 + (uVar10 & uVar8 | uVar3 & (uVar8 ^ 0xffffffff)) +
          ((uVar8 >> 6 | uVar8 * 0x4000000) ^ (uVar8 >> 0xb | uVar8 * 0x200000) ^
          (uVar8 >> 0x19 | uVar8 * 0x80)) + -0x6423f959;
  uVar7 = iVar2 + uVar7;
  uVar5 = ((uVar6 >> 2 | uVar6 * 0x40000000) ^ (uVar6 >> 0xd | uVar6 * 0x80000) ^
          (uVar6 >> 0x16 | uVar6 * 0x400)) + ((uVar6 | uVar12) & uVar4 | uVar6 & uVar12) + iVar2;
  iVar2 = local_12c + uVar3 + (uVar8 & uVar7 | uVar10 & (uVar7 ^ 0xffffffff)) +
          ((uVar7 >> 6 | uVar7 * 0x4000000) ^ (uVar7 >> 0xb | uVar7 * 0x200000) ^
          (uVar7 >> 0x19 | uVar7 * 0x80)) + -0x3e640e8c;
  uVar4 = iVar2 + uVar4;
  uVar3 = ((uVar5 >> 2 | uVar5 * 0x40000000) ^ (uVar5 >> 0xd | uVar5 * 0x80000) ^
          (uVar5 >> 0x16 | uVar5 * 0x400)) + ((uVar5 | uVar6) & uVar12 | uVar5 & uVar6) + iVar2;
  iVar2 = local_128[0] + uVar10 + (uVar7 & uVar4 | uVar8 & (uVar4 ^ 0xffffffff)) +
          ((uVar4 >> 6 | uVar4 * 0x4000000) ^ (uVar4 >> 0xb | uVar4 * 0x200000) ^
          (uVar4 >> 0x19 | uVar4 * 0x80)) + -0x1b64963f;
  uVar12 = iVar2 + uVar12;
  uVar10 = ((uVar3 >> 2 | uVar3 * 0x40000000) ^ (uVar3 >> 0xd | uVar3 * 0x80000) ^
           (uVar3 >> 0x16 | uVar3 * 0x400)) + ((uVar3 | uVar5) & uVar6 | uVar3 & uVar5) + iVar2;
  iVar2 = local_128[1] + uVar8 + (uVar4 & uVar12 | uVar7 & (uVar12 ^ 0xffffffff)) +
          ((uVar12 >> 6 | uVar12 * 0x4000000) ^ (uVar12 >> 0xb | uVar12 * 0x200000) ^
          (uVar12 >> 0x19 | uVar12 * 0x80)) + -0x1041b87a;
  uVar6 = iVar2 + uVar6;
  uVar8 = ((uVar10 >> 2 | uVar10 * 0x40000000) ^ (uVar10 >> 0xd | uVar10 * 0x80000) ^
          (uVar10 >> 0x16 | uVar10 * 0x400)) + ((uVar10 | uVar3) & uVar5 | uVar10 & uVar3) + iVar2;
  iVar2 = local_128[2] + uVar7 + (uVar12 & uVar6 | uVar4 & (uVar6 ^ 0xffffffff)) +
          ((uVar6 >> 6 | uVar6 * 0x4000000) ^ (uVar6 >> 0xb | uVar6 * 0x200000) ^
          (uVar6 >> 0x19 | uVar6 * 0x80)) + 0xfc19dc6;
  uVar5 = iVar2 + uVar5;
  uVar7 = ((uVar8 >> 2 | uVar8 * 0x40000000) ^ (uVar8 >> 0xd | uVar8 * 0x80000) ^
          (uVar8 >> 0x16 | uVar8 * 0x400)) + ((uVar8 | uVar10) & uVar3 | uVar8 & uVar10) + iVar2;
  iVar2 = local_128[3] + uVar4 + (uVar6 & uVar5 | uVar12 & (uVar5 ^ 0xffffffff)) +
          ((uVar5 >> 6 | uVar5 * 0x4000000) ^ (uVar5 >> 0xb | uVar5 * 0x200000) ^
          (uVar5 >> 0x19 | uVar5 * 0x80)) + 0x240ca1cc;
  uVar3 = iVar2 + uVar3;
  uVar4 = ((uVar7 >> 2 | uVar7 * 0x40000000) ^ (uVar7 >> 0xd | uVar7 * 0x80000) ^
          (uVar7 >> 0x16 | uVar7 * 0x400)) + ((uVar7 | uVar8) & uVar10 | uVar7 & uVar8) + iVar2;
  iVar2 = local_128[4] + uVar12 + (uVar5 & uVar3 | uVar6 & (uVar3 ^ 0xffffffff)) +
          ((uVar3 >> 6 | uVar3 * 0x4000000) ^ (uVar3 >> 0xb | uVar3 * 0x200000) ^
          (uVar3 >> 0x19 | uVar3 * 0x80)) + 0x2de92c6f;
  uVar10 = iVar2 + uVar10;
  uVar12 = ((uVar4 >> 2 | uVar4 * 0x40000000) ^ (uVar4 >> 0xd | uVar4 * 0x80000) ^
           (uVar4 >> 0x16 | uVar4 * 0x400)) + ((uVar4 | uVar7) & uVar8 | uVar4 & uVar7) + iVar2;
  iVar2 = local_128[5] + uVar6 + (uVar3 & uVar10 | uVar5 & (uVar10 ^ 0xffffffff)) +
          ((uVar10 >> 6 | uVar10 * 0x4000000) ^ (uVar10 >> 0xb | uVar10 * 0x200000) ^
          (uVar10 >> 0x19 | uVar10 * 0x80)) + 0x4a7484aa;
  uVar8 = iVar2 + uVar8;
  uVar6 = ((uVar12 >> 2 | uVar12 * 0x40000000) ^ (uVar12 >> 0xd | uVar12 * 0x80000) ^
          (uVar12 >> 0x16 | uVar12 * 0x400)) + ((uVar12 | uVar4) & uVar7 | uVar12 & uVar4) + iVar2;
  iVar2 = local_128[6] + uVar5 + (uVar10 & uVar8 | uVar3 & (uVar8 ^ 0xffffffff)) +
          ((uVar8 >> 6 | uVar8 * 0x4000000) ^ (uVar8 >> 0xb | uVar8 * 0x200000) ^
          (uVar8 >> 0x19 | uVar8 * 0x80)) + 0x5cb0a9dc;
  uVar7 = iVar2 + uVar7;
  uVar5 = ((uVar6 >> 2 | uVar6 * 0x40000000) ^ (uVar6 >> 0xd | uVar6 * 0x80000) ^
          (uVar6 >> 0x16 | uVar6 * 0x400)) + ((uVar6 | uVar12) & uVar4 | uVar6 & uVar12) + iVar2;
  iVar2 = local_128[7] + uVar3 + (uVar8 & uVar7 | uVar10 & (uVar7 ^ 0xffffffff)) +
          ((uVar7 >> 6 | uVar7 * 0x4000000) ^ (uVar7 >> 0xb | uVar7 * 0x200000) ^
          (uVar7 >> 0x19 | uVar7 * 0x80)) + 0x76f988da;
  uVar4 = iVar2 + uVar4;
  uVar3 = ((uVar5 >> 2 | uVar5 * 0x40000000) ^ (uVar5 >> 0xd | uVar5 * 0x80000) ^
          (uVar5 >> 0x16 | uVar5 * 0x400)) + ((uVar5 | uVar6) & uVar12 | uVar5 & uVar6) + iVar2;
  iVar2 = local_128[8] + uVar10 + (uVar7 & uVar4 | uVar8 & (uVar4 ^ 0xffffffff)) +
          ((uVar4 >> 6 | uVar4 * 0x4000000) ^ (uVar4 >> 0xb | uVar4 * 0x200000) ^
          (uVar4 >> 0x19 | uVar4 * 0x80)) + -0x67c1aeae;
  uVar12 = iVar2 + uVar12;
  uVar10 = ((uVar3 >> 2 | uVar3 * 0x40000000) ^ (uVar3 >> 0xd | uVar3 * 0x80000) ^
           (uVar3 >> 0x16 | uVar3 * 0x400)) + ((uVar3 | uVar5) & uVar6 | uVar3 & uVar5) + iVar2;
  iVar2 = local_128[9] + uVar8 + (uVar4 & uVar12 | uVar7 & (uVar12 ^ 0xffffffff)) +
          ((uVar12 >> 6 | uVar12 * 0x4000000) ^ (uVar12 >> 0xb | uVar12 * 0x200000) ^
          (uVar12 >> 0x19 | uVar12 * 0x80)) + -0x57ce3993;
  uVar6 = iVar2 + uVar6;
  uVar8 = ((uVar10 >> 2 | uVar10 * 0x40000000) ^ (uVar10 >> 0xd | uVar10 * 0x80000) ^
          (uVar10 >> 0x16 | uVar10 * 0x400)) + ((uVar10 | uVar3) & uVar5 | uVar10 & uVar3) + iVar2;
  iVar2 = local_128[10] + uVar7 + (uVar12 & uVar6 | uVar4 & (uVar6 ^ 0xffffffff)) +
          ((uVar6 >> 6 | uVar6 * 0x4000000) ^ (uVar6 >> 0xb | uVar6 * 0x200000) ^
          (uVar6 >> 0x19 | uVar6 * 0x80)) + -0x4ffcd838;
  uVar5 = iVar2 + uVar5;
  uVar7 = ((uVar8 >> 2 | uVar8 * 0x40000000) ^ (uVar8 >> 0xd | uVar8 * 0x80000) ^
          (uVar8 >> 0x16 | uVar8 * 0x400)) + ((uVar8 | uVar10) & uVar3 | uVar8 & uVar10) + iVar2;
  iVar2 = local_128[0xb] + uVar4 + (uVar6 & uVar5 | uVar12 & (uVar5 ^ 0xffffffff)) +
          ((uVar5 >> 6 | uVar5 * 0x4000000) ^ (uVar5 >> 0xb | uVar5 * 0x200000) ^
          (uVar5 >> 0x19 | uVar5 * 0x80)) + -0x40a68039;
  uVar3 = iVar2 + uVar3;
  uVar4 = ((uVar7 >> 2 | uVar7 * 0x40000000) ^ (uVar7 >> 0xd | uVar7 * 0x80000) ^
          (uVar7 >> 0x16 | uVar7 * 0x400)) + ((uVar7 | uVar8) & uVar10 | uVar7 & uVar8) + iVar2;
  iVar2 = local_128[0xc] + uVar12 + (uVar5 & uVar3 | uVar6 & (uVar3 ^ 0xffffffff)) +
          ((uVar3 >> 6 | uVar3 * 0x4000000) ^ (uVar3 >> 0xb | uVar3 * 0x200000) ^
          (uVar3 >> 0x19 | uVar3 * 0x80)) + -0x391ff40d;
  uVar10 = iVar2 + uVar10;
  uVar12 = ((uVar4 >> 2 | uVar4 * 0x40000000) ^ (uVar4 >> 0xd | uVar4 * 0x80000) ^
           (uVar4 >> 0x16 | uVar4 * 0x400)) + ((uVar4 | uVar7) & uVar8 | uVar4 & uVar7) + iVar2;
  iVar2 = local_128[0xd] + uVar6 + (uVar3 & uVar10 | uVar5 & (uVar10 ^ 0xffffffff)) +
          ((uVar10 >> 6 | uVar10 * 0x4000000) ^ (uVar10 >> 0xb | uVar10 * 0x200000) ^
          (uVar10 >> 0x19 | uVar10 * 0x80)) + -0x2a586eb9;
  uVar8 = iVar2 + uVar8;
  uVar6 = ((uVar12 >> 2 | uVar12 * 0x40000000) ^ (uVar12 >> 0xd | uVar12 * 0x80000) ^
          (uVar12 >> 0x16 | uVar12 * 0x400)) + ((uVar12 | uVar4) & uVar7 | uVar12 & uVar4) + iVar2;
  iVar2 = local_128[0xe] + uVar5 + (uVar10 & uVar8 | uVar3 & (uVar8 ^ 0xffffffff)) +
          ((uVar8 >> 6 | uVar8 * 0x4000000) ^ (uVar8 >> 0xb | uVar8 * 0x200000) ^
          (uVar8 >> 0x19 | uVar8 * 0x80)) + 0x6ca6351;
  uVar7 = iVar2 + uVar7;
  uVar5 = ((uVar6 >> 2 | uVar6 * 0x40000000) ^ (uVar6 >> 0xd | uVar6 * 0x80000) ^
          (uVar6 >> 0x16 | uVar6 * 0x400)) + ((uVar6 | uVar12) & uVar4 | uVar6 & uVar12) + iVar2;
  iVar2 = local_128[0xf] + uVar3 + (uVar8 & uVar7 | uVar10 & (uVar7 ^ 0xffffffff)) +
          ((uVar7 >> 6 | uVar7 * 0x4000000) ^ (uVar7 >> 0xb | uVar7 * 0x200000) ^
          (uVar7 >> 0x19 | uVar7 * 0x80)) + 0x14292967;
  uVar4 = iVar2 + uVar4;
  uVar3 = ((uVar5 >> 2 | uVar5 * 0x40000000) ^ (uVar5 >> 0xd | uVar5 * 0x80000) ^
          (uVar5 >> 0x16 | uVar5 * 0x400)) + ((uVar5 | uVar6) & uVar12 | uVar5 & uVar6) + iVar2;
  iVar2 = local_128[0x10] + uVar10 + (uVar7 & uVar4 | uVar8 & (uVar4 ^ 0xffffffff)) +
          ((uVar4 >> 6 | uVar4 * 0x4000000) ^ (uVar4 >> 0xb | uVar4 * 0x200000) ^
          (uVar4 >> 0x19 | uVar4 * 0x80)) + 0x27b70a85;
  uVar12 = iVar2 + uVar12;
  uVar10 = ((uVar3 >> 2 | uVar3 * 0x40000000) ^ (uVar3 >> 0xd | uVar3 * 0x80000) ^
           (uVar3 >> 0x16 | uVar3 * 0x400)) + ((uVar3 | uVar5) & uVar6 | uVar3 & uVar5) + iVar2;
  iVar2 = local_128[0x11] + uVar8 + (uVar4 & uVar12 | uVar7 & (uVar12 ^ 0xffffffff)) +
          ((uVar12 >> 6 | uVar12 * 0x4000000) ^ (uVar12 >> 0xb | uVar12 * 0x200000) ^
          (uVar12 >> 0x19 | uVar12 * 0x80)) + 0x2e1b2138;
  uVar6 = iVar2 + uVar6;
  uVar8 = ((uVar10 >> 2 | uVar10 * 0x40000000) ^ (uVar10 >> 0xd | uVar10 * 0x80000) ^
          (uVar10 >> 0x16 | uVar10 * 0x400)) + ((uVar10 | uVar3) & uVar5 | uVar10 & uVar3) + iVar2;
  iVar2 = local_128[0x12] + uVar7 + (uVar12 & uVar6 | uVar4 & (uVar6 ^ 0xffffffff)) +
          ((uVar6 >> 6 | uVar6 * 0x4000000) ^ (uVar6 >> 0xb | uVar6 * 0x200000) ^
          (uVar6 >> 0x19 | uVar6 * 0x80)) + 0x4d2c6dfc;
  uVar5 = iVar2 + uVar5;
  uVar7 = ((uVar8 >> 2 | uVar8 * 0x40000000) ^ (uVar8 >> 0xd | uVar8 * 0x80000) ^
          (uVar8 >> 0x16 | uVar8 * 0x400)) + ((uVar8 | uVar10) & uVar3 | uVar8 & uVar10) + iVar2;
  iVar2 = local_128[0x13] + uVar4 + (uVar6 & uVar5 | uVar12 & (uVar5 ^ 0xffffffff)) +
          ((uVar5 >> 6 | uVar5 * 0x4000000) ^ (uVar5 >> 0xb | uVar5 * 0x200000) ^
          (uVar5 >> 0x19 | uVar5 * 0x80)) + 0x53380d13;
  uVar3 = iVar2 + uVar3;
  uVar4 = ((uVar7 >> 2 | uVar7 * 0x40000000) ^ (uVar7 >> 0xd | uVar7 * 0x80000) ^
          (uVar7 >> 0x16 | uVar7 * 0x400)) + ((uVar7 | uVar8) & uVar10 | uVar7 & uVar8) + iVar2;
  iVar2 = local_128[0x14] + uVar12 + (uVar5 & uVar3 | uVar6 & (uVar3 ^ 0xffffffff)) +
          ((uVar3 >> 6 | uVar3 * 0x4000000) ^ (uVar3 >> 0xb | uVar3 * 0x200000) ^
          (uVar3 >> 0x19 | uVar3 * 0x80)) + 0x650a7354;
  uVar10 = iVar2 + uVar10;
  uVar12 = ((uVar4 >> 2 | uVar4 * 0x40000000) ^ (uVar4 >> 0xd | uVar4 * 0x80000) ^
           (uVar4 >> 0x16 | uVar4 * 0x400)) + ((uVar4 | uVar7) & uVar8 | uVar4 & uVar7) + iVar2;
  iVar2 = local_128[0x15] + uVar6 + (uVar3 & uVar10 | uVar5 & (uVar10 ^ 0xffffffff)) +
          ((uVar10 >> 6 | uVar10 * 0x4000000) ^ (uVar10 >> 0xb | uVar10 * 0x200000) ^
          (uVar10 >> 0x19 | uVar10 * 0x80)) + 0x766a0abb;
  uVar8 = iVar2 + uVar8;
  uVar6 = ((uVar12 >> 2 | uVar12 * 0x40000000) ^ (uVar12 >> 0xd | uVar12 * 0x80000) ^
          (uVar12 >> 0x16 | uVar12 * 0x400)) + ((uVar12 | uVar4) & uVar7 | uVar12 & uVar4) + iVar2;
  iVar2 = local_128[0x16] + uVar5 + (uVar10 & uVar8 | uVar3 & (uVar8 ^ 0xffffffff)) +
          ((uVar8 >> 6 | uVar8 * 0x4000000) ^ (uVar8 >> 0xb | uVar8 * 0x200000) ^
          (uVar8 >> 0x19 | uVar8 * 0x80)) + -0x7e3d36d2;
  uVar7 = iVar2 + uVar7;
  uVar5 = ((uVar6 >> 2 | uVar6 * 0x40000000) ^ (uVar6 >> 0xd | uVar6 * 0x80000) ^
          (uVar6 >> 0x16 | uVar6 * 0x400)) + ((uVar6 | uVar12) & uVar4 | uVar6 & uVar12) + iVar2;
  iVar2 = local_128[0x17] + uVar3 + (uVar8 & uVar7 | uVar10 & (uVar7 ^ 0xffffffff)) +
          ((uVar7 >> 6 | uVar7 * 0x4000000) ^ (uVar7 >> 0xb | uVar7 * 0x200000) ^
          (uVar7 >> 0x19 | uVar7 * 0x80)) + -0x6d8dd37b;
  uVar4 = iVar2 + uVar4;
  uVar3 = ((uVar5 >> 2 | uVar5 * 0x40000000) ^ (uVar5 >> 0xd | uVar5 * 0x80000) ^
          (uVar5 >> 0x16 | uVar5 * 0x400)) + ((uVar5 | uVar6) & uVar12 | uVar5 & uVar6) + iVar2;
  iVar2 = local_128[0x18] + uVar10 + (uVar7 & uVar4 | uVar8 & (uVar4 ^ 0xffffffff)) +
          ((uVar4 >> 6 | uVar4 * 0x4000000) ^ (uVar4 >> 0xb | uVar4 * 0x200000) ^
          (uVar4 >> 0x19 | uVar4 * 0x80)) + -0x5d40175f;
  uVar12 = iVar2 + uVar12;
  uVar10 = ((uVar3 >> 2 | uVar3 * 0x40000000) ^ (uVar3 >> 0xd | uVar3 * 0x80000) ^
           (uVar3 >> 0x16 | uVar3 * 0x400)) + ((uVar3 | uVar5) & uVar6 | uVar3 & uVar5) + iVar2;
  iVar2 = local_128[0x19] + uVar8 + (uVar4 & uVar12 | uVar7 & (uVar12 ^ 0xffffffff)) +
          ((uVar12 >> 6 | uVar12 * 0x4000000) ^ (uVar12 >> 0xb | uVar12 * 0x200000) ^
          (uVar12 >> 0x19 | uVar12 * 0x80)) + -0x57e599b5;
  uVar6 = iVar2 + uVar6;
  uVar8 = ((uVar10 >> 2 | uVar10 * 0x40000000) ^ (uVar10 >> 0xd | uVar10 * 0x80000) ^
          (uVar10 >> 0x16 | uVar10 * 0x400)) + ((uVar10 | uVar3) & uVar5 | uVar10 & uVar3) + iVar2;
  iVar2 = local_128[0x1a] + uVar7 + (uVar12 & uVar6 | uVar4 & (uVar6 ^ 0xffffffff)) +
          ((uVar6 >> 6 | uVar6 * 0x4000000) ^ (uVar6 >> 0xb | uVar6 * 0x200000) ^
          (uVar6 >> 0x19 | uVar6 * 0x80)) + -0x3db47490;
  uVar5 = iVar2 + uVar5;
  uVar7 = ((uVar8 >> 2 | uVar8 * 0x40000000) ^ (uVar8 >> 0xd | uVar8 * 0x80000) ^
          (uVar8 >> 0x16 | uVar8 * 0x400)) + ((uVar8 | uVar10) & uVar3 | uVar8 & uVar10) + iVar2;
  iVar2 = local_128[0x1b] + uVar4 + (uVar6 & uVar5 | uVar12 & (uVar5 ^ 0xffffffff)) +
          ((uVar5 >> 6 | uVar5 * 0x4000000) ^ (uVar5 >> 0xb | uVar5 * 0x200000) ^
          (uVar5 >> 0x19 | uVar5 * 0x80)) + -0x3893ae5d;
  uVar3 = iVar2 + uVar3;
  uVar4 = ((uVar7 >> 2 | uVar7 * 0x40000000) ^ (uVar7 >> 0xd | uVar7 * 0x80000) ^
          (uVar7 >> 0x16 | uVar7 * 0x400)) + ((uVar7 | uVar8) & uVar10 | uVar7 & uVar8) + iVar2;
  iVar2 = local_128[0x1c] + uVar12 + (uVar5 & uVar3 | uVar6 & (uVar3 ^ 0xffffffff)) +
          ((uVar3 >> 6 | uVar3 * 0x4000000) ^ (uVar3 >> 0xb | uVar3 * 0x200000) ^
          (uVar3 >> 0x19 | uVar3 * 0x80)) + -0x2e6d17e7;
  uVar10 = iVar2 + uVar10;
  uVar12 = ((uVar4 >> 2 | uVar4 * 0x40000000) ^ (uVar4 >> 0xd | uVar4 * 0x80000) ^
           (uVar4 >> 0x16 | uVar4 * 0x400)) + ((uVar4 | uVar7) & uVar8 | uVar4 & uVar7) + iVar2;
  iVar2 = local_128[0x1d] + uVar6 + (uVar3 & uVar10 | uVar5 & (uVar10 ^ 0xffffffff)) +
          ((uVar10 >> 6 | uVar10 * 0x4000000) ^ (uVar10 >> 0xb | uVar10 * 0x200000) ^
          (uVar10 >> 0x19 | uVar10 * 0x80)) + -0x2966f9dc;
  uVar8 = iVar2 + uVar8;
  uVar6 = ((uVar12 >> 2 | uVar12 * 0x40000000) ^ (uVar12 >> 0xd | uVar12 * 0x80000) ^
          (uVar12 >> 0x16 | uVar12 * 0x400)) + ((uVar12 | uVar4) & uVar7 | uVar12 & uVar4) + iVar2;
  iVar2 = local_128[0x1e] + uVar5 + (uVar10 & uVar8 | uVar3 & (uVar8 ^ 0xffffffff)) +
          ((uVar8 >> 6 | uVar8 * 0x4000000) ^ (uVar8 >> 0xb | uVar8 * 0x200000) ^
          (uVar8 >> 0x19 | uVar8 * 0x80)) + -0xbf1ca7b;
  uVar7 = iVar2 + uVar7;
  uVar5 = ((uVar6 >> 2 | uVar6 * 0x40000000) ^ (uVar6 >> 0xd | uVar6 * 0x80000) ^
          (uVar6 >> 0x16 | uVar6 * 0x400)) + ((uVar6 | uVar12) & uVar4 | uVar6 & uVar12) + iVar2;
  iVar2 = local_128[0x1f] + uVar3 + (uVar8 & uVar7 | uVar10 & (uVar7 ^ 0xffffffff)) +
          ((uVar7 >> 6 | uVar7 * 0x4000000) ^ (uVar7 >> 0xb | uVar7 * 0x200000) ^
          (uVar7 >> 0x19 | uVar7 * 0x80)) + 0x106aa070;
  uVar4 = iVar2 + uVar4;
  uVar3 = ((uVar5 >> 2 | uVar5 * 0x40000000) ^ (uVar5 >> 0xd | uVar5 * 0x80000) ^
          (uVar5 >> 0x16 | uVar5 * 0x400)) + ((uVar5 | uVar6) & uVar12 | uVar5 & uVar6) + iVar2;
  iVar2 = local_128[0x20] + uVar10 + (uVar7 & uVar4 | uVar8 & (uVar4 ^ 0xffffffff)) +
          ((uVar4 >> 6 | uVar4 * 0x4000000) ^ (uVar4 >> 0xb | uVar4 * 0x200000) ^
          (uVar4 >> 0x19 | uVar4 * 0x80)) + 0x19a4c116;
  uVar12 = iVar2 + uVar12;
  uVar10 = ((uVar3 >> 2 | uVar3 * 0x40000000) ^ (uVar3 >> 0xd | uVar3 * 0x80000) ^
           (uVar3 >> 0x16 | uVar3 * 0x400)) + ((uVar3 | uVar5) & uVar6 | uVar3 & uVar5) + iVar2;
  iVar2 = local_128[0x21] + uVar8 + (uVar4 & uVar12 | uVar7 & (uVar12 ^ 0xffffffff)) +
          ((uVar12 >> 6 | uVar12 * 0x4000000) ^ (uVar12 >> 0xb | uVar12 * 0x200000) ^
          (uVar12 >> 0x19 | uVar12 * 0x80)) + 0x1e376c08;
  uVar6 = iVar2 + uVar6;
  uVar8 = ((uVar10 >> 2 | uVar10 * 0x40000000) ^ (uVar10 >> 0xd | uVar10 * 0x80000) ^
          (uVar10 >> 0x16 | uVar10 * 0x400)) + ((uVar10 | uVar3) & uVar5 | uVar10 & uVar3) + iVar2;
  iVar2 = local_128[0x22] + uVar7 + (uVar12 & uVar6 | uVar4 & (uVar6 ^ 0xffffffff)) +
          ((uVar6 >> 6 | uVar6 * 0x4000000) ^ (uVar6 >> 0xb | uVar6 * 0x200000) ^
          (uVar6 >> 0x19 | uVar6 * 0x80)) + 0x2748774c;
  uVar5 = iVar2 + uVar5;
  uVar7 = ((uVar8 >> 2 | uVar8 * 0x40000000) ^ (uVar8 >> 0xd | uVar8 * 0x80000) ^
          (uVar8 >> 0x16 | uVar8 * 0x400)) + ((uVar8 | uVar10) & uVar3 | uVar8 & uVar10) + iVar2;
  iVar2 = local_128[0x23] + uVar4 + (uVar6 & uVar5 | uVar12 & (uVar5 ^ 0xffffffff)) +
          ((uVar5 >> 6 | uVar5 * 0x4000000) ^ (uVar5 >> 0xb | uVar5 * 0x200000) ^
          (uVar5 >> 0x19 | uVar5 * 0x80)) + 0x34b0bcb5;
  uVar3 = iVar2 + uVar3;
  uVar4 = ((uVar7 >> 2 | uVar7 * 0x40000000) ^ (uVar7 >> 0xd | uVar7 * 0x80000) ^
          (uVar7 >> 0x16 | uVar7 * 0x400)) + ((uVar7 | uVar8) & uVar10 | uVar7 & uVar8) + iVar2;
  iVar2 = local_128[0x24] + uVar12 + (uVar5 & uVar3 | uVar6 & (uVar3 ^ 0xffffffff)) +
          ((uVar3 >> 6 | uVar3 * 0x4000000) ^ (uVar3 >> 0xb | uVar3 * 0x200000) ^
          (uVar3 >> 0x19 | uVar3 * 0x80)) + 0x391c0cb3;
  uVar10 = iVar2 + uVar10;
  uVar12 = ((uVar4 >> 2 | uVar4 * 0x40000000) ^ (uVar4 >> 0xd | uVar4 * 0x80000) ^
           (uVar4 >> 0x16 | uVar4 * 0x400)) + ((uVar4 | uVar7) & uVar8 | uVar4 & uVar7) + iVar2;
  iVar2 = local_128[0x25] + uVar6 + (uVar3 & uVar10 | uVar5 & (uVar10 ^ 0xffffffff)) +
          ((uVar10 >> 6 | uVar10 * 0x4000000) ^ (uVar10 >> 0xb | uVar10 * 0x200000) ^
          (uVar10 >> 0x19 | uVar10 * 0x80)) + 0x4ed8aa4a;
  uVar8 = iVar2 + uVar8;
  uVar6 = ((uVar12 >> 2 | uVar12 * 0x40000000) ^ (uVar12 >> 0xd | uVar12 * 0x80000) ^
          (uVar12 >> 0x16 | uVar12 * 0x400)) + ((uVar12 | uVar4) & uVar7 | uVar12 & uVar4) + iVar2;
  iVar2 = local_128[0x26] + uVar5 + (uVar10 & uVar8 | uVar3 & (uVar8 ^ 0xffffffff)) +
          ((uVar8 >> 6 | uVar8 * 0x4000000) ^ (uVar8 >> 0xb | uVar8 * 0x200000) ^
          (uVar8 >> 0x19 | uVar8 * 0x80)) + 0x5b9cca4f;
  uVar7 = iVar2 + uVar7;
  uVar5 = ((uVar6 >> 2 | uVar6 * 0x40000000) ^ (uVar6 >> 0xd | uVar6 * 0x80000) ^
          (uVar6 >> 0x16 | uVar6 * 0x400)) + ((uVar6 | uVar12) & uVar4 | uVar6 & uVar12) + iVar2;
  iVar2 = local_128[0x27] + uVar3 + (uVar8 & uVar7 | uVar10 & (uVar7 ^ 0xffffffff)) +
          ((uVar7 >> 6 | uVar7 * 0x4000000) ^ (uVar7 >> 0xb | uVar7 * 0x200000) ^
          (uVar7 >> 0x19 | uVar7 * 0x80)) + 0x682e6ff3;
  uVar4 = iVar2 + uVar4;
  uVar3 = ((uVar5 >> 2 | uVar5 * 0x40000000) ^ (uVar5 >> 0xd | uVar5 * 0x80000) ^
          (uVar5 >> 0x16 | uVar5 * 0x400)) + ((uVar5 | uVar6) & uVar12 | uVar5 & uVar6) + iVar2;
  iVar2 = local_128[0x28] + uVar10 + (uVar7 & uVar4 | uVar8 & (uVar4 ^ 0xffffffff)) +
          ((uVar4 >> 6 | uVar4 * 0x4000000) ^ (uVar4 >> 0xb | uVar4 * 0x200000) ^
          (uVar4 >> 0x19 | uVar4 * 0x80)) + 0x748f82ee;
  uVar12 = iVar2 + uVar12;
  uVar10 = ((uVar3 >> 2 | uVar3 * 0x40000000) ^ (uVar3 >> 0xd | uVar3 * 0x80000) ^
           (uVar3 >> 0x16 | uVar3 * 0x400)) + ((uVar3 | uVar5) & uVar6 | uVar3 & uVar5) + iVar2;
  iVar2 = local_128[0x29] + uVar8 + (uVar4 & uVar12 | uVar7 & (uVar12 ^ 0xffffffff)) +
          ((uVar12 >> 6 | uVar12 * 0x4000000) ^ (uVar12 >> 0xb | uVar12 * 0x200000) ^
          (uVar12 >> 0x19 | uVar12 * 0x80)) + 0x78a5636f;
  uVar6 = iVar2 + uVar6;
  uVar8 = ((uVar10 >> 2 | uVar10 * 0x40000000) ^ (uVar10 >> 0xd | uVar10 * 0x80000) ^
          (uVar10 >> 0x16 | uVar10 * 0x400)) + ((uVar10 | uVar3) & uVar5 | uVar10 & uVar3) + iVar2;
  iVar2 = local_128[0x2a] + uVar7 + (uVar12 & uVar6 | uVar4 & (uVar6 ^ 0xffffffff)) +
          ((uVar6 >> 6 | uVar6 * 0x4000000) ^ (uVar6 >> 0xb | uVar6 * 0x200000) ^
          (uVar6 >> 0x19 | uVar6 * 0x80)) + -0x7b3787ec;
  uVar5 = iVar2 + uVar5;
  uVar11 = ((uVar8 >> 2 | uVar8 * 0x40000000) ^ (uVar8 >> 0xd | uVar8 * 0x80000) ^
           (uVar8 >> 0x16 | uVar8 * 0x400)) + ((uVar8 | uVar10) & uVar3 | uVar8 & uVar10) + iVar2;
  iVar2 = local_128[0x2b] + uVar4 + (uVar6 & uVar5 | uVar12 & (uVar5 ^ 0xffffffff)) +
          ((uVar5 >> 6 | uVar5 * 0x4000000) ^ (uVar5 >> 0xb | uVar5 * 0x200000) ^
          (uVar5 >> 0x19 | uVar5 * 0x80)) + -0x7338fdf8;
  uVar3 = iVar2 + uVar3;
  uVar4 = ((uVar11 >> 2 | uVar11 * 0x40000000) ^ (uVar11 >> 0xd | uVar11 * 0x80000) ^
          (uVar11 >> 0x16 | uVar11 * 0x400)) + ((uVar11 | uVar8) & uVar10 | uVar11 & uVar8) + iVar2;
  iVar2 = local_128[0x2c] + uVar12 + (uVar5 & uVar3 | uVar6 & (uVar3 ^ 0xffffffff)) +
          ((uVar3 >> 6 | uVar3 * 0x4000000) ^ (uVar3 >> 0xb | uVar3 * 0x200000) ^
          (uVar3 >> 0x19 | uVar3 * 0x80)) + -0x6f410006;
  uVar10 = iVar2 + uVar10;
  uVar7 = ((uVar4 >> 2 | uVar4 * 0x40000000) ^ (uVar4 >> 0xd | uVar4 * 0x80000) ^
          (uVar4 >> 0x16 | uVar4 * 0x400)) + ((uVar4 | uVar11) & uVar8 | uVar4 & uVar11) + iVar2;
  iVar2 = local_128[0x2d] + uVar6 + (uVar3 & uVar10 | uVar5 & (uVar10 ^ 0xffffffff)) +
          ((uVar10 >> 6 | uVar10 * 0x4000000) ^ (uVar10 >> 0xb | uVar10 * 0x200000) ^
          (uVar10 >> 0x19 | uVar10 * 0x80)) + -0x5baf9315;
  uVar8 = iVar2 + uVar8;
  uVar6 = ((uVar7 >> 2 | uVar7 * 0x40000000) ^ (uVar7 >> 0xd | uVar7 * 0x80000) ^
          (uVar7 >> 0x16 | uVar7 * 0x400)) + ((uVar7 | uVar4) & uVar11 | uVar7 & uVar4) + iVar2;
  iVar2 = local_128[0x2e] + uVar5 + (uVar10 & uVar8 | uVar3 & (uVar8 ^ 0xffffffff)) +
          ((uVar8 >> 6 | uVar8 * 0x4000000) ^ (uVar8 >> 0xb | uVar8 * 0x200000) ^
          (uVar8 >> 0x19 | uVar8 * 0x80)) + -0x41065c09;
  uVar11 = iVar2 + uVar11;
  uVar5 = ((uVar6 >> 2 | uVar6 * 0x40000000) ^ (uVar6 >> 0xd | uVar6 * 0x80000) ^
          (uVar6 >> 0x16 | uVar6 * 0x400)) + ((uVar6 | uVar7) & uVar4 | uVar6 & uVar7) + iVar2;
  iVar2 = local_128[0x2f] + uVar3 + (uVar8 & uVar11 | uVar10 & (uVar11 ^ 0xffffffff)) +
          ((uVar11 >> 6 | uVar11 * 0x4000000) ^ (uVar11 >> 0xb | uVar11 * 0x200000) ^
          (uVar11 >> 0x19 | uVar11 * 0x80)) + -0x398e870e;
  *(uint *)(param_1 + 8) =
       ((uVar5 | uVar6) & uVar7 | uVar5 & uVar6) + *(int *)(param_1 + 8) +
       ((uVar5 >> 2 | uVar5 * 0x40000000) ^ (uVar5 >> 0xd | uVar5 * 0x80000) ^
       (uVar5 >> 0x16 | uVar5 * 0x400)) + iVar2;
  *(uint *)(param_1 + 0xc) = uVar5 + *(int *)(param_1 + 0xc);
  *(uint *)(param_1 + 0x10) = uVar6 + *(int *)(param_1 + 0x10);
  *(uint *)(param_1 + 0x14) = uVar7 + *(int *)(param_1 + 0x14);
  *(uint *)(param_1 + 0x18) = uVar4 + *(int *)(param_1 + 0x18) + iVar2;
  *(uint *)(param_1 + 0x1c) = uVar11 + *(int *)(param_1 + 0x1c);
  *(uint *)(param_1 + 0x20) = uVar8 + *(int *)(param_1 + 0x20);
  *(uint *)(param_1 + 0x24) = uVar10 + *(int *)(param_1 + 0x24);
  FUN_ffff0000b805b640(0x128);
  return;
}



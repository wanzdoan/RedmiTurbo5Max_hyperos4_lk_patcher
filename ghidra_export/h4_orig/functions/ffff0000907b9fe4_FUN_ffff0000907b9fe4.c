// ffff0000907b9fe4  size=1476  FUN_ffff0000907b9fe4

undefined4
FUN_ffff0000907b9fe4
          (long param_1,uint param_2,long param_3,uint param_4,long param_5,ulong param_6,
          long param_7,ulong param_8)

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
  int iVar13;
  undefined4 uVar14;
  long lVar15;
  long lVar16;
  uint *puVar17;
  uint *puVar18;
  undefined8 uVar19;
  uint uVar20;
  uint uVar21;
  long local_158;
  undefined8 uStack_150;
  long local_148;
  ulong uStack_140;
  undefined8 local_138;
  undefined8 uStack_130;
  long local_128;
  ulong local_120;
  long local_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 local_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 local_c8;
  undefined8 local_c0;
  undefined8 local_b8;
  undefined8 local_b0;
  undefined8 local_a8;
  undefined8 local_a0;
  undefined8 local_98;
  uint local_90;
  long local_88 [5];
  
  if (param_4 < 3) {
    uVar19 = *(undefined8 *)(&DAT_ffff0000908136a8 + (long)(int)param_4 * 8);
    local_e0 = 0;
    uStack_d8 = 0;
    local_f0 = 0;
    uStack_e8 = 0;
    local_100 = 0;
    local_f8 = 0;
    local_110 = 0;
    uStack_108 = 0;
    local_120 = 0;
    local_118 = 0;
    local_158 = 0;
    uStack_150 = 0;
    local_148 = 0;
    uStack_140 = 0;
    local_138 = 0;
    uStack_130 = 0;
    local_128 = 0;
    FUN_ffff00009077ee50(&local_118,0,0x48);
    local_e0 = local_e0 & 0xffffffff00000000;
    local_110 = local_110 & 0xffffffff;
    local_100 = local_100 & 0xffffffff;
    local_118 = param_1;
    uStack_108._0_4_ = param_4;
    uStack_108._4_4_ = param_2;
    FUN_ffff00009077ee50(&local_158,0,0x40);
    uVar4 = uStack_108._4_4_;
    uVar3 = (uint)uStack_108;
    lVar16 = local_118;
    uVar2 = local_110._4_4_;
    uVar5 = (uint)local_100;
    uVar7 = local_f8._4_4_;
    uVar8 = (uint)local_f0;
    uVar10 = uStack_e8._4_4_;
    uVar11 = (uint)local_e0;
    uVar12 = local_e0._4_4_;
    iVar13 = (int)uStack_d8;
    uVar6 = local_100._4_4_;
    uVar21 = (uint)(byte)local_f8;
    uVar9 = local_f0._4_4_;
    if (local_100._4_4_ == 0) {
      lVar15 = 0x10;
    }
    else {
      if (local_100._4_4_ != 1) goto LAB_ffff0000907ba218;
      lVar15 = 0x20;
    }
    uVar20 = (uint)lVar15;
    if (local_110._4_4_ - 1 < 2) {
      if (0x20 < uVar20 * (byte)local_f8) goto LAB_ffff0000907ba218;
joined_r0xffff0000907ba1ec:
      local_158 = param_3;
      uStack_150 = uVar19;
      local_148 = param_5;
      uStack_140 = param_6;
      local_128 = param_7;
      local_120 = param_8;
      if ((uint)local_e0 == 0) {
        local_90 = 0;
        local_a0 = 0;
        local_98 = 0;
        local_b0 = 0;
        local_a8 = 0;
        local_c0 = 0;
        local_b8 = 0;
        local_d0 = 0;
        local_c8 = 0;
        if (param_5 == 0) {
          return 0x7247;
        }
        uVar20 = (uint)param_6;
        if (0x43 < uVar20) goto LAB_ffff0000907ba210;
        FUN_ffff00009077eb20((long)&local_d0 + (ulong)(0x44 - uVar20),param_5,param_6 & 0xffffffff);
        uVar20 = (uint)local_d0 | uVar20;
        uVar1 = (local_90 & 0xff00ff00) >> 8 | (local_90 & 0xff00ff) << 8;
        local_d0 = CONCAT44(local_d0._4_4_,uVar20);
        *(uint *)(lVar16 + 0x40) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = (local_98._4_4_ & 0xff00ff00) >> 8 | (local_98._4_4_ & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x44) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = ((uint)local_98 & 0xff00ff00) >> 8 | ((uint)local_98 & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x48) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = (local_a0._4_4_ & 0xff00ff00) >> 8 | (local_a0._4_4_ & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x4c) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = ((uint)local_a0 & 0xff00ff00) >> 8 | ((uint)local_a0 & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x50) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = (local_a8._4_4_ & 0xff00ff00) >> 8 | (local_a8._4_4_ & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x54) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = ((uint)local_a8 & 0xff00ff00) >> 8 | ((uint)local_a8 & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x58) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = (local_b0._4_4_ & 0xff00ff00) >> 8 | (local_b0._4_4_ & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x5c) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = ((uint)local_b0 & 0xff00ff00) >> 8 | ((uint)local_b0 & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x60) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = (local_b8._4_4_ & 0xff00ff00) >> 8 | (local_b8._4_4_ & 0xff00ff) << 8;
        *(uint *)(lVar16 + 100) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = ((uint)local_b8 & 0xff00ff00) >> 8 | ((uint)local_b8 & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x68) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = (local_c0._4_4_ & 0xff00ff00) >> 8 | (local_c0._4_4_ & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x6c) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = ((uint)local_c0 & 0xff00ff00) >> 8 | ((uint)local_c0 & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x70) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = (local_c8._4_4_ & 0xff00ff00) >> 8 | (local_c8._4_4_ & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x74) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = ((uint)local_c8 & 0xff00ff00) >> 8 | ((uint)local_c8 & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x78) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = (local_d0._4_4_ & 0xff00ff00) >> 8 | (local_d0._4_4_ & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x7c) = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar20 = (uVar20 & 0xff00ff00) >> 8 | (uVar20 & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0x80) = uVar20 >> 0x10 | uVar20 << 0x10;
      }
      if (uVar4 == 0) {
        local_c0 = 0;
        local_b8 = 0;
        local_d0 = 0;
        local_c8 = 0;
        if (param_3 == 0) {
          return 0x7247;
        }
        FUN_ffff00009077eb20
                  ((long)&local_d0 + ((ulong)(0x20 - (int)uVar19) & 0xfffffff8),param_3,uVar19);
        uVar20 = (local_b8._4_4_ & 0xff00ff00) >> 8 | (local_b8._4_4_ & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0xa0) = uVar20 >> 0x10 | uVar20 << 0x10;
        uVar20 = ((uint)local_b8 & 0xff00ff00) >> 8 | ((uint)local_b8 & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0xa4) = uVar20 >> 0x10 | uVar20 << 0x10;
        uVar20 = (local_c0._4_4_ & 0xff00ff00) >> 8 | (local_c0._4_4_ & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0xa8) = uVar20 >> 0x10 | uVar20 << 0x10;
        uVar20 = ((uint)local_c0 & 0xff00ff00) >> 8 | ((uint)local_c0 & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0xac) = uVar20 >> 0x10 | uVar20 << 0x10;
        uVar20 = (local_c8._4_4_ & 0xff00ff00) >> 8 | (local_c8._4_4_ & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0xb0) = uVar20 >> 0x10 | uVar20 << 0x10;
        uVar20 = ((uint)local_c8 & 0xff00ff00) >> 8 | ((uint)local_c8 & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0xb4) = uVar20 >> 0x10 | uVar20 << 0x10;
        uVar20 = (local_d0._4_4_ & 0xff00ff00) >> 8 | (local_d0._4_4_ & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0xb8) = uVar20 >> 0x10 | uVar20 << 0x10;
        uVar20 = ((uint)local_d0 & 0xff00ff00) >> 8 | ((uint)local_d0 & 0xff00ff) << 8;
        *(uint *)(lVar16 + 0xbc) = uVar20 >> 0x10 | uVar20 << 0x10;
      }
      local_d0 = CONCAT44((int)((ulong)local_d0 >> 0x20),0xff);
      *(uint *)(lVar16 + 0x84) =
           (uVar7 & 1) << 0x14 | (uVar8 & 1) << 0x16 | (uVar9 & 1) << 0x17 | (uVar10 & 1) << 0x1a |
           (uVar11 & 1) << 0x1b | (uVar12 & 1) << 0x1c | iVar13 << 0x1e |
           (uVar2 & 3) << 4 | (uVar3 & 3) << 6 | (uVar4 & 0xf) << 8 | (uVar5 & 7) << 0xc |
           (uVar6 & 1) << 0xf | (uVar21 & 0xf) << 0x10 | 1;
      iVar13 = FUN_ffff0000907bbd64((uint *)(lVar16 + 0x84),1,1,5000);
      if (iVar13 != 0) {
        return 0x7262;
      }
      uVar21 = *(uint *)(lVar16 + 0x8c);
      if ((uVar21 & 0xfff) != 0) {
        if ((uVar21 & 1) != 0) {
          lVar16 = 0;
code_r0xffff0000907ba598:
          return (&DAT_ffff000090813634)[lVar16 * 2];
        }
        if ((uVar21 >> 1 & 1) != 0) {
          lVar16 = 1;
          goto code_r0xffff0000907ba598;
        }
        if ((uVar21 >> 2 & 1) != 0) {
          lVar16 = 2;
          goto code_r0xffff0000907ba598;
        }
        if ((uVar21 >> 3 & 1) != 0) {
          lVar16 = 3;
          goto code_r0xffff0000907ba598;
        }
        if ((uVar21 >> 4 & 1) != 0) {
          lVar16 = 4;
          goto code_r0xffff0000907ba598;
        }
        if ((uVar21 >> 5 & 1) != 0) {
          lVar16 = 5;
          goto code_r0xffff0000907ba598;
        }
        if ((uVar21 >> 6 & 1) != 0) {
          lVar16 = 6;
          goto code_r0xffff0000907ba598;
        }
        if ((uVar21 >> 7 & 1) != 0) {
          lVar16 = 7;
          goto code_r0xffff0000907ba598;
        }
        if ((uVar21 >> 8 & 1) != 0) {
          lVar16 = 8;
          goto code_r0xffff0000907ba598;
        }
        if ((uVar21 >> 9 & 1) != 0) {
          lVar16 = 9;
          goto code_r0xffff0000907ba598;
        }
      }
      uVar14 = 0x724d;
      if ((uVar21 & 0x9f000000) == 0x80000000 || uVar2 == 0) {
        uVar14 = 0;
      }
      if (uVar2 != 0) {
        return uVar14;
      }
      local_a0 = 0;
      local_98 = 0;
      local_88[0] = lVar16 + 0xc;
      local_88[1] = lVar16 + 0x1c;
      local_88[2] = lVar16 + 0x2c;
      local_88[3] = lVar16 + 0x3c;
      local_b0 = 0;
      local_a8 = 0;
      local_c0 = 0;
      local_b8 = 0;
      local_d0 = 0;
      local_c8 = 0;
      if (param_7 == 0) {
        return 0x7247;
      }
      if ((uint)param_8 < 0x41) {
        lVar16 = 0;
        puVar17 = (uint *)&local_c8;
        do {
          puVar18 = *(uint **)((long)local_88 + lVar16);
          lVar16 = lVar16 + 8;
          puVar17[-2] = (*puVar18 & 0xff00) << 8 | *puVar18 << 0x18 | *puVar18 >> 8 & 0xff00 |
                        *puVar18 >> 0x18;
          puVar17[-1] = (puVar18[-1] & 0xff00) << 8 | puVar18[-1] << 0x18 |
                        puVar18[-1] >> 8 & 0xff00 | puVar18[-1] >> 0x18;
          *puVar17 = (puVar18[-2] & 0xff00) << 8 | puVar18[-2] << 0x18 | puVar18[-2] >> 8 & 0xff00 |
                     puVar18[-2] >> 0x18;
          puVar17[1] = (puVar18[-3] & 0xff00) << 8 | puVar18[-3] << 0x18 | puVar18[-3] >> 8 & 0xff00
                       | puVar18[-3] >> 0x18;
          puVar17 = puVar17 + 4;
        } while (lVar16 != 0x20);
        FUN_ffff00009077eb20(param_7,&local_d0,param_8 & 0xffffffff);
        return 0;
      }
    }
    else {
      if (local_110._4_4_ != 0) goto LAB_ffff0000907ba218;
      if (param_7 == 0) {
        return 0x7247;
      }
      if (param_8 < 0x41) {
        uVar21 = 0;
        if (uVar20 != 0) {
          uVar21 = ((uint)param_8 & 0xff) / uVar20;
        }
        if ((lVar15 - 1U & param_8) != 0) {
          uVar21 = uVar21 + 1;
        }
        goto joined_r0xffff0000907ba1ec;
      }
    }
LAB_ffff0000907ba210:
    uVar14 = 0x7246;
  }
  else {
LAB_ffff0000907ba218:
    uVar14 = 0x724c;
  }
  return uVar14;
}


